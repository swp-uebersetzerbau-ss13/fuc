package swp_compiler_ss13.fuc.backend;

import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.primitive.*;
import swp_compiler_ss13.common.types.derived.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Iterator;

/**
 * This implements a backend for LLVM IR.
 *
 */
public class LLVMBackend implements Backend
{
	/**
	 * Contains the preamble for any generated
	 * LLVM IR program; this preamble contains
	 * declarations of stdlib C functions used
	 * (e.g. printf for output to stdout),
	 * declarations of the external C++ functions
	 * used for runtime exceptions, the list
	 * of currently implemented (custom) exceptions
	 * and LLVM IR functions used by generated LLVM
	 * IR code for various other purposes, such
	 * as conversion functions and functions
	 * used to safely envelop volatile LLVM IR
	 * instructions that may lead to segfaults
	 * for bad arguments, throwing exceptions instead.
	 *
	 */
	public final String llvm_preamble;
	/**
	 * Contains the default catch for all thrown exceptions,
	 * so that instead of crashing with verbosity dependent
	 * on both the operating system and the respective
	 * implementation of the C++ standard library, information
	 * about the exception causing the program crash is
	 * given in a uniform fashion.
	 * This is achieved by appending the code for this
	 * default catch to the main function after it has been
	 * generated.
	 *
	 */
	public final String llvm_uncaught;

