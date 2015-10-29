package de.unihannover.l3s.mws.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.unihannover.l3s.mws.model.SearchImageResult;
import de.unihannover.l3s.mws.model.SearchResult;
import de.unihannover.l3s.mws.model.SearchVideoResult;
import de.unihannover.l3s.mws.model.YData;

public class StatsManager {

    public List<YData> getMatcthTable(List<String> sites)
    {
        List<YData> ydata = new ArrayList<YData>();
        List<String> checked=new ArrayList<String>();
        
        for (String s : sites){
        	if (!checked.contains(s)){
        		int occurrences = Collections.frequency(sites, s);
        		YData yd = new YData();
                yd.setQty(occurrences );
                yd.setSite(s);
                ydata.add(yd);
        	}
        	checked.add(s);
        }
        
        /*
        for (int i = 0; i < sites.size(); i++)
        {
            if (ydata.size() == 0)
            {
                YData yd = new YData();
                yd.setQty(1);
                yd.setSite(sites.get(i));
                ydata.add(yd);
            }
            else 
            { 
                int index = -1;
                for (int j = 0; j < ydata.size(); j++) 
                {
                	if (i<ydata.size() && ydata.get(j).getSite().compareTo(ydata.get(i).getSite())==0)
                    {
                        index = j;
                        YData yd = ydata.get(j);
                        yd.setQty(yd.getQty()+1);
                        ydata.get(j).setQty(yd.getQty());
                        ydata.get(j).setSite(yd.getSite());
                        break;
                    }
                }
                if (index == -1) 
                {
                    YData yd = new YData();
                    yd.setQty(1);
                    yd.setSite(sites.get(i));
                    ydata.add(yd);
                }
            }
        }*/

        return ydata;
    }
	
    private String getTldString(String urlString) {
        URL url = null;
        String tldString = null;
        try {
            url = new URL(urlString);
            String[] domainNameParts = url.getHost().split("\\.");
            tldString = domainNameParts[domainNameParts.length-1];
        }
        catch (MalformedURLException e) {   
        }

        return tldString;
    }
    
    public List<String> getLangSites(List<SearchResult> results,List<SearchImageResult> image_results,List<SearchVideoResult> video_results)
    {
        List<String> sites = new ArrayList<String>();
        
        if (results != null)
        {
            for (SearchResult r : results)
            {
                if(r.getUrl().contains("http://"))
                    sites.add(getTldString(r.getUrl()));
            }
        }
        return sites;
    }
    
	public List<String> getSites(List<SearchResult> results,List<SearchImageResult> image_results,List<SearchVideoResult> video_results)
    {
        List<String> sites = new ArrayList<String>();
        // List<SearchResult> results=new ArrayList<SearchResult>();
        // List<SearchImageResult> image_results=new ArrayList<SearchImageResult>();
        // List<SearchVideoResult> video_results=new ArrayList<SearchVideoResult>();

        if (results != null)
        {
            for (SearchResult r : results)
            {
                if(r.getUrl().contains("http://"))
                    sites.add(r.getUrl().substring(7).substring(0,r.getUrl().substring(7).indexOf('/')));
            }
        }
        else if (image_results != null) 
        {
            for (SearchImageResult r : image_results)
            {
            	// String[] a=new String[]{">"};
            	// sites.Add(r.title.Split(new string[]{">"}, StringSplitOptions.None)[1].Replace("</cite",""));
                sites.add(r.getTitle().split("")[1].replace("</cite",""));
            }
        }
        else if (video_results != null) 
        {
            for (SearchVideoResult r : video_results)
            {
                if (r.getUrl().contains("http://"))
                    sites.add(r.getUrl().substring(7).substring(0, r.getUrl().substring(7).indexOf('/')));
            }
        }
	
        return sites;
    }


	public List<YData> getMatcthWeightedTable(List<String> sites) {
		List<YData> ydata = new ArrayList<YData>();
        List<String> checked=new ArrayList<String>();
        int conta=1;
        for (String s : sites){
        	int value = sites.size()/conta;
        	if (!checked.contains(s)){
        		YData yd = new YData();
                yd.setQty(value);
                yd.setSite(s);
                ydata.add(yd);
        	}else{
        		for (YData y : ydata){
        			if (y.getSite().compareTo(s)==0){
        				// System.out.println("Aggiungo "+value+" a "+y.getSite()+" che aveva "+y.getQty());
        				y.setQty(y.getQty()+value);
        				break;
        			}
        		}
        	}
        	checked.add(s);
        	conta++;
        }
        return ydata;
	}
	
}
