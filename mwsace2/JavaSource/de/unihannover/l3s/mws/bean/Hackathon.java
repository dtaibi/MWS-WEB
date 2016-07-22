/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package de.unihannover.l3s.mws.bean;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.billylieurance.azuresearch.AzureSearchResultSet;
import net.billylieurance.azuresearch.AzureSearchWebQuery;
import net.billylieurance.azuresearch.AzureSearchWebResult;

import org.apache.http.HttpException;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.unihannover.l3s.mws.dao.BingcacheDao;
import de.unihannover.l3s.mws.dao.DandelioncacheDao;
import de.unihannover.l3s.mws.dao.TrackDao;
import de.unihannover.l3s.mws.dao.UtenteDao;
import de.unihannover.l3s.mws.model.Bingcache;
import de.unihannover.l3s.mws.model.Cloud;
import de.unihannover.l3s.mws.model.CloudItem;
import de.unihannover.l3s.mws.model.Generalsettings;
import de.unihannover.l3s.mws.model.SearchResult;
import de.unihannover.l3s.mws.model.SearchWebResult;
import de.unihannover.l3s.mws.model.Track;
import de.unihannover.l3s.mws.util.TextManager;
import de.unihannover.l3s.solrclient.ArchiveUrl;
import de.unihannover.l3s.solrclient.PrometheusDataObject;
import de.unihannover.l3s.solrclient.SolrService;
import eu.dandelion.DBpediaConnect;
import eu.dandelion.DandelionDataObject;
import eu.dandelion.EntityExtractionService;


/**
 * Created by JBoss Tools
 */
@ManagedBean(name="hackathon")
@ViewScoped
public class Hackathon {

	private List<String> searchterms;
	@ManagedProperty(value="#{user}")
	private User user;

	private String name;
	
	private String radiowebnews;
	
	private String diff1="";
	private String diff2="";
	private ArrayList<SearchResult> searchResultEn=null;
	private ArrayList<SearchResult> searchResultWeb=null;
	private Map<String,ArrayList<SearchResult>> searchResultWebMap=new HashMap<String, ArrayList<SearchResult>>();
	
	ArrayList<SearchResult> resultItList=null;
	Double minconfidence;
	Integer resultnumber;
	
	ArrayList<String> excludesel = new ArrayList<String>();
	ArrayList<String> includeFullText = new ArrayList<String>();
	
	
	
	
	private ArrayList<Cloud> searchClouds=new ArrayList<Cloud>();
	private String javascriptTimeline="";
	private String moreinfo="";
	
	/* -------------------------------- */

	
	private String resultId="0";
	
	private String siteSetId;
	private String siteText;
	
	private String searchsettings="";
	
	
	public String getMoreinfo() {
		return moreinfo;
	}

	public void setMoreinfo(String moreinfo) {
		this.moreinfo = moreinfo;
	}

	public String getJavascriptTimeline() {
		return javascriptTimeline;
	}

	public void setJavascriptTimeline(String javascriptTimeline) {
		this.javascriptTimeline = javascriptTimeline;
	}

	public ArrayList<Cloud> getSearchClouds() {
		return searchClouds;
	}

	public void setSearchClouds(ArrayList<Cloud> searchClouds) {
		this.searchClouds = searchClouds;
	}



	public String getDiff1() {
		return diff1;
	}

	public void setDiff1(String diff1) {
		this.diff1 = diff1;
	}

	public String getDiff2() {
		return diff2;
	}

	public void setDiff2(String diff2) {
		this.diff2 = diff2;
	}

	public ArrayList<String> getIncludeFullText() {
		return includeFullText;
	}

	public void setIncludeFullText(ArrayList<String> includeFullText) {
		this.includeFullText = includeFullText;
	}

	public ArrayList<String> getExcludesel() {
		return excludesel;
	}

	public void setExcludesel(ArrayList<String> excludesel) {
		this.excludesel = excludesel;
	}

	public Integer getResultnumber() {
		return resultnumber;
	}

	public void setResultnumber(Integer resultnumber) {
		this.resultnumber = resultnumber;
	}

	public Double getMinconfidence() {
		return minconfidence;
	}

	public void setMinconfidence(Double minconfidence) {
		this.minconfidence = minconfidence;
	}

	public String getRadiowebnews() {
		return radiowebnews;
	}

	public void setRadiowebnews(String radiowebnews) {
		this.radiowebnews = radiowebnews;
	}
	
	public String getSiteSetId() {
		return siteSetId;
	}

	public void setSiteSetId(String siteSetId) {
		this.siteSetId = siteSetId;
	}
	public String getSiteText() {
		return siteText;
	}

	public void setSiteText(String siteText) {
		this.siteText = siteText;
	}
	

