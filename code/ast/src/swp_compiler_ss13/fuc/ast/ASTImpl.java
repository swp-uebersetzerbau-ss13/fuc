package swp_compiler_ss13.fuc.ast;

import java.util.Iterator;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
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
		return this.rootNode.getDFSLTRNodeIterator();
	}

	@Override
	public SymbolTable getRootSymbolTable() {
		return this.rootNode.getSymbolTable();
	}

}
