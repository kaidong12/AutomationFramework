package com.cisco.framework.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import com.cisco.framework.core.exceptions.FrameworkException;
import com.cisco.framework.utilities.logging.Log;

/**
 * @author Francesco Ferrante
 *
 */
public class TestData {

	private String				pathToTestDataFile			= "";
	private String				testCaseNameColumnHeader	= "TestCaseName";
	private String				browserOSColumnHeader		= "BrowserOS";
	private String				useThisDataColumnHeader		= "UseThisData";
	private List<String>		testDataColumnHeaders		= null;
	private int					testDataCount				= 0;
	private int					testTotalDataCount			= 0;
	private boolean				checkForMandatoryColumns	= false;
	public static final String	UTF8						= "UTF-8";
	public static final String	UTF16						= "UTF-16";
	private String				encodingFormat				= "";

	/**
	 * @author Francesco Ferrante <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         This is a temporary default constructor for purposes of making code in "TestDataFactory" to compile with no errors.
	 */
	private TestData() {
	}

	/**
	 * @author Francesco Ferrante
	 * @param pathToTestDataFile
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Use this constructor to instantiate a "TestData" object given a valid path and filename<br>
	 *            to a csv test data file. By default the checking for mandatory columns is enabled<br>
	 */
	public TestData(String pathToTestDataFile) {
		this.checkForMandatoryColumns = true;
		this.pathToTestDataFile = pathToTestDataFile;
	}

	/**
	 * @author Francesco Ferrante
	 * @param pathToTestDataFile
	 * @param checkForMandatoryColumns
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Use this constructor to instantiate a "TestData" object given a valid path and filename<br>
	 *            to a csv test data file. If "checkForMandatoryColumns" is set to "true"(default) checking of all<br>
	 *            mandatory columns will be performed. If set to "false", the checking of all mandatory columns will<br>
	 *            disabled.<br>
	 */
	public TestData(String pathToTestDataFile, boolean checkForMandatoryColumns) {
		this.checkForMandatoryColumns = checkForMandatoryColumns;
		this.pathToTestDataFile = pathToTestDataFile;
	}

	/**
	 * @author Francesco Ferrante
	 * @param pathToTestDataFile
	 * @param checkForMandatoryColumns
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Use this constructor to instantiate a "TestData" object given a valid path and filename<br>
	 *            to a csv test data file. If "checkForMandatoryColumns" is set to "true"(default) checking of all<br>
	 *            mandatory columns will be performed. If set to "false", the checking of all mandatory columns will<br>
	 *            disabled.<br>
	 */
	public TestData(String pathToTestDataFile, final String encodingFormat) {
		this.pathToTestDataFile = pathToTestDataFile;
		this.encodingFormat = encodingFormat;
	}

	/**
	 * @author Francesco Ferrante
	 * @param pathToTestDataFile
	 * @param checkForMandatoryColumns
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Use this constructor to instantiate a "TestData" object given a valid path and filename<br>
	 *            to a csv test data file. If "checkForMandatoryColumns" is set to "true"(default) checking of all<br>
	 *            mandatory columns will be performed. If set to "false", the checking of all mandatory columns will<br>
	 *            disabled.<br>
	 */
	public TestData(String pathToTestDataFile, boolean checkForMandatoryColumns, final String encodingFormat) {
		this.checkForMandatoryColumns = checkForMandatoryColumns;
		this.pathToTestDataFile = pathToTestDataFile;
		this.encodingFormat = encodingFormat;
	}

	/**
	 * @author Francesco Ferrante
	 * @return the testCaseNameColumnHeader <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to retrieve the current "test case name column header" in use.<br>
	 *         The default value is "TestCaseName".<br>
	 */
	public String getTestCaseNameColumnHeader() {
		return testCaseNameColumnHeader;
	}

