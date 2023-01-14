package com.cisco.framework.cli.comparer;

import java.io.IOException;
import java.util.List;
import com.cisco.framework.cli.utils.CLIAssert;
import com.cisco.framework.cli.utils.NAMInfo;

public class CommandComparer {
	public static void compareCmdOutput(String actualCmdOutput, String cmd, NAMInfo namInfo, String... key_value_pairs) throws IOException {

		List<String> srcLst = FileComparer.parseCmdOutput(actualCmdOutput, cmd);

		if (key_value_pairs.length != 0) {
			srcLst = FileComparer.replaceArgsInLst(srcLst, key_value_pairs);
			// desLst=FileComparer.replaceArgsInLst(desLst, key_value_pairs);
		}

		String fileName = FileComparer.convertCmdToFileName(cmd);

		// store the actual command outputs to file
		FileComparer.writeCmdOutputToFile(fileName, srcLst);

		List<String> desLst = FileComparer.getStdCmdOutput(fileName + ";golden_data", namInfo);

		if (desLst.size() != 0) {
			if (desLst.get(0).startsWith("$LINE_TO_IGNORE")) {
				srcLst = FileComparer.ignoreSpecifiedLines(srcLst, desLst.get(0));
			}

			// //store the actual command outputs to file
			FileComparer.writeCmdOutputToFile("handled_" + fileName, srcLst);

			if (key_value_pairs.length != 0) {
				desLst = FileComparer.replaceArgsInLst(desLst, key_value_pairs);
			}
		}
		// Assert.assertEquals(srcLst, desLst);
		CLIAssert.assertMatchesRegex(srcLst, desLst);

	}
}
