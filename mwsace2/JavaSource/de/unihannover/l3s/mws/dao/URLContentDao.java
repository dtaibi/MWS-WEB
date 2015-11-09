package de.unihannover.l3s.mws.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.unihannover.l3s.mws.model.Generalsettings;
import de.unihannover.l3s.mws.model.UrlContent;
import de.unihannover.l3s.mws.util.HibernateUtil;


public class URLContentDao {

	public void SaveURLContent(UrlContent Content){
		Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.save(Content);
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