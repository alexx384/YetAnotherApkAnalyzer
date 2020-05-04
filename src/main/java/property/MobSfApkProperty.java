package property;

import lombok.Setter;

public class MobSfApkProperty implements ApkProperty {
    @Setter private int browsableActivities;
    @Setter private int activities;
    @Setter private int receivers;
    @Setter private int providers;
    @Setter private int services;
    @Setter private int libraries;
    @Setter private int targetSDK;
    @Setter private int maxSDK;
    @Setter private int minSDK;
    @Setter private int versionCode;
    @Setter private int dangerousPermissions;
    @Setter private int signaturePermissions;
    @Setter private int appopPermissions;
    @Setter private int privilegedPermissions;
    @Setter private int developmentPermissions;
    @Setter private int normalPermissions;
    @Setter private int instantPermissions;
    @Setter private int preinstalledPermissions;
    @Setter private int retailDemoPermissions;
    @Setter private int installerPermissions;
    @Setter private int pre23Permissions;
    @Setter private int unusedPermissions;
    @Setter private int deprecatedPermissions;
    @Setter private int totalPermissions;
    @Setter private int manifestIssues;
    @Setter private int codeIssues;
    @Setter private int codeHighIssues;
    @Setter private int codeWarningIssues;
    @Setter private int codeInfoIssues;
    @Setter private int activitiesWithUrl;
    @Setter private int domains;
    @Setter private int emails;
    @Setter private int firebaseUrls;
    @Setter private int files;
    @Setter private int xmlFiles;
    @Setter private int pngFiles;
    @Setter private int kotlinMetadataFiles;
    @Setter private int versionFiles;
    @Setter private int kotlinBuiltinsFiles;
    @Setter private int proFiles;
    @Setter private int ttfFiles;
    @Setter private int dexFiles;
    @Setter private int gifFiles;
    @Setter private int otherFiles;
    @Setter private int exportedActivities;
    @Setter private int exportedServices;
    @Setter private int exportedReceivers;
    @Setter private int exportedProviders;
    @Setter private int detectedTrackers;
    @Setter private int mobsfAverageCVSS;
    @Setter private int securityScore;

    @Override
    public StringBuilder toBuilder(StringBuilder builder) {
        return builder.append(exportedActivities).append(',')
                .append(browsableActivities).append(',')
                .append(activities).append(',')
                .append(receivers).append(',')
                .append(providers).append(',')
                .append(services).append(',')
                .append(libraries).append(',')
                .append(targetSDK).append(',')
                .append(maxSDK).append(',')
                .append(minSDK).append(',')
                .append(versionCode).append(',')
                .append(dangerousPermissions).append(',')
                .append(signaturePermissions).append(',')
                .append(appopPermissions).append(',')
                .append(privilegedPermissions).append(',')
                .append(developmentPermissions).append(',')
                .append(normalPermissions).append(',')
                .append(instantPermissions).append(',')
                .append(preinstalledPermissions).append(',')
                .append(retailDemoPermissions).append(',')
                .append(installerPermissions).append(',')
                .append(pre23Permissions).append(',')
                .append(unusedPermissions).append(',')
                .append(deprecatedPermissions).append(',')
                .append(totalPermissions).append(',')
                .append(manifestIssues).append(',')
                .append(codeIssues).append(',')
                .append(codeHighIssues).append(',')
                .append(codeWarningIssues).append(',')
                .append(codeInfoIssues).append(',')
                .append(codeWarningIssues).append(',')
                .append(activitiesWithUrl).append(',')
                .append(domains).append(',')
                .append(emails).append(',')
                .append(firebaseUrls).append(',')
                .append(files).append(',')
                .append(xmlFiles).append(',')
                .append(pngFiles).append(',')
                .append(kotlinMetadataFiles).append(',')
                .append(versionFiles).append(',')
                .append(kotlinBuiltinsFiles).append(',')
                .append(proFiles).append(',')
                .append(ttfFiles).append(',')
                .append(dexFiles).append(',')
                .append(gifFiles).append(',')
                .append(otherFiles).append(',')
                .append(exportedServices).append(',')
                .append(exportedReceivers).append(',')
                .append(exportedProviders).append(',')
                .append(detectedTrackers).append(',')
                .append(mobsfAverageCVSS).append(',')
                .append(securityScore);
    }
}
