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

public class ESearchPost {

	private String apiUrl;

	// = "http://194.119.209.206:9200/index_2/_search";
	public ESearchPost(String apiUrl) {
		this.apiUrl = (String) apiUrl;
	}

	public List<ESearchDataObject> searhByContent(String query) {
		// HttpClient httpClient = HttpClientBuilder.create().build();
		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpPost method;
		StringBuffer jsonString = new StringBuffer();
		try {

			method = new HttpPost(apiUrl); // +"?size=200&q="+query.trim());
			method.setHeader("Accept", "application/json");
			method.addHeader("content-type", "application/json");

			/*
			 * String annotationsurface=""; if (usesub)
			 * annotationsurface="annotation.resources.surfaceForm.sub"; else
			 * annotationsurface="annotation.resources.surfaceForm";
			 */

			StringEntity entity = new StringEntity(query);
			// new
			// StringEntity("{\"size\":10, \"query\":{\"function_score\":{\"query\":{\"bool\":{\"should\":[{\"match\":{\""+annotationsurface+"\":{\"query\":\""+query+"\",\"boost\":2}}},{\"bool\":{\"should\":[{\"match\":{\"content\":\""+query+"\"}}]}}]}},\"filter\":{\"bool\":{\"must\":[{\"range\":{\"timestamp.sub\":{\"gte\":"+startdate+"000000,\"lte\":"+enddate+"000000}}}], \"should\":[{\"term\":{\"language\":\"en\"}}],\"must_not\":[{\"term\":{\"hasAnnotation\":\"false\"}}]}},\"field_value_factor\":{\"field\":\"annotation.resources.tf\",\"modifier\": \"ln2p\",\"factor\":2}}}}");

			System.out.println(query);
			method.setEntity(entity);

			// System.out.println("URL:"+apiUrl+"?size=200&q="+query.trim());
			// Send POST request

			int statusCode;

			HttpResponse response = httpClient.execute(method);
			statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				System.err
						.println("Method failed: " + response.getStatusLine());
			}

			InputStream rstream = null;
			rstream = response.getEntity().getContent();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					rstream));
			String line;

			while ((line = br.readLine()) != null) {
				jsonString.append(line);
				jsonString.append("\n");
			}
			// System.out.println(jsonString);
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
			obj = (JSONObject) obj.get("hits");
			JSONArray arr = obj.getJSONArray("hits");
			System.out.println(arr.length());
			for (int i = 0; i < arr.length(); i++) {
				obj = arr.getJSONObject(i).getJSONObject("_source");
				dataObj = new ESearchDataObject();
				JSONArray entity_arr = new JSONArray();
				if (obj.has("resources"))
					entity_arr = obj.getJSONArray("resources");

				/*
				 * Integer month=Integer.parseInt(date.substring(2,4)); Integer
				 * year=Integer.parseInt(date.substring(4,8)); Integer season=0;
				 * season=year-2004; if (season!=0){ if (month>=9) season++;
				 * }else season=1;
				 */

				dataObj.setTitle(obj.getString("title"));
				dataObj.setScene(obj.getString("contentDigest"));
				dataObj.setUrl(obj.getString("url"));
				dataObj.setContent(obj.getString("content")); // .substring(0,100));
				dataObj.setTstamp("" + obj.getLong("timestamp"));
				for (int j = 0; j < entity_arr.length(); j++) {
					// String ent=entity_arr.getJSONObject(j).getString("uri");
					// System.out.println(ent);
					dataObj.addEntities(entity_arr.getJSONObject(j).getString(
							"uri"));
				}
				/*
				 * dataObj.setTitle(obj.getString("title"));
				 * dataObj.setUrl(obj.getString("url"));
				 * dataObj.setContent(obj.getString("content"));
				 */
				objList.add(dataObj);

			}
			// System.out.println(obj.get("content"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objList;
	}

	public List<ESearchDataObject> searhByContent2(String query) {
		// HttpClient httpClient = HttpClientBuilder.create().build();
		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpPost method;
		StringBuffer jsonString = new StringBuffer();
		try {

			method = new HttpPost(apiUrl); // +"?size=200&q="+query.trim());
			method.setHeader("Accept", "application/json");
			method.addHeader("content-type", "application/json");

			StringEntity entity = new StringEntity(query);

			method.setEntity(entity);

			int statusCode;

			HttpResponse response = httpClient.execute(method);
			statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				System.err
						.println("Method failed: " + response.getStatusLine());
			}

			InputStream rstream = null;
			rstream = response.getEntity().getContent();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					rstream));
			String line;

			while ((line = br.readLine()) != null) {
				jsonString.append(line);
				jsonString.append("\n");
			}
			// System.out.println(jsonString);
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
			obj = (JSONObject) obj.get("aggregations");
			obj = (JSONObject) obj.get("NestedResources Path");
			obj = (JSONObject) obj.get("EntityType");
			
			obj = (JSONObject) obj.get("GroupByEntities");
			JSONArray arr = obj.getJSONArray("buckets");
			// System.out.println(arr.length());
			for (int i = 0; i < arr.length(); i++) {
				obj = arr.getJSONObject(i);
				dataObj = new ESearchDataObject();
				dataObj.setUrl(obj.getString("key"));
				dataObj.setContent(""+obj.getInt("doc_count")); 
				objList.add(dataObj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return objList;
	}

}
