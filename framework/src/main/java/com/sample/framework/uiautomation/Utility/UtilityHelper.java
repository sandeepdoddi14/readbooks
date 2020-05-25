/**
 * 
 */
package com.sample.framework.uiautomation.Utility;

import java.io.File;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Properties;

import com.sample.framework.uiautomation.base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class UtilityHelper {
	
	/**
	 * This method create a directory if it does not exists
	 * @param DirectoryName
	 */
	public static void CreateADirectory(String DirectoryName) {
		
		String workingDirectory = ResourceHelper.getBaseResourcePath();
		String dir = workingDirectory + File.separator + DirectoryName;
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdir();
		} 
	}
	

	/**
	 * This file returns value from Property file
	 * @param filename
	 * @param key
	 * @return String
	 */
	public static String getProperty(String filename, String key) {
		Properties prop = new Properties();
		try {
			prop.load(ResourceHelper.getResourcePathInputStream("/src/main/resources/"+ filename +".properties"));
			return prop.getProperty(key,"no");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * This method round a number to HalfUp
	 * @param num
	 * @return double
	 */
	public double roundHalfUp(double num) {
		NumberFormat roundUp = NumberFormat.getNumberInstance();
		roundUp.setMaximumFractionDigits(0);
		roundUp.setRoundingMode(RoundingMode.HALF_UP);

		String numRoundUpString = roundUp.format(num);
		double doubleNum = Double.parseDouble(numRoundUpString);
		return doubleNum;
	}

	/**
	 * This method rounds up a number to half down
	 * @param num
	 * @return double
	 */
	public double roundHalfDown(double num) {
		NumberFormat roundUp = NumberFormat.getNumberInstance();
		roundUp.setMaximumFractionDigits(0);
		roundUp.setRoundingMode(RoundingMode.FLOOR);

		String numRoundUpString = roundUp.format(num);
		double doubleNum = Double.parseDouble(numRoundUpString);
		return doubleNum;
	}

	/**
	 * This method provides front end value of API you called
	 * @param text
	 * @return
	 */
	public String getHTMLTextFromAPI(WebDriver driver, String text) {
		try {
		String applicationURL = TestBase.data.get("@@url");
		String URL = applicationURL + "/" + text;
		driver.navigate().to(URL);
		String frontEndValue = driver.findElement(By.xpath("//body")).getText();
		if (frontEndValue.isEmpty()) {
			throw new RuntimeException("Front End value is empty PLEASE CHECK");
		}else {
			return frontEndValue;
		}	
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method calls the API
	 * @param text
	 * @return
	 */
	public boolean callTheAPI(WebDriver driver, String text) {
		try {
		String applicationURL = TestBase.data.get("@@url");
		String URL = applicationURL + "/" + text;
		driver.navigate().to(URL);
		return true;
		}catch(Exception e){
			throw new RuntimeException("Exception while calling the API" );
		}
	}


	public static void createDir(String resultsDir) {
		
		File dir = new File(resultsDir);
		if (!dir.exists()) {
			dir.mkdirs();
			System.out.println(resultsDir);
		}
	}
}