	public void savelocalesettings(){
		List<Generalsettings> toremove=new ArrayList<Generalsettings>();
		
		for (Generalsettings gs : this.getUser().getUtente().getGeneralsettings()){
			if ( (gs.getType().compareTo("lang")==0) || (gs.getType().compareTo("loc")==0) )
				toremove.add(gs);
		}
		for (Generalsettings gs : toremove){
			this.getUser().getUtente().getGeneralsettings().remove(gs);
		}
		
		this.searchsettings = "";
		loadRestriction();
		UtenteDao udao=new UtenteDao();
		udao.updateUtente(this.getUser().getUtente());
	}
	
	public void showmore(){
		System.out.println("------- show more -------");
		moreinfo="";
		ArrayList<CloudItem> added=new ArrayList<CloudItem>();
		ArrayList<CloudItem> deleted=new ArrayList<CloudItem>();
		ArrayList<CloudItem> changed=new ArrayList<CloudItem>();
		 if (Integer.parseInt(diff2)!=this.searchClouds.size()){
			 Cloud cdiff=this.searchClouds.get(Integer.parseInt(diff1)).bidirectionalDiff(this.searchClouds.get(Integer.parseInt(diff2)));
			 for (CloudItem ci : cdiff.getList()){
				 if (ci.getText().startsWith("+")){
					 added.add(ci);
				 } else if (ci.getText().startsWith("-")){
					 deleted.add(ci);
				 } else {
					 changed.add(ci);
				 }
			 }
			 
			 moreinfo+="<table border=0>";
			 moreinfo+="<tr><th colspan='2'>Added concepts:</th></tr>";
			 for (CloudItem ci : added)
				 moreinfo+="<tr><td>"+ci.getText()+"</td><td>"+ci.getValue()+"</td></tr>";
			 moreinfo+="<tr><th colspan='2'>Changed concepts:</th></tr>";
			 for (CloudItem ci : changed)
				 moreinfo+="<tr><td>"+ci.getText()+"</td><td>"+ci.getValue()+"</td></tr>";
			 moreinfo+="<tr><th colspan='2'>Deleted concepts:</th></tr>";
			 for (CloudItem ci : deleted)
				 moreinfo+="<tr><td>"+ci.getText()+"</td><td>"+ci.getValue()+"</td></tr>";
			 moreinfo+="</table>";
			 
		 }	
		 System.out.println(moreinfo);
	}
	
	public void loadRestriction(){
		searchsettings="";
		searchsettings+="<b>Collection(s):</b> "+this.radiowebnews+"<br />";
		// searchsettings+="<b>Confidence level:</b> "+this.minconfidence+"<br />";
		searchsettings+="<b>Number of results:</b> "+this.resultnumber+"<br />";
		if (this.excludesel.size()>0){
			searchsettings+="<b>Exclude:</b> ";
			for (String s:this.excludesel)
				searchsettings+=s+" ";
			searchsettings+="<br />";
		}
		
		/* if (this.includeFullText.size()>0){
			searchsettings+="<b>Include Full Text:</b> ";
			for (String s:this.includeFullText)
				searchsettings+=s+" ";
			searchsettings+="<br />";
		} */
		
	}
	






	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	


	public void setSearchResultWeb(ArrayList<SearchResult> searchResultWeb) {
		this.searchResultWeb = searchResultWeb;
	}

	public ArrayList<SearchResult> getSearchResultEn() {
		return searchResultEn;
	}

	public void setSearchResultEn(ArrayList<SearchResult> searchResultEn) {
		this.searchResultEn = searchResultEn;
	}

	public String getSearchsettings() {
		return searchsettings;
	}

	public void setSearchsettings(String searchsettings) {
		this.searchsettings = searchsettings;
	}
	
	
	@PostConstruct
    public void init() {
		radiowebnews="1068 - Human Rights";
		minconfidence=0.4;
		resultnumber=1;
		includeFullText.add("true");
		/*excludelist.add("wikipedia.org");
		excludelist.add("youtube.com");
		excludelist.add("linkedin.com");
		excludelist.add("facebook.com");
		*/
		
		searchterms = new ArrayList<String>();
		searchterms.add("");

		loadRestriction();
    }
	
    public <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
    
    public List<String> getSearchterms() {
		return searchterms;
	}


	public void setSearchterms(List<String> searchterms) {
		this.searchterms = searchterms;
	}

	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ArrayList<SearchResult> resultList(String lang){
		return searchResultWebMap.get(lang);
	}
	
	public ArrayList<SearchResult> getSearchResultWeb(){
		if (searchResultWeb!=null)
			System.out.println("searchResultWeb: "+searchResultWeb.size());
		else
			System.out.println("searchResultWeb: null");
		return searchResultWeb;
	}
	
