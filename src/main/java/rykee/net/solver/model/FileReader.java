package rykee.net.solver.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileReader {

    private FileReader() {

    }

    public static List<String> getLines(Path path) {
        try {
            return Files.lines(path).collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Failed to load file from: " + path);
            throw new RuntimeException(e);
        }
    }

}
