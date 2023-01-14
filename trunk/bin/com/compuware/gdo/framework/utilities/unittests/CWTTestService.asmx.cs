using System;
using System.Collections;
using System.ComponentModel;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Web.Services.Protocols;
using System.Xml.Linq;
using System.Data.OleDb;
using System.Text;

namespace CWTTestWebService
{
    /// <summary>
    /// Summary description for CWTTestService
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class CWTTestService : System.Web.Services.WebService
    {

        [WebMethod]
        public string HelloWorld()
        {
            return "Hello World";
        }

        [WebMethod]
        public string Test(string input)
        {
            return input;
        }

        [WebMethod]
        public string Wait(int numSeconds)
        {
            string res = "";
            if (numSeconds > 0)
            {
                System.Threading.Thread.Sleep(numSeconds * 1000);
                res = "Processing time in seconds: " + "'" + numSeconds + "'";
            }
            return res;
        }

        [WebMethod]
        public int Add(int num1, int num2)
        {
            return num1 + num2;
        }

        [WebMethod]
        public int Subtract(int num1, int num2)
        {
            return num1 - num2;
        }

        [WebMethod]
        public int Multiply(int num1, int num2)
        {
            return num1 * num2;
        }

        [WebMethod]
        public double Divide(int num1, int num2)
        {
            return (double)num1 / (double)num2;
        }

        [WebMethod]
        public string GetTable(string dbConnectionString, string sqlStatement)
        {
            OleDbConnection dbConnection = new OleDbConnection(dbConnectionString);
            OleDbDataReader dbReader = null;
            StringBuilder res = new StringBuilder();

            try
            {
                dbConnection.Open();
                OleDbCommand cmd = new OleDbCommand(sqlStatement, dbConnection);
                dbReader = cmd.ExecuteReader();
                for (int col = 0; col < dbReader.FieldCount; col++)
                {
                    string columnName = dbReader.GetName(col);
                    res.Append("START TABLE COLUMN: " + "[" + columnName + "]");
                    res.AppendLine();
                    int tableItemIndex = 1;
                    while (dbReader.Read())
                    {
                        res.Append("TABLE ITEM: " + tableItemIndex + "'" + dbReader[columnName] + "'");
                        res.AppendLine();
                        tableItemIndex++;
                    }
                    res.Append("END TABLE COLUMN: " + "[" + columnName + "]");
                    res.AppendLine();
                }
            }
            catch (OleDbException e)
            {
                res.Append(e.Message);
            }
            finally
            {
                if (dbConnection != null)
                {
                    dbConnection.Close();
                }
                if (dbReader != null)
                {
                    dbReader.Close();
                }
            }

            return res.ToString();
        }

    }
}
