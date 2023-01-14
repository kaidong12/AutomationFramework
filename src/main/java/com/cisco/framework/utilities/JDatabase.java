package com.cisco.framework.utilities;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Properties;

import com.cisco.framework.core.exceptions.FrameworkException;
import com.cisco.framework.utilities.logging.Log;

/**
 * @author Francesco Ferrante<br>
 *         <br>
 * 
 *         Class <code>JDatabase</code> is a very thin wrapper around java's JDBC API<br>
 *         which provides support for the following three basic actions:<br>
 *         <br>
 * 
 *         1. Opening a connection to a user-specified DBMS.<br>
 *         2. Executing a user-sepecified sequel query.<br>
 *         3. Closing a connection.<br>
 *         <br>
 * 
 *         The sample code below illustrates the basic usage of class <code>JDatabase</code>.<br>
 *         <br>
 * 
 *         <code>
 *   import java.sql.Connection;<br>
 *   import java.sql.Driver;<br>
 *   import java.sql.DriverManager;<br>
 *   import java.sql.ResultSet;<br>
 *   import java.sql.SQLException;<br>
 *   import java.sql.Statement;<br><br><br>
 *   
 *   try {<br>
 *     String dbConnectionString = "jdbc:oracle:thin:@localhost:9001:cccwtqa";<br>
 *     String dbUserName         = "crs";<br>
 *     String dbPassword         = "crs";<br><br>
 *     
 *     // Instantiate db object using a valid db connection string, db username and db password<br>
 *     JDatabase db = new JDatabase(dbConnectionString, dbUserName, dbPassword);<br><br>
 *     
 *      // Open connection to db.<br>
 *      db.openConnection();<br><br>
 *      
 *      // Eexecute sequel query...<br>
 *      Oject queryResult = db.executeQuery("SELECT * FROM TABLE_A");<br><br>
 *      
 *      if(queryResult != null) {<br><br>
 *        
 *        // Need to type cast class Object to class ResultSet<br>
 *        ResultSet resultSet = (ResultSet)queryResult;<br><br>
 *        
 *        // Code to manipulate ResultSet object goes here.<br> 
 *      }<br><br>
 *     
 *   } catch(SQLException sqle) {<br>
 *     // Code to handle sequel exceptions goes here.<br>
 *     sqle.printStackTrace();<br>
 *   } catch(Exception e) {<br>
 *     // Code to handle non-sequel execptions goes here.<br>   
 *     e.printStackTrace();<br>
 *   } finally {<br>
 *     try {<br>
 *      // Code to close db connection goes here.<br>
 *      db.closeConnection();<br>
 *     } catch(SQLException sqle) {<br>
 *       // More code to handle sequel exceptions goes here.<br>
 *       sqle.printStackTrace();<br>
 *     }<br>
 *   }<br><br>
 *   
 *  </code>
 */
public class JDatabase {

	private Log				log							= null;
	protected Connection	dbConnection				= null;
	protected Statement		sqlStatement				= null;

	private String			dbConnectionString			= "";
	private String			userName					= "";
	private String			password					= "";

	// Versions of JDBC drivers less than 4.0 need special attention
	private final int		JDBC_DRIVER_MAJOR_VERSION	= 4;

	private final String	NEW_LINE					= System.getProperty("line.separator");

	// Maximum time in seconds to wait while attempting to connect to a database.
	// A zero value indicates a no limit wait time.
	private int				connectionTimeoutInSeconds	= 0;

	/**
	 * @param log
	 * @param dbConnectionString
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             If your database connection string contains valid login credentials, then use<br>
	 *             this constructor to instatiate a database object<br>
	 *             <br>
	 *             EXAMPLE:<br>
	 *             <br>
	 *             <p>
	 *             jdbc:oracle:thin:[user/password]@[host][:port]:SID <br>
	 *             jdbc:oracle:thin:crs/crs@localhost:9001:cccwtqa
	 */
	public JDatabase(Log log, String dbConnectionString) throws FrameworkException {
		validateInput(log, dbConnectionString, "JDatabase");
	}

