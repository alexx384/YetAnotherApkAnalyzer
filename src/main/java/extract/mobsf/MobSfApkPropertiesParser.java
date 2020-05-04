package extract.mobsf;

import extract.mobsf.filetype.FileType;
import extract.mobsf.local.MobSfLocalPropertiesExtractor;
import extract.mobsf.permission.AndroidPermissionGroup;
import extract.mobsf.permission.PermissionClassifier;
import extract.mobsf.remote.MobSfRemotePropertiesExtractor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import property.MobSfApkProperty;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MobSfApkPropertiesParser {
    public static final String DIR_PREFIX = "dir.";

    private static final String standardPermissionPrefix = "android.permission.";
    private static final int permissionPrefixLenght = standardPermissionPrefix.length();
    private static final AndroidPermissionGroup[] enumAndroidPermissions = AndroidPermissionGroup.values();
    private static final int[] permissionGroupElements = new int[enumAndroidPermissions.length];
    private static final FileType[] enumFileTypes = FileType.values();
    private static final int[] countFileTypes = new int[enumFileTypes.length];

    private static JSONObject init(Path filePath, String mobsfAddress, String mobsfApiKey) {
        MobSfLocalPropertiesExtractor localExtractor = new MobSfLocalPropertiesExtractor(filePath);
        JSONObject jsonReport = localExtractor.getJsonObject();

        if (jsonReport == null || localExtractor.isSourceDirNotPresent()) {
            jsonReport = null;
            MobSfRemotePropertiesExtractor remoteExtractor = MobSfRemotePropertiesExtractor
                    .build(mobsfAddress, mobsfApiKey);
            if (remoteExtractor != null) {
                String apkSourceDir = DIR_PREFIX + filePath.getFileName().toString();
                String jsonReportFile = remoteExtractor.extract(filePath, apkSourceDir);
                if (jsonReportFile != null) {
                    jsonReport = new JSONObject(jsonReportFile);
                }
            }
        }
        return jsonReport;
    }

    public static boolean parseTo(MobSfApkProperty property, Path filePath, String mobsfAddress, String mobsfApiKey,
                                  String permissionDbPath) throws JSONException {
        JSONObject jsonObject = init(filePath, mobsfAddress, mobsfApiKey);
        if (jsonObject == null) {
            return false;
        }

        property.setBrowsableActivities(
                getCountBrowsableActivities(jsonObject.getJSONObject("browsable_activities")));
        property.setActivities(jsonObject.getJSONArray("activities").length());
        property.setReceivers(jsonObject.getJSONArray("receivers").length());
        property.setProviders(jsonObject.getJSONArray("providers").length());
        property.setServices(jsonObject.getJSONArray("services").length());
        property.setLibraries(jsonObject.getJSONArray("libraries").length());
        property.setTargetSDK(jsonObject.getInt("target_sdk"));
        property.setMaxSDK(parseMaxSDK(jsonObject.getString("max_sdk")));
        property.setMinSDK(jsonObject.getInt("min_sdk"));
        property.setVersionCode(jsonObject.getInt("version_code"));
        if (!parsePermissionsTo(permissionDbPath, property, jsonObject.getJSONObject("permissions"))) {
            return false;
        }
        property.setManifestIssues(jsonObject.getJSONArray("manifest_analysis").length());
        parseCodeIssues(jsonObject.getJSONObject("code_analysis"), property);
        property.setActivitiesWithUrl(jsonObject.getJSONArray("urls").length());
        property.setDomains(getJSONObjectElements(jsonObject.getJSONObject("domains")));
        property.setEmails(jsonObject.getJSONArray("emails").length());
        property.setFirebaseUrls(jsonObject.getJSONArray("firebase_urls").length());
        parseFilesInfo(jsonObject.getJSONArray("files"), property);
        parseExported(jsonObject.getJSONObject("exported_count"), property);
        parseTrackers(jsonObject.getJSONObject("trackers"), property);
        property.setMobsfAverageCVSS((int) (jsonObject.getFloat("average_cvss") * 10));
        property.setSecurityScore(jsonObject.getInt("security_score"));
        return true;
    }

    private static int getCountBrowsableActivities(JSONObject jsonObject) {
        int totalBrowsableActivities = 0;
        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
            it.next();
            totalBrowsableActivities += 1;
        }
        return totalBrowsableActivities;
    }

    private static int parseMaxSDK(String value) {
        if (value == null || value.length() == 0) {
            return 0;
        } else {
            return Integer.parseInt(value);
        }
    }

    private static boolean parsePermissionsTo(String permissionDbPath, MobSfApkProperty property,
                                              JSONObject androidPermissions) {
        try (PermissionClassifier classifier = new PermissionClassifier(permissionDbPath)) {
            Arrays.fill(permissionGroupElements, 0);

            int totalPermissions = 0;
            Iterator<String> permissionIterator = androidPermissions.keys();
            while (permissionIterator.hasNext()) {
                totalPermissions += 1;
                String fullPermissonName = permissionIterator.next();
                if (!fullPermissonName.startsWith(standardPermissionPrefix)) {
                    continue;
                }
                String permissonName = fullPermissonName.substring(permissionPrefixLenght);
                int groupValue = classifier.getProtectionGroupByName(permissonName);
                for (int i = 0; i < enumAndroidPermissions.length; i++) {
                    if ((enumAndroidPermissions[i].value & groupValue) > 0) {
                        permissionGroupElements[i] += 1;
                    }
                }
            }
            property.setDangerousPermissions(
                    permissionGroupElements[AndroidPermissionGroup.DANGEROUS.ordinal()]);
            property.setSignaturePermissions(
                    permissionGroupElements[AndroidPermissionGroup.SIGNATURE.ordinal()]);
            property.setAppopPermissions(
                    permissionGroupElements[AndroidPermissionGroup.APPOP.ordinal()]);
            property.setPrivilegedPermissions(
                    permissionGroupElements[AndroidPermissionGroup.PRIVILEGED.ordinal()]);
            property.setDevelopmentPermissions(
                    permissionGroupElements[AndroidPermissionGroup.DEVELOPMENT.ordinal()]);
            property.setNormalPermissions(
                    permissionGroupElements[AndroidPermissionGroup.NORMAL.ordinal()]);
            property.setInstantPermissions(
                    permissionGroupElements[AndroidPermissionGroup.INSTANT.ordinal()]);
            property.setPreinstalledPermissions(
                    permissionGroupElements[AndroidPermissionGroup.PREINSTALLED.ordinal()]);
            property.setRetailDemoPermissions(
                    permissionGroupElements[AndroidPermissionGroup.RETAILDEMO.ordinal()]);
            property.setInstallerPermissions(
                    permissionGroupElements[AndroidPermissionGroup.INSTALLER.ordinal()]);
            property.setPre23Permissions(
                    permissionGroupElements[AndroidPermissionGroup.PRE23.ordinal()]);
            property.setUnusedPermissions(
                    permissionGroupElements[AndroidPermissionGroup.UNUSED.ordinal()]);
            property.setDeprecatedPermissions(
                    permissionGroupElements[AndroidPermissionGroup.DEPRECATED.ordinal()]);
            property.setTotalPermissions(totalPermissions);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
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

    private static void parseCodeIssues(JSONObject jsonObject, MobSfApkProperty property) {
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

        property.setCodeIssues(highIssues + warningIssues + infoIssues);
        property.setCodeHighIssues(highIssues);
        property.setCodeWarningIssues(warningIssues);
        property.setCodeInfoIssues(infoIssues);
    }

    private static void fileInfo(String filename) {
        for (FileType fileType : enumFileTypes) {
            int fileLength = filename.length();
            int typeValueLength = fileType.value.length();
            if (fileLength >= (typeValueLength + 1)
                    && filename.charAt(fileLength - typeValueLength - 1) == '.'
                    && filename.endsWith(fileType.value)) {
                MobSfApkPropertiesParser.countFileTypes[fileType.ordinal()] += 1;
                return;
            }
        }
        MobSfApkPropertiesParser.countFileTypes[FileType.OTHER.ordinal()] += 1;
    }

    private static void parseFilesInfo(JSONArray jsonArray, MobSfApkProperty property) {
        for (Object o : jsonArray) {
            fileInfo((String) o);
        }

        property.setFiles(jsonArray.length());
        property.setXmlFiles(countFileTypes[FileType.XML.ordinal()]);
        property.setPngFiles(countFileTypes[FileType.PNG.ordinal()]);
        property.setKotlinMetadataFiles(countFileTypes[FileType.KOTLIN_METADATA.ordinal()]);
        property.setVersionFiles(countFileTypes[FileType.VERSION.ordinal()]);
        property.setKotlinBuiltinsFiles(countFileTypes[FileType.KOTLIN_BUILTINS.ordinal()]);
        property.setProFiles(countFileTypes[FileType.PRO.ordinal()]);
        property.setTtfFiles(countFileTypes[FileType.TTF.ordinal()]);
        property.setDexFiles(countFileTypes[FileType.DEX.ordinal()]);
        property.setGifFiles(countFileTypes[FileType.GIF.ordinal()]);
        property.setOtherFiles(countFileTypes[FileType.OTHER.ordinal()]);
    }

    private static void parseExported(JSONObject jsonObject, MobSfApkProperty property) {
        property.setExportedActivities(jsonObject.getInt("exported_activities"));
        property.setExportedServices(jsonObject.getInt("exported_services"));
        property.setExportedReceivers(jsonObject.getInt("exported_receivers"));
        property.setExportedProviders(jsonObject.getInt("exported_providers"));
    }

    private static void parseTrackers(JSONObject jsonObject, MobSfApkProperty property) {
        property.setDetectedTrackers(jsonObject.getInt("detected_trackers"));
    }
}
