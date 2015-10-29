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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.OrderBy;


@Entity
public class Storyboard {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	private String nome;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "utente", nullable = false)
	private Utente utente;
	
	@OneToMany(targetEntity=StoryboardItem.class, mappedBy="storyboard", fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	@OrderBy("pos")
	private List<StoryboardItem> storyboardItem;

	@Temporal(TemporalType.TIMESTAMP)
    private java.util.Date data;
	
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
	public List<StoryboardItem> getStoryboardItem() {
		return storyboardItem;
	}
	public void setStoryboardItem(List<StoryboardItem> storyboardItem) {
		this.storyboardItem = storyboardItem;
	}
	public java.util.Date getData() {
		return data;
	}
	public void setData(java.util.Date data) {
		this.data = data;
	}
} 