package swp_compiler_ss13.fuc.gui.sourcecode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;
import swp_compiler_ss13.fuc.gui.ide.mvc.IDE;
import swp_compiler_ss13.fuc.gui.ide.mvc.Position;
import swp_compiler_ss13.fuc.gui.ide.mvc.View;

public class SourceCodeView implements View {

	private final SourceCodeController controller;

	private final JTextArea sourceCodeField;
	private final JScrollPane component;

	public SourceCodeView(SourceCodeController controller) {
		this.controller = controller;
		sourceCodeField = new JTextArea();
		sourceCodeField.setEditable(true);
		component = new JScrollPane();
		component.setViewportView(sourceCodeField);
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

	void setSourceCode(String code) {
		sourceCodeField.setText(code);
	}

	@Override
	public void initComponents(IDE ide) {
		JButton copyButton = new JButton("copy code to model");
		copyButton.addActionListener(new CopyListener(ide));
		ide.addButton(copyButton, getPosition(), false);
	}

	private class CopyListener implements ActionListener {

		private final IDE ide;

		public CopyListener(IDE ide) {
			this.ide = ide;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			ide.setSourceCode(sourceCodeField.getText());
		}

	}

}
