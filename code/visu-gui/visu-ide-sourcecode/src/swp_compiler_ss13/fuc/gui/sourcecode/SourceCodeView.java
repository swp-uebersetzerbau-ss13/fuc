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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
	private final List<Integer> errorLines;
	private final JScrollPane component;
	private final SimpleAttributeSet keywordAttributes = new SimpleAttributeSet();
	private final SimpleAttributeSet stringAttributes = new SimpleAttributeSet();
	private final SimpleAttributeSet commentAttributes = new SimpleAttributeSet();
	private final SimpleAttributeSet defaultAttributes = new SimpleAttributeSet();
	private final SimpleAttributeSet errorAttributes = new SimpleAttributeSet();
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
		errorLines = new LinkedList<>();
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
		errorAttributes.addAttribute(UnderlineStyledEditorKit.WAVY_LINE, true);
		errorAttributes.addAttribute(UnderlineStyledEditorKit.UNDERLINE_COLOR, Color.RED);
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
		SaveFileListener saveListener = new SaveFileListener();
		JMenu menu = new JMenu("File");
		JMenuItem loadFile = new JMenuItem("Open File...");
		loadFile.addActionListener(new LoadFileListener(ide, saveListener));
		menu.add(loadFile);
		JMenuItem saveNewFile = new JMenuItem("Save File...");
		saveNewFile.setActionCommand(SaveFileListener.SAFE_NEW_FILE);
		saveNewFile.addActionListener(saveListener);
		menu.add(saveNewFile);
		JMenuItem saveOldFile = new JMenuItem("Save File");
		saveOldFile.setActionCommand(SaveFileListener.SAFE_OLD_FILE);
		saveOldFile
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.META_DOWN_MASK));
		saveOldFile.addActionListener(saveListener);
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
		int actualLine = 0;
		StringBuilder builder = new StringBuilder("");
		for (int line : errorLines) {
			for (; actualLine < line; actualLine++) {
				builder.append('\n');
			}
			builder.append('I');
		}
		if (builder.length() == 0) {
			builder.append(' ');
		}
		markErrorField.setText(builder.toString());
	}

	private class CopyListener implements ActionListener, DocumentListener {

		private final IDE ide;

		public CopyListener(IDE ide) {
			this.ide = ide;
		}

		private void statusChanged() {
			LOG.trace("wrote source code");
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					String text = sourceCodeField.getText();
					int caretPosition = sourceCodeField.getCaretPosition();
					Document document = sourceCodeField.getDocument();
					document.removeDocumentListener(CopyListener.this);
					try {
						document.remove(0, document.getLength());
						document.insertString(0, text, defaultAttributes);
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
						int[] lineLength = new int[lines.length];
						lineLength[0] = 0;
						for (int i = 0; i < lines.length - 1; i++) {
							lineLength[i + 1] = lineLength[i] + 1 + lines[i].length();
						}
						// List<Token> tokens =
						lexerCheck(document, text, lineLength);
						// parserCheck(document, tokens, lineLength);
						sourceCodeField.setCaretPosition(caretPosition);
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

				private List<Token> lexerCheck(Document document, String text, int[] lineLength)
						throws BadLocationException {
					SimpleAttributeSet attrs;
					int tokenstart;
					errorLines.clear();
					List<Token> tokens = ide.runLexer(text, true);
					for (Token token : tokens) {
						attrs = attributes.get(token.getTokenType());
						if (attrs != null) {
							if (attrs == errorAttributes) {
								errorLines.add(token.getLine() - 1);
								LOG.info("Found Error in line " + (token.getLine() - 1));
							}
							tokenstart = lineLength[token.getLine() - 1] + token.getColumn() - 1;
							document.remove(tokenstart, token.getValue().length());
							document.insertString(tokenstart, token.getValue(), attrs);
						}
					}
					paintErrors();
					return tokens;
				}

				private void parserCheck(Document document, List<Token> tokens, int[] lineLength)
						throws BadLocationException {
					SimpleAttributeSet attrs;
					AST ast = ide.runParser(tokens, true);
					/*
					 * TODO get reportLog
					 */
					int tokenstart;
					for (Token token : tokens) {
						attrs = attributes.get(token.getTokenType());
						if (attrs != null) {
							tokenstart = lineLength[token.getLine() - 1] + token.getColumn() - 1;
							document.remove(tokenstart, token.getValue().length());
							document.insertString(tokenstart, token.getValue(), attrs);
						}
					}
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

	private class LoadFileListener implements ActionListener {

		private final IDE ide;
		private final SaveFileListener listener;

		public LoadFileListener(IDE ide, SaveFileListener listener) {
			this.ide = ide;
			this.listener = listener;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					JFileChooser chooser = new JFileChooser();
					chooser.setFileFilter(new FileNameExtensionFilter("choose a prog file", "prog"));
					int showOpenDialog = chooser.showOpenDialog(component);
					switch (showOpenDialog) {
					case JFileChooser.APPROVE_OPTION:
						File file = chooser.getSelectedFile();
						BufferedReader reader = null;
						StringBuilder sourceCode = new StringBuilder();
						String line;
						try {
							reader = new BufferedReader(new InputStreamReader(new FileInputStream(
									file)));
							while ((line = reader.readLine()) != null) {
								sourceCode.append(line);
								sourceCode.append('\n');
							}
							sourceCodeLock.lock();
							try {
								boolean oldValue = setSourceCode;
								setSourceCode = true;
								ide.setSourceCode(sourceCode.substring(0, sourceCode.length() - 1));
								setSourceCode = oldValue;
							} finally {
								sourceCodeLock.unlock();
							}
							if (listener != null) {
								listener.file = file;
							}
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
					case JFileChooser.CANCEL_OPTION:
					case JFileChooser.ERROR_OPTION:
						break;
					default:
						LOG.warn("unknown result while choosing the load file");
						break;
					}
				}
			});
		}
	}

	private class SaveFileListener implements ActionListener {
		private File file = null;

		private static final String SAFE_NEW_FILE = "safe new";
		private static final String SAFE_OLD_FILE = "safe old";

		@Override
		public void actionPerformed(ActionEvent e) {
			final String text = sourceCodeField.getText();
			final boolean safeNew = SAFE_NEW_FILE.equals(e.getActionCommand());
			if (safeNew || SAFE_OLD_FILE.equals(e.getActionCommand())) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						File newFile = file;
						if (safeNew || newFile == null) {
							JFileChooser chooser = new JFileChooser();
							chooser.setFileFilter(new FileNameExtensionFilter("choose a filename",
									"prog"));
							int showOpenDialog = chooser.showSaveDialog(component);
							switch (showOpenDialog) {
							case JFileChooser.APPROVE_OPTION:
								newFile = chooser.getSelectedFile();
								if (!newFile.exists()) {
									try {
										newFile.createNewFile();
									} catch (IOException e) {
										LOG.error("couldn't create file", e);
										return;
									}
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

}
