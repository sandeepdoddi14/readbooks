package com.darwinbox.leaves.Accural.MultipleAllotment.MAWithCF.Fiancial;


import com.darwinbox.Services;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Accural.Credit_On_Accural_Basis;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.leaves.Services.LeaveBalanceAPI;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class MutlipleAllotmentWithCarryForward extends LeaveAccuralBase {

    Employee employee = new Employee();

    LoginPage loginpage = null;
    CommonAction commonAction = null;
    Boolean runTest = true;



    LocalDate doj=null;

    LeavesAction leavesAction = null;

    double tenureLeaves=0.0D;
    double firstYearBalance=0.0D;
    double totalLeaves=0.0D;

    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");
        leavesAction= new LeavesAction(driver);

    }

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyMultipleAllotmentBalance(Map<String, String> testData) {

        LeavePolicyObject multipleAllotmentLeavePolicy = getMultipleAllotmentWithCarryForwardLeavePolicy(testData);
        super.setLeavePolicyObject(multipleAllotmentLeavePolicy);

        Reporter("Leave Type is"+multipleAllotmentLeavePolicy.getLeave_Type(),"Info");

        //always start from previous year
        LocalDate firstLeaveCycleStartDate=LocalDate.parse("2019-04-01");
        LocalDate firstLeaveCyclceEndDate=LocalDate.parse("2020-03-31");

        leaveCycleStartDate = firstLeaveCycleStartDate;
        leaveCycleEndDate = firstLeaveCyclceEndDate;

        HashMap<String,String> empTypes=new Services().getEmployeeTypes();

        //to generate employee
        //it will create a full time employee
        changeServerDate(LocalDate.now().toString());
        try {
            employee = new EmployeeServices().generateAnFullTimeEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.toString(), "no");
        }
        catch (Exception e){
            try {
                employee = new EmployeeServices().generateAnFullTimeEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.toString(), "no");
            }
            catch (Exception e1){
                employee = new EmployeeServices().generateAnFullTimeEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.toString(), "no");

            }
        }

        Reporter("Employee DOJ is --> "+employee.getDoj(),"Info");
        super.setEmployee(employee);

        Boolean prorata_afterProbation=testData.get("Leave Probation Period according to Employee Probation Period").equalsIgnoreCase("yes")?true:false;



        //BEFORE FIRST TRANSFER
        changeServerDate(leaveCycleStartDate.plusMonths(5).minusDays(1).toString());
        double beforeFirstTransfer = calculateLeaveBalance(leaveCycleStartDate.toString(),serverChangedDate);
        double actualBeforeFirstTransfer= new LeaveBalanceAPI(employee.getEmployeeID(),multipleAllotmentLeavePolicy.getLeave_Type()).getBalance();

        Reporter("Before first Transfer Balance"+beforeFirstTransfer,"Info");
        Reporter("Actaul Before first Transfer Balance"+actualBeforeFirstTransfer,"Info");


        //CALCULATE DEACTIVATION BALANCE FOR 1ST TRANSFER
        deActiavation=true;
        //making default to begin of month for calculation
        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getIndicator()){
            Credit_On_Accural_Basis credit_on_accural_basis=multipleAllotmentLeavePolicy.getCredit_on_accural_basis();
            credit_on_accural_basis.setMonthlyAccuralSetting(true,true,false);
            credit_on_accural_basis.setQuarterlyAccural(false,false,false);
            credit_on_accural_basis.setBiAnnual(false);
            multipleAllotmentLeavePolicy.setCredit_on_accural_basis(credit_on_accural_basis);
        }
        super.setLeavePolicyObject(multipleAllotmentLeavePolicy);

        double firstDeactivagtionBalance=calculateLeaveBalance(leaveCycleStartDate.toString(),serverChangedDate);
        double firstDeactivagtionBalanceValue=5;
        deActiavation=false;


        if(testData.get("Accrual").equalsIgnoreCase("yes")?true:false){
            Credit_On_Accural_Basis credit_on_accural_basis= new Credit_On_Accural_Basis();
            credit_on_accural_basis.setIndicator(true);

            if(!testData.get("Monthly").equalsIgnoreCase("yes")?true:false)
                credit_on_accural_basis.setMonthlyAccuralSetting(false,false,false);
            else
                credit_on_accural_basis.setMonthlyAccuralSetting(true,testData.get("Begin of month/Quarter").equalsIgnoreCase("yes")?true:false,testData.get("End of month/Quarter").equalsIgnoreCase("yes")?true:false);

            if(!testData.get("Quarterly").equalsIgnoreCase("yes")?true:false)
                credit_on_accural_basis.setQuarterlyAccural(false,false,false);
            else
                credit_on_accural_basis.setQuarterlyAccural(true,testData.get("Begin of month/Quarter").equalsIgnoreCase("yes")?true:false,testData.get("End of month/Quarter").equalsIgnoreCase("yes")?true:false);

            credit_on_accural_basis.setBiAnnual(testData.get("Biannually").equalsIgnoreCase("yes")?true:false);

            multipleAllotmentLeavePolicy.setCredit_on_accural_basis(credit_on_accural_basis);
        }




        //EMPLOYEE WILL BE CHANGED FROM FULL TIME TO PART TIME AT THIS DATE
        changeServerDate(leaveCycleStartDate.plusMonths(5).plusDays(1).toString());

        //part time
        new EmployeeServices().addUserEmployment(employee.getMongoID(),"4",empTypes.entrySet().stream().filter(x->x.getKey().equalsIgnoreCase("part time")).findFirst().get().getValue(),leaveCycleStartDate.plusMonths(5).toString());
        multipleAllotmentLeavePolicy.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Alloted Leaves").split(",")[1]));

        leaveCycleStartDate=leaveCycleStartDate.plusMonths(5);
        leaveCycleEndDate=leaveCycleStartDate.plusYears(1).minusDays(1);

        super.setLeavePolicyObject(multipleAllotmentLeavePolicy);
        double afterFirstTransferDate = calculateLeaveBalance(leaveCycleStartDate.toString(),leaveCycleEndDate.toString());
        double afterFirstTransferDatetemp1 = 24.0;
        double actualAfterFirstTransfer= new LeaveBalanceAPI(employee.getEmployeeID(),multipleAllotmentLeavePolicy.getLeave_Type()).getBalance();


        if(!multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getIndicator()){

            afterFirstTransferDate=(afterFirstTransferDate/12.0)*7;

        }
        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBeginOfQuarter())
        {
            afterFirstTransferDate=(afterFirstTransferDate/3.0)*1;
        }

        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBeginOfMonth()){
            afterFirstTransferDate=(afterFirstTransferDate/12.0)*1;
        }

        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getEndOfMonth()){
            afterFirstTransferDate=(afterFirstTransferDate/12.0)*0.0;
        }

        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getEndOfQuarter())
        {
            afterFirstTransferDate=0.0D;
            firstDeactivagtionBalance=(firstDeactivagtionBalance/5.0)*3.0;
        }
        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBiAnnual()){
            afterFirstTransferDate = (24/12.0)*1.0;
        }


        double afterFirstTransferBalance=afterFirstTransferDate+firstDeactivagtionBalance;

        Reporter("after First Transfer Date Balance"+(afterFirstTransferBalance),"Info");
        Reporter("Actual After First Transfer Date Balance"+actualAfterFirstTransfer,"Info");


        changeServerDate(firstLeaveCycleStartDate.plusMonths(10).minusDays(1).toString());

        //set for probation = true
        // if(true)
        //   afterFirstTransferDatetemp1 = calculateLeaveBalance(leaveCycleStartDate.toString(),leaveCycleEndDate.toString());
        if(!multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getIndicator()){

            afterFirstTransferDatetemp1=(afterFirstTransferDatetemp1/12.0)*7;

        }
        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBeginOfQuarter())
        {
            afterFirstTransferDatetemp1=(afterFirstTransferDatetemp1/12.0)*7;
        }
        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getEndOfQuarter())
        {
            afterFirstTransferDatetemp1=(afterFirstTransferDatetemp1/12.0)*4;
        }

        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBeginOfMonth()){
            afterFirstTransferDatetemp1=(afterFirstTransferDatetemp1/12.0)*5.0;
        }


        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getEndOfMonth()){
            afterFirstTransferDatetemp1=(afterFirstTransferDatetemp1/11.0)*4.0;
        }
        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBiAnnual()){
            afterFirstTransferDatetemp1 = (24.0/12.0)*7.0;
        }

        double actualBeforeSecondTrnsfer= new LeaveBalanceAPI(employee.getEmployeeID(),multipleAllotmentLeavePolicy.getLeave_Type()).getBalance();

        //   Reporter("before seccond transfer balnce"+(firstDeactivagtionBalanceValue+afterFirstTransferDatetemp1),"Info");

        // Reporter("actual before seccond transfer balnce"+(actualBeforeSecondTrnsfer),"Info");



        //employee changes to contract from this date
        changeServerDate(leaveCycleStartDate.plusMonths(5).toString());


        //contract
        new EmployeeServices().addUserEmployment(employee.getMongoID(),"4",empTypes.entrySet().stream().filter(x->x.getKey().equalsIgnoreCase("contract")).findFirst().get().getValue(),leaveCycleStartDate.plusMonths(5).toString());


        multipleAllotmentLeavePolicy.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Alloted Leaves").split(",")[2]));

        leaveCycleStartDate=leaveCycleStartDate.plusMonths(5);
        leaveCycleEndDate=leaveCycleStartDate.plusYears(1).minusDays(1);


        super.setLeavePolicyObject(multipleAllotmentLeavePolicy);
        double afterSecondTransferDate = calculateLeaveBalance(leaveCycleStartDate.toString(),leaveCycleEndDate.toString());
        double afterSecondTransferDatetemp1 = 36.0;
        double actualAfterSecondTransfer= new LeaveBalanceAPI(employee.getEmployeeID(),multipleAllotmentLeavePolicy.getLeave_Type()).getBalance();


        if(!multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getIndicator()){

            afterSecondTransferDate=(afterSecondTransferDate/12.0)*2;

        }
        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBeginOfQuarter())
        {
            afterSecondTransferDate=((afterSecondTransferDate/3.0))/2;
        }

        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBeginOfMonth()){
            afterSecondTransferDate=(afterSecondTransferDate/12.0)*1;
        }

        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getEndOfMonth()){
            afterSecondTransferDate=(afterSecondTransferDate/12.0)*0.0;
        }

        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getEndOfQuarter())
        {
            afterSecondTransferDate=0.0D;
            //firstDeactivagtionBalance=(firstDeactivagtionBalance/5.0)*3.0;
        }
        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBiAnnual()){
            afterSecondTransferDate = (36.0/12.0)*2.0;
        }


        double afterSecondTransferBalance=afterSecondTransferDate+15.0D;

        Reporter("after Second Transfer Date Balance"+(afterSecondTransferBalance),"Info");
        Reporter("Actual Second Transfer Date Balance"+actualAfterSecondTransfer,"Info");


        double expectedBalanceForCarryForward = 6.0D;
       // double expectedBalanceForCarryForward = calculateLeaveBalance(leaveCycleStartDate.toString(),leaveCycleEndDate.toString());

       /* if(!multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getIndicator()){

            expectedBalanceForCarryForward=(expectedBalanceForCarryForward/12.0)*2;

        }
        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBeginOfQuarter())
        {
            expectedBalanceForCarryForward=((expectedBalanceForCarryForward/3.0))/2;
        }

        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBeginOfMonth()){
            expectedBalanceForCarryForward=(expectedBalanceForCarryForward/12.0)*2;
        }

        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getEndOfMonth()){
            expectedBalanceForCarryForward=(expectedBalanceForCarryForward/12.0)*2;
        }

        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getEndOfQuarter())
        {
            expectedBalanceForCarryForward=0.0D;
            //firstDeactivagtionBalance=(firstDeactivagtionBalance/5.0)*3.0;
        }
        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBiAnnual()){
            expectedBalanceForCarryForward = (36.0/12.0)*2.0;
        }
*/

        expectedBalanceForCarryForward=expectedBalanceForCarryForward+15.0D;


        if (multipleAllotmentLeavePolicy.getCarryForwardUnusedLeave().indicator) {
            if (multipleAllotmentLeavePolicy.getCarryForwardUnusedLeave().carryForwardAllUnusedLeave) {
                expectedBalanceForCarryForward = expectedBalanceForCarryForward;
            } else if (multipleAllotmentLeavePolicy.getCarryForwardUnusedLeave().fixed) {
                double fixedValue = Double.valueOf(multipleAllotmentLeavePolicy.getCarryForwardUnusedLeave().fixedValue);
                if (fixedValue > expectedBalanceForCarryForward) {
                    expectedBalanceForCarryForward = expectedBalanceForCarryForward;
                } else if (fixedValue <= expectedBalanceForCarryForward) {
                    expectedBalanceForCarryForward = fixedValue;
                }
            } else if (multipleAllotmentLeavePolicy.getCarryForwardUnusedLeave().percentage) {
                double percentageValue = Double.valueOf(multipleAllotmentLeavePolicy.getCarryForwardUnusedLeave().percentageValue);
                expectedBalanceForCarryForward = ((expectedBalanceForCarryForward * percentageValue) / 100);

            }
        }


        changeServerDate(leaveCycleStartDate.plusMonths(2).plusDays(1).toString());


        leavesAction.setEmployeeID(employee.getEmployeeID());

       /* leavesAction.navigateToSettings_Leaves();
        leavesAction.Leave_Type=multipleAllotmentLeavePolicy.getLeave_Type();

        leavesAction.editLeaveType();
        try {
            Thread.sleep(5000);
        }
        catch (Exception e){
            Reporter("Error in saving policy","error");
        }
        //new CreateAndManageLeavePoliciesPage(driver).clickCreateLeavePolicySaveButton();
        driver.findElement(By.xpath("//input[@class = 'btn btn-primary btn-sm text-uppercase company_leave_update_btn']")).click();

        try {
            Thread.sleep(2000);
        }
        catch (Exception e){
            Reporter("Error in saving policy","error");
        }
       */
        //leavesAction.removeEmployeeLeaveLogs();
        leavesAction.runCarryFrowardCronByEndPointURL();

        //expectedBalanceForCarryForward = getCarryFowardBalance(expectedBalanceForCarryForward);
        double actualBalanceForCarryForward= new LeaveBalanceAPI(employee.getEmployeeID(),multipleAllotmentLeavePolicy.getLeave_Type()).getCarryForwardBalance();


        Reporter("Expected carry foprward Balance is "+ expectedBalanceForCarryForward ,"Info");


        Reporter("Actual carry foprward Balance is "+ actualBalanceForCarryForward ,"Info");

        Assert.assertTrue(expectedBalanceForCarryForward==actualBalanceForCarryForward,"Carry Foraward Balances are not same");

    }

    public void changeServerDate(String date){
        new DateTimeHelper().changeServerDate(driver,date);
        serverDateInFormat=LocalDate.parse(date);
        serverChangedDate=serverDateInFormat.toString();


    }

}

