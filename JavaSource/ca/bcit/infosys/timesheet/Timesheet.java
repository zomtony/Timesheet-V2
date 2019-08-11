package ca.bcit.infosys.timesheet;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.List;

import ca.bcit.infosys.employee.Employee;

/**
 * A class representing a single Timesheet.
 *
 * @author Bruce Link
 * @version 1.1
 */
public class Timesheet implements java.io.Serializable {

    /** Number of days in a week. */
    public static final int DAYS_IN_WEEK = 7;
    
    /** Number of hours in a day as double. */
    public static final double HOURS_IN_A_DAY = 24.0;
    
    /** Number of hours in a day. */
    public static final BigDecimal HOURS_IN_DAY =
           new BigDecimal(HOURS_IN_A_DAY).setScale(1, BigDecimal.ROUND_HALF_UP);
    
    /** Number of work hours in week as double. */
    public static final double WORK_HOURS = 40.0;

    /** Full work week in units of hours. */
    public static final BigDecimal FULL_WORK_WEEK =
            new BigDecimal(WORK_HOURS).setScale(1, BigDecimal.ROUND_HALF_UP);

    /** Serial version number. */
    private static final long serialVersionUID = 2L;
    /** BigDecimal DailyHours. */
    private BigDecimal[] dailyHours;
    /** BigDecimal totalHours. */
    private BigDecimal totalHours;

    /** The user associated with this timesheet. */
    private Employee employee;
    /** The date of Friday for the week of the timesheet. */
    private Date endWeek;
    /** The ArrayList of all details (i.e. rows) that the form contains. */
    private List<TimesheetRow> details;
    /** The total number of overtime hours on the timesheet. */
    private BigDecimal overtime;
    /** The total number of flextime hours on the timesheet. */
    private BigDecimal flextime;


    /**
     * Constructor for Timesheet.
     * Initialize a Timesheet with no rows, no employee and
     * to the current date.
     */
    public Timesheet() {
        details = new ArrayList<TimesheetRow>();
        Calendar c = new GregorianCalendar();
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);
        endWeek = c.getTime();
    }

    /**
     * Creates a Timesheet object with all fields set. 
     * 
     * @param user The owner of the timesheet
     * @param end The date of the end of the week for the timesheet (Friday)
     * @param charges The detailed hours charged for the week for this 
     *        timesheet
     */
    public Timesheet(final Employee user, final Date end,
            final List<TimesheetRow> charges) {
        employee = user;
        checkFriday(end);
        endWeek = end;
        details = charges;
    }

    /**
     * Getter for time sheet owner.
     * @return the employee.
     */
    public Employee getEmployee() {
        return employee;
    }
    
    /**
     * Setter for time sheet owner.
     * Allows user to be null.
     * @param user the employee for the timesheet.
     */
    public void setEmployee(final Employee user) {
        employee = user;
    }

    /**
     * Getter for timesheet's end of week date.
     * @return the endWeek
     */
    public Date getEndWeek() {
        return endWeek;
    }

    /**
     * Verify date is a friday.
     * @param end a date which should be on a Friday
     * @throws IllegalArgumentException if date is not on Friday.
     */
    private void checkFriday(final Date end) {
        Calendar c = new GregorianCalendar();
        c.setTime(end);
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        if (currentDay != Calendar.FRIDAY) {
            throw new IllegalArgumentException("EndWeek must be a Friday");
        }

    }
    /**
     * Sets the timesheet end of week. Must be a Friday.
     * @param end the endWeek to set. 
     * @throws IllegalArgumentException if date is not on Friday.
     */
    public void setEndWeek(final Date end) {
        checkFriday(end);
        endWeek = end;
    }

    /**
     * Calculate the week number of the timesheet.
     * @return the calculated week number
     */
    public int getWeekNumber() {
        Calendar c = new GregorianCalendar();
        c.setTime(endWeek);
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Sets the end of week based on the week number.
     *
     * @param weekNo the week number of the timesheet week
     * @param weekYear the year of the timesheet
     */
    public void setWeekNumber(final int weekNo, final int weekYear) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.setTime(endWeek);
        c.setWeekDate(weekYear, weekNo, Calendar.FRIDAY);
        endWeek = c.getTime();
    }

    /**
     * Calculate the time sheet's end date as a string.
     * @return the endWeek as string
     */
    public String getWeekEnding() {
        Calendar c = new GregorianCalendar();
        c.setTime(endWeek);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        month += 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        return month + "/" + day + "/" + year;
    }

    /**
     * Getter for timesheet row details.
     * @return the details
     */
    public List<TimesheetRow> getDetails() {
        return details;
    }

    /**
     * Sets the details of the timesheet.
     *
     * @param newDetails new weekly charges to set
     */
    public void setDetails(final ArrayList<TimesheetRow> newDetails) {
        details = newDetails;
    }

    /**
     * Getter for overtime field, indicates number of hours to
     * be paid for overtime for this week.
     * @return the overtime
     */
    public BigDecimal getOvertime() {
        return overtime;
    }

    /**
     * Setter for overtime field, indicates number of hours to
     * be paid for overtime for this week.
     * @param ot the overtime to set
     */
    public void setOvertime(final BigDecimal ot) {
        overtime = ot.setScale(1, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Getter for flextime field, indicates number of hours to
     * save for flextime this week.
     * @return the flextime
     */
    public BigDecimal getFlextime() {
        return flextime;
    }

    /**
     * Setter for flextime field, indicates number of hours to
     * save for flextime this week.
     * @param flex the flextime to set
     */
    public void setFlextime(final BigDecimal flex) {
        flextime = flex.setScale(1, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Calculates the total hours.
     *
     * @return total hours for timesheet.
     */
    public BigDecimal getTotalHours() {
        BigDecimal sum = BigDecimal.ZERO;
        for (TimesheetRow row : details) {
            sum = sum.add(row.getSum());
        }
        totalHours = sum;
        return totalHours;
    }
    
    
    
    /**
     * Sets the totalHours for this Timesheet.
     * @param totalHours the totalHours to set
     */
    public void setTotalHours(BigDecimal totalHours) {
        this.totalHours = totalHours;
    }


    /**
     * Calculates the daily hours.
     *
     * @return array of total hours for each day of week for timesheet.
     */
    public BigDecimal[] getDailyHours() {
        BigDecimal[] sums = new BigDecimal[DAYS_IN_WEEK];
        for (TimesheetRow day : details) {
            BigDecimal[] hours = day.getHoursForWeek();
            for (int i = 0; i < DAYS_IN_WEEK; i++) {
                if (hours[i] != null) {
                    if (sums[i] == null) {
                        sums[i] = hours[i];
                    } else {
                        sums[i] = sums[i].add(hours[i]);
                    }
                }
            }
        }
        dailyHours = sums;
        return dailyHours;
    }

    /**
     * Checks to see if timesheet total nets 40 hours.
     * @return true if FULL_WORK_WEEK == hours -flextime - overtime
     */
    public boolean isValid() {
        BigDecimal net = getTotalHours();
        if (overtime != null) {
            net = net.subtract(overtime);
        }
        if (flextime != null) {
            net = net.subtract(flextime);
        }
        return net.equals(FULL_WORK_WEEK);
    }

    /**
     * Deletes the specified row from the timesheet.
     *
     * @param rowToRemove
     *            the row to remove from the timesheet.
     */
    public void deleteRow(final TimesheetRow rowToRemove) {
        details.remove(rowToRemove);
    }

    /**
     * Add an empty row to to the timesheet.
     */
    public void addRow() {
        details.add(new TimesheetRow());
    }

}
