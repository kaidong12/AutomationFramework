package com.cisco.framework.utilities.logging;

import java.util.ArrayList;
import java.util.List;

import com.cisco.framework.utilities.logging.html.HTMLListItemElement;
import com.cisco.framework.utilities.logging.html.HTMLTableElement;
import com.cisco.framework.utilities.logging.html.HTMLTableHeaderElement;
import com.cisco.framework.utilities.logging.html.HTMLUnOrderedListElement;

/**
 * @author Francesco Ferrante
 * 
 */
public class TestFunction {
	private String						htmlListItemElementLabel				= "";
	private String						testFunctionName						= "";
	private String						entryStatusColor						= "";
	private String						entryissueCategory						= "";
	private String						entryissueDetails						= "";
	private List<TestStep>				testSteps								= null;
	private HTMLListItemElement			htmlListItemElementTestFunction			= null;
	private HTMLUnOrderedListElement	htmlUnOrderedListElementTestFunction	= null;
	private HTMLTableElement			htmlTableElementTestFunction			= null;
	private static int					methodNameColumnHeaderWidth				= 20;
	private static int					entryStatusColumnHeaderWidth			= 10;
	private static int					locatorNameOrParameterColumnHeaderWidth	= 20;
	private static int					actionValueOrMessageColumnHeaderWidth	= 50;

	public static void setColumnuWidth(int methodNameColumnHeaderWidth, int entryStatusColumnHeaderWidth, int locatorNameOrParameterColumnHeaderWidth,
			int actionValueOrMessageColumnHeaderWidth) {
		TestFunction.methodNameColumnHeaderWidth = methodNameColumnHeaderWidth;
		TestFunction.entryStatusColumnHeaderWidth = entryStatusColumnHeaderWidth;
		TestFunction.locatorNameOrParameterColumnHeaderWidth = locatorNameOrParameterColumnHeaderWidth;
		TestFunction.actionValueOrMessageColumnHeaderWidth = actionValueOrMessageColumnHeaderWidth;
	}

	public TestFunction(String testFunctionName) {
		init(testFunctionName);
	}

	private void init(String testFunctionName) {
		this.testFunctionName = testFunctionName;
		testSteps = new ArrayList<TestStep>();
	}

	public void addTestStep(String message) {
		testSteps.add(new TestStep(message));
	}

	public void addTestStep(String screenShot, int entryStatus) {
		testSteps.add(new TestStep(screenShot, entryStatus));
	}

	public void addTestStep(String methodName, String locatorNameOrParameter, String actionValueOrMessage) {
		testSteps.add(new TestStep(methodName, locatorNameOrParameter, actionValueOrMessage));
	}

	public void addTestStep(String methodName, String locatorNameOrParameter, String actionValueOrMessage, int entryStatus) {
		testSteps.add(new TestStep(methodName, locatorNameOrParameter, actionValueOrMessage, entryStatus));
	}

	public void addTestStep(String methodName, String locatorNameOrParameter, String actionValueOrMessage, int entryStatus, int issueCategory) {
		testSteps.add(new TestStep(methodName, locatorNameOrParameter, actionValueOrMessage, entryStatus, issueCategory));
	}

	public String gettestFunctionName() {
		return testFunctionName;
	}

	public String getEntryStatusColor() {
		String testFunctionColors = "";
		// Iterate through each test step and for each test step get its entry status color
		for (TestStep testStep : testSteps) {
			testFunctionColors += testStep.getEntryStatusColor() + com.cisco.framework.utilities.logging.html.Element.SPACE;
		}
		testFunctionColors = testFunctionColors.trim();
		if (testFunctionColors.contains(Log.RED)) {
			entryStatusColor = Log.RED;
		} else {
			if (testFunctionColors.contains(Log.YELLOW)) {
				entryStatusColor = Log.YELLOW;
			} else {
				entryStatusColor = Log.GREEN;
			}
		}
		return entryStatusColor;
	}

