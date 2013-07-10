package swp_compiler_ss13.fuc.ast;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.PrintNode;

/**
 * PrintNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class PrintNodeImpl extends ASTNodeImpl implements PrintNode {

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger(PrintNodeImpl.class);

	/**
	 * the identifier node
	 */
	private IdentifierNode node;

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.PrintNode;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new LinkedList<>();
		if (this.node != null) {
			children.add(this.node);
		}
		return children;
	}

	@Override
	public void setRightValue(IdentifierNode identifier) {
		if (identifier == null) {
			logger.error("The argument identifier can not be null!");
			throw new IllegalArgumentException("The argument identifier can not be null!");
		}
		
		identifier.setParentNode(this);
		
		this.node = identifier;
	}

	@Override
	public IdentifierNode getRightValue() {
		if (this.node == null) {
			logger.warn("returning null as the right value for the print statement");
		}
		return this.node;
	}

}
