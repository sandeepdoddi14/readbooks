package com.darwinbox.attendance.leavedeductions.sortorder;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.leavedeductions.EarlyDuration;
import com.darwinbox.attendance.objects.policy.leavedeductions.LateMark;
import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Map;

public class TestForLateMark extends TestBase {

    public static Employee employee = null;
    public static Date date;
    LoginPage loginPage;
    GenericHelper genHelper;
    DateTimeHelper dateHelper;
    EmployeeServices empService;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        genHelper = new GenericHelper(driver);
        empService = new EmployeeServices();
        dateHelper = new DateTimeHelper();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "EarlyDuration,LeaveDeduction", retryAnalyzer = TestBase.class)
    public void testFirstHalfAppliedAndApproved(Map<String, String> testData) {

        String title = " With First Half Applied and Approved ";

        boolean isApproved = true;
        boolean isFirst = true;
        boolean isSecond = false;

        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "Login Unsuccessfull ");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessfull ");

        AttendanceTestBase atb = AttendanceTestBase.getObject("SortOrder.xlsx");

        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("Shift Name"));

        if (employee == null) {
            employee = empService.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);
            atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj());
            date = dateHelper.formatStringToDate("yyyy-MM-dd", employee.getDoj());
        }

        Reporter("Employee created " + employee.getUserID(), "INFO");

        String leaveName = testData.get("Leave Name");
        String leaveToApply = testData.get("ApplyLeave");

        LateMark lateMark = policy.getLateMark();

        Reporter(" Test Scenario  : " + title, "INFO");


            date = dateHelper.getNextDate(date);

            String temp = " >> Status ";

            String leaveid = atb.getLeaveId(leaveToApply);
            atb.applyLeave(date, employee, leaveid, isFirst, isSecond, isApproved);

            String status = atb.getAttendanceStatus(employee.getMongoID(), date);

            Reporter(" Day Status " + temp, "INFO");

            atb.validateLeave(isApproved, isFirst || isSecond, status, leaveToApply, this);

            boolean proceed = false;

            if (proceed) {
                atb.validateLeave(!lateMark.isApprovalRequired(), isFirst || isSecond, status, leaveName, this);
            } else {
                atb.validateNoLeave(status, leaveName, this);
            }

            validateDate();
    }

    private void validateDate() {

        Date local = dateHelper.getNextDate(date);

        String curr = dateHelper.formatDateTo(new Date(), "yyyy-MM-dd");
        String expected = dateHelper.formatDateTo(local, "yyyy-MM-dd");

        if (curr.equals(expected)) {
            employee = empService.createAnEmployee(employee.getCompanyID().equals("main"));
            AttendanceTestBase.getObject().assignPolicyAndShift(employee.getUserID(), employee.getDoj());
            date = dateHelper.formatStringToDate("yyyy-MM-dd", employee.getDoj());
        }
    }
}