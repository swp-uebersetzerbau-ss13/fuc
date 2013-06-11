package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.errorLog.LogEntry;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;

public class GrammarTestHelper {
	private static final String TMP_FILE_PATH = "tmp";
	
	// for long	
	public static Token num(long i) {
		return new TestToken(i + "", TokenType.NUM);
	}

	// for  double
	public static Token real(double i) {
		return new TestToken(i + "", TokenType.REAL);
	}

	public static Token t(Terminal terminal) {
		// TODO Handle special terminals better
		if (terminal == Terminal.EOF) {
			return new TestToken(terminal.getId(), TokenType.EOF);
		}
		return new TestToken(terminal.getId(), terminal.getTokenTypes().next());
	}

	public static Token id(String value) {
		return new TestToken(value, TokenType.ID);
	}
	
	public static void compareReportLogEntries(List<LogEntry> expected, List<LogEntry> actual) {
		compareReportLogEntries(expected, actual, true);
	}
	
	public static void compareReportLogEntries(List<LogEntry> expected, List<LogEntry> actual, boolean checkTokenCoords) {
		Iterator<LogEntry> expIt = expected.iterator();
		Iterator<LogEntry> actIt = actual.iterator();
		while (expIt.hasNext() && actIt.hasNext()) {
			LogEntry exp = expIt.next();
			LogEntry act = actIt.next();
			compare(exp, act, checkTokenCoords);
		}
		
		if (expIt.hasNext() != actIt.hasNext()) {
			fail("Different number of LogEntrys!");
		}
	}
	
	private static void compare(LogEntry expected, LogEntry actual, boolean checkTokenCoords) {
		assertEquals(expected.getLogType(), actual.getLogType());
		// TODO Compare error messages?!?!?
//		assertEquals(expected.getMessage(), actual.getMessage());
		assertEquals(expected.getReportType(), actual.getReportType());

		assertEquals(expected.getTokens().size(), actual.getTokens().size());
		Iterator<Token> expTokenIt = expected.getTokens().iterator();
		Iterator<Token> actTokenIt = actual.getTokens().iterator();
		while (expTokenIt.hasNext()) {
			Token expToken = expTokenIt.next();
			Token actToken = actTokenIt.next();
			compare(expToken, actToken, checkTokenCoords);
		}
	}
	
	private static void compare(Token expected, Token actual, boolean checkTokenCoords) {
		if (checkTokenCoords) {
			assertEquals(expected.getColumn(), actual.getColumn());
			assertEquals(expected.getLine(), actual.getLine());
		}
		assertEquals(expected.getTokenType(), actual.getTokenType());
		assertEquals(expected.getValue(), actual.getValue());
	}
	
	public static List<Token> tokens(Token... tokens) {
		return Arrays.asList(tokens);
	}
	
	
	/**
	 * Loads the file content from the file with the given relative path
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static String loadExample(String name) throws Exception {
		String relPath = name;
		File file = new File(relPath);
		if (!file.exists()) {
			throw new RuntimeException("No file at: '" + relPath + "'");
		}
		
		return readFromFile(file);
	}
	
	private static String readFromFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		FileChannel fc = fis.getChannel();
		ByteBuffer bb = ByteBuffer.allocate((int) file.length());
		fc.read(bb);
		fis.close();
		
		return new String(bb.array());
	}
	
	/**
	 * Stores the given string in a temporary file and loads it as file content with a {@link FileInputStream}
	 * 
	 * @param input
	 * @return
	 */
	public static String loadFromString(String input) {
		try {
			File tmp = new File(TMP_FILE_PATH);
			if (tmp.exists()) {
				throw new RuntimeException("File '" + TMP_FILE_PATH + "' already exists!");
			}
		
			if (!tmp.createNewFile()) {
				throw new RuntimeException("Unable to create tmp file!");
			}
			
			FileWriter fw = new FileWriter(tmp);
			fw.write(input);
			fw.close();
			
			String result = readFromFile(tmp);
			
			tmp.delete();
		
			return result;
		} catch (IOException err) {
			err.printStackTrace();
			throw new RuntimeException("Unable to load test from string!");
		}
	}
}
