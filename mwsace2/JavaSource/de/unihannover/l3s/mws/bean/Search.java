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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

import org.json.JSONObject;

import com.google.common.base.Joiner;

import de.unihannover.l3s.mws.dao.TrackDao;
import de.unihannover.l3s.mws.model.SearchImageResult;
import de.unihannover.l3s.mws.model.SearchResult;
import de.unihannover.l3s.mws.model.SearchVideoResult;
import de.unihannover.l3s.mws.model.SearchWebResult;
import de.unihannover.l3s.mws.model.Track;
import de.unihannover.l3s.mws.model.Utente;
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
@ManagedBean(name="search")
@ViewScoped
public class Search {
	// private String searchtext;
	private String searchtype="Web";
	private String searchDataPie="";
	private ArrayList<String> searchtypelist=null;
	private ArrayList<String> siteAvailablelist=null;
	private List<String> siteSelectedlist;
	private ArrayList<SearchResult> searchResult=null;
	private String timeline;
	private List<String> searchterms;
	@ManagedProperty(value="#{user}")
	private User user;
	
	
	@PostConstruct
    public void init() {
		searchterms = new ArrayList<String>();
		siteAvailablelist = new ArrayList<String>();
		siteSelectedlist = new ArrayList<String>();
		searchterms.add("");
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

	public ArrayList<String> getSiteAvailablelist() {
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
		if (this.searchResult!=null) {
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

	public String getSearchDataPie() {
		return searchDataPie;
	}

	public void setSearchDataPie(String searchDataPie) {
		this.searchDataPie = searchDataPie;
	}

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

	public ArrayList<SearchResult> getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(ArrayList<SearchResult> searchResult) {
		this.searchResult = searchResult;
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
	
	
	public String searcMe(int nuovo){
		
		
		System.out.println("TYPE: "+this.searchtype+" nuovo: "+nuovo);
		
		String accountKey = "BmbX+6Sy9/VEcS5oOjurccO5MQpKr2ewvLQ2vRHBKXQ";
		TextManager tmgr=new TextManager();
		tmgr.setCotextrange(this.user.getUtente().getCotextrange());
		searchterms.removeAll(Collections.singleton(""));
		String q="";
		for (String t : this.searchterms){
			q+="\""+t+"\" ";
		}
		
		if (nuovo==1){
			siteAvailablelist.clear();
			siteSelectedlist.clear();
		}
		List<String> exclude=new ArrayList<String>(siteAvailablelist);
		exclude.removeAll(siteSelectedlist);
		for (String s : exclude)
			q+=" -site:"+s+" ";
		
		System.out.println(q);
		Track track=new Track();
		track.setDate((new GregorianCalendar()).getTime());
		track.setOperation("search");
		track.setParam1(q);
		track.setParam2(this.searchtype);
		track.setParam3(""+nuovo);
		track.setUtente(this.user.getUtente());
		TrackDao td=new TrackDao();
		td.addTrack(track);
		
		if (this.searchtype.compareTo("Web")==0){
			AzureSearchWebQuery aq = new AzureSearchWebQuery();
			aq.setAppid(accountKey);			
			aq.setQuery(q);

			// aq.setQuery(q);
			// System.out.println(q);
			List<AzureSearchWebResult> arsall=new ArrayList<AzureSearchWebResult>();
			for (int i=1;i<8;i++){
				aq.setPage(i);
				aq.doQuery();
				AzureSearchResultSet<AzureSearchWebResult> ars = aq.getQueryResult();
				for (AzureSearchWebResult anr : ars){
					arsall.add(anr);
				}
			}
			
			searchResult=new ArrayList<SearchResult>();
			// WIKIMEDIA: http://en.wikipedia.org/w/api.php?action=query&titles=Berlin&prop=revisions&rvprop=timestamp&rvdir=newer&format=xml
			// LASTFM: http://ws.audioscrobbler.com/2.0/?method=artist.getshouts&artist=Berlin&api_key=9b6009eca365ded3a03c2b9673d54eb9&page=3
			
			for (AzureSearchWebResult anr : arsall){
				
				
				SearchWebResult r=new SearchWebResult();
				r.setTitle(anr.getTitle());
				r.setDescription(tmgr.SingleTextToCheck(this.searchterms.get(0), anr.getDescription(), 0));
				r.setUrl(anr.getUrl());
				System.out.println(r.getUrl());
				searchResult.add(r);
			        // System.out.println(anr.getTitle()); 
			        // System.out.println(tmgr.SingleTextToCheck(this.searchtext, anr.getDescription(), 1));
			        // System.out.println(anr.getUrl());
			        // System.out.println();
			}
		}
		if (this.searchtype.compareTo("Video")==0){
			AzureSearchVideoQuery aq = new AzureSearchVideoQuery();
			aq.setAppid(accountKey);
			aq.setQuery(q);
			List<AzureSearchVideoResult> arsall=new ArrayList<AzureSearchVideoResult>();
			for (int i=1;i<8;i++){
				aq.setPage(i);
				aq.doQuery();
				AzureSearchResultSet<AzureSearchVideoResult> ars = aq.getQueryResult();
				for (AzureSearchVideoResult anr : ars){
					arsall.add(anr);
				}
			}
			searchResult=new ArrayList<SearchResult>();
			for (AzureSearchVideoResult anr : arsall){
				SearchVideoResult r=new SearchVideoResult();
				r.setTitle(anr.getTitle());
				// r.setHeight(anr.getThumbnail().getHeight());
				// r.setWidth(anr.getThumbnail().getWidth());
				r.setRuntime(""+anr.getRunTime());
				r.setThumbnail(anr.getThumbnail());
				r.setUrl(anr.getMediaUrl());
				searchResult.add(r);
			}
		}
		if (this.searchtype.compareTo("Image")==0){
			AzureSearchImageQuery aq = new AzureSearchImageQuery();
			aq.setAppid(accountKey);
			aq.setQuery(q);
			List<AzureSearchImageResult> arsall=new ArrayList<AzureSearchImageResult>();
			for (int i=1;i<8;i++){
				aq.setPage(i);
				aq.doQuery();
				AzureSearchResultSet<AzureSearchImageResult> ars = aq.getQueryResult();
				for (AzureSearchImageResult anr : ars){
					arsall.add(anr);
				}
			}
			searchResult=new ArrayList<SearchResult>();
			for (AzureSearchImageResult anr : arsall){
				SearchImageResult r=new SearchImageResult();
				r.setTitle(anr.getTitle());
				r.setHeight(anr.getHeight());
				r.setWidth(anr.getWidth());
				r.setUrl(anr.getMediaUrl());
				searchResult.add(r);
			}
		}
		if (searchterms.size()==0) searchterms.add("");
		StatsManager sm=new StatsManager();
		List<YData> list=sm.getMatcthTable(sm.getSites(searchResult, null, null));
		
		searchDataPie="var data = [ "; 
		List<String> datastring=new ArrayList<String>();
		for (YData a : list){
			// System.out.println(a.getSite()+"---"+a.getQty());
			datastring.add("{ label: \""+a.getSite()+"\", data: "+a.getQty()+"} ");
			if (nuovo==1){
				siteAvailablelist.add(a.getSite());
				siteSelectedlist.add(a.getSite());
			}
			
		}
		searchDataPie+=Joiner.on(",").join(datastring);
		searchDataPie+=" ]; ";
		
		// searchDataPie+=" var options = { series: { pie: { show: true } }, legend: { show: true, labelFormatter: function(label, series) { return('<input type=\"checkbox\" name=\"' + label +'\" checked=\"checked\" id=\"id' + label + '\"><a href=\"http://'+label+'\" target=\"_blank\">'+label+'</a> '); } } }; ";
		searchDataPie+=" var options = { series: { pie: {show: true, label: {show: false} }  }, grid: { hoverable: true, clickable: true }, legend: {show: false} }; ";
		searchDataPie+="$.plot($(\"#chartpie\"), data, options ); \n";
		
		String hover=" $(\"#chartpie\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black;background-color:\", obj.series.color, \"\\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive\").html(html.join('')); }); ";
		hover=" $(\"#chartpie\").bind(\"plothover\", function(event, pos, obj){ if (!obj){return;} percent = parseFloat(obj.series.percent).toFixed(2); var html = []; html.push(\"<div style=\\\"flot:left;width:105px;height:20px;text-align:center;border:0px solid black; \\\">\", \"<span style=\\\"font-weight:bold;color:red\\\">\", obj.series.label, \" (\", percent, \"%)</span>\", \"</div>\"); $(\"#showInteractive\").html(html.join('')); }); ";
		
		searchDataPie+=hover;  
		searchDataPie+=" var choiceContainer = $(\"#chartpie\");";
		searchDataPie+=" choiceContainer.find(\"input\").click(plotAccordingToChoices);";
		searchDataPie+=" function plotAccordingToChoices() { ";
		searchDataPie+=" var key = $(this).attr(\"name\"); ";
		searchDataPie+=" $( \"input[value*='\"+key+\"']\" ).trigger('click'); ";
		
		 
		
		// searchDataPie+=" document.getElementById('checkform').submit();";
		  // searchDataPie+=" var data = []; ";
		/* searchDataPie+=" choiceContainer.find(\"input:checked\").each(function () { ";
		
		searchDataPie+="var data1 = [ "; 
		searchDataPie+=Joiner.on(",").join(datastring);
		searchDataPie+=" ]; ";
		searchDataPie+=" var options1 = { series: { pie: { show: true } }, legend: { show: true, labelFormatter: function(label, series) { return('<input type=\"checkbox\" name=\"' + label +'\" checked=\"checked\" id=\"id' + label + '\"><a href=\"http://'+label+'\" target=\"_blank\">'+label+'</a> '); } } }; ";
		// searchDataPie+=" var key = $(this).attr(\"name\"); if (key && data[key]) data.push(datasets[key]); });";
		searchDataPie+=" var key = $(this).attr(\"name\");";
		searchDataPie+=" var index = data1.indexOf(key); if (index > -1) { data1.splice(index, 1);	};";
		// searchDataPie+=" if (data.length > 0) $.plot($(\"#placeholder\"), data, {  yaxis: { min: 0 }, xaxis: { tickDecimals: 0 }  }); ";
		searchDataPie+="$.plot($(\"#chartpie\"), data1, options1 ); });"; */
		searchDataPie+=" }";
		
		searchDataPie+="  ";
		calculateTimeline(searchResult);
		
		return "basicSearch";
		
		
		
	}
}