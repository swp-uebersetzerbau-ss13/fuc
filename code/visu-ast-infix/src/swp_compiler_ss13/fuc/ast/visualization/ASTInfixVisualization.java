package swp_compiler_ss13.fuc.ast.visualization;

import java.io.PrintStream;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArithmeticUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.visualization.ASTVisualization;

/**
 * @author Manuel
 * 
 */
public class ASTInfixVisualization implements ASTVisualization {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visualizeAST(AST ast) {
		PrintStream out = System.out;

		out.println("# Printing the AST in Infix Notation");
		this.visualize(ast.getRootNode(), "", out);
	}

	/**
	 * Recursively visualizes all Nodes
	 * 
	 * @param node
	 *            The current node to be displayed
	 * @param indent
	 *            A String of spaces for the current indentation level
	 * @param out
	 *            The stream to which the output gets written
	 */
	protected void visualize(ASTNode node, String indent, PrintStream out) {
		switch (node.getNodeType()) {
		case ArithmeticBinaryExpressionNode:
			out.print("(");
			this.visualize(((ArithmeticBinaryExpressionNode) node).getLeftValue(), indent + "  ", out);
			out.print(this.getBinaryOperatorSign((ArithmeticBinaryExpressionNode) node));
			this.visualize(((ArithmeticBinaryExpressionNode) node).getRightValue(), indent + "  ", out);
			out.print(")");
			break;
		case ArithmeticUnaryExpressionNode:
			out.print("(");
			out.print(this.getUnaryOperatorSign((ArithmeticUnaryExpressionNode) node));
			this.visualizeChildren(node, indent + "  ", out);
			out.print(")");
			break;
		case ArrayIdentifierNode:
			// TODO
			break;
		case AssignmentNode:
			out.print(indent);
			this.visualize(((AssignmentNode) node).getLeftValue(), indent + "  ", out);
			out.print(" = ");
			this.visualize(((AssignmentNode) node).getRightValue(), indent + "  ", out);
			out.println(";");
			break;
		case BasicIdentifierNode:
			out.print(((BasicIdentifierNode) node).getIdentifier());
			break;
		case BlockNode:
			out.print(indent);
			out.println("{");
			this.visualizeChildren(node, indent + "  ", out);
			out.print(indent);
			out.println("}");
			break;
		case BranchNode:
			// TODO
			break;
		case BreakNode:
			// TODO
			break;
		case DeclarationNode:
			out.print(indent);
			out.print(((DeclarationNode) node).getType().toString().replaceFirst("Type$", ""));
			out.print(" ");
			out.print(((DeclarationNode) node).getIdentifier().toString());
			out.println(";");
			break;
		case DoWhileNode:
			// TODO
			break;
		case LiteralNode:
			out.print(((LiteralNode) node).getLiteral());
			break;
		case LogicBinaryExpressionNode:
			// TODO
			break;
		case LogicUnaryExpressionNode:
			// TODO
			break;
		case PrintNode:
			// TODO
			break;
		case RelationExpressionNode:
			// TODO
			break;
		case ReturnNode:
			out.print(indent);
			out.print("return ");
			this.visualizeChildren(node, indent + "  ", out);
			out.println(";");
			break;
		case StructIdentifierNode:
			// TODO
			break;
		case WhileNode:
			// TODO
			break;
		}
	}

	/**
	 * Recursively prints all Child Nodes
	 * 
	 * @param node
	 *            The node whose children should be visualized
	 * @param indent
	 *            A String of spaces for the current indentation level
	 * @param out
	 *            The stream to which the output gets written
	 */
	protected void visualizeChildren(ASTNode node, String indent, PrintStream out) {
		for (ASTNode child : node.getChildren()) {
			this.visualize(child, indent, out);
		}
	}

	/**
	 * Convert the operator of the given node into a string that represents the
	 * operator.
	 * 
	 * @param node
	 *            The node from which to get the operator
	 * @return a string representing the operator
	 */
	protected String getBinaryOperatorSign(ArithmeticBinaryExpressionNode node) {
		String operator;
		BinaryOperator binaryOperator = node.getOperator();

		switch (binaryOperator) {
		case ADDITION:
			operator = "+";
			break;
		case DIVISION:
			operator = "/";
			break;
		case EQUAL:
			operator = "==";
			break;
		case GREATERTHAN:
			operator = ">";
			break;
		case GREATERTHANEQUAL:
			operator = ">=";
			break;
		case INEQUAL:
			operator = "!=";
			break;
		case LESSTHAN:
			operator = "<";
			break;
		case LESSTHANEQUAL:
			operator = "<=";
			break;
		case LOGICAL_AND:
			operator = "&&";
			break;
		case LOGICAL_OR:
			operator = "||";
			break;
		case MULTIPLICATION:
			operator = "*";
			break;
		case SUBSTRACTION:
			operator = "-";
			break;
		default:
			operator = "[invalid ArithmeticBinaryExpressionNode operator: " + binaryOperator.toString() + "]";
			break;
		}

		return operator;
	}

	/**
	 * Convert the operator of the given node into a string that represents the
	 * operator.
	 * 
	 * @param node
	 *            The node from which to get the operator
	 * @return a string representing the operator
	 */
	protected String getUnaryOperatorSign(ArithmeticUnaryExpressionNode node) {
		String operator;
		UnaryOperator unaryOperator = node.getOperator();

		switch (unaryOperator) {
		case LOGICAL_NEGATE:
			operator = "!";
			break;
		case MINUS:
			operator = "-";
			break;
		default:
			operator = "[invalid ArithmeticUnaryExpressionNode operator: " + unaryOperator.toString() + "]";
			break;
		}

		return operator;
	}
}
