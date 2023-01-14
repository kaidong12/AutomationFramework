package com.cisco.framework.cli.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.UUID;

public class CmdParser {
	/**
	 *
	 * @param cmd
	 * @param key_value_pairs:
	 *            format of each pair: $APPID;1234567
	 * @return
	 */
	public static String replaceCmd(String cmd, String... key_value_pairs) {
		String newCmd = cmd;
		for (String key_value : key_value_pairs) {
			String[] pairs = key_value.split(";");
			if (newCmd.contains(pairs[0])) {
				newCmd = newCmd.replace(pairs[0], pairs[1]);
			}
		}
		return newCmd;
	}

	public static String getDataFolder(String namVersion) {
		if (namVersion.contains("5.1"))
			return "5x";
		else
			return "6x";
	}

	public static String convertCDBCmd(String CDBCmd, NAMInfo namInfo) {
		if (namInfo.getNamVersion().equals("5x")) {
			String ver5xCDBCmd = CDBCmd.replace("cdb ", "cdb /storage/cdb/");
			ver5xCDBCmd += ".cdb";
			return ver5xCDBCmd;
		} else {
			return CDBCmd;
		}
	}

	/**
	 *
	 * @param stepsCmdsMap
	 *            format: [#1;app xxx]
	 * @param key_value_pairs
	 * @return
	 */
	public static LinkedHashMap<String, String> replaceCmd(LinkedHashMap<String, String> stepCmdsMap, String... key_value_pairs) {
		LinkedHashMap<String, String> actualStepMap = new LinkedHashMap<String, String>();

		for (Entry<String, String> entry : stepCmdsMap.entrySet()) {
			String newCmd = CmdParser.replaceCmd(entry.getKey(), key_value_pairs);
			String newValue = CmdParser.replaceCmd(entry.getValue(), key_value_pairs);
			actualStepMap.put(newCmd, newValue);
		}

		return actualStepMap;
	}

	public static String getExistingAppTag(List<String> parsedCmdOutput, String appFullName) {
		for (String app : parsedCmdOutput) {
			if (app.endsWith(appFullName)) {
				int startPos = app.indexOf("(") + 1;
				int endPos = app.lastIndexOf(")");
				return app.substring(startPos, endPos);
			}
		}
		return "";
	}

	public static String getNewAppTag(String cmdOutput) {
		String line = getDesiredLineByKey(cmdOutput, "(app tag");

		return getStringByReg(line, "[^0-9]");
	}

	public static String getIpv4Addr(String cmdOutput) throws CommandException {
		String ipv4Line = getDesiredLineByKey(cmdOutput, "IP address:");
		if (ipv4Line.equals("")) {
			throw new CommandException("Cannot get ipv4 address!");
		} else {
			return ipv4Line.split(":")[1].trim();
		}
	}

	public static String getIpv6Addr(String cmdOutput) throws CommandException {
		String ipv6Line = getDesiredLineByKey(cmdOutput, "IPv6 address:");
		if (ipv6Line.equals("")) {
			throw new CommandException("Cannot get ipv6 address!");
		} else {
			return ipv6Line.replace("IPv6 address:", "").trim();
		}
	}

	public static String getIpv4Subnetmask(String cmdOutput) throws CommandException {
		String subnetmask = getDesiredLineByKey(cmdOutput, "Subnet mask:");
		if (subnetmask.equals("")) {
			throw new CommandException("Cannot get ipv4 subnet mask!");
		} else {
			return subnetmask.split(":")[1].trim();
		}
	}

	public static String getIpBroadCast(String cmdOutput) throws CommandException {
		String ipBroadCast = getDesiredLineByKey(cmdOutput, "IP Broadcast:");
		if (ipBroadCast.equals("")) {
			throw new CommandException("Cannot get IP Broadcast!");
		} else {
			return ipBroadCast.split(":")[1].trim();
		}
	}

	public static String getDnsName(String cmdOutput) throws CommandException {
		String dnsName = getDesiredLineByKey(cmdOutput, "Host Name:");
		if (dnsName.equals("")) {
			throw new CommandException("Cannot get Host Name!");
		} else {
			return dnsName.split(":")[1].trim();
		}
	}

	public static String getDefaultGateway(String cmdOutput) throws CommandException {
		String dftGateway = getDesiredLineByKey(cmdOutput, "Default Gateway:");
		if (dftGateway.equals("")) {
			throw new CommandException("Cannot get default gateway!");
		} else {
			return dftGateway.split(":")[1].trim();
		}
	}

	public static String getIPv6Gateway(String cmdOutput) throws CommandException {
		String ipv6Gateway = getDesiredLineByKey(cmdOutput, "IPv6 Gateway:");
		if (ipv6Gateway.equals("")) {
			throw new CommandException("Cannot get IPv6 gateway!");
		} else {
			return ipv6Gateway.split(":")[1].trim();
		}
	}

