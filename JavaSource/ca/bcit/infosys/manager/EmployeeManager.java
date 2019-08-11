package ca.bcit.infosys.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.access.CredentialCheck;
import ca.bcit.infosys.access.EmployeeAccess;
import ca.bcit.infosys.access.TimesheetAccess;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;
import ca.bcit.infosys.timesheet.Timesheet;

/**
 * Manage Employee.
 * 
 * @author Yang
 * @version 1.1
 */
@Named
@ConversationScoped
public class EmployeeManager implements EmployeeList {

    /** DEFAULT_PWD default password. */
    public static final String DEFAULT_PWD = "000000";
    /** credentials as Credentials. */
    @Inject
    private Credentials credentials;
    /** employeeInfoList as Employee list. */
    private List<Employee> employeeInfoList;
    /** IdPwdMap as Map list. */
    private Map<String, String> idPwdMap = new HashMap<String, String>();
    /** employee as Employee. */
    @Inject
    private Employee employee;
    /** message as String. */
    private String message;
    /** timesheetManager as TimesheetManager. */
    private TimesheetManager timesheetManager;
    /** timesheet as Timesheet. */
    @Inject
    private Timesheet timesheet;
    /** curEmployee as Employee. */
    private Employee curEmployee;
    /** adminEmployee as Employee. */
    private Employee adminEmployee;
    /** loginCheck as LoginCheck. */
    @Inject
    private CredentialCheck credentialCheck;
    /** employeeAccess as EmployeeAccess. */
    @Inject
    private EmployeeAccess employeeAccess;
    /** timesheetAccess as TimesheetAccess. */
    @Inject
    private TimesheetAccess timesheetAccess;

    /** conversation as Conversation. */
    private @Inject 
    Conversation conversation;

    
    /**
     * The no-argument constructor. 
     * Used to create new EmployeeManager from within
     * the application.
     */
    public EmployeeManager() {
        employeeInfoList = new ArrayList<Employee>();
        timesheetManager = new TimesheetManager(this);
    }



    
    /**
     * function login.
     * 
     * @return the page direction
     */
    public String login() {
        employeeInfoList = employeeAccess.getAllEmployee();
        if (verifyUser(credentials)) {
            if (credentials.getUserName().equals("admin")) {
                this.conversation.begin(); 
                adminEmployee = getEmployee("admin");
                message = "";
                return "adminPanel";
            } else {
                this.conversation.begin(); 
                curEmployee = getEmployee(credentials.getUserName());
                timesheetManager.setTimeSheetList(
                        timesheetAccess.getAllEmployeeTimeSheetList());
                timesheetManager.setCurEmployeeTimeSheetList(
                        timesheetManager.getTimesheets(curEmployee));
                message = "";
                return "employeePanel";
            }
        }
        message = "User name or password is wrong;";
        return "loginFailed";
    }

    @Override
    public List<Employee> getEmployees() {
        // TODO Auto-generated method stub
        return employeeInfoList;
    }

