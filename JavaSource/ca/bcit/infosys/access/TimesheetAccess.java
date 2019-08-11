package ca.bcit.infosys.access;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.Statement;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;

/**
 * Access database which edit, create and view for timesheet.
 * 
 * @author Yang
 * @version 1.1
 */
@Named
@ConversationScoped
public class TimesheetAccess implements Serializable {
    private static final long serialVersionUID = 1L;
    /** dataSource for connection pool on JBoss AS 7 or higher. */
    @Resource(mappedName = "java:jboss/datasources/EmployeeManagement")
    private DataSource ds;

    /**
     * createTimesheetEmployee
     *          access database find a employee to 
     *          create an employee object for timesheet.
     * @param empNumber as int.
     * @return tempEmployee as Employee.
     */
    private Employee createTimesheetEmployee(int empNumber) {
        Statement stmt = null;
        Connection connection = null;
        Employee tempEmployee = null;

        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet resultEmp = stmt
                            .executeQuery("SELECT * FROM EmployeeInfo WHERE " 
                                    + "empNumber = '" + empNumber + "'");

                    if (resultEmp.next()) {
                        tempEmployee = new Employee(resultEmp.getInt(
                                "empNumber"), resultEmp.getString("name"),
                                resultEmp.getString("userName"));

                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }

                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in select all");
            ex.printStackTrace();
            return null;
        }
        return tempEmployee;
    }

    /**
     * createTimesheetListRow
     *          access database to create timesheetRow.
     * @param empNumber as int.
     * @param weekEnding as Date.
     * @return tempEmployee as Employee.
     */
    private ArrayList<TimesheetRow> createTimesheetListRow(
        int empNumber, Date weekEnding) {
        Statement stmt = null;
        Connection connection = null;

        ArrayList<TimesheetRow> timesheetListRow 
                = new ArrayList<TimesheetRow>();
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet resultTimesheetRow 
                        = stmt.executeQuery("SELECT * FROM TimesheetRow "
                            + "WHERE empNumber = '" 
                            + empNumber + "' AND weekEnding ='" 
                            + weekEnding + "'");

                    while (resultTimesheetRow.next()) {
                                               
                        final int three = 3;
                        final int four = 4;
                        final int five = 5;
                        final int six = 6;
                        final int seven = 7;
                        
                        BigDecimal[] hoursForWeek = new BigDecimal[seven];
                        hoursForWeek[0] = 
                                resultTimesheetRow.getBigDecimal("sat");
                        hoursForWeek[1] = 
                                resultTimesheetRow.getBigDecimal("sun");
                        hoursForWeek[2] = 
                                resultTimesheetRow.getBigDecimal("mon");
                        hoursForWeek[three] = 
                                resultTimesheetRow.getBigDecimal("tue");
                        hoursForWeek[four] = 
                                resultTimesheetRow.getBigDecimal("wed");
                        hoursForWeek[five] = 
                                resultTimesheetRow.getBigDecimal("thu");
                        hoursForWeek[six] = 
                                resultTimesheetRow.getBigDecimal("fri");

                        timesheetListRow.add(new TimesheetRow(
                                resultTimesheetRow.getString("wp"),
                                resultTimesheetRow.getInt("project"), 
                                hoursForWeek,
                                resultTimesheetRow.getString("notes")));

                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }

                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in select all");
            return null;
        }
        return timesheetListRow;
    }

    /**
     * getAllEmployeeTimeSheetList
     *          access database to get all employee timesheet list.
     * @return allEmployeeTimeSheetList as Employee timesheetlist.
     */
    public ArrayList<Timesheet> getAllEmployeeTimeSheetList() {

        ArrayList<Timesheet> allEmployeeTimeSheetList 
            = new ArrayList<Timesheet>();

        Statement stmt = null;
        Connection connection = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result 
                        = stmt.executeQuery("SELECT * FROM Timesheet");

                    while (result.next()) {

                        int empNumber = result.getInt("empNumber");
                        java.sql.Date weekEnding 
                            = new java.sql.Date(
                                    result.getDate("weekEnding").getTime());
                        
                        Employee tempEmployee 
                            = createTimesheetEmployee(empNumber);
                        ArrayList<TimesheetRow> timesheetListRow 
                            = createTimesheetListRow(empNumber, weekEnding);
                        allEmployeeTimeSheetList.add(new Timesheet(
                                tempEmployee, weekEnding, timesheetListRow));

                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }

                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in select all");
            ex.printStackTrace();
            return null;
        }

        return allEmployeeTimeSheetList;
    }

    /**
     * insertTimesheet
     *          access database to insert timesheet.
     * @param timesheet as Timesheet.
     */
    public boolean insertTimesheet(Timesheet timesheet) {
        // order of fields in INSERT statement
        Connection connection = null;
        PreparedStatement stmt = null;
        final int empNumber = 1;
        final int name = 2;
        final int weekNumber = 3;
        final int weekEnding = 4;

        final int wp = 3;
        final int project = 4;
        final int sat = 5;
        final int sun = 6;
        final int mon = 7;
        final int tue = 8;
        final int wed = 9;
        final int thu = 10;
        final int fir = 11;
        final int notes = 12;

        final int three = 3;
        final int four = 4;
        final int five = 5;
        final int six = 6;
       
        try {
            try {
                connection = ds.getConnection();
                try {
                    java.sql.Date endWeek 
                        = new java.sql.Date(timesheet.getEndWeek().getTime());
                    stmt = connection.prepareStatement(
                            "INSERT INTO Timesheet VALUES (?, ?, ?, ?)");
                    stmt.setInt(empNumber, 
                            timesheet.getEmployee().getEmpNumber());
                    stmt.setString(name, timesheet.getEmployee().getName());
                    stmt.setInt(weekNumber, timesheet.getWeekNumber());
                    stmt.setDate(weekEnding, endWeek);
                    stmt.executeUpdate();

                    for (TimesheetRow timesheetRow : timesheet.getDetails()) {
                        stmt = connection.prepareStatement(
                                "INSERT INTO TimesheetRow VALUES " + "(?, "
                                        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                        stmt.setInt(1, timesheet.getEmployee().getEmpNumber());

                        stmt.setDate(2, endWeek);
                        stmt.setString(wp, timesheetRow.getWorkPackage());
                        stmt.setInt(project, timesheetRow.getProjectID());
                        stmt.setBigDecimal(
                                sat, timesheetRow.getHoursForWeek()[0]);
                        stmt.setBigDecimal(
                                sun, timesheetRow.getHoursForWeek()[1]);
                        stmt.setBigDecimal(
                                mon, timesheetRow.getHoursForWeek()[2]);
                        stmt.setBigDecimal(
                                tue, timesheetRow.getHoursForWeek()[three]);
                        stmt.setBigDecimal(
                                wed, timesheetRow.getHoursForWeek()[four]);
                        stmt.setBigDecimal(
                                thu, timesheetRow.getHoursForWeek()[five]);
                        stmt.setBigDecimal(
                                fir, timesheetRow.getHoursForWeek()[six]);
                        stmt.setString(notes, timesheetRow.getNotes());
                        stmt.executeUpdate();
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }

                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            rollBackSheet(timesheet.getEmployee().getEmpNumber(), timesheet.getEndWeek());
            System.out.println("Error in insert" + ex);
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * rollBackSheet
     *          roll back for wrong data saved in database.
     * @param empNumber as int.
     * @param weekEnding as Date.
     */
    private void rollBackSheet(int empNumber, Date weekEnding) {
        Connection connection = null;
        PreparedStatement stmtSheet = null;
        PreparedStatement stmtSheetRow = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    java.sql.Date endWeek 
                        = new java.sql.Date(weekEnding.getTime());
                                        
                    stmtSheet = connection.prepareStatement(
                            "DELETE FROM Timesheet "
                                   + "WHERE empNumber =  ? AND weekEnding = ?");
                    
                    stmtSheet.setInt(1, empNumber);
                    stmtSheet.setDate(2, endWeek);
                    stmtSheet.executeUpdate(); 
                    
                    stmtSheetRow = connection.prepareStatement(
                            "DELETE FROM TimesheetRow "
                                   + "WHERE empNumber =  ? AND weekEnding = ?");
                    
                    stmtSheetRow.setInt(1, empNumber);
                    stmtSheetRow.setDate(2, endWeek);
                    stmtSheetRow.executeUpdate();
                    
                } finally {
       
                    
                    if (stmtSheet != null) {
                        stmtSheet.close();
                    }
                    
                    if (stmtSheetRow != null) {
                        stmtSheetRow.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in delete employee ");
            ex.printStackTrace();
        }
    }
    /**
     * updateTimesheet
     *          access database to update timesheet.
     * @param timesheet as Timesheet.
     */
    public void updateTimesheet(Timesheet timesheet) {
        // order of fields in UPDATE statement
        Connection connection = null;
        PreparedStatement stmtSheetRow = null;
        PreparedStatement stmtSheet = null;

        try {
            try {
                connection = ds.getConnection();
                try {
                    int empNumberTimesheet 
                        = timesheet.getEmployee().getEmpNumber();
                    java.sql.Date weekEndingTimesheet 
                        = new java.sql.Date(timesheet.getEndWeek().getTime());
                    
                    stmtSheetRow = connection.prepareStatement(
                            "DELETE FROM TimesheetRow "
                                   + "WHERE empNumber =  ? AND weekEnding = ?");
                    stmtSheetRow.setInt(1, empNumberTimesheet);
                    stmtSheetRow.setDate(2, weekEndingTimesheet);
                    stmtSheetRow.executeUpdate();
                    
                    stmtSheet = connection.prepareStatement(
                            "DELETE FROM Timesheet "
                                   + "WHERE empNumber =  ? "
                                   + "AND weekEnding = ?");                   
                    stmtSheet.setInt(1, empNumberTimesheet);
                    stmtSheet.setDate(2, weekEndingTimesheet);
                    stmtSheet.executeUpdate();
                    
                    insertTimesheet(timesheet);                   
                    
                } finally {
                    if (stmtSheetRow != null) {
                        stmtSheetRow.close();
                    }
                    if (stmtSheet != null) {
                        stmtSheet.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in merge ");
            ex.printStackTrace();
        }
    }
}
