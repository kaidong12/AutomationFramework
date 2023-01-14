package com.cisco.framework.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Francesco Ferrante<br>
 *         <br>
 * 
 *         Class <code>JSoapUITestRunner</code> is a thin wrapper around SoapUI's testrunner.bat functionality which<br>
 *         allows programatic access to most of the functionality of testrunner.bat through the java programming language.<br>
 *         The following actions are supported:<br>
 *         <br>
 * 
 *         1. setPasswordForSoapUISettingsXmlFile()/getPasswordForSoapUISettingsXmlFile.<br>
 *         2. setSoapUISettingsXmlFileToUse()/getSoapUISettingsXmlFileToUse.<br 3. Getting a soap respoonse result from a soap response obtained in
 *         step 2.<br>
 *         4. resetAllCommandLineArguments().<br>
 *         5. isDecryptProjectPassword()/setDecryptProjectPassword(boolean decryptProjectPassword).<br>
 *         6. isExportAllResults()/setExportAllResults(boolean exportAllResults).<br>
 *         7. isSaveProjectAfterRunningTests()/setSaveProjectAfterRunningTests(boolean saveProjectAfterRunningTests).<br>
 *         8. getMaximumTestStepErrors()/setMaximumTestStepErrors(int maximumTestStepErrors).<br>
 *         9. getDomain()/setDomain(String domain).<br>
 *         10. getEndPoint()/setEndPoint(String endPoint).<br>
 *         11. getGlobalProperty()/setGlobalProperty(String name, String value).<br>
 *         12. getHost()/setHost(String host).<br>
 *         13. getjUnitXMLReportsOutput()/setjUnitXMLReportsOutput(String jUnitXMLReportsOutput).<br>
 *         14. getOutPutFolder()/setOutPutFolder(String outPutFolder).<br>
 *         15. getProjectProperty()/setProjectProperty(String name, String value).<br>
 *         16. getSoapRequest(String testResultsFile).<br>
 *         17. getSoapResponse(String testResultsFile).<br>
 *         18. getSystemProperty()/setSystemProperty(String name, String value).<br>
 *         19. getWssPassword()/setWssPassword(String wssPassword).<br>
 *         20. isDecryptProjectPassword()/setPathToTestRunnerBathFile(String pathToTestRunnerBathFile).<br>
 *         21. execute(String pathToSoapUIXMLProjectFile, String testSuite, String testCase, String endPoint, String outPutFolder, String userName,
 *         String password).<br>
 *         22. execute(String pathToSoapUIXMLProjectFile, String testSuite, String testCase, String endPoint, String outPutFolder).<br>
 *         23. execute(String pathToSoapUIXMLProjectFile, String testSuite, String testCase, String outPutFolder, String userName, String password).
 *         <br>
 *         24. execute(String pathToSoapUIXMLProjectFile, String testSuite, String testCase, String outPutFolder).<br>
 *         25. executeTestSuite(String pathToSoapUIXMLProjectFile, String testSuite, String endPoint, String outPutFolder, String userName, String
 *         password).<br>
 *         26. executeTestSuite(String pathToSoapUIXMLProjectFile, String testSuite, String endPoint, String outPutFolder).<br>
 *         27. executeTestSuite(String pathToSoapUIXMLProjectFile, String testSuite, String outPutFolder, String userName, String password).<br>
 *         28. executeTestSuite(String pathToSoapUIXMLProjectFile, String testSuite, String outPutFolder).<br>
 *         <br>
 * 
 *         The following subset of testrunner switches are supported:<br>
 *         <br>
 *         "-v, -t, -D, -G, -P, -S, -a, -c, -d, -e, -f, -h, -j, -m, -s, -u, -p, -w, -x".<br>
 *         <br>
 * 
 *         Please see testrunner.bat for detailed description of the above switches.
 * 
 *         The sample code below illustrates the basic usage of class <code>JSoapClient</code>.<br>
 *         <br>
 * 
 *         <code>
 *  	String pathToTestRunnerBathFile    = "C:/Program Files/eviware/soapUI-3.6.1/bin/testrunner.bat";
 *		String pathToSoapUIXMLProjectFile  = "C:/Automation/CWT/CWT-soapui-project.xml";
 *		String outPutFolder                = "C:/Automation/CWT/temp";
 *		String testSuite                   = "TestSuite1";
 *		String testCase                    = "TestCaseRequest2";
 *		String endPoint                    = "https://ws.messaging.qa.covisint.com/webservices-gateway/service/connect";
 *		String userName                    = "imsspml";
 *		String password                    = "imsspml!qa";
 *		String testResultsFile             = "C:/Automation/CWT/temp/TestSuite1-TestCaseRequest2-SyncSpmlAdd__Request_2-0-FAILED_2011-05-16-17_59_00.768.txt";
 *		
 *		JSoapUITestRunner soapUITestRunner = null;		
 *		try {
 *			soapUITestRunner = new JSoapUITestRunner(pathToTestRunnerBathFile);
 *			//soapUITestRunner.execute(pathToSoapUIXMLProjectFile, testSuite, testCase, endPoint, outPutFolder, userName, password);
 *			soapUITestRunner.executeTestSuite(pathToSoapUIXMLProjectFile, testSuite, outPutFolder, userName, password);
 *			String soapRequest  = soapUITestRunner.getSoapRequest(testResultsFile);
 *			String soapResponse = soapUITestRunner.getSoapResponse(testResultsFile);
 *
 *			System.out.println("*** SOAP REQUEST ***");
 *			System.out.println(soapRequest);
 *			System.out.println();
 *			System.out.println("*** SOAP RESPONSE ***");
 *			System.out.println(soapResponse);
 *		} catch (Exception e) {
 *			e.printStackTrace();
 *		}
 *  </code>
 */
