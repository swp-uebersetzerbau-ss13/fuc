package swp_compiler_ss13.fuc.parser.grammar;

import swp_compiler_ss13.common.lexer.TokenType;

/**
 * This classes define our grammar as specified in the requirements for the separate milestones
 * 
 * @see GrammarSpec
 * @author Gero
 */
public class ProjectGrammar {
	public static class M1 extends GrammarSpec {
		public static final Terminal lcb = new Terminal("{",
				TokenType.LEFT_BRACE);
		public static final Terminal rcb = new Terminal("}",
				TokenType.RIGHT_BRACE);
		public static final Terminal id = new Terminal("id", TokenType.ID);
		public static final Terminal sem = new Terminal(";",
				TokenType.SEMICOLON);
//		public static final Terminal lsb = new Terminal("[",
//				TokenType.LEFT_BRACKET);
//		public static final Terminal rsb = new Terminal("]",
//				TokenType.RIGHT_BRACKET);
		public static final Terminal basic = new Terminal("basic", TokenType.LONG_SYMBOL, TokenType.DOUBLE_SYMBOL, TokenType.BOOL_SYMBOL);
		// public static final Terminal record = new Terminal("record", null);

		// public static final Terminal iff = new Terminal("if", TokenType.IF);
		 public static final Terminal lb = new Terminal("(",
		 TokenType.LEFT_PARAN);
		 public static final Terminal rb = new Terminal(")",
		 TokenType.RIGHT_PARAN);
		// public static final Terminal elsee = new Terminal("else",
		// TokenType.ELSE);
		// public static final Terminal whilee = new Terminal("while",
		// TokenType.WHILE);
		// public static final Terminal doo = new Terminal("do", TokenType.DO);
		// public static final Terminal breakk = new Terminal("break",
		// TokenType.BREAK);
		public static final Terminal returnn = new Terminal("return",
				TokenType.RETURN);
		// public static final Terminal print = new Terminal("print",
		// TokenType.PRINT);
		// public static final Terminal dot = new Terminal(".", null); // TODO
		// ???
		public static final Terminal assignop = new Terminal("=",
				TokenType.ASSIGNOP);
//		public static final Terminal orop = new Terminal("||", TokenType.OR);
//		public static final Terminal andop = new Terminal("&&", TokenType.AND);
//		public static final Terminal equalop = new Terminal("==",
//				TokenType.EQUALS);
//		public static final Terminal notequalop = new Terminal("!=",
//				TokenType.NOT_EQUALS);
//		public static final Terminal lt = new Terminal("<", TokenType.LESS);
//		public static final Terminal lte = new Terminal("<=",
//				TokenType.LESS_OR_EQUAL);
//		public static final Terminal gte = new Terminal(">=",
//				TokenType.GREATER_EQUAL);
//		public static final Terminal gt = new Terminal(">", TokenType.GREATER);
		public static final Terminal plus = new Terminal("+", TokenType.PLUS);
		public static final Terminal minus = new Terminal("-", TokenType.MINUS);
		public static final Terminal times = new Terminal("*", TokenType.TIMES);
		public static final Terminal div = new Terminal("/", TokenType.DIVIDE);
//		public static final Terminal not = new Terminal("!", TokenType.NOT);
		public static final Terminal num = new Terminal("num", TokenType.NUM);
		public static final Terminal real = new Terminal("real", TokenType.REAL);
//		public static final Terminal truee = new Terminal("true",
//				TokenType.TRUE);
//		public static final Terminal falsee = new Terminal("false",
//				TokenType.FALSE);
//		public static final Terminal stringg = new Terminal("string",
//				TokenType.STRING);

		public static final NonTerminal program = new NonTerminal("program");
		public static final NonTerminal block = new NonTerminal("block");
		public static final NonTerminal decls = new NonTerminal("decls");
		public static final NonTerminal decl = new NonTerminal("decl");
		public static final NonTerminal type = new NonTerminal("type");
		public static final NonTerminal stmts = new NonTerminal("stmts");
		public static final NonTerminal stmt = new NonTerminal("stmt");
		public static final NonTerminal loc = new NonTerminal("loc");
		public static final NonTerminal assign = new NonTerminal("assign");
		public static final NonTerminal bool = new NonTerminal("bool");
		public static final NonTerminal join = new NonTerminal("join");
		public static final NonTerminal equality = new NonTerminal("equality");
		public static final NonTerminal rel = new NonTerminal("rel");
		public static final NonTerminal expr = new NonTerminal("expr");
		public static final NonTerminal term = new NonTerminal("term");
		public static final NonTerminal unary = new NonTerminal("unary");
		public static final NonTerminal factor = new NonTerminal("factor");

