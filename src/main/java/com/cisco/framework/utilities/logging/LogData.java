package com.cisco.framework.utilities.logging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cisco.framework.utilities.FileIO;
import com.cisco.framework.utilities.logging.Log;
import com.cisco.framework.utilities.logging.TestIteration;
import com.cisco.framework.utilities.logging.TestMethod;

public class LogData {
	public class TestCaseResult {
		public TestCaseResult() {
		}

		private String	TestCaseName;
		private String	Status;
		private String	Message;
		private String	LogFileName;
		private String	Description;

		public String getTestCaseName() {
			return TestCaseName;
		}

		public void setTestCaseName(String testCaseName) {
			TestCaseName = testCaseName;
		}

		public String getStatus() {
			return Status;
		}

		public void setStatus(String status) {
			Status = status;
		}

		public String getMessage() {
			return Message;
		}

		public void setMessage(String message) {
			Message = message;
		}

		public String getLogFileName() {
			return LogFileName;
		}

		public void setLogFileName(String LogFileName) {
			this.LogFileName = LogFileName;
		}

		public String getDescription() {
			return Description;
		}

		public void setDescription(String description) {
			Description = description;
		}

	}// TestCaseResult

	public LogData(Log log) {
		this.log = log;
		// TODO Auto-generated constructor stub
	}

	protected Log					log									= null;
	private List<TestMethod>		testMethods							= null;
	private List<TestIteration>		testIterations						= null;
	private int						iTestStepPassCounter				= 0;
	private int						iTestStepFailCounter				= 0;
	private int						iTestStepWarningCounter				= 0;
	private int						iTestMethodPassCounter				= 0;
	private int						iTestMethodFailCounter				= 0;
	private int						iTestMethodWarningCounter			= 0;
	private int						iTestIterationPasscounter			= 0;
	private int						iTestIterationFailcounter			= 0;
	private int						iTestIterationWarningcounter		= 0;
	private int						iTestMajorIssueCounter				= 0;
	private int						iTestScriptIssueCounter				= 0;
	private String					LogFilePath							= "";
	private int						iTotalTestsExecutedCounter			= 0;
	private int						iTestIterationMajorIssueCounter		= 0;
	private int						iTestIterationScriptIssueCounter	= 0;

	// List to Store TestCase Results
	private List<TestCaseResult>	testCaseResults						= new ArrayList<TestCaseResult>();

	public List<TestCaseResult> getTestCaseResults() {
		return testCaseResults;
	}

	/**
	 * @return the iTestStepPassCounter
	 */
	public int getiTestStepPassCounter() {
		return iTestStepPassCounter;
	}

	/**
	 * @param iTestStepPassCounter
	 *            the iTestStepPassCounter to set
	 */
	public void setiTestStepPassCounter(int iTestStepPassCounter) {
		this.iTestStepPassCounter = iTestStepPassCounter;
	}

	/**
	 * @return the iTestStepFailCounter
	 */
	public int getiTestStepFailCounter() {
		return iTestStepFailCounter;
	}

	/**
	 * @param iTestStepFailCounter
	 *            the iTestStepFailCounter to set
	 */
	public void setiTestStepFailCounter(int iTestStepFailCounter) {
		this.iTestStepFailCounter = iTestStepFailCounter;
	}

	/**
	 * @return the iTestStepWarningCounter
	 */
	public int getiTestStepWarningCounter() {
		return iTestStepWarningCounter;
	}

	/**
	 * @param iTestStepWarningCounter
	 *            the iTestStepWarningCounter to set
	 */
	public void setiTestStepWarningCounter(int iTestStepWarningCounter) {
		this.iTestStepWarningCounter = iTestStepWarningCounter;
	}

	/**
	 * @return the iTestMethodPassCounter
	 */
	public int getiTestMethodPassCounter() {
		return iTestMethodPassCounter;
	}

	/**
	 * @param iTestMethodPassCounter
	 *            the iTestMethodPassCounter to set
	 */
	public void setiTestMethodPassCounter(int iTestMethodPassCounter) {
		this.iTestMethodPassCounter = iTestMethodPassCounter;
	}

