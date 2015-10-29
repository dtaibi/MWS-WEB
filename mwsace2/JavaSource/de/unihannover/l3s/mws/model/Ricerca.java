package de.unihannover.l3s.mws.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class Ricerca {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	private String nome;
	private String searchDataPies;
	private String searchWeightedDataPies;
	private String searchDomainDataPies;
	private String searchterms;
	private String siteSelectedlists;
	private String siteAvailablelists;

	@Temporal(TemporalType.TIMESTAMP)
    private java.util.Date data;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utente", nullable = false)
	private Utente utente;
	
	@OneToMany(targetEntity=Risultati.class, mappedBy="ricerca", fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<Risultati> risultati;

	public Utente getUtente() {
		return utente;
	}
	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSearchDataPies() {
		return searchDataPies;
	}
	public void setSearchDataPies(String searchDataPies) {
		this.searchDataPies = searchDataPies;
	}
	public String getSearchWeightedDataPies() {
		return searchWeightedDataPies;
	}
	public void setSearchWeightedDataPies(String searchWeightedDataPies) {
		this.searchWeightedDataPies = searchWeightedDataPies;
	}
	public String getSearchDomainDataPies() {
		return searchDomainDataPies;
	}
	public void setSearchDomainDataPies(String searchDomainDataPies) {
		this.searchDomainDataPies = searchDomainDataPies;
	}
	public String getSearchterms() {
		return searchterms;
	}
	public void setSearchterms(String searchterms) {
		this.searchterms = searchterms;
	}
	public String getSiteSelectedlists() {
		return siteSelectedlists;
	}
	public void setSiteSelectedlists(String siteSelectedlists) {
		this.siteSelectedlists = siteSelectedlists;
	}
	public String getSiteAvailablelists() {
		return siteAvailablelists;
	}
	public void setSiteAvailablelists(String siteAvailablelists) {
		this.siteAvailablelists = siteAvailablelists;
	}
	public java.util.Date getData() {
		return data;
	}
	public void setData(java.util.Date data) {
		this.data = data;
	}
	public List<Risultati> getRisultati() {
		return risultati;
	}
	public void setRisultati(List<Risultati> risultati) {
		this.risultati = risultati;
	}

	
	

		
} 