package de.unihannover.l3s.es;

import java.util.ArrayList;

public class TestService {

	public static void main(String[] args) {
		
		//Pass Index URL
		ESearch eSearch= new ESearch("http://194.119.209.206:9200/index_2/_search");
		
		String query="circular burns";
		int noOfResults=1;
		
		ArrayList<ESearchDataObject> objList;		
		objList=(ArrayList<ESearchDataObject>) eSearch.searhByContent(query, noOfResults);
		for(int i=0; i<objList.size(); i++)
		{
		System.out.println(i+1+": "+objList.get(i).getTitle());
		System.out.println("Content: "+objList.get(i).getContent());
		System.out.println("URL: "+objList.get(i).getUrl());
		System.out.println("Timestamp: "+objList.get(i).getTstamp());
		System.out.println();
		}
	}

}
