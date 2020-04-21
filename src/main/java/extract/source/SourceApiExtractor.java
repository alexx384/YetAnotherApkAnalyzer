package extract.source;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithBody;
import com.github.javaparser.ast.stmt.*;
import property.SourceApiJavaProperty;

import java.util.*;

public class SourceApiExtractor {

    //    private static final Map<String, Map<String, MutableInteger>> detectSignMap = Map.ofEntries(
//            Map.entry("Intent", Map.ofEntries(
//                    Map.entry("addFlags", new MutableInteger()),
//                    Map.entry("setFlags", new MutableInteger()),
//                    Map.entry("setDataAndType", new MutableInteger()),
//                    Map.entry("putExtra", new MutableInteger())
//            ))
//    );

    private static final Set<String> detectMethodSet = Set.of(
            "toLowerCase",
            "strip",
            "setDataAndType",
            "charAt"
    );

    private final Map<String, Map<String, MutableInteger>> detectSignMap;
    private final List<Map<String, Map<String, MutableInteger>>> instanceVariableMapList;
    private final List<Map<String, Map<String, MutableInteger>>> otherVariableMapList;

    public SourceApiExtractor() {
        detectSignMap = Map.ofEntries(
                Map.entry("String", Map.ofEntries(
                        Map.entry("toLowerCase", new MutableInteger()),
                        Map.entry("strip", new MutableInteger()),
                        Map.entry("setDataAndType", new MutableInteger()),
                        Map.entry("charAt", new MutableInteger())
                ))
        );
        instanceVariableMapList = new ArrayList<>();
        otherVariableMapList = new ArrayList<>();
    }

    /**
     * returns count of method calls of the specific class
     * if the class and method are stored in {@code detectSignMap} database
     *
     * @param methodName method name to count
     * @param type method
     * @return count of calls is the record present in database, otherwise returns -1
     */
    public int getMethodCallCountOfType(String methodName, String type) {
        Map<String, MutableInteger> methodMap = detectSignMap.get(type);
        if (methodMap == null) {
            return -1;
        }
        MutableInteger integer = methodMap.get(methodName);
        if (integer == null) {
            return -1;
        }
        return integer.intValue();
    }

    /**
     * Extracts information about api calls from CompilationUnit of java file
     * The extraction is mostly based on how Jadx decompiler generates code.
     * It means, that case when:
     * 1. variables are overlapping is out of scope;
     * 2. instance variables permitted to invoke without this is out of scope.
     *
     * @param cu Compilation unit of java file
     * @return true - if the compilation unit not null and contains class or interface
     *          declaration, otherwise - false
     */
    public boolean extractInfo(CompilationUnit cu) {
        if (cu == null) {
            return false;
        }
        Optional<String> name = cu.getPrimaryTypeName();
        if (name.isEmpty()) {
            return false;
        }
        Optional<TypeDeclaration<?>> optionalTypeDeclaration = cu.getPrimaryType();
        if (optionalTypeDeclaration.isEmpty()) {
            return false;
        }
        TypeDeclaration<?> typeDeclaration = optionalTypeDeclaration.get();
        if (typeDeclaration.isClassOrInterfaceDeclaration()) {
            ClassOrInterfaceDeclaration declaration = typeDeclaration.asClassOrInterfaceDeclaration();
            if (declaration.isInterface()) {
                extractInterfaceDeclaration(declaration);
            } else {
                extractClassDeclaration(declaration);
            }
        } else if (typeDeclaration.isEnumDeclaration()) {
            EnumDeclaration declaration = typeDeclaration.asEnumDeclaration();
            extractEnumDeclaration(declaration);
        }

        return true;
    }

    /**
     * Clears counters value that parsed before
     */
    public void clearCounters() {
        for (Map<String, MutableInteger> methods : detectSignMap.values()) {
            for (MutableInteger counter : methods.values()) {
                counter.clear();
            }
        }
    }

