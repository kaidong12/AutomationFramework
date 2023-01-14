package com.cisco.framework.utilities;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.cisco.framework.core.exceptions.FrameworkException;
import com.cisco.framework.utilities.logging.Log;

public class RegistryUtils {
	private Log					log							= null;

	private FileReader			fileReader					= null;

	private FileWriter			fileWriter					= null;

	private File				fsRegFile					= null;

	private StringBuilder		locatorNameOrParameter		= null;
	private StringBuilder		actionValueOrMessage		= null;
	private StringBuilder		contents					= null;
	private StringBuilder		registryOutput				= null;

	private final static String	NEW_LINE					= System.getProperty("line.separator");
	private final static String	UPDATE_REGISTRY_FILE_HEADER	= "Windows Registry Editor Version 5.00";
	private final static String	QUOTATION_MARK				= "\"";

	private final static int	MAXIMUM_CHARACTERS_TO_READ	= 1024;

	public RegistryUtils(Log log) throws FrameworkException {
		super();
		// If log makes reference to a null pointer, through an appropriate "FrameworkException".
		if (log == null) {
			throw new FrameworkException("RegistryUtils", "log", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		this.log = log;
	}

	/**
	 * @param registryKey
	 * @param registryValueName
	 * @param registryValue
	 * @param eRegOps
	 * @throws FrameworkException
	 * @return "true/false" <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to update the windows registry according to the following rules:<br>
	 *         Returns "true" windows registry was updated successfully or "false" otherwise.<br>
	 *         <br>
	 * 
	 *         ---------------------------<br>
	 *         - -<br>
	 *         --- CREATE_REGISTRY_KEY ---<br>
	 *         - -<br>
	 *         ---------------------------<br>
	 *         <br>
	 * 
	 *         1.0 "registryKey" = null<br>
	 *         "registryValueName" = null<br>
	 *         "registryValue" = null<br>
	 *         <br>
	 * 
	 *         ACTIONS TAKEN:<br>
	 *         <br>
	 * 
	 *         1.1 No registry key gets created.<br>
	 *         1.2 An appropriate warning is logged.<br>
	 *         <br>
	 * 
	 *         2.0 "registryKey" = ""<r>, "registryValueName" = ""<br>
	 *         "registryValue" = ""<br>
	 *         <br>
	 *
	 *         ACTIONS TAKEN:<br>
	 *         <br>
	 * 
	 *         2.1 No registry key will be created.<br>
	 *         2.2 An appropriate warning will be logged.<br>
	 *         <br>
	 * 
	 *         3.0 "registryKey" = "HKEY_USERS\\.DEFAULT\\MyName"<br>
	 *         "registryValueName" = ""<br>
	 *         "registryValue" = ""<br>
	 *         <br>
	 * 
	 *         ACTIONS TAKEN:<br>
	 *         <br>
	 * 
	 *         3.1 Registry key [HKEY_USERS\.DEFAULT\MyName] will be created.<br>
	 *         3.2 An appropriate debug entry will be logged.<br>
	 *         3.3 An appropriate warning will be logged.<br>
	 *         <br>
	 * 
	 *         4.0 "registryKey" = "HKEY_USERS\\.DEFAULT\\MyName"<br>
	 *         "registryValueName" = "FirstName"<br>
	 *         "registryValue" = ""<br>
	 *         <br>
	 * 
	 *         ACTIONS TAKEN:<br>
	 *         <br>
	 * 
	 *         4.1 Registry key [HKEY_USERS\.DEFAULT\MyName] will be created.<br>
	 *         4.2 "FirstName"="" will be created.<br>
	 *         4.3 An appropriate debug entry will be logged.<br>
	 *         4.4 An appropriate warning will be logged.<br>
	 *         <br>
	 * 
	 *         5.0 "registryKey" = "HKEY_USERS\\.DEFAULT\\MyName"<br>
	 *         "registryValueName" = "FirstName"<br>
	 *         "registryValue" = "Francesco"<br>
	 *         <br>
	 * 
	 *         ACTIONS TAKEN:<br>
	 *         <br>
	 * 
	 *         5.1 Registry key [HKEY_USERS\.DEFAULT\MyName] will be created.<br>
	 *         5.2 "FirstName"="Francesco" will be created.<br>
	 *         5.3 An appropriate debug entry will be logged.<br>
	 *         <br>
	 * 
	 *         ---------------------------<br>
	 *         - -<br>
	 *         --- DELETE_REGISTRY_KEY ---<br>
	 *         - -<br>
	 *         ---------------------------<br>
	 *         <br>
	 * 
	 *         If "registryKey" is empty or points to a non-existant key, no registry key will be deleted.<br>
	 *         If "registryKey" points to an existant registry key, the registry key together with all of its<br>
	 *         constituent components will be deleted.<br>
	 *         <br>
	 * 
	 *         -----------------------------<br>
	 *         - -<br>
	 *         --- DELETE_REGISTRY_VALUE ---<br>
	 *         - -<br>
	 *         -----------------------------<br>
	 *         <br>
	 * 
	 *         If "registryKey" is empty or points to a non-existant key and "registryValueName" is empty, no registry key will be deleted<br>
	 *         and an appropriate warning will be logged.<br>
	 *         <br>
	 * 
	 *         If "registryKey" points to an existant registry key and "registryValueName" is empty, no registry key will be deleted<br>
	 *         and an appropriate warning will be logged.<br>
	 *         <br>
	 * 
	 *         If "registryKey" points to an existant registry key and "registryValueName" is not empty, only the specified item "registryValueName"
	 *         <br>
	 *         under "registryKey" will be deleted, and an appropriate debug entry will be logged.<br>
	 */
	public boolean updateRegistry(String registryKey, String registryValueName, String registryValue, RegistryOperations eRegOps)
			throws FrameworkException {

		boolean res = false;

		try {

			if (registryKey == null) {
				throw new FrameworkException("updateRegistry", "registryKey", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
			}

			if (registryValueName == null) {
				throw new FrameworkException("updateRegistry", "registryValueName", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
			}

			if (registryValue == null) {
				throw new FrameworkException("updateRegistry", "registryValue", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
			}

			if (eRegOps == null) {
				throw new FrameworkException("updateRegistry", "eRegOps", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
			}

			// Create New File Instance for .reg file
			fsRegFile = new File("UpdateRegistry.reg");

			// If file "UpdateRegistry.reg" exists, delete it
			if (fsRegFile.exists()) {
				fsRegFile.delete();
			}

			// Create file "UpdateRegistry.reg"
			if (fsRegFile.createNewFile()) {

				// Path to file "UpdateRegistry.reg" was successfully created
				locatorNameOrParameter = new StringBuilder();
				actionValueOrMessage = new StringBuilder();
				registryOutput = new StringBuilder();

				// Read contents of file "fsRegFile".
				contents = new StringBuilder();
				fileReader = new FileReader(fsRegFile);
				char[] buffer = new char[MAXIMUM_CHARACTERS_TO_READ];
				while (fileReader.read(buffer) != -1) {
					contents.append(buffer);
					contents.append(NEW_LINE);
				}
				fileReader.close();

				// Generate .reg File Content based on operation
				fileWriter = new FileWriter(fsRegFile);
				if (!contents.toString().contains(UPDATE_REGISTRY_FILE_HEADER)) {
					fileWriter.write(UPDATE_REGISTRY_FILE_HEADER);
					fileWriter.write(NEW_LINE);
					fileWriter.write(NEW_LINE);
				}

				switch (eRegOps) {
				case CREATE_REGISTRY_KEY:
					createRegistryKey(registryKey, registryValueName, registryValue);
					break;

				case DELETE_REGISTRY_KEY:
					deleteRegistryKey(registryKey);
					break;

				case DELETE_REGISTRY_VALUE:
					deleteRegistryValue(registryKey, registryValueName);
					break;
				}

				locatorNameOrParameter.append("FILE:");

				actionValueOrMessage.append(fsRegFile.getAbsolutePath());

				Runtime.getRuntime().exec("regedit.exe /s " + fsRegFile.getAbsolutePath());

				if ((locatorNameOrParameter.length() != 0) && (actionValueOrMessage.length() != 0)) {
					log.comment("updateRegistry", locatorNameOrParameter.toString(), actionValueOrMessage.toString(), Log.DEBUG, Log.SCRIPT_ISSUE);
				}

				res = true;

			} else {
				// File "UpdateRegistry.reg" was not created, throw appropriate "FrameworkException"
				throw new FrameworkException("updateRegistry", "FILE:", "'" + fsRegFile.getAbsolutePath() + "' WAS NOT CREATED", Log.ERROR,
						Log.SCRIPT_ISSUE);
			}

		} catch (IOException e) {
			// Handle "IOException"
			throw new FrameworkException("updateRegistry", e.getClass().getName(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} catch (SecurityException e) {
			// Handle "SecurityException"
			throw new FrameworkException("updateRegistry", e.getClass().getName(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException e) {
				// Handle "IOException"
				throw new FrameworkException("updateRegistry", e.getClass().getName(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
			}
		}

		return res;
	}

	/*
	 * Use this method to create a new registry key or update am existing one. For details please refer to the javadoc for method "updateRegistry".
	 */
	private void createRegistryKey(String registryKey, String registryValueName, String registryValue) throws IOException {

		// Create "registryKey"
		if (!registryKey.isEmpty()) {
			// "registryKey" has been specified, let's go ahead and create it
			registryOutput.append("[");
			registryOutput.append(registryKey);
			registryOutput.append("]");
			registryOutput.append(NEW_LINE);

			locatorNameOrParameter.append("REGISTRY OPERATION:");
			locatorNameOrParameter.append(NEW_LINE);
			locatorNameOrParameter.append("REGISTRY KEY:");
			locatorNameOrParameter.append(NEW_LINE);

			actionValueOrMessage.append("CREATE REGISTRY KEY");
			actionValueOrMessage.append(NEW_LINE);
			actionValueOrMessage.append(registryKey);
			actionValueOrMessage.append(NEW_LINE);

			// Create "registryValueName"
			if (!registryValueName.isEmpty()) {
				// "registryValueName" has been specified, let's go ahead and create it
				registryOutput.append(QUOTATION_MARK);
				registryOutput.append(registryValueName);
				registryOutput.append(QUOTATION_MARK);
				registryOutput.append("=");

				locatorNameOrParameter.append("REGISTRY VALUE NAME:");
				locatorNameOrParameter.append(NEW_LINE);

				actionValueOrMessage.append(registryValueName);
				actionValueOrMessage.append(NEW_LINE);

				// Create "registryValue"
				if (!registryValue.isEmpty()) {
					// "registryValue" has been specified, let's go ahead and create it
					registryOutput.append(QUOTATION_MARK);
					registryOutput.append(registryValue);
					registryOutput.append(QUOTATION_MARK);

					locatorNameOrParameter.append("REGISTRY VALUE:");
					locatorNameOrParameter.append(NEW_LINE);

					actionValueOrMessage.append(registryValue);
				} else {
					// "registryValue" has not been specified, create empty "registryValue", and log an appropriate warning
					registryOutput.append(QUOTATION_MARK);
					registryOutput.append(QUOTATION_MARK);

					actionValueOrMessage.append(QUOTATION_MARK);
					actionValueOrMessage.append(QUOTATION_MARK);

					log.comment("updateRegistry", "registryValue", "NOT SPECIFIED. EMPTY VALUE CREATED.", Log.WARN, Log.SCRIPT_ISSUE);
				}

				registryOutput.append(NEW_LINE);
				actionValueOrMessage.append(NEW_LINE);

			} else {
				// "registryValueName" has not been specified, log an appropriate warning
				log.comment("updateRegistry", "registryValueName", "NOT SPECIFIED", Log.WARN, Log.SCRIPT_ISSUE);
			}

		} else {
			// "registryKey" has not been specified, log an appropriate warning
			log.comment("updateRegistry", "registryKey", "NOT SPECIFIED", Log.WARN, Log.SCRIPT_ISSUE);
		}

		// Write "registryOutput" to file
		if (registryOutput.length() != 0) {
			fileWriter.write(registryOutput.toString());
		}
	}

	/*
	 * Use this method to delete an existing registry key together with any registry values that might be under it. For details please refer to the
	 * javadoc for method "updateRegistry".
	 */
	private void deleteRegistryKey(String registryKey) throws IOException {
		if (!registryKey.isEmpty()) {
			// "registryKey" for deletion has been specified, let's delete it
			registryOutput.append("[");
			registryOutput.append("-" + registryKey);
			registryOutput.append("]");
			registryOutput.append(NEW_LINE);

			locatorNameOrParameter.append("REGISTRY OPERATION:");
			locatorNameOrParameter.append(NEW_LINE);
			locatorNameOrParameter.append("REGISTRY KEY:");
			locatorNameOrParameter.append(NEW_LINE);

			actionValueOrMessage.append("DELETE REGISTRY KEY");
			actionValueOrMessage.append(NEW_LINE);
			actionValueOrMessage.append(registryKey);
			actionValueOrMessage.append(NEW_LINE);
		} else {
			// "registryKey" for deletion has not been specified, log an appropriate warning
			log.comment("deleteRegistryKey", "registryKey", "NOT SPECIFIED", Log.WARN, Log.SCRIPT_ISSUE);
		}

		// Write "registryOutput" to file
		if (registryOutput.length() != 0) {
			fileWriter.write(registryOutput.toString());
		}
	}

	/*
	 * Use this method to delete only an existing registry value under an existing registry key. For details please refer to the javadoc for method
	 * "updateRegistry".
	 */
	private void deleteRegistryValue(String registryKey, String registryValueName) throws IOException {
		if (!registryKey.isEmpty()) {
			// "registryKey" has been specified.
			registryOutput.append("[");
			registryOutput.append(registryKey);
			registryOutput.append("]");
			registryOutput.append(NEW_LINE);

			locatorNameOrParameter.append("REGISTRY OPERATION:");
			locatorNameOrParameter.append(NEW_LINE);
			locatorNameOrParameter.append("REGISTRY KEY:");
			locatorNameOrParameter.append(NEW_LINE);

			actionValueOrMessage.append("DELETE REGISTRY VALUE");
			actionValueOrMessage.append(NEW_LINE);
			actionValueOrMessage.append(registryKey);
			actionValueOrMessage.append(NEW_LINE);

			if (!registryValueName.isEmpty()) {
				// "registryValueName" for deletion has been specified, let's delete it
				registryOutput.append(QUOTATION_MARK);
				registryOutput.append(registryValueName);
				registryOutput.append(QUOTATION_MARK);
				registryOutput.append("=");
				registryOutput.append("-");

				locatorNameOrParameter.append("REGISTRY VALUE NAME:");
				locatorNameOrParameter.append(NEW_LINE);

				actionValueOrMessage.append(registryValueName);
				actionValueOrMessage.append(NEW_LINE);

			} else {
				// "registryValueName" for deletion has not been specified, log an appropriate warning
				log.comment("deleteRegistryKey", "registryValueName", "NOT SPECIFIED", Log.WARN, Log.SCRIPT_ISSUE);
			}
		} else {
			// "registryKey" has not been specified, log an appropriate warning.
			log.comment("deleteRegistryKey", "registryKey", "NOT SPECIFIED", Log.WARN, Log.SCRIPT_ISSUE);
		}

		// Write "registryOutput" to file
		if (registryOutput.length() != 0) {
			fileWriter.write(registryOutput.toString());
		}
	}
}