public class JSoapUITestRunner {

	// Some constants
	private final String	NEW_LINE										= System.getProperty("line.separator");
	// Supported switches
	private final String	PASSWORD_FOR_SOAPUI_SETTINGS_XML_FILE_SWITCH	= "-v";
	private final String	SOAPUI_SETTINGS_XML_FILE_TO_USE_SWITCH			= "-t";
	private final String	SYSTEM_PROPERTY_SWITCH							= "-D";
	private final String	GLOBAL_PROPERTY_SWITCH							= "-G";
	private final String	PROJECT_PROPERTY_SWITCH							= "-P";
	private final String	SAVE_PROJECT_AFTER_RUNNING_TESTS_SWITCH			= "-S";
	private final String	EXPORT_ALL_RESULTS_SWITCH						= "-a";
	private final String	TESTCASE_SWITCH									= "-c";
	private final String	DOMAIN_SWITCH									= "-d";
	private final String	ENDPOINT_SWITCH									= "-e";
	private final String	OUTPUT_FOLDER_SWITCH							= "-f";
	private final String	HOST_SWITCH										= "-h";
	private final String	JUNIT_XML_REPORTS_OUTPUT_SWITCH					= "-j";
	private final String	MAXIMUM_TESTSTEP_ERRORS_SWITCH					= "-m";
	private final String	TESTSUITE_SWITCH								= "-s";
	private final String	USERNAME_SWITCH									= "-u";
	private final String	PASSWORD_SWITCH									= "-p";
	private final String	WSSPASSWORD_SWITCH								= "-w";
	private final String	PROJECTPASSWORD_SWITCH							= "-x";

	// Holds a valid path and filename to the SoapUI "test runner" batch file.
	private String			pathToTestRunnerBathFile						= "";

	// Holds a valid path and filename to the SoapUI XML project file.
	private String			pathToSoapUIXMLProjectFile						= "";

	// Holds the password for soapui-settings.xml file switch -> '-v'.
	private String			passwordForSoapUISettingsXmlFile				= "";

	// Holds the soapui-settings.xml file to use switch -> '-t'.
	private String			soapUISettingsXmlFileToUse						= "";

	// Holds the system property key value pair in the form 'name=value' switch -> '-D'.
	private String			systemProperty									= "";

	// Holds the global property key value pair in the form 'name=value' switch -> '-G'.
	private String			globalProperty									= "";

	// Holds the project property key value pair in the form 'name=value' switch -> '-P'.
	private String			projectProperty									= "";

	// If 'saveProjectAfterRunningTests' is set to 'false' the project is not saved after running
	// tests. If set to 'true' project is saved after running tests switch -> '-S'.
	private boolean			saveProjectAfterRunningTests					= false;

	// If 'exportAllResults' is set to 'false' exporting of all results is not turned on.
	// If set to 'true' exporting of all results is turned on switch -> '-a'.
	private boolean			exportAllResults								= false;

	// Holds the name of the testcase to execute switch -> '-c'.
	private String			testCase										= "";

	// Holds the name of the domain for text execution switch -> '-d'.
	private String			domain											= "";

	// Holds the endpoint for text execution switch -> '-e'.
	private String			endPoint										= "";

	// Holds the output folder to export results to switch -> '-f'.
	private String			outPutFolder									= "";

	// Holds the host switch -> '-h'.
	private String			host											= "";

	// Holds the output to include JUnit XML reports switch -> '-j'.
	private String			jUnitXMLReportsOutput							= "";

	// Holds the maximum number of TestStep errors to save for each testcase switch -> '-m'.
	private int				maximumTestStepErrors							= 0;

	// Holds the testsuite switch -> '-s'.
	private String			testSuite										= "";

	// Holds the username switch -> '-u'.
	private String			userName										= "";

	// Holds the pasword switch -> '-p'.
	private String			password										= "";

	// Holds the WSS password type, either 'Text' or 'Digest' switch -> '-w'.
	private String			wssPassword										= "";

	// If 'decryptProjectPassword' is set to 'true' the project password is set for decryption if it is encrypted.
	// The default setting is 'false' which means that the project password is not set for decryption. switch -> '-x'.
	private boolean			decryptProjectPassword							= false;

	public JSoapUITestRunner(String pathToTestRunnerBathFile) throws Exception {
		setPathToTestRunnerBathFile(pathToTestRunnerBathFile);
	}

