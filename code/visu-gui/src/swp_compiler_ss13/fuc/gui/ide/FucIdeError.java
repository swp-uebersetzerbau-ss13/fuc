package swp_compiler_ss13.fuc.gui.ide;

import java.awt.Component;

import javax.swing.JOptionPane;

public class FucIdeError {
	public FucIdeError(Component parent, String errorMessage) {
		JOptionPane.showMessageDialog(parent, errorMessage, "FUC IDE Error", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}
}
