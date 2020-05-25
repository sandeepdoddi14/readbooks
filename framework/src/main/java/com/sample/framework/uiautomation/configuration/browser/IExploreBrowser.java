/**
 * 
 */
package com.sample.framework.uiautomation.configuration.browser;

import com.sample.framework.uiautomation.Utility.ResourceHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.ElementScrollBehavior;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;



public class IExploreBrowser {

	public InternetExplorerOptions getIExplorerOptions() {

		InternetExplorerOptions cap = new InternetExplorerOptions();

		cap.setCapability(InternetExplorerDriver.ELEMENT_SCROLL_BEHAVIOR, ElementScrollBehavior.BOTTOM);
		cap.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION,true);
		cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
		cap.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);

		return cap;
	}

	public WebDriver getIExplorerDriver(InternetExplorerOptions options) {
		System.setProperty("webdriver.ie.driver", ResourceHelper.getResourcePath("driver/IEDriverServer.exe"));
		return new InternetExplorerDriver();
	}

	public WebDriver getIExplorerDriver() {
		System.setProperty("webdriver.ie.driver", ResourceHelper.getResourcePath("driver/IEDriverServer.exe"));
		return new InternetExplorerDriver(getIExplorerOptions());
	}
}
