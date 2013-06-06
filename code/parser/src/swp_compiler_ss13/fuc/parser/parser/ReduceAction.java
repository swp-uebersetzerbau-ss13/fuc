package swp_compiler_ss13.fuc.parser.parser;

public interface ReduceAction {
	
	/**
	 * Reduce the given Objects in Array of ASTnodes and Tokens to a
	 * new ASTNode and return this
	 * @param objs
	 * @return
	 * @throws ParserException
	 */
	Object create(Object... objs) throws ParserException;
	
}
