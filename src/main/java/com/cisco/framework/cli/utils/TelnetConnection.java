package com.cisco.framework.cli.utils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.telnet.TelnetClient;

import com.cisco.framework.utilities.logging.Log;


public class TelnetConnection extends Connection {
	private TelnetClient		telnet					= new TelnetClient();;
	private int					waitCharNumber			= 10000;
	private int					sleeptimeBeforeLogin	= 50;								// ms
	private boolean				connected				= true;
	public static final String	CONNECT_ERROR_MESSAGE	= "ERROR: telnet connect problem";
	public static final String	CONNECTED				= "Telnet is connected";

	public TelnetConnection(String ip, int port, String user, String password, Log log) throws CommandException {
		super(ip, port, user, password, log);

		try {
			System.out.println("\nIP=" + ip + ":");
			telnet.setConnectTimeout(60000);
			telnet.connect(this.testbedip, this.connectport);
			in = telnet.getInputStream();
			out = telnet.getOutputStream();
			// out = new PrintStream(telnet.getOutputStream());

			System.out.println("Before login(user, password); sleep " + sleeptimeBeforeLogin + "(ms)...");
			try {
				Thread.sleep(sleeptimeBeforeLogin);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			System.out.println("\nIP=" + ip);
			e.printStackTrace();
			connected = false;
			throw new CommandException("Failed to set up telnet connection with device " + ip, e);
		}
	}

	public boolean login(String user, String password, String enablePwd) {
		if (!user.equals("")) {
			try {
				executeUntilPrompt("", ":");
				executeUntilPrompt(user, ":");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		write(password);

		preDeal(" ", enablePwd);

		return true;

	}

	public void preDeal(String cmd, String enablePwd) {
		try {
			String result = enableNAM(cmd, 30);
			Thread.sleep(500);
			if (result.endsWith(">")) {
				executeUntilPrompt("en", "word:", 30);
				executeUntilPrompt(enablePwd, "#", 30);
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String enableNAM(String cmd, int number) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			// rh]:|login:
			cmd += "\n";

			out.write(cmd.getBytes());
			out.flush();

			long start = System.currentTimeMillis();
			long end = System.currentTimeMillis();
			long timeRange = end - start;
			long max_time = number * 1000;
			if (number == 1) {
				max_time = 6000000;
			}

			char ch = (char) in.read();

			while (timeRange < max_time) {
				sb.append(ch);

				if (sb.toString().endsWith(">") || sb.toString().endsWith("#"))
					break;
				ch = (char) in.read();
				System.out.print(ch);
				end = System.currentTimeMillis();
				timeRange = end - start;

			}
			if (timeRange >= max_time) {
				throw new CommandException("time out for enable command: " + cmd);
			}
			return sb.toString();
		} catch (Exception e) {
			throw e;
		}

	}

	public boolean login(String user, String password) {

		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (waitFor(":", waitCharNumber) == CONNECT_ERROR_MESSAGE) {
			return false;
		}

		write(user);
		//
		if (waitFor(":", waitCharNumber) == CONNECT_ERROR_MESSAGE) {
			return false;
		}
		write(password);

		// try {
		// executeUntilPrompt(password,"#",120);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		if (enableUser() != CONNECTED) {
			return false;
		}
		return true;
	}

	public String waitFor(String pattern) {
		return waitFor(pattern, waitCharNumber);
	}

	public DeviceStatus getStatusAfterLogin() {
		StringBuffer sb = new StringBuffer();
		try {

			char ch = (char) in.read();
			sb.append(ch);
			while (ch != -1 || sb.toString().endsWith("name:") || sb.toString().endsWith("word:")) {
				ch = (char) in.read();
				if (sb.toString().endsWith("name:") || sb.toString().endsWith("word:"))
					break;
				sb.append(ch);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		String output = sb.toString().toLowerCase();
		System.out.println();
		System.out.println("-----output after login----");
		System.out.println(output);
		System.out.println("-----end----");
		System.out.println();
		if (output.contains("username:")) {
			return DeviceStatus.USERNAME;
		}

		if (output.contains("password:")) {
			return DeviceStatus.PASSWORD;
		}

		if (output.contains(">")) {
			return DeviceStatus.ENABLE_PWD;
		}

		return DeviceStatus.UNKNOWN;

	}

	public String enableUser() {

		write("\n");
		write("\n");
		StringBuffer sb = new StringBuffer();
		try {

			char ch = (char) in.read();
			sb.append(ch);
			while (ch != -1) {
				if (ch == '#') {
					return CONNECTED;
				}
				if (ch == '>') {
					write("en");
					write("targa");
				}
				ch = (char) in.read();
				sb.append(ch);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(sb.toString());
		return null;

	}

	public String enableUser(String enablePwd) {

		write("\n");
		write("\n");
		StringBuffer sb = new StringBuffer();
		try {

			char ch = (char) in.read();
			sb.append(ch);
			while (ch != -1) {
				if (ch == '#') {
					return CONNECTED;
				}
				if (ch == '>') {
					write("en");
					write(enablePwd);
				}
				ch = (char) in.read();
				sb.append(ch);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(sb.toString());
		return null;

	}

	public String waitFor(String pattern, long waitCharNumber) {
		try {
			char lastChar = pattern.charAt(pattern.length() - 1);
			StringBuffer sb = new StringBuffer();

			char ch = (char) in.read();
			while (ch != -1) {
				sb.append(ch);
				if (ch == lastChar) {
					if (sb.toString().endsWith(pattern))
						return CONNECTED;
				}

				if (sb.length() > waitCharNumber) {
					System.out.println("\n" + CONNECT_ERROR_MESSAGE + "-- not get expected char (" + pattern + ") after check " + waitCharNumber
							+ " received chars");
					return CONNECT_ERROR_MESSAGE;
				}
				ch = (char) in.read();
				System.out.print(ch);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void write(String value) {
		try {
			value += "\n";
			out.write(value.getBytes());
			out.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			if (connected) {
				telnet.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public int getWaitCharNumber() {
		return waitCharNumber;
	}

	public void setWaitCharNumber(int waitCharNumber) {
		this.waitCharNumber = waitCharNumber;
	}

	public boolean searchIP(String nam_ip, String content) {
		Pattern pattern = Pattern.compile(nam_ip);
		Matcher matcher = pattern.matcher(content);
		boolean flag = false;
		while (matcher.find()) {
			flag = true;
		}
		return flag;

	}

	public String ftpDownloadCheck() throws IOException {
		StringBuffer sb = new StringBuffer();
		char ch = (char) in.read();
		char lastChar = ']';
		while (ch != lastChar) {

			sb.append(ch);
			ch = (char) in.read();
		}
		sb.append(ch);
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception{
//		TelnetConnection t = new TelnetConnection("10.79.46.21","root","root",23);
		Log log = new Log("C:\\TestResults\\log.html");
		
		log.startTestExecution("main");
		log.startFunction("tesst");
		TelnetConnection t = new TelnetConnection("10.79.46.64",23,"root","root",log);
		t.login("root", "root");
		t.readTheRestAvailableStream(3);
		//t.executeUntilPrompt("show clock details", "#");

	
		
	//	System.out.println(t.executeUntilPrompt("show clock details", "#"));
		
		Thread.sleep(5000);
		t.readTheRestAvailableStream(3);
		t.executeUntilPrompt("show version", "#");

				
		t.execute("exit");

		log.endFunction();
		log.endTestExecution();
	}

}