	/**
	 * @return the iTestMethodFailCounter
	 */
	public int getiTestMethodFailCounter() {
		return iTestMethodFailCounter;
	}

	/**
	 * @param iTestMethodFailCounter
	 *            the iTestMethodFailCounter to set
	 */
	public void setiTestMethodFailCounter(int iTestMethodFailCounter) {
		this.iTestMethodFailCounter = iTestMethodFailCounter;
	}

	/**
	 * @return the iTestMethodWarningCounter
	 */
	public int getiTestMethodWarningCounter() {
		return iTestMethodWarningCounter;
	}

	/**
	 * @param iTestMethodWarningCounter
	 *            the iTestMethodWarningCounter to set
	 */
	public void setiTestMethodWarningCounter(int iTestMethodWarningCounter) {
		this.iTestMethodWarningCounter = iTestMethodWarningCounter;
	}

	/**
	 * @return the iTestIterationPasscounter
	 */
	public int getiTestIterationPasscounter() {
		return iTestIterationPasscounter;
	}

	/**
	 * @param iTestIterationPasscounter
	 *            the iTestIterationPasscounter to set
	 */
	public void setiTestIterationPasscounter(int iTestIterationPasscounter) {
		this.iTestIterationPasscounter = iTestIterationPasscounter;
	}

	/**
	 * @return the iTestIterationFailcounter
	 */
	public int getiTestIterationFailcounter() {
		return iTestIterationFailcounter;
	}

	/**
	 * @param iTestIterationFailcounter
	 *            the iTestIterationFailcounter to set
	 */
	public void setiTestIterationFailcounter(int iTestIterationFailcounter) {
		this.iTestIterationFailcounter = iTestIterationFailcounter;
	}

	/**
	 * @return the iTestIterationWarningcounter
	 */
	public int getiTestIterationWarningcounter() {
		return iTestIterationWarningcounter;
	}

	/**
	 * @param iTestIterationWarningcounter
	 *            the iTestIterationWarningcounter to set
	 */
	public void setiTestIterationWarningcounter(int iTestIterationWarningcounter) {
		this.iTestIterationWarningcounter = iTestIterationWarningcounter;
	}

	/**
	 * @return the iTestMajorIssueCounter
	 */
	public int getiTestMajorIssueCounter() {
		return iTestMajorIssueCounter;
	}

	/**
	 * @param iTestMajorIssueCounter
	 *            the iTestMajorIssueCounter to set
	 */
	public void setiTestMajorIssueCounter(int iTestMajorIssueCounter) {
		this.iTestMajorIssueCounter = iTestMajorIssueCounter;
	}

	/**
	 * @return the iTestScriptIssueCounter
	 */
	public int getiTestScriptIssueCounter() {
		return iTestScriptIssueCounter;
	}

	/**
	 * @param iTestScriptIssueCounter
	 *            the iTestScriptIssueCounter to set
	 */
	public void setiTestScriptIssueCounter(int iTestScriptIssueCounter) {
		this.iTestScriptIssueCounter = iTestScriptIssueCounter;
	}

	/**
	 * @param logFileName
	 *            the logFileName to set
	 */
	public void setLogFileName(String logFileName) {
	}

	/**
	 * @return the iTotalTestsExecutedCounter
	 */
	public int getiTotalTestsExecutedCounter() {
		return iTotalTestsExecutedCounter;
	}

	/**
	 * @param iTotalTestsExecutedCounter
	 *            the iTotalTestsExecutedCounter to set
	 */
	public void setiTotalTestsExecutedCounter(int iTotalTestsExecutedCounter) {
		this.iTotalTestsExecutedCounter = iTotalTestsExecutedCounter;
	}

	public void setLogFilePath(String logFilePath) {
		LogFilePath = logFilePath;
	}

	public String getLogFilePath() {
		return LogFilePath;
	}