	public String getTestStepIssueCategory() {
		String testIterationIssueCategory = "";
		// Iterate through each test step and for each test step get its entry status color
		for (TestStep testStep : testSteps) {
			testIterationIssueCategory += testStep.getIssueCategory() + com.cisco.framework.utilities.logging.html.Element.SPACE;
		}
		testIterationIssueCategory = testIterationIssueCategory.trim();
		if (testIterationIssueCategory.contains("Script Issue")) {
			entryissueCategory = "Script_Issue";
		} else {
			if (testIterationIssueCategory.contains("Major Issue")) {
				entryissueCategory = "Major_Issue";
			} else {
				entryissueCategory = "";
			}
		}
		return entryissueCategory;
	}

	/**
	 * @author Mohammed Alam
	 * @return
	 * 
	 * @Usage: Extracts the detailed content of each step with in a test Iteration.
	 */
	public String getTestStepIssueDetails() {
		String testFunctionIssueCategory = "";
		// Iterate through each test step and for each test step get its entry status color
		for (TestStep testStep : testSteps) {
			testFunctionIssueCategory += testStep.getIssueCategory() + com.cisco.framework.utilities.logging.html.Element.SPACE;
		}
		entryissueDetails = testFunctionIssueCategory.trim();

		return entryissueDetails;
	}

	public HTMLListItemElement getContent() {
		htmlListItemElementLabel = testFunctionName;
		htmlListItemElementTestFunction = new HTMLListItemElement(htmlListItemElementLabel);
		htmlUnOrderedListElementTestFunction = new HTMLUnOrderedListElement();
		htmlTableElementTestFunction = new HTMLTableElement("border", 1, 0, 0, 100, true);

		// Let's go ahead and create a table header for "htmlTableElementTestMethod"
		HTMLTableHeaderElement htmlTableHeaderElementMethodName = new HTMLTableHeaderElement(Log.WHITE, "center", "middle", 0, 0,
				methodNameColumnHeaderWidth, 0);
		htmlTableHeaderElementMethodName.append("Method Name");
		HTMLTableHeaderElement htmlTableHeaderElementEntryStatus = new HTMLTableHeaderElement(Log.WHITE, "center", "middle", 0, 0,
				entryStatusColumnHeaderWidth, 0);
		htmlTableHeaderElementEntryStatus.append("Entry Status");
		HTMLTableHeaderElement htmlTableHeaderElementLocatorNameOrParameter = new HTMLTableHeaderElement(Log.WHITE, "center", "middle", 0, 0,
				locatorNameOrParameterColumnHeaderWidth, 0);
		htmlTableHeaderElementLocatorNameOrParameter.append("Locator or Parameter");
		HTMLTableHeaderElement htmlTableHeaderElementActionValueOrMessage = new HTMLTableHeaderElement(Log.WHITE, "center", "middle", 0, 0,
				actionValueOrMessageColumnHeaderWidth, 0);
		htmlTableHeaderElementActionValueOrMessage.append("Action Value or Message");

		htmlTableElementTestFunction.append(htmlTableHeaderElementMethodName);
		htmlTableElementTestFunction.append(htmlTableHeaderElementEntryStatus);
		htmlTableElementTestFunction.append(htmlTableHeaderElementLocatorNameOrParameter);
		htmlTableElementTestFunction.append(htmlTableHeaderElementActionValueOrMessage);

		// Let's start populating "htmlTableElementTestMethod" with all test steps
		for (TestStep testStep : testSteps) {
			htmlTableElementTestFunction.append(testStep.getContent());
		}

		// Now let's append "htmlTableElementTestMethod" to "htmlUnOrderedListElementTestMethod"
		htmlUnOrderedListElementTestFunction.append(this.htmlTableElementTestFunction);

		// Finally let's append "htmlUnOrderedListElementTestMethod" to "htmlListItemElementTestMethod"
		htmlListItemElementTestFunction.append(this.htmlUnOrderedListElementTestFunction);

		// content = this.htmlListItemElementTestMethod.getContent().trim();
		return htmlListItemElementTestFunction;

	}

}
