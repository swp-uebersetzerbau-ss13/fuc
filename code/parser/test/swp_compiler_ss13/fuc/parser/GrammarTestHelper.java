package swp_compiler_ss13.fuc.parser;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;

public class GrammarTestHelper {
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
	
	public static String loadExample(String name) throws Exception {
		String relPath = name;
		File file = new File(relPath);
		if (!file.exists()) {
			throw new RuntimeException("No file at: '" + relPath + "'");
		}
		
		FileInputStream fis = new FileInputStream(file);
		FileChannel fc = fis.getChannel();
		ByteBuffer bb = ByteBuffer.allocate((int) file.length());
		fc.read(bb);
		
		return new String(bb.array());
	}
}
