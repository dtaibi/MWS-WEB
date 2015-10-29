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
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import de.unihannover.l3s.mws.dao.GeneralsettingsDao;
import de.unihannover.l3s.mws.dao.UtenteDao;
import de.unihannover.l3s.mws.model.Generalsettings;


@ManagedBean(name="generalSettings")
@SessionScoped
public class GeneralSettings {
	private List<Generalsettings> weblist;
	private List<String> weblistsel;
	private List<Generalsettings> vidlist;
	private List<String> vidlistsel;
	private List<Generalsettings> imglist;
	private List<String> imglistsel;
	private List<Generalsettings> langlist;
	private String langlistsel;
	private List<Generalsettings> loclist;
	private String loclistsel;
	
	
	
	@ManagedProperty(value="#{user}")
	private User user;


	public List<Generalsettings> getWeblist() {
		return weblist;
	}
	
	public void setWeblist(List<Generalsettings> weblist) {
		this.weblist = weblist;
	}

	public List<String> getWeblistsel() {
		return weblistsel;
	}
	
	public void setWeblistsel(List<String> weblistsel) {
		this.weblistsel = weblistsel;
	}
	
	public List<Generalsettings> getVidlist() {
		return vidlist;
	}
	
	public void setVidlist(List<Generalsettings> vidlist) {
		this.vidlist = vidlist;
	}
	
	public List<Generalsettings> getImglist() {
		return imglist;
	}
	
	public void setImglist(List<Generalsettings> imglist) {
		this.imglist = imglist;
	}
	
	public List<Generalsettings> getLanglist() {
		return langlist;
	}
	
	public void setLanglist(List<Generalsettings> langlist) {
		this.langlist = langlist;
	}

	public List<Generalsettings> getLoclist() {
		return loclist;
	}
	
	public void setLoclist(List<Generalsettings> loclist) {
		this.loclist = loclist;
	}
	
	public List<String> getImglistsel() {
		return imglistsel;
	}
	
	public void setImglistsel(List<String> imglistsel) {
		this.imglistsel = imglistsel;
	}
	
	public List<String> getvidlistsel() {
		return vidlistsel;
	}
	
	public void setVidlistsel(List<String> vidlistsel) {
		this.vidlistsel = vidlistsel;
	}
	
	public String getLanglistsel() {
		return langlistsel;
	}
	
	public void setLanglistsel(String langlistsel) {
		this.langlistsel = langlistsel;
	}
	
	public String getLoclistsel() {
		return loclistsel;
	}
	
	public void setLoclistsel(String loclistsel) {
		this.loclistsel = loclistsel;
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
	
	public void savesettings(){
		
		this.getUser().getUtente().getGeneralsettings().clear();
		
		/* List<Generalsettings> toremove=new ArrayList<Generalsettings>();
		for (Generalsettings gs : this.getUser().getUtente().getGeneralsettings()){
			System.out.println("User GS:"+gs.getType()+"::"+gs.getValue());
			if (( (gs.getType().compareTo("lang")==0) || (gs.getType().compareTo("loc")==0) )){
				toremove.add(gs);
				System.out.println("To remove:"+gs.getType()+"::"+gs.getValue());
			}
		}
		for (Generalsettings gs : toremove){
			this.getUser().getUtente().getGeneralsettings().remove(gs);
			System.out.println("Remove : "+gs.getValue());
		} */
		
		for (Generalsettings gs : weblist){
			boolean trovato=false;
			for (String id : weblistsel){
				if (gs.getId() == Integer.parseInt(id)) {
					trovato = true; break;
				}
			}
			if (trovato==false)
				this.getUser().getUtente().getGeneralsettings().add(gs);
		}
		for (Generalsettings gs : imglist){
			boolean trovato=false;
			for (String id : imglistsel){
				if (gs.getId() == Integer.parseInt(id)) {
					trovato = true; break;
				}
			}
			if (trovato==false)
				this.getUser().getUtente().getGeneralsettings().add(gs);
		}
		for (Generalsettings gs : vidlist){
			boolean trovato=false;
			for (String id : vidlistsel){
				if (gs.getId() == Integer.parseInt(id)) {
					trovato = true; break;
				}
			}
			if (trovato==false)
				this.getUser().getUtente().getGeneralsettings().add(gs);
		} 
		
		for (Generalsettings gs : langlist){
				if (gs.getId() == Integer.parseInt(langlistsel)) { 
					this.getUser().getUtente().getGeneralsettings().add(gs);
				}					
		}

		for (Generalsettings gs : loclist){
				if (gs.getId() == Integer.parseInt(loclistsel)){ 
					this.getUser().getUtente().getGeneralsettings().add(gs);
				}
		}
		
		// this.getUser().getUtente().getGeneralsettings().add(cotextGs);
				
		/* for (Generalsettings gs : langlist){
			boolean trovato=false;
			for (String id : langlistsel){
				if (gs.getId() == Integer.parseInt(id)) {
					trovato = true; break;
				}
			}
			if (trovato==false)
				this.getUser().getUtente().getGeneralsettings().add(gs);
		} */
		UtenteDao udao=new UtenteDao();
		udao.updateUtente(this.getUser().getUtente());
	}
	
	private boolean hasGS(Generalsettings g){
		for (Generalsettings gs : this.user.getUtente().getGeneralsettings()){
			if (gs.getId() == g.getId())
				return true;
		}
		return false;
	}
	
	public String loadList(){
		List<Generalsettings> listags;
		GeneralsettingsDao gsdao=new GeneralsettingsDao();
		listags=gsdao.getAllGeneralsettings();
		
		
		weblist = new ArrayList<Generalsettings>();
		imglist = new ArrayList<Generalsettings>();
		vidlist = new ArrayList<Generalsettings>();
		langlist = new ArrayList<Generalsettings>();
		loclist = new ArrayList<Generalsettings>();
		
		weblistsel = new ArrayList<String>();
		imglistsel = new ArrayList<String>();
		vidlistsel = new ArrayList<String>();
		langlistsel = new String();
		loclistsel = new String();
		
		for (Generalsettings gs : listags){

			if (gs.getType().compareTo("web")==0){
				weblist.add(gs);
				if (!hasGS(gs))	weblistsel.add(""+gs.getId());
			}else if (gs.getType().compareTo("img")==0){
				imglist.add(gs);
				if (!hasGS(gs))	imglistsel.add(""+gs.getId()); 
			}else if (gs.getType().compareTo("video")==0){
				vidlist.add(gs);
				if (!hasGS(gs))	vidlistsel.add(""+gs.getId()); 
			} else if (gs.getType().compareTo("lang")==0){
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

		
		
		return "generalSettings";
	}
	
}