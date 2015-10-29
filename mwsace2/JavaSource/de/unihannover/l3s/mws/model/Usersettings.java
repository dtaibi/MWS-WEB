package de.unihannover.l3s.mws.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Usersettings {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	private String value;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utente", nullable = false)
	private Utente utente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "generalsettings", nullable = false)
	private Generalsettings generalsettings;
	
	
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public Generalsettings getGeneralsettings() {
		return generalsettings;
	}
 
	public void setGeneralsettings(Generalsettings generalsettings) {
		this.generalsettings = generalsettings;
	}
	
	public Utente getUtente() {
		return utente;
	}
 
	public void setUtente(Utente utente) {
		this.utente = utente;
	}
		
} 