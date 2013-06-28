package swp_compiler_ss13.fuc.ast;

import java.util.Iterator;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.DoWhileNode;
import swp_compiler_ss13.common.ast.nodes.binary.LogicBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.RelationExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.WhileNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArithmeticUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArrayIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.LogicUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.PrintNode;
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
import swp_compiler_ss13.common.ast.nodes.unary.StructIdentifierNode;
import swp_compiler_ss13.common.parser.SymbolTable;

/**
 * Implementation of the AST interface
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 * 
 */
public class ASTImpl implements AST {

	/**
	 * The root node of the AST
	 */
	private BlockNode rootNode;

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger(ASTImpl.class);

	/**
	 * Create a new empty AST.
	 */
	public ASTImpl() {
		logger.debug("Creating a new empty AST");
		this.rootNode = new BlockNodeImpl();
	}

	/**
	 * Create a new AST with the given root node.
	 * 
	 * @param rootNode
	 *            The root node of the ast.
	 */
	public ASTImpl(BlockNode rootNode) {
		logger.debug(String.format("Creating AST with root node: %s", rootNode));
		if (rootNode == null) {
			logger.error("The parameter rootNode can not be null!");
			throw new IllegalArgumentException("The parameter rootNode can not be null!");
		}
		this.rootNode = rootNode;
	}

	@Override
	public BlockNode getRootNode() {
		return this.rootNode;
	}

	@Override
	public void setRootNode(BlockNode rootNode) {
		if (rootNode == null) {
			logger.error("The parameter rootNode can not be null!");
			throw new IllegalArgumentException("The parameter rootNode can not be null!");
		}
		this.rootNode = rootNode;
	}

	@Override
	public Integer getNumberOfNodes() {
		logger.debug(String.format("Numer of nodes in AST: %d", this.rootNode.getNumberOfNodes()));
		return this.rootNode.getNumberOfNodes();
	}