	/**
	 * @author Francesco Ferrante
	 * @param testCaseNameColumnHeader
	 *            the testCaseNameColumnHeader to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Use this method to change the default value of "test case name column header".<br>
	 *            If an invalid entry in the form of a null-valued quantity or an empty string is<br>
	 *            attempted the default value will not be changed.<br>
	 */
	public void setTestCaseNameColumnHeader(String testCaseNameColumnHeader) {
		if (testCaseNameColumnHeader != null) {
			if (!testCaseNameColumnHeader.isEmpty()) {
				this.testCaseNameColumnHeader = testCaseNameColumnHeader;
			}
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the browserOSColumnHeader <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to retrieve the current "browser OS column header" in use.<br>
	 *         The default value is "BrowserOS".<br>
	 */
	public String getBrowserOSColumnHeader() {
		return browserOSColumnHeader;
	}

	/**
	 * @author Francesco Ferrante
	 * @param browserOSColumnHeader
	 *            the browserOSColumnHeader to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Use this method to change the default value of "browser OS column header".<br>
	 *            If an invalid entry in the form of a null-valued quantity or an empty string is<br>
	 *            attempted the default value will not be changed.<br>
	 */
	public void setBrowserOSColumnHeader(String browserOSColumnHeader) {
		if (browserOSColumnHeader != null) {
			if (!browserOSColumnHeader.isEmpty()) {
				this.browserOSColumnHeader = browserOSColumnHeader;
			}
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the useThisDataColumnHeader <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to retrieve the current "use this data column header" in use.<br>
	 *         The default value is "UseThisData".<br>
	 */
	public String getUseThisDataColumnHeader() {
		return useThisDataColumnHeader;
	}

	/**
	 * @author Francesco Ferrante
	 * @param useThisDataColumnHeader
	 *            the useThisDataColumnHeader to set <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Use this method to change the default value of "use this data column header".<br>
	 *            If an invalid entry in the form of a null-valued quantity or an empty string is<br>
	 *            attempted the default value will not be changed.<br>
	 */
	public void setUseThisDataColumnHeader(String useThisDataColumnHeader) {
		if (useThisDataColumnHeader != null) {
			if (!useThisDataColumnHeader.isEmpty()) {
				this.useThisDataColumnHeader = useThisDataColumnHeader;
			}
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @return the testDataColumnHeaders <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to retrieve the current column headers use.<br>
	 */
	public List<String> getTestDataColumnHeaders() {
		return testDataColumnHeaders;
	}

	/**
	 * @author Francesco Ferrante
	 * @return the testDataCount <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to retrieve the "test data count".<br>
	 *         The "test data count" holds the number of test methods that were executed.<br>
	 */
	public int getTestDataCount() {
		return testDataCount;
	}

	/**
	 * @author Francesco Ferrante
	 * @return the testTotalDataCount <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to retrieve the "total test data count".<br>
	 *         The "total test data count" holds the number test items in csv test data file.<br>
	 */
	public int getTestTotalDataCount() {
		return testTotalDataCount;
	}

	/**
	 * @author Francesco Ferrante
	 * @param testData
	 * @param columnName
	 * @return
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to retrieve a test data value by column name based on a given user-specified<br>
	 *             test data item.<br>
	 */
	public String getTestDataValue(Object[] testDataItem, String column) throws FrameworkException {

		if (column == null) {
			throw new FrameworkException("getTestDataValue", "column", "ARGUMENT MAKES REFERENCE TO NULL POINTER.", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		if (column.trim().length() == 0) {
			throw new FrameworkException("getTestDataValue", "column", "INVALID COLUMN NAME.", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		int columnIndex = getColumnIndex(column);

		if (columnIndex < 0) {
			throw new FrameworkException("getTestDataValue", "column", "COLUMN NAME NOT FOUND: " + "'" + column + "'.", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		return testDataItem == null ? null : testDataItem[columnIndex] == null ? null : testDataItem[columnIndex].toString();

	}

	/**
	 * @author Francesco Ferrante
	 * @return Iterator<Object[]>
	 * @throws FrameworkException
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to read an csv test data file containing an arbitrary number of columns<br>
	 *             and rows into an Iterator object. This method uses readTestDataFromCSVFile() to<br>
	 *             perform the actual reading of test data.<br>
	 */
	public Iterator<Object[]> get() throws FrameworkException, Exception {
		return readTestDataFromCSVFile(pathToTestDataFile).iterator();
	}

	/**
	 * @author Francesco Ferrante
	 * @param testCaseName
	 * @return Iterator<Object[]>
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to read an csv test data file containing an arbitrary number of columns<br>
	 *             and filtered rows based on test case name into an Iterator object. This method uses<br>
	 *             readTestDataFromCSVFile() to perform the actual reading of test data.<br>
	 */
	public Iterator<Object[]> get(String testCaseName) throws FrameworkException, Exception {
		return readTestDataFromCSVFile(pathToTestDataFile, testCaseName).iterator();
	}

	/**
	 * @author Francesco Ferrante
	 * @param csvTestDataFile
	 * @return List<Object[]>
	 * @throws IOException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to read a csv test data file containing an arbitrary number of columns<br>
	 *             and rows into an array of array of objects. This method is used as an helper method for<br>
	 *             method getTestDataFromCSVFile(). If csv test data file does not contain mandatory columns<br>
	 *             "testCaseNameColumnHeader", "browserOSColumnHeader", and "useThisDataColumnHeader"<br>
	 *             an appropriate exception will be thrown.<br>
	 */
	private List<Object[]> readTestDataFromCSVFile(String csvTestDataFile) throws Exception, FrameworkException {
		ICsvMapReader mr = null;
		Map<String, String> testDataRow = null;
		List<Object[]> testData = null;
		testTotalDataCount = 0;

		try {
			testData = new ArrayList<Object[]>();
			if (this.encodingFormat != "") {
				mr = new CsvMapReader(new InputStreamReader(new FileInputStream(csvTestDataFile), this.encodingFormat),
						CsvPreference.STANDARD_PREFERENCE);
			} else {
				mr = new CsvMapReader(new InputStreamReader(new FileInputStream(csvTestDataFile)), CsvPreference.STANDARD_PREFERENCE);
			}
			// final String[] columns = mr.getCSVHeader(true);
			final String[] columns = mr.getHeader(true);

			// Check for empty test data file
			if (columns == null) {
				throw new FrameworkException("readTestDataFromCSVFile", "csvTestDataFile", "TEST DATA FILE IS EMPTY: " + "'" + csvTestDataFile + "'.",
						Log.ERROR, Log.SCRIPT_ISSUE);
			}

			// Populate "testDataColumnHeaders" array list with all column
			// headers
			// and check that mandatory columns "TestCaseName", "BrowserOS", and
			// "UseThisData" have been specified only once.
			// If a mandatory column is missing or has been specified mulitple
			// times throw an appropriate exception.
			testDataColumnHeaders = new ArrayList<String>();
			int testCaseNameColumnCounter = 0;
			int browserOSColumnCounter = 0;
			int useThisDataColumnCounter = 0;
			for (String c : columns) {

				// Do a check for mandatory column "testCaseNameColumnHeader".
				if (c.trim().equalsIgnoreCase(testCaseNameColumnHeader)) {
					// Mandatory column has been specified therefore
					// increase counter "testCaseNameColumnCounter" by unity.
					testCaseNameColumnCounter++;
				}

				// Do a check for mandatory column "browserOSColumnHeader".
				if (c.trim().equalsIgnoreCase(browserOSColumnHeader)) {
					// Mandatory column has been specified therefore
					// increase counter "browserOSColumnCounter" by unity.
					browserOSColumnCounter++;
				}

				// Do a check for mandatory column "useThisDataColumnHeader".
				if (c.trim().equalsIgnoreCase(useThisDataColumnHeader)) {
					// Mandatory column has been specified therefore
					// increase counter "useThisDataColumnCounter" by unity.
					useThisDataColumnCounter++;
				}

				testDataColumnHeaders.add(c);
			}

			if (checkForMandatoryColumns) {
				// If mandatory column "testCaseNameColumnHeader" is missing or
				// has been specified multiple times throw an appropriate
				// exception.
				if (testCaseNameColumnCounter == 0) {
					String errorMessage = "MISSING MANDATORY COLUMN: " + "'" + testCaseNameColumnHeader + "'" + System.getProperty("line.separator")
							+ "TEST DATA FILE: " + "'" + csvTestDataFile + "'.";
					throw new FrameworkException("readTestDataFromCSVFile", "csvTestDataFile", errorMessage, Log.ERROR, Log.SCRIPT_ISSUE);
				} else {
					if (testCaseNameColumnCounter > 1) {
						String errorMessage = "DUPLICATE COLUMN: " + "'" + testCaseNameColumnHeader + "'" + System.getProperty("line.separator")
								+ "TEST DATA FILE: " + "'" + csvTestDataFile + "'.";
						throw new FrameworkException("readTestDataFromCSVFile", "csvTestDataFile", errorMessage, Log.ERROR, Log.SCRIPT_ISSUE);
					}
				}
				// If mandatory column "BrowserOS" is missing throw an
				// appropriate exception.
				if (browserOSColumnCounter == 0) {
					String errorMessage = "MISSING MANDATORY COLUMN: " + "'" + browserOSColumnHeader + "'" + System.getProperty("line.separator")
							+ "TEST DATA FILE: " + "'" + csvTestDataFile + "'.";
					throw new FrameworkException("readTestDataFromCSVFile", "csvTestDataFile", errorMessage, Log.ERROR, Log.SCRIPT_ISSUE);
				} else {
					if (browserOSColumnCounter > 1) {
						String errorMessage = "DUPLICATE COLUMN: " + "'" + browserOSColumnHeader + "'" + System.getProperty("line.separator")
								+ "TEST DATA FILE: " + "'" + csvTestDataFile + "'.";
						throw new FrameworkException("readTestDataFromCSVFile", "csvTestDataFile", errorMessage, Log.ERROR, Log.SCRIPT_ISSUE);
					}
				}
				// If mandatory column "UseThisData" is missing throw an
				// appropriate exception.
				if (useThisDataColumnCounter == 0) {
					String errorMessage = "MISSING MANDATORY COLUMN: " + "'" + useThisDataColumnHeader + "'" + System.getProperty("line.separator")
							+ "TEST DATA FILE: " + "'" + csvTestDataFile + "'.";
					throw new FrameworkException("readTestDataFromCSVFile", "csvTestDataFile", errorMessage, Log.ERROR, Log.SCRIPT_ISSUE);
				} else {
					if (useThisDataColumnCounter > 1) {
						String errorMessage = "DUPLICATE COLUMN: " + "'" + useThisDataColumnHeader + "'" + System.getProperty("line.separator")
								+ "TEST DATA FILE: " + "'" + csvTestDataFile + "'.";
						throw new FrameworkException("readTestDataFromCSVFile", "csvTestDataFile", errorMessage, Log.ERROR, Log.SCRIPT_ISSUE);
					}
				}
			}

			while ((testDataRow = mr.read(columns)) != null) {

				// Icrement testTotalDataCount.
				testTotalDataCount++;

				List<Object> row = new ArrayList<Object>();

				for (String c : columns) {
					row.add(testDataRow.get(c));
				}

				testData.add(new Object[] { row.toArray() });
				((ArrayList<Object[]>) testData).trimToSize();
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (mr != null) {
				mr.close();
			}
		}

		return testData;
	}

	/**
	 * @author Francesco Ferrante
	 * @param csvTestDataFile
	 * @param testCaseName
	 * @return List<Object[]>
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to filter out test data based on test case name.<br>
	 */
	private List<Object[]> readTestDataFromCSVFile(String csvTestDataFile, String testCaseName) throws FrameworkException, Exception {
		return filterTestDataByTestCaseName(readTestDataFromCSVFile(csvTestDataFile), testCaseName);
	}

	/**
	 * @author Francesco Ferrante
	 * @param testData
	 * @param testCaseName
	 * @return List<Object[]>
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to filter out all test data items that have a matching test case name and<br>
	 *             a and 'useThisDataColumnHeader' value of case-insensitive "true".<br>
	 */
	private List<Object[]> filterTestDataByTestCaseName(List<Object[]> testData, String testCaseName) throws FrameworkException {

		List<Object[]> filteredTestData = new ArrayList<Object[]>();
		;
		testDataCount = 0;

		validateTestCaseName(testCaseName);

		// Iterate through all test data items in testData and pick
		// those test data items that have as their first column matching
		// the value of 'testCaseName' and a value of 'TRUE' in their third
		// column.
		for (Object[] testDataItem : testData) {

			Object[] tdi = (Object[]) testDataItem[0];
			boolean isTestCaseNameMatch = tdi[getColumnIndex(testCaseNameColumnHeader)].toString().trim().equalsIgnoreCase(testCaseName.trim());
			boolean useThisData = tdi[getColumnIndex(useThisDataColumnHeader)].toString().trim().equalsIgnoreCase("true");

			if (isTestCaseNameMatch && useThisData) {
				testDataCount++;
				filteredTestData.add(testDataItem);
			}

			((ArrayList<Object[]>) filteredTestData).trimToSize();

		}

		if (filteredTestData.size() == 0) {
			throw new FrameworkException("filterTestDataByTestCaseName", "testData", "ZERO DATA ROWS RETURNED.", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		return filteredTestData;
	}

	/**
	 * @author Francesco Ferrante
	 * @param testCaseName
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Performs basic input validation by testing for a null value<br>
	 *             and an empty string.<br>
	 */
	private void validateTestCaseName(String testCaseName) throws FrameworkException {
		// Perform basic input validation...
		if (testCaseName == null) {
			String errorMessage = "ARGUMENT MAKES REFERENCE TO NULL POINTER.";
			throw new FrameworkException("validateTestCaseName", "testCaseName", errorMessage, Log.ERROR, Log.SCRIPT_ISSUE);
		}

		if (testCaseName.trim().length() == 0) {
			String errorMessage = "ARGUMENT MAKES REFERENCE TO AN EMPTY STRING.";
			throw new FrameworkException("validateTestCaseName", "testCaseName", errorMessage, Log.ERROR, Log.SCRIPT_ISSUE);
		}
	}

	/**
	 * @author Francesco Ferrante
	 * @param column
	 * @return int
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to retrieve a column index based on a user-specified column name.<br>
	 */
	private int getColumnIndex(String column) {
		return testDataColumnHeaders == null ? -1 : testDataColumnHeaders.indexOf(column);
	}
}