	/**
	 * 
	 * @param sOutputFilePath
	 * @param suiteName
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            This function creates a object of logData for each testMethod per transaction.
	 */
	public void GenerateMetrics(String sOutputFilePath, String suiteName) {
		System.out.println("*******START : Collecting Metrics ************");
		int iTestMethodCounter = 0;
		int iTestIterationCounter = 0;
		initMetrics();
		testMethods = log.GetTestMethods();

		for (TestMethod overAllTestMethod : testMethods) {
			iTestMethodCounter = iTestMethodCounter + 1;
			String str = overAllTestMethod.getTestMethodName();
			String[] strArray = str.split(":");

			/*
			 * for(String strEach : strArray){ TestMethod testMethod = overAllTestMethod; testMethod.setTestMethodName(strEach);
			 * 
			 * if (testMethod.getTestMethodName() != "BeforeSuite" && testMethod.getTestMethodName() != "AfterSuite") { // Get List of Iterations for
			 * Test Method testIterations = testMethod.getTestIterations();
			 * 
			 * // update Test Method Counters updateTestMethodCounters(testMethod,sOutputFilePath);
			 * 
			 * // Update Pass,Fail and Warning Counters for each Iteration updateTestIterationCounters(); }//if }//for
			 */

			// For each test method
			for (String strEach : strArray) {
				iTestIterationCounter = 0;
				TestMethod testMethodWithIterations = overAllTestMethod;

				// Ignore the BeforeSuite and AfterSuite
				if (testMethodWithIterations.getTestMethodName() != "BeforeSuite" && testMethodWithIterations.getTestMethodName() != "AfterSuite") {

					testIterations = testMethodWithIterations.getTestIterations();
					// traverse through each transaction
					for (int i = 0; i < testIterations.size(); i++) {
						iTestIterationCounter = iTestIterationCounter + 1;
						if ((testIterations.size() - iTestIterationCounter) % 5 == 0) {
							System.out.println("Debug::Collecting Metrics for Test Iteration: " + iTestIterationCounter + " of "
									+ testIterations.size() + " for Test Method " + iTestMethodCounter + " of " + testMethods.size());
						}

						TestMethod testMethod = testMethodWithIterations;
						testMethod.setTestMethodName(strEach);

						// update the testMethod counters and sets the logData object properly
						updateTestMethodCounters(testMethod, testIterations.get(i), sOutputFilePath);

						// Update Pass,Fail and Warning Counters for each Iteration
						updateTestIterationCounters();

					} // for each iteration
				} // for each test method
			} // for
		}
		System.out.println("*******END : Collecting Metrics ************");
	}

	/**
	 * @author Murali K Parepalli
	 * @param testMethod
	 *            <br>
	 *            <br>
	 *            USAGE <br>
	 *            <br>
	 *            updateTestMethodCounters generates TestMethod Counters at highlevel. This doesn't consider the number of iterations in a test
	 *            method.
	 * 
	 */
	/*
	 * private void updateTestMethodCounters(TestMethod testMethod, String logPath) { if (testMethod.getEntryStatusColor() == Log.GREEN) {
	 * this.iTestMethodPassCounter = this.iTestMethodPassCounter + 1; } if (testMethod.getEntryStatusColor() == Log.YELLOW) {
	 * this.iTestMethodWarningCounter = this.iTestMethodWarningCounter + 1; } if (testMethod.getEntryStatusColor() == Log.RED) {
	 * this.iTestMethodFailCounter = this.iTestMethodFailCounter + 1; }
	 * 
	 * //this.testCaseResults.add(getTestResult(testMethod)); this.testCaseResults.add(getTestResultForTestMethod(testMethod, logPath)); }
	 */

	/**
	 * @author Mohammed Alam
	 * @param testMethod
	 * @param testIteration
	 * @param logPath
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            generates testMethod Counters at a high level.
	 */
	private void updateTestMethodCounters(TestMethod testMethod, TestIteration testIteration, String logPath) {
		if (testMethod.getEntryStatusColor() == Log.GREEN) {
			this.iTestMethodPassCounter = this.iTestMethodPassCounter + 1;
		}
		if (testMethod.getEntryStatusColor() == Log.YELLOW) {
			this.iTestMethodWarningCounter = this.iTestMethodWarningCounter + 1;
		}
		if (testMethod.getEntryStatusColor() == Log.RED) {
			this.iTestMethodFailCounter = this.iTestMethodFailCounter + 1;
		}

		// this.testCaseResults.add(getTestResult(testMethod));
		this.testCaseResults.add(getTestResultForTestIteration(testMethod, testIteration, logPath));
	}

