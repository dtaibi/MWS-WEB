package de.unihannover.l3s.mws.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Track {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	private String operation;
	private String param1;
	private String param2;
	private String param3;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utente", nullable = false)
	private Utente utente;
	
	@Temporal(TemporalType.TIMESTAMP)
    private java.util.Date date;
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	public String getParam3() {
		return param3;
	}
	public void setParam3(String param3) {
		this.param3 = param3;
	}
	public java.util.Date getDate() {
		return date;
	}
	public void setDate(java.util.Date date) {
		this.date = date;
	}
		
	
	public Utente getUtente() {
		return utente;
	}
 
	public void setUtente(Utente utente) {
		this.utente = utente;
	}
		
} 