package extract.mobsf;

import extract.PropertiesWriter;
import org.json.JSONObject;

import java.io.IOException;
import java.io.SequenceInputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Arrays;

public class MobSfExtractor {

    private static final String HTTP_ADDRESS_HEADER = "http://";

    private final String mobsfAddress;
    private final String mobsfApiKey;

    private MobSfExtractor(String mobsfAddress, String mobsfApiKey) {

        this.mobsfAddress = mobsfAddress;
        this.mobsfApiKey = mobsfApiKey;
    }

    public static boolean isAddressReachable(String host, int port) {
        try (Socket s = new Socket(host, port)) {
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static MobSfExtractor build(String mobsfAddress, String mobsfApiKey) {

        int delimiterIndex = mobsfAddress.indexOf(':');
        try {

            String ipAddress = mobsfAddress.substring(0, delimiterIndex);
            int port = Integer.parseInt(
                    mobsfAddress.substring(delimiterIndex + 1, mobsfAddress.length())
            );
            if (isAddressReachable(ipAddress, port)) {
                return new MobSfExtractor(mobsfAddress, mobsfApiKey);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean extract(Path apkFilePath, PropertiesWriter propertiesWriter) {

        return false;
//        HttpClient httpClient = HttpClient.newHttpClient();
//        try {
//            HttpRequest httpRequest = HttpRequest
//                    .newBuilder(
//                            new URI(HTTP_ADDRESS_HEADER + mobsfAddress + CMD_UPLOAD_FILE)
//                    )
//                    .POST(
//                            HttpRequest.BodyPublishers.ofFile(apkFilePath)
//                    )
//                    .header("Authorization", mobsfApiKey)
//                    .build();
//
//            HttpResponse<byte[]> response =
//                    httpClient.send(
//                            httpRequest,
//                            HttpResponse.BodyHandlers.ofByteArray()
//                    );
//            if (response.statusCode() != 200) {
//                return false;
//            }
//            JSONObject obj = new JSONObject(Arrays.toString(response.body()));
//            String hash = obj.getString("hash");
//            System.out.println(hash);
//            return true;
//        } catch (URISyntaxException | IOException | InterruptedException e) {
//            System.out.println(e.getMessage());
//        }
//        return true;
    }
}
