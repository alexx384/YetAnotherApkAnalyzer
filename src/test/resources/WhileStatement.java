public class WhileStatement {
    public static void main(String[] args) {
        String value = "value";
        int i = 0;
        while (i < value.length()) {
            System.out.println(value.charAt(i));
            ++i;
        }
    }
}