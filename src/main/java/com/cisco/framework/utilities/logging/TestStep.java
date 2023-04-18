package com.cisco.framework.utilities.logging;

import java.io.File;

import com.cisco.framework.utilities.logging.html.*;

/**
 * @author Lance Yan
 * 
 */
public class TestStep {
	private String				entryStatusColor						= "";
	private String				issueCategory							= "";
	private final int			DIV_MESSAGE_HEIGHT						= 75;
	private final int			DIV_METHOD_NAME_WIDTH					= 250;
	private final int			DIV_METHOD_NAME_HEIGHT					= 50;
	private final int			DIV_ENTRY_STATUS_WIDTH					= 120;
	private final int			DIV_ENTRY_STATUS_HEIGHT					= 75;
	private final int			DIV_LOCATOR_NAME_OR_PARAMETER_WIDTH		= 230;
	private final int			DIV_LOCATOR_NAME_OR_PARAMETER_HEIGHT	= 75;
	private final int			DIV_ACTION_VALUE_OR_MESSAGE_WIDTH		= 585;
	private final int			DIV_ACTION_VALUE_OR_MESSAGE_HEIGHT		= 75;
	private final int			DIV_MESSAGE_WIDTH						= 1175;
	private final int			DIV_SCREEN_SHOT_WIDTH					= 500;
	private final int			DIV_SCREEN_SHOT_HEIGHT					= 150;
	private HTMLTableRowElement	htmlTableRowElementStep					= null;

	public TestStep(String message) {
		init(message);
	}

	public TestStep(String screenShot, int entryStatus) {
		init(screenShot, entryStatus);
	}

	public TestStep(String methodName, String locatorNameOrParameter, String actionValueOrMessage) {
		init(methodName, locatorNameOrParameter, actionValueOrMessage);
	}

	public TestStep(String methodName, String locatorNameOrParameter, String actionValueOrMessage, int entryStatus) {
		init(methodName, locatorNameOrParameter, actionValueOrMessage, entryStatus);
	}

	public TestStep(String methodName, String locatorNameOrParameter, String actionValueOrMessage, int entryStatus, int issueCategory) {
		init(methodName, locatorNameOrParameter, actionValueOrMessage, entryStatus, issueCategory);
	}

	public static void setScreenShot(String screenShot) {
	}

	public String getEntryStatusColor() {
		return entryStatusColor;
	}

	public String getIssueCategory() {
		return issueCategory;
	}

	public HTMLTableRowElement getContent() {
		return htmlTableRowElementStep;
	}

	private void init(String message) {
		htmlTableRowElementStep = new HTMLTableRowElement(this.setTableRowElementBackgroundColor(Log.DEBUG), "", "");
		if (htmlTableRowElementStep != null) {
			htmlTableRowElementStep.append(setMessage(message));
		}
	}

	private void init(String screenShot, int entryStatus) {
		htmlTableRowElementStep = new HTMLTableRowElement(this.setTableRowElementBackgroundColor(Log.DEBUG), "", "");
		if (htmlTableRowElementStep != null) {
			HTMLTableCellElement res = setScreenShot(screenShot, entryStatus);
			if (res != null) {
				htmlTableRowElementStep.append(res);
			}
		}
	}

	private void init(String methodName, String locatorNameOrParameter, String actionValueOrMessage) {
		htmlTableRowElementStep = new HTMLTableRowElement(this.setTableRowElementBackgroundColor(Log.DEBUG), "", "");
		if (htmlTableRowElementStep != null) {
			htmlTableRowElementStep.append(setMethodName(methodName));
			htmlTableRowElementStep.append(setEntryStatus(Log.DEBUG));
			htmlTableRowElementStep.append(setLocatorNameOrParameter(locatorNameOrParameter));
			htmlTableRowElementStep.append(setActionValueOrMessage(actionValueOrMessage));
		}
	}

	private void init(String methodName, String locatorNameOrParameter, String actionValueOrMessage, int entryStatus) {
		htmlTableRowElementStep = new HTMLTableRowElement(this.setTableRowElementBackgroundColor(entryStatus), "", "");
		if (htmlTableRowElementStep != null) {
			htmlTableRowElementStep.append(setMethodName(methodName));
			htmlTableRowElementStep.append(setEntryStatus(entryStatus));
			htmlTableRowElementStep.append(setLocatorNameOrParameter(locatorNameOrParameter));
			htmlTableRowElementStep.append(setActionValueOrMessage(actionValueOrMessage));
		}
	}

	private void init(String methodName, String locatorNameOrParameter, String actionValueOrMessage, int entryStatus, int issueCategory) {
		htmlTableRowElementStep = new HTMLTableRowElement(this.setTableRowElementBackgroundColor(entryStatus), "", "");
		if (htmlTableRowElementStep != null) {
			htmlTableRowElementStep.append(setMethodName(methodName));
			htmlTableRowElementStep.append(setEntryStatus(entryStatus, issueCategory));
			htmlTableRowElementStep.append(setLocatorNameOrParameter(locatorNameOrParameter));
			htmlTableRowElementStep.append(setActionValueOrMessage(actionValueOrMessage));
		}
	}

