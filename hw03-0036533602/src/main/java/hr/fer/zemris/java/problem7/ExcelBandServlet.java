package hr.fer.zemris.java.problem7;

import hr.fer.zemris.java.util.Util;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@WebServlet(name = "glasanje-xls",urlPatterns = "/glasanje-xls")
public class ExcelBandServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/octet-stream");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Disposition", "attachment; filename=\"band.xls\"");

        String resultsPath = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        String bandsPath = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");

        List<BandVotes> votes = Util.getResults(bandsPath,resultsPath);
        votes.sort(Comparator.comparingInt(BandVotes::getVotes).reversed());

        Workbook workbook = Util.createBandWorkbook(votes);

        workbook.write(resp.getOutputStream());
        workbook.close();
    }
}
