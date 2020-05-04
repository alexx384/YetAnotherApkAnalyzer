package extract.source;

import property.SourceApiJavaProperty;
import property.SourceCodeJavaProperty;
import property.SourceImportJavaProperty;

import java.io.IOException;
import java.nio.file.*;

public class SourcesParser {
    public static boolean parseSources(Path directoryPath, SourceImportJavaProperty importProperty,
                                       SourceApiJavaProperty apiProperty, SourceCodeJavaProperty codeProperty) {
        SourceJavaParser javaParser = new SourceJavaParser();
        SourceApiExtractor apiExtractor = new SourceApiExtractor();
        try {
            Files.walkFileTree(directoryPath, new SourceJavaFileVisitor(javaParser, apiExtractor));
        } catch (IOException e) {
            System.out.println("Error while sources parsing");
            System.err.println(e.getMessage());
            return false;
        }
        javaParser.exportInProperties(importProperty);
        apiExtractor.exportInProperties(apiProperty, codeProperty);
        return true;
    }
}
