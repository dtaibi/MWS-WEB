package de.unihannover.l3s.mws.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Entity
public class UrlContent {
	
@Id 
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
public Long getId() { return id; }
public void setId(Long id) { this.id = id; }

private String url;
private String content;

public void SetUrl(String url) {
	this.url=url;
	}
public String GetUrl() {
 return url;
}


public void SetContent() throws IOException {
	
	URL weblink;
	content="";
	try{
	weblink = new URL(url);
	URLConnection conn = weblink.openConnection();

	// open the stream and put it into BufferedReader
	BufferedReader br = new BufferedReader(
                       new InputStreamReader(conn.getInputStream()));
//
	String inputLine;
	while ((inputLine = br.readLine()) != null) {
		content+=inputLine;
	}
	}
	catch (MalformedURLException e) {
		e.printStackTrace();
	}
}

public String GetContent() {
 return content;
}

}