	/*
	*//***
		 * 
		 * @param testMethod
		 * @return TestCaseResult object
		 */
	/*
	 * private TestCaseResult getTestResult(TestMethod testMethod) { TestCaseResult tr= new TestCaseResult(); // TODO Auto-generated method stub if
	 * (testMethod.getEntryStatusColor() == Log.GREEN) { tr.setStatus("PASS"); } if (testMethod.getEntryStatusColor() == Log.YELLOW) {
	 * tr.setStatus("UNKNOWN"); } if (testMethod.getEntryStatusColor() == Log.RED) { tr.setStatus("FAIL"); }
	 * 
	 * tr.setTestCaseName(testMethod.getTestMethodName()); tr.setDescription(testMethod.getTestMethodName()); tr.setLogFileName(log.getLogFilePath());
	 * 
	 * return tr; }
	 * 
	 * 
	 * 
	 * private TestCaseResult getTestResultNew(TestMethod testMethod){ int test =0; TestCaseResult tr= new TestCaseResult();
	 * 
	 * if (testMethod.getEntryStatusColor() == Log.YELLOW && testMethod.getContent().contains("ScriptName:" + testMethod.getTestMethodName())) {
	 * if(testMethod.getContent().contains("WARNING" + ":ScriptName:" + testMethod.getTestMethodName())){ tr.setStatus("UNKNOWN"); } } else if
	 * (testMethod.getEntryStatusColor() == Log.RED && testMethod.getContent().contains("ScriptName:" + testMethod.getTestMethodName())) {
	 * if(testMethod.getContent().contains("Script Issue" + ":ScriptName:" + testMethod.getTestMethodName())){ tr.setStatus("SCRIPT_ISSUE"); } else
	 * if(testMethod.getContent().contains("Defect" + ":ScriptName:" + testMethod.getTestMethodName()) || testMethod.getContent().contains(
	 * "Major Issue" + ":ScriptName:" + testMethod.getTestMethodName())){ tr.setStatus("FAIL"); } } else if(testMethod.getEntryStatusColor() ==
	 * Log.GREEN && (testMethod.getContent().contains(":ScriptName:"))) { tr.setStatus("PASS"); } else tr.setStatus("NOT_RUN");
	 * 
	 * tr.setTestCaseName(testMethod.getTestMethodName()); tr.setDescription(testMethod.getTestMethodName()); tr.setLogFileName(log.getLogFilePath());
	 * 
	 * 
	 * return tr; }
	 * 
	 * private TestCaseResult getTestResultForTestMethod(TestMethod testMethod, String logPath){ int test =0; TestCaseResult tr= new TestCaseResult();
	 * 
	 * if (testMethod.getContent().contains("Defect" + ":ScriptName:" + testMethod.getTestMethodName()) || testMethod.getContent().contains(
	 * "Major Issue" + ":ScriptName:" + testMethod.getTestMethodName())){ tr.setStatus("FAIL"); tr.setMessage("FAIL: " + logPath); } else if
	 * (testMethod.getContent().contains("Script Issue" + ":ScriptName:" + testMethod.getTestMethodName())){ tr.setStatus("SCRIPT_ISSUE");
	 * tr.setMessage("Script Issue: " + logPath); } else if (testMethod.getContent().contains("WARNING" + ":ScriptName:" +
	 * testMethod.getTestMethodName())){ tr.setStatus("UNKNOWN"); tr.setMessage("Warning: " + logPath); } else if
	 * (testMethod.getContent().contains("PASS" + ":ScriptName:" + testMethod.getTestMethodName())){ tr.setStatus("PASS"); } else
	 * tr.setStatus("NOT_RUN");
	 * 
	 * 
	 * 
	 * tr.setTestCaseName(testMethod.getTestMethodName()); tr.setDescription(testMethod.getTestMethodName()); tr.setLogFileName(log.getLogFilePath());
	 * 
	 * 
	 * return tr; }
	 */

	/**
	 * @author Mohammed Alam
	 * @param testMethod
	 * @param testIteration
	 * @param logPath
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Initializes a TestCaseResult object for each testMethod/Iteration. Depending on the color and error description of each RED/Green
	 *            test step in a testIteration the object is initialized.
	 */

