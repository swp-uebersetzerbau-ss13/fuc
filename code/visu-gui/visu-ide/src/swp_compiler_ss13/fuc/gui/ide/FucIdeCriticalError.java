package swp_compiler_ss13.fuc.gui.ide;

import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.swing.JOptionPane;

/**
 * Displays a critical error on the view
 * 
 * @author "Frank Zechert"
 * 
 */
public class FucIdeCriticalError {
	private boolean recoverable;
	private String errormsg;
	private Component parent;

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
		this.parent = parent;
		this.errormsg = errormsg;
		this.recoverable = recoverable;
		this.show();
	}

	public FucIdeCriticalError(Component parent, Throwable e, boolean recoverable) {
		this.parent = parent;
		this.recoverable = recoverable;
		StringBuilder message = new StringBuilder();
		message.append("An Exception occurred!\n\n");
		message.append(e.toString() + "\n");
		message.append(e.getMessage() + "\n");
		Throwable t = e;
		while (t.getCause() != null && message.length() < 400)
		{
			message.append("caused by: " + t.toString() + "\n");
		}
		message.append("\n");
		message.append("Stacktrace (truncated):\n");
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		t.printStackTrace(printWriter);
		message.append(result.toString().substring(0, 600) + "...");
		this.errormsg = message.toString();
		this.show();

	}

	private void show() {
		if (FucIdeCriticalError.this.recoverable) {
			FucIdeCriticalError.this.errormsg += "\n\nPress OK to quit. Press Cancel to continue anyway.";
			int r = JOptionPane.showConfirmDialog(FucIdeCriticalError.this.parent,
					FucIdeCriticalError.this.errormsg, "Recoverable Error",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.ERROR_MESSAGE);
			if (r == JOptionPane.OK_OPTION) {
				System.exit(1);
			}
		}
		else {
			JOptionPane.showMessageDialog(FucIdeCriticalError.this.parent,
					FucIdeCriticalError.this.errormsg, "Critical Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

	}
}
