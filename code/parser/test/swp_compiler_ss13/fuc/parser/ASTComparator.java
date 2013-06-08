package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;

import junit.extensions.PA;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.DoWhileNode;
import swp_compiler_ss13.common.ast.nodes.binary.LogicBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.LoopNode;
import swp_compiler_ss13.common.ast.nodes.binary.RelationExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.WhileNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BreakNode;
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
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode;
import swp_compiler_ss13.common.optimization.Liveliness;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.Type;

public class ASTComparator {
	
	/**
	 * Compares the two given {@link AST}s and assert their equality
	 * 
	 * @param expected
	 * @param actual
	 */
	public static void compareAST(AST expected, AST actual) {
		assertNotNull(actual);
		
		// Iterate both trees
		Iterator<ASTNode> actualIt = actual.getDFSLTRIterator();
		Iterator<ASTNode> expectedIt = expected.getDFSLTRIterator();
		while (expectedIt.hasNext()) {
			ASTNode expectedNode = expectedIt.next();
			ASTNode actualNode = actualIt.next();
			
			compare(expectedNode, actualNode);
		}
		
		if (actualIt.hasNext()) {
			fail("Expected AST is finished, but actual AST has still nodes!");
		}
		
		// Success!
	}
	
	private static void compare(ASTNode expected, ASTNode actual) {
			switch (expected.getNodeType()) {
				case BasicIdentifierNode:
					compare((BasicIdentifierNode) expected, (BasicIdentifierNode) actual);
					break;
				case BreakNode:
					compare((BreakNode) expected, (BreakNode) actual);
					break;
				case LiteralNode:
					compare((LiteralNode) expected, (LiteralNode) actual);
					break;
				case ArithmeticUnaryExpressionNode:
					compare((ArithmeticUnaryExpressionNode) expected, (ArithmeticUnaryExpressionNode) actual);
					break;
				case ArrayIdentifierNode:
					compare((ArrayIdentifierNode) expected, (ArrayIdentifierNode) actual);
					break;
				case DeclarationNode:
					compare((DeclarationNode) expected, (DeclarationNode) actual);
					break;
				case LogicUnaryExpressionNode:
					compare((LogicUnaryExpressionNode) expected, (LogicUnaryExpressionNode) actual);
					break;
				case PrintNode:
					compare((PrintNode) expected, (PrintNode) actual);
					break;
				case ReturnNode:
					compare((ReturnNode) expected, (ReturnNode) actual);
					break;
				case StructIdentifierNode:
					compare((StructIdentifierNode) expected, (StructIdentifierNode) actual);
					break;
				case ArithmeticBinaryExpressionNode:
					compare((ArithmeticBinaryExpressionNode) expected, (ArithmeticBinaryExpressionNode) actual);
					break;
				case AssignmentNode:
					compare((AssignmentNode) expected, (AssignmentNode) actual);
					break;
				case DoWhileNode:
					compare((DoWhileNode) expected, (DoWhileNode) actual);
					break;
				case LogicBinaryExpressionNode:
					compare((LogicBinaryExpressionNode) expected, (LogicBinaryExpressionNode) actual);
					break;
				case RelationExpressionNode:
					compare((RelationExpressionNode) expected, (RelationExpressionNode) actual);
					break;
				case WhileNode:
					compare((WhileNode) expected, (WhileNode) actual);
					break;
				case BranchNode:
					compare((BranchNode) expected, (BranchNode) actual);
					break;
				case BlockNode:
					compare((BlockNode) expected, (BlockNode) actual);
					break;
				default:
					throw new IllegalArgumentException("unknown ASTNodeType");
			}
	}
	
	private static void compare(DeclarationNode expected, DeclarationNode actual) {
		assertEquals(expected.getIdentifier(), actual.getIdentifier());
		assertEquals(expected.getType(), actual.getType());
	}
	
