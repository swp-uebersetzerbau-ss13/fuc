package swp_compiler_ss13.fuc.gui.ide.mvc;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.lexer.Token;

/**
 * Model interface for the MVC pattern
 * 
 * @author "Frank Zechert"
 * 
 */
public interface Model {
	/**
	 * Get the controller associated with this model
	 * 
	 * @return The controller
	 */
	public Controller getController();

	/**
	 * Set the source code
	 * 
	 * @param sourceCode
	 *            the source code
	 * @return return true if the new value is used. true will trigger a
	 *         notifyModelChanged on the controller. return false otherwise.
	 */
	public boolean setSourceCode(String sourceCode);

	/**
	 * Set the token list
	 * 
	 * @param tokens
	 *            the tokens
	 * @return return true if the new value is used. true will trigger a
	 *         notifyModelChanged on the controller. return false otherwise.
	 */
	public boolean setTokens(List<Token> tokens);

	/**
	 * Set the ast
	 * 
	 * @param ast
	 *            the ast
	 * @return return true if the new value is used. true will trigger a
	 *         notifyModelChanged on the controller. return false otherwise.
	 */
	public boolean setAST(AST ast);

	/**
	 * Set the tac
	 * 
	 * @param tac
	 *            the tac
	 * @return return true if the new value is used. true will trigger a
	 *         notifyModelChanged on the controller. return false otherwise.
	 */
	public boolean setTAC(List<Quadruple> tac);

	/**
	 * Set the target code
	 * 
	 * @param target
	 *            the target code
	 * @return return true if the new value is used. true will trigger a
	 *         notifyModelChanged on the controller. return false otherwise.
	 */
	public boolean setTargetCode(Map<String, InputStream> target);
}
