package oprpp2.web.servlets;

import oprpp2.dao.DAO;
import oprpp2.dao.DAOProvider;
import oprpp2.model.BlogEntry;
import oprpp2.model.BlogUser;
import oprpp2.model.form.BlogEntryForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "author", urlPatterns = "/servleti/author/*")
public class AuthorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String getRestOfPath = req.getPathInfo();
        DAO dao = DAOProvider.getDAO();

        if(getRestOfPath.endsWith("/new")){
            req.getRequestDispatcher("/WEB-INF/pages/event-form.jsp").forward(req,resp);
            return;
        }
        else if(getRestOfPath.endsWith("/edit")){

            Long id = Long.parseLong(getRestOfPath.split("/")[2]);
            BlogEntry blogEntry = dao.getBlogEntryById(id);

            req.setAttribute("blog", blogEntry);
            req.getRequestDispatcher("/WEB-INF/pages/event-form.jsp").forward(req,resp);

        }else if(getRestOfPath.matches("^[^/]*/[^/]*$")){

            String nick = getRestOfPath.substring(1);

            BlogUser blogUser = dao.getUserByNick(nick);

            if(blogUser == null){
                req.setAttribute("error", "Page not found");
                req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,resp);
                return;
            }

            List<BlogEntry> blogEntries = dao.getBlogEntriesForUser(blogUser);
            req.setAttribute("blogs", blogEntries);
            req.getRequestDispatcher("/WEB-INF/pages/entries.jsp").forward(req,resp);
          }


        Long entryId = Long.parseLong(getRestOfPath.split("/")[2]);
        BlogEntry blogEntry = dao.getBlogEntryById(entryId);

        req.setAttribute("blog",blogEntry);
        req.getRequestDispatcher("/WEB-INF/pages/entry.jsp").forward(req,resp);



    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogEntryForm blogEntryForm = new BlogEntryForm();
        blogEntryForm.httpRequestFill(req);
        blogEntryForm.validate();

        if(blogEntryForm.hasAnyError()){
            for(var entry : blogEntryForm.getErrors().entrySet())
                req.setAttribute(entry.getKey(), entry.getValue());

            req.setAttribute("blog",blogEntryForm);
            req.getRequestDispatcher("/WEB-INF/pages/event-form.jsp").forward(req,resp);
            return;
        }

        DAO dao = DAOProvider.getDAO();

        if (blogEntryForm.getId().isEmpty()){
            Long userId = (Long) req.getSession().getAttribute("current.user.id");
            BlogUser user = dao.getUserById(userId);

            BlogEntry blogEntry = new BlogEntry();

            blogEntryForm.fillToBlogEntry(blogEntry);
            blogEntry.setCreatedAt(new Date());
            blogEntry.setUser(user);

            dao.saveEntry(blogEntry);
            resp.sendRedirect(req.getContextPath()+ "/servleti/main");
            return;
        }

        BlogEntry entry = dao.getBlogEntryById(Long.valueOf(blogEntryForm.getId()));
        if(entry == null){
            req.setAttribute("error", "Page not found");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,resp);
            return;
        }
        blogEntryForm.update(entry);
        dao.updateEntry(entry);
        resp.sendRedirect(req.getContextPath()+ "/servleti/main");
    }
}
