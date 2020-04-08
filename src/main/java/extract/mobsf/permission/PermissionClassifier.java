package extract.mobsf.permission;

import java.io.Closeable;
import java.io.File;
import java.sql.*;

public class PermissionClassifier implements AutoCloseable {
    private static final String DB_DISK_PATH_PREFIX = "jdbc:sqlite:";
    private static final String DEFAULT_DB_PATH = System.getProperty("user.dir") + File.separator +
            "db" + File.separator + "permission.db";

    private final Connection dbConnection;

    public PermissionClassifier(String dbPathOnDisk) throws SQLException {
        String dbPath = DB_DISK_PATH_PREFIX + dbPathOnDisk;

        dbConnection = DriverManager.getConnection(dbPath);
    }

    public PermissionClassifier() throws SQLException {
        this(DEFAULT_DB_PATH);
    }

    public int getProtectionGroupByName(String permissionName) throws SQLException {
        String query = "SELECT protection FROM permissions WHERE name = \"" + permissionName + "\"";
        int result;
        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                result = rs.getInt("protection");
            } else {
                throw new SQLException("Could not get protection field of the permission " + permissionName);
            }
        }

        return result;
    }

    @Override
    public void close() throws Exception {
        dbConnection.close();
    }
}
