package com.cisco.framework.utilities.logging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Robot;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.cisco.framework.core.exceptions.FrameworkException;
import com.cisco.framework.utilities.JFileTimeStamper;
import com.cisco.framework.utilities.logging.html.*;

/**
 * @author Francesco Ferrante
 *
 */
public class Log {

	public final static String	MKTREE_JS					= "mktree.js";
	public final static String	MKTREE_CSS					= "mktree.css";

	public final static String	WHITE						= "#FFFFFF";
	public final static String	YELLOW						= "#FFFBD3";
	// #FFFF00
	public final static String	GREEN						= "#65C400";
	// #9CE62A
	// #00FF00
	// #7FFF00
	// #B2FF66
	// #9CE62A
	// #B0FD66
	// #FFFF66
	// #AED75B
	public final static String	RED							= "#C20000";
	// #FF4500
	// #DC143C
	// #FF4D4D
	// #FF4500
	// #E65940

	private final String		NEW_LINE					= System.getProperty("line.separator");

	public final static int		SCREEN_SHOT					= 0;
	public final static int		DEBUG						= 1;
	public final static int		ERROR						= 2;
	public final static int		DEFECT						= 3;
	public final static int		FAIL						= 4;
	public final static int		PASS						= 5;
	public final static int		WARN						= 6;

	public final static int		FEATURE_CHANGE				= 7;
	public final static int		SCRIPT_ISSUE				= 8;
	public final static int		MINOR_ISSUE					= 9;
	public final static int		MAJOR_ISSUE					= 10;
	public final static int		NO_ISSUE					= 11;
	public final static int		SCREEN_SHOT_WIDTH			= 100;
	public final static int		SCREEN_SHOT_HEIGHT			= 100;

	private final static String	SCREEN_SHOTS_FOLDER			= "screenshots";
	private final static String	RESOURCES_FOLDER			= "resources";
	private final static String	SCREEN_SHOT_SUFFIX			= ".png";
	private final static String	BULLET_GIF					= "bullet.gif";
	private final static String	MINUS_GIF					= "minus.gif";
	private final static String	PLUS_GIF					= "plus.gif";
	private final static int	SCREEN_SHOT_COUNTER_WIDTH	= 4;

	private HTMLElement			htmlElement					= null;
	private HTMLHeadElement		htmlHeadElement				= null;
	private File				logFile						= null;
	private File				resultMetricsFile			= null;
	private File				screenShotsFolder			= null;
	private File				resourcesFolder				= null;
	private File				mkTreeURLJS					= null;
	private File				mkTreeURLCSS				= null;
	private File				bulletURLGIF				= null;
	private File				minusURLGIF					= null;
	private File				plusURLGIF					= null;
	private TestIteration		testIteration				= null;
	private TestMethod			testMethod					= null;
	private List<TestMethod>	testMethods					= null;
	private TestFunction		testFunction				= null;
	private String				LogMode						= "SingleIteration";

	private String				testResults					= null;
	private String				dataProviderName			= "Iteration";
	private String				testMethodName				= "";
	private String				testMethodDesc				= "";
	private String				methodName					= "";
	private String[]			supportedOperators			= new String[] { "<", "<=", ">", ">=", "==", "!=" };

	private boolean				logCreated					= false;
	private boolean				isTestStarted				= false;
	private boolean				captureScreenShot			= true;
	private int					startTestCounter			= 0;
	private int					endTestCounter				= 0;
	private int					screenShotCounter			= 0;
	private static List<File>	capturedScreenShots			= new ArrayList<File>();

	private WebDriver			_driver						= null;
	private SoftAssert			softAssert					= null;

	public Log(String logFile) {
		init(logFile);
		testMethods = new ArrayList<TestMethod>();
		softAssert = new SoftAssert();
	}

	public void setLogMode(String mode) {
		this.LogMode = mode;
	}

	public void setDriver(WebDriver driver) {
		this._driver = driver;
	}

	/***
	 * @author Francesco Ferrante
	 * @param none
	 * @return List of <TestMethod> <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns List of Test Methods from Current Log Object.
	 */
	public List<TestMethod> GetTestMethods() {
		return this.testMethods;
	}

	public String getLogFilePath() {
		return this.logFile.getAbsolutePath();
	}

	public String getLogFileName() {
		return this.logFile.getName();
	}

	public String getResultMetricsFilePath() {
		return this.resultMetricsFile.getAbsolutePath();
	}

	public void setDataProviderName(String dataProviderName) {
		if (dataProviderName != null) {
			if (!dataProviderName.isEmpty()) {
				this.dataProviderName = dataProviderName;
			}
		}
	}

	/**
	 * @param testMethodName
	 * @throws NullPointerException
	 *             , IllegalArgumentException, RuntimeException <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             All test methods must start with the 'startTestExecution' method.<br>
	 *             Since all log entry details are logged between a 'startTestExecution' and a 'endTestExecution',<br>
	 *             it is important to put all the test method code between these 2 methods, including variable declarations.<br>
	 */
	public void startTestExecution(String testMethodName) throws NullPointerException, IllegalArgumentException, RuntimeException {
		if (this.isTestStarted) {
			// throw new RuntimeException("TEST: " + "'" + this.testMethodName + "' WAS ALREADY STARTED.");
			System.out.println("TEST: " + "'" + this.testMethodName + "' WAS ALREADY STARTED.");
		} else {
			// beforeTest();
			if (testMethodName == null) {
				throw new NullPointerException("ARGUMENT 'testMethodName' MAKES REFERENCE TO A NULL POINTER.");
			}
			if (testMethodName.isEmpty()) {
				throw new IllegalArgumentException("ARGUMENT 'testMethodName' MAKES REFERENCE TO AN EMPTY STRING.");
			} else {
				this.testMethodName = testMethodName;
			}
			if (this.testMethods != null) {
				TestMethod testMethod = new TestMethod(testMethodName);
				if (testMethod != null) {
					if (!this.testMethods.contains(testMethod)) {
						this.testMethod = testMethod;
						this.testMethod.setTestMethodDesc(this.testMethodDesc);
						this.testMethods.add(this.testMethod);
					}
					this.testIteration = new TestIteration(this.dataProviderName);
					if (this.testMethodName.startsWith("Before") || this.testMethodName.startsWith("After")) {
						this.startFunction("Comments");
					}
					// this.testMethod.addTestIteration(this.testIteration);
					if (this.testMethod.getTestMethodName().equals(testMethodName)) {
						this.testMethod.addTestIteration(this.testIteration);
					} else {
						this.testMethod = testMethod;
						this.testMethods.add(this.testMethod);
						this.testMethod.addTestIteration(this.testIteration);
					}
					// Set "isTestStarted" flag to "true".
					this.isTestStarted = true;
					// Increment "startTestCounter" by unity.
					this.startTestCounter++;
				}
			}
		}
	}

