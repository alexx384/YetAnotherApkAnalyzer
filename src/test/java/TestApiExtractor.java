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

class TestApiExtractor {

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
    private static JavaParser parser;

    @BeforeAll
    static void init() {
        whileStatementPath = Path.of(TestApiExtractor.class.getResource("WhileStatement.java").getFile());
        doStatementPath = Path.of(TestApiExtractor.class.getResource("DoStatement.java").getFile());
        ifStatementPath = Path.of(TestApiExtractor.class.getResource("IfStatement.java").getFile());
        assertStatementPath = Path.of(TestApiExtractor.class.getResource("AssertStatement.java").getFile());
        switchStatementPath = Path.of(TestApiExtractor.class.getResource("SwitchStatement.java").getFile());
        enumStatementPath = Path.of(TestApiExtractor.class.getResource("EnumStatement.java").getFile());
        innerClassStatementPath = Path.of(TestApiExtractor.class.getResource("InnerClassStatement.java").
                getFile());
        blockStatementPath = Path.of(TestApiExtractor.class.getResource("BlockStatement.java").getFile());
        lambdaExpressionPath = Path.of(TestApiExtractor.class.getResource("LambdaExpression.java").
                getFile());
        ternaryExpressionPath = Path.of(TestApiExtractor.class.getResource("TernaryExpression.java").getFile());
        annotationMethodPath = Path.of(TestApiExtractor.class.getResource("AnnotationMethod.java").
                getFile());
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
        assertEquals(1, extractor.getMethodCallCountOfType("charAt", "String"));
    }

    @Test
    void testDoStatement() {
        CompilationUnit cu = getCuFromPath(doStatementPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(1, extractor.getMethodCallCountOfType("charAt", "String"));
    }

    @Test
    void testIfStatement() {
        CompilationUnit cu = getCuFromPath(ifStatementPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(3, extractor.getMethodCallCountOfType("charAt", "String"));
    }

    @Test
    void testAssertStatement() {
        CompilationUnit cu = getCuFromPath(assertStatementPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(1, extractor.getMethodCallCountOfType("charAt", "String"));
    }

    @Test
    void testSwitchStatement() {
        CompilationUnit cu = getCuFromPath(switchStatementPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(4, extractor.getMethodCallCountOfType("charAt", "String"));
    }

    @Test
    void testEnumStatement() {
        CompilationUnit cu = getCuFromPath(enumStatementPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(3, extractor.getMethodCallCountOfType("toLowerCase", "String"));
    }

    @Test
    void testInnerClassStatement() {
        CompilationUnit cu = getCuFromPath(innerClassStatementPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(1, extractor.getMethodCallCountOfType("toLowerCase", "String"));
    }

    @Test
    void testBlockStatement() {
        CompilationUnit cu = getCuFromPath(blockStatementPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(2, extractor.getMethodCallCountOfType("toLowerCase", "String"));
    }

    @Test
    void testLambdaExpression() {
        CompilationUnit cu = getCuFromPath(lambdaExpressionPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(1, extractor.getMethodCallCountOfType("toLowerCase", "String"));
    }

    @Test
    void testTernaryExpression() {
        CompilationUnit cu = getCuFromPath(ternaryExpressionPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(1, extractor.getMethodCallCountOfType("toLowerCase", "String"));
    }

    @Test
    void testAnnotationMethod() {
        CompilationUnit cu = getCuFromPath(annotationMethodPath);
        assertNotNull(cu);
        extractor.extractInfo(cu);
        assertEquals(1, extractor.getMethodCallCountOfType("toLowerCase", "String"));
    }
}
