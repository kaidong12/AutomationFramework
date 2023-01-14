package com.cisco.framework.utilities.unittests;

import com.cisco.framework.utilities.RegistryOperations;
import com.cisco.framework.utilities.RegistryUtils;
import com.cisco.framework.utilities.logging.Log;


/**
 * @author Francesco Ferrante
 */
public class RegistryUtils_UnitTests {
	private Log log                          = null;
	private RegistryUtils regUtils           = null;
	private final static String TEST_RESULTS = "/TestResults/RegistryUtils_UnitTests_Results.html";
	private final static String REGISTRY_KEY = "HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\International\\MyTestKey";
	
	public RegistryUtils_UnitTests(String testResults) {
		super();
		log      = new Log(testResults);
		regUtils = new RegistryUtils(log);
	}
	
	public static void main(String[] args) {
		new RegistryUtils_UnitTests(TEST_RESULTS).execute();
	}
	
	public void testUpdateRegistry_CreateRegistryKey() {
		try {
			log.startTestExecution("testUpdateRegistry_CreateRegistryKey");
			log.verifyStep(regUtils.updateRegistry(REGISTRY_KEY, "", "",RegistryOperations.CREATE_REGISTRY_KEY) == true, 
					       "VERIFY CREATE REGISTRY KEY", 
					       "PASS");
		} catch (Exception e) {
			log.exception(e);
		} finally {
			log.endTestExecution();
		}
	}
	
	public void testUpdateRegistry_CreateRegistryValue() {
		String RegistryValueName = "TestKey";
		String RegistryValue     = "TestValue";
		try {
			log.startTestExecution("testUpdateRegistry_CreateRegistryValue");
			log.verifyStep(regUtils.updateRegistry(REGISTRY_KEY, RegistryValueName, RegistryValue,RegistryOperations.CREATE_REGISTRY_KEY) == true, 
					       "VERIFY CREATE REGISTRY VALUE", 
					       "PASS");
		} catch (Exception e) {
			log.exception(e);
		} finally {
			log.endTestExecution();
		}
	}
	
	public void testUpdateRegistry_UpdateRegistryValue() {
		String RegistryValueName = "TestKey";
		String RegistryValue     = "TestValueNew";
		try {
			log.startTestExecution("testUpdateRegistry_UpdateRegistryValue");
			log.verifyStep(regUtils.updateRegistry(REGISTRY_KEY, RegistryValueName, RegistryValue,RegistryOperations.CREATE_REGISTRY_KEY) == true, 
					       "VERIFY UPDATE REGISTRY VALUE", 
					       "PASS");
		} catch (Exception e) {
			log.exception(e);
		} finally {
			log.endTestExecution();
		}
	}
	
	public void testUpdateRegistry_DeleteRegistryValue() {
		String RegistryValueName = "TestKey";
		String RegistryValue     = "TestValueNew";
		try {
			log.startTestExecution("testUpdateRegistry_DeleteRegistryValue");
			log.verifyStep(regUtils.updateRegistry(REGISTRY_KEY, RegistryValueName, RegistryValue,RegistryOperations.DELETE_REGISTRY_VALUE) == true, 
					       "VERIFY DELETE REGISTRY VALUE", 
					       "PASS");
		} catch (Exception e) {
			log.exception(e);
		} finally {
			log.endTestExecution();
		}
	}
	
	public void testUpdateRegistry_DeleteRegistryKey() {
		String RegistryValueName = "TestKey";
		String RegistryValue     = "TestValueNew";
		try {
			log.startTestExecution("testUpdateRegistry_DeleteRegistryKey");
			log.verifyStep(regUtils.updateRegistry(REGISTRY_KEY, RegistryValueName, RegistryValue,RegistryOperations.DELETE_REGISTRY_KEY) == true, 
					       "VERIFY DELETE REGISTRY KEY", 
					       "PASS");
		} catch (Exception e) {
			log.exception(e);
		} finally {
			log.endTestExecution();
		}
	}
	
	public void execute() {
		System.out.println("STARTING TEST EXECUTION...");
		testUpdateRegistry_CreateRegistryKey();
		testUpdateRegistry_CreateRegistryValue();
		testUpdateRegistry_UpdateRegistryValue();
		testUpdateRegistry_DeleteRegistryValue();
		testUpdateRegistry_DeleteRegistryKey();
		System.out.println("TEST EXECUTION COMPLETED");
	}
	
}