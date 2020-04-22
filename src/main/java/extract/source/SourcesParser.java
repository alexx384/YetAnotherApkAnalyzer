package extract.source;

import property.SourceJavaProperty;

import java.io.IOException;
import java.nio.file.*;

public class SourcesParser {
    public static boolean parseSources(Path directoryPath, SourceJavaProperty property) {
        SourceJavaParser javaParser = new SourceJavaParser();
        SourceApiExtractor apiExtractor = new SourceApiExtractor();
        try {
            Files.walkFileTree(directoryPath, new SourceJavaFileVisitor(javaParser, apiExtractor));
        } catch (IOException e) {
            return false;
        }
        javaParser.exportInProperties(property);
        apiExtractor.exportInProperties(property);
        return true;
    }
}
