package oprpp2.web.servlets;

import oprpp2.dao.DAO;
import oprpp2.dao.DAOProvider;
import oprpp2.dao.jpa.JPADAOImpl;
import oprpp2.model.BlogUser;
import oprpp2.model.form.SignUpForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "register", urlPatterns = "/servleti/register")
public class SignUpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.httpRequestFill(req);
        signUpForm.validate();

        if (signUpForm.hasAnyError()) {
            for (var errorSet : signUpForm.getErrors().entrySet())
                req.setAttribute(errorSet.getKey(), errorSet.getValue());

            req.setAttribute("user", signUpForm);

            req.getRequestDispatcher("/WEB-INF/pages/signup.jsp").forward(req, resp);
            return;
        }

        BlogUser blogUser = new BlogUser();
        signUpForm.fillToBlogUser(blogUser);

        DAO dao = DAOProvider.getDAO();

        boolean exists = dao.checkEmailExists(blogUser.getEmail());

        if (exists) {
            req.setAttribute("email", "Email " + blogUser.getEmail() + " is already in use");
            req.setAttribute("user", signUpForm);

            req.getRequestDispatcher("/WEB-INF/pages/signup.jsp").forward(req, resp);
            return;
        }

        exists = dao.checkNickExists(blogUser.getNick());

        if (exists) {
            req.setAttribute("nick", "Nickname " + blogUser.getNick() + " is already in use");
            req.setAttribute("user", signUpForm);

            req.getRequestDispatcher("/WEB-INF/pages/signup.jsp").forward(req, resp);
            return;
        }

        dao.saveUser(blogUser);

        req.getSession().setAttribute("current.user.id", blogUser.getId());
        req.getSession().setAttribute("current.user.fn", blogUser.getFirstName());
        req.getSession().setAttribute("current.user.ln", blogUser.getLastName());
        req.getSession().setAttribute("current.user.nick", blogUser.getNick());

        resp.sendRedirect(req.getContextPath() + "/servleti/main");
    }
}
