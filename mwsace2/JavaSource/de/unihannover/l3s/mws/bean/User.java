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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import de.unihannover.l3s.mws.dao.TrackDao;
import de.unihannover.l3s.mws.dao.UtenteDao;
import de.unihannover.l3s.mws.model.Track;
import de.unihannover.l3s.mws.model.Utente;

/**
 * Created by JBoss Tools
 */
@ManagedBean(name="user")
@SessionScoped
public class User {
	private String name;
	private String password;
	private Utente utente;

	public User() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public String sayHello() {
		/* ODatabaseDocumentTx db = new ODatabaseDocumentTx("remote:localhost/mwsace");
		  db.open("root", "root");
		  try {
			  List<ODocument> result = db.query(
					    new OSQLSynchQuery<ODocument>("select * from User where username = \""+this.name+"\" and password = \""+this.password+"\""));
			  if (result.size()>0)
				  return "basicSearch";
			  else
				  return "inputname";
		  } finally {
		    db.close();Anthony Baldry <anthony.baldry@gmail.com>
		  } */
		UtenteDao ud=new UtenteDao();
		TrackDao td=new TrackDao();
		List<Utente> ut=ud.getUtente(this.name, this.password);
		System.out.println(ut.size());
		if (ut.size()>0) {
			this.utente = ut.get(0);
			
			Track track=new Track();
			Calendar c = new GregorianCalendar();
			track.setDate(c.getTime());
			track.setOperation("login");
			track.setParam1(this.name);
			track.setUtente(this.utente);
			td.addTrack(track);
			if (this.utente.getRole().compareTo("wsstudent")==0)
				return "languageSearchStudent";
			if (this.utente.getRole().compareTo("nosarweb")==0)
				return "searchListStudent";
			if (this.utente.getRole().compareTo("nosarweb2")==0)
				return "searchListStudent2";
			if (this.utente.getRole().compareTo("hackathon")==0)
				return "hackathon";
			if (this.utente.getRole().compareTo("external")==0)
				return "languageSearch";
			return "basicSearch";
		} else {
			this.utente = null;
			return "/pages/login.jsf?faces-redirect=true";
		}
	}
	
	public String logout() {
		TrackDao td=new TrackDao();
		Track track=new Track();
		Calendar c = new GregorianCalendar();
		track.setDate(c.getTime());
		track.setOperation("logout");
		track.setParam1(this.name);
		track.setUtente(this.utente);
		td.addTrack(track);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/pages/login.jsf?faces-redirect=true";
    }
}