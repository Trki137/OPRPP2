package hr.fer.zemris.java.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.LocalDateTime;

@WebListener
public class RuntimeListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext context = servletContextEvent.getServletContext();

        context.setAttribute("start",System.currentTimeMillis());

        System.out.println(System.currentTimeMillis());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
