package com.cisco.framework.utilities.unittests;

import com.cisco.framework.core.MultiLanguageLocators;
import com.cisco.framework.core.exceptions.FrameworkException;
import com.cisco.framework.utilities.logging.Log;


/**
 * @author Lance Yan
 */
public class MultiLanguageLocators_UnitTests {
	
	private static Log log                                     = null;
	private static MultiLanguageLocators multiLanguageLocators = null;
	private static String pathToTestResultsHTMLFile            = "/TestResults/MultiLanguageLocators_UnitTests.html";
	private static String pathToMultiLanguageLocatorsTextFile  = "..//TestAutomationFramework/trunk/src/main/java/com/compuware/gdo/framework/utilities/unittests/MultiLanguageLocators.txt";
	
	public static void main(String[] args) {
		log = new Log(pathToTestResultsHTMLFile);
		unitTestSuite_MultiLanguageLocators();
	}
	
	private static void unitTestSuite_MultiLanguageLocators() {
		log.startTestExecution("UNITTEST1");
		multiLanguageLocators = new MultiLanguageLocators(log,pathToMultiLanguageLocatorsTextFile);
		log.comment("TEST1: GET CURRENT LANGUAGE/REGION SETTING IN USE...");
		log.comment("getCurrentLanguage()", "", multiLanguageLocators.getCurrentLanguage().toString(), Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();
		
		log.startTestExecution("UNITTEST2");
		log.comment("TEST2: CHANGE THE CURRENT LANGUAGE/REGION SETTING TO ANY IN THE FOLLOWING LIST: 'enCA', 'frCA', OR, 'spUS'");
		multiLanguageLocators.setCurrentLanguage("enCA");
		log.endTestExecution();
		
		log.startTestExecution("UNITTEST3");
		log.comment("TEST3: GET THE NEW LANGUAGE/REGION SETTING IN USE...");		
		log.comment("getCurrentLanguage()", "", multiLanguageLocators.getCurrentLanguage().toString(), Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();
		
		log.startTestExecution("UNITTEST4");
		log.comment("TEST4: GET DEFAULT VALUE OF 'key1' THAT CORRESPONDS TO LANGUAGE/REGION SETTING 'enCA'");
		log.comment("getLocator()", "key1", multiLanguageLocators.getLocator("key1").toString(), Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();

		log.startTestExecution("UNITTEST5");
		log.comment("TEST5: GET USER MODIFIED VALUE OF 'key1' THAT CORRESPONDS TO LANGUAGE/REGION SETTING 'enCA'");
		log.comment("getLocator()", "key1", multiLanguageLocators.getLocator("key1","user_modified_value1_enCA").toString(), Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();

		log.startTestExecution("UNITTEST6");
		log.comment("TEST6: GET THE VALUE OF 'key2' THAT CORRESPONDS TO LANGUAGE/REGION SETTING 'enCA'");
		log.comment("getLocator()", "key2", multiLanguageLocators.getLocator("key2").toString(), Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();
		
		try {
			log.startTestExecution("UNITTEST7");
			log.comment("TEST7: GET VALUE OF 'key3' THAT CORRESPONDS TO LANGUAGE/REGION SETTING 'enCA'");
			log.comment("getLocator()", "key3", multiLanguageLocators.getLocator("key3").toString(), Log.DEBUG, Log.SCRIPT_ISSUE);
		} catch (FrameworkException e) {
			log.exception(e);
		} finally {
			log.endTestExecution();
		}

		log.startTestExecution("UNITTEST8");
		log.comment("TEST8: SET CURRENT LANGUAGE/REGION SETTING TO EXISTING LANGUAGE/REGION SETTING: 'enCA'");
		multiLanguageLocators.setCurrentLanguage("enCA");
		log.endTestExecution();

		try {
			log.startTestExecution("UNITTEST9");
			log.comment("TEST9: SET CURRENT LANGUAGE/REGION SETTING TO AN UNSUPPORTED LANGUAGE/REGION SETTING");
			multiLanguageLocators.setCurrentLanguage("spCA");
		} catch (FrameworkException e) {
			log.exception(e);
		} finally {
			log.endTestExecution();
		}
		
		log.startTestExecution("UNITTEST10");
		log.comment("TEST10: GET ALL SUPPORTED LANGUAGE/REGION SETTINGS...");
		log.comment("getSupportedLanguages()", "", multiLanguageLocators.getSupportedLanguages(), Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();
		
		log.startTestExecution("UNITTEST11");
		log.comment("TEST11: ADD NEW LANGUAGE/REGION SETTING 'spCA'...");
		multiLanguageLocators = new MultiLanguageLocators(log,pathToMultiLanguageLocatorsTextFile,"spCA");
		log.endTestExecution();
		
		log.startTestExecution("UNITTEST12");
		log.comment("TEST12: GET ALL NEWLY SUPPORTED LANGUAGE/REGION SETTINGS...");
		log.comment("getSupportedLanguages()", "", multiLanguageLocators.getSupportedLanguages(), Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();
		
		log.startTestExecution("UNITTEST13");
		log.comment("TEST13: GET CURRENT LANGUAGE/REGION SETTING IN USE...");
		log.comment("getCurrentLanguage()", "", multiLanguageLocators.getCurrentLanguage().toString(), Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();
		
		log.startTestExecution("UNITTEST14");
		log.comment("TEST14: CHANGE CURRENT LANGUAGE/REGION SETTING TO 'spCA'");
		multiLanguageLocators.setCurrentLanguage("spCA");
		log.endTestExecution();
		
		log.startTestExecution("UNITTEST15");
		log.comment("TEST15: GET NEW CURRENT LANGUAGE/REGION SETTING IN USE...");
		log.comment("getCurrentLanguage()", "", multiLanguageLocators.getCurrentLanguage().toString(), Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();

		log.startTestExecution("UNITTEST16");
		log.comment("TEST16: GET THE VALUE OF 'key3' THAT CORRESPONDS TO LANGUAGE/REGION SETTING 'spCA'");
		log.comment("getLocator()", "key3", multiLanguageLocators.getLocator("key3").toString(), Log.DEBUG, Log.SCRIPT_ISSUE);
		log.endTestExecution();
	}
}