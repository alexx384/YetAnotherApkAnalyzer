package extract.mobsf.permission;

public enum AndroidPermissionGroup {
    DANGEROUS(1),
    SIGNATURE(2),
    APPOP(4),
    PRIVILEGED(8),
    DEVELOPMENT(16),
    NORMAL(32),
    INSTANT(64),
    PREINSTALLED(128),
    RETAILDEMO(256),
    INSTALLER(512),
    PRE23(1024),
    UNUSED(2048),
    DEPRECATED(4096);

    private static final AndroidPermissionGroup[] permissionGroups = AndroidPermissionGroup.values();
    public static final int length = permissionGroups.length;

    public final int value;

    AndroidPermissionGroup(int value) {
        this.value = value;
    }

    public static void decomposePermission(String permission, int[] permissionGroupValues) {
        int groupValue = getPermissionValue(permission);
        for (int i = 0; i < permissionGroups.length; i++) {
            if ((permissionGroups[i].value & groupValue) == permissionGroups[i].value) {
                permissionGroupValues[i] += 1;
            }
        }
    }

    private static int getPermissionValue(String permission) {
        switch (permission) {
            case "ACCEPT_HANDOVER":
            case "ACCESS_MEDIA_LOCATION":
            case "ACCESS_COARSE_LOCATION":
            case "ACCESS_FINE_LOCATION":
            case "ANSWER_PHONE_CALLS":
            case "ADD_VOICEMAIL":
            case "ACTIVITY_RECOGNITION":
            case "ACCESS_BACKGROUND_LOCATION":
            case "BODY_SENSORS":
            case "CALL_PHONE":
            case "CAMERA":
            case "GET_ACCOUNTS":
            case "PROCESS_OUTGOING_CALLS":
            case "READ_CALL_LOG":
            case "READ_CALENDAR":
            case "READ_CONTACTS":
            case "READ_EXTERNAL_STORAGE":
            case "READ_PHONE_NUMBERS":
            case "READ_PHONE_STATE":
            case "READ_SMS":
            case "RECEIVE_MMS":
            case "RECEIVE_SMS":
            case "RECEIVE_WAP_PUSH":
            case "RECORD_AUDIO":
            case "SEND_SMS":
            case "WRITE_CALENDAR":
            case "USE_SIP":
            case "WRITE_CALL_LOG":
            case "WRITE_CONTACTS":
            case "WRITE_EXTERNAL_STORAGE":
                return 1;
            case "ACCESS_CALL_AUDIO":
                return 6;
            case "ACCESS_CHECKIN_PROPERTIES":
                return 2048;
            case "ACCESS_LOCATION_EXTRA_COMMANDS":
                return 32;
            case "ACCESS_NETWORK_STATE":
                return 32;
            case "ACCESS_NOTIFICATION_POLICY":
                return 32;
            case "ACCESS_WIFI_STATE":
                return 32;
            case "ACCOUNT_MANAGER":
                return 2048;
            case "BATTERY_STATS":
                return 26;
            case "BIND_ACCESSIBILITY_SERVICE":
                return 2;
            case "BIND_APPWIDGET":
                return 2048;
            case "BIND_AUTOFILL_SERVICE":
                return 2;
            case "BIND_CALL_REDIRECTION_SERVICE":
                return 10;
            case "BIND_CARRIER_MESSAGING_CLIENT_SERVICE":
                return 2;
            case "BIND_CARRIER_MESSAGING_SERVICE":
                return 4096;
            case "BIND_CARRIER_SERVICES":
                return 10;
            case "BIND_CHOOSER_TARGET_SERVICE":
                return 2;
            case "BIND_CONDITION_PROVIDER_SERVICE":
                return 2;
            case "BIND_CONTROLS":
                return 2048;
            case "BIND_DEVICE_ADMIN":
                return 2;
            case "BIND_DREAM_SERVICE":
                return 2;
            case "BIND_INCALL_SERVICE":
                return 10;
            case "BIND_INPUT_METHOD":
                return 2;
            case "BIND_MIDI_DEVICE_SERVICE":
                return 2;
            case "BIND_NFC_SERVICE":
                return 2;
            case "BIND_NOTIFICATION_LISTENER_SERVICE":
                return 2;
            case "BIND_PRINT_SERVICE":
                return 2;
            case "BIND_QUICK_ACCESS_WALLET_SERVICE":
                return 2;
            case "BIND_QUICK_SETTINGS_TILE":
                return 2048;
            case "BIND_REMOTEVIEWS":
                return 10;
            case "BIND_SCREENING_SERVICE":
                return 10;
            case "BIND_TELECOM_CONNECTION_SERVICE":
                return 10;
            case "BIND_TEXT_SERVICE":
                return 2;
            case "BIND_TV_INPUT":
                return 10;
            case "BIND_VISUAL_VOICEMAIL_SERVICE":
                return 10;
            case "BIND_VOICE_INTERACTION":
                return 2;
            case "BIND_VPN_SERVICE":
                return 2;
            case "BIND_VR_LISTENER_SERVICE":
                return 2;
            case "BIND_WALLPAPER":
                return 10;
            case "BLUETOOTH":
                return 32;
            case "BLUETOOTH_ADMIN":
                return 32;
            case "BLUETOOTH_PRIVILEGED":
                return 2048;
            case "BROADCAST_PACKAGE_REMOVED":
                return 2048;
            case "BROADCAST_SMS":
                return 2048;
            case "BROADCAST_STICKY":
                return 32;
            case "BROADCAST_WAP_PUSH":
                return 2048;
            case "CALL_COMPANION_APP":
                return 32;
            case "CALL_PRIVILEGED":
                return 2048;
            case "CAPTURE_AUDIO_OUTPUT":
                return 2048;
            case "CHANGE_COMPONENT_ENABLED_STATE":
                return 2048;
            case "CHANGE_CONFIGURATION":
                return 26;
            case "CHANGE_NETWORK_STATE":
                return 32;
            case "CHANGE_WIFI_MULTICAST_STATE":
                return 32;
            case "CHANGE_WIFI_STATE":
                return 32;
            case "CLEAR_APP_CACHE":
                return 10;
            case "CONTROL_LOCATION_UPDATES":
                return 2048;
            case "DELETE_CACHE_FILES":
                return 10;
            case "DELETE_PACKAGES":
                return 2048;
            case "DIAGNOSTIC":
                return 2048;
            case "DISABLE_KEYGUARD":
                return 32;
            case "DUMP":
                return 2048;
            case "EXPAND_STATUS_BAR":
                return 32;
            case "FACTORY_TEST":
                return 2048;
            case "FOREGROUND_SERVICE":
                return 32;
            case "GET_ACCOUNTS_PRIVILEGED":
                return 10;
            case "GET_PACKAGE_SIZE":
                return 32;
            case "GET_TASKS":
                return 4096;
            case "GLOBAL_SEARCH":
                return 10;
            case "INSTALL_LOCATION_PROVIDER":
                return 2048;
            case "INSTALL_PACKAGES":
                return 2048;
            case "INSTALL_SHORTCUT":
                return 32;
            case "INSTANT_APP_FOREGROUND_SERVICE":
                return 86;
            case "INTERACT_ACROSS_PROFILES":
                return 2048;
            case "INTERNET":
                return 32;
            case "KILL_BACKGROUND_PROCESSES":
                return 32;
            case "LOADER_USAGE_STATS":
                return 14;
            case "LOCATION_HARDWARE":
                return 2048;
            case "MANAGE_DOCUMENTS":
                return 2048;
            case "MANAGE_EXTERNAL_STORAGE":
                return 134;
            case "MANAGE_OWN_CALLS":
                return 32;
            case "MASTER_CLEAR":
                return 2048;
            case "MEDIA_CONTENT_CONTROL":
                return 2048;
            case "MODIFY_AUDIO_SETTINGS":
                return 32;
            case "MODIFY_PHONE_STATE":
                return 2048;
            case "MOUNT_FORMAT_FILESYSTEMS":
                return 2048;
            case "MOUNT_UNMOUNT_FILESYSTEMS":
                return 2048;
            case "NFC":
                return 32;
            case "NFC_PREFERRED_PAYMENT_INFO":
                return 32;
            case "NFC_TRANSACTION_EVENT":
                return 32;
            case "PACKAGE_USAGE_STATS":
                return 286;
            case "PERSISTENT_ACTIVITY":
                return 4096;
            case "QUERY_ALL_PACKAGES":
                return 2048;
            case "READ_INPUT_STATE":
                return 4096;
            case "READ_LOGS":
                return 2048;
            case "READ_PRECISE_PHONE_STATE":
                return 2048;
            case "READ_SYNC_SETTINGS":
                return 32;
            case "READ_SYNC_STATS":
                return 32;
            case "READ_VOICEMAIL":
                return 10;
            case "REBOOT":
                return 2048;
            case "RECEIVE_BOOT_COMPLETED":
                return 32;
            case "REORDER_TASKS":
                return 32;
            case "REQUEST_COMPANION_RUN_IN_BACKGROUND":
                return 32;
            case "REQUEST_COMPANION_USE_DATA_IN_BACKGROUND":
                return 32;
            case "REQUEST_DELETE_PACKAGES":
                return 32;
            case "REQUEST_IGNORE_BATTERY_OPTIMIZATIONS":
                return 32;
            case "REQUEST_INSTALL_PACKAGES":
                return 2;
            case "REQUEST_PASSWORD_COMPLEXITY":
                return 32;
            case "RESTART_PACKAGES":
                return 4096;
            case "SEND_RESPOND_VIA_MESSAGE":
                return 2048;
            case "SET_ALARM":
                return 32;
            case "SET_ALWAYS_FINISH":
                return 2048;
            case "SET_ANIMATION_SCALE":
                return 2048;
            case "SET_DEBUG_APP":
                return 2048;
            case "SET_PREFERRED_APPLICATIONS":
                return 4096;
            case "SET_PROCESS_LIMIT":
                return 2048;
            case "SET_TIME":
                return 2048;
            case "SET_TIME_ZONE":
                return 2048;
            case "SET_WALLPAPER":
                return 32;
            case "SET_WALLPAPER_HINTS":
                return 32;
            case "SIGNAL_PERSISTENT_PROCESSES":
                return 2048;
            case "SMS_FINANCIAL_TRANSACTIONS":
                return 6;
            case "START_VIEW_PERMISSION_USAGE":
                return 514;
            case "STATUS_BAR":
                return 2048;
            case "SYSTEM_ALERT_WINDOW":
                return 1174;
            case "TRANSMIT_IR":
                return 32;
            case "UNINSTALL_SHORTCUT":
                return 4096;
            case "UPDATE_DEVICE_STATS":
                return 2048;
            case "USE_BIOMETRIC":
                return 32;
            case "USE_FINGERPRINT":
                return 32;
            case "USE_FULL_SCREEN_INTENT":
                return 32;
            case "VIBRATE":
                return 32;
            case "WAKE_LOCK":
                return 32;
            case "WRITE_APN_SETTINGS":
                return 2048;
            case "WRITE_GSERVICES":
                return 2048;
            case "WRITE_SECURE_SETTINGS":
                return 2048;
            case "WRITE_SETTINGS":
                return 1158;
            case "WRITE_SYNC_SETTINGS":
                return 32;
            case "WRITE_VOICEMAIL":
                return 10;
            default:
                return DEPRECATED.value;
        }
    }
}
