package extract.androwarn;

import org.json.JSONArray;
import property.AndrowarnApkProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.BiConsumer;

public class AndrowarnPropertyExtractor {
    private static final int ANALYSIS_RESULTS_IDX = 1;
    private static final String ANALYSIS_RESULTS = "analysis_results";
    private static final String TELEPHONY_IDENTIFIERS_LEAKAGE = "telephony_identifiers_leakage";
    private static final String DEVICE_SETTINGS_HARVESTING = "device_settings_harvesting";
    private static final String LOCATION_LOOKUP = "location_lookup";
    private static final String CONNECTION_INTERFACES_EXFILTRATION = "connection_interfaces_exfiltration";
    private static final String TELEPHONY_SERVICES_ABUSE = "telephony_services_abuse";
    private static final String AUDIO_VIDEO_EAVESDROPPING = "audio_video_eavesdropping";
    private static final String SUSPICIOUS_CONNECTION_ESTABLISHMENT = "suspicious_connection_establishment";
    private static final String PIM_DATA_LEAKAGE = "PIM_data_leakage";
    private static final String CODE_EXECUTION = "code_execution";

    private static final int APIS_USED_IDX = 4;
    private static final String APIS_USED = "apis_used";
    private static final String CLASSES_LIST = "classes_list";
    private static final String INTERNAL_CLASSES_LIST = "internal_classes_list";
    private static final String INTENTS_SENT = "intents_sent";

    private final String pythonPath;
    private final String androwarnPath;
    private int countTelephonyIdentifiersLeakage;
    private int countDeviseSettingsHarvesting;
    private int countLocationLookup;
    private int countConnectionInterfacesExfiltration;
    private int countTelephonyServicesAbuse;
    private int countAudioVideoEavesdroping;
    private int countSuspiciousConnectionEstablishment;
    private int countPimDataLeakage;
    private int countCodeExecution;
    private int countClassesList;
    private int countInternalClassesList;
    private int countIntentsSent;

    /**
     * Builds AndrowarnParametersExtractor instance
     * during build made checks about Androwarn path correctness
     *
     * @param pythonPath path to python3 interpreter
     * @param androwarnPath path to androwarn.py
     * @return object of type AndrowarnParametersExtractor if succeeded to run Androwarn, otherwise - null
     */
    public static AndrowarnPropertyExtractor build(String pythonPath, String androwarnPath) {
        ProcessBuilder pb = new ProcessBuilder(pythonPath, androwarnPath, "-h");
        Process p;
        try {
            p = pb.start();
        } catch (IOException e) {
            return null;
        }
        try {
            if (p.waitFor() != 0) {
                return null;
            }
        } catch (InterruptedException e) {
            return null;
        }
        return new AndrowarnPropertyExtractor(pythonPath, androwarnPath);
    }

    private AndrowarnPropertyExtractor(String pythonPath, String androwarnPath) {
        this.pythonPath = pythonPath;
        this.androwarnPath = androwarnPath;
    }

    /**
     * Process apk and fill property values
     *
     * @param apkPath path to apk which needed to process
     * @param androwarnApkProperty property values to fill
     * @return true if process successful, otherwise - false
     */
    public boolean processApk(String apkPath, AndrowarnApkProperty androwarnApkProperty) {
        Objects.requireNonNull(apkPath);
        String jsonResultFile = getResultJsonFromAndrowarn(apkPath);
        if (jsonResultFile == null) {
            return false;
        }

        if (fillParametersFromJson(jsonResultFile)) {
            try {
                Files.delete(Path.of(jsonResultFile));
            } catch (IOException e) {
                return false;
            }
            exportInProperties(androwarnApkProperty);
            return true;
        } else {
            return false;
        }
    }

    private void exportInProperties(AndrowarnApkProperty property) {
        property.setTelephonyIdentifiersLeakage(countTelephonyIdentifiersLeakage);
        property.setDeviseSettingsHarvesting(countDeviseSettingsHarvesting);
        property.setLocationLookup(countLocationLookup);
        property.setConnectionInterfacesExfiltration(countConnectionInterfacesExfiltration);
        property.setTelephonyServicesAbuse(countTelephonyServicesAbuse);
        property.setAudioVideoEavesdroping(countAudioVideoEavesdroping);
        property.setSuspiciousConnectionEstablishment(countSuspiciousConnectionEstablishment);
        property.setPimDataLeakage(countPimDataLeakage);
        property.setCodeExecution(countCodeExecution);
        property.setClassesList(countClassesList);
        property.setInternalClassesList(countInternalClassesList);
        property.setIntentsSent(countIntentsSent);

        resetCounters();
    }

