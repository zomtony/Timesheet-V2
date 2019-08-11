package ca.bcit.infosys.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.sql.DataSource;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.manager.EmployeeManager;

/**
 * Access database which check if the password right.
 * 
 * @author Yang
 * @version 1.1
 */
@Named
@ConversationScoped
public class CredentialCheck  implements Serializable {
    private static final long serialVersionUID = 1L;
    /** dataSource for connection pool on JBoss AS 7 or higher. */
    @Resource(mappedName = "java:jboss/datasources/EmployeeManagement")
    private DataSource ds;
    
    /**
     * loginCheck  
     *      access database check if the user name and password are right.
     * 
     * @param credential
     *            credential the infor for user name and password.
     * @return boolean
     *            return true or false.
     */
    public boolean loginCheck(Credentials credential) {
        // order of fields in INSERT statement
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement("SELECT * "
                            + "FROM Credentials "
                            + "WHERE  userName = ? and password = ?");
                    stmt.setString(1, credential.getUserName());
                    stmt.setString(2, credential.getPassword());
                    ResultSet result = stmt.executeQuery();
                    
                    return result.next();
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
            System.out.println("Error in login");
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * setDefaultPwd
     *          set default password for user.
     * 
     * @param myEmployee
     *            myEmployee Employee include employee info.
     */
    public void setDefaultPwd(Employee myEmployee) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE Credentials "
                            + "SET password = ? WHERE userName = ?");
                    stmt.setString(1, EmployeeManager.DEFAULT_PWD);
                    stmt.setString(2, myEmployee.getUserName());
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
            System.out.println("Error in merge " + myEmployee);
            ex.printStackTrace();
        }
    }
    
    /**
     * changePwd
     *          access database change password for user.
     * @param credential
     *            Credentials for user name and password.
     */
    public void changePwd(Credentials credential) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = ds.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE Credentials "
                            + "SET password = ? WHERE userName = ?");
                    stmt.setString(1, credential.getPassword());
                    stmt.setString(2, credential.getUserName());
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
            System.out.println("Error in changePwd ");
            ex.printStackTrace();
        }
    }
}
