package swp_compiler_ss13.fuc.ast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;

/**
 * BasicIdentifierNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class BasicIdentifierNodeImpl extends ASTNodeImpl implements BasicIdentifierNode {

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger(DeclarationNodeImpl.class);

	/**
	 * The identifier name
	 */
	private String identifier;

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.BasicIdentifierNode;
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
			logger.warn("Returning null as the identifier!");
		} else if (this.identifier.length() == 0) {
			logger.warn("Returning an empty string as the identifier");
		}
		return this.identifier;
	}

	@Override
	public void setIdentifier(String identifier) {
		if (identifier == null) {
			logger.error("The argument identifier can not be null!");
			throw new IllegalArgumentException("The argument identifier can not be null!");
		}
		if (identifier.length() == 0) {
			logger.warn("The given identifier is an empty string!");
		}
		this.identifier = identifier;
	}

}
