package property;

public enum SourceImportProperties {
    JAVA_IO_FILE("java.io.File"),
    JAVA_IO_DATAINPUTSTREAM("java.io.DataInputStream"),
    JAVA_IO_DATAOUTPUTSTREAM("java.io.DataOutputStream"),
    JAVA_IO_FILEINPUTSTREAM("java.io.FileInputStream"),
    JAVA_IO_FILEOUTPUTSTREAM("java.io.FileOutputStream"),
    JAVA_IO_BUFFEREDREADER("java.io.BufferedReader"),
    JAVA_IO_BUFFEREDWRITER("java.io.BufferedWriter"),
    JAVA_NET_INETSOCKETADDRESS("java.net.InetSocketAddress"),
    JAVA_UTIL_STREAM_STREAM("java.util.stream.Stream"),
    JAVA_UTIL_TIMER("java.util.Timer"),
    JAVA_UTIL_TIMERTASK("java.util.TimerTask"),
    ANDROID_APP_ACTIVITY("android.app.Activity"),
    ANDROID_APP_SERVICE("android.app.Service"),
    ANDROID_CONTENT_INTENT("android.content.Intent"),
    ANDROID_CONTENT_INTENTFILTER("android.content.IntentFilter"),
    ANDROID_CONTENT_CONTENTRESOLVER("android.content.ContentResolver"),
    ANDROID_CONTENT_CONTEXT("android.content.Context"),
    ANDROID_CONTENT_PM_PACKAGEMANAGER("android.content.pm.PackageManager"),
    ANDROID_CONTENT_PM_APPLICATIONINFO("android.content.pm.ApplicationInfo"),
    ANDROID_NET_NETWORKINFO("android.net.NetworkInfo"),
    ANDROID_NET_CONNECTIVITYMANAGER("android.net.ConnectivityManager"),
    ANDROID_OS_BUNDLE("android.os.Bundle"),
    ANDROID_TELEPHONY_TELEPHONYMANAGER("android.telephony.TelephonyManager"),
    ANDROID_TELEPHONY_SMSMANAGER("android.telephony.SmsManager"),
    ANDROID_UTIL_LOG("android.util.Log");


    public static final int length = SourceImportProperties.values().length;

    public final String name;

    SourceImportProperties(String name) {
        this.name = name;
    }
}
