package com.sample.framework.runner;

import com.sample.framework.beans.Configuration;
import com.sample.framework.importExport.ReadTestCases;
import com.sample.framework.importExport.ReadTestConfiguration;
import com.sample.framework.uiautomation.Utility.FileUtilsSerialized;

public class TestNGSuiteCreator {

	public static void main(String[] args) {

		String fileName = System.getProperty("configFile");
		String configFile = System.getProperty("testdata") + "config";

		ReadTestConfiguration configuration = new ReadTestConfiguration(fileName);
		Configuration config = configuration.readConfigs().get(0);

		FileUtilsSerialized.writeToFile(config, configFile);

		ReadTestCases rdtcs = new ReadTestCases(config.getTestDataFile());
		rdtcs.readTestCases(config.getTestcases());
		rdtcs.createTestngFile();
	}
}
