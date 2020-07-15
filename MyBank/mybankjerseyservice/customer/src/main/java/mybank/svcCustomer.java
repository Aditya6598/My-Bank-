package mybank;

import javax.ws.rs.GET;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.http.HTTPException;


import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//simple JSON
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

//common bank database object
import jdatabase.*;
import jevent.*;

/**
 * Root resource (exposed at "svcCustomer" path)
 */
@Path("customer")
public class svcCustomer {
  
  public String data;
  public dbsql db;
  public String query;
  public jeventClient eventclient;
  final int READ_QUERY = 0;
  final int WRITE_QUERY = 1;

  public svcCustomer(){
	db = new dbsql(0); //create the database object  
	eventclient = new jeventClient("customer");
  }
    
  @POST
  @Path("/executequery")
  @Produces({"application/json"})
  @Consumes({"application/json"})
  public Response executequery(String data) {
	
	String jsonresult="";
	
	System.out.println("svcCustomer executequery called");
	
	try{

		
			data = data.replace("\n", "").replace("\\r", "").replace("\t", "");
			
			
			// parsing file "JSONExample.json" 
			Object obj = new JSONParser().parse(data); 
				  
			// typecasting obj to JSONObject 
			JSONObject jo = (JSONObject) obj; 

			//get the query and query type
			String query = (String) jo.get("query"); 
			int querytype = ((Long)jo.get("querytype")).intValue(); 

			jsonresult = db.executequery(query,querytype) ; //return json result from the query
			
			System.out.println("svcCustomer executequery result=" + jsonresult);
			
			//at present we are just forwarding the query and querytype as it is received
			//we may have to create a standard json data for events which contains source, destination
			eventclient.sendEvent(data);	

	}
	catch(Exception e)
	{
		System.out.println("returning post2 failure");
		System.out.println("[ {'error':'" + e.toString() + "'}]");
		return sendjsonresponse("[ {'error':'" + e.toString() + "'}]"); //send the error as response
	}
	
	return sendjsonresponse(jsonresult);
  }
  
  public Response sendjsonresponse(String output)
	 {
		 try {            
			data = output;
			return Response.status(200).entity(data).build();
		}        
		catch(Exception e) {            
			throw new HTTPException(400);
		}
	 }
	 

	
	@POST
	@Path("/syncevent")
	@Produces({"application/json"})
	@Consumes({"application/json"})
	public void syncEvent(String eventdata)
	{
			//connect to event service
			//sync event into local database
			eventclient.syncEvent(eventdata,db);
	}	
}
