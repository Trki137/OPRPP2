package oprpp2.dao.jpa;

import oprpp2.dao.DAOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JPAEMProvider {
    private static ThreadLocal<EntityManager> locals = new ThreadLocal<>();

    public static EntityManager getEntityManager() {
        EntityManager em = locals.get();
        if (em == null) {
            em = JPAEMFProvider.getEmf().createEntityManager();
            em.getTransaction().begin();
            locals.set(em);
        }
        return em;
    }

    public static void close(){
        EntityManager em = locals.get();
        if(em == null) return;

        try{
            em.getTransaction().commit();
        }catch (Exception ex){
            throw new DAOException("Unable to commit transaction.", ex);
        }

        try{
            em.close();
        }catch (Exception ex){
           throw new DAOException("Unable to close entity manager.", ex);
        }

        locals.remove();
     }
}
