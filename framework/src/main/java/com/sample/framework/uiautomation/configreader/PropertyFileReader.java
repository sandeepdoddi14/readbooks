/**
 *
 */
package com.sample.framework.uiautomation.configreader;

import java.util.Properties;
import com.sample.framework.uiautomation.base.TestBase;
import com.sample.framework.uiautomation.configuration.browser.BrowserType;
import com.sample.framework.uiautomation.Utility.ResourceHelper;


public class PropertyFileReader implements ConfigReader {

    private Properties prop = null;

    public PropertyFileReader() {
        prop = new Properties();
        try {
            prop.load(ResourceHelper.getResourcePathInputStream("src/main/resources/config.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAdminUserName() {
        return TestBase.data.get("@@username");
    }

    public String getAdminPassword() {
        return TestBase.data.get("@@password");
    }

    public String getApplication() {
        return TestBase.data.get("@@url");
    }

    public int getPageLoadTimeOut() {
        return Integer.parseInt(prop.getProperty("PageLoadTimeOut"));
    }

    public int getImplicitWait() {
        return Integer.parseInt(prop.getProperty("ImplcitWait"));
    }

    public int getExplicitWait() {
        return Integer.parseInt(prop.getProperty("ExplicitWait"));
    }

    public BrowserType getBrowser() {
        return BrowserType.valueOf(prop.getProperty("Browser"));
    }

    public String getLogLevel() {
        return prop.getProperty("Logger.Level");
    }

    public int getMaxRetryOnFailureCount() { return Integer.valueOf(prop.getProperty("Max.Retry.Failure.Count")); }

}
