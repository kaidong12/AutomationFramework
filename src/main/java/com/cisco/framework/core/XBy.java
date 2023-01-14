package com.cisco.framework.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;

public class XBy extends By {
	private final String selector;

	public static By cssSelector(final String selector) {
		return new XBy(selector);
	}

	public XBy(final String selector) {
		this.selector = selector;
	}

	/*
	 * Sizzle v1.9.4-pre | (c) 2013 jQuery Foundation, Inc. | jquery.org/license
	 */
	private static String loadJS() {
		BufferedReader bfr = null;
		String jsstr = null;
		try {
			bfr = new BufferedReader(new FileReader("..//AutomationFramework" + System.getProperty("file.separator") + "js\\sizzle.min.js"));
			jsstr = bfr.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bfr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return jsstr;
	}

	private static final String sizzleJavascript = loadJS();

	@Override
	public WebElement findElement(final SearchContext context) {
		List<WebElement> allElements = findElements(context);
		if (allElements == null || allElements.isEmpty())
			throw new NoSuchElementException("Cannot locate an element using " + toString());
		return allElements.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WebElement> findElements(final SearchContext context) {
		if (context instanceof JavascriptExecutor) {
			final JavascriptExecutor javascriptContext = (JavascriptExecutor) context;
			javascriptContext.executeScript(sizzleJavascript);
			return (List<WebElement>) javascriptContext.executeScript("return Sizzle(arguments[0])", selector);
		}

		if (context instanceof WebElement && context instanceof WrapsDriver) {
			final WrapsDriver wrapsdriverContext = (WrapsDriver) context;
			final WebDriver driver = wrapsdriverContext.getWrappedDriver();
			final JavascriptExecutor javascriptContext = (JavascriptExecutor) driver;
			javascriptContext.executeScript(sizzleJavascript);
			if (driver instanceof JavascriptExecutor) {
				return (List<WebElement>) javascriptContext.executeScript("return Sizzle(arguments[0], arguments[1])", selector, context);
			}
		}
		throw new UnsupportedOperationException("Can only search using sizzle on JavascriptExecutors, or WebElements which wrap JavascriptExecutors");
	}

	@Override
	public String toString() {
		// A stub to prevent endless recursion in hashCode()
		return "[unknown locator]";
	}

}
