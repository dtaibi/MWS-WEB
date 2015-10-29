package de.unihannover.l3s.mws.dao;


import org.hibernate.Session;
import org.hibernate.Transaction;

import de.unihannover.l3s.mws.model.Risultati;
import de.unihannover.l3s.mws.util.HibernateUtil;
 
public class RisultatiDao {
 
    public void addRisultati(Risultati track) {
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

}