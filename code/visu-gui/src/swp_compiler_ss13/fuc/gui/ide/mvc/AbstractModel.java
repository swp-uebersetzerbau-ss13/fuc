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
	public void setSourceCode(String sourceCode) {
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTokens(List<Token> tokens) {
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAST(AST ast) {
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTAC(List<Quadruple> tac) {
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTargetCode(Map<String, InputStream> target) {
	};
}