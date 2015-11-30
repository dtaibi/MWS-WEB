package de.unihannover.l3s.mws.dao;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.unihannover.l3s.mws.model.SiteSet;
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
 
   
}