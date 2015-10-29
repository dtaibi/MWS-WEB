package de.unihannover.l3s.mws.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class SiteSet {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utente", nullable = false)
	private Utente utente;
	
	public Utente getUtente() {
		return utente;
	}
	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	
	@OneToMany(targetEntity=SiteSetItem.class, mappedBy="siteset", fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<SiteSetItem> sitesetitem;
	
	public List<SiteSetItem> getSiteSetItem() {
		return sitesetitem;
	}
	public void setSiteSetItem(List<SiteSetItem> sitesetitem) {
		this.sitesetitem = sitesetitem;
	}
} 