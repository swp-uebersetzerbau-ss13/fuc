package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.fuc.errorLog.LogEntry;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.lexer.token.BoolTokenImpl;
import swp_compiler_ss13.fuc.lexer.token.NumTokenImpl;
import swp_compiler_ss13.fuc.lexer.token.RealTokenImpl;
import swp_compiler_ss13.fuc.lexer.token.TokenImpl;
import swp_compiler_ss13.fuc.parser.generator.ALRGenerator;
import swp_compiler_ss13.fuc.parser.generator.LR1Generator;
import swp_compiler_ss13.fuc.parser.generator.items.LR1Item;
import swp_compiler_ss13.fuc.parser.generator.states.LR1State;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;
import swp_compiler_ss13.fuc.parser.parser.LRParser;
import swp_compiler_ss13.fuc.parser.parser.LexerWrapper;
import swp_compiler_ss13.fuc.parser.parser.tables.LRParsingTable;

public class GrammarTestHelper {
	private static final String TMP_FILE_PATH = "tmp";

	// for long
	public static Token num(long i) {
		return new NumTokenImpl(i + "", TokenType.NUM, -1, -1);
	}

	// for double
	public static Token real(double i) {
		return new RealTokenImpl(i + "", TokenType.REAL, -1, -1);
	}

	// for bool
	public static Token b(boolean b) {
		return new BoolTokenImpl(b + "", b ? TokenType.TRUE : TokenType.FALSE,
				-1, -1);
	}

	public static Token t(Terminal terminal) {
		// TODO Handle special terminals better
		if (terminal == Terminal.EOF) {
			return new TokenImpl(terminal.getId(), TokenType.EOF, -1, -1);
		}
		return new TokenImpl(terminal.getId(), terminal.getTokenTypes().next(),
				-1, -1);
	}

	public static Token t(String value, TokenType type) {
		return new TokenImpl(value, type, -1, -1);
	}

	public static Token id(String value) {
		return new TokenImpl(value, TokenType.ID, -1, -1);
	}

	public static void compareReportLogEntries(List<LogEntry> expected,
			List<LogEntry> actual) {
		compareReportLogEntries(expected, actual, true);
	}

	public static void compareReportLogEntries(List<LogEntry> expected,
			List<LogEntry> actual, boolean checkTokenCoords) {
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

	private static void compare(LogEntry expected, LogEntry actual,
			boolean checkTokenCoords) {
		assertEquals(expected.getLogType(), actual.getLogType());
		// TODO Compare error messages?!?!?
		// assertEquals(expected.getMessage(), actual.getMessage());
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

	private static void compare(Token expected, Token actual,
			boolean checkTokenCoords) {
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
	 * Usses {@link LR1Generator}, {@link LexerImpl} (wrapped by
	 * {@link LexerWrapper}) and {@link LRParser} to parse the given input
	 * 
	 * @param input
	 * @return
	 */
	public static AST parseToAst(String input) {
		// Generate parsing table
		Grammar grammar = new ProjectGrammar.Complete().getGrammar();
		ALRGenerator<LR1Item, LR1State> generator = new LR1Generator(grammar);
		LRParsingTable table = generator.getParsingTable();

		// Simulate input
		Lexer lexer = new LexerImpl();
		lexer.setSourceStream(new ByteArrayInputStream(input.getBytes()));

		// Run LR-parser with table
		LRParser lrParser = new LRParser();
		LexerWrapper lexWrapper = new LexerWrapper(lexer, grammar);
		ReportLog reportLog = new ReportLogImpl();
		return lrParser.parse(lexWrapper, reportLog, table);
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
	 * Stores the given string in a temporary file and loads it as file content
	 * with a {@link FileInputStream}
	 * 
	 * @param input
	 * @return
	 */
	public static String loadFromString(String input) {
		try {
			File tmp = new File(TMP_FILE_PATH);
			if (tmp.exists()) {
				throw new RuntimeException("File '" + TMP_FILE_PATH
						+ "' already exists!");
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
