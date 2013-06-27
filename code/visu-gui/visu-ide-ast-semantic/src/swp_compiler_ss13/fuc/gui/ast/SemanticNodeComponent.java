package swp_compiler_ss13.fuc.gui.ast;

import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

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
		result.setLayout(new GridLayout(attributeValues.size(), 2, 2, 2));
		for (Entry<?, ?> entry : attributeValues.entrySet()) {
			result.add(new JLabel(entry.getKey().toString()));
			result.add(new JLabel(entry.getValue().toString()));
			LOG.debug(String.format("found attribute %s with value %s", entry.getKey(), entry
					.getValue()));
		}
		return result;
	}

}
