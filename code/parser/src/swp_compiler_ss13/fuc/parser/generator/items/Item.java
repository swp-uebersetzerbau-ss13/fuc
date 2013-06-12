package swp_compiler_ss13.fuc.parser.generator.items;

import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;

/**
 * Basic interface for a {@link Production} associated with a position ("dot").
 * 
 * @author Gero
 */
public interface Item {
	public Production getProduction();

	public int getPosition();

	/**
	 * @return The next {@link Symbol} (after the dot) in the {@link Production}
	 *         . <code>null</code> if {@link #isComplete()}
	 */
	public Symbol getNextSymbol();

	/**
	 * @return If there are still {@link Symbol}s left in the prodution. Reverse of {@link #isComplete()}
	 */
	public boolean isShiftable();

	/**
	 * @return Reverse of {@link #isShiftable()}
	 */
	public boolean isComplete();

	/**
	 * @return Creates a new instance with the position = position + 1
	 */
	public Item shift();
	
	public LR0Item getLR0Kernel();
}
