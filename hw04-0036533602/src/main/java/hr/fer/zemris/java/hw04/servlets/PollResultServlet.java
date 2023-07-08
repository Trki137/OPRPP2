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

@WebServlet(name = "result", urlPatterns = "/servleti/glasanje-rezultati")
public class PollResultServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer pollID = req.getParameter("pollID") == null ? null : Integer.parseInt(req.getParameter("pollID"));

        if(pollID == null)
            throw new RuntimeException();

        DAO dao = DAOProvider.getInstance();

        List<PollOptionsModel> results = dao.getPollResults(pollID);
        List<PollOptionsModel> winners = results.stream().filter(option -> option.getVotesCount() == results.get(0).getVotesCount()).toList();

        req.setAttribute("votingResults",results);
        req.setAttribute("winners", winners);
        req.setAttribute("id", pollID);
        req.getRequestDispatcher("/WEB-INF/pages/vote_result.jsp").forward(req,resp);
    }
}
