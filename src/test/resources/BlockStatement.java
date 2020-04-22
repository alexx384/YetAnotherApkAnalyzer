public class BlockStatement {
    {
        String value = "Hello";
        System.out.println(value.toLowerCase());
    }

    static {
        String value = "World";
        System.out.println(value.toLowerCase());
    }

    public static void main(String[] args) {
        new AnnotationDeclaration();
    }
}