		public static final Production program1 = new Production(0, program,
				decls, stmts);
		// public static final Production block1 = new Production(1, block, lcb,
		// decls, stmts, rcb);
		public static final Production decls1 = new Production(2, decls, decls,
				decl);
		public static final Production decls2 = new Production(3, decls,
				Terminal.Epsilon);
		public static final Production decl1 = new Production(4, decl, type,
				id, sem);
//		public static final Production type1 = new Production(5, type, type,
//				lsb, num, rsb);
		public static final Production type2 = new Production(6, type, basic);
		// public static final Production type3 = new Production(7, type,
		// record,
		// lcb, decls, rcb);
		public static final Production stmts1 = new Production(8, stmts, stmts,
				stmt);
		public static final Production stmts2 = new Production(9, stmts,
				Terminal.Epsilon);
		public static final Production stmt1 = new Production(10, stmt, assign,
				sem);
		// public static final Production stmt2 = new Production(11, stmt, iff,
		// lb, assign, rb, stmt);
		// public static final Production stmt3 = new Production(12, stmt, iff,
		// lb, assign, rb, stmt, elsee, stmt);
		// public static final Production stmt4 = new Production(13, stmt,
		// whilee,
		// lb, assign, rb, stmt);
//		public static final Production stmt9 = new Production(52, stmt, doo, stmt, whilee, lb, assign, rb, sem);
		// public static final Production stmt5 = new Production(14, stmt,
		// breakk,
		// sem);
		public static final Production stmt6 = new Production(15, stmt,
				returnn, sem);
		public static final Production stmt10 = new Production(15, stmt,
				returnn, loc, sem);
		// public static final Production stmt7 = new Production(16, stmt,
		// print,
		// loc, sem);
		public static final Production stmt8 = new Production(17, stmt, block);
//		public static final Production loc1 = new Production(18, loc, loc, lsb,
//				assign, rsb);
		public static final Production loc2 = new Production(19, loc, id);

		// public static final Production loc3 = new Production(20, loc, loc,
		// dot,
		// id);
		public static final Production assign1 = new Production(22, assign,
				loc, assignop, assign);
		public static final Production assign2 = new Production(23, assign,
				bool);
//		public static final Production bool1 = new Production(24, bool, bool,
//				orop, join);
		public static final Production bool2 = new Production(25, bool, join);
//		public static final Production join1 = new Production(26, join, join,
//				andop, equality);
		public static final Production join2 = new Production(27, join,
				equality);
//		public static final Production equality1 = new Production(28, equality,
//				equality, equalop, rel);
//		public static final Production equality2 = new Production(29, equality,
//				notequalop, rel);
		public static final Production equality3 = new Production(30, equality,
				rel);
//		public static final Production rel1 = new Production(31, rel, expr, lt,
//		public static final Production rel2 = new Production(32, rel, expr,
//				lte, expr);
//		public static final Production rel3 = new Production(33, rel, expr,
//				gte, expr);
//		public static final Production rel4 = new Production(34, rel, expr, gt,
//				expr);
		public static final Production rel5 = new Production(35, rel, expr);
		public static final Production expr1 = new Production(36, expr, expr,
				plus, term);
		public static final Production expr2 = new Production(37, expr, expr,
				minus, term);
		public static final Production expr3 = new Production(38, expr, term);
		public static final Production term1 = new Production(39, term, term,
				times, unary);
		public static final Production term2 = new Production(40, term, term,
				div, unary);
		public static final Production term3 = new Production(41, term, unary);
//		public static final Production unary1 = new Production(42, unary, not,
//				unary);
		public static final Production unary2 = new Production(43, unary,
				minus, unary);
		public static final Production unary3 = new Production(44, unary,
				factor);
		public static final Production factor1 = new Production(45, factor, lb,
				assign, rb);

