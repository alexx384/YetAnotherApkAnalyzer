package property;

public interface MobSfApkProperty {
    void setCountBrowsableActivities(int browsableActivities);

    void setCountActivities(int activities);

    void setCountReceivers(int receivers);

    void setCountProviders(int providers);

    void setCountServices(int services);

    void setCountLibraries(int libraries);

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

    void setCountTotalPermissions(int totalPermissions);

    void setCountManifestIssues(int manifestIssues);

    void setCountCodeIssues(int codeIssues);

    void setCountCodeHighIssues(int codeHighIssues);

    void setCountCodeWarningIssues(int codeWarningIssues);

    void setCountCodeInfoIssues(int codeInfoIssues);

    void setCountActivitiesWithUrl(int activitiesWithUrl);

    void setCountDomains(int domains);

    void setCountEmails(int emails);

    void setCountFirebaseUrls(int firebaseUrls);

    void setCountFiles(int files);

    void setCountXmlFiles(int xmlFiles);

    void setCountPngFiles(int pngFiles);

    void setCountKotlinMetadataFiles(int kotlinMetadataFiles);

    void setCountVersionFiles(int versionFiles);

    void setCountKotlinBuiltinsFiles(int kotlinBuiltinsFiles);

    void setCountProFiles(int proFiles);

    void setCountTtfFiles(int ttfFiles);

    void setCountDexFiles(int dexFiles);

    void setCountGifFiles(int gifFiles);

    void setCountExportedActivities(int exportedActivities);

    void setCountExportedServices(int exportedServices);

    void setCountExportedReceivers(int exportedReceivers);

    void setCountExportedProviders(int exportedProviders);

    void setCountDetectedTrackers(int detectedTrackers);

    void setMobsfAverageCVSS(int mobsfAverageCVSS);

    void setSecurityScore(int securityScore);
}
