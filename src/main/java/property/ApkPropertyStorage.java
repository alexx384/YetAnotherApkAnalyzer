package property;

public class ApkPropertyStorage implements MobSfApkProperty {
    private int targetSDK;
    private int maxSDK;
    private int minSDK;
    private int versionCodeName;
    private int versionCode;
    private int dangerousPermission;
    private int signaturePermission;
    private int appopPermission;
    private int privilegedPermission;
    private int developmentPermission;
    private int normalPermission;
    private int instantPermission;
    private int preinstalledPermission;
    private int retailDemoPermission;
    private int installerPermission;
    private int pre23Permission;
    private int unusedPermission;
    private int deprecatedPermission;

    public String getCSVRepresentation() {
        return targetSDK + ","
                + maxSDK + ","
                + minSDK + ","
                + versionCodeName + ","
                + versionCode + ","
                + dangerousPermission + ","
                + signaturePermission + ","
                + appopPermission + ","
                + privilegedPermission + ","
                + developmentPermission + ","
                + normalPermission + ","
                + instantPermission + ","
                + preinstalledPermission + ","
                + retailDemoPermission + ","
                + installerPermission + ","
                + pre23Permission + ","
                + unusedPermission + ","
                + deprecatedPermission;
    }

    @Override
    public void setTargetSDK(int targetSDK) {
        this.targetSDK = targetSDK;
    }

    @Override
    public void setMaxSDK(int maxSDK) {
        this.maxSDK = maxSDK;
    }

    @Override
    public void setMinSDK(int minSDK) {
        this.minSDK = minSDK;
    }

    @Override
    public void setVersionCodeName(int versionCodeName) {
        this.versionCodeName = versionCodeName;
    }

    @Override
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public void setCountDangerousPermissions(int dangerousPermissions) {
        this.dangerousPermission = dangerousPermissions;
    }

    @Override
    public void setCountSignaturePermissions(int signaturePermissions) {
        this.signaturePermission = signaturePermissions;
    }

    @Override
    public void setCountAppopPermissions(int appopPermissions) {
        this.appopPermission = appopPermissions;
    }

    @Override
    public void setCountPrivilegedPermissions(int privilegedPermissions) {
        this.privilegedPermission = privilegedPermissions;
    }

    @Override
    public void setCountDevelopmentPermissions(int developmentPermissions) {
        this.developmentPermission = developmentPermissions;
    }

    @Override
    public void setCountNormalPermissions(int normalPermissions) {
        this.normalPermission = normalPermissions;
    }

    @Override
    public void setCountInstantPermissions(int instantPermissions) {
        this.instantPermission = instantPermissions;
    }

    @Override
    public void setCountPreinstalledPermissions(int preinstalledPermissions) {
        this.preinstalledPermission = preinstalledPermissions;
    }

    @Override
    public void setCountRetailDemoPermissions(int retailDemoPermissions) {
        this.retailDemoPermission = retailDemoPermissions;
    }

    @Override
    public void setCountInstallerPermissions(int installerPermissions) {
        this.installerPermission = installerPermissions;
    }

    @Override
    public void setCountPre23Permissions(int pre23Permissions) {
        this.pre23Permission = pre23Permissions;
    }

    @Override
    public void setCountUnusedPermissions(int unusedPermissions) {
        this.unusedPermission = unusedPermissions;
    }

    @Override
    public void setCountDeprecatedPermissions(int deprecatedPermissions) {
        this.deprecatedPermission = deprecatedPermissions;
    }
}