		public static final Production factor2 = new Production(46, factor, loc);
		public static final Production factor3 = new Production(47, factor, num);
		public static final Production factor4 = new Production(48, factor,
				real);
//		public static final Production factor5 = new Production(49, factor,
//				truee);
//		public static final Production factor6 = new Production(50, factor,
//				falsee);
//		public static final Production factor7 = new Production(51, factor,
//				stringg);
	}

	
	public static class Complete extends GrammarSpec {
		public static final Terminal lcb = new Terminal("{",
				TokenType.LEFT_BRACE);
		public static final Terminal rcb = new Terminal("}",
				TokenType.RIGHT_BRACE);
		public static final Terminal id = new Terminal("id", TokenType.ID);
		public static final Terminal sem = new Terminal(";",
				TokenType.SEMICOLON);
		public static final Terminal lsb = new Terminal("[",
				TokenType.LEFT_BRACKET);
		public static final Terminal rsb = new Terminal("]",
				TokenType.RIGHT_BRACKET);
		public static final Terminal record = new Terminal("record", TokenType.RECORD_SYMBOL);

		public static final Terminal iff = new Terminal("if", TokenType.IF);
		public static final Terminal lb = new Terminal("(",
				TokenType.LEFT_PARAN);
		public static final Terminal rb = new Terminal(")",
				TokenType.RIGHT_PARAN);
		public static final Terminal elsee = new Terminal("else",
				TokenType.ELSE);
		public static final Terminal whilee = new Terminal("while",
				TokenType.WHILE);
		public static final Terminal doo = new Terminal("do", TokenType.DO);
		public static final Terminal breakk = new Terminal("break",
				TokenType.BREAK);
		public static final Terminal returnn = new Terminal("return",
				TokenType.RETURN);
		public static final Terminal print = new Terminal("print",
				TokenType.PRINT);
		public static final Terminal dot = new Terminal(".", TokenType.DOT); // TODO Dot???
		public static final Terminal assignop = new Terminal("=",
				TokenType.ASSIGNOP);
		public static final Terminal orop = new Terminal("||", TokenType.OR);
		public static final Terminal andop = new Terminal("&&", TokenType.AND);
		public static final Terminal equalop = new Terminal("==",
				TokenType.EQUALS);
		public static final Terminal notequalop = new Terminal("!=",
				TokenType.NOT_EQUALS);
		public static final Terminal lt = new Terminal("<", TokenType.LESS);
		public static final Terminal lte = new Terminal("<=",
				TokenType.LESS_OR_EQUAL);
		public static final Terminal gte = new Terminal(">=",
				TokenType.GREATER_EQUAL);
		public static final Terminal gt = new Terminal(">", TokenType.GREATER);
		public static final Terminal plus = new Terminal("+", TokenType.PLUS);
		public static final Terminal minus = new Terminal("-", TokenType.MINUS);
		public static final Terminal times = new Terminal("*", TokenType.TIMES);
		public static final Terminal div = new Terminal("/", TokenType.DIVIDE);
		public static final Terminal not = new Terminal("!", TokenType.NOT);
		public static final Terminal num = new Terminal("num", TokenType.NUM);
		public static final Terminal real = new Terminal("real", TokenType.REAL);
		public static final Terminal basic = new Terminal("basic", TokenType.LONG_SYMBOL, TokenType.DOUBLE_SYMBOL, TokenType.BOOL_SYMBOL);
		public static final Terminal truee = new Terminal("true",
				TokenType.TRUE);
		public static final Terminal falsee = new Terminal("false",
				TokenType.FALSE);
		public static final Terminal stringg = new Terminal("string",
				TokenType.STRING);

		public static final NonTerminal program = new NonTerminal("program");
		public static final NonTerminal block = new NonTerminal("block");
		public static final NonTerminal decls = new NonTerminal("decls");
		public static final NonTerminal decl = new NonTerminal("decl");
		public static final NonTerminal type = new NonTerminal("type");
		public static final NonTerminal stmts = new NonTerminal("stmts");
		public static final NonTerminal stmt = new NonTerminal("stmt");
		public static final NonTerminal loc = new NonTerminal("loc");
		public static final NonTerminal assign = new NonTerminal("assign");
		public static final NonTerminal bool = new NonTerminal("bool");
		public static final NonTerminal join = new NonTerminal("join");
		public static final NonTerminal equality = new NonTerminal("equality");
		public static final NonTerminal rel = new NonTerminal("rel");
		public static final NonTerminal expr = new NonTerminal("expr");
		public static final NonTerminal term = new NonTerminal("term");
		public static final NonTerminal unary = new NonTerminal("unary");
		public static final NonTerminal factor = new NonTerminal("factor");

