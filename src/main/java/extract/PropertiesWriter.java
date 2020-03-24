package extract;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class PropertiesWriter {

    enum Property {
        TEST_KEY1(0),
        TEST_KEY2(1);

        private final int idx;

        Property(int idx) {
            this.idx = idx;
        }
    }

    private final Path outputPath;
    private int[] properties = new int[Property.values().length];

    public PropertiesWriter(Path outputPath) {

        this.outputPath = outputPath;
    }

    public void setTestKey1(int value) {
        properties[Property.TEST_KEY1.idx] = value;
    }

    public void setTestKey2(int value) {
        properties[Property.TEST_KEY2.idx] = value;
    }

    public boolean flushProperties() {

        try (FileWriter fileWriter = new FileWriter(outputPath.toFile());
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            for (int i = 0; i < properties.length - 1; i++) {

                bufferedWriter.write(properties[i] + ", ");
            }
            bufferedWriter.write(properties[properties.length - 1] + "\n");
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }
}
