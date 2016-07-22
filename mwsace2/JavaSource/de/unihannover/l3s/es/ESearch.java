package de.unihannover.l3s.es;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ESearch {
	
	private  String apiUrl;
	//= "http://194.119.209.206:9200/index_2/_search";
	public ESearch(String apiUrl)
	{
		this.apiUrl=(String)apiUrl;	
	}
public  List<ESearchDataObject> searhByContent(String query, int noOfResults)
{
	// HttpClient httpClient = HttpClientBuilder.create().build();
	DefaultHttpClient httpClient = new DefaultHttpClient();
	
	// URIBuilder builder;
	HttpPost method;
	StringBuffer jsonString = new StringBuffer();
	try {
		
		StringEntity payload = new StringEntity("{"+
		 "\"query\" : {"+
         "\"match\" : { \"content\": \""+query+"\" }"+
		"},"+
         "\"size\": \""+noOfResults+"\" "+
		"}");
	

	    method = new HttpPost(apiUrl);
	    method.setHeader("Accept", "application/json");
	    method.addHeader("content-type", "application/json");
	    method.setEntity(payload);

	    
	 // Send POST request

	    int statusCode;
	    
	    HttpResponse response = httpClient.execute(method);
	    statusCode = response.getStatusLine().getStatusCode();
	    
		if (statusCode != HttpStatus.SC_OK) {
        	System.err.println("Method failed: " + response.getStatusLine());
        }

	    InputStream rstream = null;
	    rstream = response.getEntity().getContent(); 

        BufferedReader br = new BufferedReader(new InputStreamReader(rstream));
        String line;

        while ((line = br.readLine()) != null) {
            jsonString.append(line);
            jsonString.append("\n");
        }
        System.out.println(jsonString);
        br.close();
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	ESearchDataObject dataObj;
	ArrayList<ESearchDataObject> objList = new ArrayList<ESearchDataObject>();
	JSONObject obj;
	try {
		obj = new JSONObject(jsonString.toString());
		obj =(JSONObject) obj.get("hits");
		JSONArray arr = obj.getJSONArray("hits");
		for (int i = 0; i < arr.length(); i++)
			{
			obj= arr.getJSONObject(i).getJSONObject("_source");
			dataObj=new ESearchDataObject();
			dataObj.setTitle(obj.getString("title"));
			dataObj.setUrl(obj.getString("url"));
			dataObj.setContent(obj.getString("content"));
			dataObj.setTstamp(obj.getString("tstamp"));
			objList.add(dataObj);
			}
	//System.out.println(obj.get("content"));
	}
	catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return objList;
}
}
