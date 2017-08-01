package de.unihannover.l3s.es;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
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
	
	httpClient.getCredentialsProvider().setCredentials(
            new AuthScope("194.119.209.206", 9200),
            new UsernamePasswordCredentials(<USER>, <PASSWORD>));
	
	// URIBuilder builder;
	HttpGet method;
	StringBuffer jsonString = new StringBuffer();
	try {
		
		/*StringEntity payload = new StringEntity("{"+
		 "\"query\" : {"+
         "\"match\" : { \"content\": \""+query+"\" }"+
		"},"+
         "\"size\": \""+noOfResults+"\" "+
		"}");
		*/

	    method = new HttpGet(apiUrl+"?size=200&q="+query.trim());
	    method.setHeader("Accept", "application/json");
	    method.addHeader("content-type", "application/json");
	    // method.setEntity(payload);

	    //System.out.println("URL:"+apiUrl+"?size=200&q="+query.trim());
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
		System.out.println(arr.length());
		for (int i = 0; i < arr.length(); i++)
		{
			obj= arr.getJSONObject(i).getJSONObject("_source");
			dataObj=new ESearchDataObject();
			JSONArray entity_arr = new JSONArray();
			if (obj.has("resources")) 
				entity_arr = obj.getJSONArray("resources");
			String date=obj.getInt("airdate")+"";
			if (date.length()<8)
				date="0"+date;
			/*Integer month=Integer.parseInt(date.substring(2,4));
			Integer year=Integer.parseInt(date.substring(4,8));
			Integer season=0;
			season=year-2004;
			if (season!=0){
				if (month>=9)
					season++;
			}else 
				season=1; */
			String season1=obj.getInt("season")+"";
			String episode=obj.getInt("episode")+"";
			String title=obj.getString("title");
			String scene="";
			String sceneid="";
			if (obj.has("scene")){
				sceneid=obj.getInt("scene")+"";
				if (Integer.parseInt(sceneid)<10)
					sceneid="0"+sceneid;
				scene=season1+episode+sceneid;
			}
			
			dataObj.setScene(scene);
				
			if (Integer.parseInt(episode)<10)
				episode="0"+episode;
			dataObj.setTitle("Season: "+season1+" - Episode: "+episode+" -  "+title); // +" - "+obj.getString("author")+" - "+date.substring(0,2)+"-"+date.substring(2,4)+"-"+date.substring(4,8));
			dataObj.setUrl(obj.getString("url"));
			dataObj.setContent("<h2>"+dataObj.getTitle()+" - Scene: "+sceneid+"</h2>"+obj.getString("transcript")); // .substring(0,100));
			dataObj.setTstamp("");
			for (int j = 0; j < entity_arr.length(); j++)
			{
				// String ent=entity_arr.getJSONObject(j).getString("uri");
				// System.out.println(ent);
				dataObj.addEntities(entity_arr.getJSONObject(j).getString("uri"));
			}
			/*dataObj.setTitle(obj.getString("title"));
			dataObj.setUrl(obj.getString("url"));
			dataObj.setContent(obj.getString("content"));
			dataObj.setTstamp(obj.getString("tstamp")); 
			*/
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
