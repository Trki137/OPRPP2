package hr.fer.zemris.java.problem7;

import hr.fer.zemris.java.problem4.PieChart;
import hr.fer.zemris.java.util.Util;
import org.jfree.chart.ChartUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "band-chart", urlPatterns = "/glasanje-grafika")
public class BandPieChartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/png");
        String resultsPath = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        String bandsPath = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
        OutputStream outputStream = resp.getOutputStream();


        List<BandVotes> votes = Util.getResults(bandsPath,resultsPath);
        Map<String,Integer> data = new HashMap<>();

        votes.forEach(vote -> data.put(vote.getBandName(),vote.getVotes()));

        PieChart pieChart = new PieChart("Band votes",data);

        ChartUtils.writeChartAsPNG(outputStream,pieChart.getChart(),500,270);
    }
}
