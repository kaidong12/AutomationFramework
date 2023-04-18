package com.cisco.framework.utilities.logging.unittests;

import com.cisco.framework.core.exceptions.FrameworkException;
import com.cisco.framework.utilities.logging.Log;
import com.cisco.framework.utilities.logging.LogData;

/**
 * @author Lance Yan
 *
 */
public class LogUnitTests {

	private static final String	USER_SPECIED_METHOD	= "UserSpecifedMethod";
	private static Log			log					= null;
	private static LogData		tlLogData			= null;
	private final static String	NEW_LINE			= System.getProperty("line.separator");

	public static void main(String[] args) {
		log = new Log("C:\\TestResults\\log.html");
		log.setLogMode("notsimple");

		// tlLogData = new LogData(log);
		try {
			// MTP_3__MyTest_Pass_unitest();
			// MTP_4__MyTest_Warning_unitest();
			// MTP_5__MyTest_Fail_unitest();
			// unitTest1();
			// unitTest2();
			// unitTest3();
			// unitTest4();
			// unitTest5();
			// unitTest6();
			// unitTest7();
			// unitTest8();
			// unitTest9();
			// unitTest10();
			// unitTest11();
			// unitTest12();
			// unitTest13();
			// unitTest14();
			// unitTest15();
			// unitTest16();
			// unitTest17();
			//
			// unitTest();
			// unitNegativeTest1();
			// unitNegativeTest2();
			// unitNegativeTest3();
			// unitNegativeTest4();

			unitTest18();

			// test();
			System.out.println("DONE");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// TODO: Close out the tlLogData
			// tlLogData.GenerateMetrics("C:/TestResults/", "AnySuitName");
			System.out.println("Done!!!!");
		}
	}

	@SuppressWarnings("unused")
	private static void test() throws Exception {
		try {
			startTest("test");
			log.startTestExecution("test");
			// throw new FrameworkException("METHOD NAME","LOCATOR NAME OR PARAMETER", "ACTION VALUE OR MESSAGE" + Element.NEW_LINE + "This is a
			// test",Log.ERROR,Log.SCRIPT_ISSUE);
			throw new Exception("This is an exception");
		} catch (FrameworkException e) {
			log.exception(e);
			throw e;
		} catch (Exception e) {
			log.exception(e);
			throw e;
		} finally {
			log.endTestExecution();
			endTest("test");
		}
	}

	private static void MTP_3__MyTest_Pass_unitest() {
		log.startTestExecution("MTP-3:MyTest_Pass_unitest");
		String message = "";
		int numMessages = 20;
		int numLines = 20;

		for (int n = 1; n <= numLines; n++) {
			for (int m = 1; m <= numMessages; m++) {
				message += "0123456789";
			}
			message += System.getProperty("line.separator");
		}

		log.comment(message);
		log.comment("This is a test");
		log.comment("unitTest", message, message, Log.DEFECT, Log.SCRIPT_ISSUE);
		log.endTestExecution();
	}// MTP_3__MyTest_Pass_unitest()

	private static void MTP_4__MyTest_Warning_unitest() {
		startTest("Unit Test 1: 1 test method, 1 iteration, only debug statements");

		log.startTestExecution("MTP-4:MyTest_Warning_unitest");
		log.comment("This is a 1 line log entry");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();

		endTest("Unit Test 1: 1 test method, 1 iteration, only debug statements");
	}// MTP_4__MyTest_Pass_unitest()

	private static void MTP_5__MyTest_Fail_unitest() {
		startTest("Unit Test 3: 1 test method, 1 iteration, Error statement");

		log.startTestExecution("MTP-5:MyTest_Fail_unitest");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Password", "Locator not found", Log.ERROR, Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();

		endTest("Unit Test 3: 1 test method, 1 iteration, Error statement");
	}// MTP_5__MyTest_Pass_unitest()

	private static void unitTest() throws Exception {
		log.startTestExecution("unitTest");
		String message = "";
		int numMessages = 20;
		int numLines = 20;

		for (int n = 1; n <= numLines; n++) {
			for (int m = 1; m <= numMessages; m++) {
				message += "0123456789";
			}
			message += System.getProperty("line.separator");
		}

		log.comment(message);
		log.comment("This is a test");
		log.comment("unitTest", message, message, Log.DEFECT, Log.SCRIPT_ISSUE);
		log.endTestExecution();
		// tlLogData.GenerateMetrics(sOutputFilePath, suiteName);
	}

