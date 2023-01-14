package com.cisco.framework.core.exceptions;

/**
 * @author Francesco Ferrante
 */
public class FrameworkException extends RuntimeException {

	private static final long	serialVersionUID		= 1L;
	private String				methodName				= "";
	private String				locatorNameOrParameter	= "";
	private String				actionValueOrMessage	= "";
	private int					entryStatus				= 0;
	private int					issueCategory			= 0;

	/**
	 * @author Francesco Ferrante
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
	 *            Use this constructor to instantiate a "FrameworkException" object given a user-specified:<br>
	 *            1. "methodName".<br>
	 *            2. "locatorNameOrParameter".<br>
	 *            3. "actionValueOrMessage".<br>
	 *            4. "entryStatus".<br>
	 *            5. "issueCategory".<br>
	 *            <br>
	 *            For a "FrameworkException" object arguments "entryStatus" and "issueCategory" usually take on the respective values<br>
	 *            "Log.ERROR" and "Log.SCRIPT_ISSUE".<br>
	 */
	public FrameworkException(String methodName, String locatorNameOrParameter, String actionValueOrMessage, int entryStatus, int issueCategory) {
		super();
		this.methodName = methodName;
		this.locatorNameOrParameter = locatorNameOrParameter;
		this.actionValueOrMessage = actionValueOrMessage;
		this.entryStatus = entryStatus;
		this.issueCategory = issueCategory;
	}

	/**
	 * @author Francesco Ferrante
	 * @return String <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to return the "method name".
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @author Francesco Ferrante
	 * @return the locatorNameOrParameter <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to return the "locator name or parameter".
	 */
	public String getLocatorNameOrParameter() {
		return locatorNameOrParameter;
	}

	/**
	 * @author Francesco Ferrante
	 * @return the actionValueOrMessage <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to return the "action value or message".
	 */
	public String getActionValueOrMessage() {
		return actionValueOrMessage;
	}

	/**
	 * @author Francesco Ferrante
	 * @return the entryStatus <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to return the "entry status".
	 */
	public int getEntryStatus() {
		return entryStatus;
	}

	/**
	 * @author Francesco Ferrante
	 * @return the issueCategory <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to return the "issue category".
	 */
	public int getIssueCategory() {
		return issueCategory;
	}
}