	private TestCaseResult getTestResultForTestIteration(TestMethod testMethod, TestIteration testIteration, String logPath) {
		int test = 0;
		TestCaseResult tr = new TestCaseResult();

		if (testIteration.getTestStepIssueDetails().contains("Defect" + ":ScriptName:" + testMethod.getTestMethodName())
				|| testIteration.getTestStepIssueDetails().contains("Major Issue" + ":ScriptName:" + testMethod.getTestMethodName())) {
			tr.setStatus("FAIL");
			tr.setMessage("FAIL: " + logPath);
		} else if (testIteration.getTestStepIssueDetails().contains("Script Issue" + ":ScriptName:" + testMethod.getTestMethodName())) {
			tr.setStatus("SCRIPT_ISSUE");
			tr.setMessage("Script Issue: " + logPath);
		} else if (testIteration.getTestStepIssueDetails().contains("WARNING" + ":ScriptName:" + testMethod.getTestMethodName())) {
			tr.setStatus("UNKNOWN");
			tr.setMessage("Warning: " + logPath);
		} else if (testIteration.getTestStepIssueDetails().contains("PASS" + ":ScriptName:" + testMethod.getTestMethodName())) {
			tr.setStatus("PASS");
		} else
			tr.setStatus("NOT_RUN");

		tr.setTestCaseName(testMethod.getTestMethodName());
		tr.setDescription(testMethod.getTestMethodName());
		tr.setLogFileName(log.getLogFilePath());

		return tr;
	}

	/**
	 * @author Murali K Parepalli
	 * 
	 */
	private void updateTestIterationCounters() {
		int test = 0;
		for (TestIteration testIteration : testIterations) {
			if (testIteration.getEntryStatusColor() == Log.GREEN) {
				test = this.iTestIterationPasscounter + 1;
				this.iTestIterationPasscounter = test;
			}
			if (testIteration.getEntryStatusColor() == Log.YELLOW) {
				test = this.iTestIterationWarningcounter + 1;
				this.iTestIterationWarningcounter = test;
			}
			if (testIteration.getEntryStatusColor() == Log.RED) {
				test = this.iTestIterationFailcounter + 1;
				this.iTestIterationFailcounter = test;
			}
			if (testIteration.getContent().toString().contains("Major Issue")) {
				test = this.iTestIterationMajorIssueCounter + 1;
				this.iTestIterationMajorIssueCounter = test;
			}
			if (testIteration.getContent().toString().contains("Script Issue") == true
					&& testIteration.getContent().toString().contains("Major Issue") == false) {
				test = this.iTestIterationScriptIssueCounter + 1;
				this.iTestIterationScriptIssueCounter = test;
			}
			// updateTotalTestCounter Here
			iTotalTestsExecutedCounter = iTotalTestsExecutedCounter + 1;

		}
	}

	public String getSuiteName(String filePath) {
		return new FileIO(log).getFileName(filePath).replace(".xml", "");
	}

	/**
	 * @author Murali K Parepalli
	 * @param sOutputFilePath
	 * @param suiteName
	 * @param fileIO
	 * @param testMethod
	 *            <br>
	 *            <br>
	 *            USAGE: <br>
	 *            <br>
	 *            TO BE USED to Log Metrics about methods passed....
	 */
	/*
	 * private void writeTestMethodToLog(String sOutputFilePath, String suiteName, FileIO fileIO, TestMethod testMethod) { String eol =
	 * System.getProperty("line.separator"); Date dtNow = new Date(); SimpleDateFormat formatter = new SimpleDateFormat("yy/dd/mm HH:mm:ss a");
	 * StringBuilder strOut = new StringBuilder(); strOut.append(eol); strOut.append(formatter.format(dtNow) + "^"); strOut.append(suiteName);
	 * strOut.append("^"); strOut.append(testMethod.getTestMethodName()); strOut.append("^"); strOut.append("P"); strOut.append(
	 * "Execution of Test Method" + testMethod.getTestMethodName()); try { if (fileIO.isFileExists(sOutputFilePath + suiteName) == false) { // Create
	 * Output Log File fileIO.createFile(sOutputFilePath + suiteName);
	 * 
	 * // Write to Output fileIO.writeToFile(sOutputFilePath + suiteName, strOut.toString()); } else { // Append To File
	 * fileIO.appendToFile(sOutputFilePath + suiteName, strOut.toString()); } } catch (Exception e) { System.out.println(e.getStackTrace()); }
	 * 
	 * }
	 */