	@SuppressWarnings("unchecked")
	private static void compare(BlockNode expected, BlockNode actual) {
		// Compare declarations
//		assertEquals(expected.getDeclarationList(), actual.getDeclarationList());
//		
//		Iterator<DeclarationNode> expectedIt = expected.getDeclarationIterator();
//		Iterator<DeclarationNode> actualIt = actual.getDeclarationIterator();
		// TODO
		
		// Compare
		// TODO
		
		

		SymbolTable expectedTable = expected.getSymbolTable();
		SymbolTable actualTable = actual.getSymbolTable();
		assertEquals(expectedTable.getParentSymbolTable(), actualTable.getParentSymbolTable());
		assertEquals(expectedTable.getRootSymbolTable(), actualTable.getRootSymbolTable());
		
		HashMap<String, Type> expectedSymbolMap = (HashMap<String, Type>) PA.getValue(expectedTable, "symbolMap");
		HashMap<String, Type> actualSymbolMap = (HashMap<String, Type>) PA.getValue(actualTable, "symbolMap");
		assertEquals(expectedSymbolMap, actualSymbolMap);
		HashMap<String, Liveliness> expectedLiveMap = (HashMap<String, Liveliness>) PA.getValue(expectedTable, "liveMap");
		HashMap<String, Liveliness> actualLiveMap = (HashMap<String, Liveliness>) PA.getValue(actualTable, "liveMap");
		assertEquals(expectedLiveMap, actualLiveMap);
		HashMap<String, String> expectedAliasMap = (HashMap<String, String>) PA.getValue(expectedTable, "aliasMap");
		HashMap<String, String> actualAliasMap = (HashMap<String, String>) PA.getValue(actualTable, "aliasMap");
		assertEquals(expectedAliasMap, actualAliasMap);
	}
	
	private static void compare(BranchNode expected, BranchNode actual) {
		compare(expected.getCondition(), actual.getCondition());
		compare(expected.getStatementNodeOnFalse(), actual.getStatementNodeOnFalse());
		compare(expected.getStatementNodeOnTrue(), actual.getStatementNodeOnTrue());
	}
	
	private static void compare(BreakNode expected, BreakNode actual) {
		// Intentionally left blank
	}
	
	private static void compare(AssignmentNode expected, AssignmentNode actual) {
		compare(expected.getLeftValue(), actual.getLeftValue());
		compare(expected.getRightValue(), actual.getRightValue());
	}
	
//	private static void compare(ArithmeticBinaryExpressionNode expected, ArithmeticBinaryExpressionNode actual) {
//		
//	}
	
//	private static void compare(LogicBinaryExpressionNode expected, LogicBinaryExpressionNode actual) {
//		
//	}
	
//	private static void compare(RelationExpressionNode expected, RelationExpressionNode actual) {
//		
//	}
	
	private static void compare(BinaryExpressionNode expected, BinaryExpressionNode actual) {
		compare(expected.getLeftValue(), expected.getLeftValue());
		compare(expected.getRightValue(), expected.getRightValue());
		assertEquals(expected.getOperator(), actual.getOperator());
	}
	
	private static void compare(ArrayIdentifierNode expected, ArrayIdentifierNode actual) {
		compare(expected.getIdentifierNode(), actual.getIdentifierNode());
		assertEquals(expected.getIndex(), actual.getIndex());
	}
	
	private static void compare(BasicIdentifierNode expected, BasicIdentifierNode actual) {
		assertEquals(expected.getIdentifier(), actual.getIdentifier());
	}
	
	private static void compare(StructIdentifierNode expected, StructIdentifierNode actual) {
		compare(expected.getIdentifierNode(), actual.getIdentifierNode());
		assertEquals(expected.getFieldName(), actual.getFieldName());
	}
	
	private static void compare(LiteralNode expected, LiteralNode actual) {
		assertEquals(expected.getLiteral(), actual.getLiteral());
		assertEquals(expected.getLiteralType(), actual.getLiteralType());
	}
	
//	private static void compare(ArithmeticUnaryExpressionNode expected, ArithmeticUnaryExpressionNode actual) {
//		
//	}
//	
//	private static void compare(LogicUnaryExpressionNode expected, LogicUnaryExpressionNode actual) {
//		
//	}
	
	private static void compare(UnaryExpressionNode expected, UnaryExpressionNode actual) {
		assertEquals(expected.getOperator(), actual.getOperator());
		compare(expected.getRightValue(), actual.getRightValue());
	}
	
//	private static void compare(DoWhileNode expected, DoWhileNode actual) {
//		
//	}
//	
//	private static void compare(WhileNode expected, WhileNode actual) {
//		
//	}
	
	private static void compare(LoopNode expected, LoopNode actual) {
		compare(expected.getCondition(), actual.getCondition());
		compare(expected.getLoopBody(), actual.getLoopBody());
	}
	
	private static void compare(PrintNode expected, PrintNode actual) {
		compare(expected.getRightValue(), actual.getRightValue());
	}
	
	private static void compare(ReturnNode expected, ReturnNode actual) {
		compare(expected.getRightValue(), actual.getRightValue());
	}
}
