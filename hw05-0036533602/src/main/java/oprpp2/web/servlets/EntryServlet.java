package oprpp2.web.servlets;

import oprpp2.dao.DAO;
import oprpp2.dao.DAOProvider;
import oprpp2.model.BlogComment;
import oprpp2.model.BlogEntry;
import oprpp2.model.form.BlogCommentForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "entry", urlPatterns = "/servleti/entry")
public class EntryServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogCommentForm blogCommentForm = new BlogCommentForm();
        blogCommentForm.httpRequestFill(req);
        blogCommentForm.validate();

        DAO dao = DAOProvider.getDAO();
        if(blogCommentForm.hasAnyError()){
            for(var entry : blogCommentForm.getErrors().entrySet())
                req.setAttribute(entry.getKey(),entry.getValue());

            String entryId = req.getParameter("entry_id");

            BlogEntry blogEntry = dao.getBlogEntryById(Long.parseLong(entryId));
            req.setAttribute("blog",blogEntry);

            req.getRequestDispatcher("/WEB-INF/pages/entry.jsp").forward(req,resp);
            return;
        }


        Long userId = (Long) req.getSession().getAttribute("current.user.id");
        Long entryId = Long.valueOf(blogCommentForm.getEntryId());

        BlogEntry blogEntry = dao.getBlogEntryById(entryId);
        String userEmail = dao.getUserById(userId).getEmail();

        BlogComment blogComment = new BlogComment();

        blogCommentForm.fillToBlogComment(blogComment);
        blogComment.setUsersEMail(userEmail);
        blogComment.setBlogEntry(blogEntry);

        dao.saveBlogComment(blogComment);
        resp.sendRedirect(req.getContextPath() + "/servleti/author/"+blogEntry.getUser().getNick()+"/"+entryId);
    }
}
