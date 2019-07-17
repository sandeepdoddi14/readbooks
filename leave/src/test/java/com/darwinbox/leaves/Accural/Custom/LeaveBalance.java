package com.darwinbox.leaves.Accural.Custom;

import Objects.Employee;
import Objects.LeavePolicyObject.LeavePolicyObject;
import Service.EmployeeServices;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;


public class LeaveBalance extends LeaveAccuralBase {

    Employee employee = null;
    LocalDate dateOfJoining= null;
    LeavePolicyObject leaveBalancePolicy=null;
    String employeeProbation="no";

    LoginPage loginpage=null;
    CommonAction commonAction=null;

    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");

    }
        @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyLeaveBalance(Map<String,String> testData) {
        dateOfJoining = LocalDate.now();

        LeavePolicyObject leaveBalancePolicy=getLeaveBalancePolicy(testData);
        super.setLeavePolicyObject(leaveBalancePolicy);

        //if(leaveBalancePolicy.getProbation_period_before_leave_validity().probation)
       // employeeProbation=testData.get("Employee Probation Period");

       employee = new EmployeeServices().generateAnEmployee("no","Working Days (DO NOT TOUCH)",dateOfJoining.toString(),employeeProbation);

        super.setEmployee(employee);
        verifyEmployeeLeaveBalanceForWholeLeaveCycleForFourEdgeDays();
    }

    }