    private void extractClassDeclaration(ClassOrInterfaceDeclaration classDeclaration) {
        instanceVariableMapList.add(new HashMap<>());
        otherVariableMapList.add(new HashMap<>());
        extractClassFields(classDeclaration.getFields());

        for (MethodDeclaration method : classDeclaration.getMethods()) {
            extractMethod(method);
        }
        instanceVariableMapList.remove(instanceVariableMapList.size() - 1);
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractInterfaceDeclaration(ClassOrInterfaceDeclaration interfaceDeclaration) {
        otherVariableMapList.add(new HashMap<>());
        for (MethodDeclaration method : interfaceDeclaration.getMethods()) {
            extractMethod(method);
        }
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractEnumDeclaration(EnumDeclaration enumDeclaration) {
        for (EnumConstantDeclaration constantDeclaration : enumDeclaration.getEntries()) {
            extractEnumConstantDeclaration(constantDeclaration);
        }
    }

    private void extractEnumConstantDeclaration(EnumConstantDeclaration constantDeclaration) {
        otherVariableMapList.add(new HashMap<>());
        for (Expression expression : constantDeclaration.getArguments()) {
            extractExpression(expression);
        }
        // TODO: implement me
//        BodyDeclaration
//        constantDeclaration.getClassBody()
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractClassFields(List<FieldDeclaration> classFields) {
        Map<String, Map<String, MutableInteger>> instanceVariableMap = instanceVariableMapList.get(
                instanceVariableMapList.size() - 1
        );
        Map<String, Map<String, MutableInteger>> classVariableMap = otherVariableMapList.get(
                otherVariableMapList.size() - 1
        );
        for (FieldDeclaration field : classFields) {

            NodeList<VariableDeclarator> variables = field.getVariables();
            if (field.isStatic()) {
                putVariablesToMap(variables, classVariableMap);
            } else {
                putVariablesToMap(variables, instanceVariableMap);
            }
        }
    }

    private void putVariablesToMap(List<VariableDeclarator> variables,
                                   Map<String, Map<String, MutableInteger>> variablesMap) {
        for (VariableDeclarator variable : variables) {

            String type = variable.getTypeAsString();
            Map<String, MutableInteger> methodsInvocationMap = detectSignMap.get(type);
            if (methodsInvocationMap != null) {
                variablesMap.put(variable.getNameAsString(), methodsInvocationMap);
            }
        }
    }

    private void extractMethod(MethodDeclaration method) {
        Optional<BlockStmt> optionalBody = method.getBody();
        if (optionalBody.isEmpty()) {
            return;
        }
        otherVariableMapList.add(new HashMap<>());
        extractParameters(method.getParameters());
        extractBody(optionalBody.get());

        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractParameters(List<Parameter> parameters) {
        Map<String, Map<String, MutableInteger>> variablesMap = otherVariableMapList.get(
                otherVariableMapList.size() - 1
        );
        for (Parameter parameter : parameters) {
            extractParameter(parameter, variablesMap);
        }
    }

    private void extractParameter(Parameter parameter, Map<String, Map<String, MutableInteger>> variablesMap) {
        String type = parameter.getTypeAsString();
        Map<String, MutableInteger> methodsInvocationMap = detectSignMap.get(type);
        if (methodsInvocationMap != null) {
            variablesMap.put(parameter.getNameAsString(), methodsInvocationMap);
        }
    }

    private void extractBody(BlockStmt body) {
        otherVariableMapList.add(new HashMap<>());
        for (Statement statement : body.getStatements()) {
            extractStatement(statement);
        }
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractStatement(Statement statement) {
        /* Out of scope: breakStatement, continueStatement, emptyStatement, unparsableStatement */
        if (statement.isExpressionStmt()) {
            extractExpression(statement.asExpressionStmt().getExpression());
        } else if (statement.isBlockStmt()) {
            extractBody(statement.asBlockStmt());
        } else if (statement.isIfStmt()) {
            extractIfStatement(statement.asIfStmt());
        } else if (statement.isForStmt()) {
            extractForStatement(statement.asForStmt());
        } else if (statement.isForEachStmt()) {
            extractForEachStatement(statement.asForEachStmt());
        } else if (statement.isDoStmt()) {
            extractDoWhileStatement(statement.asDoStmt());
        } else if (statement.isWhileStmt()) {
            extractDoWhileStatement(statement.asWhileStmt());
        } else if (statement.isTryStmt()) {
            extractTryStatement(statement.asTryStmt());
        } else if (statement.isAssertStmt()) {
            extractAssertStatement(statement.asAssertStmt());
        } else if (statement.isLabeledStmt()) {
            extractStatement(statement.asLabeledStmt().getStatement());
        } else if (statement.isReturnStmt()) {
            statement.asReturnStmt().getExpression().ifPresent(this::extractExpression);
        } else if (statement.isSwitchStmt()) {
            extractSwitchStatement(statement.asSwitchStmt());
        } else if (statement.isSynchronizedStmt()) {
            extractSynchronizedStatement(statement.asSynchronizedStmt());
        } else if (statement.isThrowStmt()) {
            extractExpression(statement.asThrowStmt().getExpression());
        } else if (statement.isExplicitConstructorInvocationStmt()) {
            extractExplicitConstructorInvocationStatement(statement.asExplicitConstructorInvocationStmt());
        } else if (statement.isLocalClassDeclarationStmt()) {
            extractClassDeclaration(statement.asLocalClassDeclarationStmt().getClassDeclaration());
        } else if (statement.isYieldStmt()) {
            extractExpression(statement.asYieldStmt().getExpression());
        }
    }

    private void extractExpression(Expression expression) {
        if (expression.isVariableDeclarationExpr()) {
            extractVariableDeclaration(expression.asVariableDeclarationExpr());
        }
        for (MethodCallExpr methodCallExpr : expression.findAll(MethodCallExpr.class)) {
            extractMethodCallExpression(methodCallExpr);
        }
    }

    private void extractVariableDeclaration(VariableDeclarationExpr variableDeclarationExpr) {
        Map<String, Map<String, MutableInteger>> variablesMap = otherVariableMapList.get(
                otherVariableMapList.size() - 1
        );
        putVariablesToMap(variableDeclarationExpr.getVariables(), variablesMap);
    }

    private void extractIfStatement(IfStmt ifStmt) {
        otherVariableMapList.add(new HashMap<>());
        extractExpression(ifStmt.getCondition());
        extractStatement(ifStmt.getThenStmt());
        ifStmt.getElseStmt().ifPresent(this::extractStatement);
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractForStatement(ForStmt forStmt) {
        otherVariableMapList.add(new HashMap<>());
        for (Expression expression : forStmt.getInitialization()) {
            extractVariableDeclaration(expression.asVariableDeclarationExpr());
        }
        extractStatement(forStmt.getBody());
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractForEachStatement(ForEachStmt forEachStmt) {
        otherVariableMapList.add(new HashMap<>());
        extractVariableDeclaration(forEachStmt.getVariable());
        extractStatement(forEachStmt.getBody());
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    @SuppressWarnings("rawtypes")
    private <T extends NodeWithBody> void extractDoWhileStatement(T stmt) {
        otherVariableMapList.add(new HashMap<>());
        extractStatement(stmt.getBody());
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractTryStatement(TryStmt tryStmt) {
        otherVariableMapList.add(new HashMap<>());
        for (Expression expression : tryStmt.getResources()) {
            extractExpression(expression);
        }
        extractBody(tryStmt.getTryBlock());
        for (CatchClause catchClause : tryStmt.getCatchClauses()) {
            extractCatchClause(catchClause);
        }
        Optional<BlockStmt> optionalFinalBlock = tryStmt.getFinallyBlock();
        optionalFinalBlock.ifPresent(this::extractBody);
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractCatchClause(CatchClause catchClause) {
        Map<String, Map<String, MutableInteger>> variablesMap = otherVariableMapList.get(
                otherVariableMapList.size() - 1
        );
        extractParameter(catchClause.getParameter(), variablesMap);
        extractBody(catchClause.getBody());
    }

    private void extractAssertStatement(AssertStmt assertStmt) {
        otherVariableMapList.add(new HashMap<>());
        extractExpression(assertStmt.getCheck());
        assertStmt.getMessage().ifPresent(this::extractExpression);
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractSwitchStatement(SwitchStmt switchStmt) {
        extractExpression(switchStmt.getSelector());
        for (SwitchEntry switchEntry : switchStmt.getEntries()) {
            otherVariableMapList.add(new HashMap<>());
            for (Statement statement : switchEntry.getStatements()) {
                extractStatement(statement);
            }
            otherVariableMapList.remove(otherVariableMapList.size() - 1);
        }
    }

    private void extractSynchronizedStatement(SynchronizedStmt synchronizedStmt) {
        otherVariableMapList.add(new HashMap<>());
        extractExpression(synchronizedStmt.getExpression());
        extractBody(synchronizedStmt.getBody());
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractExplicitConstructorInvocationStatement(ExplicitConstructorInvocationStmt statement) {
        for (Expression expression : statement.asExplicitConstructorInvocationStmt().getArguments()) {
            extractExpression(expression);
        }
    }

    private void extractMethodCallExpression(MethodCallExpr expr) {
        String methodName = expr.getNameAsString();
        if (!detectMethodSet.contains(methodName)) {
            return;
        }
        Optional<Expression> optionalMethodScopeExpression = expr.getScope();
        if (optionalMethodScopeExpression.isEmpty()) {
            return;
        }
        Expression methodScopeExpression = optionalMethodScopeExpression.get();
        if (methodScopeExpression.isNameExpr()) {

            String variableName = methodScopeExpression.asNameExpr().getNameAsString();
            parseVariableAndMethod(variableName, methodName, otherVariableMapList);
        } else if (methodScopeExpression.isFieldAccessExpr()) {

            FieldAccessExpr methodScope = methodScopeExpression.asFieldAccessExpr();
            String variableName = methodScope.getNameAsString();
            Expression variableScope = methodScope.getScope();

            // Check if variable with name variableName is instance variable
            if (variableScope.isNameExpr() && variableScope.asNameExpr().getNameAsString().equals("this")
                    && parseVariableAndMethod(variableName, methodName, instanceVariableMapList)) {
                return;
            }
            parseVariableAndMethod(variableName, methodName, otherVariableMapList);
        }
    }

    private static boolean parseVariableAndMethod(String variableName, String methodName,
                                                  List<Map<String, Map<String, MutableInteger>>> variables) {
        for (Map<String, Map<String, MutableInteger>> variable : variables) {
            Map<String, MutableInteger> objectMethods = variable.get(variableName);
            if (objectMethods == null) {
                continue;
            }
            MutableInteger counter = objectMethods.get(methodName);
            if (counter != null) {
                counter.increment();
                return true;
            }
        }
        return false;
    }

    public void exportInProperties(SourceApiJavaProperty property) {
        // TODO: implement me
        System.out.println(detectSignMap);
    }
}
