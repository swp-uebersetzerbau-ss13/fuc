package swp_compiler_ss13.fuc.semantic_analyser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.report.ReportType;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.derived.Member;
import swp_compiler_ss13.common.types.derived.StructType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.errorLog.LogEntry;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;

public class StructTests {

	private SemanticAnalyser analyser;
	private ReportLogImpl log;

	public StructTests() {
	}

	@Before
	public void setUp() {
		log = new ReportLogImpl();
		analyser = new SemanticAnalyser();
		analyser.setReportLog(log);
	}

	@After
	public void tearDown() {
		analyser = null;
		log = null;
	}
	
	/**
	 * # no errors expected<br/>
	 * struct{bool b} s;<br/>
	 * bool b;<br/>
	 * <br/>
	 * b = s.b;
	 */
	@Test
	public void testStructAssigment() {
		Type structType = new StructType("struct", new Member("b", new BooleanType()));
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("s", structType);
		astFactory.addDeclaration("b", new BooleanType());
		IdentifierNode identifier_b = astFactory.newBasicIdentifier("b");
		IdentifierNode identifier_s = astFactory.newBasicIdentifier("s");
		IdentifierNode identifier_s_b = astFactory.newStructIdentifier("b", identifier_s);
		astFactory.addAssignment(identifier_b, identifier_s_b);
		
		AST ast = astFactory.getAST();
		analyser.analyse(ast);
		
		assertFalse(log.hasErrors());
	}
	
	/**
	 * # no errors expected<br/>
	 * struct{bool b} s1;<br/>
	 * struct{bool b} s2;<br/>
	 * <br/>
	 * s1 = s2;
	 */
	@Test
	public void testStructTypeAssigment() {
		Type structType1 = new StructType("struct1", new Member("b", new BooleanType()));
		Type structType2 = new StructType("struct2", new Member("b", new BooleanType()));
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("s1", structType1);
		astFactory.addDeclaration("s2", structType2);
		IdentifierNode identifier_s1 = astFactory.newBasicIdentifier("s1");
		IdentifierNode identifier_s2 = astFactory.newBasicIdentifier("s2");
		astFactory.addAssignment(identifier_s1, identifier_s2);
		
		AST ast = astFactory.getAST();
		analyser.analyse(ast);
		
		System.out.println(log);
		assertFalse(log.hasErrors());
	}
	
	/**
	 * # error: invalid assignment<br/>
	 * struct{bool t; struct{long t} s} s;<br/>
	 * bool b;<br/>
	 * long l;<br/>
	 * <br/>
	 * l = s.s.t;
	 * b = s.s.t;
	 */
	@Test
	public void tsetNestedStructAssigmentTypeError() {
		Member longMember = new Member("t", new LongType());
		Type innerStructType = new StructType("s", longMember);
		Member boolMember = new Member("t", new BooleanType());
		Member structMember = new Member("s", innerStructType);
		Type structType = new StructType("struct", boolMember, structMember);
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("s", structType);
		astFactory.addDeclaration("b", new BooleanType());
		astFactory.addDeclaration("l", new LongType());
		IdentifierNode identifier_b = astFactory.newBasicIdentifier("b");
		IdentifierNode identifier_l = astFactory.newBasicIdentifier("l");
		IdentifierNode identifier_s1 = astFactory.newBasicIdentifier("s");
		IdentifierNode identifier_s2 = astFactory.newBasicIdentifier("s");
		IdentifierNode identifier_s_s1 = astFactory.newStructIdentifier("s", identifier_s1);
		IdentifierNode identifier_s_s2 = astFactory.newStructIdentifier("s", identifier_s2);
		IdentifierNode identifier_s_s_t1 = astFactory.newStructIdentifier("t", identifier_s_s1);
		IdentifierNode identifier_s_s_t2 = astFactory.newStructIdentifier("t", identifier_s_s2);
		astFactory.addAssignment(identifier_l, identifier_s_s_t2);
		astFactory.addAssignment(identifier_b, identifier_s_s_t1);
		
		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}
	
	/**
	 * # error: assignment of bool to long<br/>
	 * struct{bool b} s;<br/>
	 * long l;<br/>
	 * <br/>
	 * l = s.b;
	 */
	@Test
	public void testStructAssigmentTypeError() {
		Type structType = new StructType("struct", new Member("b", new BooleanType()));
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("s", structType);
		astFactory.addDeclaration("l", new LongType());
		IdentifierNode identifier_l = astFactory.newBasicIdentifier("l");
		IdentifierNode identifier_s = astFactory.newBasicIdentifier("s");
		IdentifierNode identifier_s_b = astFactory.newStructIdentifier("b", identifier_s);
		astFactory.addAssignment(identifier_l, identifier_s_b);
		
		AST ast = astFactory.getAST();
		analyser.analyse(ast);
		
		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}
	
	/**
	 * # error: assignment of long to bool<br/>
	 * struct{bool b; long l} s;<br/>
	 * <br/>
	 * s.b = s.l;
	 */
	@Test
	public void testInnerStructAssigmentTypeError() {
		Member boolMember = new Member("b", new BooleanType());
		Member longMember = new Member("l", new LongType());
		Type structType = new StructType("struct", boolMember, longMember);
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("s", structType);
		IdentifierNode identifier_s1 = astFactory.newBasicIdentifier("s");
		IdentifierNode identifier_s2 = astFactory.newBasicIdentifier("s");
		IdentifierNode identifier_s_b = astFactory.newStructIdentifier("b", identifier_s1);
		IdentifierNode identifier_s_l = astFactory.newStructIdentifier("l", identifier_s2);
		astFactory.addAssignment(identifier_s_b, identifier_s_l);
		
		AST ast = astFactory.getAST();
		analyser.analyse(ast);
		
		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}
}
