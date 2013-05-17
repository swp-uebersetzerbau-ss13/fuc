package swp_compiler_ss13.fuc.parser.generator.terminals;

import java.util.Set;

import swp_compiler_ss13.fuc.parser.grammar.Terminal;


public interface ITerminalSet {
   public ITerminalSet empty();
   
   
   public ITerminalSet plus(Terminal terminal);
   public ITerminalSet plusAll(ITerminalSet terminals);
   public ITerminalSet plusAllExceptEpsilon(ITerminalSet terminals);
   public boolean contains(Terminal terminal);
   public Set<Terminal> getTerminals();
}