	public void writeLogHeader() {
		BufferedWriter outputStream = null;
		try {
			if (this.logCreated) {
				if ("SingleIteration".equals(this.LogMode)) {
					try {
						outputStream = new BufferedWriter(new FileWriter(this.logFile));
						outputStream.write(this.getHeader());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// Reset "isTestStarted" to "false".
			this.isTestStarted = false;
		}
	}

	public void writeLogFooter() {
		BufferedWriter outputStream = null;
		try {
			if (this.logCreated) {
				if ("SingleIteration".equals(this.LogMode)) {
					String footer = "</body></html>";
					try {
						outputStream = new BufferedWriter(new FileWriter(this.logFile, true));
						outputStream.write(footer);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// Reset "isTestStarted" to "false".
			this.isTestStarted = false;
		}
	}

	/**
	 * @param endTestExecution
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            All test methods must end with the 'endTestExecution' method.<br>
	 *            Since all test methods have a try-catch, the 'endTestExecution' call must be executed from the 'finally' clause.<br>
	 */
	public void endTestExecution() {
		BufferedWriter outputStream = null;
		BufferedWriter outputStream1 = null;
		try {
			if (!this.isTestStarted) {
				throw new RuntimeException("TEST: " + "'" + this.testMethodName + "' WAS NOT STARTED.");
			} else {
				if (this.logCreated) {
					if ("SingleIteration".equals(this.LogMode)) {
						outputStream = new BufferedWriter(new FileWriter(this.logFile, true));
						outputStream.write(this.getTableContent());
					} else {
						if (this.testMethodName.startsWith("Before") || this.testMethodName.startsWith("After")) {
							outputStream = new BufferedWriter(new FileWriter(this.logFile));
							outputStream.write(this.getContent());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				if ("SingleIteration".equals(this.LogMode)) {
					outputStream1 = new BufferedWriter(new FileWriter(this.resultMetricsFile, true));
				} else {
					outputStream1 = new BufferedWriter(new FileWriter(this.resultMetricsFile));
				}
				outputStream1.write(this.getResults());
				outputStream1.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				this.testIteration = null;
			}

		}

		// Reset "isTestStarted" to "false".
		this.isTestStarted = false;
		// Increment "endTestCounter" by unity.
		this.endTestCounter++;

		if ("SingleIteration".equals(this.LogMode)) {
			this.testMethods.remove(0);
		}

		// afterTest();
		this.testMethodDesc = "";

	}

	/**
	 * @param message
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Logs a non-detailed entry to the log.<br>
	 *            For this entry, the columns of the log entry row are merged.<br>
	 *            Use this method to comment the log, like specifying the step number of the test case.<br>
	 *            A comment logged by this method is automatically considered a 'debug' entry.<br>
	 *            Scripters usually use this signature of the 'comment' method.<br>
	 */
	public void comment(String message) {
		// this.testIteration.addTestStep(message);
		this.testFunction.addTestStep(message);
	}

	/**
	 * @param methodName
	 * @param locatorNameOrParameter
	 * @param actionValueOrMessage
	 * @param entryStatus
	 * @param issueCategory
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            An all purpose log entry with full details. The entry can have any supported status, thus affecting the outcome of the execution
	 *            (green, yellow, red).<br>
	 *            For this entry, the log row uses the 4 log columns.<br>
	 *            Note that the 'issueCategory' is used only for a Fail/Error/Defect entry and will appear under the Entry Status column.<br>
	 *            Usually, only the framework methods use this signature, allowing it to log exceptions, actions (click, setText, etc), and more.<br>
	 */
	public void comment(String methodName, String locatorNameOrParameter, String actionValueOrMessage, int entryStatus, int issueCategory) {
		// this.testIteration.addTestStep(methodName, locatorNameOrParameter, actionValueOrMessage, validateEntryStatus(entryStatus),
		// validateIssueCategory(issueCategory));
		// this.testIteration.addTestStep(captureScreenShot(entryStatus), entryStatus);
		this.testFunction.addTestStep(methodName, locatorNameOrParameter, actionValueOrMessage, validateEntryStatus(entryStatus),
				validateIssueCategory(issueCategory));
		this.testFunction.addTestStep(captureScreenShot(entryStatus), entryStatus);
	}

	public void exception(Exception e) {
		if (e != null) {
			if (e instanceof FrameworkException) {
				String methodName = ((FrameworkException) e).getMethodName();
				String locatorNameOrParameter = ((FrameworkException) e).getLocatorNameOrParameter();
				String actionValueOrMessage = ((FrameworkException) e).getActionValueOrMessage();
				int entryStatus = ((FrameworkException) e).getEntryStatus();
				this.comment(methodName, locatorNameOrParameter, actionValueOrMessage, entryStatus, ((FrameworkException) e).getIssueCategory());
				// Only print to console if entry status is Log.ERROR
				if (entryStatus == Log.ERROR) {
					System.out.println("METHOD NAME:             " + methodName);
					System.out.println("LOCATOR OR PARAMETER:    " + locatorNameOrParameter);
					if (actionValueOrMessage.contains(Element.NEW_LINE)) {
						System.out.println();
						System.out.println("*** ACTION VALUE OR MESSAGE ***");
						System.out.println(actionValueOrMessage);
					} else {
						System.out.println("ACTION VALUE OR MESSAGE: " + actionValueOrMessage);
					}
				}
			} else {
				StackTraceElement[] elements = e.getStackTrace();
				if (elements != null) {
					if (elements.length > 0) {
						String methodName = elements[0].getMethodName();
						String className = elements[0].getClassName();
						StringBuffer messages = new StringBuffer();
						for (int i = 0; i < elements.length && i < 5; i++) {
							messages.append(elements[i].toString());
							messages.append(Element.NEW_LINE);
						}
						messages.append("... ...");
						this.comment(methodName, className, messages.toString(), Log.ERROR, Log.SCRIPT_ISSUE);
						System.out.println("METHOD NAME: " + methodName);
						System.out.println("CLASS NAME:  " + className);
						String message = e.getMessage();
						if (message != null) {
							if (message.contains(Element.NEW_LINE)) {
								System.out.println();
								System.out.println("*** MESSAGE ***");
								System.out.println(message);
							} else {
								System.out.println("MESSAGE:     " + message);
							}
						}
					}
				}
			}
			System.out.println();
			System.out.println("STACK TRACE:");
			e.printStackTrace();
			Assert.assertTrue(false, "Exception occurs: \n");
		} else {
			this.comment("exception", "EXCEPTION", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}
	}

	/**
	 * @throws Exception
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Takes a printscreen and log an entry to the log.<br>
	 *             The entry contains a small picture and link which allows the scripter to open it to normal size.<br>
	 *             NOTE: this method is automatically called whenever a Fail/Error/Defect entry is logged.<br>
	 */
	public void captureScreenShot() throws Exception {
		// Turn screen capture on
		this.captureScreenShot = true;
		this.testFunction.addTestStep(captureScreenShot(SCREEN_SHOT), SCREEN_SHOT);
	}

	/**
	 * @return the captureScreenShot USAGE: <br>
	 *         <br>
	 *         Returns "true" or "false". "true" if a screen shot is captured and "false" otherwise. <br>
	 *         <br>
	 */
	public boolean isCaptureScreenShot() {
		return captureScreenShot;
	}

	/**
	 * @param captureScreenShot
	 *            the captureScreenShot to set USAGE: <br>
	 *            <br>
	 *            Use this method to trun on screen capture (true) or<br>
	 *            to turn off screen capture (false). <br>
	 *            <br>
	 */
	public void setCaptureScreenShot(boolean captureScreenShot) {
		this.captureScreenShot = captureScreenShot;
	}

	/**
	 * @param condition
	 * @param verifyName
	 * @param passMessage
	 * @param defectList
	 * @param issueCategory
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            verifyStep is a method to validate the AUT. Because the condition parameter is a boolean, use this method as a 'yes/no' question.
	 *            <br>
	 *            It will log an entry based on the result of a condition and whether a defect has already been found.<br>
	 *            Example of usage:<br>
	 *            - To validate if a window appeared or not;<br>
	 *            - To validate if an element is present or not (not to validate text in it);<br>
	 *            <br>
	 *            verifyName: All validations must be uniquely name to help correlate the log with the code. This is always useful when a test method
	 *            fails and analysis must be done.<br>
	 *            <br>
	 *            passMessage: A useful message that tells why the validation passed or failed. When failing, the message will have the prefix 'did
	 *            not expect: '.<br>
	 *            <br>
	 *            defectList: If a failed validation reproduces a defect, add it to the method. On the next execution, the log entry will change from
	 *            'fail' to 'defect', thus speeding the fail analysis.<br>
	 *            <br>
	 *            issueCategory: A way to determine the severity of a failed validation. Like 'defectList', this parameter speeds the fail analysis.
	 *            Only appears when the validation fails.<br>
	 *            <br>
	 *            Log entry logic:<br>
	 *            - If the validation passed and 'defectList' is an empty string -> entryStatus == Pass, the last column entry will display the
	 *            'passMessage'<br>
	 *            - If the validation passed and 'defectList' is not empty -> entryStatus == Warning, the last column entry will display the
	 *            'passMessage' and the 'defectList'<br>
	 *            - If the validation failed and 'defectList' is an empty string -> entryStatus == Fail, the last column entry will display the
	 *            'failMessage'<br>
	 *            - If the validation failed and 'defectList' is not empty -> entryStatus == Defect, the last column entry will display the
	 *            'failMessage' and the 'defectList'<br>
	 */
	public boolean verifyStep(boolean condition, String verifyName, String passMessage, String defectList, int issueCategory) throws Exception {
		this.methodName = "verifyStep";
		return verifyStepHelper(condition, verifyName, passMessage, "DID NOT EXPECT: " + passMessage, defectList, issueCategory);
	}

	/**
	 * @param condition
	 * @param verifyName
	 * @param passMessage
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            verifyStep is a method to validate the AUT. Because the condition parameter is a boolean, use this method as a 'yes/no' question.
	 *            <br>
	 *            It will log an entry based on the result of a condition and whether a defect has already been found.<br>
	 *            <br>
	 *            Use this verifyStep signature when there are no defect list and assume that a failed validation is a 'major issue'.<br>
	 *            <br>
	 *            Example of usage:<br>
	 *            - To validate if a window appeared or not;<br>
	 *            - To validate if an element is present or not (not to validate text in it);<br>
	 *            <br>
	 *            verifyName: All validations must be uniquely name to help correlate the log with the code. This is always useful when a test method
	 *            fails and analysis must be done.<br>
	 *            <br>
	 *            passMessage: A useful message that tells why the validation passed or failed. When failing, the message will have the prefix 'did
	 *            not expect: '.<br>
	 *            <br>
	 *            Log entry logic:<br>
	 *            - If the validation passed and 'defectList' is an empty string -> entryStatus == Pass, the last column entry will display the
	 *            'passMessage'<br>
	 *            - If the validation passed and 'defectList' is not empty -> entryStatus == Warning, the last column entry will display the
	 *            'passMessage' and the 'defectList'<br>
	 *            - If the validation failed and 'defectList' is an empty string -> entryStatus == Fail, the last column entry will display the
	 *            'failMessage'<br>
	 *            - If the validation failed and 'defectList' is not empty -> entryStatus == Defect, the last column entry will display the
	 *            'failMessage' and the 'defectList'<br>
	 */
	public boolean verifyStep(boolean condition, String verifyName, String passMessage) throws Exception {
		this.methodName = "verifyStep";
		boolean res = false;
		if (condition == true) {
			res = verifyStepHelper(condition, verifyName, passMessage, "DID NOT EXPECT: " + passMessage, "", Log.NO_ISSUE);
		} else {
			res = verifyStepHelper(condition, verifyName, passMessage, "DID NOT EXPECT: " + passMessage, "", Log.MAJOR_ISSUE);
		}
		return res;
	}

	/**
	 * @param condition
	 * @param verifyName
	 * @param passMessage
	 * @param defectList
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            verifyStep is a method to validate the AUT. Because the condition parameter is a boolean, use this method as a 'yes/no' question.
	 *            <br>
	 *            It will log an entry based on the result of a condition and whether a defect has already been found.<br>
	 *            <br>
	 *            Use this verifyStep signature when there is a defect and assume that it is a 'major issue'.<br>
	 *            <br>
	 *            Example of usage:<br>
	 *            - To validate if a window appeared or not;<br>
	 *            - To validate if an element is present or not (not to validate text in it);<br>
	 *            <br>
	 *            verifyName: All validations must be uniquely name to help correlate the log with the code. This is always useful when a test method
	 *            fails and analysis must be done.<br>
	 *            <br>
	 *            passMessage: A useful message that tells why the validation passed or failed. When failing, the message will have the prefix 'did
	 *            not expect: '.<br>
	 *            <br>
	 *            defectList: If a failed validation reproduces a defect, add it to the method. On the next execution, the log entry will change from
	 *            'fail' to 'defect', thus speeding the fail analysis.<br>
	 *            <br>
	 *            Log entry logic:<br>
	 *            - If the validation passed and 'defectList' is an empty string -> entryStatus == Pass, the last column entry will display the
	 *            'passMessage'<br>
	 *            - If the validation passed and 'defectList' is not empty -> entryStatus == Warning, the last column entry will display the
	 *            'passMessage' and the 'defectList'<br>
	 *            - If the validation failed and 'defectList' is an empty string -> entryStatus == Fail, the last column entry will display the
	 *            'failMessage'<br>
	 *            - If the validation failed and 'defectList' is not empty -> entryStatus == Defect, the last column entry will display the
	 *            'failMessage' and the 'defectList'<br>
	 */
	public boolean verifyStep(boolean condition, String verifyName, String passMessage, String defectList) throws Exception {
		this.methodName = "verifyStep";
		boolean res = false;
		if (condition == true) {
			res = verifyStepHelper(condition, verifyName, passMessage, "DID NOT EXPECT: " + passMessage, defectList, Log.NO_ISSUE);
		} else {
			res = verifyStepHelper(condition, verifyName, passMessage, "DID NOT EXPECT: " + passMessage, defectList, Log.MAJOR_ISSUE);
		}
		return res;
	}

	/**
	 * @param actualValue
	 * @param operator
	 * @param expectedValue
	 * @param verifyName
	 * @param expectedMessagePrefix
	 * @param actualMessagePrefix
	 * @param defectList
	 * @param issueCategory
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            verifyCompare is a method to validate the AUT. Use this method to compare 2 values with an operator. The result is an expression.
	 *            <br>
	 *            When using verifyCompare, think 'expected' versus 'actual' values.<br>
	 *            It will log an entry based on the result of the expression and whether a defect has already been found.<br>
	 *            Example of usage:<br>
	 *            - To compare an expected error message with the actual displayed by the application;<br>
	 *            - To compare a calculated value with one from the application;<br>
	 *            - To compare a value from a data provider with one displayed in the application;<br>
	 *            <br>
	 *            verifyName: All validations must be uniquely name to help correlate the log with the code. This is always useful when a test method
	 *            fails and analysis must be done.<br>
	 *            <br>
	 *            expectedMessagePrefix and actualMessagePrefix: A useful message that details the value used in the expression. Both expected and
	 *            actual values are displayed, whether the validation passed or failed.<br>
	 *            <br>
	 *            defectList: If a failed validation reproduces a defect, add it to the method. On the next execution, the log entry will change from
	 *            'fail' to 'defect', thus speeding the fail analysis.<br>
	 *            <br>
	 *            issueCategory: A way to determine the severity of a failed validation. Like 'defectList', this parameter speeds the fail analysis.
	 *            Only appears when the validation fails.<br>
	 *            <br>
	 *            Log entry logic:<br>
	 *            - If the validation passed and 'defectList' is an empty string -> entryStatus == Pass, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation passed and 'defectList' is not empty -> entryStatus == Warning, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 *            - If the validation failed and 'defectList' is an empty string -> entryStatus == Fail, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation failed and 'defectList' is not empty -> entryStatus == Defect, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 */
	public boolean verifyCompare(String actualValue, String operator, String expectedValue, String verifyName, String expectedMessagePrefix,
			String actualMessagePrefix, String defectList, int issueCategory) throws Exception {
		this.methodName = "verifyCompare";
		boolean condition = false;

		validateExpectedActualValues(expectedValue, actualValue);
		validateSupportedOperator(operator);

		if (operator.equals("<") || operator.equals("<=") || operator.equals(">") || operator.equals(">=")) {
			throw new UnsupportedOperationException("*** THE FOLLOWING OPERATIONS ARE SUPPORTED: '==' AND '!=' ***");
		} else {
			if (operator.equals("==")) {
				condition = expectedValue.equals(actualValue);
			} else {
				// user entered "!=" operator
				condition = !(expectedValue.equals(actualValue));
			}
		}

		String message = validateExpectedMessagePrefix(expectedMessagePrefix) + Element.SPACE + "'" + expectedValue + "'" + Element.NEW_LINE
				+ validateActualMessagePrefix(actualMessagePrefix) + Element.SPACE + "'" + actualValue + "'";

		return verifyStepHelper(condition, verifyName, message, message, defectList, issueCategory);
	}

	/**
	 * @param actualValue
	 * @param operator
	 * @param expectedValue
	 * @param verifyName
	 * @param expectedMessagePrefix
	 * @param actualMessagePrefix
	 * @param defectList
	 * @param issueCategory
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            verifyCompare is a method to validate the AUT. Use this method to compare 2 values with an operator. The result is an expression.
	 *            <br>
	 *            When using verifyCompare, think 'expected' versus 'actual' values.<br>
	 *            It will log an entry based on the result of the expression and whether a defect has already been found.<br>
	 *            Example of usage:<br>
	 *            - To compare an expected error message with the actual displayed by the application;<br>
	 *            - To compare a calculated value with one from the application;<br>
	 *            - To compare a value from a data provider with one displayed in the application;<br>
	 *            <br>
	 *            verifyName: All validations must be uniquely name to help correlate the log with the code. This is always useful when a test method
	 *            fails and analysis must be done.<br>
	 *            <br>
	 *            expectedMessagePrefix and actualMessagePrefix: A useful message that details the value used in the expression. Both expected and
	 *            actual values are displayed, whether the validation passed or failed.<br>
	 *            <br>
	 *            defectList: If a failed validation reproduces a defect, add it to the method. On the next execution, the log entry will change from
	 *            'fail' to 'defect', thus speeding the fail analysis.<br>
	 *            <br>
	 *            issueCategory: A way to determine the severity of a failed validation. Like 'defectList', this parameter speeds the fail analysis.
	 *            Only appears when the validation fails.<br>
	 *            <br>
	 *            Log entry logic:<br>
	 *            - If the validation passed and 'defectList' is an empty string -> entryStatus == Pass, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation passed and 'defectList' is not empty -> entryStatus == Warning, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 *            - If the validation failed and 'defectList' is an empty string -> entryStatus == Fail, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation failed and 'defectList' is not empty -> entryStatus == Defect, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 */
	public boolean verifyCompare(int actualValue, String operator, int expectedValue, String verifyName, String actualMessagePrefix,
			String expectedMessagePrefix, String defectList, int issueCategory) throws Exception {
		this.methodName = "verifyCompare";
		boolean condition = false;

		validateSupportedOperator(operator);

		if (operator.equals("<")) {
			condition = actualValue < expectedValue;
		} else if (operator.equals("<=")) {
			condition = actualValue <= expectedValue;
		} else if (operator.equals(">")) {
			condition = actualValue > expectedValue;
		} else if (operator.equals(">=")) {
			condition = actualValue >= expectedValue;
		} else if (operator.equals("==")) {
			condition = actualValue == expectedValue;
		} else {
			condition = actualValue != expectedValue;
		}

		String message = validateExpectedMessagePrefix(expectedMessagePrefix) + "'" + expectedValue + "'" + Element.NEW_LINE
				+ validateActualMessagePrefix(actualMessagePrefix) + "'" + actualValue + "'";

		return verifyStepHelper(condition, verifyName, message, message, defectList, issueCategory);

	}

	/**
	 * @param actualValue
	 * @param expectedValue
	 * @param verifyName
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            verifyCompare is a method to validate the AUT. Use this method to compare 2 values with an operator. The result is an expression.
	 *            <br>
	 *            When using verifyCompare, think 'expected' versus 'actual' values.<br>
	 *            It will log an entry based on the result of the expression and whether a defect has already been found.<br>
	 *            Example of usage:<br>
	 *            - To compare an expected error message with the actual displayed by the application;<br>
	 *            - To compare a calculated value with one from the application;<br>
	 *            - To compare a value from a data provider with one displayed in the application;<br>
	 *            <br>
	 *            Use this verifyCompare signature to validate that both expected and actual values are equal, when there is no defect, and assume
	 *            that it is a 'major issue'.<br>
	 *            Since no prefix messages will be logged, it is highly recommended to use a descriptive verifyName.<br>
	 *            <br>
	 *            verifyName: All validations must be uniquely name to help correlate the log with the code. This is always useful when a test method
	 *            fails and analysis must be done.<br>
	 *            <br>
	 *            Log entry logic:<br>
	 *            - If the validation passed and 'defectList' is an empty string -> entryStatus == Pass, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation passed and 'defectList' is not empty -> entryStatus == Warning, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 *            - If the validation failed and 'defectList' is an empty string -> entryStatus == Fail, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation failed and 'defectList' is not empty -> entryStatus == Defect, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 */
	public boolean verifyCompare(String actualValue, String expectedValue, String verifyName) throws Exception {
		this.methodName = "verifyCompare";
		boolean condition = false;
		boolean res = false;

		// validateExpectedActualValues(expectedValue, actualValue);
		// condition = expectedValue.equals(actualValue);
		condition = compareExpectedActualValues(expectedValue, actualValue);

		String message = "E: '" + expectedValue + "'" + Element.NEW_LINE + "A: '" + actualValue + "'";

		if (condition == true) {
			res = verifyStepHelper(condition, verifyName, message, message, "", Log.NO_ISSUE);
		} else {
			res = verifyStepHelper(condition, verifyName, message, message, "", Log.MAJOR_ISSUE);
		}

		return res;
	}

	public boolean matchText(String actualValue, String regEx, String verifyName) throws Exception {
		this.methodName = "verifyStartWith";
		boolean condition = false;
		boolean res = false;

		condition = patternMatch(regEx, actualValue);

		String message = "regEx: '" + regEx + "'" + Element.NEW_LINE + "Text: '" + actualValue + "'";

		if (condition == true) {
			res = verifyStepHelper(condition, verifyName, message, message, "", Log.NO_ISSUE);
		} else {
			res = verifyStepHelper(condition, verifyName, message, message, "", Log.MAJOR_ISSUE);
		}

		return res;
	}

	/**
	 * @param actualValue
	 * @param expectedValue
	 * @param verifyName
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            verifyCompare is a method to validate the AUT. Use this method to compare 2 values with an operator. The result is an expression.
	 *            <br>
	 *            When using verifyCompare, think 'expected' versus 'actual' values.<br>
	 *            It will log an entry based on the result of the expression and whether a defect has already been found.<br>
	 *            Example of usage:<br>
	 *            - To compare an expected error message with the actual displayed by the application;<br>
	 *            - To compare a calculated value with one from the application;<br>
	 *            - To compare a value from a data provider with one displayed in the application;<br>
	 *            <br>
	 *            Use this verifyCompare signature to validate that both expected and actual values are equal, when there is no defect, and assume
	 *            that it is a 'major issue'.<br>
	 *            Since no prefix messages will be logged, it is highly recommended to use a descriptive verifyName.<br>
	 *            <br>
	 *            verifyName: All validations must be uniquely name to help correlate the log with the code. This is always useful when a test method
	 *            fails and analysis must be done.<br>
	 *            <br>
	 *            Log entry logic:<br>
	 *            - If the validation passed and 'defectList' is an empty string -> entryStatus == Pass, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation passed and 'defectList' is not empty -> entryStatus == Warning, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 *            - If the validation failed and 'defectList' is an empty string -> entryStatus == Fail, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation failed and 'defectList' is not empty -> entryStatus == Defect, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 */
	public boolean verifyCompare(int actualValue, int expectedValue, String verifyName) throws Exception {
		this.methodName = "verifyCompare";
		boolean condition = false;
		boolean res = false;

		condition = actualValue == expectedValue;

		String message = "E: '" + expectedValue + "'" + Element.NEW_LINE + "A: '" + actualValue + "'";

		if (condition == true) {
			res = verifyStepHelper(condition, verifyName, message, message, "", Log.NO_ISSUE);
		} else {
			res = verifyStepHelper(condition, verifyName, message, message, "", Log.MAJOR_ISSUE);
		}

		return res;

	}

	/**
	 * @param actualValue
	 * @param expectedValue
	 * @param verifyName
	 * @param defectList
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            verifyCompare is a method to validate the AUT. Use this method to compare 2 values with an operator. The result is an expression.
	 *            <br>
	 *            When using verifyCompare, think 'expected' versus 'actual' values.<br>
	 *            It will log an entry based on the result of the expression and whether a defect has already been found.<br>
	 *            Example of usage:<br>
	 *            - To compare an expected error message with the actual displayed by the application;<br>
	 *            - To compare a calculated value with one from the application;<br>
	 *            - To compare a value from a data provider with one displayed in the application;<br>
	 *            <br>
	 *            Use this verifyCompare signature to validate that both expected and actual values are equal, when there is a defect, and assume that
	 *            it is a 'major issue'.<br>
	 *            Since no prefix messages will be logged, it is highly recommended to use a descriptive verifyName.<br>
	 *            <br>
	 *            verifyName: All validations must be uniquely name to help correlate the log with the code. This is always useful when a test method
	 *            fails and analysis must be done.<br>
	 *            <br>
	 *            defectList: If a failed validation reproduces a defect, add it to the method. On the next execution, the log entry will change from
	 *            'fail' to 'defect', thus speeding the fail analysis.<br>
	 *            <br>
	 *            Log entry logic:<br>
	 *            - If the validation passed and 'defectList' is an empty string -> entryStatus == Pass, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation passed and 'defectList' is not empty -> entryStatus == Warning, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 *            - If the validation failed and 'defectList' is an empty string -> entryStatus == Fail, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation failed and 'defectList' is not empty -> entryStatus == Defect, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 */
	public boolean verifyCompare(String actualValue, String expectedValue, String verifyName, String defectList) throws Exception {
		this.methodName = "verifyCompare";
		boolean condition = false;
		boolean res = false;

		// validateExpectedActualValues(expectedValue, actualValue);
		// condition = expectedValue.equals(actualValue);
		condition = compareExpectedActualValues(expectedValue, actualValue);

		String message = "E: '" + expectedValue + "'" + Element.NEW_LINE + "A: '" + actualValue + "'";

		if (condition == true) {
			res = verifyStepHelper(condition, verifyName, message, message, defectList, Log.NO_ISSUE);
		} else {
			res = verifyStepHelper(condition, verifyName, message, message, defectList, Log.MAJOR_ISSUE);
		}

		return res;
	}

	/**
	 * @param actualValue
	 * @param expectedValue
	 * @param verifyName
	 * @param defectList
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            verifyCompare is a method to validate the AUT. Use this method to compare 2 values with an operator. The result is an expression.
	 *            <br>
	 *            When using verifyCompare, think 'expected' versus 'actual' values.<br>
	 *            It will log an entry based on the result of the expression and whether a defect has already been found.<br>
	 *            Example of usage:<br>
	 *            - To compare an expected error message with the actual displayed by the application;<br>
	 *            - To compare a calculated value with one from the application;<br>
	 *            - To compare a value from a data provider with one displayed in the application;<br>
	 *            <br>
	 *            Use this verifyCompare signature to validate that both expected and actual values are equal, when there is a defect, and assume that
	 *            it is a 'major issue'.<br>
	 *            Since no prefix messages will be logged, it is highly recommended to use a descriptive verifyName.<br>
	 *            <br>
	 *            verifyName: All validations must be uniquely name to help correlate the log with the code. This is always useful when a test method
	 *            fails and analysis must be done.<br>
	 *            <br>
	 *            defectList: If a failed validation reproduces a defect, add it to the method. On the next execution, the log entry will change from
	 *            'fail' to 'defect', thus speeding the fail analysis.<br>
	 *            <br>
	 *            Log entry logic:<br>
	 *            - If the validation passed and 'defectList' is an empty string -> entryStatus == Pass, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation passed and 'defectList' is not empty -> entryStatus == Warning, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 *            - If the validation failed and 'defectList' is an empty string -> entryStatus == Fail, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation failed and 'defectList' is not empty -> entryStatus == Defect, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 */
	public boolean verifyCompare(int actualValue, int expectedValue, String verifyName, String defectList) throws Exception {
		this.methodName = "verifyCompare";
		boolean condition = false;
		boolean res = false;

		condition = actualValue == expectedValue;

		String message = "E: '" + expectedValue + "'" + Element.NEW_LINE + "A: '" + actualValue + "'";

		if (condition == true) {
			res = verifyStepHelper(condition, verifyName, message, message, defectList, Log.NO_ISSUE);
		} else {
			res = verifyStepHelper(condition, verifyName, message, message, defectList, Log.MAJOR_ISSUE);
		}

		return res;

	}

	/**
	 * @param actualValue
	 * @param expectedValue
	 * @param verifyName
	 * @param defectList
	 * @param issueCategory
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            verifyCompare is a method to validate the AUT. Use this method to compare 2 values with an operator. The result is an expression.
	 *            <br>
	 *            When using verifyCompare, think 'expected' versus 'actual' values.<br>
	 *            It will log an entry based on the result of the expression and whether a defect has already been found.<br>
	 *            Example of usage:<br>
	 *            - To compare an expected error message with the actual displayed by the application;<br>
	 *            - To compare a calculated value with one from the application;<br>
	 *            - To compare a value from a data provider with one displayed in the application;<br>
	 *            <br>
	 *            Use this verifyCompare signature to validate that both expected and actual values are equal. The defectList and issueCategory are
	 *            available for the scripters to set.<br>
	 *            Since no prefix messages will be logged, it is highly recommended to use a descriptive verifyName.<br>
	 *            <br>
	 *            verifyName: All validations must be uniquely name to help correlate the log with the code. This is always useful when a test method
	 *            fails and analysis must be done.<br>
	 *            <br>
	 *            defectList: If a failed validation reproduces a defect, add it to the method. On the next execution, the log entry will change from
	 *            'fail' to 'defect', thus speeding the fail analysis.<br>
	 *            <br>
	 *            Log entry logic:<br>
	 *            - If the validation passed and 'defectList' is an empty string -> entryStatus == Pass, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation passed and 'defectList' is not empty -> entryStatus == Warning, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 *            - If the validation failed and 'defectList' is an empty string -> entryStatus == Fail, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation failed and 'defectList' is not empty -> entryStatus == Defect, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 */
	public boolean verifyCompare(String actualValue, String expectedValue, String verifyName, String defectList, int issueCategory) throws Exception {
		this.methodName = "verifyCompare";
		boolean condition = false;
		boolean res = false;

		// validateExpectedActualValues(expectedValue, actualValue);
		// condition = expectedValue.equals(actualValue);
		condition = compareExpectedActualValues(expectedValue, actualValue);

		String message = "E: '" + expectedValue + "'" + Element.NEW_LINE + "A: '" + actualValue + "'";

		if (condition == true) {
			res = verifyStepHelper(condition, verifyName, message, message, defectList, Log.NO_ISSUE);
		} else {
			res = verifyStepHelper(condition, verifyName, message, message, defectList, Log.MAJOR_ISSUE);
		}

		return res;
	}

	/**
	 * @param actualValue
	 * @param expectedValue
	 * @param verifyName
	 * @param defectList
	 * @param issueCategory
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            verifyCompare is a method to validate the AUT. Use this method to compare 2 values with an operator. The result is an expression.
	 *            <br>
	 *            When using verifyCompare, think 'expected' versus 'actual' values.<br>
	 *            It will log an entry based on the result of the expression and whether a defect has already been found.<br>
	 *            Example of usage:<br>
	 *            - To compare an expected error message with the actual displayed by the application;<br>
	 *            - To compare a calculated value with one from the application;<br>
	 *            - To compare a value from a data provider with one displayed in the application;<br>
	 *            <br>
	 *            Use this verifyCompare signature to validate that both expected and actual values are equal. The defectList and issueCategory are
	 *            available for the scripters to set.<br>
	 *            Since no prefix messages will be logged, it is highly recommended to use a descriptive verifyName.<br>
	 *            <br>
	 *            verifyName: All validations must be uniquely name to help correlate the log with the code. This is always useful when a test method
	 *            fails and analysis must be done.<br>
	 *            <br>
	 *            defectList: If a failed validation reproduces a defect, add it to the method. On the next execution, the log entry will change from
	 *            'fail' to 'defect', thus speeding the fail analysis.<br>
	 *            <br>
	 *            Log entry logic:<br>
	 *            - If the validation passed and 'defectList' is an empty string -> entryStatus == Pass, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation passed and 'defectList' is not empty -> entryStatus == Warning, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 *            - If the validation failed and 'defectList' is an empty string -> entryStatus == Fail, the last column entry will display the
	 *            expected message and actual message<br>
	 *            - If the validation failed and 'defectList' is not empty -> entryStatus == Defect, the last column entry will display the expected
	 *            message and actual message and the 'defectList'<br>
	 */
	public boolean verifyCompare(int actualValue, int expectedValue, String verifyName, String defectList, int issueCategory) throws Exception {
		this.methodName = "verifyCompare";
		boolean condition = false;
		boolean res = false;

		condition = actualValue == expectedValue;

		String message = "E: '" + expectedValue + "'" + Element.NEW_LINE + "A: '" + actualValue + "'";

		if (condition == true) {
			res = verifyStepHelper(condition, verifyName, message, message, defectList, Log.NO_ISSUE);
		} else {
			res = verifyStepHelper(condition, verifyName, message, message, defectList, Log.MAJOR_ISSUE);
		}

		return res;
	}

	// public void checkAllTestMethodsExecuted(String testMethodName, int numberOfIncludeTestMethods) {
	// if(!(this.startTestCounter == numberOfIncludeTestMethods && this.endTestCounter == numberOfIncludeTestMethods) ||
	// (this.startTestCounter == 0
	// && this.endTestCounter == 0 && numberOfIncludeTestMethods == 0)) {
	// String newLine = System.getProperty("line.separator");
	// String errorMessage = "ALL TEST METHODS WERE NOT EXECUTED";
	// if(this.startTestCounter == this.endTestCounter) {
	// errorMessage += newLine;
	// errorMessage += "NUMBER OF INCLUDE TEST METHODS: " + "'" +
	// String.valueOf(numberOfIncludeTestMethods) + "'";
	// errorMessage += newLine;
	// errorMessage += "NUMBER OF INCLUDE TEST METHODS EXECUTED: " + "'" + String.valueOf(this.startTestCounter) +
	// "'";
	// errorMessage += newLine;
	// errorMessage += "NUMBER OF INCLUDE TEST METHODS NOT EXECUTED: " + "'" + String.valueOf(numberOfIncludeTestMethods
	// - this.startTestCounter) +
	// "'";
	// }
	// this.startTestExecution(testMethodName);
	// this.comment("", "", errorMessage, ERROR, SCRIPT_ISSUE);
	// this.endTestExecution();
	// }
	// }

	private String validateExpectedMessagePrefix(String expectedMessagePrefix) throws NullPointerException {
		String res = "E: ";
		if (expectedMessagePrefix == null) {
			throw new NullPointerException("expectedMessagePrefix");
		}
		if (!expectedMessagePrefix.isEmpty()) {
			res += expectedMessagePrefix;
		}
		return res;
	}

	private String validateActualMessagePrefix(String actualMessagePrefix) throws NullPointerException {
		String res = "A: ";
		if (actualMessagePrefix == null) {
			throw new NullPointerException("actualMessagePrefix");
		}
		if (!actualMessagePrefix.isEmpty()) {
			res += actualMessagePrefix;
		}
		return res;
	}

	private void validateExpectedActualValues(String expectedValue, String actualValue) throws NullPointerException {
		if (expectedValue == null) {
			throw new NullPointerException("expectedValue");
		}

		if (actualValue == null) {
			throw new NullPointerException("actualValue");
		}
	}

	private boolean compareExpectedActualValues(String expectedValue, String actualValue) {
		boolean result = false;
		if (actualValue == null || actualValue.equals(" ")) {
			actualValue = "";
		}
		result = expectedValue.equals(actualValue);
		return result;
	}

	private boolean patternMatch(String regEx, String actualValue) {
		boolean result = false;
		if (actualValue == null || actualValue.equals(" ")) {
			actualValue = "";
		}
		Pattern ptn = Pattern.compile(regEx);
		Matcher mat = ptn.matcher(actualValue);
		result = mat.find();
		return result;

	}

	private void validateSupportedOperator(String operator) throws NullPointerException, IllegalArgumentException {
		if (operator == null) {
			throw new NullPointerException("operator");
		}

		if (!isOperatorSupported(operator)) {
			StringBuilder errorMessage = new StringBuilder();
			int counter = 1;
			errorMessage.append("*** VALID ARGUMENT OPERATORS ***");
			errorMessage.append(Element.NEW_LINE);
			for (String supportedOperator : supportedOperators) {
				errorMessage.append(String.valueOf(counter) + ": " + "'" + supportedOperator + "'");
				errorMessage.append(Element.NEW_LINE);
				counter++;
			}
			throw new IllegalArgumentException(errorMessage.toString());
		}
	}

	private boolean isOperatorSupported(String operator) {
		boolean res = false;
		for (String supportedOperator : supportedOperators) {
			if (supportedOperator.equals(operator)) {
				res = true;
				break;
			}
		}
		return res;
	}

	private boolean verifyStepHelper(boolean condition, String verifyName, String passMessage, String failMessage, String defectList,
			int issueCategory) throws Exception {
		String locatorNameOrParameter = verifyName;
		String actionValueOrMessage = "";
		String defaultPassMessage = "*** PLEASE SPECIFY A 'PASS MESSAGE' ***";
		String defaultFailMessage = "*** PLEASE SPECIFY A 'FAIL MESSAGE' ***";
		int entryStatus = 0;

		if (verifyName == null) {
			throw new NullPointerException("verifyName");
		}

		if (passMessage == null) {
			throw new NullPointerException("passMessage");
		}

		if (failMessage == null) {
			throw new NullPointerException("failMessage");
		}

		if (defectList == null) {
			throw new NullPointerException("defectList");
		}

		if (passMessage.isEmpty()) {
			passMessage = defaultPassMessage;
		}

		if (failMessage.isEmpty()) {
			failMessage = defaultFailMessage;
		}

		if (condition == true) {
			if (defectList.isEmpty()) {
				entryStatus = Log.PASS;
				actionValueOrMessage = passMessage;
			} else {
				entryStatus = Log.WARN;
				actionValueOrMessage = passMessage + Element.NEW_LINE + "Possible defect to close:" + Element.SPACE + defectList;
			}
		} else {
			if (defectList.isEmpty()) {
				entryStatus = Log.FAIL;
				actionValueOrMessage = failMessage;
			} else {
				entryStatus = Log.DEFECT;
				actionValueOrMessage = failMessage + Element.NEW_LINE + "Defect:" + Element.SPACE + defectList;
			}
		}

		this.comment(methodName, locatorNameOrParameter, actionValueOrMessage, entryStatus, issueCategory);

		if (condition == true) {
			// Reporter.log(verifyName + "</br>" + passMessage + "</br>*pass*</br>====================</br>");
			Reporter.log("*passed*  " + verifyName + " ==> " + passMessage + "</br>");
			softAssert.assertTrue(condition, verifyName + NEW_LINE + passMessage + NEW_LINE);
		} else {
			try {
				Reporter.log("*failed*  " + verifyName + " ==> " + failMessage + "</br>");
				softAssert.assertTrue(condition, verifyName + NEW_LINE + failMessage + NEW_LINE);
			} catch (AssertionError e) {
				// Reporter.log("<font color=\"red\" size=10>*fail*</font></br>====================</br>");
				// Reporter.log("*failed*" + "</br>");
				e.printStackTrace();
			} finally {
				// Reporter.log(verifyName + "</br>" + failMessage + "</br>");
				// Reporter.log("*failed* " + verifyName + " ==> " + failMessage + "</br>");
				// Reporter.setCurrentTestResult(ITestResult.FAILURE);
			}
		}
		return condition;

	}

	public void beforeTest() {
		if (Reporter.getCurrentTestResult() != null) {
			System.out.println(NEW_LINE + ">>>>>>>>>> " + Reporter.getCurrentTestResult().getMethod().getMethodName() + " <<<<<<<<<<" + NEW_LINE);
			Reporter.getOutput(Reporter.getCurrentTestResult()).clear();
			this.testMethodDesc = Reporter.getCurrentTestResult().getMethod().getDescription();
			softAssert = new SoftAssert();
		}
	}

	public void afterTest() {
		if (Reporter.getCurrentTestResult() != null) {
			System.out.println(NEW_LINE + "<<<<<<<<<< " + Reporter.getCurrentTestResult().getMethod().getMethodName() + " >>>>>>>>>>" + NEW_LINE);
			softAssert.assertAll();
			// List<String> msg = Reporter.getOutput(Reporter.getCurrentTestResult());
			// for (int i = 0; i < msg.size(); i++) {
			// if (msg.get(i).contains("*failed*")) {
			// Assert.fail();
			//
			// // softassert.assertAll();
			// // Assert.assertTrue(false, "Test failed! ");
			// // Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
			// }
			// }
		}
	}

	public void startFunction(String functionName) {
		if (this.testFunction == null || this.testFunction.gettestFunctionName().equals("Comments")) {
			this.testFunction = new TestFunction(functionName);
			this.testIteration.addFunction(this.testFunction);
		} else {
			throw new FrameworkException("Log", "startFunction", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}
	}

	public void endFunction() {
		this.testFunction = this.testIteration.getFunction("Comments");

	}

	// pthisublic void endFunction(String methodName) {
	// .testIteration.addTestStep(methodName, locatorNameOrParameter, actionValueOrMessage, validateEntryStatus(entryStatus),
	// validateIssueCategory(issueCategory));
	// }

	private String getResults() {
		StringBuilder resultsStr = new StringBuilder();
		for (TestMethod testMethod : testMethods) {
			testMethod.getEntryStatusColor();
			String testName = testMethod.getTestMethodName();
			if (testName.contains(":")) {
				String[] nameArray = testName.split(":");
				resultsStr.append(nameArray[0].replaceFirst("#", ""));
			} else {
				resultsStr.append(testName.replaceFirst("#", ""));
			}
			resultsStr.append("=");
			resultsStr.append(testMethod.getEntryStatus().toLowerCase());
			resultsStr.append(NEW_LINE);

		}
		this.testResults = resultsStr.toString();
		return testResults;

	}

	private String getHeader() {
		this.htmlElement = new HTMLElement();
		this.htmlHeadElement = new HTMLHeadElement();
		this.createHtmlHeader();
		return htmlElement.getContents() + "<Body>";
	}

	private String getContent() {
		this.htmlElement = new HTMLElement();
		this.htmlHeadElement = new HTMLHeadElement();
		this.createHtmlHeader();

		HTMLBodyElement htmlBodyElement = new HTMLBodyElement();

		for (TestMethod testMethod : testMethods) {
			HTMLUnOrderedListElement htmlUnOrderedListElementMKTreeTestCase = new HTMLUnOrderedListElement(
					Log.MKTREE_CSS.replaceAll(".css", "").trim());
			htmlUnOrderedListElementMKTreeTestCase.append(testMethod.getContent());
			HTMLTableCellElement htmlTableCellElementTestCase = new HTMLTableCellElement();
			htmlTableCellElementTestCase.append(htmlUnOrderedListElementMKTreeTestCase);
			HTMLTableRowElement htmlTableRowElementTestCase = new HTMLTableRowElement(testMethod.getEntryStatusColor());
			htmlTableRowElementTestCase.append(htmlTableCellElementTestCase);
			HTMLTableElement htmlTableElementTestCase = new HTMLTableElement("", 1, 0, 0, 100);
			htmlTableElementTestCase.append(htmlTableRowElementTestCase);
			HTMLTableCellElement htmlTableCellElement = new HTMLTableCellElement();
			htmlTableCellElement.append(htmlTableElementTestCase);
			HTMLTableRowElement logTableBody = new HTMLTableRowElement();
			logTableBody.append(htmlTableCellElement);
			HTMLTableElement tableBody = new HTMLTableElement("", 1, 0, 0, 100);
			tableBody.append(logTableBody);
			htmlBodyElement.append(tableBody);
		}

		htmlElement.append(htmlBodyElement);

		return htmlElement.getContent();
	}

	private String getTableContent() {
		HTMLTableElement tableBody = new HTMLTableElement("", 1, 0, 0, 100);

		for (TestMethod testMethod : testMethods) {
			HTMLUnOrderedListElement htmlUnOrderedListElementMKTreeTestCase = new HTMLUnOrderedListElement(
					Log.MKTREE_CSS.replaceAll(".css", "").trim());
			htmlUnOrderedListElementMKTreeTestCase.append(testMethod.getContent());
			HTMLTableCellElement htmlTableCellElementTestCase = new HTMLTableCellElement();
			htmlTableCellElementTestCase.append(htmlUnOrderedListElementMKTreeTestCase);
			HTMLTableRowElement htmlTableRowElementTestCase = new HTMLTableRowElement(testMethod.getEntryStatusColor());
			htmlTableRowElementTestCase.append(htmlTableCellElementTestCase);
			HTMLTableElement htmlTableElementTestCase = new HTMLTableElement("", 1, 0, 0, 100);
			htmlTableElementTestCase.append(htmlTableRowElementTestCase);
			HTMLTableCellElement htmlTableCellElement = new HTMLTableCellElement();
			htmlTableCellElement.append(htmlTableElementTestCase);
			HTMLTableRowElement logTableBody = new HTMLTableRowElement();
			logTableBody.append(htmlTableCellElement);
			tableBody.append(logTableBody);

		}

		return tableBody.getContent();

	}

	private void init(String logFile) {
		try {
			this.mkTreeURLJS = new File("..//AutomationFramework/src/main/java/com/cisco/framework/utilities/logging/" + MKTREE_JS);
			this.mkTreeURLCSS = new File("..//AutomationFramework/src/main/java/com/cisco/framework/utilities/logging/" + MKTREE_CSS);
			this.bulletURLGIF = new File("..//AutomationFramework/src/main/java/com/cisco/framework/utilities/logging/" + BULLET_GIF);
			this.minusURLGIF = new File("..//AutomationFramework/src/main/java/com/cisco/framework/utilities/logging/" + MINUS_GIF);
			this.plusURLGIF = new File("..//AutomationFramework/src/main/java/com/cisco/framework/utilities/logging/" + PLUS_GIF);
			this.logFile = new File(logFile);
			this.logCreated = createLogWriterAndScreenShotsAndResourcesFolder();
			this.resultMetricsFile = new File(this.logFile.getAbsolutePath().replace(".html", ".txt"));
		} catch (Exception e) {
			this.logCreated = false;
			e.printStackTrace();
		}
	}

	private void createHtmlHeader() {
		this.htmlHeadElement.append(new HTMLScriptElement(RESOURCES_FOLDER + "/" + mkTreeURLJS.getName()));
		this.htmlHeadElement.append(new HTMLLinkDocumentElement(RESOURCES_FOLDER + "/" + mkTreeURLCSS.getName()));
		this.htmlElement.append(htmlHeadElement);
	}

	private boolean createLogWriterAndScreenShotsAndResourcesFolder() throws Exception {
		boolean res = false;

		// Get the parent folder
		String parent = this.logFile.getParent();
		if (parent == null) {
			throw new NullPointerException("*** INVALID HTML LOG FILE ***");
		}

		// Create the "screenshots" folder in the parent folder.
		String parentScreenShotsFolder = parent.concat(File.separatorChar + SCREEN_SHOTS_FOLDER);
		screenShotsFolder = new File(parentScreenShotsFolder);
		if (!screenShotsFolder.exists()) {
			res = screenShotsFolder.mkdirs();
			// Insure that the screenshots folder has been created in the parent folder.
			if (!res) {
				throw new Exception("*** UNABLE TO CREATE: " + "'" + parentScreenShotsFolder + "' ***");
			}
		}

		// Create the "resources" folder in the parent folder.
		String parentResourcesFolder = parent.concat(File.separatorChar + RESOURCES_FOLDER);
		resourcesFolder = new File(parentResourcesFolder);
		if (!resourcesFolder.exists()) {
			res = resourcesFolder.mkdirs();
			if (!res) {
				// Folder "resources" has not been created in the parent folder.
				// Throw appropriate exception
				throw new Exception("*** UNABLE TO CREATE: " + "'" + parentResourcesFolder + "' ***");
			}
		}

		// Copy file "mktree.js" to folder "resources".
		copyFile(mkTreeURLJS.getAbsolutePath(), resourcesFolder.getAbsolutePath() + "\\" + mkTreeURLJS.getName(), false);

		// Copy file "mktree.css" to folder "resources".
		copyFile(mkTreeURLCSS.getAbsolutePath(), resourcesFolder.getAbsolutePath() + "\\" + mkTreeURLCSS.getName(), false);

		// Copy file "bullet.gif" to folder "resources".
		copyFile(bulletURLGIF.getAbsolutePath(), resourcesFolder.getAbsolutePath() + "\\" + bulletURLGIF.getName(), true);

		// Copy file "minus.gif" to folder "resources".
		copyFile(minusURLGIF.getAbsolutePath(), resourcesFolder.getAbsolutePath() + "\\" + minusURLGIF.getName(), true);

		// Copy file "plus.gif" to folder "resources".
		copyFile(plusURLGIF.getAbsolutePath(), resourcesFolder.getAbsolutePath() + "\\" + plusURLGIF.getName(), true);

		if (this.logFile.exists()) {
			if (!this.logFile.delete()) {
				throw new Exception("*** UNABLE TO DELETE: " + "'" + this.logFile.getAbsolutePath() + "' ***");
			}
		}

		res = this.logFile.createNewFile();
		if (!res) {
			throw new Exception("*** UNABLE TO CREATE: " + "'" + this.logFile.getAbsolutePath() + "' ***");
		}

		this.logFile = JFileTimeStamper.stampIt(this.logFile);

		return res;
	}

	private int validateEntryStatus(int entryStatus) throws IllegalArgumentException {
		int res = Log.DEBUG;
		if ((Log.DEBUG <= entryStatus) && (entryStatus <= Log.WARN)) {
			res = entryStatus;
		} else {
			StringBuilder errorMessage = new StringBuilder();
			errorMessage.append("*** ENTRY STATUS VALUES ***");
			errorMessage.append(Element.NEW_LINE);
			errorMessage.append("Log.DEBUG:  1");
			errorMessage.append(Element.NEW_LINE);
			errorMessage.append("Log.ERROR:  2");
			errorMessage.append(Element.NEW_LINE);
			errorMessage.append("Log.DEFECT: 3");
			errorMessage.append(Element.NEW_LINE);
			errorMessage.append("Log.FAIL:   4");
			errorMessage.append(Element.NEW_LINE);
			errorMessage.append("Log.PASS:   5");
			errorMessage.append(Element.NEW_LINE);
			errorMessage.append("Log.WARN:   6");
			throw new IllegalArgumentException(errorMessage.toString());
		}
		return res;
	}

	private int validateIssueCategory(int issueCategory) throws IllegalArgumentException {
		int res = Log.DEBUG;
		if ((Log.FEATURE_CHANGE <= issueCategory) && (issueCategory <= Log.NO_ISSUE)) {
			res = issueCategory;
		} else {
			StringBuilder errorMessage = new StringBuilder();
			errorMessage.append("*** ENTRY STATUS VALUES ***");
			errorMessage.append(Element.NEW_LINE);
			errorMessage.append("FEATURE_CHANGE:   7");
			errorMessage.append(Element.NEW_LINE);
			errorMessage.append("Log.SCRIPT_ISSUE: 8");
			errorMessage.append(Element.NEW_LINE);
			errorMessage.append("Log.MINOR_ISSUE:  9");
			errorMessage.append(Element.NEW_LINE);
			errorMessage.append("Log.MAJOR_ISSUE:  10");
			errorMessage.append(Element.NEW_LINE);
			errorMessage.append("Log.NO_ISSUE:  11");
			errorMessage.trimToSize();
			throw new IllegalArgumentException(errorMessage.toString());
		}
		return res;
	}

	private String captureScreenShot(int entryStatus) {
		File screenShot = null;
		String res = "";
		if (this.captureScreenShot == true) {
			try {
				if (entryStatus == Log.DEFECT || entryStatus == Log.ERROR || entryStatus == Log.FAIL || entryStatus == Log.SCREEN_SHOT
						|| entryStatus == Log.WARN) {

					screenShot = new File(screenShotsFolder.getAbsolutePath() + File.separator
							+ logFile.getName().replace(".html", "_" + getZeroPaddedScreenShotCounter(screenShotCounter, SCREEN_SHOT_COUNTER_WIDTH))
							+ SCREEN_SHOT_SUFFIX);

					if (_driver == null) {
						ImageIO.write(new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())),
								SCREEN_SHOT_SUFFIX.replace(".", "").trim(), screenShot);
					} else {
						File screenShotFile = ((TakesScreenshot) _driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(screenShotFile, screenShot);
					}

					// Increment screenShotCounter
					screenShotCounter++;

					// Store all captured screen shots
					capturedScreenShots.add(screenShot);

					res = SCREEN_SHOTS_FOLDER + "/" + screenShot.getName();
				}
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	private String getZeroPaddedScreenShotCounter(int screenShotCounter, int width) {
		return String.format("%1$0" + String.valueOf(width) + "d", screenShotCounter);
	}

	/*
	 * Copies an existing file from one location to another. An IOException is thrown if the operation fails.
	 *
	 * If "isImageData" is set to "true" then raw byte chunks of size 1024 is copied from "sourceFile" to "targetFile".
	 *
	 * If "isImageData" is set to "false" then buffered character data is copied from "sourceFile" to "targetFile".
	 */
	private void copyFile(String sourceFile, String targetFile, boolean isImageData) throws IOException {
		FileInputStream inputImageDataStream = null;
		FileOutputStream outputImageDataStream = null;
		BufferedReader inputCharacterDataStream = null;
		BufferedWriter outputCharacterDataStream = null;
		byte[] buffer = null;
		final int NUMBER_OF_BYTES_TO_READ = 1024;

		try {
			if (isImageData) {
				// Copy image data (raw bytes) from "sourceFile" to "targetFile".
				inputImageDataStream = new FileInputStream(sourceFile);
				outputImageDataStream = new FileOutputStream(targetFile);

				// Variable "ch" holds the current character to write to the target file...
				buffer = new byte[NUMBER_OF_BYTES_TO_READ];
				while (inputImageDataStream.read(buffer) != -1) {
					outputImageDataStream.write(buffer);
				}
			} else {
				// Copy character data from "sourceFile" to "targetFile".
				inputCharacterDataStream = new BufferedReader(new FileReader(sourceFile));
				outputCharacterDataStream = new BufferedWriter(new FileWriter(targetFile));

				int ch = 0;
				while ((ch = inputCharacterDataStream.read()) != -1) {
					outputCharacterDataStream.write(ch);
				}
			}
		} finally {
			if (inputImageDataStream != null) {
				inputImageDataStream.close();
			}
			if (outputImageDataStream != null) {
				outputImageDataStream.close();
			}
			if (inputCharacterDataStream != null) {
				inputCharacterDataStream.close();
			}
			if (outputCharacterDataStream != null) {
				outputCharacterDataStream.close();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		if (this.isTestStarted) {
			// Display appropriate message to console
			System.out.println("TEST: " + "'" + this.testMethodName + "' WAS NOT TERMINATED.");
		}
		super.finalize();
	}

	/**
	 * @return the startTestCounter
	 */
	public int getStartTestCounter() {
		return startTestCounter;
	}

	public void setColumnuWidth(int methodNameColumnHeaderWidth, int entryStatusColumnHeaderWidth, int locatorNameOrParameterColumnHeaderWidth,
			int actionValueOrMessageColumnHeaderWidth) {
		TestFunction.setColumnuWidth(methodNameColumnHeaderWidth, entryStatusColumnHeaderWidth, locatorNameOrParameterColumnHeaderWidth,
				actionValueOrMessageColumnHeaderWidth);
	}

	public static void main(String[] args) throws Exception {
		boolean result1, result2;
		Log log = new Log("C:\\TestResults\\log.html");
		// result1 = log.patternMatch("^string","string1");
		// result2 = log.patternMatch("^string","strin");
		//
		// result1 = log.patternMatch("string$","dsstring");
		// result2 = log.patternMatch("string$","tring");
		//
		// result1 = log.patternMatch("string, 1233, sss", "string, 1234, sss");
		// result2 = log.patternMatch("string, \\d+, sss", "string, 1234, sss");
		//
		result1 = log.patternMatch("string, \\d{10}, sss", "string, 123456789, sss");
		result2 = log.patternMatch("string, \\d{10}, sss", "string, 0123456789, sss");

		System.out.println(String.format("result1 = %s", result1));
		System.out.println(String.format("result2 = %s", result2));

	}

}