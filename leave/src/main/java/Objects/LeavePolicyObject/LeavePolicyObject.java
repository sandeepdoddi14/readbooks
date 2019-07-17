package Objects.LeavePolicyObject;

import Objects.Employee;
import Objects.LeavePolicyObject.Accural.Credit_On_Accural_Basis;
import Objects.LeavePolicyObject.Accural.Credit_On_Pro_Rata_Basis;
import Objects.LeavePolicyObject.Fields.*;
import Service.*;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.leaves.Utils.LeaveBase;
import com.darwinbox.leaves.Utils.MapUtils;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeavePolicyObject extends LeaveBase {

    /*---------Policy Fields Ends-------*/
    public String groupCompanyMongoId;
    LeaveService leaveService;
    EmployeeServices employeeServices;
    LeaveAdmin leaveAdmin;
    LeavesPage leavesPage;
    DateTimeHelper dateTimeHelper;
    MapUtils mapUtils;
    Employee employee = null;
    /*
   default values are set
   except mandatory fields
     */
    private String assignment_Type = null;
    private String assignmemnt_Framework = null;
    private String group_Company = null;
    private String leave_Type = null;
    private String description;

    public String getCustomLeaveCycleMonth() {
        return customLeaveCycleMonth;
    }

    public void setCustomLeaveCycleMonth(String customLeaveCycleMonth) {
        this.customLeaveCycleMonth = customLeaveCycleMonth;
    }

    private String customLeaveCycleMonth=null;
    private int maximum_leave_allowed_per_year;
    private RestrictCondition restriction_Condition = new RestrictCondition();
    private int consecutive_leave_allowed;
    private String leave_cycle;
    private String push_all_these_leave_requests_to_admin;
    private String restrict_to_Week_Days = null;
    private String auto_ApproveLeave_Request;
    private MaximumLeaveApplicationSettings maximum_Leave_application_settings = new MaximumLeaveApplicationSettings();
    private String attachment_mandatory;
    private String gender_applicability;
    private String minimum_Notice_Period;
    private ProbationPeriodForLeaveValidity probation_period_before_leave_validity = new ProbationPeriodForLeaveValidity();
    private int maximum_leave_allowed_per_month = 0;
    private int maximum_Number_of_Leave_which_can_be_accrued = 0;
    private int minimum_consecutive_days_allowed_in_a_single_application = 0;
    private int maximum_consecutive_days_allowed_in_a_single_application = 0;
    private boolean dontShowAndApplyInNoticePeriod = false;
    private Boolean allow_half_day = false;
    private PrefixSuffixSetting prefixSuffixSetting = new PrefixSuffixSetting();
    private CountInterveningHolidaysWeeklyOff Count_intervening_holidays_weeklys_offs_as_leave = new CountInterveningHolidaysWeeklyOff();
    private PastDatedLeave pastDatedLeave = new PastDatedLeave();
    private Clubbing Clubbing = new Clubbing();
    private OverUtilization OverUtilization = new OverUtilization();

    private ApprovalFlow approvalFlow=null;


    /*
    Leave Accural Objects
     */
    private Credit_On_Pro_Rata_Basis credit_on_pro_rata_basis=new Credit_On_Pro_Rata_Basis();
    private Credit_On_Accural_Basis credit_on_accural_basis=new Credit_On_Accural_Basis();





    public LeavePolicyObject() {

        leaveService = new LeaveService();
        employeeServices = new EmployeeServices();
        leaveAdmin = new LeaveAdmin();
        leavesPage = new LeavesPage(driver);
        dateTimeHelper = new DateTimeHelper();
        mapUtils = new MapUtils();
    }
    //clubbing scenario
    public LeavePolicyObject(String leaveType) {
        this.leave_Type = leaveType;
        this.group_Company = "Working Days (DO NOT TOUCH)";
        this.assignment_Type = "0";
        this.description = "auto test";
        this.maximum_leave_allowed_per_year = 12;

        if (leaveType == "unpaid" || leaveType == "compoff") {
            this.getClubbing().indicator = true;
            this.getClubbing().clubWithAnyLeave = true;
        }

        groupCompanyMongoId = new Service().getGroupCompanyIds().get(group_Company);
    }

    public PastDatedLeave getPastDatedLeave() {
        return pastDatedLeave;
    }

    public void setPastDatedLeave(PastDatedLeave pastDatedLeave) {
        this.pastDatedLeave = pastDatedLeave;
    }

    public PrefixSuffixSetting getPrefixSuffixSetting() {
        return prefixSuffixSetting;
    }

    public void setPrefixSuffixSetting(PrefixSuffixSetting prefixSuffixSetting) {
        this.prefixSuffixSetting = prefixSuffixSetting;
    }

    public Credit_On_Pro_Rata_Basis getCredit_on_pro_rata_basis() {
        return credit_on_pro_rata_basis;
    }

    public void setCredit_on_pro_rata_basis(Credit_On_Pro_Rata_Basis credit_on_pro_rata_basis) {
        this.credit_on_pro_rata_basis = credit_on_pro_rata_basis;
    }

    public Credit_On_Accural_Basis getCredit_on_accural_basis() {
        return credit_on_accural_basis;
    }

    public void setCredit_on_accural_basis(Credit_On_Accural_Basis credit_on_accural_basis) {
        this.credit_on_accural_basis = credit_on_accural_basis;
    }


    public boolean isDontShowAndApplyInNoticePeriod() {
        return dontShowAndApplyInNoticePeriod;
    }

    public void setDontShowAndApplyInNoticePeriod(boolean dontShowAndApplyInNoticePeriod) {
        this.dontShowAndApplyInNoticePeriod = dontShowAndApplyInNoticePeriod;
    }

    public String getAssignment_Type() {
        return assignment_Type;
    }

    public void setAssignment_Type(String assignment_Type) {
        this.assignment_Type = assignment_Type;
    }

    public String getAssignmemnt_Framework() {
        return assignmemnt_Framework;
    }

    public void setAssignmemnt_Framework(String assignmemnt_Framework) {
        this.assignmemnt_Framework = assignmemnt_Framework;
    }

    public ApprovalFlow getApprovalFlow() {
        return approvalFlow;
    }

    public void setApprovalFlow(ApprovalFlow approvalFlow) {
        this.approvalFlow = approvalFlow;
    }

    public String getGroup_Company() {
        return group_Company;
    }

    public void setGroup_Company(String group_Company) {
        this.group_Company = group_Company;
        groupCompanyMongoId = new Service().getGroupCompanyIds().get(this.group_Company);

    }

    public String getLeave_Type() {
        return leave_Type;
    }

    public void setLeave_Type(String leave_Type) {
        this.leave_Type = leave_Type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaximum_leave_allowed_per_year() {
        return maximum_leave_allowed_per_year;
    }

    public void setMaximum_leave_allowed_per_year(int maximum_leave_allowed_per_year) {
        this.maximum_leave_allowed_per_year = maximum_leave_allowed_per_year;
    }

    public RestrictCondition getRestriction_Condition() {
        return restriction_Condition;
    }

    public void setRestriction_Condition(RestrictCondition restriction_Condition) {
        this.restriction_Condition = restriction_Condition;
    }

    public int getConsecutive_leave_allowed() {
        return consecutive_leave_allowed;
    }

    public void setConsecutive_leave_allowed(int consecutive_leave_allowed) {
        this.consecutive_leave_allowed = consecutive_leave_allowed;
    }

    public String getLeave_cycle() {
        return leave_cycle;
    }

    public void setLeave_cycle(String leave_cycle) {
        this.leave_cycle = leave_cycle;
    }

    public String getPush_all_these_leave_requests_to_admin() {
        return push_all_these_leave_requests_to_admin;
    }

    public void setPush_all_these_leave_requests_to_admin(String push_all_these_leave_requests_to_admin) {
        this.push_all_these_leave_requests_to_admin = push_all_these_leave_requests_to_admin;
    }

    public String getRestrict_to_Week_Days() {
        return restrict_to_Week_Days;
    }

    public void setRestrict_to_Week_Days(String restrict_to_Week_Days) {
        this.restrict_to_Week_Days = restrict_to_Week_Days;
    }

    public String getAuto_ApproveLeave_Request() {
        return auto_ApproveLeave_Request;
    }

    public void setAuto_ApproveLeave_Request(String auto_ApproveLeave_Request) {
        this.auto_ApproveLeave_Request = auto_ApproveLeave_Request;
    }

    public MaximumLeaveApplicationSettings getMaximum_Leave_application_settings() {
        return maximum_Leave_application_settings;
    }

    public void setMaximum_Leave_application_settings(MaximumLeaveApplicationSettings maximum_Leave_application_settings) {
        this.maximum_Leave_application_settings = maximum_Leave_application_settings;
    }

    public String getAttachment_mandatory() {
        return attachment_mandatory;
    }

    public void setAttachment_mandatory(String attachment_mandatory) {
        this.attachment_mandatory = attachment_mandatory;
    }

    public String getGender_applicability() {
        return gender_applicability;
    }

    public void setGender_applicability(String gender_applicability) {
        this.gender_applicability = gender_applicability;
    }

    public String getMinimum_Notice_Period() {
        return minimum_Notice_Period;
    }

    public void setMinimum_Notice_Period(String minimum_Notice_Period) {
        this.minimum_Notice_Period = minimum_Notice_Period;
    }

    public ProbationPeriodForLeaveValidity getProbation_period_before_leave_validity() {
        return probation_period_before_leave_validity;
    }

    public void setProbation_period_before_leave_validity(ProbationPeriodForLeaveValidity probation_period_before_leave_validity) {
        this.probation_period_before_leave_validity = probation_period_before_leave_validity;
    }

    public int getMaximum_leave_allowed_per_month() {
        return maximum_leave_allowed_per_month;
    }

    public void setMaximum_leave_allowed_per_month(int maximum_leave_allowed_per_month) {
        this.maximum_leave_allowed_per_month = maximum_leave_allowed_per_month;
    }

    public int getMaximum_Number_of_Leave_which_can_be_accrued() {
        return maximum_Number_of_Leave_which_can_be_accrued;
    }

    public void setMaximum_Number_of_Leave_which_can_be_accrued(int maximum_Number_of_Leave_which_can_be_accrued) {
        this.maximum_Number_of_Leave_which_can_be_accrued = maximum_Number_of_Leave_which_can_be_accrued;
    }

    public int getMinimum_consecutive_days_allowed_in_a_single_application() {
        return minimum_consecutive_days_allowed_in_a_single_application;
    }

    public void setMinimum_consecutive_days_allowed_in_a_single_application(int minimum_consecutive_days_allowed_in_a_single_application) {
        this.minimum_consecutive_days_allowed_in_a_single_application = minimum_consecutive_days_allowed_in_a_single_application;
    }

    public int getMaximum_consecutive_days_allowed_in_a_single_application() {
        return maximum_consecutive_days_allowed_in_a_single_application;
    }

    public void setMaximum_consecutive_days_allowed_in_a_single_application(int maximum_consecutive_days_allowed_in_a_single_application) {
        this.maximum_consecutive_days_allowed_in_a_single_application = maximum_consecutive_days_allowed_in_a_single_application;
    }

    public Boolean getAllow_half_day() {
        return allow_half_day;
    }

    public void setAllow_half_day(Boolean allow_half_day) {
        this.allow_half_day = allow_half_day;
    }

    public CountInterveningHolidaysWeeklyOff getCount_intervening_holidays_weeklys_offs_as_leave() {
        return Count_intervening_holidays_weeklys_offs_as_leave;
    }

    public void setCount_intervening_holidays_weeklys_offs_as_leave(CountInterveningHolidaysWeeklyOff count_intervening_holidays_weeklys_offs_as_leave) {
        Count_intervening_holidays_weeklys_offs_as_leave = count_intervening_holidays_weeklys_offs_as_leave;
    }

    public Objects.LeavePolicyObject.Fields.Clubbing getClubbing() {
        return Clubbing;
    }

    public void setClubbing(Objects.LeavePolicyObject.Fields.Clubbing clubbing) {
        Clubbing = clubbing;
    }

    public Objects.LeavePolicyObject.Fields.OverUtilization getOverUtilization() {
        return OverUtilization;
    }

    public void setOverUtilization(Objects.LeavePolicyObject.Fields.OverUtilization overUtilization) {
        OverUtilization = overUtilization;
    }

    public void setFields(Map<String, String> data) {
        this.setAssignment_Type(data.get("Assignment_Type"));
        this.setAssignmemnt_Framework(data.get("Assignmemnt_Framework"));
        this.setGroup_Company(data.get("Group_Company"));
        this.setLeave_Type(data.get("Leave_Type"));
        this.setDescription(data.get("Description"));
        this.setMaximum_leave_allowed_per_year(Integer.parseInt(data.get("Maximum_leave_allowed_per_year")));
        this.restriction_Condition.AND_OR = data.get("Restriction_Condition");
        this.restriction_Condition.Restriction = data.get("Restriction_(Department,_Employee_Type_or_Location)");
        this.consecutive_leave_allowed = data.get("Consecutive_leave_allowed").equalsIgnoreCase("") ? 0 : Integer.parseInt(data.get("Consecutive_leave_allowed"));
        this.leave_cycle = data.get("Leave_cycle");
        this.push_all_these_leave_requests_to_admin = data.get("Push_all_these_leave_requests_to_admin");

        this.restrict_to_Week_Days = data.get("Restrict_to_Week_Days").equalsIgnoreCase("") ? null : data.get("Restrict_to_Week_Days");
        this.auto_ApproveLeave_Request = data.get("Auto_ApproveLeave_Request_(days)");
        this.maximum_Leave_application_settings.indicator = data.get("Maximum_Leave_application_settings").equalsIgnoreCase("") ? Boolean.FALSE : Boolean.parseBoolean(data.get("Maximum_Leave_application_settings"));
        if (this.maximum_Leave_application_settings.indicator) {
            this.maximum_Leave_application_settings.maximum_leave_applications_allowed_per_month = data.get("Maximum_leave_applications_allowed_per_month").equalsIgnoreCase("") ? 1 : Integer.parseInt(data.get("Maximum_leave_applications_allowed_per_month"));
            this.maximum_Leave_application_settings.maximum_leave_applications_allowed_per_year = data.get("Maximum_leave_applications_allowed_per_year").equalsIgnoreCase("") ? 0 : Integer.parseInt(data.get("Maximum_leave_applications_allowed_per_year"));
            this.maximum_Leave_application_settings.maximum_leave_applications_allowed_in_tenure = data.get("Maximum_leave_applications_allowed_in_tenure").equalsIgnoreCase("") ? 0 : Integer.parseInt(data.get("Maximum_leave_applications_allowed_in_tenure"));
        }
        this.attachment_mandatory = data.get("Attachment_mandatory");
        this.minimum_Notice_Period = data.get("Minimum_Notice_Period_(days)");
        this.gender_applicability = data.get("Gender_applicability");
        if (data.get("Probation_period_before_leave_validity").equalsIgnoreCase("probation")) {
            this.probation_period_before_leave_validity.probation = true;
            this.probation_period_before_leave_validity.custom = false;
        } else {
            this.probation_period_before_leave_validity.custom = true;
            this.probation_period_before_leave_validity.customMonths = data.get("Probation_period_before_leave_validity_months").equalsIgnoreCase("") ? 0 : Integer.parseInt(data.get("Probation_period_before_leave_validity_months"));
        }

        this.maximum_leave_allowed_per_month = data.get("Maximum_leave_allowed_per_month").equalsIgnoreCase("") ? 0 : Integer.parseInt(data.get("Maximum_leave_allowed_per_month"));
        this.maximum_Number_of_Leave_which_can_be_accrued = data.get("Maximum_Number_of_Leave_which_can_be_accrued").equalsIgnoreCase("") ? 0 : Integer.parseInt(data.get("Maximum_Number_of_Leave_which_can_be_accrued"));
        this.minimum_consecutive_days_allowed_in_a_single_application = data.get("Minimum_consecutive_days_allowed_in_a_single_application").equalsIgnoreCase("") ? 0 : Integer.parseInt(data.get("Minimum_consecutive_days_allowed_in_a_single_application"));
        this.maximum_consecutive_days_allowed_in_a_single_application = data.get("Maximum_consecutive_days_allowed_in_a_single_application").equalsIgnoreCase("") ? 0 : Integer.parseInt(data.get("Maximum_consecutive_days_allowed_in_a_single_application"));

        this.setDontShowAndApplyInNoticePeriod(!data.get("Don’t_Show_And_Apply_In_Notice_Period").equalsIgnoreCase("") && Boolean.parseBoolean(data.get("Don’t_Show_And_Apply_In_Notice_Period")));
        this.allow_half_day = Boolean.parseBoolean(data.get("Allow_half-day"));

        this.Count_intervening_holidays_weeklys_offs_as_leave.indicator = Boolean.parseBoolean(data.get("Count_intervening_holidays/weeklys_offs_as_leave"));
        if (this.Count_intervening_holidays_weeklys_offs_as_leave.indicator) {
            this.Count_intervening_holidays_weeklys_offs_as_leave.CountWeeklyOffs = Boolean.parseBoolean(data.get("Count_Weeklyoffs"));
            this.Count_intervening_holidays_weeklys_offs_as_leave.CountHolidays = Boolean.parseBoolean(data.get("Count_Holidays"));
        }

        this.pastDatedLeave.indicator=Boolean.parseBoolean(data.get("AllowPastDated"));
        if(this.pastDatedLeave.indicator){
            if(!data.get("MaxNoOfDaysPostLeave").equalsIgnoreCase("")){
                this.pastDatedLeave.maxNumOfDaysPostLeave=Integer.parseInt(data.get("MaxNoOfDaysPostLeave"));
            }
        }

        this.Clubbing.indicator = Boolean.parseBoolean(data.get("Clubbing_Allowed"));
        if (this.Clubbing.indicator) {
            this.Clubbing.clubWithAnyLeave = Boolean.parseBoolean(data.get("Allow_clubbing_with_any_available_leave"));
            this.Clubbing.clubWithFollowingLeave = Boolean.parseBoolean(data.get("Allow_clubbing_only_with_following_leave"));
            this.Clubbing.leaveList = data.get("Leave_List");
        }

        this.OverUtilization.indicator = Boolean.parseBoolean(data.get("OverUtilization"));
        if (this.OverUtilization.indicator) {
            this.OverUtilization.countExcessAsPaid = Boolean.parseBoolean(data.get("Count_excess_leave_as_Paid,_by_default"));
            this.OverUtilization.countExcessAsUnPaid = Boolean.parseBoolean(data.get("Count_excess_leave_as_Unpaid,_by_default"));
            this.OverUtilization.utilizeFrom = Boolean.parseBoolean(data.get("Utilize_from"));
            this.OverUtilization.dontAllowMoreThanYearlyAllocation = Boolean.parseBoolean(data.get("Dont_Allow_leave_more_than_Yearly_allocation"));
            this.OverUtilization.dontAllowMoreThanYearlyAccural = Boolean.parseBoolean(data.get("Dont_Allow_leave_more_than_Yearly_accrual"));
            this.OverUtilization.fixedOverUtilization = data.get("fixed_overutilization").equalsIgnoreCase("") ? 0 : Integer.parseInt(data.get("fixed_overutilization"));
            this.OverUtilization.utlizeFromDropDown = data.get("LeavePolicy_OverUtilization").equalsIgnoreCase("") ? null : data.get("LeavePolicy_OverUtilization");
        }

    }

    public List<NameValuePair> createRequest() {
        //  Map<String, String> requestBody = new HashMap<>();
        List<NameValuePair> formData = new LeaveService().mapToFormData(new LeaveService().getDefaultforLeaveDeduction());

        //set assignment type
        if (this.getAssignment_Type().equalsIgnoreCase("company wise")) {
            formData.removeIf(x -> x.getName().contains("Leaves[assignment_type]"));
            formData.add(new BasicNameValuePair("Leaves[assignment_type]", "0"));
            Reporter("Assignment Type is set to Company Wise", "Info");
        }

        if ((this.getAssignment_Type().equalsIgnoreCase("assignment framework"))) {
            formData.removeIf(x -> x.getName().contains("Leaves[assignment_type]"));
            formData.add(new BasicNameValuePair("Leaves[assignment_type]", "1"));
            Reporter("Assignment Type is set to Assignment framework", "Info");
        }

        //set group company
        if (this.getGroup_Company() != null) {
            formData.removeIf(x -> x.getName().contains("Leaves[parent_company_id]"));
            formData.add(new BasicNameValuePair("Leaves[parent_company_id]",
                    this.groupCompanyMongoId));
            Reporter("Group Company is set to  " + this.group_Company, "Info");
        }

        //set leave type
        if (this.getLeave_Type() != null) {
            formData.removeIf(x -> x.getName().contains("Leaves[name]"));
            formData.add(new BasicNameValuePair("Leaves[name]", this.leave_Type));
            Reporter("Leave Type is   " + this.leave_Type, "Info");
        }

        //set description
        if (this.getGroup_Company() != null) {
            formData.removeIf(x -> x.getName().contains("Leaves[description]"));
            formData.add(new BasicNameValuePair("Leaves[description]", this.description));
            Reporter("Description is   " + this.description, "Info");
        }

        //Maximum Leave Allowed Per Year
        if (this.maximum_leave_allowed_per_year != 0) {
            formData.removeIf(x -> x.getName().contains("Leaves[yearly_endowment]"));
            formData.add(new BasicNameValuePair("Leaves[yearly_endowment]", this.maximum_leave_allowed_per_year + ""));
            Reporter("Maximum Leave Allowed Per Year  is     " + this.maximum_leave_allowed_per_year, "Info");
        }

        if (this.restriction_Condition != null) {

            if (this.restriction_Condition.AND_OR != null) {
                formData.removeIf(x -> x.getName().contains("Leaves[or_and]"));

                //if (this.restriction_Condition.AND_OR.equalsIgnoreCase("OR"))
                formData.add(new BasicNameValuePair("Leaves[or_and]", this.restriction_Condition.AND_OR.equalsIgnoreCase("AND") ? 0 + "" : 1 + ""));
/*
                if (this.restriction_Condition.AND_OR.equalsIgnoreCase("AND")
                formData.add(new BasicNameValuePair("Leaves[or_and]", "0"));*/

            }
            if (this.restriction_Condition.Restriction != null) {
                formData.removeIf(x -> x.getName().contains("Leaves[restrictOther][]"));

                for (String restriction : this.restriction_Condition.Restriction.split(",")) {
                    formData.add(new BasicNameValuePair("Leaves[restrictOther][]", restriction));
                }
            }
        }

        if (this.consecutive_leave_allowed != 0) {
            formData.removeIf(x -> x.getName().contains("Leaves[p3_max_consecutive_days_limit]"));
            formData.add(new BasicNameValuePair("Leaves[p3_max_consecutive_days_limit]", this.consecutive_leave_allowed + ""));
        }

        if (this.restrict_to_Week_Days != null) {
            formData.removeIf(x -> x.getName().contains("Leaves[restrictweek][]"));
            for (String day : this.restrict_to_Week_Days.split(",")) {
                formData.add(new BasicNameValuePair("Leaves[restrictweek][]", getIntegerValueOfWeekDay(day) + ""));
            }
        }

        if (this.maximum_Leave_application_settings.indicator == true) {
            formData.removeIf(x -> x.getName().contains("Leaves[maximum_leave_application]"));
            formData.removeIf(x -> x.getName().contains("Leaves[max_leave_application_month]"));
            formData.removeIf(x -> x.getName().contains("Leaves[max_leave_application_year]"));
            formData.removeIf(x -> x.getName().contains("Leaves[max_leave_application_tenure]"));

            formData.add(new BasicNameValuePair("Leaves[maximum_leave_application]", "1"));

            formData.add(new BasicNameValuePair("Leaves[max_leave_application_month]",
                    this.maximum_Leave_application_settings.maximum_leave_applications_allowed_per_month + ""));
            formData.add(new BasicNameValuePair("Leaves[max_leave_application_year]",
                    this.maximum_Leave_application_settings.maximum_leave_applications_allowed_in_tenure + ""));
            formData.add(new BasicNameValuePair("Leaves[max_leave_application_tenure]",
                    this.maximum_Leave_application_settings.maximum_leave_applications_allowed_per_year + ""));
        }

        if (this.gender_applicability != null) {
            formData.removeIf(x -> x.getName().contains("Leaves[restrictGender]"));

            formData.add(new BasicNameValuePair("Leaves[restrictGender]",
                    new EmployeeServices().getGenders().get(this.gender_applicability)));
        }


        if (this.probation_period_before_leave_validity.probation) {
            formData.removeIf(x -> x.getName().contains("Leaves[p1_waiting_after_doj_status]"));
            formData.add(new BasicNameValuePair("Leaves[p1_waiting_after_doj_status]", "0"));
        } else {
            formData.removeIf(x -> x.getName().contains("Leaves[p1_waiting_after_doj_status]"));
            formData.removeIf(x -> x.getName().contains("Leaves[p1_waiting_after_doj]"));
            formData.add(new BasicNameValuePair("Leaves[p1_waiting_after_doj_status]",
                    "1"));
            formData.add(new BasicNameValuePair("Leaves[p1_waiting_after_doj]",
                    this.probation_period_before_leave_validity.customMonths + ""));
        }

        if (this.maximum_leave_allowed_per_month != 0) {
            formData.removeIf(x -> x.getName().contains("Leaves[p2_max_per_month]"));
            formData.add(new BasicNameValuePair("Leaves[p2_max_per_month]", this.maximum_leave_allowed_per_month + ""));
        }

        if (this.maximum_Number_of_Leave_which_can_be_accrued != 0) {
            formData.removeIf(x -> x.getName().contains("Leaves[max_number_of_leaves_accrued]"));
            formData.add(new BasicNameValuePair("Leaves[max_number_of_leaves_accrued]", this.maximum_Number_of_Leave_which_can_be_accrued + ""));
        }
        if (this.minimum_consecutive_days_allowed_in_a_single_application != 0) {
            formData.removeIf(x -> x.getName().contains("Leaves[min_consecutive_days_limit]"));
            formData.add(new BasicNameValuePair("Leaves[min_consecutive_days_limit]", this.minimum_consecutive_days_allowed_in_a_single_application + ""));
        }
        if (this.maximum_consecutive_days_allowed_in_a_single_application != 0) {
            formData.removeIf(x -> x.getName().contains("Leaves[max_consecutive_days_limit]"));
            formData.add(new BasicNameValuePair("Leaves[max_consecutive_days_limit]", this.maximum_consecutive_days_allowed_in_a_single_application + ""));
        }

        if (this.dontShowAndApplyInNoticePeriod) {
            formData.removeIf(x -> x.getName().contains("Leaves[allow_in_notice_period]"));
            formData.add(new BasicNameValuePair("Leaves[allow_in_notice_period]", "1"));
        } else if (!this.dontShowAndApplyInNoticePeriod) {
            formData.removeIf(x -> x.getName().contains("Leaves[allow_in_notice_period]"));
            formData.add(new BasicNameValuePair("Leaves[allow_in_notice_period]", "0"));
        }

        if (this.allow_half_day) {
            formData.removeIf(x -> x.getName().contains("LeavePolicy_HalfDays[status]"));
            formData.add(new BasicNameValuePair("LeavePolicy_HalfDays[status]", this.allow_half_day ? 1 + "" : 0 + ""));

        }
        if (this.getCount_intervening_holidays_weeklys_offs_as_leave().indicator) {
            formData.removeIf(x -> x.getName().contains("LeavePolicy_InterveningHolidays[status]"));
            formData.removeIf(x -> x.getName().contains("LeavePolicy_InterveningHolidays[count_intervening_holidays][count_weekly_off]"));
            formData.removeIf(x -> x.getName().contains("LeavePolicy_InterveningHolidays[count_intervening_holidays][count_public_holiday]"));

            Map<String, String> countInterveningRequest = getRequestForCountIntervening(this.getCount_intervening_holidays_weeklys_offs_as_leave().indicator, this.getCount_intervening_holidays_weeklys_offs_as_leave().CountWeeklyOffs, this.getCount_intervening_holidays_weeklys_offs_as_leave().CountHolidays);
            formData.addAll(new LeaveService().mapToFormData(countInterveningRequest));
        }

        if(this.getPastDatedLeave().indicator){
            formData.removeIf(x -> x.getName().contains("LeavePolicy_PreviousDates[status]"));
            formData.add(new BasicNameValuePair("LeavePolicy_PreviousDates[status]", "1"));

            formData.removeIf(x -> x.getName().contains("LeavePolicy_PreviousDates[no_of_days]"));
            formData.add(new BasicNameValuePair("LeavePolicy_PreviousDates[no_of_days]",this.getPastDatedLeave().maxNumOfDaysPostLeave+""));

        }

        if (this.getClubbing().indicator) {
            formData.add(new BasicNameValuePair("LeavePolicy_ClubbingOthers[status]", "1"));
            if (this.getClubbing().clubWithAnyLeave)
                formData.add(new BasicNameValuePair("LeavePolicy_ClubbingOthers[clubbing_type]", "0"));
            if (this.getClubbing().clubWithFollowingLeave) {
                formData.add(new BasicNameValuePair("LeavePolicy_ClubbingOthers[clubbing_type]", "1"));
                for (String leaveType : this.getClubbing().leaveList.split(",")) {
                    formData.add(new BasicNameValuePair("LeavePolicy_ClubbingOthers[clubbing_leaves_list][]", leaveType));
                }
            }
        } else if (!this.Clubbing.indicator) {
            formData.removeIf(x -> x.getName().contains("LeavePolicy_ClubbingOthers[status]"));
            formData.add(new BasicNameValuePair("LeavePolicy_ClubbingOthers[status]", "0"));

        }


        if (this.getOverUtilization().indicator) {
            formData.removeIf(x -> x.getName().contains("LeavePolicy_OverUtilization[status]"));
            formData.add(new BasicNameValuePair("LeavePolicy_OverUtilization[status]", "1"));
            if (this.getOverUtilization().countExcessAsPaid) {
                formData.removeIf(x -> x.getName().contains("LeavePolicy_OverUtilization[utilize_from]"));
                formData.add(new BasicNameValuePair("LeavePolicy_OverUtilization[utilize_from]", "0"));
            }
            if (this.getOverUtilization().countExcessAsUnPaid) {
                formData.removeIf(x -> x.getName().contains("LeavePolicy_OverUtilization[utilize_from]"));
                formData.add(new BasicNameValuePair("LeavePolicy_OverUtilization[utilize_from]", "1"));
            }
            if (this.getOverUtilization().utilizeFrom) {
                formData.removeIf(x -> x.getName().contains("LeavePolicy_OverUtilization[utilize_from]"));
                formData.add(new BasicNameValuePair("LeavePolicy_OverUtilization[utilize_from]", "2"));
            }
            if (this.getOverUtilization().dontAllowMoreThanYearlyAllocation) {
                formData.removeIf(x -> x.getName().contains("LeavePolicy_OverUtilization[not_more_than_yearly]"));
                formData.add(new BasicNameValuePair("LeavePolicy_OverUtilization[not_more_than_yearly]", "1"));
            }
            if (this.getOverUtilization().dontAllowMoreThanYearlyAccural) {
                formData.removeIf(x -> x.getName().contains("LeavePolicy_OverUtilization[not_more_than_yearly_accrual]"));
                formData.add(new BasicNameValuePair("LeavePolicy_OverUtilization[not_more_than_yearly_accrual]", "1"));
            }
            formData.add(new BasicNameValuePair("LeavePolicy_OverUtilization[fixed_overutilization]", this.getOverUtilization().fixedOverUtilization + ""));

            if (this.getOverUtilization().utlizeFromDropDown != null) {
                formData.removeIf(x -> x.getName().contains("LeavePolicy_OverUtilization[other_leave]"));
                formData.add(new BasicNameValuePair("LeavePolicy_OverUtilization[other_leave]", new LeaveService().getLeaveID(this.getOverUtilization().utlizeFromDropDown, this.groupCompanyMongoId) + ""));
            }
        }

        if (this.getPrefixSuffixSetting().indicator) {
            formData.removeIf(x -> x.getName().contains("LeavePolicyPrefixSuffix[status]"));
            formData.add(new BasicNameValuePair("LeavePolicyPrefixSuffix[status]", "1"));
            if (this.getPrefixSuffixSetting().weeklyOffPrefix.countPrefixedAsLeave) {
                formData.removeIf(x -> x.getName().contains("LeavePolicyPrefixSuffix[prefix_weekly_off]"));
                formData.add(new BasicNameValuePair("LeavePolicyPrefixSuffix[prefix_weekly_off]", "1"));
            }
            if (this.getPrefixSuffixSetting().weeklyOffPrefix.blockLeaveAfterWeeklyOff) {
                formData.removeIf(x -> x.getName().contains("LeavePolicyPrefixSuffix[prefix_weekly_off]"));

                formData.add(new BasicNameValuePair("LeavePolicyPrefixSuffix[prefix_weekly_off]", "2"));
            }

            if (this.getPrefixSuffixSetting().weeklyOffPrefix.allowLeaveAfterWeeklyOff) {
                formData.removeIf(x -> x.getName().contains("LeavePolicyPrefixSuffix[prefix_weekly_off]"));

                formData.add(new BasicNameValuePair("LeavePolicyPrefixSuffix[prefix_weekly_off]", "0"));
            }

            if (this.getPrefixSuffixSetting().weeklyOffSuffix.countSuffixedWeeklyOffAsLeave) {
                formData.removeIf(x -> x.getName().contains("LeavePolicyPrefixSuffix[suffix_weekly_off]"));

                formData.add(new BasicNameValuePair("LeavePolicyPrefixSuffix[suffix_weekly_off]", "1"));
            }

            if (this.getPrefixSuffixSetting().weeklyOffSuffix.blockLeaveBeforeWeeklyOff) {
                formData.removeIf(x -> x.getName().contains("LeavePolicyPrefixSuffix[suffix_weekly_off]"));

                formData.add(new BasicNameValuePair("LeavePolicyPrefixSuffix[suffix_weekly_off]", "2"));
            }

            if (this.getPrefixSuffixSetting().weeklyOffSuffix.allowLeaveBeforeWeeklyOff) {
                formData.removeIf(x -> x.getName().contains("LeavePolicyPrefixSuffix[suffix_weekly_off]"));

                formData.add(new BasicNameValuePair("LeavePolicyPrefixSuffix[suffix_weekly_off]", "0"));
            }

            if (this.getPrefixSuffixSetting().holidayPrefix.countPrefixedHolidayAsLeave) {
                formData.removeIf(x -> x.getName().contains("LeavePolicyPrefixSuffix[prefix_holiday]"));

                formData.add(new BasicNameValuePair("LeavePolicyPrefixSuffix[prefix_holiday]", "1"));
            }

            if (this.getPrefixSuffixSetting().holidayPrefix.blockLeaveAfterHoliday) {
                formData.removeIf(x -> x.getName().contains("LeavePolicyPrefixSuffix[prefix_holiday]"));

                formData.add(new BasicNameValuePair("LeavePolicyPrefixSuffix[prefix_holiday]", "2"));
            }

            if (this.getPrefixSuffixSetting().holidayPrefix.allowLeaveAfterHoliday) {
                formData.removeIf(x -> x.getName().contains("LeavePolicyPrefixSuffix[prefix_holiday]"));

                formData.add(new BasicNameValuePair("LeavePolicyPrefixSuffix[prefix_holiday]", "0"));
            }

            if (this.getPrefixSuffixSetting().holidaySuffix.countSuffixedHolidayAsLeave) {
                formData.removeIf(x -> x.getName().contains("LeavePolicyPrefixSuffix[suffix_holiday]"));

                formData.add(new BasicNameValuePair("LeavePolicyPrefixSuffix[suffix_holiday]", "1"));
            }

            if (this.getPrefixSuffixSetting().holidaySuffix.blockLeaveBeforeHoliday) {
                formData.removeIf(x -> x.getName().contains("LeavePolicyPrefixSuffix[suffix_holiday]"));

                formData.add(new BasicNameValuePair("LeavePolicyPrefixSuffix[suffix_holiday]", "1"));
            }

            if (this.getPrefixSuffixSetting().holidaySuffix.allowLeaveBeforeHoliday) {
                formData.removeIf(x -> x.getName().contains("LeavePolicyPrefixSuffix[suffix_holiday]"));

                formData.add(new BasicNameValuePair("LeavePolicyPrefixSuffix[suffix_holiday]", "0"));
            }
        }

        if (this.getPastDatedLeave().indicator) {
            formData.removeIf(x -> x.getName().contains("LeavePolicy_PreviousDates[status]"));
            formData.add(new BasicNameValuePair("LeavePolicy_PreviousDates[status]", "1"));

            formData.removeIf(x -> x.getName().contains("LeavePolicy_PreviousDates[no_of_days]"));
            formData.add(new BasicNameValuePair("LeavePolicy_PreviousDates[no_of_days]", this.getPastDatedLeave().maxNumOfDaysPostLeave + ""));

        }

        if(this.approvalFlow!=null){
            formData.removeIf(x -> x.getName().contains("Leaves[approval_flow]"));
            formData.add(new BasicNameValuePair("Leaves[approval_flow]", new ApprovalFlowServices().getAllApprovalFlows().get(this.getApprovalFlow().getName())));
        }

        if(this.getCredit_on_pro_rata_basis()!=null && this.getCredit_on_pro_rata_basis().indicator){

            formData.removeIf(x -> x.getName().contains("LeavePolicy_Prorated[status]"));
            formData.add(new BasicNameValuePair("LeavePolicy_Prorated[status]", "1"));

            formData.removeIf(x -> x.getName().contains("LeavePolicy_Prorated[probation_status]"));
            formData.add(new BasicNameValuePair("LeavePolicy_Prorated[probation_status]",
                        this.getCredit_on_pro_rata_basis().calculateFromJoiningDate?"0":"1"));

            formData.removeIf(x -> x.getName().contains("LeavePolicy_Prorated[mid_joining_leaves]"));
            if(this.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th)
                formData.add(new BasicNameValuePair("LeavePolicy_Prorated[mid_joining_leaves]","1"));

            formData.removeIf(x -> x.getName().contains("LeavePolicy_Prorated[mid_joining_leaves_full]"));
            if(this.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th)
            formData.add(new BasicNameValuePair("LeavePolicy_Prorated[mid_joining_leaves_full]","1"));

        }

        if(this.getCredit_on_accural_basis()!=null && this.getCredit_on_accural_basis().getIndicator()){
            formData.removeIf(x -> x.getName().contains("LeavePolicy_Accural[status]"));
            formData.add(new BasicNameValuePair("LeavePolicy_Accural[status]", "1"));

            if(this.getCredit_on_accural_basis().getMonth())
            {
                formData.removeIf(x -> x.getName().contains("LeavePolicy_Accural[is_monthly_quaterly]"));
                formData.add(new BasicNameValuePair("LeavePolicy_Accural[is_monthly_quaterly]", "0"));
                if(this.getCredit_on_accural_basis().getBeginOfMonth()) {
                    formData.removeIf(x -> x.getName().contains("LeavePolicy_Accural[starting_from]"));
                    formData.removeIf(x -> x.getName().contains("LeavePolicy_Accural[starting_from_monthly]"));


                    formData.add(new BasicNameValuePair("LeavePolicy_Accural[starting_from]", "0"));
                    formData.add(new BasicNameValuePair("LeavePolicy_Accural[starting_from_monthly]", "0"));

                }
                if(this.getCredit_on_accural_basis().getEndOfMonth()) {
                    formData.removeIf(x -> x.getName().contains("LeavePolicy_Accural[starting_from]"));
                    formData.removeIf(x -> x.getName().contains("LeavePolicy_Accural[starting_from_monthly]"));

                    formData.add(new BasicNameValuePair("LeavePolicy_Accural[starting_from]", "1"));
                    formData.add(new BasicNameValuePair("LeavePolicy_Accural[starting_from_monthly]", "1"));

                }
            }
            if(this.getCredit_on_accural_basis().getQuarter())
            {
                formData.removeIf(x -> x.getName().contains("LeavePolicy_Accural[is_monthly_quaterly]"));
                formData.add(new BasicNameValuePair("LeavePolicy_Accural[is_monthly_quaterly]", "1"));
                if(this.getCredit_on_accural_basis().getBeginOfQuarter()) {
                    formData.removeIf(x -> x.getName().contains("LeavePolicy_Accural[starting_from]"));
                    formData.removeIf(x -> x.getName().contains("LeavePolicy_Accural[starting_from_monthly]"));

                    formData.add(new BasicNameValuePair("LeavePolicy_Accural[starting_from]", "0"));
                    formData.add(new BasicNameValuePair("LeavePolicy_Accural[starting_from_monthly]", "0"));
                }
                if(this.getCredit_on_accural_basis().getEndOfQuarter()) {
                    formData.removeIf(x -> x.getName().contains("LeavePolicy_Accural[starting_from]"));
                    formData.removeIf(x -> x.getName().contains("LeavePolicy_Accural[starting_from_monthly]"));

                    formData.add(new BasicNameValuePair("LeavePolicy_Accural[starting_from]", "1"));
                    formData.add(new BasicNameValuePair("LeavePolicy_Accural[starting_from_monthly]", "0"));
                }
            }

            if(this.getCredit_on_accural_basis().getBiAnnual()){
                formData.removeIf(x -> x.getName().contains("LeavePolicy_Accural[is_monthly_quaterly]"));
                formData.add(new BasicNameValuePair("LeavePolicy_Accural[is_monthly_quaterly]", "2"));
                //formData.add(new BasicNameValuePair("LeavePolicy_Accural[starting_from]", "1"));

            }



        }
        return formData;
    }


    public void genderApplicability() {
        employee = employeeServices.generateAnEmployee("no", group_Company, "random", "no");

        Map<String, String> defaultBody = leaveService.getDefaultforLeaveDeduction();
        defaultBody.putAll(leaveService.mandatoryFieldsToCreateLeave(this));

        HashMap<String, String> empGenders = employeeServices.getGenders();
        for (String gender : empGenders.values()) {
            defaultBody.put("Leaves[restrictGender]", gender);
            leaveService.createLeaveForPolicy(defaultBody, this);

        /*    Reporter("Employee Gender is :" + employee.getGender(), "Info");
            Reporter("Leave Policy Gender Applicablility is :" + new MapUtils().getValueOfReversedMap(empGenders, gender), "Info");
*/
            Map<String, String> empLeaves = leaveAdmin.GetLeavesByOnBehalf(employee.getMongoID());

            if (empGenders.get(employee.getGender()) == gender || gender == "0") {
                if (empLeaves.keySet().contains(leave_Type))
                    Reporter(" PASS -- Leave Type is Present for user Employee Gender =" + employee.getGender()
                            + "     Policy Gender Applicability =" + mapUtils.getValueOfReversedMap(empGenders, gender), "PASS");
                else
                    Reporter(" FAIL -- Leave Type is Not Present for user Employee Gender =" + employee.getGender()
                            + "     Policy Gender Applicability =" + mapUtils.getValueOfReversedMap(empGenders, gender), "Fail");
            } else if (empGenders.get(employee.getGender()) != gender) {
                if (!empLeaves.keySet().contains(leave_Type))
                    Reporter(" PASS -- Leave Type is Not Present for user Employee Gender =" + employee.getGender()
                            + "     Policy Gender Applicability =" + mapUtils.getValueOfReversedMap(empGenders, gender), "PASS");
                else
                    Reporter(" FAIL -- Leave Type is Present for user Employee Gender =" + employee.getGender()
                            + "     Policy Gender Applicability =" + mapUtils.getValueOfReversedMap(empGenders, gender), "FAIL");

            }
        }
    }

    //returns true if date bypasses notice days
    public boolean minimumNoticeDays(LocalDate applyDate) {
        int minimumNoticeDays = Integer.parseInt(minimum_Notice_Period.replace(".0", ""));

        LocalDate currentDate = LocalDate.now();
        long diff =
                currentDate.compareTo(applyDate);
        return diff < 0;
    }

    public boolean leaveValidtyCustomMonths(Employee employee) {
        int customMonths = this.getProbation_period_before_leave_validity().customMonths;
        LocalDate leaveStartDate = LocalDate.parse(employee.getDoj()).plusMonths(customMonths);
        double diff = LocalDate.now().compareTo(leaveStartDate);
        return diff >= 0;
    }


    public boolean probation(Employee employee) {
        HashMap<String, HashMap<String, String>> probations = new Service().getProbations();
        Object[] probationsKeySet = probations.keySet().stream().collect(Collectors.toList()).toArray();

        String employeeProbation = employee.getProbation();
        int employeeProbationDays = 0;
        LocalDate employeeDOJ = LocalDate.parse(employee.getDoj());
        for (Object key : probationsKeySet) {
            HashMap<String, String> data = probations.get(key);
            if (data.values().toString().contains(employeeProbation)) {
                employeeProbationDays = Integer.parseInt(data.keySet().toString().replace("[", "").replace("]", ""));
            }
        }

        long diff = dateTimeHelper.getDifferenceBetweenDOJAndCurrentLocalLate(employeeDOJ.toString());
        return diff < employeeProbationDays;

    }


    public PrefixSuffixSetting getPrefixSuffixObject(Map<String, String> testData) {
        PrefixSuffixSetting prefixSuffixSetting = new PrefixSuffixSetting();
        if (testData.get("Indicator") != null && testData.get("Indicator") != "" && testData.get("Indicator").equalsIgnoreCase("true")) {
            prefixSuffixSetting.indicator = Boolean.parseBoolean(testData.get("Indicator"));
            if (testData.get("CountPrefixWeeklyOff") != null && testData.get("CountPrefixWeeklyOff") != "") {
                prefixSuffixSetting.weeklyOffPrefix.countPrefixedAsLeave = Boolean.parseBoolean(testData.get("CountPrefixWeeklyOff"));
                prefixSuffixSetting.weeklyOffPrefix.allowLeaveAfterWeeklyOff = false;

            }
            if (testData.get("BlockLeaveAfterWeeklyOff") != null && testData.get("") != "BlockLeaveAfterWeeklyOff") {
                prefixSuffixSetting.weeklyOffPrefix.blockLeaveAfterWeeklyOff = Boolean.parseBoolean(testData.get("BlockLeaveAfterWeeklyOff"));
                prefixSuffixSetting.weeklyOffPrefix.allowLeaveAfterWeeklyOff = false;
            }
            if (testData.get("AllowLeaveAfterWeeklyOFF") != null && testData.get("AllowLeaveAfterWeeklyOFF") != "") {
                prefixSuffixSetting.weeklyOffPrefix.allowLeaveAfterWeeklyOff = Boolean.parseBoolean(data.get("AllowLeaveAfterWeeklyOFF"));

            }
            if (testData.get("CountSuffixedWeeklyOff") != null && testData.get("CountSuffixedWeeklyOff") != "") {
                prefixSuffixSetting.weeklyOffSuffix.countSuffixedWeeklyOffAsLeave = Boolean.parseBoolean(testData.get("CountSuffixedWeeklyOff"));
                prefixSuffixSetting.weeklyOffSuffix.allowLeaveBeforeWeeklyOff = false;
            }
            if (testData.get("BlockLeaveBeforeWeeklyOff") != null && testData.get("BlockLeaveBeforeWeeklyOff") != "") {
                prefixSuffixSetting.weeklyOffSuffix.blockLeaveBeforeWeeklyOff = Boolean.parseBoolean(testData.get("BlockLeaveBeforeWeeklyOff"));
                prefixSuffixSetting.weeklyOffSuffix.allowLeaveBeforeWeeklyOff = false;
            }
            if (testData.get("AllowLeaveBeforeWeeklyOff") != null && testData.get("AllowLeaveBeforeWeeklyOff") != "") {
                prefixSuffixSetting.weeklyOffSuffix.allowLeaveBeforeWeeklyOff = Boolean.parseBoolean(testData.get("AllowLeaveBeforeWeeklyOff"));
            }
            if (testData.get("CountPrefixedHoliday") != null && testData.get("CountPrefixedHoliday") != "") {
                prefixSuffixSetting.holidayPrefix.countPrefixedHolidayAsLeave = Boolean.parseBoolean(testData.get("CountPrefixedHoliday"));
                prefixSuffixSetting.holidayPrefix.allowLeaveAfterHoliday = false;
            }
            if (testData.get("BlockLeaveAfterHoliday") != null && testData.get("BlockLeaveAfterHoliday") != "") {
                prefixSuffixSetting.holidayPrefix.blockLeaveAfterHoliday = Boolean.parseBoolean(testData.get("BlockLeaveAfterHoliday"));
                prefixSuffixSetting.holidayPrefix.allowLeaveAfterHoliday = false;
            }
            if (testData.get("AllowLeaveAfterHoliday") != null && testData.get("AllowLeaveAfterHoliday") != "") {
                prefixSuffixSetting.holidayPrefix.allowLeaveAfterHoliday = Boolean.parseBoolean(testData.get("AllowLeaveAfterHoliday"));
            }
            if (testData.get("CountSuffixedHoliday") != null && testData.get("CountSuffixedHoliday") != "") {
                prefixSuffixSetting.holidaySuffix.countSuffixedHolidayAsLeave = Boolean.parseBoolean(testData.get("CountSuffixedHoliday"));
                prefixSuffixSetting.holidaySuffix.allowLeaveBeforeHoliday = false;
            }
            if (testData.get("BlockLeaveBeforeHoliday") != null && testData.get("BlockLeaveBeforeHoliday") != "") {
                prefixSuffixSetting.holidaySuffix.blockLeaveBeforeHoliday = Boolean.parseBoolean(testData.get("BlockLeaveBeforeHoliday"));
                prefixSuffixSetting.holidaySuffix.allowLeaveBeforeHoliday = false;
            }
            if (testData.get("AllowLeaveBeforeHoliday") != null && testData.get("AllowLeaveBeforeHoliday") != "") {
                prefixSuffixSetting.holidaySuffix.allowLeaveBeforeHoliday = Boolean.parseBoolean(testData.get("AllowLeaveBeforeHoliday"));
            }
        }
        return prefixSuffixSetting;
    }

}









