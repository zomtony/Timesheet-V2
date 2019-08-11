package ca.bcit.infosys.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.manager.EmployeeManager;

/**
 * Access database 
 *      which can edit, create, view and delete.
 * 
 * @author Yang
 * @version 1.1
 */
@Named
@ConversationScoped
public class EmployeeAccess implements Serializable {
    private static final long serialVersionUID = 1L;
    /** dataSource for connection pool on JBoss AS 7 or higher. */
    @Resource(mappedName = "java:jboss/datasources/EmployeeManagement")
    private DataSource ds;
    
    /**
     * insertEmployee
     *          access database insert a employee 
     *          create an employee object for timesheet.
     * @param newEmployee as Employee.
     * @return boolean.
     */
    public boolean insertEmployee(Employee newEmployee) {
        // order of fields in INSERT statement
        Connection connection = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        final int three = 3;
        System.out.println(newEmployee.getName());
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "INSERT INTO EmployeeInfo VALUES (?, ?, ?)");
                    stmt.setInt(1, newEmployee.getEmpNumber());
                    stmt.setString(2, newEmployee.getUserName());
                    stmt.setString(three, newEmployee.getName());
                    stmt.executeUpdate();

                    stmt1 = connection.prepareStatement(
                            "INSERT INTO Credentials VALUES (?, ?)");
                    stmt1.setString(1, newEmployee.getUserName());
                    stmt1.setString(2, EmployeeManager.DEFAULT_PWD);
                    stmt1.executeUpdate();

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
            System.out.println("Error in insert employee");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * getAllEmployee
     *          access database get all employee.
     * @return employeeList as Employee.
     */
    public ArrayList<Employee> getAllEmployee() {
        ArrayList<Employee> employeeList = new ArrayList<Employee>();
        Connection connection = null;
        Statement stmt = null;

        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM EmployeeInfo ORDER BY empNumber");
                    
                    while (result.next()) {
                        
                        employeeList.add(new Employee(
                                result.getInt("empNumber"), 
                                result.getString("name"),
                                result.getString("userName")));
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
            System.out.println("Error in getAll");
            ex.printStackTrace();
            return null;
        }

        return employeeList;
    }
    
    /**
     * removeEmployee
     *          access database remove an employee.
     * @param employee as Employee.
     * @return boolean.
     */
    public boolean removeEmployee(Employee employee) {
        Connection connection = null;
        PreparedStatement stmtEmp = null;
        PreparedStatement stmtCre = null;
        PreparedStatement stmtSheet = null;
        PreparedStatement stmtSheetRow = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    String userName = employee.getUserName();
                    int empNumber = employee.getEmpNumber();
                    stmtCre = connection.prepareStatement(
                            "DELETE FROM Credentials "
                                   + "WHERE userName =  ?");
                    
                    stmtCre.setString(1, userName);
                    stmtCre.executeUpdate();     
                    
                    stmtEmp = connection.prepareStatement(
                            "DELETE FROM EmployeeInfo "
                                   + "WHERE userName =  ?");
                    
                    stmtEmp.setString(1, userName);
                    stmtEmp.executeUpdate();     
                                        
                    stmtSheet = connection.prepareStatement(
                            "DELETE FROM Timesheet "
                                   + "WHERE empNumber =  ?");
                    
                    stmtSheet.setInt(1, empNumber);
                    stmtSheet.executeUpdate(); 
                    
                    stmtSheetRow = connection.prepareStatement(
                            "DELETE FROM TimesheetRow "
                                   + "WHERE empNumber =  ?");
                    
                    stmtSheetRow.setInt(1, empNumber);
                    stmtSheetRow.executeUpdate();
                    
                } finally {
                    if (stmtCre != null) {
                        stmtCre.close();
                    }
                    
                    if (stmtEmp != null) {
                        stmtEmp.close();
                    }
                    
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
        return true;        
    }
    
    /**
     * updateEmployee
     *          access database update a employee Infor.
     * @param updateEmployee as Employee.
     * @return boolean.
     */
    public boolean updateEmployee(Employee updateEmployee) {
        // order of fields in INSERT statement
        Connection connection = null;
        PreparedStatement stmt = null;
        final int three = 3;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE EmployeeInfo "
                            + "SET name = ?, "
                            + "empNumber = ? WHERE userName =  ?");
                    
                    stmt.setString(1, updateEmployee.getName());
                    stmt.setInt(2, 
                            updateEmployee.getEmpNumber());                   
                    stmt.setString(three, updateEmployee.getUserName());
                    stmt.executeUpdate();


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
            System.out.println("Error in update employee");
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
