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
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import de.unihannover.l3s.mws.dao.TrackDao;
import de.unihannover.l3s.mws.model.Track;
import de.unihannover.l3s.mws.model.Utente;



@ManagedBean(name="tracks")
@SessionScoped
public class TrackBean {
	
	@ManagedProperty(value="#{user}")
	private User loggeduser;
	
	private HashMap<Utente, HashMap<String,Integer>> chartmap; // =new HashMap<Utente, HashMap<String,Integer>>();
	
	private List<String> oplist=new ArrayList<String>();
	
	private String startDate=""+(new SimpleDateFormat("yyyy-MM-dd")).format(new Date(0));
	private String endDate=""+(new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
	
	private Utente user;
	
	public void interactionChanged(ValueChangeEvent e){
		operation = e.getNewValue().toString();
		
	}
	public void userChanged(ValueChangeEvent e){
		operation = e.getNewValue().toString();		
	}
	
	public void operationChanged(ValueChangeEvent e){
		userselected = e.getNewValue().toString();
		
	}
	
	public TrackBean() {
		for (Operation o:this.getOperationValue())
			oplist.add(o.getOperationValue());	
	}
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
	public String getJchartcode(){
			String out="";
			
			out+="var randomScalingFactor = function(){ return Math.round(Math.random()*255)};";
			out+="var lineChartData = {";
			out+="labels : [ ";
			for (Utente u:chartmap.keySet()){
				out+="\""+u.getUsername()+"\",";
			}
			out+="],";
			out+="    datasets : [ ";
	
			
			int r=220;
			int g=220;
			int b=250;
			for (String s:oplist){
				
				out+="        { ";
				out+="             fillColor : \"rgba("+r+","+g+","+b+",0.5)\", ";
				out+=" strokeColor : \"rgba("+r+","+g+","+b+",0.8)\", ";
				out+=" highlightFill: \"rgba("+r+","+g+","+b+",0.75)\", ";
				out+=" highlightStroke: \"rgba("+r+","+g+","+b+",1)\", ";
				out+=" data : [ ";
				for (Utente u:chartmap.keySet()){
					out+=chartmap.get(u).get(s)+" , ";
				}
				out+=" ] }, ";
				r=r-30;
				g=g-30;
				b=b-30;
			}
			
			out+= "] ";
		    out+="};";
			
			return out;
	}
	
	public String getPieChart(){
		String out="";
		Map<String,Integer> interactionmap=new HashMap<String,Integer>();
		for (Interaction interaction:getInteractionValue()){
			Map<Integer,Integer> values;
			if (this.interaction!=null){
				if (interaction.interactionLabel.contains(this.interaction)){
					values=getTrackByDate(interaction.interactionLabel);
					Integer sum=0;
					for (Integer week : values.keySet())
						sum+=values.get(week);
					interactionmap.put(interaction.interactionLabel, sum);
				}else{
					values=new HashMap<Integer,Integer>();
					interactionmap.put(interaction.interactionLabel, 0);
				}
			}else{
					values=getTrackByDate(interaction.interactionLabel);
					Integer sum=0;
					for (Integer week : values.keySet())
						sum+=values.get(week);
					interactionmap.put(interaction.interactionLabel, sum);
				}
		}
		
		
		out+= "var pieData = [";
   out+= "{";
      out+= "value: 25,";
      out+= "label: 'Java',";
      out+= "color: '#811BD6'";
   out+= "},";
   out+= "{";
      out+= "value: 10,";
      out+= "label: 'Scala',";
      out+= "color: '#9CBABA'";
   out+= "},";
out+= "];";
		
		out+="var pieChartData = [";
		
		int r=220;
		int g=220;
		int b=250;
		for (Interaction interaction:getInteractionValue()){
			out+= "{";
			out+="value:"+interactionmap.get(interaction.interactionLabel)+" , ";
			out+="label: \""+interaction.interactionValue+"\" , ";
			out+= "color: \"rgba("+r+","+g+","+b+",1)\", ";
			out+= "},";
			r=r-30;
			g=g-30;
			b=b-30;
		}
		out+= "];";
		/*
		out+="    datasets : [ ";
		
		
		
		
			
		out+="        { ";
		out+="             fillColor : \"rgba("+r+","+g+","+b+",0.5)\", ";
		out+=" strokeColor : \"rgba("+r+","+g+","+b+",0.8)\", ";
		out+=" highlightFill: \"rgba("+r+","+g+","+b+",0.75)\", ";
		out+=" highlightStroke: \"rgba("+r+","+g+","+b+",1)\", ";
		out+=" data : [ ";

		for (Interaction interaction:getInteractionValue()){
				out+=interactionmap.get(interaction.interactionLabel)+" , ";
		}
		out+=" ] }, ";
		
		
		
		out+= "], ";
		out+="labels : [ ";
		for (Interaction interaction:getInteractionValue()){ // 56 weeks in a year
			out+="\""+interaction.interactionValue+"\",";
		}
		out+="]";
	    out+="};";
		*/
		return out;
	}
	
	public String getJchartall(){
		String out="";
		int minweek=56;
		int maxweek=0;
		Map<String,Map<Integer,Integer>> interactionmap=new HashMap<String,Map<Integer,Integer>>();
		for (Interaction interaction:getInteractionValue()){
			Map<Integer,Integer> values;
			if (this.interaction!=null){
				if (interaction.interactionLabel.contains(this.interaction)){
					values=getTrackByDate(interaction.interactionLabel);
					interactionmap.put(interaction.interactionLabel, values);
					if (values.keySet().size()>0){
						int minrel=Collections.min(values.keySet());
						if (minrel<minweek)
							minweek=minrel;
						int maxrel=Collections.max(values.keySet());
						if (maxrel>maxweek)
							maxweek=maxrel;
					}
				}else{
					values=new HashMap<Integer,Integer>();
					interactionmap.put(interaction.interactionLabel, values);
				}
			}else{
					values=getTrackByDate(interaction.interactionLabel);
					interactionmap.put(interaction.interactionLabel, values);
					if (values.keySet().size()>0){
						int minrel=Collections.min(values.keySet());
						if (minrel<minweek)
							minweek=minrel;
						int maxrel=Collections.max(values.keySet());
						if (maxrel>maxweek)
							maxweek=maxrel;
					}
				}
		}
		
		
		out+="var allChartData = {";
		
		out+="    datasets : [ ";
		
		
		int r=220;
		int g=220;
		int b=250;
		for (Interaction interaction:getInteractionValue()){
			
			out+="        { ";
			out+="             label : \""+interaction.interactionLabel+"\", ";
			out+="             fillColor : \"rgba("+r+","+g+","+b+",0.5)\", ";
			out+=" strokeColor : \"rgba("+r+","+g+","+b+",0.8)\", ";
			out+=" highlightFill: \"rgba("+r+","+g+","+b+",0.75)\", ";
			out+=" highlightStroke: \"rgba("+r+","+g+","+b+",1)\", ";
			out+=" data : [ ";

				for (int i=minweek;i<=maxweek;i++){
					if (interactionmap.get(interaction.interactionLabel).containsKey(i))
						out+=interactionmap.get(interaction.interactionLabel).get(i)+" , ";
					else
						out+="0"+" , ";
				}
			out+=" ] }, ";
			r=r-30;
			g=g-30;
			b=b-30;
		}
		
		out+= "], ";
		out+="labels : [ ";
		for (int i=minweek;i<=maxweek;i++){ // 56 weeks in a year
			out+="\""+i+"\",";
		}
		out+="]";
	    out+="};";
		
		return out;
}
	
	@PostConstruct
    public void init() {
		
    }

	
	private Map<Integer,Integer> getTrackByDate(String interactionLabel){ 
		TrackDao trackdao=new TrackDao();
		List<Track> tracklist = trackdao.getTrackByOperationListAndUserAndDate(getOperationsFromInteraction(interactionLabel),this.userselected,this.startDate,this.endDate);
		Map<Integer,Integer> values=new HashMap<Integer,Integer>();
		for(Track t : tracklist){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(t.getDate());
			Integer week=gc.get(Calendar.WEEK_OF_YEAR);
			if (values.containsKey(week)){
				values.put(week,values.get(week)+1);
			}else{
				values.put(week, 1);
			}
		}
		return values;
	}
// Anousheh
	List<Track> list;
	public String  interaction ;
	
	
	public String  operation ;
	List<Track> operationlist;

	public String  userselected ;
	List<Utente> userselectedList;
	
	public String getUserselected() {
		return userselected;
	}

	public void setUserselected(String userselected) {
		this.userselected = userselected;
	}

	public List<Utente> getUserselectedList() {
		TrackDao trackdao=new TrackDao(); 
		userselectedList=trackdao.getAllUsersInTrack();
		return userselectedList;
	}

	public void setUserselectedList(List<Utente> userselectedList) {
		this.userselectedList = userselectedList;
	}

	public List<Track> getList() {
		return list;
	}

	public void setList(List<Track> list) {
		this.list = list;
	}
	
	public Utente getUser() {
		return user;
	}

	public void setUser(Utente user) {
		this.user = user;
	}
	
	public User getLoggeduser() {
		return loggeduser;
	}

	public void setLoggeduser(User loggeduser) {
		this.loggeduser = loggeduser;
	}
// Interactions	
	public String getInteraction() {
		return interaction;
	}

	public void setInteraction(String interaction) {
		this.interaction = interaction;
	}
	public void changeInteraction(AjaxBehaviorEvent event){
		TrackDao trackdao=new TrackDao();
		List<String> selectedoperations=new ArrayList<String>();
		if (this.operation!=null && this.operation.compareTo("")!=0){
			selectedoperations.add(this.operation);
		}else{
			for(Operation o : this.getOperationValue()){
				selectedoperations.add(o.operationValue);
			}
		}
		// List<Track> tracklist = trackdao.getTrackByOperation(this.operation);
		List<Track> tracklist = trackdao.getTrackByOperationListAndUserAndDate(selectedoperations,this.userselected,this.startDate,this.endDate);
		this.list=tracklist;
		updateChartMap(tracklist);
	}
	
	public static class Interaction{
  		public String interactionLabel;
  		public String interactionValue;
  		
  		public Interaction(String interactionLabel, String interactionValue){
  			this.interactionLabel = interactionLabel;
  			this.interactionValue = interactionValue;
  		}
  		
  		public String getInteractionLabel(){
  			return interactionLabel;
  		}
  		
  		public String getInteractionValue(){
  			return interactionValue;
  		}
  	}
  	
  	public Interaction[] getInteractionValue() {
  		
  		Interaction[] interactionList = new Interaction[3];
  		interactionList[0] = new Interaction("Interaction - System", "System");
  		interactionList[1] = new Interaction("Interaction - Search", "Search");
  		interactionList[2] = new Interaction("Interaction - Storyboard", "Storyboard");
  		
  		return interactionList;
  	
  	}
	
  	public void setDates(AjaxBehaviorEvent event){
  		TrackDao trackdao=new TrackDao();
  		
  		List<String> selectedoperations=new ArrayList<String>();
		if (this.operation!=null && this.operation.compareTo("")!=0){
			selectedoperations.add(this.operation);
		}else{
			for(Operation o : this.getOperationValue()){
				selectedoperations.add(o.operationValue);
			}
		}
		List<Track> tracklist = trackdao.getTrackByOperationListAndUserAndDate(selectedoperations,this.userselected,this.startDate,this.endDate);
		
		// List<Track> tracklist = trackdao.getTrackByOperationAndUserAndDate(this.operation,this.userselected,this.startDate,this.endDate);
		this.list=tracklist;
		oplist=new ArrayList<String>();
		if (this.operation!=null && this.operation.compareTo("")!=0){
			oplist.add(this.operation);
		}else{
			for (Operation o:this.getOperationValue())
				oplist.add(o.getOperationValue());	
		}
		updateChartMap(tracklist);
  	}
  	
	public void changeOperation(AjaxBehaviorEvent event){
		TrackDao trackdao=new TrackDao(); 
		
		List<String> selectedoperations=new ArrayList<String>();
		if (this.operation!=null && this.operation.compareTo("")!=0){
			selectedoperations.add(this.operation);
		}else{
			for(Operation o : this.getOperationValue()){
				selectedoperations.add(o.operationValue);
			}
		}
		List<Track> tracklist = trackdao.getTrackByOperationListAndUserAndDate(selectedoperations,this.userselected,this.startDate,this.endDate);
		
		//List<Track> tracklist = trackdao.getTrackByOperationAndUserAndDate(this.operation,this.userselected,this.startDate,this.endDate);
		this.list=tracklist;
		oplist=new ArrayList<String>();
		if (this.operation!=null && this.operation.compareTo("")!=0){
			oplist.add(this.operation);
		}else{
			for (Operation o:this.getOperationValue())
				oplist.add(o.getOperationValue());	
		}
		updateChartMap(tracklist);
	}
	
	public void changeUserselected(AjaxBehaviorEvent event){
		TrackDao trackdao=new TrackDao(); 
		
		List<String> selectedoperations=new ArrayList<String>();
		if (this.operation!=null && this.operation.compareTo("")!=0){
			selectedoperations.add(this.operation);
		}else{
			for(Operation o : this.getOperationValue()){
				selectedoperations.add(o.operationValue);
			}
		}
		List<Track> tracklist = trackdao.getTrackByOperationListAndUserAndDate(selectedoperations,this.userselected,this.startDate,this.endDate);
		
		//List<Track> tracklist = trackdao.getTrackByOperationAndUserAndDate(this.operation,this.userselected,this.startDate,this.endDate);
		this.list=tracklist;
		oplist=new ArrayList<String>();
		if (this.operation!=null && this.operation.compareTo("")!=0){
			oplist.add(this.operation);
		}else{
			for (Operation o:this.getOperationValue())
				oplist.add(o.getOperationValue());	
		}
		updateChartMap(tracklist);
	}
	
	
	private void updateChartMap(List<Track> tracklist){
		chartmap=new HashMap<Utente, HashMap<String,Integer>>();
		
		
		for (Track t:tracklist){
			if (chartmap.containsKey(t.getUtente())){
				HashMap<String,Integer> map=chartmap.get(t.getUtente());
				String operation=t.getOperation();
				if(map.containsKey(operation)){
					map.put(operation,map.get(operation)+1);
				}
			}else{
				HashMap<String,Integer> map=new HashMap<String,Integer>();
				for (String op : oplist)
					map.put(op, 0);
				String operation=t.getOperation();
				if(map.containsKey(operation)){
					map.put(operation,map.get(operation)+1);
				}
				chartmap.put(t.getUtente(), map);
			}
		}
	}
	
	public String loadList(){
		TrackDao trackdao=new TrackDao(); 
		List<Track> tracklist = trackdao.getAllTrackBeans();
		this.list=tracklist;
		updateChartMap(tracklist);
		return "trackchart";
	}
	
	public ArrayList<String> getOperationsFromInteraction(String interaction) {
		ArrayList<String> operations=new ArrayList<String>();
  		if (interaction==null){
  			{
  				operations.add("login");
  				operations.add("logout");
  				operations.add( "load_site_settings");
  				operations.add("save_site_settings");
  				operations.add("search");
  				operations.add("load_search_list");
  				operations.add("save_search");
  				operations.add("load_search");
  				operations.add("delete_search");
  				operations.add( "open_search");
  				operations.add("load_storyboard_list");
  				operations.add("save_storyboard");
  				operations.add("load_storyboard");
  				operations.add("save_Comment");
  				operations.add("delete_storyboard");
  				operations.add("load_student_storyboard_list");
  				operations.add("open_storyboard");
  				operations.add("delete_storyboard_item");
		  	}
  		}else
  		 if (interaction.contains("System")){
  			operations.add("login");
  			operations.add("logout");
  			operations.add("load_site_settings");
  			operations.add("save_site_settings"); 
  		 } else
	  		 if (interaction.contains("Search")){
		  			operations.add("search");
		  			operations.add("load_search_list");
		  			operations.add("save_search");
		  			operations.add("load_search");
		  			operations.add("delete_search");
		  			operations.add("open_search");
		  	} else
		  		 if (interaction.contains("Storyboard")){
			  			operations.add("load_storyboard_list");
			  			operations.add("save_storyboard");
			  			operations.add("load_storyboard");
			  			operations.add("save_Comment");
			  			operations.add("delete_storyboard");
			  			operations.add("load_student_storyboard_list");
			  			operations.add("open_storyboard");
			  			operations.add("delete_storyboard_item");
			  	}else{
			  		operations.add("login");
			  		operations.add("logout");
			  		operations.add("load_site_settings");
			  		operations.add("save_site_settings");
			  		operations.add("search");
			  		operations.add("load_search_list");
			  		operations.add("save_search");
			  		operations.add("load_search");
			  		operations.add("delete_search");
			  		operations.add("open_search");
			  		operations.add("load_storyboard_list");
			  		operations.add("save_storyboard");
			  		operations.add("load_storyboard");
			  		operations.add("save_Comment");
			  		operations.add("delete_storyboard");
			  		operations.add("load_student_storyboard_list");
			  		operations.add("open_storyboard");
			  		operations.add("delete_storyboard_item");
				  }
			  	
  		return operations;
  	}
	
	 //Anousheh 2
	
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	//Generated by Object array
	  	public static class Operation{
	  		public String operationLabel;
	  		public String operationValue;
	  		
	  		public Operation(String operationLabel, String operationValue){
	  			this.operationLabel = operationLabel;
	  			this.operationValue = operationValue;
	  		}
	  		
	  		public String getOperationLabel(){
	  			return operationLabel;
	  		}
	  		
	  		public String getOperationValue(){
	  			return operationValue;
	  		}
	  	}
	  	
	  	public Operation[] operationList;
	  	
		public List<Track> getoperationList() {
			return list;
		}

		public void setoperationList(List<Track> operationlist) {
			this.operationlist = operationlist;
		}
		
	  	
	  	public Operation[] getOperationValue() {
	  		if (this.interaction==null){
	  			{
			  		
			  		operationList = new Operation[18];
			  		operationList[0] = new Operation("System - Login", "login");
			  		operationList[1] = new Operation("System - Logout", "logout");
			  		operationList[2] = new Operation("System - Loadsitesettings", "load_site_settings");
			  		operationList[3] = new Operation("System - savesitesettings", "save_site_settings");
			  		operationList[4] = new Operation("Search - Search", "search");
			  		operationList[5] = new Operation("Search - loadsearchlist", "load_search_list");
			  		operationList[6] = new Operation("Search - savesearch", "save_search");
			  		operationList[7] = new Operation("Search - loadsearch", "load_search");
			  		operationList[8] = new Operation("Search - deletesearch", "delete_search");
			  		operationList[9] = new Operation("Search - opensearch", "open_search");
			  		operationList[10] = new Operation("Storyboard - LoadStoryBoardList", "load_storyboard_list");
			  		operationList[11] = new Operation("Storyboard - SaveStoryboard", "save_storyboard");
			  		operationList[12] = new Operation("Storyboard - LoadStoryboard", "load_storyboard");
			  		operationList[13] = new Operation("Storyboard - SaveComment", "save_Comment");
			  		operationList[14] = new Operation("Storyboard - deleteStoryboard", "delete_storyboard");
			  		operationList[15] = new Operation("Storyboard - loadstudentStoryboardlist", "load_student_storyboard_list");
			  		operationList[16] = new Operation("Storyboard - OpenStoryboard", "open_storyboard");
			  		operationList[17] = new Operation("Storyboard - deleteStoryboarditem", "delete_storyboard_item");
			  	}
	  		}else
	  		 if (this.interaction.contains("System")){
	  			operationList = new Operation[4];
		  		operationList[0] = new Operation("System - Login", "login");
		  		operationList[1] = new Operation("System - Logout", "logout");
		  		operationList[2] = new Operation("System - Loadsitesettings", "load_site_settings");
		  		operationList[3] = new Operation("System - savesitesettings", "save_site_settings"); 
	  		 } else
		  		 if (this.interaction.contains("Search")){
			  			operationList = new Operation[6];
			  			operationList[0] = new Operation("Search - Search", "search");
				  		operationList[1] = new Operation("Search - loadsearchlist", "load_search_list");
				  		operationList[2] = new Operation("Search - savesearch", "save_search");
				  		operationList[3] = new Operation("Search - loadsearch", "load_search");
				  		operationList[4] = new Operation("Search - deletesearch", "delete_search");
				  		operationList[5] = new Operation("Search - opensearch", "open_search");
			  	} else
			  		 if (this.interaction.contains("Storyboard")){
				  			operationList = new Operation[8];
				  			operationList[0] = new Operation("Storyboard - LoadStoryBoardList", "load_storyboard_list");
					  		operationList[1] = new Operation("Storyboard - SaveStoryboard", "save_storyboard");
					  		operationList[2] = new Operation("Storyboard - LoadStoryboard", "load_storyboard");
					  		operationList[3] = new Operation("Storyboard - SaveComment", "save_Comment");
					  		operationList[4] = new Operation("Storyboard - deleteStoryboard", "delete_storyboard");
					  		operationList[5] = new Operation("Storyboard - loadstudentStoryboardlist", "load_student_storyboard_list");
					  		operationList[6] = new Operation("Storyboard - OpenStoryboard", "open_storyboard");
					  		operationList[7] = new Operation("Storyboard - deleteStoryboarditem", "delete_storyboard_item");
				  	}else{
				  		
					  		operationList = new Operation[18];
					  		operationList[0] = new Operation("System - Login", "login");
					  		operationList[1] = new Operation("System - Logout", "logout");
					  		operationList[2] = new Operation("System - Loadsitesettings", "load_site_settings");
					  		operationList[3] = new Operation("System - savesitesettings", "save_site_settings");
					  		operationList[4] = new Operation("Search - Search", "search");
					  		operationList[5] = new Operation("Search - loadsearchlist", "load_search_list");
					  		operationList[6] = new Operation("Search - savesearch", "save_search");
					  		operationList[7] = new Operation("Search - loadsearch", "load_search");
					  		operationList[8] = new Operation("Search - deletesearch", "delete_search");
					  		operationList[9] = new Operation("Search - opensearch", "open_search");
					  		operationList[10] = new Operation("Storyboard - LoadStoryBoardList", "load_storyboard_list");
					  		operationList[11] = new Operation("Storyboard - SaveStoryboard", "save_storyboard");
					  		operationList[12] = new Operation("Storyboard - LoadStoryboard", "load_storyboard");
					  		operationList[13] = new Operation("Storyboard - SaveComment", "save_Comment");
					  		operationList[14] = new Operation("Storyboard - deleteStoryboard", "delete_storyboard");
					  		operationList[15] = new Operation("Storyboard - loadstudentStoryboardlist", "load_student_storyboard_list");
					  		operationList[16] = new Operation("Storyboard - OpenStoryboard", "open_storyboard");
					  		operationList[17] = new Operation("Storyboard - deleteStoryboarditem", "delete_storyboard_item");
					  	}
				  	
	  		return operationList;
	  	}
}