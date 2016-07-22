package de.unihannover.l3s.mws.dao;



import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.unihannover.l3s.mws.model.Bingcache;
import de.unihannover.l3s.mws.model.Dbpediacache;
import de.unihannover.l3s.mws.model.SearchResult;
import de.unihannover.l3s.mws.model.SearchWebResult;
import de.unihannover.l3s.mws.util.HibernateUtil;
 
public class BingcacheDao {
  
	
public void addToCache(String title, String url, String snippet, String market, String querystring) {
		
		Bingcache cache=new Bingcache();
		cache.setMarket(market);
		cache.setQuerystring(querystring);
		cache.setTitle(title);
		cache.setUrl(url);
		cache.setSnippet(snippet);
		cache.setDate((new GregorianCalendar()).getTime());
		
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.save(cache);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
    }
	
public void addToCache(SearchWebResult swr,String market, String querystring) {
	
	Bingcache cache=new Bingcache();
	cache.setMarket(market);
	cache.setQuerystring(querystring);
	cache.setTitle(swr.getTitle());
	cache.setUrl(swr.getUrl());
	cache.setSnippet(swr.getDescription());
	cache.setDate((new GregorianCalendar()).getTime());
	
    Transaction trns = null;
    Session session = HibernateUtil.getSessionFactory().openSession();
    try {
        trns = session.beginTransaction();
        session.save(cache);
        session.getTransaction().commit();
    } catch (RuntimeException e) {
        if (trns != null) {
            trns.rollback();
        }
        e.printStackTrace();
    } finally {
        session.flush();
        session.close();
    }
}
	
	/* public List<Bingcache> getCache(String querystring,String market) {
		List<Bingcache> list;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
        	session.beginTransaction();
        	String queryString = "from Bingcache c where querystring = :querystring and market = :market";
            Query query = session.createQuery(queryString);
            query.setString("querystring", querystring);
            query.setString("market", market);
            list = query.list();
            return (list.size()>0) ? list : null;
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return null;
	}*/
	
	public ArrayList<SearchResult> getCache(String querystring,String market) {
		List<Bingcache> list;
		ArrayList<SearchResult> listresult;
		
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
        	session.beginTransaction();
        	String queryString = "from Bingcache c where querystring = :querystring and market = :market";
            Query query = session.createQuery(queryString);
            query.setString("querystring", querystring);
            query.setString("market", market);
            list = query.list();
            if (list.size()>0){
            	listresult=new ArrayList<SearchResult>();
	            for (Bingcache bc : list ){
	            	SearchWebResult swr=new SearchWebResult();
	            	swr.setDescription(bc.getSnippet());
	            	swr.setTitle(bc.getTitle());
	            	swr.setUrl(bc.getUrl());
	            	listresult.add(swr);
	            }
	            return listresult;
            }
            return null;
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return null;
	}
	
	
}