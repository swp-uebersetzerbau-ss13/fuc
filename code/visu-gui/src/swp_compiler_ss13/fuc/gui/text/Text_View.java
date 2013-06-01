package swp_compiler_ss13.fuc.gui.text;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.log4j.Logger;

import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;
import swp_compiler_ss13.fuc.gui.ide.mvc.IDE;
import swp_compiler_ss13.fuc.gui.ide.mvc.Position;
import swp_compiler_ss13.fuc.gui.ide.mvc.View;

public class Text_View implements View {

	private Logger log = Logger.getLogger(Text_View.class);

	private final Position position;
	private final Controller controller;
	private final JScrollPane panel;
	private final JPanel contentPanel;

	public Text_View(Controller controller, Position position) {
		this.controller = controller;
		this.position = position;
		contentPanel = new JPanel();
		panel = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JComponent getComponent() {
		return panel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return position.name();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Position getPosition() {
		return position;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Controller getController() {
		return controller;
	}

	public void setViewInformation(List<StringColourPair> viewInformation) {
		contentPanel.removeAll();
		JTextPane area = new JTextPane();
		SimpleAttributeSet attrs;
		Document document = area.getDocument();
		for (StringColourPair pair : viewInformation) {
			attrs = new SimpleAttributeSet();
			StyleConstants.setForeground(attrs, pair.getColor().getColor());
			try {
				document.insertString(document.getLength(), pair.getText(), attrs);
			} catch (BadLocationException e) {
				log.error("Error while adding text to View component", e);
			}
		}
		contentPanel.add(area);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initComponents(IDE ide) {}

}
