public class DoStatement {
    public static void main(String[] args) {
        String value = "value";
        int i = 0;
        do {
            System.out.println(value.charAt(i));
            ++i;
        } while (i < value.length());
    }
}