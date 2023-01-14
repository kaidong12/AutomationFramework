package com.cisco.framework.cli.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class CaseHandler {
	private Document	doc;
	private IConnection	connection;

	public CaseHandler(NAMInfo namInfo, IConnection connection) throws Exception {
		SAXReader reader = new SAXReader();
		doc = reader.read("./test-input/expected-command-outputs/" + namInfo.getNamVersion() + "/regression_data/regression_testcases.xml");
		this.connection = connection;
	}

	// get all the step commands for a case
	// key as command, value as indicator to represent whether needs to compare command output
	// [#1;command NULL #]
	// [#2;command #2 :]
	public LinkedHashMap<String, String> getCaseStepsByName(String caseName) {
		LinkedHashMap<String, String> stepsMap = new LinkedHashMap<String, String>();

		String recordAndPrompt;
		String command;
		int index = 1;
		for (@SuppressWarnings("rawtypes")
		Iterator itor = doc.selectNodes("/testcase/test[@name='" + caseName + "']/step").iterator(); itor.hasNext();) {
			Element stepElem = (Element) itor.next();
			recordAndPrompt = "";
			command = stepElem.attributeValue("command");
			if (stepElem.attribute("output") != null && stepElem.attributeValue("output").equals("y")) {
				recordAndPrompt = "#" + String.valueOf(index) + ": " + command;
			} else {
				recordAndPrompt = "NULL#" + String.valueOf(index) + ": " + command;
			}

			if (stepElem.attribute("prompt") != null) {
				recordAndPrompt += ";" + stepElem.attributeValue("prompt");
			} else {
				recordAndPrompt += ";#";
			}

			// some commands need to be delayed after executing additional commands, like changing data aggregation time.
			// since these commands are few, appending a string like ";DELAYTIME_10" to the end of the command
			if (stepElem.attribute("delayTime") != null) {
				recordAndPrompt += ";DELAYTIME_" + stepElem.attributeValue("delayTime");
			}

			stepsMap.put("#" + String.valueOf(index) + ";" + command, recordAndPrompt);
			index++;

		}
		return stepsMap;
	}

	public LinkedHashMap<String, String> executeCommandsInTest(LinkedHashMap<String, String> stepCmdsMap) throws Exception {

		return executeStepCmds(stepCmdsMap);
	}

	public LinkedHashMap<String, String> executeCommandsInTest(String caseName) throws Exception {

		LinkedHashMap<String, String> stepCmdsMap = getCaseStepsByName(caseName);

		return executeStepCmds(stepCmdsMap);
	}

	private LinkedHashMap<String, String> executeStepCmds(LinkedHashMap<String, String> stepCmdsMap) throws Exception {

		LinkedHashMap<String, String> stepOutputsMap = new LinkedHashMap<String, String>();

		for (Entry<String, String> entry : stepCmdsMap.entrySet()) {
			// some commands need to be delayed after executing additional commands, like changing data aggregation time.
			// since these commands are few, appending a string like ";DELAYTIME_10" to the end of the command
			String recordAndPromptValue = entry.getValue();
			int delayTime = 0;
			if (recordAndPromptValue.contains(";DELAYTIME_")) {
				int startPos = recordAndPromptValue.indexOf(";DELAYTIME_");
				delayTime = Integer.parseInt(recordAndPromptValue.substring(startPos + 11));
				recordAndPromptValue = recordAndPromptValue.substring(0, startPos);
			}

			String[] recordAndPrompt = recordAndPromptValue.split(";");
			String prompt = recordAndPrompt[1];
			String record = recordAndPrompt[0];
			String command = entry.getKey().split(";")[1];

			try {
				// 2014-4-14, chaoy, change the step time out to 90 seconds
				// String cmdOutput=connection.executeUntilPrompt(command, prompt,600);
				String cmdOutput = connection.executeUntilPrompt(command, prompt, 90);
				try {
					if (delayTime != 0) {
						Thread.sleep(delayTime * 1000);
					}
				} catch (Exception e) {
					;
				}

				if (!record.startsWith("NULL")) {
					stepOutputsMap.put(command + ";" + record, cmdOutput);
				} else {
					stepOutputsMap.put(command + ";" + record.replace("NULL", ""), "$");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		// [email;#1 $] $ will not write to txt file
		// [email;#2: email ""]
		// [server;#3: server "adadadad"]
		return stepOutputsMap;
	}

	public static void main(String[] args) throws Exception {
		// NAMInfo namInfo = new NAMInfo("10.75.169.27");
		// IConnection connection = (new NAMConnection(namInfo)).setupConnection();
		// CaseHandler ch=new CaseHandler(namInfo,connection);
		// ch.executeCommandsInTest("email_command_group");
		// ch.executeCommandsInTest("email_command_group1");
		String abc = "abc;DELAYTIME_10";
		int startPos = abc.indexOf(";DELAYTIME_");
		int delayTime = Integer.parseInt(abc.substring(startPos + 11));
		abc = abc.substring(0, startPos);
		System.out.println(delayTime);
		System.out.println(abc);

	}

}
