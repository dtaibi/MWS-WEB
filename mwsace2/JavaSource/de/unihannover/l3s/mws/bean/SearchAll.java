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

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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

import net.billylieurance.azuresearch.AzureSearchImageQuery;
import net.billylieurance.azuresearch.AzureSearchImageResult;
import net.billylieurance.azuresearch.AzureSearchResultSet;
import net.billylieurance.azuresearch.AzureSearchVideoQuery;
import net.billylieurance.azuresearch.AzureSearchVideoResult;
import net.billylieurance.azuresearch.AzureSearchWebQuery;
import net.billylieurance.azuresearch.AzureSearchWebResult;
import net.billylieurance.azuresearch.BingThumbnail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Joiner;

import de.unihannover.l3s.mws.dao.GeneralsettingsDao;
import de.unihannover.l3s.mws.dao.RicercaDao;
import de.unihannover.l3s.mws.dao.SiteSetDao;
import de.unihannover.l3s.mws.dao.StoryboardDao;
import de.unihannover.l3s.mws.dao.TrackDao;
import de.unihannover.l3s.mws.dao.UtenteDao;
import de.unihannover.l3s.mws.model.Generalsettings;
import de.unihannover.l3s.mws.model.Ricerca;
import de.unihannover.l3s.mws.model.Risultati;
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
import de.unihannover.l3s.mws.model.timeline.Asset;
import de.unihannover.l3s.mws.model.timeline.Timeline;
import de.unihannover.l3s.mws.model.timeline.WholeTimeline;
import de.unihannover.l3s.mws.util.DateManager;
import de.unihannover.l3s.mws.util.StatsManager;
import de.unihannover.l3s.mws.util.TextManager;


/**
 * Created by JBoss Tools
 */
@ManagedBean(name="searchall")
@ViewScoped
public class SearchAll {
	// private String searchtext;
	private String searchtype="Web";
	// private String searchDataPie="";
	private ArrayList<String> searchtypelist=null;
	// private ArrayList<String> siteAvailablelist=null;
	// private List<String> siteSelectedlist;
	// private ArrayList<SearchResult> searchResult=null;
	private String timeline;
	private List<String> searchterms;
	@ManagedProperty(value="#{user}")
	private User user;

	private String name;
	private Ricerca r;
	private Storyboard storyboard;
	
	private String searchDataPie1="";
	private String searchDataPie2="";
	private String searchDataPie3="";
	
	private String searchDataBar1="";
	
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
	private ArrayList<SearchResult> searchResult3=null;
	
	private ArrayList<SearchResult> searchResultVideo=null;
	private ArrayList<SearchResult> searchResultWeb=null;
	private ArrayList<SearchResult> searchResultImg=null;
	
	private String storybname;
	
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
	
	public String getStorybname() {
		return storybname;
	}

