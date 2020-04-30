import static org.junit.jupiter.api.Assertions.*;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import extract.source.SourceApiExtractor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

class TestSourceApiExtractor {

    private static final SourceApiExtractor extractor = new SourceApiExtractor();
    private static Path whileStatementPath;
    private static Path doStatementPath;
    private static Path ifStatementPath;
    private static Path assertStatementPath;
    private static Path switchStatementPath;
    private static Path enumStatementPath;
    private static Path innerClassStatementPath;
    private static Path blockStatementPath;
    private static Path lambdaExpressionPath;
    private static Path ternaryExpressionPath;
    private static Path annotationMethodPath;
    private static Path constructorDeclarationPath;
    private static JavaParser parser;

    @BeforeAll
    static void init() {
        whileStatementPath = Path.of(TestSourceApiExtractor.class.getResource("WhileStatement.java").getFile());
        doStatementPath = Path.of(TestSourceApiExtractor.class.getResource("DoStatement.java").getFile());
        ifStatementPath = Path.of(TestSourceApiExtractor.class.getResource("IfStatement.java").getFile());
        assertStatementPath = Path.of(TestSourceApiExtractor.class.getResource("AssertStatement.java").getFile());
        switchStatementPath = Path.of(TestSourceApiExtractor.class.getResource("SwitchStatement.java").getFile());
        enumStatementPath = Path.of(TestSourceApiExtractor.class.getResource("EnumStatement.java").getFile());
        innerClassStatementPath = Path.of(TestSourceApiExtractor.class.getResource("InnerClassStatement.java")
                .getFile());
        blockStatementPath = Path.of(TestSourceApiExtractor.class.getResource("BlockStatement.java").getFile());
        lambdaExpressionPath = Path.of(TestSourceApiExtractor.class.getResource("LambdaExpression.java")
                .getFile());
        ternaryExpressionPath = Path.of(TestSourceApiExtractor.class.getResource("TernaryExpression.java").getFile());
        annotationMethodPath = Path.of(TestSourceApiExtractor.class.getResource("AnnotationMethod.java")
                .getFile());
        constructorDeclarationPath = Path.of(TestSourceApiExtractor.class.getResource("ConstructorDeclaration.java")
                .getFile());
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setAttributeComments(false);
        parser = new JavaParser(parserConfiguration);
    }

    @BeforeEach
    void prepare() {
        extractor.clearCounters();
    }

    private static CompilationUnit getCuFromPath(Path javaFilePath) {
        try {
            Optional<CompilationUnit> optionalCU = parser.parse(javaFilePath).getResult();
            if (optionalCU.isEmpty()) {
                return null;
            } else {
                return optionalCU.get();
            }
        } catch (IOException e) {
            fail();
            return null;
        }
    }

    @Test
    void testWhileStatement() {
        CompilationUnit cu = getCuFromPath(whileStatementPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(1, extractor.getMethodCallCountOfType("String", "charAt"));
    }

    @Test
    void testDoStatement() {
        CompilationUnit cu = getCuFromPath(doStatementPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(1, extractor.getMethodCallCountOfType("String", "charAt"));
    }

    @Test
    void testIfStatement() {
        CompilationUnit cu = getCuFromPath(ifStatementPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(3, extractor.getMethodCallCountOfType("String", "charAt"));
    }

    @Test
    void testAssertStatement() {
        CompilationUnit cu = getCuFromPath(assertStatementPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(1, extractor.getMethodCallCountOfType("String", "charAt"));
    }

    @Test
    void testSwitchStatement() {
        CompilationUnit cu = getCuFromPath(switchStatementPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(4, extractor.getMethodCallCountOfType("String", "charAt"));
    }

    @Test
    void testEnumStatement() {
        CompilationUnit cu = getCuFromPath(enumStatementPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(3, extractor.getMethodCallCountOfType("String", "toLowerCase"));
    }

    @Test
    void testInnerClassStatement() {
        CompilationUnit cu = getCuFromPath(innerClassStatementPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(1, extractor.getMethodCallCountOfType("String", "toLowerCase"));
    }

    @Test
    void testBlockStatement() {
        CompilationUnit cu = getCuFromPath(blockStatementPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(2, extractor.getMethodCallCountOfType("String", "toLowerCase"));
    }

    @Test
    void testLambdaExpression() {
        CompilationUnit cu = getCuFromPath(lambdaExpressionPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(1, extractor.getMethodCallCountOfType("String", "toLowerCase"));
    }

    @Test
    void testTernaryExpression() {
        CompilationUnit cu = getCuFromPath(ternaryExpressionPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(1, extractor.getMethodCallCountOfType("String", "toLowerCase"));
    }

    @Test
    void testAnnotationMethod() {
        CompilationUnit cu = getCuFromPath(annotationMethodPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(1, extractor.getMethodCallCountOfType("String", "toLowerCase"));
    }

    @Test
    void testConstructorDeclaration() {
        CompilationUnit cu = getCuFromPath(constructorDeclarationPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(1, extractor.getConstructorCallCountOfType("String"));
    }
}
