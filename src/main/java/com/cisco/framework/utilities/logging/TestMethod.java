package com.cisco.framework.utilities.logging;

import java.util.ArrayList;
import java.util.List;

import com.cisco.framework.utilities.logging.html.HTMLListItemElement;
import com.cisco.framework.utilities.logging.html.HTMLTableCellElement;
import com.cisco.framework.utilities.logging.html.HTMLTableElement;
import com.cisco.framework.utilities.logging.html.HTMLTableRowElement;
import com.cisco.framework.utilities.logging.html.HTMLUnOrderedListElement;

/**
 * @author Francesco Ferrante
 * 
 */
public class TestMethod {
	private String						testMethodName								= "";
	private String						testMethodDesc								= "";
	private String						entryStatusColor							= "";
	private String						entryStatus									= "";

	private List<TestIteration>			testIterations								= null;
	private HTMLUnOrderedListElement	htmlUnOrderedListElementMKTreeTestIteration	= null;
	private HTMLTableCellElement		htmlTableCellElementTestIteration			= null;
	private HTMLTableRowElement			htmlTableRowElementTestIteration			= null;
	private HTMLTableElement			htmlTableElementTestIteration				= null;
	private HTMLUnOrderedListElement	htmlUnOrderedListElementTestIteration		= null;
	private HTMLListItemElement			htmlListItemElementTestMethod				= null;

	public TestMethod(String testMethodName) {
		this.init(testMethodName);
	}

	public void addTestIteration(TestIteration testIteration) {
		this.testIterations.add(testIteration);
	}

	public String getTestMethodName() {
		return this.testMethodName;
	}

	public String getEntryStatus() {
		return this.entryStatus;
	}

	public void setTestMethodName(String strTestMethodName) {
		testMethodName = strTestMethodName;
	}

	public void setTestMethodDesc(String strTestMethodDesc) {
		testMethodDesc = strTestMethodDesc;
	}

	@Override
	public boolean equals(Object testMethod) {
		return this.testMethodName.equalsIgnoreCase(((TestMethod) testMethod).getTestMethodName());
	}

	public String getEntryStatusColor() {
		String testMethodColors = "";
		for (TestIteration testIteration : testIterations) {
			testMethodColors += testIteration.getEntryStatusColor() + com.cisco.framework.utilities.logging.html.Element.SPACE;
		}
		if (testMethodColors.contains(Log.RED)) {
			this.entryStatusColor = Log.RED;
			this.entryStatus = "failed";
		} else {
			if (testMethodColors.contains(Log.YELLOW)) {
				this.entryStatusColor = Log.YELLOW;
				this.entryStatus = "pass/x";
			} else {
				this.entryStatusColor = Log.GREEN;
				this.entryStatus = "passed";
			}
			// this.entryStatus = "pass";
		}
		return this.entryStatusColor;
	}

	public String getContent() {
		if (testIterations != null) {
			if (!testIterations.isEmpty()) {
				int testExecutionCounter = 0;
				String entryStatusColor = "";
				String testMethodTotalExecutions = String.valueOf(testIterations.size());
				if (this.testMethodDesc.equals("")) {
					this.htmlListItemElementTestMethod = new HTMLListItemElement(this.testMethodName);
				} else {
					this.htmlListItemElementTestMethod = new HTMLListItemElement(this.testMethodDesc);
				}

				for (TestIteration testIteration : testIterations) {
					this.htmlUnOrderedListElementMKTreeTestIteration = new HTMLUnOrderedListElement(Log.MKTREE_CSS.replaceAll(".css", "").trim());
					this.htmlTableCellElementTestIteration = new HTMLTableCellElement();
					this.htmlTableElementTestIteration = new HTMLTableElement("", 1, 0, 0, 100);

					testExecutionCounter++;
					entryStatusColor = testIteration.getEntryStatusColor();
					HTMLListItemElement htmlListItemElement = testIteration.getContent();
					htmlListItemElement.replace(TestIteration.TEST_METHOD_EXECUTION_LABEL,
							"(" + String.valueOf(testExecutionCounter) + "/" + testMethodTotalExecutions + ")").trim();
					this.htmlUnOrderedListElementMKTreeTestIteration.append(htmlListItemElement);

					// Populate "htmlTableCellElementTestMethod" with "htmlUnOrderedListElementMKTreeTestMethod"
					this.htmlTableCellElementTestIteration.append(this.htmlUnOrderedListElementMKTreeTestIteration);

					// Create a new HTMLTableRowElement object initialized with the appropriate background color
					this.htmlTableRowElementTestIteration = new HTMLTableRowElement(entryStatusColor);

					// Populate "htmlTableRowElementTestMethod" with "htmlTableCellElementTestMethod"
					this.htmlTableRowElementTestIteration.append(htmlTableCellElementTestIteration);

					// Populate "htmlTableElementTestMethod" with "htmlTableRowElementTestMethod"
					this.htmlTableElementTestIteration.append(this.htmlTableRowElementTestIteration);

					// Create a new "htmlUnOrderedListElementTestMethod" object and populate it with "htmlTableElementTestMethod" object
					this.htmlUnOrderedListElementTestIteration = new HTMLUnOrderedListElement();
					this.htmlUnOrderedListElementTestIteration.append(this.htmlTableElementTestIteration);

					// Create a new "htmlListItemElementTestCase" object and populate it with "htmlUnOrderedListElementTestMethod" object
					this.htmlListItemElementTestMethod.append(this.htmlUnOrderedListElementTestIteration);
				}
			}
		}

		return this.htmlListItemElementTestMethod.getContent();
	}

	private void init(String testMethodName) {
		this.testMethodName = testMethodName;
		this.testIterations = new ArrayList<TestIteration>();
	}

	/**
	 * @author Murali K Parepalli
	 * @return the testIterations
	 */
	public List<TestIteration> getTestIterations() {
		return testIterations;
	}
}