		public static final Production program1 = new Production(0, program,
				decls, stmts);
		public static final Production block1 = new Production(1, block, lcb,
				decls, stmts, rcb);
		public static final Production decls1 = new Production(2, decls, decls,
				decl);
		public static final Production decls2 = new Production(3, decls,
				Terminal.Epsilon);
		public static final Production decl1 = new Production(4, decl, type,
				id, sem);
		public static final Production type1 = new Production(5, type, type,
				lsb, num, rsb);
		public static final Production type2 = new Production(6, type, basic);
		public static final Production type3 = new Production(7, type, record,
				lcb, decls, rcb);
		public static final Production stmts1 = new Production(8, stmts, stmts,
				stmt);
		public static final Production stmts2 = new Production(9, stmts,
				Terminal.Epsilon);
		public static final Production stmt1 = new Production(10, stmt, assign,
				sem);
		public static final Production stmt2 = new Production(11, stmt, iff,
				lb, assign, rb, stmt);
		public static final Production stmt3 = new Production(12, stmt, iff,
				lb, assign, rb, stmt, elsee, stmt);
		public static final Production stmt4 = new Production(13, stmt, whilee,
				lb, assign, rb, stmt);
		public static final Production stmt9 = new Production(52, stmt, doo, stmt, whilee, lb, assign, rb, sem);
		public static final Production stmt5 = new Production(14, stmt, breakk,
				sem);
		public static final Production stmt6 = new Production(15, stmt,
				returnn, sem);
		public static final Production stmt10 = new Production(15, stmt,
				returnn, loc, sem);
		public static final Production stmt7 = new Production(16, stmt, print,
				loc, sem);
		public static final Production stmt8 = new Production(17, stmt, block);
		public static final Production loc1 = new Production(18, loc, loc, lsb,
				assign, rsb);
		public static final Production loc2 = new Production(19, loc, id);
		public static final Production loc3 = new Production(20, loc, loc, dot,
				id);
		public static final Production assign1 = new Production(22, assign,
				loc, assignop, assign);
		public static final Production assign2 = new Production(23, assign,
				loc, assignop, bool);
		public static final Production bool1 = new Production(24, bool, bool,
				orop, join);
		public static final Production bool2 = new Production(25, bool, join);
		public static final Production join1 = new Production(26, join, join,
				andop, equality);
		public static final Production join2 = new Production(27, join,
				equality);
		public static final Production equality1 = new Production(28, equality,
				equality, equalop, rel);
		public static final Production equality2 = new Production(29, equality,
				notequalop, rel);
		public static final Production equality3 = new Production(30, equality,
				rel);
		public static final Production rel1 = new Production(31, rel, expr, lt,
				expr);
		public static final Production rel2 = new Production(32, rel, expr,
				lte, expr);
		public static final Production rel3 = new Production(33, rel, expr,
				gte, expr);
		public static final Production rel4 = new Production(34, rel, expr, gt,
				expr);
		public static final Production rel5 = new Production(35, rel, expr);
		public static final Production expr1 = new Production(36, expr, expr,
				plus, term);
		public static final Production expr2 = new Production(37, expr, expr,
				minus, term);
		public static final Production expr3 = new Production(38, expr, term);
		public static final Production term1 = new Production(39, term, term,
				times, unary);
		public static final Production term2 = new Production(40, term, term,
				div, unary);
		public static final Production term3 = new Production(41, term, unary);
		public static final Production unary1 = new Production(42, unary, not,
				unary);
		public static final Production unary2 = new Production(43, unary,
				minus, unary);
		public static final Production unary3 = new Production(44, unary,
				factor);
		public static final Production factor1 = new Production(45, factor, lb,
				assign, rb);
		public static final Production factor2 = new Production(46, factor, loc);
		public static final Production factor3 = new Production(47, factor, num);
		public static final Production factor4 = new Production(48, factor,
				real);
		public static final Production factor5 = new Production(49, factor,
				truee);
		public static final Production factor6 = new Production(50, factor,
				falsee);
		public static final Production factor7 = new Production(51, factor,
				stringg);
	}
}
