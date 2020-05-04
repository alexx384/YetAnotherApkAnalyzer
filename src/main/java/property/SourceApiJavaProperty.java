package property;

import lombok.Setter;

public class SourceApiJavaProperty implements ApkProperty {
    @Setter private int intentAddFlags;
    @Setter private int intentSetFlags;
    @Setter private int intentSetDataAndType;
    @Setter private int intentPutExtra;
    @Setter private int intentAddCategory;
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
    @Setter private int bufferedWriterConstructor;
    @Setter private int fileOutputStreamConstructor;
    @Setter private int fileInputStreamConstructor;
    @Setter private int inetSocketAddressConstructor;
    @Setter private int timerConstructor;
    @Setter private int timerTaskConstructor;
    @Setter private int activityConstructor;
    @Setter private int serviceConstructor;
    @Setter private int contentResolverConstructor;
    @Setter private int contextConstructor;
    @Setter private int packageManagerConstructor;
    @Setter private int applicationInfoConstructor;
    @Setter private int networkInfoConstructor;
    @Setter private int connectivityManagerConstructor;
    @Setter private int bundleConstructor;
    @Setter private int telephonyManagerConstructor;
    @Setter private int smsManagerConstructor;

    @Override
    public StringBuilder toBuilder(StringBuilder builder) {
        return builder.append(intentAddFlags).append(',')
                .append(intentSetFlags).append(',')
                .append(intentSetDataAndType).append(',')
                .append(intentPutExtra).append(',')
                .append(intentAddCategory).append(',')
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
                .append(streamConstructor).append(',')
                .append(bufferedWriterConstructor).append(',')
                .append(fileOutputStreamConstructor).append(',')
                .append(fileInputStreamConstructor).append(',')
                .append(inetSocketAddressConstructor).append(',')
                .append(timerConstructor).append(',')
                .append(timerTaskConstructor).append(',')
                .append(activityConstructor).append(',')
                .append(serviceConstructor).append(',')
                .append(contentResolverConstructor).append(',')
                .append(contextConstructor).append(',')
                .append(packageManagerConstructor).append(',')
                .append(applicationInfoConstructor).append(',')
                .append(networkInfoConstructor).append(',')
                .append(connectivityManagerConstructor).append(',')
                .append(bundleConstructor).append(',')
                .append(telephonyManagerConstructor).append(',')
                .append(smsManagerConstructor);
    }
}
