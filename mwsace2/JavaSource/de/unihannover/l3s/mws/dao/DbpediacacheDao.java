package de.unihannover.l3s.mws.dao;



import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.unihannover.l3s.mws.model.Dbpediacache;
import de.unihannover.l3s.mws.util.HibernateUtil;
 
public class DbpediacacheDao {
  
	
public void addCache(String dbpedia, String dbpedialoc) {
		
		Dbpediacache cache=new Dbpediacache();
		cache.setDbpedia(dbpedia);
		cache.setDbpedialoc(dbpedialoc);
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
	
	
	public Dbpediacache getCache(String dbpedialoc) {
		List<Dbpediacache> list;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
        	session.beginTransaction();
        	String queryString = "from Dbpediacache c where dbpedialoc = :dbpedialoc";
            Query query = session.createQuery(queryString);
            query.setString("dbpedialoc", dbpedialoc);
            list = query.list();
            if (list.size()>0)
            	return list.get(0);
            else
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