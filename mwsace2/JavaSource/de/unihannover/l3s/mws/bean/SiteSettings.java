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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import de.unihannover.l3s.mws.dao.SiteSetDao;
import de.unihannover.l3s.mws.dao.TrackDao;
import de.unihannover.l3s.mws.dao.UtenteDao;
import de.unihannover.l3s.mws.model.SiteSet;
import de.unihannover.l3s.mws.model.SiteSetItem;
import de.unihannover.l3s.mws.model.Track;


@ManagedBean(name="siteSettings")
@SessionScoped
public class SiteSettings {


	@ManagedProperty(value="#{user}")
	private User user;


	String treeString;
	String treedata;
	String nuovitreedata;
	String editedtreedata;
	String deletedtreedata;
	
	
	public String getTreeString() {
		return treeString;
	}
	
	public void setTreeString(String treeString) {
		this.treeString = treeString;
	}
	
	public String getEditedtreedata() {
		return editedtreedata;
	}
	
	public void setEditedtreedata(String editedtreedata) {
		this.editedtreedata = editedtreedata;
	}
	
	public String getDeletedtreedata() {
		return deletedtreedata;
	}
	
	public void setDeletedtreedata(String deletedtreedata) {
		this.deletedtreedata = deletedtreedata;
	}
	
	public String getTreedata() {
		return treedata;
	}
	
	public void setTreedata(String treedata) {
		this.treedata = treedata;
	}
	
	public String getNuovitreedata() {
		return nuovitreedata;
	}
	
	public void setNuovitreedata(String nuovitreedata) {
		this.nuovitreedata = nuovitreedata;
	}
	
	@PostConstruct
    public void init() {

    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String savesettings(){
		SiteSetDao ssd=new SiteSetDao();
		if (!this.deletedtreedata.trim().isEmpty()){
			ssd.deleteSiteSet(this.deletedtreedata);
			this.deletedtreedata="";
		}
		if (!this.editedtreedata.trim().isEmpty()){
			System.out.println(this.editedtreedata);
			for (String s : editedtreedata.split(",")){
				String[] val=s.split(":");
				if (val[0].startsWith("u")){
					SiteSet siteset=ssd.getSiteSetById(Long.parseLong(val[0].replace("u", "")));
					System.out.println("aaa:"+siteset);
					siteset.setName(val[1]);
					ssd.editSiteSet(siteset);
				}
				if (val[0].startsWith("t")){
					SiteSetItem sitesetitem=ssd.getSiteSetItemById(Long.parseLong(val[0].replace("t", "")));
					System.out.println("bbb:"+sitesetitem);
					sitesetitem.setUrl(val[1]);
					ssd.editSiteSetItem(sitesetitem);
				}
				this.editedtreedata="";
			}
		}
		// System.out.println("Nuovi tree data:"+nuovitreedata);
		String[] newnodes = nuovitreedata.split("\\|");
		Map<String,SiteSet> new_siteset = new HashMap<String,SiteSet>(); // new or modified sitesets
		Map<String,SiteSetItem> new_sitesetitem = new HashMap<String,SiteSetItem>();
		for (String s : newnodes){
			// System.out.println("newnodes:"+s);
			if (s.trim().compareTo("")!=0){
				String[] s_data = s.split(":");
				/*for (String ooo : s_data)
					System.out.print(ooo+"___");
				System.out.println(); */
				
				if (s_data[0].compareTo("root")==0){
					SiteSet ss=new SiteSet();
					ss.setName(s_data[2]);
					ss.setUtente(this.user.getUtente());
					new_siteset.put(s_data[1],ss);
				}else{
					SiteSetItem ssitem=new SiteSetItem();
					ssitem.setUrl(s_data[2]);
					SiteSet parent=null;
					if (!s_data[0].startsWith("j") && !new_siteset.containsKey(s_data[0])){
						parent=ssd.getSiteSetById(Long.parseLong(s_data[0].replace(s_data[0].subSequence(0, 1), "")));
						new_siteset.put(s_data[0], parent);
					}
					
					parent=new_siteset.get(s_data[0]);
					
					ssitem.setSiteSet(parent);
					
					if (parent.getSiteSetItem() == null)
						parent.setSiteSetItem(new ArrayList<SiteSetItem>());
						
					parent.getSiteSetItem().add(ssitem);
					/* System.out.println("1-"+s_data[0]);
					System.out.println("2-"+new_siteset.get(s_data[0]));
					System.out.println("3-"+new_siteset.get(s_data[0]).getSiteSetItem().size());
					*/
					new_sitesetitem.put(s_data[1], ssitem);
				}
			}
		}
		
		for (String sskey:new_siteset.keySet()){
			ssd.addSiteSet(new_siteset.get(sskey));
		}
		
		
		String[] selectednodes = treedata.split(",");
		List<Long> ids=new ArrayList<Long>();
		for (String s : selectednodes){
			if (s.startsWith("i"))
				ids.add(Long.parseLong(s.replace("i", "")));
			if (s.startsWith("t"))
				ids.add(Long.parseLong(s.replace("t", "")));
			if (s.startsWith("j")) {
				if (new_sitesetitem.get(s)!=null)
					ids.add(new_sitesetitem.get(s).getId());
			}
		}
		if (ids.size()!=0)
			this.getUser().getUtente().setSitesetitem(ssd.getAllSiteSetItem(ids));
		else
			this.getUser().getUtente().setSitesetitem(null);
		UtenteDao udao=new UtenteDao();
		udao.updateUtente(this.getUser().getUtente());
		
		TrackDao td=new TrackDao();
		Track track=new Track();
		Calendar c = new GregorianCalendar();
		track.setDate(c.getTime());
		track.setOperation("save_site_settings");
		track.setParam1(nuovitreedata);
		track.setUtente(this.user.getUtente());
		td.addTrack(track);
		
		return loadList();
	}
	
	public String loadList(){
		SiteSetDao ssd=new SiteSetDao(); 
		String sel="";
		List<SiteSet> sitesets = ssd.getAllSiteSet();
		treeString="";
		List<Long> seltreeid=new ArrayList<Long>();
		if (this.getUser().getUtente().getSitesetitem()!=null)
			for (SiteSetItem l : this.getUser().getUtente().getSitesetitem())
				seltreeid.add(l.getId());
		
		treeString+="<li id=\"root\" > Site List";
		treeString+="<ul>";
		for (SiteSet ss: sitesets){
			if (ss.getUtente()!=null)
				treeString+="<li id=\"u"+ss.getId()+"\" >"+ss.getName();
			else
				treeString+="<li id=\"s"+ss.getId()+"\" >"+ss.getName();
			treeString+="<ul>";
			for (SiteSetItem ssi:ss.getSiteSetItem()){
				sel=""; 
				if (seltreeid.contains(ssi.getId()))
					sel=" data-jstree='{\"selected\":true}' ";
				
				if (ss.getUtente()!=null)
					treeString+="<li id=\"t"+ssi.getId()+"\" "+sel+" >"+ssi.getUrl()+"</li>";
				else
					treeString+="<li id=\"i"+ssi.getId()+"\" "+sel+" >"+ssi.getUrl()+"</li>";
				
			}
			treeString+="</ul>";
			treeString+="</li>";
		}
		treeString+="</ul>";
		treeString+="</li>";
		
		TrackDao td=new TrackDao();
		Track track=new Track();
		Calendar c = new GregorianCalendar();
		track.setDate(c.getTime());
		track.setOperation("load_site_settings");
		track.setUtente(this.user.getUtente());
		td.addTrack(track);
		
		return "siteSettings";
	}
	
}