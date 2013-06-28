package swp_compiler_ss13.fuc.ir.data;

import java.util.LinkedList;

import org.apache.log4j.Logger;

/**
 * A normal linked list extended to log calls to the add-method with Log4J.
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 * @param <E>
 *            The class of the list elements
 */
public class LoggingLinkedList<E> extends LinkedList<E> {

	/**
	 * The serial version UID of this class.
	 */
	private static final long serialVersionUID = 6844805057880276057L;

	/**
	 * The logger to log the information to.
	 */
	private static Logger logger = Logger.getLogger(LoggingLinkedList.class);

	/**
	 * Create a new instance of the LogigngList class.
	 */
	public LoggingLinkedList() {
		super();
	}

	@Override
	public boolean add(E item) {
		LoggingLinkedList.logger.debug("Adding " + item.toString());
		return super.add(item);
	}
}
