package swp_compiler_ss13.fuc.lexer.util;

import org.junit.Ignore;

import swp_compiler_ss13.common.lexer.LexerContractTest;
import swp_compiler_ss13.fuc.lexer.LexerImpl;

public class CrossTest extends LexerContractTest<LexerImpl> {

	@Ignore
	@Override
	protected LexerImpl getLexerInstance() {
		return new LexerImpl();
	}

}