	/**
	 * @author Francesco Ferrante
	 * @return String[] <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to return a string array of a subset of switches supported by<br>
	 *         SoapUI testrunner.<br>
	 */
	public String[] getSupportedSwitches() {
		return new String[] { PASSWORD_FOR_SOAPUI_SETTINGS_XML_FILE_SWITCH, SOAPUI_SETTINGS_XML_FILE_TO_USE_SWITCH, SYSTEM_PROPERTY_SWITCH,
				GLOBAL_PROPERTY_SWITCH, PROJECT_PROPERTY_SWITCH, SAVE_PROJECT_AFTER_RUNNING_TESTS_SWITCH, EXPORT_ALL_RESULTS_SWITCH, TESTCASE_SWITCH,
				DOMAIN_SWITCH, ENDPOINT_SWITCH, OUTPUT_FOLDER_SWITCH, HOST_SWITCH, JUNIT_XML_REPORTS_OUTPUT_SWITCH, MAXIMUM_TESTSTEP_ERRORS_SWITCH,
				TESTSUITE_SWITCH, USERNAME_SWITCH, PASSWORD_SWITCH, WSSPASSWORD_SWITCH, PROJECTPASSWORD_SWITCH };
	}

	/**
	 * @author Francesco Ferrante
	 * @return the passwordForSoapUISettingsXmlFile <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns password for soapui-settings.xml file.<br>
	 */
	public String getPasswordForSoapUISettingsXmlFile() {
		return passwordForSoapUISettingsXmlFile.replace(PASSWORD_FOR_SOAPUI_SETTINGS_XML_FILE_SWITCH, "").trim();
	}

