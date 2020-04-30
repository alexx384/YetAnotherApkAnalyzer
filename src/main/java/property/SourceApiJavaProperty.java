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
}
