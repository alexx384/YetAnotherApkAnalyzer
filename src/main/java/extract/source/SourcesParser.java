package extract.source;

import property.SourceJavaProperty;

import java.io.IOException;
import java.nio.file.*;

public class SourcesParser {
    public static boolean parseSources(Path directoryPath, SourceJavaProperty property) {
        SourceJavaParser javaParser = new SourceJavaParser();
        try {
            Files.walkFileTree(directoryPath, new SourceJavaFileVisitor(javaParser));
        } catch (IOException e) {
            return false;
        }
        javaParser.exportInProperties(property);
        return true;
    }
}
