package eu.dandelion;

import java.io.*;
import java.util.*;
public class test {


	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
				
		
		String content="The world is a wonderful world so live "
				+ "while you can, as healthy as it can be";
		
		String content1 ="Your health is really important, not only for"
				+ " you, but also for your loved ones in this world";
				
		String content2 ="What is more healthy, apple or orange? ";
		
		String content3 ="Apple and orange, both are fruits. Fruits are"
				+ "grown in the farms in this world. Fruits are healty"
				+ "and really benificial for your health ";
		
		HashMap<String, Long> hmap;
		
//		UrlContentHandler  obj= new UrlContentHandler("C:\\Users\\Mohsin\\workspace\\indexData\\1454122164262"); 
		// Please set the locdir in DataIndex folder
		// The following will create a folder with timestap in the local dir.
		DataIndex  obj= new DataIndex(); 
		obj.cacheContent("http://www.cdc.gov/zika/index.html","url");
		obj.cacheContent("http://edition.cnn.com/2016/01/28/health/zika-virus-global-response/index.html","url");
		obj.cacheContent("https://en.wikipedia.org/wiki/Zika_virus","url");
		// obj.cacheContent(content3,"text");
		obj.cacheContent("http://www.who.int/mediacentre/factsheets/zika/en/","url");
		obj.cacheContent("http://www.quotidiano.net/virus-zika-1.1671880","url");
		
		obj.EndCachingSession();
		
		hmap= obj.getAllTermsAndTermFrequiences();
		
		Set<Map.Entry<String, Long>> set = hmap.entrySet();
	    for (Map.Entry<String, Long> me : set) {
	      System.out.println(me.getKey() + ": "+me.getValue());
	      
	    }
		System.out.println(obj.getCachePath());
		//obj.clearCache(); //deletes the CachedIndex from Harddrive
// ............................................................................		
//DataIndex obj = new DataIndex();
//	
//	 obj.addDoc(content);
//	 obj.addDoc(content1);
//     obj.addDoc(content2);
//	 obj.addDoc(content3);
//	 obj.endWriting();
//	 String word= "fruits";
//	 word =obj.stemContent(word);
//	 obj.openDirForReading();
//	 System.out.println("Term Frequency of the word '"+word+"' is: "+obj.termFreq(word));
//	 System.out.println("Document Frequency of the word '"+word+"' is: "+obj.documentFreq(word));
//	 System.out.println("IDF of the word '"+word+" is "+obj.idf(word));
//	 obj.deleteIndexData();	
//	 
//	 
//	 
//	 
	 
	 
	 
	 	}

}
