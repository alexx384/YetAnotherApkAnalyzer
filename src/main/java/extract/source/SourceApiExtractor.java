package extract.source;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
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
            "setDataAndType"
    );

    private final Map<String, Map<String, MutableInteger>> detectSignMap;
    private final List<Map<String, Map<String, MutableInteger>>> instanceVariableMapList;
    private final List<Map<String, Map<String, MutableInteger>>> otherVariableMapList;

    public SourceApiExtractor() {
        detectSignMap = Map.ofEntries(
                Map.entry("String", Map.ofEntries(
                        Map.entry("toLowerCase", new MutableInteger()),
                        Map.entry("strip", new MutableInteger()),
                        Map.entry("setDataAndType", new MutableInteger())
                ))
        );
        instanceVariableMapList = new ArrayList<>();
        otherVariableMapList = new ArrayList<>();
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
        Optional<ClassOrInterfaceDeclaration> optionalDeclaration = cu.getClassByName(name.get());
        if (optionalDeclaration.isEmpty()) {
            return false;
        }
        ClassOrInterfaceDeclaration declaration = optionalDeclaration.get();
        if (declaration.isInterface()) {
            extractInterfaceDeclaration(declaration);
        } else {
            extractClassDeclaration(declaration);
        }

        return true;
    }

    private void extractClassDeclaration(ClassOrInterfaceDeclaration classDeclaration) {
        instanceVariableMapList.add(new HashMap<>());
        otherVariableMapList.add(new HashMap<>());
        extractClassFields(classDeclaration.getFields());

        for (MethodDeclaration method : classDeclaration.getMethods()) {
            extractClassMethod(method);
        }
        instanceVariableMapList.remove(instanceVariableMapList.size() - 1);
        otherVariableMapList.remove(otherVariableMapList.size() - 1);
    }

    private void extractInterfaceDeclaration(ClassOrInterfaceDeclaration interfaceDeclaration) {

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

    private void extractClassMethod(MethodDeclaration method) {
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
        if (statement.isExpressionStmt()) {
            extractExpression(statement.asExpressionStmt().getExpression());
        } else if (statement.isTryStmt()) {
            extractTryStatement(statement.asTryStmt());
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
