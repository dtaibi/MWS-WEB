package de.unihannover.l3s.mws.dao;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.unihannover.l3s.mws.bean.TrackBean;
import de.unihannover.l3s.mws.model.SiteSet;
import de.unihannover.l3s.mws.model.Storyboard;
import de.unihannover.l3s.mws.model.Track;
import de.unihannover.l3s.mws.model.Utente;
import de.unihannover.l3s.mws.util.HibernateUtil;
 
public class TrackDao {
 
    public void addTrack(Track track) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.save(track);
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
    
    //Anousheh 1
    public List<Track> getAllTrackBeans() {
		List<Track> list;
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	session.beginTransaction();
        	
            String queryString = "from Track t";
            Query query = session.createQuery(queryString);
            
            list = query.list();
            
            return list;
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
		return null;
	}
    
    //Anousheh 2
   public List<Track> getTrackByOperation(String operation) {
	   List<Track> list;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
        	session.beginTransaction();
//        	String queryString = "from Track  t where t.operation in (select id from Operation o where o.role = :ro)";
        	String queryString = "from Track t where operation = :operation";
            Query query = session.createQuery(queryString);
            query.setString("operation", operation);
            list = query.list();
            return list;
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return null;
    }

   public List<Track> getTrackByUser(String utente) {
	   List<Track> list;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
        	session.beginTransaction();
//        	String queryString = "from Track  t where t.operation in (select id from Operation o where o.role = :ro)";
        	String queryString = "from Track t where utente = :utente";
            Query query = session.createQuery(queryString);
            query.setString("utente", utente);
            list = query.list();
            return list;
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return null;
    }
  
   public List<Utente> getAllUsersInTrack() {
		List<Utente> list;
		Session session = HibernateUtil.getSessionFactory().openSession();
       try {
    	   session.beginTransaction();
       	
    	   String queryString = "from Utente ";
           Query query = session.createQuery(queryString);
           list = query.list();
           return list;
       } catch (RuntimeException e) {
           e.printStackTrace();
       } finally {
           session.flush();
           session.close();
       }
		return null;
	}
    
    public List<Track> getAllSiteSetByUser(Utente user) {
        List<Track> tracks = new ArrayList<Track>();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	String queryString = "from Track where utente = :user";
            Query query = session.createQuery(queryString);
            query.setLong("user", user.getId());
            tracks = query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return tracks;
    }

	public List<Track> getTrackByOperationAndUser(String operation,
			String userselected) {
		List<Track> list;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
        	session.beginTransaction();
        	String queryString = "from Track t where utente = :utente and operation = :operation";
            Query query = session.createQuery(queryString);
            query.setString("utente", userselected);
            query.setString("operation", operation);
            list = query.list();
            return list;
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return null;
	}
  	
   
}