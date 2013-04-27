package swp_compiler_ss13.fuc.ast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.types.Type;

/**
 * PrintNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class LiteralNodeImpl extends ASTNodeImpl implements LiteralNode {

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger(LiteralNodeImpl.class);

	/**
	 * left node
	 */
	private String literal;

	/**
	 * right node
	 */
	private Type literalType;

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.LiteralNode;
	}

	@Override
	public Integer getNumberOfNodes() {
		return 1;
	}

	@Override
	public Iterator<ASTNode> getDFSLTRNodeIterator() {
		return new EmptyIterator();
	}

	@Override
	public List<ASTNode> getChildren() {
		return new LinkedList<>();
	}

	@Override
	public String getLiteral() {
		if (this.literal == null) {
			logger.warn("Returning null as the literal!");
		}
		if (this.literal.length() == 0) {
			logger.warn("Returning an empty string as the literal");
		}
		return this.literal;
	}

	@Override
	public Type getLiteralType() {
		if (this.literalType == null) {
			logger.warn("Returning null as the type of the literal!");
		}
		return this.literalType;
	}

	@Override
	public void setLiteral(String literal) {
		if (literal == null) {
			logger.error("The argument literal can not be null!");
			throw new IllegalArgumentException("The argument literal can not be null!");
		}
		if (literal.length() == 0) {
			logger.warn("The given literal is an empty string!");
		}
		this.literal = literal;
	}

	@Override
	public void setLiteralType(Type literalType) {
		if (literalType == null) {
			logger.error("The argument literalType can not be null!");
			throw new IllegalArgumentException("The argument literalType can not be null!");
		}
		this.literalType = literalType;
	}

}
