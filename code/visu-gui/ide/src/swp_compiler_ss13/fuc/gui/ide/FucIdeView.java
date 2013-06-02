package swp_compiler_ss13.fuc.gui.ide;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.common.semanticAnalysis.SemanticAnalyser;

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
	private JTabbedPane componentTabs;
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

	/**
	 * Create the frame.
	 */
	public FucIdeView(FucIdeController controller) {
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
		this.aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		this.aboutMenuItem.setMnemonic(KeyEvent.VK_A);
		this.ideMenu.add(this.aboutMenuItem);

		JSeparator separator = new JSeparator();
		this.ideMenu.add(separator);

		this.quitMenuItem = new JMenuItem("Quit");
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

		this.buttonPanel = new JPanel();
		this.contentPane.add(this.buttonPanel, BorderLayout.NORTH);
		this.buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		this.runButton = new JButton("run");
		this.runButton.setMargin(new Insets(2, 2, 2, 2));
		this.runButton
				.setIcon(new ImageIcon(FucIdeView.class.getResource("/swp_compiler_ss13/fuc/gui/ide/assets/run.png")));
		this.buttonPanel.add(this.runButton);

		this.splitPane = new JSplitPane();
		this.splitPane.setOneTouchExpandable(true);
		this.splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.contentPane.add(this.splitPane, BorderLayout.CENTER);

		this.consolePanel = new JPanel();
		this.splitPane.setRightComponent(this.consolePanel);
		this.consolePanel.setLayout(new GridLayout(1, 1, 0, 0));

		this.consoleOutput = new JTextPane();
		this.consolePanel.add(this.consoleOutput);

		this.componentTabs = new JTabbedPane(JTabbedPane.TOP);
		this.splitPane.setLeftComponent(this.componentTabs);
		this.splitPane.setDividerLocation(200);

		this.statusPanel = new JPanel();
		this.contentPane.add(this.statusPanel, BorderLayout.SOUTH);
	}

	public void addComponentRadioMenuItem(Lexer lexer) {
		String name = lexer.getClass().getName();
		JRadioButtonMenuItem jrb = new JRadioButtonMenuItem(name);
		this.lexerMenus.put(jrb, lexer);
		this.lexerMenu.add(jrb);
		this.lexerGroup.add(jrb);
		if (this.lexerMenus.size() == 1) {
			jrb.setSelected(true);
		}
	}

	public void addComponentRadioMenuItem(Parser parser) {
		String name = parser.getClass().getName();
		JRadioButtonMenuItem jrb = new JRadioButtonMenuItem(name);
		this.parserMenus.put(jrb, parser);
		this.parserMenu.add(jrb);
		this.parserGroup.add(jrb);
		if (this.parserMenus.size() == 1) {
			jrb.setSelected(true);
		}
	}

	public void addComponentRadioMenuItem(SemanticAnalyser analyzer) {
		String name = analyzer.getClass().getName();
		JRadioButtonMenuItem jrb = new JRadioButtonMenuItem(name);
		this.semanticAnalysisMenus.put(jrb, analyzer);
		this.semanticAnalysisMenu.add(jrb);
		this.semanticGroup.add(jrb);
		if (this.semanticAnalysisMenus.size() == 1) {
			jrb.setSelected(true);
		}
	}

	public void addComponentRadioMenuItem(IntermediateCodeGenerator irgen) {
		String name = irgen.getClass().getName();
		JRadioButtonMenuItem jrb = new JRadioButtonMenuItem(name);
		this.irgenMenus.put(jrb, irgen);
		this.irgenMenu.add(jrb);
		this.irgenGroup.add(jrb);
		if (this.irgenMenus.size() == 1) {
			jrb.setSelected(true);
		}
	}

	public void addComponentRadioMenuItem(Backend backend) {
		String name = backend.getClass().getName();
		JRadioButtonMenuItem jrb = new JRadioButtonMenuItem(name);
		this.backendMenus.put(jrb, backend);
		this.backendMenu.add(jrb);
		this.backendGroup.add(jrb);
		if (this.backendMenus.size() == 1) {
			jrb.setSelected(true);
		}
	}

}
