package swp_compiler_ss13.fuc.ir;

import java.util.Stack;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.unary.StructIdentifierNode;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.derived.Member;
import swp_compiler_ss13.common.types.derived.StructType;
import swp_compiler_ss13.common.types.primitive.PrimitiveType;

/**
 * This utility class contains helper methods for structs.
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class StructHelper {

	/**
	 * Check if the given structIdentifierNode is used as the target of an
	 * assignment. The target of an assignment is the left hand side value
	 * (assignment: target = source)
	 * 
	 * @param node
	 *            The node to check
	 * @return true if the node is target of an assignment.
	 */
	public static boolean isTargetOfAssignment(StructIdentifierNode node) {
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
	 *            The node
	 * @return An array with the type and the node
	 */
	public static Object[] getInternalBaseType(Type type, ASTNode node) {
		Stack<String> fieldAccess = new Stack<String>();
		while (!(type instanceof PrimitiveType)) {
			if (!(node instanceof StructIdentifierNode)) {
				while (!fieldAccess.empty()) {
					String field = fieldAccess.pop();
					Member[] members = ((StructType) type).members();
					for (Member member : members) {
						if (member.getName().equals(field)) {
							type = member.getType();
						}
					}
				}
			}
			if (type instanceof ArrayType) {
				Object[] retA = ArrayHelper.getInternalBaseType(type, node);
				type = (Type) retA[0];
				node = (ASTNode) retA[1];
			}
			if (type instanceof StructType) {
				if (node instanceof StructIdentifierNode) {
					String field = ((StructIdentifierNode) node).getFieldName();
					node = ((StructIdentifierNode) node).getIdentifierNode();
					fieldAccess.push(field);
				}
				else {
					break;
				}
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
	 *            The node
	 * @return The base type
	 */
	public static Type getBaseType(Type type, ASTNode node) {
		node = getOuterStruct(node);
		return (Type) getInternalBaseType(type, node)[0];
	}

	/**
	 * Get the outer most struct identifier node
	 * 
	 * @param node
	 *            the node to use
	 * @return the outer most struct identifier node
	 */
	public static ASTNode getOuterStruct(ASTNode node) {
		while (node.getParentNode() instanceof StructIdentifierNode) {
			node = node.getParentNode();
		}
		return node;
	}

	/**
	 * Check if the given node is the outer most dimension of a struct.
	 * 
	 * @param node
	 *            The node to check
	 * @return true if the given node is the outer most dimension of a struct.
	 */
	public static boolean isOuterMostDimension(StructIdentifierNode node) {
		return !(node.getParentNode() instanceof StructIdentifierNode);
	}

	/**
	 * Check if the ArrayType has a field named index
	 * 
	 * @param type
	 *            the type to check
	 * @param index
	 *            the fieldname of the struct
	 * @return true if the field exists
	 */
	public static boolean fieldExists(StructType type, String index) {
		Member[] members = type.members();
		for (Member member : members) {
			if (member.getName().equals(index)) {
				return true;
			}
		}
		return false;
	}
}
