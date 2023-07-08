package oprpp2.web.servlets;

import oprpp2.dao.DAO;
import oprpp2.dao.DAOProvider;
import oprpp2.model.BlogUser;
import oprpp2.model.form.SignInForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@WebServlet(name = "signin", urlPatterns = "/servleti/signin")
public class SignInServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SignInForm signInForm = new SignInForm();
        signInForm.httpRequestFill(req);
        signInForm.validate();
        DAO dao = DAOProvider.getDAO();

        if(signInForm.hasAnyError()){
            for(var entry : signInForm.getErrors().entrySet())
                req.setAttribute(entry.getKey(),entry.getValue());

            req.setAttribute("user", signInForm);

            List<BlogUser> listOfAuthors = dao.getAllUsers();
            req.setAttribute("authors", listOfAuthors);

            req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req,resp);
            return;
        }


        boolean exists = dao.checkNickExists(signInForm.getNick());

        if(!exists){
            req.setAttribute("nick", "Nickname "+ signInForm.getNick()+" doesn't exist");
            req.setAttribute("user", signInForm);

            List<BlogUser> listOfAuthors = dao.getAllUsers();
            req.setAttribute("authors", listOfAuthors);

            req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req,resp);
        }

        BlogUser blogUser = dao.getUserByNick(signInForm.getNick());
        boolean passwordValid;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA3-512");
            byte[] digest = md.digest(signInForm.getPassword().getBytes());
            passwordValid = blogUser.getPasswordHash().equals(new String(digest));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        if(!passwordValid){
            req.setAttribute("password", "Invalid password");
            signInForm.setPassword("");
            req.setAttribute("user", signInForm);

            List<BlogUser> listOfAuthors = dao.getAllUsers();
            req.setAttribute("authors", listOfAuthors);

            req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req,resp);
        }

        req.getSession().setAttribute("current.user.id", blogUser.getId());
        req.getSession().setAttribute("current.user.fn", blogUser.getFirstName());
        req.getSession().setAttribute("current.user.ln", blogUser.getLastName());
        req.getSession().setAttribute("current.user.nick", blogUser.getNick());


        resp.sendRedirect(req.getContextPath()+ "/servleti/main");
    }
}
