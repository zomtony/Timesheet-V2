package ca.bcit.infosys.employee;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;

/**
 * A class representing a single Employee.
 *
 * @author Bruce Link
 * @version 1.1
 */
@ConversationScoped
public class Employee implements Serializable {

    private static final long serialVersionUID = 11L;
    /** The employee's name. */
    private String name;
    /** The employee's employee number. */
    private int empNumber;
    /** The employee's login ID. */
    private String userName;
    /** if editable. */
    private boolean editable;
    
    /**
     * The no-argument constructor. Used to create new employees from within the
     * application.
     */
    public Employee() {
    }

    /**
     * The argument-containing constructor. Used to create the
     *  initial employees who
     * have access as well as the administrator.
     * 
     * @param number  the empNumber of the user.
     * @param theName the name of the employee.
     * @param theUserName   the loginID of the user.
     */
    public Employee(final int number, 
            final String theName, final String theUserName) {
        name = theName;
        empNumber = number;
        userName = theUserName;
    }
    
    /**
     * name isEditable.
     * 
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * name setEditable.
     * 
     * @param newValue as boolean.
     */
    public void setEditable(boolean newValue) {
        editable = newValue;
    }

    /**
     * name getter.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * name setter.
     * 
     * @param empName the name to set
     */
    public void setName(final String empName) {
        name = empName;
    }

    /**
     * empNumber getter.
     * 
     * @return the empNumber
     */
    public int getEmpNumber() {
        return empNumber;
    }

    /**
     * empNumber setter.
     * 
     * @param number the empNumber to set
     */
    public void setEmpNumber(final int number) {
        empNumber = number;
    }

    /**
     * userName getter.
     * 
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * userName setter.
     * 
     * @param id the userName to set
     */
    public void setUserName(final String id) {
        userName = id;
    }

}
