package extract.mobsf.permission;

import java.sql.*;

public class PermissionClassifier implements AutoCloseable {
    private static final String DB_DISK_PATH_PREFIX = "jdbc:sqlite:";

    private final Connection dbConnection;

    public PermissionClassifier(String dbPathOnDisk) throws SQLException {
        String dbPath = DB_DISK_PATH_PREFIX + dbPathOnDisk;

        dbConnection = DriverManager.getConnection(dbPath);
    }

    public int getProtectionGroupByName(String permissionName) throws SQLException {
        String query = "SELECT protection FROM permissions WHERE name = \"" + permissionName + "\"";
        int result;
        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                result = rs.getInt("protection");
            } else {
                result = AndroidPermissionGroup.DEPRECATED.ordinal();
            }
        }

        return result;
    }

    @Override
    public void close() throws Exception {
        dbConnection.close();
    }
}
