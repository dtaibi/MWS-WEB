package eu.dandelion;

import javax.xml.parsers.*;
import org.w3c.dom.*;


public class NewsParser {

	 
	    public static void main(String[] args)
	    {
	    	String query="Renzi";
	    	// http://news.google.ca/news?ned=ca&hl=en&as_drrb=q&as_qdr=a&as_nloc=winnipeg&scoring=r&output=rss&num=50&q="Candidate Name"
	        String url = "http://news.google.ca/news?ned=en&hl=en&output=rss&num=40&q=\""+query+"\"";   
	        try
	        {
	            DocumentBuilderFactory f = 
	                    DocumentBuilderFactory.newInstance();
	            DocumentBuilder b = f.newDocumentBuilder();
	            Document doc = b.parse(url);
	 
	            doc.getDocumentElement().normalize();
	            System.out.println ("Root element: " + 
	                        doc.getDocumentElement().getNodeName());
	       
	            
	            // loop through each item
	            NodeList items = doc.getElementsByTagName("channel");
	            
	            
	            
	            
	            
	            for (int i = 0; i < items.getLength(); i++)
	            {
	                Node n = items.item(i);
	                if (n.getNodeType() != Node.ELEMENT_NODE)
	                    continue;
	                Element e = (Element) n;
	 
	                
	                // get the "title elem" in this item (only one)
	                NodeList titleList = 
	                                e.getElementsByTagName("item");
	                
	                System.out.println("length:"+titleList.getLength());
	                
	                for (int j = 0; j < titleList.getLength(); j++)
		            {
	                
	                	Node n1 = titleList.item(j);
		                if (n.getNodeType() != Node.ELEMENT_NODE)
		                    continue;
		                Element e1 = (Element) n1;
		                
		                NodeList titleList1 = 
                                e1.getElementsByTagName("link");
		                
		                Element titleElem = (Element) titleList1.item(0);
		 
		                // get the "text node" in the title (only one)
		                Node titleNode = titleElem.getChildNodes().item(0);
		                // System.out.println(titleNode.getNodeValue().replaceAll("\\<.*?>"," ")); // for descr and title
		                System.out.println(titleNode.getNodeValue().split("url=")[1]); // for link
		            }
	            }
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	    }
	}
