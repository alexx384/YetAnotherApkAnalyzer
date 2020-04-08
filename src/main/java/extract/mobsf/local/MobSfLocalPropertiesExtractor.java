package extract.mobsf.local;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MobSfLocalPropertiesExtractor {
    public static final String JSON_PROPERTIES_EXTENSION = ".json";

    private final File jsonPropertiesFile;
    private final Path jsonPropertiesFilePath;

    public MobSfLocalPropertiesExtractor(Path filePath) {
        jsonPropertiesFile = new File(filePath.toString() + JSON_PROPERTIES_EXTENSION);
        jsonPropertiesFilePath = filePath;
    }

    public JSONObject getJsonObject() {
        if (!jsonPropertiesFile.exists() || !jsonPropertiesFile.canRead()) {
            return null;
        }

        try {
            String jsonContent = Files.readString(jsonPropertiesFilePath, StandardCharsets.UTF_8);
            return new JSONObject(jsonContent);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean overrideFile(String jsonContent) {
        try (FileWriter writer = new FileWriter(jsonPropertiesFile)) {
            writer.write(jsonContent);
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
