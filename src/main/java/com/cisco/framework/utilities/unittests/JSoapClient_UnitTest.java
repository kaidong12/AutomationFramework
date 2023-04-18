package com.cisco.framework.utilities.unittests;

import java.util.Random;

import com.cisco.framework.utilities.JSoapClient;
import com.cisco.framework.utilities.logging.Log;


/**
 * @author Lance Yan
 *
 */
public class JSoapClient_UnitTest {

	private static Log log               = null;
	private static String soapUrl        = "http://localhost:2864/CWTTestService.asmx";
	private static String soapConnectUrl = "https://ws.messaging.qa.covisint.com/webservices-gateway/service/connect";
	
	private static String soapTerraServiceUrl = "http://terraservice.net/terraservice.asmx";
	public static void main(String[] args) {
		log = new Log("C:\\TestResults\\CWTTestService.html");
		try {
			System.out.println("EXECUTING TEST: 'testWebMethodHelloWorld'");
			testWebMethodHelloWorld();
//			
//			System.out.println("EXECUTING TEST: 'testWebMethodTest'");
//			testWebMethodTest();
//			
//			System.out.println("EXECUTING TEST: 'testWebMethodWait'");
//			testWebMethodWait(10);
//			
//			System.out.println("EXECUTING TEST: 'testWebMethodAdd'");
//			testWebMethodAdd(numberOfIntegers, maximumInteger);
//			
//			System.out.println("EXECUTING TEST: 'testWebMethodSubtract'");
//			testWebMethodSubtract(numberOfIntegers, maximumInteger);
//			
//			System.out.println("EXECUTING TEST: 'testWebMethodMultiply'");
//			testWebMethodMultiply(numberOfIntegers, maximumInteger);
//			
//			System.out.println("EXECUTING TEST: 'testWebMethodDivide'");
//			testWebMethodDivide(numberOfIntegers, maximumInteger);
			

			testSyncSpmlAdd();

//			testTerraServiceGetPlaceFacts();
			
			System.out.println("*** UNIT TESTING COMPLETED ***");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static void testWebMethodHelloWorld() throws Exception {
		String soapRequestXML     = "C:/Automation/CWT/HelloWorldRequest.xml";
		String soapAction         = "http://tempuri.org/HelloWorld";
		String soapResponse       = "";
		String soapResponseResult = "";
		try {
			log.startTestExecution("UNIT TEST 1: WEB METHOD 'HelloWorld'");
			soapResponse = JSoapClient.sendSoapRequest(soapUrl, soapRequestXML, soapAction);
			soapResponseResult = JSoapClient.getSoapResponseResult(soapResponse);
			log.comment(soapResponse);
			log.verifyStep(soapResponseResult.equals("Hello World"), 
					"verifyWebMethodHelloWorld", 
					"Web Method 'HelloWorld' returned correct message", 
					"", 
					Log.SCRIPT_ISSUE);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			log.endTestExecution();
		}
	}
	
	public static void testWebMethodTest() throws Exception {
		String soapRequestXML     = "C:/Automation/CWT/TestRequest.xml";
		String soapAction         = "http://tempuri.org/Test";
		String soapResponse       = "";
		String soapResponseResult = "";
		try {
			log.startTestExecution("UNIT TEST 2: WEB METHOD 'Test'");
			String soapRequest = JSoapClient.readXMLFile(soapRequestXML);
			String testInput = "This is a test.";
			soapResponse = JSoapClient.sendSoapRequest(soapUrl, soapRequest.replace("arg", testInput).getBytes(), soapAction);
			soapResponseResult = JSoapClient.getSoapResponseResult(soapResponse);
			log.comment(soapResponse);
			log.verifyStep(soapResponseResult.equals(testInput), 
					"verifyWebMethodTest", 
					"Web Method 'Test' returned correct message", 
					"", 
					Log.SCRIPT_ISSUE);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			log.endTestExecution();
		}
	}
	
	public static void testWebMethodWait(int testInputInSeconds) throws Exception {
		String soapRequestXML     = "C:/Automation/CWT/WaitRequest.xml";
		String soapAction         = "http://tempuri.org/Wait";
		String soapResponse       = "";
		String soapResponseResult = "";
		try {
			log.startTestExecution("UNIT TEST 3: WEB METHOD 'Wait'");
			String soapRequest = JSoapClient.readXMLFile(soapRequestXML);
			soapResponse = JSoapClient.sendSoapRequest(soapUrl, soapRequest.replace("arg", String.valueOf(testInputInSeconds)).getBytes(), soapAction);
			soapResponseResult = JSoapClient.getSoapResponseResult(soapResponse);
			log.comment(soapResponse);
			log.verifyStep(soapResponseResult.equals("Processing time in seconds: " + "'" + String.valueOf(testInputInSeconds) + "'"), 
					"verifyWebMethodWait", 
					"Web Method 'Wait' returned correct message", 
					"", 
					Log.SCRIPT_ISSUE);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			log.endTestExecution();
		}
	}
	
	public static void testWebMethodAdd(int numberOfIntegers, int maximumInteger) throws Exception {
		String soapRequestXML     = "C:/Automation/CWT/AddRequest.xml";
		String soapAction         = "http://tempuri.org/Add";
		String soapResponse       = "";
		String soapResponseResult = "";
		int[] arg1 = getUniformlyDistributedRandomIntegers(numberOfIntegers, maximumInteger);
		int[] arg2 = getUniformlyDistributedRandomIntegers(numberOfIntegers, maximumInteger);
		String soapRequest = JSoapClient.readXMLFile(soapRequestXML);
		
		for(int i = 0; i < numberOfIntegers; i++) {
			try {
				log.startTestExecution("UNIT TEST 4: WEB METHOD 'Add'");
				soapResponse = JSoapClient.sendSoapRequest(soapUrl, soapRequest.replace("arg1", String.valueOf(arg1[i])).replace("arg2", String.valueOf(arg2[i])).getBytes(), soapAction);
				soapResponseResult = JSoapClient.getSoapResponseResult(soapResponse);
				log.comment(soapResponse);
				log.verifyStep(Integer.parseInt(soapResponseResult) == arg1[i] + arg2[i], 
						"verifyWebMethodAdd", 
						"Web Method 'Add' returned correct sum", 
						"", 
						Log.SCRIPT_ISSUE);
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				log.endTestExecution();
			}
		}
	}
	
	public static void testWebMethodSubtract(int numberOfIntegers, int maximumInteger) throws Exception {
		String soapRequestXML     = "C:/Automation/CWT/SubtractRequest.xml";
		String soapAction         = "http://tempuri.org/Subtract";
		String soapResponse       = "";
		String soapResponseResult = "";
		int[] arg1 = getUniformlyDistributedRandomIntegers(numberOfIntegers, maximumInteger);
		int[] arg2 = getUniformlyDistributedRandomIntegers(numberOfIntegers, maximumInteger);
		String soapRequest = JSoapClient.readXMLFile(soapRequestXML);
		
		for(int i = 0; i < numberOfIntegers; i++) {
			try {
				log.startTestExecution("UNIT TEST 5: WEB METHOD 'Subtract'");
				soapResponse = JSoapClient.sendSoapRequest(soapUrl, soapRequest.replace("arg1", String.valueOf(arg1[i])).replace("arg2", String.valueOf(arg2[i])).getBytes(), soapAction);
				soapResponseResult = JSoapClient.getSoapResponseResult(soapResponse);
				log.comment(soapResponse);
				log.verifyStep(Integer.parseInt(soapResponseResult) == arg1[i] - arg2[i], 
						"testWebMethodSubtract", 
						"Web Method 'Subtract' returned correct difference", 
						"", 
						Log.SCRIPT_ISSUE);
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				log.endTestExecution();
			}
		}
	}
	
	public static void testWebMethodMultiply(int numberOfIntegers, int maximumInteger) throws Exception {
		String soapRequestXML     = "C:/Automation/CWT/MultiplyRequest.xml";
		String soapAction         = "http://tempuri.org/Multiply";
		String soapResponse       = "";
		String soapResponseResult = "";
		int[] arg1 = getUniformlyDistributedRandomIntegers(numberOfIntegers, maximumInteger);
		int[] arg2 = getUniformlyDistributedRandomIntegers(numberOfIntegers, maximumInteger);
		String soapRequest = JSoapClient.readXMLFile(soapRequestXML);
		
		for(int i = 0; i < numberOfIntegers; i++) {
			try {
				log.startTestExecution("UNIT TEST 6: WEB METHOD 'Multiply'");
				soapResponse = JSoapClient.sendSoapRequest(soapUrl, soapRequest.replace("arg1", String.valueOf(arg1[i])).replace("arg2", String.valueOf(arg2[i])).getBytes(), soapAction);
				soapResponseResult = JSoapClient.getSoapResponseResult(soapResponse);
				log.comment(soapResponse);
				log.verifyStep(Integer.parseInt(soapResponseResult) == arg1[i] * arg2[i], 
						"verifyWebMethodMultiply", 
						"Web Method 'Multiply' returned correct product", 
						"", 
						Log.SCRIPT_ISSUE);
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				log.endTestExecution();
			}
		}
	}
	
	public static void testWebMethodDivide(int numberOfIntegers, int maximumInteger) throws Exception {
		String soapRequestXML     = "C:/Automation/CWT/DivideRequest.xml";
		String soapAction         = "http://tempuri.org/Divide";
		String soapResponse       = "";
		String soapResponseResult = "";
		int[] arg1 = getUniformlyDistributedRandomIntegers(numberOfIntegers, maximumInteger);
		int[] arg2 = getUniformlyDistributedRandomIntegers(numberOfIntegers, maximumInteger);
		String soapRequest = JSoapClient.readXMLFile(soapRequestXML);
		
		for(int i = 0; i < numberOfIntegers; i++) {
			if(arg2[i] == 0) {
				arg2[i] = 1;
			}
			try {
				log.startTestExecution("UNIT TEST 7: WEB METHOD 'Divide'");
				soapResponse = JSoapClient.sendSoapRequest(soapUrl, soapRequest.replace("arg1", String.valueOf(arg1[i])).replace("arg2", String.valueOf(arg2[i])).getBytes(), soapAction);
				soapResponseResult = JSoapClient.getSoapResponseResult(soapResponse);
				log.comment(soapResponse);
				log.verifyStep(Double.parseDouble(soapResponseResult) == ((double)arg1[i] / (double)arg2[i]), 
						"verifyWebMethodDivide", 
						"Web Method 'Divide' returned correct quotient", 
						"", 
						Log.SCRIPT_ISSUE);
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				log.endTestExecution();
			}
		}
	}
	
	public static void testSyncSpmlAdd() throws Exception {
		String soapRequestXML     = "D:/Covisint/CWT/CWT-SOAPUI/SyncSpmlAdd.xml";
		String soapAction         = "urn:Connect:2010:SyncSpmlAdd";
		
		String soapResponse       = JSoapClient.sendSoapRequest(soapConnectUrl, soapRequestXML, soapAction);
		System.out.println(soapResponse);
		
	}
	
	public static void testTerraServiceGetPlaceFacts() throws Exception {
		String soapRequestXML     = "C:/Automation/CWT/TerraService_GetPlaceFacts.xml";
		String soapAction         = "http://terraserver-usa.com/terraserver/GetPlaceFacts";
		
		
		String soapResponse       = JSoapClient.sendSoapRequest(soapTerraServiceUrl, soapRequestXML, soapAction);
		System.out.println(soapResponse);
		
	}
	
	private static int[] getUniformlyDistributedRandomIntegers(int numberOfIntegers, int maximumInteger) throws Exception {
		int[] res = new int[numberOfIntegers];
		Random randomIntegerGenerator = new Random();
		for(int i = 0; i < numberOfIntegers; i++ ) {
			res[i] = randomIntegerGenerator.nextInt(maximumInteger);
		}
		return res;
	}
}
