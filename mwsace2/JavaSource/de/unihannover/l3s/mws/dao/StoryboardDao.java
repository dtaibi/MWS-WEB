package de.unihannover.l3s.mws.dao;


import java.security.acl.Owner;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;

import de.unihannover.l3s.mws.model.Storyboard;
import de.unihannover.l3s.mws.model.StoryboardItem;
import de.unihannover.l3s.mws.model.Utente;
import de.unihannover.l3s.mws.util.HibernateUtil;
 
public class StoryboardDao {
 
    public void addStoryboard(Storyboard sb) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.saveOrUpdate(sb);
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

    public Storyboard getStoryboardById(Long sbid) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	session.beginTransaction();
            String queryString = "from Storyboard where id = :id";
            Query query = session.createQuery(queryString);
            query.setLong("id", sbid);
            List<Storyboard> list = query.list();
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
    
    public Storyboard getStoryboardByName(String nome) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	session.beginTransaction();
            String queryString = "from Storyboard where nome = :nome";
            Query query = session.createQuery(queryString);
            query.setString("nome", nome);
            List<Storyboard> list = query.list();
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

	public List<Storyboard> getStoryboardByUser(Utente utente) {
		List<Storyboard> list;
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	session.beginTransaction();
            String queryString = "from Storyboard where utente = :utente";
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

	public void deleteStoryboard(Storyboard ricerca) {
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

	public void deleteStoryboardItem(StoryboardItem sr) {
		Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.delete(sr);
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

	public void saveStoryboard(StoryboardItem sbi) {
		Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.saveOrUpdate(sbi);
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

	public List<Storyboard> getStudentStoryboard() {
		List<Storyboard> list;
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	session.beginTransaction();
        	
        	/*Criteria c = session.createCriteria(Storyboard.class, "storyboard");
        	c.createAlias("storyboard.utente", "utente");
        	c.add(Restrictions.eq("utente.role", "student"));
        	c.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        	*/
            String queryString = "from Storyboard s where s.utente in (select id from Utente u where u.role = :ro)";
            Query query = session.createQuery(queryString);
            query.setString("ro", "student");
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