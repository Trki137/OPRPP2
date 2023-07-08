package hr.fer.zemris.java.demo;

import hr.fer.zemris.java.scripting.nodes.WriterVisitor;
import hr.fer.zemris.java.scripting.parser.SmartScriptParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class TreeWriter {
    public static void main(String[] args) throws IOException {
        if(args.length != 1)
            throw new IllegalArgumentException("Only path was expected");

        String documentBody = Files.readString(Path.of(args[0]));
        SmartScriptParser parser = new SmartScriptParser(documentBody);
        WriterVisitor writerVisitor = new WriterVisitor();
        parser.getDocumentNode().accept(writerVisitor);
    }
}
