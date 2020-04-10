package extract.mobsf.remote;

import extract.mobsf.remote.net.PostDataMultipartBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Optional;

public class MobSfRemoteApiProcessor {
    private static final String PATH_UPLOAD_FILE = "/api/v1/upload";
    private static final String PATH_SCAN_FILE = "/api/v1/scan";
    private static final String PATH_DELETE_FILE = "/api/v1/delete_scan";
    private static final String PATH_GENERATE_DOWNLOADS = "/generate_downloads/?hash=";
    private static final String GENERATE_DOWNLOADS_POSTFIX = "&file_type=java";

    private final String serverUrl;
    private final String mobsfApiKey;

    public MobSfRemoteApiProcessor(String serverUrl, String mobsfApiKey) {
        this.serverUrl = serverUrl;
        this.mobsfApiKey = mobsfApiKey;
    }

    public String uploadFile(Path filePath) {
        HttpClient httpClient = HttpClient.newHttpClient();
        PostDataMultipartBuilder formBuilder = new PostDataMultipartBuilder()
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

    public String scanFile(String fileName, String hash, String scanType) {
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

            return response.body();
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

    public boolean downloadFile(String hash, String filePath) {
        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            HttpRequest httpGenerateDownloadRequest = HttpRequest
                    .newBuilder(
                            new URI(serverUrl + PATH_GENERATE_DOWNLOADS + hash + GENERATE_DOWNLOADS_POSTFIX)
                    )
                    .GET()
                    .build();

            HttpResponse<Void> generateDownloadsResponse =
                    httpClient.send(
                            httpGenerateDownloadRequest,
                            HttpResponse.BodyHandlers.discarding()
                    );
            Optional<String> location = generateDownloadsResponse.headers().firstValue("Location");
            if (generateDownloadsResponse.statusCode() != 302 || location.isEmpty()) {
                return false;
            }

            HttpRequest httpDownloadRequest = HttpRequest
                    .newBuilder(
                            new URI(serverUrl + location.get())
                    )
                    .GET()
                    .build();
            HttpResponse<Path> downloadResponse =
                    httpClient.send(
                            httpDownloadRequest,
                            HttpResponse.BodyHandlers.ofFile(Path.of(filePath))
                    );
            return downloadResponse.statusCode() == 200;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
