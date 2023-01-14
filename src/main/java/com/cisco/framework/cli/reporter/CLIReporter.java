package com.cisco.framework.cli.reporter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

public class CLIReporter implements IReporter {

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

		BufferedWriter writer = null;
		try {
			// writer=new BufferedWriter(new FileWriter("c:/"+ convertCmdToFileName(cmd)));
			writer = new BufferedWriter(new FileWriter("./test-output/suite.txt"));
			for (ISuite suite : suites) {
				// writer.write(suite.getName());
				// writer.write("\n");
				Map<String, ISuiteResult> results = suite.getResults();
				@SuppressWarnings("rawtypes")
				Set s = results.entrySet();
				@SuppressWarnings("rawtypes")
				Iterator it = s.iterator();
				while (it.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String, ISuiteResult> m = (Map.Entry<String, ISuiteResult>) it.next();
					ISuiteResult result = m.getValue();
					ITestContext context = result.getTestContext();
					IResultMap passedTests = context.getPassedTests();
					IResultMap failedTests = context.getFailedTests();
					IResultMap skippedTests = context.getSkippedTests();

					for (ITestResult r : passedTests.getAllResults()) {
						writer.write("passed: " + r.getName());
						writer.write("\n");
					}

					for (ITestResult r : failedTests.getAllResults()) {
						String message = r.getThrowable().getMessage();
						if (message == null || message.equals("")) {
							message = "null";
						}
						writer.write("failed: " + r.getName() + "MESSAGE" + message);
						writer.write("\n");
					}

					for (ITestResult r : skippedTests.getAllResults()) {
						// String message=r.getThrowable().getMessage();
						String message = null;
						if (r != null && r.getThrowable() != null) {
							message = r.getThrowable().getMessage();
						}

						if (message == null || message.equals("")) {
							message = "null";
						}

						if (r != null) {
							writer.write("skipped: " + r.getName() + "MESSAGE" + message);
							writer.write("\n");
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
