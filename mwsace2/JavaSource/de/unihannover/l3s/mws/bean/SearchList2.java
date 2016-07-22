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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import net.billylieurance.azuresearch.AzureSearchResultSet;
import net.billylieurance.azuresearch.AzureSearchWebQuery;
import net.billylieurance.azuresearch.AzureSearchWebResult;


import de.unihannover.l3s.mws.dao.BingcacheDao;
import de.unihannover.l3s.mws.dao.TrackDao;
import de.unihannover.l3s.mws.dao.UtenteDao;
import de.unihannover.l3s.mws.model.Generalsettings;
import de.unihannover.l3s.mws.model.SearchResult;
import de.unihannover.l3s.mws.model.SearchWebResult;
import de.unihannover.l3s.mws.model.Track;


/**
 * Created by JBoss Tools
 */
@ManagedBean(name="searchlist2")
@ViewScoped
public class SearchList2 {

	private List<String> searchterms;
	
	@ManagedProperty(value="#{user}")
	private User user;

	
	private String radiowebnews;
	
	private String clickedTag="";
	private String clickedLink="";
	private ArrayList<SearchResult> searchResultEn=null;
	private ArrayList<SearchResult> searchResultWeb=null;
	
	Integer resultnumber;
	
	
	private ArrayList<SearchResult> searchResultDe=null;	
		
	private String resultId="0";
	
	public ArrayList<SearchResult> getSearchResultDe() {
		return searchResultDe;
	}

	public void setSearchResultDe(ArrayList<SearchResult> searchResultDe) {
		this.searchResultDe = searchResultDe;
	}


	private String siteSetId;
	private String siteText;
	private String searchsettings="";

	
	public Integer getResultnumber() {
		return resultnumber;
	}

	public void setResultnumber(Integer resultnumber) {
		this.resultnumber = resultnumber;
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
	
	
	
	public void loadRestriction(){
		searchsettings="";
		searchsettings+="<b>Search type:</b> "+this.radiowebnews+"<br />";
		searchsettings+="<b>Number of results:</b> "+this.resultnumber+"<br />";
	}

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}
	
	public String getClickedLink() {
		return clickedLink;
	}

	public void setClickedLink(String clickedLink) {
		this.clickedLink = clickedLink;
	}

	public String getClickedTag() {
		return clickedTag;
	}

	public void setClickedTag(String clickedTag) {
		this.clickedTag = clickedTag;
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
		radiowebnews="web";
		resultnumber=10;
		
		searchterms = new ArrayList<String>();
		searchterms.add("");
		searchterms.add("");
		searchterms.add("");
		searchterms.add("");
		
		loadRestriction();
    }
	
    public void extend() {
    	searchterms.add("");
    }
	
    
    public List<String> getSearchterms() {
		return searchterms;
	}


	public void setSearchterms(List<String> searchterms) {
		this.searchterms = searchterms;
	}

	public void traceclick(String lang){
		Track track=new Track();
        track.setDate((new GregorianCalendar()).getTime());
        track.setOperation("wsstudent_clicklink");
        track.setParam1(this.clickedLink);
        track.setParam2(lang);
        track.setParam3("");
        track.setUtente(this.user.getUtente());
        TrackDao td=new TrackDao();
        td.addTrack(track);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	
	public ArrayList<SearchResult> getSearchResultWeb(){
		if (searchResultWeb!=null)
			System.out.println("searchResultWeb: "+searchResultWeb.size());
		else
			System.out.println("searchResultWeb: null");
		return searchResultWeb;
	}

	private ArrayList<SearchResult> search(String lang,String q){
		String accountKey = "BmbX+6Sy9/VEcS5oOjurccO5MQpKr2ewvLQ2vRHBKXQ";
		String market="";
		if (lang.compareTo("en")==0){
			market="en-US";
		}else{
			market="de-DE";
		}
		BingcacheDao bcdao=new BingcacheDao();
		searchResultWeb = bcdao.getCache(q+":"+this.resultnumber, market);
		if (searchResultWeb==null){
			searchResultWeb = new ArrayList<SearchResult>();
			AzureSearchWebQuery aq = new AzureSearchWebQuery();
			aq.setAppid(accountKey);
			aq.setMarket(market);
			aq.setQuery(q);
			// System.out.println(q);
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
			
			
	
			List<String> ripetiz=new ArrayList<String>();
			for (AzureSearchWebResult anr : arsall){			
				SearchWebResult r=new SearchWebResult();
				r.setTitle(anr.getTitle());
				r.setDescription(anr.getDescription());
				r.setUrl(anr.getUrl());
				
				// System.out.println(anr.getUrl());
				if (!ripetiz.contains(r.getUrl())){
					ripetiz.add(r.getUrl());
					searchResultWeb.add(r);
					bcdao.addToCache(r, market, q+":"+this.resultnumber);
				}
			}
		}
		return searchResultWeb;
	}
	
	public String searcAll(int nuovo) throws IOException{
		searchResultEn=new ArrayList<SearchResult>();
		searchResultDe=new ArrayList<SearchResult>();
		clickedTag="";
		// accountKey = "TTtbr2RdFtN8bvzLZG9SIEgkDa7ecUDNrAkdwEy86UI"; // davide.taibi@libero.it

		for (int l=0;l<searchterms.size();l++)
			if (searchterms.get(l).trim().compareTo("")==0)
				searchterms.set(l, searchterms.get(0));
				
		String q="";
		q="\""+searchterms.get(0)+"\"";
		searchResultEn=search("en",q);
		
		q="\""+searchterms.get(1)+"\"";
		searchResultDe=search("de",q);
		
		Track track=new Track();
	        track.setDate((new GregorianCalendar()).getTime());
	        track.setOperation("wsstudent_search_list");
	        track.setParam1(q);
	        track.setParam2(this.radiowebnews);
	        track.setParam3("");
	        track.setUtente(this.user.getUtente());
	        TrackDao td=new TrackDao();
	        td.addTrack(track);
			
		if (searchterms.size()==0) searchterms.add("");
		return "languageSearchStudent";	
	}
}