	public void setStorybname(String storybname) {
		this.storybname = storybname;
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
	
	public String getSearchDataPie1() {
		return searchDataPie1;
	}

	public void setSearchDataPie1(String searchDataPie1) {
		this.searchDataPie1 = searchDataPie1;
	}

	public String getSearchDataPie2() {
		return searchDataPie2;
	}

	public void setSearchDataPie2(String searchDataPie2) {
		this.searchDataPie2 = searchDataPie2;
	}

	public String getSearchDataPie3() {
		return searchDataPie3;
	}

	public void setSearchDataPie3(String searchDataPie3) {
		this.searchDataPie3 = searchDataPie3;
	}

	public String getSearchDataBar1() {
		return searchDataBar1;
	}

	public void setSearchDataBar1(String searchDataBar1) {
		this.searchDataBar1 = searchDataBar1;
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

	public ArrayList<SearchResult> getSearchResult3() {
		return searchResult3;
	}

	public void setSearchResult3(ArrayList<SearchResult> searchResult3) {
		this.searchResult3 = searchResult3;
	}

	public String getSearchsettings() {
		return searchsettings;
	}

	public void setSearchsettings(String searchsettings) {
		this.searchsettings = searchsettings;
	}
	
	
	@PostConstruct
    public void init() {
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
	
	public void loadSearch(Long id){
		id = Long.parseLong(this.resultId);
		if (id!=0){
		
		RicercaDao rd=new RicercaDao();
		Ricerca ric=rd.getRicercaById(id);
		this.r = ric;
		this.name = ric.getNome();
		try {
			
			
			TrackDao td=new TrackDao();
			Track track=new Track();
			Calendar c = new GregorianCalendar();
			track.setDate(c.getTime());
			track.setOperation("load_search");
			track.setParam1(this.name);
			track.setUtente(this.user.getUtente());
			td.addTrack(track);
			
			JSONObject avList=new JSONObject(ric.getSiteAvailablelists());
			JSONArray arr1=(JSONArray)avList.get("siteAvailablelist1");
			this.siteAvailablelist1 = new ArrayList<String>();
			for (int i=0;i<arr1.length();i++)
				this.siteAvailablelist1.add(arr1.get(i).toString());
			
			arr1=(JSONArray)avList.get("siteAvailablelist2");
			this.siteAvailablelist2 = new ArrayList<String>();
			for (int i=0;i<arr1.length();i++)
				this.siteAvailablelist2.add(arr1.get(i).toString());
			
			arr1=(JSONArray)avList.get("siteAvailablelist3");
			this.siteAvailablelist3 = new ArrayList<String>();
			for (int i=0;i<arr1.length();i++)
				this.siteAvailablelist3.add(arr1.get(i).toString());
			
			JSONObject slList=new JSONObject(ric.getSiteSelectedlists());
			arr1=(JSONArray)slList.get("siteSelectedlist1");
			this.siteSelectedlist1 = new ArrayList<String>();
			for (int i=0;i<arr1.length();i++)
				this.siteSelectedlist1.add(arr1.get(i).toString());
			
			arr1=(JSONArray)slList.get("siteSelectedlist2");
			this.siteSelectedlist2 = new ArrayList<String>();
			for (int i=0;i<arr1.length();i++)
				this.siteSelectedlist2.add(arr1.get(i).toString());
			
			arr1=(JSONArray)slList.get("siteSelectedlist3");
			this.siteSelectedlist3 = new ArrayList<String>();
			for (int i=0;i<arr1.length();i++)
				this.siteSelectedlist3.add(arr1.get(i).toString());
			
			JSONObject stList=new JSONObject(ric.getSearchterms());
			arr1=(JSONArray)stList.get("searchTerms");
			this.searchterms= new ArrayList<String>();
			for (int i=0;i<arr1.length();i++)
				this.searchterms.add(arr1.get(i).toString());
			
			JSONObject dpList=new JSONObject(ric.getSearchDataPies());
			this.searchDataPie1 =dpList.get("searchDataPie1").toString(); 
			this.searchDataPie2 =dpList.get("searchDataPie2").toString(); 
			this.searchDataPie3 =dpList.get("searchDataPie3").toString();
			
			
			JSONObject WdpList=new JSONObject(ric.getSearchWeightedDataPies());
			this.searchWeightedDataPie1 =WdpList.get("searchWeightedPie1").toString();
			
			JSONObject DdpList=new JSONObject(ric.getSearchDomainDataPies());
			this.searchLangDataPie1 =DdpList.get("searchDomainPie1").toString();
			
			searchResultWeb = new ArrayList<SearchResult>();
			searchResultVideo = new ArrayList<SearchResult>();
			searchResultImg = new ArrayList<SearchResult>();
			
			List<String> exclude1=new ArrayList<String>(siteAvailablelist1);
			exclude1.removeAll(siteSelectedlist1);
			
			List<String> exclude2=new ArrayList<String>(siteAvailablelist2);
			exclude2.removeAll(siteSelectedlist2);
			
			List<String> exclude3=new ArrayList<String>(siteAvailablelist3);
			exclude3.removeAll(siteSelectedlist3);
			
			teasers=new ArrayList<String>();
			for (Risultati r : ric.getRisultati()) {
				JSONObject ris=new JSONObject(r.getRisultato());
				
				if (r.getType().equals("WEB")){
					SearchWebResult rweb=new SearchWebResult();
					rweb.setTitle(ris.getString("title"));
					rweb.setDescription(ris.getString("description"));
					teasers.add(ris.getString("description"));
					rweb.setUrl(ris.getString("url"));
					searchResultWeb.add(rweb);
				}
				if (r.getType().equals("VIDEO")){
					SearchVideoResult rvideo=new SearchVideoResult();
					rvideo.setTitle(ris.getString("title"));
					rvideo.setRuntime(ris.getString("runtime"));
					JSONObject thumbnail=(JSONObject) ris.get("thumbnail");
					BingThumbnail bt=new BingThumbnail();
					if (thumbnail.has("fileSize")) bt.setFileSize(thumbnail.getLong("fileSize"));
					if (thumbnail.has("height")) bt.setHeight(thumbnail.getInt("height"));
					if (thumbnail.has("mediaUrl")) bt.setMediaUrl(thumbnail.getString("mediaUrl"));
					if (thumbnail.has("width")) bt.setHeight(thumbnail.getInt("width"));
					if (thumbnail.has("contentType")) bt.setContentType(thumbnail.getString("contentType"));
					rvideo.setThumbnail(bt);
					if (ris.has("url")) rvideo.setUrl(ris.getString("url")); else System.out.println("SENZA URL : "+r.getId());
					searchResultVideo.add(rvideo);
				}
				if (r.getType().equals("IMAGE")){
					SearchImageResult rimg=new SearchImageResult();
					rimg.setTitle(ris.getString("title"));
					if (ris.has("height")) rimg.setHeight(ris.getInt("height"));
					if (ris.has("width")) rimg.setWidth(ris.getInt("width"));
					if (ris.has("url")) rimg.setUrl(ris.getString("url"));	
					searchResultImg.add(rimg);
				}
			}
			searchResult1=new ArrayList<SearchResult>(searchResultWeb);
			ArrayList<SearchResult> toremove=new ArrayList<SearchResult>();
			for (SearchResult res:searchResult1){
				for (String exc : exclude1)
					if (res.getUrl().contains(exc))
						toremove.add(res);
			}
			for (SearchResult sr : toremove) searchResult1.remove(sr);
			
			searchResult2=new ArrayList<SearchResult>(searchResultVideo);
			toremove=new ArrayList<SearchResult>();
			for (SearchResult res:searchResult2){
				for (String exc : exclude2)
					if (res.getUrl().contains(exc))
						toremove.add(res);
			}
			for (SearchResult sr : toremove) searchResult2.remove(sr);
			
			searchResult3=new ArrayList<SearchResult>(searchResultImg);
			toremove=new ArrayList<SearchResult>();
			for (SearchResult res:searchResult3){
				for (String exc : exclude3)
					if (res.getUrl().contains(exc))
						toremove.add(res);
			}
			for (SearchResult sr : toremove) searchResult3.remove(sr);
			
			StatsManager sm=new StatsManager();
			List<YData> Llist=sm.getMatcthTable(sm.getLangSites(searchResult1, null, null));
			
			
			searchLangDataPie1="var langdata = [ "; 
			List<String >datastring=new ArrayList<String>();
			for (YData a : Llist){
				datastring.add("{ label: \""+a.getSite()+"\", data: "+a.getQty()+"} ");
			}
			searchLangDataPie1+=Joiner.on(",").join(datastring);
			searchLangDataPie1+=" ]; ";
			searchLangDataPie1+="$.plot($(\"#chartlangpie1\"), langdata, options ); \n";
			String hoverL=" $(\"#chartlangpie1\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black; \\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive1L\").html(html.join('')); }); ";
			searchLangDataPie1+=hoverL; 
			String plotclickL=" $(\"#chartlangpie1\").bind(\"plotclick\", function(event, pos, obj){ if (!obj){return;} }); ";
			searchLangDataPie1+=plotclickL; 
			searchLangDataPie1+=" var choiceContainerL = $(\"#chartlangpie1\");";
			searchLangDataPie1+=" choiceContainerL.find(\"input\").click(plotAccordingToChoicesL);";
			searchLangDataPie1+=" function plotAccordingToChoicesL() { ";
			searchLangDataPie1+=" var key = $(this).attr(\"name\"); ";
			searchLangDataPie1+=" $( \"input[value*='\"+key+\"']\" ).trigger('click'); ";
			searchLangDataPie1+=" }";
			searchLangDataPie1+="  ";
			alignSiteDomain();
			
			this.resultId="0";
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		}
		// return "basicSearch";
	}
	
	public void openStoryboard(){
		StoryboardDao sbd=new StoryboardDao();
		storyboard=sbd.getStoryboardById(Long.parseLong(storyboardsel));
		storybname = storyboard.getNome();
		// storyboardlist = new ArrayList<StoryboardItem>();
		for (StoryboardItem sbi : storyboard.getStoryboardItem()) {
			/*StoryboardItem sblocal=new StoryboardItem();
			sblocal.setTitle(sbi.getTitle());
			sblocal.setContent(sbi.getContent());
			sblocal.setType(sbi.getType());
			sblocal.setWebcontent(sbi.getWebcontent());
			sblocal.setComment(sbi.getComment());
			sblocal.setId(Long.parseLong(""+newstoryboardlist.size())); */
			sbi.setPos(Long.parseLong(""+newstoryboardlist.size()));
			newstoryboardlist.add(sbi);
		}
		storyboardlist = newstoryboardlist;
		
		TrackDao td=new TrackDao();
		Track track=new Track();
		Calendar c = new GregorianCalendar();
		track.setDate(c.getTime());
		track.setOperation("open_storyboard");
		track.setParam1(this.storybname);
		track.setUtente(this.user.getUtente());
		td.addTrack(track);
	}
	
	public void saveNewSearch(){
		r = null;
		RicercaDao rd=new RicercaDao();
		while (rd.getRicercaByName(this.name)!=null){
			this.name = this.name+"_copy";
		}
		saveSearch();
	}
	
	public void saveNewStoryboard(){
		this.storyboard = null;
		StoryboardDao sbd=new StoryboardDao();
		while (sbd.getStoryboardByName(this.storybname)!=null){
			this.storybname = this.storybname+"_copy";
		}
		
		List<StoryboardItem> clone = new ArrayList<StoryboardItem>(storyboardlist.size());
	    for(StoryboardItem item: storyboardlist) clone.add(item.clone());
		
		storyboardlist.clear();
		storyboardlist = clone;
		
		saveStoryboard();
	}
	
	public void saveStoryboard(){
		if (storyboard==null)
			storyboard=new Storyboard();
		storyboard.setNome(this.storybname);
		for (StoryboardItem sbi : this.storyboardlist) {
			// sbi.setId(null);
			sbi.setStoryboard(storyboard);
		}
		storyboard.setStoryboardItem(this.storyboardlist);
		storyboard.setUtente(this.user.getUtente());
		storyboard.setData((new GregorianCalendar()).getTime());
		StoryboardDao rd=new StoryboardDao();
		rd.addStoryboard(storyboard);
		TrackDao td=new TrackDao();
		Track track=new Track();
		Calendar c = new GregorianCalendar();
		track.setDate(c.getTime());
		track.setOperation("save_storyboard");
		track.setParam1(this.storybname);
		track.setUtente(this.user.getUtente());
		td.addTrack(track);
		
	}
	
	public void saveSearch(){
		if (r==null)
			r=new Ricerca();
		
		r.setData((new GregorianCalendar()).getTime());
		r.setNome(this.name);
		r.setUtente(this.user.getUtente());
		  
		try {
			TrackDao td=new TrackDao();
			Track track=new Track();
			Calendar c = new GregorianCalendar();
			track.setDate(c.getTime());
			track.setOperation("save_search");
			track.setParam1(this.name);
			track.setUtente(this.user.getUtente());
			td.addTrack(track);
			
			JSONObject avList=new JSONObject();
			avList.put("siteAvailablelist1", this.siteAvailablelist1);
			avList.put("siteAvailablelist2", this.siteAvailablelist2);
			avList.put("siteAvailablelist3", this.siteAvailablelist3);
			r.setSiteAvailablelists(avList.toString());
			
			JSONObject pieList=new JSONObject();
			pieList.put("searchDataPie1", this.searchDataPie1);
			pieList.put("searchDataPie2", this.searchDataPie2);
			pieList.put("searchDataPie3", this.searchDataPie3);
			r.setSearchDataPies(pieList.toString());
			
			JSONObject pieWList=new JSONObject();
			pieWList.put("searchWeightedPie1", this.searchWeightedDataPie1);
			r.setSearchWeightedDataPies(pieWList.toString());
			
			JSONObject pieDList=new JSONObject();
			pieDList.put("searchDomainPie1", this.searchLangDataPie1);
			r.setSearchDomainDataPies(pieDList.toString());
			
			JSONObject selList=new JSONObject();
			selList.put("siteSelectedlist1", this.siteSelectedlist1);
			selList.put("siteSelectedlist2", this.siteSelectedlist2);
			selList.put("siteSelectedlist3", this.siteSelectedlist3);
			r.setSiteSelectedlists(selList.toString());
			
			JSONObject termList=new JSONObject();
			termList.put("searchTerms", this.searchterms);
			r.setSearchterms(termList.toString());
			List<Risultati> rislist=new ArrayList<Risultati>(); 
			for (SearchResult sr : searchResultWeb){
				Risultati ris=new Risultati(); 
				JSONObject risultato=new JSONObject(sr);
				ris.setRicerca(r);
				ris.setRisultato(risultato.toString());
				ris.setType("WEB");
				rislist.add(ris);
			}
			for (SearchResult sr : searchResultVideo){
				Risultati ris=new Risultati(); 
				JSONObject risultato=new JSONObject(sr);
				ris.setRicerca(r);
				ris.setRisultato(risultato.toString());
				ris.setType("VIDEO");
				rislist.add(ris);
			}
			for (SearchResult sr : searchResultImg){
				Risultati ris=new Risultati(); 
				JSONObject risultato=new JSONObject(sr);
				ris.setRicerca(r);
				ris.setRisultato(risultato.toString());
				ris.setType("IMAGE");
				rislist.add(ris);
			}
			r.setRisultati(rislist);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		RicercaDao rd=new RicercaDao();
		rd.addRicerca(r);
	}
	
	private boolean chechAny(List<String> lista, String word){
		boolean check= false;
		for (String s : lista){
			if (s.compareTo(word)==0){
				return true;
			}
		}
		return check;
	}
	
	public String getTimeline() {
		return this.timeline;
	}
	
	public String calculateTimeline(ArrayList<SearchResult> searchResult) {
		List<String> urlAnalyzed=new ArrayList<String>();
		Date dinizio=new Date();
		Timeline timeline1=new Timeline();
		timeline1.setHeadline("Starting");
		timeline1.setType("default");
		timeline1.setStartDate("2009,1");
		timeline1.setText("Starting Point");
		Asset a=new Asset();
		a.setMedia("aaa");
		a.setCredit("");
		a.setCaption("");
		timeline1.setAsset(a);
		
		timeline1.setDate(new ArrayList<de.unihannover.l3s.mws.model.timeline.Date>());
		// if (this.searchResult!=null)
			for (SearchResult sr : searchResult){
				System.out.println(sr.getUrl());
				if (sr.getUrl().contains("wikipedia")||sr.getUrl().contains("youtube")||sr.getUrl().contains("slideshare")||sr.getUrl().contains("flickr")){
					de.unihannover.l3s.mws.model.timeline.Date date2=new de.unihannover.l3s.mws.model.timeline.Date();
					int year=2009 + (int)(Math.random() * ((2013 - 2009) + 1));
					int month=1 + (int)(Math.random() * ((12 - 1) + 1));
					date2.setStartDate(year+","+month);
					date2.setHeadline(sr.getUrl().replace(".", " ").replace("-", " ").replace(",", "").replace("_", " ").replace("?", "").replace("=", " "));
					date2.setText("");
					Asset a2=new Asset();
					a2.setCaption("");
					a2.setCredit("");
					
					if (sr.getUrl().contains("wikipedia")){
						if (!chechAny(urlAnalyzed, sr.getUrl())){
							urlAnalyzed.add(sr.getUrl());
							SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy,MM");
							Date d=DateManager.getWikipediaDate(sr.getUrl());
							if (d!=null) {
								String data1=FORMATTER.format(d);
								date2.setStartDate(data1);
								date2.setAsset(a2);
							
							
								Calendar cal = Calendar.getInstance();
								cal.setTime(d);
								cal.add(Calendar.MONTH, -2);
								
								if (dinizio.compareTo(cal.getTime()) > 0){
									dinizio=cal.getTime();
									timeline1.setStartDate(FORMATTER.format(dinizio));
								}
								
								a2.setMedia("http://localhost:8080/mwsace2/javax.faces.resource/img/icon/icon-wikipedia.png.jsf?ln=timeline");
								timeline1.getDate().add(date2);
							}
						}
					} else if (sr.getUrl().contains("youtube")){
						if (!chechAny(urlAnalyzed, sr.getUrl())){
							urlAnalyzed.add(sr.getUrl());
							SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy,MM");
							Date d=DateManager.getYoutubeDate(sr.getUrl());
							if (d!=null){
								String data1=FORMATTER.format(d);
								
								date2.setStartDate(data1);
								date2.setAsset(a2);
								
								Calendar cal = Calendar.getInstance();
								cal.setTime(d);
								cal.add(Calendar.MONTH, -2);
								
								if (dinizio.compareTo(cal.getTime()) > 0){
									dinizio=cal.getTime();
									timeline1.setStartDate(FORMATTER.format(dinizio));
								}
								
								a2.setMedia("http://localhost:8080/mwsace2/javax.faces.resource/img/icon/y_icon.png.jsf?ln=timeline");
								timeline1.getDate().add(date2);
							}
						}
					}else if (sr.getUrl().contains("slideshare")){
						if (!chechAny(urlAnalyzed, sr.getUrl())  && sr.getUrl().startsWith("http://www.slideshare.net/") && sr.getUrl().compareTo("http://www.slideshare.net/")!=0 && !sr.getUrl().contains("www.slideshare.net/login")){
							urlAnalyzed.add(sr.getUrl());
							SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy,MM");
							Date d=DateManager.getSlideshareDate(sr.getUrl());
							if (d!=null){
								String data1=FORMATTER.format(d);
								
								date2.setStartDate(data1);
								date2.setAsset(a2);
								
								Calendar cal = Calendar.getInstance();
								cal.setTime(d);
								cal.add(Calendar.MONTH, -2);
								
								if (dinizio.compareTo(cal.getTime()) > 0){
									dinizio=cal.getTime();
									timeline1.setStartDate(FORMATTER.format(dinizio));
								}
								
								a2.setMedia("http://localhost:8080/mwsace2/javax.faces.resource/img/icon/slideshare-icon.png.jsf?ln=timeline");
								timeline1.getDate().add(date2);
							}
						}
					}else if (sr.getUrl().contains("flickr")){
						a2.setMedia("http://localhost:8080/mwsace2/javax.faces.resource/img/icon/flickr-icon.png.jsf?ln=timeline");
						date2.setAsset(a2);
						timeline1.getDate().add(date2);
					}
					
					
					
				}
			}
		//else
		//	System.out.println("SR NULL");
		/* de.unihannover.l3s.mws.model.timeline.Date date1=new de.unihannover.l3s.mws.model.timeline.Date();
		date1.setStartDate("2009,2");
		date1.setHeadline("My first experiment in time-lapse photography");
		date1.setText("Nature at its finest in this video.");
		Asset a1=new Asset();
		a1.setMedia("");
		a1.setCaption("");
		a1.setCredit("");
		date1.setAsset(a1);
		timeline1.getDate().add(date1); */
		
		WholeTimeline wl=new WholeTimeline();
		wl.setTimeline(timeline1);
		if (this.searchResult1!=null) {
			timeline=(new JSONObject(wl)).toString();
			// timeline=" var timeline = new VMM.Timeline(); timeline.init('"+timeline+"'); ";
			timeline=" $(\"#timeline\").empty(); var timeline = new VMM.Timeline(); timeline.init('"+timeline+"'); ";
		} else
				timeline="";
		
		System.out.println(timeline);
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}

	/* public String getSearchDataPie() {
		return searchDataPie;
	}

	public void setSearchDataPie(String searchDataPie) {
		this.searchDataPie = searchDataPie;
	} */

	/* public String getSearchtext() {
		return searchtext;
	}

	public void setSearchtext(String searchtext) {
		this.searchtext = searchtext;
	} */
	
	public String getSearchtype() {
		return searchtype;
	}

	public void setSearchtype(String searchtype) {
		this.searchtype = searchtype;
	}

	/* public ArrayList<SearchResult> getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(ArrayList<SearchResult> searchResult) {
		this.searchResult = searchResult;
	} */

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
		searchDataPie1="var data = [ "; 
		List<String> datastring=new ArrayList<String>();
		for (YData a : list){
			datastring.add("{ label: \""+a.getSite()+"\", data: "+a.getQty()+"} ");
		}
		searchDataPie1+=Joiner.on(",").join(datastring);
		searchDataPie1+=" ]; ";
		searchDataPie1+=" var options = { series: { pie: {show: true, label: {show: false} }  }, grid: { hoverable: true, clickable: true }, legend: {show: false} }; ";
		searchDataPie1+="$.plot($(\"#chartpie1\"), data, options ); \n";
		String hover=" $(\"#chartpie1\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black;background-color:\", obj.series.color, \"\\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive\").html(html.join('')); }); ";
		hover=" $(\"#chartpie1\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black; \\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive\").html(html.join('')); }); ";
		searchDataPie1+=hover;  
		searchDataPie1+=" var choiceContainer = $(\"#chartpie1\");";
		searchDataPie1+=" choiceContainer.find(\"input\").click(plotAccordingToChoices);";
		searchDataPie1+=" function plotAccordingToChoices() { ";
		searchDataPie1+=" var key = $(this).attr(\"name\"); ";
		searchDataPie1+=" $( \"input[value*='\"+key+\"']\" ).trigger('click'); ";
		searchDataPie1+=" }";
		searchDataPie1+="  ";
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
	
	public String refineImgSearch(){
		// System.out.println("TYPE: REFINE IMG ");

		
		searchterms.removeAll(Collections.singleton(""));
		String q="";
		for (String t : this.searchterms){
			q+="\""+t+"\" ";
		}
		
		List<String> exclude=new ArrayList<String>(siteAvailablelist3);
		exclude.removeAll(siteSelectedlist3);
		exclude.addAll(excludeImg);
		for (String s : exclude)
			q+=" -site:"+s+" ";
		
		
		
		System.out.println(q);
		Track track=new Track();
		track.setDate((new GregorianCalendar()).getTime());
		track.setOperation("search");
		track.setParam1(q);
		track.setParam2("Refine IMG");
		track.setParam3("1");
		track.setUtente(this.user.getUtente());
		TrackDao td=new TrackDao();
		td.addTrack(track);
		
		List<SearchResult> toremove=new ArrayList<SearchResult>();
		searchResult3 = new ArrayList<SearchResult>(searchResultImg);
		for (SearchResult res:searchResult3){
			for (String exc : exclude)
				if (res.getUrl().contains(exc)){
					// System.out.println("removing "+exc);
					toremove.add(res);
				}
		}
		
		for (SearchResult sr : toremove){
			searchResult3.remove(sr);
		}
		
		if (searchterms.size()==0) searchterms.add("");
		// PIE IMAGE		
		StatsManager sm=new StatsManager();
		List<YData> list = sm.getMatcthTable(sm.getSites(searchResult3, null, null));
		searchDataPie3="var data = [ "; 
		ArrayList<String> datastring = new ArrayList<String>();
		for (YData a : list){
			datastring.add("{ label: \""+a.getSite()+"\", data: "+a.getQty()+"} ");
		}
		searchDataPie3+=Joiner.on(",").join(datastring);
		searchDataPie3+=" ]; ";
		// searchDataPie3+=" var options = { series: { pie: {show: true, label: {show: false} }  }, grid: { hoverable: true, clickable: true }, legend: {show: false} }; ";
		searchDataPie3+="$.plot($(\"#chartpie3\"), data, options ); \n";
		String hover=" $(\"#chartpie3\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black;background-color:\", obj.series.color, \"\\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive\").html(html.join('')); }); ";
		hover=" $(\"#chartpie3\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black; \\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive\").html(html.join('')); }); ";
		searchDataPie3+=hover;  
		searchDataPie3+=" var choiceContainer3 = $(\"#chartpie3\");";
		searchDataPie3+=" choiceContainer3.find(\"input\").click(plotAccordingToChoices3);";
		searchDataPie3+=" function plotAccordingToChoices3() { ";
		searchDataPie3+=" var key = $(this).attr(\"name\"); ";
		searchDataPie3+=" $( \"input[value*='\"+key+\"']\" ).trigger('click'); ";
		searchDataPie3+=" }";
		searchDataPie3+="  ";
		return "basicSearch";
	}
	
public String refineVideoSearch(){
		
		
		// System.out.println("TYPE: REFINE Video nuovo: ");
		
		//String accountKey = "BmbX+6Sy9/VEcS5oOjurccO5MQpKr2ewvLQ2vRHBKXQ";
		// TextManager tmgr=new TextManager();
		
		searchterms.removeAll(Collections.singleton(""));
		String q="";
		for (String t : this.searchterms){
			q+="\""+t+"\" ";
		}
		
		List<String> exclude=new ArrayList<String>(siteAvailablelist2);
		exclude.removeAll(siteSelectedlist2);
		exclude.addAll(excludeVid);
		for (String s : exclude)
			q+=" -site:"+s+" ";
		
		
		System.out.println(q);
		Track track=new Track();
		track.setDate((new GregorianCalendar()).getTime());
		track.setOperation("search");
		track.setParam1(q);
		track.setParam2("Refine Video");
		track.setParam3("1");
		track.setUtente(this.user.getUtente());
		TrackDao td=new TrackDao();
		td.addTrack(track);
		
		List<SearchResult> toremove=new ArrayList<SearchResult>();
		searchResult2 = new ArrayList<SearchResult>(searchResultVideo);
		for (SearchResult res:searchResult2){
			for (String exc : exclude)
				if (res.getUrl().contains(exc)){
					System.out.println("removing "+exc);
					toremove.add(res);
				}
		}
		
		for (SearchResult sr : toremove){
			searchResult2.remove(sr);
		}

		if (searchterms.size()==0) searchterms.add("");
		// PIE VIDEO		
		StatsManager sm=new StatsManager();
		List<YData> list = sm.getMatcthTable(sm.getSites(searchResult2, null, null));
		searchDataPie2="var data = [ "; 
		ArrayList<String> datastring = new ArrayList<String>();
		for (YData a : list){
			datastring.add("{ label: \""+a.getSite()+"\", data: "+a.getQty()+"} ");
			// siteAvailablelist2.add(a.getSite());
			// siteSelectedlist2.add(a.getSite());
		}
		searchDataPie2+=Joiner.on(",").join(datastring);
		searchDataPie2+=" ]; ";
		// searchDataPie2+=" var options = { series: { pie: {show: true, label: {show: false} }  }, grid: { hoverable: true, clickable: true }, legend: {show: false} }; ";
		searchDataPie2+="$.plot($(\"#chartpie2\"), data, options ); \n";
		String hover=" $(\"#chartpie2\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black;background-color:\", obj.series.color, \"\\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive\").html(html.join('')); }); ";
		hover=" $(\"#chartpie2\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black; \\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive\").html(html.join('')); }); ";
		searchDataPie2+=hover;  
		searchDataPie2+=" var choiceContainer2 = $(\"#chartpie2\");";
		searchDataPie2+=" choiceContainer2.find(\"input\").click(plotAccordingToChoices2);";
		searchDataPie2+=" function plotAccordingToChoices2() { ";
		searchDataPie2+=" var key = $(this).attr(\"name\"); ";
		searchDataPie2+=" $( \"input[value*='\"+key+\"']\" ).trigger('click'); ";
		searchDataPie2+=" }";
		searchDataPie2+="  ";
		
		
		
		return "basicSearch";
		
		
		
	}
	
	public String searcAll(int nuovo){
		
		
		
		// System.out.println("TYPE: ALL nuovo: "+nuovo);
		
		String accountKey = "BmbX+6Sy9/VEcS5oOjurccO5MQpKr2ewvLQ2vRHBKXQ";
		// accountKey = "TTtbr2RdFtN8bvzLZG9SIEgkDa7ecUDNrAkdwEy86UI"; // davide.taibi@libero.it
		TextManager tmgr=new TextManager();
		tmgr.setCotextrange(this.user.getUtente().getCotextrange());
		searchterms.removeAll(Collections.singleton(""));
		String q="";
		for (String t : this.searchterms){
			q+="\""+t+"\" ";
		}
		
		if (nuovo==1){
			siteAvailablelist1.clear();
			siteAvailablelist2.clear();
			siteAvailablelist3.clear();
			siteSelectedlist1.clear();
			siteSelectedlist2.clear();
			siteSelectedlist3.clear();
		}
		
		if (!(this.user.getUtente().hasAllLoc()))
				for (Generalsettings gs : this.loclist)
					if (this.loclistsel.compareTo(gs.getId()+"")==0)
						q+=" loc:"+locMap.get(gs.getValue());
		
		List<String> exclude=new ArrayList<String>(siteAvailablelist1);
		exclude.removeAll(siteSelectedlist1);
		exclude.addAll(excludeWeb);
		exclude.addAll(excludeImg);
		exclude.addAll(excludeVid);
		for (String s : exclude)
			q+=" -site:"+s;
		
		if (!(this.user.getUtente().hasAllLang()))
			// for (String s : this.langlistsel)
				for (Generalsettings gs : this.langlist)
					if (this.langlistsel.compareTo(gs.getId()+"")==0)
						q+=" language:"+gs.getValue()+" ";
		
		String qweb="    ";
		for (SiteSetItem ssi : this.user.getUtente().getSitesetitem()){
			qweb+= " site:"+ssi.getUrl()+" OR ";
		}
		qweb=qweb.substring(0, qweb.length()-4);
		if (qweb.trim().length()!=0) qweb=" ("+qweb+") ";
		
		q=q+" "+qweb; // ADDED TO HAVE RESTRICTION ALSO ON VIDEOS AND IMAGES 
		qweb="";
		System.out.println(q+" "+qweb);
		
		
		// if (this.searchtype.compareTo("Web")==0){
			AzureSearchWebQuery aq = new AzureSearchWebQuery();
			aq.setAppid(accountKey);			
			aq.setQuery(q+" "+qweb);

			// aq.setQuery(q);
			// System.out.println(q);
			List<AzureSearchWebResult> arsall=new ArrayList<AzureSearchWebResult>();
			int conta=-1;
            int contaquery=0;
            for (int i=1;i<=2 && conta!=0;i++){
                contaquery++;
                aq.setPerPage(50);
                aq.setPage(i);
				aq.doQuery();
				
				AzureSearchResultSet<AzureSearchWebResult> ars = aq.getQueryResult();
				// System.out.println("TOTAL:"+ars.getWebTotal());
				if (ars.getAsrs().size()<50) conta=0;
				for (AzureSearchWebResult anr : ars){
					arsall.add(anr);
				}
			}
			
			
			
			searchResultWeb = new ArrayList<SearchResult>();
			// WIKIMEDIA: http://en.wikipedia.org/w/api.php?action=query&titles=Berlin&prop=revisions&rvprop=timestamp&rvdir=newer&format=xml
			// LASTFM: http://ws.audioscrobbler.com/2.0/?method=artist.getshouts&artist=Berlin&api_key=9b6009eca365ded3a03c2b9673d54eb9&page=3
			List<String> ripetiz=new ArrayList<String>();
			for (AzureSearchWebResult anr : arsall){
				
				
				SearchWebResult r=new SearchWebResult();
				r.setTitle(anr.getTitle());
				// r.setDescription(tmgr.SingleTextToCheck(this.searchterms.get(0), anr.getDescription(), 0));
				
				r.setDescription(tmgr.MultipleTextToCheck(this.searchterms, anr.getDescription(), 0));
				teasers.add(anr.getDescription());
				r.setUrl(anr.getUrl());
				if (!ripetiz.contains(r.getUrl())){
					ripetiz.add(r.getUrl());
					searchResultWeb.add(r);
				}
			}
			searchResult1=new ArrayList<SearchResult>(searchResultWeb);
		// }
		// if (this.searchtype.compareTo("Video")==0){
			AzureSearchVideoQuery aqv = new AzureSearchVideoQuery();
			aqv.setAppid(accountKey);
			aqv.setQuery(q);
			List<AzureSearchVideoResult> arsallv=new ArrayList<AzureSearchVideoResult>();
			conta =-1;
            for (int i=1;i<=2 && conta!=0;i++){
            	contaquery++;
				aqv.setPerPage(50);
				aqv.setPage(i);
				aqv.doQuery();
				AzureSearchResultSet<AzureSearchVideoResult> ars = aqv.getQueryResult();
				
				if (ars.getAsrs().size()<50) conta=0;
				for (AzureSearchVideoResult anr : ars){
					// System.out.println("Page : "+i+" size ars: "+anr.getMediaUrl());
					arsallv.add(anr);
				}
			}
			// searchResult2=new ArrayList<SearchResult>();
			searchResultVideo = new ArrayList<SearchResult>();
			ripetiz=new ArrayList<String>();
			for (AzureSearchVideoResult anr : arsallv){
				SearchVideoResult r=new SearchVideoResult();
				r.setTitle(anr.getTitle());
				// r.setHeight(anr.getThumbnail().getHeight());
				// r.setWidth(anr.getThumbnail().getWidth());
				r.setRuntime(""+anr.getRunTime());
				r.setThumbnail(anr.getThumbnail());
				r.setUrl(anr.getMediaUrl());
				// System.out.println(r.getUrl());
				if (!ripetiz.contains(r.getUrl())){
					ripetiz.add(r.getUrl());
					searchResultVideo.add(r);
				}
			}
			searchResult2=new ArrayList<SearchResult>(searchResultVideo);
		// }
		// if (this.searchtype.compareTo("Image")==0){
			AzureSearchImageQuery aqi = new AzureSearchImageQuery();
			aqi.setAppid(accountKey);
			aqi.setQuery(q);
			List<AzureSearchImageResult> arsalli=new ArrayList<AzureSearchImageResult>();
			conta=-1;
            for (int i=1;i<=2 && conta!=0;i++){
                contaquery++;
				aqi.setPage(i);
				aqi.setPerPage(50);
				aqi.doQuery();
				AzureSearchResultSet<AzureSearchImageResult> ars = aqi.getQueryResult();
				if (ars.getAsrs().size()<50) conta=0;
				
				for (AzureSearchImageResult anr : ars){
					arsalli.add(anr);
				}
			}
			searchResultImg = new ArrayList<SearchResult>();
			ripetiz=new ArrayList<String>();
			for (AzureSearchImageResult anr : arsalli){
				SearchImageResult r=new SearchImageResult();
				r.setTitle(anr.getTitle());
				r.setHeight(anr.getHeight());
				r.setWidth(anr.getWidth());
				r.setUrl(anr.getMediaUrl());
				if (!ripetiz.contains(r.getUrl())){
					ripetiz.add(r.getUrl());
					searchResultImg.add(r);
				}
			}
			searchResult3=new ArrayList<SearchResult>(searchResultImg);
		// }
		
			Track track=new Track();
	        track.setDate((new GregorianCalendar()).getTime());
	        track.setOperation("search");
	        track.setParam1(q+" "+qweb);
	        track.setParam2(this.searchtype);
	        track.setParam3(""+contaquery);
	        track.setUtente(this.user.getUtente());
	        TrackDao td=new TrackDao();
	        td.addTrack(track);
			
		if (searchterms.size()==0) searchterms.add("");
		
		// PIE WEB
		StatsManager sm=new StatsManager();
		List<YData> list=sm.getMatcthTable(sm.getSites(searchResult1, null, null));
		searchDataPie1="var data = [ "; 
		List<String> datastring=new ArrayList<String>();
		for (YData a : list){
			datastring.add("{ label: \""+a.getSite()+"\", data: "+a.getQty()+"} ");
			if (nuovo==1){
				siteAvailablelist1.add(a.getSite());
				siteSelectedlist1.add(a.getSite());
			}
		}
		searchDataPie1+=Joiner.on(",").join(datastring);
		searchDataPie1+=" ]; ";
		searchDataPie1+=" var options = { series: { pie: {show: true, label: {show: false} }  }, grid: { hoverable: true, clickable: true }, legend: {show: false} }; ";
		searchDataPie1+="$.plot($(\"#chartpie1\"), data, options ); \n";
		// String hover=" $(\"#chartpie1\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black;background-color:\", obj.series.color, \"\\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive1\").html(html.join('')); }); ";
		String hover=" $(\"#chartpie1\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black; \\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive1\").html(html.join('')); }); ";
		searchDataPie1+=hover;  
		searchDataPie1+=" var choiceContainer = $(\"#chartpie1\");";
		searchDataPie1+=" choiceContainer.find(\"input\").click(plotAccordingToChoices);";
		searchDataPie1+=" function plotAccordingToChoices() { ";
		searchDataPie1+=" var key = $(this).attr(\"name\"); ";
		searchDataPie1+=" $( \"input[value*='\"+key+\"']\" ).trigger('click'); ";
		searchDataPie1+=" }";
		searchDataPie1+="  ";
		// calculateTimeline(searchResult);
		
		// PIE WEIGHTED WEB
		
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
		
		// PIE DOMAIN WEB
		
		List<YData> Llist=sm.getMatcthTable(sm.getLangSites(searchResult1, null, null));
		searchLangDataPie1="var langdata = [ "; 
		datastring=new ArrayList<String>();
		for (YData a : Llist){
			datastring.add("{ label: \""+a.getSite()+"\", data: "+a.getQty()+"} ");
			/* if (nuovo==1) {
				siteAvailableDomainlist1.add(a.getSite());
				siteSelectedDomainlist1.add(a.getSite());
			} */
		}
		searchLangDataPie1+=Joiner.on(",").join(datastring);
		searchLangDataPie1+=" ]; ";
		searchLangDataPie1+="$.plot($(\"#chartlangpie1\"), langdata, options ); \n";
		String hoverL=" $(\"#chartlangpie1\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black; \\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive1L\").html(html.join('')); }); ";
		searchLangDataPie1+=hoverL; 
		String plotclickL=" $(\"#chartlangpie1\").bind(\"plotclick\", function(event, pos, obj){ if (!obj){return;} }); ";
		searchLangDataPie1+=plotclickL; 
		searchLangDataPie1+=" var choiceContainerL = $(\"#chartlangpie1\");";
		searchLangDataPie1+=" choiceContainerL.find(\"input\").click(plotAccordingToChoicesL);";
		searchLangDataPie1+=" function plotAccordingToChoicesL() { ";
		searchLangDataPie1+=" var key = $(this).attr(\"name\"); ";
		searchLangDataPie1+=" $( \"input[value*='\"+key+\"']\" ).trigger('click'); ";
		searchLangDataPie1+=" }";
		searchLangDataPie1+="  ";
		alignSiteDomain();
		
		// BAR WEB
		searchDataBar1=" var datawebbar = [ "; 
		datastring=new ArrayList<String>();
		int conta2=0;
		for (YData a : list){
			datastring.add("{ label: \""+a.getSite()+"\", data: [ ["+conta2+","+a.getQty()+"] ]} ");
			conta2++;
		}
		searchDataBar1+=Joiner.on(",").join(datastring);
		searchDataBar1+=" ]; ";
		searchDataBar1+=" var baroptions = { series: { bars: {show: true, label: {show: false} }  }, grid: { hoverable: true, clickable: true }, legend: {show: false} }; ";
		searchDataBar1+="$.plot($(\"#chartbar1\"), datawebbar, baroptions ); \n";
		searchDataBar1+="  ";
		
		// PIE VIDEO		
		sm=new StatsManager();
		list=sm.getMatcthTable(sm.getSites(searchResult2, null, null));
		searchDataPie2="var data = [ "; 
		datastring=new ArrayList<String>();
		for (YData a : list){
			datastring.add("{ label: \""+a.getSite()+"\", data: "+a.getQty()+"} ");
			if (nuovo==1){
				siteAvailablelist2.add(a.getSite());
				siteSelectedlist2.add(a.getSite());
			}
		}
		searchDataPie2+=Joiner.on(",").join(datastring);
		searchDataPie2+=" ]; ";
		// searchDataPie2+=" var options = { series: { pie: {show: true, label: {show: false} }  }, grid: { hoverable: true, clickable: true }, legend: {show: false} }; ";
		searchDataPie2+="$.plot($(\"#chartpie2\"), data, options ); \n";
		// hover=" $(\"#chartpie2\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black;background-color:\", obj.series.color, \"\\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive2\").html(html.join('')); }); ";
		hover=" $(\"#chartpie2\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black; \\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive2\").html(html.join('')); }); ";
		searchDataPie2+=hover;  
		searchDataPie2+=" var choiceContainer2 = $(\"#chartpie2\");";
		searchDataPie2+=" choiceContainer2.find(\"input\").click(plotAccordingToChoices2);";
		searchDataPie2+=" function plotAccordingToChoices2() { ";
		searchDataPie2+=" var key = $(this).attr(\"name\"); ";
		searchDataPie2+=" $( \"input[value*='\"+key+\"']\" ).trigger('click'); ";
		searchDataPie2+=" }";
		searchDataPie2+="  ";
		
		// PIE IMAGE		
		sm=new StatsManager();
		list=sm.getMatcthTable(sm.getSites(searchResult3, null, null));
		searchDataPie3="var data = [ "; 
		datastring=new ArrayList<String>();
		for (YData a : list){
			datastring.add("{ label: \""+a.getSite()+"\", data: "+a.getQty()+"} ");
			if (nuovo==1){
				siteAvailablelist3.add(a.getSite());
				siteSelectedlist3.add(a.getSite());
			}
		}
		searchDataPie3+=Joiner.on(",").join(datastring);
		searchDataPie3+=" ]; ";
		// searchDataPie3+=" var options = { series: { pie: {show: true, label: {show: false} }  }, grid: { hoverable: true, clickable: true }, legend: {show: false} }; ";
		searchDataPie3+="$.plot($(\"#chartpie3\"), data, options ); \n";
		// hover=" $(\"#chartpie3\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black;background-color:\", obj.series.color, \"\\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive3\").html(html.join('')); }); ";
		hover=" $(\"#chartpie3\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black; \\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive3\").html(html.join('')); }); ";
		searchDataPie3+=hover;  
		searchDataPie3+=" var choiceContainer3 = $(\"#chartpie3\");";
		searchDataPie3+=" choiceContainer3.find(\"input\").click(plotAccordingToChoices3);";
		searchDataPie3+=" function plotAccordingToChoices3() { ";
		searchDataPie3+=" var key = $(this).attr(\"name\"); ";
		searchDataPie3+=" $( \"input[value*='\"+key+\"']\" ).trigger('click'); ";
		searchDataPie3+=" }";
		searchDataPie3+="  ";
		// System.out.println("IMAGES: "+searchResult3.size());
		
		return "basicSearch";
		
		
		
	}
	
	
}