package swp_compiler_ss13.fuc.ast.visualization;

import java.io.PrintStream;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.PrintNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.types.Type.Kind;
import swp_compiler_ss13.common.visualization.ASTVisualization;

/**
 * @author Manuel
 * 
 */
public class ASTInfixVisualization implements ASTVisualization {

	/**
	 * Represents the current environment of the node. It changes the output
	 * e.g. nested assignments don't have semicolons
	 */
	protected enum Environment {
		/**
		 * use newlines and semicolons
		 */
		BLOCK, /**
		 * print everything in one line
		 */
		ASSIGNMENT
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visualizeAST(AST ast) {
		PrintStream out = System.out;

		out.println("# Printing the AST in Infix Notation");
		this.visualizeChildren(ast.getRootNode(), "", out, Environment.BLOCK);
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
	 * @param env
	 *            the environment of the current node
	 */
	protected void visualize(ASTNode node, String indent, PrintStream out, Environment env) {
		switch (node.getNodeType()) {
		case RelationExpressionNode:
		case LogicBinaryExpressionNode:
		case ArithmeticBinaryExpressionNode:
			out.print("(");
			this.visualize(((BinaryExpressionNode) node).getLeftValue(), indent + "  ", out, env);
			out.print(this.getBinaryOperatorSign((BinaryExpressionNode) node));
			this.visualize(((BinaryExpressionNode) node).getRightValue(), indent + "  ", out, env);
			out.print(")");
			break;
		case LogicUnaryExpressionNode:
		case ArithmeticUnaryExpressionNode:
			out.print("(");
			out.print(this.getUnaryOperatorSign((UnaryExpressionNode) node));
			this.visualizeChildren(node, indent + "  ", out, env);
			out.print(")");
			break;
		case ArrayIdentifierNode:
			this.visualizeChildren(node, indent + "  ", out, env);
			// FIXME: out.print("[" + ((ArrayIdentifierNode) node).getIndex() +
			// "]");
			break;
		case AssignmentNode:
			if (env == Environment.ASSIGNMENT) {
				out.print("(");
			} else {
				out.print(indent);
			}
			this.visualize(((AssignmentNode) node).getLeftValue(), indent + "  ", out, Environment.ASSIGNMENT);
			out.print(" = ");
			this.visualize(((AssignmentNode) node).getRightValue(), indent + "  ", out, Environment.ASSIGNMENT);
			if (env == Environment.ASSIGNMENT) {
				out.print(")");
			} else {
				out.println(";");
			}
			break;
		case BasicIdentifierNode:
			out.print(((BasicIdentifierNode) node).getIdentifier());
			break;
		case BlockNode:
			assert (env != Environment.ASSIGNMENT);
			out.print(indent);
			out.println("{");
			this.visualizeChildren(node, indent + "  ", out, env);
			out.print(indent);
			out.println("}");
			break;
		case BranchNode:
			out.print(indent);
			out.print("if (");
			this.visualize(((BranchNode) node).getCondition(), indent + "  ", out, Environment.ASSIGNMENT);
			out.println(")");
			this.visualize(((BranchNode) node).getStatementNodeOnTrue(), indent + "  ", out, env);
			if (((BranchNode) node).getStatementNodeOnFalse() != null) {
				out.print(indent);
				out.println("else");
				this.visualize(((BranchNode) node).getStatementNodeOnFalse(), indent + "  ", out, env);
			}
			break;
		case BreakNode:
			out.print(indent);
			out.println("break;");
			break;
		case DeclarationNode:
			assert (env != Environment.ASSIGNMENT);
			out.print(indent);
			out.print(((DeclarationNode) node).getType().toString().replaceFirst("Type($|\\[)", "$1")
					.replaceFirst("String\\[\\d*\\]", "String"));
			out.print(" ");
			out.print(((DeclarationNode) node).getIdentifier().toString());
			out.println(";");
			break;
		case DoWhileNode:
			// TODO
			break;
		case LiteralNode:
			LiteralNode literalNode = (LiteralNode) node;
			String literal = literalNode.getLiteral();
			if (literalNode.getLiteralType().getKind() != Kind.STRING || literal.matches("^\".*\"$")) {
				out.print(literal);
			} else {
				out.print("\"" + literal + "\"");
			}
			break;
		case PrintNode:
			out.print(indent);
			out.print("print ");
			this.visualize(((PrintNode) node).getRightValue(), indent + "  ", out, Environment.ASSIGNMENT);
			out.println(";");
			break;
		case ReturnNode:
			assert (env != Environment.ASSIGNMENT);
			out.print(indent);
			out.print("return ");
			this.visualizeChildren(node, indent + "  ", out, env);
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
	 * @param env
	 *            the environment of the current node
	 */
	protected void visualizeChildren(ASTNode node, String indent, PrintStream out, Environment env) {
		for (ASTNode child : node.getChildren()) {
			this.visualize(child, indent, out, env);
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
	protected String getBinaryOperatorSign(BinaryExpressionNode node) {
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
	protected String getUnaryOperatorSign(UnaryExpressionNode node) {
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
