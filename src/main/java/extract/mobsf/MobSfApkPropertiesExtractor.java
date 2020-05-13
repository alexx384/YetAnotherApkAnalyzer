package extract.mobsf;

import extract.mobsf.filetype.FileType;
import extract.mobsf.local.MobSfLocalPropertiesExtractor;
import extract.mobsf.permission.AndroidPermissionGroup;
import extract.mobsf.remote.MobSfRemotePropertiesExtractor;
import org.json.JSONArray;
import org.json.JSONObject;
import property.MobSfApkProperties;
import property.MobSfApkProperty;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MobSfApkPropertiesExtractor {
    private static final String STANDARD_PERMISSION_PREFIX = "android.permission.";
    private static final FileType[] enumFileTypes = FileType.values();
    private static final int[] countFileTypes = new int[enumFileTypes.length];

    public static boolean extract(MobSfApkProperty property, Path filePath, String mobsfAddress, String mobsfApiKey) {
        JSONObject jsonReport = getJsonReport(filePath, mobsfAddress, mobsfApiKey);
        if (jsonReport == null) {
            System.err.println("Error: the json report is null");
            return false;
        }
        int[] properties = new int[MobSfApkProperties.length];
        extractProperties(jsonReport, properties);
        property.setMobSfProperties(properties);
        return true;
    }

    private static JSONObject getJsonReport(Path filePath, String mobsfAddress, String mobsfApiKey) {
        MobSfLocalPropertiesExtractor localExtractor = new MobSfLocalPropertiesExtractor(filePath);
        JSONObject jsonReport = localExtractor.getJsonObject();

        if (jsonReport == null || localExtractor.isSourceZipNotPresent()) {
            jsonReport = null;
            MobSfRemotePropertiesExtractor remoteExtractor = MobSfRemotePropertiesExtractor
                    .build(mobsfAddress, mobsfApiKey);
            if (remoteExtractor != null) {
                String jsonReportFile = remoteExtractor.extract(filePath);
                if (jsonReportFile != null) {
                    jsonReport = new JSONObject(jsonReportFile);
                } else {
                    System.err.println("Error: Extracted json report is null");
                }
            } else {
                System.err.println("Error: Could not create remote properties extractor");
            }
        }
        return jsonReport;
    }

    private static void extractProperties(JSONObject json, int[] properties) {
        properties[MobSfApkProperties.BROWSABLE_ACTIVITIES.ordinal()] =
                getCountBrowsableActivities(json.getJSONObject("browsable_activities"));
        properties[MobSfApkProperties.ACTIVITIES.ordinal()] = json.getJSONArray("activities").length();
        properties[MobSfApkProperties.RECEIVERS.ordinal()] = json.getJSONArray("receivers").length();
        properties[MobSfApkProperties.PROVIDERS.ordinal()] = json.getJSONArray("providers").length();
        properties[MobSfApkProperties.SERVICES.ordinal()] = json.getJSONArray("services").length();
        properties[MobSfApkProperties.LIBRARIES.ordinal()] = json.getJSONArray("libraries").length();
        properties[MobSfApkProperties.TARGET_SDK.ordinal()] = json.optInt("target_sdk");
        properties[MobSfApkProperties.MAX_SDK.ordinal()] = json.optInt("max_sdk");
        properties[MobSfApkProperties.MIN_SDK.ordinal()] = json.optInt("min_sdk");
        properties[MobSfApkProperties.VERSION_CODE.ordinal()] = json.optInt("version_code");
        properties[MobSfApkProperties.MANIFEST_ISSUES.ordinal()] = json.getJSONArray("manifest_analysis").length();
        properties[MobSfApkProperties.ACTIVITIES_WITH_URL.ordinal()] = json.getJSONArray("urls").length();
        properties[MobSfApkProperties.DOMAINS.ordinal()] = getJSONObjectElements(json.getJSONObject("domains"));
        properties[MobSfApkProperties.EMAILS.ordinal()] = json.getJSONArray("emails").length();
        properties[MobSfApkProperties.FIREBASE_URLS.ordinal()] = json.getJSONArray("firebase_urls").length();
        properties[MobSfApkProperties.MOBSF_AVERAGE_CVSS.ordinal()] = (int) (json.getFloat("average_cvss") * 10);
        properties[MobSfApkProperties.SECURITY_SCORE.ordinal()] = json.getInt("security_score");

        parsePermissionsTo(properties, json.getJSONObject("permissions"));
        parseCodeIssues(json.getJSONObject("code_analysis"), properties);
        parseFilesInfo(json.getJSONArray("files"), properties);
        parseExported(json.getJSONObject("exported_count"), properties);
        parseTrackers(json.getJSONObject("trackers"), properties);
    }

    private static int getCountBrowsableActivities(JSONObject jsonObject) {
        int totalBrowsableActivities = 0;
        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
            it.next();
            totalBrowsableActivities += 1;
        }
        return totalBrowsableActivities;
    }

    private static void parsePermissionsTo(int[] properties, JSONObject permissions) {
        int[] permissionGroupCounters = new int[AndroidPermissionGroup.length];
        int totalPermissions = 0;

        Iterator<String> permissionIterator = permissions.keys();
        while (permissionIterator.hasNext()) {
            totalPermissions += 1;
            String fullPermissonName = permissionIterator.next();
            if (!fullPermissonName.startsWith(STANDARD_PERMISSION_PREFIX)) {
                continue;
            }
            String permissonName = fullPermissonName.substring(STANDARD_PERMISSION_PREFIX.length());
            AndroidPermissionGroup.decomposePermission(permissonName, permissionGroupCounters);
        }

        System.arraycopy(permissionGroupCounters, 0, properties,
                MobSfApkProperties.PERMISSION_DANGEROUS.ordinal(), permissionGroupCounters.length);
        properties[MobSfApkProperties.TOTAL_PERMISSIONS.ordinal()] = totalPermissions;
    }

    private static int getJSONObjectElements(JSONObject jsonObject) {
        int elements = 0;
        Iterator<String> elementsIterator = jsonObject.keys();
        while (elementsIterator.hasNext()) {
            elementsIterator.next();
            elements += 1;
        }
        return elements;
    }

    private static void parseCodeIssues(JSONObject jsonObject, int[] properties) {
        int highIssues = 0;
        int warningIssues = 0;
        int infoIssues = 0;
        for (Map.Entry<String, Object> stringObjectEntry : jsonObject.toMap().entrySet()) {
            //noinspection rawtypes
            HashMap objMap = (HashMap) stringObjectEntry.getValue();
            String level = (String) objMap.get("level");
            switch (level) {
                case "high":
                    highIssues += 1;
                    break;
                case "warning":
                    warningIssues += 1;
                    break;
                case "info":
                    infoIssues += 1;
                    break;
            }
        }

        properties[MobSfApkProperties.CODE_ISSUES.ordinal()] = highIssues + warningIssues + infoIssues;
        properties[MobSfApkProperties.CODE_HIGH_ISSUES.ordinal()] = highIssues;
        properties[MobSfApkProperties.CODE_WARNING_ISSUES.ordinal()] = warningIssues;
        properties[MobSfApkProperties.CODE_INFO_ISSUES.ordinal()] = infoIssues;
    }

    private static void fileInfo(String filename) {
        for (FileType fileType : enumFileTypes) {
            int fileLength = filename.length();
            int typeValueLength = fileType.value.length();
            if (fileLength >= (typeValueLength + 1)
                    && filename.charAt(fileLength - typeValueLength - 1) == '.'
                    && filename.endsWith(fileType.value)) {
                MobSfApkPropertiesExtractor.countFileTypes[fileType.ordinal()] += 1;
                return;
            }
        }
        MobSfApkPropertiesExtractor.countFileTypes[FileType.OTHER.ordinal()] += 1;
    }

    private static void parseFilesInfo(JSONArray jsonArray, int[] properties) {
        for (Object o : jsonArray) {
            fileInfo((String) o);
        }

        properties[MobSfApkProperties.FILES.ordinal()] = jsonArray.length();
        properties[MobSfApkProperties.XML_FILES.ordinal()] = countFileTypes[FileType.XML.ordinal()];
        properties[MobSfApkProperties.PNG_FILES.ordinal()] = countFileTypes[FileType.PNG.ordinal()];
        properties[MobSfApkProperties.KOTLIN_METADATA_FILES.ordinal()] =
                countFileTypes[FileType.KOTLIN_METADATA.ordinal()];
        properties[MobSfApkProperties.VERSION_FILES.ordinal()] = countFileTypes[FileType.VERSION.ordinal()];
        properties[MobSfApkProperties.KOTLIN_BUILTINS_FILES.ordinal()] =
                countFileTypes[FileType.KOTLIN_BUILTINS.ordinal()];
        properties[MobSfApkProperties.PRO_FILES.ordinal()] = countFileTypes[FileType.PRO.ordinal()];
        properties[MobSfApkProperties.TTF_FILES.ordinal()] = countFileTypes[FileType.TTF.ordinal()];
        properties[MobSfApkProperties.DEX_FILES.ordinal()] = countFileTypes[FileType.DEX.ordinal()];
        properties[MobSfApkProperties.GIF_FILES.ordinal()] = countFileTypes[FileType.GIF.ordinal()];
        properties[MobSfApkProperties.OTHER_FILES.ordinal()] = countFileTypes[FileType.OTHER.ordinal()];
    }

    private static void parseExported(JSONObject jsonObject, int[] properties) {
        properties[MobSfApkProperties.EXPORTED_ACTIVITIES.ordinal()] = jsonObject.getInt("exported_activities");
        properties[MobSfApkProperties.EXPORTED_SERVICES.ordinal()] = jsonObject.getInt("exported_services");
        properties[MobSfApkProperties.EXPORTED_RECEIVERS.ordinal()] = jsonObject.getInt("exported_receivers");
        properties[MobSfApkProperties.EXPORTED_PROVIDERS.ordinal()] = jsonObject.getInt("exported_providers");
    }

    private static void parseTrackers(JSONObject jsonObject, int[] properties) {
        properties[MobSfApkProperties.DETECTED_TRACKERS.ordinal()] = jsonObject.getInt("detected_trackers");
    }
}
