/**
 * 
 */
package com.sample.framework.uiautomation.helper.Wait;

import java.util.concurrent.TimeUnit;

import com.sample.framework.uiautomation.base.TestBase;
import org.apache.log4j.Logger;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class WaitHelper extends TestBase {
	private WebDriver driver;
	private static Logger Log = Logger.getLogger(WaitHelper.class);
	
	public WaitHelper(WebDriver driver) {
		this.driver = driver;
		Log.debug("WaitHelper : " + this.driver.hashCode());
	}
	
	public void setImplicitWait(long timeout, TimeUnit unit) {
		Log.info(timeout);
		driver.manage().timeouts().implicitlyWait(timeout, unit == null ? TimeUnit.SECONDS : unit);
	}
	
	public void waitForElementVisible(WebElement locator, int timeOutInSeconds, int pollingEveryInMiliSec) {
		Log.info(locator);
		WebDriverWait wait = getWait(timeOutInSeconds, pollingEveryInMiliSec);
		wait.until(ExpectedConditions.visibilityOf(locator));
	}
	
	public void waitForElement(WebElement element, long timeout) {
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		wait.until(ExpectedConditions.visibilityOf(element));
		Log.info("element found..."+element.getText());
	}
	
	public void waitForElementDisapear(WebElement element, long timeout) {
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		wait.until(ExpectedConditions.invisibilityOf(element));
		Log.info("element disapear .."+element.getText());
	}
	
	public WebElement waitForElement(long time,WebElement element){
		WebDriverWait wait = new WebDriverWait(driver, time);
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	/**
	 * This method initializes the wait with specified time
	 * 
	 * @param driver
	 * @param timeOutInSeconds
	 * @param pollingEveryInMiliSec
	 * @return
	 */
	private WebDriverWait getWait(int timeOutInSeconds, int pollingEveryInMiliSec) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			wait.pollingEvery(pollingEveryInMiliSec, TimeUnit.MILLISECONDS);
			wait.ignoring(NoSuchElementException.class);
			wait.ignoring(ElementNotVisibleException.class);
			wait.ignoring(StaleElementReferenceException.class);
			wait.ignoring(NoSuchFrameException.class);
			return wait;
		} catch (Exception e) {
			Reporter("Exception while applying wait to the element" + e.getMessage(), "Debug");
			throw new RuntimeException("Exception while applying wait to the element");
		}
	}


	public void waitForPageToLoad() {
		try {
			driver.manage().timeouts().pageLoadTimeout(5000, TimeUnit.MINUTES);
		} catch (Exception e) {
			Reporter("Exception while applying wait to load page:" + e.getMessage(), "Fail");
		}
	}


	public void waitElementToBeClickable(WebElement element) {
		try {
			WebDriverWait wait = getWait(20, 5);
			wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			//Reporter("Exception while applying wait to the element to become clickable:" + e.getMessage(), "Debug");
		}
	}



	public boolean waitAndClickElement(WebElement element, String text) {
		try {
			WebDriverWait wait = getWait(20, 5);
			Boolean elementPresent = wait.until(ExpectedConditions.elementToBeClickable(element)).isEnabled();
			if (elementPresent == true && element.isDisplayed()) {
				element.click();
				Reporter("Clicked on the element: " + text, "Pass");
				return true;
			}

		} catch (StaleElementReferenceException elementUpdated) {
			WebDriverWait wait = new WebDriverWait(driver, 20);
			Boolean elementPresent = wait.until(ExpectedConditions.stalenessOf(element));
			if (elementPresent == true) {
				WebElement staleElement = element;
				staleElement.click();
				Reporter("Clicked on the 'Stale' element: " + text, "Pass");
				return true;
			}
		} catch (NoSuchElementException e) {
			Reporter("Exception! - Could not click on the element: " + text + ", Exception: " + e.toString(), "Fail");
			throw (e);
		} catch (TimeoutException e) {
			WebDriverWait wait = new WebDriverWait(driver, 20);
			Boolean elementPresent = wait.until(ExpectedConditions.elementToBeClickable(element)).isEnabled();
			if (elementPresent == true && element.isDisplayed()) {
				element.click();
				Reporter("Clicked on the element: " + text, "Pass");
				return true;
			}
		} catch (Exception e) {
			Reporter("Exception while waiting for element: "+text+" to be clickable:" + e.fillInStackTrace(), "Fail");
			throw new RuntimeException(e);
		} finally {
			waitForPageToLoad();
		}
		return false;
	}


	public void waitElementToBeVisible(WebElement element) {
		try {
			WebDriverWait wait = getWait(20, 5);
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (NoSuchElementException e) {
			Reporter("Element is not visible on page", "Info");
		} catch (Exception e) {
			Reporter("Not able to apply wait to the element to be visible", "Debug");
		}
	}


	public void waitForTextToBeAvailable(WebDriver driver, WebElement element, String text) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.textToBePresentInElement(element, text));
		} catch (Exception e) {
			Reporter("Exception while applying wait to the element to become clickable:" + e.getMessage(), "Fail");
		}
	}

}
