package com.cisco.framework.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cisco.framework.core.exceptions.FrameworkException;
import com.cisco.framework.utilities.logging.Log;

/**
 * @author Lance Yan
 */
public class MultiLanguageLocators extends Locators {

	private List<Language>		supportedLanguages		= null;
	private Log					log						= null;
	private Language			currentLanguage			= null;
	private static final String	LANGUAGE_PATTERN		= "[a-z][a-z][A-Z][A-Z]";
	private static final int	LANGUAGE_PATTERN_LENGTH	= 4;

	/**
	 * @param log
	 * @param pathToLocatorsFile
	 *            <br>
	 *            <br>
	 *            USAGE: <br>
	 *            <br>
	 *            <p>
	 *            Use this constructor to instantiate a "MultiLanguageLocators" object given a valid "Log" object and path to a locators file.<br>
	 */
	public MultiLanguageLocators(Log log, String pathToLocatorsFile) {
		super(pathToLocatorsFile);
		if (log == null) {
			throw new FrameworkException("MultiLanguageLocators", "log", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		this.log = log;
		init();
		log.comment("MultiLanguageLocators", "pathToLocatorsFile", "USING: 'NON-LANGUAGE MODE'", Log.DEBUG, Log.SCRIPT_ISSUE);
	}

	/**
	 * @param log
	 * @param pathToLocatorsFile
	 *            <br>
	 *            <br>
	 *            USAGE: <br>
	 *            <br>
	 *            <p>
	 *            Use this constructor to instantiate a "MultiLanguageLocators" object given a valid "Log" object and path to a locators file<br>
	 *            and to add additional supported languages.<br>
	 */
	public MultiLanguageLocators(Log log, String pathToLocatorsFile, String... additionalLanguages) {
		this(log, pathToLocatorsFile);

		if (additionalLanguages == null) {
			throw new FrameworkException("MultiLanguageLocators", "additionalLanguages", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR,
					Log.SCRIPT_ISSUE);
		} else {
			if (additionalLanguages.length == 0) {
				throw new FrameworkException("MultiLanguageLocators", "additionalLanguages", "MAKES REFERENCE TO AN EMPTY STRING ARRAY", Log.ERROR,
						Log.SCRIPT_ISSUE);
			}
		}

		for (String language : additionalLanguages) {
			Language lang = this.parseLanguage(language, "MultiLanguageLocators");
			if (lang != null) {
				if (this.supportedLanguages.contains(lang)) {
					log.comment("MultiLanguageLocators", "language", "'" + lang.toString() + "' ALREADY SUPPORTED", Log.WARN, Log.SCRIPT_ISSUE);
				} else {
					this.supportedLanguages.add(lang);
					log.comment("MultiLanguageLocators", "language", "ADDING: " + "'" + lang.toString() + "' AS A SUPPORTED LANGUAGE", Log.DEBUG,
							Log.SCRIPT_ISSUE);
				}
			} else {
				throw new FrameworkException("MultiLanguageLocators", "additionalLanguages", "LANGUAGE ITEM MAKES REFERENCE TO A NULL POINTER",
						Log.ERROR, Log.SCRIPT_ISSUE);
			}
		}
	}

	/**
	 * @return the currentLanguage <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to get the current language in use.<br>
	 */
	public Language getCurrentLanguage() {
		return currentLanguage;
	}

	/**
	 * @return the supportedLanguages <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to get a list of all currently supported languages.<br>
	 */
	public String getSupportedLanguages() {
		return Arrays.toString(supportedLanguages.toArray(new Language[0]));
	}

	/**
	 * @param language
	 *            the currentLanguage to set
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to change the current language in use.<br>
	 *             An appropriate "FrameworkException" is thrown if an invalid "language_region" entry is made.<br>
	 *             Uses helper method "validateLanguageAndRegion" to perform the actual validation.<br>
	 */
	public void setCurrentLanguage(String language) throws FrameworkException {
		validateLanguage(language, "setCurrentLanguage");
	}

	/**
	 * @param key
	 * @param value
	 * @return LocatorType
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to get a "LocatorType" object given the current language setting and a valid key\value pair.<br>
	 *             A "FrameworkException" is thrown if an invalid key\value pair is specified.<br>
	 */
	@Override
	public LocatorType getLocator(String key, String value) throws FrameworkException {
		return super.getLocator(this.useCurrentLanguage(key), value);
	}

	/**
	 * @param key
	 * @return LocatorType
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to get a "LocatorType" object given the current language setting and a valid key.<br>
	 *             A "FrameworkException" is thrown if an invalid key is specified.<br>
	 */
	@Override
	public LocatorType getLocator(String key) throws FrameworkException {
		LocatorType res = null;
		String keyWithLanguage = this.useCurrentLanguage(key);
		try {
			res = super.getLocator(keyWithLanguage);
		} catch (Exception e) {
			if (!this.currentLanguage.toString().isEmpty()) {
				this.log.comment("getLocator", "key", "NOT FOUND: " + keyWithLanguage + System.getProperty("line.separator") + "USING:     " + key,
						Log.WARN, Log.SCRIPT_ISSUE);
			}
		}
		if (res == null) {
			res = super.getLocator(key);
		}
		return res;
	}

	private String useCurrentLanguage(String key) {
		String res = key;
		if (res != null) {
			if (!res.isEmpty()) {
				if (!this.currentLanguage.toString().isEmpty()) {
					res = key + "_" + this.currentLanguage.toString();
				}
			}
		}
		return res;
	}

	private void validateLanguage(String language, String methodName) throws FrameworkException {
		Language lang = parseLanguage(language, methodName);
		if (lang != null) {
			// Make sure language is supported.
			if (supportedLanguages.contains(lang)) {
				if (currentLanguage.equals(lang)) {
					log.comment(methodName, "language", "'" + this.currentLanguage.toString() + "' ALREADY IN USE.", Log.WARN, Log.SCRIPT_ISSUE);
				} else {
					currentLanguage = lang;
					log.comment(methodName, "language", "SETTING LANGUAGE TO: " + "'" + this.currentLanguage.toString() + "'", Log.DEBUG,
							Log.SCRIPT_ISSUE);
				}
			} else {
				if (lang.toString().isEmpty()) {
					currentLanguage = this.new Language();
					log.comment(methodName, "language", "RE-SETTING TO 'NON-LANGUAGE' MODE.", Log.DEBUG, Log.SCRIPT_ISSUE);
				} else {
					// language is not a supported throw an appropriate "FrameworkException".
					throw new FrameworkException(methodName, "language", "'" + lang.toString() + "' NOT SUPPORTED.", Log.ERROR, Log.SCRIPT_ISSUE);
				}
			}
		} else {
			throw new FrameworkException(methodName, "language", "MAKES REFERENCE TO A NULL POINTER.", Log.ERROR, Log.SCRIPT_ISSUE);
		}
	}

	private Language parseLanguage(String language, String methodName) throws FrameworkException {
		Language res = null;
		if (language != null) {
			if (!language.isEmpty()) {
				// If "languageAndRegion" matches "LANGUAGE_PATTERN_LENGTH" return appropriate "Language" object else return null.
				if (isLanguageMatch(language)) {
					// It is assumed that the first two characters of "language" holds the language portion and the
					// the last two characters holds the region portion.
					res = this.new Language(language.substring(0, 2), language.substring(2, MultiLanguageLocators.LANGUAGE_PATTERN_LENGTH));
				} else {
					String errorMessage = "LANGUAGE:                              " + "'" + language + "' IS NOT IN THE VALID FORMAT."
							+ System.getProperty("line.separator") + "VALID LANGUAGE FORMAT:                 " + "'"
							+ MultiLanguageLocators.LANGUAGE_PATTERN + "'" + System.getProperty("line.separator")
							+ "VALID LANGUAGE FORMAT EXAMPLE:         " + "'" + "enUS" + "'" + System.getProperty("line.separator")
							+ "VALID LANGUAGE LOCATOR FORMAT EXAMPLE: " + "'" + "<LOCATOR KEY>_enUS" + "'";
					throw new FrameworkException(methodName, "language", errorMessage, Log.ERROR, Log.SCRIPT_ISSUE);
				}
			} else {
				res = this.new Language();
			}
		}
		return res;
	}

	private boolean isLanguageMatch(String language) {
		boolean res = false;
		if (language.length() == MultiLanguageLocators.LANGUAGE_PATTERN_LENGTH) {
			Pattern pattern = Pattern.compile(MultiLanguageLocators.LANGUAGE_PATTERN);
			Matcher matcher = pattern.matcher(language);
			while (matcher.find()) {
				res = true;
			}
		}
		return res;
	}

	private void init() {
		// The default language is "EN_US".
		this.currentLanguage = this.new Language();
		// Setup default supported languages
		this.supportedLanguages = new ArrayList<Language>();
		if (this.supportedLanguages != null) {
			this.supportedLanguages.add(this.new Language("en", "US"));
			this.supportedLanguages.add(this.new Language("en", "CA"));
			this.supportedLanguages.add(this.new Language("sp", "US"));
			this.supportedLanguages.add(this.new Language("fr", "CA"));
		}
	}

	// --- START NESTED CLASS SECTION ----------------------------------------------
	public class Language {
		private String	language	= "";
		private String	region		= "";

		public Language() {
			this("", "");
		}

		public Language(String language, String region) {

			if (language != null) {
				if (!language.isEmpty()) {
					this.language = language.toLowerCase();
				}
			}

			if (region != null) {
				if (!region.isEmpty()) {
					this.region = region.toUpperCase();
				}
			}

		}

		public String getValue() {
			return this.language;
		}

		public String getRegion() {
			return this.region;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.language.isEmpty() || this.region.isEmpty() ? "enUS" : this.language + this.region;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object arg0) {
			boolean res = false;

			if (arg0 instanceof Language) {
				Language ltArg0 = (Language) arg0;
				res = this.language.equals(ltArg0.getValue()) && this.region.equals(ltArg0.getRegion());
			} else {
				res = super.equals(arg0);
			}

			return res;
		}
	}
	// --- END NESTED CLASS SECTION ----------------------------------------------
}