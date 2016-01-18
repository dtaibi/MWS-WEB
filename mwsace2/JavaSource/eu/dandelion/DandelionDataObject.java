package eu.dandelion;


public class DandelionDataObject {

	long wordStartPosition;
	public void setWordStartPosition(long wordStartPosition){this.wordStartPosition=wordStartPosition;};
	public long getWordStartPosition(){return wordStartPosition;};
	
	long wordEndPosition;
	public void setWordEndPosition(long wordEndPosition){this.wordEndPosition=wordEndPosition;};
	public long getWordEndPosition(){return wordEndPosition;};
	
	String spot;
	public void setSpot(String spot){this.spot=spot;};
	public String getSpot(){return spot;};
	
	double confidence;
	public void setConfidence(double confidence){this.confidence=confidence;};
	public double getConfidence(){return confidence;};
	
	long id;
	public void setId(long id){this.id=id;};
	public long getId(){return id;};
	
	String title;
	public void setTitle(String title){this.title=title;};
	public String getTitle(){return title;};
	
	String uri;
	public void setUri(String uri){this.uri=uri;};
	public String getUri(){return uri;};
	
	String label;
	public void setLabel(String label){this.label=label;};
	public String getLabel(){return label;};
	
}
