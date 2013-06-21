package swp_compiler_ss13.fuc.gui.ast;

import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;

/**
 * @author "Eduard Wolf"
 * 
 */
public class AST_Component {

	private static final Logger LOG = Logger.getLogger(AST_Component.class);

	private static final int BUTTON_LAYER = 2;
	private static final int CHILD_LAYER = 1;
	private static final int ARROW_LAYER = 0;

	private final List<AST_Component> astChildren;
	private final List<JComponent> children;
	private final Arrow_Panel arrows;
	private final AST_Controller controller;
	private final JComponent wrapper;
	private final JComponent component;

	private NodeComponent nodeComponent;

	private final boolean isRoot;

	public AST_Component(AST_Controller controller, boolean isRoot) {
		this.controller = controller;
		this.isRoot = isRoot;
		astChildren = new LinkedList<>();
		wrapper = new JLayeredPane();
		component = isRoot ? new JScrollPane(wrapper) : wrapper;
		children = new ArrayList<>();
		wrapper.setLayout(new AST_LayoutManager());
		arrows = new Arrow_Panel();
		arrows.setVisible(false);
		wrapper.add(arrows, AST_LayoutManager.ARROWS, ARROW_LAYER);
	}

	public void setNode(ASTNode node) {
		wrapper.removeAll();
		if (node != null) {
			nodeComponent = new NodeComponent(node);
			JComponent label = nodeComponent.getComponent();
			label.setToolTipText(node.getNodeType().name());
			wrapper.add(label, AST_LayoutManager.BUTTON, BUTTON_LAYER);
			if (!node.getChildren().isEmpty()) {
				label.addMouseListener(new ClickListener());
			}
		}
		wrapper.add(arrows, AST_LayoutManager.ARROWS, ARROW_LAYER);

		arrows.setVisible(false);
	}

	void toggleSize() {
		if (nodeComponent != null) {
			nodeComponent.toggleSize();
		}
		for (AST_Component child : astChildren) {
			child.toggleSize();
		}
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void addChild(AST_Component child) {
		astChildren.add(child);
		children.add(child.component);
		child.component.setVisible(false);
		wrapper.add(child.component, AST_LayoutManager.CHILDREN, CHILD_LAYER);
	}

	public void changeChildState() {
		boolean visibleState = !arrows.isVisible();
		arrows.setVisible(visibleState);
		for (JComponent child : children) {
			child.setVisible(visibleState);
		}
	}

	public void recalculateLayout() {
		LOG.debug("recalculate layout");
		wrapper.getParent().validate();
	}

	JComponent getComponent() {
		return component;
	}

	private class ClickListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			LOG.debug("button clicked");
			controller.viewStateChanged();
		}
	}
}
