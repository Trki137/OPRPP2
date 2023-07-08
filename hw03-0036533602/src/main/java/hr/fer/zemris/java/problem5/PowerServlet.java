package hr.fer.zemris.java.problem5;

import hr.fer.zemris.java.util.Util;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@WebServlet(name = "powers", urlPatterns = {"/powers"})
public class PowerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Integer a = req.getParameter("a") == null ? null :  Integer.parseInt(req.getParameter("a"));
        Integer b = req.getParameter("b") == null ? null :  Integer.parseInt(req.getParameter("b"));
        Integer n = req.getParameter("n") == null ? null :  Integer.parseInt(req.getParameter("n"));

        String errorMsg = null;

        if(Objects.isNull(a)) errorMsg = "Parameter a was not sent";
        if(Objects.isNull(b)) errorMsg = "Parameter b was not sent";
        if(Objects.isNull(n)) errorMsg = "Parameter n was not sent";

        if(!Objects.isNull(errorMsg)){
            req.setAttribute("error",errorMsg);
            req.getRequestDispatcher("WEB-INF/pages/error.jsp").forward(req,resp);
            return;
        }

        if(a < -100 || a > 100) errorMsg = "Parameter a must be in range [-100, 100]";
        if(b < -100 || b > 100) errorMsg = "Parameter b must be in range [-100, 100]";
        if(n < 1 || n > 5) errorMsg = "Parameter n must be in range [1, 5]";

        if(!Objects.isNull(errorMsg)){
            req.setAttribute("error", errorMsg);
            req.getRequestDispatcher("WEB-INF/pages/error.jsp").forward(req,resp);
            return;
        }

        resp.setContentType("application/octet-stream");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");

        Workbook workbook = Util.createTableWorkbook(a,b,n);

        workbook.write(resp.getOutputStream());
        workbook.close();



    }

}
