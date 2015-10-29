package de.unihannover.l3s.mws.dao;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.unihannover.l3s.mws.model.Ricerca;
import de.unihannover.l3s.mws.model.Utente;
import de.unihannover.l3s.mws.util.HibernateUtil;
 
public class RicercaDao {
 
    public void addRicerca(Ricerca cust) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.saveOrUpdate(cust);
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
 
    public void deleteRicerca(Ricerca ricerca) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.delete(ricerca);
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
 
    public void updateRicerca(Ricerca cust) {
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
 
    public List<Ricerca> getAllRicerca() {
        List<Ricerca> users = new ArrayList<Ricerca>();
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
 
    public Ricerca getRicercaById(Long ricid) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	session.beginTransaction();
            String queryString = "from Ricerca where id = :id";
            Query query = session.createQuery(queryString);
            query.setLong("id", ricid);
            List<Ricerca> list = query.list();
            if (list.size() > 0) {
                return list.get(0);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return null;
    }
    
    public Ricerca getRicercaByName(String nome) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	session.beginTransaction();
            String queryString = "from Ricerca where nome = :nome";
            Query query = session.createQuery(queryString);
            query.setString("nome", nome);
            List<Ricerca> list = query.list();
            if (list.size() > 0) {
                return list.get(0);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return null;
    }

	public List<Ricerca> getRicercaByUser(Utente utente) {
		List<Ricerca> list;
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	session.beginTransaction();
            String queryString = "from Ricerca where utente = :utente";
            Query query = session.createQuery(queryString);
            query.setLong("utente", utente.getId());
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