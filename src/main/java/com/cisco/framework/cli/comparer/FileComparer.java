package com.cisco.framework.cli.comparer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import com.cisco.framework.cli.utils.CmdParser;
import com.cisco.framework.cli.utils.NAMInfo;
import com.cisco.framework.cli.utils.NAMType;

public class FileComparer {
	private NAMInfo namInfo;

	public FileComparer(NAMInfo namInfo) {
		this.namInfo = namInfo;
	}

	/**
	 * use default comparer CommandComparer to compare, no special operations for command output: 1. if it finds the related command comparer, use
	 * this comparer 2. if it does not find the related command comparer, use the default CommandComparer
	 */
	public void compareFile(String actualCmdOutput, String cmd, String... key_value_pairs) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, IOException {
		String clsName = cmdToCmpClass(cmd);
		if (searchComparerClass(clsName.replace("com.cisco.framework.cli.comparer.", "") + ".java")) {
			clsName = cmdToCmpClass(cmd);

			ClassLoader cl = ClassLoader.getSystemClassLoader();

			@SuppressWarnings("rawtypes")
			Class cmpClass = cl.loadClass(clsName);
			Object cmpInstance = cmpClass.newInstance();

			@SuppressWarnings("unchecked")
			Method cmpMethod = cmpClass.getMethod("compareCmdOutput", new Class[] { java.lang.String.class, java.lang.String.class,
					com.cisco.framework.cli.utils.NAMInfo.class, java.lang.String[].class });

			cmpMethod.invoke(cmpInstance, actualCmdOutput, cmd, namInfo, key_value_pairs);

		} else {
			CommandComparer.compareCmdOutput(actualCmdOutput, cmd, namInfo, key_value_pairs);
		}
	}

	/**
	 * use default comparer CmdGrpComparer to compare, no special operations for command output: 1. if it finds the related command comparer, use this
	 * comparer 2. if it does not find the related command comparer, use the default CmdGrpComparer
	 */
	public void compareFile(LinkedHashMap<String, String> actualCmdGrpOutputMap, String caseName, String... key_value_pairs)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException, IOException {
		String clsName = caseNameToCmpClass(caseName);
		if (searchComparerClass(clsName.replace("com.cisco.framework.cli.comparer.", "") + ".java")) {

			ClassLoader cl = ClassLoader.getSystemClassLoader();

			@SuppressWarnings("rawtypes")
			Class cmpClass = cl.loadClass(clsName);
			Object cmpInstance = cmpClass.newInstance();

			@SuppressWarnings("unchecked")
			Method cmpMethod = cmpClass.getMethod("compareCmdGrpOutput", new Class[] { java.util.LinkedHashMap.class, java.lang.String.class,
					com.cisco.framework.cli.utils.NAMInfo.class, java.lang.String[].class });

			cmpMethod.invoke(cmpInstance, actualCmdGrpOutputMap, caseName, namInfo, key_value_pairs);

		} else {
			CmdGrpComparer.compareCmdGrpOutput(actualCmdGrpOutputMap, caseName, namInfo, key_value_pairs);
		}
	}

	public String cmdToCmpClass(String cmd) {

		String cmpClassName = "com.cisco.framework.cli.comparer.";
		String[] methodNames = cmd.split(" ");
		for (int i = 0; i < methodNames.length; i++) {
			cmpClassName += methodNames[i].substring(0, 1).toUpperCase() + methodNames[i].substring(1);
		}
		return cmpClassName + "Comparer";
	}

	public String caseNameToCmpClass(String caseName) {
		String cmpClassName = "com.cisco.framework.cli.comparer.";
		caseName = caseName.replace("_commands", "");
		String[] methodNames = caseName.split("_");
		for (int i = 0; i < methodNames.length; i++) {
			cmpClassName += methodNames[i].substring(0, 1).toUpperCase() + methodNames[i].substring(1);
		}
		return cmpClassName + "Comparer";
	}

	// store actual cmd output to arraylist
	public static List<String> parseCmdOutput(String cmdOutput, String cmd) {
		List<String> parsedCmdLst = new ArrayList<String>();
		String[] cmdLineOutputs = cmdOutput.split("\n");
		for (String lineOutput : cmdLineOutputs) {
			lineOutput = lineOutput.replaceAll(" \b", "");

			if (lineOutput.equals("\r")) {
				continue;
			}
			if (lineOutput.endsWith("#")) {
				continue;
			}

			lineOutput = lineOutput.replaceAll("\r", "").trim();

			if (lineOutput.equals("")) {
				continue;
			}

			if (lineOutput.equals(cmd)) {
				continue;
			}

			if (lineOutput.startsWith("root@")) {
				continue;
			}

			parsedCmdLst.add(lineOutput);
		}
		// writeCmdOutputToFile(cmd,parsedCmdLst);
		return parsedCmdLst;
	}

