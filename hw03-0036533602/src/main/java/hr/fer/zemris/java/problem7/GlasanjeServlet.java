package hr.fer.zemris.java.problem7;

import hr.fer.zemris.java.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@WebServlet(name = "glasanje", urlPatterns = {"/glasanje"})
public class GlasanjeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String bandsPath = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
        List<Band> bands = Util.getBands(bandsPath);

        bands.sort(Comparator.comparingInt(Band::getId));

        req.setAttribute("bands", bands);

        req.getRequestDispatcher("WEB-INF/pages/glasanjeIndex.jsp").forward(req,resp);
    }
}
