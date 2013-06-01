package swp_compiler_ss13.fuc.gui.ast;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;
import swp_compiler_ss13.fuc.gui.ide.mvc.IDE;
import swp_compiler_ss13.fuc.gui.ide.mvc.Position;
import swp_compiler_ss13.fuc.gui.ide.mvc.View;

public class AST_View extends JLayeredPane implements View {

	private static final long serialVersionUID = 6193040949770147285L;
	private static final int BUTTON_LAYER = 2;
	private static final int CHILD_LAYER = 1;
	private static final int ARROW_LAYER = 0;

	private final List<JComponent> children;
	private final Arrow_Panel arrows;
	private final AST_Controller controller;

	public AST_View(AST_Controller controller) {
		this.controller = controller;
		children = new ArrayList<>();
		setLayout(new AST_LayoutManager());
		arrows = new Arrow_Panel();
		arrows.setVisible(false);
		add(arrows, AST_LayoutManager.ARROWS, ARROW_LAYER);
	}

	public AST_View setNode(ASTNode node) {
		removeAll();
		if (node != null) {
			JComponent label = getNodeComponent(node);
			label.setToolTipText(node.getNodeType().name());
			add(label, AST_LayoutManager.BUTTON, BUTTON_LAYER);
			if (!node.getChildren().isEmpty()) {
				label.addMouseListener(new ClickListener());
			}
		}
		arrows.setVisible(false);
		return this;
	}

	private JComponent getNodeComponent(ASTNode node) {
		Icon icon = getIcon(node);
		if (icon != null) {
			if (node.getChildren().isEmpty()) {
				return new JLabel(icon);
			} else {
				return new JButton("", icon);
			}
		} else {
			if (node.getChildren().isEmpty()) {
				JLabel label = new JLabel(getNodeString(node));
				if (node.getNodeType().equals(ASTNodeType.LiteralNode)) {
					label.setFont(new Font("Arial", Font.BOLD, 40));
				}
				return label;
			} else {
				JButton button = new JButton(getNodeString(node));
				if (node.getNodeType().equals(ASTNodeType.LiteralNode)) {
					button.setFont(new Font("Arial", Font.BOLD, 40));
				}
				return button;
			}
		}
	}

	private Icon getIcon(ASTNode node) {
		String dir = "res/icon/";
		if (node instanceof BinaryExpressionNode) {
			BinaryOperator operator = ((BinaryExpressionNode) node).getOperator();
			switch (operator) {
			case ADDITION:
				return new ImageIcon(dir + "add.png");
			case SUBSTRACTION:
				return new ImageIcon(dir + "minus.png");
			case MULTIPLICATION:
				return new ImageIcon(dir + "mult.png");
			case DIVISION:
				return new ImageIcon(dir + "div.png");
			case EQUAL:
				return new ImageIcon(dir + "equal.png");
			default:
				break;
			}
		}
		switch (node.getNodeType()) {
		case LogicUnaryExpressionNode:
			return new ImageIcon(dir + "not.png");
		case BlockNode:
			return new ImageIcon(dir + "braces.png");
		case AssignmentNode:
			return new ImageIcon(dir + "equal.png");
		default:
			return null;
		}
	}

	private String getNodeString(ASTNode node) {
		if (node.getNodeType().equals(ASTNodeType.LiteralNode)) {
			return ((LiteralNode) node).getLiteral();
		} else {
			return node.getNodeType().name();
		}
	}

	public void addChild(ASTNode node, AST_Controller controller) {
		AST_View child = new AST_View(controller).setNode(node);
		children.add(child);
		child.setVisible(false);
		add(child, AST_LayoutManager.CHILDREN, CHILD_LAYER);
	}

	public void changeChildState() {
		boolean visibleState = !arrows.isVisible();
		arrows.setVisible(visibleState);
		for (JComponent child : children) {
			child.setVisible(visibleState);
		}
	}

	public void recalculateLayout() {
		getParent().validate();
	}

	private class ClickListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			controller.viewStateChanged();
		}
	}

	@Override
	public String getName() {
		return "AST";
	}

	@Override
	public JComponent getComponent() {
		return this;
	}

	@Override
	public Position getPosition() {
		return Position.AST;
	}

	@Override
	public Controller getController() {
		return controller;
	}

	@Override
	public void initComponents(IDE ide) {
		// TODO Auto-generated method stub
	}
}
