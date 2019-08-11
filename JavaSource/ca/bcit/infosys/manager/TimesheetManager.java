package ca.bcit.infosys.manager;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;
import ca.bcit.infosys.timesheet.TimesheetRow;
/**
 * Manage Employee.
 * 
 * @author Yang
 * @version 1.1
 */
@Named
@ConversationScoped
public class TimesheetManager implements TimesheetCollection {
    /** DEFAULT_ROW_NUM default row number for initialize a timesheet. */
    public static final int DEFAULT_ROW_NUM = 5;
    /** timeSheetList as Timesheet List. */
    private List<Timesheet> timeSheetList = new ArrayList<Timesheet>();
    /** timesheetRowList as TimesheetRow List. */
    private ArrayList<TimesheetRow> timesheetRowList;
    /** employeeManager as EmployeeManager. */
    private EmployeeManager employeeManager;
    /** curEmployeeTimeSheetList as Timesheet List. */
    private List<Timesheet> curEmployeeTimeSheetList =
            new ArrayList<Timesheet>();
    /** currentTimesheet as TimesheetRow List. */
    private ArrayList<TimesheetRow> currentTimesheetRowList =
            new ArrayList<TimesheetRow>();
    /** empTimeSheetShow as Timesheet. */
    @Inject
    private Timesheet empTimeSheetShow;

    /**
     * The no-argument constructor. Used to create new TimesheetManager 
     * from within the application.
     * @param theEmployeeManager as EmployeeManager
     */
    public TimesheetManager(EmployeeManager theEmployeeManager) {
        this.employeeManager = theEmployeeManager;
    }
    
    @Override
    public List<Timesheet> getTimesheets() {
        // TODO Auto-generated method stub
        return timeSheetList;
    }

    @Override
    public List<Timesheet> getTimesheets(Employee e) {
        List<Timesheet> temp = new ArrayList<Timesheet>();

        for (Timesheet timesheet: timeSheetList) {
            if (timesheet.getEmployee() != null && e != null) {
                if (timesheet.getEmployee().getEmpNumber() 
                        == e.getEmpNumber()) {
                    temp.add(timesheet);
                }
            }
        }

        return temp;
    }

    @Override
    public Timesheet getCurrentTimesheet(Employee e) {
        
        List<Timesheet> temp = new ArrayList<Timesheet>();
        for (Timesheet timesheet: timeSheetList) {
            if (timesheet.getEmployee().getEmpNumber() == e.getEmpNumber()) {
                temp.add(timesheet);
            }
        }
        
        Timesheet max = temp.get(0);
        
        for (Timesheet tempSheet: temp) {
            if (max.getEndWeek().compareTo(tempSheet.getEndWeek()) < 0) {
                max = tempSheet;
            }
        }
        return max;
    }

    @Override
    public String addTimesheet() {   
        timesheetRowList = new ArrayList<TimesheetRow>();
        for (int i = 0; i < DEFAULT_ROW_NUM; i++) {
            timesheetRowList.add(new TimesheetRow());
        }             
        // TODO Auto-generated method stub
        return "createTimeSheet";
    }
    
    /**
     * function addOneRow.
     * @return the page direction
     */
    public String addOneRow() {                             
        for (int i = 0; i < 1; i++) {
            timesheetRowList.add(new TimesheetRow());
        }             
        // TODO Auto-generated method stub
        return "createTimeSheet";
    }
    
    /**
     * function editAddOneRow.
     * @return the page direction
     */
    public String editAddOneRow() {                             
        for (int i = 0; i < 1; i++) {
            timesheetRowList.add(new TimesheetRow());
        }             
        // TODO Auto-generated method stub
        return "editCurTimesheet";
    }
    
    /**
     * function submitTimeSheet.
     * @return the page direction
     */
    public String submitTimeSheet() {
        Timesheet temp = new Timesheet();
        temp.setDetails(timesheetRowList);
        temp.setEmployee(employeeManager.getCurrentEmployee());
        
        if (employeeManager.getTimesheetAccess().insertTimesheet(temp)) {
            timeSheetList.add(temp);
            curEmployeeTimeSheetList = 
                    getTimesheets(employeeManager.getCurrentEmployee());
    
            
            return "finsihCreateTimeSheet";
        } else {
            employeeManager.setMessage(
                    "Project and WP canot be null, and The "
                    + "combination of project and WP must be unique");
            return null;
        }
    }
    
