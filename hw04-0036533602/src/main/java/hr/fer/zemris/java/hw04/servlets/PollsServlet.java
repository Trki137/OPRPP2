package hr.fer.zemris.java.hw04.servlets;

import hr.fer.zemris.java.hw04.dao.DAO;
import hr.fer.zemris.java.hw04.dao.DAOProvider;
import hr.fer.zemris.java.hw04.model.PollsModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "poll", urlPatterns = "/servleti/index.html")
public class PollsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DAO dao = DAOProvider.getInstance();

        List<PollsModel> pollsModelList = dao.getAllPolls();

        req.setAttribute("polls", pollsModelList);
        req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req,resp);
    }
}
