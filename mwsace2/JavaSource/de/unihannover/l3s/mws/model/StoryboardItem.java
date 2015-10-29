package de.unihannover.l3s.mws.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class StoryboardItem implements Cloneable {
	private String title;
	private String type;
	private String content;
	private String comment;
	// private Risultati r;
	private String webcontent;
	// @Transient
	private Long pos;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storyboard", nullable = false)
	private Storyboard storyboard;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPos() {
		return pos;
	}
	public void setPos(Long pos) {
		this.pos = pos;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWebcontent() {
		return webcontent;
	}
	public void setWebcontent(String webcontent) {
		this.webcontent = webcontent;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Storyboard getStoryboard() {
		return storyboard;
	}
	public void setStoryboard(Storyboard storyboard) {
		this.storyboard = storyboard;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getComment() {
		return comment;
	}
	
	public StoryboardItem clone() {
        try {
        	super.clone();
        	StoryboardItem i=new StoryboardItem(); 
        	i.setComment(comment);
        	i.setContent(content);
        	i.setType(type);
        	i.setPos(pos);
        	i.setId(null);
        	i.setStoryboard(null);
        	i.setTitle(title);
        	i.setWebcontent(webcontent);
            return i; // (StoryboardItem) super.clone();
        } catch (CloneNotSupportedException e) {        
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
	
	/* public Risultati getR() {
		return r;
	}
	public void setR(Risultati r) {
		this.r = r;
	} */
	
	
}
