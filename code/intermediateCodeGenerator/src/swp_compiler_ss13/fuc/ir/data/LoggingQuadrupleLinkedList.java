package swp_compiler_ss13.fuc.ir.data;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.backend.Quadruple;

/**
 * A normal linked list extended to log calls to the add-method with Log4J.
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class LoggingQuadrupleLinkedList extends LinkedList<Quadruple> {

	/**
	 * The serial version UID of this class.
	 */
	private static final long serialVersionUID = 6844805057880276057L;

	/**
	 * The logger to log the information to.
	 */
	private static Logger logger = Logger.getLogger(LoggingQuadrupleLinkedList.class);

	/**
	 * Create a new instance of the LogigngList class.
	 */
	public LoggingQuadrupleLinkedList() {
		super();
	}

	@Override
	public boolean add(Quadruple quadruple) {
		logger.debug("Adding Quadruple: " + quadruple.toString());
		return super.add(quadruple);
	}
}
