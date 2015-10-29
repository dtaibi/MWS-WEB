package de.unihannover.l3s.mws.bean;

import java.util.ArrayList;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.json.JSONObject;

import de.unihannover.l3s.mws.model.timeline.Asset;
import de.unihannover.l3s.mws.model.timeline.Date;
import de.unihannover.l3s.mws.model.timeline.Timeline;

@ManagedBean(name="timeline")
@SessionScoped
public class TestTimeline {

	String timeline;
	
	public String getTimeline() {
		Timeline timeline1=new Timeline();
		timeline1.setHeadline("Johnny B Goode");
		timeline1.setType("default");
		timeline1.setStartDate("2009,1");
		timeline1.setText("<i><span class='c1'>Designer</span> & <span class='c2'>Developer</span></i>");
		Asset a=new Asset();
		a.setMedia("aaa");
		a.setCredit("<a href='http://dribbble.com/shots/221641-iOS-Icon'>iOS Icon by Asher</a>");
		a.setCaption("");
		timeline1.setAsset(a);
		Date date1=new Date();
		date1.setStartDate("2009,2");
		date1.setHeadline("My first experiment in time-lapse photography");
		date1.setText("Nature at its finest in this video.");
		Asset a1=new Asset();
		a1.setMedia("");
		a1.setCaption("");
		a1.setCredit("");
		date1.setAsset(a1);
		Date date2=new Date();
		date2.setStartDate("2009,5");
		date2.setHeadline("http://wikitravel.org/en/Berlin");
		date2.setText("");
		Asset a2=new Asset();
		a2.setMedia("http://localhost:8080/mwsace2/javax.faces.resource/img/icon/icon-wikipedia.png.jsf?ln=timeline");
		a2.setCaption("");
		a2.setCredit("");
		date2.setAsset(a2);
		timeline1.setDate(new ArrayList<Date>());
		timeline1.getDate().add(date1);
		timeline1.getDate().add(date2);
		this.timeline=(new JSONObject(timeline1)).toString();
		return timeline;
	}

	 public static void main(String[] args) { 
			Timeline timeline=new Timeline();
			timeline.setHeadline("Johnny B Goode");
			timeline.setType("default");
			timeline.setStartDate("2009,1");
			timeline.setText("<i><span class='c1'>Designer</span> & <span class='c2'>Developer</span></i>");
			Asset a=new Asset();
			a.setMedia("aaa");
			a.setCredit("<a href='http://dribbble.com/shots/221641-iOS-Icon'>iOS Icon by Asher</a>");
			a.setCaption("");
			timeline.setAsset(a);
			Date date1=new Date();
			date1.setStartDate("2009,2");
			date1.setHeadline("My first experiment in time-lapse photography");
			date1.setText("Nature at its finest in this video.");
			Asset a1=new Asset();
			a1.setMedia("");
			a1.setCaption("");
			a1.setCredit("");
			date1.setAsset(a1);
			Date date2=new Date();
			date2.setStartDate("2009,5");
			date2.setHeadline("http://wikitravel.org/en/Berlin");
			date2.setText("");
			Asset a2=new Asset();
			a2.setMedia("http://localhost:8080/mwsace2/javax.faces.resource/img/icon/icon-wikipedia.png.jsf?ln=timeline");
			a2.setCaption("");
			a2.setCredit("");
			date2.setAsset(a2);
			timeline.setDate(new ArrayList<Date>());
			timeline.getDate().add(date1);
			timeline.getDate().add(date2);
			
			System.out.println(new JSONObject(timeline).toString()); 
	 }
	
}
