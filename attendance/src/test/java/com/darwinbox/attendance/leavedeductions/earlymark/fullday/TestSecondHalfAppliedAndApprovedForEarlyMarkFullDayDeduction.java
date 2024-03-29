package com.darwinbox.attendance.leavedeductions.earlymark.fullday;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.AttendanceSettingsPage;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.leavedeductions.EarlyMark;
import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.attendance.services.settings.AttendanceSettingsServices;
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
import java.util.List;
import java.util.Map;

public class TestSecondHalfAppliedAndApprovedForEarlyMarkFullDayDeduction extends TestBase {

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

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "EarlyMark,LeaveDeduction", retryAnalyzer = TestBase.class)
    public void testSecondHalfAppliedAndApprovedForEarlyMarkFullDayDeduction(Map<String, String> testData) {

        String title = " With Second Half Applied and Approved ";

        boolean isApproved = true;
        boolean isFirst = false;
        boolean isSecond = true;

        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "Login Unsuccessfull ");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessfull ");

        AttendanceTestBase atb = AttendanceTestBase.getObject("LeaveDeductionPolicies.xlsx");

        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("Shift Name"));

        boolean isPayCycle = testData.get("isPayCycle").equalsIgnoreCase("yes");

        AttendanceSettingsPage attSettings = new AttendanceSettingsPage();
        attSettings.setUsePayrollCycle(isPayCycle);

        AttendanceSettingsServices srvc = new AttendanceSettingsServices();
        srvc.createSettings(attSettings);

        String leaveName = testData.get("Leave Name");
        String leaveToApply = testData.get("ApplyLeave");

        EarlyMark earlyMark = policy.getEarlyMark();

        if (earlyMark == null) {
            Reporter("EarlyMark deduction is disabled ", "FAIL");
            return;
        }

        title += " >> Attendance Policy ";

        title += earlyMark.isWeekoff() ? " >> WeeklyOff " : "";
        title += earlyMark.isHoliday() ? " >> Holiday " : "";

        if ((!earlyMark.isWeekoff()) && (!earlyMark.isHoliday())) {
            title += " >> Empty ";
        }

        Reporter(" Test Scenario  : " + title, "INFO");

        for (LeaveDeductionsBase.DAYSTATUS day : LeaveDeductionsBase.DAYSTATUS.values()) {

            String temp = " >> Status ";

            boolean isboth = day.equals(LeaveDeductionsBase.DAYSTATUS.WH);
            boolean isholiday = day.equals(LeaveDeductionsBase.DAYSTATUS.HOLIDAY) || isboth;
            boolean isWeekoff = day.equals(LeaveDeductionsBase.DAYSTATUS.WEEKOFF) || isboth;

            temp += isWeekoff ? " >> WeeklyOff " : "";
            temp += isholiday ? " >> Holiday " : "";

            if ((!isWeekoff) && (!isholiday)) {
                temp += " >> Empty ";
            }

            Employee employee = empService.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);
            atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj());
            Date date = dateHelper.formatStringToDate("yyyy-MM-dd", employee.getDoj());
            date = dateHelper.getPreviousDate(dateHelper.getByPayCycle(isPayCycle,date));

            Reporter("Employee created " + employee.getUserID(), "INFO");
            List<Date> dates = dateHelper.getDatesForNextNDays(date, earlyMark.getCount() * 2+2);

            for ( Date d : dates ) {

                Map<String, String> body = earlyMark.getEarlymark(employee.getEmployeeID(), policy.getPolicyInfo(), shift, d, isWeekoff);

                String leaveid = atb.getLeaveId(leaveToApply);
                atb.applyLeave(d, employee, leaveid, isFirst, isSecond, isApproved);

                if (isholiday) {
                    atb.createHoliday(d);
                }

                atb.importBackdated(body);

                String date_test = " >> Date :" + body.get("UserAttendanceImportBack[2][1]");
                temp += date_test;

                String status = atb.getAttendanceStatus(employee.getMongoID(), d);

                Reporter(" Day Status " + temp, "INFO");
                Reporter(" Actual Status " + date_test + " " + status.replaceAll("\\<.*?>", ""), "INFO");

                atb.validateHoliday(isholiday, status, this);
                atb.validateWeekoff(isWeekoff, status, this);

                atb.validateLeave(isApproved, isFirst || isSecond, status, leaveToApply, this);
                atb.validateNoLeave(status, leaveName, this);

            }
        }
    }

}