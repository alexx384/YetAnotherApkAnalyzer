package property;

import lombok.Setter;

public class SourceCodeJavaProperty implements ApkProperty {
    @Setter private int enums;
    @Setter private int interfaces;
    @Setter private int classes;
    @Setter private int bodyDeclarations;
    @Setter private int enumConstants;
    @Setter private int methods;
    @Setter private int bodies;
    @Setter private int classFields;
    @Setter private int parameters;
    @Setter private int statements;
    @Setter private int expressions;
    @Setter private int ifStatements;
    @Setter private int forStatements;
    @Setter private int forEachStatements;
    @Setter private int doWhileStatements;
    @Setter private int tryStatements;
    @Setter private int assertStatements;
    @Setter private int switchStatements;
    @Setter private int synchronizedStatements;
    @Setter private int constructorInvocationStatements;
    @Setter private int variableStatements;
    @Setter private int lambdaExpressions;
    @Setter private int objectCreationExpressions;
    @Setter private int fieldAccessExpressions;
    @Setter private int arrayCreationExpressions;
    @Setter private int assignExpressions;
    @Setter private int binaryExpressions;
    @Setter private int conditionalExpressions;
    @Setter private int catchExpressions;
    @Setter private int arrayInitializedObjects;
    @Setter private int initializedDeclarations;
    @Setter private int constructorDeclarations;
    @Setter private int returnStatements;
    @Setter private int yieldStatements;
    @Setter private int localClassDeclarations;
    @Setter private int thrownStatements;
    @Setter private int labeledStatements;
    @Setter private int castExpressions;
    @Setter private int enclosedExpressions;
    @Setter private int unaryExpressions;
    @Setter private int arrayAccessExpressions;
    @Setter private int methodCallExpressions;

    @Override
    public StringBuilder toBuilder(StringBuilder builder) {
        return builder.append(enums).append(',')
                .append(interfaces).append(',')
                .append(classes).append(',')
                .append(bodyDeclarations).append(',')
                .append(enumConstants).append(',')
                .append(methods).append(',')
                .append(bodies).append(',')
                .append(classFields).append(',')
                .append(parameters).append(',')
                .append(statements).append(',')
                .append(expressions).append(',')
                .append(ifStatements).append(',')
                .append(forStatements).append(',')
                .append(forEachStatements).append(',')
                .append(doWhileStatements).append(',')
                .append(tryStatements).append(',')
                .append(assertStatements).append(',')
                .append(switchStatements).append(',')
                .append(synchronizedStatements).append(',')
                .append(constructorInvocationStatements).append(',')
                .append(variableStatements).append(',')
                .append(lambdaExpressions).append(',')
                .append(objectCreationExpressions).append(',')
                .append(fieldAccessExpressions).append(',')
                .append(arrayCreationExpressions).append(',')
                .append(assignExpressions).append(',')
                .append(binaryExpressions).append(',')
                .append(conditionalExpressions).append(',')
                .append(catchExpressions).append(',')
                .append(arrayInitializedObjects).append(',')
                .append(initializedDeclarations).append(',')
                .append(constructorDeclarations).append(',')
                .append(returnStatements).append(',')
                .append(yieldStatements).append(',')
                .append(localClassDeclarations).append(',')
                .append(thrownStatements).append(',')
                .append(labeledStatements).append(',')
                .append(castExpressions).append(',')
                .append(enclosedExpressions).append(',')
                .append(unaryExpressions).append(',')
                .append(arrayAccessExpressions).append(',')
                .append(methodCallExpressions);
    }
}
