package com.cisco.framework.utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.cisco.framework.core.exceptions.FrameworkException;
import com.cisco.framework.utilities.JDatabase;
import com.cisco.framework.utilities.logging.Log;

/**
 * @author Lance Yan<br>
 *         <br>
 *
 *         Class <code>DataProvider</code> is a extends of Francesco Ferrante's <code>JDatabase</code><br>
 *         which provides support for the following three basic actions:<br>
 *         <br>
 *
 *         1. Retrieve a specific value in a specific table in sqlite database.<br>
 *         2. Retrieve one row of data in one table or several _table0 with a specific TestCaseID.<br>
 *         3. Update site ID to database when a site is created.<br>
 *         4. Update database when a entity such as sitepage, site been deleted during test execution.<br>
 *         <br>
 *
 *         The sample code below illustrates the basic usage of class <code>DataProvider</code>.<br>
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

public class DataProvider<C, D> extends JDatabase {
	public Log			log			= null;
	public final String	NEW_LINE	= System.getProperty("line.separator");

	public DataProvider(Log log, String dbConnectionString) throws FrameworkException {
		super(log, dbConnectionString);
		this.log = log;
	}

	public DataProvider<C, D>.DataTable<C, D> getTable(C table) {
		if (table == null) {
			throw new FrameworkException("DataProvider.getTable", "", "No table passed!!!", Log.ERROR, Log.SCRIPT_ISSUE);

		} else {
			return new DataTable<C, D>(table);

		}

	}

	@Override
	protected void createStatement() {
		try {
			sqlStatement = dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected ArrayList<Map<String, String>> dataProvider(ResultSet res) throws FrameworkException {
		ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
		// Map<String, String> oneRowData = Collections.synchronizedMap(new HashMap<String, String>());
		Map<String, String> oneRowData = null;
		StringBuilder ResultSetStr = new StringBuilder();
		try {
			ResultSetStr.append("DataProvider->dataProvider->" + res.getMetaData().getTableName(1).toString() + ":" + NEW_LINE);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			while (res.next()) {
				oneRowData = new HashMap<String, String>();
				ResultSetStr.append("================================================================\n");
				for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
					String name = res.getMetaData().getColumnName(i);
					String value = res.getString(i);
					oneRowData.put(name, value);
					String str = String.format("%20s\t%-40s%n", name, value);
					ResultSetStr.append(str);
				}
				arrayList.add(oneRowData);
			}
			ResultSetStr.append("================================================================\n");
			System.out.println(ResultSetStr.toString());
			System.out.println();
			if (oneRowData.isEmpty()) {
				log.comment("DataProvider.dataProvider",
						"Populate a Map with test data retrieve form Database.\nBut the ResultSet is NULL, please check parameters in getDataProvider!",
						ResultSetStr.toString(), Log.WARN, Log.SCRIPT_ISSUE);
			} else {
				log.comment("DataProvider.dataProvider", "Populate a Map with test data retrieve form Database", ResultSetStr.toString(), Log.DEBUG,
						Log.SCRIPT_ISSUE);
			}

		} catch (SQLException e) {
			throw new FrameworkException("dataProvider", e.getClass().getName(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}

		return arrayList;
	}

	public class DataTable<E, F> {
		private E	_table0		= null;
		private E[]	_tableArray	= null;
		private F[]	_fieldArray	= null;
		private int	tc			= 0;
		private int	fc			= 0;

		public DataTable(E table) {
			this._table0 = table;

		}

		public DataTable<E, F> joinTable(E... table) {
			if (table == null) {
				throw new FrameworkException("DataProvider.DataTable.joinTable", "", "No table passed!!!", Log.ERROR, Log.SCRIPT_ISSUE);

			} else {
				this._tableArray = table;
				this.tc = table.length;
				return this;

			}

		}

		public DataTable<E, F> onField(F... field) {
			if (field == null) {
				throw new FrameworkException("DataProvider.DataTable.joinTable", "", "No on field passed!!!", Log.ERROR, Log.SCRIPT_ISSUE);

			} else {
				this._fieldArray = field;
				this.fc = field.length;
				return this;

			}

		}

		public ArrayList<Map<String, String>> getDataByCaseID(String testCaseID) throws FrameworkException {
			log.startFunction("Retrieve test data from datatable [" + this._table0.toString() + "] by Test Case ID: " + testCaseID);

			ArrayList<Map<String, String>> testdata = null;
			try {
				if (testCaseID == null) {
					throw new FrameworkException("DataProvider.DataTable.getDataByCaseID", "", "Table Name is NULL!!!", Log.ERROR, Log.SCRIPT_ISSUE);
				}

				if (this.tc != this.fc) {
					throw new FrameworkException("DataProvider.DataTable.getDataByCaseID", "", "Table count and field count does not match!!!",
							Log.ERROR, Log.SCRIPT_ISSUE);
				}

				if (this.tc == 0) {
					// SELECT * FROM site WHERE TestCaseID="#CreateSite";
					testdata = dataProvider((ResultSet) executeQuery(getSQL(this._table0, testCaseID)));

					if (testdata.isEmpty()) {
						throw new FrameworkException("DataProvider.DataTable.getDataByCaseID", "", "Result Set is NULL!!!", Log.ERROR,
								Log.SCRIPT_ISSUE);
					}

				} else {
					// SELECT * FROM student, class, book WHERE student.classid=class.classid and and student.bookid=book.bookid and
					// student.studentid='#1111';
					testdata = dataProvider((ResultSet) executeQuery(getSQL(this._table0, this._tableArray, this._fieldArray, testCaseID)));

					if (testdata.isEmpty()) {
						throw new FrameworkException("DataProvider.DataTable.getDataByTestCaseID", "", "Result Set is NULL!!!", Log.ERROR,
								Log.SCRIPT_ISSUE);
					}

				}

			} catch (FrameworkException e) {
				throw new FrameworkException("DataProvider.DataTable.getDataByTestCaseID", e.getClass().getName(),
						"Exception occurs when retrieve data from table: " + this._table0, Log.ERROR, Log.SCRIPT_ISSUE);
			} finally {
				log.endFunction();
			}
			return testdata;

		}

		public ArrayList<Map<String, String>> getDataByEntityName(String entityName) throws FrameworkException {
			log.startFunction("Retrieve test data from [" + this._table0.toString() + "] by Entity Name: " + entityName);

			ArrayList<Map<String, String>> testdata = null;
			try {
				if (entityName == null) {
					throw new FrameworkException("DataProvider.DataTable.getDataByEntityName", "", "Table Name is NULL!!!", Log.ERROR,
							Log.SCRIPT_ISSUE);
				}

				if (this.tc != this.fc) {
					throw new FrameworkException("DataProvider.DataTable.getDataByEntityName", "", "Table count and field count does not match!!!",
							Log.ERROR, Log.SCRIPT_ISSUE);
				}

				if (this.tc == 0) {
					// SELECT * FROM site WHERE TestCaseID="#CreateSite";
					testdata = dataProvider((ResultSet) executeQuery(getSQL(this._table0, entityName)));

					if (testdata.isEmpty()) {
						throw new FrameworkException("DataProvider.DataTable.getDataByEntityName", "", "Result Set is NULL!!!", Log.ERROR,
								Log.SCRIPT_ISSUE);
					}

				} else if (this.tc == 1) {
					// SELECT * FROM student, class, book WHERE student.classid=class.classid and and student.bookid=book.bookid and
					// class.className='C++';
					testdata = dataProvider((ResultSet) executeQuery(getSQL(this._table0, this._tableArray, this._fieldArray, entityName)));

					if (testdata.isEmpty()) {
						throw new FrameworkException("DataProvider.DataTable.getDataByEntityName", "", "Result Set is NULL!!!", Log.ERROR,
								Log.SCRIPT_ISSUE);
					}

				} else {
					throw new FrameworkException("DataProvider.DataTable.getDataByEntityName", "", "Only support two tables join at once at most!!!",
							Log.ERROR, Log.SCRIPT_ISSUE);

				}

			} catch (FrameworkException e) {
				throw new FrameworkException("DataProvider.DataTable.getDataByEntityName", e.getClass().getName(),
						"Exception occurs when retrieve data from table", Log.ERROR, Log.SCRIPT_ISSUE);
			} finally {
				log.endFunction();
			}
			return testdata;
		}

		public ArrayList<String> getDataByColummnName(String columnName) {
			log.startFunction("Retrieve test data from [" + this._table0.toString() + "] by Colummn Name: " + columnName);

			ArrayList<String> testdata = new ArrayList<String>();
			ResultSet rs = null;
			StringBuilder ResultSetStr = new StringBuilder();

			try {
				if (columnName == null) {
					throw new FrameworkException("DataProvider.DataTable.getDataByColummnName", "", "Table Name is NULL!!!", Log.ERROR,
							Log.SCRIPT_ISSUE);
				}

				if (this.tc != this.fc) {
					throw new FrameworkException("DataProvider.DataTable.getDataByColummnName", "", "Table count and field count does not match!!!",
							Log.ERROR, Log.SCRIPT_ISSUE);
				}

				if (this.tc == 0) {
					// SELECT SiteName FROM site;
					rs = (ResultSet) executeQuery(getColumnDataSQL(this._table0, columnName));
					if (rs == null) {
						throw new FrameworkException("DataProvider.DataTable.getDataByColummnName", "", "Result Set is NULL!!!", Log.ERROR,
								Log.SCRIPT_ISSUE);
					}

				} else {
					// SELECT bookName FROM student, class, book WHERE student.classid=class.classid and and student.bookid=book.bookid;
					rs = (ResultSet) executeQuery(getColumnDataSQL(this._table0, this._tableArray, this._fieldArray, columnName));
					if (rs == null) {
						throw new FrameworkException("DataProvider.DataTable.getDataByColummnName", "", "Result Set is NULL!!!", Log.ERROR,
								Log.SCRIPT_ISSUE);
					}

				}

				try {
					ResultSetStr.append("========================================\n");
					String str = String.format("%-40s%n", columnName);
					ResultSetStr.append(str);
					ResultSetStr.append("========================================\n");
					while (rs.next()) {
						try {
							String valueStr = rs.getString(columnName);
							str = String.format("%-40s%n", valueStr);
							ResultSetStr.append(str);
							testdata.add(valueStr);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					ResultSetStr.append("========================================\n");
				} catch (SQLException e) {
					e.printStackTrace();
				}

				System.out.println(ResultSetStr.toString());
				System.out.println();
				log.comment("DataProvider.DataTable.getDataByColummnName", "Populate an ArrayList with test data retrieve form Database",
						ResultSetStr.toString(), Log.DEBUG, Log.SCRIPT_ISSUE);

			} catch (FrameworkException e) {
				throw new FrameworkException("DataProvider.DataTable.getDataByColummnName", e.getClass().getName(),
						"Exception occurs when retrieve data from table: " + this._table0, Log.ERROR, Log.SCRIPT_ISSUE);
			} finally {
				log.endFunction();
			}
			return testdata;
		}

		public Row updateRow(String columnName, String columnValue) {

			Row row = null;
			try {
				if (columnName == null) {
					new FrameworkException("DataProvider.DataTable.updateRowAt", "", "Column name is NULL!!!", Log.ERROR, Log.SCRIPT_ISSUE);
				}

				if (columnValue == null) {
					new FrameworkException("DataProvider.DataTable.updateRowAt", "", "Column value is NULL!!!", Log.ERROR, Log.SCRIPT_ISSUE);
				}

				row = new Row(this, columnName, columnValue);

			} catch (FrameworkException e) {
				throw new FrameworkException("DataProvider.DataTable.getDataByTestCaseID", e.getClass().getName(),
						"Exception occurs when retrieve data from table: " + this._table0, Log.ERROR, Log.SCRIPT_ISSUE);
			}

			return row;

		}

		private String getSQL(E table, String fieldValue) {
			StringBuilder sqlStr = new StringBuilder();

			if (fieldValue.toLowerCase().startsWith("#")) {
				// SELECT * FROM user WHERE TestCaseID='#CreateUser';
				sqlStr.append("SELECT * FROM ");
				sqlStr.append(table.toString());
				sqlStr.append(" WHERE CaseID='");
				sqlStr.append(fieldValue);
				sqlStr.append("';");
			} else {
				// SELECT * FROM site WHERE siteName="local";
				sqlStr.append("SELECT * FROM ");
				sqlStr.append(table.toString());
				sqlStr.append(" WHERE ");
				sqlStr.append(table.toString());
				sqlStr.append("Name='");
				sqlStr.append(fieldValue);
				sqlStr.append("';");
			}

			log.comment(sqlStr.toString());
			return sqlStr.toString();
		}

		private String getSQL(E table0, E[] tables, F[] fields, String fieldValue) {
			StringBuilder sqlStr = new StringBuilder();
			int counter = tables.length;
			String table0Name = table0.toString();

			// SELECT * FROM student, class, book WHERE student.classid=class.classid and student.bookid=book.bookid and
			// student.studentid='#1111';
			sqlStr.append("SELECT * FROM ");
			sqlStr.append(table0Name);
			for (int i = 0; i < counter; i++) {
				sqlStr.append(", ");
				sqlStr.append(tables[i].toString());
			}
			sqlStr.append(" WHERE ");
			for (int i = 0; i < counter; i++) {
				String field = fields[i].toString();
				if (field.contains(":")) {
					String[] fd = field.split(":");
					sqlStr.append(table0Name);
					sqlStr.append(".");
					sqlStr.append(fd[0]);
					sqlStr.append("=");
					sqlStr.append(tables[i].toString());
					sqlStr.append(".");
					sqlStr.append(fd[1]);
					sqlStr.append(" AND ");

				} else {
					sqlStr.append(table0Name);
					sqlStr.append(".");
					sqlStr.append(field);
					sqlStr.append("=");
					sqlStr.append(tables[i].toString());
					sqlStr.append(".");
					sqlStr.append(field);
					sqlStr.append(" AND ");

				}

			}
			if (fieldValue.toLowerCase().startsWith("#")) {
				sqlStr.append(table0Name);
				sqlStr.append(".CaseID='");
				sqlStr.append(fieldValue);
				sqlStr.append("';");
			} else {
				sqlStr.append(tables[0]);
				sqlStr.append(".");
				sqlStr.append(tables[0]);
				sqlStr.append("Name='");
				sqlStr.append(fieldValue);
				sqlStr.append("';");
			}

			log.comment(sqlStr.toString());
			return sqlStr.toString();

		}

		// select TestCaseID from webcontent;
		private String getColumnDataSQL(E table, String field) {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("SELECT ");
			sqlStr.append(field);
			sqlStr.append(" FROM ");
			sqlStr.append(table);
			sqlStr.append(";");

			log.comment(sqlStr.toString());
			return sqlStr.toString();

		}

		// select TestCaseID from webcontent;
		private String getColumnDataSQL(E table0, E[] tables, F[] fields, String columnName) {
			StringBuilder sqlStr = new StringBuilder();
			int counter = tables.length;
			String table0Name = table0.toString();

			// SELECT bookName FROM student, class, book WHERE student.classid=class.classid and student.bookid=book.bookid;
			sqlStr.append("SELECT ");
			if (columnName.contains(":")) {
				String[] cn = columnName.split(":");
				sqlStr.append(cn[0]);
				sqlStr.append(".");
				sqlStr.append(cn[1]);
			} else {
				sqlStr.append(table0Name);
				sqlStr.append(".");
				sqlStr.append(columnName);
			}
			sqlStr.append(" FROM ");
			sqlStr.append(table0Name);
			for (int i = 0; i < counter; i++) {
				sqlStr.append(", ");
				sqlStr.append(tables[i].toString());
			}
			sqlStr.append(" WHERE ");
			for (int i = 0; i < counter; i++) {
				String field = fields[i].toString();
				if (field.contains(":")) {
					String[] fd = field.split(":");
					sqlStr.append(table0Name);
					sqlStr.append(".");
					sqlStr.append(fd[0]);
					sqlStr.append("=");
					sqlStr.append(tables[i].toString());
					sqlStr.append(".");
					sqlStr.append(fd[1]);

				} else {
					sqlStr.append(table0Name);
					sqlStr.append(".");
					sqlStr.append(field);
					sqlStr.append("=");
					sqlStr.append(tables[i].toString());
					sqlStr.append(".");
					sqlStr.append(field);

				}

			}
			sqlStr.append(";");

			log.comment(sqlStr.toString());
			return sqlStr.toString();

		}

		// UPDATE site SET Deleted= 'Yes' WHERE SiteName= 'Auto_NAM_live_1';
		private String getUpdateSQL(String table, String field, String value, String updateColumn, String updateValue) {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("UPDATE ");
			sqlStr.append(table);
			sqlStr.append(" SET ");
			sqlStr.append(updateColumn);
			sqlStr.append(" = '");
			sqlStr.append(updateValue);
			sqlStr.append("' WHERE ");
			sqlStr.append(field);
			sqlStr.append(" = '");
			sqlStr.append(value);
			sqlStr.append("';");

			log.comment(sqlStr.toString());
			return sqlStr.toString();
		}

		public class Row {
			DataTable<E, F>	datatable	= null;
			String			column		= "";
			String			value		= "";

			public Row(DataTable<E, F> tbl, String col, String val) {
				this.datatable = tbl;
				this.column = col;
				this.value = val;
			}

			public Object setValue(String field, String newValue) {
				log.startFunction("Update value of Colummn [" + field + "] with value [" + newValue + "]");
				Object ro = executeQuery(getUpdateSQL(this.datatable._table0.toString(), column, value, field, newValue));
				log.endFunction();
				return ro;
			}

		}

	}

}
