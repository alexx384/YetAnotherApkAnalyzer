package extract.mobsf.remote;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class ZipExtractor {
    private static final int BUFFER_SIZE = 4096;
    private final byte[] buffer;

    public ZipExtractor() {
        buffer = new byte[BUFFER_SIZE];
    }

    public boolean extractToFolder(String zipFile, String extractFolder) {
        try {
            ZipFile zip = new ZipFile(new File(zipFile));

            Files.createDirectory(Path.of(extractFolder));
            Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();

            // Process each entry
            while (zipFileEntries.hasMoreElements()) {
                // grab a zip file entry
                ZipEntry entry = zipFileEntries.nextElement();
                String currentEntry = entry.getName();

                File destFile = new File(extractFolder, currentEntry);
                //destFile = new File(newPath, destFile.getName());
                File destinationParent = destFile.getParentFile();

                // create the parent directory structure if needed
                Files.createDirectories(destinationParent.toPath());

                if (!entry.isDirectory()) {
                    int currentByte;
                    // write the current file to disk
                    try (BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
                         FileOutputStream fos = new FileOutputStream(destFile);
                         BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE)) {
                        // read and write until last byte is encountered
                        while ((currentByte = is.read(buffer, 0, BUFFER_SIZE)) != -1) {
                            dest.write(buffer, 0, currentByte);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static void main(String[] args) {

    }
}
