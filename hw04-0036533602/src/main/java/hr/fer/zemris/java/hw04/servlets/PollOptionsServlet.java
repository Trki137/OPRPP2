package hr.fer.zemris.java.hw04.servlets;

import hr.fer.zemris.java.hw04.dao.DAO;
import hr.fer.zemris.java.hw04.dao.DAOProvider;
import hr.fer.zemris.java.hw04.model.PollOptionsModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "pollOptions", urlPatterns = "/servleti/glasanje")
public class PollOptionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = req.getParameter("pollID") == null ? null : Integer.parseInt(req.getParameter("pollID"));

        if(id == null)
            throw new RuntimeException();

        DAO dao = DAOProvider.getInstance();

        List<PollOptionsModel> pollOptionsModelList = dao.getOptionsForPoll(id);
        String message = dao.getPollMessage(id);

        if(message == null)
            throw new RuntimeException();

        req.setAttribute("pollOptions", pollOptionsModelList);
        req.setAttribute("message",message);

        req.getRequestDispatcher("/WEB-INF/pages/vote.jsp").forward(req,resp);
    }
}
