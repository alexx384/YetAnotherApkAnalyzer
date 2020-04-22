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
    private final SourceJavaParser javaParser;
    private final SourceApiExtractor apiExtractor;

    public SourceJavaFileVisitor(SourceJavaParser javaParser, SourceApiExtractor apiExtractor) {
        this.javaParser = javaParser;
        this.apiExtractor = apiExtractor;
    }

    public CompilationUnit parseFile(Path filePath) throws IOException {
        ParserConfiguration configuration = new ParserConfiguration();
        configuration.setAttributeComments(false);
        JavaParser parser = new JavaParser(configuration);
        Optional<CompilationUnit> optionalCU = parser.parse(filePath).getResult();
        if (optionalCU.isEmpty()) {
            System.out.println("empty");
            throw new NullPointerException("Compilation unit is null");
        }
        return optionalCU.get();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException
    {
        if (file.toString().endsWith(".java")) {
            CompilationUnit cu = parseFile(file);
            javaParser.extractInfo(cu);
            apiExtractor.extractInfo(cu);
        }
        return FileVisitResult.CONTINUE;
    }
}
