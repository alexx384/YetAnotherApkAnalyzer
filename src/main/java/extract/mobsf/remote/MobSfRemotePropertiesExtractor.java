package extract.mobsf.remote;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;

public class MobSfRemotePropertiesExtractor {

    private static final String HTTP_ADDRESS_HEADER = "http://";
    private static final String DEFAULT_SCAN_TYPE = "apk";

    private final String mobsfAddress;
    private final String mobsfApiKey;

    private MobSfRemotePropertiesExtractor(String mobsfAddress, String mobsfApiKey) {

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

    public static MobSfRemotePropertiesExtractor build(String mobsfAddress, String mobsfApiKey) {

        int delimiterIndex = mobsfAddress.indexOf(':');
        try {

            String ipAddress = mobsfAddress.substring(0, delimiterIndex);
            int port = Integer.parseInt(
                    mobsfAddress.substring(delimiterIndex + 1)
            );
            if (isAddressReachable(ipAddress, port)) {
                return new MobSfRemotePropertiesExtractor(mobsfAddress, mobsfApiKey);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String extract(Path apkFilePath) {
        MobSfRemoteApiProcessor processor = new MobSfRemoteApiProcessor(
                HTTP_ADDRESS_HEADER + mobsfAddress,
                mobsfApiKey
        );
        String hash = processor.uploadFile(apkFilePath);
        if (hash == null) {
            return null;
        }

        return processor.scanFile(apkFilePath.getFileName().toString(), hash, DEFAULT_SCAN_TYPE);
    }
}
