package de.unihannover.l3s.mws.util;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
 
public class LogicaJPA
{
    private EntityManagerFactory emf;
    private EntityManager em;  
 
    public LogicaJPA(String persistenceUnitName) {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
    }
     
    public void jpaCreate(Object obj) {
        em.getTransaction().begin();
        em.persist(obj);       
        em.getTransaction().commit();  
        em.detach(obj);
    }
    public Query jpaRead(String query) {
        Query q = em.createQuery(query);
        return q;
    }
    public void jpaUpdate(Object obj) {    
        em.getTransaction().begin();
        em.merge(obj); 
        em.getTransaction().commit();
        em.detach(obj);
    }
    public void jpaDelete(Object obj) {
        em.getTransaction().begin();
        Object managed = em.merge(obj);
        em.remove(managed);
        em.getTransaction().commit();
    }
    public void closeLogicaJPA() {
        em.close();
        emf.close();
    }
}