	public static List<String> getNameServers(String cmdOutput) {
		String nameSrvs = getDesiredLineByKey(cmdOutput, "Nameserver(s):");
		List<String> nameSrvsLst = new ArrayList<String>();
		if (nameSrvs.equals("")) {
			return nameSrvsLst;
		} else {
			String[] servers = nameSrvs.split(":")[1].trim().split(" ");
			for (String srv : servers) {
				if (srv.trim().equals("")) {
					continue;
				}
				nameSrvsLst.add(srv.trim());
			}
			return nameSrvsLst;
		}
	}

	public static String getHttpPort(String cmdOutput) throws CommandException {
		String httpPort = getDesiredLineByKey(cmdOutput, "HTTP port:");
		if (httpPort.equals("")) {
			throw new CommandException("Cannot get http port!");
		} else {
			return httpPort.split(":")[1].trim();
		}
	}

	public static String getSecurePort(String cmdOutput) throws CommandException {
		String securePort = getDesiredLineByKey(cmdOutput, "HTTP secure port:");
		if (securePort.equals("")) {
			throw new CommandException("Cannot get http secure port!");
		} else {
			return securePort.split(":")[1].trim();
		}
	}

	public static String getTACACSStatus(String cmdOutput) {
		String tacacsStatus = getDesiredLineByKey(cmdOutput, "TACACS+ configured:");
		if (tacacsStatus.equals("")) {
			return "";
		} else {
			return tacacsStatus.split(":")[1].trim();
		}
	}

	public static String getTACACSPrimarySrv(String cmdOutput) {
		String primarySrv = getDesiredLineByKey(cmdOutput, "TACACS+ primary server:");
		if (primarySrv.equals("")) {
			return "";
		} else {
			return primarySrv.split(":")[1].trim();
		}
	}

	public static String getErspanDeviceID(String cmdOutput) {
		String[] outputLines = cmdOutput.split("\n");
		for (int i = 0; i < outputLines.length; i++) {
			if (outputLines[i].startsWith("DEVICE TYPE") && outputLines[i].contains(":")) {
				if (outputLines[i].split(":")[1].trim().contains("ERSPAN"))
					return outputLines[i - 1].split(":")[1].trim();
			}
		}
		return "";
	}

	public static String getDesiredLineByKey(String cmdOutput, String key) {
		String[] outputLines = cmdOutput.split("\n");
		for (String line : outputLines) {
			if (line.contains(key)) {
				return line;
			}
		}
		return "";
	}

	public static String getStringByReg(String sourceString, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(sourceString);
		return m.replaceAll("").trim();
	}

	public static String getRandomString(int length) {
		String randomStr = UUID.randomUUID().toString();
		int size = randomStr.length();
		if (length > size) {
			length = size;
		}
		return randomStr.substring(0, length);
	}

	public static String getCertficateRequestCode(String output) {
		StringBuffer sb = new StringBuffer();
		String[] outputLines = output.split("\n");
		for (int i = 0; i < outputLines.length; i++) {
			if (outputLines[i].contains("BEGIN CERTIFICATE") || outputLines[i].contains("END CERTIFICATE"))
				continue;
			if (outputLines[i].equals("\r")) {
				continue;
			}
			if (outputLines[i].endsWith("#")) {
				continue;
			}
			String line = outputLines[i].replaceAll("\r", "").trim();

			if (line.equals("")) {
				continue;
			}
			if (line.contains("show certificate-request") || line.contains("show certificate")) {
				continue;
			}
			if (line.contains("feifeng@cisco.com")) {
				continue;
			}
			if (line.startsWith("root@")) {
				continue;
			}
			sb.append(line);
		}
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		// String s="ethertype:2048 (301991936) ipv4";
		// int startPos=s.indexOf("(")+1;
		// int endPos=s.lastIndexOf(")");
		// System.out.println(s.substring(startPos, endPos));

		StringBuilder sb = new StringBuilder();
		sb.append("abc").append("\n");
		sb.append("bcd").append("\n");
		sb.append("bcd").append("\n");
		sb.append("bcd").append("\n");
		System.out.println(sb.toString());
		// IConnection connection;
		// NAMInfo namInfo;
		//
		// namInfo = new NAMInfo("172.20.110.196");
		// connection = (new NAMConnection(namInfo)).setupConnection();
		// String output=connection.executeUntilPrompt("show ip");
		//
		//
		// connection.execute("exit");
		// connection.disconnect();
		// String []lines=output.split("\n");
		// for(String line: lines)
		// if(line.contains("root"))
		// System.out.println("************"+line);

	}
}
