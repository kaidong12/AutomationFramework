package com.cisco.framework.cli.utils;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cisco.framework.utilities.logging.Log;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHConnection extends Connection {
	private static Session		session			= null;
	private static Channel		channel			= null;
	private final StringBuffer	errorMessage	= new StringBuffer();

	public SSHConnection(String ip, int port, String user, String password, Log log) throws Exception {
		super(ip, port, user, password, log);
		this.init();
	}

	private void init() throws Exception {
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(this.username, this.testbedip, 22);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setPassword(this.password);
			session.connect();

			channel = (ChannelShell) session.openChannel("shell");
			channel.connect();
			in = channel.getInputStream();
			out = channel.getOutputStream();

		} catch (JSchException jsche) {
			System.err.println(jsche.getLocalizedMessage());
			throw jsche;
		}
	}

	public String getCurrentConnection() {
		return this.testbedip;
	}

	public void disconnect() {
		try {
			in.close();
			out.close();
			channel.disconnect();
			session.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

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

	public boolean login(String user, String password) {
		System.out.println("***********no need to login for ssh connection********");
		return true;
	}

	public boolean login(String user, String password, String enablePwd) {
		System.out.println("***********no need to implement for ssh connection******");
		return true;
	}

}
