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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import de.unihannover.l3s.mws.dao.RicercaDao;
import de.unihannover.l3s.mws.dao.TrackDao;
import de.unihannover.l3s.mws.model.Ricerca;
import de.unihannover.l3s.mws.model.Track;


@ManagedBean(name="searchWork")
@SessionScoped
public class SearchWork {
	private List<Ricerca> list;

	@ManagedProperty(value="#{user}")
	private User user;


	public List<Ricerca> getList() {
		return list;
	}

	public void setList(List<Ricerca> list) {
		this.list = list;
	}

	
	@PostConstruct
    public void init() {
		// list = new ArrayList<Ricerca>();
		// loadList();
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String loadList(){
		RicercaDao rdao=new RicercaDao();
		this.list=rdao.getRicercaByUser(this.user.getUtente());
		System.out.println("SIZE:"+list.size());
		TrackDao td=new TrackDao();
		Track track=new Track();
		Calendar c = new GregorianCalendar();
		track.setDate(c.getTime());
		track.setOperation("load_search_list");
		track.setUtente(this.user.getUtente());
		td.addTrack(track);
		return "searchWork";
	}
	
	public void delete(Ricerca ricerca){
		RicercaDao rdao=new RicercaDao();
		rdao.deleteRicerca(ricerca);
		TrackDao td=new TrackDao();
		Track track=new Track();
		Calendar c = new GregorianCalendar();
		track.setDate(c.getTime());
		track.setOperation("delete_search");
		track.setParam1(ricerca.getNome());
		track.setUtente(this.user.getUtente());
		td.addTrack(track);
		loadList();
	}
	
}