    /**
     * function updataTimeSheet.
     * @return the page direction
     */
    public String updataTimeSheet() {
        Timesheet temp = empTimeSheetShow;
        temp.setDetails(currentTimesheetRowList);
        
        timeSheetList.remove(getCurrentTimesheet(employeeManager.
                getCurrentEmployee()));
       
        timeSheetList.add(temp);
        System.out.println(temp.getEndWeek());
        //update database for update timesheet
        employeeManager.getTimesheetAccess().updateTimesheet(temp);
        curEmployeeTimeSheetList = 
                getTimesheets(employeeManager.getCurrentEmployee());
        return "finsihCreateTimeSheet";
    }
    /**
     * function editCurTimesheet.
     * @return the page direction
     */
    public String editCurTimesheet() {
        

        currentTimesheetRowList = 
                (ArrayList<TimesheetRow>) getCurrentTimesheet(
                        employeeManager.getCurrentEmployee()).getDetails();
        timesheetRowList = currentTimesheetRowList;
        empTimeSheetShow = getCurrentTimesheet(
                employeeManager.getCurrentEmployee());
        return "editCurTimesheet";
    }
    
    /**
     * function viewTimeSheet.
     * @param empTimeSheetList as Timesheet.
     * @return the page direction
     */
    public String viewTimeSheet(Timesheet empTimeSheetList) {
        empTimeSheetShow = empTimeSheetList;
        timesheetRowList = 
                (ArrayList<TimesheetRow>) empTimeSheetList.getDetails();
        return "showTimeSheet";     
    }
    
    /**
     * Returns the timeSheetList for this TimesheetManager.
     * @return the timeSheetList
     */
    public List<Timesheet> getTimeSheetList() {
        return timeSheetList;
    }
    /**
     * Sets the timeSheetList for this TimesheetManager.
     * @param timeSheetList the timeSheetList to set
     */
    public void setTimeSheetList(List<Timesheet> timeSheetList) {
        this.timeSheetList = timeSheetList;
    }

    /**
     * Returns the timesheetRowList for this TimesheetManager.
     * @return the timesheetRowList
     */
    public ArrayList<TimesheetRow> getTimesheetRowList() {
        return timesheetRowList;
    }

    /**
     * Sets the timesheetRowList for this TimesheetManager.
     * @param timesheetRowList the timesheetRowList to set
     */
    public void setTimesheetRowList(ArrayList<TimesheetRow> timesheetRowList) {
        this.timesheetRowList = timesheetRowList;
    }

    /**
     * Returns the curEmployeeTimeSheetList for this TimesheetManager.
     * @return the curEmployeeTimeSheetList
     */
    public List<Timesheet> getCurEmployeeTimeSheetList() {
        return curEmployeeTimeSheetList;
    }

    /**
     * Sets the curEmployeeTimeSheetList for this TimesheetManager.
     * @param curEmployeeTimeSheetList the curEmployeeTimeSheetList to set
     */
    public void setCurEmployeeTimeSheetList(List<Timesheet> 
    curEmployeeTimeSheetList) {
        this.curEmployeeTimeSheetList = curEmployeeTimeSheetList;
    }

    /**
     * Returns the currentTimesheet for this TimesheetManager.
     * @return the currentTimesheet
     */
    public ArrayList<TimesheetRow> getCurrentTimesheetRowList() {
        return currentTimesheetRowList;
    }

    /**
     * Sets the currentTimesheet for this TimesheetManager.
     * @param currentTimesheet the currentTimesheet to set
     */
    public void setCurrentTimesheetRowList(
            ArrayList<TimesheetRow> currentTimesheet) {
        this.currentTimesheetRowList = currentTimesheet;
    }

    /**
     * Returns the empTimeSheetShow for this TimesheetManager.
     * @return the empTimeSheetShow
     */
    public Timesheet getEmpTimeSheetShow() {
        return empTimeSheetShow;
    }

    /**
     * Sets the empTimeSheetShow for this TimesheetManager.
     * @param empTimeSheetShow the empTimeSheetShow to set
     */
    public void setEmpTimeSheetShow(Timesheet empTimeSheetShow) {
        this.empTimeSheetShow = empTimeSheetShow;
    }


    

}
