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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
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

import de.l3s.interwebj.IllegalResponseException;
import de.l3s.interwebj.InterWeb;
import de.l3s.interwebj.jaxb.SearchResponse;
import de.l3s.interwebj.jaxb.SearchResultEntity;
import de.unihannover.l3s.mws.dao.BingcacheDao;
import de.unihannover.l3s.mws.dao.DandelioncacheDao;
import de.unihannover.l3s.mws.dao.TrackDao;
import de.unihannover.l3s.mws.dao.UrlContentDao;
import de.unihannover.l3s.mws.dao.UtenteDao;
import de.unihannover.l3s.mws.model.Generalsettings;
import de.unihannover.l3s.mws.model.SearchResult;
import de.unihannover.l3s.mws.model.SearchWebResult;
import de.unihannover.l3s.mws.model.Track;
import de.unihannover.l3s.mws.model.UrlContent;
import de.unihannover.l3s.mws.util.TextManager;
import eu.dandelion.DBpediaConnect;
import eu.dandelion.DandelionDataObject;
import eu.dandelion.EntityExtractionService;


/**
 * Created by JBoss Tools
 */
@ManagedBean(name="languagesearch")
@ViewScoped
public class LanguageSearch {

	private List<String> searchterms;
	@ManagedProperty(value="#{user}")
	private User user;

	private String name;
	
	private String tagclouddata="";
	private String tagclouddatait="";
	private String tagclouddataes="";
	private String tagclouddataen="";
	
	private String radiowebnews;
	
	private String clickedTag="";
	private ArrayList<SearchResult> searchResultEn=null;
	private ArrayList<SearchResult> searchResultWeb=null;
	private Map<String,ArrayList<SearchResult>> searchResultWebMap=new HashMap<String, ArrayList<SearchResult>>();
	
	ArrayList<SearchResult> resultItList=null;
	Double minconfidence;
	Integer resultnumber=20;
	
	ArrayList<String> excludesel = new ArrayList<String>();
	ArrayList<String> includeFullText = new ArrayList<String>();
	
	
	Map<String,Integer> categoryFreq= new HashMap<String,Integer>();
	Map<String,Integer> categoryFreqIT= new HashMap<String,Integer>();
	Map<String,Integer> categoryFreqDE= new HashMap<String,Integer>();
	Map<String,Integer> categoryFreqFR= new HashMap<String,Integer>();
	ArrayList<String> categoryFreqList = new ArrayList<String>();
	ArrayList<String> categoryFreqListIT = new ArrayList<String>();
	ArrayList<String> categoryFreqListDE = new ArrayList<String>();
	ArrayList<String> categoryFreqListFR = new ArrayList<String>();
	// ArrayList<String> excludelist=new ArrayList<String>();
	
	/* -------------------------------- */
	

	
	private ArrayList<String> siteAvailablelist1=null;
	private ArrayList<String> siteAvailablelist2=null;
	private ArrayList<String> siteAvailablelist3=null;
	
	private ArrayList<String> siteAvailableDomainlist1=null;
	
	private List<String> siteSelectedlist1;
	private List<String> siteSelectedlist2;
	private List<String> siteSelectedlist3;
	
	private List<String> siteSelectedDomainlist1;
	
	private ArrayList<SearchResult> searchResult1=null;
	private ArrayList<SearchResult> searchResult2=null;
	
	private String resultId="0";
	
	private String siteSetId;
	private String siteText;
	
	private String searchsettings="";
	
	private List<String> teasers;
	
	
	
	public ArrayList<String> getCategoryFreqList() {
		return categoryFreqList;
	}

