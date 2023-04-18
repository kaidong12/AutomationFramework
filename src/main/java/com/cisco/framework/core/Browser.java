package com.cisco.framework.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.HasCapabilities;
import org.testng.ITestContext;

import com.cisco.framework.core.enums.BrowserType;
import com.cisco.framework.core.exceptions.FrameworkException;
import com.cisco.framework.utilities.logging.Log;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

/**
 * @author Lance Yan
 */
public class Browser {

	private WebDriver					webDriver					= null;
	private ITestContext				context						= null;
	private FirefoxProfile				fireFoxProfile				= null;
	private static Log					log							= null;
	private StringBuilder				supportedBrowsers			= null;
	private Map<String, String>			parameters					= null;
	private static ChromeDriverService	service;

	private boolean						isUsingFirefoxBrowser		= false;

	private final static String			NEW_LINE					= System.getProperty("line.separator");
	private final static String			CHROME_DRIVER_EXECUTABLE	= "chromedriver.exe";

	// A "base url", used by selenium to resolve relative URLs.
	private String						baseUrl						= "http://www.google.com/";

	/**
	 * @param Log
	 * @param ITestContext
	 * @param BrowserType
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this constructor to instantiate on of the following browser types:<bt> 1. FIREFOX (Default)<br>
	 *             2. INTERNET_EXPLORER<br>
	 *             3. CHROME<br>
	 *             4. OPERA<br>
	 *             5. SAFARI<br>
	 *             An appropriate DEBUG log entry is entered for each item.<br>
	 * @throws IOException
	 */
	public Browser(Log log, ITestContext context, BrowserType browserType) throws FrameworkException {
		initializeSupportedBrowsers();
		validateTestLog(log, "Browser");
		validateTestContext(context, "Browser");
		init(browserType, "Browser");
	}

	/**
	 * @param Log
	 * @param ITestContext
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Given a valid "ITestContext" object use this constructor to instantiate a "Firefox" browser object by defaulr.<br>
	 *             An appropriate DEBUG log entry is entered.<br>
	 * @throws IOException
	 */
	public Browser(Log log, ITestContext context) throws FrameworkException {
		initializeSupportedBrowsers();
		validateTestLog(log, "Browser");
		validateTestContext(context, "Browser");
		init(context, "Browse");
	}

	/**
	 * @param browserType
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to switch to one of the following supported browser types:<br>
	 *             1. FIREFOX (Default)<br>
	 *             2. INTERNET_EXPLORER<br>
	 *             3. CHROME<br>
	 *             4. OPERA<br>
	 *             5. SAFARI<br>
	 *             An appropriate DEBUG log entry is entered for each item.<br>
	 * @throws IOException
	 */
	public void setBrowserType(BrowserType browserType) throws FrameworkException {
		initializeSupportedBrowsers();
		init(browserType, "setBrowserType");
	}

	/**
	 * @param browserType
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to switch to one of the following supported browser types:<br>
	 *             1. FIREFOX (Default)<br>
	 *             2. INTERNET_EXPLORER<br>
	 *             3. CHROME<br>
	 *             4. OPERA<br>
	 *             5. SAFARI<br>
	 *             An appropriate DEBUG log entry is entered for each item.<br>
	 * @throws IOException
	 */
	public void setBrowserType(String browserType) throws FrameworkException {
		initializeSupportedBrowsers();
		init(browserType, "setBrowserType");
	}

	/**
	 * @return WebDriver <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Workaround for issue: Issue 1438: WebDriver and Firefox 4+: this.getWindow() is null<br>
	 *         Please see details in: "http://code.google.com/p/selenium/issues/detail?id=1438"
	 */
	public WebDriver getWebDriver() {
		WebDriver res = null;

		if (webDriver != null) {

			res = webDriver;

		}

		return res;
	}

