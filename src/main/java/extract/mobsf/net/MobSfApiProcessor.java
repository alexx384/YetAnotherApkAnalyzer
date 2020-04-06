package extract.mobsf.net;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class MobSfApiProcessor {
    private static final String PATH_UPLOAD_FILE = "/api/v1/upload";
    private static final String PATH_SCAN_FILE = "/api/v1/scan";
    private static final String PATH_DELETE_FILE = "/api/v1/delete_scan";

    private final String serverUrl;
    private final String mobsfApiKey;

    public MobSfApiProcessor(String serverUrl, String mobsfApiKey) {
        this.serverUrl = serverUrl;
        this.mobsfApiKey = mobsfApiKey;
    }

    public String uploadFile(Path filePath) {
        HttpClient httpClient = HttpClient.newHttpClient();
        MultipartBuilder formBuilder = new MultipartBuilder()
                .addFile("file", filePath);
        try {
            HttpRequest httpRequest = HttpRequest
                    .newBuilder(
                            new URI(serverUrl + PATH_UPLOAD_FILE)
                    )
                    .header("Authorization", mobsfApiKey)
                    .header("Content-Type", "multipart/form-data; boundary=" + formBuilder.getBoundary())
                    .POST(formBuilder.build())
                    .build();

            HttpResponse<String> response =
                    httpClient.send(
                            httpRequest,
                            HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
                    );

            if (response.statusCode() != 200) {
                return null;
            }

            JSONObject obj = new JSONObject(response.body());
            return obj.getString("hash");
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public JSONObject scanFile(String fileName, String hash, String scanType) {
        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            HttpRequest httpRequest = HttpRequest
                    .newBuilder(
                            new URI(serverUrl + PATH_SCAN_FILE)
                    )
                    .header("Authorization", mobsfApiKey)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "scan_type=" + scanType + "&file_name=" + fileName + "&hash=" + hash)
                    )
                    .build();

            HttpResponse<String> response =
                    httpClient.send(
                            httpRequest,
                            HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
                    );

            if (response.statusCode() != 200) {
                return null;
            }

            return new JSONObject(response.body());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean deleteScanResult(String hash) {
        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            HttpRequest httpRequest = HttpRequest
                    .newBuilder(
                            new URI(serverUrl + PATH_DELETE_FILE)
                    )
                    .header("Authorization", mobsfApiKey)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "hash=" + hash
                    ))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(
                            httpRequest,
                            HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
                    );

            return response.statusCode() == 200;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
