package hr.fer.zemris.java.hw04.servlets;

import hr.fer.zemris.java.hw04.dao.DAO;
import hr.fer.zemris.java.hw04.dao.DAOProvider;
import hr.fer.zemris.java.hw04.model.ExcelPollResultModel;
import hr.fer.zemris.java.hw04.util.Util;
import org.jfree.chart.JFreeChart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import org.jfree.chart.ChartUtils;

@WebServlet(name = "graph", urlPatterns = "/servleti/glasanje-grafika")
public class GraphServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer pollID = req.getParameter("pollID") == null ? null : Integer.parseInt(req.getParameter("pollID"));

        if(pollID == null)
            throw new RuntimeException();

        resp.setContentType("image/png");

        DAO dao = DAOProvider.getInstance();
        ExcelPollResultModel result = dao.getExcelResult(pollID);
        result.setPollOptionsModelList(result.getPollOptionsModelList().stream().filter(option -> option.getVotesCount() != 0).toList());

        OutputStream outputStream = resp.getOutputStream();

        JFreeChart pieChart = Util.createPieChart(result);

        ChartUtils.writeChartAsPNG(outputStream,pieChart,500,270);
    }
}
