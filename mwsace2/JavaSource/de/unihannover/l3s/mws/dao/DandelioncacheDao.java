package de.unihannover.l3s.mws.dao;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.unihannover.l3s.mws.model.Dandelioncache;
import de.unihannover.l3s.mws.util.HibernateUtil;
import eu.dandelion.DandelionDataObject;
import eu.dandelion.EntityExtractionService;
 
public class DandelioncacheDao {
  
	
public void addToCache(String url, String response, Double minConfidence) {
		
		Dandelioncache cache=new Dandelioncache();
		cache.setResponse(response);
		cache.setUrl(url);
		cache.setMinConfidence(minConfidence);
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
	
	public ArrayList<DandelionDataObject> getCache(String url, Double minConfidence) {
		List<Dandelioncache> list;
		
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
        	session.beginTransaction();
        	String queryString = "from Dandelioncache c where url = :url and minConfidence = :minConfidence";
            Query query = session.createQuery(queryString);
            query.setString("url", url);
            query.setDouble("minConfidence", minConfidence);
            list = query.list();
            if (list.size()>0){
            	return EntityExtractionService.getObjectList(list.get(0).getResponse(),list.get(0).getMinConfidence());
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