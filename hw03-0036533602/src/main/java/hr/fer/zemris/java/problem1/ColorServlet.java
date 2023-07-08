package hr.fer.zemris.java.problem1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebServlet(name = "setcolor", urlPatterns = {"/setcolor"})
public class ColorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        String colorHex = req.getParameter("bgColor");
        boolean validHexColor = checkValidHex(colorHex);

        if(!validHexColor) colorHex="#000000";
        else colorHex = "#"+colorHex;

        req.getSession().setAttribute("pickedBgColor",colorHex);

        req.getRequestDispatcher("index.jsp").forward(req,resp);

    }

    private boolean checkValidHex(String colorHex) {
        if (Objects.isNull(colorHex)) return false;

        colorHex = colorHex.toLowerCase();
        for(char c: colorHex.toCharArray()){
            if(!(Character.isDigit(c) || (c >= 'a' && c<='f'))) return false;
        }

        return true;
    }
}
