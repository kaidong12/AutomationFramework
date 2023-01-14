package com.cisco.framework.utilities;

import java.net.*;
import java.io.*;
import java.util.regex.Pattern;

/**
 * @author Francesco Ferrante<br>
 *         <br>
 * 
 *         Class <code>JSoapClient</code> is a general purpose soap client for the sending and recieving of soap requests/responses respectively.<br>
 *         The following basic actions are supported:<br>
 *         <br>
 * 
 *         1. Reading a soap xml file.<br>
 *         2. Sending a soap request.<br 3. Getting a soap response result from a soap response obtained in step 2.<br>
 *         <br>
 * 
 *         The code for <code>JSoapClient</code> was borrowed from the following source with special thanks to "Bob DuCharme"
 *         http://www.ibm.com/developerworks/xml/library/x-soapcl/index.html
 * 
 *         The sample code below illustrates the basic usage of class <code>JSoapClient</code>.<br>
 *         <br>
 * 
 *         <code>
 *   import java.net.*;<br>
 *   import java.io.*;<br>
 *   import import java.util.regex.Pattern;<br>
 *   
 *   try {<br>
 *      // "CWTTestService" is an ASP.NET Web service written with VS2008.  Web service <code>CWTTestService<code><br> 
 *      // was created to not only unit test <code>JSoapClient</code> but also to serve as demonstartion piece for the<br>
 *         // eventual testing of web services for CWT and other projects.<br>
 *         <br>
 *         // <code>CWTTestService<code> supports the following web methods:<br>
 *      <br>
 *      // 1. Add:        returns the sum of two integers.<br>
 *      // 2. Divide:     returns the quotient of two integers.<br>
 *      // 3. Multiply:   returns the product of two integers.<br>
 *      // 4. Subtract:   returns the difference of two integers.<br>
 *      // 5. HelloWorld: returns the string "Hello World".<br>
 *      // 6. Test:       returns a user-specified string.<br>
 *      // 7. Wait:       Waits for the suer-specified number of seconds and returns appropriate message.<br>
 *      // 8. GetTable:   returns the entire contents of a user-specified table from a given user-specified DBMS.<br>
 *      //<br>
 *      // NOTE: TO USE "CWTTestService.asmx" YOU MUST INSTALL VS2008 OR SOME EARLIER VERSION SUCH AS VS2005 WHICH SUPPORTS ASP.NET<br>
 *      //<br> 
 *		String soapUrl = "http://localhost:2864/CWTTestService.asmx";<br>
 *		String soapRequestXML = "C:/Automation/CWT/AddRequest.xml";<br>
 *		String soapAction = "http://tempuri.org/Add";<br>
 *		String soapResponse = "";<br>
 *		String soapResponseResult = "";<br>
 *		<br>
 *		// Read in soap request<br>
 *		String soapRequest = JSoapClient.readXMLFile(soapRequestXML);<br>
 *		<br>
 *		soapResponse = JSoapClient.sendSoapRequest(soapUrl, soapRequest.getBytes(), soapAction);<br>
 *		soapResponseResult = JSoapClient.getSoapResponseResult(soapResponse);<br>
 *		<br>		
 *		System.out.println("*** SOAP RESPONSE ***");<br>
 *		System.out.println(soapResponse);<br>
 *		System.out.println("*** SOAP RESPONSE RESULT***");<br>
 *		System.out.println(soapResponseResult);<br>
 *   } catch(Exception e) {<br>
 *     // Code to handle non-sequel execptions goes here.<br>   
 *     e.printStackTrace();<br>
 *   }<br><br>
 *  </code>
 */
public class JSoapClient {

	private static HttpURLConnection	httpConnection	= null;
	private static byte[]				xmlContent		= null;

