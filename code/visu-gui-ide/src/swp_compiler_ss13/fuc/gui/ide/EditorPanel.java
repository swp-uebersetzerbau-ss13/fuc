package swp_compiler_ss13.fuc.gui.ide;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

public class EditorPanel extends JPanel {
	private JTextPane textPane;

	/**
	 * Create the panel.
	 */
	public EditorPanel() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 35 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0 };
		gridBagLayout.rowWeights = new double[] { 0.0 };
		this.setLayout(gridBagLayout);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setFocusable(false);
		scrollPane_1.setEnabled(false);
		scrollPane_1.setBorder(null);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 0;
		this.add(scrollPane_1, gbc_scrollPane_1);

		JTextPane textPane_1 = new JTextPane();
		scrollPane_1.setViewportView(textPane_1);
		textPane_1.setEnabled(false);
		textPane_1.setEditable(false);
		textPane_1.setDisabledTextColor(Color.BLACK);
		textPane_1.setBackground(Color.LIGHT_GRAY);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.weighty = 1.0;
		gbc_scrollPane.weightx = 1.0;
		gbc_scrollPane.anchor = GridBagConstraints.NORTHWEST;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 0;
		this.add(scrollPane, gbc_scrollPane);

		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		this.textPane = new JTextPane();
		panel.add(this.textPane);
		this.textPane.setAlignmentY(Component.TOP_ALIGNMENT);
		this.textPane.setAlignmentX(Component.LEFT_ALIGNMENT);

		StringBuilder linenumbers = new StringBuilder();
		for (int i = 1; i < 1000; i++) {
			linenumbers.append(String.format("%03d", i)).append("\n");
		}
		textPane_1.setText(linenumbers.toString());
		textPane_1.setPreferredSize(new Dimension(30, 21));
		textPane_1.setMinimumSize(new Dimension(30, 6));

		scrollPane_1.getVerticalScrollBar().setModel(scrollPane.getVerticalScrollBar().getModel());
	}

	public void setText(String text) {
		this.textPane.setText(text);
	}

	public String getText() {
		return this.textPane.getText();
	}

	public void clearText() {
		this.textPane.setText("");
	}

}
