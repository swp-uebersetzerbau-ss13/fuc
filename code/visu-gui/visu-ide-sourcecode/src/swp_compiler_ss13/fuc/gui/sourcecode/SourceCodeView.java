package swp_compiler_ss13.fuc.gui.sourcecode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.errorLog.LogEntry;
import swp_compiler_ss13.fuc.errorLog.LogEntry.Type;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;
import swp_compiler_ss13.fuc.gui.ide.mvc.IDE;
import swp_compiler_ss13.fuc.gui.ide.mvc.Position;
import swp_compiler_ss13.fuc.gui.ide.mvc.View;

/**
 * @author "Eduard Wolf"
 * 
 */
public class SourceCodeView implements View {

	private static final Logger LOG = Logger.getLogger(SourceCodeView.class);

	private final SourceCodeController controller;

	private final JTextPane sourceCodeField;
	private final JTextPane lineNumberField;
	private final JTextPane markErrorField;
	private final MyList errorLines;
	private final JScrollPane component;
	private final SimpleAttributeSet keywordAttributes = new SimpleAttributeSet();
	private final SimpleAttributeSet stringAttributes = new SimpleAttributeSet();
	private final SimpleAttributeSet commentAttributes = new SimpleAttributeSet();
	private final SimpleAttributeSet defaultAttributes = new SimpleAttributeSet();
	private final SimpleAttributeSet errorAttributes = new SimpleAttributeSet();
	private final SimpleAttributeSet warningAttributes = new SimpleAttributeSet();
	private final SimpleAttributeSet markErrorAttributes = new SimpleAttributeSet();
	private final SimpleAttributeSet markWarningAttributes = new SimpleAttributeSet();
	private static final Color WARNING_YELLOW = new Color(240, 240, 0);

	private final List<TokenType> highlightedKeywords = Arrays.asList(TokenType.BOOL_SYMBOL,
			TokenType.BREAK, TokenType.DO, TokenType.DOUBLE_SYMBOL, TokenType.ELSE,
			TokenType.FALSE, TokenType.IF, TokenType.LONG_SYMBOL, TokenType.PRINT,
			TokenType.RECORD_SYMBOL, TokenType.RETURN, TokenType.STRING_SYMBOL, TokenType.TRUE,
			TokenType.WHILE);
	private final TokenType highlightedString = TokenType.STRING;
	private final TokenType highlightedComment = TokenType.COMMENT;
	private final TokenType highlightedError = TokenType.NOT_A_TOKEN;
	private final Map<TokenType, SimpleAttributeSet> attributes;

	private boolean setSourceCode = false;
	private final Lock sourceCodeLock = new ReentrantLock();

	public SourceCodeView(SourceCodeController controller) {
		this.controller = controller;
		sourceCodeField = new JTextPane();
		sourceCodeField.setEditable(true);
		sourceCodeField.setEditorKit(new UnderlineStyledEditorKit());
		lineNumberField = new JTextPane();
		lineNumberField.setText("1");
		lineNumberField.setEditable(false);
		lineNumberField.setBackground(new Color(224, 224, 224));
		markErrorField = new JTextPane();
		markErrorField.setText(" ");
		markErrorField.setEditable(false);
		markErrorField.setBackground(new Color(224, 224, 224));
		markErrorField.setForeground(Color.RED);
		errorLines = new MyList();
		JPanel viewPort = new JPanel(new BorderLayout(2, 1));
		viewPort.add(lineNumberField, BorderLayout.WEST);
		viewPort.add(sourceCodeField, BorderLayout.CENTER);
		viewPort.add(markErrorField, BorderLayout.EAST);
		component = new JScrollPane(viewPort);
		StyleConstants.setBold(keywordAttributes, true);
		StyleConstants.setForeground(keywordAttributes, new Color(127, 0, 85));
		StyleConstants.setForeground(stringAttributes, new Color(42, 0, 255));
		StyleConstants.setItalic(commentAttributes, true);
		StyleConstants.setForeground(commentAttributes, Color.GRAY);
		StyleConstants.setForeground(markErrorAttributes, Color.RED);
		StyleConstants.setForeground(markWarningAttributes, WARNING_YELLOW);
		errorAttributes.addAttribute(UnderlineStyledEditorKit.WAVY_LINE, true);
		errorAttributes.addAttribute(UnderlineStyledEditorKit.UNDERLINE_COLOR, Color.RED);
		warningAttributes.addAttribute(UnderlineStyledEditorKit.WAVY_LINE, true);
		warningAttributes.addAttribute(UnderlineStyledEditorKit.UNDERLINE_COLOR, WARNING_YELLOW);
		attributes = new HashMap<>();
		for (TokenType type : highlightedKeywords) {
			attributes.put(type, keywordAttributes);
		}
		attributes.put(highlightedString, stringAttributes);
		attributes.put(highlightedComment, commentAttributes);
		attributes.put(highlightedError, errorAttributes);
	}

