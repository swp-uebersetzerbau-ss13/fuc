package swp_compiler_ss13.fuc.ir.components.statements.expressions;

import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.fuc.ir.GeneratorExecutor;
import swp_compiler_ss13.fuc.ir.GeneratorState;
import swp_compiler_ss13.fuc.ir.components.NodeProcessor;

/**
 * Processor for a literal node
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class LiteralNodeProcessor extends NodeProcessor {

	/**
	 * Create a new instance of the literal node processor.
	 * 
	 * @param state
	 *            the state of the intermediate code generator
	 * @param executor
	 *            the intermediate code generator executor
	 */
	public LiteralNodeProcessor(GeneratorState state, GeneratorExecutor executor) {
		super(state, executor);
	}

	/**
	 * Process the literal node.
	 * 
	 * @param literal
	 *            the literal node to process.
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred while processing the node.
	 */
	public void processLiteralNode(LiteralNode literal) throws IntermediateCodeGeneratorException {
		String literalValue = literal.getLiteral();
		Type literalType = literal.getLiteralType();

		switch (literalType.getKind()) {
			case BOOLEAN:
				// convert true and false to uppercase
				this.state.pushIntermediateResult(literalType, "#" + literalValue.toUpperCase());
				break;
			case DOUBLE:
			case LONG:
				this.state.pushIntermediateResult(literalType, "#" + literalValue);
				break;
			case STRING:
				// escape the string
				literalValue = this.escape(literalValue);
				// prepend and append a quote
				this.state.pushIntermediateResult(literalType, "#\"" + literalValue + "\"");
				break;
			default:
				// literals of other types (array, struct) are not supported
				String err = String.format("A literal of Type %s is not supported",
						literalType.toString());
				NodeProcessor.logger.fatal(err);
				throw new IntermediateCodeGeneratorException(err);

		}
	}

	/**
	 * Escape a string with c-style escapes.
	 * 
	 * @param literal
	 *            The string to escape.
	 * @return The c-style escaped string.
	 */
	private String escape(String literal) {
		// if the string starts and end with double quotes
		// remove the quotes. We will add them again later and
		// do not want to have them twice.
		// Condition:
		// The literal starts with " and ends with "
		// and the last " is not an escaped quote (\")
		if (literal.startsWith("\"") && literal.endsWith("\"") && !literal.endsWith("\\\"")) {
			literal = literal.substring(1, literal.length() - 1);
		}

		// Make C-Style escapings, only if they are not escaped already
		literal = this.escapeIfNeeded(literal, "\"", "\\\"");
		literal = this.escapeIfNeeded(literal, "\n", "\\n");
		literal = this.escapeIfNeeded(literal, "\r", "\\r");
		literal = this.escapeIfNeeded(literal, "\t", "\\t");
		literal = this.escapeIfNeeded(literal, "\0", "\\0");
		return literal;
	}

	/**
	 * Replace the search string with the replace string inside the literal string. Only replace if
	 * the substring was not already replaced to prevent double replacement.
	 * 
	 * @param literal
	 *            The literal to replace substring inside.
	 * @param search
	 *            The substring to replace.
	 * @param replace
	 *            The replacement to replace matches with.
	 * @return The string where all occurrences of search are replace by replace.
	 */
	private String escapeIfNeeded(String literal, String search, String replace) {
		int fromIndex = 0;
		int pos = 0;
		while ((pos = literal.indexOf(search, fromIndex)) >= 0) {
			// for every occurrence of the search string
			// check if the occurrence is already escaped
			if ((pos > 0) && (literal.charAt(pos - 1) == '\\')) {
				// skip this occurrence as it is already escaped
				fromIndex = pos + 1;
				continue;
			}
			fromIndex = pos + replace.length();
			if (pos > 0) {
				literal = literal.substring(0, pos) + replace + literal.substring(pos + 1);
			}
			else {
				literal = replace + literal.substring(pos + 1);
			}
		}
		return literal;
	}
}
