package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import java.util.List;

public class Item extends Production {

	public Item(Variable left, List<Symbol> right, int dotPos) {
		super(left, right);
		this.dotPos = dotPos;
	}
	public Item( Production prod, int dotPos ) {
		super( prod.getLeft(), prod.getRight() );
		this.dotPos = dotPos;
	}
	public int getDotPos() {
		return dotPos;
	}
	public Symbol getSymbolAfterDot() {
		if( dotPos < this.getRight().size() ) {
			return getRight().get( dotPos );
		}
		return null;
	}
	@Override
	public int compareTo(Production other) {
		if(other instanceof Item)
		{
			Item otherItem = (Item )other;
			if(super.compareTo(otherItem) == 0)
				return otherItem.getDotPos() - this.getDotPos();
		}
		return super.compareTo(other);
	}
	@Override
	public boolean equals(Object other) {
		if( other instanceof Item)
		{
			Item otherItem = (Item )other;
			return (compareTo(otherItem) == 0);
		}
		return super.equals(other);
	}
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	private int dotPos;
}
