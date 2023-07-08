package hr.fer.zemris.java.problem7;

import hr.fer.zemris.java.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@WebServlet(name = "glasanje-glasaj", urlPatterns = "/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String resultsPath = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        String bandsPath = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
        List<String> fileRecords = Files.readAllLines(Path.of(bandsPath));

        List<Band> bands = new ArrayList<>();
        for (String bandString : fileRecords){
            bands.add(Util.parseBand(bandString));
        }

        Path resultPathFile = Path.of(resultsPath);

        if(!Files.exists(resultPathFile))
            Files.createFile(resultPathFile);

        List<String> results = Files.readAllLines(resultPathFile);

        int bandId = Integer.parseInt(req.getParameter("id"));
        StringBuilder sb = new StringBuilder();
        HashSet<Integer> readId = new HashSet<>();

        boolean found = false;
        for(String line: results){
            String[] split = line.split("\t");
            int id = Integer.parseInt(split[0]);
            readId.add(id);

            if(id!=bandId) {
                sb.append(line).append("\n");
                continue;
            }

            found = true;
            int numOfVotes = Integer.parseInt(split[1]);
            sb.append(id).append("\t").append(numOfVotes+1).append("\n");
        }

        if(!found) {
            sb.append(bandId).append("\t").append(1).append("\n");
            readId.add(bandId);
        }

        for(Band band : bands){
            if(readId.contains(band.getId())) continue;

            sb.append(band.getId()).append("\t0\n");
        }

        Files.writeString(resultPathFile,sb.toString(), StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);

        resp.sendRedirect(req.getContextPath()+"/glasanje-rezultati");
    }
}
