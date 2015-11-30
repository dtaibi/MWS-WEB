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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import de.unihannover.l3s.mws.dao.TrackDao;
import de.unihannover.l3s.mws.model.Track;
import de.unihannover.l3s.mws.model.Utente;


@ManagedBean(name="tracks")
@SessionScoped
public class TrackBean {

	@ManagedProperty(value="#{user}")
	private User loggeduser;
	
	private Utente user;
	
	@PostConstruct
    public void init() {

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
	
	public String loadList(){
		TrackDao trackdao=new TrackDao(); 
		List<Track> tracklist = trackdao.getAllSiteSetByUser(loggeduser.getUtente());

		
		return "trackchart";
	}
	
}