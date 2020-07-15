package jdatabase;

//simple JSON
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;

/* class to have all common functions */
public class serviceCommon {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Reading JSON ..");
		System.out.println("websitefolder=" + getJSONStringValuefromFile("dbconfig.json","websitefolder"));
		
	}
	public static String getJSONStringValuefromFile(String filename, String node)
		{
				String nodevalue = "";
				
				try (FileReader config = new FileReader(filename))
				{
					Object obj = new JSONParser().parse(config); 
					  
					// typecasting obj to JSONObject 
					JSONObject jo = (JSONObject) obj; 
						  
					// getting firstName and lastName 
					nodevalue = (String) jo.get(node); 

				}
				catch (FileNotFoundException e) {
							e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (ParseException e) {
								e.printStackTrace();
							}
				
				return nodevalue;			
		}
}