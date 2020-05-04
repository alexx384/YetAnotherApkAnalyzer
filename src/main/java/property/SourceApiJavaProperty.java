package property;

import lombok.Setter;

public class SourceApiJavaProperty implements ApkProperty {
    @Setter private int intentAddFlags;
    @Setter private int intentSetFlags;
    @Setter private int intentSetDataAndType;
    @Setter private int intentPutExtra;
    @Setter private int dataOutputStreamWriteBytes;
    @Setter private int stringBuilderAppend;
    @Setter private int stringBuilderIndexOf;
    @Setter private int stringBuilderSubstring;
    @Setter private int stringBufferAppend;
    @Setter private int stringBufferIndexOf;
    @Setter private int stringBufferSubstring;
    @Setter private int contentResolverQuery;
    @Setter private int contentResolverInsert;
    @Setter private int contentResolverUpdate;
    @Setter private int intentConstructor;
    @Setter private int intentFilterConstructor;
    @Setter private int dataInputStreamConstructor;
    @Setter private int dataOutputStreamConstructor;
    @Setter private int bufferedReaderConstructor;
    @Setter private int stringBuilderConstructor;
    @Setter private int stringBufferConstructor;
    @Setter private int stringConstructor;
    @Setter private int stringToLowerCase;
    @Setter private int stringToUpperCase;
    @Setter private int stringTrim;
    @Setter private int stringCharAt;
    @Setter private int fileConstructor;
    @Setter private int streamConstructor;

    @Override
    public StringBuilder toBuilder(StringBuilder builder) {
        return builder.append(intentAddFlags).append(',')
                .append(intentSetFlags).append(',')
                .append(intentSetDataAndType).append(',')
                .append(intentPutExtra).append(',')
                .append(dataOutputStreamWriteBytes).append(',')
                .append(stringBuilderAppend).append(',')
                .append(stringBuilderIndexOf).append(',')
                .append(stringBuilderSubstring).append(',')
                .append(stringBufferAppend).append(',')
                .append(stringBufferIndexOf).append(',')
                .append(stringBufferSubstring).append(',')
                .append(contentResolverQuery).append(',')
                .append(contentResolverInsert).append(',')
                .append(contentResolverUpdate).append(',')
                .append(intentConstructor).append(',')
                .append(intentFilterConstructor).append(',')
                .append(dataInputStreamConstructor).append(',')
                .append(dataOutputStreamConstructor).append(',')
                .append(bufferedReaderConstructor).append(',')
                .append(stringBuilderConstructor).append(',')
                .append(stringBufferConstructor).append(',')
                .append(stringConstructor).append(',')
                .append(stringToLowerCase).append(',')
                .append(stringToUpperCase).append(',')
                .append(stringTrim).append(',')
                .append(stringCharAt).append(',')
                .append(fileConstructor).append(',')
                .append(streamConstructor);
    }
}
