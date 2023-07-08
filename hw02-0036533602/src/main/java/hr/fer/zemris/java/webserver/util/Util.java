package hr.fer.zemris.java.webserver.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Util class for my project
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public class Util {

    /**
     * Extracts header from request
     *
     * @param request request
     * @return all header lines in list
     */

    public static List<String> extractHeader(String request) {
        List<String> header = new ArrayList<>();
        String currentLine = "";
        for(String line : request.split("\n")){
            if(line.isEmpty()) break;

            char spaceOrTab = line.charAt(0);
            if(spaceOrTab == 9 || spaceOrTab == 32){
                currentLine += line;
            }else {
                if(!currentLine.isEmpty()){
                    header.add(currentLine);
                }
                currentLine = line;
            }

        }

        if(!currentLine.isEmpty()) {
            header.add(currentLine);
        }

        return header;
    }

    /**
     * Reads request
     *
     * @param istream from which we read
     * @return String that represent request
     * @throws IOException if something goes wrong with reading data
     */

    public static String readRequest(InputStream istream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int state = 0;
        l:
        while(true) {
            int b = istream.read();
            if(b==-1) {
                if(bos.size()!=0) {
                    throw new IOException("Incomplete header received.");
                }
                return null;
            }
            if(b!=13) {
                bos.write(b);
            }
            switch (state) {
                case 0 -> {
                    if (b == 13) {
                        state = 1;
                    } else if (b == 10) state = 4;
                }
                case 1 -> {
                    if (b == 10) {
                        state = 2;
                    } else state = 0;
                }
                case 2 -> {
                    if (b == 13) {
                        state = 3;
                    } else state = 0;
                }
                case 3, 4 -> {
                    if (b == 10) {
                        break l;
                    } else state = 0;
                }
            }
        }

        return bos.toString(StandardCharsets.US_ASCII);
    }


}
