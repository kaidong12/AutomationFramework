package com.cisco.framework.cli.utils;

import com.cisco.framework.utilities.logging.Log;

public class NAMConnection {
	private NAMInfo	namInfo;
	protected Log	log	= null;

	public NAMConnection(NAMInfo namInfo) {
		this.namInfo = namInfo;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public IConnection setupConnection() throws Exception {
		IConnection connection = null;

		NAMType namType = namInfo.getNamType();
		switch (namType) {
		case RHEL:
			log.comment("Setup a SSH connect with Linux box: " + namInfo.getDeviceIP());
			connection = new SSHConnection(namInfo.getDeviceIP(), 22, namInfo.getDeviceUsr(), namInfo.getDevicePwd(), log);
			if (!connection.login(namInfo.getDeviceUsr(), namInfo.getDevicePwd())) {
				connection.disconnect();
				throw new CommandException("Failed to set up SSH connection with RHEL box " + namInfo.getDeviceIP());
			}
			connection.readTheRestAvailableStream(3);
			connection.executeUntilPrompt("");
			connection.executeUntilPrompt("w");
			break;
		case APP_2320:
		case APP_2304:
		case APP_2420:
		case APP_2440:
			log.comment("Setup a SSH connect with NAM appliance: " + namInfo.getDeviceIP());
			connection = new SSHConnection(namInfo.getDeviceIP(), 22, namInfo.getDeviceUsr(), namInfo.getDevicePwd(), log);
			if (!connection.login(namInfo.getDeviceUsr(), namInfo.getDevicePwd())) {
				connection.disconnect();
				throw new CommandException("Failed to set up telnet connection with device " + namInfo.getDeviceIP());
			}
			connection.readTheRestAvailableStream(3);
			connection.executeUntilPrompt("");
			connection.executeUntilPrompt("connect host", "Exit the session");
			connection.executeUntilPrompt("", "login:");
			connection.executeUntilPrompt("root", "word:");
			connection.executeUntilPrompt("root");
			break;

		case NAM_3:
			log.comment("Setup a TELNET connect with NAM-3 testbed: " + namInfo.getDeviceIP());
			connection = new TelnetConnection(namInfo.getDeviceIP(), 23, namInfo.getDeviceUsr(), namInfo.getDevicePwd(), log);
			// if (!connection.login(namInfo.getDeviceUsr(), namInfo.getDevicePwd())) {
			if (!connection.login(namInfo.getDeviceUsr(), namInfo.getDevicePwd(), "targa")) {
				connection.disconnect();
				throw new CommandException("Failed to set up telnet connection with device " + namInfo.getDeviceIP());
			}
			String cmd = "session";
			if (!namInfo.getSwitchNum().equals("-1")) {
				cmd += " switch " + namInfo.getSwitchNum();
			}
			cmd += " slot " + namInfo.getSlot() + " processor 1";
			connection.executeUntilPrompt(cmd, "login:");
			connection.executeUntilPrompt("root", ":");
			connection.executeUntilPrompt("root", "#");
			break;

		case SMSRE_710:
		case SMSRE_910:
		case SMSRE_700:
		case SMSRE_900:
			log.comment("Setup a TELNET connect with SME-SRE testbed: " + namInfo.getDeviceIP());
			connection = new TelnetConnection(namInfo.getDeviceIP(), 23, namInfo.getDeviceUsr(), namInfo.getDevicePwd(), log);
			if (!connection.login(namInfo.getDeviceUsr(), namInfo.getDevicePwd(), namInfo.getEnablePwd())) {
				connection.disconnect();
				throw new CommandException("Failed to set up telnet connection with device " + namInfo.getDeviceIP());
			}
			cmd = "service-module sm " + namInfo.getSM() + " session";
			connection.executeUntilPrompt(cmd + " clear", "confirm]");
			connection.executeUntilPrompt("");
			connection.executeUntilPrompt(cmd, "Open");
			connection.executeUntilPrompt("", "login:");
			connection.executeUntilPrompt("root", ":");
			connection.executeUntilPrompt("root");
			break;

		case APP_2220:
		case APP_2204:
			log.comment("Setup a TELNET connect with NAM appliance: " + namInfo.getDeviceIP());
			connection = new TelnetConnection(namInfo.getDeviceIP(), Integer.parseInt(namInfo.getPort()), namInfo.getDeviceUsr(),
					namInfo.getDevicePwd(), log);
			// connection=new TelnetConnection(namInfo.getNamIP(),"root","root",23);
			Thread.sleep(500);
			connection.executeUntilPrompt("", "login:");
			connection.executeUntilPrompt(namInfo.getDeviceUsr(), "word:");
			connection.executeUntilPrompt(namInfo.getDevicePwd());
			Thread.sleep(500);
			// if (!connection.login(namInfo.getDeviceUsr(), namInfo.getDevicePwd())) {
			// connection.disconnect();
			// throw new CommandException("Failed to set up telnet connection with device " + namInfo.getDeviceIP());
			// }
			break;

		case AGNI:
			log.comment("Setup a TELNET connect with AGNI testbed: " + namInfo.getDeviceIP());
			connection = new TelnetConnection(namInfo.getDeviceIP(), 23, namInfo.getDeviceUsr(), namInfo.getDevicePwd(), log);
			if (!connection.login(namInfo.getDeviceUsr(), namInfo.getDevicePwd())) {
				// if (!connection.login(namInfo.getDeviceUsr(), namInfo.getDevicePwd(),"")){
				connection.disconnect();
				throw new CommandException("Failed to set up telnet connection with device " + namInfo.getDeviceIP());
			}
			cmd = "attach module " + namInfo.getSlot() + " p 1";
			connection.executeUntilPrompt(cmd, "login:");
			connection.executeUntilPrompt("root", ":");
			connection.executeUntilPrompt("root");
			break;

		case VSB_N1110:
		case VSB_N1010:
			log.comment("Setup a SSH connect with VSB testbed: " + namInfo.getDeviceIP());
			connection = new SSHConnection(namInfo.getDeviceIP(), 22, namInfo.getDeviceUsr(), namInfo.getDevicePwd(), log);
			// connection.executeUntilPrompt("", "#");
			// Thread.sleep(10000);
			// connection.executeUntilPrompt(namInfo.getDeviceUsr(), ":");
			// Thread.sleep(2000);
			// connection.executeUntilPrompt(namInfo.getDevicePwd(), "#");
			Thread.sleep(10000);
			cmd = "login virtual-service-blade  " + namInfo.getVsb();
			connection.executeUntilPrompt(cmd, "Escape");
			System.out.println("escape character.....");
			Thread.sleep(3000);
			connection.executeUntilPrompt("", "login:");
			connection.executeUntilPrompt("root", "Password:");
			connection.executeUntilPrompt("root", "#");

			// connection = differentSituation("", connection, 10);
			connection.executeUntilPrompt("show ip", "#");
			// connection.execute("");
			// connection = differentSituation("",connection,5);
			break;

		case vNAM_ESXi:
		case vNAM_UCSE:
			log.comment("Setup a TELNET connect with ESXi/UCSE testbed: " + namInfo.getDeviceIP());
			try {
				for (int i = 0; i < 5; i++) {
					connection = new TelnetConnection(namInfo.getDeviceIP(), Integer.parseInt(namInfo.getPort()), namInfo.getDeviceUsr(),
							namInfo.getDevicePwd(), log);
					Thread.sleep(5000);
					connection.execute("");
					Thread.sleep(10000);
					connection.executeUntilPrompt("root", "Password:");
					Thread.sleep(1000);
					connection.executeUntilPrompt("root", "#");
					Thread.sleep(1000);
					break;
				}

			} catch (Exception e) {
				;
			} finally {
				;
			}
			break;

		case vNAM_KVM:
			log.comment("Setup a SSH connect with KVM testbed: " + namInfo.getDeviceIP());
			try {
				for (int i = 0; i < 5; i++) {
					connection = new SSHConnection(namInfo.getDeviceIP(), 22, namInfo.getDeviceUsr(), namInfo.getDevicePwd(), log);
					Thread.sleep(1000);
					connection.executeUntilPrompt("virsh", "virsh #");
					Thread.sleep(1000);
					cmd = "console  " + namInfo.getConsoleID();
					connection.executeUntilPrompt(cmd, "^]");
					Thread.sleep(1000);
					connection.executeUntilPrompt("", "login:");
					connection.executeUntilPrompt("root", "Password:");
					connection.executeUntilPrompt("root", "#");
					Thread.sleep(2000);
					break;

				}

			} catch (Exception e) {
				;
			} finally {
				;
			}
			break;

		default:
			log.comment("Cannot set ssh/telnet connection for nam: " + namInfo.getNamIP() + " on device: " + namInfo.getDeviceIP());
			System.out.println("Cannot set ssh/telnet connection for nam: " + namInfo.getNamIP() + " on device: " + namInfo.getDeviceIP());
			throw new Exception();
		}

		return connection;
	}
}
