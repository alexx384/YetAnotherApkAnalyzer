package extract.mobsf.net;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Arrays;

public class MobSfApiProcessor {
    private static final String PATH_UPLOAD_FILE = "/api/v1/upload";

    private final String serverUrl;
    private final String mobsfApiKey;

    public MobSfApiProcessor(String serverUrl, String mobsfApiKey) {
        this.serverUrl = serverUrl;
        this.mobsfApiKey = mobsfApiKey;
    }

    public String uploadFile(Path filePath) throws FileNotFoundException {
        HttpClient httpClient = HttpClient.newHttpClient();
        MultipartBuilder formBuilder = new MultipartBuilder()
                .addFile("\"file\"", filePath);
        try {
            HttpRequest httpRequest = HttpRequest
                    .newBuilder(
                            new URI(serverUrl + PATH_UPLOAD_FILE)
                    )
                    .header("Authorization", mobsfApiKey)
                    .header("Content-Type", "multipart/form-data; boundary=" + formBuilder.getBoundary())
                    .POST(formBuilder.build())
                    .build();

            HttpResponse<byte[]> response =
                    httpClient.send(
                            httpRequest,
                            HttpResponse.BodyHandlers.ofByteArray()
                    );

            if (response.statusCode() != 200) {
                System.out.println(new String(response.body()));
                return null;
            }

            JSONObject obj = new JSONObject(new String(response.body()));
            return obj.getString("hash");
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        MobSfApiProcessor processor = new MobSfApiProcessor("http://localhost:8000",
                "a661aead976959736b8bea938df95752f9ecca6d18fdc4051eb83ab0b8c02108"
        );
//        MobSfApiProcessor processor = new MobSfApiProcessor("http://localhost:8001",
//                "a661aead976959736b8bea938df95752f9ecca6d18fdc4051eb83ab0b8c02108"
//        );
//        Path apkPath = Path.of("testFiles/hello.txt");
        Path apkPath = Path.of("testFiles/k9mail-release.apk");
        System.out.println(processor.uploadFile(apkPath));
    }
}
