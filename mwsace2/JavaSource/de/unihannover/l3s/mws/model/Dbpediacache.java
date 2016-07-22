package de.unihannover.l3s.mws.model;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Dbpediacache {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	private String dbpedia;
	private String dbpedialoc;
	
	@Temporal(TemporalType.TIMESTAMP)
    private java.util.Date date;
	
	
	public String getDbpedia() {
		return dbpedia;
	}
	public void setDbpedia(String dbpedia) {
		this.dbpedia = dbpedia;
	}
	public String getDbpedialoc() {
		return dbpedialoc;
	}
	public void setDbpedialoc(String dbpedialoc) {
		this.dbpedialoc = dbpedialoc;
	}

	public java.util.Date getDate() {
		return date;
	}
	public void setDate(java.util.Date date) {
		this.date = date;
	}

		
} 