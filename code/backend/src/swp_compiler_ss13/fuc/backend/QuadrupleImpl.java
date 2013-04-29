package swp_compiler_ss13.fuc.backend;

/**
 * Basic Quadruple Implementation (for testing)
 */
public class QuadrupleImpl implements swp_compiler_ss13.common.backend.Quadruple {

    Operator operator;
    String argument1;
    String argument2;
    String result;

    public QuadrupleImpl(Operator operator, String argument1, String argument2, String result) {
        this.operator = operator;
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.result = result;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

    @Override
    public String getArgument1() {
        return argument1;
    }

    @Override
    public String getArgument2() {
        return argument2;
    }

    @Override
    public String getResult() {
        return result;
    }
}
