package swp_compiler_ss13.fuc.backend;

import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class describes a just-in-time compiler
 * for three address code to LLVM IR code, that
 * can also execute the generated code via
 * LLVM's <code>lli</code> command and show the
 * result of that execution (exit code).
 * The format for the text-style TAC is:
 * "Operator|Arg1|Arg2|Res" (without the quotes)
 *
 */
public class TACExecutor
{
	/**
	 * Reads three address code from an input stream
	 * and formats it to the list of <code>Quadruple</code>'s
	 * the <code>Backend</code> requires.
	 *
	 * @param stream the stream containing one TAC operation per line
	 * @return the formatted TAC
	 * @exception IOException if an error occurs
	 */
	public static List<Quadruple> readTAC(InputStream stream) throws IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
		List<Quadruple> tac = new ArrayList<Quadruple>();

		String line = null;
		while((line = in.readLine()) != null)
		{
			String[] tupleFields = line.split("\\|");
			Quadruple q = new Q(Quadruple.Operator.valueOf(tupleFields[0]),
			                    tupleFields[1],
			                    tupleFields[2],
			                    tupleFields[3]);
			tac.add(q);
		}

		return tac;
	}

	/**
	 * Reads three address code from an input stream,
	 * calls the <code>LLVMBackend</code> with it
	 * and returns the generated LLVM IR code.
	 *
	 * @param stream the stream containing one TAC operation per line
	 * @return the generated LLVM IR code
	 * @exception IOException if an error occurs
	 */
	public static InputStream jitTAC(InputStream stream) throws IOException, BackendException {
		Backend b = new LLVMBackend();

		List<Quadruple> tac = readTAC(stream);

		Map.Entry<String,InputStream> entry = b.generateTargetCode("", tac).entrySet().iterator().next();
		InputStream targetCode = entry.getValue();
		String name = entry.getKey();

		return targetCode;
	}

	/**
	 * Reads three address code from an input stream,
	 * just-in-time compiles it to LLVM IR code,
	 * exectues the generated code via LLVM's
	 * <code>lli</code> tool show the result
	 * of that execution (exit code).
	 *
	 * @param stream an <code>InputStream</code> value
	 * @exception IOException if an error occurs
	 * @exception InterruptedException if an error occurs
	 */
	public static int runTAC(InputStream stream) throws IOException, InterruptedException, BackendException {
		BufferedReader in = new BufferedReader(new InputStreamReader(jitTAC(stream)));

		ProcessBuilder pb = new ProcessBuilder("lli", "-");
		pb.redirectErrorStream(true);
		Process p = pb.start();
		PrintWriter out = new PrintWriter(p.getOutputStream());

		System.out.println("\nGenerated LLVM IR:\n");

		String line = null;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
			out.println(line);
		}

		out.close();
		System.out.println("Executing on LLVM...\n<execution output=\"stdout,stderr\">");

		in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		line = null;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
			out.println(line);
		}

		int exitCodeLLI = p.waitFor();

		System.out.println("</execution>\nThe execution of that code yielded: " + String.valueOf(exitCodeLLI));

		return exitCodeLLI;
	}

	/**
	 * Just-in-time compiles and executes text-style TAC, either:
	 * Command line arguments exist:
	 *   Every argument is a file name and the contents
	 *   will be jitted and executed.
	 * No command line arguments:
	 *   The standard input (stdin) will be read from until
	 *   end-of-file (EOF) is found and everything read will
	 *   be jitted and executed.
	 *
	 *
	 * @param args file names to jit and execute
	 * @exception IOException if an error occurs
	 * @exception InterruptedException if an error occurs
	 */
	public static void main(String[] args) throws IOException, InterruptedException, BackendException {
		if (args.length > 0) {
			for (String arg : args) {
				System.out.println("Generating LLVM IR for " + arg);
				runTAC(new FileInputStream(arg));
			}
		} 
		else {
			System.out.println("Generating LLVM IR for stdin, please enter" + " one quadruple per line in the format "
					+ "\"Operator|Arg1|Arg2|Res\" (without the quotes):");
			runTAC(System.in);
		}
	}

	/**
	 * A bare-bones implementation of the
	 * <code>Quadruple</code> interface used to format
	 * the text-style TAC to <code>Quadruple</code>-TAC
	 *
	 */
	private static class Q implements Quadruple
	{
		private Operator operator;
		private String argument1;
		private String argument2;
		private String result;

		public Q(Operator o, String a1, String a2, String r)
		{
			operator = o;
			argument1 = a1;
			argument2 = a2;
			result = r;
		}

		public String toString() { return "(" + String.valueOf(operator) + "|" + argument1  + "|" + argument2 + "|" + result + ")"; }

		public Operator getOperator() { return operator; }
		public String getArgument1() { return argument1; }
		public String getArgument2() { return argument2; }
		public String getResult() { return result; }
	}
}