    private void resetCounters() {
        countTelephonyIdentifiersLeakage = 0;
        countDeviseSettingsHarvesting = 0;
        countLocationLookup = 0;
        countConnectionInterfacesExfiltration = 0;
        countTelephonyServicesAbuse = 0;
        countAudioVideoEavesdroping = 0;
        countSuspiciousConnectionEstablishment = 0;
        countPimDataLeakage = 0;
        countCodeExecution = 0;
        countClassesList = 0;
        countInternalClassesList = 0;
        countIntentsSent = 0;
    }

    private String getResultJsonFromAndrowarn(String apkPath) {
        ProcessBuilder pb = new ProcessBuilder(pythonPath, androwarnPath,
                "-i", apkPath,
                "-v", "3",
                "-r", "json");
        Process p;
        try {
            p = pb.start();
        } catch (IOException e) {
            return null;
        }

        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            if (p.waitFor() != 0) {
                return null;
            }
            String line;
            while ((line = buffer.readLine()) != null) {
                if (!line.startsWith("[+] Analysis successfully completed")) {
                    continue;
                }
                int singleQuotationStartIdx = line.indexOf('\'');
                if (singleQuotationStartIdx == -1) {
                    return null;
                }
                int singleQuotationStopIdx = line.indexOf('\'', singleQuotationStartIdx + 1);
                if (singleQuotationStopIdx == -1) {
                    return null;
                }
                return line.substring(singleQuotationStartIdx + 1, singleQuotationStopIdx);
            }
        } catch (InterruptedException | IOException e) {
            return null;
        }
        return null;
    }

    private boolean fillParametersFromJson(String jsonFilePath) {
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(Files.readString(Path.of(jsonFilePath)));
        } catch (IOException e) {
            return false;
        }
        fillParameters(jsonArray.getJSONObject(ANALYSIS_RESULTS_IDX).getJSONArray(ANALYSIS_RESULTS), this::fillAnalysisResult);
        fillParameters(jsonArray.getJSONObject(APIS_USED_IDX).getJSONArray(APIS_USED), this::fillApisUsed);
        return true;
    }

    private void fillParameters(JSONArray array, BiConsumer<String, Integer> biConsumer) {
        for (Object entity : array) {
            JSONArray resultArray = (JSONArray)entity;
            String entityName = resultArray.getString(0);
            int length = resultArray.getJSONArray(1).length();
            biConsumer.accept(entityName, length);
        }
    }

    private void fillAnalysisResult(String entityName, Integer value) {
        switch (entityName) {
            case TELEPHONY_IDENTIFIERS_LEAKAGE: {
                countTelephonyIdentifiersLeakage = value;
            } break;
            case DEVICE_SETTINGS_HARVESTING: {
                countDeviseSettingsHarvesting = value;
            } break;
            case LOCATION_LOOKUP: {
                countLocationLookup = value;
            } break;
            case CONNECTION_INTERFACES_EXFILTRATION: {
                countConnectionInterfacesExfiltration = value;
            } break;
            case TELEPHONY_SERVICES_ABUSE: {
                countTelephonyServicesAbuse = value;
            } break;
            case AUDIO_VIDEO_EAVESDROPPING: {
                countAudioVideoEavesdroping = value;
            } break;
            case SUSPICIOUS_CONNECTION_ESTABLISHMENT: {
                countSuspiciousConnectionEstablishment = value;
            } break;
            case PIM_DATA_LEAKAGE: {
                countPimDataLeakage = value;
            } break;
            case CODE_EXECUTION: {
                countCodeExecution = value;
            } break;
        }
    }

    private void fillApisUsed(String entityName, Integer value) {
        switch (entityName) {
            case CLASSES_LIST: {
                countClassesList = value;
            } break;
            case INTERNAL_CLASSES_LIST: {
                countInternalClassesList = value;
            } break;
            case INTENTS_SENT: {
                countIntentsSent = value;
            } break;
        }
    }
}
