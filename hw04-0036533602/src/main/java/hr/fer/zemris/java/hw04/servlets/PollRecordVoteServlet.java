package hr.fer.zemris.java.hw04.servlets;

import hr.fer.zemris.java.hw04.dao.DAO;
import hr.fer.zemris.java.hw04.dao.DAOProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "vote", urlPatterns = "/servleti/glasanje-glasaj")
public class PollRecordVoteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer optionID = req.getParameter("optionID") == null ? null : Integer.parseInt(req.getParameter("optionID"));

        if(optionID == null)
            throw new RuntimeException();

        DAO dao = DAOProvider.getInstance();
        dao.addVote(optionID);
        Integer pollID = dao.getPollID(optionID);


        resp.sendRedirect(req.getContextPath()+"/servleti/glasanje-rezultati?pollID="+pollID);
    }
}
