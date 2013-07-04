package swp_compiler_ss13.fuc.parser.parser;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;
import swp_compiler_ss13.fuc.parser.grammar.TokenEx;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Reduce;

/**
 * This interface combines all neccessary grammar specific actions that have to
 * be performed during the parsing process
 */
public interface IGrammarImpl {
	/**
	 * @param prod The {@link Production} which should be performed reduced
	 * @return The {@link ReduceAction} for the given {@link Reduce}
	 */
	public ReduceAction getReduceAction(Production prod);
	
	/**
	 * Tries to put the parser into a state from where it can recover
	 * itself
	 * 
	 * @param possibleTerminals The terminals that the parser expected
	 * @param curToken The Token that the parser received instead
	 * @param lastToken The Token that the parser receiven before
	 * @param valueStack The current value stack
	 * @return A new instance of {@link RecoveryResult} which tells the
	 * 		parser how to change its state
	 */
	public RecoveryResult tryErrorRecovery(List<Terminal> possibleTerminals,
			TokenEx curToken, TokenEx lastToken,
			Stack<Object> valueStack);
	
	/**
	 * @param reportLog The parsers {@link ReportLog}
	 */
	public void setReportLog(ReportLog reportLog);
	
	
	/**
	 * The result of a recovery attempt. Contains information for the new
	 * state of the parser
	 */
	public static class RecoveryResult {
		private final TokenEx newCurToken;
		private final List<TokenEx> nextTokens;
		
		public RecoveryResult(TokenEx newCurToken,
				List<TokenEx> nextTokens) {
			this.newCurToken = newCurToken;
			this.nextTokens = nextTokens;
		}
		
		public RecoveryResult(TokenEx newCurToken,
				TokenEx... nextTokens) {
			this(newCurToken, Arrays.asList(nextTokens));
		}
		
		public TokenEx getNewCurToken() {
			return newCurToken;
		}
		
		public List<TokenEx> getNextTokens() {
			return nextTokens;
		}
	}
}
