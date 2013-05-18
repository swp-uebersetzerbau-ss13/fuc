package swp_compiler_ss13.fuc.ast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.types.Type;

/**
 * DeclarationNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class DeclarationNodeImpl extends ASTNodeImpl implements DeclarationNode {

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger(DeclarationNodeImpl.class);

	/**
	 * The declared identifier
	 */
	private String identifier;

	/**
	 * Type of the identifier
	 */
	private Type type;

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
	public String getIdentifier() {
		if (this.identifier == null) {
			logger.warn("Returning null as the name of the identifier!");
		} else if (this.identifier.length() == 0) {
			logger.warn("Returning an empty string as the name of the identifier!");
		}
		return this.identifier;
	}

	@Override
	public Type getType() {
		if (this.type == null) {
			logger.warn("Returning null as the type of the identifier!");
		}
		return this.type;
	}

	@Override
	public void setIdentifier(String identifier) {
		if (identifier == null) {
			logger.error("The argument identifer can not be null!");
			throw new IllegalArgumentException("The argument identifier can not be null!");
		}
		if (identifier.length() == 0) {
			logger.warn("The given identifier is an empty string!");
		}
		this.identifier = identifier;
	}

	@Override
	public void setType(Type type) {
		if (type == null) {
			logger.error("The argument type can not be null!");
			throw new IllegalArgumentException("The argument type can not be null!");
		}
		this.type = type;
	}

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.DeclarationNode;
	}

}
