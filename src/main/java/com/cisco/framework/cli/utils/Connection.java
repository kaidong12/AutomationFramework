package com.cisco.framework.cli.utils;

import java.io.InputStream;
import java.io.OutputStream;

import com.cisco.framework.utilities.logging.Log;

public abstract class Connection implements IConnection {
	protected static Log	log			= null;
	protected InputStream	in			= null;
	protected OutputStream	out			= null;
	protected String		testbedip;
	protected int			connectport	= 22;
	protected String		username;
	protected String		password;

	public Connection() {

	}

	public Connection(String ip, int port, String user, String password, Log log) throws CommandException {
		Connection.log = log;
		this.testbedip = ip;
		if (port != 0) {
			this.connectport = port;
		}
		this.username = user;
		this.password = password;

	}

	public abstract boolean login(String user, String password);

	public abstract boolean login(String user, String password, String enablePwd);

	public abstract void disconnect();

	public void execute(String cmd) throws Exception {
		try {

			System.out.println("\r\n====================================================");
			System.out.println("Sending string: " + cmd);
			System.out.println("====================================================");
			cmd += "\n";
			out.write(cmd.getBytes());
			out.flush();
			Thread.sleep(5000);
			String outputs = readTheRestAvailableStream(5);
			log.comment("execute", "Testbed: " + this.testbedip + "\nCommand: " + cmd.trim(), outputs, Log.DEBUG, Log.SCRIPT_ISSUE);

		} catch (Exception e) {
			e.printStackTrace();
			log.comment("execute", "Testbed: " + this.testbedip + "\nCommand: " + cmd.trim(), "Error Message: \n" + e.getMessage(), Log.DEFECT,
					Log.MAJOR_ISSUE);
			throw new CommandException("Failed to execute command: " + cmd.trim());

		}

	}

	public String executeUntilPrompt(String cmd) throws Exception {
		return executeUntilPrompt(cmd, "#", 90);
	}

	public String executeUntilPrompt(String cmd, String prompt) throws Exception {
		return executeUntilPrompt(cmd, prompt, 90);
	}

	public String executeUntilPrompt(String cmd, String prompt, int timeout_sec) throws Exception {
		try {
			System.out.println("\r\n====================================================");
			System.out.println("Sending string: " + cmd);
			System.out.println("====================================================");
			Thread.sleep(200);
			cmd += "\r\n";
			out.write(cmd.getBytes());
			out.flush();
			Thread.sleep(300);
			String outputs = readUntil(cmd, prompt, timeout_sec);
			log.comment("executeUntilPrompt", "Testbed: " + this.testbedip + "\nCommand: " + cmd + "Prompt: " + prompt, outputs, Log.DEBUG,
					Log.SCRIPT_ISSUE);
			return outputs;

		} catch (Exception e) {
			e.printStackTrace();
			log.comment("executeUntilPrompt", "Testbed: " + this.testbedip + "\nCommand: " + cmd + "Prompt: " + prompt,
					"Error Message: \n" + e.getMessage(), Log.DEFECT, Log.MAJOR_ISSUE);
			throw new CommandException("Failed to execute command: " + cmd.trim());
		}
	}

	public String readTheRestAvailableStream(double timeout) throws Exception {
		long rest_start = System.currentTimeMillis();
		long rest_end = System.currentTimeMillis();
		long rest_time_range = rest_end - rest_start;
		StringBuffer sb = new StringBuffer();
		char ch;

		while (rest_time_range < timeout * 1000) {
			if (in.available() > 0) {
				ch = (char) in.read();
				sb.append(ch);
			} else {
				Thread.sleep(20);
			}
			rest_end = System.currentTimeMillis();
			rest_time_range = rest_end - rest_start;
		}

		return sb.toString();
	}

	public String readUntil(String cmd, String prompt, int timeout_sec) throws Exception {
		try {

			long start = System.currentTimeMillis();
			long end = System.currentTimeMillis();
			long timeRange = end - start;
			String outputString = "";
			String nextLine = "";
			String nextLineTrim = "";
			long max_time = timeout_sec * 1000;

			while (timeRange < max_time) {// max wait time control

				nextLine = readNextLine(prompt, 10);
				nextLineTrim = nextLine.trim();
				outputString = outputString + nextLine;
				System.out.print(nextLine);

				if ("".equals(nextLine)) {
					if (outputString.trim().endsWith(prompt)) {
						System.out.print("$$$$$$$$$$$$$$$$$$$$$$$$");
						break;
					} else {
						Thread.sleep(5000);
					}

				} else {
					if (nextLineTrim.endsWith(prompt)) {// prompt met
						if (prompt.equals("#")) {// if prompt is #
							int sbLen = nextLineTrim.length();
							if (sbLen >= 2) {
								int ch_second_last = nextLineTrim.charAt(sbLen - 2);
								if (ch_second_last != 35) {// 2 ## at the end of the line means it is a process bar
									break;
								}
							} else {
								Thread.sleep(5000);
							}
						} else {
							break;
						}

					}

				}
				end = System.currentTimeMillis();
				timeRange = end - start;

			}

			if (timeRange >= max_time) {
				/**
				 * delete the failed incorrect cli to avoid it affect the following CLIs
				 */
				if (outputString.trim().endsWith("[n]:") || outputString.trim().endsWith("[n]")) {
					out.write("\n".getBytes());
					out.flush();
				} else if (!outputString.trim().endsWith("#") && outputString.contains("#")) {
					int lastNumCharIndex = outputString.lastIndexOf("#");
					int sbLen = outputString.length();
					String delExtra = "";
					for (int i = 0; i < sbLen - lastNumCharIndex; i++) {
						delExtra += "\b";
					}
					out.write(delExtra.getBytes());
					out.flush();

					if (outputString.charAt(lastNumCharIndex - 1) == ')') {
						out.write("cancel\n".getBytes());
						out.flush();
					}
				} else if (outputString.trim().endsWith(")#")) {
					out.write("cancel\n".getBytes());
					out.flush();
				}

				throw new CommandException("Timeout for cmd: " + cmd + "\n" + "expect prompt: \"" + prompt + "\"\n" + "actual output: \""
						+ outputString.trim() + "\"\n\n");
			}

			return outputString;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	public String readNextLine(String prompt, int timeout_sec) throws Exception {
		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		long timeRange = end - start;
		StringBuffer sb = new StringBuffer();
		long max_time = timeout_sec * 1000;
		int ch;

		while (timeRange < max_time) {
			if (in.available() > 0) {
				ch = in.read();
				sb.append((char) ch);
				if ((ch == 10) || (ch == 13)) {// end of line (both # and ftp download progress bar exit here)
					Thread.sleep(200);
					break;
				}
			} else {
				if (sb.toString().trim().endsWith(prompt)) {
					if (prompt.equals("#")) {// only take effect when progress bar
						int len = sb.toString().length();
						if (sb.toString().charAt(len - 1) == 32) {
							break;
						} else {
							System.out.println("sleep 4s");
							Thread.sleep(4000);
						}

					} else {// prompt other than #
						Thread.sleep(200);
						break;
					}

				}
				System.out.println("sleep 1s");
				Thread.sleep(1000);
			}
			end = System.currentTimeMillis();
			timeRange = end - start;
		}

		return sb.toString();
	}
}
