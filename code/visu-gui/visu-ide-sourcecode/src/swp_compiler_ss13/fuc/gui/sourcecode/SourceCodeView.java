package swp_compiler_ss13.fuc.gui.sourcecode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

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

	void setSourceCode(final String code) {}

	@Override
	public void initComponents(IDE ide) {
		sourceCodeField.getDocument().addDocumentListener(new CopyListener(ide));
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
					ide.setSourceCode(sourceCodeField.getText());
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

}
