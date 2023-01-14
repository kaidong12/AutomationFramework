package com.cisco.framework.cli.utils;

import java.util.List;
import org.testng.Assert;

public class CLIAssert extends Assert {
	protected CLIAssert() {
		super();
	}

	public static void assertMatchesRegex(List<String> actualLst, List<String> expectedLst) {
		if (expectedLst.size() != actualLst.size()) {
			failWithMessage("Expected size: " + expectedLst.size(), "Actual size: " + actualLst.size());
		} else {
			for (int i = 0; i < expectedLst.size(); i++) {
				if (expectedLst.get(i).endsWith("$")) {
					String expectedStr = expectedLst.get(i).replace("$", "").trim().toLowerCase();
					if (!(actualLst.get(i).toLowerCase().startsWith(expectedStr))) {
						failWithMessage("Expected regex(startsWith): " + expectedLst.get(i), "Actual text: " + actualLst.get(i));
					}
				} else if (expectedLst.get(i).startsWith("__END__")) {
					String expectedStr = expectedLst.get(i).replace("__END__", "").trim().toLowerCase();
					if (!(actualLst.get(i).toLowerCase().endsWith(expectedStr))) {
						failWithMessage("Expected regex(endsWith): " + expectedLst.get(i), "Actual text: " + actualLst.get(i));
					}
				} else if (expectedLst.get(i).startsWith("__CONTAINS__")) {
					String expectedStr = expectedLst.get(i).replace("__CONTAINS__", "").trim().toLowerCase();
					if (!(actualLst.get(i).toLowerCase().endsWith(expectedStr))) {
						failWithMessage("Expected regex(contains): " + expectedLst.get(i), "Actual text: " + actualLst.get(i));
					}
				} else {
					// Assert.assertEquals(actualLst.get(i), expectedLst.get(i));
					Assert.assertEquals(actualLst.get(i).trim().toLowerCase(), expectedLst.get(i).trim().toLowerCase());
				}
			}
		}
	}

	/**
	 *
	 * 2014-10-9, chaoy Description:
	 *
	 * @param msg
	 */
	public static void assertNoErrorMessage(String msg) {
		if (msg == null || msg.trim().equals("")) {
			return;
		}

		String[] errorKeyWordsDirectory = { "error", "fail", "wrong", "dead" };
		String[] msgLines = msg.split("\n");

		for (String line : msgLines) {
			for (String keyWord : errorKeyWordsDirectory) {
				if (line.contains(keyWord)) {
					Assert.fail("find error message: " + line);
				}
			}
		}
	}

	private static void failWithMessage(String message, String ourMessage) {
		Assert.fail(message != null ? message + " | " + ourMessage : ourMessage);
	}

}
