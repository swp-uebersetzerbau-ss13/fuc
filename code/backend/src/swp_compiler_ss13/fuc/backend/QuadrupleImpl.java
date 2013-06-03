package swp_compiler_ss13.fuc.backend;

import swp_compiler_ss13.common.backend.Quadruple;

/**
* A bare-bones implementation of the <code>Quadruple</code> interface used in
* the tests and the <code>TACExecutor</code>
*
*/
public class QuadrupleImpl implements Quadruple
{
	private Operator operator;
	private String argument1;
	private String argument2;
	private String result;

	public QuadrupleImpl(Operator o, String a1, String a2, String r)
	{
		operator = o;
		argument1 = a1;
		argument2 = a2;
		result = r;
	}

	public String toString() { return "(" + String.valueOf(operator) + "|" + argument1  + "|" + argument2 + "|" + result + ")"; }

	public Operator getOperator() { return operator; }
	public String getArgument1() { return argument1; }
	public String getArgument2() { return argument2; }
	public String getResult() { return result; }
}