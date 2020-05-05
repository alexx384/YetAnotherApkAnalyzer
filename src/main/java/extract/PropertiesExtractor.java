package extract;

import extract.androwarn.AndrowarnPropertyExtractor;
import extract.mobsf.MobSfApkPropertiesExtractor;
import extract.source.SourcesParser;
import property.ApkPropertyStorage;
import write.PropertiesWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

import static extract.mobsf.local.MobSfLocalPropertiesExtractor.JSON_PROPERTIES_EXTENSION;

public class PropertiesExtractor {

    private static final String DEFAULT_PYTHON_PATH = "python";
    private static final String DEFAULT_ANDROWARN_PATH = "androwarn/androwarn.py";

    private static final byte[] APK_MAGIC = new byte[]{0x50, 0x4B, 0x03, 0x04};
    private final String mobsfAddress;
    private final String mobsfApiKey;
    private final AndrowarnPropertyExtractor androwarnPropertyExtractor;

    public static PropertiesExtractor build(String mobsfAddress, String mobsfApiKey, String pythonPath,
                                            String androwarnPath) {
        if (pythonPath == null) {
            System.out.println("Failed to read pythonPath. Use default: " + DEFAULT_PYTHON_PATH);
            pythonPath = DEFAULT_PYTHON_PATH;
        }
        if (androwarnPath == null) {
            System.out.println("Failed to read androwarnPath. Use default: " + DEFAULT_ANDROWARN_PATH);
            androwarnPath = DEFAULT_ANDROWARN_PATH;
        }
        AndrowarnPropertyExtractor androwarnPropertyExtractor = AndrowarnPropertyExtractor
                .build(pythonPath, androwarnPath);
        if (androwarnPropertyExtractor == null) {
            System.err.println("Failed to create AndrowarnParametersExtractor. This happens due to incorrect python " +
                    "path or could not run androwarn");
            return null;
        }
        if (mobsfAddress == null) {
            System.err.println("The MobSf ip address is null");
            return null;
        }
        if (mobsfApiKey == null) {
            System.err.println("The MobSf ip key is null");
            return null;
        }
        return new PropertiesExtractor(mobsfAddress, mobsfApiKey, androwarnPropertyExtractor);
    }

    public PropertiesExtractor(String mobsfAddress, String mobsfApiKey,
                               AndrowarnPropertyExtractor androwarnPropertyExtractor) {
        this.mobsfAddress = mobsfAddress;
        this.mobsfApiKey = mobsfApiKey;
        this.androwarnPropertyExtractor = androwarnPropertyExtractor;
    }

    public boolean extract(String apkFileObject, String resultFilePath, boolean isNotDeleteCache) {
        PropertiesWriter writer;
        if (resultFilePath == null) {
            writer = PropertiesWriter.build();
        } else {
            writer = PropertiesWriter.build(resultFilePath);
        }
        if (writer == null) {
            return false;
        }

        Path apkPathObject = Path.of(apkFileObject);
        if (!Files.exists(apkPathObject)) {
            return false;
        }
        boolean result;
        if (Files.isDirectory(Path.of(apkFileObject))) {
            result = extractDirectory(apkFileObject, writer, isNotDeleteCache);
        } else {
            result = extractApkFile(apkFileObject, writer, isNotDeleteCache);
        }
        try {
            writer.close();
        } catch (IOException e) {
            return false;
        }
        return result;
    }

    private boolean extractDirectory(String apkDirPath, PropertiesWriter writer, boolean isNotDeleteCache) {
        FilenameFilter filenameFilter = (dir, name) ->
                !Files.isDirectory(
                        Path.of(dir.getPath() + File.separatorChar + name)
                ) && name.endsWith(".apk");
        boolean result = true;
        File[] files = new File(apkDirPath).listFiles(filenameFilter);
        if (files == null) {
            System.err.println("Failed to access directory " + apkDirPath +
                    ". This possibly happens due to I/O error occurs");
            return false;
        }
        for (File file : files) {
            result &= extractApkFile(file.getPath(), writer, isNotDeleteCache);
        }
        return result;
    }

    private boolean extractApkFile(String apkFilePath, PropertiesWriter writer, boolean isNotDeleteCache) {
        Path apkPath = Path.of(apkFilePath);
        if (!checkApkFile(apkPath)) {
            System.err.println("Failed apk file: " + apkFilePath);
            return false;
        }

        ApkPropertyStorage propertyStorage = new ApkPropertyStorage();
        if (!MobSfApkPropertiesExtractor.extract(propertyStorage, apkPath, mobsfAddress, mobsfApiKey)) {
            System.err.println("Could not get scan parameters from MobSf");
            return false;
        }

        Path sourcesDir = Path.of(MobSfApkPropertiesExtractor.DIR_PREFIX + apkPath.getFileName().toString());
        if (!SourcesParser.parseSources(sourcesDir, propertyStorage)) {
            System.err.println("Could not parse apk decompiled source files");
            return false;
        }

        if (!androwarnPropertyExtractor.processApk(apkFilePath, propertyStorage)) {
            System.err.println("Error: Androwarn could not process apk");
            return false;
        }

        if (!writer.saveProperties(propertyStorage)) {
            System.err.println("Error: could not save properties");
            return false;
        }
        if (!isNotDeleteCache) {
            cleanUp(apkPath.getFileName().toString() +
                    JSON_PROPERTIES_EXTENSION, sourcesDir);
        }
        System.out.println("[+]" + apkFilePath);
        return true;
    }

    private static void cleanUp(String jsonReport, Path sourceFilesDir) {
        try {
            Files.deleteIfExists(Path.of(jsonReport));
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
