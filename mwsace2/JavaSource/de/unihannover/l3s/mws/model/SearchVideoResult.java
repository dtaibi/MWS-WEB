package de.unihannover.l3s.mws.model;

import net.billylieurance.azuresearch.BingThumbnail;

public class SearchVideoResult extends SearchResult {
	private String click_url;
	private int width;
	private int height;
	private String runtime;
	private BingThumbnail thumbnail;
	public String getClick_url() {
		return click_url;
	}
	public void setClick_url(String click_url) {
		this.click_url = click_url;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public BingThumbnail getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(BingThumbnail thumbnail) {
		this.thumbnail = thumbnail;
	}
	
}
