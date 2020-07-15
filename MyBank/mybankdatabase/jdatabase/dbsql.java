package jdatabase;
    
	import java.sql.*;  
	
    //simple JSON
	import org.json.simple.JSONArray; 
	import org.json.simple.JSONObject; 
	import org.json.simple.parser.*; 

	
	public class dbsql
	{  
		final static int READ_QUERY = 0;
		final static int WRITE_QUERY = 1;
		
		
		static String databasename = "dbCustomer";
		static String databaseusername = "root";
		static String databasepassword = "aditya";
		static String databasetestquery = "SELECT CONCAT( " + 
							" '[', " + 
						" GROUP_CONCAT(JSON_OBJECT('_id', _id)), " +
							"']'" + 
							") as jsonresult from tCustomer;";
							
		static serviceCommon cmn = new serviceCommon();
		
		
		
		//can test the sql independently from command prompt too
		public static void main(String args[]){ 
			try 
			{
			String data ;
			String query = databasetestquery;
			
			initializedatabase(0);
			
			//data = executeQuery("SELECT * FROM _qbinv",READ_QUERY);
			System.out.println("Executing query :" + query);
			data = executequery(query,READ_QUERY);
			
			System.out.println(data);
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
				
			}
		}
		
		/* constructor */
		/*
			databaseID = 0 dbCustomer, 1 dbAccount , 2 dbMoneyTransfer, 3 dbEventSynch
			
		*/
			
		public dbsql(int databaseID)
		{
			initializedatabase(databaseID); //bydefault connect to dbCustomer 
		}
	
		
		public static void initializedatabase(int databaseID)
		{
			
			try
			{
				String dbConfigFileName = "dbconfig_customer.json";
				
				System.out.println("databaseid=" + databaseID);
				
				if (databaseID == 0)
					dbConfigFileName = "dbconfig_customer.json";
				else if (databaseID == 1)
					dbConfigFileName = "dbconfig_account.json";
				else if (databaseID == 2)
					dbConfigFileName = "dbconfig_moneytransfer.json";
				else if (databaseID == 3)
					dbConfigFileName = "dbconfig_eventsync.json";
				System.out.println("reading configuration file" + dbConfigFileName);
				
				databasename = cmn.getJSONStringValuefromFile(dbConfigFileName,  "databasename");
				databaseusername = cmn.getJSONStringValuefromFile(dbConfigFileName,  "databaseusername");
				databasepassword = cmn.getJSONStringValuefromFile(dbConfigFileName,  "databasepassword");
				databasetestquery = cmn.getJSONStringValuefromFile(dbConfigFileName,  "databasetestquery");
				
				System.out.println("initializing database");
				
				System.out.println("databasename="+databasename);
				System.out.println("databaseusername="+databaseusername);
				System.out.println("databasepassword="+databasepassword);
				System.out.println("databasetestquery="+databasetestquery);
			}
			catch(Exception e){
				System.out.println("jdatabase error :" + e.toString());
			}
			
		
			}
		
		/* querytype = 0 means SELECT query, querytype 1 means data manipulation query */
		// create the mysql insert preparedstatement
		  /*
		  //EXAMPLE OF DATA MANIPULATION QUERY
		  PreparedStatement preparedStmt = conn.prepareStatement(query);
		  preparedStmt.setString (1, "Barney");
		  preparedStmt.setString (2, "Rubble");
		  preparedStmt.setDate   (3, startDate);
		  preparedStmt.setBoolean(4, false);
		  preparedStmt.setInt    (5, 5000);

		  // execute the preparedstatement
		  preparedStmt.execute();
		  */
		  
		
		public static String executequery(String query, int querytype)
		{
			
			Statement stmt; //used for SELECT query
			PreparedStatement preparedStmt; //used for UPDATE, DELETE, INSERT
			ResultSet rs;
			String sResult;
			
			sResult = "";
			boolean displayResult = true;
			String querydelimiter = ":::";
			
			try
			{  
				System.out.println("attempting query against database:"+databasename+";user="+databaseusername+";password="+databasepassword);
				
				Class.forName("com.mysql.cj.jdbc.Driver");  
				Connection con=DriverManager.getConnection(  
					"jdbc:mysql://localhost:3306/" + databasename,databaseusername,databasepassword);  
					
				stmt=con.createStatement();  
		
				if (querytype == READ_QUERY)
				{
					
					displayResult = false;
					rs=stmt.executeQuery(query);  
					while(rs.next())  
					{
						sResult = rs.getString(1);

					}
					
					
				}
				else if (querytype == WRITE_QUERY)
				{
					System.out.println("executing query type 1 : " + query );
					
					String[] querylist = query.split(querydelimiter);
					String q = "";
					System.out.println("Total queries to execute = " + querylist.length);
					
					for (int i = 0; i < querylist.length; i++) {
						q = querylist[i]; //+ ";"; //add the semi colon back for each query
						System.out.print("\n\n executing query=" + q);
						preparedStmt = con.prepareStatement(q);
						preparedStmt.execute();
					}
					
					sResult = "[{'success': 'WRITE_QUERY executed'}]";
				}
								
				
				con.close();  
				
				
			}
			catch(Exception e)
			{ 
				sResult = "[{'error':'" + e.toString() +"'}]";
			}  
			
			if (displayResult)
				System.out.println("Result: " + sResult);
			else
				System.out.println("successful read query");
				
			return sResult;
			
		}
	
    }  

/*
how to compile: 
F:\_JAMES\pers\accountingsoftware>javac -cp "mysql-connector-java-8.0.18.jar"; webservice\database\dbsql.java
E:\_JAMES\Documents\cloudcomputing-sem3\practicals\soa\mybankdatabase>javac -cp "json-simple-1.1.1.jar";"mysql-connector-java-8.0.18.jar"; jdatabase\*.java
how to run: java -cp "mysql-connector-java-8.0.18.jar"; webservice.database.dbsql
java -cp "json-simple-1.1.1.jar";"mysql-connector-java-8.0.18.jar"; jdatabase.dbsql
how to jar:
jar cvf jdatabase.jar ./jdatabase/*.class json-simple-1.1.1.jar mysql-connector-java-8.0.18.jar

*/
