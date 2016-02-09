package io.pivotal;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TestUtilities {
    public static FileReader fixtureReader(String fixture) throws FileNotFoundException {
        return new FileReader("src/test/resources/input/" + fixture + ".json");
    }

    public static String jsonFileToString(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        Stream<String> stream = Files.lines(Paths.get(path));
        stream.forEach(x -> sb.append(x));
        stream.close();
        return sb.toString();
    }
}
