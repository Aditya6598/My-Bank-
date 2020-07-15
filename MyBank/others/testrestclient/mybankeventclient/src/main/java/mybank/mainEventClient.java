package mybank;

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


public class mainEventClient {
	
	private static final String REST_URI = "http://localhost:8085/myapp/customer/executequery";
	  
	private static Client client = ClientBuilder.newClient();

    public static void main(String[] args) {
        
		//initialize rest client
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
        WebTarget service  = client.target(getBaseURI());
		
		//build JSON to be sent 
		JSONObject inputJsonObj = new JSONObject();
		String qry = "SELECT CONCAT(  '[',  GROUP_CONCAT(JSON_OBJECT('_id', _id)), ']') as jsonresult from tCustomer";
		inputJsonObj.put("query", qry);
		inputJsonObj.put("querytype", 0);
		
		//executequery and print result
		System.out.println("The response received is:");
		Response resp = 
		service.path("customer")
		.path("executequery")
		.request(MediaType.APPLICATION_JSON)
		.post(Entity.json(inputJsonObj.toString()));
		
		String output = resp.readEntity(String.class);
		System.out.println(output);
		
		//this time try with the full path
		//executequery and print result
		
		service  = client.target(REST_URI);
		
		System.out.println("The 2nd response received is:");
		resp = 
		service
		.request(MediaType.APPLICATION_JSON)
		.post(Entity.json(inputJsonObj.toString()));
		
		output = resp.readEntity(String.class);
		System.out.println(output);

		
    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8085/myapp/").build();
    }
}