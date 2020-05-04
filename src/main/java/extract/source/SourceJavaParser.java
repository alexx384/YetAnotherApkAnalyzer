package extract.source;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import property.SourceImportJavaProperty;

import java.util.List;
import java.util.Map;

class SourceJavaParser {
    private final Map<String, MutableInteger> importsMap;

    public SourceJavaParser() {
        importsMap = Map.ofEntries(
                Map.entry("java.io.File", new MutableInteger()),
                Map.entry("java.io.DataInputStream", new MutableInteger()),
                Map.entry("java.io.DataOutputStream", new MutableInteger()),
                Map.entry("java.io.FileInputStream", new MutableInteger()),
                Map.entry("java.io.FileOutputStream", new MutableInteger()),
                Map.entry("java.io.BufferedReader", new MutableInteger()),
                Map.entry("java.io.BufferedWriter", new MutableInteger()),
                Map.entry("java.net.InetSocketAddress", new MutableInteger()),
                Map.entry("java.util.stream.Stream", new MutableInteger()),
                Map.entry("java.util.Timer", new MutableInteger()),
                Map.entry("java.util.TimerTask", new MutableInteger()),
                Map.entry("android.app.Activity", new MutableInteger()),
                Map.entry("android.app.Service", new MutableInteger()),
                Map.entry("android.content.Intent", new MutableInteger()),
                Map.entry("android.content.IntentFilter", new MutableInteger()),
                Map.entry("android.content.ContentResolver", new MutableInteger()),
                Map.entry("android.content.Context", new MutableInteger()),
                Map.entry("android.content.pm.PackageManager", new MutableInteger()),
                Map.entry("android.content.pm.ApplicationInfo", new MutableInteger()),
                Map.entry("android.net.NetworkInfo", new MutableInteger()),
                Map.entry("android.net.ConnectivityManager", new MutableInteger()),
                Map.entry("android.os.Bundle", new MutableInteger()),
                Map.entry("android.telephony.TelephonyManager", new MutableInteger()),
                Map.entry("android.telephony.SmsManager", new MutableInteger()),
                Map.entry("android.util.Log", new MutableInteger())
        );
    }

    public void extractInfo(CompilationUnit cu) {
        processImports(cu.getImports());
    }

    public void exportInProperties(SourceImportJavaProperty property) {
        property.setFileImports(importsMap.get("java.io.File").intValue());
        property.setDataInputStreamImports(importsMap.get("java.io.DataInputStream").intValue());
        property.setDataOutputStreamImports(importsMap.get("java.io.DataOutputStream").intValue());
        property.setFileInputStreamImports(importsMap.get("java.io.FileInputStream").intValue());
        property.setFileOutputStreamImports(importsMap.get("java.io.FileOutputStream").intValue());
        property.setBufferedReaderImports(importsMap.get("java.io.BufferedReader").intValue());
        property.setBufferedWriterImports(importsMap.get("java.io.BufferedWriter").intValue());
        property.setInetSocketAddressImports(importsMap.get("java.net.InetSocketAddress").intValue());
        property.setStreamImports(importsMap.get("java.util.stream.Stream").intValue());
        property.setTimerImports(importsMap.get("java.util.Timer").intValue());
        property.setTimerTaskImports(importsMap.get("java.util.TimerTask").intValue());
        property.setActivityImports(importsMap.get("android.app.Activity").intValue());
        property.setServiceImports(importsMap.get("android.app.Service").intValue());
        property.setIntentImports(importsMap.get("android.content.Intent").intValue());
        property.setIntentFilterImports(importsMap.get("android.content.IntentFilter").intValue());
        property.setContentResolverImports(importsMap.get("android.content.ContentResolver").intValue());
        property.setContextImports(importsMap.get("android.content.Context").intValue());
        property.setPackageManagerImports(importsMap.get("android.content.pm.PackageManager").intValue());
        property.setApplicationInfoImports(importsMap.get("android.content.pm.ApplicationInfo").intValue());
        property.setNetworkInfoImports(importsMap.get("android.net.NetworkInfo").intValue());
        property.setConnectivityManagerImports(importsMap.get("android.net.ConnectivityManager").intValue());
        property.setBundleImports(importsMap.get("android.os.Bundle").intValue());
        property.setTelephonyManagerImports(importsMap.get("android.telephony.TelephonyManager").intValue());
        property.setSmsManagerImports(importsMap.get("android.telephony.SmsManager").intValue());
        property.setLogImports(importsMap.get("android.util.Log").intValue());

        clearCounters();
    }

    private void processImports(List<ImportDeclaration> importDeclarations) {
        for (ImportDeclaration importDeclaration : importDeclarations) {
            MutableInteger counter = importsMap.get(importDeclaration.getNameAsString());
            if (counter != null) {
                counter.increment();
            }
        }
    }

    private void clearCounters() {
        for (MutableInteger counter : importsMap.values()) {
            counter.clear();
        }
    }
}
