/**
 * 
 */
package com.cisco.framework.utilities.unittests;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.cisco.framework.utilities.JDatabase;
import com.cisco.framework.utilities.logging.Log;


/**
 * @author Lance Yan
 *
 */
public class JDatabase_UnitTest {
	
	private Log log            = null;
	private JDatabase db       = null;
	
	public JDatabase_UnitTest(String testResults) {
		log = new Log(testResults);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dbUrl = "jdbc:oracle:thin:@localhost:9001:cccwtqa";
		String userName = "crs";
		String password = "crs";
		new JDatabase_UnitTest("/TestResults/JDatabase_UnitTest.html").runQueryTest(dbUrl, userName, password);
	}
	
	public void runQueryTest(String dbUrl, String userName, String password ) {
		final String OPEN_INVITATIONS_TABLE = "OPEN_INVITATIONS";
		final String EMAIL_LOG_TABLE        = "EMAIL_LOG";
		final String SCHEMA                 = "CRS";
		try {
			log.startTestExecution("runQueryTest");
			db = new JDatabase(log,dbUrl,userName,password);
			db.openConnection();
			this.displayTable(OPEN_INVITATIONS_TABLE, SCHEMA);
			this.displayTable(EMAIL_LOG_TABLE, SCHEMA);
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		} catch(Exception e) {
			log.exception(e);
		} finally {
			db.closeConnection();
			log.endTestExecution();
		}
	}
	
	private void displayTable(String table, String schema) throws NullPointerException,IllegalArgumentException,SQLException, InterruptedException {
		
		if(table == null) {
			throw new NullPointerException("TABLE: " + "'" + table + "' MAKES REFERENCE TO A NULL POINTER");
		}
		
		if(table.isEmpty()) {
			throw new IllegalArgumentException("TABLE: " + "'" + table + "' MAKES REFERENCE TO AN EMPTY STRING");
		}
		
		if(schema == null) {
			throw new NullPointerException("SCHEMA: " + "'" + schema + "' MAKES REFERENCE TO A NULL POINTER");
		}
		
		if(schema.isEmpty()) {
			throw new IllegalArgumentException("SCHEMA: " + "'" + schema + "' MAKES REFERENCE TO AN EMPTY STRING");
		}
		
		ResultSet rs = (ResultSet)db.executeQuery("SELECT * FROM " + schema + "." + table);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		
		System.out.println("START TABLE: " + "'" + table + "'");
		System.out.println();
		
		// Iterate through all columns in table
		String column = "";
		for(int c = 1; c <= columnCount; c++) {
			Thread.sleep(100);
			column = rsmd.getColumnLabel(c);
			System.out.println("START COLUMN: " + "'" + column + "'");
			System.out.println();
			
			int row = 1;
			while(rs.next()) {
				Object obj = rs.getObject(column);
				if(obj != null) {
					System.out.println(row + ". " + "DATA VALUE: " + "'" + obj.toString() + "'");
				} else {
					System.out.println(row + ". " + "DATA VALUE: " + "'(NULL)'");
				}
				// update row
				row++;
			}
			
			// Position cursor to before first row. 
			rs.beforeFirst();
			
			System.out.println();
			System.out.println("END COLUMN: " + "'" + column + "'");
			System.out.println();
			System.out.println();
		}
		
		System.out.println();
		System.out.println("END TABLE: " + "'" + table + "'");
		System.out.println();
		System.out.println();
	}

}
