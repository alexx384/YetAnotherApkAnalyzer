package extract.mobsf.permission;

public class PermissionClassifier {
    private static final String DB_DISK_PATH_PREFIX = "jdbc:sqlite:";
    private static final String DEFAULT_DB_PATH = "db/permission.db";

    private final String dbPath;

    public PermissionClassifier(String dbPathOnDisk) {
        this.dbPath = DB_DISK_PATH_PREFIX + dbPathOnDisk;
    }

    public PermissionClassifier() {
        this(DB_DISK_PATH_PREFIX + DEFAULT_DB_PATH);
    }

    public static void main(String[] args) {

    }
}
