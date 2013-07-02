package swp_compiler_ss13.fuc.ir.data;

import java.util.Stack;

import org.apache.log4j.Logger;

/**
 * A normal stack extended to log calls to the push and pop methods with Log4J.
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 * @param <E>
 *            The class of the stack elements
 */
public class LoggingStack<E> extends Stack<E> {

	/**
	 * serial version UID of this class
	 */
	private static final long serialVersionUID = -2928888809894348391L;

	/**
	 * The logger to log the calls
	 */
	private static Logger logger = Logger.getLogger(LoggingStack.class);

	/**
	 * Generate a new instance of the LoggingIntermediateResultStack class
	 */
	public LoggingStack() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E push(E result) {
		LoggingStack.logger.debug("Pushed " + result.toString());
		return super.push(result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E pop() {
		E item = super.pop();
		LoggingStack.logger.debug("Popped " + item);
		return item;
	}
}
