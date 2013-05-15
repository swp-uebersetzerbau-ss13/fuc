package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.interfaces.ParseTable;

public class ParseTableImpl implements ParseTable {
	@Override
	public ParseTableEntry getEntry(int state, Token symbol) throws StateOutOfBoundsException, TokenNotFoundException {
		//return entries.get(new Index( state, new Terminal(symbol) ));
		if( state < entries.size() )
			throw new StateOutOfBoundsException();
		int indexCol = tokens.indexOf(symbol);
		if( indexCol == -1 )
			throw new TokenNotFoundException();
		return entries.get( state ).get( indexCol );
	}
	
	/**
	 * create parseTable
	 * 
	 * @param stateCount is the number of possible states, that serve as an index for the rows of the table
	 * @param tokens is a list of the possible tokens, that server as index for the columns
	 */
	public ParseTableImpl(int stateCount, List<Terminal> tokens) {
		this.stateCount = stateCount;
		this.tokens = new ArrayList<Terminal>(tokens);
		//this.entries = new HashMap<Index,ParseTableEntry>();
		this.entries = new ArrayList<List<ParseTableEntry>>();
		addRows( stateCount );
	}
	
	public void addEntry(int state, Terminal symbol, ParseTableEntry entry) throws AlreadySetException, TokenNotFoundException {
		int indexCol = tokens.indexOf(symbol);
		if( indexCol == -1 )
			throw new TokenNotFoundException();
		int missingRowsCount = state - (entries.size() -1) ;
		if( missingRowsCount > 0 ) {
			// add as many columns as needed:
			addRows( missingRowsCount );
		}
		if( entries.get(state).get( indexCol ) != null )
			throw new AlreadySetException();
		entries.get(state).set(indexCol, entry);
	}
		
	private void addRows( int count ) {
		for( int indexState=0; indexState<stateCount; indexState++ ) {
			ArrayList<ParseTableEntry> row = new ArrayList<ParseTableEntry>();
			this.entries.add( row );
			for(int iToken=0; iToken<tokens.size(); iToken++)
				row.add(null);
		}
	}
		
		//Index index = new Index(state, symbol);
		/*if(entries.containsKey(index))
			throw new AlreadySetException();
		entries.put(index, entry);*/
	
	void setStateCount(int count) {
		this.stateCount = count;
	}
	
	public class AlreadySetException extends Exception {
		AlreadySetException() { super(); };
		AlreadySetException(String message) { super(message); };
		private static final long serialVersionUID = 1L;
	};
	
	/*private class Index implements Comparable<Index>{
		public Index(int state, Terminal symbol) {
			this.state = state;
			this.symbol = tokens.indexOf(symbol);
		}
		private int state;
		private int symbol;
		
		@Override
		public int compareTo(Index other) {
			return other.hashCode() - hashCode();
		}
		
		@Override
		public boolean equals(Object other) {
			if( other instanceof Index)
				return this.compareTo((Index )other) == 0;
			return false;
		}
		@Override
		public int hashCode() {
			return symbol*stateCount + state;
		}
	};*/
	
	private int stateCount;
	private List<Terminal> tokens;
	// List of Rows, where a Row is a List of Entries:
	private ArrayList<List<ParseTableEntry>> entries;
	//private Map<Index,ParseTableEntry> entries;
}
