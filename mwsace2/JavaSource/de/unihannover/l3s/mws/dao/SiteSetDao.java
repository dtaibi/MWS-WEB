package de.unihannover.l3s.mws.dao;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import de.unihannover.l3s.mws.model.SiteSet;
import de.unihannover.l3s.mws.model.SiteSetItem;
import de.unihannover.l3s.mws.model.Utente;
import de.unihannover.l3s.mws.util.HibernateUtil;
 
public class SiteSetDao {
 
    public List<SiteSet> getAllSiteSet() {
        List<SiteSet> siteset = new ArrayList<SiteSet>();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	Query query = session.createQuery("from SiteSet"); 
            siteset = query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return siteset;
    }
    
    public List<SiteSet> getAllSiteSetByUser(Utente utente) {
        List<SiteSet> siteset = new ArrayList<SiteSet>();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	String queryString = "from SiteSet where utente = :utente";
            Query query = session.createQuery(queryString);
            query.setLong("utente", utente.getId());
            siteset = query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return siteset;
    }
    
    public List<SiteSetItem> getAllSiteSetItem(List<Long> list) {
        List<SiteSetItem> siteset = new ArrayList<SiteSetItem>();
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	Criteria criteria = session.createCriteria(SiteSetItem.class);
        	criteria.add(Restrictions.in("id", list.toArray()));
        	siteset = (List<SiteSetItem>) criteria.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return siteset;
    }

	public SiteSet getSiteSetById(long sitesetid) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	session.beginTransaction();
            String queryString = "from SiteSet where id = :id";
            Query query = session.createQuery(queryString);
            query.setLong("id", sitesetid);
            List<SiteSet> list = query.list();
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

	
	
	public void addSiteSet(SiteSet siteSet) {
		Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.saveOrUpdate(siteSet);
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
	
	public void deleteSiteSet(String siteSets) {
		for (String s : siteSets.split(",")){
			if (s.startsWith("u")){
				SiteSet siteSet=getSiteSetById(Long.parseLong(s.replace("u", "")));
		        Transaction trns = null;
		        Session session = HibernateUtil.getSessionFactory().openSession();
		        try {
		            trns = session.beginTransaction();
		            session.delete(siteSet);
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
			if (s.startsWith("t")){
				SiteSetItem siteSetitem=getSiteSetItemById(Long.parseLong(s.replace("t", "")));
		        Transaction trns = null;
		        Session session = HibernateUtil.getSessionFactory().openSession();
		        try {
		            trns = session.beginTransaction();
		            session.delete(siteSetitem);
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
    }

	public void editSiteSet(SiteSet siteset) {
			Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        try {
	            trns = session.beginTransaction();
	            session.update(siteset);
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

	public SiteSetItem getSiteSetItemById(long sitesetid) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	session.beginTransaction();
            String queryString = "from SiteSetItem where id = :id";
            Query query = session.createQuery(queryString);
            query.setLong("id", sitesetid);
            List<SiteSetItem> list = query.list();
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

	public void editSiteSetItem(SiteSetItem sitesetitem) {
		Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(sitesetitem);
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