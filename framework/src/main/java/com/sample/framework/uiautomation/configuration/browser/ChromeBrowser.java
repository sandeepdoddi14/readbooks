/**
 * 
 */
package com.sample.framework.uiautomation.configuration.browser;

import com.sample.framework.uiautomation.base.TestBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;



public class ChromeBrowser {

	public ChromeOptions getChromeCapabilities() {

		ChromeOptions option = new ChromeOptions();
		option.addArguments("start-maximized");
		
		return option;
	}

	public WebDriver getChromeDriver(ChromeOptions chromeOptions) {
		if (System.getProperty("os.name").contains("Mac")){
			System.setProperty("webdriver.chrome.driver", TestBase.respath + "drivers/mac/chromedriver");
			return new ChromeDriver(chromeOptions);
		}
		else if(System.getProperty("os.name").contains("Window")){
			System.setProperty("webdriver.chrome.driver", TestBase.respath +"drivers/windows/chromedriver.exe");
			return new ChromeDriver(chromeOptions);
		}
		else if(System.getProperty("os.name").contains("Linux")){
			System.setProperty("webdriver.chrome.driver", TestBase.respath + "drivers/linux/chromedriver");
			return new ChromeDriver(chromeOptions);
		}
		return null;
	}
}
