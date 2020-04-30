package extract;

import extract.androwarn.AndrowarnParametersExtractor;
import extract.mobsf.MobSfApkPropertiesParser;
import extract.mobsf.local.MobSfLocalPropertiesExtractor;
import extract.source.SourcesParser;
import property.ApkPropertyStorage;
import write.PropertiesWriter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

public class PropertiesExtractor {

    private static final String DEFAULT_APK_FILE_PATH = "app.apk";
    private static final String DEFAULT_MOBSF_ADDRESS = "192.168.1.100:8000";
    private static final String DEFAULT_MOBSF_API_KEY =
            "a661aead976959736b8bea938df95752f9ecca6d18fdc4051eb83ab0b8c02108";
    private static final String DEFAULT_PYTHON_PATH = "python";
    private static final String DEFAULT_ANDROWARN_PATH = "androwarn/androwarn.py";

    private static final byte[] APK_MAGIC = new byte[]{0x50, 0x4B, 0x03, 0x04};
    private final String apkFilePath;
    private final String mobsfAddress;
    private final String mobsfApiKey;
    private final String pythonPath;
    private final String androwarnPath;
    private final PropertiesWriter writer;

    public PropertiesExtractor(String apkFilePath, PropertiesWriter writer, String mobsfAddress, String mobsfApiKey,
                               String pythonPath, String androwarnPath) {

        if (apkFilePath == null) {
            System.out.println("Failed to read apkFilePath. Use default: " + DEFAULT_APK_FILE_PATH);
            this.apkFilePath = DEFAULT_APK_FILE_PATH;
        } else {
            this.apkFilePath = apkFilePath;
        }

        this.writer = writer;

        if (mobsfAddress == null) {
            System.out.println("Failed to read mobsfAddress. Use default: " + DEFAULT_MOBSF_ADDRESS);
            this.mobsfAddress = DEFAULT_MOBSF_ADDRESS;
        } else {
            this.mobsfAddress = mobsfAddress;
        }

        if (mobsfApiKey == null) {
            System.out.println("Failed to read mobsfApiKey. Use default: " + DEFAULT_MOBSF_API_KEY);
            this.mobsfApiKey = DEFAULT_MOBSF_API_KEY;
        } else {
            this.mobsfApiKey = mobsfApiKey;
        }

        if (pythonPath == null) {
            System.out.println("Failed to read pythonPath. Use default: " + DEFAULT_PYTHON_PATH);
            this.pythonPath = DEFAULT_PYTHON_PATH;
        } else {
            this.pythonPath = pythonPath;
        }

        if (androwarnPath == null) {
            System.out.println("Failed to read androwarnPath. Use default: " + DEFAULT_ANDROWARN_PATH);
            this.androwarnPath = DEFAULT_ANDROWARN_PATH;
        } else {
            this.androwarnPath = androwarnPath;
        }
    }

    public boolean extract() {
        Path apkPath = Path.of(this.apkFilePath);
        if (!checkApkFile(apkPath)) {
            System.err.println("Failed to check apk path");
            return false;
        }
        AndrowarnParametersExtractor androwarnParametersExtractor = AndrowarnParametersExtractor
                .build(pythonPath, androwarnPath);
        if (androwarnParametersExtractor == null) {
            System.err.println("Failed to create AndrowarnParametersExtractor. This happens due to incorrect python " +
                    "path or could not run androwarn");
            return false;
        }

        ApkPropertyStorage propertyStorage = new ApkPropertyStorage();
        if (!MobSfApkPropertiesParser.parseTo(propertyStorage, apkPath, mobsfAddress, mobsfApiKey)) {
            System.err.println("Could not get scan parameters from MobSf");
            return false;
        }

        Path sourcesDir = apkPath.resolveSibling(
                MobSfApkPropertiesParser.DIR_PREFIX + apkPath.getFileName().toString()
        );
        if (!SourcesParser.parseSources(sourcesDir, propertyStorage)) {
            System.err.println("Could not parse apk decompiled source files");
            return false;
        }

        if (!androwarnParametersExtractor.processApk(apkFilePath, propertyStorage)) {
            System.err.println("Error: Androwarn could not process apk");
            return false;
        }

        if (!writer.saveProperties(propertyStorage)) {
            System.err.println("Error: could not save properties");
            return false;
        }
        cleanUp(apkFilePath + MobSfLocalPropertiesExtractor.JSON_PROPERTIES_EXTENSION, sourcesDir);
        return true;
    }

    private static void cleanUp(String jsonReport, Path sourceFilesDir) {
        try {
            Files.delete(Path.of(jsonReport));
            Files.walkFileTree(sourceFilesDir, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean checkApkFile(Path apkPath) {

        try (FileInputStream inputStream = new FileInputStream(apkPath.toFile())) {

            byte[] fileMagic = inputStream.readNBytes(4);
            if (Arrays.equals(fileMagic, APK_MAGIC)) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
