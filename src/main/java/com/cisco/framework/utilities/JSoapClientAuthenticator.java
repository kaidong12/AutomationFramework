package com.cisco.framework.utilities;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

//import sun.misc.BASE64Encoder;

/**
 * @author Lance Yan
 *
 */
public class JSoapClientAuthenticator extends Authenticator {

	private String	username	= "";
	private String	password	= "";

	public JSoapClientAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		PasswordAuthentication pa = null;
		if (this.getRequestingProtocol().equalsIgnoreCase("https")) {
			pa = new PasswordAuthentication(username, password.toCharArray());
		}
		return pa;
	}

}
