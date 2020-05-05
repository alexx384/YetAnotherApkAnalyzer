package extract.source;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;

class SourceJavaFileVisitor extends SimpleFileVisitor<Path> {
    private final SourcePropertyExtractor extractor;

    public SourceJavaFileVisitor(SourcePropertyExtractor extractor) {
        this.extractor = extractor;
    }

    public CompilationUnit parseFile(Path filePath) throws IOException {
        ParserConfiguration configuration = new ParserConfiguration();
        configuration.setAttributeComments(false);
        JavaParser parser = new JavaParser(configuration);
        Optional<CompilationUnit> optionalCU = parser.parse(filePath).getResult();
        if (optionalCU.isEmpty()) {
            System.err.println("Warning could not compile " + filePath);
            return null;
        }
        return optionalCU.get();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException
    {
        if (file.toString().endsWith(".java")) {
            CompilationUnit cu = parseFile(file);
            if (cu != null) {
                extractor.extractInfo(cu);
            }
        }
        return FileVisitResult.CONTINUE;
    }
}
