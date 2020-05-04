package property;

import lombok.Setter;

public class SourceImportJavaProperty implements ApkProperty {
    @Setter private int activityImports;
    @Setter private int intentImports;
    @Setter private int intentFilterImports;
    @Setter private int contentResolverImports;
    @Setter private int dataInputStreamImports;
    @Setter private int bufferedReaderImports;
    @Setter private int dataOutputStreamImports;
    @Setter private int inetSocketAddressImports;
    @Setter private int fileImports;
    @Setter private int streamImports;
    @Setter private int telephonyManagerImports;
    @Setter private int serviceImports;
    @Setter private int contextImports;
    @Setter private int packageManagerImports;
    @Setter private int smsManagerImports;
    @Setter private int timerImports;
    @Setter private int bundleImports;
    @Setter private int applicationInfoImports;
    @Setter private int timerTaskImports;
    @Setter private int fileOutputStreamImports;
    @Setter private int networkInfoImports;
    @Setter private int connectivityManagerImports;
    @Setter private int logImports;

    @Override
    public StringBuilder toBuilder(StringBuilder builder) {
        return builder.append(activityImports).append(',')
                .append(intentImports).append(',')
                .append(intentFilterImports).append(',')
                .append(contentResolverImports).append(',')
                .append(dataInputStreamImports).append(',')
                .append(bufferedReaderImports).append(',')
                .append(dataOutputStreamImports).append(',')
                .append(inetSocketAddressImports).append(',')
                .append(fileImports).append(',')
                .append(streamImports).append(',')
                .append(telephonyManagerImports).append(',')
                .append(serviceImports).append(',')
                .append(contextImports).append(',')
                .append(packageManagerImports).append(',')
                .append(smsManagerImports).append(',')
                .append(timerImports).append(',')
                .append(bundleImports).append(',')
                .append(applicationInfoImports).append(',')
                .append(timerTaskImports).append(',')
                .append(fileOutputStreamImports).append(',')
                .append(networkInfoImports).append(',')
                .append(connectivityManagerImports).append(',')
                .append(logImports);
    }
}
