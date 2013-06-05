package swp_compiler_ss13.fuc.errorLog;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import swp_compiler_ss13.common.report.ReportType;

public class ReportLogImplTest {
    /**
     * Test of reportError method, of class ReportLogImpl.
     */
    @Test
    public void testReportError() {
        System.out.println("reportError");
		
        ReportLogImpl instance = new ReportLogImpl();
        instance.reportError(ReportType.UNDEFINED, null, "Assignment expects a lvalue.");
    }

    /**
     * Test of hasErrors method, of class ReportLogImpl.
     */
    @Test
    public void testHasErrors() {
        System.out.println("hasErrors");
        ReportLogImpl instance = new ReportLogImpl();
		
		if (instance.hasErrors()) {
			fail("Exptected: hasErrors = false.");
		}
		
        instance.reportError(ReportType.UNDEFINED, null, "Assignment expects a lvalue.");
		
		if (!instance.hasErrors()) {
			fail("Exptected: hasErrors = true.");
		}
    }

    /**
     * Test of getErrors method, of class ReportLogImpl.
     */
    @Test
    public void testGetErrors() {
        System.out.println("getErrors");
        ReportLogImpl instance = new ReportLogImpl();
		
		
		if (instance.getErrors().size() != 0) {
			fail("Exptected: errors.size = 0.");
		}
		
        instance.reportError(ReportType.UNDEFINED, null, "Assignment expects a lvalue.");
		List<LogEntry> l = instance.getErrors();
		
		if (l.size() != 1) {
			fail("Exptected: errors.size = 1.");
		}
    }
}