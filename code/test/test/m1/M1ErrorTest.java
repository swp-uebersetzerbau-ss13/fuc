package m1;

import junit.extensions.PA;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import swp_compiler_ss13.common.lexer.LexerImpl;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.fuc.backend.LLVMBackend;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.parser.ParserImpl;
import swp_compiler_ss13.fuc.parser.errorHandling.Error;
import swp_compiler_ss13.fuc.parser.errorHandling.ReportLogImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Test for the M1 examples producing an expected error.
 * @author Jens V. Fischer
 */
@RunWith(value = Parameterized.class)
public class M1ErrorTest {

	private static Lexer lexer;
	private static Parser parser;
	private static ReportLogImpl errlog;

	private String prog;
	private String expected;

	public M1ErrorTest(String nameOfTestProg, String prog, String expected) {
		this.prog = prog;
		this.expected = expected;
	}

	@Parameterized.Parameters(name = "{index}: {0}")
	public static Collection<Object[]> data() {

		String doubleDeclaration = "" +
				"# error: two decls for same id i\n" +
				"long i;\n" +
				"long i;\n";
		String invalidIds = "" +
				"# error: invalid ids\n" +
				"long foo$bar;\n" +
				"long spam_ham;\n" +
				"long 2fooly;\n" +
				"long return;\n" +
				"long string;\n" +
				"long bool;\n" +
				"long fü_berlin;";
		String multipleMinusENotation = "" +
				"# error: id foo has multiple minus in expontent notation\n" +
				"long foo;\n" +
				"foo = 10e----1;";
		String multiplePlusesInExp = "" +
				"# error: too many pluses in an expression\n" +
				"long foo;\n" +
				"long bar;\n" +
				"foo = 3;\n" +
				"bar = foo ++ 1;";
		String undefReturn = "" +
				"# error: id spam is not initialized and returned\n" +
				"long spam;\n" +
				"return spam;";

		return Arrays.asList(new Object[][] {
				/* mask: {testName, progCode, expectedReportLogError} */
				{ "doubleDeclaration", doubleDeclaration,
						"The variable 'i' of type 'LongType' has been declared twice!" },
				{ "invalidIds", invalidIds, "Found undefined token 'foo$bar'!" },
				{ "multipleMinusENotation", multipleMinusENotation, "Found undefined token '10e----1'!" },
				{ "multiplePlusesInExp", multiplePlusesInExp, "Found undefined token '++'!" },
				{ "undefReturn", undefReturn, "NotInitializedException" } });
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Logger.getRootLogger().setLevel(Level.ERROR);

	}

	@Before
	public void setUp() throws Exception {
		lexer = new LexerImpl();
		parser = new ParserImpl();
		errlog = new ReportLogImpl();
	}


	@Test
	public void errorTest() throws InterruptedException, IOException, IntermediateCodeGeneratorException, BackendException {
		Error e = compileForError(this.prog).get(0);
		assertEquals(this.expected, e.getMessage());
	}


	private List<Error> compileForError(String prog) throws BackendException,
			IntermediateCodeGeneratorException, IOException, InterruptedException {
		lexer.setSourceStream(new ByteArrayInputStream(prog.getBytes("UTF-8")));
		parser.setLexer(lexer);
		parser.setReportLog(errlog);
		AST ast = parser.getParsedAST();
		return errlog.getErrors();
	}
}
