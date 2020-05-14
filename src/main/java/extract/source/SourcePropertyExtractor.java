package extract.source;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithBody;
import com.github.javaparser.ast.stmt.*;
import property.*;

import java.util.*;

public class SourcePropertyExtractor {
    private static final Map<String, Integer> importMap;
    private static final Map<String, Integer> constructorMap;
    private static final Map<String, Map<String, Integer>> classMethodMap;
    private static final Set<String> methodNameSet;
    private static final int TOTAL_METHOD_PROPERTIES;
    private static final int TOTAL_IMPORT_PROPERTIES;
    private static final int TOTAL_CONSTRUCTOR_PROPERTIES;

    static {
        importMap = new HashMap<>();
        TOTAL_IMPORT_PROPERTIES = SourcePropertiesLoader.loadImportProperties(importMap);

        constructorMap = new HashMap<>();
        TOTAL_CONSTRUCTOR_PROPERTIES = SourcePropertiesLoader.loadConstructorProperties(constructorMap);

        classMethodMap = new HashMap<>();
        methodNameSet = new HashSet<>();
        TOTAL_METHOD_PROPERTIES = SourcePropertiesLoader.loadMethodProperties(classMethodMap, methodNameSet);
    }

    private final List<Map<String, Map<String, Integer>>> instanceVariableMapList;
    private final List<Map<String, Map<String, Integer>>> otherVariableMapList;

    private int[] importProperties;
    private int[] codeProperties;
    private int[] constructorProperties;
    private int[] methodProperties;

    public SourcePropertyExtractor() {
        importProperties = new int[TOTAL_IMPORT_PROPERTIES];
        codeProperties = new int[SourceCodeProperties.length];
        constructorProperties = new int[TOTAL_CONSTRUCTOR_PROPERTIES];
        methodProperties = new int[TOTAL_METHOD_PROPERTIES];

        instanceVariableMapList = new ArrayList<>();
        otherVariableMapList = new ArrayList<>();
    }

    /**
     * Returns count of method calls of the specific type
     *
     * @param type   class name
     * @param method method name to count
     * @return count of calls is we are track them, otherwise returns -1
     */
    public int getMethodCallCountOfType(String type, String method) {
        Map<String, Integer> methodNameMap = classMethodMap.get(type);
        if (methodNameMap == null) {
            return -1;
        }
        Integer counterIdx = methodNameMap.get(method);
        if (counterIdx == null) {
            return -1;
        }
        return methodProperties[counterIdx];
    }

    /**
     * Returns count of constructor calls of the specific type
     *
     * @param type class name
     * @return count of calls is we are track them, otherwise returns -1
     */
    public int getConstructorCallCountOfType(String type) {
        Integer counterIdx = constructorMap.get(type);
        if (counterIdx == null) {
            return -1;
        }
        return constructorProperties[counterIdx];
    }

    /**
     * Extracts information about api calls from CompilationUnit of java file
     * The extraction is mostly based on how Jadx decompiler generates code.
     * It means, that case when:
     * 1. variables are overlapping is out of scope;
     * 2. instance variables permitted to invoke without this is out of scope.
     *
     * @param cu Compilation unit of java file
     */
    public void extractInfo(CompilationUnit cu) {
        if (cu == null) {
            return;
        }
        processImports(cu.getImports());
        for (TypeDeclaration<?> typeDeclaration : cu.getTypes()) {
            if (typeDeclaration.isClassOrInterfaceDeclaration()) {
                extractClassOrInterfaceDeclaration(typeDeclaration.asClassOrInterfaceDeclaration());
            } else if (typeDeclaration.isEnumDeclaration()) {
                extractEnumDeclaration(typeDeclaration.asEnumDeclaration());
            }
        }
    }

    /**
     * Clears counters value that parsed before
     */
    public void clearCounters() {
        importProperties = new int[TOTAL_IMPORT_PROPERTIES];
        codeProperties = new int[SourceCodeProperties.length];
        constructorProperties = new int[TOTAL_CONSTRUCTOR_PROPERTIES];
        methodProperties = new int[TOTAL_METHOD_PROPERTIES];
    }

