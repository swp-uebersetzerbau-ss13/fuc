package swp_compiler_ss13.fuc.parser.errorHandling;

import java.util.List;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArithmeticUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;

/**
 * @author Manuel
 * 
 * 
 */
public class ParserASTXMLVisualization {


	public StringBuffer visualizeAST(AST ast) {
		StringBuffer out = new StringBuffer();

		out = out.append("<!-- Printing the AST as XML -->\n");
		out = out.append("<AST>\n");
		this.visualize(ast.getRootNode(), "  ", out);
		return out = out.append("</AST>\n");
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
	protected void visualize(ASTNode node, String indent, StringBuffer out) {
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
			// TODO
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
			// TODO
			break;
		case BreakNode:
			// TODO
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
			// nothing to do here
			break;
		case StructIdentifierNode:
			// TODO
			break;
		case WhileNode:
			// TODO
			break;
		}

		out = out.append(indent + "<" + type + attributes + ">");

		if (children.isEmpty()) {
			out = out.append(value);
		} else {
			assert (value == "");

			out = out.append("\n");
			for (ASTNode child : children) {
				this.visualize(child, indent + "  ", out);
			}
			out = out.append(indent);
		}

		out = out.append("</" + type + ">\n");
	}
}
