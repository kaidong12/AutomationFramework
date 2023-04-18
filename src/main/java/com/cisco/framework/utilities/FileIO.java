package com.cisco.framework.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import com.cisco.framework.core.exceptions.FrameworkException;
import com.cisco.framework.utilities.logging.Log;

public class FileIO {

	private Log					log							= null;
	private static final String	NEW_LINE					= System.getProperty("line.separator");
	private static final int	MAXIMUM_CHARATCERS_TO_LOG	= 100;

	public FileIO(Log log) throws FrameworkException {
		super();
		// If log makes reference to a null pointer, through an appropriate "FrameworkException".
		if (log == null) {
			throw new FrameworkException("FileIO", "log", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		this.log = log;
	}

	public FileIO() {
		super();
	}

	/***
	 * @author Lance Yan
	 * @param pathToFolder
	 * @return "a list of file names"
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to return a list of all files names within a user-sepcified folder.<br>
	 *             If a folder as sub-folders only a list of all files names and names of all sub-folders is returned<br>
	 *             within the parent folder.<br>
	 */
	public List<String> getListOfFileNamesInFolder(String pathToFolder) throws FrameworkException {

		if (!isFileExists(pathToFolder)) {
			throw new FrameworkException("getListOfFileNamesInFolder", "pathToFolder", "MAKES REFERENCE TO A NON-EXISTANT FOLDER", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}

		return Arrays.asList(new File(pathToFolder).list());
	}

	/***
	 * @author Lance Yan
	 * @param pathToFolder
	 * @return "a list of files"
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to return a list of all files within a user-sepcified folder.<br>
	 *             If a folder as sub-folders only a list of all files and all sub-folders is returned<br>
	 *             within the parent folder.<br>
	 */
	public List<File> getListOfFilesInFolder(String pathToFolder) throws FrameworkException {

		if (!isFileExists(pathToFolder)) {
			throw new FrameworkException("getListOfFilesInFolder", "pathToFolder", "MAKES REFERENCE TO A NON-EXISTANT FOLDER", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}

		return Arrays.asList(new File(pathToFolder).listFiles());
	}

	/***
	 * @author Murali K Parepalli
	 * @param sFilePath
	 * @return "true/false"
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to determine if a given file exists.<br>
	 *             This method returns "true" If file exists and "false" otherwise.<br>
	 */
	public boolean isFileExists(String sFilePath) throws FrameworkException {
		boolean res = false;

		try {
			validateInputFile(sFilePath, "isFileExists", "sFilePath");
			res = new File(sFilePath).exists();
		} catch (SecurityException e) {
			// Handle any security exceptions which might arise.
			throw new FrameworkException("isFileExists", "sFilePath", e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}

		return res;
	}

	/***
	 * @author Murali K Parepalli
	 * @param sFilePath
	 * @return "true/false"
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to create a given file or directory.<br>
	 *             If the file/directory is created, "true" is returned otherwise "false".<br>
	 */
	public boolean createFile(String sFilePath) throws FrameworkException {
		boolean res = false;
		File file = null;
		try {
			validateInputFile(sFilePath, "createFile", "sFilePath");
			file = new File(sFilePath);
			if (file.exists() == false) {
				// File Not exists, lets Create it.
				try {
					File directory = file.getParentFile();
					if (!directory.exists()) {
						res = directory.mkdirs();
					}
					res = file.createNewFile();
				} catch (IOException e) {
					res = false;
				}
				if (res) {
					if (log != null) {
						// File or directory has been successfully created, let's log a debug entry.
						log.comment("createFile", "sFilePath", "FILE/DIRECTORY: " + "'" + file.getAbsolutePath() + "' HAS BEEN SUCCESSFULLY CREATED",
								Log.DEBUG, Log.SCRIPT_ISSUE);
					}
				} else {
					if (log != null) {
						// File or directory has not been created, let's log a warning.
						log.comment("createFile", "sFilePath", "FILE/DIRECTORY: " + "'" + file.getAbsolutePath() + "' NOT CREATED", Log.WARN,
								Log.SCRIPT_ISSUE);
					}
				}

			} else {
				if (log != null) {
					// File or directory already exists, lets log a warning.
					log.comment("createFile", "sFilePath", "FILE/DIRECTORY: " + "'" + file.getAbsolutePath() + "' ALREADY EXISTS", Log.WARN,
							Log.SCRIPT_ISSUE);
				}
			}
		} catch (SecurityException e) {
			// Handle any security exceptions which might arise.
			throw new FrameworkException("createFile", "sFilePath", e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
		return res;
	}

	/***
	 * @author Murali K Parepalli
	 * @param sFilePath
	 * @return "true/false"
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to delete a given file or directory.<br>
	 *             If the file/directory is deleted, "true" is returned otherwise "false".<br>
	 */
	public boolean deleteFile(String sFilePath) throws FrameworkException {
		boolean res = false;
		File file = null;
		try {
			validateInputFile(sFilePath, "deleteFile", "sFilePath");
			file = new File(sFilePath);
			if (file.exists()) {
				// File or directory exists, lets delete it.
				res = file.delete();
				if (res) {
					if (log != null) {
						// File or directory has been successfully deleted, let's log a debug entry.
						log.comment("deleteFile", "sFilePath", "FILE/DIRECTORY: " + "'" + file.getAbsolutePath() + "' HAS BEEN SUCCESSFULLY DELETED",
								Log.DEBUG, Log.SCRIPT_ISSUE);
					}
				} else {
					if (log != null) {
						// File or directory has not been deleted, let's log a warning.
						log.comment("deleteFile", "sFilePath", "FILE/DIRECTORY: " + "'" + file.getAbsolutePath() + "' HAS NOT DELETED", Log.WARN,
								Log.SCRIPT_ISSUE);
					}
				}

			} else {
				if (log != null) {
					// File or directory does not exist, lets log a warning.
					log.comment("deleteFile", "sFilePath", "FILE/DIRECTORY: " + "'" + file.getAbsolutePath() + "' DOES NOT EXIST", Log.WARN,
							Log.SCRIPT_ISSUE);
				}
			}
		} catch (SecurityException e) {
			// Handle any security exceptions which might arise.
			throw new FrameworkException("deleteFile", "sFilePath", e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
		return res;
	}

	/***
	 * @author Murali K Parepalli
	 * @param sOldFilePath
	 * @param sNewFilePath
	 * @return "true/false"
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to move or rename a file.<> If move or rename operation completed successfully, then "true" is returned otherwise
	 *             "false".<br>
	 */
	public boolean moveFile(String sOldFilePath, String sNewFilePath) throws FrameworkException {
		boolean res = false;
		File oldFile = null;
		File newFile = null;
		try {
			validateInputFile(sOldFilePath, "moveFile", "sOldFilePath");
			validateInputFile(sNewFilePath, "moveFile", "sNewFilePath");
			oldFile = new File(sOldFilePath);
			newFile = new File(sNewFilePath);

			if (oldFile.exists()) {
				// File or directory exists, lets rename it.
				res = oldFile.renameTo(newFile);
				if (res) {
					if (log != null) {
						// File or directory has been successfully renamed, let's log a debug entry.
						log.comment("moveFile", "sOldFilePath:" + NEW_LINE + "sNewFilePath:",
								oldFile.getAbsolutePath() + NEW_LINE + newFile.getAbsolutePath(), Log.DEBUG, Log.SCRIPT_ISSUE);
					}
				} else {
					if (log != null) {
						// File or directory has not been renamed, let's log a warning.
						log.comment("moveFile", "sOldFilePath", "FILE/DIRECTORY: " + "'" + oldFile.getAbsolutePath() + "' WAS NOT RENAMED TO: "
								+ NEW_LINE + "'" + newFile.getAbsolutePath() + "'", Log.WARN, Log.SCRIPT_ISSUE);
					}
				}
			} else {
				if (log != null) {
					// File or directory does not exist, lets log a warning.
					log.comment("moveFile", "sOldFilePath", "FILE/DIRECTORY: " + "'" + oldFile.getAbsolutePath() + "' DOES NOT EXIST", Log.WARN,
							Log.SCRIPT_ISSUE);
				}
			}
		} catch (SecurityException e) {
			// Handle any security exceptions which might arise.
			throw new FrameworkException("moveFile", "sOldFilePath", e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}
		return res;
	}

	/***
	 * @author Murali K Parepalli
	 * @param sFilePath
	 * @return String
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to return the contents of a given file.<br>
	 */
	public String readFromFile(String sFilePath) throws FrameworkException {
		String contents = "";
		StringBuffer sOut = null;
		FileInputStream fStream = null;
		BufferedReader in = null;
		File file = null;

		try {
			validateInputFile(sFilePath, "readFromFile", "sFilePath");

			file = new File(sFilePath);

			if (file.exists()) {
				// File or directory exists, lets read from it.
				sOut = new StringBuffer();
				fStream = new FileInputStream(sFilePath);
				in = new BufferedReader(new InputStreamReader(fStream));
				// Read the Stream until
				while (in.ready()) {
					sOut.append(in.readLine());
					sOut.append(NEW_LINE);
				}
				if (sOut != null) {
					contents = sOut.toString().replace(NEW_LINE, "");
					if (!contents.isEmpty()) {
						// File is not empty, log a debug entry containing the file that was read from, and the first 100 characters followed by a
						// "...".
						String actionValueOrMessage = "";
						if (contents.length() >= MAXIMUM_CHARATCERS_TO_LOG) {
							actionValueOrMessage = contents.substring(0, MAXIMUM_CHARATCERS_TO_LOG - 1) + "...";
						} else {
							actionValueOrMessage = contents;
						}
						if (log != null) {
							log.comment("readFromFile", "FILE:" + NEW_LINE + "CONTENTS:",
									"'" + file.getAbsolutePath() + "'" + NEW_LINE + actionValueOrMessage, Log.DEBUG, Log.SCRIPT_ISSUE);
						}
					} else {
						if (log != null) {
							// File is empty, log a warning
							log.comment("readFromFile", "sFilePath", "FILE: " + "'" + file.getAbsolutePath() + "' IS EMPTY", Log.WARN,
									Log.SCRIPT_ISSUE);
						}
					}
				}
			} else {
				if (log != null) {
					// File or directory does not exist, lets log a warning.
					log.comment("readFromFile", "sFilePath", "FILE/DIRECTORY: " + "'" + file.getAbsolutePath() + "' DOES NOT EXIST", Log.WARN,
							Log.SCRIPT_ISSUE);
				}
			}

		} catch (IOException e) {
			if (log != null) {
				// Handle any "IOException's" which might arise.
				log.exception(e);
			}
		} finally {
			try {
				if (fStream != null) {
					fStream.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				if (log != null) {
					// Handle any "IOException's" which might arise.
					log.exception(e);
				}
			}
		}

		return sOut.toString();
	}

	/***
	 * @author Murali K Parepalli
	 * @param sFilePath
	 * @param inputStr
	 * @return "true/false"
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to write the contents of "inputStr" to an existing file.<br>
	 *             Returns "true" if write operation successeded and "false" otherwise.<br>
	 */
	public boolean writeToFile(String sFilePath, String inputStr) throws FrameworkException {

		File file = null;
		FileOutputStream fOutStream = null;
		PrintStream prtStream = null;
		boolean res = false;

		try {
			validateInputFile(sFilePath, "writeToFile", "sFilePath");

			if (inputStr != null) {
				if (!inputStr.isEmpty()) {
					file = new File(sFilePath);
					if (file.exists()) {
						// File or directory exists, lets write to it and log a debug entry.
						fOutStream = new FileOutputStream(sFilePath);
						prtStream = new PrintStream(fOutStream);
						prtStream.println(inputStr);
						res = true;
						String actionValueOrMessage = "";
						if (inputStr.length() >= MAXIMUM_CHARATCERS_TO_LOG) {
							actionValueOrMessage = inputStr.substring(0, MAXIMUM_CHARATCERS_TO_LOG - 1) + "...";
						} else {
							actionValueOrMessage = inputStr;
						}
						if (log != null) {
							log.comment("writeToFile", "FILE:" + NEW_LINE + "INPUT STRING:",
									"'" + file.getAbsolutePath() + "'" + NEW_LINE + actionValueOrMessage, Log.DEBUG, Log.SCRIPT_ISSUE);
						}
					} else {
						if (log != null) {
							// File or directory does not exist, lets log a warning.
							log.comment("writeToFile", "sFilePath", "FILE/DIRECTORY: " + "'" + file.getAbsolutePath() + "' DOES NOT EXIST", Log.WARN,
									Log.SCRIPT_ISSUE);
						}
					}
				} else {
					if (log != null) {
						// Argument "inputStr" makes reference to an empty string, log a warning.
						log.comment("writeToFile", "inputStr", "MAKES REFERENCE TO AN EMPTY STRING", Log.WARN, Log.SCRIPT_ISSUE);
					}
				}
			} else {
				// Argument "inputStr" makes reference to a null pointer, through appropriate "FrameworkException".
				throw new FrameworkException("writeToFile", "inputStr", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
			}
		} catch (IOException e) {
			if (log != null) {
				// Handle any "IOException's" which might arise.
				log.exception(e);
			}
		} finally {
			try {
				if (fOutStream != null) {
					fOutStream.close();
				}
			} catch (IOException e) {
				if (log != null) {
					// Handle any "IOException's" which might arise.
					log.exception(e);
				}
			} finally {
				if (prtStream != null) {
					prtStream.close();
				}
			}
		}

		return res;
	}

	/***
	 * @author Murali K Parepalli
	 * @param fileName
	 * @param content
	 * @return "true/false"
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to append the contents of "inputStr" to an existing file.<br>
	 */
	public boolean appendToFile(String sFilePath, String inputStr) throws FrameworkException {

		File file = null;
		FileWriter fileWriter = null;
		boolean res = false;

		try {
			validateInputFile(sFilePath, "appendToFile", "sFilePath");

			if (inputStr != null) {
				if (!inputStr.isEmpty()) {
					file = new File(sFilePath);
					if (file.exists()) {
						// File or directory exists, lets append to it and log a debug entry.
						fileWriter = new FileWriter(sFilePath, true);
						fileWriter.write(inputStr.trim());
						res = true;
						String actionValueOrMessage = "";
						if (inputStr.length() >= MAXIMUM_CHARATCERS_TO_LOG) {
							actionValueOrMessage = inputStr.substring(0, MAXIMUM_CHARATCERS_TO_LOG - 1) + "...";
						} else {
							actionValueOrMessage = inputStr;
						}
						if (log != null) {
							log.comment("appendToFile", "FILE:" + NEW_LINE + "INPUT STRING:",
									"'" + file.getAbsolutePath() + "'" + NEW_LINE + actionValueOrMessage, Log.DEBUG, Log.SCRIPT_ISSUE);
						}
					} else {
						if (log != null) {
							// File or directory does not exist, lets log a warning.
							log.comment("appendToFile", "sFilePath", "FILE/DIRECTORY: " + "'" + file.getAbsolutePath() + "' DOES NOT EXIST", Log.WARN,
									Log.SCRIPT_ISSUE);
						}
					}
				} else {
					if (log != null) {
						// Argument "inputStr" makes reference to an empty string, log a warning.
						log.comment("appendToFile", "inputStr", "MAKES REFERENCE TO AN EMPTY STRING", Log.WARN, Log.SCRIPT_ISSUE);
					}
				}
			} else {
				// Argument "inputStr" makes reference to a null pointer, through appropriate "FrameworkException".
				throw new FrameworkException("appendToFile", "inputStr", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
			}
		} catch (IOException e) {
			if (log != null) {
				// Handle any "IOException's" which might arise.
				log.exception(e);
			}
		} finally {
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
				if (file != null) {
					file = null;
				}
			} catch (IOException e) {
				if (log != null) {
					// Handle any "IOException's" which might arise.
					log.exception(e);
				}
			}
		}

		return res;
	}

	/***
	 * @author Murali K Parepalli
	 * @param sourceFile
	 * @param targetFile
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method copy an existing file from one location to another.<br>
	 */
	public void copyFile(String sourceFile, String targetFile) throws FrameworkException {

		File fileSource = null;
		File fileDestination = null;
		FileInputStream in = null;
		FileOutputStream out = null;

		try {
			validateInputFile(sourceFile, "copyFile", "sourceFile");
			validateInputFile(targetFile, "copyFile", "targetFile");

			fileSource = new File(sourceFile);
			fileDestination = new File(targetFile);

			if (fileSource.exists()) {
				in = new FileInputStream(sourceFile);
				out = new FileOutputStream(targetFile);

				byte[] buf = new byte[1024];
				int i = 0;
				while ((i = in.read(buf)) != -1) {
					out.write(buf, 0, i);
				}

				if (fileDestination.exists()) {
					if (log != null) {
						// File or directory exists, lets make a copy of it and log a debug entry.
						log.comment("copyFile", "SOURCE:" + NEW_LINE + "DESTINATION:",
								"'" + fileSource.getAbsolutePath() + "'" + NEW_LINE + "'" + fileDestination.getAbsolutePath() + "'", Log.DEBUG,
								Log.SCRIPT_ISSUE);
					}
				} else {
					if (log != null) {
						// File or directory does not exist, lets log a warning.
						log.comment("copyFile", "targetFile", "FILE/DIRECTORY: " + "'" + fileDestination.getAbsolutePath() + "' DOES NOT EXIST",
								Log.WARN, Log.SCRIPT_ISSUE);
					}
				}

			} else {
				if (log != null) {
					// File or directory does not exist, lets log a warning.
					log.comment("copyFile", "sourceFile", "FILE/DIRECTORY: " + "'" + fileSource.getAbsolutePath() + "' DOES NOT EXIST", Log.WARN,
							Log.SCRIPT_ISSUE);
				}
			}

		} catch (IOException e) {
			if (log != null) {
				// Handle any "IOException's" which might arise.
				log.exception(e);
			}
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				if (log != null) {
					// Handle any "IOException's" which might arise.
					log.exception(e);
				}
			}
		}
	}

	/*
	 * Use this method to insure that points to a non-null, non-empty "String" object. If argument "inputFile" makes reference to a null pointer or an
	 * empty string, an appropriate "FrameworkException" will be thrown.
	 */
	private void validateInputFile(String inputFile, String callingMethod, String callingMethodArgument) throws FrameworkException {

		if (inputFile == null) {
			throw new FrameworkException(callingMethod, callingMethodArgument, "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		if (inputFile.isEmpty()) {
			throw new FrameworkException(callingMethod, callingMethodArgument, "MAKES REFERENCE TO AN EMPTY STRING", Log.ERROR, Log.SCRIPT_ISSUE);
		}
	}

	public String getAbsolutePath(String relativeFilePath) {
		return new File(relativeFilePath).getAbsolutePath();
	}

	public String getFileName(String filePath) {
		return new File(filePath).getName();
	}
}
