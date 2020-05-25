package com.sample.framework.uiautomation.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.sample.framework.beans.Configuration;
import com.sample.framework.uiautomation.Utility.DateTimeHelper;
import com.sample.framework.uiautomation.Utility.MasterFileReader;
import com.sample.framework.uiautomation.Utility.UtilityHelper;
import com.sample.framework.uiautomation.configuration.browser.BrowserType;
import com.sample.framework.uiautomation.configuration.browser.ChromeBrowser;
import com.sample.framework.uiautomation.configuration.browser.FirefoxBrowser;
import com.sample.framework.uiautomation.configuration.browser.IExploreBrowser;
import com.sample.framework.uiautomation.Utility.FileUtilsSerialized;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class TestBase implements IRetryAnalyzer {

    public static Configuration config;
    public static String datapath, respath;
    public static String resultsDir;
    public static WebDriver driver;
    public static MasterFileReader ms;
    public static boolean onLaunch = false;
    public static ExtentReports extent = new ExtentReports();
    public static ExtentTest xtReportLog = null;
    public static Map<String, String> data = new HashMap<>();
    public static List<Map<String, String>> dataItem = new ArrayList<>();
    public static int dataCounter = 0;
    public static int currentData = 0;
    //gets count of tests when multiple test are used in testNG.xml
    public static int counter = 0;
    private static int retryCnt = 0, maxRetryCnt;
    public Logger log = Logger.getLogger(TestBase.class);
    ExtentTest parentLog = null;

    @BeforeSuite
    public void initSuite() {

        datapath = System.getProperty("testdata", System.getProperty("user.dir") + File.separator);
        respath = System.getProperty("resources", datapath + ".." + File.separator + "tests/src/main/resources");
        config = (FileUtilsSerialized.readFromFile(datapath + "config"));

        String sdft = new SimpleDateFormat("YYYYMMdd_HHmmss").format(new Date());
        resultsDir = datapath + "TestResults" + File.separator + "Results_" + sdft;
        UtilityHelper.createDir(resultsDir);

        PropertyConfigurator.configure((respath + "log4j.properties"));

        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(resultsDir + File.separator + "TestExecutionReport.html");
        htmlReporter.setAppendExisting(true);
        htmlReporter.config().setChartVisibilityOnOpen(false);
        htmlReporter.loadXMLConfig(new File(respath + "extent-config.xml"));

        extent = new ExtentReports();

        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("User Name", System.getProperty("user.name"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Environment", config.getInstance());
        extent.setSystemInfo("Browser", config.getBrowser());
        extent.setSystemInfo("Host Name", "Yetendra_Automation_Team");
        extent.setReportUsesManualConfiguration(true);

        List statusHierarchy = Arrays.asList(Status.ERROR, Status.FAIL, Status.FATAL, Status.WARNING, Status.PASS,
                Status.SKIP, Status.DEBUG, Status.INFO);

        extent.config().statusConfigurator().setStatusHierarchy(statusHierarchy);
        ms = new MasterFileReader(config.getTestDataFile(), "TestCaseMaster");
        ms.setExcelFileObject();

        readInstanceDetails();

        init();

        onLaunch = true;
        retryCnt++;

        log.info(" Test suite initialized ");
    }

    private void init() {
        if (onLaunch && retryCnt != 1) {
            try {
                driver.quit();
            } finally {
                System.exit(0);
            }
        }
        try {
            counter = 0;
            setUpDriver(BrowserType.valueOf(config.getBrowser().toUpperCase()));
            log.info(config.getBrowser());
            driver.manage().window().maximize();
        } catch (Exception e) {
            e.printStackTrace();
            Reporter("Exception while initlization :" + e.getMessage(), "Error");
            log.info("Browser didn't initialise. No tests run occured");
        }
    }

    private void readInstanceDetails() {

        log.debug(" Reading instance details from config.ini ");
        HierarchicalINIConfiguration properties = null;

        String fileName = respath + "config.ini";

        try {
            properties = new HierarchicalINIConfiguration(fileName);
        } catch (Exception e) {
            log.error(" Exception while reading properties from config.ini ");
            log.error(" Exiting system as reading config failed ");
            log.error(e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }


        SubnodeConfiguration c = properties.getSection(config.getInstance());

        String url = (c.getProperty("url").toString());


        data.put("@@url", url);
        data.put("@@username", (c.getProperty("username").toString()));
        data.put("@@password", (c.getProperty("password").toString()));

        data.put("@@isGuest", (c.getProperty("isGuest").toString()));


        log.info(" Instance URL ----> " + url);

    }

    private void reinit() {
        try {
            driver.quit();
        } catch (Exception e) {

        }
        init();
    }

    private void setUpDriver(BrowserType bType) {
        try {
            driver = getBrowserObject(bType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WebDriver getBrowserObject(BrowserType bType) throws Exception {
        try {
            log.info(bType);

            switch (bType) {

                case CHROME:
                    ChromeBrowser chrome = ChromeBrowser.class.newInstance();
                    return chrome.getChromeDriver(chrome.getChromeCapabilities());

                case FIREFOX:
                    FirefoxBrowser firefox = FirefoxBrowser.class.newInstance();
                    return firefox.getFirefoxDriver(firefox.getFirefoxCapabilities());

                case IEXPLORE:
                    IExploreBrowser iExplore = IExploreBrowser.class.newInstance();
                    return iExplore.getIExplorerDriver();
                default:
                    throw new Exception(" Driver Not Found : " + bType);
            }
        } catch (Exception e) {
            log.fatal(e);
            throw e;
        }
    }

    @BeforeClass
    public void beforeClass() {
        try {
            parentLog = extent.createTest((this.getClass().getSimpleName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentData = 0;
    }

    @BeforeMethod()
    public void beforeMethod(Method method) {
        try {
            gotoHomePage();
            data.putAll(dataItem.get(currentData++));
            dataCounter = dataItem.size();
            Reporter.log("*****" + method.getName() + ":" + data.get("Test_Description") + "****", true);
            if (dataCounter >= 2)
                xtReportLog = parentLog.createNode(data.get("Test_Description"))
                        .assignCategory(this.getClass().getPackage().toString()
                                .substring(this.getClass().getPackage().toString().lastIndexOf(".") + 1));
            else {
                xtReportLog = parentLog.assignCategory(this.getClass().getPackage().toString()
                        .substring(this.getClass().getPackage().toString().lastIndexOf(".") + 1));
            }

            log.info("******************************************************************************");
            log.info("*********** " + (this.getClass().getPackage().toString()
                    .substring(this.getClass().getPackage().toString().lastIndexOf(".") + 1) + "::"
                    + this.getClass().getSimpleName() + " ******"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception in Before Method" + e.getMessage());
        }

    }

    @AfterMethod()
    public void afterMethod(ITestResult result) {
        try {
            getresult(result);
            extent.flush();
        } catch (Exception e) {
            Reporter("Exception in @AfterMethod: " + e, "Error", log);
        }
    }

    @AfterClass(alwaysRun = true)
    public void endTest() {
        gotoHomePage();
    }

    public void gotoHomePage() {
        try {

            driver.get(data.get("@@url"));
            driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);


        } catch (Exception e) {
            log.info("Error in Navigating to Home Page. Creating new session and closing existing one");
            driver.manage().deleteAllCookies();
            reinit();
            gotoHomePage();
        }
    }

    @AfterSuite
    public void closeBrowser() {
        try {
            driver.close();
            driver.quit();
        } catch (NoSuchSessionException e) {
            log.info("browser closed");
        }
    }

    public void getresult(ITestResult result) {
        try {

            File screenShotName;
            String tempPath = resultsDir + File.separator + "Screenshots" + File.separator + dataItem.get(currentData - 1).get("TestCaseName");
            if (result.getStatus() == ITestResult.SUCCESS) {
                xtReportLog.log(Status.PASS, result.getName() + " test is pass");
            } else if (result.getStatus() == ITestResult.SKIP) {
                log.debug(result.getName() + " test is skipped and skip reason is:-" + result.getThrowable());
            } else if (result.getStatus() == ITestResult.FAILURE) {

                xtReportLog.log(Status.FAIL, result.getName() + " test is failed");
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String scrPath = tempPath + "-" + "FAIL" + "-" + DateTimeHelper.getCurrentLocalDateAndTime().replace(":", "_") + ".png";
                screenShotName = new File(scrPath);
                FileUtils.copyFile(scrFile, screenShotName);
                String filePath = screenShotName.toString();
                xtReportLog.warning("Failure Screenshot",
                        MediaEntityBuilder.createScreenCaptureFromPath(filePath).build());
                log.error(result.getName() + " test is failed" + result.getThrowable());
                // xtReportLog.fail(result.getThrowable());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This method is used for reporting in extent report
     *
     * @param text
     * @param status
     */
    public void Reporter(String text, String status) {
        if (status.equalsIgnoreCase("Pass")) {
            xtReportLog.log(Status.PASS, text);
            log.info(text);
        } else if (status.equalsIgnoreCase("Fail")) {
            xtReportLog.log(Status.FAIL, text);
            log.error(text);
        } else if (status.equalsIgnoreCase("Fatal")) {
            xtReportLog.log(Status.ERROR, text);
            log.fatal(text);
        } else if (status.equalsIgnoreCase("Error")) {
            xtReportLog.log(Status.ERROR, text);
            log.fatal(text);
        } else {
            xtReportLog.log(Status.INFO, text);
            log.info(text);
        }
        Reporter.log(text);
    }

    public void Reporter(String text, String status, Logger log) {
        if (status.equalsIgnoreCase("Pass")) {
            xtReportLog.log(Status.PASS, text);
            log.info(text);
        } else if (status.equalsIgnoreCase("Fail")) {
            xtReportLog.log(Status.FAIL, text);
            log.error(text);
        } else if (status.equalsIgnoreCase("Skip")) {
            xtReportLog.log(Status.SKIP, text);
            log.debug(text);
        } else if (status.equalsIgnoreCase("Fatal")) {
            xtReportLog.log(Status.ERROR, text);
            log.fatal(text);
        } else if (status.equalsIgnoreCase("Error")) {
            xtReportLog.log(Status.ERROR, text);
            log.fatal(text);
        } else {
            xtReportLog.log(Status.INFO, text);
            log.info(text);
        }
        Reporter.log(text);
    }


    public boolean retry(ITestResult result) {
        if (retryCnt < maxRetryCnt) {
            Reporter("Retrying " + result.getName() + " again and the count is " + (retryCnt + 1), "Info");
            retryCnt++;
            currentData--;
            return true;
        }
        return false;
    }


}
