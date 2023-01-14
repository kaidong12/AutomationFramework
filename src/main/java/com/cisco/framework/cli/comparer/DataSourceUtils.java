package com.cisco.framework.cli.comparer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.cisco.framework.cli.utils.CLIAssert;
import com.cisco.framework.cli.utils.NAMInfo;

public class DataSourceUtils {
	// to parse the output of "show data-source" command with a specified "data source name"
	// output is a list contains the matched data source name part.
	public static void compareCmdGrpOutput(LinkedHashMap<String, String> actualCmdGrpOutputMap, String caseName, NAMInfo namInfo, String dsName,
			String... key_value_pairs) throws IOException {

		String fileName = caseName + ".txt";

		List<String> srcLst = new ArrayList<String>();
		for (Entry<String, String> entry : actualCmdGrpOutputMap.entrySet()) {
			String cmdOutput = entry.getValue();
			String[] commandAndrecord = entry.getKey().split(";");
			String command = commandAndrecord[0];
			// String actualCommand=command.split(":")[1].trim();
			String record = commandAndrecord[1];
			try {
				srcLst.add(record);
				if (!cmdOutput.equals("$")) {
					// need to filter the specified data source name
					if (command.equals("show data-source")) {
						List<String> parsedStepCmdLst = new ArrayList<String>();
						String[] cmdLineOutputs = cmdOutput.split("\n");
						boolean bAdded = false;
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

							if (lineOutput.equals(command)) {
								continue;
							}

							if (lineOutput.startsWith("root@")) {
								continue;
							}

							if (lineOutput.startsWith("DATA SOURCE NAME")) {
								if (lineOutput.split(":")[1].trim().equals(dsName)) {
									bAdded = true;
								}
							}

							if (lineOutput.startsWith("----")) {
								if (bAdded) {
									// may have two datasouce with the same data source name
									// add the specified datasource to global list
									srcLst.addAll(parsedStepCmdLst);
									parsedStepCmdLst.clear();
									bAdded = false;
									continue;
								} else {
									parsedStepCmdLst.clear();
									continue;
								}
							} else {
								parsedStepCmdLst.add(lineOutput);
							}
						}
					} else {
						List<String> parsedStepCmdLst = FileComparer.parseCmdOutput(cmdOutput, command);
						srcLst.addAll(parsedStepCmdLst);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// replace 1 xxxx
		if (key_value_pairs.length != 0) {
			srcLst = FileComparer.replaceArgsInLst(srcLst, key_value_pairs);
		}

		// store the actual command outputs to file
		FileComparer.writeCmdOutputToFile(fileName, srcLst);

		List<String> desLst = FileComparer.getStdCmdOutput(fileName + ";regression_data", namInfo);
		if (key_value_pairs.length != 0) {
			desLst = FileComparer.replaceArgsInLst(desLst, key_value_pairs);
		}

		if (desLst.get(0).startsWith("$LINE_TO_IGNORE")) {
			srcLst = FileComparer.ignoreSpecifiedLines(srcLst, desLst.get(0));
		}
		CLIAssert.assertMatchesRegex(srcLst, desLst);
	}

	public static List<String> getDataSourceInfoByName(String cmdOutput, String dsName) {
		String command = "show data-source";
		List<String> parsedStepCmdLst = new ArrayList<String>();
		String[] cmdLineOutputs = cmdOutput.split("\n");
		boolean bAdded = false;
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

			if (lineOutput.equals(command)) {
				continue;
			}

			if (lineOutput.startsWith("root@")) {
				continue;
			}

			if (lineOutput.startsWith("DATA SOURCE NAME")) {
				if (lineOutput.split(":")[1].trim().equals(dsName)) {
					bAdded = true;
				}
			}

			if (lineOutput.startsWith("----")) {
				if (bAdded) {
					return parsedStepCmdLst;
				} else {
					parsedStepCmdLst.clear();
					continue;
				}
			} else {
				parsedStepCmdLst.add(lineOutput);
			}
		}
		return parsedStepCmdLst;
	}

	public static String getDataSourceIdByName(String cmdOutput, String dsName) {
		List<String> dsInfo = getDataSourceInfoByName(cmdOutput, dsName);

		String dsId = "-1";
		for (String line : dsInfo) {
			if (line.startsWith("DATA SOURCE ID")) {
				return line.split(":")[1].trim();
			}
		}
		return dsId;
	}
}
