package swp_compiler_ss13.fuc.gui.sourcecode;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;

import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;
import swp_compiler_ss13.fuc.gui.ide.mvc.IDE;
import swp_compiler_ss13.fuc.gui.ide.mvc.Position;
import swp_compiler_ss13.fuc.gui.ide.mvc.View;

public class FucIdeSourceCodeView extends JPanel implements View {
	private JTextPane txtpnLinenumbers;
	private JTextPane txtpnEditorpane;
	private Controller controller;

	/**
	 * Create the panel.
	 */
	public FucIdeSourceCodeView(Controller controller) {
		this.controller = controller;
		this.setLayout(new GridLayout(1, 1, 0, 0));

		ScrollablePanel scrollablePanel = new ScrollablePanel();
		this.add(scrollablePanel);
		SpringLayout sl_scrollablePanel = new SpringLayout();
		scrollablePanel.setLayout(sl_scrollablePanel);

		this.txtpnLinenumbers = new JTextPane();
		sl_scrollablePanel.putConstraint(SpringLayout.EAST, this.txtpnLinenumbers, 32, SpringLayout.WEST,
				scrollablePanel);
		this.txtpnLinenumbers.setDisabledTextColor(Color.BLACK);
		this.txtpnLinenumbers.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.txtpnLinenumbers.setEnabled(false);
		this.txtpnLinenumbers.setEditable(false);
		this.txtpnLinenumbers.setBackground(new Color(211, 211, 211));
		sl_scrollablePanel.putConstraint(SpringLayout.SOUTH, this.txtpnLinenumbers, 0, SpringLayout.SOUTH,
				scrollablePanel);
		sl_scrollablePanel.putConstraint(SpringLayout.NORTH, this.txtpnLinenumbers, 0, SpringLayout.NORTH,
				scrollablePanel);
		sl_scrollablePanel.putConstraint(SpringLayout.WEST, this.txtpnLinenumbers, 0, SpringLayout.WEST,
				scrollablePanel);
		scrollablePanel.add(this.txtpnLinenumbers);

		this.txtpnEditorpane = new JTextPane();
		sl_scrollablePanel.putConstraint(SpringLayout.WEST, this.txtpnEditorpane, 2, SpringLayout.EAST,
				this.txtpnLinenumbers);
		sl_scrollablePanel.putConstraint(SpringLayout.SOUTH, this.txtpnEditorpane, 0, SpringLayout.SOUTH,
				scrollablePanel);
		sl_scrollablePanel
				.putConstraint(SpringLayout.EAST, this.txtpnEditorpane, 0, SpringLayout.EAST, scrollablePanel);
		this.txtpnEditorpane.setText("editorPane");
		sl_scrollablePanel.putConstraint(SpringLayout.NORTH, this.txtpnEditorpane, 0, SpringLayout.NORTH,
				this.txtpnLinenumbers);
		scrollablePanel.add(this.txtpnEditorpane);

	}

	@Override
	public JComponent getComponent() {
		return this;
	}

	@Override
	public Position getPosition() {
		return Position.SOURCE_CODE;
	}

	@Override
	public Controller getController() {
		return this.controller;
	}

	@Override
	public void initComponents(IDE ide) {
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		ide.addMenu(fileMenu, Position.SOURCE_CODE, true);
	}
}