	/**
	 * Creates a new <code>LLVMBackend</code> instance
	 * and sets up all necessary variables.
	 *
	 */
	public LLVMBackend() {
		/* Load the preamble containing
		   functionality used in generated IR code */
		StringBuilder out = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(
					this.getClass().getResourceAsStream("preamble.ll")));
			String line;
			while((line = reader.readLine()) != null) {
				out.append(line + "\n");
			}
		}
		catch(IOException e) { }

		this.llvm_preamble = out.toString();

		/* Load the exception handler for uncaught
		   exceptions. */
		out = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(
					this.getClass().getResourceAsStream("uncaught.ll")));
			String line;
			while((line = reader.readLine()) != null) {
				out.append("  " + line + "\n");
			}
		}
		catch(IOException e) { }

		this.llvm_uncaught = "\n" + out.toString();
	}

	/**
	 * This is a pass the backend performs on the TAC
	 * to ensure it is correct before generating any code.
	 * This pass in particular ensures that all basic blocks
	 * are terminated by a valid Quadruple, which is either
	 * a <code>Quadruple.Operator.RETURN</code> or a <code>Quadruple.Operator.BRANCH</code>,
	 * by adding them where necessary.
	 *
	 * @param tac the program in TAC format to perform the pass on
	 * @return the program in TAC format after the pass has been performed
	 */
	private List<Quadruple> basicBlocksTerminatedPass(List<Quadruple> tac) {

		/* Whenever a label is not preceded by a terminator instruction,
		 * add a branching instruction to the label before it. */
		for(int i = 0; i < tac.size(); i += 1) {
			Quadruple q = tac.get(i);
			Quadruple.Operator op = q.getOperator();
			if((op == Quadruple.Operator.LABEL)) {
				Quadruple.Operator last_op = tac.get(i-1).getOperator();
				if(! ((last_op == Quadruple.Operator.RETURN) ||
				      (last_op == Quadruple.Operator.BRANCH))) {
					tac.add(i, new QuadrupleImpl(
						        Quadruple.Operator.BRANCH,
						        q.getArgument1(),
						        Quadruple.EmptyArgument,
						        Quadruple.EmptyArgument));
					i += 1;
				}
			}
		}

		/* If the program is not terminated by a proper return,
		 * add such a return with the default value of 0 (indicating
		 * successful termination). */
		if(tac.size() == 0 || tac.get(tac.size() - 1).getOperator() != Quadruple.Operator.RETURN) {
			tac.add(new QuadrupleImpl(Quadruple.Operator.RETURN,
			                          "#0",
			                          Quadruple.EmptyArgument,
			                          Quadruple.EmptyArgument));
		}

		return tac;
	}

	private LLVMBackendArrayType parseArrayDeclaration(Module m, Iterator<Quadruple> tacIterator, int size) throws BackendException {
		boolean done = false;
		Type type = null;
		List<Integer> dimensions = new LinkedList<Integer>();
		dimensions.add(size);

		while(!done && tacIterator.hasNext()) {
			Quadruple q = tacIterator.next();

			if(q.getOperator() == Quadruple.Operator.DECLARE_ARRAY) {
				size = Integer.parseInt(q.getArgument1().substring(1));
				assert(size>=0);
				dimensions.add(size);
			}
			else {
				switch(q.getOperator()) {
					case DECLARE_DOUBLE:
						type = new DoubleType();
						break;
					case DECLARE_LONG:
						type = new LongType();
						break;
					case DECLARE_BOOLEAN:
						type = new BooleanType();
						break;
					case DECLARE_STRING:
						type = new StringType(0L);
						break;
					case DECLARE_STRUCT:
						size = Integer.parseInt(q.getArgument1().substring(1));
						assert(size >= 0);
						type = this.parseStructDeclaration(m, tacIterator, size);
						break;
					default:
						throw new BackendException("Unknown type for array declaration");
				}

				done = true;
			}
		}

		return new LLVMBackendArrayType(dimensions, type);
	}

	private LLVMBackendStructType parseStructDeclaration(Module m, Iterator<Quadruple> tacIterator, int size) throws BackendException {
		int i = 0;
		List<Member> members = new LinkedList<Member>();

		while((i < size) && tacIterator.hasNext()) {
			Quadruple q = tacIterator.next();
			int memberSize = 0;

			switch(q.getOperator()) {
				case DECLARE_DOUBLE:
					members.add(new Member(q.getResult(), new DoubleType()));
					break;
				case DECLARE_LONG:
					members.add(new Member(q.getResult(), new LongType()));
					break;
				case DECLARE_BOOLEAN:
					members.add(new Member(q.getResult(), new BooleanType()));
					break;
				case DECLARE_STRING:
					members.add(new Member(q.getResult(), new StringType(0L)));
					break;
				case DECLARE_ARRAY:
					memberSize = Integer.parseInt(q.getArgument1().substring(1));
					assert(size>=0);
					members.add(new Member(q.getResult(), this.parseArrayDeclaration(m, tacIterator, memberSize)));
					break;
				case DECLARE_STRUCT:
					memberSize = Integer.parseInt(q.getArgument1().substring(1));
					assert(size>=0);
					members.add(new Member(q.getResult(), this.parseStructDeclaration(m, tacIterator, memberSize)));
					break;
				default:
					break;
			}

			i += 1;
		}

		return new LLVMBackendStructType(members);
	}

	/**
	 * This function generates LLVM IR code for
	 * given three address code.
	 *
	 * @param tac the three address code
	 * @return the generated LLVM IR code.
	 */
	@Override
	public Map<String, InputStream> generateTargetCode(String baseFileName, List<Quadruple> tac) throws BackendException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		PrintWriter out = new PrintWriter(outStream);

		/* Run passes on the three address code to ensure it is correct. */
		tac = basicBlocksTerminatedPass(tac);

		Module m = new Module();

		/* Write preamble */
		out.println(this.llvm_preamble);

		/* Write begin for main function */
		out.println("define i64 @main() {");

		Iterator<Quadruple> tacIterator = tac.iterator();

		/* Generate LLVM IR code corresponding
		 * to the TAC given as input */
		while(tacIterator.hasNext())
		{
			Quadruple q = tacIterator.next();
			int size = 0;

			switch(q.getOperator())
			{
				/* Variable declaration */
				case DECLARE_LONG:
					m.addPrimitiveDeclare(
						Type.Kind.LONG,
						q.getResult(),
						q.getArgument1());
					break;
				case DECLARE_DOUBLE:
					m.addPrimitiveDeclare(
						Type.Kind.DOUBLE,
						q.getResult(),
						q.getArgument1());
					break;
				case DECLARE_BOOLEAN:
					m.addPrimitiveDeclare(
						Type.Kind.BOOLEAN,
						q.getResult(),
						Module.toIRBoolean(q.getArgument1()));
					break;
				case DECLARE_STRING:
					m.addPrimitiveDeclare(
						Type.Kind.STRING,
						q.getResult(),
						Module.toIRString(q.getArgument1()));
					break;
				case DECLARE_REFERENCE:
					/* Reference declarations are only needed to keep the syntax
					 * style of the TAC homogenous, they hold no semantic relevance
					 * (as it is a meta-type) and thus get simply ignored. */
					break;
				case DECLARE_ARRAY:
					size = Integer.parseInt(q.getArgument1().substring(1));
					assert(size>=0);
					LLVMBackendArrayType array = this.parseArrayDeclaration(m, tacIterator, size);
					m.addArrayDeclare(array, q.getResult());
					break;
				case DECLARE_STRUCT:
					size = Integer.parseInt(q.getArgument1().substring(1));
					assert(size>=0);
					LLVMBackendStructType struct = this.parseStructDeclaration(m, tacIterator, size);
					m.addStructDeclare(struct, q.getResult());
					break;

				/* Type conversion */
				case LONG_TO_DOUBLE:
					m.addPrimitiveConversion(
						Type.Kind.LONG,
						q.getArgument1(),
						Type.Kind.DOUBLE,
						q.getResult());
					break;
				case DOUBLE_TO_LONG:
					m.addPrimitiveConversion(
						Type.Kind.DOUBLE,
						q.getArgument1(),
						Type.Kind.LONG,
						q.getResult());
					break;
				case BOOLEAN_TO_STRING:
					m.addPrimitiveConversion(
						Type.Kind.BOOLEAN,
						q.getArgument1(),
						Type.Kind.STRING,
						q.getResult());
					break;
				case LONG_TO_STRING:
					m.addPrimitiveConversion(
						Type.Kind.LONG,
						q.getArgument1(),
						Type.Kind.STRING,
						q.getResult());
					break;
				case DOUBLE_TO_STRING:
					m.addPrimitiveConversion(
						Type.Kind.LONG,
						q.getArgument1(),
						Type.Kind.STRING,
						q.getResult());
					break;

				/* Unindexed copy */
				case ASSIGN_LONG:
					m.addPrimitiveAssign(
						Type.Kind.LONG,
						q.getResult(),
						q.getArgument1());
					break;
				case ASSIGN_DOUBLE:
					m.addPrimitiveAssign(
						Type.Kind.DOUBLE,
						q.getResult(),
						q.getArgument1());
					break;
				case ASSIGN_BOOLEAN:
					m.addPrimitiveAssign(
						Type.Kind.BOOLEAN,
						q.getResult(),
						Module.toIRBoolean(q.getArgument1()));
					break;
				case ASSIGN_STRING:
					m.addPrimitiveAssign(
						Type.Kind.STRING,
						q.getResult(),
						Module.toIRString(q.getArgument1()));
					break;

				/* Indexed Copy */
				case ARRAY_GET_LONG:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.ARRAY,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.LONG,
						q.getResult(),
						false);
					break;
				case ARRAY_GET_DOUBLE:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.ARRAY,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.DOUBLE,
						q.getResult(),
						false);
					break;
				case ARRAY_GET_BOOLEAN:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.ARRAY,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.BOOLEAN,
						q.getResult(),
						false);
					break;
				case ARRAY_GET_STRING:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.ARRAY,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.STRING,
						q.getResult(),
						false);
					break;
				case ARRAY_GET_REFERENCE:
					m.addReferenceDerivedGet(
						Type.Kind.ARRAY,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
					break;
				case ARRAY_SET_LONG:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.ARRAY,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.LONG,
						q.getResult(),
						true);
					break;
				case ARRAY_SET_DOUBLE:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.ARRAY,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.DOUBLE,
						q.getResult(),
						true);
					break;
				case ARRAY_SET_BOOLEAN:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.ARRAY,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.BOOLEAN,
						q.getResult(),
						true);
					break;
				case ARRAY_SET_STRING:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.ARRAY,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.STRING,
						q.getResult(),
						true);
					break;
				case STRUCT_GET_LONG:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.STRUCT,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.LONG,
						q.getResult(),
						false);
					break;
				case STRUCT_GET_DOUBLE:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.STRUCT,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.DOUBLE,
						q.getResult(),
						false);
					break;
				case STRUCT_GET_BOOLEAN:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.STRUCT,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.BOOLEAN,
						q.getResult(),
						false);
					break;
				case STRUCT_GET_STRING:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.STRUCT,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.STRING,
						q.getResult(),
						false);
					break;
				case STRUCT_GET_REFERENCE:
					m.addReferenceDerivedGet(
						Type.Kind.STRUCT,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
					break;
				case STRUCT_SET_LONG:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.STRUCT,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.LONG,
						q.getResult(),
						true);
					break;
				case STRUCT_SET_DOUBLE:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.STRUCT,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.DOUBLE,
						q.getResult(),
						true);
					break;
				case STRUCT_SET_BOOLEAN:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.STRUCT,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.BOOLEAN,
						q.getResult(),
						true);
					break;
				case STRUCT_SET_STRING:
					m.addPrimitiveDerivedSetOrGet(
						Type.Kind.STRUCT,
						q.getArgument1(),
						q.getArgument2(),
						Type.Kind.STRING,
						q.getResult(),
						true);
					break;

				/* Arithmetic */
				case ADD_LONG:
					m.addPrimitiveBinaryInstruction(
						q.getOperator(),
						Type.Kind.LONG,
						Type.Kind.LONG,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
					break;
				case ADD_DOUBLE:
					m.addPrimitiveBinaryInstruction(
						q.getOperator(),
						Type.Kind.DOUBLE,
						Type.Kind.DOUBLE,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
					break;
				case SUB_LONG:
					m.addPrimitiveBinaryInstruction(
						q.getOperator(),
						Type.Kind.LONG,
						Type.Kind.LONG,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
					break;
				case SUB_DOUBLE:
					m.addPrimitiveBinaryInstruction(
						q.getOperator(),
						Type.Kind.DOUBLE,
						Type.Kind.DOUBLE,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
					break;
				case MUL_LONG:
					m.addPrimitiveBinaryInstruction(
						q.getOperator(),
						Type.Kind.LONG,
						Type.Kind.LONG,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
					break;
				case MUL_DOUBLE:
					m.addPrimitiveBinaryInstruction(
						q.getOperator(),
						Type.Kind.DOUBLE,
						Type.Kind.DOUBLE,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
					break;
				case DIV_LONG:
					m.addPrimitiveBinaryCall(
						q.getOperator(),
						Type.Kind.LONG,
						Type.Kind.LONG,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
					break;
				case DIV_DOUBLE:
					m.addPrimitiveBinaryCall(
						q.getOperator(),
						Type.Kind.DOUBLE,
						Type.Kind.DOUBLE,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
					break;
				case NOT_BOOLEAN:
					m.addBooleanNot(Module.toIRBoolean(q.getArgument1()), q.getResult());
					break;
				case OR_BOOLEAN:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.BOOLEAN,
							Type.Kind.BOOLEAN,
							Module.toIRBoolean(q.getArgument1()),
							Module.toIRBoolean(q.getArgument2()),
							q.getResult());
					break;
				case AND_BOOLEAN:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.BOOLEAN,
							Type.Kind.BOOLEAN,
							Module.toIRBoolean(q.getArgument1()),
							Module.toIRBoolean(q.getArgument2()),
							q.getResult());
					break;
				case CONCAT_STRING:
					m.addPrimitiveBinaryCall(
						q.getOperator(),
						Type.Kind.STRING,
						Type.Kind.STRING,
						Module.toIRString(q.getArgument1()),
						Module.toIRString(q.getArgument2()),
						q.getResult());
					break;

				/* Comparisons */
				case COMPARE_LONG_E:
				m.addPrimitiveBinaryInstruction(
						q.getOperator(),
						Type.Kind.BOOLEAN,
						Type.Kind.LONG,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
				break;
				case COMPARE_LONG_G:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.LONG,
							Type.Kind.BOOLEAN,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_LONG_L:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.BOOLEAN,
							Type.Kind.LONG,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_LONG_GE:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.BOOLEAN,
							Type.Kind.LONG,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_LONG_LE:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.BOOLEAN,
							Type.Kind.LONG,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_DOUBLE_E:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.BOOLEAN,
							Type.Kind.DOUBLE,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_DOUBLE_G:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.BOOLEAN,
							Type.Kind.DOUBLE,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_DOUBLE_L:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.BOOLEAN,
							Type.Kind.DOUBLE,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_DOUBLE_GE:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.BOOLEAN,
							Type.Kind.DOUBLE,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_DOUBLE_LE:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.BOOLEAN,
							Type.Kind.DOUBLE,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;

				/* Control flow */
				case RETURN:
					m.addMainReturn(q.getArgument1());
					break;
				case LABEL:
					m.addLabel(q.getArgument1());
					break;
				case BRANCH:
					m.addBranch(q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;

				/* Print */
				case PRINT_STRING:
					m.addPrint(Module.toIRString(q.getArgument1()), Type.Kind.STRING);
					break;
			}
		}

		/* Write the generated LLVM IR code */
		out.print(m.getCode());

		/* Write handler for uncaught exceptions */
		out.print(this.llvm_uncaught);

		/* Write end for main function */
		out.println("}");

		/* Finish writing */
		out.close();

		/* Convert written target code to readable input format */
		Map<String,InputStream> map = new HashMap<String,InputStream>();
		map.put(baseFileName+".ll", new ByteArrayInputStream(outStream.toByteArray()));

		/* Return the map containing the readable target code */
		return map;
	}
}
