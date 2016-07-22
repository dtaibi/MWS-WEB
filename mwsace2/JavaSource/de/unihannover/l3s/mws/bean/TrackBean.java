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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;

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
	private Utente user;
	
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

	private HashSet<String> distinctOperation(){
		HashSet<String> distinctOp=new HashSet<String>();
		for (Utente u:chartmap.keySet()){
			for (String s:chartmap.get(u).keySet()){
				distinctOp.add(s);
			}
		}
		return distinctOp;
	}
	
	public String getJchartcode(){
			String out="";
			
			out+="var randomScalingFactor = function(){ return Math.round(Math.random()*255)};";
			out+="var barChartData = {";
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
	
	
	@PostConstruct
    public void init() {
		
    }

// Anousheh
	List<Track> list;
	
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
	
	public void changeOperation(AjaxBehaviorEvent event){
		TrackDao trackdao=new TrackDao(); 
		List<Track> tracklist = trackdao.getTrackByOperationAndUser(this.operation,this.userselected);
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
		List<Track> tracklist = trackdao.getTrackByOperationAndUser(this.operation,this.userselected);
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
	  		
	  		operationList = new Operation[6];
	  		operationList[0] = new Operation("Operation - Login", "login");
	  		operationList[1] = new Operation("Operation - Search", "search");
	  		operationList[2] = new Operation("Operation - SaveSearch", "save_search");
	  		operationList[3] = new Operation("Operation - LoadSearchList", "load_search_list");
	  		operationList[4] = new Operation("Operation - SaveSiteSetting", "save_site_settings");
	  		operationList[5] = new Operation("Operation - LoadStoryBoardList", "load_storyboard_list");
	  		
	  		return operationList;
	  	
	  	}
}