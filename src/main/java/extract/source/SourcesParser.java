package extract.source;

import property.SourceProperty;

import java.io.IOException;
import java.nio.file.*;

public class SourcesParser {
    public static boolean parseSources(Path directoryPath, SourceProperty sourceProperty) {
        SourcePropertyExtractor extractor = new SourcePropertyExtractor();
        try {
            Files.walkFileTree(directoryPath, new SourceJavaFileVisitor(extractor));
        } catch (IOException e) {
            System.out.println("Error while sources parsing");
            System.err.println(e.getMessage());
            return false;
        }
        extractor.exportInProperties(sourceProperty);
        return true;
    }
}
