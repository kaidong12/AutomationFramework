package com.cisco.framework.cli.utils;

public interface IConnection {

	public void execute(String cmd) throws Exception;

	public String executeUntilPrompt(String cmd) throws Exception;

	public String executeUntilPrompt(String cmd, String prompt) throws Exception;

	public String executeUntilPrompt(String cmd, String prompt, int timeout_sec) throws Exception;

	public boolean login(String user, String password);

	public boolean login(String user, String password, String enablePwd);

	public void disconnect();

	public String readTheRestAvailableStream(double timeout) throws Exception;

}
