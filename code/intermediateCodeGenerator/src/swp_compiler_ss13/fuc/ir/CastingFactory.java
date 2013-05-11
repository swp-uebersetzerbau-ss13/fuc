package swp_compiler_ss13.fuc.ir;

/**
 * This factory adds castings where needed to the IR Code. Castings can be
 * disabled by setting the property -Dfuc.irgen.castings=false
 * 
 * @author "Frank Zechert"
 * @version 2
 */
public class CastingFactory {

	/**
	 * Indicates whether casting should be enabled or disabled. By default
	 * casting is enabled. To disable casting set -Dfuc.irgen.castings=false
	 */
	static final boolean castingEnabled = Boolean.parseBoolean(System.getProperty("fuc.irgen.castings", "true"));
}
