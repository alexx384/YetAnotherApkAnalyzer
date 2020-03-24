package extract;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class ApkExtractor {

    private static final String DEFAULT_RESULT_FILE_PATH = "result.csv";
    private static final byte[] APK_MAGIC = new byte[]{0x50, 0x4B, 0x03, 0x04};
    private final String apkFilePath;
    private final String resultFilePath;

    public ApkExtractor(String apkFilePath) {

        this(apkFilePath, DEFAULT_RESULT_FILE_PATH);
    }

    public ApkExtractor(String apkFilePath, String resultFilePath) {

        this.apkFilePath = apkFilePath;
        this.resultFilePath = resultFilePath;
    }

    public boolean extract() {

        Path apkPath = Path.of(this.apkFilePath);
        if (!checkApkFile(apkPath)) {
            return false;
        }
        Path resultPath = Path.of(this.resultFilePath);
        if (!checkResultFile(resultPath)) {
            return false;
        }

        PropertiesWriter writer = new PropertiesWriter(resultPath);

        // TODO: add extractors
        writer.setTestKey1(12);
        writer.setTestKey2(15);

        return writer.flushProperties();
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
}
