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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import de.unihannover.l3s.mws.dao.BingcacheDao;
import de.unihannover.l3s.mws.model.SearchResult;

/**
 * Created by JBoss Tools
 */
@ManagedBean(name="searchlist")
@SessionScoped
public class SearchList {
	
	private ArrayList<SearchResult> searchResultEn=null;
	private ArrayList<SearchResult> searchResultDe=null;
	private ArrayList<SearchResult> searchResultEn2=null;
	private ArrayList<SearchResult> searchResultDe2=null;
	public ArrayList<SearchResult> getSearchResultEn() {
		BingcacheDao bcd=new BingcacheDao();
		searchResultEn=bcd.getCache("\"nuclear\":10", "en-US");
		// System.out.println(searchResultEn.size());
		return searchResultEn;
	}
	public void setSearchResultEn(ArrayList<SearchResult> searchResultEn) {
		this.searchResultEn = searchResultEn;
	}
	public ArrayList<SearchResult> getSearchResultDe() {
		BingcacheDao bcd=new BingcacheDao();
		searchResultDe=bcd.getCache("\"nuklear\":10", "de-DE");
		return searchResultDe;
	}
	public void setSearchResultDe(ArrayList<SearchResult> searchResultDe) {
		this.searchResultDe = searchResultDe;
	}
	public ArrayList<SearchResult> getSearchResultEn2() {
		BingcacheDao bcd=new BingcacheDao();
		searchResultEn2=bcd.getCache("\"rights\":10", "en-US");
		return searchResultEn2;
	}
	public void setSearchResultEn2(ArrayList<SearchResult> searchResultEn2) {
		this.searchResultEn2 = searchResultEn2;
	}
	public ArrayList<SearchResult> getSearchResultDe2() {
		BingcacheDao bcd=new BingcacheDao();
		searchResultDe2=bcd.getCache("\"rechte\":10", "de-DE");
		return searchResultDe2;
	}
	public void setSearchResultDe2(ArrayList<SearchResult> searchResultDe2) {
		this.searchResultDe2 = searchResultDe2;
	}
	
	
	
}