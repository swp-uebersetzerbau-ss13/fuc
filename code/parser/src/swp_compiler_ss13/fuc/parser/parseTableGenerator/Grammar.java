package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;


public class Grammar {
	public List<Production> getProductions() {
		return productions;
	}
	public Terminal getTerminal(String term) throws NoSuchElementException {
		int index = terminals.indexOf(new Terminal(term));
		if (index == -1)
			throw new NoSuchElementException();
		return terminals.get(index);
	}
	public Variable getVariable(String term) throws NoSuchElementException {
		int index = variables.indexOf(new Variable(term));
		if (index == -1)
			throw new NoSuchElementException();
		return variables.get(index);
	}
	public List<Terminal> getTerminals() {
		return terminals;
	}	
	public List<Variable> getVariables() {
		return variables;
	}
	// too dangerous, ...
	/*public Grammar(List<Production> productions) {
		this.productions = productions;
	}*/
	public Grammar() {
		this.productions = new ArrayList<Production>();
		this.terminals = new ArrayList<Terminal>();
		this.variables = new ArrayList<Variable>();
	}
	/**
	 * read grammar from stream
	 * 
	 * @param inputStream
	 */
	public void readFromFile(Reader inputStream) throws WrongGrammarFormatException, IOException {
		// to do: enable escaping for ":", " ", ",", ...?
		BufferedReader in = new BufferedReader(inputStream);
		/*List<Terminal> terminals;
		List<Variable> variables;*/
		try {
			String line;
			// read symbols:
			line = getLine(in);
			//System.out.print("\"" + line + "\"");
			if( !line.equals("symbols:")) {
				throw new WrongGrammarFormatException("wrong header! Format: symbols:\n sym1, sym2, ...");
			}
			{
				terminals = parseTerminals(in);
			}
			// read variables:
			{
				variables = parseVariables(in);
			}
			//System.out.println("symbols.length: " + symbols.size() + ", variables.size(): " + variables.size());
			// read productions:
			{
				line = getLine(in);
				if (line == null)
					return;
				do
				{ // production block:
					if( ! line.endsWith(":"))
						throw new WrongGrammarFormatException("expected: <Var>:\n");
					String varName = line.substring(0, line.length()-1 );
					// check if var has been declared:
					int iVar = variables.indexOf(new Variable(varName));
					if( iVar == -1 )
						throw new WrongGrammarFormatException("variable \"" + varName + "\" has not been declared!");
					Variable left = variables.get(iVar);
					List<Symbol> right = new ArrayList<Symbol>();
					
					// read right sides:
					line = getLine(in); if ( line == null) throw new WrongGrammarFormatException("no right sides after <Var>:\n");
					do
					{
						String[] rightSide = line.split(" ");
						for(int iRight=0; iRight< rightSide.length; iRight++)
						{
							String current = rightSide[iRight];
							int indexRight = variables.indexOf(new Variable(current));
							if( indexRight != -1 ) // current symbol has been declared to be a variable
							{
								//System.out.println("Variable: " + current);
								right.add( variables.get(indexRight));
							}
							else {
								indexRight = terminals.indexOf(new Terminal(current));
								if( indexRight != -1 )
								{
									//System.out.println("Terminal: " + current);
									right.add( terminals.get(indexRight));
								}
								else
									throw new WrongGrammarFormatException("neither a variable or a symbol in right side");
							}
						}
						getProductions().add(new Production(left,right));
						right.clear();
						//right = new ArrayList<Symbol>();
						//System.out.println("production added!");
						line = getLine(in);
					}
					while (line != null && (!line.endsWith(":")) ); // to do: check if ":" is escaped!... 
				}
				while( line != null);
			}
		}
		catch(WrongGrammarFormatException e) {
			throw e;
		}
		catch(IOException e)
		{
			throw e;
		}
		calcFIRST();
		calcFOLLOW();
	}
	private void calcFIRST() {
		Terminal eps = new Terminal("");
		int iCard = 0;
		int iCardOld = 0;
		do {
			iCardOld = iCard;
			Iterator<Production> i = productions.iterator();
			while (i.hasNext()) { // prod
				Production prod = i.next();
				//System.out.println(prod.getString() + " " + prod.getFIRST().size());
				Variable left = prod.getLeft();
				Iterator<Symbol> iRight = prod.getRight().iterator();
				while(iRight.hasNext()) { // right (step through right side of the production)
					Symbol right = iRight.next();
					if( right.getType() == Symbol.SymbolType.TERMINAL ) {
						Terminal t = (Terminal )right;
						if(t.compareTo(eps) != 0) { // if t != epsilon 
							// ? -> t x1 x2 x3 ...
							int oldSize = left.getFIRST().size();
							prod.getLeft().getFIRST().add(t);
							iCard += (left.getFIRST().size()- oldSize);
								//System.out.println("\t adding " + t.getString() + " (" + (left.getFIRST().size()) + ")");
							break;
						}
						else if ( iRight.hasNext()) {
							throw new RuntimeException("Production of form: <Var> -> eps x1 x2");
						}
					}
					else { // current symbol is a variable:
						Variable v = (Variable )right;
						if( v.getFIRST().contains(eps))
						{
							Set<Terminal> FIRSTWithoutEps = new HashSet<Terminal>(v.getFIRST());
							FIRSTWithoutEps.remove(eps);
							int oldSize = left.getFIRST().size();
							left.getFIRST().addAll(FIRSTWithoutEps);
								//System.out.println("\tadding FIRST( " + v.getString() + " ) \\ eps " + "(" + (left.getFIRST().size()) + ")");
							iCard += (left.getFIRST().size() - oldSize);
							if( ! iRight.hasNext()) //if this is the rightmost symbol:
							{
								oldSize = left.getFIRST().size();
								left.getFIRST().add(eps); // add eps
									System.out.println("\tadding eps" + "(" + (left.getFIRST().size()) + ")");
								iCard += (left.getFIRST().size() - oldSize);
							}
						}
						else {
							int oldSize = left.getFIRST().size();
							left.getFIRST().addAll( v.getFIRST() );
								//System.out.println( "v.Name: " + v.getString() + ", card: " + ((Variable )right).getFIRST().size());
								//System.out.println("\tadding FIRST( " + v.getString() + " )" + "(" + (left.getFIRST().size()) + ")");
							iCard += (left.getFIRST().size() - oldSize);
							break;
						}
					}
				}
			}
		}
		while ( iCard > iCardOld);
	}
	private void calcFOLLOW() {
		Terminal eps = new Terminal("");
		Terminal eof = new Terminal("$");
		int iCard = 0;
		int iCardOld = 0;
		getProductions().get(0).getFOLLOW().add(eof);
		do {
			iCardOld = iCard;
			Iterator<Production> i = productions.iterator();
			while (i.hasNext()) { // prod
				Production prod = i.next();
				System.out.println( prod.getString() );
				Variable left = prod.getLeft();
				// now iterate through the right side from right to left:
				ListIterator<Symbol> iRight = prod.getRight().listIterator(prod.getRight().size());
				Set<Terminal> FOLLOWToAdd = new HashSet<Terminal>(left.getFOLLOW());
				/*if( ! iRight.hasPrevious())
					break;*/
				while ( iRight.hasPrevious()) {  // right (step through right side of the production)
					Symbol right = iRight.previous();
					if ( right.getType() == Symbol.SymbolType.TERMINAL )
					{
						FOLLOWToAdd.clear(); FOLLOWToAdd.add((Terminal )right);
					}
					else {
						// if right is a VARIABLE
						Variable varRight = (Variable )right;
						// 1. add FOLLOWToAdd to varRight.getFOLLOW():
							int oldSize = varRight.getFOLLOW().size();
						varRight.getFOLLOW().addAll(FOLLOWToAdd);
							iCard += (varRight.getFOLLOW().size()-oldSize);
						// 2. prepare new FOLLOWToAdd:
						FOLLOWToAdd.clear();
						Set<Terminal> currentFIRST = new HashSet<Terminal>(varRight.getFIRST()); currentFIRST.remove(eps);
						FOLLOWToAdd.addAll(currentFIRST);
						if( varRight.getFIRST().contains(eps) ){
							FOLLOWToAdd.addAll( varRight.getFOLLOW() );
						}
					}
				}
			}
		}
		while( iCard > iCardOld );
	}
	private List<Terminal> parseTerminals(BufferedReader in) throws WrongGrammarFormatException, IOException{
		//format: "symbols:\nsym1,sym2,sym3,...\n"
		List<Terminal> ret = new ArrayList<Terminal>();
		String line = getLine(in);
		while( ! line.equals( "variables:"))
		{
			String[] aSym = line.split(",");
			for(int i=0; i<aSym.length; i++)
				ret.add(new Terminal(aSym[i]));
			line = getLine(in);
		};
		return ret;
	}
	private List<Variable> parseVariables(BufferedReader in) throws WrongGrammarFormatException, IOException{
		//format: "variables:\nvar1,var2,...\n"
		List<Variable> ret = new ArrayList<Variable>();
		String line = getLine(in);
		while( ! line.equals( "productions:"))
		{
			String[] aSym = line.split(",");
			for(int i=0; i<aSym.length; i++)
				ret.add(new Variable(aSym[i]));
			line = getLine(in);
		};
		return ret;
	}
	private String getLine(BufferedReader in) throws WrongGrammarFormatException, IOException {
		String ret;
		try {
			ret = in.readLine();
		}
		catch(IOException e) {
			throw e;
		}
		// check, if end of file has been reached:
		/*if( ret == null)
			throw new WrongGrammarFormatException("end of file!");*/
		if(ret != null)
			ret = ret.trim();
		return ret;
	}
	private List<Terminal> terminals;
	private List<Variable> variables;
	private List<Production> productions;
}