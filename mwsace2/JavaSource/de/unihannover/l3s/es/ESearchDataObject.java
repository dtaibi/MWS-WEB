package de.unihannover.l3s.es;

import java.util.ArrayList;

import org.json.JSONObject;


public class ESearchDataObject {

	String tstamp;
	public void setTstamp(String tstamp){this.tstamp=tstamp;};
	public String getTstamp(){return tstamp;};

	String title;
	public void setTitle(String title){this.title=title;};
	public String getTitle(){return title;};
	
	String scene;
	public void setScene(String scene){this.scene=scene;};
	public String getScene(){return scene;};

	String content;
	public void setContent(String content){this.content=content;};
	public String getContent(){return content;};

	String url;
	public void setUrl(String url){this.url=url;};
	public String getUrl(){return url;};
	
	ArrayList<String> types;
	public void setTypes(ArrayList<String> tyoes){this.types=types;};
	public ArrayList<String> getTypes(){return types;};
	
	ArrayList<String> entities=null;
	public void setEntities(ArrayList<String> entities){this.entities=entities;};
	public ArrayList<String> getEntities(){return entities;}
	public void addEntities(String value) {
		if (this.getEntities()==null)
			this.entities=new ArrayList<String>();
		this.entities.add(value);
	};
	
}