	public void setCategoryFreqList(ArrayList<String> categoryFreqList) {
		this.categoryFreqList = categoryFreqList;
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

	/* public ArrayList<String> getExcludelist() {
		return excludelist;
	}

	public void setExcludelist(ArrayList<String> excludelist) {
		this.excludelist = excludelist;
	} */

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
	
	public String cotext(String term){
        TextManager tmgr=new TextManager();
        tmgr.setCotextrange(this.user.getUtente().getCotextrange());
        return tmgr.cotextTable(term,teasers);
    }
	
	public String multiplecotext(){
        TextManager tmgr=new TextManager();
        tmgr.setCotextrange(this.user.getUtente().getCotextrange());
        return tmgr.multipleCotextTable(searchterms, teasers);
    }
	
	public void loadRestriction(){
		searchsettings="";
		searchsettings+="<b>Search type:</b> "+this.radiowebnews+"<br />";
		searchsettings+="<b>Confidence level:</b> "+this.minconfidence+"<br />";
		searchsettings+="<b>Number of results:</b> "+this.resultnumber+"<br />";
		if (this.excludesel.size()>0){
			searchsettings+="<b>Exclude:</b> ";
			for (String s:this.excludesel)
				searchsettings+=s+" ";
			searchsettings+="<br />";
		}
		
		if (this.includeFullText.size()>0){
			searchsettings+="<b>Include Full Text:</b> ";
			for (String s:this.includeFullText)
				searchsettings+=s+" ";
			searchsettings+="<br />";
		}
		
		/*
		Set <String> siteset=new HashSet<String>();
		for (Generalsettings gs : this.user.getUtente().getGeneralsettings()){
		   if (gs.getType().compareTo("lang")==0){
				if (gs.getValue().compareTo("All")!=0){
					this.searchsettings += "<b>Language:</b> "+gs.getValue()+"<br />";
				}
			}
			if (gs.getType().compareTo("loc")==0){
				if (gs.getValue().compareTo("All")!=0){
					this.searchsettings += "<b>Localization:</b> "+gs.getValue()+"<br />";
				}
			} 
			if (gs.getType().compareTo("web")==0){
					this.searchsettings += "<b>Excluding for web:</b> "+gs.getValue()+"<br />";
			}
			if (gs.getType().compareTo("img")==0){
				this.searchsettings += "<b>Excluding for image:</b> "+gs.getValue()+"<br />";
			}
			if (gs.getType().compareTo("video")==0){
				this.searchsettings += "<b>Excluding for video:</b> "+gs.getValue()+"<br />";
			}
		}
		if (this.user.getUtente().getSitesetitem()!=null)
			for (SiteSetItem ssi : this.user.getUtente().getSitesetitem()){
				siteset.add(ssi.getSiteSet().getName());
			}
		if (siteset.size()>0)
			this.searchsettings += "<b>Selected Site set:</b> ";
		for (String s:siteset)
			this.searchsettings += s+" | ";
		if (siteset.size()>0)
			this.searchsettings += "<br />"; */
	}
	
	
	
	public ArrayList<String> getCategoryFreqListIT() {
		return categoryFreqListIT;
	}

	public void setCategoryFreqListIT(ArrayList<String> categoryFreqListIT) {
		this.categoryFreqListIT = categoryFreqListIT;
	}

	public ArrayList<String> getCategoryFreqListDE() {
		return categoryFreqListDE;
	}

	public void setCategoryFreqListDE(ArrayList<String> categoryFreqListDE) {
		this.categoryFreqListDE = categoryFreqListDE;
	}

	public ArrayList<String> getCategoryFreqListFR() {
		return categoryFreqListFR;
	}

	public void setCategoryFreqListFR(ArrayList<String> categoryFreqListFR) {
		this.categoryFreqListFR = categoryFreqListFR;
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
	
	public String getTagclouddata() {
		return tagclouddata;
	}

	public void setTagclouddata(String tagclouddata) {
		this.tagclouddata = tagclouddata;
	}

	public String getTagclouddatait() {
		return tagclouddatait;
	}

	public void setTagclouddatait(String tagclouddatait) {
		this.tagclouddatait = tagclouddatait;
	}

	public String getTagclouddataes() {
		return tagclouddataes;
	}

	public void setTagclouddataes(String tagclouddataes) {
		this.tagclouddataes = tagclouddataes;
	}

	public String getTagclouddataen() {
		return tagclouddataen;
	}

	public void setTagclouddataen(String tagclouddataen) {
		this.tagclouddataen = tagclouddataen;
	}
	
	public String getClickedTag() {
		return clickedTag;
	}

	public void setClickedTag(String clickedTag) {
		this.clickedTag = clickedTag;
	}

	public ArrayList<String> getSiteAvailablelist1() {
		return siteAvailablelist1;
	}

	public void setSiteAvailablelist1(ArrayList<String> siteAvailablelist1) {
		this.siteAvailablelist1 = siteAvailablelist1;
	}

	public ArrayList<String> getSiteAvailablelist2() {
		return siteAvailablelist2;
	}

	public void setSiteAvailablelist2(ArrayList<String> siteAvailablelist2) {
		this.siteAvailablelist2 = siteAvailablelist2;
	}

	public ArrayList<String> getSiteAvailablelist3() {
		return siteAvailablelist3;
	}

	public void setSiteAvailablelist3(ArrayList<String> siteAvailablelist3) {
		this.siteAvailablelist3 = siteAvailablelist3;
	}
	
	public ArrayList<String> getSiteAvailableDomainlist1() {
		return siteAvailableDomainlist1;
	}

	public void setSiteAvailableDomainlist1(
			ArrayList<String> siteAvailableDomainlist1) {
		this.siteAvailableDomainlist1 = siteAvailableDomainlist1;
	}

	public List<String> getSiteSelectedDomainlist1() {
		return siteSelectedDomainlist1;
	}

	public void setSiteSelectedDomainlist1(List<String> siteSelectedDomainlist1) {
		this.siteSelectedDomainlist1 = siteSelectedDomainlist1;
	}

	public List<String> getSiteSelectedlist1() {
		return siteSelectedlist1;
	}

	public void setSiteSelectedlist1(List<String> siteSelectedlist1) {
		this.siteSelectedlist1 = siteSelectedlist1;
	}

	public List<String> getSiteSelectedlist2() {
		return siteSelectedlist2;
	}

	public void setSiteSelectedlist2(List<String> siteSelectedlist2) {
		this.siteSelectedlist2 = siteSelectedlist2;
	}

	public List<String> getSiteSelectedlist3() {
		return siteSelectedlist3;
	}

	public void setSiteSelectedlist3(List<String> siteSelectedlist3) {
		this.siteSelectedlist3 = siteSelectedlist3;
	}

	public ArrayList<SearchResult> getSearchResult1() {
		/* if (searchResultWeb!=null)
			System.out.println("searchResultWeb: "+searchResultWeb.size());
		else
			System.out.println("searchResultWeb: null");
		return searchResultWeb; */
		return searchResult1;
	}

	public void setSearchResult1(ArrayList<SearchResult> searchResult1) {
		this.searchResult1 = searchResult1;
	}

	public ArrayList<SearchResult> getSearchResult2() {
		return searchResult2;
	}

	public void setSearchResultWeb(ArrayList<SearchResult> searchResultWeb) {
		this.searchResultWeb = searchResultWeb;
	}

	public void setSearchResult2(ArrayList<SearchResult> searchResult2) {
		this.searchResult2 = searchResult2;
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
		radiowebnews="web";
		minconfidence=0.6;
		/*excludelist.add("wikipedia.org");
		excludelist.add("youtube.com");
		excludelist.add("linkedin.com");
		excludelist.add("facebook.com");
		*/
		
		searchterms = new ArrayList<String>();
		siteAvailablelist1 = new ArrayList<String>();
		siteAvailablelist2 = new ArrayList<String>();
		siteAvailablelist3 = new ArrayList<String>();
		siteAvailableDomainlist1 = new ArrayList<String>();
		siteSelectedDomainlist1 = new ArrayList<String>();
		siteSelectedlist1 = new ArrayList<String>();
		siteSelectedlist2 = new ArrayList<String>();
		siteSelectedlist3 = new ArrayList<String>();
		searchterms.add("");
		searchterms.add("");
		searchterms.add("");
		searchterms.add("");
		
		
		teasers = new ArrayList<String>();
		loadRestriction();
    }
	
    public void extend() {
    	searchterms.add("");
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

	/* public ArrayList<String> getSiteAvailablelist() {
		return siteAvailablelist;
	}

	public void setSiteAvailablelist(ArrayList<String> siteAvailablelist) {
		this.siteAvailablelist = siteAvailablelist;
	}

	public List<String> getSiteSelectedlist() {
		return siteSelectedlist;
	}

	public void setSiteSelectedlist(List<String> siteSelectedlist) {
		this.siteSelectedlist = siteSelectedlist;
	} */


	
	
		


	
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
	
	
Map<Integer, Integer> indexSearchResult;
Map<String,Map<String, ArrayList<Integer>>> indexDandelion = new HashMap<String, Map<String, ArrayList<Integer>>>();


	public Map<String,Integer> createTagCloud (String q, String lang, String radiowebnews) throws IOException {
		String accountKey = "BmbX+6Sy9/VEcS5oOjurccO5MQpKr2ewvLQ2vRHBKXQ";
		// accountKey = "1145b358/226e46f8067efd14ea58d05d4b4a25c1";
		// accountKey="b5e0c812/87ddc4f508e0322abe43b9edd7b4adba";
		if (radiowebnews.compareTo("web")==0){
		
			/*
			TreeMap<String, String> params = new TreeMap<String, String>();

			params.put("media_types", "text"); // ,image
			params.put("services", "Bing"); // "YouTube,Vimeo"
			params.put("number_of_results", "10");
			params.put("page", "1");
			params.put("language", lang);

			InterWeb iw = new InterWeb("http://learnweb.l3s.uni-hannover.de/interweb/api/", "3gg1Gw_AhfGzfH8M", "N38V5RkPqp4eT_8-RCj43G96");
			
			searchResultWeb = new ArrayList<SearchResult>();
			try {
				SearchResponse response = iw.search(q, params);
				
				List<String> ripetiz=new ArrayList<String>();
				UrlContent obj1 = new UrlContent();
				int temp_counter =1;
				
				
				for(SearchResultEntity result :  response.getQuery().getResults())
				{
					
					if(temp_counter==1)
					{
						obj1.SetUrl(result.getUrl());
						System.out.println(obj1.GetUrl());
						obj1.SetContent();
						System.out.println(obj1.GetContent());
						UrlContentDao ContentSave = new UrlContentDao();
						ContentSave.SaveURLContent(obj1); 
						temp_counter++;
					}
					SearchWebResult r=new SearchWebResult();
					r.setTitle(result.getTitle());
					r.setDescription(result.getDescription());
					teasers.add(result.getDescription());
					r.setUrl(result.getUrl());
					if (!ripetiz.contains(r.getUrl())){
						ripetiz.add(r.getUrl());
						searchResultWeb.add(r);
						
					}
				}
			} catch (IllegalResponseException e) {
				e.printStackTrace();
			} */
			
			/*
			AzureSearchWebQuery aq = new AzureSearchWebQuery();
			aq.setAppid(accountKey);
			if (lang.compareTo("en")==0)
				q+=" language:en ";
			else
				q+=" loc:"+lang+ " language:"+lang+" ";
			
			for (String s : this.excludesel)
				q+="-site:"+s+" ";
			
			aq.setQuery(q);
			System.out.println(q);
			List<AzureSearchWebResult> arsall=new ArrayList<AzureSearchWebResult>();
			int conta=-1;
	        for (int i=1;i<=1 && conta!=0;i++){
	            aq.setPerPage(this.resultnumber);
	            aq.setPage(i);
				aq.doQuery();
				AzureSearchResultSet<AzureSearchWebResult> ars = aq.getQueryResult();
				if (ars.getAsrs().size()<this.resultnumber) conta=0;
				for (AzureSearchWebResult anr : ars){
					arsall.add(anr);
				}
			}
			
			searchResultWeb = new ArrayList<SearchResult>();
	
			List<String> ripetiz=new ArrayList<String>();
			for (AzureSearchWebResult anr : arsall){			
				SearchWebResult r=new SearchWebResult();
				r.setTitle(anr.getTitle());
				r.setDescription(anr.getDescription());
				r.setUrl(anr.getUrl());
				if (!ripetiz.contains(r.getUrl())){
					ripetiz.add(r.getUrl());
					searchResultWeb.add(r);
				}
			} */
	}else{
		searchResultWeb = new ArrayList<SearchResult>();
        try
        {
        	String url = "http://news.google.ca/news?ned="+lang+"&hl="+lang+"&output=rss&num=40&q="+URLEncoder.encode(q, "UTF-8");
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse(url);
 
            doc.getDocumentElement().normalize();
            NodeList items = doc.getElementsByTagName("channel");
            System.out.println(url);
            for (int i = 0; i < items.getLength(); i++)
            {
                Node n = items.item(i);
                if (n.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                Element e = (Element) n;
 
                NodeList titleList = e.getElementsByTagName("item");
                
                for (int j = 0; j < titleList.getLength(); j++)
	            {
                
                	Node n1 = titleList.item(j);
	                if (n.getNodeType() != Node.ELEMENT_NODE)
	                    continue;
	                Element e1 = (Element) n1;	                
	                NodeList titleList1 = e1.getElementsByTagName("description");	                
	                Element descElem = (Element) titleList1.item(0);
	                Node descNode = descElem.getChildNodes().item(0);

	                NodeList titleList2 = e1.getElementsByTagName("title");	                
	                Element titleElem = (Element) titleList2.item(0);
	                Node titleNode = titleElem.getChildNodes().item(0);

	                NodeList titleList3 = e1.getElementsByTagName("link");	                
	                Element urlElem = (Element) titleList3.item(0);
	                Node urlNode = urlElem.getChildNodes().item(0);
	                
        			SearchWebResult r=new SearchWebResult();
        			r.setTitle(titleNode.getNodeValue().replaceAll("\\<.*?>"," "));
        			r.setDescription(descNode.getNodeValue().replaceAll("\\<.*?>"," "));
        			r.setUrl(urlNode.getNodeValue().split("url=")[1]);
        			searchResultWeb.add(r);

	            }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
		System.out.println("Dandelion Start");
		indexSearchResult=new HashMap<Integer, Integer> ();
		String textToAnalyse="";
		for (int index=0;index<searchResultWeb.size();index++){
			textToAnalyse+=" "+searchResultWeb.get(index).getTitle()+" "+((SearchWebResult)searchResultWeb.get(index)).getDescription();
			indexSearchResult.put(index, textToAnalyse.length());
		}	
		
		// System.out.println(textToAnalyse);
		
		ArrayList<DandelionDataObject> objList = null;
		Map<String, ArrayList<Integer>> dandemap=new HashMap<String, ArrayList<Integer>>();
		DBpediaConnect dbpc=new DBpediaConnect();
		Map<String, Integer> conceptFreq=new HashMap<String,Integer>();
		try {
			if (this.includeFullText.size()==0)
				objList=EntityExtractionService.postText(textToAnalyse, this.minconfidence);
			else{
				objList=new ArrayList<DandelionDataObject>();
				for (int index=0;index<searchResultWeb.size();index++){
					System.out.println(index+"::"+searchResultWeb.get(index).getUrl());
					
					ArrayList<DandelionDataObject> ddo=EntityExtractionService.postTextType(searchResultWeb.get(index).getUrl(), "url",this.minconfidence);
					objList.addAll(ddo);
					for (DandelionDataObject d:ddo){
						String key="";
						if (lang.compareTo("en")!=0)
							key=dbpc.getEng(d.getUri());
						else
							key = d.getUri();
						
						if (conceptFreq.containsKey(key)){
							conceptFreq.put(key, conceptFreq.get(key)+1);
							if (!dandemap.get(key).contains(index))
								dandemap.get(key).add(index);
						} else {
							conceptFreq.put(key,1);
							dandemap.put(key, new ArrayList<Integer>());
							dandemap.get(key).add(index);
						}
					}
					
				}
			}
		
		} catch (HttpException e) {
			e.printStackTrace();
		};
		
		
		if (this.includeFullText.size()==0){
			for (int i=0; i<objList.size(); i++)	
			{
			
				int f=0;
				while ((objList.get(i).getWordStartPosition() > indexSearchResult.get(f)) && f<indexSearchResult.size()){
					f++;
					// System.out.println(f+"::"+indexSearchResult.get(f)+"::"+indexSearchResult.size()+"::"+objList.get(i).getWordStartPosition());
				}
				String key="";
				if (lang.compareTo("en")!=0)
					key=dbpc.getEng(objList.get(i).getUri());
				else
					key = objList.get(i).getUri();
				
				if (conceptFreq.containsKey(key)){
					conceptFreq.put(key, conceptFreq.get(key)+1);
					if (!dandemap.get(key).contains(f))
						dandemap.get(key).add(f);
				} else {
					conceptFreq.put(key,1);
					dandemap.put(key, new ArrayList<Integer>());
					dandemap.get(key).add(f);
				}
				
					try {
						if (objList.get(i).getTypes()!=null)
							for (int k=0; k<objList.get(i).getTypes().length();k++){
								String kk=objList.get(i).getTypes().get(k).toString();
								if (lang.compareTo("en")==0){
									if (categoryFreq.containsKey(kk)){
										categoryFreq.put(kk, categoryFreq.get(kk)+1);
									}else{
										categoryFreq.put(kk, 1);
									}
								}
								if (lang.compareTo("de")==0){
									if (categoryFreqDE.containsKey(kk)){
										categoryFreqDE.put(kk, categoryFreqDE.get(kk)+1);
									}else{
										categoryFreqDE.put(kk, 1);
									}
								}
								if (lang.compareTo("it")==0){
									if (categoryFreqIT.containsKey(kk)){
										categoryFreqIT.put(kk, categoryFreqIT.get(kk)+1);
									}else{
										categoryFreqIT.put(kk, 1);
									}
								}
								if (lang.compareTo("fr")==0){
									if (categoryFreqFR.containsKey(kk)){
										categoryFreqFR.put(kk, categoryFreqFR.get(kk)+1);
									}else{
										categoryFreqFR.put(kk, 1);
									}
								}
							}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
			
		}
		indexDandelion.put(lang,dandemap);
		
		searchResultWebMap.put(lang, searchResultWeb);	
		if (lang.compareTo("it")==0){
			categoryFreqIT=sortByValue(categoryFreqIT);
			for (String kkk:categoryFreqIT.keySet()){
				categoryFreqListIT.add(categoryFreqIT.get(kkk)+"::"+kkk);
			}
			//categoryFreqListIT=(ArrayList<String>) iterableReverseList(categoryFreqListIT);
			Collections.reverse((List<?>) categoryFreqListIT);
		}
		if (lang.compareTo("de")==0){
			categoryFreqDE=sortByValue(categoryFreqDE);
			for (String kkk:categoryFreqDE.keySet()){
				categoryFreqListDE.add(categoryFreqDE.get(kkk)+"::"+kkk);
			}
			//categoryFreqListDE=(ArrayList<String>) iterableReverseList(categoryFreqListDE);
			Collections.reverse((List<?>) categoryFreqListDE);
		}
		if (lang.compareTo("fr")==0){
			categoryFreqFR=sortByValue(categoryFreqFR);
			for (String kkk:categoryFreqFR.keySet()){
				categoryFreqListFR.add(categoryFreqFR.get(kkk)+"::"+kkk);
			}
			//categoryFreqListFR=(ArrayList<String>) iterableReverseList(categoryFreqListFR);
			Collections.reverse((List<?>) categoryFreqListFR);
		}
		if (lang.compareTo("en")==0){
			categoryFreq=sortByValue(categoryFreq);
			for (String kkk:categoryFreq.keySet()){
				categoryFreqList.add(categoryFreq.get(kkk)+"::"+kkk);
			}
			// categoryFreqList=
			Collections.reverse((List<?>) categoryFreqList);
		}
		// resultItList= new ArrayList<SearchResult>(searchResultWeb);
		
		
		return conceptFreq;
		
	}
	
	private Map<String,Integer> createTagCloud2 (String lang){
		if (lang.compareTo("en")==0){
			this.minconfidence=0.85;
		}else{
			this.minconfidence=0.75;
		}

	System.out.println("Dandelion Start");
	indexSearchResult=new HashMap<Integer, Integer> ();
	String textToAnalyse="";
	for (int index=0;index<searchResultWeb.size();index++){
		textToAnalyse+=" "+searchResultWeb.get(index).getTitle()+" "+((SearchWebResult)searchResultWeb.get(index)).getDescription();
		indexSearchResult.put(index, textToAnalyse.length());
	}	
	
	// System.out.println(textToAnalyse);
	
	ArrayList<DandelionDataObject> objList = null;
	Map<String, ArrayList<Integer>> dandemap=new HashMap<String, ArrayList<Integer>>();
	DBpediaConnect dbpc=new DBpediaConnect();
	Map<String, Integer> conceptFreq=new HashMap<String,Integer>();
	DandelioncacheDao dandedao=new DandelioncacheDao();
	try {
		if (this.includeFullText.size()==0)
			objList=EntityExtractionService.postText(textToAnalyse, this.minconfidence);
		else{
			objList=new ArrayList<DandelionDataObject>();
			for (int index=0;index<searchResultWeb.size();index++){
				String searchweburl = searchResultWeb.get(index).getUrl();
				// System.out.println(index+"::"+searchweburl);
				ArrayList<DandelionDataObject> ddo=dandedao.getCache(searchweburl,this.minconfidence);
				if (ddo==null)
					ddo=EntityExtractionService.postTextType(searchweburl, "url",this.minconfidence);
				
				objList.addAll(ddo);
				for (DandelionDataObject d:ddo){
					String key="";
					if (lang.compareTo("en")!=0){
						key=dbpc.getEng(d.getUri());
						key=key.replace("http://dbpedia.org/resource/", "");
					}else{
						key = java.net.URLDecoder.decode(d.getUri(), "UTF-8");
						key=key.replace("http://en.wikipedia.org/wiki/", "");
					}
					if (conceptFreq.containsKey(key)){
						conceptFreq.put(key, conceptFreq.get(key)+1);
						if (!dandemap.get(key).contains(index))
							dandemap.get(key).add(index);
					} else {
						conceptFreq.put(key,1);
						dandemap.put(key, new ArrayList<Integer>());
						dandemap.get(key).add(index);
					}
					/*if (freqRel.containsKey(lang+":"+index+":"+key)){
						freqRel.put(lang+":"+index+":"+key,freqRel.get(lang+":"+index+":"+key)+1);
					}else{
						freqRel.put(lang+":"+index+":"+key, 1);
					}*/
				}
			}
		}
	
	} catch (HttpException e) {
		e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	};
	
	
	if (this.includeFullText.size()==0){
		for (int i=0; i<objList.size(); i++)	
		{
		
			int f=0;
			while ((objList.get(i).getWordStartPosition() > indexSearchResult.get(f)) && f<indexSearchResult.size()){
				f++;
				// System.out.println(f+"::"+indexSearchResult.get(f)+"::"+indexSearchResult.size()+"::"+objList.get(i).getWordStartPosition());
			}
			String key="";
			if (lang.compareTo("en")!=0){
				key=dbpc.getEng(objList.get(i).getUri());
				key=key.replace("http://dbpedia.org/resource/", "");
			}else{
				key = objList.get(i).getUri();
				key=key.replace("http://en.wikipedia.org/wiki/", "");
			}
			if (conceptFreq.containsKey(key)){
				conceptFreq.put(key, conceptFreq.get(key)+1);
				if (!dandemap.get(key).contains(f))
					dandemap.get(key).add(f);
			} else {
				conceptFreq.put(key,1);
				dandemap.put(key, new ArrayList<Integer>());
				dandemap.get(key).add(f);
			}
			
				try {
					if (objList.get(i).getTypes()!=null)
						for (int k=0; k<objList.get(i).getTypes().length();k++){
							String kk=objList.get(i).getTypes().get(k).toString();
							if (lang.compareTo("en")==0){
								if (categoryFreq.containsKey(kk)){
									categoryFreq.put(kk, categoryFreq.get(kk)+1);
								}else{
									categoryFreq.put(kk, 1);
								}
							}
							if (lang.compareTo("de")==0){
								if (categoryFreqDE.containsKey(kk)){
									categoryFreqDE.put(kk, categoryFreqDE.get(kk)+1);
								}else{
									categoryFreqDE.put(kk, 1);
								}
							}
							
						}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		
	}
	indexDandelion.put(lang,dandemap);
	
	searchResultWebMap.put(lang, searchResultWeb);	
	
	if (lang.compareTo("de")==0){
		categoryFreqDE=sortByValue(categoryFreqDE);
		for (String kkk:categoryFreqDE.keySet()){
			categoryFreqListDE.add(categoryFreqDE.get(kkk)+"::"+kkk);
		}
		Collections.reverse((List<?>) categoryFreqListDE);
	}
	
	if (lang.compareTo("en")==0){
		categoryFreq=sortByValue(categoryFreq);
		for (String kkk:categoryFreq.keySet()){
			categoryFreqList.add(categoryFreq.get(kkk)+"::"+kkk);
		}
		Collections.reverse((List<?>) categoryFreqList);
	}
	
	return conceptFreq;
}	
	
	public <K, V extends Comparable<? super V>> Map<K, V> 
    sortByValue( Map<K, V> map )
{
      Map<K,V> result = new LinkedHashMap<>();
     Stream <Entry<K,V>> st = map.entrySet().stream();

     st.sorted(Comparator.comparing(e -> e.getValue()))
          .forEachOrdered(e ->result.put(e.getKey(),e.getValue()));

     return result;
}
	
	public String filterSearch(String lang){
			
		searchResultEn=new ArrayList<SearchResult>();
		Map<String, ArrayList<Integer>> dandemap=indexDandelion.get(lang);
		
		System.out.println("lang:"+lang+" -- "+indexDandelion.containsKey(lang)+" __ "+dandemap);
		
		for (String key : dandemap.keySet()){
			String keyclean=key.replace("http://en.wikipedia.org/wiki/", "").replace("http://dbpedia.org/resource/","").replace("http://de.wikipedia.org/wiki/", "").replace("http://it.wikipedia.org/wiki/", "").replace("http://fr.wikipedia.org/wiki/", "");
			if(this.clickedTag.compareTo(keyclean)==0){
				for (Integer ind : dandemap.get(key)){
					SearchWebResult swr=new SearchWebResult();
					ArrayList<SearchResult> searchResultWebLoc=searchResultWebMap.get(lang);
					swr.setDescription(((SearchWebResult)searchResultWebLoc.get(ind)).getDescription());
					swr.setTitle(searchResultWebLoc.get(ind).getTitle());
					swr.setUrl(searchResultWebLoc.get(ind).getUrl());
					searchResultEn.add(swr);
				}
			}
		}
		return "";
	}

	
private void searching(String q,String lang) {
		
		System.out.println("Cached value not available "+q+" "+lang);
		
		TreeMap<String, String> params = new TreeMap<String, String>();

		params.put("media_types", "text"); // ,image
		params.put("services", "Bing"); // "YouTube,Vimeo"
		params.put("number_of_results", ""+this.resultnumber);
		params.put("page", "1");
		params.put("language", lang);

		InterWeb iw = new InterWeb("http://learnweb.l3s.uni-hannover.de/interweb/api/", "3gg1Gw_AhfGzfH8M", "N38V5RkPqp4eT_8-RCj43G96");
		BingcacheDao bcdao=new BingcacheDao();
		searchResultWeb = new ArrayList<SearchResult>();
		try {
			SearchResponse response = iw.search(q, params);
			
			List<String> ripetiz=new ArrayList<String>();
			UrlContent obj1 = new UrlContent();
			int temp_counter =1;
			
			
			for(SearchResultEntity result :  response.getQuery().getResults())
			{
				
				/*if(temp_counter==1)
				{
					obj1.SetUrl(result.getUrl());
					System.out.println(obj1.GetUrl());
					obj1.SetContent();
					System.out.println(obj1.GetContent());
					UrlContentDao ContentSave = new UrlContentDao();
					ContentSave.SaveURLContent(obj1); 
					temp_counter++;
				}*/
				SearchWebResult r=new SearchWebResult();
				r.setTitle(result.getTitle());
				r.setDescription(result.getDescription());
				teasers.add(result.getDescription());
				r.setUrl(result.getUrl());
				if (!ripetiz.contains(r.getUrl())){
					ripetiz.add(r.getUrl());
					searchResultWeb.add(r);
					
				}
				// bcdao.addToCache(r, "BN_"+lang, q+":"+this.resultnumber);
			}
		} catch (IllegalResponseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}	
	
	public String searcAll(int nuovo) throws IOException{
		
		// accountKey = "TTtbr2RdFtN8bvzLZG9SIEgkDa7ecUDNrAkdwEy86UI"; // davide.taibi@libero.it

		for (int l=0;l<searchterms.size();l++)
			if (searchterms.get(l).trim().compareTo("")==0)
				searchterms.set(l, searchterms.get(0));
				
		String q="";
		q+="\""+searchterms.get(2)+"\"";
		
		Map<String, Integer> conceptFreq=new HashMap<String, Integer>();
		
		// TreeMap<String, String> params = new TreeMap<String, String>();

		/*params.put("media_types", "text"); // ,image
		params.put("services", "Bing"); // "YouTube,Vimeo"
		params.put("number_of_results", "10");
		params.put("page", "1");
		params.put("language", "de");

		InterWeb iw = new InterWeb("http://learnweb.l3s.uni-hannover.de/interweb/api/", "3gg1Gw_AhfGzfH8M", "N38V5RkPqp4eT_8-RCj43G96");
		*/
		// searchResultWeb = new ArrayList<SearchResult>();
		BingcacheDao bcdao=new BingcacheDao();
		searchResultWeb = bcdao.getCache(q+":"+this.resultnumber, "BN_de");
		if (searchResultWeb==null)
			searching(q,"de");
		
		conceptFreq=createTagCloud2("de");
		conceptFreq=sortByValue(conceptFreq);
		
		// conceptFreq=createTagCloud(q, "de", this.radiowebnews);
		
		tagclouddata=" var wordsde = [ ";
		for (String key : conceptFreq.keySet()){
			Integer size=conceptFreq.get(key); if (size>12) size=12; 
			tagclouddata+=" { text: \""+key.replace("http://dbpedia.org/resource/", "")+"\", size: "+size*7+"}, ";
			System.out.println("de;"+key+";"+conceptFreq.get(key));
		}
		// tagclouddata+=" ]; $('#demo').jQCloud(words, {heigth: 500, width: 1032, autoResize: true}); ";
		tagclouddata+="]; d3.layout.cloud().size([1000, 500]).words(wordsde).rotate(0).font(\"Impact\").fontSize(function(d) { return d.size; }).on(\"end\", drawde).start();";
		
		// tagclouddata+=" function showdetail(lang,text){ alert('You clicked the word '+lang+' in '+text); }";
		q="";
		q+="\""+searchterms.get(1)+"\"";
		// q+=" loc:it language:it ";
		
		// conceptFreq=createTagCloud(q, "it", this.radiowebnews);			
		searchResultWeb = bcdao.getCache(q+":"+this.resultnumber, "BN_it");
		if (searchResultWeb==null)
			searching(q,"it");
		
		conceptFreq=createTagCloud2("it");
		conceptFreq=sortByValue(conceptFreq);
		
		tagclouddatait=" var wordsit = [ ";
		for (String key : conceptFreq.keySet()){
			Integer size=conceptFreq.get(key); if (size>12) size=12;
			tagclouddatait+=" { text: \""+key.replace("http://dbpedia.org/resource/", "")+"\", size: "+size*7+"}, ";
			System.out.println("it;"+key+";"+conceptFreq.get(key));
		}
		// tagclouddatait+=" ]; $('#demoit').jQCloud(wordsit, {heigth: 500, width: 1032, autoResize: true});";
		tagclouddatait+="]; d3.layout.cloud().size([1000, 500]).words(wordsit).rotate(0).font(\"Impact\").fontSize(function(d) { return d.size; }).on(\"end\", drawit).start();";
		
		q="";
		q+="\""+searchterms.get(3)+"\"";
		// q+=" loc:fr language:fr ";
		// System.out.println(q);
		// conceptFreq=createTagCloud(q, "fr", this.radiowebnews);	
		searchResultWeb = bcdao.getCache(q+":"+this.resultnumber, "BN_fr");
		if (searchResultWeb==null)
			searching(q,"fr");
		
		conceptFreq=createTagCloud2("fr");
		conceptFreq=sortByValue(conceptFreq);
		tagclouddataes=" var wordses = [ ";
		for (String key : conceptFreq.keySet()){
			Integer size=conceptFreq.get(key); if (size>12) size=12;
			tagclouddataes+=" { text: \""+key.replace("http://dbpedia.org/resource/", "")+"\", size: "+size*7+"}, ";
			System.out.println("fr;"+key+";"+conceptFreq.get(key));
		}
		// tagclouddataes+=" ]; $('#demoes').jQCloud(wordses, {heigth: 500, width: 1032, autoResize: true});";
		tagclouddataes+="]; d3.layout.cloud().size([1000, 500]).words(wordses).rotate(0).font(\"Impact\").fontSize(function(d) { return d.size; }).on(\"end\", drawes).start();";
		q="";
		q+="\""+searchterms.get(0)+"\"";
		// q+=" language:en ";

		searchResultWeb = bcdao.getCache(q+":"+this.resultnumber, "BN_en");
		if (searchResultWeb==null)
			searching(q,"en");
		
		conceptFreq=createTagCloud2("en");
		conceptFreq=sortByValue(conceptFreq);
		// conceptFreq=createTagCloud(q, "en", this.radiowebnews);			
		tagclouddataen=" var wordsen = [ ";
		for (String key : conceptFreq.keySet()){
			Integer size=conceptFreq.get(key); if (size>12) size=12;
			tagclouddataen+=" { text: \""+key.replace("http://en.wikipedia.org/wiki/", "")+"\", size: "+size*7+"}, ";
			System.out.println("en;"+key+";"+conceptFreq.get(key));
		}
		// tagclouddataen+=" ]; $('#demoen').jQCloud(wordsen);";
		tagclouddataen+="]; d3.layout.cloud().size([1000, 500]).words(wordsen).rotate(0).font(\"Impact\").fontSize(function(d) { return d.size; }).on(\"end\", drawen).start();";
		
		Track track=new Track();
	        track.setDate((new GregorianCalendar()).getTime());
	        track.setOperation("languagesearch");
	        track.setParam1(q);
	        track.setParam2(this.radiowebnews);
	        track.setParam3("");
	        track.setUtente(this.user.getUtente());
	        TrackDao td=new TrackDao();
	        td.addTrack(track);
			
		if (searchterms.size()==0) searchterms.add("");
		return "languageSearch";	
	}
}