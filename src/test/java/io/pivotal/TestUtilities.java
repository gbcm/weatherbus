package io.pivotal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by pivotal on 1/6/16.
 */
public class TestUtilities {
    public static String jsonFileToString(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        Stream<String> stream = Files.lines(Paths.get(path));
        stream.forEach(x -> sb.append(x));
        stream.close();
        return sb.toString();
    }
}