	@Override
	public JComponent getComponent() {
		return component;
	}

	@Override
	public String getName() {
		return "input source code";
	}

	@Override
	public Position getPosition() {
		return Position.SOURCE_CODE;
	}

	@Override
	public Controller getController() {
		return controller;
	}

	void setSourceCode(final String code) {
		if (setSourceCode) {
			sourceCodeField.setText(code);
		}
	}

	@Override
	public void initComponents(IDE ide) {
		sourceCodeField.getDocument().addDocumentListener(new CopyListener(ide));
		FileListener listener = new FileListener(ide);
		JMenu menu = new JMenu("File");
		JMenuItem loadFile = new JMenuItem("Open File...");
		loadFile.setActionCommand(FileListener.LOAD_FILE);
		loadFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.META_DOWN_MASK));
		loadFile.addActionListener(listener);
		menu.add(loadFile);
		JMenuItem saveNewFile = new JMenuItem("Save File...");
		saveNewFile.setActionCommand(FileListener.SAFE_NEW_FILE);
		saveNewFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.META_DOWN_MASK
				| InputEvent.SHIFT_DOWN_MASK));
		saveNewFile.addActionListener(listener);
		menu.add(saveNewFile);
		JMenuItem saveOldFile = new JMenuItem("Save File");
		saveOldFile.setActionCommand(FileListener.SAFE_OLD_FILE);
		saveOldFile
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.META_DOWN_MASK));
		saveOldFile.addActionListener(listener);
		menu.add(saveOldFile);
		ide.addMenu(menu, Position.SOURCE_CODE, true);
	}

	protected void setLineNumbers(int amount) {
		StringBuilder builder = new StringBuilder("1");
		for (int i = 2; i <= amount; i++) {
			builder.append('\n');
			builder.append(i);
		}
		lineNumberField.setText(builder.toString());
	}

	protected void paintErrors() {
		int actualLine = 1;
		int line;
		int textIndex = 0;
		Document document = markErrorField.getDocument();
		try {
			document.remove(0, document.getLength());
			for (int transferredLine : errorLines) {
				line = Math.abs(transferredLine);
				for (; actualLine < line; actualLine++) {
					document.insertString(textIndex++, "\n", defaultAttributes);
				}
				document.insertString(textIndex++, "I",
						line == transferredLine ? markErrorAttributes : markWarningAttributes);
			}
			if (textIndex == 0) {
				document.insertString(0, " ", defaultAttributes);
			}
		} catch (BadLocationException e) {
			LOG.error("error on marking errors", e);
		}
	}

	private class CopyListener implements ActionListener, DocumentListener {

		private final IDE ide;
		private int modCount = 0;
		private final Lock lock = new ReentrantLock();

		public CopyListener(IDE ide) {
			this.ide = ide;
		}

		private void statusChanged() {
			LOG.trace("wrote source code");
			final int expectedModCount;
			final String text;
			lock.lock();
			try {
				expectedModCount = ++modCount;
				text = sourceCodeField.getText();
			} finally {
				lock.unlock();
			}
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					int caretPosition = sourceCodeField.getCaretPosition();
					Document document = sourceCodeField.getDocument();
					document.removeDocumentListener(CopyListener.this);
					try {
						{
							int newLineAmount = 1;
							char newLine = '\n';
							for (char c : text.toCharArray()) {
								if (newLine == c) {
									newLineAmount++;
								}
							}
							setLineNumbers(newLineAmount);
						}
						String[] lines = text.split("\n");
						int[] lineOffset = new int[lines.length];
						lineOffset[0] = 0;
						for (int i = 0; i < lines.length - 1; i++) {
							lineOffset[i + 1] = lineOffset[i] + 1 + lines[i].length();
						}
						List<Token> tokens = lexerCheck(document, text, lineOffset,
								expectedModCount);
						if (tokens != null) {
							AST ast = parserCheck(document, tokens, lineOffset, expectedModCount);
							if (ast != null) {
								ast = semanticCheck(document, ast, lineOffset, expectedModCount);
								sourceCodeField.setCaretPosition(caretPosition);
							} else {
								sourceCodeField.setCaretPosition(caretPosition);
							}
						}

					} catch (Exception e) {
						LOG.warn("error durring highlighting", e);
					} finally {
						document.addDocumentListener(CopyListener.this);
					}
					sourceCodeLock.lock();
					try {
						boolean oldValue = setSourceCode;
						setSourceCode = false;
						LOG.info("send sourcecode");
						ide.setSourceCode(text);
						setSourceCode = oldValue;
					} finally {
						sourceCodeLock.unlock();
					}
				}

				private List<Token> lexerCheck(Document document, String text, int[] lineLength,
						int expectedModCount) throws BadLocationException {
					SimpleAttributeSet attrs;
					int tokenstart;
					errorLines.clear();
					List<Token> tokens = ide.runLexer(text, true);
					lock.lock();
					try {
						if (expectedModCount == modCount) {
							document.remove(0, document.getLength());
							document.insertString(0, text, defaultAttributes);
							for (Token token : tokens) {
								attrs = attributes.get(token.getTokenType());
								if (attrs != null) {
									if (attrs == errorAttributes) {
										errorLines.add(token.getLine());
									}
									tokenstart = lineLength[token.getLine() - 1]
											+ token.getColumn() - 1;
									document.remove(tokenstart, token.getValue().length());
									document.insertString(tokenstart, token.getValue(), attrs);
								}
							}
							paintErrors();
						} else {
							return null;
						}
					} finally {
						lock.unlock();
					}
					return tokens;
				}

				private AST parserCheck(Document document, List<Token> tokens, int[] lineLength,
						int expectedModCount) throws BadLocationException {
					AST ast = ide.runParser(tokens, false);
					return printAstErrors(document, lineLength, expectedModCount, ast);
				}

				private AST semanticCheck(Document document, AST ast, int[] lineLength,
						int expectedModCount) throws BadLocationException {
					ast = ide.runSemanticAnalysis(ast, false);
					return printAstErrors(document, lineLength, expectedModCount, ast);
				}

				private AST printAstErrors(Document document, int[] lineLength,
						int expectedModCount, AST ast) throws BadLocationException {
					ReportLogImpl reportLog = ide.getReportLog();
					lock.lock();
					try {
						if (expectedModCount == modCount && reportLog != null) {
							// errors from Lexer can be removed, because they
							// are repeated in the parser
							errorLines.clear();
							int tokenstart;
							boolean isError;
							List<Token> tokens;
							for (LogEntry entry : reportLog.getEntries()) {
								isError = entry.getLogType() == Type.ERROR;
								tokens = entry.getTokens();
								errorLines.add(tokens.get(0).getLine() * (isError ? 1 : -1));
								for (Token token : tokens) {
									if (!token.getTokenType().equals(TokenType.EOF)) {
										tokenstart = lineLength[token.getLine() - 1]
												+ token.getColumn() - 1;
										document.remove(tokenstart, token.getValue().length());
										document.insertString(tokenstart, token.getValue(),
												isError ? errorAttributes : warningAttributes);
									}
								}
								paintErrors();
							}
						} else {
							return null;
						}
					} finally {
						lock.unlock();
					}
					return ast;
				}

			});
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			statusChanged();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			statusChanged();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			statusChanged();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			statusChanged();
		}

	}

	private class FileListener implements ActionListener {
		private static final String SAFE_NEW_FILE = "safe new";
		private static final String SAFE_OLD_FILE = "safe old";
		private static final String LOAD_FILE = "load";

		private final JFileChooser chooser;
		private final IDE ide;

		private File file = null;

		public FileListener(IDE ide) {
			this.ide = ide;
			this.chooser = new JFileChooser();
			this.chooser.setFileFilter(new FileNameExtensionFilter("choose a filename", "prog"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			final String text = sourceCodeField.getText();
			final boolean safeNew = SAFE_NEW_FILE.equals(e.getActionCommand());
			final boolean load = LOAD_FILE.equals(e.getActionCommand());
			if (safeNew || load || SAFE_OLD_FILE.equals(e.getActionCommand())) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						File newFile = file;
						if (load || safeNew || newFile == null) {
							int showOpenDialog;
							if (load) {
								showOpenDialog = chooser.showOpenDialog(component);
							} else {
								showOpenDialog = chooser.showSaveDialog(component);
							}
							switch (showOpenDialog) {
							case JFileChooser.APPROVE_OPTION:
								newFile = new File(chooser.getSelectedFile().getPath()
										.replaceFirst("(|\\.prog)\\Z", ".prog"));
								if (!newFile.exists()) {
									try {
										newFile.createNewFile();
									} catch (IOException e) {
										LOG.error("couldn't create file", e);
										return;
									}
								} else if (load) {
									BufferedReader reader = null;
									StringBuilder sourceCode = new StringBuilder();
									String line;
									try {
										reader = new BufferedReader(new InputStreamReader(
												new FileInputStream(newFile)));
										while ((line = reader.readLine()) != null) {
											sourceCode.append(line);
											sourceCode.append('\n');
										}
										if (sourceCode.length() != 0) {
											sourceCode.setLength(sourceCode.length() - 1);
										}
										sourceCodeLock.lock();
										try {
											boolean oldValue = setSourceCode;
											setSourceCode = true;
											ide.setSourceCode(sourceCode.toString());
											setSourceCode = oldValue;
										} finally {
											sourceCodeLock.unlock();
										}
										file = newFile;
									} catch (FileNotFoundException e1) {
										LOG.error("choosen file doesn't exist", e1);
									} catch (IOException e1) {
										LOG.error("error while reading from file", e1);
									} finally {
										if (reader != null) {
											try {
												reader.close();
											} catch (IOException e1) {
												LOG.error("error while closing file", e1);
											}
										}
									}
									return;
								}
								file = newFile;
								break;
							case JFileChooser.CANCEL_OPTION:
							case JFileChooser.ERROR_OPTION:
								return;
							default:
								LOG.warn("unknown result while choosing the load file");
								return;
							}
						}
						PrintWriter writer = null;
						try {
							writer = new PrintWriter(newFile);
							writer.write(text);
						} catch (FileNotFoundException e1) {
							LOG.error("choosen file doesn't exist", e1);
						} finally {
							if (writer != null) {
								writer.close();
							}
						}
						LOG.info("saved file");
					}
				});
			}
		}
	}

	private static class MyList implements Iterable<Integer> {
		private List<Integer> list = new ArrayList<>();

		public void clear() {
			list.clear();
		}

		public void add(int i) {
			int index = list.size();
			int element;
			for (ListIterator<Integer> iterator = list.listIterator(list.size()); iterator
					.hasPrevious();) {
				element = iterator.previous();
				if (element == i || element == -i) {
					index = iterator.nextIndex();
					break;
				}
			}
			list.add(index, i);
		}

		@Override
		public Iterator<Integer> iterator() {
			return list.iterator();
		}
	}

}