	/**
	 * @param log
	 * @param dbConnectionString
	 * @param connectionTimeoutInSeconds
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this constructor to specify a db connection timeout in seconds.<br>
	 *             The default db connection timeout is set to zero to indicate a no<br>
	 *             limit db connection timeout. If a db connection timeout is specified,<br>
	 *             then method <code>openConnection()</code> throws a <code>SQLException</code><br>
	 *             to indicate that a connection has not been established within<br>
	 *             connectionTimeoutInSeconds.<br>
	 */
	public JDatabase(Log log, String dbConnectionString, int connectionTimeoutInSeconds) throws FrameworkException {
		validateInput(log, dbConnectionString, "JDatabase");
		setLoginTimeoutInSeconds(connectionTimeoutInSeconds);
	}

	/**
	 * @param log
	 * @param dbConnectionString
	 * @param userName
	 * @param password
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             If your database connection string does not contain valid login credentials, then use<br>
	 *             this constructor to instatiate a database object<br>
	 *             <br>
	 *             EXAMPLE:<br>
	 *             <br>
	 *             <p>
	 *             jdbc:oracle:thin:@localhost:9001:cccwtqa
	 */
	public JDatabase(Log log, String dbConnectionString, String userName, String password) throws FrameworkException {
		validateInput(log, dbConnectionString, "JDatabase");
		validateLoginCredentials(userName, password, "JDatabase");
	}

	/**
	 * @param log
	 * @param dbConnectionString
	 * @param connectionTimeoutInSeconds
	 * @param userName
	 * @param password
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Use this constructor to specify a db connection timeout in seconds.<br>
	 *             The default db connection timeout is set to zero to indicate a no<br>
	 *             limit db connection timeout. If a db connection timeout is specified,<br>
	 *             then method <code>openConnection()</code> throws a <code>SQLException</code><br>
	 *             to indicate that a connection has not been established within<br>
	 *             connectionTimeoutInSeconds.<br>
	 */
	public JDatabase(Log log, String dbConnectionString, String userName, String password, int connectionTimeoutInSeconds) throws FrameworkException {
		validateInput(log, dbConnectionString, "JDatabase");
		validateLoginCredentials(userName, password, "JDatabase");
		setLoginTimeoutInSeconds(connectionTimeoutInSeconds);
	}

