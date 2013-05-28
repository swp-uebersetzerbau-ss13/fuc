package swp_compiler_ss13.fuc.gui.ide;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import swp_compiler_ss13.fuc.gui.ide.mvc.IDE;
import swp_compiler_ss13.fuc.gui.ide.mvc.Position;

public class FUCIDE extends JFrame implements IDE {

	private JPanel contentPane;
	private JMenuBar menuBar;
	private JPanel buttonPanel;
	private JSplitPane splitPane;
	private JPanel statusBar;
	private JPanel components;
	private JPanel output;
	private JTabbedPane componentsTabs;
	private JTextPane outputText;

	private Map<Position, List<JMenu>> menus = new HashMap<>();
	private Map<Position, List<JButton>> buttons = new HashMap<>();
	private Map<Position, List<JComponent>> tabs = new HashMap<>();
	private Map<Position, List<JLabel>> statuses = new HashMap<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					FUCIDE frame = new FUCIDE();
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
	public FUCIDE() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);

		this.menuBar = new JMenuBar();
		this.setJMenuBar(this.menuBar);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(new BorderLayout(0, 0));

		this.buttonPanel = new JPanel();
		this.contentPane.add(this.buttonPanel, BorderLayout.NORTH);
		this.buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		this.splitPane = new JSplitPane();
		this.splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.contentPane.add(this.splitPane, BorderLayout.CENTER);

		this.components = new JPanel();
		this.splitPane.setLeftComponent(this.components);
		this.components.setLayout(new GridLayout(0, 1, 0, 0));

		this.componentsTabs = new JTabbedPane(JTabbedPane.TOP);
		this.components.add(this.componentsTabs);

		this.output = new JPanel();
		this.splitPane.setRightComponent(this.output);
		this.output.setLayout(new GridLayout(0, 1, 0, 0));

		this.outputText = new JTextPane();
		this.output.add(this.outputText);

		this.statusBar = new JPanel();
		FlowLayout fl_statusBar = (FlowLayout) this.statusBar.getLayout();
		fl_statusBar.setAlignment(FlowLayout.RIGHT);
		this.contentPane.add(this.statusBar, BorderLayout.SOUTH);
	}

	@Override
	public void addMenu(JMenu menu, Position position) {
		if (!this.menus.containsKey(position)) {
			this.menus.put(position, new ArrayList<JMenu>());
		}
		this.menus.get(position).add(menu);
		this.updateOwnComponents();
	}

	@Override
	public void addButton(JButton button, Position position) {
		if (!this.buttons.containsKey(position)) {
			this.buttons.put(position, new ArrayList<JButton>());
		}
		this.buttons.get(position).add(button);
		this.updateOwnComponents();
	}

	@Override
	public void addStatusLabel(JLabel label, Position position) {
		if (!this.statuses.containsKey(position)) {
			this.statuses.put(position, new ArrayList<JLabel>());
		}
		this.statuses.get(position).add(label);
		this.updateOwnComponents();
	}

	private void updateOwnComponents() {

		// update the menuBar
		this.menuBar.removeAll();
		for (Position p : Position.values()) {
			List<JMenu> menus = this.menus.get(p);
			if (menus == null) {
				continue;
			}
			Collections.sort(menus, new Comparator<JMenu>() {

				@Override
				public int compare(JMenu o1, JMenu o2) {
					return o1.getText().compareTo(o2.getText());
				}
			});
			for (JMenu menu : menus) {
				this.menuBar.add(menu);
			}
		}

		// update the buttonBar
		this.buttonPanel.removeAll();
		for (Position p : Position.values()) {
			List<JButton> buttons = this.buttons.get(p);
			if (buttons == null) {
				continue;
			}
			Collections.sort(buttons, new Comparator<JButton>() {

				@Override
				public int compare(JButton o1, JButton o2) {
					return o1.getText().compareTo(o2.getText());
				}
			});
			for (JButton button : buttons) {
				this.buttonPanel.add(button);
			}
		}

		// update the statusbar
		this.statusBar.removeAll();
		for (Position p : Position.values()) {
			List<JLabel> statuses = this.statuses.get(p);
			if (statuses == null) {
				continue;
			}
			Collections.sort(statuses, new Comparator<JLabel>() {

				@Override
				public int compare(JLabel o1, JLabel o2) {
					return o1.getText().compareTo(o2.getText());
				}
			});
			for (JLabel status : statuses) {
				this.statusBar.add(status);
			}
		}
	}
}
