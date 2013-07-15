package swp_compiler_ss13.fuc.parser.grammar;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * The LR-generator assumes operators to be left-associative by default.
 * This class allows to set operators to be right-associative, too.
 */
public class OpAssociativities {
	public enum EAssociativityType {
		/** the default */
		LEFT,
		RIGHT;
	}
	
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static Logger LOG = Logger.getLogger(OpAssociativities.class);
	
	private final Map<Terminal, EAssociativityType> assocs = new HashMap<>();

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public OpAssociativities() {
	
	}

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public OpAssociativities set(Terminal terminal, EAssociativityType assoc) {
		if (assocs.containsKey(terminal)) {
			LOG.warn("Former associativity for terminal '" + terminal + "' is overwritten!");
		}
		
		assocs.put(terminal, assoc);
		return this;	// Convenience for chaining
	}
	
	/**
	 * @param terminal
	 * @return Whether this terminal is left-associative (default)
	 */
	public boolean isLeftAssociative(Terminal terminal) {
		EAssociativityType assoc = assocs.get(terminal);
		return assoc == null || assoc == EAssociativityType.LEFT;
	}
	
	/**
	 * @param terminal
	 * @return Whether this terminal is right-associative
	 */
	public boolean isRightAssociative(Terminal terminal) {
		EAssociativityType assoc = assocs.get(terminal);
		return assoc == EAssociativityType.RIGHT;
	}

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
