package extract.mobsf;

import extract.ApkProperty;
import extract.mobsf.permission.AndroidPermissionGroup;
import extract.mobsf.permission.PermissionClassifier;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class MobSfApkProperty implements ApkProperty {
    private static final String standardPermissionPrefix = "android.permission.";
    private static final int permissionPrefixLenght = standardPermissionPrefix.length();

    private int targetSDK;
    private int maxSDK;
    private int minSDK;
    private float versionName;
    private int versionCode;
    private int[] permissionsGroup;

    public static MobSfApkProperty extract(JSONObject mobsfData) throws JSONException {
        MobSfApkProperty apkProperty = new MobSfApkProperty();
        apkProperty.targetSDK = mobsfData.getInt("target_sdk");
        apkProperty.maxSDK = parseMaxSDK(mobsfData.getString("max_sdk"));
        apkProperty.minSDK = mobsfData.getInt("min_sdk");
        apkProperty.versionName = mobsfData.getFloat("version_name");
        apkProperty.versionCode = mobsfData.getInt("version_code");
        apkProperty.permissionsGroup = parsePermissions(mobsfData.getJSONObject("permissions"));
        // Permission provider
        // TODO: And so on
        return apkProperty;
    }

    @Override
    public String getCSVRepresentation() {
        StringBuilder permissionsStr = new StringBuilder();
        permissionsStr.append(targetSDK)
                .append(maxSDK).append(',')
                .append(minSDK).append(',')
                .append(versionName).append(',')
                .append(versionCode).append(',');
        for (int i = 0; i < permissionsGroup.length - 1; i++) {
            permissionsStr.append(permissionsGroup[i]).append(',');
        }
        permissionsStr.append(permissionsGroup[permissionsGroup.length - 1]);
        return permissionsStr.toString();
    }

    private static int parseMaxSDK(String value) {
        if (value == null || value.length() == 0) {
            return 0;
        } else {
            return Integer.parseInt(value);
        }
    }

    private static int[] parsePermissions(JSONObject androidPermissions) {
        try (PermissionClassifier classifier = new PermissionClassifier()) {
            AndroidPermissionGroup[] enumAndroidPermissions = AndroidPermissionGroup.values();
            int[] permissionGroupElements = new int[enumAndroidPermissions.length];
            Iterator<String> permissionIterator = androidPermissions.keys();
            while (permissionIterator.hasNext()) {
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
            return permissionGroupElements;
        } catch (Exception e) {
            return null;
        }
    }

    private MobSfApkProperty() {

    }
}
