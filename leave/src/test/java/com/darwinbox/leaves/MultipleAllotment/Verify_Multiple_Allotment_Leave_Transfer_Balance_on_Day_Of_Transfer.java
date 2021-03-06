package com.darwinbox.leaves.MultipleAllotment;


import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.dashboard.pageObjectRepo.generic.RightMenuOptionsPage;
import com.darwinbox.dashboard.pageObjectRepo.settings.CommonSettingsPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.darwinbox.leaves.pageObjectRepo.settings.CreateAndManageLeavePoliciesPage;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesSettingsPage;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class Verify_Multiple_Allotment_Leave_Transfer_Balance_on_Day_Of_Transfer extends TestBase {

    HomePage homepage;
    LoginPage loginpage;
    WaitHelper objWaitHelper;
    CommonSettingsPage commonSettings;
    LeavesSettingsPage leaveSettings;
    CreateAndManageLeavePoliciesPage createManageLeaves;
    RightMenuOptionsPage rightMenuOption;
    LeavesAction leavesAction;
    UtilityHelper objUtil;
    CommonAction commonAction;

    private static final Logger log = Logger.getLogger(Verify_Multiple_Allotment_Leave_Transfer_Balance_on_Day_Of_Transfer.class);

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass( ).getName( ));
    }

    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
        commonSettings = PageFactory.initElements(driver, CommonSettingsPage.class);
        leaveSettings = PageFactory.initElements(driver, LeavesSettingsPage.class);
        createManageLeaves = PageFactory.initElements(driver, CreateAndManageLeavePoliciesPage.class);
        rightMenuOption = PageFactory.initElements(driver, RightMenuOptionsPage.class);
        leavesAction = PageFactory.initElements(driver, LeavesAction.class);
        objUtil = PageFactory.initElements(driver, UtilityHelper.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void Verify_Multiple_Allotment_Leave_Transfer_Balance_on_Day_Of_Transfer(Map<String, String> data) throws Exception {

        Assert.assertTrue(leavesAction.setEmployeeID(UtilityHelper.getProperty("config", "Employee.id")), "Employee ID is set successfully to test");
        Assert.assertTrue(leavesAction.setLeaveType( ), "Leave Type is set successfully");
        Assert.assertTrue(leavesAction.setLeaveScenarioFromExcelFile( ), "Leave scenario is set successfully");

        Assert.assertTrue(loginpage.loginToApplication( ), "User Loggin to Application as Admin");
        Assert.assertTrue(commonAction.changeApplicationAccessMode("Admin"), "Application access changed to Admin mode");

        leavesAction.getAllEmployeeTypesInInstance( );
        leavesAction.getEmployeeData( );
//		Assert.assertTrue(leavesAction.verifyMultipleAllotmentLeaveTransferBalanceForCurrentCycle(), "Leave Balance for whole leave cycle calculated successfully") ;
        Assert.assertTrue(leavesAction.verifyMultipleAllotmentLeaveTransferBalanceOnDayOfTransfer( ), "Leave Balance for whole leave cycle calculated successfully");

    }
}