    /**
     * Export parameters in property object
     *
     * @param property object which receives properties
     */
    public void exportInProperties(SourceProperty property) {
        property.setImportProperties(importProperties);
        property.setCodeProperties(codeProperties);
        property.setConstructorProperties(constructorProperties);
        property.setMethodProperties(methodProperties);
    }

    private void processImports(List<ImportDeclaration> importDeclarations) {
        for (ImportDeclaration importDeclaration : importDeclarations) {
            Integer counterIdx = importMap.get(importDeclaration.getNameAsString());
            if (counterIdx != null) {
                importProperties[counterIdx] += 1;
            }
        }
    }

    private void extractClassOrInterfaceDeclaration(ClassOrInterfaceDeclaration declaration) {
        if (declaration.isInterface()) {
            extractInterfaceDeclaration(declaration);
        } else {
            extractClassDeclaration(declaration);
        }
    }

    private void extractEnumDeclaration(EnumDeclaration enumDeclaration) {
        incremenetCodeProperty(SourceCodeProperties.ENUM_COUNTER);
        instanceVariableMapList.add(new HashMap<>());
        otherVariableMapList.add(new HashMap<>());
        for (BodyDeclaration<?> declaration : enumDeclaration.getMembers()) {
            extractBodyDeclaration(declaration);
        }
        for (EnumConstantDeclaration constantDeclaration : enumDeclaration.getEntries()) {
            extractEnumConstantDeclaration(constantDeclaration);
        }
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
        instanceVariableMapList.remove(instanceVariableMapList.size() - 1);
    }

    private void extractInterfaceDeclaration(ClassOrInterfaceDeclaration interfaceDeclaration) {
        incremenetCodeProperty(SourceCodeProperties.INTERFACE_COUNTER);
        instanceVariableMapList.add(new HashMap<>());
        otherVariableMapList.add(new HashMap<>());
        for (BodyDeclaration<?> declaration : interfaceDeclaration.getMembers()) {
            extractBodyDeclaration(declaration);
        }
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
        instanceVariableMapList.remove(instanceVariableMapList.size() - 1);
    }

    private void extractClassDeclaration(ClassOrInterfaceDeclaration classDeclaration) {
        incremenetCodeProperty(SourceCodeProperties.CLASS_COUNTER);
        instanceVariableMapList.add(new HashMap<>());
        otherVariableMapList.add(new HashMap<>());
        for (BodyDeclaration<?> declaration : classDeclaration.getMembers()) {
            extractBodyDeclaration(declaration);
        }
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
        instanceVariableMapList.remove(instanceVariableMapList.size() - 1);
    }

    private void extractBodyDeclaration(BodyDeclaration<?> bodyDeclaration) {
        incremenetCodeProperty(SourceCodeProperties.BODY_DECLARATION_COUNTER);
        if (bodyDeclaration.isMethodDeclaration()) {
            extractMethod(bodyDeclaration.asMethodDeclaration());
        } else if (bodyDeclaration.isClassOrInterfaceDeclaration()) {
            extractClassOrInterfaceDeclaration(bodyDeclaration.asClassOrInterfaceDeclaration());
        } else if (bodyDeclaration.isEnumDeclaration()) {
            extractEnumDeclaration(bodyDeclaration.asEnumDeclaration());
        } else if (bodyDeclaration.isConstructorDeclaration()) {
            incremenetCodeProperty(SourceCodeProperties.CONSTRUCTOR_DECLARATION_COUNTER);
            extractBody(bodyDeclaration.asConstructorDeclaration().getBody());
        } else if (bodyDeclaration.isFieldDeclaration()) {
            extractClassField(bodyDeclaration.asFieldDeclaration());
        } else if (bodyDeclaration.isInitializerDeclaration()) {
            incremenetCodeProperty(SourceCodeProperties.INITIALIZED_DECLARATION_COUNTER);
            extractBody(bodyDeclaration.asInitializerDeclaration().getBody());
        }
    }

