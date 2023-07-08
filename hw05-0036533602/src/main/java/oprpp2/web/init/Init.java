package oprpp2.web.init;


import oprpp2.dao.jpa.JPAEMFProvider;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Init implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("baza.podataka.za.blog");
        servletContextEvent.getServletContext().setAttribute("emf", emf);
        JPAEMFProvider.setEmf(emf);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        JPAEMFProvider.setEmf(null);
        EntityManagerFactory entityManagerFactory = (EntityManagerFactory) servletContextEvent.getServletContext().getAttribute("emf");

        if(entityManagerFactory != null)
            entityManagerFactory.close();

    }
}
