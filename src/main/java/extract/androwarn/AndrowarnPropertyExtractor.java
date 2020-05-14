package extract.androwarn;

import org.json.JSONArray;
import property.AndrowarnApkProperties;
import property.AndrowarnApkProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class AndrowarnPropertyExtractor {
    private static final int ANALYSIS_RESULTS_IDX = 1;
    private static final String ANALYSIS_RESULTS = "analysis_results";
    private static final int APIS_USED_IDX = 4;
    private static final String APIS_USED = "apis_used";
    private static final Map<String, Integer> propertiesMap;

    static {
        AndrowarnApkProperties[] properties = AndrowarnApkProperties.values();
        propertiesMap = new HashMap<>(properties.length);
        for (AndrowarnApkProperties property : properties) {
            propertiesMap.put(property.name, property.ordinal());
        }
    }

    private final String pythonPath;
    private final String androwarnPath;

    private int[] properties;

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
        properties = new int[AndrowarnApkProperties.length];
    }

    /**
     * Process apk and fill property values
     *
     * @param apkPath path to apk which needed to process
     * @param property property values to fill
     * @return true if process successful, otherwise - false
     */
    public boolean processApk(String apkPath, AndrowarnApkProperty property) {
        Objects.requireNonNull(apkPath);
        String jsonResultFile = getResultJsonFromAndrowarn(apkPath);
        if (jsonResultFile == null) {
            property.setAndrowarnProperties(properties);
            resetCounters();
            return false;
        }

        if (fillParametersFromJson(jsonResultFile)) {
            property.setAndrowarnProperties(properties);
            resetCounters();
            try {
                Files.delete(Path.of(jsonResultFile));
            } catch (IOException e) {
                return false;
            }
            return true;
        } else {
            property.setAndrowarnProperties(properties);
            resetCounters();
            return false;
        }
    }

    private void resetCounters() {
        properties = new int[AndrowarnApkProperties.length];
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
        fillParameters(jsonArray.getJSONObject(ANALYSIS_RESULTS_IDX).getJSONArray(ANALYSIS_RESULTS), this::fillResult);
        fillParameters(jsonArray.getJSONObject(APIS_USED_IDX).getJSONArray(APIS_USED), this::fillResult);
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

    private void fillResult(String entityName, Integer value) {
        Integer counterIdx = propertiesMap.get(entityName);
        if (counterIdx != null) {
            properties[counterIdx] = value;
        }
    }
}