	private HTMLTableCellElement setMessage(String message) {
		// HTMLTableCellElement htmlTableCellElement = new HTMLTableCellElement("","left","top",4,0,0,0);
		HTMLTableCellElement htmlTableCellElement = new HTMLTableCellElement("", "left", "bottom", 4, 0, 0, 0);
		if (htmlTableCellElement != null) {
			if (!message.isEmpty()) {
				HTMLPreElement pre = new HTMLPreElement();
				HTMLDivElement div = new HTMLDivElement("left", this.DIV_MESSAGE_WIDTH, this.DIV_MESSAGE_HEIGHT);
				pre.append(asciiToHtmlEntityConverter(message));
				div.append(pre);
				htmlTableCellElement.append(div);
			} else {
				htmlTableCellElement.append("&nbsp;");
			}
		}
		return htmlTableCellElement;
	}

	private HTMLTableCellElement setMethodName(String methodName) {
		// HTMLTableCellElement htmlTableCellElement = new HTMLTableCellElement("","justify","middle",0,0,0,0);
		HTMLTableCellElement htmlTableCellElement = new HTMLTableCellElement("", "justify", "bottom", 0, 0, 0, 0);
		if (htmlTableCellElement != null) {
			if (!methodName.isEmpty()) {
				HTMLPreElement pre = new HTMLPreElement();
				HTMLDivElement div = new HTMLDivElement("left", this.DIV_METHOD_NAME_WIDTH, this.DIV_METHOD_NAME_HEIGHT);
				pre.append(methodName);
				div.append(pre);
				htmlTableCellElement.append(div);
			} else {
				htmlTableCellElement.append("&nbsp;");
			}
		}
		return htmlTableCellElement;
	}

	private HTMLTableCellElement setEntryStatus(int entryStatus, int issueCategory) {
		if (issueCategory == Log.FEATURE_CHANGE) {
			this.issueCategory = "Feature Change";
		}
		if (issueCategory == Log.SCRIPT_ISSUE) {
			this.issueCategory = "Script Issue";
		}
		if (issueCategory == Log.MINOR_ISSUE) {
			this.issueCategory = "Minor Issue";
		}
		if (issueCategory == Log.MAJOR_ISSUE) {
			this.issueCategory = "Major Issue";
		}
		if (issueCategory == Log.NO_ISSUE) {
			this.issueCategory = "";
		}
		return this.setEntryStatus(entryStatus);
	}

	private HTMLTableCellElement setEntryStatus(int entryStatus) {
		// HTMLTableCellElement htmlTableCellElement = new HTMLTableCellElement("", "center", "middle", 0, 0, 0, 0);
		HTMLTableCellElement htmlTableCellElement = new HTMLTableCellElement("", "center", "bottom", 0, 0, 0, 0);
		HTMLPreElement pre = new HTMLPreElement();
		HTMLDivElement div = new HTMLDivElement("center", this.DIV_ENTRY_STATUS_WIDTH, this.DIV_ENTRY_STATUS_HEIGHT);
		if (htmlTableCellElement != null) {
			if (entryStatus == Log.DEBUG) {
				entryStatusColor = Log.WHITE;
				htmlTableCellElement.append("&nbsp;");
			}
			if ((entryStatus == Log.ERROR) || (entryStatus == Log.FAIL) || (entryStatus == Log.DEFECT)) {
				entryStatusColor = Log.RED;
				if (entryStatus == Log.ERROR) {
					if (issueCategory.isEmpty()) {
						pre.append("ERROR");
						div.append(pre);
						htmlTableCellElement.append(div);
					} else {
						pre.append("ERROR" + Element.LINE_BREAK + issueCategory);
						div.append(pre);
						htmlTableCellElement.append(div);
					}
				}
				if (entryStatus == Log.DEFECT) {
					if (issueCategory.isEmpty()) {
						pre.append("DEFECT");
						div.append(pre);
						htmlTableCellElement.append(div);
					} else {
						pre.append("DEFECT" + Element.LINE_BREAK + issueCategory);
						div.append(pre);
						htmlTableCellElement.append(div);
					}
				}
				if (entryStatus == Log.FAIL) {
					if (issueCategory.isEmpty()) {
						pre.append("FAIL");
						div.append(pre);
						htmlTableCellElement.append(div);
					} else {
						pre.append("FAIL" + Element.LINE_BREAK + issueCategory);
						div.append(pre);
						htmlTableCellElement.append(div);
					}
				}
			}
			if (entryStatus == Log.PASS) {
				if (issueCategory.isEmpty()) {
					entryStatusColor = Log.GREEN;
					pre.append("PASS");
					div.append(pre);
					htmlTableCellElement.append(div);
				} else {
					entryStatusColor = Log.GREEN;
					pre.append("PASS" + Element.LINE_BREAK + issueCategory);
					div.append(pre);
					htmlTableCellElement.append(div);
				}

			}
			if (entryStatus == Log.WARN) {
				if (issueCategory.isEmpty()) {
					entryStatusColor = Log.YELLOW;
					pre.append("WARNING");
					div.append(pre);
					htmlTableCellElement.append(div);
				} else {
					entryStatusColor = Log.YELLOW;
					pre.append("WARNING" + Element.LINE_BREAK + issueCategory);
					div.append(pre);
					htmlTableCellElement.append(div);
				}
			}
		}
		return htmlTableCellElement;
	}

