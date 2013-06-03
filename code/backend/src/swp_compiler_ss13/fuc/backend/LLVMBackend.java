package swp_compiler_ss13.fuc.backend;

import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.types.Type;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		Module m = new Module(out);

		/* Write preamble */
		out.println(this.llvm_preamble);

		/* Write begin for main function */
		out.println("define i64 @main() {");

		for(Quadruple q : tac)
		{
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

				/* Arithmetic */
				case ADD_LONG:
					m.addPrimitiveBinaryInstruction(
						q.getOperator(),
						Type.Kind.LONG,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
					break;
				case ADD_DOUBLE:
					m.addPrimitiveBinaryInstruction(
						q.getOperator(),
						Type.Kind.DOUBLE,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
					break;
				case SUB_LONG:
					m.addPrimitiveBinaryInstruction(
						q.getOperator(),
						Type.Kind.LONG,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
					break;
				case SUB_DOUBLE:
					m.addPrimitiveBinaryInstruction(
						q.getOperator(),
						Type.Kind.DOUBLE,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
					break;
				case MUL_LONG:
					m.addPrimitiveBinaryInstruction(
						q.getOperator(),
						Type.Kind.LONG,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
					break;
				case MUL_DOUBLE:
					m.addPrimitiveBinaryInstruction(
						q.getOperator(),
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
							Module.toIRBoolean(q.getArgument1()),
							Module.toIRBoolean(q.getArgument2()),
							q.getResult());
					break;
				case AND_BOOLEAN:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.BOOLEAN,
							Module.toIRBoolean(q.getArgument1()),
							Module.toIRBoolean(q.getArgument2()),
							q.getResult());
					break;

				/* Comparisons */
				case COMPARE_LONG_E:
				m.addPrimitiveBinaryInstruction(
						q.getOperator(),
						Type.Kind.LONG,
						q.getArgument1(),
						q.getArgument2(),
						q.getResult());
				break;
				case COMPARE_LONG_G:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.LONG,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_LONG_L:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.LONG,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_LONG_GE:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.LONG,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_LONG_LE:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.LONG,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_DOUBLE_E:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.DOUBLE,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_DOUBLE_G:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.DOUBLE,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_DOUBLE_L:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.DOUBLE,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_DOUBLE_GE:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
							Type.Kind.DOUBLE,
							q.getArgument1(),
							q.getArgument2(),
							q.getResult());
					break;
				case COMPARE_DOUBLE_LE:
					m.addPrimitiveBinaryInstruction(
							q.getOperator(),
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
				case PRINT_LONG:
					m.addPrint(q.getArgument1(), Type.Kind.LONG);
					break;
				case PRINT_DOUBLE:
					m.addPrint(q.getArgument1(), Type.Kind.DOUBLE);
					break;
				case PRINT_BOOLEAN:
					m.addPrint(Module.toIRBoolean(q.getArgument1()), Type.Kind.BOOLEAN);
					break;
				case PRINT_STRING:
					m.addPrint(Module.toIRString(q.getArgument1()), Type.Kind.STRING);
					break;
			}
		}

		/* Write return 0 if last operation is not a return */
		if(tac.size() == 0 || tac.get(tac.size() - 1).getOperator() != Quadruple.Operator.RETURN)
		{
			out.println("  ret i64 0");
		}

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