package extract;

import extract.mobsf.MobSfExtractor;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class PropertiesExtractor {

    private static final String DEFAULT_RESULT_FILE_PATH = "result.csv";
    private static final String DEFAULT_APK_FILE_PATH = "app.apk";
    private static final String DEFAULT_MOBSF_ADDRESS = "192.168.1.100:8000";
    private static final String DEFAULT_MOBSF_API_KEY =
            "a661aead976959736b8bea938df95752f9ecca6d18fdc4051eb83ab0b8c02108";

    private static final byte[] APK_MAGIC = new byte[]{0x50, 0x4B, 0x03, 0x04};
    private final String apkFilePath;
    private final String resultFilePath;
    private final String mobsfAddress;
    private final String mobsfApiKey;

    public PropertiesExtractor(String apkFilePath, String resultFilePath, String mobsfAddress, String mobsfApiKey) {

        if (apkFilePath == null) {
            System.out.println("Failed to read apkFilePath. Use default: " + DEFAULT_APK_FILE_PATH);
            this.apkFilePath = DEFAULT_APK_FILE_PATH;
        } else {
            this.apkFilePath = apkFilePath;
        }

        if (resultFilePath == null) {
            System.out.println("Failed to read resultFilePath. Use default: " + DEFAULT_RESULT_FILE_PATH);
            this.resultFilePath = DEFAULT_RESULT_FILE_PATH;
        } else {
            this.resultFilePath = resultFilePath;
        }

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
        MobSfExtractor mobSfExtractor = MobSfExtractor.build(this.mobsfAddress, this.mobsfApiKey);
        if (mobSfExtractor == null) {
            return false;
        }

        PropertiesWriter writer = new PropertiesWriter(resultPath);

        boolean mobsfResult = mobSfExtractor.extract(apkPath, writer);
        if (!mobsfResult) {
            return false;
        }

        // TODO: add extractors
//        writer.setTestKey1(12);
//        writer.setTestKey2(15);

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
