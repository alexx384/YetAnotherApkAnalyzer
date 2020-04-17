package extract.source;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

class SourceJavaFileVisitor extends SimpleFileVisitor<Path> {
    private final SourceJavaParser javaParser;

    public SourceJavaFileVisitor(SourceJavaParser javaParser) {
        this.javaParser = javaParser;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException
    {
        if (file.toString().endsWith(".java")) {
            javaParser.parseFile(file);
        }
        return FileVisitResult.CONTINUE;
    }
}
