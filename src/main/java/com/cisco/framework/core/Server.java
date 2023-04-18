package com.cisco.framework.core;

import java.io.File;

import org.openqa.selenium.server.SeleniumServer;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.testng.ITestContext;

import com.cisco.framework.core.exceptions.FrameworkException;
import com.cisco.framework.utilities.logging.Log;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.HttpCommandProcessor;

/**
 * @author Lance Yan
 */
public class Server {
	private String						serverHost					= "localhost";
	private String						browserStartCommand			= "*firefox";
	private String						browserURL					= "http://blank.org/";
	private String						fireFoxProfileTemplate		= "";

	private int							serverPort					= 4444;
	private ITestContext				context						= null;
	private SeleniumServer				server						= null;
	private DefaultSelenium				selenium					= null;
	private RemoteControlConfiguration	remoteControlConfiguration	= null;

	/**
	 * USAGE: <br>
	 * <br>
	 * <p>
	 * Use this constructor to instantiate a default "Server" object.<br>
	 * For a default "Server", the settings for "serverHost", "browserStartCommand",<br>
	 * "browserURL", and "fireFoxProfileTemplate" are as follows:<br>
	 * 1. serverHost = "localhost".<br>
	 * 2. browserStartCommand = "*firefox".<br>
	 * 3. browserURL = "http://blank.org/".<br>
	 * 4. fireFoxProfileTemplate = "".<br>
	 */
	public Server() {

	}

	/**
	 * @param serverHost
	 * @param browserStartCommand
	 * @param browserURL
	 * @param fireFoxProfileTemplate
	 * @param serverPort
	 *            <br>
	 *            <br>
	 *            USAGE: <br>
	 *            <br>
	 *            <p>
	 *            Use this constructor to instantiate a "Server" object<br>
	 *            given a valid "serverHost", "browserStartCommand", "browserURL", "fireFoxProfileTemplate"<br>
	 *            and "serverPort".<br>
	 */
	public Server(String serverHost, String browserStartCommand, String browserURL, String fireFoxProfileTemplate, int serverPort) {

		if (serverHost != null) {
			if (!serverHost.isEmpty()) {
				this.serverHost = serverHost;
			}
		}

		if (browserStartCommand != null) {
			if (!browserStartCommand.isEmpty()) {
				this.browserStartCommand = browserStartCommand;
			}
		}

		if (browserURL != null) {
			if (!browserURL.isEmpty()) {
				this.browserURL = browserURL;
			}
		}

		if (fireFoxProfileTemplate != null) {
			if (!fireFoxProfileTemplate.isEmpty()) {
				this.fireFoxProfileTemplate = fireFoxProfileTemplate;
			}
		}

		if (serverPort > 0) {
			this.serverPort = serverPort;
		}
	}

