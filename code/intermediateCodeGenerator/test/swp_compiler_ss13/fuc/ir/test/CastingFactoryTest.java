package swp_compiler_ss13.fuc.ir.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ir.CastingFactory;
import swp_compiler_ss13.fuc.ir.IntermediateResult;

public class CastingFactoryTest {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test the isCastNeeded(Type, Type) method
	 * 
	 * @throws IntermediateCodeGeneratorException
	 *             on error
	 */
	@Test
	public void testIsCastNeededTypeType() throws IntermediateCodeGeneratorException {
		BooleanType booleanType = new BooleanType();
		DoubleType doubleType = new DoubleType();
		LongType longType = new LongType();
		StringType stringType = new StringType(255L);

		// test same types
		assertTrue(!CastingFactory.isCastNeeded(booleanType, booleanType));
		assertTrue(!CastingFactory.isCastNeeded(doubleType, doubleType));
		assertTrue(!CastingFactory.isCastNeeded(longType, longType));
		assertTrue(!CastingFactory.isCastNeeded(stringType, stringType));

		// test different types
		assertTrue(CastingFactory.isCastNeeded(booleanType, doubleType));
		assertTrue(CastingFactory.isCastNeeded(booleanType, longType));
		assertTrue(CastingFactory.isCastNeeded(booleanType, stringType));
		assertTrue(CastingFactory.isCastNeeded(doubleType, booleanType));
		assertTrue(CastingFactory.isCastNeeded(doubleType, longType));
		assertTrue(CastingFactory.isCastNeeded(doubleType, stringType));
		assertTrue(CastingFactory.isCastNeeded(longType, booleanType));
		assertTrue(CastingFactory.isCastNeeded(longType, doubleType));
		assertTrue(CastingFactory.isCastNeeded(longType, stringType));
		assertTrue(CastingFactory.isCastNeeded(stringType, booleanType));
		assertTrue(CastingFactory.isCastNeeded(stringType, doubleType));
		assertTrue(CastingFactory.isCastNeeded(stringType, longType));

		// test special cases
		try {
			CastingFactory.isCastNeeded(new DoubleType(), new ArrayType(new LongType(), 0));
		} catch (IntermediateCodeGeneratorException e) {
		}
		try {
			CastingFactory.isCastNeeded(new ArrayType(new LongType(), 0), new DoubleType());
		} catch (IntermediateCodeGeneratorException e) {
		}
		try {
			CastingFactory.isCastNeeded(new ArrayType(new LongType(), 0), new ArrayType(new LongType(), 0));
		} catch (IntermediateCodeGeneratorException e) {
		}

		PA.setValue(CastingFactory.class, "castingEnabled", false);
		assertTrue(!CastingFactory.isCastNeeded(doubleType, longType));
		PA.setValue(CastingFactory.class, "castingEnabled", true);
	}

	/**
	 * Test the isCastNeeded(IntermediateResult, IntermediateResult) method
	 * 
	 * @throws IntermediateCodeGeneratorException
	 *             on error
	 */
	@Test
	public void testIsCastNeededIntermediateResultIntermediateResult() throws IntermediateCodeGeneratorException {

		IntermediateResult booleanIR = new IntermediateResult("", new BooleanType());
		IntermediateResult doubleIR = new IntermediateResult("", new DoubleType());
		IntermediateResult longIR = new IntermediateResult("", new LongType());
		IntermediateResult stringIR = new IntermediateResult("", new StringType(255L));

		// test same types
		assertTrue(!CastingFactory.isCastNeeded(booleanIR, booleanIR));
		assertTrue(!CastingFactory.isCastNeeded(doubleIR, doubleIR));
		assertTrue(!CastingFactory.isCastNeeded(longIR, longIR));
		assertTrue(!CastingFactory.isCastNeeded(stringIR, stringIR));

		// test different types
		assertTrue(CastingFactory.isCastNeeded(booleanIR, doubleIR));
		assertTrue(CastingFactory.isCastNeeded(booleanIR, longIR));
		assertTrue(CastingFactory.isCastNeeded(booleanIR, stringIR));
		assertTrue(CastingFactory.isCastNeeded(doubleIR, booleanIR));
		assertTrue(CastingFactory.isCastNeeded(doubleIR, longIR));
		assertTrue(CastingFactory.isCastNeeded(doubleIR, stringIR));
		assertTrue(CastingFactory.isCastNeeded(longIR, booleanIR));
		assertTrue(CastingFactory.isCastNeeded(longIR, doubleIR));
		assertTrue(CastingFactory.isCastNeeded(longIR, stringIR));
		assertTrue(CastingFactory.isCastNeeded(stringIR, booleanIR));
		assertTrue(CastingFactory.isCastNeeded(stringIR, doubleIR));
		assertTrue(CastingFactory.isCastNeeded(stringIR, longIR));
	}

	/**
	 * Test the CastingFactory.isPrimitive(Type type) method
	 */
	@Test
	public void testIsPrimitive() {
		assertTrue(CastingFactory.isPrimitive(new BooleanType()));
		assertTrue(CastingFactory.isPrimitive(new DoubleType()));
		assertTrue(CastingFactory.isPrimitive(new LongType()));
		assertTrue(CastingFactory.isPrimitive(new StringType(255L)));

		assertTrue(!CastingFactory.isPrimitive(new ArrayType(new DoubleType(), 0)));
	}

	/**
	 * Test the CastingFactory.createCast()
	 * 
	 * @throws IntermediateCodeGeneratorException
	 */
	@Test
	public void testCreateCast() throws IntermediateCodeGeneratorException {
		Quadruple q1 = CastingFactory.createCast(new LongType(), "a", new DoubleType(), "b");
		assertEquals(q1.getOperator(), Quadruple.Operator.LONG_TO_DOUBLE);
		assertEquals(q1.getArgument1(), "a");
		assertEquals(q1.getArgument2(), Quadruple.EmptyArgument);
		assertEquals(q1.getResult(), "b");

		Quadruple q2 = CastingFactory.createCast(new DoubleType(), "a", new LongType(), "b");
		assertEquals(q2.getOperator(), Quadruple.Operator.DOUBLE_TO_LONG);
		assertEquals(q2.getArgument1(), "a");
		assertEquals(q2.getArgument2(), Quadruple.EmptyArgument);
		assertEquals(q2.getResult(), "b");

		try {
			CastingFactory.createCast(new DoubleType(), "a", new BooleanType(), "b");
		} catch (IntermediateCodeGeneratorException e) {
		}

		try {
			CastingFactory.createCast(new LongType(), "a", new BooleanType(), "b");
		} catch (IntermediateCodeGeneratorException e) {
		}

		try {
			CastingFactory.createCast(new StringType(2L), "a", new BooleanType(), "b");
		} catch (IntermediateCodeGeneratorException e) {
		}
	}

}
