package de.unihannover.l3s.mws.model.timeline;

import java.util.ArrayList;

public class Timeline {
	String headline;
	String type;
	String startDate;
	String text;
	Asset asset;
	ArrayList<Date> date=new ArrayList<Date>();
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Asset getAsset() {
		return asset;
	}
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	public ArrayList<Date> getDate() {
		return date;
	}
	public void setDate(ArrayList<Date> date) {
		this.date = date;
	}
	
	
	
}
