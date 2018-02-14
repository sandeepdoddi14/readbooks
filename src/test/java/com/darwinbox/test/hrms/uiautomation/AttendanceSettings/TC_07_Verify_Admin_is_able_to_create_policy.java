package com.darwinbox.test.hrms.uiautomation.AttendanceSettings;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.darwinbox.test.hrms.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.test.hrms.uiautomation.Pages.HomePage;
import com.darwinbox.test.hrms.uiautomation.Pages.LoginPage;
import com.darwinbox.test.hrms.uiautomation.Pages.RightMenuOptionsPage;
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.AttendanceSettingsPolicyPage;
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.CommonSettingsPage;
import com.darwinbox.test.hrms.uiautomation.Utility.ExcelReader;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;

import java.util.Map;

public class TC_07_Verify_Admin_is_able_to_create_policy extends TestBase {

	HomePage homepage;
	LoginPage loginpage;
	WaitHelper objWaitHelper;
	CommonSettingsPage commonSettings;
	AttendanceSettingsPolicyPage attPolicySettings;
	RightMenuOptionsPage rightMenuOption;

	private static final Logger log = Logger
			.getLogger(TC_07_Verify_Admin_is_able_to_create_policy.class);

	@BeforeClass
	public void setup() throws Exception {
		ExcelReader.setFilenameAndSheetName("Attendance_Settings_TestData.xlsx", "TC_07");
	}
	
	@BeforeMethod
	public void initializeObjects() {
		loginpage = PageFactory.initElements(driver, LoginPage.class);
		objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
		homepage = PageFactory.initElements(driver, HomePage.class);
		commonSettings = PageFactory.initElements(driver, CommonSettingsPage.class);
		attPolicySettings = PageFactory.initElements(driver, AttendanceSettingsPolicyPage.class);
		rightMenuOption = PageFactory.initElements(driver, RightMenuOptionsPage.class);
	}

	@Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Attendance_Settings")
	public void Verify_Admin_is_able_to_create_policy(Map<String,String> data) throws Exception {
		
		Assert.assertTrue(loginpage.loginToApplication(), "User Loggin to Application as Admin");
		Assert.assertTrue(homepage.clickUserProfileIcon(), "Click User Profile Icon");
		Assert.assertTrue(rightMenuOption.clickSidebarSwitchToAdmin(), "Click on Switch to Admin");
		Assert.assertTrue(homepage.clickUserProfileIconAdmin(), "Click User Profile Icon");
		Assert.assertTrue(rightMenuOption.clickSidebarSettings(), "Click on Settings link");
		Assert.assertTrue(commonSettings.clickAttendance(), "Click on Attendance link");
		Assert.assertTrue(attPolicySettings.clickPoliciesLink(), "Click on Shifts link");
		Assert.assertTrue(attPolicySettings.clickCreatePolicyButton(), "Click on Create Policy button");
		Assert.assertTrue(attPolicySettings.insertPolicyName(data.get("Policy Name_TextBox")),"Insert Policy Name.");
		Assert.assertTrue(attPolicySettings.selectLeaveDeductionPolicy(
				data.get("Leave Deduction Policy_Dropdown")), "Select Leave deduction policy");
		Assert.assertTrue(
				attPolicySettings.enableOrDisableLateMarkPolicy(data.get("Late Mark Policy_Dropdown")),
				"Select Late Mark policy");
		Assert.assertTrue(attPolicySettings.enableOrDisableWorkDurationPolicy(
				data.get("Work Duration Policy_Dropdown")), "Select Work Duration policy");
		Assert.assertTrue(
				attPolicySettings.enableOrDisableEarlyMarkPolicy(data.get("Early Mark Policy_Dropdown")),
				"Select Early Mark Duration policy");
		Assert.assertTrue(attPolicySettings.clickSaveButton(), "Save button clicked successfully");
		Assert.assertTrue(attPolicySettings.searchPolicyName(), "Search for Policy Name created");
		Assert.assertTrue(attPolicySettings.checkPolicyDetails(), "Policy details created successfully");
		Assert.assertTrue(attPolicySettings.deletePolicy(), "Policy details deleted successfully");
		
		Assert.assertTrue(rightMenuOption.clickSidebarLogout(),"Logout from application successfully");
	}
}