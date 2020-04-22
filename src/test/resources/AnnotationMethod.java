import java.io.Closeable;
import java.io.IOException;

public class AnnotationMethod implements Closeable {

    public static void main(String[] args) throws IOException {
        new AnnotationDeclaration().close();
    }

    @Override
    public void close() throws IOException {
        String value = "Hello";
        System.out.println(value.toLowerCase());
    }
}
