package extract.mobsf.remote;

import extract.mobsf.local.MobSfLocalPropertiesExtractor;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class MobSfRemotePropertiesExtractor {
    public static final String ZIP_FILE_PREFIX = ".zip";

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
                System.err.println("Error the MobSf ip address is unreachable");
                return null;
            }
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
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
            System.err.println("Could not upload file to MobSf");
            return null;
        }

        String apkFileName = apkFilePath.getFileName().toString();
        String resultJsonMessage = processor.scanFile(apkFilePath.getFileName().toString(), hash, DEFAULT_SCAN_TYPE);
        if (resultJsonMessage != null) {
            try {
                Files.writeString(Path.of(apkFileName + MobSfLocalPropertiesExtractor.JSON_PROPERTIES_EXTENSION),
                        resultJsonMessage);
            } catch (IOException e) {
                System.err.println("Could not save json report file");
                return null;
            }
        } else {
            System.err.println("Could not get scan file json report");
            return null;
        }

        String zipFileName = apkFilePath.getFileName().toString() + ZIP_FILE_PREFIX;
        if (!processor.downloadFile(hash, zipFileName)) {
            System.err.println("Could not download file with source files");
            return null;
        }

        if (!processor.deleteScanResult(hash)) {
            System.err.println("Warning: Could not delete scan result");
        }
        return resultJsonMessage;
    }
}
