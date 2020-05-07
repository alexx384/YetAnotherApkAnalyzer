package property;

public enum SourceConstructorProperties {
    STRING("String"),
    STRINGBUILDER("StringBuilder"),
    STRINGBUFFER("StringBuffer"),
    FILE("File"),
    DATAINPUTSTREAM("DataInputStream"),
    DATAOUTPUTSTREAM("DataOutputStream"),
    FILEINPUTSTREAM("FileInputStream"),
    FILEOUTPUTSTREAM("FileOutputStream"),
    BUFFEREDREADER("BufferedReader"),
    BUFFEREDWRITER("BufferedWriter"),
    INETSOCKETADDRESS("InetSocketAddress"),
    STREAM("Stream"),
    TIMER("Timer"),
    TIMERTASK("TimerTask"),
    ACTIVITY("Activity"),
    SERVICE("Service"),
    INTENT("Intent"),
    INTENTFILTER("IntentFilter"),
    CONTENTRESOLVER("ContentResolver"),
    CONTEXT("Context"),
    PACKAGEMANAGER("PackageManager"),
    APPLICATIONINFO("ApplicationInfo"),
    NETWORKINFO("NetworkInfo"),
    CONNECTIVITYMANAGER("ConnectivityManager"),
    BUNDLE("Bundle"),
    TELEPHONYMANAGER("TelephonyManager"),
    SMSMANAGER("SmsManager"),
    BROWSER("Browser"),
    GOOGLEACCOUNTLOGIN("GoogleAccountLogin"),
    PATHPERMISSIONSHELPER("PathPermissionsHelper"),
    DAYVIEW("DayView"),
    VIDEOVIEW("VideoView"),
    WEBVIEW("WebView"),
    FRAGMENT("Fragment");

    public static final int length = SourceConstructorProperties.values().length;

    public final String name;

    SourceConstructorProperties(String name) {
        this.name = name;
    }
}