	@Override
	public Iterator<ASTNode> getDFSLTRIterator() {
		return new Iterator<ASTNode>() {
			private boolean outputRoot = false;
			private Iterator<ASTNode> innerIt;

			@Override
			public boolean hasNext() {
				if (!this.outputRoot) {
					return true;
				}
				if (this.innerIt == null) {
					this.innerIt = ASTImpl.this.getRootNode().getDFSLTRNodeIterator();
				}
				return this.innerIt.hasNext();
			}

			@Override
			public ASTNode next() {
				if (!this.outputRoot) {
					this.outputRoot = true;
					return ASTImpl.this.rootNode;
				}
				return this.innerIt.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public SymbolTable getRootSymbolTable() {
		return this.rootNode.getSymbolTable();
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		String indentation = "";
		toString(b, indentation, this.rootNode);
		return b.toString();
	}

	public static void toString(StringBuilder b, String indentation, ASTNode node) {
		b.append(indentation);
		switch (node.getNodeType()) {
		case ArithmeticBinaryExpressionNode:
			b.append("ArithmeticBinaryExpression(");
			toString(b, "", ((ArithmeticBinaryExpressionNode) node).getLeftValue());
			b.append(" ").append(((ArithmeticBinaryExpressionNode) node).getOperator().toString()).append(" ");
			toString(b, "", ((ArithmeticBinaryExpressionNode) node).getRightValue());
			b.append(")");
			break;
		case ArithmeticUnaryExpressionNode:
			b.append("ArithmeticUnaryExpressionNode(");
			b.append(" ").append(((ArithmeticUnaryExpressionNode) node).getOperator().toString()).append(" ");
			toString(b, "", ((ArithmeticUnaryExpressionNode) node).getRightValue());
			b.append(")");
			break;
		case ArrayIdentifierNode:
			b.append("ArrayIdentifierNode(");
			toString(b, "", ((ArrayIdentifierNode) node).getIdentifierNode());
			b.append(" [");
			toString(b, "", ((ArrayIdentifierNode) node).getIndexNode());
			b.append("])");
			break;
		case AssignmentNode:
			b.append("AssignmentNode(");
			toString(b, "", ((AssignmentNode) node).getLeftValue());
			b.append(" = ");
			toString(b, "", ((AssignmentNode) node).getRightValue());
			b.append(")\n");
			break;
		case BasicIdentifierNode:
			b.append("BasicIdentifierNode(");
			b.append(((BasicIdentifierNode) node).getIdentifier());
			b.append(")");
			break;
		case BlockNode:
			b.append("{\n");
			String newindentation = indentation + "\t";
			for (DeclarationNode d : ((BlockNode) node).getDeclarationList()) {
				toString(b, newindentation, d);
			}
			for (StatementNode s : ((BlockNode) node).getStatementList()) {
				toString(b, newindentation, s);
			}
			b.append(indentation).append("}");
			b.append("\n");
			break;
		case BranchNode:
			b.append("BranchNode(if(");
			toString(b, "", (((BranchNode) node).getCondition()));
			b.append(")\n");
			toString(b, indentation + "\t", (((BranchNode) node).getStatementNodeOnTrue()));
			if (((BranchNode) node).getStatementNodeOnFalse() != null) {
				b.append(indentation + "\nelse\n");
				toString(b, indentation + "\t", (((BranchNode) node).getStatementNodeOnFalse()));
			}
			b.append(indentation).append(")\n");
			break;
		case BreakNode:
			b.append("BreakNode\n");
			break;
		case DeclarationNode:
			b.append("DeclarationNode(");
			b.append(((DeclarationNode) node).getType().toString()).append(" ");
			b.append(((DeclarationNode) node).getIdentifier());
			b.append(")\n");
			break;
		case DoWhileNode:
			b.append("DoWhileNode(do(\n");
			toString(b, indentation + "\t", (((DoWhileNode) node).getLoopBody()));
			b.append(indentation).append(")\n");
			b.append(indentation).append("while(");
			toString(b, "", (((DoWhileNode) node).getCondition()));
			b.append(")\n");
			break;
		case LiteralNode:
			b.append("LiteralNode(");
			b.append(((LiteralNode) node).getLiteralType().toString());
			b.append(" ");
			b.append(((LiteralNode) node).getLiteral());
			b.append(")");
			break;
		case LogicBinaryExpressionNode:
			b.append("LogicBinaryExpressionNode(");
			toString(b, "", ((LogicBinaryExpressionNode) node).getLeftValue());
			b.append(" ").append(((LogicBinaryExpressionNode) node).getOperator().toString()).append(" ");
			toString(b, "", ((LogicBinaryExpressionNode) node).getRightValue());
			b.append(")");
			break;
		case LogicUnaryExpressionNode:
			b.append("LogicUnaryExpressionNode(");
			b.append(" ").append(((LogicUnaryExpressionNode) node).getOperator().toString()).append(" ");
			toString(b, "", ((LogicUnaryExpressionNode) node).getRightValue());
			b.append(")");
			break;
		case PrintNode:
			b.append("PrintNode(");
			toString(b, "", ((PrintNode) node).getRightValue());
			b.append(")\n");
			break;
		case RelationExpressionNode:
			b.append("RelationExpressionNode(");
			toString(b, "", ((RelationExpressionNode) node).getLeftValue());
			b.append(" ").append(((RelationExpressionNode) node).getOperator().toString()).append(" ");
			toString(b, "", ((RelationExpressionNode) node).getRightValue());
			b.append(")");
			break;
		case ReturnNode:
			b.append("ReturnNode(");
			toString(b, "", ((ReturnNode) node).getRightValue());
			b.append(")\n");
			break;
		case StructIdentifierNode:
			b.append("StructIdentifierNode(");
			toString(b, "", ((StructIdentifierNode) node).getIdentifierNode());
			b.append(".").append(((StructIdentifierNode) node).getFieldName()).append(")");
			break;
		case WhileNode:
			b.append("WhileNode(\n");
			b.append(indentation).append("\t").append("while(");
			toString(b, "", (((WhileNode) node).getCondition()));
			b.append(")\n");
			toString(b, indentation + "\t", (((WhileNode) node).getLoopBody()));
			b.append(indentation).append(")\n");
			break;
		default:
			break;
		}
	}
}
