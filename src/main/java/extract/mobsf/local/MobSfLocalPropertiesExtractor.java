package extract.mobsf.local;

import org.json.JSONObject;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MobSfLocalPropertiesExtractor {
    public static final String JSON_PROPERTIES_EXTENSION = ".json";
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final File jsonPropertiesFile;

    public MobSfLocalPropertiesExtractor(Path filePath) {
        jsonPropertiesFile = new File(filePath.toString() + JSON_PROPERTIES_EXTENSION);
    }

    public JSONObject getJsonObject() {
        if (!jsonPropertiesFile.exists() || !jsonPropertiesFile.canRead()) {
            return null;
        }

        try {
            String jsonContent = Files.readString(jsonPropertiesFile.toPath(), DEFAULT_CHARSET);
            return new JSONObject(jsonContent);
        } catch (IOException e) {
            return null;
        }
    }

    public void overrideFile(String jsonContent) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(jsonPropertiesFile), DEFAULT_CHARSET)) {
            writer.write(jsonContent);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