	private void dandelionNER(){
		ArrayList<DandelionDataObject> objList = null;
		Map<String, ArrayList<Integer>> dandemap=new HashMap<String, ArrayList<Integer>>();
		// DBpediaConnect dbpc=new DBpediaConnect();
		// Map<String, Integer> conceptFreq=new HashMap<String,Integer>();
		
		DandelioncacheDao dandedao=new DandelioncacheDao();
		try {
			objList=new ArrayList<DandelionDataObject>();
			for (int index=0;index<searchResultWeb.size();index++){
				Cloud c=new Cloud();
				c.setName("cloud"+index);
				c.setTitle(searchResultWeb.get(index).getTitle());
				String[] dd=searchResultWeb.get(index).getTitle().split(" - ");
				
				DateFormat df1=new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy",Locale.ENGLISH);
				DateFormat df2=new SimpleDateFormat("dd MM yyyy");
				try {
					Date date = df1.parse(dd[1].trim());
					c.setStartdate(df2.format(date));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				String searchweburl = searchResultWeb.get(index).getUrl();
				ArrayList<DandelionDataObject> ddo=dandedao.getCache(searchweburl,this.minconfidence);
				if (ddo==null)
					ddo=EntityExtractionService.postTextType(searchweburl, "url",this.minconfidence);
				
				objList.addAll(ddo);
				for (DandelionDataObject d:ddo){
					String key="";
					key = java.net.URLDecoder.decode(d.getUri(), "UTF-8");
					key=key.replace("http://en.wikipedia.org/wiki/", "");
					CloudItem clouditem=c.getItemByName(key);
					if (clouditem!=null){
						clouditem.setValue(clouditem.getValue()+1);
						if (!dandemap.get(key).contains(index))
							dandemap.get(key).add(index);
					} else {
						clouditem=new CloudItem(key,1.0);
						c.getList().add(clouditem);
						dandemap.put(key, new ArrayList<Integer>());
						dandemap.get(key).add(index);
					}
				}
				this.searchClouds.add(c);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		};
		
	}
	
	

	
	
	public <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
	{
		Map<K,V> result = new LinkedHashMap<>();
		Stream <Entry<K,V>> st = map.entrySet().stream();

		st.sorted(Comparator.comparing(e -> e.getValue())).forEachOrdered(e ->result.put(e.getKey(),e.getValue()));

		return result;
	}
	


	
	public String searcAll(int nuovo) throws IOException{
		searchResultWeb=new ArrayList<SearchResult>();
		searchClouds = new ArrayList<Cloud>();
		
		SolrService solrSerObj = new SolrService();
		List<PrometheusDataObject> queryResults; 
		
		String q="";
		// q+="\""+searchterms.get(0)+"\"";
		q+=searchterms.get(0);
		
		queryResults=solrSerObj.search(q, this.resultnumber);
		
		
		System.out.println("Query: "+q+"\nResultsConsidered: "+this.resultnumber);
		 System.out.println("----------------------------------------------------------");
		 System.out.println("Query Results: "+queryResults.size());
		 
		 for(int i=0; i<queryResults.size(); i++)
		 {
			 System.out.println("\nResult: "+(i+1));
			 System.out.println("\tTitle: "+queryResults.get(i).getTitle());
			 System.out.println("\tURL: "+queryResults.get(i).getUrl());
			 System.out.println("\tDescription: "+queryResults.get(i).getDescription());
			 System.out.println("\tTimestamp: "+queryResults.get(i).getTimeStamp());
			 System.out.println("\tVersions#: "+queryResults.get(i).getVersions().size());
			 System.out.println("\tVersionsTimeSpan: "+queryResults.get(i).getFirstCrawlingVersionDate()+" to "+queryResults.get(i).getLastCrawlingVersionDate());
			 System.out.println("\tVersions: ");
			 this.searchResultWeb.clear();
			 for(ArchiveUrl obj: queryResults.get(i).getVersions())
			 {
			 	SearchResult sr=new SearchResult();
			 	sr.setTitle(queryResults.get(i).getTitle()+" - "+obj.getTimestamp());
			 	sr.setUrl(obj.getArchiveUrl());
			 	this.searchResultWeb.add(sr);
				System.out.println("\t\tURL: "+obj.getArchiveUrl() +"\n\t\tTimestamp: "+obj.getTimestamp());
			 }
			 dandelionNER();
		 }
		
		 
		 /* String s1="http://wayback.archive-it.org/1068/20090606015922/http://www.i-indiaonline.com/";
		 String t1="Sat Jun 06 03:59:22 CEST 2009";
		 String s2="http://wayback.archive-it.org/1068/20100319192254/http://www.i-indiaonline.com/";
		 String t2="Fri Mar 19 20:22:54 CET 2010";
		 String t3="Thu Sep 02 20:53:56 CEST 2010";
		 String s3=	"http://wayback.archive-it.org/1068/20100902190721/http://www.i-indiaonline.com//";
		 String s4="http://wayback.archive-it.org/1068/20100602185839/http://www.i-indiaonline.com/";
		 String t4="Wed Jun 02 20:58:39 CEST 2010";
		 String t5="Wed Mar 02 20:31:44 CET 2011";
		 String s5=	"http://wayback.archive-it.org/1068/20110302222211/http://www.i-indiaonline.com//";
		 
		 SearchResult sr=new SearchResult();
		 sr.setTitle("I-India"+" - "+t1);
		 sr.setUrl(s1);
		 this.searchResultWeb.add(sr);
		 sr=new SearchResult();
		 sr.setTitle("I-India"+" - "+t2);
		 sr.setUrl(s2);
		 this.searchResultWeb.add(sr);
		 sr=new SearchResult();
		 sr.setTitle("I-India"+" - "+t3);
		 sr.setUrl(s3);
		 this.searchResultWeb.add(sr);
		 sr=new SearchResult();
		 sr.setTitle("I-India"+" - "+t4);
		 sr.setUrl(s4);
		 this.searchResultWeb.add(sr);
		 sr=new SearchResult();
		 sr.setTitle("I-India"+" - "+t5);
		 sr.setUrl(s5);
		 this.searchResultWeb.add(sr);
		 
		 dandelionNER();
		 */
		 // System.out.println(this.searchClouds.get(1).sameAs(this.searchClouds.get(0)));
		 
		 // System.out.println(this.searchClouds.get(0).diff(this.searchClouds.get(1)).getList().size());
		 // System.out.println(this.searchClouds.get(1).diff(this.searchClouds.get(3)).getList().size());
		 javascriptTimeline="";
		 javascriptTimeline+="var jsontimeline=JSON.parse('{\"title\": {\"media\": { \"url\": \"\",\"caption\": \"\",\"credit\": \"\"},\"text\": { \"headline\": \"Analysis of different version of the URL:<br/>"+queryResults.get(0).getUrl()+" <br/> Title: "+queryResults.get(0).getTitle()+" \",\"text\": \"\"} }, ";
		 javascriptTimeline+="\"events\": [";
		 ArrayList<String> events=new ArrayList<String>();
		 String first="0";
		 String second="0";
		 for (int k=0;k<this.searchClouds.size();k++){
			 Cloud c=this.searchClouds.get(k);
			 // System.out.println(c);
			 String dateparts[]=c.getStartdate().split(" ");
			 String startdate="\"month\": \""+dateparts[1]+"\",\"day\": \""+dateparts[0]+"\",\"year\": \""+dateparts[2]+"\"";
			 String enddate="";
			 
			 int succ=k+1;
			 while ((succ<this.searchClouds.size()) && c.sameAs(this.searchClouds.get(succ))){
				 System.out.println(k+" e "+succ+" sono uguali!");
				 succ++;
			 }
			 if (succ!=k+1){
				 /* System.out.println(k+" and "+(succ)+" are different");
				 if (succ!=this.searchClouds.size()){
					 Cloud cdiff=c.bidirectionalDiff(this.searchClouds.get(succ));
					 System.out.println(cdiff.toString());
					 System.out.println("--------------");
				 } */
				 String datepartssucc[]=this.searchClouds.get(succ-1).getStartdate().split(" ");
				 enddate="\"month\": \""+datepartssucc[1]+"\",\"day\": \""+datepartssucc[0]+"\",\"year\": \""+datepartssucc[2]+"\"";
				 k=succ-1;
				 
			 }else{
				 enddate="\"month\": \""+dateparts[1]+"\",\"day\": \""+dateparts[0]+"\",\"year\": \""+dateparts[2]+"\"";
			 }
			 
		 	 
			 // events[k]="{\"media\": {\"url\": \"\",\"caption\": \"\",\"credit\": \"\"},\"start_date\": {\"year\": \""+c.getStartdate()+"\"},\"text\": {\"headline\": \""+c.getName()+"\",\"text\": \"<div id=\\'demo"+c.getName()+"\\'></div>\"}} ";
			 events.add("{\"start_date\": {"+startdate+"},\"end_date\": {"+enddate+"},\"text\": {\"headline\": \"<a href=\\'#moreinfopanel\\' onclick=showmore("+first+","+second+")>"+c.getTitle()+"</a>\",\"text\": \"<div id=\\'demo"+c.getName()+"\\' style=\\'margin-left: -150px\\' ></div>\"}} ");
			 first=""+k;
			 second=""+succ;
		 }
		 javascriptTimeline+=String.join(",", events);
		 javascriptTimeline+=" ] }'); ";
		 javascriptTimeline+="timeline = new TL.Timeline('timeline-embed',jsontimeline);";
		 //System.out.println(javascriptTimeline);
		
		return "languageSearchStudent";	
	}
}