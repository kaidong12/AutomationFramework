package com.cisco.framework.core;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.Reporter;

import com.cisco.framework.core.Locators.LocatorType;
import com.cisco.framework.core.exceptions.FrameworkException;
import com.cisco.framework.utilities.logging.Log;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;

/**
 * @author Francesco Ferrante
 */
public abstract class WebApp {

	protected static Log			log									= null;
	protected static Browser		browser								= null;
	protected static WebDriver		driver								= null;
	protected static Selenium		selenium							= null;
	protected Map<String, String>	parameters							= null;

	protected static int			numSecondsToWaitForElementPresent	= 10;
	protected static int			numSecondsToWaitForPageToLoad		= 30;
	protected static int			numSecondsToWaitForFrameToLoad		= 30;
	private static boolean			enableHighlight						= false;

	private final int				SECOND								= 1000;
	protected final String			NEW_LINE							= System.getProperty("line.separator");

	// --- START CONSTRUCTOR SECTION
	// -----------------------------------------------------------------------------------------------------------
	public WebApp(Log log, Browser browser) {

		if (log == null) {
			throw new FrameworkException("WebApp", "log", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		if (browser == null) {
			throw new FrameworkException("WebApp", "browser", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		WebApp.log = log;
		WebApp.browser = browser;
		WebApp.driver = browser.getWebDriver();
		WebApp.selenium = browser.getSelenium();
		this.parameters = browser.getParameters();

	}

	// --- END CONSTRUCTOR SECTION
	// -----------------------------------------------------------------------------------------------------------

	// --- START PUBLIC METHODS SECTION
	// --------------------------------------------------------------------------------------------------------

	/**
	 * @param iTestContext
	 * @param methodName
	 * @param logExcludedTestMethodsAsWrnings
	 * @param logExcludedGroupsAsWarnings
	 * @return String
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to log all non-executed test methods and/or all excluded test methods if "logExcludedTestMethodsAsWrnings" is
	 *             "true"<br>
	 *             and/or all excluded groups if "logExcludedGroupsAsWarnings" is "true".<br>
	 */
	public void checkAllTestMethodsExecuted(ITestContext iTestContext, String methodName, boolean logExcludedTestMethodsAsWrnings,
			boolean logExcludedGroupsAsWarnings) {

		String newLine = System.getProperty("line.separator");

		if (iTestContext != null) {
			ITestNGMethod[] testIncludedNGMethods = iTestContext.getAllTestMethods();
			Collection<ITestNGMethod> testExcludedNGMethods = iTestContext.getExcludedMethods();
			String[] excludedGroups = iTestContext.getExcludedGroups();

			if ((testIncludedNGMethods != null) && (testExcludedNGMethods != null) && (excludedGroups != null)) {

				// Determine if any "included" test methods have not been executed.
				int includedTestMethodNotExecutedCounter = 0;
				if (testIncludedNGMethods.length > 0) {
					for (ITestNGMethod includedTestMethod : testIncludedNGMethods) {
						if (includedTestMethod.isTest()) {
							// TestNG will assign a unique string id to a test method that get's executed
							// and an empty string otherwise. This is if "getId()" returns an empty string
							// then the corresponding test method has not been executed.
							if (includedTestMethod.getId().isEmpty()) {
								// Increment "includedTestMethodNotExecutedCounter".
								includedTestMethodNotExecutedCounter++;
							}
						}
					}
				}

				if ((includedTestMethodNotExecutedCounter > 0) || ((testExcludedNGMethods.size() > 0) && logExcludedTestMethodsAsWrnings)
						|| ((excludedGroups.length > 0) && logExcludedGroupsAsWarnings)) {
					log.startTestExecution(methodName);

					// Log all non-executed test methods.
					if (includedTestMethodNotExecutedCounter > 0) {
						int counter = 0;
						for (ITestNGMethod includedTestMethod : testIncludedNGMethods) {
							if (includedTestMethod.isTest()) {
								if (includedTestMethod.getId().isEmpty()) {
									counter++;
									String errorMessage = "TEST METHOD: " + "(" + String.valueOf(counter) + "\\"
											+ String.valueOf(includedTestMethodNotExecutedCounter) + ") NOT EXECUTED";
									errorMessage += newLine;
									errorMessage += "POSSIBLE CAUSES:";
									errorMessage += newLine;
									errorMessage += "1. MISSING 'DATA PROVIDER' OR 'DATA PROVIDER ANNOTATION'";
									errorMessage += newLine;
									errorMessage += "2. MISSING 'TEST METHOD' OR 'TEST ANNOTATION'";
									log.comment(includedTestMethod.getMethodName(), includedTestMethod.getDescription(), errorMessage, Log.ERROR,
											Log.SCRIPT_ISSUE);
								}
							}
						}
					}

					// Log all excluded test methods.
					if (testExcludedNGMethods.size() > 0) {
						int counter = 0;
						for (ITestNGMethod excludedTestNGMethod : testExcludedNGMethods) {
							if (excludedTestNGMethod.isTest()) {
								counter++;
								String errorMessage = "TEST METHOD: " + "(" + String.valueOf(counter) + "\\"
										+ String.valueOf(testExcludedNGMethods.size()) + ") EXCLUDED";
								log.comment(excludedTestNGMethod.getMethodName(), excludedTestNGMethod.getDescription(), errorMessage, Log.WARN,
										Log.SCRIPT_ISSUE);
							}
						}
					}

					// Log all excluded groups.
					if (excludedGroups.length > 0) {
						int counter = 0;
						for (String excludedGroup : excludedGroups) {
							counter++;
							String errorMessage = "GROUP: " + "(" + String.valueOf(counter) + "\\" + String.valueOf(testExcludedNGMethods.size())
									+ ") EXCLUDED";
							log.comment(excludedGroup, "", errorMessage, Log.WARN, Log.SCRIPT_ISSUE);
						}
					}

					log.endTestExecution();
				}

			} else {
				log.startTestExecution(methodName);
				if (testIncludedNGMethods == null) {
					log.comment("checkAllTestMethodsExecuted", "INTERNAL 'testIncludedNGMethods' OBJECT", "MAKES REFERENCE TO A NULL POINTER",
							Log.ERROR, Log.SCRIPT_ISSUE);
				}
				if (testExcludedNGMethods == null) {
					log.comment("checkAllTestMethodsExecuted", "INTERNAL 'testExcludedNGMethods' OBJECT", "MAKES REFERENCE TO A NULL POINTER",
							Log.ERROR, Log.SCRIPT_ISSUE);
				}
				if (excludedGroups == null) {
					log.comment("checkAllTestMethodsExecuted", "INTERNAL 'excludedGroups' OBJECT", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR,
							Log.SCRIPT_ISSUE);
				}
				log.endTestExecution();
			}
		} else {
			log.startTestExecution(methodName);
			log.comment("checkAllTestMethodsExecuted", "INTERNAL 'ITestContext' OBJECT", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR,
					Log.SCRIPT_ISSUE);
			log.endTestExecution();
		}
	}

	/**
	 * @return the Browser <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to return the "Browser" object.<br>
	 */
	public Browser getBrowser() {
		return browser;
	}

	/**
	 * @param browser
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to set a new "Browser" object.<br>
	 */
	public void setBrowser(Browser browser) throws FrameworkException {
		if (browser == null) {
			throw new FrameworkException("setBrowser", "browser", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		WebApp.browser = browser;
	}

	public void enableElementHighlight() {
		enableHighlight = true;
	}

	/**
	 * @return int <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to get the current value for the<br>
	 *         number of seconds to wait for a frame in a web page to<br>
	 *         finish loading. The default value is 30 seconds.<br>
	 */
	public int getNumSecondsToWaitForFrameToLoad() {
		return numSecondsToWaitForFrameToLoad;
	}

	/**
	 * @param numSecondsToWaitForFrameToLoad
	 * @return void <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to set the number of seconds for a frame in a web page to finish loading.<br>
	 *         The default value is 30 seconds.<br>
	 */
	public void setNumSecondsToWaitForFrameToLoad(int numSecondsToWaitForFrameToLoad) {

		// Make sure the new timeout is valid. If not, log an error, but don't need to stop the execution.
		if (numSecondsToWaitForFrameToLoad > 0) {
			WebApp.numSecondsToWaitForFrameToLoad = numSecondsToWaitForFrameToLoad;
		} else {
			log.comment("setNumSecondsToWaitForFrameToLoad", String.valueOf(numSecondsToWaitForFrameToLoad), "INVALID VALUE - Value not set",
					Log.ERROR, Log.SCRIPT_ISSUE);
		}
	}

	/**
	 * @return int <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to get the current value for the<br>
	 *         number of seconds to wait for a web page to finish loading.<> The default value is 30 seconds.<br>
	 */
	public int getNumSecondsToWaitForPageToLoad() {
		return numSecondsToWaitForPageToLoad;
	}

	/**
	 * @param numSecondsToWaitForPageToLoad
	 * @return void <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to set the number of seconds for a web page to finish loading.<br>
	 *         The default value is 30 seconds.<br>
	 */
	public void setNumSecondsToWaitForPageToLoad(int numSecondsToWaitForPageToLoad) {
		log.startFunction("Change wait timeouts for page to load");

		// Make sure the new timeout is valid. If not, log an error, but don't need to stop the execution.
		if (numSecondsToWaitForPageToLoad > 0) {
			log.comment("Change wait timeouts for page to load to(s): " + numSecondsToWaitForPageToLoad);
			WebApp.numSecondsToWaitForPageToLoad = numSecondsToWaitForPageToLoad;
		} else {
			log.comment("setNumSecondsToWaitForPageToLoad", String.valueOf(numSecondsToWaitForPageToLoad), "INVALID VALUE - Value not set", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}

		log.endFunction();

	}

	/**
	 * @return int <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to get the current value for<br>
	 *         the number of seconds to wait for a web page element<br>
	 *         to be present. The default value is 10 seconds.<br>
	 */
	public int getNumSecondsToWaitForElementPresent() {
		return numSecondsToWaitForElementPresent;
	}

	/**
	 * @param numSecondsToWaitForElementPresent
	 * @return void <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to set the number of seconds to wait for an web page element to be present.<br>
	 *         The default value is 10 seconds.<br>
	 */
	public void setNumSecondsToWaitForElementPresent(int numSecondsToWaitForElementPresent) {
		log.startFunction("Change wait timeouts for element to present");

		// Make sure the new timeout is valid. If not, log an error, but don't need to stop the execution.
		if (numSecondsToWaitForElementPresent > 0) {
			log.comment("Change wait timeouts for element to present to(s): " + numSecondsToWaitForElementPresent);
			WebApp.numSecondsToWaitForElementPresent = numSecondsToWaitForElementPresent;
		} else {
			log.comment("setNumSecondsToWaitForElementPresent", String.valueOf(numSecondsToWaitForElementPresent), "INVALID VALUE - Value not set",
					Log.ERROR, Log.SCRIPT_ISSUE);
		}

		log.endFunction();

	}

	/**
	 * @param locatorType
	 *            object
	 * @param attribute
	 * @return String
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns the attribute value of "locatorType" given a valid attribute.<br>
	 */
	public String getAttribute(Locators.LocatorType locatorType, String attribute) throws FrameworkException {
		String res = "";

		try {

			// Make sure the element is present before attempting a Selenium call. If not present, throw an exception
			if (waitForElementPresentHelper(locatorType, true, numSecondsToWaitForElementPresent, "getAttribute")) {

				// If the element is not visible, log a warning, but still perform the Selenium call.
				if (!isElementVisible(locatorType)) {
					log.comment("getAttribute", locatorType.getString(), "ELEMENT NOT VISIBLE", Log.WARN, Log.FEATURE_CHANGE);
				}

				// Perform the Selenium call and log a debug entry.
				res = getWebElement(locatorType, "getAttribute").getAttribute(attribute);

			} else {
				// The element was not present, log an error entry of type Feature Change.
				throw new FrameworkException("getAttribute", locatorType.getString(),
						"ELEMENT WAS NOT PRESENT AFTER: " + "'" + String.valueOf(numSecondsToWaitForElementPresent) + "' SECONDS.", Log.ERROR,
						Log.FEATURE_CHANGE);
			}
		} catch (NullPointerException e) {
			throw new FrameworkException("getAttribute", "locatorType", "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException("getAttribute", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("getAttribute", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
		return res;
	}

	/**
	 * @param locatorType
	 *            object
	 * @param isInputField
	 * @return String
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns the text from either the "text" or "value" attribute of the element.<br>
	 */
	public String getElementText(Locators.LocatorType locatorType, boolean isInputField) throws FrameworkException {
		String res = "";

		try {

			// Make sure the element is present before attempting a Selenium call. If not present, throw an exception
			if (waitForElementPresentHelper(locatorType, true, numSecondsToWaitForElementPresent, "getElementText")) {

				// If the element is not visible, log a warning, but still perform the Selenium call.
				if (isElementVisible(locatorType) == false) {
					log.comment("getElementText", locatorType.getString(), "ELEMENT NOT VISIBLE", Log.WARN, Log.FEATURE_CHANGE);
				}

				// If the element is an input field, use the Selenium.getValue method. Otherwise, use the
				// Selenium.getText method.
				if (isInputField) {
					res = selenium.getValue(locatorType.getValue());
				} else {
					res = selenium.getText(locatorType.getValue());
				}

			} else {

				// The element was not present, log an error entry of type Feature Change.
				throw new FrameworkException("getElementText", locatorType.getString(),
						"ELEMENT WAS NOT PRESENT AFTER: " + "'" + String.valueOf(numSecondsToWaitForElementPresent) + "' SECONDS.", Log.ERROR,
						Log.FEATURE_CHANGE);
			}
		} catch (NullPointerException e) {
			throw new FrameworkException("getElementText", locatorType.getValue(), "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException("getElementText", locatorType.getValue(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("getElementText", locatorType.getValue(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
		return res;
	}

	/**
	 * @param locatorType
	 *            object
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if "locatorType" is present and "false" otherwise.<br>
	 */
	public boolean isElementPresent(Locators.LocatorType locatorType) throws FrameworkException {
		boolean res = false;

		try {
			// Perform the Selenium call, but don't log any debug entry.
			res = isElementPresentHelper(locatorType, "isElementPresent");
		} catch (NullPointerException e) {
			throw new FrameworkException("isElementPresent", "locatorType", "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException("isElementPresent", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("isElementPresent", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}

		return res;
	}

	/**
	 * @param locatorType
	 *            object
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if "locatorType" is visible and "false" otherwise.<br>
	 */
	public boolean isElementVisible(Locators.LocatorType locatorType) throws FrameworkException {
		boolean res = false;

		try {
			// Make sure the element is present before attempting a Selenium call. If not present, throw an exception
			if (waitForElementPresentHelper(locatorType, true, numSecondsToWaitForElementPresent, "isElementEnabled")) {
				WebElement element = getWebElement(locatorType, "isElementVisible");
				if (element != null) {
					res = getWebElement(locatorType, "isElementVisible").isDisplayed();
				}

			} else {
				// The element is not present, at the same time not visible. Return False without throwing an exception.
				// Special case: Will allow the validation of element being visible even if the element is not present.
				res = false;

			}
		} catch (NullPointerException e) {
			throw new FrameworkException("isElementVisible", "locatorType", "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException("isElementVisible", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("isElementVisible", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
		return res;
	}

	/**
	 * @param locatorType
	 *            object
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if "locatorType" is enabled and "false" otherwise.<br>
	 */
	public boolean isElementEnabled(Locators.LocatorType locatorType) throws FrameworkException {
		boolean res = false;

		try {

			// Make sure the element is present before attempting a Selenium call. If not present, throw an exception
			if (waitForElementPresentHelper(locatorType, true, numSecondsToWaitForElementPresent, "isElementEnabled")) {

				// If the element is not visible, log a warning, but still perform the Selenium call.
				if (!isElementVisible(locatorType)) {
					log.comment("isElementEnabled", locatorType.getString(), "ELEMENT NOT VISIBLE", Log.WARN, Log.FEATURE_CHANGE);
				}

				// Perform the Selenium call, but don't log any debug entry.
				res = getWebElement(locatorType, "isElementEnabled").isEnabled();

			} else {

				// The element was not present, log an error entry of type Feature Change.
				throw new FrameworkException("isElementEnabled", locatorType.getString(),
						"ELEMENT WAS NOT PRESENT AFTER: " + "'" + String.valueOf(numSecondsToWaitForElementPresent) + "' SECONDS.", Log.ERROR,
						Log.FEATURE_CHANGE);
			}
		} catch (NullPointerException e) {
			throw new FrameworkException("isElementEnabled", "locatorType", "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException("isElementEnabled", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("isElementEnabled", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
		return res;
	}

	/**
	 * @param locatorType
	 *            object
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             If the element cannot be checked/unchecked, an exception will be thrown.<br>
	 *             Returns "true" if "locatorType" is checked and "false" otherwise.<br>
	 */
	public boolean isElementChecked(Locators.LocatorType locatorType) throws FrameworkException {
		boolean res = false;

		try {

			// Make sure the element is present before attempting a Selenium call. If not present, throw an exception
			if (waitForElementPresentHelper(locatorType, true, numSecondsToWaitForElementPresent, "isElementChecked")) {

				// If the element is not visible, log a warning, but still perform the Selenium call.
				if (isElementVisible(locatorType) == false) {
					log.comment("isElementChecked", locatorType.getString(), "ELEMENT NOT VISIBLE", Log.WARN, Log.FEATURE_CHANGE);
				}

				// Perform the Selenium call, but don't log any debug entry.
				res = getWebElement(locatorType, "isElementChecked").isSelected();

			} else {

				// The element was not present, log an error entry of type Feature Change.
				throw new FrameworkException("isElementChecked", locatorType.getString(),
						"ELEMENT WAS NOT PRESENT AFTER: " + "'" + String.valueOf(numSecondsToWaitForElementPresent) + "' SECONDS.", Log.ERROR,
						Log.FEATURE_CHANGE);
			}
		} catch (NullPointerException e) {
			throw new FrameworkException("isElementChecked", "locatorType", "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException("isElementChecked", locatorType.getValue(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("isElementChecked", locatorType.getValue(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
		return res;
	}

	/**
	 * @param textToSearch
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if "textToSearch" is present in the web page and "false" otherwise.<br>
	 */
	public boolean isTextPresent(String textToSearch) throws FrameworkException {
		boolean res = false;

		try {

			// Perform the Selenium call, but don't log any debug entry.
			res = selenium.isTextPresent(textToSearch);
		} catch (NullPointerException e) {
			throw new FrameworkException("isTextPresent", "textToSearch", "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException("isTextPresent", "textToSearch", e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("isTextPresent", "textToSearch", e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
		return res;
	}

	/**
	 * @param arg1
	 *            generic object
	 * @param arg2
	 *            generic object
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if both "arg1" and "arg2" are equal and "false" otherwise.<br>
	 */
	public boolean isEqual(Object arg1, Object arg2) throws FrameworkException {
		boolean res = false;

		try {
			// Perform the Selenium call, but don't log any debug entry.
			res = arg1.equals(arg2);
		} catch (NullPointerException e) {
			// Determine which parameter was null. Report only the first found null parameter.
			String nullArg = arg1 == null ? "arg1" : "arg2";
			throw new FrameworkException("select", nullArg, "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		return res;
	}

	/**
	 * @param locatorType
	 *            object
	 * @param textToSearch
	 * @param isInputField
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             If the element is an input field, set "isInputField" to "true".<br>
	 *             Returns "true" if "textToSearch" is present in the element "locatorType" and "false" otherwise.<br>
	 */
	public boolean isTextInElement(Locators.LocatorType locatorType, String textToSearch, boolean isInputField) throws FrameworkException {
		boolean res = false;

		try {

			// Make sure the element is present before attempting a Selenium call. If not present, throw an exception
			if (waitForElementPresentHelper(locatorType, true, numSecondsToWaitForElementPresent, "isTextInElement")) {

				// If the element is not visible, log a warning, but still perform the Selenium call.
				if (!isElementVisible(locatorType)) {
					log.comment("isTextInElement", locatorType.getString(), "ELEMENT NOT VISIBLE", Log.WARN, Log.FEATURE_CHANGE);
				}

				// If the element is an input field, use the Selenium.getValue method. Otherwise, use the
				// Selenium.getText method.
				if (isInputField) {
					res = selenium.getValue(locatorType.getValue()).contains(textToSearch);
				} else {
					res = selenium.getText(locatorType.getValue()).contains(textToSearch);
				}

			} else {

				// The element was not present, log an error entry of type Feature Change.
				throw new FrameworkException("isTextInElement", locatorType.getString(),
						"ELEMENT WAS NOT PRESENT AFTER: " + "'" + String.valueOf(numSecondsToWaitForElementPresent) + "' SECONDS.", Log.ERROR,
						Log.FEATURE_CHANGE);
			}
		} catch (NullPointerException e) {
			String locParameter = locatorType == null ? "locatorType" : "textToSearch";
			throw new FrameworkException("isTextInElement", locParameter, "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException("isTextInElement", locatorType.getValue(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("isTextInElement", locatorType.getValue(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
		return res;
	}

	/**
	 * @param locatorType
	 *            object
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if "locatorType" is present within the default number of seconds and "false" otherwise.<br>
	 */
	public boolean waitForElementPresent(Locators.LocatorType locatorType) throws FrameworkException {
		return waitForElementPresentHelper(locatorType, true, numSecondsToWaitForElementPresent, "waitForElementPresent");
	}

	/**
	 * @param locatorType
	 *            object
	 * @param maxDelayInSeconds
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if element "locatorType" is present within "maxDelayInSeconds" seconds and "false" otherwise.<br>
	 */
	public boolean waitForElementPresent(Locators.LocatorType locatorType, int maxDelayInSeconds) throws FrameworkException {
		return waitForElementPresentHelper(locatorType, true, maxDelayInSeconds, "waitForElementPresent");
	}

	/**
	 * @param locatorType
	 *            object
	 * @throws FrameworkException
	 * @return boolean <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns "true if element defined "locatorType" object is not present within the default number of seconds and " false" otherwise.<br>
	 */
	public boolean waitForElementVanish(Locators.LocatorType locatorType) throws FrameworkException {
		return waitForElementPresentHelper(locatorType, false, numSecondsToWaitForElementPresent, "waitForElementNotPresent");
	}

	/**
	 * @param locatorType
	 *            object
	 * @param maxDelayInSeconds
	 * @throws FrameworkException
	 * @return boolean <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns "true if element "locatorType" is not present within "maxDelayInSeconds" seconds and "false" otherwise.<br>
	 */
	public boolean waitForElementNotPresent(Locators.LocatorType locatorType, int maxDelayInSeconds) throws FrameworkException {
		return waitForElementPresentHelper(locatorType, false, maxDelayInSeconds, "waitForElementNotPresent");
	}

	/**
	 * @throws FrameworkException
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if the page loaded before the maximum timeout is reached, and "false" otherwise.<br>
	 */
	public boolean waitForPageToLoad() throws FrameworkException {
		return waitForPageToLoadHelper(numSecondsToWaitForPageToLoad, "waitForPageToLoad");
	}

	/**
	 * @param maxDelayInSeconds
	 * @return true/false
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if the page loaded before the maximum timeout is reached, and "false" otherwise.<br>
	 */
	public boolean waitForPageToLoad(int maxDelayInSeconds) throws FrameworkException {
		return waitForPageToLoadHelper(maxDelayInSeconds, "waitForPageToLoad");
	}

	/**
	 * @param frameAddress
	 * @throws FrameworkException
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if the frame loaded before the maximum timeout is reached, and "false" otherwise.<br>
	 */
	public boolean waitForFrameToLoad(String frameAddress) throws FrameworkException {
		return waitForFrameToLoadHelper(frameAddress, numSecondsToWaitForFrameToLoad, "waitForFrameToLoad");
	}

	/**
	 * @param frameAddress
	 * @param maxDelayInSeconds
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if the page loaded before the maximum timeout is reached, and "false" otherwise.<br>
	 */
	public boolean waitForFrameToLoad(String frameAddress, int maxDelayInSeconds) throws FrameworkException {
		return waitForFrameToLoadHelper(frameAddress, maxDelayInSeconds, "waitForFrameToLoad");
	}

	/**
	 * @param numberOfSecondsToWait
	 * @throws FrameworkException
	 * @return void <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to wait for a user-sepcified number of seconds.<br>
	 */
	public void waitFor(int numberOfSecondsToWait) throws FrameworkException {
		try {

			// Make sure the timeout is valid. If not, throw an execution.
			if (numberOfSecondsToWait > 0) {
				java.lang.Thread.sleep(numberOfSecondsToWait * SECOND);
			} else {
				throw new FrameworkException("waitFor", String.valueOf(numberOfSecondsToWait), "INVALID VALUE - Must be greater than zero", Log.ERROR,
						Log.SCRIPT_ISSUE);
			}

		} catch (InterruptedException e) {
			throw new FrameworkException("waitFor", "numberOfSecondsToWait", e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
	}

	/**
	 * @param locatorType
	 *            object
	 * @param maxDelayInSeconds
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if "locatorType" is visible within "maxDelayInSeconds" seconds and "false" otherwise. <br>
	 */
	public boolean waitForElementVisible(Locators.LocatorType locatorType, int maxDelayInSeconds) throws FrameworkException {
		return waitForElementVisibleHelper(locatorType, true, maxDelayInSeconds, "waitForElementVisible");
	}

	/**
	 * @param locatorType
	 *            object
	 * @param maxDelayInSeconds
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if "locatorType" is not visible within "maxDelayInSeconds" seconds and "false" if it is visible.<br>
	 */
	public boolean waitForElementNotVisible(Locators.LocatorType locatorType, int maxDelayInSeconds) throws FrameworkException {
		return waitForElementVisibleHelper(locatorType, false, maxDelayInSeconds, "waitForElementNotVisible");
	}

	/**
	 * @param textPresent
	 * @param maxDelayInSeconds
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if "locatorType" is visible within "maxDelayInSeconds" seconds and "false" otherwise. <br>
	 */
	public boolean waitForTextPresent(String textPresent, int maxDelayInSeconds) throws FrameworkException {
		return waitForTextPresentHelper(textPresent, true, maxDelayInSeconds, "waitForTextPresent");
	}

	/**
	 * @param textPresent
	 * @param maxDelayInSeconds
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if "locatorType" is not visible within "maxDelayInSeconds" seconds and "false" if it is visible.<br>
	 */
	public boolean waitForTextNotPresent(String textPresent, int maxDelayInSeconds) throws FrameworkException {
		return waitForTextPresentHelper(textPresent, false, maxDelayInSeconds, "waitForTextNotPresent");
	}

	/**
	 * @param locatorType
	 *            object
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Clicks on "locatorType" but assumes there are no page that will load.<br>
	 */
	public void click(Locators.LocatorType locatorType) throws FrameworkException {
		clickHelper(locatorType);
	}

	/**
	 * @param locatorType
	 *            object
	 * @param maxDelayForPageToLoadInSeconds
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Clicks on "locatorType" and waits for the page to load.<br>
	 */
	public void click(Locators.LocatorType locatorType, int maxDelayForPageToLoadInSeconds) throws FrameworkException {
		clickHelper(locatorType);
		waitForPageToLoadHelper(maxDelayForPageToLoadInSeconds, "click");
	}

	// /**
	// * @param locatorType
	// * object
	// * @return void
	// * @throws FrameworkException
	// * <br>
	// * <br>
	// * USAGE:<br>
	// * <br>
	// * <p>
	// * Double clicks on "locatorType" but assumes there are no page that will load.<br>
	// */
	// public void doubleClick(Locators.LocatorType locatorType) throws FrameworkException {
	// doubleClickHelper(locatorType);
	// }

	// /**
	// * @param locatorType
	// * object
	// * @param maxDelayForPageToLoadInSeconds
	// * @return void
	// * @throws FrameworkException
	// * <br>
	// * <br>
	// * USAGE:<br>
	// * <br>
	// * <p>
	// * Double clicks on "locatorType" and waits for the page to load.<br>
	// */
	// public void doubleClick(Locators.LocatorType locatorType, int maxDelayForPageToLoadInSeconds)
	// throws FrameworkException {
	// doubleClickHelper(locatorType);
	// waitForPageToLoadHelper(maxDelayForPageToLoadInSeconds, "doubleClick");
	// }

	/**
	 * @param locatorType
	 *            object
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Right clicks on "locatorType" but assumes there are no page that will load.<br>
	 */
	public void contextMenuClick(Locators.LocatorType locatorType) throws FrameworkException {
		contextMenuClickHelper(locatorType);
	}

	/**
	 * @param locatorType
	 *            object
	 * @param maxDelayForPageToLoadInSeconds
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Right clicks on "locatorType" and waits for the page to load.<br>
	 */
	public void contextMenuClick(Locators.LocatorType locatorType, int maxDelayForPageToLoadInSeconds) throws FrameworkException {
		contextMenuClickHelper(locatorType);
		waitForPageToLoadHelper(maxDelayForPageToLoadInSeconds, "contextMenuClick");
	}

	/**
	 * @param locatorType
	 *            object
	 * @param options
	 *            ...
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Selects one or more options in "locatorType" but assumes there are no page that will load.<br>
	 */
	public void select(Locators.LocatorType locatorType, String... options) throws FrameworkException {
		selectHelper(locatorType, options);
	}

	/**
	 * @param locatorType
	 *            object
	 * @param maxDelayForPageToLoadInSeconds
	 * @param options
	 *            ...
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Selects one or more options in "locatorType" and waits for the page to load.<br>
	 */
	public void select(Locators.LocatorType locatorType, int maxDelayForPageToLoadInSeconds, String... options) throws FrameworkException {
		selectHelper(locatorType, options);
		waitForPageToLoadHelper(maxDelayForPageToLoadInSeconds, "select");
	}

	/**
	 * @param locatorType
	 *            object
	 * @param option
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Selects one option in "locatorType" using the item index but assumes there are no page that will load.<br>
	 *             Item index is zero-based, meaning the first item has index zero.<br>
	 */
	public void select(Locators.LocatorType locatorType, int option) throws FrameworkException {
		selectHelper(locatorType, option);
	}

	/**
	 * @param locatorType
	 *            object
	 * @param maxDelayForPageToLoadInSeconds
	 * @param option
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Selects one option in "locatorType" using the item index and waits for the page to load.<br>
	 *             Item index is zero-based, meaning the first item has index zero.<br>
	 */
	public void select(Locators.LocatorType locatorType, int maxDelayForPageToLoadInSeconds, int option) throws FrameworkException {
		selectHelper(locatorType, option);
		waitForPageToLoadHelper(maxDelayForPageToLoadInSeconds, "select");
	}

	/**
	 * @param locatorType
	 *            object
	 * @param textToSet
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Sets the text of an element using the "locatorType" but assumes there are no page that will load.<br>
	 */
	public void setText(Locators.LocatorType locatorType, String textToSet) throws FrameworkException {
		setTextHelper(locatorType, textToSet);
	}

	/**
	 * @param locatorType
	 *            object
	 * @param textToSet
	 * @param maxDelayForPageToLoadInSeconds
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Sets the text of an element using the "locatorType" and waits for the page to load.<br>
	 */
	public void setText(Locators.LocatorType locatorType, String textToSet, int maxDelayForPageToLoadInSeconds) throws FrameworkException {
		setTextHelper(locatorType, textToSet);
		waitForPageToLoadHelper(maxDelayForPageToLoadInSeconds, "setText");
	}

	/**
	 * @param locatorType
	 *            object
	 * @param setToChecked
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Sets a checkbox element to checked/unchecked using the "locatorType" but assumes there are no page that will load.<br>
	 */
	public void setCheck(Locators.LocatorType locatorType, boolean setToChecked) throws FrameworkException {
		setCheckHelper(locatorType, setToChecked);
	}

	/**
	 * @param locatorType
	 *            object
	 * @param setToChecked
	 * @param maxDelayForPageToLoadInSeconds
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Sets a checkbox element to checked/unchecked using the "locatorType" and waits for the page to load. <br>
	 */
	public void setCheck(Locators.LocatorType locatorType, boolean setToChecked, int maxDelayForPageToLoadInSeconds) throws FrameworkException {
		setCheckHelper(locatorType, setToChecked);
		waitForPageToLoadHelper(maxDelayForPageToLoadInSeconds, "setCheck");
	}

	/**
	 * @param locatorType
	 *            object
	 * @param option
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Removes the selection of a single option from an element using the "locatorType" but assumes there are no page that will load.<br>
	 */
	public void removeSelection(Locators.LocatorType locatorType, String option) throws FrameworkException {
		removeSelectionHelper(locatorType, option);
	}

	/**
	 * @param locatorType
	 *            object
	 * @param option
	 * @param maxDelayForPageToLoadInSeconds
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Removes the selection of a single option from an element using the "locatorType" and waits for the page to load.<br>
	 */
	public void removeSelection(Locators.LocatorType locatorType, String option, int maxDelayForPageToLoadInSeconds) throws FrameworkException {
		removeSelectionHelper(locatorType, option);
		waitForPageToLoadHelper(maxDelayForPageToLoadInSeconds, "removeSelection");
	}

	/**
	 * @param locatorType
	 *            object
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Removes all selected options from an element using the "locatorType" but assumes there are no page that will load.<br>
	 */
	public void removeAllSelections(Locators.LocatorType locatorType) throws FrameworkException {
		removeAllSelectionsHelper(locatorType);
	}

	/**
	 * @param locatorType
	 *            object
	 * @param maxDelayForPageToLoadInSeconds
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Removes all selected options from an element using the "locatorType" and waits for the page to load. <br>
	 */
	public void removeAllSelections(Locators.LocatorType locatorType, int maxDelayForPageToLoadInSeconds) throws FrameworkException {
		removeAllSelectionsHelper(locatorType);
		waitForPageToLoadHelper(maxDelayForPageToLoadInSeconds, "removeAllSelections");
	}

	/**
	 * @param ageIncrement
	 * @param dateFormat
	 * @return String
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Generates a datetime based on a factor from the current time.<br>
	 *             1) Indicate whether the date is to be prior to or after today (symbol '+' or '-')<br>
	 *             2) Indicate the number to add/substract from today<br>
	 *             3) Indicate the unit by which the number will add/substract from today<br>
	 *             Starting from 'Year', the symbols are: Y, M, D, H, N, S<br>
	 *             <br>
	 *             Examples:<br>
	 *             "-1D" removed 1 day to the current date and time<br>
	 *             "+2H" adds 2 hours to the current date and time<br>
	 *             <br>
	 *             Date and Time formats are using Java formats:<br>
	 *             y: year<br>
	 *             M: month<br>
	 *             d: day<br>
	 *             H: hour (use HH for 24H format)<br>
	 *             m: minute<br>
	 *             s: second<br>
	 */
	public String getAgeDate(String ageIncrement, String dateFormat) throws FrameworkException {
		Calendar agedDate = Calendar.getInstance();
		DateFormat AgedDateFormat = new SimpleDateFormat(dateFormat);
		int dateIncrement = 0;
		String absIncrement = "";
		String incrementUnit = "";

		// Get the absolute date increment. If the format is respected, the first and last character must be removed.
		absIncrement = ageIncrement.substring(1, ageIncrement.length() - 1);
		dateIncrement = Integer.valueOf(absIncrement);

		// If the first character of the age increment is '-', multiply the dateIncrement by -1.
		if (ageIncrement.subSequence(0, 1).equals("-")) {
			dateIncrement *= -1;
		}

		// Get the increment unit, which is the last character of the ageIncrement.
		incrementUnit = ageIncrement.substring(ageIncrement.length() - 1, ageIncrement.length()).toUpperCase();

		// If unit is 'Y', increment in years
		if (incrementUnit.equals("Y")) {
			agedDate.add(Calendar.YEAR, dateIncrement);
		} else if (incrementUnit.equals("M")) {
			agedDate.add(Calendar.MONTH, dateIncrement);
		} else if (incrementUnit.equals("D")) {
			agedDate.add(Calendar.DATE, dateIncrement);
		} else if (incrementUnit.equals("H")) {
			agedDate.add(Calendar.HOUR, dateIncrement);
		} else if (incrementUnit.equals("N")) {
			agedDate.add(Calendar.MINUTE, dateIncrement);
		} else if (incrementUnit.equals("S")) {
			agedDate.add(Calendar.SECOND, dateIncrement);
		}

		// Return the aged date with the desired format.
		return AgedDateFormat.format(agedDate.getTime());
	}

	/**
	 * @return String
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns the workspace path which is different per workstation.<br>
	 *             This path is useful when a relative path cannot be used.<br>
	 *             <br>
	 *             Code example:<br>
	 *             String puttyPath = aut.getWorkspaceProjectRootPath() + parameters.get("PuttyPath");<br>
	 *             <br>
	 *             where 'PuttyPath' is a config value found in the .xml TestSuite file.<br>
	 */
	public String getWorkspaceProjectRootPath() throws FrameworkException {
		File file = new File("");

		return file.getAbsolutePath();
	}

	public void acceptAlert() {
		if (isAlertPresent()) {
			driver.switchTo().alert().accept();
		}
	}

	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException Ex) {
			return false;
		}
	}

	public void acceptAlertIfFound() {
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			System.out.println("no alert found.");
		}
	}

	public Alert getAlert() {
		return driver.switchTo().alert();
	}

	public void cancleAlert() {
		driver.switchTo().alert().dismiss();
	}

	public String getAlertText() {
		return driver.switchTo().alert().getText();
	}

	// --- END PUBLIC METHODS SECTION
	// ----------------------------------------------------------------------------------------------------------

	// --- START PRIVATE METHODS SECTION
	// -------------------------------------------------------------------------------------------------------
	/**
	 * @param locatorType
	 *            object
	 * @param isPresent
	 * @param maxDelayInSeconds
	 * @param callingMethodName
	 *            : The method that called this method. For logging purposes.
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used by the following methods:<br>
	 *             1. waitForElementPresent(locatorType)<br>
	 *             2. waitForElementPresent(locatorType,maxDelayInSeconds)<br>
	 *             3. waitForElementNotPresent(locatorType)<br>
	 *             4. waitForElementNotPresent(locatorType,maxDelayInSeconds)<br>
	 */
	protected boolean waitForElementPresentHelper(final Locators.LocatorType locatorType, final boolean isPresent, int maxDelayInSeconds,
			final String callingMethodName) throws FrameworkException {
		boolean res = true;

		try {
			// log.comment("WaitForElementPresent", locatorType.getString(), "Wait timeout in(s): " + maxDelayInSeconds, Log.DEBUG, Log.SCRIPT_ISSUE);

			// Make sure the timeout is valid. If not, throw an execution.
			if (maxDelayInSeconds < 1) {
				res = false;
				throw new FrameworkException(callingMethodName, String.valueOf(maxDelayInSeconds), "INVALID VALUE - Must be greater than zero",
						Log.ERROR, Log.FEATURE_CHANGE);
			}

			// Loop until the element equals "isPresent" or the timeout is reached.
			(new WebDriverWait(driver, maxDelayInSeconds)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return isElementPresentHelper(locatorType, callingMethodName) == isPresent;
				}
			});

		} catch (NullPointerException e) {
			throw new FrameworkException(callingMethodName, "locatorType", "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException(callingMethodName, locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			res = false;
		}
		return res;
	}

	/**
	 * @param maxDelayInSeconds
	 * @param callingMethodName
	 * @return boolean
	 * @throws RuntimeException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used by the following waitForPageToLoad() method calls:<br>
	 *             1. waitForPageToLoad()<br>
	 *             2. waitForPageToLoad(maxDelayInSeconds)<br>
	 *             3. click 4. doucleClick 5. select 6. setText
	 */
	protected boolean waitForPageToLoadHelper(int maxDelayInSeconds, String callingMethodName) throws FrameworkException {
		boolean res = false;

		try {

			// Make sure the timeout is valid. If not, throw an execution.
			if (maxDelayInSeconds < 1) {
				throw new FrameworkException(callingMethodName, String.valueOf(maxDelayInSeconds), "INVALID VALUE - Must be greater than zero",
						Log.ERROR, Log.FEATURE_CHANGE);
			}

			selenium.waitForPageToLoad(String.valueOf(maxDelayInSeconds * SECOND));
			res = true;

			// return false if the timeout is reached.
		} catch (SeleniumException e) {
			throw new FrameworkException(callingMethodName, "maxDelayInSeconds", e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			res = false;
		}
		return res;
	}

	/**
	 * @param frameAddress
	 * @param maxDelayInSeconds
	 * @param callingMethodName
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used by the following waitForFrameToLoad() method calls:<br>
	 *             1. waitForFrameToLoad(frameAddress)<br>
	 *             2. waitForFrameToLoad(frameAddress, maxDelayInSeconds)<br>
	 */
	private boolean waitForFrameToLoadHelper(String frameAddress, int maxDelayInSeconds, String callingMethodName) throws FrameworkException {
		boolean res = false;

		try {

			// Make sure the timeout is valid. If not, throw an execution.
			if (maxDelayInSeconds < 1) {
				throw new FrameworkException(callingMethodName, String.valueOf(maxDelayInSeconds), "INVALID VALUE - Must be greater than zero",
						Log.ERROR, Log.FEATURE_CHANGE);
			}

			selenium.waitForFrameToLoad(frameAddress, String.valueOf(maxDelayInSeconds * SECOND));
			res = true;
			// return false if the timeout is reached.
		} catch (SeleniumException e) {
			throw new FrameworkException(callingMethodName, "maxDelayInSeconds", e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			res = false;
		}
		return res;
	}

	/**
	 * @param locatorType
	 *            object
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used by the following click() method calls:<br>
	 *             1. click(locatorType)<br>
	 *             2. click(locatorType, maxDelayInSeconds)<br>
	 */
	private void clickHelper(Locators.LocatorType locatorType) throws FrameworkException {
		try {

			if (isElementVisible(locatorType)) {
				// Perform the Selenium call and log a debug entry.
				WebElement e = getWebElement(locatorType, "click");
				e.click();
				log.comment("click", locatorType.getString(), "", Log.DEBUG, Log.SCRIPT_ISSUE);

			} else {
				// If the element is not visible, log a warning, but still perform the Selenium call.
				log.comment("click", locatorType.getString(), "ELEMENT NOT VISIBLE OR NOT PRESENT ON PAGE!", Log.WARN, Log.FEATURE_CHANGE);
				WebElement e = getWebElement(locatorType, "click");
				if (e != null) {
					clickHiddenElement(e);
				}

			}

		} catch (NullPointerException e) {
			// Handle any other type of Error and log an appropriate Error entry.
			throw new FrameworkException("click", "locatorType", "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException("click", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("click", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @param locatorType
	 *            object
	 * @param options
	 *            ...
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used by the following select() method calls:<br>
	 *             1. select(locatorType, options)<br>
	 *             2. select(locatorType, options, maxDelayInSeconds)<br>
	 */
	private void selectHelper(Locators.LocatorType locatorType, String... options) throws FrameworkException {
		try {

			if (isElementVisible(locatorType)) {

				// Loop through all options and log a debug entry for each selected option.
				for (int optionCnt = 0; optionCnt < options.length; optionCnt++) {
					// The first option must use the "select" method to override any selection.
					if (optionCnt == 0) {
						// selenium.select(locatorType.getValue(), options[optionCnt]);
						WebElement e = this.getWebElement(locatorType, "select");
						Select dropDownBox = new Select(e);
						dropDownBox.selectByVisibleText(options[optionCnt]);

						// Use "addSelection" to select the additional options.
					} else {
						selenium.addSelection(locatorType.getValue(), options[optionCnt]);
					}

					// Log a Debug entry for each selected option.
					log.comment("select", locatorType.getString(), options[optionCnt], Log.DEBUG, Log.SCRIPT_ISSUE);

				}

			} else {
				// If the element is not visible, log a warning
				log.comment("select", locatorType.getString(), "ELEMENT NOT VISIBLE OR NOT PRESENT ON PAGE!", Log.WARN, Log.FEATURE_CHANGE);

			}

		} catch (NullPointerException e) {
			String nullArg = locatorType == null ? "locatorType" : "options";
			throw new FrameworkException("select", nullArg, "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException("select", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("select", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
	}

	/**
	 * @param locatorType
	 *            object
	 * @param option
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used by the following select() method calls:<br>
	 *             1. select(locatorType, option)<br>
	 *             2. select(locatorType, option, maxDelayInSeconds)<br>
	 */
	private void selectHelper(Locators.LocatorType locatorType, int option) throws FrameworkException {
		try {

			// Make sure the element is present before attempting a Selenium call. If not present, throw an exception
			if (waitForElementPresentHelper(locatorType, true, getNumSecondsToWaitForElementPresent(), "select")) {

				// If the element is not visible, log a warning, but still perform the Selenium call.
				if (!isElementVisible(locatorType)) {
					log.comment("select", locatorType.getString(), "ELEMENT NOT VISIBLE", Log.WARN, Log.FEATURE_CHANGE);
				}

				Select dropDownBox = new Select(getWebElement(locatorType, "select"));
				dropDownBox.selectByIndex(option);

				// Log a Debug entry for each selected option.
				log.comment("select", locatorType.getString(), "Index: " + Integer.toString(option), Log.DEBUG, Log.SCRIPT_ISSUE);

			} else {
				// The element was not present, log an error entry of type Feature Change.
				throw new FrameworkException("select", locatorType.getString(),
						"ELEMENT WAS NOT PRESENT AFTER: " + "'" + String.valueOf(numSecondsToWaitForElementPresent) + "' SECONDS.", Log.ERROR,
						Log.FEATURE_CHANGE);
			}
		} catch (NullPointerException e) {
			String nullArg = locatorType == null ? "locatorType" : "option";
			throw new FrameworkException("select", nullArg, "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException("select", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("select", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
	}

	// /**
	// * @param locatorType
	// * object
	// * @return void
	// * @throws FrameworkException
	// * <br>
	// * <br>
	// * USAGE:<br>
	// * <br>
	// * <p>
	// * Helper method used by the following doubleClick() method calls:<br>
	// * 1. doubleClick(locatorType)<br>
	// * 2. doubleClick(locatorType, maxDelayInSeconds)<br>
	// */
	// private void doubleClickHelper(Locators.LocatorType locatorType) throws FrameworkException {
	// try {
	// // Make sure the element is present before attempting a Selenium call. If not present, throw an exception
	// if (waitForElementPresentHelper(locatorType, true, numSecondsToWaitForElementPresent, "doubleClick")) {
	//
	// // If the element is not visible, log a warning, but still perform the Selenium call.
	// if (!isElementVisible(locatorType)) {
	// log.comment("doubleClick", locatorType.getString(), "ELEMENT NOT VISIBLE", Log.WARN,
	// Log.FEATURE_CHANGE);
	// }
	//
	// // Perform the Selenium call and log a debug entry.
	// ((HasInputDevices) driver).getMouse().doubleClick(
	// ((Locatable) getWebElement(locatorType, "doubleClick")).getCoordinates());
	//
	// log.comment("doubleClick", locatorType.getString(), "", Log.DEBUG, Log.SCRIPT_ISSUE);
	//
	// } else {
	// // The element was not present, log an error entry of type Feature Change.
	// throw new FrameworkException("doubleClick", locatorType.getString(), "ELEMENT WAS NOT PRESENT AFTER: "
	// + "'" + String.valueOf(numSecondsToWaitForElementPresent) + "' SECONDS.", Log.ERROR,
	// Log.FEATURE_CHANGE);
	// }
	//
	// // Handle any other type of Error and log an appropriate Error entry.
	// } catch (NullPointerException e) {
	// throw new FrameworkException("doubleClick", "locatorType", "ENCOUNTERED NULL POINTER", Log.ERROR,
	// Log.SCRIPT_ISSUE);
	// } catch (SeleniumException e) {
	// throw new FrameworkException("doubleClick", locatorType.getString(), e.getMessage(), Log.ERROR,
	// Log.SCRIPT_ISSUE);
	// } catch (WebDriverException e) {
	// throw new FrameworkException("doubleClick", locatorType.getString(), e.getMessage(), Log.ERROR,
	// Log.SCRIPT_ISSUE);
	// }
	// }

	/**
	 * @param locatorType
	 *            object
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used by the following doubleClick() method calls:<br>
	 *             1. contextMenuClick(locatorType)<br>
	 *             2. contextMenuClick(locatorType, maxDelayInSeconds)<br>
	 */
	void contextMenuClickHelper(Locators.LocatorType locatorType) throws FrameworkException {
		try {
			// Make sure the element is present before attempting a Selenium call. If not present, throw an exception
			if (waitForElementPresentHelper(locatorType, true, numSecondsToWaitForElementPresent, "contextMenuClickHelper")) {

				// If the element is not visible, log a warning, but still perform the Selenium call.
				if (!isElementVisible(locatorType)) {
					log.comment("contextMenuClickHelper", locatorType.getString(), "ELEMENT NOT VISIBLE", Log.WARN, Log.FEATURE_CHANGE);
				}

				Actions rightClick = new Actions(driver);
				rightClick.contextClick((getWebElement(locatorType, "contextMenuClickHelper")));
				rightClick.perform();
				// Perform the Selenium call and log a debug entry.
				// ((HasInputDevices)
				// driver).getMouse().doubleClick(((Locatable)getWebElement(locatorType,"doubleClick")).getCoordinates());

				log.comment("contextMenuClickHelper", locatorType.getString(), "", Log.DEBUG, Log.SCRIPT_ISSUE);

			} else {
				// The element was not present, log an error entry of type Feature Change.
				throw new FrameworkException("contextMenuClickHelper", locatorType.getString(),
						"ELEMENT WAS NOT PRESENT AFTER: " + "'" + String.valueOf(numSecondsToWaitForElementPresent) + "' SECONDS.", Log.ERROR,
						Log.FEATURE_CHANGE);
			}

			// Handle any other type of Error and log an appropriate Error entry.
		} catch (NullPointerException e) {
			throw new FrameworkException("contextMenuClickHelper", "locatorType", "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException("contextMenuClickHelper", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("contextMenuClickHelper", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
	}

	/**
	 * @param locatorType
	 *            object
	 * @param setToChecked
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used by the following doubleClick() method calls:<br>
	 *             1. setCheck(locatorType, setToChecked)<br>
	 *             2. setCheck(locatorType, setToChecked, maxDelayInSeconds)<br>
	 */
	private void setCheckHelper(Locators.LocatorType locatorType, boolean setToChecked) throws FrameworkException {
		try {

			// If the element is not visible, log a warning, but still perform the Selenium call.
			if (!isElementVisible(locatorType)) {
				log.comment("setCheck", locatorType.getString(), "ELEMENT NOT VISIBLE OR NOT PRESENT ON PAGE!", Log.WARN, Log.FEATURE_CHANGE);
			}

			// Perform the Selenium call and log a debug entry according to the "setToChecked" parameter.
			if (setToChecked) {
				selenium.check(locatorType.getValue());
				log.comment("setCheck", locatorType.getString(), "Checked", Log.DEBUG, Log.SCRIPT_ISSUE);
			} else {
				selenium.uncheck(locatorType.getValue());
				log.comment("setCheck", locatorType.getString(), "Unchecked", Log.DEBUG, Log.SCRIPT_ISSUE);
			}

		} catch (NullPointerException e) {
			// Handle any other type of Error and log an appropriate Error entry.
			throw new FrameworkException("setCheck", "locatorType", "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException("setCheck", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("setCheck", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
	}

	/**
	 * @param locatorType
	 *            object
	 * @param textToSet
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used by the following doubleClick() method calls:<br>
	 *             1. setText(locatorType, textToSet)<br>
	 *             2. setText(locatorType, textToSet, maxDelayInSeconds)<br>
	 */
	private void setTextHelper(Locators.LocatorType locatorType, String textToSet) throws FrameworkException {
		try {

			if (isElementVisible(locatorType)) {
				// Perform the Selenium call and log a debug entry.
				WebElement e = getWebElement(locatorType, "setText");
				e.clear();
				e.sendKeys(textToSet);

			} else {
				// If the element is not visible, log a warning, but still perform the Selenium call.
				log.comment("setText", locatorType.getString(), "ELEMENT NOT VISIBLE OR NOT PRESENT ON PAGE!", Log.WARN, Log.FEATURE_CHANGE);
				// Perform the Selenium call and log a debug entry.
				WebElement e = getWebElement(locatorType, "setText");
				if (e != null) {
					setValueForHiddenElement(e, textToSet);
				}

			}

			log.comment("setText", locatorType.getString(), textToSet, Log.DEBUG, Log.SCRIPT_ISSUE);

		} catch (NullPointerException e) {
			// Throw an exception based on the first parameter set to null.
			String nullArg = locatorType == null ? "locatorType" : "textToSet";
			throw new FrameworkException("setText", nullArg, "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
			// Handle any other type of Error and log an appropriate Error entry.
		} catch (SeleniumException e) {
			throw new FrameworkException("setText", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("setText", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
	}

	/**
	 * @param locatorType
	 *            object
	 * @param textToSet
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used by the following doubleClick() method calls:<br>
	 *             1. removeSelection(locatorType, option)<br>
	 *             2. removeSelection(locatorType, option, maxDelayInSeconds)<br>
	 */
	private void removeSelectionHelper(Locators.LocatorType locatorType, String option) throws FrameworkException {
		try {
			// Make sure the element is present before attempting a Selenium call. If not present, throw an exception
			if (waitForElementPresentHelper(locatorType, true, numSecondsToWaitForElementPresent, "setText")) {

				// If the element is not visible, log a warning, but still perform the Selenium call.
				if (!isElementVisible(locatorType)) {
					log.comment("removeSelection", locatorType.getString(), "ELEMENT NOT VISIBLE", Log.WARN, Log.FEATURE_CHANGE);
				}

				// Perform the Selenium call and log a debug entry.
				selenium.removeSelection(locatorType.getValue(), option);
				log.comment("removeSelection", locatorType.getString(), option, Log.DEBUG, Log.SCRIPT_ISSUE);

			} else {
				// The element was not present, log an error entry of type Feature Change.
				throw new FrameworkException("removeSelection", locatorType.getString(),
						"ELEMENT WAS NOT PRESENT AFTER: " + "'" + String.valueOf(numSecondsToWaitForElementPresent) + "' SECONDS.", Log.ERROR,
						Log.FEATURE_CHANGE);
			}

		} catch (NullPointerException e) {
			// Throw an exception based on the first parameter set to null.
			String nullArg = locatorType == null ? "locatorType" : "option";
			throw new FrameworkException("removeSelection", nullArg, "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);

			// Handle any other type of Error and log an appropriate Error entry.
		} catch (SeleniumException e) {
			throw new FrameworkException("removeSelection", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("removeSelection", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
	}

	/**
	 * @param locatorType
	 *            object
	 * @return void
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used by the following doubleClick() method calls:<br>
	 *             1. removeAllSelections(locatorType)<br>
	 *             2. removeAllSelections(locatorType, maxDelayInSeconds)<br>
	 */
	private void removeAllSelectionsHelper(Locators.LocatorType locatorType) throws FrameworkException {
		try {
			// Make sure the element is present before attempting a Selenium call. If not present, throw an exception
			if (waitForElementPresentHelper(locatorType, true, numSecondsToWaitForElementPresent, "setText")) {

				// If the element is not visible, log a warning, but still perform the Selenium call.
				if (!isElementVisible(locatorType)) {
					log.comment("removeAllSelections", locatorType.getString(), "ELEMENT NOT VISIBLE", Log.WARN, Log.FEATURE_CHANGE);
				}

				// Perform the Selenium call and log a debug entry.
				selenium.removeAllSelections(locatorType.getValue());
				log.comment("removeAllSelections", locatorType.getString(), "", Log.DEBUG, Log.SCRIPT_ISSUE);

			} else {
				// The element was not present, log an error entry of type Feature Change.
				throw new FrameworkException("removeAllSelections", locatorType.getString(),
						"ELEMENT WAS NOT PRESENT AFTER: " + "'" + String.valueOf(numSecondsToWaitForElementPresent) + "' SECONDS.", Log.ERROR,
						Log.FEATURE_CHANGE);
			}

			// Handle any type of Error and log an appropriate Error entry.
		} catch (NullPointerException e) {
			throw new FrameworkException("removeAllSelections", locatorType.getString(), "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException("removeAllSelections", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			throw new FrameworkException("removeAllSelections", locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
	}

	/**
	 * @param locatorType
	 *            object
	 * @param isVisible
	 * @param maxDelayInSeconds
	 * @param callingMethodName
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used by the following methods:<br>
	 *             1. waitForElementVisible(locatorType,maxDelayInSeconds)<br>
	 *             2. waitForElementNotVisible(locatorType,maxDelayInSeconds)<br>
	 */
	private boolean waitForElementVisibleHelper(final Locators.LocatorType locatorType, final boolean isVisible, int maxDelayInSeconds,
			String callingMethodName) throws FrameworkException {
		boolean res = true;

		try {

			// Make sure the timeout is valid. If not, throw an execution.
			if (maxDelayInSeconds < 1) {
				throw new FrameworkException(callingMethodName, String.valueOf(maxDelayInSeconds), "INVALID VALUE - Must be greater than zero",
						Log.ERROR, Log.FEATURE_CHANGE);
			}

			// Loop until the element equals "isVisible" or the timeout is reached.
			(new WebDriverWait(driver, maxDelayInSeconds)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return isElementVisible(locatorType) == isVisible;
				}
			});

		} catch (NullPointerException e) {
			throw new FrameworkException(callingMethodName, "locatorType", "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException(callingMethodName, locatorType.getString(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			res = false;
		}
		return res;
	}

	/**
	 * @param textPresent
	 * @param isPresent
	 * @param maxDelayInSeconds
	 * @param callingMethodName
	 *            : The method that called this method. For logging purposes.
	 * @return boolean
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used by the following methods:<br>
	 *             1. waitForTextPresent(isPresent,maxDelayInSeconds)<br>
	 *             2. waitForTextNotPresent(isPresent,maxDelayInSeconds)<br>
	 */
	private boolean waitForTextPresentHelper(final String textPresent, final boolean isTextPresent, int maxDelayInSeconds, String callingMethodName)
			throws FrameworkException {
		boolean res = true;

		try {

			// Make sure the timeout is valid. If not, throw an execution.
			if (maxDelayInSeconds < 1) {
				throw new FrameworkException(callingMethodName, String.valueOf(maxDelayInSeconds), "INVALID VALUE - Must be greater than zero",
						Log.ERROR, Log.FEATURE_CHANGE);
			}

			// Loop until the element equals "isTextPresent" or the timeout is reached.
			(new WebDriverWait(driver, maxDelayInSeconds)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return isTextPresent(textPresent) == isTextPresent;
				}
			});

		} catch (NullPointerException e) {
			throw new FrameworkException(callingMethodName, "textPresent", "ENCOUNTERED NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SeleniumException e) {
			throw new FrameworkException(callingMethodName, "textPresent", e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (WebDriverException e) {
			res = false;
		}
		return res;
	}

	/**
	 * <b>Description: </b> Get dynamic locator.
	 *
	 * @param locators
	 *            -->commonLocators or testcaseLocators<br>
	 * @param key
	 *            -->Locator key in Common_Locators.txt or testcase_Locators.txt<br>
	 * @param value
	 *            -->array of text value which used to replace the position hold %TEXT% in Locator value<br>
	 * @return LocatorType
	 * @date 2011-07-27
	 */
	public LocatorType getDynamicLocatorType(Locators locators, String key, String... value) {
		LocatorType locatorType = null;
		try {
			if (locators.containsKey(key)) {
				String locatorKey = locators.getLocator(key).getKey();
				String locatorValue = locators.getLocator(key).getValue();
				locatorValue = getReplacedLocator(locatorValue, value);
				locatorType = locators.getLocator(locatorKey, locatorValue);

			} else {
				String locatorValue = key;
				locatorValue = getReplacedLocator(locatorValue, value);
				locatorType = locators.getLocator("WebElement_on_Page", locatorValue);

			}

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
		}
		return locatorType;

	}

	public String getReplacedLocator(String locator, String... txt) {

		String replacedString = locator;
		for (int i = 0; i < txt.length; i++) {
			replacedString = replacedString.replaceFirst("%TEXT%", txt[i]);

		}
		return replacedString;

	}

	/**
	 * @param by
	 * @return webElement <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns "webElement" if found otherwise "null".<br>
	 *         This method will consume the "NoSuchElementException".<br>
	 *         Upon consumption, "webElement" will be set to "null" in<br>
	 *         order to ensure that a "null" values is always returned if<br>
	 *         the first matching element is not found.<br>
	 */
	@SuppressWarnings("unused")
	private WebElement findElement(By by) {
		WebElement webElement = null;

		try {
			webElement = driver.findElement(by);
		} catch (NoSuchElementException e) {
			// Make sure that "webElement" is null
			webElement = null;
		}

		return webElement;
	}

	private List<WebElement> findElements(By by) {
		List<WebElement> webElements = null;
		webElements = driver.findElements(by);

		return webElements;
	}

	/**
	 * @param locatorType
	 * @param callingMethodName
	 * @return webElements
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use method "getWebElements" to perform a search by:<br>
	 *             1. "xpath" if locator start with "//" is an xpath.<br>
	 *             2. "sizzle" if css locator contains "pesudo function".<br>
	 *             3. "css" if css locator.<br>
	 */
	protected List<WebElement> getWebElements(Locators.LocatorType locatorType, String callingMethodName) throws FrameworkException {
		List<WebElement> webElements = null;
		// Let's first do some input validation.
		if (locatorType == null) {
			throw new FrameworkException(callingMethodName, "locatorType", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		if (locatorType.getValue() == null) {
			throw new FrameworkException(callingMethodName, "locatorType.getValue()", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}

		if (locatorType.getValue().isEmpty()) {
			throw new FrameworkException(callingMethodName, "locatorType.getValue()", "MAKES REFERENCE TO AN EMPTY STRING", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}

		if (locatorType.getValue().startsWith("//")) {
			// LocatorType is an xpath.
			webElements = findElements(By.xpath(locatorType.getValue()));
		} else {
			// css locator with pesudo functions
			String locator = locatorType.getValue();
			if (locator.contains(":contains(") || locator.contains(":first-child") || locator.contains(":last-child")
					|| locator.contains(":nth-child") || locator.contains(":first") || locator.contains(":last")) {
				webElements = findElements(XBy.cssSelector(locator));

			} else {
				// css locator, this is the recommended selector
				webElements = findElements(By.cssSelector(locator));

			}

		}

		return webElements;

	}

	protected WebElement getWebElement(Locators.LocatorType locatorType, String callingMethodName) {
		List<WebElement> webElements = getWebElements(locatorType, callingMethodName);
		if (webElements.isEmpty() || webElements == null) {
			return null;
		} else {
			highlightElement(webElements.get(0));
			return webElements.get(0);
		}

	}

	/**
	 * @param locatorType
	 * @param callingMethodName
	 * @return true/false
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns "true" if locator is present otherwise "false".<br>
	 */
	private boolean isElementPresentHelper(Locators.LocatorType locatorType, String callingMethodName) throws FrameworkException {
		return getWebElement(locatorType, callingMethodName) == null ? false : true;
	}

	private void setValueForHiddenElement(WebElement element, String text) {

		String hiddenAttr = element.getAttribute("aria-hidden");
		log.comment("unhideElement-> element attribute unhideElement: aria-hidden: " + hiddenAttr);
		hiddenAttr = "false";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('aria-hidden', arguments[1]);", element, hiddenAttr);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		js.executeScript("arguments[0].setAttribute('value', arguments[1]);", element, text);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void clickHiddenElement(WebElement element) {
		String hiddenAttr = element.getAttribute("aria-hidden");
		log.comment("unhideElement-> element attribute unhideElement: aria-hidden: " + hiddenAttr);
		hiddenAttr = "false";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('aria-hidden', arguments[1]);", element, hiddenAttr);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		js.executeScript("arguments[0].click();", element);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void highlightElement(WebElement element) {
		if (enableHighlight) {
			// for (int i = 0; i < 2; i++) {
			String cssValue = element.getAttribute("style");
			if ("".equals(cssValue)) {
				cssValue = null;
			}
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, cssValue + "; outline:#E800E8 solid 5px;");
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, cssValue);
			// }
		}
	}

	private void purpleElement(WebElement element) {
		if (driver != null) {
			String cssValue = element.getAttribute("style");
			if ("".equals(cssValue)) {
				cssValue = null;
			}
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, cssValue + "; outline:#E800E8 solid 5px;");
		}
	}

	private void greyElement(WebElement element) {
		if (driver != null) {
			String cssValue = element.getAttribute("style");
			if ("".equals(cssValue)) {
				cssValue = null;
			}
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, cssValue + "; outline: none;");
		}
	}

	// --- END PRIVATE METHODS SECTION
	// ------------------------------------------------------------------------------------------------------

	public class XWebElement {
		private String		name;
		private String		locator;
		private Locators	locatorType;

		public XWebElement(Log log) {
			locatorType = Locators.DummyLocators();
		}

		public WebElement get_element() {
			return getWebElement(this.getLocator(), "XWebElement->get_element()");

		}

		public LocatorType getLocator() {
			return locatorType.getLocator(name, locator);
		}

		public List<WebElement> get_elements() {
			return getWebElements(this.getLocator(), "XWebElement->get_elements()");
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getLocatorStr() {
			StringBuilder lctStr = new StringBuilder();
			if (this.locator.startsWith("//")) {
				lctStr.append("xpath=").append(locator);
			} else {
				lctStr.append("css=").append(locator);
			}
			return lctStr.toString();
		}

		public void setLocator(String locator) {
			this.locator = locator;
		}

		public void click() {
			// WebElement e = this.get_element();
			// if (e == null) {
			// throw new NoSuchElementException("Cannot locate an element using locator: " + locator);
			// }
			// String txt = e.getText();
			// if (txt == null || txt.length() <= 1) {
			// log.comment("Click", "Element: " + this.getName() + "\nLocator: " + this.getLocatorStr(), "", Log.DEBUG, Log.SCRIPT_ISSUE);
			// } else {
			// log.comment("Click", "Element: " + this.getName() + "\nLocator: " + this.getLocatorStr(), "Text on Element: [" + txt + "]", Log.DEBUG,
			// Log.SCRIPT_ISSUE);
			// }
			// highlightElement(e);
			// e.click();
			WebApp.this.click(this.getLocator());

		}

		public void check() {
			WebApp.this.setCheck(this.getLocator(), true);
		}

		public void uncheck() {
			WebApp.this.setCheck(this.getLocator(), false);
		}

		public void clear() {
			WebElement e = this.get_element();
			if (e == null) {
				throw new NoSuchElementException("Cannot locate an element using locator: " + locator);
			}
			e.clear();

		}

		public String getText() {
			WebElement e = this.get_element();
			if (e == null) {
				throw new NoSuchElementException("Cannot locate an element using locator: " + locator);
			}
			highlightElement(e);
			return e.getText();

		}

		public String getCssValue(String propertyName) {
			WebElement e = this.get_element();
			if (e == null) {
				throw new NoSuchElementException("Cannot locate an element using locator: " + locator);
			}
			return e.getCssValue(propertyName);
		}

		public boolean isEnabled() {
			WebElement e = this.get_element();
			if (e == null) {
				throw new NoSuchElementException("Cannot locate an element using locator: " + locator);
			}
			highlightElement(e);
			return e.isEnabled();

		}

		public boolean isSelected() {
			WebElement e = this.get_element();
			if (e == null) {
				throw new NoSuchElementException("Cannot locate an element using locator: " + locator);
			}
			highlightElement(e);
			return e.isSelected();

		}

		public boolean isDisplayed() {
			WebElement e = this.get_element();
			if (e == null) {
				return false;
			} else {
				highlightElement(e);
				return e.isDisplayed();
			}

		}

		public void submit() {
			WebElement e = this.get_element();
			if (e == null) {
				throw new NoSuchElementException("Cannot locate an element using locator: " + locator);
			}
			highlightElement(e);
			e.submit();

		}

		public void sendKeys(String keys) {
			log.comment("SendKeys", "Element: " + this.getName() + "\nLocator: " + this.getLocatorStr(), "Input: [" + keys + "]", Log.DEBUG,
					Log.SCRIPT_ISSUE);
			WebElement e = this.get_element();
			if (e == null) {
				throw new NoSuchElementException("Cannot locate an element using locator: " + locator);
			}
			highlightElement(e);
			e.sendKeys(keys);

		}

		public void setText(String keys) {
			WebApp.this.setText(this.getLocator(), keys);
		}

		public void select(int index) {
			log.comment("Select", "Element: " + this.getName() + "\nLocator: " + this.getLocatorStr(), "Option Index: [" + index + "]", Log.DEBUG,
					Log.SCRIPT_ISSUE);
			WebElement e = this.get_element();
			if (e == null) {
				throw new NoSuchElementException("Cannot locate an element using locator: " + locator);
			}
			highlightElement(e);
			Select dropDownBox = new Select(e);
			dropDownBox.selectByIndex(index);

		}

		public void selectByValue(String value) {
			if (value != null) {
				log.comment("Select", "Element: " + this.getName() + "\nLocator: " + this.getLocatorStr(), "Option @Value: [" + value + "]",
						Log.DEBUG, Log.SCRIPT_ISSUE);
				WebElement e = this.get_element();
				if (e == null) {
					throw new NoSuchElementException("Cannot locate an element using locator: " + locator);
				}
				highlightElement(e);
				Select dropDownBox = new Select(e);
				dropDownBox.selectByValue(value);
			}
		}

		public void selectByText(String text) {
			if (text != null) {
				log.comment("Select", "Element: " + this.getName() + "\nLocator: " + this.getLocatorStr(), "Option Text(): [" + text + "]", Log.DEBUG,
						Log.SCRIPT_ISSUE);
				WebElement e = this.get_element();
				if (e == null) {
					throw new NoSuchElementException("Cannot locate an element using locator: " + locator);
				}
				highlightElement(e);
				Select dropDownBox = new Select(e);
				dropDownBox.selectByVisibleText(text);
			}
		}

		public void checkText(String text) {
			String _text = getText();
			WebElement e = this.get_element();
			try {
				if (text == null) {
					text = "";
				}

				purpleElement(e);
				log.verifyCompare(_text, text, "Check text on WebElement!");
				greyElement(e);

			} catch (Exception e1) {
				log.exception(e1);
				e1.printStackTrace();
			}

		}

		public void checkText_regExp(String regExp) {
			String _text = getText();
			WebElement e = this.get_element();
			try {
				if (regExp == null) {
					regExp = "";
				}

				purpleElement(e);
				log.matchText(_text, regExp, "Check text on WebElement with regular expression!");
				greyElement(e);

			} catch (Exception e1) {
				log.exception(e1);
				e1.printStackTrace();
			}

		}

		public void isExists() {
			WebElement e = this.get_element();
			boolean exists = false;
			try {
				if (e == null) {
					log.comment("XWebElement->isDisplayed", "Element: " + this.getName() + "\nLocator: " + this.getLocatorStr(),
							"Element does not display!", Log.FAIL, Log.MAJOR_ISSUE);
					// Reporter.log("Element does not display! </br>Element: " + this.getName() + "</br>Locator: " + this.getLocatorStr()
					// + "</br>====================</br>");
					Reporter.log("Element does not display! Element: " + this.getName() + " Locator: " + this.getLocatorStr());
					Assert.assertTrue(exists, "Check if WebElement displays! ");

				} else {
					String txt = e.getText();
					highlightElement(e);
					exists = e.isDisplayed();
					if (txt == null || txt.length() <= 1) {
						log.comment("XWebElement->isDisplayed", "Element: " + this.getName() + "\nLocator: " + this.getLocatorStr(),
								"Element displays!", Log.PASS, Log.NO_ISSUE);

					} else {
						log.comment("XWebElement->isDisplayed", "Element: " + this.getName() + "\nLocator: " + this.getLocatorStr(),
								"Text on Element: [" + txt + "]" + "\nElement displays!", Log.PASS, Log.NO_ISSUE);

					}
					// Reporter.log("Element displays! </br>Element: " + this.getName() + "</br>Locator: " +
					// this.getLocatorStr()
					// + "</br>====================</br>");
					Reporter.log("Element displays!  Element: " + this.getName() + " Locator: " + this.getLocatorStr());
					Assert.assertTrue(exists, "Check if WebElement displays! ");

				}

			} catch (Exception ex) {
				log.exception(ex);
				ex.printStackTrace();

			}

		}

	}

}