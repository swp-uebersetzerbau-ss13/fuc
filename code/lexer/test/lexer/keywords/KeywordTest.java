package lexer.keywords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import lexer.LexerImpl;
import lexer.util.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;

/**
 * Testclass for tokenizing of keywords
 * 
 * @author "Thomas Benndorf"
 * 
 */
public class KeywordTest {
	private Lexer lexer;

	@Before
	public void setUp() throws Exception {
		this.lexer = new LexerImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test for tokenizing of keywords
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void simpleTokenizingKeywordsTest()
			throws UnsupportedEncodingException {

		String simpleKeywordString = Constants.IFSTRING + " "
				+ Constants.WHILESTRING + " " + Constants.DOSTRING + " "
				+ Constants.BREAKSTRING + " " + Constants.RETURNSTRING + " "
				+ Constants.PRINTSTRING;

		this.lexer.setSourceStream(new ByteArrayInputStream(simpleKeywordString
				.getBytes("UTF-8")));

		Token token = this.lexer.getNextToken();
		assertEquals(Constants.IFSTRING, token.getValue());
		assertEquals(TokenType.IF, token.getTokenType());
		assertTrue(token.getLine() == 1);
		assertTrue(token.getColumn() == simpleKeywordString
				.indexOf(Constants.IFSTRING));

		token = this.lexer.getNextToken();
		assertEquals(Constants.WHILESTRING, token.getValue());
		assertEquals(TokenType.WHILE, token.getTokenType());
		assertTrue(token.getLine() == 1);
		assertTrue(token.getColumn() == simpleKeywordString
				.indexOf(Constants.WHILESTRING));

		token = this.lexer.getNextToken();
		assertEquals(Constants.DOSTRING, token.getValue());
		assertEquals(TokenType.DO, token.getTokenType());
		assertTrue(token.getLine() == 1);
		assertTrue(token.getColumn() == simpleKeywordString
				.indexOf(Constants.DOSTRING));

		token = this.lexer.getNextToken();
		assertEquals(Constants.BREAKSTRING, token.getValue());
		assertEquals(TokenType.BREAK, token.getTokenType());
		assertTrue(token.getLine() == 1);
		assertTrue(token.getColumn() == simpleKeywordString
				.indexOf(Constants.BREAKSTRING));

		token = this.lexer.getNextToken();
		assertEquals(Constants.RETURNSTRING, token.getValue());
		assertEquals(TokenType.RETURN, token.getTokenType());
		assertTrue(token.getLine() == 1);
		assertTrue(token.getColumn() == simpleKeywordString
				.indexOf(Constants.RETURNSTRING));

		token = this.lexer.getNextToken();
		assertEquals(Constants.PRINTSTRING, token.getValue());
		assertEquals(TokenType.PRINT, token.getTokenType());
		assertTrue(token.getLine() == 1);
		assertTrue(token.getColumn() == simpleKeywordString
				.indexOf(Constants.PRINTSTRING));

	}

	/**
	 * @deprecated not supported by grammar
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Deprecated
	@Ignore
	public void permutatedKeywordsTokenizingTest()
			throws UnsupportedEncodingException {
		/*
		 * permutate "if"-String
		 */
		ArrayList<String> permutatedKeywords = this
				.permutateKeyword(Constants.IFSTRING);
		for (int i = 0; i < permutatedKeywords.size(); i++) {
			System.out.println(permutatedKeywords.get(i));
			this.lexer.setSourceStream(new ByteArrayInputStream(
					permutatedKeywords.get(i).getBytes("UTF-8")));
			assertEquals(permutatedKeywords.get(i), this.lexer.getNextToken()
					.getValue());
		}

	}

	private ArrayList<String> permutateKeyword(String simpleKeyword) {
		ArrayList<String> permutatedKeywords = new ArrayList<>();
		char[] keywordChars = simpleKeyword.toCharArray();
		for (int i = 0, n = (int) Math.pow(2, keywordChars.length); i < n; i++) {
			char[] permutation = new char[keywordChars.length];
			for (int j = 0; j < keywordChars.length; j++) {
				if ((i >> j & 1) != 0) {
					permutation[j] = Character.toUpperCase(keywordChars[j]);
				} else {
					permutation[j] = keywordChars[j];
				}
			}
			permutatedKeywords.add(new String(permutation));
		}
		return permutatedKeywords;
	}
}
