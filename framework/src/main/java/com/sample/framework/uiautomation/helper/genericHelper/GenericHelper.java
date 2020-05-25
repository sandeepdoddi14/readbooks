package com.sample.framework.uiautomation.helper.genericHelper;

import com.sample.framework.uiautomation.Utility.ResourceHelper;
import com.sample.framework.uiautomation.base.TestBase;
import com.sample.framework.uiautomation.helper.Wait.WaitHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;


public class    GenericHelper extends TestBase {

    private static final Logger log = Logger.getLogger(GenericHelper.class);
    WaitHelper objWait;
    private WebDriver driver;

    public GenericHelper(WebDriver driver) {
        this.driver = driver;
        log.debug("DropDownHelper : " + this.driver.hashCode( ));
        objWait = PageFactory.initElements(driver, WaitHelper.class);
    }

    public static synchronized String getElementText(WebElement element) {
        if (null == element) {
            log.info("weblement is null");
            return null;
        }
        String elementText = null;
        try {
            elementText = element.getText( );
        } catch (Exception ex) {
            log.info("Element not found " + ex);
        }
        return elementText;
    }

    public String getTextFromElement(WebElement element, String label) {

        if (null == element) {
            log.info("weblement is null");
            return null;
        }

        boolean displayed = false;
        try {
            displayed = isDisplayed(element, label);
        } catch (Exception e) {
            log.error(e);
            return null;
        }

        if (!displayed)
            return null;
        String text = element.getText( );
        Reporter(label + " valus is.." + text, "Info");
        return text;
    }

    public String getValuefromAttribute(WebElement element, String label) {
        if (null == element)
            return null;
        if (!isDisplayed(element, label))
            return null;
        String value = element.getAttribute("value");
        log.info("weblement valus is.." + value);
        return value;
    }

    public boolean isDisplayed(WebElement element, String label) {
        try {
            element.isDisplayed( );
            log.info("element is displayed.." + element);
            Reporter(label + " : is Displayed on page ", "Pass", log);
            return true;
        } catch (NoSuchElementException n) {
                Reporter(label + " : is not Displayed on page ", "Info");
                return false;
        } catch (Exception e) {
            log.info(e);
            Reporter("Exception while loading element " + label, "Error", log);
            throw new RuntimeException(e.getLocalizedMessage( ));

        }
    }

    /**
     * This method is used to insert value in text box
     *
     * @param element
     * @param label
     * @param value
     * @return
     */
    public boolean setElementText(WebElement element, String value, String label) {

        try {
            element.clear( );
            element.sendKeys(value);
            Reporter("In " + label + " textbox parameter inserted is: '" + value + "'", "Pass", log);
            return true;
        } catch (Exception e) {
            e.printStackTrace( );
            Reporter("Exception while inserting text in " + label + " textbox", "Error", log);
            throw new RuntimeException( );
        }
    }

    /**
     * This method is used to insert value in text box
     *
     * @param element
     * @param label
     * @param num
     * @return
     */
    public boolean setElementText(WebElement element, String label, Integer num) {

        try {
            String value = num.toString( );
            element.clear( );
            element.sendKeys(value);
            Reporter("In " + label + " textbox parameter inserted is: '" + value + "'", "Pass", log);
            return true;
        } catch (Exception e) {
            e.printStackTrace( );
            Reporter("Exception while inserting text in " + label + " textbox", "Error", log);
            throw new RuntimeException(e.getLocalizedMessage( ));
        }
    }

    public boolean setElementTextinSelection(WebElement element, String label, String value, boolean clear) {
        try {
            if (clear)
                element.clear( );
            element.sendKeys(value);
            Reporter("In " + label + " textbox parameter inserted is: '" + value + "'", "Pass", log);
            return true;
        } catch (Exception e) {
            e.printStackTrace( );
            Reporter("Exception while inserting text in " + label + " textbox", "Error", log);
            throw new RuntimeException(e.getLocalizedMessage( ));
        }
    }

    public boolean submitForm(WebElement element, String label) {
        try {
            element.submit( );
            Reporter(label + " is submitted successfully", "Pass", log);
            return true;
        } catch (Exception e) {
            Reporter("Exception while submitted " + label, "Error");
            throw new RuntimeException("Exception while submitted " + label + ": " + e.getMessage( ));
        }
    }

    public boolean elementClick(WebElement element, String label) {
        try {
            element.click( );
            Reporter(label + " is clicked successfully", "Pass", log);
            return true;
        } catch (Exception e) {
            Reporter("Exception while clicking " + label, "Error");
            throw new RuntimeException("Exception while clicking " + label + ": " + e.getMessage( ));
        }
    }

    /**
     * This method enables or disables text based on parameter passed
     *
     * @param element
     * @param label
     * @param toggleStatus
     * @return
     */
    public boolean toggleElementStatus(WebElement element, String toggleStatus, String label) {
        try {
            if (toggleStatus.equalsIgnoreCase("Enable")) {
                if (element.isEnabled( ) == false) {
                    element.click( );
                    Reporter(label + "checkbox is " + toggleStatus + " successfully", "Pass");
                    return true;
                } else {
                    return true;
                }
            } else if (toggleStatus.equalsIgnoreCase("Disable")) {
                if (element.isEnabled( ) == true) {
                    element.click( );
                    Reporter(label + "checkbox is " + toggleStatus + " successfully", "Pass");
                    return true;
                }
            } else {
                Reporter(label + "checkbox is " + toggleStatus + " successfully", "Pass");
                return true;
            }
            Reporter(label + " checkbox is not displayed", "Fail");
            return false;
        } catch (Exception e) {
            e.printStackTrace( );
            Reporter("Exception while " + toggleStatus + "ing " + label + " checkbox", "Error");
            throw new RuntimeException(e.getMessage( ));
        }
    }