	public static List<String> parseCmdGrpOutput(LinkedHashMap<String, String> cmdGrpOutputMap) {
		List<String> caseOutputLst = new ArrayList<String>();
		for (Entry<String, String> entry : cmdGrpOutputMap.entrySet()) {
			String cmdOutput = entry.getValue();
			String[] commandAndrecord = entry.getKey().split(";");
			String command = commandAndrecord[0];
			String record = commandAndrecord[1];
			try {
				caseOutputLst.add(record);
				if (!cmdOutput.equals("$")) {
					List<String> parsedStepCmdLst = FileComparer.parseCmdOutput(cmdOutput, command);
					caseOutputLst.addAll(parsedStepCmdLst);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return caseOutputLst;
	}

	public static void writeCmdOutputToFile(String fileName, List<String> parsedCmdLst) {
		BufferedWriter writer = null;
		try {
			// writer=new BufferedWriter(new FileWriter("c:/"+ convertCmdToFileName(cmd)));
			writer = new BufferedWriter(new FileWriter("./actual-command-outputs/" + fileName));
			for (String lineOutput : parsedCmdLst) {
				if (lineOutput.equals("")) {
					continue;
				}
				writer.write(lineOutput + "\n");
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

	// get expected cmd output from related file and the store them into arraylist
	public static List<String> getStdCmdOutput(String fileName, NAMInfo namInfo) throws IOException {
		NAMType namType = namInfo.getNamType();
		String namFolder = namType.toString();
		String[] folderInfo = fileName.split(";");
		List<String> stdCmdLst = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(
				"./test-input/expected-command-outputs/" + namInfo.getNamVersion() + "/" + folderInfo[1] + "/" + namFolder + "/" + folderInfo[0]));
		String line = null;
		while ((line = br.readLine()) != null) {
			stdCmdLst.add(line.replaceAll("\r", "").trim());
		}
		return stdCmdLst;
	}

	public static List<String> getCDBOutputByVersion(String version, String fileName) throws IOException {
		List<String> stdCmdLst = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader("./upgrade-test-output/" + version + "/" + fileName));
		String line = null;
		while ((line = br.readLine()) != null) {
			stdCmdLst.add(line.replaceAll("\r", "").trim());
		}
		return stdCmdLst;
	}

	public static String convertCmdToFileName(String cmd) {
		String fileName = "";
		if (cmd.contains("$")) {
			System.out.println("not implement yet!");

			return fileName;
		}

		fileName = cmd.replaceAll(" ", "_") + ".txt";
		return fileName;
	}

	private static boolean searchComparerClass(String cmpClass) {
		File files = new File(System.getProperty("user.dir") + "/src/main/java/com/cisco/businessfunction");
		String[] allFiles = files.list();

		for (String file : allFiles) {
			if (file.equals(cmpClass))
				return true;
		}
		return false;
	}

	public static List<String> replaceArgsInLst(List<String> srcLst, String... key_value_pairs) {
		List<String> newLst = new ArrayList<String>();

		for (String line : srcLst) {
			String parsedLine = CmdParser.replaceCmd(line, key_value_pairs);
			newLst.add(parsedLine);
		}

		return newLst;
	}

	public static List<String> ignoreSpecifiedLines(List<String> srcLst, String lines) {
		List<String> newLst = new ArrayList<String>();
		newLst.add(lines);
		String[] specialLines = lines.replace("$LINE_TO_IGNORE:", "").split(";");
		List<String> trimedSpecialLines = new ArrayList<String>();
		for (String line : specialLines) {
			trimedSpecialLines.add(line.trim());
		}

		for (String line : srcLst) {
			boolean bIgnore = false;
			for (String lineToIgnore : trimedSpecialLines) {
				if (line.startsWith(lineToIgnore)) {
					bIgnore = true;
					break;
				}
			}
			if (!bIgnore) {
				newLst.add(line);
			}
		}

		return newLst;
	}

	public static List<String> parseCdbCmdsForUpgrade(String cmdOutput, String cmd) {
		List<String> parsedCmdLst = new ArrayList<String>();
		String[] cmdLineOutputs = cmdOutput.split("\n");
		for (String lineOutput : cmdLineOutputs) {

			if (lineOutput.equals("\r")) {
				continue;
			}
			if (lineOutput.endsWith("#")) {
				continue;
			}

			lineOutput = lineOutput.replaceAll("\r", "").trim();

			if (lineOutput.equals("")) {
				continue;
			}

			if (lineOutput.equals(cmd)) {
				continue;
			}

			if (lineOutput.startsWith("root@")) {
				continue;
			}

			parsedCmdLst.add(lineOutput);
		}
		// writeCmdOutputToFile(cmd,parsedCmdLst);
		return parsedCmdLst;
	}

	public static void CompareCDBUpgradeFile(String cmd) {

	}

	public static void main(String[] args) throws SecurityException, IllegalArgumentException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
		// compareFile("abcd","show version$", "aa","bb","cc");
		searchComparerClass("ShowVersionComparer.java");
	}
}
