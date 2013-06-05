package swp_compiler_ss13.fuc.backend;

import org.junit.*;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertEquals;


/**
 * Base Class LLVMBackend Backend Tests
 */
public class TestBase {

	static LLVMBackend backend;
	static ArrayList<Quadruple> tac;
	static String header;
	static String mainFooter;


	/* Called before all the tests are started*/
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		backend = new LLVMBackend();
		header = backend.llvm_preamble +
			"\ndefine i64 @main() {\n";
		mainFooter = backend.llvm_uncaught + "}\n";
	}

	/* Called after all the tests have ended */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/* Called before every test */
	@Before
	public void setUp() throws Exception {
		tac = new ArrayList<Quadruple>();
	}

	/* Called after every test */
	@After
	public void tearDown() throws Exception {
	}

	/* Util */

	String generateCodeAsString(ArrayList<Quadruple> tac) throws IOException, BackendException {
		String baseFileName = "baseFileName";
		Map<String, InputStream> result = backend.generateTargetCode(baseFileName, tac);
		InputStream module = result.get("baseFileName.ll");
		BufferedReader in = new BufferedReader(new InputStreamReader(module));
		StringBuilder builder = new StringBuilder();
		for (String line = in.readLine(); line != null; line = in.readLine())
			builder.append(line + "\n");
		return builder.toString();
	}


	void expectMain(String mainFunctionCode, String ir)
	{
		String expectedCode = header + mainFunctionCode + mainFooter;
		assertEquals(expectedCode, ir);
	}
}
