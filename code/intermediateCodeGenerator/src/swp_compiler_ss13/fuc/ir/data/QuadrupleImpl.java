package swp_compiler_ss13.fuc.ir.data;

import swp_compiler_ss13.common.backend.Quadruple;

/**
 * Implementation of the Quadruple interface. A Quadruple is an immutable object.
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class QuadrupleImpl implements Quadruple {

	/**
	 * The Operator
	 */
	private final Operator operator;
	/**
	 * The argument 1
	 */
	private final String argument1;
	/**
	 * The argument 2
	 */
	private final String argument2;
	/**
	 * The result
	 */
	private final String result;

	/**
	 * Create a new Quadruple
	 * 
	 * @param operator
	 *            The operator
	 * @param arg1
	 *            Argument 1
	 * @param arg2
	 *            Argument 2
	 * @param res
	 *            The result
	 */
	public QuadrupleImpl(Operator operator, String arg1, String arg2, String res) {
		this.operator = operator;
		this.argument1 = arg1;
		this.argument2 = arg2;
		this.result = res;
	}

	/**
	 * Create a new Quadruple
	 * 
	 * @param operator
	 *            The operator
	 * @param arg1
	 *            Argument 1
	 * @param res
	 *            The result
	 */
	public QuadrupleImpl(Operator operator, String arg1, String res) {
		this.operator = operator;
		this.argument1 = arg1;
		this.argument2 = Quadruple.EmptyArgument;
		this.result = res;
	}

	/**
	 * Create a new Quadruple
	 * 
	 * @param operator
	 *            The operator
	 * @param res
	 *            The result
	 */
	public QuadrupleImpl(Operator operator, String res) {
		this.operator = operator;
		this.argument1 = Quadruple.EmptyArgument;
		this.argument2 = Quadruple.EmptyArgument;
		this.result = res;
	}

	@Override
	public Operator getOperator() {
		return this.operator;
	}

	@Override
	public String getArgument1() {
		return this.argument1;
	}

	@Override
	public String getArgument2() {
		return this.argument2;
	}

	@Override
	public String getResult() {
		return this.result;
	}

	@Override
	public String toString() {
		return String.format("Quadruple: (%s | %s | %s | %s)", this.operator.toString(),
				this.argument1, this.argument2, this.result);
	}

}
