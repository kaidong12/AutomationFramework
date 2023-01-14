package com.cisco.framework.cli.comparer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.testng.Assert;

import com.cisco.framework.cli.utils.NAMInfo;

public class IpHostsComparer {
	public static void compareCmdGrpOutput(LinkedHashMap<String, String> actualCmdGrpOutputMap, String caseName, NAMInfo namInfo,
			String... key_value_pairs) throws IOException {

		String fileName = caseName + ".txt";

		List<String> srcLst = FileComparer.parseCmdGrpOutput(actualCmdGrpOutputMap);

		// store the actual command outputs to file
		FileComparer.writeCmdOutputToFile(fileName, srcLst);

		List<String> desLst = FileComparer.getStdCmdOutput(fileName + ";regression_data", namInfo);

		// append the original hosts entry
		List<String> realDesLst = new ArrayList<String>();

		for (String line : desLst) {
			realDesLst.add(line);
			if (line.contains("show hosts")) {
				for (int i = 0; i < key_value_pairs.length; i++) {
					realDesLst.add(key_value_pairs[i]);
				}
			}
		}

		FileComparer.writeCmdOutputToFile(fileName + ".real", realDesLst);

		Assert.assertEquals(srcLst, realDesLst);
		// CLIAssert.assertMatchesRegex(srcLst, realDesLst);
	}
}
