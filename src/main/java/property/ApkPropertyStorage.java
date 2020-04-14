package property;

public class ApkPropertyStorage implements MobSfApkProperty, SourceJavaProperty {
    /* === MobSfApkProperty === */
    private int browsableActivities;
    private int activities;
    private int receivers;
    private int providers;
    private int services;
    private int libraries;
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
    private int totalPermissions;
    private int manifestIssues;
    private int codeIssues;
    private int codeHighIssues;
    private int codeWarningIssues;
    private int codeInfoIssues;
    private int activitiesWithUrl;
    private int domains;
    private int emails;
    private int firebaseUrls;
    private int files;
    private int xmlFiles;
    private int pngFiles;
    private int kotlinMetadataFiles;
    private int versionFiles;
    private int kotlinBuiltinsFiles;
    private int proFiles;
    private int ttfFiles;
    private int dexFiles;
    private int gifFiles;
    private int otherFiles;
    private int exportedActivities;
    private int exportedServices;
    private int exportedReceivers;
    private int exportedProviders;
    private int detectedTrackers;
    private int mobsfAverageCVSS;
    private int securityScore;

    /* === SourceJavaProperty === */

    public String getCSVRepresentation() {
        return exportedActivities + ","
                + browsableActivities + ","
                + activities + ","
                + receivers + ","
                + providers + ","
                + services + ","
                + libraries + ","
                + targetSDK + ","
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
                + deprecatedPermission + ","
                + totalPermissions + ","
                + manifestIssues + ","
                + codeIssues + ","
                + codeHighIssues + ","
                + codeWarningIssues + ","
                + codeInfoIssues + ","
                + codeWarningIssues + ","
                + activitiesWithUrl + ","
                + domains + ","
                + emails + ","
                + firebaseUrls + ","
                + files + ","
                + xmlFiles + ","
                + pngFiles + ","
                + kotlinMetadataFiles + ","
                + versionFiles + ","
                + kotlinBuiltinsFiles + ","
                + proFiles + ","
                + ttfFiles + ","
                + dexFiles + ","
                + gifFiles + ","
                + otherFiles + ","
                + exportedServices + ","
                + exportedReceivers + ","
                + exportedProviders + ","
                + detectedTrackers + ","
                + mobsfAverageCVSS + ","
                + securityScore;
    }

    @Override
    public void setCountBrowsableActivities(int browsableActivities) {
        this.browsableActivities = browsableActivities;
    }

    @Override
    public void setCountActivities(int activities) {
        this.activities = activities;
    }

    @Override
    public void setCountReceivers(int receivers) {
        this.receivers = receivers;
    }

    @Override
    public void setCountProviders(int providers) {
        this.providers = providers;
    }

    @Override
    public void setCountServices(int services) {
        this.services = services;
    }

    @Override
    public void setCountLibraries(int libraries) {
        this.libraries = libraries;
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

    @Override
    public void setCountTotalPermissions(int totalPermissions) {
        this.totalPermissions = totalPermissions;
    }

    @Override
    public void setCountManifestIssues(int manifestIssues) {
        this.manifestIssues = manifestIssues;
    }

    @Override
    public void setCountCodeIssues(int codeIssues) {
        this.codeIssues = codeIssues;
    }

    @Override
    public void setCountCodeHighIssues(int codeHighIssues) {
        this.codeHighIssues = codeHighIssues;
    }

    @Override
    public void setCountCodeWarningIssues(int codeWarningIssues) {
        this.codeWarningIssues = codeWarningIssues;
    }

    @Override
    public void setCountCodeInfoIssues(int codeInfoIssues) {
        this.codeInfoIssues = codeInfoIssues;
    }

    @Override
    public void setCountActivitiesWithUrl(int activitiesWithUrl) {
        this.activitiesWithUrl = activitiesWithUrl;
    }

    @Override
    public void setCountDomains(int domains) {
        this.domains = domains;
    }

    @Override
    public void setCountEmails(int emails) {
        this.emails = emails;
    }

    @Override
    public void setCountFirebaseUrls(int firebaseUrls) {
        this.firebaseUrls = firebaseUrls;
    }

    @Override
    public void setCountFiles(int files) {
        this.files = files;
    }

    @Override
    public void setCountXmlFiles(int xmlFiles) {
        this.xmlFiles = xmlFiles;
    }

    @Override
    public void setCountPngFiles(int pngFiles) {
        this.pngFiles = pngFiles;
    }

    @Override
    public void setCountKotlinMetadataFiles(int kotlinMetadataFiles) {
        this.kotlinMetadataFiles = kotlinMetadataFiles;
    }

    @Override
    public void setCountVersionFiles(int versionFiles) {
        this.versionFiles = versionFiles;
    }

    @Override
    public void setCountKotlinBuiltinsFiles(int kotlinBuiltinsFiles) {
        this.kotlinBuiltinsFiles = kotlinBuiltinsFiles;
    }

    @Override
    public void setCountProFiles(int proFiles) {
        this.proFiles = proFiles;
    }

    @Override
    public void setCountTtfFiles(int ttfFiles) {
        this.ttfFiles = ttfFiles;
    }

    @Override
    public void setCountDexFiles(int dexFiles) {
        this.dexFiles = dexFiles;
    }

    @Override
    public void setCountGifFiles(int gifFiles) {
        this.gifFiles = gifFiles;
    }

    @Override
    public void setCountOtherFiles(int otherFiles) {
        this.otherFiles = otherFiles;
    }

    @Override
    public void setCountExportedActivities(int exportedActivities) {
        this.exportedActivities = exportedActivities;
    }

    @Override
    public void setCountExportedServices(int exportedServices) {
        this.exportedServices = exportedServices;
    }

    @Override
    public void setCountExportedReceivers(int exportedReceivers) {
        this.exportedReceivers = exportedReceivers;
    }

    @Override
    public void setCountExportedProviders(int exportedProviders) {
        this.exportedProviders = exportedProviders;
    }

    @Override
    public void setCountDetectedTrackers(int detectedTrackers) {
        this.detectedTrackers = detectedTrackers;
    }

    @Override
    public void setMobsfAverageCVSS(int mobsfAverageCVSS) {
        this.mobsfAverageCVSS = mobsfAverageCVSS;
    }

    @Override
    public void setSecurityScore(int securityScore) {
        this.securityScore = securityScore;
    }
}
