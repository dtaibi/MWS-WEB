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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;


import net.billylieurance.azuresearch.AzureSearchNewsQuery;
import net.billylieurance.azuresearch.AzureSearchNewsResult;
import net.billylieurance.azuresearch.AzureSearchResultSet;
import net.billylieurance.azuresearch.AzureSearchWebQuery;
import net.billylieurance.azuresearch.AzureSearchWebResult;

import org.apache.http.HttpException;

import com.google.common.base.Joiner;

import de.unihannover.l3s.mws.dao.GeneralsettingsDao;
import de.unihannover.l3s.mws.dao.SiteSetDao;
import de.unihannover.l3s.mws.dao.StoryboardDao;
import de.unihannover.l3s.mws.dao.TrackDao;
import de.unihannover.l3s.mws.dao.UtenteDao;
import de.unihannover.l3s.mws.model.Generalsettings;
import de.unihannover.l3s.mws.model.SearchImageResult;
import de.unihannover.l3s.mws.model.SearchResult;
import de.unihannover.l3s.mws.model.SearchVideoResult;
import de.unihannover.l3s.mws.model.SearchWebResult;
import de.unihannover.l3s.mws.model.SiteSet;
import de.unihannover.l3s.mws.model.SiteSetItem;
import de.unihannover.l3s.mws.model.Storyboard;
import de.unihannover.l3s.mws.model.StoryboardItem;
import de.unihannover.l3s.mws.model.Track;
import de.unihannover.l3s.mws.model.YData;
import de.unihannover.l3s.mws.util.StatsManager;
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
	private String searchtype="Web";
	private ArrayList<String> searchtypelist=null;

	private List<String> searchterms;
	@ManagedProperty(value="#{user}")
	private User user;

	private String name;

	private Storyboard storyboard;
	
	private String tagclouddata="";
	private String tagclouddatait="";
	private String tagclouddataes="";
	private String tagclouddataen="";
	
	private String radiowebnews;
	
	private String clickedTag="";
	private ArrayList<SearchResult> searchResultEn=null;
	private ArrayList<SearchResult> searchResultWeb=null;
	
	/* -------------------------------- */
	
	
	private String searchWeightedDataPie1="";
	
	private String searchLangDataPie1="";
	
	private ArrayList<String> siteAvailablelist1=null;
	private ArrayList<String> siteAvailablelist2=null;
	private ArrayList<String> siteAvailablelist3=null;
	
	private ArrayList<String> siteAvailableDomainlist1=null;
	
	private List<String> siteSelectedlist1;
	private List<String> siteSelectedlist2;
	private List<String> siteSelectedlist3;
	
	private List<String> siteSelectedDomainlist1;
	
	private List<String> excludeWeb;
	private List<String> excludeImg;
	private List<String> excludeVid;
	
	private ArrayList<SearchResult> searchResult1=null;
	private ArrayList<SearchResult> searchResult2=null;
	
	
	
	
	
	

	
	private String resultId="0";
	private String storydata;
	
	private List<StoryboardItem> newstoryboardlist;
	
	private List<Generalsettings> langlist;
	private String langlistsel;

	private List<Generalsettings> loclist;
	private String loclistsel;
	private Map<String,String> locMap;
	
	private String storyboardsel;
	private List<Storyboard> userStoryboards;
	
	private String siteSetId;
	private String siteText;
	private String contextmenuchild;
	
	private String searchsettings="";
	
	private List<String> teasers;
	
	public enum Type {
	    REFINE_PIE,
	    REFINE_WEIGHTED_PIE,
	    REFINE_DOMAIN_PIE
	}
	
	List<StoryboardItem> storyboardlist;
	
	public List<StoryboardItem> getStoryboardlist() {
		return storyboardlist;
	}

	public void setStoryboardlist(List<StoryboardItem> storyboardlist) {
		this.storyboardlist = storyboardlist;
	}

	public List<Storyboard>  getUserStoryboards() {
		return userStoryboards;
	}

	public void setUserStoryboards(List<Storyboard> userStoryboards) {
		this.userStoryboards = userStoryboards;
	}
	
	public Storyboard getStoryboard() {
		return storyboard;
	}

	public void setStoryboard(Storyboard storyboard) {
		this.storyboard = storyboard;
	}
	
	public String getStoryboardsel() {
		return storyboardsel;
	}

	public void setStoryboardsel(String storyboardsel) {
		this.storyboardsel = storyboardsel;
	}
	
	public String getRadiowebnews() {
		return radiowebnews;
	}

	public void setRadiowebnews(String radiowebnews) {
		this.radiowebnews = radiowebnews;
	}
	
	public String getContextmenuchild() {
		return contextmenuchild; 
	}

	public void setContextmenuchild(String contextmenuchild) {
		this.contextmenuchild = contextmenuchild;
	}	
	
	
	public String getStorydata() {
		return storydata;
	}

	public void setStorydata(String storydata) {
		this.storydata = storydata;
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
	
    public List<String> getTeasers() {
        return teasers;
    }
 
    public void setTeasers(List<String> teasers) {
        this.teasers = teasers;
    }
	
	public void addToSiteSetting(){
		Long sitesetid=Long.parseLong(this.siteSetId);
		SiteSet ss=null;
		SiteSetDao ssd=new SiteSetDao();
		if (sitesetid==0){
			ss=new SiteSet();
			ss.setName("New Group");
			ss.setUtente(this.user.getUtente());
		}else{
			ss=ssd.getSiteSetById(sitesetid);
		}
		SiteSetItem ssi=new SiteSetItem();
		ssi.setSiteSet(ss);
		ssi.setUrl(this.siteText);
		if (ss.getSiteSetItem() == null)
			ss.setSiteSetItem(new ArrayList<SiteSetItem>());
		ss.getSiteSetItem().add(ssi);
		ssd.addSiteSet(ss);
		loadContextMenu();
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
		
		// if (langlist.size()!=langlistsel.size())
			for (Generalsettings gs : langlist){
				/* boolean trovato=false;
				for (String id : langlistsel){
					if (gs.getId() == Integer.parseInt(id)) {
						trovato = true; break;
					}
				}
				if (trovato==false) */
				// for (String id : langlistsel)
					if (gs.getId() == Integer.parseInt(langlistsel)) { 
						this.getUser().getUtente().getGeneralsettings().add(gs);
					}
						
			}
		// if (loclist.size()!=loclistsel.size())
			for (Generalsettings gs : loclist){
				/* boolean trovato=false;
				for (String id : loclistsel){
					if (gs.getId() == Integer.parseInt(id)) {
						trovato = true; break;
					}
				}
				if (trovato==false) */
				// for (String id : loclistsel)
					if (gs.getId() == Integer.parseInt(loclistsel)){ 
						this.getUser().getUtente().getGeneralsettings().add(gs);
					}
			}
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
	
	private boolean hasGS(Generalsettings g){
		for (Generalsettings gs : this.user.getUtente().getGeneralsettings()){
			if (gs.getId() == g.getId())
				return true;
		}
		return false;
	}
	

	
	private void loadList(){
		List<Generalsettings> listags;
		GeneralsettingsDao gsdao=new GeneralsettingsDao();
		listags=gsdao.getAllGeneralsettings();
		
		langlist = new ArrayList<Generalsettings>();
		langlistsel = new String();
		loclist = new ArrayList<Generalsettings>();
		loclistsel = new String();
		
		for (Generalsettings gs : listags){
			if (gs.getType().compareTo("lang")==0){
				langlist.add(gs);
				if (hasGS(gs))	{
					langlistsel=""+gs.getId();
				}
			} else if (gs.getType().compareTo("loc")==0){
				loclist.add(gs);
				if (hasGS(gs))	{
					loclistsel=""+gs.getId();
				}
			}
		}
		/* 
		if (langlistsel.size()==0)
			for (Generalsettings gs : langlist)
				langlistsel.add(""+gs.getId());
		if (loclistsel.size()==0)
			for (Generalsettings gs : loclist)
				loclistsel.add(""+gs.getId());
		*/
	}
	
	private void loadRestriction(){
		searchsettings="";
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
			this.searchsettings += "<br />";
	}
	
	public List<Generalsettings> getLoclist() {
		return loclist;
	}

	public void setLoclist(List<Generalsettings> loclist) {
		this.loclist = loclist;
	}

	public String getLoclistsel() {
		return loclistsel;
	}

	public void setLoclistsel(String loclistsel) {
		this.loclistsel = loclistsel;
	}

	public List<Generalsettings> getLanglist() {
		return langlist;
	}

	public void setLanglist(List<Generalsettings> langlist) {
		this.langlist = langlist;
	}

	public String getLanglistsel() {
		return langlistsel;
	}

	public void setLanglistsel(String langlistsel) {
		this.langlistsel = langlistsel;
	}

	/*public boolean isNameEmpty() {
		System.out.println("NAME"+name+"<");
		if (this.name.trim().compareTo("")==0)
			return true;
		
		return false;
	}*/

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

	public String getSearchWeightedDataPie1() {
		return searchWeightedDataPie1;
	}

	public void setSearchWeightedDataPie1(String searchWeightedDataPie1) {
		this.searchWeightedDataPie1 = searchWeightedDataPie1;
	}

	public String getSearchLangDataPie1() {
		return searchLangDataPie1;
	}

	public void setSearchLangDataPie1(String searchLangDataPie1) {
		this.searchLangDataPie1 = searchLangDataPie1;
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
		return searchResult1;
	}

	public void setSearchResult1(ArrayList<SearchResult> searchResult1) {
		this.searchResult1 = searchResult1;
	}

	public ArrayList<SearchResult> getSearchResult2() {
		return searchResult2;
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
		
		Set<Generalsettings> gensets=this.user.getUtente().getGeneralsettings();
		excludeWeb = new ArrayList<String>();
		excludeImg = new ArrayList<String>();
		excludeVid = new ArrayList<String>();
		for (Generalsettings gs : gensets){
			if (gs.getType().compareTo("web")==0) excludeWeb.add(gs.getValue());
			if (gs.getType().compareTo("img")==0) excludeImg.add(gs.getValue());
			if (gs.getType().compareTo("video")==0) excludeVid.add(gs.getValue());
		}
		loadList();
		locMap = new HashMap<String,String>();
		locMap.put("Italy","it");
		locMap.put("Spain","es");
		locMap.put("France","fr");
		locMap.put("Germany","de");
		locMap.put("United Kingdom","uk");
		storyboardlist = new ArrayList<StoryboardItem>();
		newstoryboardlist = new ArrayList<StoryboardItem>();
		teasers = new ArrayList<String>();
		loadContextMenu();
		loadStoryboard();
		loadRestriction();
    }

	private void loadStoryboard(){
		userStoryboards = new ArrayList<Storyboard>() ;
		StoryboardDao sbd=new StoryboardDao();
		for (Storyboard sb : sbd.getStoryboardByUser(this.user.getUtente())){
			this.userStoryboards.add(sb);
		}
	}
	
	private void loadContextMenu() {
		SiteSetDao ssd=new SiteSetDao();
		List<SiteSet> list=ssd.getAllSiteSetByUser(this.getUser().getUtente());
		contextmenuchild=" ";
		for (SiteSet s : list){
			contextmenuchild +=" {title: \""+s.getName()+"\", cmd: \""+s.getId()+"\"},";
		}
		if (hasSiteSet(list,"New Group")==false)
			contextmenuchild+="{title: \"New Group\", cmd: \"0\"} "; 
		else
			contextmenuchild=contextmenuchild.substring(0,contextmenuchild.length()-1);
	}

	private boolean hasSiteSet(List<SiteSet> list, String string) {
		for (SiteSet s : list){
			if (s.getName().compareTo(string)==0)
				return true;
		}
		return false;
	}

	public void addWebRes(SearchResult sr) {
		StoryboardItem a=new StoryboardItem();
		a.setId(null);
		a.setTitle(sr.getTitle());
		a.setWebcontent("<iframe id=\"scaled-frame\" src=\""+sr.getUrl()+"\" sandbox=\"allow-forms allow-scripts\"></iframe>");
		a.setContent(sr.getUrl());
		a.setType("web");
		a.setPos(Long.parseLong(""+newstoryboardlist.size()));
		newstoryboardlist.add(a);
		storyboardlist = newstoryboardlist;
	}
	
	public void addVideoRes(SearchResult sr) {
		StoryboardItem a=new StoryboardItem();
		a.setId(null);
		a.setTitle(sr.getTitle());
		SearchVideoResult svr=(SearchVideoResult)sr;
		a.setWebcontent("<img src=\""+svr.getThumbnail().getMediaUrl()+"\" width=\""+svr.getThumbnail().getWidth()+"\" height=\""+svr.getThumbnail().getHeight()+"\" />");
		a.setContent(sr.getUrl());
		a.setType("video");
		a.setPos(Long.parseLong(""+newstoryboardlist.size()));
		newstoryboardlist.add(a);
		storyboardlist = newstoryboardlist;
	}
	
	public void addImgRes(SearchResult sr) {
		StoryboardItem a=new StoryboardItem();
		a.setId(null);
		a.setTitle(sr.getTitle());
		SearchImageResult svr=(SearchImageResult)sr;
		a.setWebcontent("<img src=\""+svr.getUrl()+"\" width=\"100\" />");
		a.setContent(sr.getUrl());
		a.setType("image");
		a.setPos(Long.parseLong(""+newstoryboardlist.size()));
		newstoryboardlist.add(a);
		storyboardlist = newstoryboardlist;
	}
	
	
	public void RemoveWebRes(StoryboardItem sr) {
		storyboardlist.remove(sr);
	}
	
	
	public void reorderList() {
		newstoryboardlist = new ArrayList<StoryboardItem>();
		System.out.println(storydata);
		storydata=storydata.replace("widget[]=", "");
		storydata=storydata.replace("col1:","");
		String[] pos=storydata.split("&");
		System.out.println("");
		int i=0;
		for (String s : pos) {
			
			int id=Integer.parseInt(s);
			StoryboardItem sbi=storyboardlist.get(id);
			System.out.println("Reordering:"+s+" - "+sbi.getTitle());
			sbi.setPos(Long.parseLong(""+i));
			newstoryboardlist.add(sbi);
			i++;
		}
		
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
		// searchterms.removeAll(Collections.singleton(""));
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

	private void alignSiteDomain(){
		// siteAvailableDomainlist1=new ArrayList<String>();
		Set<String> domains=new HashSet<String>();
		for (String s: siteAvailablelist1)
			domains.add(getTldString("http://"+s));
		siteAvailableDomainlist1.clear();
		siteAvailableDomainlist1.addAll(domains);
		
		// siteSelectedDomainlist1=new ArrayList<String>();
		domains.clear();
		for (String s: siteSelectedlist1)
			domains.add(getTldString("http://"+s));
		siteSelectedDomainlist1.clear();
		siteSelectedDomainlist1.addAll(domains);
	}
	
	
		
	public String getSearchtype() {
		return searchtype;
	}

	public void setSearchtype(String searchtype) {
		this.searchtype = searchtype;
	}

		public ArrayList<String> getSearchtypelist() {
		if (searchtypelist==null){
			searchtypelist=new ArrayList<String>();
			searchtypelist.add("Web");
			searchtypelist.add("Image");
			searchtypelist.add("Video");
		}
		return searchtypelist;
	}

	public void setSearchtypelist(ArrayList<String> searchtypelist) {
		this.searchtypelist = searchtypelist;
	}	

	private String getTldString(String urlString) {
        URL url = null;
        String tldString = null;
        try {
            url = new URL(urlString);
            String[] domainNameParts = url.getHost().split("\\.");
            tldString = domainNameParts[domainNameParts.length-1];
        }
        catch (MalformedURLException e) {   
        }

        return tldString;
    }
	
	public void refineWebSearch(Type type){
		System.out.println("TYPE: REFINE Web ");
		
		//String accountKey = "BmbX+6Sy9/VEcS5oOjurccO5MQpKr2ewvLQ2vRHBKXQ";
		// TextManager tmgr=new TextManager();
		
		searchterms.removeAll(Collections.singleton(""));
		String q="";
		for (String t : this.searchterms){
			q+="\""+t+"\" ";
		}
		
		if (type == Type.REFINE_DOMAIN_PIE){
			List<String> excludedomain=new ArrayList<String>(siteAvailableDomainlist1);
			excludedomain.removeAll(siteSelectedDomainlist1);
			List<String> excludelist=new ArrayList<String>();
			for (String s:siteSelectedlist1)
				if (excludedomain.contains(getTldString("http://"+s)))
					excludelist.add(s);
			
			List<String> includelist=new ArrayList<String>();
			for (String s:siteAvailablelist1)
				if (!excludedomain.contains(getTldString("http://"+s)))
					includelist.add(s); 
			
			siteSelectedlist1.removeAll(excludelist);
			siteSelectedlist1.addAll(includelist);
		}
		
		List<String> exclude=new ArrayList<String>(siteAvailablelist1);
		exclude.removeAll(siteSelectedlist1);
		exclude.addAll(excludeWeb);
		
		for (String s : exclude)
			q+=" -site:"+s+" ";
		
		System.out.println(q);
		Track track=new Track();
		track.setDate((new GregorianCalendar()).getTime());
		track.setOperation("search");
		track.setParam1(q);
		track.setParam2(type.name());
		track.setParam3("1");
		track.setUtente(this.user.getUtente());
		TrackDao td=new TrackDao();
		td.addTrack(track);
		
		List<SearchResult> toremove=new ArrayList<SearchResult>();
		searchResult1 = new ArrayList<SearchResult>(searchResultWeb);
		for (SearchResult res:searchResult1){
			for (String exc : exclude)
				if (res.getUrl().contains(exc)) { // || excludedomain.contains(this.getTldString(res.getUrl()))){
					System.out.println("removing "+exc);
					toremove.add(res);
				}
		}
		
		for (SearchResult sr : toremove){
			searchResult1.remove(sr);
		}
		
		if (searchterms.size()==0) searchterms.add("");
		
		// PIE WEB
		StatsManager sm=new StatsManager();
		List<YData> list=sm.getMatcthTable(sm.getSites(searchResult1, null, null));
		tagclouddata="var data = [ "; 
		List<String> datastring=new ArrayList<String>();
		for (YData a : list){
			datastring.add("{ label: \""+a.getSite()+"\", data: "+a.getQty()+"} ");
		}
		tagclouddata+=Joiner.on(",").join(datastring);
		tagclouddata+=" ]; ";
		tagclouddata+=" var options = { series: { pie: {show: true, label: {show: false} }  }, grid: { hoverable: true, clickable: true }, legend: {show: false} }; ";
		tagclouddata+="$.plot($(\"#chartpie1\"), data, options ); \n";
		String hover=" $(\"#chartpie1\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black;background-color:\", obj.series.color, \"\\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive\").html(html.join('')); }); ";
		hover=" $(\"#chartpie1\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black; \\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive\").html(html.join('')); }); ";
		tagclouddata+=hover;  
		tagclouddata+=" var choiceContainer = $(\"#chartpie1\");";
		tagclouddata+=" choiceContainer.find(\"input\").click(plotAccordingToChoices);";
		tagclouddata+=" function plotAccordingToChoices() { ";
		tagclouddata+=" var key = $(this).attr(\"name\"); ";
		tagclouddata+=" $( \"input[value*='\"+key+\"']\" ).trigger('click'); ";
		tagclouddata+=" }";
		tagclouddata+="  ";
		// return "basicSearch";
		
		List<YData> wlist=sm.getMatcthWeightedTable(sm.getSites(searchResult1, null, null));
		searchWeightedDataPie1="var weighteddata = [ "; 
		datastring=new ArrayList<String>();
		for (YData a : wlist){
			datastring.add("{ label: \""+a.getSite()+"\", data: "+a.getQty()+"} ");
		}
		searchWeightedDataPie1+=Joiner.on(",").join(datastring);
		searchWeightedDataPie1+=" ]; ";
		searchWeightedDataPie1+="$.plot($(\"#chartweightedpie1\"), weighteddata, options ); \n";
		String hoverW=" $(\"#chartweightedpie1\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black; \\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive1W\").html(html.join('')); }); ";
		searchWeightedDataPie1+=hoverW;  
		searchWeightedDataPie1+=" var choiceContainerW = $(\"#chartweightedpie1\");";
		searchWeightedDataPie1+=" choiceContainerW.find(\"input\").click(plotAccordingToChoicesW);";
		searchWeightedDataPie1+=" function plotAccordingToChoicesW() { ";
		searchWeightedDataPie1+=" var key = $(this).attr(\"name\"); ";
		searchWeightedDataPie1+=" $( \"input[value*='\"+key+\"']\" ).trigger('click'); ";
		searchWeightedDataPie1+=" }";
		searchWeightedDataPie1+="  ";
		
		List<YData> Llist=sm.getMatcthTable(sm.getLangSites(searchResult1, null, null));
		searchLangDataPie1="var langdata = [ "; 
		datastring=new ArrayList<String>();
		for (YData a : Llist){
			datastring.add("{ label: \""+a.getSite()+"\", data: "+a.getQty()+"} ");		
		}
		searchLangDataPie1+=Joiner.on(",").join(datastring);
		searchLangDataPie1+=" ]; ";
		searchLangDataPie1+="$.plot($(\"#chartlangpie1\"), langdata, options ); \n";
		String hoverL=" $(\"#chartlangpie1\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black; \\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive1L\").html(html.join('')); }); ";
		searchLangDataPie1+=hoverL;  
		searchLangDataPie1+=" var choiceContainerL = $(\"#chartlangpie1\");";
		searchLangDataPie1+=" choiceContainerL.find(\"input\").click(plotAccordingToChoicesL);";
		searchLangDataPie1+=" function plotAccordingToChoicesL() { ";
		searchLangDataPie1+=" var key = $(this).attr(\"name\"); ";
		searchLangDataPie1+=" $( \"input[value*='\"+key+\"']\" ).trigger('click'); ";
		searchLangDataPie1+=" }";
		searchLangDataPie1+="  ";
		
		alignSiteDomain();
	}

Map<Integer, Integer> indexSearchResult;
Map<String, ArrayList<Integer>> indexDandelion;
	
	private Map<String,Integer> createTagCloud (String q, String lang, String radiowebnews){
		String accountKey = "BmbX+6Sy9/VEcS5oOjurccO5MQpKr2ewvLQ2vRHBKXQ";
		
		if (radiowebnews.compareTo("web")==0){
		
			AzureSearchWebQuery aq = new AzureSearchWebQuery();
			aq.setAppid(accountKey);
			if (lang.compareTo("en")==0)
				q+=" language:en ";
			else
				q+=" loc:"+lang+ " language:"+lang+" ";
			aq.setQuery(q);
			System.out.println(q);
			List<AzureSearchWebResult> arsall=new ArrayList<AzureSearchWebResult>();
			int conta=-1;
	        for (int i=1;i<=2 && conta!=0;i++){
	            aq.setPerPage(50);
	            aq.setPage(i);
				aq.doQuery();
				AzureSearchResultSet<AzureSearchWebResult> ars = aq.getQueryResult();
				if (ars.getAsrs().size()<50) conta=0;
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
			}
	}else{
		AzureSearchNewsQuery aq = new AzureSearchNewsQuery();
		aq.setAppid(accountKey);
		// aq.setLocationOverride(lang+"."+lang.toUpperCase());
		aq.setLocationOverride("IT");
		aq.setSortBy("Date");
		aq.setQuery(q);
		List<AzureSearchNewsResult> arsall=new ArrayList<AzureSearchNewsResult>();
		int conta=-1;
        for (int i=1;i<=2 && conta!=0;i++){
            aq.setPerPage(50);
            aq.setPage(i);
			aq.doQuery();
			AzureSearchResultSet<AzureSearchNewsResult> ars = aq.getQueryResult();
			if (ars!=null && ars.getAsrs().size()<50) conta=0;
			for (AzureSearchNewsResult anr : ars){
				arsall.add(anr);
			}
		}
		
		searchResultWeb = new ArrayList<SearchResult>();

		List<String> ripetiz=new ArrayList<String>();
		for (AzureSearchNewsResult anr : arsall){			
			SearchWebResult r=new SearchWebResult();
			r.setTitle(anr.getTitle());
			r.setDescription(anr.getDescription());
			r.setUrl(anr.getUrl());
			if (!ripetiz.contains(r.getUrl())){
				ripetiz.add(r.getUrl());
				searchResultWeb.add(r);
			}
		}	
	}
		indexSearchResult=new HashMap<Integer, Integer> ();
		String textToAnalyse="";
		for (int index=0;index<10 && index<searchResultWeb.size();index++){
			textToAnalyse+=" "+searchResultWeb.get(index).getTitle()+" "+((SearchWebResult)searchResultWeb.get(index)).getDescription();
			indexSearchResult.put(index, textToAnalyse.length());
		}	
		
		// System.out.println(textToAnalyse);
		
		ArrayList<DandelionDataObject> objList = null;
		indexDandelion=new HashMap<String, ArrayList<Integer>>();
		
		try {
			objList=EntityExtractionService.postText(textToAnalyse);
		} catch (HttpException e) {
			e.printStackTrace();
		};
		DBpediaConnect dbpc=new DBpediaConnect();
		Map<String, Integer> conceptFreq=new HashMap<String,Integer>();
		
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
				indexDandelion.get(key).add(f);
			} else {
				conceptFreq.put(key,1);
				indexDandelion.put(key, new ArrayList<Integer>());
				indexDandelion.get(key).add(f);
			}
		}		
		return conceptFreq;
	}
	
	public String filterSearch(){
		searchResultEn=new ArrayList<SearchResult>();
		
		for (String key : indexDandelion.keySet()){
			if(this.clickedTag.compareTo(key.replace("http://en.wikipedia.org/wiki/", ""))==0){
				for (Integer ind : indexDandelion.get(key)){
					SearchWebResult swr=new SearchWebResult();
					swr.setDescription(((SearchWebResult)searchResultWeb.get(ind)).getDescription());
					swr.setTitle(searchResultWeb.get(ind).getTitle());
					swr.setUrl(searchResultWeb.get(ind).getUrl());
					searchResultEn.add(swr);
				}
			}
		}
		return "";
	}

	public String searcAll(int nuovo) throws IOException{
		
		
		
		// System.out.println("TYPE: ALL nuovo: "+nuovo);
		
		
		// accountKey = "TTtbr2RdFtN8bvzLZG9SIEgkDa7ecUDNrAkdwEy86UI"; // davide.taibi@libero.it
		
		// searchterms.removeAll(Collections.singleton(""));
		for (int l=0;l<searchterms.size();l++)
			if (searchterms.get(l).trim().compareTo("")==0)
				searchterms.set(l, searchterms.get(0));
				
		String q="";
		q+="\""+searchterms.get(2)+"\"";
		
		/* List<String> exclude=new ArrayList<String>(siteAvailablelist1);
		exclude.removeAll(siteSelectedlist1);
		exclude.addAll(excludeWeb);
		exclude.addAll(excludeImg);
		exclude.addAll(excludeVid);
		for (String s : exclude)
			q+=" -site:"+s;
		*/
		
		
		Map<String, Integer> conceptFreq=new HashMap<String, Integer>();
		conceptFreq=createTagCloud(q, "de", this.radiowebnews);
		tagclouddata=" var wordsde = [ ";
		for (String key : conceptFreq.keySet()){
			tagclouddata+=" { text: \""+key.replace("http://dbpedia.org/resource/", "")+"\", size: "+conceptFreq.get(key)*15+"}, ";
		}
		// tagclouddata+=" ]; $('#demo').jQCloud(words, {heigth: 500, width: 1032, autoResize: true}); ";
		tagclouddata+="]; d3.layout.cloud().size([500, 500]).words(wordsde).rotate(0).font(\"Impact\").fontSize(function(d) { return d.size; }).on(\"end\", drawde).start();";
		
		// tagclouddata+=" function showdetail(lang,text){ alert('You clicked the word '+lang+' in '+text); }";
		q="";
		q+="\""+searchterms.get(1)+"\"";
		// q+=" loc:it language:it ";

		/*
		 * 
		 *     var frequency_list = [{"text":"study","size":40},{"text":"motion","size":15},{"text":"forces","size":10},{"text":"electricity","size":15},{"text":"movement","size":10},{"text":"relation","size":5},{"text":"things","size":10},{"text":"force","size":5},{"text":"ad","size":5},{"text":"energy","size":85},{"text":"living","size":5},{"text":"nonliving","size":5},{"text":"laws","size":15},{"text":"speed","size":45},{"text":"velocity","size":30},{"text":"define","size":5},{"text":"constraints","size":5},{"text":"universe","size":10},{"text":"physics","size":120},{"text":"describing","size":5},{"text":"matter","size":90},{"text":"physics-the","size":5},{"text":"world","size":10},{"text":"works","size":10},{"text":"science","size":70},{"text":"interactions","size":30},{"text":"studies","size":5},{"text":"properties","size":45},{"text":"nature","size":40},{"text":"branch","size":30},{"text":"concerned","size":25},{"text":"source","size":40},{"text":"google","size":10},{"text":"defintions","size":5},{"text":"two","size":15},{"text":"grouped","size":15},{"text":"traditional","size":15},{"text":"fields","size":15},{"text":"acoustics","size":15},{"text":"optics","size":15},{"text":"mechanics","size":20},{"text":"thermodynamics","size":15},{"text":"electromagnetism","size":15},{"text":"modern","size":15},{"text":"extensions","size":15},{"text":"thefreedictionary","size":15},{"text":"interaction","size":15},{"text":"org","size":25},{"text":"answers","size":5},{"text":"natural","size":15},{"text":"objects","size":5},{"text":"treats","size":10},{"text":"acting","size":5},{"text":"department","size":5},{"text":"gravitation","size":5},{"text":"heat","size":10},{"text":"light","size":10},{"text":"magnetism","size":10},{"text":"modify","size":5},{"text":"general","size":10},{"text":"bodies","size":5},{"text":"philosophy","size":5},{"text":"brainyquote","size":5},{"text":"words","size":5},{"text":"ph","size":5},{"text":"html","size":5},{"text":"lrl","size":5},{"text":"zgzmeylfwuy","size":5},{"text":"subject","size":5},{"text":"distinguished","size":5},{"text":"chemistry","size":5},{"text":"biology","size":5},{"text":"includes","size":5},{"text":"radiation","size":5},{"text":"sound","size":5},{"text":"structure","size":5},{"text":"atoms","size":5},{"text":"including","size":10},{"text":"atomic","size":10},{"text":"nuclear","size":10},{"text":"cryogenics","size":10},{"text":"solid-state","size":10},{"text":"particle","size":10},{"text":"plasma","size":10},{"text":"deals","size":5},{"text":"merriam-webster","size":5},{"text":"dictionary","size":10},{"text":"analysis","size":5},{"text":"conducted","size":5},{"text":"order","size":5},{"text":"understand","size":5},{"text":"behaves","size":5},{"text":"en","size":5},{"text":"wikipedia","size":5},{"text":"wiki","size":5},{"text":"physics-","size":5},{"text":"physical","size":5},{"text":"behaviour","size":5},{"text":"collinsdictionary","size":5},{"text":"english","size":5},{"text":"time","size":35},{"text":"distance","size":35},{"text":"wheels","size":5},{"text":"revelations","size":5},{"text":"minute","size":5},{"text":"acceleration","size":20},{"text":"torque","size":5},{"text":"wheel","size":5},{"text":"rotations","size":5},{"text":"resistance","size":5},{"text":"momentum","size":5},{"text":"measure","size":10},{"text":"direction","size":10},{"text":"car","size":5},{"text":"add","size":5},{"text":"traveled","size":5},{"text":"weight","size":5},{"text":"electrical","size":5},{"text":"power","size":5}];
   
		 * 
		 */
		
		conceptFreq=createTagCloud(q, "it", this.radiowebnews);			
		tagclouddatait=" var wordsit = [ ";
		for (String key : conceptFreq.keySet()){
			tagclouddatait+=" { text: \""+key.replace("http://dbpedia.org/resource/", "")+"\", size: "+conceptFreq.get(key)*15+"}, ";
		}
		// tagclouddatait+=" ]; $('#demoit').jQCloud(wordsit, {heigth: 500, width: 1032, autoResize: true});";
		tagclouddatait+="]; d3.layout.cloud().size([500, 500]).words(wordsit).rotate(0).font(\"Impact\").fontSize(function(d) { return d.size; }).on(\"end\", drawit).start();";
		
		q="";
		q+="\""+searchterms.get(3)+"\"";
		// q+=" loc:fr language:fr ";
		// System.out.println(q);
		conceptFreq=createTagCloud(q, "fr", this.radiowebnews);	
		tagclouddataes=" var wordses = [ ";
		for (String key : conceptFreq.keySet()){
			tagclouddataes+=" { text: \""+key.replace("http://dbpedia.org/resource/", "")+"\", size: "+conceptFreq.get(key)*15+"}, ";
		}
		// tagclouddataes+=" ]; $('#demoes').jQCloud(wordses, {heigth: 500, width: 1032, autoResize: true});";
		tagclouddataes+="]; d3.layout.cloud().size([500, 500]).words(wordses).rotate(0).font(\"Impact\").fontSize(function(d) { return d.size; }).on(\"end\", drawes).start();";
		q="";
		q+="\""+searchterms.get(0)+"\"";
		// q+=" language:en ";

		conceptFreq=createTagCloud(q, "en", this.radiowebnews);			
		tagclouddataen=" var wordsen = [ ";
		for (String key : conceptFreq.keySet()){
			tagclouddataen+=" { text: \""+key.replace("http://en.wikipedia.org/wiki/", "")+"\", size: "+conceptFreq.get(key)*15+"}, ";
		}
		// tagclouddataen+=" ]; $('#demoen').jQCloud(wordsen);";
		tagclouddataen+="]; d3.layout.cloud().size([500, 500]).words(wordsen).rotate(0).font(\"Impact\").fontSize(function(d) { return d.size; }).on(\"end\", drawen).start();";
	
		/* tagclouddataen+=" var content = '  [ ";
		for (String key : indexDandelion.keySet()){
			tagclouddataen+=" { \"word\": \""+key.replace("http://en.wikipedia.org/wiki/", "")+"\", \"values\":[";
			for (Integer ind : indexDandelion.get(key)){
				tagclouddataen+=" { \"title\": \""+searchResultWeb.get(ind).getTitle().replace("\"", "\\\\\\\"").replace("'", "\\'")+"\" , ";
				tagclouddataen+=" \"snippet\": \""+((SearchWebResult)searchResultWeb.get(ind)).getDescription().replace("\"", "\\\\\\\"").replace("'", "\\'")+"\" ,";
				tagclouddataen+=" \"url\": \""+searchResultWeb.get(ind).getUrl()+"\"},";
			}
			tagclouddataen=tagclouddataen.substring(0,tagclouddataen.length()-1);
			tagclouddataen+=" ] },";
		}
		tagclouddataen=tagclouddataen.substring(0,tagclouddataen.length()-1);
		tagclouddataen+="]'; var obbbj = JSON.parse(content);"; */
		
		Track track=new Track();
	        track.setDate((new GregorianCalendar()).getTime());
	        track.setOperation("languagesearch");
	        track.setParam1(q);
	        track.setParam2(this.searchtype);
	        track.setParam3("");
	        track.setUtente(this.user.getUtente());
	        TrackDao td=new TrackDao();
	        td.addTrack(track);
			
		if (searchterms.size()==0) searchterms.add("");
		
		return "languageSearch";
		
		
		
	}
	
	
}