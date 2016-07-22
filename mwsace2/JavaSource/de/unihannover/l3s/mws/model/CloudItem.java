package de.unihannover.l3s.mws.model;

public class CloudItem {
	String text;
	Double value;
	
	public CloudItem(String text, Double value) {
		super();
		this.text = text;
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	
}
