/**
 * 
 */
package com.cisco.framework.utilities;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lance Yan
 */
public class JFileTimeStamper {

	private static final String TIME_STAMP_PATTERN = "[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]_[0-9][0-9]-[0-9][0-9]-[0-9][0-9]";

	// Let's add a private constructor to dis-allow users from instantiating class
	private JFileTimeStamper() {
	}

	/**
	 * @author Lance Yan
	 * @param file
	 * @throws Exception
	 *             This method appends the timestamp 'timeStamp' at the end of the filename.<br>
	 *             The filename and timestamp are separated by an underscore character.<br>
	 *             <br>
	 *             For example if the filename is 'filename'<br>
	 *             then the filename with an appended timestamp is<br>
	 *             filename_2011-05-16-14_57_14.877<br>
	 */
	public static File stampIt(File file) throws Exception {
		File timeStampedFile = null;
		if (file.exists()) {
			String parent = file.getParent();
			String oldFileName = file.getName();
			if (!isFileNameTimeStamped(oldFileName)) {
				String timeStampedFileName = oldFileName.replace(".", "_" + generateTimeStampFromSystemTime() + ".");
				timeStampedFile = new File(parent + File.separatorChar + timeStampedFileName);
				file.renameTo(timeStampedFile);
			}
		}

		return timeStampedFile;
	}

	/**
	 * @author Lance Yan
	 * @param outPutFolder
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             This method uses method "stampIt" to append a timestamp at the end of the filename.<br>
	 *             The filename and timestamp are separated by an underscore character.<br>
	 *             <br>
	 *             For example if the filename is 'filename'<br>
	 *             then the filename with an appended timestamp is<br>
	 *             filename_2011-05-16-14_57_14.877<br>
	 */
	public static void timeStampTestResultFiles(String outPutFolder) throws Exception {
		File outputFolder = new File(outPutFolder);
		if (outputFolder.exists()) {
			// Get a list of all files in output folder.
			for (File file : outputFolder.listFiles()) {
				stampIt(file);
			}
		}
	}

	/**
	 * @author Lance Yan
	 * @return String <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         Helper method used by method "timeStampTestResultFiles".<br>
	 */
	private static String generateTimeStampFromSystemTime() {

		// Generate a SQL timestamp value based on the system's current time in milliseconds.
		String timeStamp = new java.sql.Timestamp(System.currentTimeMillis()).toString();
		// Replace occurence of the ' ' character with the '_' character.
		timeStamp = timeStamp.replace(" ", "_");
		// Replace of all occurences of the ":" character with the "-".
		timeStamp = timeStamp.replace(":", "-");
		// Get the timestamp portion without the millisecond part.
		timeStamp = timeStamp.substring(0, timeStamp.indexOf("."));

		return timeStamp;
	}

	/**
	 * @author Lance Yan
	 * @param fileName
	 * @return true/false <br>
	 *         <br>
	 *         USAGE:<br>
	 *         <br>
	 *         <p>
	 *         This method returns 'true' if 'fileName' is time stamped and 'false' otherwise.<br>
	 */
	private static boolean isFileNameTimeStamped(String fileName) {
		boolean res = false;

		if (fileName != null) {
			if (!fileName.isEmpty()) {
				Pattern pattern = Pattern.compile(TIME_STAMP_PATTERN);
				Matcher matcher = pattern.matcher(fileName);
				while (matcher.find()) {
					res = true;
				}
			}
		}

		return res;
	}

}