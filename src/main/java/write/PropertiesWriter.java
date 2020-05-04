package write;

import property.ApkPropertyStorage;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PropertiesWriter implements AutoCloseable {
    private static final String DEFAULT_RESULT_FILE_PATH = "result.csv";

    private final FileWriter writer;

    private PropertiesWriter(FileWriter writer) {
        this.writer = writer;
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
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(resultPath.toFile());
        } catch (IOException e) {
            return null;
        }
        return new PropertiesWriter(fileWriter);
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
        try {
            writer.write(propertyStorage.getCSVRepresentation());
            writer.write('\n');
            writer.flush();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
