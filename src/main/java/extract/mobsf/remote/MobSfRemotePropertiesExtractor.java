package extract.mobsf.remote;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class MobSfRemotePropertiesExtractor {

    private static final String HTTP_ADDRESS_HEADER = "http://";
    private static final String DEFAULT_SCAN_TYPE = "apk";

    private final String mobsfAddress;
    private final String mobsfApiKey;
    private final ZipExtractor zipExtractor;

    private MobSfRemotePropertiesExtractor(String mobsfAddress, String mobsfApiKey) {

        this.mobsfAddress = mobsfAddress;
        this.mobsfApiKey = mobsfApiKey;
        this.zipExtractor = new ZipExtractor();
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

    public String extract(Path apkFilePath, String sourceDirName) {
        MobSfRemoteApiProcessor processor = new MobSfRemoteApiProcessor(
                HTTP_ADDRESS_HEADER + mobsfAddress,
                mobsfApiKey
        );
        String hash = processor.uploadFile(apkFilePath);
        if (hash == null) {
            return null;
        }

        String resultJsonMessage = processor.scanFile(apkFilePath.getFileName().toString(), hash, DEFAULT_SCAN_TYPE);
        if (resultJsonMessage == null) {
            return null;
        }

        String zipFilePath = "testFiles/" + apkFilePath.getFileName().toString() + ".zip";
        if (!processor.downloadFile(hash, zipFilePath)) {
            return null;
        }

        if (!zipExtractor.extractToFolder(zipFilePath, "testFiles/" + sourceDirName)) {
            return null;
        } else {
            try {
                Files.delete(Path.of(zipFilePath));
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

        if (processor.deleteScanResult(hash)) {
            return resultJsonMessage;
        } else {
            return null;
        }
    }
}
