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

@WebServlet(name = "glasanje-rezultati", urlPatterns = "/glasanje-rezultati")
public class GlasanjeRezultatServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String resultsPath = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        String bandsPath = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");

        List<BandVotes> votes = Util.getResults(bandsPath,resultsPath);
        votes.sort(Comparator.comparingInt(BandVotes::getVotes).reversed());

        int max = votes.get(0).getVotes();


        List<Band> allBands = Util.getBands(bandsPath);
        List<BandVotes> bandVotesMax = votes.stream().filter(vote -> vote.getVotes() == max).toList();
        List<Band> winners = new ArrayList<>();

        for(BandVotes bandVotes : bandVotesMax){
            List<Band> result = allBands.stream().filter(band -> band.getBandName().equals(bandVotes.getBandName())).toList();
            winners.add(result.get(0));
        }

        req.setAttribute("results", votes);
        req.setAttribute("winners",winners);
        req.getRequestDispatcher("WEB-INF/pages/glasanjeRez.jsp").forward(req,resp);

    }
}