	/**
	 * @param context
	 *            <br>
	 *            <br>
	 *            USAGE: <br>
	 *            <br>
	 *            <p>
	 *            Use this constructor to instantiate a "Server" object using a valid object of type "ITestContext".<br>
	 */
	public Server(ITestContext context) {

		if (context != null) {
			this.context = context;
		} else {
			throw new FrameworkException("Server", "context", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		if (context.getCurrentXmlTest().getParameter("selenium.host") != null) {
			if (!context.getCurrentXmlTest().getParameter("selenium.host").isEmpty()) {
				serverHost = context.getCurrentXmlTest().getParameter("selenium.host");
			}
		}

		if (context.getCurrentXmlTest().getParameter("selenium.browser") != null) {
			if (!context.getCurrentXmlTest().getParameter("selenium.browser").isEmpty()) {
				browserStartCommand = context.getCurrentXmlTest().getParameter("selenium.browser");
			}
		}

		if (context.getCurrentXmlTest().getParameter("selenium.url") != null) {
			if (!context.getCurrentXmlTest().getParameter("selenium.url").isEmpty()) {
				browserURL = context.getCurrentXmlTest().getParameter("selenium.url");
			}
		}

		if (context.getCurrentXmlTest().getParameter("selenium.port") != null) {
			if (!context.getCurrentXmlTest().getParameter("selenium.port").isEmpty()) {
				serverPort = Integer.parseInt(context.getCurrentXmlTest().getParameter("selenium.port"));
			}
		}

		if (context.getCurrentXmlTest().getParameter("firefox.profileTemplate") != null) {
			if (!context.getCurrentXmlTest().getParameter("firefox.profileTemplate").isEmpty()) {
				fireFoxProfileTemplate = context.getCurrentXmlTest().getParameter("firefox.profileTemplate");
			}
		}
	}

	/**
	 * @return the context <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to get a "ITestContext" object.<br>
	 */
	public ITestContext getContext() {
		return context;
	}

	/**
	 * @param context
	 * @return the Server
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE: <br>
	 *             <br>
	 *             <p>
	 *             Use this method to start and return a selenium server.<br>
	 */
	public Server start() throws Exception {
		remoteControlConfiguration = new RemoteControlConfiguration();
		remoteControlConfiguration.setSingleWindow(false);
		remoteControlConfiguration.setPort(serverPort);
		remoteControlConfiguration.setTrustAllSSLCertificates(true);
		if (!fireFoxProfileTemplate.isEmpty()) {
			remoteControlConfiguration.setFirefoxProfileTemplate(new File(fireFoxProfileTemplate));
		}
		remoteControlConfiguration.setReuseBrowserSessions(true);
		server = new SeleniumServer(false, remoteControlConfiguration);
		server.boot();
		selenium = new DefaultSelenium(new HttpCommandProcessor(serverHost, serverPort, browserStartCommand, browserURL));
		selenium.start();
		return this;
	}

	/**
	 * USAGE: <br>
	 * <br>
	 * <p>
	 * Use this method to stop a selenium server.<br>
	 */
	public void stop() {
		if (selenium != null) {
			selenium.stop();
		}
		if (server != null) {
			server.stop();
		}
	}

	/**
	 * @return the serverHost <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to get the current "Server Host" in use.<br>
	 *         The default "Server Host" is "localhost".<br>
	 */
	public String getServerHost() {
		return serverHost;
	}

	/**
	 * @param serverHost
	 *            the serverHost to set <br>
	 *            <br>
	 *            USAGE: <br>
	 *            <br>
	 *            <p>
	 *            Use this method to set the current "Server Host".<br>
	 *            The default "Server Host" is "localhost".<br>
	 */
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	/**
	 * @return the browserStartCommand <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to get the current "Browser Start Command" in use.<br>
	 *         The default "Browser Start Command" is "*firefox".<br>
	 */
	public String getBrowserStartCommand() {
		return browserStartCommand;
	}

	/**
	 * @param browserStartCommand
	 *            the browserStartCommand to set <br>
	 *            <br>
	 *            USAGE: <br>
	 *            <br>
	 *            <p>
	 *            Use this method to set the current "Browser Start Command".<br>
	 *            The default "Browser Start Command" is "*firefox".<br>
	 */
	public void setBrowserStartCommand(String browserStartCommand) {
		if (browserStartCommand != null) {
			if (!browserStartCommand.isEmpty()) {
				if (browserStartCommand.startsWith("*")) {
					this.browserStartCommand = browserStartCommand;
				} else {
					this.browserStartCommand = "*" + browserStartCommand;
				}
			}
		}
	}

	/**
	 * @return the browserURL <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to get the current "Browser URL" in use.<br>
	 *         The default "Browser URL" is "http://blank.org/".<br>
	 */
	public String getBrowserURL() {
		return browserURL;
	}

	/**
	 * @param browserURL
	 *            the browserURL to set <br>
	 *            <br>
	 *            USAGE: <br>
	 *            <br>
	 *            <p>
	 *            Use this method to set the current "Browser URL".<br>
	 *            The default "Browser URL" is "http://blank.org/".<br>
	 */
	public void setBrowserURL(String browserURL) {
		this.browserURL = browserURL;
	}

	/**
	 * @return the fireFoxProfileTemplate <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to get the current "FireFox Profile Template" in use.<br>
	 *         The default "FireFox Profile Template" is an empty string.<br>
	 */
	public String getFireFoxProfileTemplate() {
		return fireFoxProfileTemplate;
	}

	/**
	 * @param fireFoxProfileTemplate
	 *            the fireFoxProfileTemplate to set <br>
	 *            <br>
	 *            USAGE: <br>
	 *            <br>
	 *            <p>
	 *            Use this method to set the current "FireFox Profile Template".<br>
	 *            The default "FireFox Profile Template" is an empty string.<br>
	 */
	public void setFireFoxProfileTemplate(String fireFoxProfileTemplate) {
		this.fireFoxProfileTemplate = fireFoxProfileTemplate;
	}

	/**
	 * @return the serverPort <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to get the current "Server Port" in use.<br>
	 *         The default "Server Port" is "4444".<br>
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort
	 *            the serverPort to set <br>
	 *            <br>
	 *            USAGE: <br>
	 *            <br>
	 *            <p>
	 *            Use this method to set the current "Server Port".<br>
	 *            The default "Server Port" is "4444".<br>
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * @return the server <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to get the current "Selenium Serve" in use.<br>
	 *         This method is used if special settings need to be made to the<br>
	 *         "Selenium Server" before actually starting it.<br>
	 */
	public SeleniumServer getSeleniumServer() {
		return server;
	}

	/**
	 * @return the selenium <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to get the current "Default Selenium" object in use.<br>
	 *         It's this object that will be used in the "WebApp" object to send all browser "action"<br>
	 *         commands to the "Selenium Server".<br>
	 */
	public DefaultSelenium getDefaultSelenium() {
		return selenium;
	}

	/**
	 * @return the remoteControlConfiguration <br>
	 *         <br>
	 *         USAGE: <br>
	 *         <br>
	 *         <p>
	 *         Use this method to get the current "Remote Control Configuration" object in use.<br>
	 *         This method is used if special settings need to be made to the<br>
	 *         "Remote Control Configuration" object before actually starting the "Selenium Server".<br>
	 */
	public RemoteControlConfiguration getRemoteControlConfiguration() {
		return remoteControlConfiguration;
	}
}