package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import java.util.List;

public class Item extends Production {
	private int dotPos;

	
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
	public Symbol getNextSymbol() {
		if( dotPos < this.getRight().size() ) {
			return getRight().get( dotPos );
		}
		return null;
	}
	public boolean isComplete() {
		return dotPos == getRight().size();
	}
	public Item shift() {
		if (isComplete()) {
			return null;
		}
		return new Item(getLeft(), getRight(), dotPos + 1);
	}
	public String getStringItem() {
		String ret = getLeft().getString();
		ret += " ->";
		int pos = 0;
		for (Symbol s : getRight()) {
			if( pos == getDotPos())
				ret += " .";
			ret += (" " + s.getString());
			pos ++;
		}
		if( getDotPos() == getRight().size())
			ret += " .";
		return ret;
	}
	@Override
	public int compareTo(Production other) {
		if(other instanceof Item)
		{
			Item otherItem = (Item )other;
			if(super.compareTo(otherItem) == 0)
				return this.getDotPos() - otherItem.getDotPos();
		}
		return super.compareTo(other);
	}
	@Override
	public boolean equals(Object other) {
		//System.out.println("item.equals called");
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
	@Override
	public String toString() {
		String result = getLeft().toString() + " -> ";
		for (int i = 0; i < getRight().size(); i++) {
			Symbol s = getRight().get(i);
			if (i == dotPos) {
				result += " .";
			}
			result += " " + s.toString();
		}
		return result;
	}
}
