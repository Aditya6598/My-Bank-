package jevent;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;


import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;


import org.glassfish.jersey.client.ClientConfig;

//simple JSON
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

import javax.xml.ws.http.HTTPException;

import jdatabase.*;


	public class jeventClient
	{  
		private static String eventsyncURI = "http://localhost:8084/myapp";
	    private static WebTarget service = null; 
		private static String eventsource; 
		private static Client client;
							
		public jeventClient(String eventsrc)
		{
			//initialize rest client to event sync 
			ClientConfig config = new ClientConfig();
			client = ClientBuilder.newClient(config);
			service  = client.target(getBaseURI(eventsyncURI));
			eventsource = eventsrc;
		}
		
		//can test the sql independently from command prompt too
		public static void main(String args[]){ 
			System.out.println("event client is initialized sync events at " + eventsyncURI);
		}
		
		public Response sendevent(JSONObject inputJsonObj)
		{
			Response resp = 
					service.path("event")
					.path("sync")
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.json(inputJsonObj.toString()));
		
					String output = resp.readEntity(String.class);
					System.out.println(output);
					
			return resp;
					
		}
		
		public Response sendEvent(String data)
		{		
			String output= "";
			
			try
			{
			data = data.replace("\n", "").replace("\\r", "").replace("\t", "");
			Object obj = new JSONParser().parse(data); 
			JSONObject inputJsonObj  = (JSONObject) obj; 
			
			Response resp = 
					service.path("event")
					.path("sync")
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.json(inputJsonObj.toString()));
		
					output = resp.readEntity(String.class);
					System.out.println(output);
			}
			catch(Exception e )
			{	
				System.out.println("returning sendevent failure");
				System.out.println("[ {'error':'" + e.toString() + "'}]");
				return sendjsonresponse("[ {'error':'" + e.toString() + "'}]"); //send the error as response
			}
			
			return sendjsonresponse(output);
					
		}
		
				
		// syncevents are raised by eventsync. It should be processed by microservices as needed
		// the component seeking to sync the event also passes the database into which the event needs to be synced 
		public String syncEvent(String eventdata, dbsql db)
		{		
				//store the event in local database as per dbConfigFileName
				//return status 0, 1
				String query,jsonresult;
				int querytype = 1; //insert, update, delete queries are set to 1 
				
				// since we are synching locally eventdestination is the source that is set
				// we do not know who sent the message,hence event source is unknown for now
				// eventstatus = 0 means event is saved and needs to be processed
				// eventdirection = 2 since its a received event, waiting to be synched locally 
				
				//json data needs the escape character to understand single quote contained within query
				eventdata = eventdata.replace("'","\\'");
				
				query = "INSERT INTO tEvents " + 
							" (eventdata, " + 
							" eventdestination, " + 
							" eventstatus, " + 
							" eventdirection, " + 
							" eventid, " + 
							" eventsource) " +
				" VALUES ("  + 
							"'" + eventdata + "'" + "," + 
							"'" + eventsource + "'" + "," + 
							"0"  + "," + 
							 "2"  + "," + 
							"-1"  + "," + 
							"'" + "unknown" + "'" + 
							 ");";
				
				System.out.println("query to be fired =" + query);
				
				jsonresult = db.executequery(query,querytype) ; //return json result from the query
				
				System.out.println("svcCustomer executequery result=" + jsonresult);
				
				return jsonresult;
				
		}
		
		public Response broadcastEvent(String data, String uri)
		{		
			String output= "";
			
			try
			{
				
			System.out.println("broadcasting to service at :" + uri);	
				
			data = data.replace("\n", "").replace("\\r", "").replace("\t", "");
			Object obj = new JSONParser().parse(data); 
			JSONObject inputJsonObj  = (JSONObject) obj; 
			
			WebTarget broadcasttoservice  = client.target(uri);
			
			Response resp = 
					broadcasttoservice
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.json(inputJsonObj.toString()));
		
					output = resp.readEntity(String.class);
					System.out.println(output);
			}
			catch(Exception e )
			{	
				System.out.println("returning broadcast event failure");
				System.out.println("[ {'error':'" + e.toString() + "'}]");
				return sendjsonresponse("[ {'error':'" + e.toString() + "'}]"); //send the error as response
			}
			
			return sendjsonresponse(output);
		}
		
		
		
		private static URI getBaseURI(String uri) {
			return UriBuilder.fromUri(uri).build();
		}
		
		public Response sendjsonresponse(String output)
		 {
			 String data = "";
			 try {            
				data = output;
				return Response.status(200).entity(data).build();
			}        
			catch(Exception e) {            
				throw new HTTPException(400);
			}
		 }
		
	} // eventClient class end 