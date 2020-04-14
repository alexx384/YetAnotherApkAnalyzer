package extract.parse.source;

import property.SourceJavaProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SourceJavaParser {
//    private final SourceJavaProperty property;
//
//    public SourceJavaParser(SourceJavaProperty property) {
//        this.property = property;
//    }

    public static void main(String[] args) throws IOException {
        String fileContent = Files.readString(Path.of(""));
    }
}
