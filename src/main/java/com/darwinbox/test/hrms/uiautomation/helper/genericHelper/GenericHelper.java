/**
 * 
 */
package com.darwinbox.test.hrms.uiautomation.helper.genericHelper;

import java.io.File;
import java.util.Random;

import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.darwinbox.test.hrms.uiautomation.Utility.ResourceHelper;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;
import org.openqa.selenium.support.ui.Select;

/**
 * @author balaji
 * @Creation_Date: 20 Nov 2017
 * @ClassName: GenericHelper.java
 * @LastModified_Date: 20 Nov 2017
 */
public class GenericHelper extends TestBase {

	private static final Logger log = Logger.getLogger(GenericHelper.class);
	private WebDriver driver;
	WaitHelper objWait;

	public GenericHelper(WebDriver driver) {
		this.driver = driver;
		log.debug("DropDownHelper : " + this.driver.hashCode());
		objWait = PageFactory.initElements(driver, WaitHelper.class);
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
		String text = element.getText();
		log.info("weblement valus is.." + text);
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
			element.isDisplayed();
			log.info("element is displayed.." + element);
			Reporter(label + " : is Displayed on page ", "Pass", log);
			return true;
		} catch (Exception e) {
			log.info(e);
			Reporter("Exception while loadind element " + label, "Fail", log);
			throw new RuntimeException(e.getLocalizedMessage());

		}
	}

	public static synchronized String getElementText(WebElement element) {
		if (null == element) {
			log.info("weblement is null");
			return null;
		}
		String elementText = null;
		try {
			elementText = element.getText();
		} catch (Exception ex) {
			log.info("Element not found " + ex);
		}
		return elementText;
	}

	/**
	 * This method is used to insert value in text box
	 * 
	 * @param element
	 * @param label
	 * @param value
	 * @return
	 */
	public boolean setElementText(WebElement element, String label, String value) {

		try {
			element.clear();
			element.sendKeys(value);
			Reporter("In " + label + " textbox parameter inserted is: '" + value + "'", "Pass", log);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while inserting text in " + label + " textbox", "Fail", log);
			throw new RuntimeException();			
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
	public boolean setElementText(WebElement element, String label, Integer num) {

		try {
			String value = num.toString();
			element.clear();
			element.sendKeys(value);
			Reporter("In " + label + " textbox parameter inserted is: '" + value + "'", "Pass", log);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while inserting text in " + label + " textbox", "Fail", log);
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	public boolean setElementTextinSelection(WebElement element, String label, String value,boolean clear) {
		try {
			if (clear)
				element.clear();
			element.sendKeys(value);
			Reporter("In " + label + " textbox parameter inserted is: '" + value + "'", "Pass",log);
			return true;
		} catch (Exception e) {
	 		e.printStackTrace();
			Reporter("Exception while inserting text in " + label + " textbox", "Fail",log);
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	public boolean elementClick(WebElement element, String label) {
		try {
			element.click();
			Reporter(label + " is clicked successfully", "Pass", log);
			return true;
		} catch (Exception e) {
			Reporter("Exception while clicking " + label, "Fail");
			throw new RuntimeException("Exception while clicking " + label + ": " + e.getMessage());
		}
	}

	/**
	 * This method enables or disables text based on parameter passed
	 * 
	 * @param element
	 * @param enableOrDisable
	 * @param text
	 * @return
	 */
	public boolean toggleElementStatus(WebElement element, String toggleStatus, String label) {
		try {
			if (toggleStatus.equalsIgnoreCase("Enable")) {
				if (element.isEnabled() == false) {
					element.click();
					Reporter(label + "checkbox is "+ toggleStatus + " successfully", "Pass");
					return true;
				}else {
					return true;
				}
			} else if (toggleStatus.equalsIgnoreCase("Disable")) {
				if (element.isEnabled() == true) {
					element.click();
					Reporter(label + "checkbox is "+ toggleStatus + " successfully", "Pass");
					return true;
				}
			} else {
				Reporter(label + "checkbox is "+ toggleStatus + " successfully", "Pass");
				return true;
			}	
			Reporter(label + " checkbox is not displayed", "Fail");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while " + toggleStatus + "ing " + label + " checkbox", "Fail");
			throw new RuntimeException(e.getMessage());
		}
	}

	public String getRandomNumber() throws Exception {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100);
		return String.valueOf(randomInt);
	}

	/**
	 * This method checks visibility of element on page
	 * 
	 * @author shikhar
	 * @param WebElement,
	 *            info text for WebElement
	 * 
	 */
	public boolean checkVisbilityOfElement(WebElement element, String text) {
		try {
			objWait.waitElementToBeVisible(element);
			if (element.isDisplayed() == true) {
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
	 * @author shikhar
	 * @param WebElement,
	 *            info text for WebElement
	 */
	public boolean checkInvisbilityOfElement(WebElement element, String text) {
		try {
			if (element.isDisplayed() == false) {
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

		String workingDirectory = ResourceHelper.getBaseResourcePath();
		String dir = workingDirectory + File.separator + DirectoryName;
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	public boolean selectDropdown(WebElement element, String dropdownText, String msg) {

		try {
			Select drpElement = new Select(element);
			drpElement.selectByVisibleText(dropdownText);
			Reporter("From '" + msg + "' drop down '" + dropdownText+ "' is selected", "Pass");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while selecting text from " + msg + " dropdown", "Fail");
			throw new RuntimeException(e.getMessage());
		}
	}

	public boolean toggleElement(WebElement element,boolean expected,String label){

		try{
			boolean actual = element.isSelected();
			if( actual != expected) {
				element.click();
			}return  true;
		}catch (Exception e){
return false;
		}

	}
}
