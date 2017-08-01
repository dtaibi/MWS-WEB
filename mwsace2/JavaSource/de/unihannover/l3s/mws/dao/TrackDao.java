package de.unihannover.l3s.mws.dao;


import java.util.ArrayList;
import java.util.Date;
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
    
    public List<Track> getTrackByDates(Date startdate, Date enddate) {
 	   List<Track> list;
         Session session = HibernateUtil.getSessionFactory().openSession();
         
         try {
         	session.beginTransaction();
         	String queryString = "from Track t where date <= :enddate and date>= :startdate";
             Query query = session.createQuery(queryString);
             query.setDate("startdate", startdate);
             query.setDate("enddate", enddate);
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
		if (operation!=null && operation.trim().compareTo("")==0)
			operation=null;
		if (userselected!=null && userselected.trim().compareTo("")==0)
			userselected=null;
		
		if (operation==null && userselected==null)
			return this.getAllTrackBeans();
		
		
		List<Track> list;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
        	session.beginTransaction();
        	String queryString ="";
        	if (operation==null)
        		queryString = "from Track t where utente = :utente ";
        	else if (userselected==null)
        		queryString = "from Track t where operation = :operation";
        	else
        		queryString = "from Track t where utente = :utente and operation = :operation";
            
        	Query query = session.createQuery(queryString);
            // System.out.println(queryString+"::"+operation+"::"+userselected);
            if (operation==null)
            	query.setString("utente", userselected);
            else if (userselected==null)
            	query.setString("operation", operation);
            else {
            	query.setString("utente", userselected);
            	query.setString("operation", operation);
            }
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

	public List<Track> getTrackByOperationListAndUserAndDate(
			List<String> selectedoperations, String userselected,
			String startDate, String endDate) {
		if (userselected!=null && userselected.trim().compareTo("")==0)
			userselected=null;
		if (endDate!=null && endDate.trim().compareTo("")==0)
			endDate=null;
		if (startDate!=null && startDate.trim().compareTo("")==0)
			startDate=null;
		
		
		List<Track> list;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
        	session.beginTransaction();
        	String queryString ="";
        	queryString = "from Track t ";
        	
        	ArrayList<String> optional=new ArrayList<String>();
        	if (userselected!=null)
        		optional.add(" utente = :utente");
        	if (endDate != null)
        		optional.add(" date <= :enddate");
        	if (startDate != null)
        		optional.add(" date>= :startdate");
        	
        	if (optional.size()>0) {
        		queryString+=" where "+String.join(" and ", optional);
        		if (selectedoperations.size()>0){
        			queryString+=" and operation IN ( ";
        			for (String selop : selectedoperations)
        				queryString+="'"+selop+"',";
        			queryString+="'' ) ";
        		}
        	}else
        		if (selectedoperations.size()>0){
        			queryString+=" where operation IN ( ";
        			for (String selop : selectedoperations)
        				queryString+="'"+selop+"',";
        			queryString+="'' ) ";
        		}
        	
        	// System.out.println(queryString+"::"+endDate+"::"+startDate+"::"+userselected);
        	
        	Query query = session.createQuery(queryString);
            // System.out.println(queryString+"::"+operation+"::"+userselected);
            //if (operation!=null)
            //	query.setString("operation", operation);
            if (userselected!=null)
            	query.setString("utente", userselected);
            if (endDate != null)
            	query.setString("enddate", endDate);
            if (startDate != null)
            	query.setString("startdate", startDate);
            
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