package swp_compiler_ss13.fuc.ast;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.StructIdentifierNode;

/**
 * StructIdentifierNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class StructIdentifierNodeImpl extends ASTNodeImpl implements
		StructIdentifierNode {

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger(DeclarationNodeImpl.class);

	/**
	 * The identifier name
	 */
	private IdentifierNode identifier;

	/**
	 * the struct field
	 */
	private String field;

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.StructIdentifierNode;
	}

	@Override
	public Integer getNumberOfNodes() {
		if (this.identifier == null) {
			return 1;
		}
		return 1 + this.identifier.getNumberOfNodes();
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new LinkedList<>();
		if (this.identifier != null) {
			children.add(this.identifier);
		}
		return children;
	}

	@Override
	public void setFieldName(String field) {
		if (field == null) {
			logger.error("The given field can not be null");
			throw new IllegalArgumentException(
					"The given field can not be null");
		}
		if (field.length() == 0) {
			logger.error("The given field can not be the empty string");
			throw new IllegalArgumentException(
					"The given field can not be the empty string");
		}
		this.field = field;
	}

	@Override
	public String getFieldName() {
		if (this.field == null) {
			logger.warn("Returning null as field name.");
		}
		return this.field;
	}

	@Override
	public void setIdentifierNode(IdentifierNode node) {
		if (node == null) {
			logger.error("The given node can not be null");
			throw new IllegalArgumentException("The given node can not be null");
		}
		this.identifier = node;
	}

	@Override
	public IdentifierNode getIdentifierNode() {
		if (this.field == null) {
			logger.warn("Returning null as identifier node.");
		}
		return this.identifier;
	}

}
