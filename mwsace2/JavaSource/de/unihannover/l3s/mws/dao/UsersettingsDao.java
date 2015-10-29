package de.unihannover.l3s.mws.dao;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import de.unihannover.l3s.mws.model.Generalsettings;
import de.unihannover.l3s.mws.model.Usersettings;
import de.unihannover.l3s.mws.model.Utente;
import de.unihannover.l3s.mws.util.HibernateUtil;
 
public class UsersettingsDao {
 
      public void deleteUtente(int custid) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            Utente cust = (Utente) session.load(Utente.class, new Integer(custid));
            session.delete(cust);
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
 
    public void updateUtente(Utente cust) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(cust);
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
 
    public List<Utente> getAllUtenti() {
        List<Utente> users = new ArrayList<Utente>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            users = session.createQuery("select concat(first_name, ' ', last_name) as name from Customer").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return users;
    }
 



	/* public Usersettings getUserSeetings(Utente utente, Generalsettings gs) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {

        	List<Usersettings> l=session.createCriteria(Usersettings.class)
            	    .add( Restrictions.eq("utente", utente ) )
            	    .add( Restrictions.eq("generalsettings", gs) )
            	    .list();
        	if (l.size()>0)
        		return l.get(0);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
		return null;
	} */
}