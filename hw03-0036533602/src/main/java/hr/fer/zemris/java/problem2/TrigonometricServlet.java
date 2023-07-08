package hr.fer.zemris.java.problem2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "trig", urlPatterns = {"/trigonometric"})
public class TrigonometricServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String,String[]> paramMap = req.getParameterMap();

        int a = Integer.parseInt(paramMap.getOrDefault("a",new String[]{"0"})[0]);
        int b = Integer.parseInt(paramMap.getOrDefault("b",new String[]{"360"})[0]);

        if(a > b){
            int tmp = b;
            b = a;
            a = tmp;
        }

        if(b > a + 720){
            b = a + 720;
        }

        req.setAttribute("from", a);
        req.setAttribute("to", b);

        req.getRequestDispatcher("WEB-INF/pages/trigonometric.jsp").forward(req,resp);
    }
}