    @Override
    public Employee getEmployee(String name) {
        for (Employee myEmployee : employeeInfoList) {
            if (myEmployee.getUserName().equals(name)) {
                return myEmployee;
            }
        }
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, String> getLoginCombos() {
        // TODO Auto-generated method stub
        return idPwdMap;
    }

    @Override
    public Employee getCurrentEmployee() {
        // TODO Auto-generated method stub
        return curEmployee;
    }

    @Override
    public Employee getAdministrator() {
        // TODO Auto-generated method stub
        return adminEmployee;
    }

    @Override
    public boolean verifyUser(Credentials credential) {
        return credentialCheck.loginCheck(credentials);
    }

    @Override
    public String logout(Employee myEmployee) {
        message = "";
        this.conversation.end();
        // TODO Auto-generated method stub
        return "logout";
    }

    @Override
    public void deleteEmployee(Employee userToDelete) {
        message = "";
        if (!userToDelete.getUserName().equalsIgnoreCase("admin")) {
            employeeAccess.removeEmployee(userToDelete);
            employeeInfoList.remove(userToDelete);
        } else {
            message = "You can not delete admin";
        }
    }

    @Override
    public void addEmployee(Employee newEmployee) {
        
        this.employeeInfoList
                .add(new Employee(newEmployee.getEmpNumber(), 
                        newEmployee.getName(), newEmployee.getUserName()));
        idPwdMap.put(newEmployee.getUserName(), DEFAULT_PWD);
    }

    /**
     * function updateInfo.
     * 
     * @return null
     */
    public String updateInfo() {
        for (Employee myEmployee : employeeInfoList) {
            if (myEmployee.isEditable()) {
                if (employeeAccess.updateEmployee(myEmployee)) {
                    message = "";
                    myEmployee.setEditable(false);
                } else {
                    message = "The employee number for " 
                            + myEmployee.getUserName() + " already exist, "
                            + "please choose another one";
                }
            }
        }
        return null;
    }

    /**
     * function createEmployee.
     * 
     * @return page direction
     */
    public String createEmployee() {
        message = "";
        if (employeeAccess.insertEmployee(employee)) {
            addEmployee(employee);
            return "sucessCreated";
        } else {
            message = "The employee number or " 
                    + "employee user name for already exist, "
                    + "please choose another one";
            return null;
        }     
    }

    /**
     * function createTimeSheet.
     * 
     * @return page direction
     */
    public String createTimeSheet() {
        timesheet.setEmployee(curEmployee);
        timesheetManager.getTimesheets().add(timesheet);
        return "sucessCreated";
    }

    /**
     * function resetting password.
     * 
     * @param myEmployee as Employee
     */
    public void resettingPwd(Employee myEmployee) {
        credentialCheck.setDefaultPwd(myEmployee);
        idPwdMap.put(myEmployee.getUserName(), DEFAULT_PWD);
        // return "admin";
    }

    /**
     * function changePassword.
     * 
     * @return page direction
     */
    public String changePassword() {
        return "changePassword";
    }

    /**
     * function changePassword.
     * 
     * @return page direction
     */
    public String back() {
        message = "";
        if (credentials.
                getUserName().equalsIgnoreCase("admin")) {
            return "backAdmin";
        } else {
            return "backEmp";
        }     
    }

    
    /**
     * function changePwd.
     * 
     * @return page direction
     */
    public String changePwd() {
        String newPwd = credentials.getPassword();
        credentialCheck.changePwd(credentials);
        idPwdMap.put(curEmployee.getUserName(), newPwd);
        return "successChangePwd";
    }

    /**
     * Returns the credentials for this EmployeeManager.
     * 
     * @return the credentials
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * Sets the credentials for this EmployeeManager.
     * 
     * @param credentials the credentials to set
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Returns the employeeInfoList for this EmployeeManager.
     * 
     * @return the employeeInfoList
     */
    public List<Employee> getEmployeeInfoList() {
        return employeeInfoList;
    }

    /**
     * Sets the employeeInfoList for this EmployeeManager.
     * 
     * @param employeeInfoList the employeeInfoList to set
     */
    public void setEmployeeInfoList(List<Employee> employeeInfoList) {
        this.employeeInfoList = employeeInfoList;
    }

    /**
     * Returns the employee for this EmployeeManager.
     * 
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Sets the employee for this EmployeeManager.
     * 
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Returns the timesheetManager for this EmployeeManager.
     * 
     * @return the timesheetManager
     */
    public TimesheetManager getTimesheetManager() {
        return timesheetManager;
    }

    /**
     * Sets the timesheetManager for this EmployeeManager.
     * 
     * @param timesheetManager the timesheetManager to set
     */
    public void setTimesheetManager(TimesheetManager timesheetManager) {
        this.timesheetManager = timesheetManager;
    }

    /**
     * Returns the timesheet for this EmployeeManager.
     * 
     * @return the timesheet
     */
    public Timesheet getTimesheet() {
        return timesheet;
    }

    /**
     * Sets the timesheet for this EmployeeManager.
     * 
     * @param timesheet the timesheet to set
     */
    public void setTimesheet(Timesheet timesheet) {
        this.timesheet = timesheet;
    }

    /**
     * Returns the curEmployee for this EmployeeManager.
     * 
     * @return the curEmployee
     */
    public Employee getCurEmployee() {
        return curEmployee;
    }

    /**
     * Sets the curEmployee for this EmployeeManager.
     * 
     * @param curEmployee the curEmployee to set
     */
    public void setCurEmployee(Employee curEmployee) {
        this.curEmployee = curEmployee;
    }

    /**
     * Returns the message for this EmployeeManager.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message for this EmployeeManager.
     * 
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the timesheetAccess for this EmployeeManager.
     * 
     * @return the timesheetAccess
     */
    public TimesheetAccess getTimesheetAccess() {
        return timesheetAccess;
    }

    /**
     * Sets the timesheetAccess for this EmployeeManager.
     * 
     * @param timesheetAccess the timesheetAccess to set
     */
    public void setTimesheetAccess(TimesheetAccess timesheetAccess) {
        this.timesheetAccess = timesheetAccess;
    }

    /**
     * Returns the adminEmployee for this EmployeeManager.
     * @return the adminEmployee
     */
    public Employee getAdminEmployee() {
        return adminEmployee;
    }

    /**
     * Sets the adminEmployee for this EmployeeManager.
     * @param adminEmployee the adminEmployee to set
     */
    public void setAdminEmployee(Employee adminEmployee) {
        this.adminEmployee = adminEmployee;
    }

    /**
     * Returns the conversation for this EmployeeManager.
     * @return the conversation
     */
    public Conversation getConversation() {
        return conversation;
    }


    /**
     * Sets the conversation for this EmployeeManager.
     * @param conversation the conversation to set
     */
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
    
    

}
