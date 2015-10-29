package de.unihannover.l3s.mws.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Risultati {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	private String type;
	private String risultato;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ricerca", nullable = false)
	private Ricerca ricerca;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRisultato() {
		return risultato;
	}
	public void setRisultato(String risultato) {
		this.risultato = risultato;
	}
	public Ricerca getRicerca() {
		return ricerca;
	}
	public void setRicerca(Ricerca ricerca) {
		this.ricerca = ricerca;
	}
	
	

		
} 