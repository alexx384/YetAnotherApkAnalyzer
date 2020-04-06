package extract.mobsf.net;

import java.io.*;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

public class MultipartBuilder {
    private final List<Part> partsList;
    private final String boundary;

    public MultipartBuilder() {
        partsList = new ArrayList<>();
        boundary = UUID.randomUUID().toString();
    }

    public MultipartBuilder addFile(String name, Path filePath) {
        String filename = filePath.getFileName().toString();
        String contentType = "application/octet-stream";
        String partHeader =
                "--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"\r\n" +
                        "Content-Type: " + contentType + "\r\n\r\n";
        partsList.add(new Part(partHeader.getBytes(StandardCharsets.UTF_8)));
        partsList.add(new Part(filePath.toFile()));
        return this;
    }

    public HttpRequest.BodyPublisher build() {
        if (partsList.isEmpty()) {
            throw new IllegalStateException("Must have at least one part to build multipart message.");
        }
        addFinalBoundaryPart();
        return HttpRequest.BodyPublishers.ofByteArrays(() -> new PartsIterator(partsList));
    }

    public String getBoundary() {
        return boundary;
    }

    public static class PartsIterator implements Iterator<byte[]> {
        private static final int DEFAULT_BUFFER_SIZE = 8192;

        private byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
        private boolean isNotDone = false;
        private Iterator<Part> iter;
        private InputStream stream;

        PartsIterator(List<Part> partsList) {
            iter = partsList.iterator();
        }

        @Override
        public boolean hasNext() {
            if (isNotDone) {
                return true;
            }
            return iter.hasNext();
        }

        private byte[] readByteChunk() {
            try {
                int readed = stream.read(buf, 0, DEFAULT_BUFFER_SIZE);
                if (readed > 0) {
                    byte[] actualBytes = new byte[readed];
                    System.arraycopy(buf, 0, actualBytes, 0, readed);
                    return actualBytes;
                } else {
                    stream.close();
                    stream = null;
                    isNotDone = false;
                    return "\r\n".getBytes(StandardCharsets.UTF_8);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        @Override
        public byte[] next() {
            if (!isNotDone) {
                Part elem = iter.next();
                if (Part.TYPE.BYTE.equals(elem.type)) {
                    return elem.value;
                } else {
                    try {
                        stream = new FileInputStream(elem.file);
                    } catch (FileNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    isNotDone = true;
                    return readByteChunk();
                }
            } else {
                return readByteChunk();
            }
        }
    }

    private void addFinalBoundaryPart() {
        String finalBoundary = "--" + boundary + "--";
        partsList.add(
                new Part(finalBoundary.getBytes(StandardCharsets.UTF_8))
        );
    }

    static class Part {
        public enum TYPE {
            BYTE, FILE
        }

        Part(byte[] value) {
            this.type = TYPE.BYTE;
            this.value = value;
        }

        Part(File file) {
            this.type = TYPE.FILE;
            this.file = file;
        }

        TYPE type;
        byte[] value;
        File file;
    }
}
