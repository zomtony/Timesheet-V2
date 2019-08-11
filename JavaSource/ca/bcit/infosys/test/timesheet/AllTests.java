package ca.bcit.infosys.test.timesheet;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/** Runs all tests in timesheet test suite.
 * 
 * @author blink
 * @version 1.1
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ TestTimesheet.class, TestTimesheetRow.class })
public class AllTests {

}
