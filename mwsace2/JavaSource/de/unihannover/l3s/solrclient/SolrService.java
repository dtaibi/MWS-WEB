package de.unihannover.l3s.solrclient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;


public class SolrService {

	private SolrServer solrServer;
	private List<PrometheusDataObject> queryResults; 
	private Date latestDate;
	private Date oldestDate;
	  
	 public SolrService()
	 {
		 solrServer = new HttpSolrServer("http://localhost:8983/solr/"); 
		 queryResults= new ArrayList<PrometheusDataObject>();
	 }

	 public Date getLastResultsCrawlingDate()
	 {return latestDate;}
	 
	 public Date getFirstResultsCrawlingDate()
	 {return oldestDate;}
	 
	 public List<PrometheusDataObject> search(String queryString, int noOfResults) {
		  SolrQuery query = new SolrQuery();
		  query.setQuery(queryString);
		  query.addFilterQuery("type : text");
		  query.addFilterQuery("source : ArchiveIt"); //Archive-It
		  query.addFilterQuery("groups : 943"); // Human Rights Collection
		  query.addFilterQuery("language : en");
		  query.setRows(noOfResults);
		 
		  QueryResponse queryResponse = null;
		  try {
		   queryResponse = solrServer.query(query);
		  } catch (SolrServerException e) {
		   e.printStackTrace();
		  }
		  PrometheusDataObject tempPromObj;
		  if(queryResponse!=null)
			{
			    for(SolrDocument doc : queryResponse.getResults())
			    {
			    tempPromObj= new PrometheusDataObject();
			    MementoClient mementoClient;
				 tempPromObj.setTitle((String)doc.getFieldValue("title"));
			     tempPromObj.setUrl((String)doc.getFieldValue("url"));
			     tempPromObj.setTimeStamp((Date)doc.getFieldValue("timestamp"));
			     tempPromObj.setDescription((String)doc.getFieldValue("description"));
			     mementoClient= new MementoClient();
			     //1068 Archive-it Human rights collection Id
			     tempPromObj.setVersions(mementoClient.getArchiveItVersions(1068, tempPromObj.getUrl()));
			     queryResults.add(tempPromObj);
			    }
			    if(queryResults.size()>0)
			    	calResultsTimeSpan();
			 //   System.out.println(queryResponse);
			}
		  return queryResults;
		 }

	 public void calResultsTimeSpan()
	 {
		 latestDate=queryResults.get(0).getVersions().get(0).getTimestamp();
		 oldestDate=queryResults.get(0).getVersions().get(0).getTimestamp();
		 for(int i=0; i<queryResults.size(); i++)
		 {
				 if(latestDate.before(queryResults.get(i).getLastCrawlingVersionDate()))
					 latestDate=queryResults.get(i).getLastCrawlingVersionDate();
				 if(oldestDate.after(queryResults.get(i).getFirstCrawlingVersionDate()))
					 oldestDate=queryResults.get(i).getFirstCrawlingVersionDate();
		 }
	 }

}