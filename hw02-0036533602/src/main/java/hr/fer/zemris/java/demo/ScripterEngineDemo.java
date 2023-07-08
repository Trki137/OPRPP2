package hr.fer.zemris.java.demo;

import hr.fer.zemris.java.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScripterEngineDemo {
    public static void main(String[] args) throws IOException {
        if(args.length != 1)
            throw new IllegalArgumentException("Expected only path as argument");

        Path path = Path.of("src/main/resources/scripterTests/"+args[0]);
        String documentBody = Files.readString(path);

        Map<String,String> parameters = new HashMap<>();
        Map<String,String> persistentParameters = new HashMap<>();
        List<RequestContext.RCCookie> cookies = new ArrayList<>();

        if(args[0].contains("calc")) {

        }

        if(args[0].contains("brojPoziva"))
            persistentParameters.put("brojPoziva", "3");

        if(args[0].contains("zbrajanje")){
            parameters.put("a", "4");
            parameters.put("b", "2");
        }
        RequestContext rc =
                new RequestContext(System.out,parameters,persistentParameters,cookies);
        new SmartScriptEngine(
                new SmartScriptParser(documentBody).getDocumentNode(),
                rc
        ).execute();
        if (args[0].contains("brojPoziva"))
            System.out.println("Vrijednost u mapi: "+rc.getPersistentParameter("brojPoziva"));
    }
}