	private HTMLTableCellElement setLocatorNameOrParameter(String locatorNameOrParameter) {
		// HTMLTableCellElement htmlTableCellElement = new HTMLTableCellElement("", "left", "top", 0, 0, 0, 0);
		HTMLTableCellElement htmlTableCellElement = new HTMLTableCellElement("", "left", "bottom", 0, 0, 0, 0);
		if (htmlTableCellElement != null) {
			if (locatorNameOrParameter != null) {
				if (!locatorNameOrParameter.isEmpty()) {
					HTMLPreElement pre = new HTMLPreElement();
					HTMLDivElement div = new HTMLDivElement("left", this.DIV_LOCATOR_NAME_OR_PARAMETER_WIDTH,
							this.DIV_LOCATOR_NAME_OR_PARAMETER_HEIGHT);
					pre.append(locatorNameOrParameter);
					div.append(pre);
					htmlTableCellElement.append(div);
				} else {
					htmlTableCellElement.append("&nbsp;");
				}
			}
		}
		return htmlTableCellElement;
	}

	private HTMLTableCellElement setActionValueOrMessage(String actionValueOrMessage) {
		// HTMLTableCellElement htmlTableCellElement = new HTMLTableCellElement("", "justify", "top", 0, 0, 0, 0);
		HTMLTableCellElement htmlTableCellElement = new HTMLTableCellElement("", "justify", "bottom", 0, 0, 0, 0);
		if (htmlTableCellElement != null) {
			if (actionValueOrMessage != null) {
				if (!actionValueOrMessage.isEmpty()) {
					HTMLPreElement pre = new HTMLPreElement();
					HTMLDivElement div = new HTMLDivElement("left", this.DIV_ACTION_VALUE_OR_MESSAGE_WIDTH, this.DIV_ACTION_VALUE_OR_MESSAGE_HEIGHT);
					pre.append(asciiToHtmlEntityConverter(actionValueOrMessage));
					div.append(pre);
					htmlTableCellElement.append(div);
				} else {
					htmlTableCellElement.append("&nbsp;");
				}
			}
		}
		return htmlTableCellElement;
	}

	private String setTableRowElementBackgroundColor(int entryStatus) {
		String backgroundColor = "";
		if (entryStatus == Log.DEBUG) {
			backgroundColor = Log.WHITE;
		}
		if (entryStatus == Log.ERROR) {
			backgroundColor = Log.RED;
		}
		if (entryStatus == Log.PASS) {
			backgroundColor = Log.GREEN;
		}
		if (entryStatus == Log.WARN) {
			backgroundColor = Log.YELLOW;
		}
		return backgroundColor;
	}

	private HTMLTableCellElement setScreenShot(String screenShot, int entryStatus) {
		HTMLTableCellElement htmlTableCellElement = null;
		if (!screenShot.isEmpty()) {
			if (entryStatus == Log.DEFECT || entryStatus == Log.ERROR || entryStatus == Log.FAIL || entryStatus == Log.SCREEN_SHOT
					|| entryStatus == Log.WARN) {
				String screenShotName = new File(screenShot).getName();
				// htmlTableCellElement = new HTMLTableCellElement("", "center", "middle", 4, 0, 0, 0);
				htmlTableCellElement = new HTMLTableCellElement("", "center", "bottom", 4, 0, 0, 0);
				if (htmlTableCellElement != null) {
					HTMLAnchorElement screenShotLink = new HTMLAnchorElement(screenShot, "_blank");
					if (screenShotLink != null) {
						HTMLImageElement screenShotImage = new HTMLImageElement(screenShot, "Log ScreenShot", "Log ScreenShot", Log.SCREEN_SHOT_WIDTH,
								Log.SCREEN_SHOT_HEIGHT);
						screenShotLink.append(screenShotImage.getContent() + "<br/>" + screenShotName);
						HTMLDivElement div = new HTMLDivElement("center", this.DIV_SCREEN_SHOT_WIDTH, this.DIV_SCREEN_SHOT_HEIGHT);
						div.append(screenShotLink);
						htmlTableCellElement.append(div);
					}
				}
			}
		}
		return htmlTableCellElement;
	}

	private String asciiToHtmlEntityConverter(String stringToConvert) {
		StringBuilder sb = new StringBuilder();

		if (stringToConvert != null) {
			if (!stringToConvert.isEmpty()) {
				int numCharacters = stringToConvert.length();
				for (int i = 0; i < numCharacters; i++) {
					char ch = stringToConvert.charAt(i);
					if ((Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.BASIC_LATIN) && Character.isLetterOrDigit(ch)) {
						sb.append(ch);
					} else {
						if (!Character.isWhitespace(ch)) {
							sb.append("&#" + String.valueOf((int) ch) + ";");
						} else {
							sb.append(ch);
						}
					}
				}
				sb.trimToSize();
			}
		}

		return sb.toString();
	}
}