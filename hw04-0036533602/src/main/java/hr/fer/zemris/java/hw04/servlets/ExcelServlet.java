package hr.fer.zemris.java.hw04.servlets;

import hr.fer.zemris.java.hw04.dao.DAO;
import hr.fer.zemris.java.hw04.dao.DAOProvider;
import hr.fer.zemris.java.hw04.model.ExcelPollResultModel;
import hr.fer.zemris.java.hw04.util.Util;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "excel" , urlPatterns = "/servleti/glasanje-xls")
public class ExcelServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/octet-stream");
        resp.setCharacterEncoding("UTF-8");

        Integer pollID = req.getParameter("pollID") == null ? null : Integer.parseInt(req.getParameter("pollID"));

        if(pollID == null)
            throw new RuntimeException();

        DAO dao = DAOProvider.getInstance();
        ExcelPollResultModel excelResult = dao.getExcelResult(pollID);
        resp.setHeader("Content-Disposition", "attachment; filename=\""+excelResult.getPollTitle()+".xls\"");
        Workbook workbook = Util.createBandWorkbook(excelResult.getPollOptionsModelList());

        workbook.write(resp.getOutputStream());
        workbook.close();

    }
}
