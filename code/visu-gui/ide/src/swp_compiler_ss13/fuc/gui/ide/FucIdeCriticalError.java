package swp_compiler_ss13.fuc.gui.ide;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * Displays a critical error on the view
 * 
 * @author "Frank Zechert"
 * 
 */
public class FucIdeCriticalError {
	/**
	 * Initialize and show the alert dialog
	 * 
	 * @param parent
	 *            The parent component
	 * @param errormsg
	 *            The error message
	 * @param recoverable
	 *            Whether the error is recoverable
	 */
	public FucIdeCriticalError(Component parent, String errormsg, boolean recoverable) {
		if (recoverable) {
			errormsg += "\n\nPress OK to quit. Press Cancel to continue anyway.";
			int r = JOptionPane.showConfirmDialog(parent, errormsg, "Recoverable Error", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.ERROR_MESSAGE);
			if (r == JOptionPane.OK_OPTION) {
				System.exit(1);
			}
		}
		else {
			JOptionPane.showMessageDialog(parent, errormsg, "Critical Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
}
