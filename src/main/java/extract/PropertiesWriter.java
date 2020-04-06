package extract;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PropertiesWriter {

    private List<ApkProperty> apkProperties;
    private final Path outputPath;

    public PropertiesWriter(Path outputPath) {

        this.outputPath = outputPath;
        apkProperties = new ArrayList<>();
    }

    public void addProperty(ApkProperty property) {
        apkProperties.add(property);
    }

    public boolean flushProperties() {
        try (FileWriter fileWriter = new FileWriter(outputPath.toFile());
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            Iterator<ApkProperty> iterator = apkProperties.iterator();

            ApkProperty property = iterator.next();
            bufferedWriter.write(property.getCSVRepresentation());
            while (iterator.hasNext()) {
                bufferedWriter.write(',');
                property = iterator.next();
                bufferedWriter.write(property.getCSVRepresentation());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
}
