package extract.source;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import property.SourceProperty;

import java.io.InputStream;
import java.util.Optional;

public class SourcesParser {
    public static boolean parseSources(String zipFileName, SourceProperty sourceProperty) {
        ParserConfiguration configuration = new ParserConfiguration();
        configuration.setAttributeComments(false);
        JavaParser parser = new JavaParser(configuration);
        SourcePropertyExtractor extractor = new SourcePropertyExtractor();

        if (!ZipFileVisitor.walkZipFileTree(zipFileName, (InputStream is) -> {
            Optional<CompilationUnit> cu = parser.parse(is).getResult();
            if (cu.isPresent()) {
                extractor.extractInfo(cu.get());
                return true;
            } else {
                return false;
            }
        })) {
            System.err.println("Error while sources parsing");
            return false;
        }

        extractor.exportInProperties(sourceProperty);
        return true;
    }
}
