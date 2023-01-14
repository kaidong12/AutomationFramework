package com.cisco.framework.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.cisco.framework.core.exceptions.FrameworkException;
import com.cisco.framework.utilities.logging.Log;

/**
 * @author Francesco Ferrante
 */
public class Locators {

	protected Properties	locators			= null;
	private static Locators	dummyLocators		= null;
	private static String	pathToLocatorsFile	= "";

	private Locators() {
		pathToLocatorsFile = "Dummy";
	}

	public static Locators DummyLocators() {
		if (dummyLocators == null) {
			dummyLocators = new Locators();
		}
		return dummyLocators;
	}

	/**
	 * @param pathToLocatorsFile
	 *            <br>
	 *            <br>
	 *            USAGE:<br>
	 *            <br>
	 *            <p>
	 *            Use this constructor to instantiate a "Locators" object given a valid path to a locators file.<br>
	 */
	public Locators(String pathToLocatorsFile) {
		super();
		if (pathToLocatorsFile == null) {
			throw new FrameworkException("Locators", "pathToLocatorsFile", "MAKES REFERANCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		if (pathToLocatorsFile.isEmpty()) {
			throw new FrameworkException("Locators", "pathToLocatorsFile", "MAKES REFERANCE TO AN EMPTY STRING", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		this.pathToLocatorsFile = pathToLocatorsFile;
	}

	/**
	 * @param key
	 * @return LocatorType
	 * @throws IOException
	 * @throws FrameworkException
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to get a "LocatorType" object given a valid "key".<br>
	 *             If the specified "key" is invalid, or not found, or the corresponding value<br>
	 *             is invalid, an appropriate "FrameworkException" object is thrown.<br>
	 */
	public LocatorType getLocator(String key) throws FrameworkException {

		// Get the value of the "key".
		String value = "";

		try {
			if (locators == null) {
				locators = getLocatorsFromFile(pathToLocatorsFile);
			}

			if (!isKeyValuePairValid(key)) {
				throw new FrameworkException("getLocator", "key", "MAKES REFERENCE TO A NULL POINTER OR AN EMPTY STRING.", Log.ERROR,
						Log.SCRIPT_ISSUE);
			}

			if (!containsKey(key)) {
				String errorMessage = "KEY NOT FOUND: '" + key + "'" + System.getProperty("line.separator") + "LOCATORS FILE: '" + pathToLocatorsFile
						+ "'";
				throw new FrameworkException("getLocator", "key", errorMessage, Log.ERROR, Log.SCRIPT_ISSUE);
			}

			value = locators.getProperty(key);

			// Validate "value".
			if (!isKeyValuePairValid(value)) {
				String errorMessage = "KEY: " + "'" + key + "' ENCOUNTERED REFERENCE TO AN INVALID VALUE" + System.getProperty("line.separator")
						+ "LOCATORS FILE: '" + pathToLocatorsFile + "'";
				throw new FrameworkException("getLocator", "key", errorMessage, Log.ERROR, Log.SCRIPT_ISSUE);
			}
		} catch (IOException e) {
			throw new FrameworkException("getLocator", "key", e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}

		return this.new LocatorType(key, value);
	}

	/**
	 * @param key
	 * @param value
	 * @return LocatorType <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Use this method to get a "LocatorType" object given a valid key\value pair.<br>
	 *         A "FrameworkException" is thrown if an invalid key\value pair is specified.<br>
	 */
	public LocatorType getLocator(String key, String value) throws FrameworkException {

		if (!isKeyValuePairValid(key)) {
			throw new FrameworkException("getLocator", "key", "MAKES REFERENCE TO A NULL POINTER OR AN EMPTY STRING.", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		if (!isKeyValuePairValid(value)) {
			throw new FrameworkException("getLocator", "value", "MAKES REFERENCE TO A NULL POINTER OR AN EMPTY STRING.", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		return this.new LocatorType(key, value);
	}

	/**
	 * @param key
	 * @return "true/false" <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Returns "true" if "locators" object contains key and "false" otherwise.<br>
	 */
	public boolean containsKey(String key) {
		return locators.containsKey(key);
	}

	/**
	 * @author Francesco Ferrante
	 * @param keyOrValue
	 * @return true/false This method returns "true" if "keyOrValue" is valid and "false" otherwise.<br>
	 *         A "key" or "value" is deemed to be valid if both point to a non-empty string.<br>
	 */
	private boolean isKeyValuePairValid(String keyOrValue) {
		boolean res = false;

		if (keyOrValue != null) {
			if (!keyOrValue.isEmpty()) {
				res = true;
			}
		}

		return res;
	}

	/**
	 * @author Francesco Ferrante
	 * @param pathToLocatorsFile
	 * @return Properties
	 * @throws IOException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Returns a "Properties" Object containing a collection of key\value pairs<br>
	 *             from a user-specified locators file containing key\value pairs in the following<br>
	 *             format:<br>
	 *             key1=value1<br>
	 *             key2=value2<br>
	 *             .<br>
	 *             .<br>
	 *             .<br>
	 *             keyN=valueN<br>
	 */
	private Properties getLocatorsFromFile(String pathToLocatorsFile) throws IOException {
		Properties locators = new Properties();

		if (locators != null) {
			BufferedReader inputStream = null;
			try {
				inputStream = new BufferedReader(new FileReader(pathToLocatorsFile));
				locators.load(inputStream);
			} catch (IOException e) {
				// Re-throw "IOException".
				throw e;
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		}

		return locators;
	}

	// --- START NESTED CLASS SECTION ----------------------------------------------
	public class LocatorType {
		private String	key		= "";
		private String	value	= "";

		/**
		 * @param key
		 * @param value
		 *            <br>
		 *            <br>
		 *            USAGE:<br>
		 *            <br>
		 *            <p>
		 *            This constructor is used by the "Loctors" object to instantiate a "LocatorType" object given<bt> a valid "key\value" pair.<br>
		 */
		public LocatorType(String key, String value) {
			this.key = key;
			this.value = value;
		}

		/**
		 * @return the key <br>
		 *         <br>
		 *         USAGE:<br>
		 *         <br>
		 *         <p>
		 *         Use this method to return the "LocatorType" key.<br>
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @return the value <br>
		 *         <br>
		 *         USAGE:<br>
		 *         <br>
		 *         <p>
		 *         Use this method to return the "LocatorType" value.<br>
		 */
		public String getValue() {
			return value;
		}

		public String getString() {
			return key + "\n" + value;
		}

		/**
		 * @return "key\value" <br>
		 *         <br>
		 *         USAGE:<br>
		 *         <br>
		 *         <p>
		 *         Use this method to return a string representation of<br>
		 *         "LocatorType" object in the following format: "key\value".<br>
		 */
		@Override
		public String toString() {
			return key + " = " + value;
		}

	}
	// --- END NESTED CLASS SECTION ----------------------------------------------

	// public static void main(String[] args) {
	// String pathToLocatorsFile = "C:/Automation/locators.txt";
	// Locators locators = new Locators(pathToLocatorsFile);
	// Log log = new Log("C:/TestResults/locators.html");
	// try {
	// log.startTestExecution("testLocators");
	// Locators.LocatorType locatorType = locators.getLocator("key1");
	// log.comment(locatorType.toString());
	// System.out.println(locatorType.toString());
	// } catch (FrameworkException e) {
	// log.exception(e);
	// } catch (Exception e) {
	// log.exception(e);
	// } finally {
	// log.endTestExecution();
	// }
	// }
}
