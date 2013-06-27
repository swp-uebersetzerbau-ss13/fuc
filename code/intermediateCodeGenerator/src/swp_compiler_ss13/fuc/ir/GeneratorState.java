package swp_compiler_ss13.fuc.ir;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.parser.SymbolTable;

public class GeneratorState {

	private IntermediateCodeGeneratorImpl irgen;
	/**
	 * List of used names. This is needed for single static assignment.
	 */
	private List<String> usedNames;

	/**
	 * The stack of symbol tables
	 */
	private Stack<SymbolTable> currentSymbolTable;

	/**
	 * Store for intermediate results
	 */
	private WatchedStack<IntermediateResult> intermediateResults;

	/**
	 * Name of the label to break the current loop
	 */
	// FIXME: Needs to be a stack of labels
	private String loopBreakLabel;


	/**
	 * Number of the next free label
	 */
	private static long labelNum = 0L;

	/**
	 * The generated intermediate code
	 */
	private WatchedList<Quadruple> irCode;

	public List<String> getUsedNames() {
		return usedNames;
	}

	public void setUsedNames(List<String> usedNames) {
		this.usedNames = usedNames;
	}

	public Stack<SymbolTable> getCurrentSymbolTable() {
		return currentSymbolTable;
	}

	public void setCurrentSymbolTable(Stack<SymbolTable> currentSymbolTable) {
		this.currentSymbolTable = currentSymbolTable;
	}

	public WatchedStack<IntermediateResult> getIntermediateResults() {
		return intermediateResults;
	}

	public void setIntermediateResults(
			WatchedStack<IntermediateResult> intermediateResults) {
		this.intermediateResults = intermediateResults;
	}

	public String getLoopBreakLabel() {
		return loopBreakLabel;
	}

	public void setLoopBreakLabel(String loopBreakLabel) {
		this.loopBreakLabel = loopBreakLabel;
	}

	public GeneratorState(
			IntermediateCodeGeneratorImpl intermediateCodeGeneratorImpl) {
		this.irgen = intermediateCodeGeneratorImpl;
	}

	public void reset() {
		this.irCode = new WatchedList<>(new LinkedList<Quadruple>());
		this.usedNames = new LinkedList<>();
		this.currentSymbolTable = new Stack<>();
		this.intermediateResults = new WatchedStack<IntermediateResult>();
	}
	
	public void process(ASTNode node) throws IntermediateCodeGeneratorException {
		this.irgen.callProcessing(node);
	}

}