	/**
	 * @author Francesco Ferrante
	 * @param passwordForSoapUISettingsXmlFile
	 *            the passwordForSoapUISettingsXmlFile to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets password for soapui-settings.xml file.<br>
	 */
	public void setPasswordForSoapUISettingsXmlFile(String passwordForSoapUISettingsXmlFile) throws Exception {
		if (passwordForSoapUISettingsXmlFile == null) {
			throw new NullPointerException("passwordForSoapUISettingsXmlFile");
		}
		if (passwordForSoapUISettingsXmlFile.isEmpty()) {
			this.passwordForSoapUISettingsXmlFile = "";
		} else {
			this.passwordForSoapUISettingsXmlFile = PASSWORD_FOR_SOAPUI_SETTINGS_XML_FILE_SWITCH + passwordForSoapUISettingsXmlFile;
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the soapUISettingsXmlFileToUse <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns the soapui-settings.xml file to use.<br>
	 */
	public String getSoapUISettingsXmlFileToUse() {
		return soapUISettingsXmlFileToUse.replace(SOAPUI_SETTINGS_XML_FILE_TO_USE_SWITCH, "").trim();
	}

	/**
	 * @author Francesco Ferrante
	 * @param soapUISettingsXmlFileToUse
	 *            the soapUISettingsXmlFileToUse to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets the soapui-settings.xml file to use.<br>
	 */
	public void setSoapUISettingsXmlFileToUse(String soapUISettingsXmlFileToUse) throws Exception {
		if (soapUISettingsXmlFileToUse == null) {
			throw new NullPointerException("soapUISettingsXmlFileToUse");
		}
		if (soapUISettingsXmlFileToUse.isEmpty()) {
			this.soapUISettingsXmlFileToUse = "";
		} else {
			this.soapUISettingsXmlFileToUse = SOAPUI_SETTINGS_XML_FILE_TO_USE_SWITCH + soapUISettingsXmlFileToUse;
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the systemProperty <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns system property in the form of a 'name/value' pair name=value.<br>
	 */
	public String getSystemProperty() {
		return systemProperty.replace(SYSTEM_PROPERTY_SWITCH, "").trim();
	}

	/**
	 * @author Francesco Ferrante
	 * @param systemProperty
	 *            the systemProperty to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets the system property in the form of a 'name/value' pair name=value.<br>
	 */
	public void setSystemProperty(String name, String value) throws Exception {
		if (name == null) {
			throw new NullPointerException("name");
		}
		if (value == null) {
			throw new NullPointerException("value");
		}
		if (!name.isEmpty() && !value.isEmpty()) {
			this.systemProperty = SYSTEM_PROPERTY_SWITCH + name + "=" + value;
		} else {
			this.systemProperty = "";
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the globalProperty <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns global property in the form of a 'name/value' pair name=value.<br>
	 */
	public String getGlobalProperty() {
		return globalProperty.replace(GLOBAL_PROPERTY_SWITCH, "").trim();
	}

	/**
	 * @author Francesco Ferrante
	 * @param globalProperty
	 *            the globalProperty to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets global property in the form of a 'name/value' pair name=value.<br>
	 */
	public void setGlobalProperty(String name, String value) throws Exception {
		if (name == null) {
			throw new NullPointerException("name");
		}
		if (value == null) {
			throw new NullPointerException("value");
		}
		if (!name.isEmpty() && !value.isEmpty()) {
			this.globalProperty = GLOBAL_PROPERTY_SWITCH + name + "=" + value;
		} else {
			this.globalProperty = "";
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the projectProperty <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns project property in the form of a 'name/value' pair name=value.<br>
	 */
	public String getProjectProperty() {
		return projectProperty.replace(PROJECT_PROPERTY_SWITCH, "").trim();
	}

	/**
	 * @author Francesco Ferrante
	 * @param projectProperty
	 *            the projectProperty to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets or overrides project property in the form of a 'name/value' pair name=value.<br>
	 */
	public void setProjectProperty(String name, String value) throws Exception {
		if (name == null) {
			throw new NullPointerException("name");
		}
		if (value == null) {
			throw new NullPointerException("value");
		}
		if (!name.isEmpty() && !value.isEmpty()) {
			this.projectProperty = PROJECT_PROPERTY_SWITCH + name + "=" + value;
		} else {
			this.projectProperty = "";
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the saveProjectAfterRunningTests <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns 'true' if project is saved after running tests or<br>
	 *         'false' if project is not saved after running tests.<br>
	 */
	public boolean isSaveProjectAfterRunningTests() {
		return saveProjectAfterRunningTests;
	}

	/**
	 * @author Francesco Ferrante
	 * @param saveProjectAfterRunningTests
	 *            the saveProjectAfterRunningTests to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Set argument 'saveProjectAfterRunningTests' to 'true' if project is to be<br>
	 *            saved after running tests. The default setting is 'false' which means that the<br>
	 *            is not saved after running tests.
	 */
	public void setSaveProjectAfterRunningTests(boolean saveProjectAfterRunningTests) {
		this.saveProjectAfterRunningTests = saveProjectAfterRunningTests;
	}

	/**
	 * @author Francesco Ferrante
	 * @return the exportAllResults <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns 'true' if the exporting of all results is turned on and 'false' otherwise.<br>
	 *         The default setting is 'false' exporting of all test results is not turned on.<br>
	 */
	public boolean isExportAllResults() {
		return exportAllResults;
	}

	/**
	 * @author Francesco Ferrante
	 * @param exportAllResults
	 *            the exportAllResults to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Set argument 'exportAllResults' to 'true' to turn the exporting of all test results.<br>
	 *            The default setting is 'false' which means that the exporting of all test results is<br>
	 *            not turned on.<br>
	 */
	public void setExportAllResults(boolean exportAllResults) {
		this.exportAllResults = exportAllResults;
	}

	/**
	 * @author Francesco Ferrante
	 * @param testCase
	 *            the testCase to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets the testcase to execute within a given user-specified testsuite.<br>
	 */
	private void setTestCase(String testCase) throws Exception {
		if (testCase == null) {
			throw new NullPointerException("testCase");
		}
		if (testCase.isEmpty()) {
			this.testCase = "";
		} else {
			this.testCase = TESTCASE_SWITCH + testCase;
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the domain <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns the domain.<br>
	 */
	public String getDomain() {
		return domain.replace(DOMAIN_SWITCH, "").trim();
	}

	/**
	 * @author Francesco Ferrante
	 * @param domain
	 *            the domain to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets the domain.<br>
	 */
	public void setDomain(String domain) throws Exception {
		if (domain == null) {
			throw new NullPointerException("domain");
		}
		if (domain.isEmpty()) {
			this.domain = "";
		} else {
			this.domain = DOMAIN_SWITCH + domain;
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the endPoint <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns the endpoint.<br>
	 */
	public String getEndPoint() {
		return endPoint.replace(ENDPOINT_SWITCH, "").trim();
	}

	/**
	 * @author Francesco Ferrante
	 * @param endPoint
	 *            the endPoint to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets the endpoint.<br>
	 */
	public void setEndPoint(String endPoint) throws Exception {
		if (endPoint == null) {
			throw new NullPointerException("endPoint");
		}
		if (endPoint.isEmpty()) {
			this.endPoint = "";
		} else {
			this.endPoint = ENDPOINT_SWITCH + endPoint;
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the outPutFolder <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns the output folder to export results to
	 */
	public String getOutPutFolder() {
		return outPutFolder.replace(OUTPUT_FOLDER_SWITCH, "").trim();
	}

	/**
	 * @author Francesco Ferrante
	 * @param outPutFolder
	 *            the outPutFolder to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets the output folder to export results to. If<br>
	 *            argument 'outPutFolder' then test results are exported<br>
	 *            to the current working directory.<br>
	 */
	public void setOutPutFolder(String outPutFolder) throws Exception {
		if (outPutFolder == null) {
			throw new NullPointerException("outPutFolder");
		}
		if (outPutFolder.isEmpty()) {
			this.outPutFolder = OUTPUT_FOLDER_SWITCH + System.getProperty("user.dir");
		} else {
			this.outPutFolder = OUTPUT_FOLDER_SWITCH + outPutFolder;
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the host <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns the host.<br>
	 */
	public String getHost() {
		return host.replace(HOST_SWITCH, "").trim();
	}

	/**
	 * @author Francesco Ferrante
	 * @param host
	 *            the host to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets the host.<br>
	 */
	public void setHost(String host) {
		if (host == null) {
			throw new NullPointerException("host");
		}
		if (host.isEmpty()) {
			this.host = "";
		} else {
			this.host = HOST_SWITCH + host;
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the jUnitXMLReportsOutput <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns the output to include JUnit XML reports.<br>
	 */
	public String getjUnitXMLReportsOutput() {
		return jUnitXMLReportsOutput.replace(JUNIT_XML_REPORTS_OUTPUT_SWITCH, "").trim();
	}

	/**
	 * @author Francesco Ferrante
	 * @param jUnitXMLReportsOutput
	 *            the jUnitXMLReportsOutput to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets the output to include JUnit XML reports.<br>
	 */
	public void setjUnitXMLReportsOutput(String jUnitXMLReportsOutput) {
		if (jUnitXMLReportsOutput == null) {
			throw new NullPointerException("jUnitXMLReportsOutput");
		}
		if (jUnitXMLReportsOutput.isEmpty()) {
			this.jUnitXMLReportsOutput = "";
		} else {
			this.jUnitXMLReportsOutput = JUNIT_XML_REPORTS_OUTPUT_SWITCH + jUnitXMLReportsOutput;
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the maximumTestStepErrors <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns the maximum number of TestStep errors to save for each testcase.<br>
	 */
	public int getMaximumTestStepErrors() {
		return maximumTestStepErrors;
	}

	/**
	 * @author Francesco Ferrante
	 * @param maximumTestStepErrors
	 *            the maximumTestStepErrors to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets the maximum number of TestStep errors to save for each testcase.<br>
	 */
	public void setMaximumTestStepErrors(int maximumTestStepErrors) throws Exception {
		if (maximumTestStepErrors < 0) {
			throw new IllegalArgumentException("POSITIVE QUANTITY REQUIRED");
		}
		this.maximumTestStepErrors = maximumTestStepErrors;
	}

	/**
	 * @author Francesco Ferrante
	 * @param testSuite
	 *            the testSuite to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets the testsuite.<br>
	 */
	private void setTestSuite(String testSuite) throws Exception {
		if (testSuite == null) {
			throw new NullPointerException("testSuite");
		}
		if (testSuite.isEmpty()) {
			throw new IllegalArgumentException("INVALID TESTSUITE");
		}
		this.testSuite = TESTSUITE_SWITCH + testSuite;
	}

	/**
	 * @author Francesco Ferrante
	 * @param userName
	 *            the userName to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets the username.<br>
	 */
	private void setUserName(String userName) throws Exception {
		if (userName == null) {
			throw new NullPointerException("userName");
		}
		if (userName.isEmpty()) {
			this.userName = "";
		} else {
			this.userName = USERNAME_SWITCH + userName;
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @param password
	 *            the password to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets the password.<br>
	 */
	private void setPassword(String password) throws Exception {
		if (password == null) {
			throw new NullPointerException("password");
		}
		if (password.isEmpty()) {
			this.password = "";
		} else {
			this.password = PASSWORD_SWITCH + password;
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the wssPassword <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns the WSS password type, either 'Text' or 'Digest'.<br>
	 */
	public String getWssPassword() {
		return wssPassword.replace(WSSPASSWORD_SWITCH, "").trim();
	}

	/**
	 * @author Francesco Ferrante
	 * @param wssPassword
	 *            the wssPassword to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets the WSS password type, either 'Text' or 'Digest'.<br>
	 */
	public void setWssPassword(String wssPassword) throws Exception {
		if (wssPassword == null) {
			throw new NullPointerException("wssPassword");
		}
		if (wssPassword.isEmpty()) {
			this.wssPassword = "";
		} else {
			if (!wssPassword.equalsIgnoreCase("Text") || !wssPassword.equalsIgnoreCase("Digest")) {
				throw new IllegalArgumentException("VALID WSS PASSWORDS: 'Text' OR 'Digest'");
			}
			this.wssPassword = WSSPASSWORD_SWITCH + wssPassword;
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the decryptProjectPassword <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns 'true' if the project password is set for decryption<br>
	 *         and 'false' otherwise.<br>
	 */
	public boolean isDecryptProjectPassword() {
		return decryptProjectPassword;
	}

	/**
	 * @author Francesco Ferrante
	 * @param decryptProjectPassword
	 *            the decryptProjectPassword to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            If 'decryptProjectPassword' is set to 'true' project password is set for decryption<br>
	 *            if project is encrypted. If set to 'false' project password is not set for decryption.<br>
	 */
	public void setDecryptProjectPassword(boolean decryptProjectPassword) {
		this.decryptProjectPassword = decryptProjectPassword;
	}

	/**
	 * @author Francesco Ferrante
	 * @param pathToTestRunnerBathFile
	 *            the pathToTestRunnerBathFile to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets the path and filename to SoapUI's 'testrunner' batch file.<br>
	 */
	private void setPathToTestRunnerBathFile(String pathToTestRunnerBathFile) throws Exception {
		if (pathToTestRunnerBathFile == null) {
			throw new NullPointerException("pathToTestRunnerBathFile");
		}
		if (pathToTestRunnerBathFile.isEmpty()) {
			throw new IllegalArgumentException("INVALID PATH AND FILENAME TO TEST RUNNER BATCH FILE");
		}
		if (!new File(pathToTestRunnerBathFile).exists()) {
			throw new IllegalArgumentException("FILE: " + "'" + pathToTestRunnerBathFile + "' DOES NOT EXIST.");
		}
		this.pathToTestRunnerBathFile = pathToTestRunnerBathFile;
	}

	/**
	 * @author Francesco Ferrante
	 * @param pathToSoapUIXMLProjectFile
	 *            the pathToSoapUIXMLProjectFile to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Sets the path and filename to the SoapUI XML project file.<br>
	 */
	private void setPathToSoapUIXMLProjectFile(String pathToSoapUIXMLProjectFile) throws Exception {
		if (pathToSoapUIXMLProjectFile == null) {
			throw new NullPointerException("pathToSoapUIXMLProjectFile");
		}
		if (pathToSoapUIXMLProjectFile.isEmpty()) {
			throw new IllegalArgumentException("INVALID PATH AND FILENAME TO SOAPUI XML PROJECT FILE");
		}
		if (!new File(pathToSoapUIXMLProjectFile).exists()) {
			throw new IllegalArgumentException("FILE: " + "'" + pathToSoapUIXMLProjectFile + "' DOES NOT EXIST.");
		}
		this.pathToSoapUIXMLProjectFile = pathToSoapUIXMLProjectFile;
	}

	/**
	 * @author Francesco Ferrante
	 * @return A string of command line arguments <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to get a string array of command line arguments.
	 */
	private String[] getCommandLneArguments() {
		String[] res = null;

		List<String> commandLineArguments = new ArrayList<String>();

		if (!pathToTestRunnerBathFile.isEmpty()) {
			commandLineArguments.add(pathToTestRunnerBathFile);
		}

		if (!pathToSoapUIXMLProjectFile.isEmpty()) {
			commandLineArguments.add(pathToSoapUIXMLProjectFile);
		}

		if (!testSuite.isEmpty()) {
			commandLineArguments.add(testSuite);
		}

		if (!testCase.isEmpty()) {
			commandLineArguments.add(testCase);
		}

		if (!endPoint.isEmpty()) {
			commandLineArguments.add(endPoint);
		}

		if (!userName.isEmpty()) {
			commandLineArguments.add(userName);
		}

		if (!password.isEmpty()) {
			commandLineArguments.add(password);
		}

		if (exportAllResults) {
			commandLineArguments.add(EXPORT_ALL_RESULTS_SWITCH);
		}

		if (!outPutFolder.isEmpty()) {
			commandLineArguments.add(outPutFolder);
		}

		if (maximumTestStepErrors > 0) {
			commandLineArguments.add(MAXIMUM_TESTSTEP_ERRORS_SWITCH);
		}

		if (!domain.isEmpty()) {
			commandLineArguments.add(domain);
		}

		if (!host.isEmpty()) {
			commandLineArguments.add(host);
		}

		if (saveProjectAfterRunningTests) {
			commandLineArguments.add(SAVE_PROJECT_AFTER_RUNNING_TESTS_SWITCH);
		}

		if (!systemProperty.isEmpty()) {
			commandLineArguments.add(systemProperty);
		}

		if (!globalProperty.isEmpty()) {
			commandLineArguments.add(globalProperty);
		}

		if (!projectProperty.isEmpty()) {
			commandLineArguments.add(projectProperty);
		}

		if (!soapUISettingsXmlFileToUse.isEmpty()) {
			commandLineArguments.add(soapUISettingsXmlFileToUse);
		}

		if (!passwordForSoapUISettingsXmlFile.isEmpty()) {
			commandLineArguments.add(passwordForSoapUISettingsXmlFile);
		}

		if (!jUnitXMLReportsOutput.isEmpty()) {
			commandLineArguments.add(jUnitXMLReportsOutput);
		}

		if (!wssPassword.isEmpty()) {
			commandLineArguments.add(wssPassword);
		}

		if (decryptProjectPassword) {
			commandLineArguments.add(PROJECTPASSWORD_SWITCH);
		}

		((ArrayList<String>) commandLineArguments).trimToSize();
		int numCommandLineArguments = commandLineArguments.size();
		if (numCommandLineArguments != 0) {
			res = new String[numCommandLineArguments];
			commandLineArguments.toArray(res);
		}

		return res;
	}

	/**
	 * @author Francesco Ferrante <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to reset all of the supported command line arguments to there default values.<br>
	 */
	public void resetAllCommandLineArguments() {
		passwordForSoapUISettingsXmlFile = "";
		soapUISettingsXmlFileToUse = "";
		systemProperty = "";
		globalProperty = "";
		projectProperty = "";
		testCase = "";
		domain = "";
		endPoint = "";
		outPutFolder = "";
		host = "";
		jUnitXMLReportsOutput = "";
		testSuite = "";
		userName = "";
		password = "";
		wssPassword = "";

		maximumTestStepErrors = 0;

		saveProjectAfterRunningTests = false;
		exportAllResults = false;
		decryptProjectPassword = false;
	}

	/**
	 * @author Francesco Ferrante
	 * @param pathToSoapUIXMLProjectFile
	 * @param testSuite
	 * @param testCase
	 * @param endPoint
	 * @param outPutFolder
	 * @param userName
	 * @param password
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to execute a SoapUI project xml file 'pathToSoapUIXMLProjectFile' given:<br>
	 *             1. test suite<br>
	 *             2. test case<br>
	 *             3. end point url<br>
	 *             4. a test results output folder<br>
	 *             5. uaername<br>
	 *             6. password<br>
	 */
	public void execute(String pathToSoapUIXMLProjectFile, String testSuite, String testCase, String endPoint, String outPutFolder, String userName,
			String password) throws Exception {
		setPathToSoapUIXMLProjectFile(pathToSoapUIXMLProjectFile);
		setTestSuite(testSuite);
		setTestCase(testCase);
		setEndPoint(endPoint);
		setOutPutFolder(outPutFolder);
		setUserName(userName);
		setPassword(password);
		execute();
	}

	/**
	 * @author Francesco Ferrante
	 * @param pathToSoapUIXMLProjectFile
	 * @param testSuite
	 * @param endPoint
	 * @param outPutFolder
	 * @param userName
	 * @param password
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to execute a SoapUI project xml file 'pathToSoapUIXMLProjectFile' given:<br>
	 *             1. test suite<br>
	 *             2. end point url<br>
	 *             3. a test results output folder<br>
	 *             4. uaername<br>
	 *             5. password<br>
	 */
	public void executeTestSuite(String pathToSoapUIXMLProjectFile, String testSuite, String endPoint, String outPutFolder, String userName,
			String password) throws Exception {
		setPathToSoapUIXMLProjectFile(pathToSoapUIXMLProjectFile);
		setTestSuite(testSuite);
		setTestCase("");
		setEndPoint(endPoint);
		setOutPutFolder(outPutFolder);
		setUserName(userName);
		setPassword(password);
		execute();
	}

	/**
	 * @author Francesco Ferrante
	 * @param pathToSoapUIXMLProjectFile
	 * @param testSuite
	 * @param testCase
	 * @param endPoint
	 * @param outPutFolder
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to execute a SoapUI project xml file 'pathToSoapUIXMLProjectFile' given:<br>
	 *             1. test suite<br>
	 *             2. test case<br>
	 *             3. end point url<br>
	 *             4. a test results output folder<br>
	 */
	public void execute(String pathToSoapUIXMLProjectFile, String testSuite, String testCase, String endPoint, String outPutFolder) throws Exception {
		setPathToSoapUIXMLProjectFile(pathToSoapUIXMLProjectFile);
		setTestSuite(testSuite);
		setTestCase(testCase);
		setEndPoint(endPoint);
		setOutPutFolder(outPutFolder);
		setUserName("");
		setPassword("");
		execute();
	}

	/**
	 * @author Francesco Ferrante
	 * @param pathToSoapUIXMLProjectFile
	 * @param testSuite
	 * @param endPoint
	 * @param outPutFolder
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to execute a SoapUI project xml file 'pathToSoapUIXMLProjectFile' given:<br>
	 *             1. test suite<br>
	 *             2. test case<br>
	 *             3. end point url<br>
	 *             4. a test results output folder<br>
	 */
	public void executeTestSuite(String pathToSoapUIXMLProjectFile, String testSuite, String endPoint, String outPutFolder) throws Exception {
		setPathToSoapUIXMLProjectFile(pathToSoapUIXMLProjectFile);
		setTestSuite(testSuite);
		setTestCase("");
		setEndPoint(endPoint);
		setOutPutFolder(outPutFolder);
		setUserName("");
		setPassword("");
		execute();
	}

	/**
	 * @author Francesco Ferrante
	 * @param pathToSoapUIXMLProjectFile
	 * @param testSuite
	 * @param testCase
	 * @param outPutFolder
	 * @param userName
	 * @param password
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to execute a SoapUI project xml file 'pathToSoapUIXMLProjectFile' given:<br>
	 *             1. test suite<br>
	 *             2. test case<br>
	 *             3. a test results output folder<br>
	 *             4. uaername<br>
	 *             5. password<br>
	 */
	public void execute(String pathToSoapUIXMLProjectFile, String testSuite, String testCase, String outPutFolder, String userName, String password)
			throws Exception {
		setPathToSoapUIXMLProjectFile(pathToSoapUIXMLProjectFile);
		setTestSuite(testSuite);
		setTestCase(testCase);
		setEndPoint("");
		setOutPutFolder(outPutFolder);
		setUserName(userName);
		setPassword(password);
		execute();
	}

	/**
	 * @author Francesco Ferrante
	 * @param pathToSoapUIXMLProjectFile
	 * @param testSuite
	 * @param outPutFolder
	 * @param userName
	 * @param password
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to execute a SoapUI project xml file 'pathToSoapUIXMLProjectFile' given:<br>
	 *             1. test suite<br>
	 *             2. a test results output folder<br>
	 *             3. uaername<br>
	 *             4. password<br>
	 */
	public void executeTestSuite(String pathToSoapUIXMLProjectFile, String testSuite, String outPutFolder, String userName, String password)
			throws Exception {
		setPathToSoapUIXMLProjectFile(pathToSoapUIXMLProjectFile);
		setTestSuite(testSuite);
		setTestCase("");
		setEndPoint("");
		setOutPutFolder(outPutFolder);
		setUserName(userName);
		setPassword(password);
		execute();
	}

	/**
	 * @author Francesco Ferrante
	 * @param pathToSoapUIXMLProjectFile
	 * @param testSuite
	 * @param testCase
	 * @param outPutFolder
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to execute a SoapUI project xml file 'pathToSoapUIXMLProjectFile' given:<br>
	 *             1. test suite<br>
	 *             2. test case<br>
	 *             3. a test results output folder<br>
	 */
	public void execute(String pathToSoapUIXMLProjectFile, String testSuite, String testCase, String outPutFolder) throws Exception {
		setPathToSoapUIXMLProjectFile(pathToSoapUIXMLProjectFile);
		setTestSuite(testSuite);
		setTestCase(testCase);
		setEndPoint("");
		setOutPutFolder(outPutFolder);
		setUserName("");
		setPassword("");
		execute();
	}

	/**
	 * @author Francesco Ferrante
	 * @param pathToSoapUIXMLProjectFile
	 * @param testSuite
	 * @param outPutFolder
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to execute a SoapUI project xml file 'pathToSoapUIXMLProjectFile' given:<br>
	 *             1. test suite<br>
	 *             2. a test results output folder<br>
	 */
	public void executeTestSuite(String pathToSoapUIXMLProjectFile, String testSuite, String outPutFolder) throws Exception {
		setPathToSoapUIXMLProjectFile(pathToSoapUIXMLProjectFile);
		setTestSuite(testSuite);
		setTestCase("");
		setEndPoint("");
		setOutPutFolder(outPutFolder);
		setUserName("");
		setPassword("");
		execute();
	}

	/**
	 * @author Francesco Ferrante
	 * @param testResultsFile
	 * @return String
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this to method to retrieve the soap request from a given user-specified test results file.<br>
	 */
	public String getSoapRequest(String testResultsFile) throws Exception {
		return getSoapContent(testResultsFile, "soapenv:Envelope");
	}

	/**
	 * @author Francesco Ferrante
	 * @param testResultsFile
	 * @return String
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this to method to retrieve the soap response from a given user-specified test results file.<br>
	 */
	public String getSoapResponse(String testResultsFile) throws Exception {
		return getSoapContent(testResultsFile, "soap:Envelope");
	}

	/**
	 * @author Francesco Ferrante
	 * @param testResultsFile
	 * @param soapEnvelopeTag
	 * @return String
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             This method is a helper method for getSoapRequest() and getSoapResponse().<br>
	 */
	private String getSoapContent(String testResultsFile, String soapEnvelopeTag) throws Exception {
		String res = "";
		File file = new File(testResultsFile);
		if (!file.exists()) {
			throw new FileNotFoundException(testResultsFile);
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(testResultsFile));
			String contents = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				contents += line;
				contents += NEW_LINE;
			}
			// Extract the soap request from the contents
			if (!contents.isEmpty()) {
				int startIndex = contents.indexOf("<" + soapEnvelopeTag);
				if (startIndex >= 0) {
					String soapEnvelopeTerminationTag = "</" + soapEnvelopeTag + ">";
					int endIndex = contents.indexOf(soapEnvelopeTerminationTag) + soapEnvelopeTerminationTag.length();
					if (endIndex > startIndex) {
						res = contents.substring(startIndex, endIndex);
					}
				}
			}
		} catch (IOException e) {
		} finally {
			if (br != null) {
				br.close();
			}
		}
		return res;
	}

	/**
	 * @author Francesco Ferrante <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         This method uses the 'exec()' method of the 'Runtime' class to execute a<br>
	 *         an array of command line arguments obtained from method 'getCommandLneArguments'.<br>
	 * @throws IOException
	 */
	private void execute() throws Exception {
		String[] commandLineArguments = getCommandLneArguments();
		if (commandLineArguments != null) {
			Runtime rt = Runtime.getRuntime();
			InputStream is = null;
			InputStreamReader isr = null;
			BufferedReader br = null;
			try {
				Process p = rt.exec(commandLineArguments);
				if (p != null) {
					is = p.getInputStream();
					isr = new InputStreamReader(is);
					br = new BufferedReader(isr);

					String line = "";
					while (true) {
						line = br.readLine();
						if (line != null) {
							if (!line.trim().startsWith("]")) {
								System.out.println(line);
							} else {
								break;
							}
						} else {
							break;
						}
					}

					JFileTimeStamper.timeStampTestResultFiles(getOutPutFolder());
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					br.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (is != null) {
					is.close();
				}
			}
		}
	}
}