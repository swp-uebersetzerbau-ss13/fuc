package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


public class Grammar {
	public List<Production> getProductions() {
		return productions;
	}
	public Grammar(List<Production> productions) {
		this.productions = productions;
	}
	public Grammar() {
		this.productions = new ArrayList<Production>();
	}
	/**
	 * read grammar from stream
	 * 
	 * @param inputStream
	 */
	public void readFromFile(Reader inputStream) throws WrongGrammarFormatException, IOException {
		BufferedReader in = new BufferedReader(inputStream);
		List<String> symbols;
		List<String> variables;
		try {
			String line;
			// read symbols:
			line = getLine(in);
			//System.out.print("\"" + line + "\"");
			if( !line.equals("symbols:")) {
				throw new WrongGrammarFormatException("wrong header! Format: symbols:\n sym1, sym2, ...");
			}
			{
				symbols = parseSymbols(in);
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
					if( ! variables.contains(varName))
						throw new WrongGrammarFormatException("variable \"" + varName + "\" has not been declared!");
					Variable left = new Variable(varName);
					List<Symbol> right = new ArrayList<Symbol>();
					
					
					// read right sides:
					line = getLine(in); if ( line == null) throw new WrongGrammarFormatException("no right sides after <Var>:\n");
					do
					{
						String[] rightSide = line.split(" ");
						for(int iRight=0; iRight< rightSide.length; iRight++)
						{
							String current = rightSide[iRight];
							if(variables.contains(current))
							{
								//System.out.println("Variable: " + current);
								right.add(new Variable(current));
							}
							else if( symbols.contains(current))
							{
								//System.out.println("Terminal: " + current);
								right.add(new Terminal(current));
							}
							else
								throw new WrongGrammarFormatException("neither a variable or a symbol in right side");
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
	}
	private List<String> parseSymbols(BufferedReader in) throws WrongGrammarFormatException, IOException{
		//format: "symbols:\nsym1,sym2,sym3,...\n"
		List<String> ret = new ArrayList<String>();
		String line = getLine(in);
		while( ! line.equals( "variables:"))
		{
			String[] aSym = line.split(",");
			for(int i=0; i<aSym.length; i++)
				ret.add(aSym[i]);
			line = getLine(in);
		};
		return ret;
	}
	private List<String> parseVariables(BufferedReader in) throws WrongGrammarFormatException, IOException{
		//format: "variables:\nvar1,var2,...\n"
		List<String> ret = new ArrayList<String>();
		String line = getLine(in);
		while( ! line.equals( "productions:"))
		{
			String[] aSym = line.split(",");
			for(int i=0; i<aSym.length; i++)
				ret.add(aSym[i]);
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
	private List<Production> productions;
}