package swp_compiler_ss13.fuc.lexer.util;

/**
 * Class for constants used in lexer tests
 * 
 * @author "Thomas Benndorf"
 * 
 */
public class Constants {

	/* keywords */
	public static final String IFSTRING = "if";
	public static final String ELSESTRING = "else";
	public static final String WHILESTRING = "while";
	public static final String DOSTRING = "do";
	public static final String BREAKSTRING = "break";
	public static final String RETURNSTRING = "return";
	public static final String PRINTSTRING = "print";

	/* boolean types */
	public static final String TRUESTRING = "true";
	public static final String FALSESTRING = "false";

	/* num types */
	public static final String LONGSTRING1 = "123";
	public static final String LONGSTRING2 = "123e2";
	public static final String LONGSTRING3 = "123E2";
	public static final String LONGSTRING4 = "123e-2";
	public static final String LONGSTRING5 = "123E-2";
	public static final String LONGSTRING6 = "-123";
	public static final String LONGSTRING7 = "-123e2";
	public static final String LONGSTRING8 = "-123E2";
	public static final String LONGSTRING9 = "-123e-2";
	public static final String LONGSTRING10 = "-123E+2";
	public static final String LONGSTRINGOUTOFRANGE1 = "9223372036854775808";
	public static final String LONGSTRINGOUTOFRANGE2 = "-9223372036854775809";

	/* real types */
	public static final String DOUBLESTRING1 = "123.123";
	public static final String DOUBLESTRING2 = "123.123e2";
	public static final String DOUBLESTRING3 = "123.123E2";
	public static final String DOUBLESTRING4 = "123.123e-2";
	public static final String DOUBLESTRING5 = "123.123E-2";
	public static final String DOUBLESTRING6 = "-123.123";
	public static final String DOUBLESTRING7 = "-123.123e2";
	public static final String DOUBLESTRING8 = "-123.123E2";
	public static final String DOUBLESTRING9 = "-123.123e-2";
	public static final String DOUBLESTRING10 = "-123.123E+2";
	public static final String DOUBLESTRINGOUTOFRANGE1 = "1.797694e+308";
	public static final String DOUBLESTRINGOUTOFRANGE2 = "-1.797694e+309";

	/* bracket types */
	public static final String LEFT_PARAN = "(";
	public static final String RIGHT_PARAN = ")";
	public static final String LEFT_BRACE = "{";
	public static final String RIGHT_BRACE = "}";
	public static final String LEFT_BRACKET = "[";
	public static final String RIGHT_BRACKET = "]";

	/* operation symbols */
	public static final String ASSIGNOPSTRING = "=";
	public static final String ANDSTRING = "&&";
	public static final String ORSTRING = "||";
	public static final String EQUALSSTRING = "==";
	public static final String NOTEQUALSSTRING = "!=";
	public static final String LESSSTRING = "<";
	public static final String LESS_OR_EQUALSTRING = "<=";
	public static final String GREATERSTRING = ">";
	public static final String GREATER_EQUALSTRING = ">=";
	public static final String PLUSSTRING = "+";
	public static final String MINUSSTRING = "-";
	public static final String TIMESSTRING = "*";
	public static final String DIVIDESTRING = "/";
	public static final String NOTSTRING = "!";

	/* other symbols */
	public static final String SEMICOLON = ";";
	public static final String COMMENT = "#";
	public static final String COMMENT_EXAMPLE = "a simple comment";
	public static final String EOF = "$";
	public static final String DOT = ".";

	/* type symbols */
	public static final String LONGSYMBOL = "long";
	public static final String DOUBLESYMBOL = "double";
	public static final String BOOLSYMBOL = "bool";
	public static final String STRINGSYMBOL = "string";
	public static final String RECORDSYMBOL = "record";

	/* IDs */
	public static final String ID1 = "id";
	public static final String ID2 = "id2id2id2id2";
	public static final String NOID1 = "1id";
	public static final String NOID2 = "id&";

	/* strings */
	public static final String STRING1 = "\"Ha\\\"llo We\\\"lt!\"";
	public static final String STRING2 = "\"\\ntest\"";
	public static final String STRING3 = "\"test;test\"";
	public static final String STRING4 = "\"test\"test";
	public static final String NOSTRING1 = "\"test";

}