    private void extractEnumConstantDeclaration(EnumConstantDeclaration constantDeclaration) {
        incremenetCodeProperty(SourceCodeProperties.ENUM_CONSTANTS_COUNTER);
        otherVariableMapList.add(new HashMap<>());
        for (BodyDeclaration<?> bodyDeclaration : constantDeclaration.getClassBody()) {
            if (bodyDeclaration.isMethodDeclaration()) {
                extractMethod(bodyDeclaration.asMethodDeclaration());
            }
        }
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractMethod(MethodDeclaration method) {
        incremenetCodeProperty(SourceCodeProperties.METHOD_COUNTER);
        Optional<BlockStmt> optionalBody = method.getBody();
        if (optionalBody.isEmpty()) {
            return;
        }
        otherVariableMapList.add(new HashMap<>());
        extractParameters(method.getParameters());
        extractBody(optionalBody.get());

        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractBody(BlockStmt body) {
        incremenetCodeProperty(SourceCodeProperties.BODY_COUNTER);
        otherVariableMapList.add(new HashMap<>());
        for (Statement statement : body.getStatements()) {
            extractStatement(statement);
        }
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractClassField(FieldDeclaration fieldDeclaration) {
        incremenetCodeProperty(SourceCodeProperties.CLASS_FIELD_COUNTER);
        NodeList<VariableDeclarator> variables = fieldDeclaration.getVariables();
        if (fieldDeclaration.isStatic()) {
            Map<String, Map<String, Integer>> classVariableMap = otherVariableMapList.get(
                    otherVariableMapList.size() - 1
            );
            putVariablesToMap(variables, classVariableMap);
        } else {
            Map<String, Map<String, Integer>> instanceVariableMap = instanceVariableMapList.get(
                    instanceVariableMapList.size() - 1
            );
            putVariablesToMap(variables, instanceVariableMap);
        }
    }

    private void extractParameters(List<Parameter> parameters) {
        incremenetCodeProperty(SourceCodeProperties.PARAMETER_COUNTER);
        Map<String, Map<String, Integer>> variablesMap = otherVariableMapList.get(
                otherVariableMapList.size() - 1
        );
        for (Parameter parameter : parameters) {
            extractParameter(parameter, variablesMap);
        }
    }

    private void extractStatement(Statement statement) {
        incremenetCodeProperty(SourceCodeProperties.STATEMENT_COUNTER);
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
            incremenetCodeProperty(SourceCodeProperties.LABELED_STATEMENT_COUNTER);
            extractStatement(statement.asLabeledStmt().getStatement());
        } else if (statement.isReturnStmt()) {
            incremenetCodeProperty(SourceCodeProperties.RETURN_STATEMENT_COUNTER);
            statement.asReturnStmt().getExpression().ifPresent(this::extractExpression);
        } else if (statement.isSwitchStmt()) {
            extractSwitchStatement(statement.asSwitchStmt());
        } else if (statement.isSynchronizedStmt()) {
            extractSynchronizedStatement(statement.asSynchronizedStmt());
        } else if (statement.isThrowStmt()) {
            incremenetCodeProperty(SourceCodeProperties.THROWN_STATEMENT_COUNTER);
            extractExpression(statement.asThrowStmt().getExpression());
        } else if (statement.isExplicitConstructorInvocationStmt()) {
            extractExplicitConstructorInvocationStatement(statement.asExplicitConstructorInvocationStmt());
        } else if (statement.isLocalClassDeclarationStmt()) {
            incremenetCodeProperty(SourceCodeProperties.LOCAL_CLASS_DECLARATION_COUNTER);
            extractClassDeclaration(statement.asLocalClassDeclarationStmt().getClassDeclaration());
        } else if (statement.isYieldStmt()) {
            incremenetCodeProperty(SourceCodeProperties.YIELD_STATEMENT_COUNTER);
            extractExpression(statement.asYieldStmt().getExpression());
        }
    }

    private void putVariablesToMap(List<VariableDeclarator> variables,
                                   Map<String, Map<String, Integer>> variablesMap) {
        for (VariableDeclarator variable : variables) {

            String type = variable.getTypeAsString();
            Map<String, Integer> methodsNameMap = classMethodMap.get(type);
            if (methodsNameMap != null) {
                variablesMap.put(variable.getNameAsString(), methodsNameMap);
            }
            variable.getInitializer().ifPresent(this::extractExpression);
        }
    }

    private void extractParameter(Parameter parameter, Map<String, Map<String, Integer>> variablesMap) {
        incremenetCodeProperty(SourceCodeProperties.PARAMETER_COUNTER);
        String type = parameter.getTypeAsString();
        Map<String, Integer> methodsNameMap = classMethodMap.get(type);
        if (methodsNameMap != null) {
            variablesMap.put(parameter.getNameAsString(), methodsNameMap);
        }
    }

    private void extractExpression(Expression expression) {
        incremenetCodeProperty(SourceCodeProperties.EXPRESSION_COUNTER);
        /* Out of scope: AnnotationExpr, NameExpr, UnaryExpr, ThisExpr, ArrayInitializerExpr, BooleanLiteralExpr,
            CharLiteralExpr, ClassExpr, DoubleLiteralExpr, InstanceOfExpr, IntegerLiteralExpr, LiteralExpr,
            LiteralStringValueExpr, LongLiteralExpr, MarkerAnnotationExpr, MethodReferenceExpr, NormalAnnotationExpr,
            NullLiteralExpr, SingleMemberAnnotationExpr, StringLiteralExpr, SuperExpr, SwitchExpr, TextBlockLiteralExpr,
            TypeExpr */
        if (expression.isVariableDeclarationExpr()) {
            extractVariableDeclaration(expression.asVariableDeclarationExpr());
        } else if (expression.isLambdaExpr()) {
            extractLambdaExpression(expression.asLambdaExpr());
        } else if (expression.isObjectCreationExpr()) {
            extractObjectCreationExpression(expression.asObjectCreationExpr());
        } else if (expression.isFieldAccessExpr()) {
            extractFieldAccessExpression(expression.asFieldAccessExpr());
        } else if (expression.isMethodCallExpr()) {
            extractMethodCallExpression(expression.asMethodCallExpr());
        } else if (expression.isArrayAccessExpr()) {
            extractArrayAccessExpression(expression.asArrayAccessExpr());
        } else if (expression.isArrayCreationExpr()) {
            extractArrayCreationExpression(expression.asArrayCreationExpr());
        } else if (expression.isAssignExpr()) {
            extractAssignExpression(expression.asAssignExpr());
        } else if (expression.isBinaryExpr()) {
            extractBinaryExpression(expression.asBinaryExpr());
        } else if (expression.isCastExpr()) {
            incremenetCodeProperty(SourceCodeProperties.CAST_EXPRESSION_COUNTER);
            extractExpression(expression.asCastExpr().getExpression());
        } else if (expression.isConditionalExpr()) {
            extractConditionalExpression(expression.asConditionalExpr());
        } else if (expression.isEnclosedExpr()) {
            incremenetCodeProperty(SourceCodeProperties.ENCLOSED_EXPRESSION_COUNTER);
            extractExpression(expression.asEnclosedExpr().getInner());
        } else if (expression.isUnaryExpr()) {
            incremenetCodeProperty(SourceCodeProperties.UNARY_EXPRESSION_COUNTER);
            extractExpression(expression.asUnaryExpr().getExpression());
        }
    }

    private void extractIfStatement(IfStmt ifStmt) {
        incremenetCodeProperty(SourceCodeProperties.IF_STATEMENT_COUNTER);
        otherVariableMapList.add(new HashMap<>());
        extractExpression(ifStmt.getCondition());
        extractStatement(ifStmt.getThenStmt());
        ifStmt.getElseStmt().ifPresent(this::extractStatement);
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractForStatement(ForStmt forStmt) {
        incremenetCodeProperty(SourceCodeProperties.FOR_STATEMENT_COUNTER);
        otherVariableMapList.add(new HashMap<>());
        for (Expression expression : forStmt.getInitialization()) {
            if (expression.isVariableDeclarationExpr()) {
                extractVariableDeclaration(expression.asVariableDeclarationExpr());
            }
        }
        extractStatement(forStmt.getBody());
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractForEachStatement(ForEachStmt forEachStmt) {
        incremenetCodeProperty(SourceCodeProperties.FOR_EACH_STATEMENT_COUNTER);
        otherVariableMapList.add(new HashMap<>());
        extractVariableDeclaration(forEachStmt.getVariable());
        extractStatement(forEachStmt.getBody());
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    @SuppressWarnings("rawtypes")
    private <T extends NodeWithBody> void extractDoWhileStatement(T stmt) {
        incremenetCodeProperty(SourceCodeProperties.DO_WHILE_STATEMENT_COUNTER);
        otherVariableMapList.add(new HashMap<>());
        extractStatement(stmt.getBody());
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractTryStatement(TryStmt tryStmt) {
        incremenetCodeProperty(SourceCodeProperties.TRY_STATEMENT_COUNTER);
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

    private void extractAssertStatement(AssertStmt assertStmt) {
        incremenetCodeProperty(SourceCodeProperties.ASSERT_STATEMENT_COUNTER);
        otherVariableMapList.add(new HashMap<>());
        extractExpression(assertStmt.getCheck());
        assertStmt.getMessage().ifPresent(this::extractExpression);
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractSwitchStatement(SwitchStmt switchStmt) {
        incremenetCodeProperty(SourceCodeProperties.SWITCH_STATEMENT_COUNTER);
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
        incremenetCodeProperty(SourceCodeProperties.SYNCHRONIZED_STATEMENT_COUNTER);
        otherVariableMapList.add(new HashMap<>());
        extractExpression(synchronizedStmt.getExpression());
        extractBody(synchronizedStmt.getBody());
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractExplicitConstructorInvocationStatement(ExplicitConstructorInvocationStmt statement) {
        incremenetCodeProperty(SourceCodeProperties.CONSTRUCTOR_INVOCATION_STATEMENT_COUNTER);
        for (Expression expression : statement.asExplicitConstructorInvocationStmt().getArguments()) {
            extractExpression(expression);
        }
    }

    private void extractVariableDeclaration(VariableDeclarationExpr variableDeclarationExpr) {
        incremenetCodeProperty(SourceCodeProperties.VARIABLE_STATEMENT_COUNTER);
        Map<String, Map<String, Integer>> variablesMap = otherVariableMapList.get(
                otherVariableMapList.size() - 1
        );
        putVariablesToMap(variableDeclarationExpr.getVariables(), variablesMap);
    }

    private void extractLambdaExpression(LambdaExpr lambdaExpression) {
        incremenetCodeProperty(SourceCodeProperties.LAMBDA_EXPRESSION_COUNTER);
        otherVariableMapList.add(new HashMap<>());
        extractParameters(lambdaExpression.getParameters());
        Statement body = lambdaExpression.getBody();
        if (body.isExpressionStmt()) {
            extractExpression(body.asExpressionStmt().getExpression());
        } else {
            extractBody(body.asBlockStmt());
        }
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractObjectCreationExpression(ObjectCreationExpr expr) {
        incremenetCodeProperty(SourceCodeProperties.OBJECT_CREATION_EXPRESSION_COUNTER);
        for (Expression argument : expr.getArguments()) {
            extractExpression(argument);
        }
        Optional<NodeList<BodyDeclaration<?>>> optionalBodyDeclarations = expr.getAnonymousClassBody();
        if (optionalBodyDeclarations.isPresent()) {
            instanceVariableMapList.add(new HashMap<>());
            otherVariableMapList.add(new HashMap<>());
            for (BodyDeclaration<?> declaration : optionalBodyDeclarations.get()) {
                extractBodyDeclaration(declaration);
            }
            otherVariableMapList.remove(otherVariableMapList.size() - 1);
            instanceVariableMapList.remove(instanceVariableMapList.size() - 1);
        }
        expr.getScope().ifPresent(this::extractExpression);
        String type = expr.getTypeAsString();
        Integer counterIdx = constructorMap.get(type);
        if (counterIdx != null) {
            constructorProperties[counterIdx] += 1;
        }
    }

    private void extractFieldAccessExpression(FieldAccessExpr fieldAccessExpr) {
        incremenetCodeProperty(SourceCodeProperties.FIELD_ACCESS_EXPRESSION_COUNTER);
        extractExpression(fieldAccessExpr.getScope());
    }

    private void extractMethodCallExpression(MethodCallExpr expr) {
        incremenetCodeProperty(SourceCodeProperties.METHOD_CALL_EXPRESSION_COUNTER);
        String methodName = expr.getNameAsString();
        for (Expression argument : expr.getArguments()) {
            extractExpression(argument);
        }
        expr.getScope().ifPresent(this::extractExpression);
        if (!methodNameSet.contains(methodName)) {
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

    private void extractArrayAccessExpression(ArrayAccessExpr arrayAccessExpr) {
        incremenetCodeProperty(SourceCodeProperties.ARRAY_ACCESS_EXPRESSION_COUNTER);
        extractExpression(arrayAccessExpr.getName());
        extractExpression(arrayAccessExpr.getIndex());
    }

    private void extractArrayCreationExpression(ArrayCreationExpr arrayCreationExpr) {
        incremenetCodeProperty(SourceCodeProperties.ARRAY_CREATION_EXPRESSION_COUNTER);
        Optional<ArrayInitializerExpr> optionalArrayInitializerExpr = arrayCreationExpr.getInitializer();
        if (optionalArrayInitializerExpr.isEmpty()) {
            return;
        }
        String type = arrayCreationExpr.getElementType().asString();
        Integer counterIdx = constructorMap.get(type);
        if (counterIdx != null) {
            constructorProperties[counterIdx] += getCountInitializedObjects(optionalArrayInitializerExpr.get());
        }
    }

    private void extractAssignExpression(AssignExpr assignExpr) {
        incremenetCodeProperty(SourceCodeProperties.ASSIGN_EXPRESSION_COUNTER);
        extractExpression(assignExpr.getTarget());
        extractExpression(assignExpr.getValue());
    }

    private void extractBinaryExpression(BinaryExpr binaryExpr) {
        incremenetCodeProperty(SourceCodeProperties.BINARY_EXPRESSION_COUNTER);
        extractExpression(binaryExpr.getLeft());
        extractExpression(binaryExpr.getRight());
    }

    private void extractConditionalExpression(ConditionalExpr conditionalExpr) {
        incremenetCodeProperty(SourceCodeProperties.CONDITIONAL_EXPRESSION_COUNTER);
        extractExpression(conditionalExpr.getCondition());
        extractExpression(conditionalExpr.getElseExpr());
        extractExpression(conditionalExpr.getThenExpr());
    }

    private void extractCatchClause(CatchClause catchClause) {
        incremenetCodeProperty(SourceCodeProperties.CATCH_EXPRESSION_COUNTER);
        Map<String, Map<String, Integer>> variablesMap = otherVariableMapList.get(
                otherVariableMapList.size() - 1
        );
        extractParameter(catchClause.getParameter(), variablesMap);
        extractBody(catchClause.getBody());
    }

    private boolean parseVariableAndMethod(String variableName, String methodName,
                                                  List<Map<String, Map<String, Integer>>> variables) {
        for (Map<String, Map<String, Integer>> variable : variables) {
            Map<String, Integer> objectMethods = variable.get(variableName);
            if (objectMethods == null) {
                continue;
            }
            Integer counterIdx = objectMethods.get(methodName);
            if (counterIdx != null) {
                methodProperties[counterIdx] += 1;
                return true;
            }
        }
        return false;
    }

    private int getCountInitializedObjects(ArrayInitializerExpr arrayInitializerExpr) {
        incremenetCodeProperty(SourceCodeProperties.ARRAY_INITIALIZED_OBJECTS_COUNTER);
        int counter = 0;
        for (Expression value : arrayInitializerExpr.getValues()) {
            if (value.isArrayInitializerExpr()) {
                counter += getCountInitializedObjects(arrayInitializerExpr);
            }
            counter += 1;
        }
        return counter;
    }

    private void incremenetCodeProperty(SourceCodeProperties property) {
        codeProperties[property.ordinal()] += 1;
    }
}
