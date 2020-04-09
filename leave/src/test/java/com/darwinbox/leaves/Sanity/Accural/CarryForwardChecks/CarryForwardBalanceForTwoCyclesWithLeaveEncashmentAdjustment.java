package com.darwinbox.leaves.Sanity.Accural.CarryForwardChecks;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Accural.CarryForwardUnusedLeave;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Accural.Credit_On_Accural_Basis;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.*;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.apache.http.NameValuePair;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class CarryForwardBalanceForTwoCyclesWithLeaveEncashmentAdjustment extends LeaveAccuralBase {


    Employee employee = new Employee();

    LoginPage loginpage = null;
    CommonAction commonAction = null;
    Boolean runTest = true;



    LocalDate doj = null;

    LeavesPage leavePage;
    LeavesAction leavesAction = null;


    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");
        leavesAction = new LeavesAction(driver);

        leavePage = PageFactory.initElements(driver, LeavesPage.class);

    }

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void VerifyCarryForwardForTwoCyclesWithLeaves(Map<String, String> testData) throws  Exception{


        int noOfLeaves = 3;
        //always give current year
        leaveCycleStartDate = LocalDate.parse("2019-04-01");
        leaveCycleEndDate = LocalDate.parse("2020-03-31");

        //LeavePolicyObject carryForwardBalance = getCarryForwardPolicy(testData);

        LeavePolicyObject carryForwardBalance = new LeavePolicyObject();
        carryForwardBalance.setFields(testData);
        carryForwardBalance.setEncashmentData(testData);
        if(testData.get("Carry forward").equalsIgnoreCase("yes")){
            CarryForwardUnusedLeave carryForwardUnusedLeave = new CarryForwardUnusedLeave();
            carryForwardUnusedLeave.indicator=testData.get("Carry forward").equalsIgnoreCase("yes")?true:false;

            if(testData.get("Carry forward All/Fixed/Percentage").equalsIgnoreCase("all"))
            {
                carryForwardUnusedLeave.carryForwardAllUnusedLeave=true;
            }
            if(testData.get("Carry forward All/Fixed/Percentage").equalsIgnoreCase("fixed")){
                carryForwardUnusedLeave.carryForwardOnly=true;
                carryForwardUnusedLeave.fixed=true;
                carryForwardUnusedLeave.fixedValue=Integer.parseInt(testData.get("Fixed/Percentage value"));
            }
            if(testData.get("Carry forward All/Fixed/Percentage").equalsIgnoreCase("percentage")){
                carryForwardUnusedLeave.carryForwardOnly=true;
                carryForwardUnusedLeave.percentage=true;
                carryForwardUnusedLeave.percentageValue=Integer.parseInt(testData.get("Fixed/Percentage value"));
            }

            carryForwardBalance.setCarryForwardUnusedLeave(carryForwardUnusedLeave);

        }

        List<NameValuePair> body = carryForwardBalance.createRequest();
        new LeaveService().createLeaveForPolicy(body, carryForwardBalance);

        super.carryForward = true;
        //making default to begin of month for calculation
        if (carryForwardBalance.getCredit_on_accural_basis().getIndicator()) {
            Credit_On_Accural_Basis credit_on_accural_basis = carryForwardBalance.getCredit_on_accural_basis();
            credit_on_accural_basis.setMonthlyAccuralSetting(true, true, false);
            credit_on_accural_basis.setQuarterlyAccural(false, false, false);
            credit_on_accural_basis.setBiAnnual(false);
            carryForwardBalance.setCredit_on_accural_basis(credit_on_accural_basis);
        }

        //leave validity also needs to be set to zero for carry forward
        if (carryForwardBalance.getProbation_period_before_leave_validity().custom &&
                !carryForwardBalance.getCredit_on_pro_rata_basis().calculateAfterProbationPeriod)
            carryForwardBalance.getProbation_period_before_leave_validity().customMonths = 0;

        //if(carryForwardBalance.getProbation_period_before_leave_validity().probation)
        ///carryForwardBalance.getProbation_period_before_leave_validity()

        super.setLeavePolicyObject(carryForwardBalance);


        changeServerDate(LocalDate.now());


        try {
            employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.minusYears(1).toString(), "no"));
        } catch (Exception e) {
            try {
                employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.minusYears(1).toString(), "no"));
            } catch (Exception e1) {
                employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.minusYears(1).toString(), "no"));

            }
        }

        leavesAction.setEmployeeID(employee.getEmployeeID());
        super.employee = employee;

        Reporter("Employee DOJ is --> " + employee.getDoj(), "INFO");

        //changeServerDate(leaveCycleStartDate.minusYears(1));
        //leavePage.setFromAndToDatesWithoutProperty(noOfLeaves, LocalDate.parse(employee.getDoj()));
        //applyLeave(employee,leavePolicyObject,leavePage.workingDays);


        Reporter("Leave Cycle Start Date is --> " + leaveCycleStartDate.minusYears(1).toString(), "Info");
        Reporter("Leave Cycle End Date is --> " + leaveCycleEndDate.minusYears(1).toString(), "Info");
        changeServerDate(leaveCycleStartDate);
        Reporter("Carry Forward Cron Run Date is --> " + serverChangedDate, "Info");
        leavesAction.runCarryFrowardCronByEndPointURL();


        double actualLeaveBalance = 0.0D;
        double expecetedLeaveBalance = 0.0D;

        super.carryForward = true;

        expecetedLeaveBalance = calculateLeaveBalance(employee.getDoj(), leaveCycleEndDate.minusYears(1).toString());
        Reporter("Expected CF Leave Balance is --" + expecetedLeaveBalance, "Info");


        actualLeaveBalance = new LeaveBalanceAPI(employee.getEmployeeID(), carryForwardBalance.getLeave_Type()).getCarryForwardBalance();
        Reporter("Actual CF Leave Balance is ---" + actualLeaveBalance, "Info");

        if (expecetedLeaveBalance == actualLeaveBalance)
            Reporter("Passed |||| actual and expected are same", "Pass");
        else
            Reporter("Failed |||| actual and expected are not same", "Fail");


        //(leaveCycleStartDate.minusYears(1));
        changeServerDate(leaveCycleStartDate.plusDays(10));
        leavePage.setFromAndToDatesWithoutProperty(noOfLeaves, serverDateInFormat);
        applyLeave(employee, leavePolicyObject, leavePage.workingDays);
        Reporter("No of Leaves applied -->" + noOfLeaves + ";From Date -->" + leavePage.workingDays[0] + "     ;To Date is  --> " + leavePage.workingDays[leavePage.workingDays.length - 1], "Info");


        int forwardDateForEncashmnet = leavePage.workingDays.length;
        changeServerDate(serverDateInFormat.plusDays(forwardDateForEncashmnet));


        logoutFromSession();
        // Thread.sleep(5000);
        loginpage.loginToApplication(employee.getEmailID(), employee.getPassword());
        new LeaveAdmin().applyEncashmentWithEmpSession(employee,serverChangedDate,noOfLeaves+"",new LeaveService().getLeaveID(carryForwardBalance.getLeave_Type(),carryForwardBalance.groupCompanyMongoId));
        Reporter("No of Leaves Encashed   -->" +noOfLeaves+";Date -->"+serverChangedDate,"Info");


        logoutFromSession();
        loginpage.loginToApplication();
        loginpage.switchToAdmin();


        String messageId = new LeaveAdmin().getMessageId(employee);
        new LeaveAdmin().encashmentAction(messageId, "accept");



        int adjustedBalance = -2;
        new LeaveSettings().showLeaveAdjustments(carryForwardBalance.getLeave_Type());
        new ImportServices().importLeaveAdjustmentBalance(employee.getEmployeeID(), carryForwardBalance.getLeave_Type(), adjustedBalance + "",  leaveCycleStartDate.getYear()+ "");
        Reporter("No of Leaves Adjusted is   -->" + adjustedBalance + ";Date -->" + serverChangedDate, "Info");


        Reporter("Leave Cycle Start Date is --> " + leaveCycleStartDate.toString(), "Info");
        Reporter("Leave Cycle End Date is --> " + leaveCycleEndDate.toString(), "Info");
        changeServerDate(leaveCycleStartDate.plusYears(1));
        Reporter("Carry Forward Cron Run Date is --> " + serverChangedDate, "Info");

        leavesAction.runCarryFrowardCronByEndPointURL();


        super.carryForward = true;

        expecetedLeaveBalance = expecetedLeaveBalance + carryForwardBalance.getMaximum_leave_allowed_per_year() - adjustedBalance - noOfLeaves * 2;

        Reporter("Expected CF Leave Balance is --" + expecetedLeaveBalance, "Info");


        actualLeaveBalance = new LeaveBalanceAPI(employee.getEmployeeID(), carryForwardBalance.getLeave_Type()).getCarryForwardBalance();
        Reporter("Actual CF Leave Balance is ---" + actualLeaveBalance, "Info");

        Assert.assertTrue(expecetedLeaveBalance == actualLeaveBalance, "Failed |||| actual and expected are not same");

    }
    }

