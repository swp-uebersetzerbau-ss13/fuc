package intermediateCodeGenerator.example;

import org.apache.log4j.Logger;

public class SomeClass {
	public static Logger logger = Logger.getLogger(SomeClass.class);

	public static void main(String[] args) {
		logger.trace("trace message");
		logger.debug("debug message");
		logger.info("info message");
		logger.warn("warn message");
		logger.error("error message");
		logger.fatal("fatal message");
	}
}
