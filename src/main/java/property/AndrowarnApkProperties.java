package property;

public enum AndrowarnApkProperties {
    TELEPHONY_IDENTIFIERS_LEAKAGE("telephony_identifiers_leakage"),
    DEVICE_SETTINGS_HARVESTING("device_settings_harvesting"),
    LOCATION_LOOKUP("location_lookup"),
    CONNECTION_INTERFACES_EXFILTRATION("connection_interfaces_exfiltration"),
    TELEPHONY_SERVICES_ABUSE("telephony_services_abuse"),
    AUDIO_VIDEO_EAVESDROPPING("audio_video_eavesdropping"),
    SUSPICIOUS_CONNECTION_ESTABLISHMENT("suspicious_connection_establishment"),
    PIM_DATA_LEAKAGE("PIM_data_leakage"),
    CODE_EXECUTION("code_execution"),
    CLASSES_LIST("classes_list"),
    INTERNAL_CLASSES_LIST("internal_classes_list"),
    INTENTS_SENT("intents_sent");

    public static final int length = AndrowarnApkProperties.values().length;

    public final String name;

    AndrowarnApkProperties(String name) {
        this.name = name;
    }
}
