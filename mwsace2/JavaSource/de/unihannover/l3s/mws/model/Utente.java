package de.unihannover.l3s.mws.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
@Entity
public class Utente {
	@Id private Long id;
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	private String username;
	private String password;
	private String role;
	
	
	
	@OneToMany(targetEntity=Track.class, mappedBy="utente", fetch=FetchType.LAZY)
	private List<Track> tracks;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "Usersettings", joinColumns = { 
			@JoinColumn(name = "utente", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "generalsettings", 
					nullable = false, updatable = false) })
	private Set<Generalsettings> generalsettings=new HashSet<Generalsettings>(0);
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "Usersiteset", joinColumns = { 
			@JoinColumn(name = "utente", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "sitesetitem", 
					nullable = false, updatable = false) })
	private List<SiteSetItem> sitesetitem=new ArrayList<SiteSetItem>(0);
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(targetEntity=Ricerca.class, mappedBy="utente")
	private List<Ricerca> ricerca;
	
	
	private Integer cotextrange;
	
	public Integer getCotextrange() {
		return this.cotextrange;
	}
	public void setCotextrange(Integer cotextrange) {
		this.cotextrange = cotextrange;
	}
	public List<Track> getTracks() {
		return tracks;
	}
	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}
	public List<Ricerca> getRicerca() {
		return ricerca;
	}
	public void setRicerca(List<Ricerca> ricerca) {
		this.ricerca = ricerca;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public Set<Generalsettings> getGeneralsettings() {
		return this.generalsettings;
	}
 
	public void setGeneralsettings(Set<Generalsettings> generalsettings) {
		this.generalsettings = generalsettings;
	}
	
	public List<SiteSetItem> getSitesetitem() {
		return this.sitesetitem;
	}

	public void setSitesetitem(List<SiteSetItem> sitesetitem) {
		this.sitesetitem = sitesetitem;
	}
	
	public boolean hasAllLang(){
		for (Generalsettings gs : this.getGeneralsettings()){
			if ((gs.getType().compareTo("lang")==0)&&(gs.getValue().compareTo("All")==0))
				return true;
		}
		return false;
	}
	
	public boolean hasAllLoc(){
		for (Generalsettings gs : this.getGeneralsettings()){
			if ((gs.getType().compareTo("loc")==0)&&(gs.getValue().compareTo("All")==0))
				return true;
		}
		return false;
	}
} 