package com.cisco.framework.cli.utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cisco.framework.cli.utils.NAMType;

public class NAMInfo {

	private String	namIP;
	private NAMType	namType;
	private String	deviceUsr;
	private String	devicePwd;
	private String	switchNum	= "-1";
	private String	slot;
	private String	deviceIP;
	private String	port		= "-1";
	private String	sm			= "-1";
	private String	namVersion	= "6x";
	private String	vsb;
	private String	consoleID;
	private String	enablePwd;

	public NAMInfo(String namIp, String filePath) throws DocumentException {
		System.out.println("Tested nam IP is: " + namIp);
		this.namIP = namIp;
		File xmlFile = new File(filePath);
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(xmlFile);
		Element namNode = (Element) doc.selectSingleNode("/nams/nam[@ip='" + namIp + "']");

		if (namNode == null) {
			System.out.println("Cannot find the nam ip:" + namIp + " in nam_profile.xml!");
			throw new DocumentException();
		}

		this.namType = NAMType.valueOf(namNode.attributeValue("type"));
		this.deviceIP = namNode.element("device").getTextTrim();
		this.deviceUsr = namNode.element("device").attributeValue("usr");
		this.devicePwd = namNode.element("device").attributeValue("pwd");

		if (namNode.element("device").attribute("switch_num") != null) {
			this.switchNum = namNode.element("device").attributeValue("switch_num");
		}
		if (namNode.element("device").attribute("slot") != null) {
			this.slot = namNode.element("device").attributeValue("slot");
		}
		if (namNode.element("device").attribute("port") != null) {
			this.port = namNode.element("device").attributeValue("port");
		}
		if (namNode.element("device").attribute("sm") != null) {
			this.sm = namNode.element("device").attributeValue("sm");
		}
		if (namNode.element("device").attribute("vsb") != null) {
			this.vsb = namNode.element("device").attributeValue("vsb");
		}
		if (namNode.element("device").attribute("console_ID") != null) {
			this.consoleID = namNode.element("device").attributeValue("console_ID");
		}

		if (namNode.element("device").attribute("enablePwd") != null) {
			this.enablePwd = namNode.element("device").attributeValue("enablePwd");
		} else {
			this.enablePwd = "targa";
		}
	}

	public String getNamIP() {
		return this.namIP;
	}

	public String getNamVersion() {
		return this.namVersion;
	}

	public void setNamVersion(String namVersion) {
		this.namVersion = namVersion;
	}

	public NAMType getNamType() {
		return this.namType;
	}

	public String getDeviceUsr() {
		return this.deviceUsr;
	}

	public String getDevicePwd() {
		return this.devicePwd;
	}

	public String getSwitchNum() {
		return this.switchNum;
	}

	public String getSlot() {
		return this.slot;
	}

	public String getDeviceIP() {
		return this.deviceIP;
	}

	public String getPort() {
		return this.port;
	}

	public String getSM() {
		return this.sm;
	}

	public String getVsb() {
		return vsb;
	}

	public void setVsb(String vsb) {
		this.vsb = vsb;
	}

	public String getConsoleID() {
		return consoleID;
	}

	public void setConsoleID(String consoleID) {
		this.consoleID = consoleID;
	}

	public String getEnablePwd() {
		return enablePwd;
	}

	public void setEnablePwd(String enablePwd) {
		this.enablePwd = enablePwd;
	}

	public static void main(String[] args) throws Exception {
		// NAMInfo namInfo=new NAMInfo("172.20.124.49");
		// NAMInfo namInfo=new NAMInfo("10.75.169.27");
		// NAMInfo namInfo=new NAMInfo("172.20.110.196");
		// NAMInfo namInfo=new NAMInfo("10.75.169.24");
		// NAMInfo namInfo=new NAMInfo("172.20.124.26");
		String profilePath = "./test-input/configs/nam_profile.xml";
		NAMInfo namInfo = new NAMInfo("172.20.124.117", profilePath);
		NAMConnection namConnection = new NAMConnection(namInfo);
		IConnection conn = namConnection.setupConnection();
		// IConnection conn=new TelnetConnection(namInfo.getDeviceIP(),namInfo.getDeviceUsr(),namInfo.getDevicePwd(),23);
		// conn.executeUntilPrompt(namInfo.getDeviceUsr()+"\n", "word:");
		// conn.executeUntilPrompt("lab"+"\n", "-25>");
		// conn.executeUntilPrompt("en"+"\n", "word:");
		// conn.executeUntilPrompt("lab"+"\n");
		String output = conn.executeUntilPrompt("show version", "#");
		System.out.println("=====================================");
		System.out.println(output);
		conn.execute("exit");
		conn.disconnect();
		// System.exit(0);

	}

}
