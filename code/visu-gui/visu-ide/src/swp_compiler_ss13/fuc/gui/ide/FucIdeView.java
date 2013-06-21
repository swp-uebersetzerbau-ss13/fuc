package swp_compiler_ss13.fuc.gui.ide;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.common.semanticAnalysis.SemanticAnalyser;
import swp_compiler_ss13.fuc.gui.ide.data.FucIdeButton;
import swp_compiler_ss13.fuc.gui.ide.data.FucIdeMenu;
import swp_compiler_ss13.fuc.gui.ide.data.FucIdeStatusLabel;
import swp_compiler_ss13.fuc.gui.ide.data.FucIdeTab;
import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;

public class FucIdeView extends JFrame {
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenuItem quitMenuItem;
	private JMenuItem aboutMenuItem;
	private JMenu ideMenu;
	private JMenu componentsMenu;
	private JMenu lexerMenu;
	private JMenu parserMenu;
	private JMenu semanticAnalysisMenu;
	private JMenu backendMenu;
	private JMenu irgenMenu;
	private JButton runButton;
	private JPanel buttonPanel;
	private JSplitPane splitPane;
	private JPanel statusPanel;
	private JPanel consolePanel;
	private JTextPane consoleOutput;
	private FucIdeController controller;
	private Map<JRadioButtonMenuItem, Lexer> lexerMenus = new HashMap<>();
	private Map<JRadioButtonMenuItem, Parser> parserMenus = new HashMap<>();
	private Map<JRadioButtonMenuItem, SemanticAnalyser> semanticAnalysisMenus = new HashMap<>();
	private Map<JRadioButtonMenuItem, IntermediateCodeGenerator> irgenMenus = new HashMap<>();
	private Map<JRadioButtonMenuItem, Backend> backendMenus = new HashMap<>();

	private ButtonGroup lexerGroup = new ButtonGroup();
	private ButtonGroup parserGroup = new ButtonGroup();
	private ButtonGroup semanticGroup = new ButtonGroup();
	private ButtonGroup irgenGroup = new ButtonGroup();
	private ButtonGroup backendGroup = new ButtonGroup();
	private ButtonGroup logGroup = new ButtonGroup();

	private List<JMenu> customMenus = new LinkedList<>();
	private JPanel panel;
	private JPanel panel_1;
	private List<JButton> customButtons = new LinkedList<>();
	private List<JLabel> customLabels = new LinkedList<>();
	private JScrollPane scrollPane;
	private JPanel panel_2;
	private JSplitPane splitPane_1;
	private JTabbedPane componentTabs;
	private JPanel panel_3;
	private JLabel lblErrorReport;
	private JScrollPane scrollPane_1;
	private JList<String> errorReportList;
	private Logger logger = Logger.getLogger(FucIdeView.class);
	private JButton execButton;
	private JMenu logLevelMenu;
	private JRadioButtonMenuItem rdbtnmntmAll;
	private JRadioButtonMenuItem rdbtnmntmError;
	private JRadioButtonMenuItem rdbtnmntmErrorWarn;
	private JRadioButtonMenuItem rdbtnmntmErrorWarnInfo;
	private JRadioButtonMenuItem rdbtnmntmErrorWarnInfoDebug;
	private JRadioButtonMenuItem rdbtnmntmOff;

