package com.cisco.framework.utilities.logging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cisco.framework.utilities.logging.html.HTMLListItemElement;
import com.cisco.framework.utilities.logging.html.HTMLTableCellElement;
import com.cisco.framework.utilities.logging.html.HTMLTableElement;
import com.cisco.framework.utilities.logging.html.HTMLTableHeaderElement;
import com.cisco.framework.utilities.logging.html.HTMLTableRowElement;
import com.cisco.framework.utilities.logging.html.HTMLUnOrderedListElement;

/**
 * @author Francesco Ferrante
 * 
 */
public class TestIteration {

	private List<TestFunction>			testFunctions								= null;
	private List<TestStep>				testSteps									= null;
	private HTMLUnOrderedListElement	htmlUnOrderedListElementMKTreeTestFunction	= null;
	private HTMLTableCellElement		htmlTableCellElementTestFunction			= null;
	private HTMLTableRowElement			htmlTableRowElementTestFunction				= null;
	private HTMLTableElement			htmlTableElementTestFunction				= null;
	private HTMLUnOrderedListElement	htmlUnOrderedListElementTestFunction		= null;
	private HTMLListItemElement			htmlListItemElementTestIteration			= null;

	private String						htmlListItemElementLabel					= "";
	private String						testIterationName							= "";
	private String						entryStatusColor							= "";
	private String						entryissueCategory							= "";
	private String						entryissueDetails							= "";

	public static final String			TEST_METHOD_EXECUTION_COUNTER				= "TAG_TEST_METHOD_EXECUTION_COUNTER";
	public static final String			TEST_METHOD_TOTAL_EXECUTIONS				= "TAG_TEST_METHOD_TOTAL_EXECUTIONS";
	public static final String			TEST_METHOD_EXECUTION_LABEL					= "(" + TEST_METHOD_EXECUTION_COUNTER + "/"
			+ TEST_METHOD_TOTAL_EXECUTIONS + ")";

	public TestIteration(String testIterationName) {
		init(testIterationName);
	}

	private void init(String testIterationName) {
		this.testIterationName = testIterationName;
		testFunctions = new ArrayList<TestFunction>();
	}

	public void addFunction(TestFunction testFunction) {
		testFunctions.add(testFunction);
	}

	public TestFunction getFunction(String functionName) {
		Iterator<TestFunction> it = testFunctions.iterator();
		while (it.hasNext()) {
			TestFunction tf = (TestFunction) it.next();
			if (tf.gettestFunctionName().equals(functionName)) {
				return tf;
			}
		}
		return null;
	}

	public String getTestIterationName() {
		return testIterationName;
	}

	public String getEntryStatusColor() {
		String testIterationColors = "";
		// Iterate through each test step and for each test step get its entry status color
		for (TestFunction testFunction : testFunctions) {
			testIterationColors += testFunction.getEntryStatusColor() + com.cisco.framework.utilities.logging.html.Element.SPACE;
		}
		testIterationColors = testIterationColors.trim();
		if (testIterationColors.contains(Log.RED)) {
			entryStatusColor = Log.RED;
		} else {
			if (testIterationColors.contains(Log.YELLOW)) {
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
		String testIterationIssueCategory = "";
		// Iterate through each test function and for each test step get its entry status color
		for (TestFunction testFunction : testFunctions) {
			testIterationIssueCategory += testFunction.getTestStepIssueDetails() + com.cisco.framework.utilities.logging.html.Element.SPACE;
		}
		entryissueDetails = testIterationIssueCategory.trim();

		return entryissueDetails;
	}

	public HTMLListItemElement getContent() {
		if (testFunctions != null) {
			if (!testFunctions.isEmpty()) {
				// int testFunctionCounter = 0;
				String entryStatusColor = "";
				// String testFunctionTotalCalls = String.valueOf(testFunctions.size());

				htmlListItemElementLabel = testIterationName + com.cisco.framework.utilities.logging.html.Element.SPACE + TEST_METHOD_EXECUTION_LABEL;
				htmlListItemElementTestIteration = new HTMLListItemElement(htmlListItemElementLabel);

				for (TestFunction testFunction : testFunctions) {
					this.htmlUnOrderedListElementMKTreeTestFunction = new HTMLUnOrderedListElement(Log.MKTREE_CSS.replaceAll(".css", "").trim());
					this.htmlTableCellElementTestFunction = new HTMLTableCellElement();
					this.htmlTableElementTestFunction = new HTMLTableElement("", 1, 0, 0, 100);

					// testFunctionCounter++;
					entryStatusColor = testFunction.getEntryStatusColor();
					HTMLListItemElement htmlListItemElement = testFunction.getContent();

					this.htmlUnOrderedListElementMKTreeTestFunction.append(htmlListItemElement);
					// Populate "htmlTableCellElementTestMethod" with "htmlUnOrderedListElementMKTreeTestMethod"
					this.htmlTableCellElementTestFunction.append(this.htmlUnOrderedListElementMKTreeTestFunction);

					// Create a new HTMLTableRowElement object initialized with the appropriate background color
					this.htmlTableRowElementTestFunction = new HTMLTableRowElement(entryStatusColor);
					// Populate "htmlTableRowElementTestMethod" with "htmlTableCellElementTestMethod"
					this.htmlTableRowElementTestFunction.append(htmlTableCellElementTestFunction);

					// Populate "htmlTableElementTestMethod" with "htmlTableRowElementTestMethod"
					this.htmlTableElementTestFunction.append(this.htmlTableRowElementTestFunction);

					// Create a new "htmlUnOrderedListElementTestMethod" object and populate it with "htmlTableElementTestMethod" object
					this.htmlUnOrderedListElementTestFunction = new HTMLUnOrderedListElement();
					this.htmlUnOrderedListElementTestFunction.append(this.htmlTableElementTestFunction);

					// Create a new "htmlListItemElementTestCase" object and populate it with "htmlUnOrderedListElementTestMethod" object
					this.htmlListItemElementTestIteration.append(this.htmlUnOrderedListElementTestFunction);
				}

			}
		}

		return htmlListItemElementTestIteration;
	}

}