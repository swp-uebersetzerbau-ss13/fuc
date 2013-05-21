package swp_compiler_ss13.fuc.gui.ide;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.parser.Parser;

public class FucIDE extends JFrame {

	private ServiceLoader<Lexer> lexerService;
	private ServiceLoader<Parser> parserService;
	private ServiceLoader<IntermediateCodeGenerator> irgenService;
	private ServiceLoader<Backend> backendService;
	private List<Lexer> lexerlist = new ArrayList<>();
	private List<Parser> parserlist = new ArrayList<>();
	private List<IntermediateCodeGenerator> irgenlist = new ArrayList<>();
	private List<Backend> backendlist = new ArrayList<>();
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					FucIDE frame = new FucIDE();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FucIDE() {
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				FucIDE.class.getResource("/swp_compiler_ss13/fuc/gui/ide/assets/fucide.png")));
		this.setTitle("Super Geile FUC IDE  -^_^-");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		mnNewMenu.setMnemonic('F');
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem("Open");

		mntmNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmNewMenuItem.setMnemonic('O');
		mnNewMenu.add(mntmNewMenuItem);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Save");

		mntmNewMenuItem_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnNewMenu.add(mntmNewMenuItem_1);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Close");

		mntmNewMenuItem_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		mnNewMenu.add(mntmNewMenuItem_2);

		JSeparator separator = new JSeparator();
		mnNewMenu.add(separator);

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Quit");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FucIDE.this.dispose();
			}
		});
		mntmNewMenuItem_3.setMnemonic('Q');
		mntmNewMenuItem_3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mnNewMenu.add(mntmNewMenuItem_3);

		JMenu mnNewMenu_1 = new JMenu("Componets");
		menuBar.add(mnNewMenu_1);

		JMenu mnNewMenu_2 = new JMenu("Lexer");
		mnNewMenu_1.add(mnNewMenu_2);

		JMenu mnNewMenu_3 = new JMenu("Parser");
		mnNewMenu_1.add(mnNewMenu_3);

		JMenu mnNewMenu_4 = new JMenu("IrGen");
		mnNewMenu_4.setToolTipText("IntermediateCodeGenerator");
		mnNewMenu_1.add(mnNewMenu_4);

		JMenu mnNewMenu_5 = new JMenu("Backend");
		mnNewMenu_1.add(mnNewMenu_5);

		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		this.contentPane.add(tabbedPane, BorderLayout.CENTER);

		final EditorPanel editorPanel = new EditorPanel();
		tabbedPane.addTab("Source", null, editorPanel, "Source Code Editor");

		JPanel panel = new JPanel();
		this.contentPane.add(panel, BorderLayout.NORTH);
		FlowLayout fl_panel = new FlowLayout(FlowLayout.LEFT, 2, 5);
		panel.setLayout(fl_panel);

		JButton RunButton = new JButton("Run");
		RunButton.setToolTipText("Run");
		RunButton.setMargin(new Insets(0, 0, 0, 0));
		RunButton.setIconTextGap(0);
		RunButton
				.setIcon(new ImageIcon(FucIDE.class.getResource("/swp_compiler_ss13/fuc/gui/ide/assets/runButton.png")));
		panel.add(RunButton);

		JButton StepButton = new JButton("Step");
		StepButton.setMargin(new Insets(0, 0, 0, 0));
		StepButton.setIcon(new ImageIcon(FucIDE.class
				.getResource("/swp_compiler_ss13/fuc/gui/ide/assets/stepButton.png")));
		panel.add(StepButton);

		JButton resetButton = new JButton("Reset");
		resetButton.setIcon(new ImageIcon(FucIDE.class
				.getResource("/swp_compiler_ss13/fuc/gui/ide/assets/resetButton.png")));
		resetButton.setMargin(new Insets(0, 0, 0, 0));
		panel.add(resetButton);

		mntmNewMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(FucIDE.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						byte[] encoded = Files.readAllBytes(file.toPath());
						String text = Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
						editorPanel.setText(text);
					} catch (IOException err) {
						JOptionPane.showMessageDialog(FucIDE.this, err.toString(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		});
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editorPanel.clearText();
			}
		});
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(FucIDE.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						if (file.exists()) {
							Files.write(file.toPath(), editorPanel.getText().getBytes(Charset.defaultCharset()),
									StandardOpenOption.TRUNCATE_EXISTING);
						}
						else {
							Files.write(file.toPath(), editorPanel.getText().getBytes(Charset.defaultCharset()),
									StandardOpenOption.CREATE);
						}
					} catch (IOException err) {
						JOptionPane.showMessageDialog(FucIDE.this, err.toString(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		});
		if (!this.loadPlugins()) {
			System.exit(1);
		}

		ButtonGroup lexergroup = new ButtonGroup();
		for (Lexer lexer : this.lexerlist) {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(lexer.getClass().getName());
			lexergroup.add(item);
			mnNewMenu_2.add(item);
			if (mnNewMenu_2.getItemCount() == 1)
			{
				item.setSelected(true);
			}
		}

		ButtonGroup parsergroup = new ButtonGroup();
		for (Parser parser : this.parserlist) {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(parser.getClass().getName());
			parsergroup.add(item);
			mnNewMenu_3.add(item);
			if (mnNewMenu_3.getItemCount() == 1)
			{
				item.setSelected(true);
			}
		}

		ButtonGroup irgengroup = new ButtonGroup();
		for (IntermediateCodeGenerator irgen : this.irgenlist) {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(irgen.getClass().getName());
			irgengroup.add(item);
			mnNewMenu_4.add(item);
			if (mnNewMenu_4.getItemCount() == 1)
			{
				item.setSelected(true);
			}
		}

		ButtonGroup backendgroup = new ButtonGroup();
		for (Backend backend : this.backendlist) {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(backend.getClass().getName());
			backendgroup.add(item);
			mnNewMenu_5.add(item);
			if (mnNewMenu_5.getItemCount() == 1)
			{
				item.setSelected(true);
			}
		}

	}

	private boolean loadPlugins() {

		// create loaders for component plugins
		this.lexerService = ServiceLoader.load(Lexer.class);
		this.parserService = ServiceLoader.load(Parser.class);
		this.irgenService = ServiceLoader.load(IntermediateCodeGenerator.class);
		this.backendService = ServiceLoader.load(Backend.class);

		for (Lexer lexer : this.lexerService) {
			this.lexerlist.add(lexer);
		}
		if (this.lexerlist.size() == 0)
		{
			JOptionPane.showMessageDialog(FucIDE.this, "No Lexer Plugin found!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		for (Parser parser : this.parserService) {
			this.parserlist.add(parser);
		}
		if (this.parserlist.size() == 0)
		{
			JOptionPane.showMessageDialog(FucIDE.this, "No Parser Plugin found!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		for (IntermediateCodeGenerator irgen : this.irgenService) {
			this.irgenlist.add(irgen);
		}
		if (this.irgenlist.size() == 0)
		{
			JOptionPane.showMessageDialog(FucIDE.this, "No IntermediateCodeGenerator Plugin found!", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		for (Backend backend : this.backendService) {
			this.backendlist.add(backend);
		}
		if (this.backendlist.size() == 0)
		{
			JOptionPane.showMessageDialog(FucIDE.this, "No Backend Plugin found!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
}
