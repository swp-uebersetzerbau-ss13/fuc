package swp_compiler_ss13.fuc.gui.ast;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import sun.swing.SwingAccessor;
import swp_compiler_ss13.common.ast.ASTNode;

public class SemanticNodeComponent extends NodeComponent {

	private static final Logger LOG = Logger.getLogger(SemanticNodeComponent.class);

	public SemanticNodeComponent(ASTNode node) {
		super(node);
	}

	protected JComponent getInfoComponent(ASTNode node) {
		JPanel result = new JPanel();
		result.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		Map<?, ?> attributeValues = node.getAttributeValues();
		result.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		Insets elementInsets = new Insets(2, 2, 2, 2);
		Insets seperatorInsets = new Insets(0, 0, 0, 0);
		c.gridy = -1;
		LOG.debug("checking " + (attributeValues.isEmpty() ? "empty" : "full") + " attribute map");
		for (Entry<?, ?> entry : attributeValues.entrySet()) {
			c.gridy++;
			c.gridx = 0;
			c.gridwidth = 5;
			c.insets = seperatorInsets;
			result.add(new JSeparator(SwingConstants.HORIZONTAL), c);
			c.gridy++;
			c.gridx++;
			c.gridwidth = 1;
			c.insets = elementInsets;
			result.add(new JLabel(entry.getKey().toString()), c);
			c.gridx += 2;
			result.add(new JLabel(entry.getValue().toString()), c);
			LOG.debug(String.format("found attribute %s with value %s", entry.getKey(), entry
					.getValue()));
		}
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 5;
		c.insets = seperatorInsets;
		result.add(new JSeparator(SwingConstants.HORIZONTAL), c);
		c.gridheight = c.gridy;
		c.gridy = 0;
		c.gridwidth = 1;
		result.add(new JSeparator(SwingConstants.VERTICAL), c);
		c.gridx += 2;
		result.add(new JSeparator(SwingConstants.VERTICAL), c);
		c.gridx += 2;
		result.add(new JSeparator(SwingConstants.VERTICAL), c);
		return result;
	}

}
