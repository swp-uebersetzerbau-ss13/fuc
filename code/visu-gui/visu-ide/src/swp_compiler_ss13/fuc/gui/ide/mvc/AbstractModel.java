package swp_compiler_ss13.fuc.gui.ide.mvc;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.lexer.Token;

/**
 * Abstract Model class. You will need to overwrite the corresponding set method
 * to listen to dataset changes
 * 
 * @author "Frank Zechert"
 * 
 */
public abstract class AbstractModel implements Model {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setSourceCode(String sourceCode) {
		return false;
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setTokens(List<Token> tokens) {
		return false;
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setAST(AST ast) {
		return false;
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setTAC(List<Quadruple> tac) {
		return false;
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setTargetCode(Map<String, InputStream> target) {
		return false;
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setProgramResult(String result) {
		return false;
	};
}