	private static void unitTest1() throws Exception {
		// Unit Test 1: 1 test method, 1 iteration, only debug statements
		startTest("Unit Test 1: 1 test method, 1 iteration, only debug statements");

		log.startTestExecution("unitTest1");
		log.comment("This is a 1 line log entry");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();

		endTest("Unit Test 1: 1 test method, 1 iteration, only debug statements");
	}

	private static void unitTest2() throws Exception {
		// Unit Test 2: 1 test method, 1 iteration, warning statement
		startTest("Unit Test 2: 1 test method, 1 iteration, warning statement");

		log.startTestExecution("unitTest2");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.comment("Method", "Locator", "Value", Log.WARN, Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();

		endTest("Unit Test 2: 1 test method, 1 iteration, warning statement");
	}

	private static void unitTest3() throws Exception {
		// Unit Test 3: 1 test method, 1 iteration, Error statement
		startTest("Unit Test 3: 1 test method, 1 iteration, Error statement");

		log.startTestExecution("unitTest3");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Password", "Locator not found", Log.ERROR, Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();

		endTest("Unit Test 3: 1 test method, 1 iteration, Error statement");
	}

	private static void unitTest4() throws Exception {
		// Unit Test 4: 1 test method, 1 iteration, Fail statement
		startTest("Unit Test 4: 1 test method, 1 iteration, Fail statement");

		String expected = "Patrick";
		String actual = "Francesco";
		log.startTestExecution("unitTest4");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.comment(USER_SPECIED_METHOD, "IDMDefaultUsername", "Expected: " + expected + NEW_LINE + "Actual:   " + actual, Log.FAIL,
				Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();

		endTest("Unit Test 4: 1 test method, 1 iteration, Fail statement");
	}

	private static void unitTest5() throws Exception {
		// Unit Test 5: 1 test method, 1 iteration, Defect statement
		startTest("Unit Test 5: 1 test method, 1 iteration, Defect statement");

		String expected = "Patrick";
		String actual = "Francesco";
		String defect = "NGW1234";
		log.startTestExecution("unitTest5");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.comment(USER_SPECIED_METHOD, "IDMDefaultUsername",
				"Expected: " + expected + NEW_LINE + "Actual:   " + actual + NEW_LINE + "Defect:   " + defect, Log.DEFECT, Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();

		endTest("Unit Test 5: 1 test method, 1 iteration, Defect statement");
	}

	private static void unitTest6() throws Exception {
		// Unit Test 6: 1 test method, 1 iteration, Pass statement
		startTest("Unit Test 6: 1 test method, 1 iteration, Pass statement");

		String expected = "Patrick";
		String actual = "Patrick";

		log.startTestExecution("unitTest6");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.comment(USER_SPECIED_METHOD, "IDMDefaultUsername", "Expected: " + expected + NEW_LINE + "Actual:   " + actual, Log.PASS,
				Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();

		endTest("Unit Test 6: 1 test method, 1 iteration, Pass statement");
	}

	private static void unitTest7() throws Exception {
		// Unit Test 7: 3 test method, 1 iteration, statements with different entry status
		startTest("Unit Test 7: 3 test method, 1 iteration, statements with different entry status");

		String expected = "Patrick";
		String actual = "Patrick";

		log.startTestExecution("Test MethodOne (unitTest7)");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.comment(USER_SPECIED_METHOD, "IDMDefaultUsername", "Expected: " + expected + NEW_LINE + "Actual:   " + actual, Log.PASS,
				Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();

		log.startTestExecution("Test MethodTwo (unitTest7)");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.comment("Method", "Locator", "Value", Log.WARN, Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();

		log.startTestExecution("Test MethodThree (unitTest7)");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Password", "Locator not found", Log.ERROR, Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();

		endTest("Unit Test 7: 3 test method, 1 iteration, statements with different entry status");
	}

	private static void unitTest8() throws Exception {
		// Unit Test 8: 1 test method, 3 iterations, Pass statement
		startTest("Unit Test 8: 1 test method, 3 iterations, Pass statement");

		String expected = "Patrick";
		String actual = "Patrick";
		int iterations = 3;

		for (int iteration = 1; iteration <= iterations; iteration++) {
			log.startTestExecution("unitTest8");
			log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
			log.comment(USER_SPECIED_METHOD, "IDMDefaultUsername", "Expected: " + expected + NEW_LINE + "Actual:   " + actual, Log.PASS,
					Log.SCRIPT_ISSUE);
			log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
			log.endTestExecution();
		}

		endTest("Unit Test 8: 1 test method, 3 iterations, Pass statement");
	}

	private static void unitTest9() throws Exception {
		// Unit Test 9: 5 test methods, 3 iterations each, Mixed log entry status
		startTest("Unit Test 9: 5 test methods, 3 iterations each, Mixed log entry status");
		String[] testMethods = new String[] { "Test MethodOne (unitTest9)", "Test MethodTwo (unitTest9)", "Test MethodThree (unitTest9)",
				"Test MethodFour (unitTest9)", "Test MethodFive (unitTest9)" };
		String expected = "Patrick";
		String actual = "Patrick";
		String actual1 = "Francesco";
		int iterations = 3;

		for (int testMethodCounter = 0; testMethodCounter < testMethods.length; testMethodCounter++) {
			if (testMethods[testMethodCounter].equalsIgnoreCase("Test MethodOne (unitTest9)")) {
				for (int iteration = 1; iteration <= iterations; iteration++) {
					if (iteration == 1) {
						log.startTestExecution(testMethods[testMethodCounter]);
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
						log.comment(USER_SPECIED_METHOD, "IDMDefaultUsername", "Expected: " + expected + NEW_LINE + "Actual:   " + actual, Log.PASS,
								Log.SCRIPT_ISSUE);
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
						log.endTestExecution();
					}
					if (iteration == 2) {
						log.startTestExecution(testMethods[testMethodCounter]);
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
						log.comment("Method", "Locator", "Value", Log.WARN, Log.SCRIPT_ISSUE);
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
						log.endTestExecution();
					}
					if (iteration == 3) {
						log.startTestExecution(testMethods[testMethodCounter]);
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
						log.comment(USER_SPECIED_METHOD, "IDMDefaultUsername", "Expected: " + expected + NEW_LINE + "Actual:   " + actual, Log.PASS,
								Log.SCRIPT_ISSUE);
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
						log.endTestExecution();
					}

				}
			}
			if (testMethods[testMethodCounter].equalsIgnoreCase("Test MethodTwo (unitTest9)")) {
				for (int iteration = 1; iteration <= iterations; iteration++) {
					log.startTestExecution(testMethods[testMethodCounter]);
					if (iteration == 1) {
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
						log.comment(USER_SPECIED_METHOD, "IDMDefaultUsername", "Expected: " + expected + NEW_LINE + "Actual:   " + actual, Log.PASS,
								Log.SCRIPT_ISSUE);
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
					}
					if (iteration == 2) {
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
						log.comment("setText", "IDM.Editbox_Password", "Locator not found", Log.ERROR, Log.SCRIPT_ISSUE);
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
					}
					if (iteration == 3) {
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
						log.comment(USER_SPECIED_METHOD, "IDMDefaultUsername", "Expected: " + expected + NEW_LINE + "Actual:   " + actual, Log.PASS,
								Log.SCRIPT_ISSUE);
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
					}
					log.endTestExecution();
				}
			}
			if (testMethods[testMethodCounter].equalsIgnoreCase("Test MethodThree (unitTest9)")) {
				for (int iteration = 1; iteration <= iterations; iteration++) {
					log.startTestExecution(testMethods[testMethodCounter]);
					log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
					log.comment(USER_SPECIED_METHOD, "IDMDefaultUsername", "Expected: " + expected + NEW_LINE + "Actual:   " + actual, Log.PASS,
							Log.SCRIPT_ISSUE);
					log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
					log.endTestExecution();
				}
			}
			if (testMethods[testMethodCounter].equalsIgnoreCase("Test MethodFour (unitTest9)")) {
				for (int iteration = 1; iteration <= iterations; iteration++) {
					log.startTestExecution(testMethods[testMethodCounter]);
					if (iteration == 1) {
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
						log.comment(USER_SPECIED_METHOD, "IDMDefaultUsername", "Expected: " + expected + NEW_LINE + "Actual:   " + actual, Log.PASS,
								Log.SCRIPT_ISSUE);
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
					}
					if (iteration == 2) {
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
						log.comment(USER_SPECIED_METHOD, "IDMDefaultUsername", "Expected: " + expected + NEW_LINE + "Actual:   " + actual1, Log.FAIL,
								Log.SCRIPT_ISSUE);
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
					}
					if (iteration == 3) {
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
						log.comment(USER_SPECIED_METHOD, "IDMDefaultUsername", "Expected: " + expected + NEW_LINE + "Actual:   " + actual, Log.PASS,
								Log.SCRIPT_ISSUE);
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
					}
					log.endTestExecution();
				}
			}
			if (testMethods[testMethodCounter].equalsIgnoreCase("Test MethodFive (unitTest9)")) {
				for (int iteration = 1; iteration <= iterations; iteration++) {
					log.startTestExecution(testMethods[testMethodCounter]);
					if (iteration == 1) {
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
						log.comment(USER_SPECIED_METHOD, "IDMDefaultUsername", "Expected: " + expected + NEW_LINE + "Actual:   " + actual, Log.PASS,
								Log.SCRIPT_ISSUE);
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
					}
					if (iteration == 2) {
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
						log.comment("Method", "Locator", "Value", Log.WARN, Log.SCRIPT_ISSUE);
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
					}
					if (iteration == 3) {
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
						log.comment(USER_SPECIED_METHOD, "IDMDefaultUsername", "Expected: " + expected + NEW_LINE + "Actual:   " + actual1, Log.FAIL,
								Log.SCRIPT_ISSUE);
						log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
					}
					log.endTestExecution();
					endTest("Unit Test 9: 5 test methods, 3 iterations each, Mixed log entry status");
				}
			}
		}
	}

	private static void unitTest10() throws Exception {
		// Unit Test 10: 1 test method, 1 iteration, pass statement, warning statement
		startTest("Unit Test 10: 1 test method, 1 iteration, pass statement, warning statement");
		log.startTestExecution("unitTest10");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.comment(USER_SPECIED_METHOD, "Locator", "Value", Log.PASS, Log.SCRIPT_ISSUE);
		log.comment(USER_SPECIED_METHOD, "Locator", "Value", Log.WARN, Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();
		endTest("Unit Test 10: 1 test method, 1 iteration, pass statement, warning statement");
	}

	private static void unitTest11() throws Exception {
		startTest("unitTest11");
		log.startTestExecution("unitTest11");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.comment(USER_SPECIED_METHOD, "Locator", "Value", Log.WARN, Log.SCRIPT_ISSUE);
		log.comment(USER_SPECIED_METHOD, "Locator", "Value", Log.FAIL, Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();
		endTest("unitTest11");
	}

	private static void unitTest12() throws Exception {
		startTest("unitTest12");
		log.startTestExecution("unitTest12");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.comment(USER_SPECIED_METHOD, "Locator", "Value", Log.WARN, Log.SCRIPT_ISSUE);
		log.comment(USER_SPECIED_METHOD, "Locator", "Value", Log.ERROR, Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();
		endTest("unitTest12");
	}

	private static void unitTest13() throws Exception {
		startTest("unitTest13");
		log.startTestExecution("unitTest13");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.comment(USER_SPECIED_METHOD, "Locator", "Value", Log.WARN, Log.SCRIPT_ISSUE);
		log.comment(USER_SPECIED_METHOD, "Locator", "Value", Log.DEFECT, Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();
		endTest("unitTest13");
	}

	private static void unitTest14() throws Exception {
		startTest("unitTest14");
		log.startTestExecution("unitTest14");
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.comment(USER_SPECIED_METHOD, "Locator", "Value", Log.PASS, Log.SCRIPT_ISSUE);
		log.comment(USER_SPECIED_METHOD, "Locator", "Value", Log.WARN, Log.SCRIPT_ISSUE);
		log.comment(USER_SPECIED_METHOD, "Locator", "Value", Log.DEFECT, Log.SCRIPT_ISSUE);
		log.comment("setText", "IDM.Editbox_Username", "Patrick", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();
		endTest("unitTest14");
	}

	private static void unitTest15() throws Exception {
		startTest("unitTest15");
		String actionValueOrMessage = "Entering Username: 'Lance Yan' I will enter a very long line in order to test wether a single line will wrap in an appropriate manner within a given table cell."
				+ NEW_LINE
				+ "Another very long line is entered in order to determine if the second table cell in the second table row wraps in an appropriate manner.";
		String locator1 = "Project.Portlet1.Portlet2";
		String locator2 = "Project.Portlet1.Portlet2.Portlet3.Portlet4.Portlet5.Component.Frame.DialogBox1.DialogBox2.DialogBox3.DialogBox4.textField_Username";
		log.startTestExecution("unitTest15");
		log.comment("setText", locator1, "Value", Log.DEBUG, Log.SCRIPT_ISSUE);
		log.comment("setText", locator2, actionValueOrMessage, Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();
		endTest("unitTest15");
	}

	private static void unitTest16() throws Exception {
		startTest("unitTest16");
		log.startTestExecution("unitTest16");
		String testVar = "ABC";
		log.verifyStep(testVar.equals("ABC"), "verifyStep_Pass", "The test passed", "", Log.MINOR_ISSUE);
		log.verifyStep(!testVar.equals("XYZ"), "verifyStep_Warning", "The test passed", "JIRA-1234", Log.MAJOR_ISSUE);
		log.verifyStep(testVar.equals("XYZ"), "verifyStep_Fail", "The test passed", "", Log.FEATURE_CHANGE);
		log.verifyStep(!testVar.equals("ABC"), "verifyStep_Defect", "The test passed", "JIRA-9876", Log.SCRIPT_ISSUE);
		log.endTestExecution();
		endTest("unitTest16");
	}

	private static void unitTest17() throws Exception {
		startTest("unitTest17");
		log.startTestExecution("unitTest17");
		String testStrVar = "ABC";
		int testIntVar = 123;
		log.verifyCompare("ABC", "==", testStrVar, "verifyCompare_Pass", "The string value is", "", "", Log.SCRIPT_ISSUE);
		log.verifyCompare(90, "<", testIntVar, "verifyCompare_Warning", "The int value is greater than", "JIRA-1234", "", Log.FEATURE_CHANGE);
		log.verifyCompare("ABC", "!=", testStrVar, "verifyCompare_Fail", "The string value is not", "", "", Log.MINOR_ISSUE);
		log.verifyCompare(987, "==", testIntVar, "verifyCompare_Defect", "The int value is", "JIRA-9876", "", Log.MAJOR_ISSUE);
		log.endTestExecution();
		endTest("unitTest17");
	}

	private static void unitTest18() throws Exception {
//		log.startTestExecution("unitTest18");
		
//		log.comment("unitTest18");
//		log.endTestExecution();
//
//		log.startTestExecution("unitTest18");
//		log.comment("unitTest18");
//		log.endTestExecution();
//
//		log.startTestExecution("unitTest18");
//		log.comment("unitTest18");
//		log.endTestExecution();
//
//		log.startTestExecution("unitTest1818");
//		log.comment("unitTest1818");
//		log.endTestExecution();
//
//		log.startTestExecution("unitTest18");
//		log.comment("unitTest18");
//		log.endTestExecution();
//
//		log.startTestExecution("unitTest18");
//		log.comment("unitTest18");
//		log.endTestExecution();
		
		log.startTestExecution("AfterTest18");
		log.startFunction("Function_1");
		log.comment("AfterTest18");
		log.startFunction("Function_2");
		log.comment("AfterTest18");
		log.endTestExecution();
		
		log.startTestExecution("AfterTest18");
		log.startFunction("Function_1 Function_1 Function_1 Function_1");
		log.comment("AfterTest18");
		log.startFunction("Function_2 Function_2 Function_2 Function_2 Function_2");
		log.comment("AfterTest18");
		log.endTestExecution();
		

	}

	private static void unitNegativeTest1() {
		startTest("unitNegativeTest1: Calls 'log.startTestExecution()' only once.");
		log.startTestExecution("unitNegativeTest1");
		log.comment("unitNegativeTest1: Calls 'log.startTestExecution()' only once.");
		endTest("unitNegativeTest1");
	}

	private static void unitNegativeTest2() {
		startTest("unitNegativeTest2: Calls 'log.startTestExecution()' twice in sequence.");
		try {
			log.startTestExecution("unitNegativeTest2");
			log.startTestExecution("unitNegativeTest2");
		} catch (Exception e) {
			log.exception(e);
			endTest("unitNegativeTest2");
		} finally {
			log.endTestExecution();
		}
	}

	private static void unitNegativeTest3() {
		startTest("unitNegativeTest3: Calls 'log.endTestExecution()' only once.");
		log.endTestExecution();
		endTest("unitNegativeTest3");
	}

	private static void unitNegativeTest4() {
		startTest("unitNegativeTest4: Calls 'log.startTestExecution()' only once setting log to null and calling the garbage collector.");
		log.startTestExecution("unitNegativeTest4");
		log = null;
		System.gc();
		endTest("unitNegativeTest4");
	}

	private static void startTest(String testDescription) {
		System.out.println("*** START: " + testDescription + " ***");
	}

	private static void endTest(String testDescription) {
		System.out.println("*** END: " + testDescription + " ***");

	}
}