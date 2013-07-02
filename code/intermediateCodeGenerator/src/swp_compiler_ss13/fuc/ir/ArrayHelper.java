package swp_compiler_ss13.fuc.ir;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArrayIdentifierNode;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.derived.StructType;
import swp_compiler_ss13.common.types.primitive.PrimitiveType;

/**
 * This utility class contains helper methods for arrays.
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class ArrayHelper {

	/**
	 * Check if the given arrayIdentifierNode is used as the target of an
	 * assignment. The target of an assignment is the left hand side value
	 * (assignment: target = source)
	 * 
	 * @param node
	 *            The node to check
	 * @return true if the node is target of an assignment.
	 */
	public static boolean isTargetOfAssignment(ArrayIdentifierNode node) {
		if (node.getParentNode() instanceof AssignmentNode) {
			AssignmentNode parent = (AssignmentNode) node.getParentNode();
			if (parent.getLeftValue() == node) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the base type of the given derived type. The base type of an
	 * ArrayType is the inner most type.
	 * 
	 * @param type
	 *            The type to get the base type for.
	 * @param node
	 *            The node to traverse
	 * @return An array with the type and the node after resolution
	 */
	public static Object[] getInternalBaseType(Type type, ASTNode node) {
		// if an array is used, the inner type is needed
		while (!(type instanceof PrimitiveType)) {
			while (type instanceof ArrayType) {
				type = ((ArrayType) type).getInnerType();
				node = ((ArrayIdentifierNode) node).getIdentifierNode();
			}
			if (type instanceof StructType) {
				Object[] retS = StructHelper.getInternalBaseType(type, node);
				type = (Type) retS[0];
				node = (ASTNode) retS[1];
			}
		}
		return new Object[] { type, node };
	}

	/**
	 * Get the base type of the given derived type. The base type of an
	 * ArrayType is the inner most type.
	 * 
	 * @param type
	 *            The type to get the base type for.
	 * @param node
	 *            The node to traverse
	 * @return An array with the type and the node after resolution
	 */
	public static Type getBaseType(Type type, ASTNode node) {
		node = getOuterArray(node);
		return (Type) ArrayHelper.getInternalBaseType(type, node)[0];
	}

	/**
	 * Get the outer most array identifier node
	 * 
	 * @param node
	 *            the node to use
	 * @return the outer most array identifier node
	 */
	public static ASTNode getOuterArray(ASTNode node) {
		while (node.getParentNode() instanceof ArrayIdentifierNode) {
			node = node.getParentNode();
		}
		return node;
	}

	/**
	 * Check if the given node is the outer most dimension of an array.
	 * 
	 * @param node
	 *            The node to check
	 * @return true if the given node is the outer most dimension of an array.
	 */
	public static boolean isOuterMostDimension(ArrayIdentifierNode node) {
		return !(node.getParentNode() instanceof ArrayIdentifierNode);
	}
}
