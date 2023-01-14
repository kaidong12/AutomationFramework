package com.cisco.framework.cli.comparer;

import java.io.IOException;
import java.util.LinkedHashMap;
import com.cisco.framework.cli.utils.NAMInfo;

public class DataSourceErspanComparer {
	public static void compareCmdGrpOutput(LinkedHashMap<String, String> actualCmdGrpOutputMap, String caseName, NAMInfo namInfo,
			String... key_value_pairs) throws IOException {
		String erspanName = key_value_pairs[0].split(";")[1];
		// String [] replace_key_value_pairs=new String[key_value_pairs.length-1];
		// System.arraycopy(key_value_pairs, 1, replace_key_value_pairs, 0, replace_key_value_pairs.length);
		DataSourceUtils.compareCmdGrpOutput(actualCmdGrpOutputMap, caseName, namInfo, erspanName, key_value_pairs);
	}
}