	/**
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Method <code>openConnection()</code> uses the <code>DriverManager</code> class to<br>
	 *             establish a connection to a database given a valid user-specified db connection string.<br>
	 *             <br>
	 * 
	 *             The supplied db connection string depends on the DBMS the user is trying to connect to.<br>
	 *             <br>
	 * 
	 *             Before establishing a connection, <code>openConnection()</code>, checks for the version of the<br>
	 *             JDBC driver. If the version of the JDBC driver is less than 4.0 then the <code>registerDriver</code><br>
	 *             method of class <code>DriverManager</code> is used to first load and register any JDBC drivers found<br>
	 *             in the CLASSPATH.<br>
	 *             <br>
	 * 
	 *             A call is then made to method <code>acceptsURL</code> of the <code>Driver</code> class<br>
	 *             to ensure that a connection to the DBMS can be established.<br>
	 *             <br>
	 * 
	 *             If a connection to the DBMS can be established then the <code>connect()</code> method of the <code>Driver</code> class<br>
	 *             is called togther with any user-specified connection properites to establish a connection to the user-specified DBMS.<br>
	 *             <br>
	 * 
	 *             If for any reason a connection cannot be established, then a <code>SQLException<code> is thrown.<br><br>
	 * 
	 * If the version of the JDBC driver is greater than 4.0 then a call to method <code>getConnection</code> of<br>
	 *             the <code>DriverManager</code> is made to try to establish a connection to the user-specified DBMS.<br>
	 *             <br>
	 * 
	 *             Again if for any reason a connection cannot be established, then a <code>SQLException<code> is thrown.<br><br>
	 * 
	 * If the db connection string contains login credentials, then the following method call is made<br><br>
	 * <code>DriverManager.getConnection(dbConnectionString)</code><br>
	 *             <br>
	 *             If the db connection string does not contain login credentials, then the following method call is made<br>
	 *             <br>
	 *             <code>DriverManager.getConnection(dbConnectionString, userName, password)</code><br>
	 *             <br>
	 * 
	 * @see DriverManager
	 * @see Driver
	 */
	public void openConnection() throws FrameworkException {
		log.startFunction("Open Database Connection");
		// Set login timeout
		DriverManager.setLoginTimeout(this.connectionTimeoutInSeconds);

		try {
			Driver driver = DriverManager.getDriver(dbConnectionString);

			// If version of JDBC driver is less than 4.0 then driver
			// must be registered after it has been loaded
			if (driver.getMajorVersion() < JDBC_DRIVER_MAJOR_VERSION) {
				DriverManager.registerDriver(driver);
				Properties connectionProperties = new Properties();
				// Determine if driver can indeed open a connection to the given dbUrl
				if (driver.acceptsURL(dbConnectionString)) {
					connectionProperties.put("user", userName);
					connectionProperties.put("password", password);
					dbConnection = driver.connect(dbConnectionString, connectionProperties);
				}
			} else {
				// Get connection to database
				if (!userName.isEmpty() && !password.isEmpty()) {
					dbConnection = DriverManager.getConnection(dbConnectionString, userName, password);
				} else {
					dbConnection = DriverManager.getConnection(dbConnectionString);
				}
			}

			if (this.dbConnectionString.contains("sqlserver")) {
				// For SQL Server
				dbConnection.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
			}

			// Log debug entry
			StringBuilder locatorNameOrParameter = new StringBuilder();
			StringBuilder actionValueOrMessage = new StringBuilder();

			locatorNameOrParameter.append("DB CONNECTION STRING:");
			locatorNameOrParameter.append(NEW_LINE);
			actionValueOrMessage.append(dbConnectionString);
			actionValueOrMessage.append(NEW_LINE);

			if (!userName.isEmpty()) {
				locatorNameOrParameter.append("USER NAME:");
				locatorNameOrParameter.append(NEW_LINE);
				actionValueOrMessage.append(userName);
				actionValueOrMessage.append(NEW_LINE);
			}

			if (!password.isEmpty()) {
				locatorNameOrParameter.append("PASSWORD:");
				locatorNameOrParameter.append(NEW_LINE);
				actionValueOrMessage.append(password);
				actionValueOrMessage.append(NEW_LINE);
			}

			locatorNameOrParameter.append("CONNECTION TIMEOUT IN SECONDS:");
			actionValueOrMessage.append(connectionTimeoutInSeconds);

			log.comment("openConnection", locatorNameOrParameter.toString(), actionValueOrMessage.toString(), Log.DEBUG, Log.SCRIPT_ISSUE);
		} catch (SQLException e) {
			throw new FrameworkException("openConnection", e.getClass().getName(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} finally {
			log.endFunction();
		}
	}

	/**
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Method <code>closeConnection()</code> closes a db connection opened by the<br>
	 *             <code>openConnection()</code> method.<br>
	 *             <br>
	 *             If for any reason db connection cannot be closed, a <code>SQLException</code> is thrown.
	 */
	public void closeConnection() throws FrameworkException {
		log.startFunction("Close Database Connection");
		try {
			if (dbConnection != null) {
				// closeSqlStatement();
				if (!dbConnection.getAutoCommit()) {
					// If database connection is not in auto-commit mode, then do a commit on all transactions and close
					// the database connection.
					dbConnection.commit();
				}
				log.comment("closeConnection", "DISCONNECTED FROM DATABASE:", "'" + dbConnectionString + "'", Log.DEBUG, Log.SCRIPT_ISSUE);
			}
		} catch (SQLException e) {
			throw new FrameworkException("closeConnection", e.getClass().getName(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} finally {
			try {
				if (dbConnection != null) {
					dbConnection.close();
				}
			} catch (SQLException e) {
				throw new FrameworkException("closeConnection", e.getClass().getName(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
			} finally {
				log.endFunction();
			}
		}
	}

	/***
	 * @param sql
	 * @return Object
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             Method <code>executeQuery</code> exeutes a sequel query.<br>
	 *             <br>
	 * 
	 *             If the sequel query is a "SELECT" statement then a <code>Statement</code> Object is created using the following method call:<br>
	 *             <br>
	 *             <code>dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT)</code>
	 *             <br>
	 *             <br>
	 *             A <code>ResultSet</code> Object is returned by making the following method call:<br>
	 *             <br>
	 *             <code>sqlStatement.executeQuery(sql)</code><br>
	 *             <br>
	 * 
	 *             If the sequel query is an "INSERT", "UPDATE", or "DELETE" statement, then a <code>Statement</code> Object is created using the
	 *             following method call:<br>
	 *             <br>
	 * 
	 *             <code>dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.CLOSE_CURSORS_AT_COMMIT)</code>
	 *             <br>
	 *             <br>
	 * 
	 *             The above method call returns the number of records that have been inserted, updated, or deleted.
	 * 
	 *             If the sequel query is a "BEGIN" statment then a zero value is returned.<br>
	 *             <br>
	 * 
	 *             "CREATE" and "DROP" table sequel statements are not supported and therefore <code>executeQuery</code> will return a null value<br>
	 *             <br>
	 * 
	 * @see Statement
	 * @see ResultSet
	 */
	public Object executeQuery(String sql) throws FrameworkException {
		Object res = null;
		String sqlOperation = "";

		try {
			if (sql == null) {
				throw new FrameworkException("executeQuery", "sql", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
			}

			if (sql.isEmpty()) {
				throw new FrameworkException("executeQuery", "sql", "MAKES REFERENCE TO AN EMPTY STRING", Log.ERROR, Log.SCRIPT_ISSUE);
			}

			if (sql.toUpperCase(Locale.ENGLISH).startsWith("SELECT")) {
				sqlOperation = "SELECT";
				// Sequel statement is a "SELECT" statement.
				// sqlStatement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY,
				// ResultSet.CLOSE_CURSORS_AT_COMMIT);
				// sqlStatement = dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY,
				// ResultSet.CLOSE_CURSORS_AT_COMMIT);
				createStatement();
				res = sqlStatement.executeQuery(sql);
			} else {
				// Sequel statement might be an "INSERT", "UPDATE",
				// "DELETE" or "BEGIN".
				if (sql.toUpperCase(Locale.ENGLISH).startsWith("INSERT") || sql.toUpperCase(Locale.ENGLISH).startsWith("UPDATE")
						|| sql.toUpperCase(Locale.ENGLISH).startsWith("DELETE") || sql.toUpperCase(Locale.ENGLISH).startsWith("BEGIN")) {
					// sqlStatement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE,
					// ResultSet.CLOSE_CURSORS_AT_COMMIT);
					createStatement();
					res = sqlStatement.executeUpdate(sql);
				}
			}

			if (sql.toUpperCase(Locale.ENGLISH).startsWith("INSERT")) {
				sqlOperation = "INSERT";
			}

			if (sql.toUpperCase(Locale.ENGLISH).startsWith("UPDATE")) {
				sqlOperation = "UPDATE";
			}

			if (sql.toUpperCase(Locale.ENGLISH).startsWith("DELETE")) {
				sqlOperation = "DELETE";
			}

			if (sql.toUpperCase(Locale.ENGLISH).startsWith("BEGIN")) {
				sqlOperation = "BEGIN";
			}

			// Log debug entry
			log.comment("executeQuery", "SQL OPERATION:" + NEW_LINE + sqlOperation, "SQL STATEMENT:" + NEW_LINE + sql, Log.DEBUG, Log.SCRIPT_ISSUE);

		} catch (SQLException e) {
			throw new FrameworkException("executeQuery", e.getClass().getName(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
			// } finally {
			// try {
			// closeSqlStatement();
			// } catch (SQLException e) {
			// throw new FrameworkException("executeQuery", e.getClass().getName(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
			// }
		}

		return res;
	}

	protected void createStatement() {
		try {
			sqlStatement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY,
					ResultSet.CLOSE_CURSORS_AT_COMMIT);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param connectionTimeoutInSeconds
	 * @throws FrameworkException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             A private method used by class <code>JDatabase</code> to set the<br>
	 *             db connection timeout in seconds. If <code>connectionTimeoutInSeconds</code> is less than zero, the default value of zero seconds
	 *             is not changed.
	 */
	private void setLoginTimeoutInSeconds(int connectionTimeoutInSeconds) throws FrameworkException {
		if (connectionTimeoutInSeconds >= 0) {
			this.connectionTimeoutInSeconds = connectionTimeoutInSeconds;
		} else {
			throw new FrameworkException("JDatabase", "connectionTimeoutInSeconds", "MAKES REFERENCE TO A NEGATIVE QUANTITY", Log.ERROR,
					Log.SCRIPT_ISSUE);
		}
	}

	/**
	 * @throws SQLException
	 *             <br>
	 *             <br>
	 *             USAGE:<br>
	 *             <br>
	 *             <p>
	 *             A private method used by class <code>JDatabase</code> to close<br>
	 *             an existing and opened <code>Statement</code> object<br>
	 *             <br>
	 *             .
	 * 
	 * @see Statement
	 */
	private void closeSqlStatement() throws SQLException {
		// If sequel statement is already opened, then close it
		if (sqlStatement != null) {
			sqlStatement.close();
		}
	}

	/*
	 * Throws an appropriate "FrameworkException" if "log" and "dbConnectionString" make reference to a null pointer or if "dbConnectionString" makes
	 * reference to an empty string.
	 */
	private void validateInput(Log log, String dbConnectionString, String callingMethodName) throws FrameworkException {
		if (log == null) {
			throw new FrameworkException(callingMethodName, "log", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		if (dbConnectionString == null) {
			throw new FrameworkException(callingMethodName, "dbConnectionString", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		if (dbConnectionString.isEmpty()) {
			throw new FrameworkException(callingMethodName, "dbConnectionString", "MAKES REFERENCE TO AN EMPTY STRING", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		this.log = log;
		this.dbConnectionString = dbConnectionString;
	}

	/*
	 * Throws an appropriate "FrameworkException" if "userName" and "password" make reference to a null pointer.
	 */
	private void validateLoginCredentials(String userName, String password, String callingMethodName) throws FrameworkException {
		if (userName == null) {
			throw new FrameworkException(callingMethodName, "userName", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		if (password == null) {
			throw new FrameworkException(callingMethodName, "password", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		this.userName = userName;
		this.password = password;
	}

	public static void main(String... args) {
		System.setProperty("jdbc.drivers", "org.sqlite.JDBC");

		JDatabase db = null;
		Log log = null;

		try {
			String constr = "jdbc:sqlite:C:/Users/bmofwf0/Desktop/Technology/MyDB.sqlite";
			log = new Log("/TestResults/DataDriver_UnitTest.html");
			log.startTestExecution("MyTable");
			db = new JDatabase(log, constr);
			String sqlstr = "select * from MyTable";
			db.openConnection();
			db.executeQuery(sqlstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.exception(e);
		} finally {
			// if(db != null) {
			// db.closeConnection();
			// }
			log.endTestExecution();
		}

	}
}