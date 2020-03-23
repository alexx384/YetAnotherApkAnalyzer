package extract;

import java.nio.file.Files;
import java.nio.file.Path;

public class ApkExtractor {

    private final String apkFilePath;
    private final String resultFilePath;

    public ApkExtractor(String apkFilePath, String resultFilePath) {
        this.apkFilePath = apkFilePath;
        this.resultFilePath = resultFilePath;
    }

    public boolean extract() {
        Path apkPath = Path.of(this.apkFilePath);

        return checkApkFile(apkPath);
    }

    private boolean checkApkFile(Path apkPath) {
        if (!Files.isRegularFile(apkPath)) {
            return false;
        }

        if (!Files.isReadable(apkPath)) {
            return false;
        }

        // TODO: Check APK signature

        return true;
    }
}
