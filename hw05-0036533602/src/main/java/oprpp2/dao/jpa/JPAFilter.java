package oprpp2.dao.jpa;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/servleti/*")
public class JPAFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try{
            filterChain.doFilter(servletRequest,servletResponse);
        }finally {
            JPAEMProvider.close();
        }
    }

    @Override
    public void destroy() {

    }
}
