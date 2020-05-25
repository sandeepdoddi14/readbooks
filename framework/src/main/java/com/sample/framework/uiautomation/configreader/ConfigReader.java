/**
 * 
 */
package com.sample.framework.uiautomation.configreader;

import com.sample.framework.uiautomation.configuration.browser.BrowserType;


public interface ConfigReader {
	public String getAdminUserName();
	public String getAdminPassword();
	public String getApplication();
	public int getPageLoadTimeOut();
	public int getImplicitWait();
	public int getExplicitWait();
	public String getLogLevel();
	public BrowserType getBrowser();
	public int getMaxRetryOnFailureCount();
}