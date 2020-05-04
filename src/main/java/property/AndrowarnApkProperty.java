package property;

import lombok.Setter;

public class AndrowarnApkProperty implements ApkProperty {
    @Setter private int telephonyIdentifiersLeakage;
    @Setter private int deviseSettingsHarvesting;
    @Setter private int locationLookup;
    @Setter private int connectionInterfacesExfiltration;
    @Setter private int telephonyServicesAbuse;
    @Setter private int audioVideoEavesdroping;
    @Setter private int suspiciousConnectionEstablishment;
    @Setter private int pimDataLeakage;
    @Setter private int codeExecution;
    @Setter private int classesList;
    @Setter private int internalClassesList;
    @Setter private int intentsSent;

    @Override
    public StringBuilder toBuilder(StringBuilder builder) {
        return builder.append(telephonyIdentifiersLeakage).append(',')
                .append(deviseSettingsHarvesting).append(',')
                .append(locationLookup).append(',')
                .append(connectionInterfacesExfiltration).append(',')
                .append(telephonyServicesAbuse).append(',')
                .append(audioVideoEavesdroping).append(',')
                .append(suspiciousConnectionEstablishment).append(',')
                .append(pimDataLeakage).append(',')
                .append(codeExecution).append(',')
                .append(classesList).append(',')
                .append(internalClassesList).append(',')
                .append(intentsSent);
    }
}
