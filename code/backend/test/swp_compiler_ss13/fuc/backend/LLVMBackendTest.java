package swp_compiler_ss13.fuc.backend;

import org.junit.*;
import swp_compiler_ss13.common.backend.Quadruple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for LLVMBackend
 */
public class LLVMBackendTest {

    static LLVMBackend backend;


    /* Called before all the tests are started*/
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        backend = new LLVMBackend();
    }

    /* Called after all the tests have ended */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /* Called before every test */
    @Before
    public void setUp() throws Exception {
    }

    /* Called after every test */
    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void generateTargetCodeDeclarationTest() throws IOException {
        QuadrupleImpl declaration = new QuadrupleImpl(Quadruple.Operator.DECLARE_BOOLEAN, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "boolVariable");
        InputStream codeStream = generateCode(declaration);
        String generatedCode = buildString(codeStream);
        String expectedCode = "define i64 @main() {\n  %boolVariable = alloca i8\n  ret i64 0\n}\n";
        assertEquals(expectedCode, generatedCode);
    }


    private InputStream generateCode(Quadruple quadruple) {
        ArrayList<Quadruple> tacList = new ArrayList<>();
        tacList.add(quadruple);
        Map<String, InputStream> result = backend.generateTargetCode(tacList);
        InputStream module = result.get(".ll");
        return module;
    }

    private String buildString(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        for (String line = in.readLine(); line != null; line = in.readLine())
            builder.append(line + "\n");
        return builder.toString();
    }

}
