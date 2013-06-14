package swp_compiler_ss13.fuc.ast.visualization;

import java.io.PrintStream;
import java.util.List;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.LogicBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.RelationExpressionNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArithmeticUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArrayIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.LogicUnaryExpressionNode;
import swp_compiler_ss13.common.visualization.ASTVisualization;

/**
 * @author Manuel
 * 
 */
public class ASTXMLVisualization implements ASTVisualization {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visualizeAST(AST ast) {
		PrintStream out = System.out;

		out.println("<!-- Printing the AST as XML -->");
		out.println("<AST>");
		this.visualize(ast.getRootNode(), "  ", out);
		out.println("</AST>");
	}

	/**
	 * Recursively prints all Nodes
	 * 
	 * @param node
	 *            The current node to be displayed
	 * @param indent
	 *            A String of spaces for the current indentation level
	 * @param out
	 *            the stream in which the XML gets written
	 */
	protected void visualize(ASTNode node, String indent, PrintStream out) {
		String attributes = "";
		String value = "";

		ASTNodeType nodeType = node.getNodeType();
		String type = nodeType.toString().replaceFirst("Node$", "");
		List<ASTNode> children = node.getChildren();

		switch (nodeType) {
		case ArithmeticBinaryExpressionNode:
			attributes = " operator=\"" + ((ArithmeticBinaryExpressionNode) node).getOperator().toString() + "\"";
			break;
		case ArithmeticUnaryExpressionNode:
			attributes = " operator=\"" + ((ArithmeticUnaryExpressionNode) node).getOperator().toString() + "\"";
			break;
		case ArrayIdentifierNode:
			attributes = " index=\"" + ((ArrayIdentifierNode) node).getIndex().toString() + "\"";
			break;
		case AssignmentNode:
			// nothing to do here
			break;
		case BasicIdentifierNode:
			value = ((BasicIdentifierNode) node).getIdentifier();
			break;
		case BlockNode:
			// nothing to do here
			break;
		case BranchNode:
			// nothing to do here
			break;
		case BreakNode:
			// nothing to do here
			break;
		case DeclarationNode:
			attributes = " identifier=\"" + ((DeclarationNode) node).getIdentifier().toString() + "\"";
			value = ((DeclarationNode) node).getType().toString().replaceFirst("Type($|\\[)", "$1");
			break;
		case DoWhileNode:
			// TODO
			break;
		case LiteralNode:
			value = ((LiteralNode) node).getLiteral();
			break;
		case LogicBinaryExpressionNode:
			attributes = " operator=\""
					+ ((LogicBinaryExpressionNode) node).getOperator().toString().replaceFirst("LOGICAL_", "") + "\"";
			break;
		case LogicUnaryExpressionNode:
			attributes = " operator=\""
					+ ((LogicUnaryExpressionNode) node).getOperator().toString().replaceFirst("LOGICAL_", "") + "\"";
			break;
		case PrintNode:
			// nothing to do here
			break;
		case RelationExpressionNode:
			attributes = " operator=\"" + ((RelationExpressionNode) node).getOperator().toString() + "\"";
			break;
		case ReturnNode:
			// nothing to do here
			break;
		case StructIdentifierNode:
			// TODO
			break;
		case WhileNode:
			// TODO
			break;
		}

		out.print(indent + "<" + type + attributes + ">");

		if (children.isEmpty()) {
			out.print(value);
		} else {
			assert (value == "");

			out.println();
			for (ASTNode child : children) {
				this.visualize(child, indent + "  ", out);
			}
			out.print(indent);
		}

		out.println("</" + type + ">");
	}
}
