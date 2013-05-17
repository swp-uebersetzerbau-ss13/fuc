package swp_compiler_ss13.fuc.parser.grammar;

import java.util.Arrays;
import java.util.List;

import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;

public class ProjectGrammar {
	public static final Terminal lcb = new Terminal("{", TokenType.LEFT_BRACE);
	public static final Terminal rcb = new Terminal("}", TokenType.RIGHT_BRACE);
	public static final Terminal id = new Terminal("id", TokenType.ID);
	public static final Terminal sem = new Terminal(";", TokenType.SEMICOLON);
	public static final Terminal lsb = new Terminal("[", TokenType.LEFT_BRACKET);
	public static final Terminal rsb = new Terminal("]",
			TokenType.RIGHT_BRACKET);
	public static final Terminal num = new Terminal("num", TokenType.NUM);
	public static final Terminal basic = new Terminal("basic", null);
	public static final Terminal record = new Terminal("record", null);

	public static final Terminal iff = new Terminal("if", TokenType.IF);
	public static final Terminal lb = new Terminal("(", TokenType.LEFT_PARAN);
	public static final Terminal rb = new Terminal(")", TokenType.RIGHT_PARAN);
	public static final Terminal elsee = new Terminal("else", TokenType.ELSE);
	public static final Terminal whilee = new Terminal("while", TokenType.WHILE);
	public static final Terminal doo = new Terminal("do", TokenType.DO);
	public static final Terminal breakk = new Terminal("break", TokenType.BREAK);
	public static final Terminal returnn = new Terminal("return",
			TokenType.RETURN);
	public static final Terminal print = new Terminal("print", TokenType.PRINT);
	public static final Terminal dot = new Terminal(".", null); // TODO ???

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

	public static final Production program1 = new Production(0, program, decls,
			stmts);
	public static final Production block1 = new Production(1, block, lcb,
			decls, stmts, rcb);
	public static final Production decls1 = new Production(2, decls, decls,
			decl);
	public static final Production decls2 = new Production(3, decls,
			Terminal.Epsilon);
	public static final Production decl1 = new Production(4, decl, type, id,
			sem);
	public static final Production type1 = new Production(5, type, type, lsb,
			num, rsb);
	public static final Production type2 = new Production(6, type, basic);
	public static final Production type3 = new Production(7, type, record, lcb,
			decls, rcb);
	public static final Production stmts1 = new Production(8, stmts, stmts,
			stmt);
	public static final Production stmts2 = new Production(9, stmts,
			Terminal.Epsilon);
	public static final Production stmt1 = new Production(10, stmt, assign, sem);
	public static final Production stmt2 = new Production(11, stmt, iff, lb,
			assign, rb, stmt);
	public static final Production stmt3 = new Production(12, stmt, iff, lb,
			assign, rb, stmt, elsee, stmt);
	public static final Production stmt4 = new Production(13, stmt, whilee, lb,
			assign, rb, stmt);
	public static final Production stmt5 = new Production(14, stmt, breakk, sem);
	public static final Production stmt6 = new Production(15, stmt, returnn,
			sem);
	public static final Production stmt7 = new Production(16, stmt, print, loc,
			sem);
	public static final Production stmt8 = new Production(17, stmt, block);
	public static final Production loc1 = new Production(18, loc, loc, lsb,
			assign, rsb);
	public static final Production loc2 = new Production(19, loc, id);
	public static final Production loc3 = new Production(20, loc, loc, dot, id);

	public static Grammar getGrammar() {

		List<Terminal> terminals = list(lcb, rcb, id, sem, lsb, rsb, num,
				basic, record, iff, lb, rb, elsee, whilee, doo, breakk,
				returnn, print, dot);
		List<NonTerminal> nonTerminals = list(program, block, decls, decl,
				type, stmts, stmt, loc, assign, bool, join, equality, rel,
				expr, term, unary, factor);
		List<Production> productions = list(program1, block1, decls1, decls2,
				decl1, type1, type2, type3, stmts1, stmts2, stmt1, stmt2,
				stmt3, stmt4, stmt5, stmt6, stmt7, stmt8, loc1, loc2, loc3);
		return new Grammar(terminals, nonTerminals, productions);
	}

	@SafeVarargs
	private static <T> List<T> list(T... terminals) {
		return Arrays.asList(terminals);
	}
}
