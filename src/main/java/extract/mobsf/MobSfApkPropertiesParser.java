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
import java.util.Iterator;

public class MobSfApkPropertiesParser {
    private static final String standardPermissionPrefix = "android.permission.";
    private static final int permissionPrefixLenght = standardPermissionPrefix.length();
    private static final AndroidPermissionGroup[] enumAndroidPermissions = AndroidPermissionGroup.values();
    private static final int[] permissionGroupElements = new int[enumAndroidPermissions.length];
    private static final FileType[] enumFileTypes = FileType.values();
    private static final int[] countFileTypes = new int[enumFileTypes.length];

    private static JSONObject init(Path filePath, String mobsfAddress, String mobsfApiKey) {
        MobSfLocalPropertiesExtractor localExtractor = new MobSfLocalPropertiesExtractor(filePath);
        JSONObject object = localExtractor.getJsonObject();

        if (object == null) {
            MobSfRemotePropertiesExtractor remoteExtractor = MobSfRemotePropertiesExtractor
                    .build(mobsfAddress, mobsfApiKey);
            if (remoteExtractor != null) {
                String strJsonObject = remoteExtractor.extract(filePath);
                if (strJsonObject != null) {
                    localExtractor.overrideFile(strJsonObject);
                    object = new JSONObject(strJsonObject);
                }
            }
        }
        return object;
    }

    public static boolean parseTo(MobSfApkProperty property, Path filePath, String mobsfAddress, String mobsfApiKey)
            throws JSONException {
        JSONObject jsonObject = init(filePath, mobsfAddress, mobsfApiKey);
        if (jsonObject == null) {
            return false;
        }

        property.setCountExportedActivities(
                getCountExportedActivites(jsonObject.getString("exported_activities")));
        property.setCountBrowsableActivities(
                getCountBrowsableActivities(jsonObject.getJSONObject("browsable_activities")));
        property.setCountActivities(jsonObject.getJSONArray("activities").length());
        property.setCountReceivers(jsonObject.getJSONArray("receivers").length());
        property.setCountProviders(jsonObject.getJSONArray("providers").length());
        property.setCountServices(jsonObject.getJSONArray("services").length());
        property.setCountLibraries(jsonObject.getJSONArray("libraries").length());
        property.setTargetSDK(jsonObject.getInt("target_sdk"));
        property.setMaxSDK(parseMaxSDK(jsonObject.getString("max_sdk")));
        property.setMinSDK(jsonObject.getInt("min_sdk"));
        property.setVersionCodeName((int) (jsonObject.getFloat("version_name") * 1000));
        property.setVersionCode(jsonObject.getInt("version_code"));
        if (!parsePermissionsTo(property, jsonObject.getJSONObject("permissions"))) {
            return false;
        }
        parseFilesInfo(jsonObject.getJSONArray("files"), property);

        return true;
        // Permission provider
        // TODO: And so on
//        return true;
    }

    private static int getCountExportedActivites(String jsonArray) {
        int countCommaSign = 0;
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.charAt(i) == ',') {
                countCommaSign++;
            }
        }
        return countCommaSign + 1;
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

    private static boolean parsePermissionsTo(MobSfApkProperty property, JSONObject androidPermissions) {
        try (PermissionClassifier classifier = new PermissionClassifier()) {
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
            property.setCountDangerousPermissions(
                    permissionGroupElements[AndroidPermissionGroup.DANGEROUS.ordinal()]);
            property.setCountSignaturePermissions(
                    permissionGroupElements[AndroidPermissionGroup.SIGNATURE.ordinal()]);
            property.setCountAppopPermissions(
                    permissionGroupElements[AndroidPermissionGroup.APPOP.ordinal()]);
            property.setCountPrivilegedPermissions(
                    permissionGroupElements[AndroidPermissionGroup.PRIVILEGED.ordinal()]);
            property.setCountDevelopmentPermissions(
                    permissionGroupElements[AndroidPermissionGroup.DEVELOPMENT.ordinal()]);
            property.setCountNormalPermissions(
                    permissionGroupElements[AndroidPermissionGroup.NORMAL.ordinal()]);
            property.setCountInstantPermissions(
                    permissionGroupElements[AndroidPermissionGroup.INSTANT.ordinal()]);
            property.setCountPreinstalledPermissions(
                    permissionGroupElements[AndroidPermissionGroup.PREINSTALLED.ordinal()]);
            property.setCountRetailDemoPermissions(
                    permissionGroupElements[AndroidPermissionGroup.RETAILDEMO.ordinal()]);
            property.setCountInstallerPermissions(
                    permissionGroupElements[AndroidPermissionGroup.INSTALLER.ordinal()]);
            property.setCountPre23Permissions(
                    permissionGroupElements[AndroidPermissionGroup.PRE23.ordinal()]);
            property.setCountUnusedPermissions(
                    permissionGroupElements[AndroidPermissionGroup.UNUSED.ordinal()]);
            property.setCountDeprecatedPermissions(
                    permissionGroupElements[AndroidPermissionGroup.DEPRECATED.ordinal()]);
            property.setCountTotalPermissions(totalPermissions);
            return true;
        } catch (Exception e) {
            return false;
        }
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
    }

    private static void parseFilesInfo(JSONArray jsonArray, MobSfApkProperty property) {
        for (Object o : jsonArray) {
            fileInfo((String) o);
        }

        property.setCountFiles(jsonArray.length());
        property.setCountXmlFiles(countFileTypes[FileType.XML.ordinal()]);
        property.setCountPngFiles(countFileTypes[FileType.PNG.ordinal()]);
        property.setCountKotlinMetadataFiles(countFileTypes[FileType.KOTLIN_METADATA.ordinal()]);
        property.setCountVersionFiles(countFileTypes[FileType.VERSION.ordinal()]);
        property.setCountKotlinBuiltinsFiles(countFileTypes[FileType.KOTLIN_BUILTINS.ordinal()]);
        property.setCountProFiles(countFileTypes[FileType.PRO.ordinal()]);
        property.setCountTtfFiles(countFileTypes[FileType.TTF.ordinal()]);
        property.setCountDexFiles(countFileTypes[FileType.DEX.ordinal()]);
        property.setCountGifFiles(countFileTypes[FileType.GIF.ordinal()]);
    }
}
