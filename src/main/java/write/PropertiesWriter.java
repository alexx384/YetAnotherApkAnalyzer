package write;

import property.ApkPropertyStorage;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PropertiesWriter {
    private static final String DEFAULT_RESULT_FILE_PATH = "result.csv";

    private final Path outputPath;

    private PropertiesWriter(Path outputPath) {

        this.outputPath = outputPath;
    }

    public static PropertiesWriter build() {
        System.out.println("Failed to read resultFilePath. Use default: " + DEFAULT_RESULT_FILE_PATH);
        return build(DEFAULT_RESULT_FILE_PATH);
    }

    public static PropertiesWriter build(String resultFile) {
        Path resultPath = Path.of(resultFile);
        if (!checkResultFile(resultPath)) {
            return null;
        }
        return new PropertiesWriter(resultPath);
    }

    private static boolean checkResultFile(Path resultPath) {

        if (Files.exists(resultPath)) {
            if (!Files.isRegularFile(resultPath)) {

                return false;
            }
        } else {

            try {
                return resultPath.toFile().createNewFile();
            } catch (IOException e) {

                return false;
            }
        }
        return Files.isWritable(resultPath);
    }

    public boolean saveProperties(ApkPropertyStorage propertyStorage) {
        try (FileWriter fileWriter = new FileWriter(outputPath.toFile())) {
            fileWriter.write(propertyStorage.getCSVRepresentation());
            fileWriter.write('\n');
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
}
