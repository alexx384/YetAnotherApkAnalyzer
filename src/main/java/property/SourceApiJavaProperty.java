package property;

public interface SourceApiJavaProperty {
    void setCountIntentAddFlags(int intentAddFlags);

    void setCountIntentSetFlags(int intentSetFlags);

    void setCountIntentSetDataAndType(int intentSetDataAndType);

    void setCountIntentPutExtra(int intentPutExtra);

    void setCountDataOutputStreamWriteBytes(int dataOutputStreamWriteBytes);

    void setCountStringBuilderAppend(int stringBuilderAppend);

    void setCountStringBuilderIndexOf(int stringBuilderIndexOf);

    void setCountStringBuilderSubstring(int stringBuilderSubstring);

    void setCountStringBufferAppend(int stringBufferAppend);

    void setCountStringBufferIndexOf(int stringBufferIndexOf);

    void setCountStringBufferSubstring(int stringBufferSubstring);

    void setCountContentResolverQuery(int contentResolverQuery);

    void setCountContentResolverInsert(int contentResolverInsert);

    void setCountContentResolverUpdate(int contentResolverUpdate);

    void setCountIntentConstructor(int intentConstructor);

    void setCountIntentFilterConstructor(int intentFilterConstructor);

    void setCountDataInputStreamConstructor(int dataInputStreamConstructor);

    void setCountDataOutputStreamConstructor(int dataOutputStreamConstructor);

    void setCountBufferedReaderConstructor(int bufferedReaderConstructor);

    void setCountStringBuilderConstructor(int stringBuilderConstructor);

    void setCountStringBufferConstructor(int stringBufferConstructor);

    void setCountStringConstructor(int stringConstructor);

    void setCountStringToLowerCase(int stringToLowerCase);

    void setCountStringToUpperCase(int stringToUpperCase);

    void setCountStringTrim(int stringTrim);

    void setCountStringCharAt(int stringCharAt);

    void setCountFileConstructor(int fileConstructor);

    void setCountStreamConstructor(int streamConstructor);

    void setCountEnum(int enums);

    void setCountInterface(int interfaces);

    void setCountClass(int classes);

    void setCountBodyDeclaration(int bodyDeclarations);

    void setCountEnumConstant(int enumConstants);

    void setCountMethod(int methods);

    void setCountBody(int bodies);

    void setCountClassField(int classFields);

    void setCountParameter(int parameters);

    void setCountStatement(int statements);

    void setCountExpression(int expressions);

    void setCountIfStatement(int ifStatements);

    void setCountForStatement(int forStatements);

    void setCountForEachStatement(int forEachStatements);

    void setCountDoWhileStatement(int doWhileStatements);

    void setCountTryStatement(int tryStatements);

    void setCountAssertStatement(int assertStatements);

    void setCountSwitchStatement(int switchStatements);

    void setCountSynchronizedStatement(int synchronizedStatements);

    void setCountConstructorInvocationStatement(int constructorInvocationStatements);

    void setCountVariableStatement(int variableStatements);

    void setCountLambdaExpression(int lambdaExpressions);

    void setCountObjectCreationExpression(int objectCreationExpressions);

    void setCountFieldAccessExpression(int fieldAccessExpressions);

    void setCountArrayCreationExpression(int arrayCreationExpressions);

    void setCountAssignExpression(int assignExpressions);

    void setCountBinaryExpression(int binaryExpressions);

    void setCountConditionalExpression(int conditionalExpressions);

    void setCountCatchExpression(int catchExpressions);

    void setCountArrayInitializedObjects(int arrayInitializedObjectss);
}
