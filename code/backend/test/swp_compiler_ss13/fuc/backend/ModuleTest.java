package swp_compiler_ss13.fuc.backend;

import junit.extensions.PA;
import org.junit.*;
import swp_compiler_ss13.common.types.Type;

import java.io.*;

import static org.junit.Assert.assertEquals;


/**
 * Tests for Module
 */
public class ModuleTest {

    private static Module module;
    private static ByteArrayOutputStream outStream;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        outStream = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(outStream);
        module = new Module(out);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void generateTest() throws IOException {
        String code = "code stub";
        PA.invokeMethod(module, "gen(String)", code);
        PrintWriter out = (PrintWriter) PA.getValue(module, "out");
        out.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outStream.toByteArray());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String result = reader.readLine();
        assertEquals(result.length(), code.length() + 2);
        assertEquals(result.charAt(0), ' ');
        assertEquals(result.charAt(1), ' ');
    }

    @Test
    public void getIRTypeLongTest() {
        Class kindClass = Type.Kind.class;
        String type = (String) PA.invokeMethod(module, "getIRType(swp_compiler_ss13.common.types.Type$Kind)", Type.Kind.LONG);
        assertEquals(type, "i64");
    }

    @Test
    public void getIRTypeDoubleTest() {
        Class kindClass = Type.Kind.class;
        String type = (String) PA.invokeMethod(module, "getIRType(swp_compiler_ss13.common.types.Type$Kind)", Type.Kind.DOUBLE);
        assertEquals(type, "double");
    }
}
