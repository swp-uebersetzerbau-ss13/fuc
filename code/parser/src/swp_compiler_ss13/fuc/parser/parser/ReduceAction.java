package swp_compiler_ss13.fuc.parser.parser;

import swp_compiler_ss13.common.report.ReportLog;

public interface ReduceAction {
	
	/**
	 * Reduce the given Objects in Array of ASTnodes and Tokens to a
	 * new ASTNode and return this
	 * @param objs
	 * @return The result of the reduction
	 * @throws ReduceException If there was an error which has been reported to
	 * 		{@link ReportLog} and needs no cancellation
	 * @throws ParserException Iff the reduce aborted anormally and parsing
	 * 		should be cancelled.
	 */
	Object create(Object... objs) throws ReduceException, ParserException;
	

	
	public static class ReduceException extends RuntimeException {
		private static final long serialVersionUID = -1577684340153865593L;
		
		private final Class<?> clazz;
		private final Object obj;
		
		public ReduceException(Object obj, Class<?> clazz, Throwable cause) {
			super(cause);
			this.obj = obj;
			this.clazz = clazz;
		}
		
		public Class<?> getClazz() {
			return clazz;
		}
		
		public Object getObj() {
			return obj;
		}
	}
}
