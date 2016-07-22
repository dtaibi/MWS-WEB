package de.unihannover.l3s.solrclient;

import java.util.Date;
import java.util.List;


public class PrometheusDataObject {

	private Date timeStamp;
	
	private String url;
	private String title;
	private List<ArchiveUrl> versions;
	private String description;
	private Date latestDate;
	private Date oldestDate;
	
	public PrometheusDataObject()
	{
		//versions= new ArrayList<ArchiveUrl>();
	}
	
	public void setDescription(String description)
	{this.description=description;}
	
	public String getDescription()
	{return description;}
	
	public void setTitle(String title)
	{this.title=title;}
	
	public String getTitle()
	{return title;}
	
	public void setUrl(String url)
	{this.url=url;}
	
	public String getUrl()
	{return url;}
	
	public void setTimeStamp(Date timeStamp)
	{this.timeStamp=timeStamp;}
	
	public Date getTimeStamp()
	{return timeStamp;}
	
	public void setVersions(List<ArchiveUrl> versions)
	{this.versions=versions; calResultsTimeSpan();}
	
	public List<ArchiveUrl> getVersions()
	{return versions;}
	
	 private void calResultsTimeSpan()
	 {
		 latestDate=versions.get(0).getTimestamp();
		 oldestDate=versions.get(0).getTimestamp();
		  for(ArchiveUrl obj: versions)
			 {
				 if(latestDate.before(obj.getTimestamp()))
					 latestDate=obj.getTimestamp();
				 if(oldestDate.after(obj.getTimestamp()))
					 oldestDate=obj.getTimestamp();
			 }
	 }
	
	 public Date getLastCrawlingVersionDate()
	 {return latestDate;}
	 
	 public Date getFirstCrawlingVersionDate()
	 {return oldestDate;} 
}
