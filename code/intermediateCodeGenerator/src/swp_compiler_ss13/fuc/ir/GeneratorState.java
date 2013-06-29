package swp_compiler_ss13.fuc.ir;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.fuc.ir.data.IntermediateResult;
import swp_compiler_ss13.fuc.ir.data.LoggingLinkedList;
import swp_compiler_ss13.fuc.ir.data.LoggingStack;

/**
 * This objects holds the current state of the IntermediateCodeGenerator.
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class GeneratorState {

	/**
	 * The logger instance
	 */
	private static Logger logger = Logger.getLogger(GeneratorState.class);

	/**
	 * Prefix for label names
	 */
	private static final String LABEL_PREFIX = "label";

	/**
	 * A list to store the generated intermediate code.
	 */
	private final List<Quadruple> intermediateCode;

	/**
	 * The stack for intermediate results.
	 */
	private final Stack<IntermediateResult> intermediateResults;

	/**
	 * The stack for symbol tables.
	 */
	private final Stack<SymbolTable> symbolTables;

	/**
	 * The stack for break labels.
	 */
	private final Stack<String> breakLabels;

	/**
	 * Set of all identifier names used.
	 */
	private final Set<String> usedIdentifierNames;

	/**
	 * Number of next free label
	 */
	private int label;

	/**
	 * Create a new instance of the GeneratorState class.
	 */
	public GeneratorState() {
		this.intermediateCode = new LoggingLinkedList<Quadruple>();
		this.intermediateResults = new LoggingStack<IntermediateResult>();
		this.symbolTables = new Stack<SymbolTable>();
		this.breakLabels = new Stack<String>();
		this.usedIdentifierNames = new HashSet<String>();
		this.label = 0;
	}

	/**
	 * Reset the generator state object to the state of initializing.
	 */
	public void reset() {
		this.intermediateCode.clear();
		this.intermediateResults.clear();
		this.symbolTables.clear();
		this.breakLabels.clear();
		this.usedIdentifierNames.clear();
		this.label = 0;
	}

	/**
	 * Return the intermediate code that was created up to the current state.
	 * 
	 * @return The intermediate code (three address code - tac).
	 */
	public List<Quadruple> getIntermediateCode() {
		return this.intermediateCode;
	}

	/**
	 * Pop an intermediate result from the stack of intermediate results.
	 * 
	 * @return The popped intermediate result.
	 */
	public IntermediateResult popIntermediateResult() {
		return this.intermediateResults.pop();
	}

	/**
	 * Push the intermediate result to the stack of intermediate results.
	 * 
	 * @param result
	 *            The intermediate result to push to the stack.
	 */
	public void pushIntermediateResult(IntermediateResult result) {
		this.intermediateResults.push(result);
	}

	/**
	 * Push the intermediate result to the stack of intermediate results.
	 * 
	 * @param type
	 *            The type of the intermediate result.
	 * @param value
	 *            The value of the intermediate result.
	 */
	public void pushIntermediateResult(Type type, String value) {
		this.pushIntermediateResult(new IntermediateResult(type, value));
	}

	/**
	 * Pop a symbol table from the stack of symbol tables.
	 * 
	 * @return the popped symbol table.
	 */
	public SymbolTable popSymbolTable() {
		return this.symbolTables.pop();
	}

	/**
	 * Push a symbol table to the stack of symbol tables.
	 * 
	 * @param symbolTable
	 *            the symbol table to push
	 */
	public void pushSymbolTable(SymbolTable symbolTable) {
		this.symbolTables.push(symbolTable);
	}

	/**
	 * Get the current symbol table from the stack of symbol tables.
	 * 
	 * @return symbolTable the symbol table currently on top of the stack.
	 */
	public SymbolTable getCurrentSymbolTable() {
		return this.symbolTables.peek();
	}

	/**
	 * Save a new identifier and rename it if needed.
	 * 
	 * @param identifierName
	 *            The identifier to save
	 * @param type
	 *            The type of the identifier
	 * @return The renamed identifier
	 */
	public String saveIdentifier(String identifierName, Type type) {
		SymbolTable currentSymbolTable = this.getCurrentSymbolTable();

		if (this.usedIdentifierNames.contains(identifierName)) {
			// the identifier name was already used
			// assign a new name to this identifier
			String newName = currentSymbolTable.getNextFreeTemporary();
			currentSymbolTable.putTemporary(newName, type);
			currentSymbolTable.setIdentifierAlias(identifierName, newName);
			identifierName = newName;
		}

		this.usedIdentifierNames.add(identifierName);
		return identifierName;
	}

	/**
	 * Load the given identifier and return its actual name (if renaming was done)
	 * 
	 * @param id
	 *            The identifier name to load
	 * @return The actual name of the identifier
	 * @throws IntermediateCodeGeneratorException
	 *             Identifier was not found
	 */
	public String loadIdentifier(String id) throws IntermediateCodeGeneratorException {
		String name = this.getCurrentSymbolTable().getIdentifierAlias(id);
		if (name == null) {
			GeneratorState.logger.fatal("Undeclared variable found: " + id);
			throw new IntermediateCodeGeneratorException("Undeclared variable found: " + id);
		}
		return name;
	}

	/**
	 * Create a new label name
	 * 
	 * @return the new label
	 */
	public String createNewLabel() {
		return GeneratorState.LABEL_PREFIX + (this.label++);
	}

	/**
	 * Push a new label to the break label stack
	 * 
	 * @param label
	 *            The label to push
	 */
	public void pushBreakLabel(String label) {
		this.breakLabels.push(label);
	}

	/**
	 * Pop a label from the break label stack
	 * 
	 * @return The popped label
	 */
	public String popBreakLabel() {
		return this.breakLabels.pop();
	}

	/**
	 * Add Intermediate Code (Quadruples)
	 * 
	 * @param quadruples
	 *            the quadruples to add
	 */
	public void addIntermediateCode(List<Quadruple> quadruples) {
		this.intermediateCode.addAll(quadruples);
	}

	/**
	 * Add Intermediate Code (Quadruple)
	 * 
	 * @param quadruple
	 *            the quadruple to add
	 */
	public void addIntermediateCode(Quadruple quadruple) {
		this.intermediateCode.add(quadruple);
	}

	/**
	 * Get the current break label
	 * 
	 * @return The label to use to jump out of the current loop
	 */
	public String getCurrentBreakLabel() {
		return this.breakLabels.peek();
	}

	/**
	 * Get the next free temporary identifier
	 * 
	 * @param type
	 *            The type of the temporary identifier
	 * @return The name of the temporary
	 */
	public String nextTemporaryIdentifier(Type type) {
		String id = this.getCurrentSymbolTable().getNextFreeTemporary();
		this.getCurrentSymbolTable().putTemporary(id, type);
		this.usedIdentifierNames.add(id);
		return id;
	}

}
