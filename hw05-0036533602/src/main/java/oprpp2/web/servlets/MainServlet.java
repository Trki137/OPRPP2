package oprpp2.web.servlets;

import oprpp2.dao.DAO;
import oprpp2.dao.DAOProvider;
import oprpp2.model.BlogUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "main", urlPatterns = "/servleti/main")
public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = req.getSession().getAttribute("current.user.id") != null ? (Long) req.getSession().getAttribute("current.user.id") : null;
        DAO dao = DAOProvider.getDAO();
        if(userId != null) {
            List<BlogUser> listOfAuthors = dao.getUsers(userId);
            req.setAttribute("authors", listOfAuthors);
        }else {
            List<BlogUser> listOfAuthors = dao.getAllUsers();
            req.setAttribute("authors", listOfAuthors);
        }
        req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req,resp);
    }
}
