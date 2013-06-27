package swp_compiler_ss13.fuc.gui.text;

import java.awt.BorderLayout;
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

/**
 * 
 * View of {@link IDE} to display the elements converted from {@link Text_Model}
 * 
 * @author "Eduard Wolf"
 * 
 */
public class Text_View implements View {

	private static final Logger LOG = Logger.getLogger(Text_View.class);

	private final Position position;
	private final Controller controller;
	private final JScrollPane panel;
	private final JPanel contentPanel;
	private final JTextPane area;

	private final Document document;

	public Text_View(Controller controller, Position position) {
		this.controller = controller;
		this.position = position;
		this.contentPanel = new JPanel(new BorderLayout());
		this.panel = new JScrollPane(this.contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		area = new JTextPane();
		area.setEditable(false);
		document = area.getDocument();
		contentPanel.add(area);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JComponent getComponent() {
		return this.panel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.position.name();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Position getPosition() {
		return this.position;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Controller getController() {
		return this.controller;
	}

	public void setViewInformation(List<StringColourPair> viewInformation) {
		try {
			document.remove(0, document.getLength());
		} catch (BadLocationException e) {
			LOG.error("Error while removing text to View component", e);
		}
		SimpleAttributeSet attrs;
		for (StringColourPair pair : viewInformation) {
			attrs = new SimpleAttributeSet();
			StyleConstants.setForeground(attrs, pair.getColor().getColor());
			try {
				document.insertString(document.getLength(), pair.getText(), attrs);
			} catch (BadLocationException e) {
				LOG.error("Error while adding text to View component", e);
			}
		}
		panel.validate();
		area.setCaretPosition(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initComponents(IDE ide) {}

}
