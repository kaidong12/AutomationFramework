package com.cisco.framework.utilities.TestLink;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class Apache_xmlrpc_client_Connector {

	static String	resultFile	= null;
	static String	devKey		= null, apiUrl = null;
	static String	projectName	= null, testplanName = null, buildName = null, platformName = null,
			executionComments = null;

	public static void uploadResults2TestLink(String... args) {

		processArguments(args);
		List<String> res = processResultsFile(resultFile);

		try {
			Iterator<String> itr = res.iterator();

			while (itr.hasNext()) {
				String temp = itr.next();
				if (!temp.startsWith("BeforeSuite") && !temp.startsWith("BeforeClass")
						&& !temp.startsWith("BeforeTest") && !temp.startsWith("AfterClass")
						&& !temp.startsWith("AfterSuite")) {
					doUpload(temp);

				}

			}

			System.out.println("Done.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * report test execution to TestLink API
	 * 
	 * @param String
	 *            res
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void doUpload(String res) {

		String[] tcArgs = res.split("=");
		String tcId = tcArgs[0];
		String tcStatus = tcArgs[1];
		String defectId = null;

		if (tcStatus.contains(":")) {
			String[] comments = tcStatus.split(":");
			tcStatus = comments[0];
			defectId = comments[1];
		}

		try {
			XmlRpcClient rpcClient;
			XmlRpcClientConfigImpl config;

			config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(apiUrl));
			rpcClient = new XmlRpcClient();
			rpcClient.setConfig(config);

			Hashtable<String, Object> executionData = new Hashtable<String, Object>();
			ArrayList<Object> params1 = new ArrayList<Object>();
			executionData = new Hashtable<String, Object>();
			executionData.put("devKey", devKey);
			executionData.put("testprojectname", projectName);
			executionData.put("testplanname", testplanName);
			params1.add(executionData);

			Object[] result1 = (Object[]) rpcClient.execute("tl.getTestPlanByName", params1);
			Map<Object, Object> itemp = (Map<Object, Object>) result1[0];
			int tpId = Integer.parseInt((String) itemp.get("id"));
			System.out.println("Test case external Id: " + tcId);
			System.out.println("  Keys: " + itemp.keySet().toString() + "\nvalues: " + itemp.values().toString());

			ArrayList<Object> params2 = new ArrayList<Object>();
			executionData = new Hashtable<String, Object>();
			executionData.put("devKey", devKey);
			// executionData.put("testcaseid", tcid);
			executionData.put("testcaseexternalid", tcId);
			executionData.put("testplanid", tpId);

			// executionData.put("buildid", idBuild);
			executionData.put("buildname", buildName);
			// executionData.put("buildid", 1);
			executionData.put("status", tcStatus);
			executionData.put("platformname", platformName);
			if (defectId != null) {
				executionData.put("notes", executionComments + "\nDefect ID: " + defectId);
			} else {
				executionData.put("notes", executionComments);
			}

			params2.add(executionData);

			Object[] result2 = (Object[]) rpcClient.execute("tl.reportTCResult", params2);

			// Typically you'd want to validate the result here and probably do something more useful with it
			System.out.println("reportTCResult Result was:");
			for (int i = 0; i < result2.length; i++) {
				Map<Object, Object> item = (Map<Object, Object>) result2[i];
				System.out.println("  Keys: " + item.keySet().toString() + "\nvalues: " + item.values().toString()
						+ "\n");
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}
	}

	private static void processArguments(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-resultFile")) {
				resultFile = args[++i];
			}
			if (args[i].equals("-projectName")) {
				projectName = args[++i];
			}
			if (args[i].equals("-testplanName")) {
				testplanName = args[++i];
			}
			if (args[i].equals("-buildName")) {
				buildName = args[++i];
			}
			if (args[i].equals("-platformName")) {
				platformName = args[++i];
			}
			if (args[i].equals("-devKey")) {
				devKey = args[++i];
			}
			if (args[i].equals("-apiUrl")) {
				apiUrl = args[++i];
			}
			if (args[i].equals("-comments")) {
				executionComments = args[++i];
			}
		}
	}

	private static List<String> processResultsFile(String resultsFilePath) {
		List<String> resultList = new ArrayList<String>();

		try {
			// String encoding = "GBK";
			File file = new File(resultsFilePath);
			if (file.isFile() && file.exists()) {
				// InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTXT = null;
				while ((lineTXT = bufferedReader.readLine()) != null) {
					System.out.println(lineTXT.toString().trim());
					resultList.add(lineTXT);
				}
				read.close();
			} else {
				System.out.println("Result file does not exist!");
			}
		} catch (Exception e) {
			System.out.println("Error when read file!");
			e.printStackTrace();
		}

		return resultList;

	}

}