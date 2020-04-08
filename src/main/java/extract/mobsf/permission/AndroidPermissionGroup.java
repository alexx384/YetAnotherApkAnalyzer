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
    public int value;

    AndroidPermissionGroup(int value) {
        this.value = value;
    }
}
