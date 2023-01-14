package com.cisco.framework.cli.comparer;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import com.cisco.framework.cli.utils.CLIAssert;
import com.cisco.framework.cli.utils.NAMInfo;

public class CmdGrpComparer {
	public static void compareCmdGrpOutput(LinkedHashMap<String, String> actualCmdGrpOutputMap, String caseName, NAMInfo namInfo,
			String... key_value_pairs) throws IOException {

		String fileName = caseName + ".txt";

		List<String> srcLst = FileComparer.parseCmdGrpOutput(actualCmdGrpOutputMap);
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
		// store the handled command outputs to file
		FileComparer.writeCmdOutputToFile("handled_" + fileName, srcLst);
		// Assert.assertEquals(srcLst, desLst);
		CLIAssert.assertMatchesRegex(srcLst, desLst);

	}
}
