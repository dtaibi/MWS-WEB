package de.unihannover.l3s.mws.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.unihannover.l3s.mws.model.SearchWebResult;

public class DateManager {

	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	  }
	
	public static Date getYoutubeDate(String r){
		try {
			String urlRequest="https://www.googleapis.com/youtube/v3/videos?id="+r.replace("http://www.youtube.com/watch?v=", "")+"&key=AIzaSyCAN8oO3lz8hGnocIBA_f3ddjwA2vFwXno&part=snippet";
			System.out.println(urlRequest);
			JSONObject js=readJsonFromUrl(urlRequest);
			String code=js.getJSONArray("items").getJSONObject(0).getJSONObject("snippet").get("publishedAt").toString();
			System.out.println(code);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			return formatter.parse(code);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	// 
	public static Date getSlideshareDate(String r){
		try {
			long timstamp=System.currentTimeMillis() / 1000L;
			String hash=getHash("f4XVTsXu"+timstamp);
			String urlRequest="https://www.slideshare.net/api/2/get_slideshow?slideshow_url="+r+"&api_key=sFbqabW7&hash="+hash+"&ts="+timstamp; 
			//https://www.googleapis.com/youtube/v3/videos?id="+r.replace("http://www.youtube.com/watch?v=", "")+"&key=AIzaSyCAN8oO3lz8hGnocIBA_f3ddjwA2vFwXno&part=snippet";
			System.out.println(urlRequest);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new URL(urlRequest).openStream());
			NodeList nl = doc.getChildNodes();
			System.out.println(nl.getLength());
			for (int i=0;i<nl.getLength();i++)
				System.out.println(nl.item(i));
			String code="";
			System.out.println(code);
			if (code.compareTo("1")!=0){
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				return formatter.parse(code);
			}else
				return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getWikipediaDate(String r){
		try {
			String urlRequest="http://en.wikipedia.org/w/api.php?action=query&titles="+r.replace("http://en.wikipedia.org/wiki/", "")+"&prop=revisions&rvprop=timestamp&rvdir=newer&format=json";
			System.out.println(urlRequest);
			// URL url = new URL(urlRequest);
			// HttpURLConnection httpCon =	(HttpURLConnection) url.openConnection();
			JSONObject js=readJsonFromUrl(urlRequest);
			String code=js.getJSONObject("query").getJSONObject("pages").names().getString(0);
			String a=js.getJSONObject("query").getJSONObject("pages").getJSONObject(code).getJSONArray("revisions").getJSONObject(0).getString("timestamp");
			/*Timestamp t=new 
			Date d=new Date(a);
			System.out.println(d.getYear()+","+d.getMonth());*/
			System.out.println(a);
			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			Date date = formatter.parse(a);
			return date;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	public static String getHash(String s){
		MessageDigest md = null;
	    try {
	        md = MessageDigest.getInstance("SHA-1");
	    }
	    catch(NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return byteArrayToHexString(md.digest(s.getBytes()));
	}
	
	public static void main(String[] args) { 
		 
	    
	    // System.out.println();
		getWikipediaDate("influenza");
	}
	
	public static String byteArrayToHexString(byte[] b) {
		  String result = "";
		  for (int i=0; i < b.length; i++) {
		    result +=
		          Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		  }
		  return result;
		}
	
	public Map<Date, ArrayList<String>> getDate(SearchWebResult r){
		Map<Date, ArrayList<String>> result= new HashMap<Date, ArrayList<String>>();
		
		try {
			URL url = new URL(r.getUrl());
			HttpURLConnection httpCon = 
			(HttpURLConnection) url.openConnection();
			long date1 = httpCon.getDate();
			long date2=httpCon.getLastModified();
			if (date1 == 0 )
				System.out.println("No date information.");
			else {
				
				System.out.println("date: " + new Date(date1)+" \t\t"+ new Date(date2)+"\t\t"+r.getUrl());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
