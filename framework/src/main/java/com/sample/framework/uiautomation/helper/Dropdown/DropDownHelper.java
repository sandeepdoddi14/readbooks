/**
 * 
 */
package com.sample.framework.uiautomation.helper.Dropdown;

import java.util.LinkedList;
import java.util.List;

import com.sample.framework.uiautomation.base.TestBase;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;



public class DropDownHelper extends TestBase {

	private WebDriver driver;
	private static final Logger Log = Logger.getLogger(DropDownHelper.class);

	public DropDownHelper(WebDriver driver) {
		this.driver = driver;
		Log.debug("DropDownHelper : " + this.driver.hashCode());
	}

	

	public String getSelectedValue(WebElement element) {
		String value = new Select(element).getFirstSelectedOption().getText();
		Log.info("WebELement : " + element + " Value : "+ value);
		return value;
	}
	

	
	public List<String> getAllDropDownValues(WebElement locator) {
		Select select = new Select(locator);

		List<WebElement> elementList = select.getOptions();
		List<String> valueList = new LinkedList<String>();
		
		for (WebElement element : elementList) {
			Log.info(element.getText());
			valueList.add(element.getText());
		}
		return valueList;
	}

	/**
	 * This method selects visible text from Dropdown
	 * 
	 * @param element : WebElement to locate
	 * @param drptextToSelect : Text to select from Dropdown
	 * @param text : Element name
	 * 
	 * @return true/false
	 * @return
	 * @since 25/10/2017
	 */

	public boolean selectUsingVisibleValue(WebElement element, String drptextToSelect, String text) {

		try {
			Select drpElement = new Select(element);
			drpElement.selectByVisibleText(drptextToSelect);
			Reporter("From '" + text + "' drop down '" + drptextToSelect + "' is selected", "Pass", Log);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while selecting text from " + text + " dropdown", "Error", Log);
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * This method selects text from Dropdown using index
	 * 
	 * @param element,Index,
	 *            Name of the element
	 * @return true/false
	 * @return
	 * @since 25/10/2017
	 */
	public boolean selectUsingIndex(WebElement element, int drptIndexToSelect, String text) {

		try {
			Select drpElement = new Select(element);

			drpElement.selectByIndex(drptIndexToSelect);
			Reporter("From '" + text + "' drop down test data is selected", "Pass", Log);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while selecting text from " + text + " dropdown", "Error",Log);
			throw new RuntimeException(e.getMessage());
		}
	}
}
