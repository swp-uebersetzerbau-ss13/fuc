package swp_compiler_ss13.fuc.backend;

import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * This class describes a just-in-time compiler
 * for three address code to LLVM IR code, that
 * can also execute the generated code via
 * LLVM's <code>lli</code> command and show the
 * result of that execution (exit code and execution output).
 * The format for the text-style TAC is:
 * "Operator|Arg1|Arg2|Res" (without the quotes)
 *
 */
public class TACExecutor
{

	private static Logger logger = Logger.getLogger(TACExecutor.class);

	/**
	 * Tries to start <code>lli</code> as a process.
	 * @return the process identifier
	 * @throws IOException if <code>lli</code> is not found
	 */
	private static Process tryToStartLLI() throws IOException {
		ProcessBuilder pb = new ProcessBuilder("lli", "-jit-enable-eh", "-");
		pb.redirectErrorStream(true);
		Process p = null;
		try {
			p = pb.start();
		} catch (IOException e) {
			String errorMsg = "If you have LLVM installed you might need to check your PATH:\n" +
					"Intellij IDEA: Run -> Edit Configurations -> Environment variables\n" +
					"Eclipse: Run Configurations -> Environment\n" +
					"Shell: Check $PATH";
			logger.error("No lli (interpreter and dynamic compiler, part of LLVM) found.");
			logger.info(errorMsg);
			logger.error(e.getStackTrace());
			throw e;
		}
		return p;
	}

	/**
	 * Exectues LLVM IR code via LLVM's <code>lli</code> tool and shows the
	 * result of that execution (exit code and output from the programm).
	 *
	 * @param irCode
	 *            an <code>InputStream</code> of LLVM IR Code
	 * @return the output and exit code of the execution of the LLVM IR code
	 * @exception java.io.IOException
	 *                if an error occurs
	 * @exception InterruptedException
	 *                if an error occurs
	 * @throws swp_compiler_ss13.common.backend.BackendException
	 *             if an error occurs
	 */
	public static ExecutionResult runIR(InputStream irCode) throws InterruptedException, BackendException, IOException {

		BufferedReader irCodeReader = new BufferedReader(new InputStreamReader(irCode));
		StringBuilder irCodeStringBuilder = new StringBuilder();

		Process p = tryToStartLLI();

		/* write LLVM IR code to stdin of the lli process */
		PrintWriter processInputStream = new PrintWriter(p.getOutputStream());
		String line = null;
		while ((line = irCodeReader.readLine()) != null) {
			irCodeStringBuilder.append(line + "\n");
			processInputStream.println(line);
		}
		processInputStream.close();

		/* read stdout from lli process */
		BufferedReader outPutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuilder executionOutput = new StringBuilder();
		line = null;
		while ((line = outPutReader.readLine()) != null) {
			executionOutput.append(line + "\n");
		}

		int executionExitCode = p.waitFor();

		return new ExecutionResult(executionOutput.toString(), executionExitCode, irCodeStringBuilder.toString());
	}


	/**
	 * Reads three address code (as quadruples) from an input stream, Calls the
	 * <code>LLVMBackend</code> with the quadruple list of TAC instructions and
	 * returns the generated LLVM IR code.
	 *
	 * @param tac the TAC instructions as list of quadruples
	 * @return the generated LLVM IR code as <code>InputStream</code>
	 * @throws IOException
	 * @throws BackendException
	 */
	private static InputStream compileTAC(List<Quadruple> tac) throws IOException, BackendException {
		Backend b = new LLVMBackend();

		Map.Entry<String, InputStream> entry = b.generateTargetCode("", tac).entrySet().iterator().next();
		InputStream targetCode = entry.getValue();
		String name = entry.getKey();

		return targetCode;
	}


	/**
	 * Reads three address code from an input stream
	 * and formats it to the list of <code>Quadruple</code>'s
	 * the <code>Backend</code> requires.
	 *
	 * @param stream the stream containing one TAC operation per line
	 * @return the formatted TAC
	 * @exception IOException if an error occurs
	 */
	private static List<Quadruple> readTAC(InputStream stream) throws IOException
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
	 * Reads three address in text-style TAC code from an <code>InputStream</code>,
	 * just-in-time compiles it to LLVM IR code, executes the generated code via
	 * LLVM's <code>lli</code> tool show the result of that execution (execution
	 * output and exit code).
	 *
	 * @param stream
	 *            an <code>InputStream</code> of text-style TAC
	 * @return the exit code of the execution of the llvm ir code
	 * @exception IOException
	 * @exception InterruptedException
	 * @throws BackendException
	 */
	public static ExecutionResult runTAC(InputStream stream) throws IOException, InterruptedException, BackendException {
		List<Quadruple> quadruples = readTAC(stream);
		InputStream irCode = compileTAC(quadruples);

		ExecutionResult result = runIR(irCode);

		logger.info("Generated LLVM IR:\n" + result.irCode);

		logger.info("Execution output (stdout,stderr):\n" + result.output);

		logger.info("Exit code of execution: " + String.valueOf(result.exitCode));

		return result;
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
				logger.info("Generating LLVM IR for " + arg);
				runTAC(new FileInputStream(arg));
			}
		}
		else {
			logger.info("Generating LLVM IR for stdin, please enter" + " one quadruple per line in the format "
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

	/**
	 * The result of executing LLVM IR Code. The result consists of the output
	 * and the exit code of the execution and the IR code that was executed.
	 */
	public static class ExecutionResult {
		public String output;
		public int exitCode;
		public String irCode;

		public ExecutionResult(String output, int exitCode, String irCode) {
			this.output = output;
			this.exitCode = exitCode;
			this.irCode = irCode;
		}
	}
}