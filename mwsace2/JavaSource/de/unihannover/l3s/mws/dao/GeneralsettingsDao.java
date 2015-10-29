package de.unihannover.l3s.mws.dao;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import de.unihannover.l3s.mws.model.Generalsettings;
import de.unihannover.l3s.mws.util.HibernateUtil;
 
public class GeneralsettingsDao {
  
    public List<Generalsettings> getAllGeneralsettings() {
        List<Generalsettings> gsettings = new ArrayList<Generalsettings>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Generalsettings"); 
            gsettings = query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return gsettings;
    }
 
}