	/**
	 * @param webDriver
	 * @param String
	 *            ... (args) <br>
	 *            <br>
	 *            USAGE: <br>
	 *            <br>
	 *            <p>
	 *            Use this method to switch to a different "WebDriver" type such for example to<br>
	 *            an "EventFiringWebDriver".<br>
	 */
	public void setWebDriver(WebDriver webDriver, String... args) {
		if (webDriver == null) {
			if (args != null) {
				if (args.length == 1) {
					throw new FrameworkException(args[0], "webDriver", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
				} else {
					throw new FrameworkException("setWebDriver", "webDriver", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
				}
			} else {
				throw new FrameworkException("setWebDriver", "webDriver", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
			}
		}
		this.webDriver = webDriver;
	}

	/**
	 * @return Selenium <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to return the backed end "Selenium" object.<br>
	 */
	public Selenium getSelenium() {
		// Let's get the "Selenium" implementation.
		return new WebDriverBackedSelenium(getWebDriver(), this.baseUrl);
	}

	/**
	 * @return FirefoxProfile <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to return the "firefox profile" in use.<br>
	 */
	public FirefoxProfile getFirefoxProfile() {
		return fireFoxProfile;
	}

	/**
	 * @return String <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to return the "name" of the browser.<br>
	 */
	public String getBrowserName() {
		WebDriver webDriver = getWebDriver();
		return webDriver == null ? "" : ((HasCapabilities) webDriver).getCapabilities().getBrowserName();
	}

	/**
	 * @return String <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to return the "platform" that the browser is running on.<br>
	 */
	public String getPlatform() {
		String platform = "";
		WebDriver webDriver = getWebDriver();

		if (webDriver != null) {
			switch (((HasCapabilities) webDriver).getCapabilities().getPlatform()) {
			case ANDROID:
				platform = "android";
				break;

			case ANY:
				platform = "any";
				break;

			case LINUX:
				platform = "linux";
				break;

			case MAC:
				platform = "mac";
				break;

			case UNIX:
				platform = "unix";
				break;

			case VISTA:
				platform = "vista";
				break;

			case WINDOWS:
				platform = "windows";
				break;

			case XP:
				platform = "xp";
				break;
			}
		}

		return platform;
	}

	/**
	 * @return String <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to return the "version" of the browser.<br>
	 */
	public String getVersion() {
		WebDriver webDriver = getWebDriver();
		return webDriver == null ? "" : ((HasCapabilities) webDriver).getCapabilities().getVersion();
	}

	/**
	 * @return String <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to return the "curremt url" that the browser is looking at.<br>
	 */
	public String getCurrentUrl() {
		WebDriver webDriver = getWebDriver();
		return webDriver == null ? "" : webDriver.getCurrentUrl();
	}

	/**
	 * @return String <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to return the "source" of the last loaded page. <br>
	 */
	public String getPageSource() {
		WebDriver webDriver = getWebDriver();
		return webDriver == null ? "" : webDriver.getPageSource();
	}

	/**
	 * @return String <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to return the "title" of the current page.<br>
	 */
	public String getTitle() {
		WebDriver webDriver = getWebDriver();
		return webDriver == null ? "" : webDriver.getTitle();
	}

	/**
	 * @return Map
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to return a "HashMap" object of "key/value" pairs consisting of all entries in file "common_testsuite.xml"<br>
	 *             toegther with all entries from the "current xml test file".<br>
	 *             If a "key/value" pair exists in both the "common_testsuite.xml" and the "current xml test file", then the "key/value" pair in<br>
	 *             the "current xml test file" takes precedent.
	 */
	public Map<String, String> getParameters() throws FrameworkException {
		return parameters;
	}

	/**
	 * @return Browser <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to move back a single "item" in the browser's history.<br>
	 *         An appropriate "DEBUG" entry is logged.<br>
	 */
	public Browser back() {
		WebDriver webDriver = getWebDriver();
		if (webDriver != null) {
			webDriver.navigate().back();
			log.comment("back", "", "", Log.DEBUG, Log.SCRIPT_ISSUE);
		}
		return this;
	}

	/**
	 * @return Browser <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to move a single "item" forward in the browser's history.<br>
	 *         An appropriate "DEBUG" entry is logged.<br>
	 */
	public Browser forward() {
		WebDriver webDriver = getWebDriver();
		if (webDriver != null) {
			webDriver.navigate().forward();
			log.comment("forward", "", "", Log.DEBUG, Log.SCRIPT_ISSUE);
		}
		return this;
	}

	/**
	 * @return Browser <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to refresh the current page.<br>
	 *         An appropriate "DEBUG" entry is logged.<br>
	 */
	public Browser refresh() {
		WebDriver webDriver = getWebDriver();
		if (webDriver != null) {
			webDriver.navigate().refresh();
			log.comment("refresh", "", "", Log.DEBUG, Log.SCRIPT_ISSUE);
		}
		return this;
	}

	/**
	 * @return Browser <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to load a new web page in the current browser window.<br>
	 *         An appropriate "DEBUG" entry is logged.<br>
	 */
	public Browser navigateTo(String url) throws FrameworkException {
		if (url == null) {
			throw new FrameworkException("navigateTo", "url", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		WebDriver driver = getWebDriver();
		if (driver != null) {
			driver.navigate().to(url);
			if (url.isEmpty()) {
				log.comment("NAVIGATING TO NEW PAGE");
			} else {
				log.comment("NAVIGATING TO: " + "'" + url + "'");
			}
		}
		return this;
	}

	/**
	 * @return Browser <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Quits this driver, closing every associated window.<br>
	 *         An appropriate "DEBUG" entry is logged.<br>
	 */
	public Browser quit() {
		WebDriver driver = getWebDriver();
		if (driver != null) {
			String browserName = this.getBrowserName();
			driver.quit();
			log.comment("quit", "", browserName, Log.DEBUG, Log.SCRIPT_ISSUE);
		}
		return this;
	}

	/**
	 * @return Browser <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Close the current window, quitting the browser if it's the last window currently open.<br>
	 *         An appropriate "DEBUG" entry is logged.<br>
	 */
	public Browser close() {
		WebDriver driver = getWebDriver();
		if (driver != null) {
			String browserName = this.getBrowserName();
			driver.close();
			log.comment("close", "", browserName, Log.DEBUG, Log.SCRIPT_ISSUE);
		}
		return this;
	}

	public static void createAndStartChromeService(File chromedriver) {
		// service = new
		// ChromeDriverService.Builder().usingChromeDriverExecutable(chromedriver).usingAnyFreePort().build();
		service = new ChromeDriverService.Builder().usingDriverExecutable(chromedriver).usingAnyFreePort().build();
		try {
			service.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void stopChromeService() {
		service.stop();
	}

	/*
	 * This method is used to instantiate a "FirefoxDriver" object given a user-specified Firefox profile. If "pathToTopLevelFirefoxProfile" is set to
	 * an empty string a "FirefoxDriver" object with a "WebDriver" provided firefox profile is created.
	 *
	 * NOTES:
	 *
	 * Pros 1. Runs in a real browser and supports JavaScript 2. Faster than the Internet Explorer Driver
	 *
	 * Cons 1. Slower than the HtmlUnit Driver
	 *
	 * Important System Properties The following system properties (read using System.getProperty() and set using System.setProperty() in Java code or
	 * the -DpropertyName=value command line flag) are used by the Firefox Driver:
	 *
	 * Property What it means ------------------------------------------ webdriver.firefox.bin The location of the binary used to control Firefox.
	 * webdriver.firefox.profile The name of the profile to use when starting Firefox. This defaults to WebDriver creating an anonymous profile
	 * webdriver.reap_profile Should be 搕rue�? if temporary files and profiles should not be deleted
	 */
	private void initFirefoxDriver(String methodName) throws FrameworkException {
		isUsingFirefoxBrowser = true;
		File fireFoxProfileDir = null;
		String pathToTopLevelFirefoxProfile = getParameters().get("firefox.profileTemplate");
		if (pathToTopLevelFirefoxProfile == null) {
			StringBuilder errorMessage = new StringBuilder();
			errorMessage.append("UNABLE TO GET PATH AND NAME TO TOPLEVEL FIREFOX PROFILE");
			errorMessage.append(NEW_LINE);
			errorMessage.append(NEW_LINE);
			errorMessage.append("POSSIBLE CAUSES:");
			errorMessage.append(NEW_LINE);
			errorMessage.append(NEW_LINE);
			errorMessage.append("1. <parameter name = 'firefox.profileTemplate' value = '...'> NOT DEFINED IN XML FILE");
			errorMessage.append(NEW_LINE);
			errorMessage.append("2. <parameter name = 'firefox.profileTemplate' value = '...'> IS DEFINED BUT COMMENTED OUT IN XML FILE");
			throw new FrameworkException("Browser", "context", errorMessage.toString(), Log.ERROR, Log.SCRIPT_ISSUE);
		} else {
			if (pathToTopLevelFirefoxProfile.isEmpty()) {
				// Instantiate a "FirefoxDriver" object with a "WebDriver"
				// supplied firefox profile.
				webDriver = new FirefoxDriver();
			} else {
				// Instantiate a "FirefoxDriver" object with a "user" supplied
				// firefox profile.
				fireFoxProfileDir = new File(pathToTopLevelFirefoxProfile);
				if (!fireFoxProfileDir.exists()) {
					throw new FrameworkException(methodName, "pathToTopLevelFirefoxProfile",
							"FILE: " + "'" + pathToTopLevelFirefoxProfile + "' DOES NOT EXIST.", Log.ERROR, Log.SCRIPT_ISSUE);
				}
				fireFoxProfile = new FirefoxProfile(fireFoxProfileDir);

				if (parameters.get("setAssumeUntrustedCertificateIssuer") != null) {
					if (parameters.get("setAssumeUntrustedCertificateIssuer").equalsIgnoreCase("false")
							|| parameters.get("setAssumeUntrustedCertificateIssuer").equalsIgnoreCase("true")) {
						if (parameters.get("setAssumeUntrustedCertificateIssuer").equalsIgnoreCase("false")) {
							fireFoxProfile.setAssumeUntrustedCertificateIssuer(false);
						} else if (parameters.get("setAssumeUntrustedCertificateIssuer").equalsIgnoreCase("true")) {
							fireFoxProfile.setAssumeUntrustedCertificateIssuer(true);
						}
					} else {
						fireFoxProfile.setAssumeUntrustedCertificateIssuer(false);
						throw new FrameworkException(methodName, "initFirefoxDriver",
								String.format(
										"PARAMETER: 'setAssumeUntrustedCertificateIssuer' MUST BE EITHER '%s' OR '%s' PARAMETER HAS BEEN SET TO 'false'",
										"false", "true"),
								Log.ERROR, Log.SCRIPT_ISSUE);
					}
				} else {
					// Default setting takes effect if
					// "setAssumeUntrustedCertificateIssuer" is not
					// specified in common_testsuite.xml
					fireFoxProfile.setAcceptUntrustedCertificates(false);
				}

				webDriver = new FirefoxDriver(fireFoxProfile);
			}

			// Let's specify the base url. If the base url from xml exists and
			// non-empty then use it, other wise use the default one.
			String baseUrl = context.getCurrentXmlTest().getAllParameters().get("baseUrl");
			if (baseUrl != null) {
				if (!baseUrl.isEmpty()) {
					this.baseUrl = baseUrl;
				}
			}

			logBrowserInformation(getBrowserName(), getVersion(), getPlatform(), fireFoxProfileDir);
		}
	}

	/*
	 * This method is used to instantiate a "InternetExplorerDriver" object.
	 */
	private void initInternetExplorerDriver(String methodName) throws FrameworkException {
		isUsingFirefoxBrowser = false;
		// Instantiate a "InternetExplorerDriver" object.
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		webDriver = new InternetExplorerDriver(ieCapabilities);
		logBrowserInformation(getBrowserName(), getVersion(), getPlatform(), null);
	}

	/*
	 * This method is used to instantiate a "ChromeDriver" object.
	 */
	// @SuppressWarnings("deprecation")
	// private void initChromeDriver(String methodName) throws
	// FrameworkException {
	// isUsingFirefoxBrowser = false;
	// // Get location to chromedriver.exe
	// String chromeDriverExecutable =
	// CommandLine.findExecutable(CHROME_DRIVER_EXECUTABLE);
	// if (chromeDriverExecutable == null) {
	// String errorMessage = "'" + CHROME_DRIVER_EXECUTABLE + "' " +
	// "IS NOT IN THE SYSTEM PATH. PLEASE ADD IT TO THE SYSTEM PATH.";
	// throw new FrameworkException(methodName, "CHROME DRIVER EXECUTABLE:",
	// errorMessage, Log.ERROR, Log.SCRIPT_ISSUE);
	// }
	// // Tell chrome driver to start chrome with the following command line
	// switchs: "--ignore-certificate-errors"
	// // and "--start-maximized". The first switch tells chrome to ignore any
	// certificate error which it may
	// // encounter. While the second switch starts the chrome browser in the
	// maximized state.
	// DesiredCapabilities capabilities = DesiredCapabilities.chrome();
	// capabilities.setCapability("chrome.switches",
	// Arrays.asList("--ignore-certificate-errors", "--start-maximized"));
	// // Instantiate a "ChromeDriver" object.
	// webDriver = new ChromeDriver(capabilities);
	//
	// // ChromeOptions options = new ChromeOptions();
	// // options.setBinary(new File(chromeDriverExecutable));
	// //
	// options.addArguments("--ignore-certificate-errors","--start-maximized");
	// // webDriver = new ChromeDriver(options);
	//
	// logBrowserInformation(getBrowserName(), getVersion(), getPlatform(),
	// null);
	// }
	private void initChromeDriver(String methodName) {
		isUsingFirefoxBrowser = false;
		String chromeSettings = getParameters().get("ChromeSettings");
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability("chrome.switches", Arrays.asList(chromeSettings));
		webDriver = new RemoteWebDriver(service.getUrl(), capabilities);

		if (service == null) {
			String errorMessage = "CHROME_DRIVER_SERVICE IS NULL. PLEASE CREATE AND START IT FIRST.";
			throw new FrameworkException(methodName, "CHROME_DRIVER_SERVICE:", errorMessage, Log.ERROR, Log.SCRIPT_ISSUE);

		}

		logBrowserInformation(getBrowserName(), getVersion(), getPlatform(), null);

	}

	/*
	 * This method is used to instantiate a "OperaDriver" object.
	 */
	private void initOperaDriver(String methodName) throws FrameworkException {
		isUsingFirefoxBrowser = false;
		throw new FrameworkException(methodName, "OperaDriver", "NOT IMPLEMENTED YET", Log.ERROR, Log.SCRIPT_ISSUE);
	}

	/*
	 * This method is used to instantiate a "SafariDriver" object.
	 */
	private void initSafariDriver(String methodName) throws FrameworkException {
		isUsingFirefoxBrowser = false;
		throw new FrameworkException(methodName, "SafariDriver", "NOT IMPLEMENTED YET", Log.ERROR, Log.SCRIPT_ISSUE);
	}

	/*
	 * This method is used to initialize "supportedBrowsers" with the following supported browsers: 1. FIREFOX 2. INTERNET EXPLORER 3. CHROME 4. OPERA
	 * 5. SAFARI
	 */
	private void initializeSupportedBrowsers() {
		supportedBrowsers = new StringBuilder();
		supportedBrowsers.append("FIREFOX");
		supportedBrowsers.append(Browser.NEW_LINE);
		supportedBrowsers.append("FF");
		supportedBrowsers.append(Browser.NEW_LINE);
		supportedBrowsers.append("INTERNET EXPLORER");
		supportedBrowsers.append(Browser.NEW_LINE);
		supportedBrowsers.append("IE");
		supportedBrowsers.append(Browser.NEW_LINE);
		supportedBrowsers.append("IEXPLORER");
		supportedBrowsers.append(Browser.NEW_LINE);
		supportedBrowsers.append("CHROME");
		supportedBrowsers.append(Browser.NEW_LINE);
		supportedBrowsers.append("OPERA");
		supportedBrowsers.append(Browser.NEW_LINE);
		supportedBrowsers.append("SAFARI");
		supportedBrowsers.trimToSize();
	}

	/*
	 * This method returns "true" if browser is supported and "false" otherwise.
	 */
	private boolean isBrowserSupported(String browser) {
		boolean res = false;
		if (browser != null) {
			res = supportedBrowsers.toString().contains(browser.toUpperCase());
		}
		return res;
	}

	/*
	 * Initializer method for constructor Browser(ITestContext context, BrowserType browserType)
	 */
	private void init(BrowserType browserType, String methodName) throws FrameworkException {

		mergeCommonAndLocalTestSuiteParameters();
		// getCommonAndLocalTestSuiteParameters();

		switch (browserType) {
		case FIREFOX:
			initFirefoxDriver(methodName);
			break;
		case INTERNET_EXPLORER:
			initInternetExplorerDriver(methodName);
			break;
		case CHROME:
			initChromeDriver(methodName);
			break;
		case OPERA:
			initOperaDriver(methodName);
			break;
		case SAFARI:
			initSafariDriver(methodName);
			break;
		}
	}

	/*
	 * Initializer method for constructor Browser(Log log, ITestContext context)
	 */
	private void init(ITestContext context, String methodName) {
		if (context != null) {
			String browserType = context.getCurrentXmlTest().getParameter("BrowserType");
			if (browserType != null) {
				// User user-specified browser.
				init(browserType, methodName);
			} else {
				// Use default firefox browser.
				init("FIREFOX", methodName);
			}
		}
	}

	/*
	 * Initializer method for method setBrowserType(String browserType)
	 */
	private void init(String browserType, String methodName) throws FrameworkException {

		mergeCommonAndLocalTestSuiteParameters();
		// getCommonAndLocalTestSuiteParameters();

		if (isBrowserSupported(browserType)) {
			if (browserType.equalsIgnoreCase("FIREFOX") || browserType.equalsIgnoreCase("FF")) {
				initFirefoxDriver(methodName);
			}
			if (browserType.equalsIgnoreCase("INTERNET EXPLORER") || browserType.equalsIgnoreCase("IE")
					|| browserType.equalsIgnoreCase("IEXPLORER")) {
				initInternetExplorerDriver(methodName);
			}
			if (browserType.equalsIgnoreCase("CHROME")) {
				initChromeDriver(methodName);
			}
			if (browserType.equalsIgnoreCase("OPERA")) {
				initOperaDriver(methodName);
			}
			if (browserType.equalsIgnoreCase("SAFARI")) {
				initSafariDriver(methodName);
			}

		} else {
			if (browserType == null) {
				throw new FrameworkException(methodName, "browserType", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
			} else {
				if (browserType.isEmpty()) {
					throw new FrameworkException(methodName, "browserType", "MAKES REFERENCE TO AN EMPTY STRING", Log.ERROR, Log.SCRIPT_ISSUE);
				} else {
					StringBuilder errorMessage = new StringBuilder();
					errorMessage.append("MAKES REFERENCE TO UNSUPPORTED BROWSER: " + "'" + browserType + "'");
					errorMessage.append(NEW_LINE);
					errorMessage.append("SUPPORTED BROWSERS:");
					errorMessage.append(NEW_LINE);
					errorMessage.append(supportedBrowsers);
					throw new FrameworkException("setBrowserType", "browserType", errorMessage.toString(), Log.ERROR, Log.SCRIPT_ISSUE);
				}
			}
		}
	}

	/*
	 * Method validateTestContext() ensures that a given "ITestContext" object is not null.
	 */
	private void validateTestContext(ITestContext context, String methodName) throws FrameworkException {
		if (context == null) {
			throw new FrameworkException(methodName, "context", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} else {
			this.context = context;
		}
	}

	/*
	 * Method validateTestLog() ensures that a given "log" object is not null.
	 */
	private void validateTestLog(Log log, String methodName) throws FrameworkException {
		if (log == null) {
			throw new FrameworkException(methodName, "log", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		} else {
			Browser.log = log;
		}
	}

	/*
	 * Method logBrowserInformation() logs browser "name", "version", "platform", and "profile" if applicable.
	 */
	private void logBrowserInformation(String browserName, String browserVersion, String browserPlatform, File browserProfile) {
		StringBuilder locatorOrParameter = new StringBuilder();
		StringBuilder actionValueOrMessage = new StringBuilder();

		// Log browser name.
		if (browserName != null) {
			if (!browserName.isEmpty()) {
				locatorOrParameter.append("BROWSER NAME:");
				locatorOrParameter.append(NEW_LINE);
				actionValueOrMessage.append("'" + browserName.trim() + "'");
				actionValueOrMessage.append(NEW_LINE);
			} else {
				log.comment("Browser", "browserName", "MAKES REFERENCE TO AN EMPTY STRING", Log.WARN, Log.SCRIPT_ISSUE);
			}
		} else {
			log.comment("Browser", "browserName", "MAKES REFERENCE TO A NULL POINTER", Log.WARN, Log.SCRIPT_ISSUE);
		}

		// Log browser version.
		if (browserVersion != null) {
			if (!browserVersion.isEmpty()) {
				locatorOrParameter.append("BROWSER VERSION:");
				locatorOrParameter.append(NEW_LINE);
				actionValueOrMessage.append("'" + browserVersion.trim() + "'");
				actionValueOrMessage.append(NEW_LINE);
			} else {
				log.comment("Browser", "browserVersion", "MAKES REFERENCE TO AN EMPTY STRING", Log.WARN, Log.SCRIPT_ISSUE);
			}
		} else {
			log.comment("Browser", "browserVersion", "MAKES REFERENCE TO A NULL POINTER", Log.WARN, Log.SCRIPT_ISSUE);
		}

		// Log browser platform.
		if (browserPlatform != null) {
			if (!browserPlatform.isEmpty()) {
				locatorOrParameter.append("BROWSER PLATFORM:");
				locatorOrParameter.append(NEW_LINE);
				actionValueOrMessage.append("'" + browserPlatform.trim() + "'");
				actionValueOrMessage.append(NEW_LINE);
			} else {
				log.comment("Browser", "browserPlatform", "MAKES REFERENCE TO AN EMPTY STRING", Log.WARN, Log.SCRIPT_ISSUE);
			}
		} else {
			log.comment("Browser", "browserPlatform", "MAKES REFERENCE TO A NULL POINTER", Log.WARN, Log.SCRIPT_ISSUE);
		}

		// Log browser profile.
		if (isUsingFirefoxBrowser) {
			if (browserProfile != null) {
				if (browserProfile.exists()) {
					locatorOrParameter.append("BROWSER PROFILE:");
					actionValueOrMessage.append("'" + browserProfile.getAbsolutePath().trim() + "'");
				} else {
					log.comment("Browser", "browserProfile", "MAKES REFERENCE TO A NON-EXISTANT PATH AND FILENAME", Log.WARN, Log.SCRIPT_ISSUE);
				}
			} else {
				log.comment("Browser", "browserProfile", "MAKES REFERENCE TO A NULL POINTER", Log.WARN, Log.SCRIPT_ISSUE);
			}
		}

		// Log browser/platform information
		if ((locatorOrParameter.length() != 0) && (actionValueOrMessage.length() != 0)) {
			log.comment("Browser", locatorOrParameter.toString().trim(), actionValueOrMessage.toString().toUpperCase().trim(), Log.DEBUG,
					Log.SCRIPT_ISSUE);
		}
	}

	/*
	 * Use this method to merge common testuite parameters with local testsuite parameters
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void mergeCommonAndLocalTestSuiteParameters() throws FrameworkException {
		Properties commonTestSuiteProperties = null;
		Properties testSuiteProperties = null;
		FileInputStream fileInputStream = null;

		try {

			String commonTestSuiteXML = context.getCurrentXmlTest().getParameter("commonSettings_XMLPath");

			if (commonTestSuiteXML == null) {
				throw new FrameworkException("Browser", "PARAMETER:", "'commonSettings_XMLPath' IS EITHER UNDEFINED OR COMMENTED OUT", Log.ERROR,
						Log.SCRIPT_ISSUE);
			}

			if (commonTestSuiteXML.isEmpty()) {
				throw new FrameworkException("Browser", "PARAMETER:", "'commonSettings_XMLPath' MAKES REFERENCE TO AN EMPTY STRING", Log.ERROR,
						Log.SCRIPT_ISSUE);
			}

			// Read contents of "common_testsuite.xml" onto a "FileInputStream"
			// object.
			fileInputStream = new FileInputStream(commonTestSuiteXML);

			// Instantiate a "Properties" object and populate ir with the
			// contents of "common_testsuite.xml"
			commonTestSuiteProperties = new Properties();
			commonTestSuiteProperties.loadFromXML(fileInputStream);

			// Instantiate another "Properties" object using
			// "commonTestSuiteProperties" as the default properties.
			testSuiteProperties = new Properties(commonTestSuiteProperties);

			// Populate "testSuiteProperties" with the contents of the
			// "current test xml".
			Set<Map.Entry<String, String>> currentTestXMLContents = context.getCurrentXmlTest().getAllParameters().entrySet();
			for (Map.Entry<String, String> mapEntry : currentTestXMLContents) {
				testSuiteProperties.setProperty(mapEntry.getKey(), mapEntry.getValue());
			}

			// Let's return a "HashMap" object consisting of a table of
			// "key/value" pairs from both "common_testsuite.xml" and
			// the "current test xml".
			Set<String> propertyNames = testSuiteProperties.stringPropertyNames();
			parameters = new HashMap();
			for (String propertyName : propertyNames) {
				parameters.put(propertyName, testSuiteProperties.getProperty(propertyName));
			}

		} catch (IOException e) {
			throw new FrameworkException("Browser", "", e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} finally {
			// Close all file resources
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					throw new FrameworkException("Browser", "", e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
				}
			}
		}
	}

}