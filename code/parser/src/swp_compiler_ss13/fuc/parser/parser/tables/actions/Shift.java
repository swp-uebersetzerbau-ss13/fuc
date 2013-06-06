package swp_compiler_ss13.fuc.parser.parser.tables.actions;

import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;

public class Shift extends ALRAction {

	private final LRParserState newState;
//	private final Item item;

	public Shift(LRParserState newState) {	//, Item item) {
		super(ELRActionType.SHIFT);
		this.newState = newState;
//		this.item = item;
	}

	public LRParserState getNewState() {
		return newState;
	}

//	public Item getItem() {
//		return item;
//	}

//	@Override
//	public String toString() {
//		return "[shift " + item.toString() + "]";
//	}
}