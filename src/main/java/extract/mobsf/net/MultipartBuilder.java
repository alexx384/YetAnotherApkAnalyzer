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

    public MultipartBuilder addFile(String name, Path filePath) throws FileNotFoundException {
        String filename = filePath.getFileName().toString();
        String contentType = "application/octet-stream";
        String partHeader =
                "--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=" + name + "; filename=\"" + filename + "\"\r\n" +
                        "Content-Type: " + contentType + "\r\n\r\n";
        Part headPart = new Part();
        headPart.type = Part.TYPE.BYTE;
        headPart.value = partHeader.getBytes(StandardCharsets.UTF_8);
        Part bodyPart = new Part();
        bodyPart.type = Part.TYPE.FILE;
        bodyPart.file = filePath.toFile();
//        partsList.add(
//                new ByteArrayInputStream(partHeader.getBytes(StandardCharsets.UTF_8))
//        );
        partsList.add(headPart);
        partsList.add(bodyPart);
        return this;
    }

    public HttpRequest.BodyPublisher build() {
        if (partsList.isEmpty()) {
            throw new IllegalStateException("Must have at least one part to build multipart message.");
        }
        addFinalBoundaryPart();
        return HttpRequest.BodyPublishers.ofByteArrays(PartsIterator::new);
//        return HttpRequest.BodyPublishers.ofInputStream(
//                () -> new SequenceInputStream(Collections.enumeration(partsList))
//        );
    }

    public String getBoundary() {
        return boundary;
    }

    private void addFinalBoundaryPart() {
        String finalBoundary = "--" + boundary + "--";
//        partsList.add(
//                new ByteArrayInputStream(finalBoundary.getBytes(StandardCharsets.UTF_8))
//        );
        Part part = new Part();
        part.type = Part.TYPE.BYTE;
        part.value = finalBoundary.getBytes(StandardCharsets.UTF_8);
        partsList.add(part);
    }

    static class Part {
        public enum TYPE {
            BYTE, FILE
        }
        TYPE type;
        byte[] value;
        File file;
    }

    public class PartsIterator implements Iterator<byte[]> {
        private Iterator<Part> iter;
        private InputStream stream;
        private boolean isNotDone = false;
        private byte[] buf = new byte[8192];

        PartsIterator() {
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
                int r = stream.read(buf);
                if (r > 0) {
                    byte[] actualBytes = new byte[r];
                    System.arraycopy(buf, 0, actualBytes, 0, r);
                    return actualBytes;
                } else {
                    stream.close();
                    stream = null;
                    isNotDone = false;
                    return "\r\n".getBytes(StandardCharsets.UTF_8);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public byte[] next() {
            if (isNotDone) {
                return readByteChunk();
            } else {
                Part elem = iter.next();
                if (Part.TYPE.BYTE.equals(elem.type)) {
                    return elem.value;
                } else {
                    try {
                        stream = new FileInputStream(elem.file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    isNotDone = true;
                    return readByteChunk();
                }
            }
        }
    }
}
