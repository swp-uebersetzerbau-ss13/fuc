package swp_compiler_ss13.fuc.gui.ide.mvc;

/**
 * The position within the logical order of the compiler tool chain
 * 
 * @author "Frank Zechert"
 * 
 */
public enum Position {
	/**
	 * Before all components
	 */
	FIRST,
	/**
	 * The source code
	 */
	SOURCE_CODE,
	/**
	 * The lexer component
	 */
	LEXER,
	/**
	 * The token stream
	 */
	TOKENS,
	/**
	 * The parser component
	 */
	PARSER,
	/**
	 * The AST
	 */
	AST,
	/**
	 * The semantic analyzer copmonent
	 */
	SEMANTIC_ANALYZER,
	/**
	 * The checked AST
	 */
	CHECKED_AST,
	/**
	 * The intermediate code generator component
	 */
	INTERMEDIATE_CODE_GENERATOR,
	/**
	 * The three address code
	 */
	TAC,
	/**
	 * The backend
	 */
	BACKEND,
	/**
	 * The target code
	 */
	TARGET_CODE,
	/**
	 * After all components
	 */
	LAST
}
