package extract.source;

import java.io.*;
import java.util.Enumeration;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class ZipFileVisitor {
    public static boolean walkZipFileTree(String zipFileName, Predicate<InputStream> predicate) {
        try {
            ZipFile zip = new ZipFile(new File(zipFileName));
            Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();

            // Process each entry
            while (zipFileEntries.hasMoreElements()) {
                // grab a zip file entry
                ZipEntry entry = zipFileEntries.nextElement();
                String currentEntry = entry.getName();

                if (!entry.isDirectory()) {
                    // write the current file to disk
                    try {
                        if (!predicate.test(zip.getInputStream(entry))) {
                            System.err.println("Warning could not process source " + currentEntry);
                        }
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage() + ". In " + currentEntry);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
}
