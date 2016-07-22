package de.unihannover.l3s.solrclient;

import java.util.List;

public class TestSolrService {

	public static void main(String[] args) {

		SolrService solrSerObj = new SolrService();
		List<PrometheusDataObject> queryResults; 
		String queryString="development rights";
		int ReturnResults=2;
		
		queryResults=solrSerObj.search(queryString, ReturnResults);
		
		System.out.println("Query: "+queryString+"\nResultsConsidered: "+ReturnResults);
		 System.out.println("----------------------------------------------------------");
		 System.out.println("Query Results: "+queryResults.size());
		 
		 for(int i=0; i<queryResults.size(); i++)
		 {
		 System.out.println("\nResult: "+(i+1));
		 System.out.println("\tTitle: "+queryResults.get(i).getTitle());
		 System.out.println("\tURL: "+queryResults.get(i).getUrl());
		 System.out.println("\tDescription: "+queryResults.get(i).getDescription());
		 System.out.println("\tTimestamp: "+queryResults.get(i).getTimeStamp());
		 System.out.println("\tVersions#: "+queryResults.get(i).getVersions().size());
		 System.out.println("\tVersionsTimeSpan: "+queryResults.get(i).getFirstCrawlingVersionDate()+" to "+queryResults.get(i).getLastCrawlingVersionDate());
		 System.out.println("\tVersions: ");
		 for(ArchiveUrl obj: queryResults.get(i).getVersions())
			 {
				 System.out.println("\t\tURL: "+obj.getArchiveUrl() +"\n\t\tTimestamp: "+obj.getTimestamp());
			 }
		 }
		 
		 System.out.println("\nAllResultsTimeSpan: "+solrSerObj.getFirstResultsCrawlingDate() +" to "+ solrSerObj.getLastResultsCrawlingDate());
		
		
	}

}
