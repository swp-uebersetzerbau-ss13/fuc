package swp_compiler_ss13.fuc.ast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.lexer.Token;

/**
 * AST Node implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public abstract class ASTNodeImpl implements ASTNode {

	/**
	 * parent node
	 */
	private ASTNode parent;
	
	/**
	 * coverage List
	 */
	private List<Token> coverage = new LinkedList<Token>();

	@Override
	public ASTNode getParentNode() {
		return this.parent;
	}

	@Override
	public void setParentNode(ASTNode node) {
		this.parent = node;
	}

	@Override
	public Iterator<ASTNode> getDFSLTRNodeIterator() {
		final List<ASTNode> children = this.getChildren();
		final Iterator<ASTNode> childIterator = children.iterator();
		return new DFSLTRIterator(childIterator);
	}

	@Override
	public List<Token> coverage() {
		return coverage;
	}
	
	/**
	 * Gets an array of Token and set the coverage.
	 * Only used in parser module, not implemented in 
	 * ast interface.
	 * @param token
	 */
	public void setCoverage(Token... token){
		for(Token toke: token){
			coverage.add(toke);
		}
	}
	
	/**
	 * Gets a list of Token and set the coverage.
	 * Only used in parser module, not implemented in 
	 * ast interface.
	 * @param token
	 */
	public void setCoverage(List<Token> tokenList){
		coverage.addAll(tokenList);
	}
	
	/**
	 * Gets an array of Token and set the coverage in front
	 * of the coverage list.
	 * Only used in parser module, not implemented in 
	 * ast interface.
	 * @param token
	 */
	public void setCoverageAtFront(Token... token){
		for(int i = token.length-1; i >= 0; i--){
			coverage.add(0,token[i]);
		}
	}
	
	
}
