package swp_compiler_ss13.fuc.gui.ast;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArrayIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.StructIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode;

public class NodeComponent {

	private static final Logger LOG = Logger.getLogger(NodeComponent.class);
	private static final Map<ASTNodeType, ImageIcon> IMAGE_TYPE_MAP = new HashMap<>();
	private static final Map<BinaryOperator, ImageIcon> IMAGE_OPERATOR_MAP = new HashMap<>();
	private static final String dir = "/swp_compiler_ss13/fuc/gui/ast/assets/";
	private static final int LOGO_WIDTH = 30;
	private static final int LOGO_HEIGHT = 30;

	private final JComponent component;
	private final JComponent iconComponent;
	private final JComponent nameComponent;
	private final JComponent infoComponent;

	private boolean large = true;

	public NodeComponent(ASTNode node) {
		if (node.getChildren().isEmpty()) {
			this.component = new JPanel();
		} else {
			this.component = new JButton();
		}
		this.getComponent().setLayout(new BorderLayout(3, 3));
		if (IMAGE_TYPE_MAP.isEmpty()) {
			IMAGE_TYPE_MAP.put(null, this.getIconFromString(dir + "default.png"));
			IMAGE_OPERATOR_MAP.put(null, this.getIconFromString(dir + "default.png"));
		}
		this.iconComponent = new JLabel(this.getIcon(node));
		this.getComponent().add(this.iconComponent, BorderLayout.WEST);
		this.nameComponent = new JLabel(node.getNodeType().name());
		this.getComponent().add(this.nameComponent, BorderLayout.NORTH);
		this.infoComponent = this.getInfoComponent(node);
		this.getComponent().add(this.infoComponent, BorderLayout.CENTER);
	}

	void toggleSize() {
		this.large = !this.large;
		this.nameComponent.setVisible(this.large);
		this.infoComponent.setVisible(this.large);
	}

	private JComponent getInfoComponent(ASTNode node) {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
		switch (node.getNodeType()) {
		case LiteralNode:
			LiteralNode literalNode = (LiteralNode) node;
			result.add(new JLabel("type: " + literalNode.getLiteralType()));
			result.add(new JLabel("name: " + literalNode.getLiteral()));
			break;
		case ArithmeticBinaryExpressionNode:
		case LogicBinaryExpressionNode:
		case RelationExpressionNode:
			BinaryExpressionNode binaryNode = (BinaryExpressionNode) node;
			result.add(new JLabel("operator: " + binaryNode.getOperator()));
			break;
		case ArithmeticUnaryExpressionNode:
		case LogicUnaryExpressionNode:
			UnaryExpressionNode unaryNode = (UnaryExpressionNode) node;
			result.add(new JLabel("operator: " + unaryNode.getOperator()));
			break;
		case ArrayIdentifierNode:
			ArrayIdentifierNode arrayNode = (ArrayIdentifierNode) node;
			//result.add(new JLabel("index: " + arrayNode.getIndex()));
			break;
		case BasicIdentifierNode:
			BasicIdentifierNode basicNode = (BasicIdentifierNode) node;
			result.add(new JLabel("variable: " + basicNode.getIdentifier()));
			break;
		case BlockNode:
			BlockNode blockNode = (BlockNode) node;
			result.add(new JLabel("amount of declarations: " + blockNode.getNumberOfDeclarations()));
			result.add(new JLabel("amount of statements: " + blockNode.getNumberOfStatements()));
			break;
		case DeclarationNode:
			DeclarationNode declNode = (DeclarationNode) node;
			result.add(new JLabel("type: " + declNode.getType()));
			result.add(new JLabel("name: " + declNode.getIdentifier()));
			break;
		case StructIdentifierNode:
			StructIdentifierNode structNode = (StructIdentifierNode) node;
			result.add(new JLabel("field: " + structNode.getFieldName()));
		case AssignmentNode:
		case BranchNode:
		case BreakNode:
		case DoWhileNode:
		case PrintNode:
		case WhileNode:
		default:
			break;
		}
		return result;
	}

	private Icon getIcon(ASTNode node) {
		ImageIcon imageIcon = IMAGE_TYPE_MAP.get(node.getNodeType());
		if (imageIcon != null) {
			return imageIcon;
		}
		String imageName = null;
		switch (node.getNodeType()) {
		case ArithmeticBinaryExpressionNode:
		case LogicBinaryExpressionNode:
		case RelationExpressionNode:
			BinaryOperator operator = ((BinaryExpressionNode) node).getOperator();
			imageIcon = IMAGE_OPERATOR_MAP.get(operator);
			if (imageIcon != null) {
				return imageIcon;
			}
			switch (operator) {
			case ADDITION:
				imageName = "add";
				break;
			case SUBSTRACTION:
				imageName = "minus";
				break;
			case MULTIPLICATION:
				imageName = "mult";
				break;
			case DIVISION:
				imageName = "div";
				break;
			case EQUAL:
				imageName = "equal";
				break;
			case GREATERTHAN:
				imageName = "greater";
				break;
			case GREATERTHANEQUAL:
				imageName = "greater_equal";
				break;
			case INEQUAL:
				imageName = "inequal";
				break;
			case LESSTHAN:
				imageName = "less";
				break;
			case LESSTHANEQUAL:
				imageName = "less_equal";
				break;
			case LOGICAL_AND:
				imageName = "ampersand";
				break;
			case LOGICAL_OR:
				imageName = "or";
				break;
			default:
				return IMAGE_OPERATOR_MAP.get(null);
			}
			imageIcon = this.getIconFromString(dir + imageName + ".png");
			IMAGE_OPERATOR_MAP.put(operator, imageIcon);
			return imageIcon;
		case LogicUnaryExpressionNode:
			imageName = "not";
			break;
		case ArithmeticUnaryExpressionNode:
			imageName = "minus";
			break;
		case BlockNode:
			imageName = "braces";
			break;
		case AssignmentNode:
			imageName = "assign";
			break;
		case ArrayIdentifierNode:
			imageName = "array";
			break;
		case BasicIdentifierNode:
			imageName = "basicIdentifier";
			break;
		case BranchNode:
			imageName = "if_else";
			break;
		case DeclarationNode:
			imageName = "decl";
			break;
		case DoWhileNode:
		case WhileNode:
			imageName = "loop";
			break;
		case StructIdentifierNode:
			imageName = "struct";
			break;
		case PrintNode:
			imageName = "print";
			break;
		case BreakNode:
			imageName = "break";
			break;
		case ReturnNode:
			imageName = "return";
			break;
		case LiteralNode:
			imageName = "literal";
			break;
		default:
			return IMAGE_TYPE_MAP.get(null);
		}
		imageIcon = this.getIconFromString(dir + imageName + ".png");
		IMAGE_TYPE_MAP.put(node.getNodeType(), imageIcon);
		return imageIcon;
	}

	private ImageIcon getIconFromString(String resource) {
		Image image = Toolkit.getDefaultToolkit().getImage(
				NodeComponent.class.getResource(resource));
		if (image == null) {
			LOG.warn("didn't found image: " + resource);
			return null;
		}
		return new ImageIcon(image.getScaledInstance(LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH));
	}

	public JComponent getComponent() {
		return this.component;
	}

}
