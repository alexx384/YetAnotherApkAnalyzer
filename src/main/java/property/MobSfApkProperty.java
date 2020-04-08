package property;

public interface MobSfApkProperty {
    void setTargetSDK(int tragetSDK);

    void setMaxSDK(int maxSDK);

    void setMinSDK(int minSDK);

    void setVersionCodeName(int versionCodeName);

    void setVersionCode(int versionCode);

    void setCountDangerousPermissions(int dangerousPermissions);

    void setCountSignaturePermissions(int signaturePermissions);

    void setCountAppopPermissions(int appopPermissions);

    void setCountPrivilegedPermissions(int privilegedPermissions);

    void setCountDevelopmentPermissions(int developmentPermissions);

    void setCountNormalPermissions(int normalPermissions);

    void setCountInstantPermissions(int instantPermissions);

    void setCountPreinstalledPermissions(int preinstalledPermissions);

    void setCountRetailDemoPermissions(int retailDemoPermissions);

    void setCountInstallerPermissions(int installerPermissions);

    void setCountPre23Permissions(int pre23Permissions);

    void setCountUnusedPermissions(int unusedPermissions);

    void setCountDeprecatedPermissions(int deprecatedPermissions);
}