	/**
	 * @param soapUrl
	 * @param xmlFile2Send
	 * @param soapAction
	 * @return String
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to send a soap request enclosed within a soap xml file "xmlFile2Send" to a soap url endpoint specified in "soapUrl"
	 *             <br>
	 *             using a user-specified soap action specified by "soapAction". If all goes well, a corresponding soap response is returned as a
	 *             "String"<br>
	 *             object.<br>
	 */
	public static String sendSoapRequest(String soapUrl, String xmlFile2Send, String soapAction) throws Exception {
		OutputStream out = null;
		opemHttpConnection(soapUrl);
		createSoapRequest(xmlFile2Send, soapAction);
		try {
			out = httpConnection.getOutputStream();
			out.write(xmlContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return getSoapResponse();
	}

	/**
	 * @param soapUrl
	 * @param xmlContent
	 * @param soapAction
	 * @return String
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to send a soap request "xmlContent" represented as an array of bytes to a soap url endpoint specified in "soapUrl"
	 *             <br>
	 *             using a user-specified soap action specified by "soapAction". If all goes well, a corresponding soap response is returned as a
	 *             "String"<br>
	 *             object.<br>
	 */
	public static String sendSoapRequest(String soapUrl, byte[] xmlContent, String soapAction) throws Exception {
		OutputStream out = null;
		opemHttpConnection(soapUrl);
		createSoapRequest(xmlContent, soapAction);
		try {
			out = httpConnection.getOutputStream();
			out.write(xmlContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return getSoapResponse();
	}

	/**
	 * @param soapResponse
	 * @return String
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to retrieve the result value from a soap response. Usually a call to this method is made after a<br>
	 *             call to method sendSoapRequest() as first been made.<br>
	 */
	public static String getSoapResponseResult(String soapResponse) throws Exception {
		String res = "";
		if (!soapResponse.isEmpty()) {
			// Extract contents from the "Result" xml tag
			Pattern soapResponsePattern = Pattern.compile("[><]");
			String[] soapResponseItems = soapResponsePattern.split(soapResponse);
			int index = 0;
			for (int i = 0; i < soapResponseItems.length; i++) {
				if (soapResponseItems[i].contains("Result")) {
					// Update index by adding the value of i to it
					index += i;
				}
			}
			// The value of the soap response result should always occur between the two items containing the string "Result" in the array
			// "soapResponseItems"
			// The value of "index/2" should pinpoint the exact location of the soap response result
			res = soapResponseItems[index / 2];
		}
		return res;
	}

	/**
	 * @param xmlFile2Send
	 * @return String
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this method to read in a user-specifed soap xml file specified in "xmlFile2Send".<br>
	 */
	public static String readXMLFile(String xmlFile2Send) throws Exception {
		StringBuilder xmlContent = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(xmlFile2Send));
			String line = "";
			while ((line = reader.readLine()) != null) {
				xmlContent.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return xmlContent.toString();
	}

	/**
	 * @param in
	 * @param out
	 * @throws IOException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used to copy an input stream to an output stream in a thread safe-way
	 */
	private static void copy(InputStream in, OutputStream out) throws IOException {
		// copy method from From E.R. Harold's book "Java I/O"
		// Do not allow other threads to read from the
		// input or write to the output while copying is
		// taking place
		if ((in != null) && (out != null)) {
			synchronized (in) {
				synchronized (out) {
					byte[] buffer = new byte[256];
					while (true) {
						int bytesRead = in.read(buffer);
						if (bytesRead == -1) {
							break;
						}
						out.write(buffer, 0, bytesRead);
					}
				}
			}
		}
	}

	/**
	 * @return
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method for getting a soap response.<br>
	 */
	private static String getSoapResponse() throws Exception {
		StringBuilder soapResponse = new StringBuilder();
		InputStreamReader isr = null;
		BufferedReader in = null;
		try {
			isr = new InputStreamReader(httpConnection.getInputStream());
			in = new BufferedReader(isr);
			String inputLine = "";
			while ((inputLine = in.readLine()) != null) {
				soapResponse.append(inputLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
			if (isr != null) {
				isr.close();
			}
		}
		return soapResponse.toString();
	}

	/**
	 * @param soapUrl
	 * 
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method for opening an HTTP connection.<br>
	 */
	private static void opemHttpConnection(String soapUrl) throws Exception {
		// HttpURLConnection.setFollowRedirects(true);
		// System.setProperty("http.proxyHost", "10.0.6.251");
		// System.setProperty("http.proxyPort", "3128");
		// System.setProperty("http.proxyUser", "imsspml");
		// System.setProperty("http.proxyPassword", "imsspml!qa");
		// String enconded = new BASE64Encoder().encode("imsspml:imsspml!qa".getBytes());

		Authenticator.setDefault(new JSoapClientAuthenticator("imsspml", "imsspml!qa"));
		httpConnection = (HttpURLConnection) new URL(soapUrl).openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.6.251", 3128)));

		// 172.22.174.13

		// httpConnection.setRequestProperty("Proxy-Authorization", "Basic " + enconded);
	}

	/**
	 * @param xmlContent
	 * @param soapAction
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used to create a soap request given a user-specified soap xml as an array of bytes with<br>
	 *             a corresponding user-specified soap action.<br>
	 */
	private static void createSoapRequest(byte[] xmlContent, String soapAction) throws Exception {
		// Set the appropriate HTTP parameters
		httpConnection.setRequestProperty("Content-Length", String.valueOf(xmlContent.length));
		httpConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		httpConnection.setRequestProperty("SOAPAction", soapAction);
		httpConnection.setRequestMethod("POST");
		httpConnection.setDoOutput(true);
		httpConnection.setDoInput(true);
	}

	/**
	 * @param xmlFile2Send
	 * @param soapAction
	 * @throws Exception
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Helper method used to create a soap request given a user-specified soap xml file with<br>
	 *             a corresponding user-specified soap action.<br>
	 */
	private static void createSoapRequest(String xmlFile2Send, String soapAction) throws Exception {
		FileInputStream fin = null;
		ByteArrayOutputStream bout = null;
		try {
			// Open input file and copy it to a byte array. Use the length property of the byte array to set
			// the 'Content-Length' property of the httpConnection object.
			fin = new FileInputStream(xmlFile2Send);
			bout = new ByteArrayOutputStream();
			// Copy SOAP file to the open connection
			copy(fin, bout);
			xmlContent = bout.toByteArray();
			// Set the appropriate HTTP parameters
			httpConnection.setRequestProperty("Content-Length", String.valueOf(xmlContent.length));
			httpConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpConnection.setRequestProperty("SOAPAction", soapAction);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fin != null) {
				fin.close();
			}
			if (bout != null) {
				bout.close();
			}
		}
	}
}