	/**
	 * Create the frame.
	 */
	public FucIdeView(final FucIdeController controller) {
		this.controller = controller;

		this.setTitle("FUC Compiler IDE");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				FucIdeView.class.getResource("/swp_compiler_ss13/fuc/gui/ide/assets/fuc.png")));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);

		this.menuBar = new JMenuBar();
		this.setJMenuBar(this.menuBar);

		this.ideMenu = new JMenu("IDE");
		this.ideMenu.setMnemonic(KeyEvent.VK_I);
		this.menuBar.add(this.ideMenu);

		this.aboutMenuItem = new JMenuItem("About");
		this.aboutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new FucIdeAboutView().setVisible(true);
			}
		});

		this.logLevelMenu = new JMenu("Log Verbosity");
		this.ideMenu.add(this.logLevelMenu);

		this.rdbtnmntmOff = new JRadioButtonMenuItem("OFF");
		this.logLevelMenu.add(this.rdbtnmntmOff);

		this.rdbtnmntmError = new JRadioButtonMenuItem("ERROR");
		this.logLevelMenu.add(this.rdbtnmntmError);

		this.rdbtnmntmErrorWarn = new JRadioButtonMenuItem("ERROR, WARN");
		this.logLevelMenu.add(this.rdbtnmntmErrorWarn);

		this.rdbtnmntmErrorWarnInfo = new JRadioButtonMenuItem("ERROR, WARN, INFO");
		this.logLevelMenu.add(this.rdbtnmntmErrorWarnInfo);

		this.rdbtnmntmErrorWarnInfoDebug = new JRadioButtonMenuItem("ERROR, WARN, INFO, DEBUG");
		this.logLevelMenu.add(this.rdbtnmntmErrorWarnInfoDebug);

		this.rdbtnmntmAll = new JRadioButtonMenuItem("ALL");
		this.logLevelMenu.add(this.rdbtnmntmAll);

		this.logGroup.add(this.rdbtnmntmOff);
		this.logGroup.add(this.rdbtnmntmError);
		this.logGroup.add(this.rdbtnmntmErrorWarn);
		this.logGroup.add(this.rdbtnmntmErrorWarnInfo);
		this.logGroup.add(this.rdbtnmntmErrorWarnInfoDebug);
		this.logGroup.add(this.rdbtnmntmAll);

		this.rdbtnmntmErrorWarnInfo.setSelected(true);

		this.rdbtnmntmOff.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FucIdeView.this.controller.onLogLevelSelected(org.apache.log4j.Level.OFF);
			}
		});

		this.rdbtnmntmError.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FucIdeView.this.controller.onLogLevelSelected(org.apache.log4j.Level.ERROR);
			}
		});

		this.rdbtnmntmErrorWarn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FucIdeView.this.controller.onLogLevelSelected(org.apache.log4j.Level.WARN);
			}
		});

		this.rdbtnmntmErrorWarnInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FucIdeView.this.controller.onLogLevelSelected(org.apache.log4j.Level.INFO);
			}
		});

		this.rdbtnmntmErrorWarnInfoDebug.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FucIdeView.this.controller.onLogLevelSelected(org.apache.log4j.Level.DEBUG);
			}
		});

		this.rdbtnmntmAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FucIdeView.this.controller.onLogLevelSelected(org.apache.log4j.Level.ALL);
			}
		});

		this.aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		this.aboutMenuItem.setMnemonic(KeyEvent.VK_A);
		this.ideMenu.add(this.aboutMenuItem);

		JSeparator separator = new JSeparator();
		this.ideMenu.add(separator);

		this.quitMenuItem = new JMenuItem("Quit");
		this.quitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FucIdeView.this.dispose();
				System.exit(0);
			}
		});
		this.quitMenuItem.setMnemonic(KeyEvent.VK_Q);
		this.quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		this.ideMenu.add(this.quitMenuItem);

		this.componentsMenu = new JMenu("Components");
		this.componentsMenu.setMnemonic(KeyEvent.VK_C);
		this.menuBar.add(this.componentsMenu);

		this.lexerMenu = new JMenu("Lexer");
		this.lexerMenu.setMnemonic(KeyEvent.VK_L);
		this.componentsMenu.add(this.lexerMenu);

		this.parserMenu = new JMenu("Parser");
		this.parserMenu.setMnemonic(KeyEvent.VK_P);
		this.componentsMenu.add(this.parserMenu);

		this.semanticAnalysisMenu = new JMenu("Semantic Analysis");
		this.semanticAnalysisMenu.setMnemonic(KeyEvent.VK_S);
		this.componentsMenu.add(this.semanticAnalysisMenu);

		this.irgenMenu = new JMenu("IRC Generator");
		this.irgenMenu.setToolTipText("Intermediate Representation Code Generator");
		this.irgenMenu.setMnemonic(KeyEvent.VK_I);
		this.componentsMenu.add(this.irgenMenu);

		this.backendMenu = new JMenu("Backend");
		this.backendMenu.setMnemonic(KeyEvent.VK_B);
		this.componentsMenu.add(this.backendMenu);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(new BorderLayout(0, 0));

		this.panel = new JPanel();
		this.contentPane.add(this.panel, BorderLayout.NORTH);
		this.panel.setLayout(new BorderLayout(0, 0));

		this.buttonPanel = new JPanel();
		this.panel.add(this.buttonPanel);
		this.buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

		this.runButton = new JButton("compile");
		this.runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.onRunPressed();
			}
		});
		this.runButton
				.setIcon(new ImageIcon(FucIdeView.class
						.getResource("/swp_compiler_ss13/fuc/gui/ide/assets/compile.png")));
		this.buttonPanel.add(this.runButton);

		this.execButton = new JButton("execute");
		this.execButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.onExecPressed();
			}
		});
		this.execButton.setEnabled(false);
		this.execButton.setToolTipText("Please compile first!");
		this.execButton.setIcon(new ImageIcon(FucIdeView.class
				.getResource("/swp_compiler_ss13/fuc/gui/ide/assets/execute.png")));
		this.buttonPanel.add(this.execButton);

		this.panel_1 = new JPanel();
		this.panel.add(this.panel_1, BorderLayout.SOUTH);

		this.splitPane = new JSplitPane();
		this.splitPane.setOneTouchExpandable(true);
		this.splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.contentPane.add(this.splitPane, BorderLayout.CENTER);

		this.consolePanel = new JPanel();
		this.splitPane.setRightComponent(this.consolePanel);
		this.consolePanel.setLayout(new GridLayout(1, 1, 0, 0));

		this.scrollPane = new JScrollPane();
		this.consolePanel.add(this.scrollPane);

		this.consoleOutput = new JTextPane();
		this.scrollPane.setViewportView(this.consoleOutput);
		this.consoleOutput.setEditable(false);

		this.splitPane_1 = new JSplitPane();
		this.splitPane.setLeftComponent(this.splitPane_1);

		this.componentTabs = new JTabbedPane(JTabbedPane.TOP);
		this.componentTabs.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				controller.tabChanged();
			}
		});
		this.splitPane_1.setLeftComponent(this.componentTabs);
		this.splitPane_1.setDividerLocation(600);

		this.panel_3 = new JPanel();
		this.splitPane_1.setRightComponent(this.panel_3);
		this.panel_3.setLayout(new BorderLayout(5, 5));

		this.lblErrorReport = new JLabel("Error Report");
		this.lblErrorReport.setFont(new Font("Dialog", Font.BOLD, 18));
		this.lblErrorReport.setVerticalAlignment(SwingConstants.BOTTOM);
		this.lblErrorReport.setPreferredSize(new Dimension(87, 25));
		this.lblErrorReport.setHorizontalAlignment(SwingConstants.CENTER);
		this.lblErrorReport.setHorizontalTextPosition(SwingConstants.CENTER);
		this.panel_3.add(this.lblErrorReport, BorderLayout.NORTH);
		DefaultListModel<String> lm = new DefaultListModel<String>();
		lm.addElement("No errors reported");

		this.scrollPane_1 = new JScrollPane();
		this.panel_3.add(this.scrollPane_1, BorderLayout.CENTER);

		this.errorReportList = new JList<String>();
		this.errorReportList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.errorReportList.setModel(lm);
		this.scrollPane_1.setViewportView(this.errorReportList);
		this.splitPane.setDividerLocation(400);

		this.statusPanel = new JPanel();
		this.statusPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		FlowLayout flowLayout = (FlowLayout) this.statusPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		this.contentPane.add(this.statusPanel, BorderLayout.SOUTH);

		this.panel_2 = new JPanel();
		this.contentPane.add(this.panel_2, BorderLayout.EAST);

		this.setBounds(new Rectangle(0, 0, 800, 600));
		this.setSize(new Dimension(800, 600));

		this.setLocationRelativeTo(null);
	}

	public void addComponentRadioMenuItem(final Lexer lexer) {
		String name = lexer.getClass().getName();
		JRadioButtonMenuItem jrb = new JRadioButtonMenuItem(name);
		this.lexerMenus.put(jrb, lexer);
		this.lexerMenu.add(jrb);
		this.lexerGroup.add(jrb);
		if (this.lexerMenus.size() == 1) {
			jrb.setSelected(true);
			this.controller.onLexerSelected(lexer);
		}
		jrb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FucIdeView.this.controller.onLexerSelected(lexer);
			}
		});
	}

	public void addComponentRadioMenuItem(final Parser parser) {
		String name = parser.getClass().getName();
		JRadioButtonMenuItem jrb = new JRadioButtonMenuItem(name);
		this.parserMenus.put(jrb, parser);
		this.parserMenu.add(jrb);
		this.parserGroup.add(jrb);
		if (this.parserMenus.size() == 1) {
			jrb.setSelected(true);
			this.controller.onParserSelected(parser);
		}
		jrb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FucIdeView.this.controller.onParserSelected(parser);
			}
		});
	}

	public void addComponentRadioMenuItem(final SemanticAnalyser analyzer) {
		String name = analyzer.getClass().getName();
		JRadioButtonMenuItem jrb = new JRadioButtonMenuItem(name);
		this.semanticAnalysisMenus.put(jrb, analyzer);
		this.semanticAnalysisMenu.add(jrb);
		this.semanticGroup.add(jrb);
		if (this.semanticAnalysisMenus.size() == 1) {
			jrb.setSelected(true);
			this.controller.onAnalyzerSelected(analyzer);
		}
		jrb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FucIdeView.this.controller.onAnalyzerSelected(analyzer);
			}
		});
	}

	public void addComponentRadioMenuItem(final IntermediateCodeGenerator irgen) {
		String name = irgen.getClass().getName();
		JRadioButtonMenuItem jrb = new JRadioButtonMenuItem(name);
		this.irgenMenus.put(jrb, irgen);
		this.irgenMenu.add(jrb);
		this.irgenGroup.add(jrb);
		if (this.irgenMenus.size() == 1) {
			jrb.setSelected(true);
			this.controller.onIRGSelected(irgen);
		}
		jrb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FucIdeView.this.controller.onIRGSelected(irgen);
			}
		});
	}

	public void addComponentRadioMenuItem(final Backend backend) {
		String name = backend.getClass().getName();
		JRadioButtonMenuItem jrb = new JRadioButtonMenuItem(name);
		this.backendMenus.put(jrb, backend);
		this.backendMenu.add(jrb);
		this.backendGroup.add(jrb);
		if (this.backendMenus.size() == 1) {
			jrb.setSelected(true);
			this.controller.onBackendSelected(backend);
		}
		jrb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FucIdeView.this.controller.onBackendSelected(backend);
			}
		});
	}

	public void clearTabs() {
		this.componentTabs.removeAll();
		this.componentTabs.revalidate();
		this.componentTabs.repaint();
	}

	public void addTab(String name, JComponent c) {
		this.componentTabs.add(name, c);
		this.componentTabs.revalidate();
		this.componentTabs.repaint();

	}

	public void clearMenus() {
		for (JMenu m : this.customMenus) {
			this.menuBar.remove(m);
		}
		this.customMenus.clear();
		this.menuBar.revalidate();
		this.menuBar.repaint();
	}

	public void addMenu(FucIdeMenu menu) {
		this.menuBar.add(menu.getMenu());
		this.customMenus.add(menu.getMenu());
		this.menuBar.revalidate();
		this.menuBar.repaint();
	}

	public void clearButtons() {
		for (JButton b : this.customButtons) {
			this.logger.info("Removing the button " + b.getText());
			this.buttonPanel.remove(b);
			b.revalidate();
		}
		this.customButtons.clear();
		this.buttonPanel.revalidate();
		this.buttonPanel.repaint();
	}

	public void addButton(FucIdeButton button) {
		this.logger.info("Adding the button " + button.getButton().getText());
		this.buttonPanel.add(button.getButton());
		this.customButtons.add(button.getButton());
		this.buttonPanel.revalidate();
		this.buttonPanel.repaint();
	}

	public void clearLabels() {
		for (JLabel l : this.customLabels) {
			this.statusPanel.remove(l);
		}
		this.customLabels.clear();
		this.statusPanel.revalidate();
		this.statusPanel.repaint();
	}

	public void addLabel(FucIdeStatusLabel label) {
		this.statusPanel.add(label.getLabel());
		this.customLabels.add(label.getLabel());
		this.statusPanel.revalidate();
		this.statusPanel.repaint();
	}

	public boolean isFirstTab(FucIdeTab t) {
		try {
			return this.componentTabs.getComponentAt(0) == t.getComponent();
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	public boolean isCurrentTab(FucIdeTab t) {
		return this.componentTabs.getSelectedComponent() == t.getComponent();
	}

	public void showFirstTab() {
		this.componentTabs.setSelectedIndex(0);
	}

	public void updateTextPane(String valueOf) {
		Document doc = this.consoleOutput.getDocument();
		try {
			doc.insertString(doc.getLength(), valueOf, null);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
		this.consoleOutput.setCaretPosition(doc.getLength() - 1);
	}

	public void clearErrorLog() {
		this.errorReportList.setModel(new DefaultListModel<String>());
		this.errorReportList.revalidate();
	}

	public void addErrorLog(String msg) {
		((DefaultListModel<String>) this.errorReportList.getModel()).addElement(msg);
		this.errorReportList.revalidate();
	}

	public void showTab(Controller controller) {
		this.componentTabs.setSelectedComponent(controller.getView().getComponent());
	}

	public void enableExecuteButton() {
		this.execButton.setEnabled(true);
		this.execButton.setToolTipText("");
	}

	public void disableExecuteButton() {
		this.execButton.setEnabled(false);
		this.execButton.setToolTipText("Please compile first!");
	}
}
