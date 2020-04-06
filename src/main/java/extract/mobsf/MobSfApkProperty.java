package extract.mobsf;

import extract.ApkProperty;
import org.json.JSONException;
import org.json.JSONObject;

public class MobSfApkProperty implements ApkProperty {

    private int targetSDK;
    private int maxSDK;
    private int minSDK;
    private float versionName;
    private int versionCode;

    public static MobSfApkProperty extract(JSONObject mobsfData) throws JSONException {
        MobSfApkProperty apkProperty = new MobSfApkProperty();
        apkProperty.targetSDK = mobsfData.getInt("target_sdk");
        apkProperty.maxSDK = parseMaxSDK(mobsfData.getString("max_sdk"));
        apkProperty.minSDK = mobsfData.getInt("min_sdk");
        apkProperty.versionName = mobsfData.getFloat("version_name");
        apkProperty.versionCode = mobsfData.getInt("version_code");

        // TODO: And so on
        return apkProperty;
    }

    private static int parseMaxSDK(String value) {
        if (value == null || value.length() == 0) {
            return 0;
        } else {
            return Integer.parseInt(value);
        }
    }

    @Override
    public String getCSVRepresentation() {
        return String.valueOf(targetSDK) + ',' +
                maxSDK + ',' +
                minSDK + ',' +
                versionName + ',' +
                versionCode;
    }

    private MobSfApkProperty() {

    }

    public static void main(String[] args) {

    }
}
