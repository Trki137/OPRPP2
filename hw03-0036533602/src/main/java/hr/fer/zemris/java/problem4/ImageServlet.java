package hr.fer.zemris.java.problem4;

import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "graph", urlPatterns = {"/reportImage"})
public class ImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/png");

        OutputStream outputStream = resp.getOutputStream();

        Map<String, Integer> osUsersMap = new HashMap<>();

        osUsersMap.put("Linux", 29);
        osUsersMap.put("Windows", 51);
        osUsersMap.put("OS", 10);

        PieChart pieChart = new PieChart("OS piechart",osUsersMap);

        ChartUtils.writeChartAsPNG(outputStream,pieChart.getChart(),500,270);
    }
}