	/***
	 * @author Murali K Parepalli <br>
	 *         <br>
	 *         USAGE <br>
	 *         <br>
	 *         Initializes All Metrics. This method is private and should be called from GenerateMetrics
	 */
	private void initMetrics() {
		// TODO Auto-generated method stub
		iTotalTestsExecutedCounter = 0;
		iTestStepPassCounter = 0;
		iTestStepFailCounter = 0;
		iTestStepWarningCounter = 0;
		iTestMethodPassCounter = 0;
		iTestMethodFailCounter = 0;
		iTestMethodWarningCounter = 0;
		iTestIterationPasscounter = 0;
		iTestIterationFailcounter = 0;
		iTestIterationWarningcounter = 0;
		iTestMajorIssueCounter = 0;
		iTestScriptIssueCounter = 0;

	}

	private void WriteMetricsToLog(String sOutputFilePath, String suiteName, FileIO fileIO) {
		String eol = System.getProperty("line.separator");
		Date dtNow = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yy/dd/MM HH:mm:ss a");
		StringBuilder strOut = new StringBuilder();
		String res = "";
		suiteName = suiteName.replace(".log", "");
		if (this.getiTestIterationFailcounter() > 0) {
			res = "E";
		} else if (this.getiTestIterationFailcounter() == 0 && this.getiTestIterationWarningcounter() > 0) {
			res = "W";
		} else {
			res = "I";
		}

		// Begining of Log File
		String strBegin = formatter.format(dtNow) + "^" + suiteName + "^";
		strOut.append(strBegin + "Begin Test^Executes Selenium Automation Tests from " + suiteName + eol);
		strOut.append("Begin Execute: " + suiteName + eol);
		strOut.append(strBegin + "Total Number of Tests Executed^I^ Total Tests = " + this.getiTotalTestsExecutedCounter() + eol);
		strOut.append(strBegin + "Total Tests Passed^I^Total Passed=" + this.getiTestIterationPasscounter() + eol);
		strOut.append(strBegin + "Total Tests Failed^E^Total Failed=" + this.getiTestIterationFailcounter() + eol);
		strOut.append(strBegin + "Total Tests Warning^W^Total Warnings=" + this.getiTestIterationWarningcounter() + eol);
		strOut.append(
				strBegin + "Total Tests Failed With Script Issues^W^Total Scripts with Script Issues=" + this.getiTestScriptIssueCounter() + eol);
		strOut.append(strBegin + "Total Tests Failed With Major Issues^E^Total Scripts with Major Issues=" + this.getiTestMajorIssueCounter() + eol);
		strOut.append("*************************************************************************************" + eol);
		strOut.append(strBegin + "ENDSUMMARY^" + res + "^" + this.getiTotalTestsExecutedCounter() + "^" + this.getiTestIterationPasscounter() + "^"
				+ this.getiTestIterationFailcounter() + "^" + "0^" + this.getiTestIterationWarningcounter() + "^" + eol);

		strOut.append("*************************************************************************************" + eol);
		strOut.append("NOTE: ENDSUMMARY_STATEMENT total values above are read as follows: TestTotal:Passed:Failed:Aborted:Warnings" + eol);
		strOut.append("************************************************************************************* " + eol);
		strOut.append("End Execute: " + suiteName + eol);

		if (!sOutputFilePath.endsWith("/")) {
			sOutputFilePath = sOutputFilePath + "/";
		}
		suiteName = suiteName + ".log";
		try {
			if (fileIO.isFileExists(sOutputFilePath + suiteName) == false) {
				// Create Output Log File
				if (fileIO.createFile(sOutputFilePath + suiteName)) {
					// Write to Output
					fileIO.writeToFile(sOutputFilePath + suiteName, strOut.toString());
				}
			} else {
				// Append To File
				fileIO.appendToFile(sOutputFilePath + suiteName, strOut.toString());
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}

	}

}