    public boolean toggleElement(WebElement element, boolean expected, String label) {

        try {
            boolean actual = element.isSelected( );
            if (actual != expected) {
                element.click( );
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getRandomNumber(int num) throws Exception {
        Random randomGenerator = new Random( );
        int randomInt = randomGenerator.nextInt(num);
        if (randomInt == 0) {
            randomInt = randomGenerator.nextInt(num);
        }
        return String.valueOf(randomInt);
    }

    /**
     * This method checks visibility of element on page
     *
     * @param element
     * @param text    info text for WebElement
     */
    public boolean checkVisbilityOfElement(WebElement element, String text) {
        try {
            objWait.waitElementToBeVisible(element);
            if (element.isDisplayed( ) == true) {
                Reporter(text + " element is visible", "Pass");
                return true;
            } else {
                Reporter(text + " is not visible on page", "Info");
                return false;
            }
        } catch (NoSuchElementException e) {
            Reporter(text + " is not visible on page", "Info");
            return false;
        }
    }

    /**
     * This method checks visibility of element on page
     *
     * @param element
     * @param text    info text for WebElement
     */
    public boolean checkInvisbilityOfElement(WebElement element, String text) {
        try {
            if (element.isDisplayed( ) == false) {
                Reporter("Correctly" + text + " is not visible on page", "Pass");
                return true;
            } else {
                Reporter(text + " element is visible", "fail");
                throw new RuntimeException(text + " is visible");
            }
        } catch (NoSuchElementException e) {
            Reporter("Correctly" + text + " is not visible on page", "Pass");
            return true;
        }

    }

    /**
     * This method create a directory if it does not exists
     *
     * @param DirectoryName
     */
    public void CreateADirectory(String DirectoryName) {

        String workingDirectory = ResourceHelper.getBaseResourcePath( );
        String dir = workingDirectory + File.separator + DirectoryName;
        File file = new File(dir);
        if (!file.exists( )) {
            file.mkdir( );
        }
    }

    public boolean selectDropdown(WebElement element, String dropdownText, String msg) {

        try {
            Select drpElement = new Select(element);
            drpElement.selectByVisibleText(dropdownText);
            Reporter("From '" + msg + "' drop down '" + dropdownText + "' is selected", "Pass");
            return true;
        } catch (Exception e) {
            e.printStackTrace( );
            Reporter("Exception while selecting text from " + msg + " dropdown", "Error");
            throw new RuntimeException(e.getMessage( ));
        }
    }


    public synchronized void attachFile(String fileName) {

        log.info(" Trying to attach file : " + fileName);

        sleep(1);
        if (fileName.isEmpty( )) {
            log.error(" File name provided is empty ");
            throw new RuntimeException("File name is provided empty for attachments ");
        }

        File file = new File("src/main/resources/" + fileName);
        String filePath = file.getAbsolutePath( );
        log.info(" Retrieving Absolute file path : " + filePath);

        if (!file.exists( )) {
            log.error("File doesn't exists");
            throw new RuntimeException("File doesn't exists");
        }

        log.info("Set Absolute file path to Clipboard");
        Toolkit.getDefaultToolkit( ).getSystemClipboard( ).setContents(new StringSelection(filePath), null);

        Robot robot = null;

        try {
            log.info("Create Robot Object ");
            robot = new Robot( );
        } catch (Exception e) {
            log.info(" Exception while creating Robot object ");
            log.error(e.getMessage( ));
        } finally {
            if (robot == null)
                throw new RuntimeException("Unable to initialize AWT Robot ");
        }

        boolean linux = System.getProperty("os.name").equalsIgnoreCase("linux");

        if (linux) {

            log.info(" Pressing Ctrl + L to get to file path provider ");
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_L);
            robot.keyRelease(KeyEvent.VK_L);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        }

        log.info(" Pressing Ctrl + V to paste clipboard content to file path provider ");
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        log.info(" Pressing Enter to submit attachment ");
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        sleep(1);

    }

    public void sleep(int sleepInSecs) {
        try {
            Thread.sleep(sleepInSecs * 1000);
        } catch (Exception e) {
        }
    }

    public void navigateTo(String additionURL) {
        try {
            driver.navigate( ).to(data.get("@@url") + additionURL);
            Reporter("Navigate to '" + data.get("@@url") + additionURL, "Pass");
        } catch (Exception e) {
            e.printStackTrace( );
            Reporter("Exception while navigating to '" + data.get("@@url") + additionURL + "'", "Pass");
        }
    }

    public void clearCookiesAndLoad() {
        try {
            driver.manage( ).deleteAllCookies( );
            gotoHomePage( );
            Reporter(" Done clearing cookies and reload ", "Pass");
        } catch (Exception e) {
            e.printStackTrace( );
            Reporter(" Exception while clearing cookies", "Pass");
        }
    }
}
