package extract.mobsf;

import extract.mobsf.net.MobSfApiProcessor;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;

public class MobSfManager {

    private static final String HTTP_ADDRESS_HEADER = "http://";
    private static final String DEFAULT_SCAN_TYPE = "apk";

    private final String mobsfAddress;
    private final String mobsfApiKey;

    private MobSfManager(String mobsfAddress, String mobsfApiKey) {

        this.mobsfAddress = mobsfAddress;
        this.mobsfApiKey = mobsfApiKey;
    }

    public static boolean isAddressReachable(String host, int port) {
        try (Socket ignored = new Socket(host, port)) {
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static MobSfManager build(String mobsfAddress, String mobsfApiKey) {

        int delimiterIndex = mobsfAddress.indexOf(':');
        try {

            String ipAddress = mobsfAddress.substring(0, delimiterIndex);
            int port = Integer.parseInt(
                    mobsfAddress.substring(delimiterIndex + 1)
            );
            if (isAddressReachable(ipAddress, port)) {
                return new MobSfManager(mobsfAddress, mobsfApiKey);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public MobSfApkProperty extract(Path apkFilePath) {
        MobSfApiProcessor processor = new MobSfApiProcessor(
                HTTP_ADDRESS_HEADER + mobsfAddress,
                mobsfApiKey
        );
        String hash = processor.uploadFile(apkFilePath);
        if (hash == null) {
            return null;
        }

        JSONObject scanObject = processor.scanFile(apkFilePath.getFileName().toString(), hash, DEFAULT_SCAN_TYPE);
        if (scanObject == null) {
            return null;
        }

        return MobSfApkProperty.extract(scanObject);
    }
}
