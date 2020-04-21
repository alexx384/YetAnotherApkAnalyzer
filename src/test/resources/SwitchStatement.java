public class SwitchStatement {

    public static void main(String[] args) {
        String value = "value";

        switch (value.charAt(3)) {
            case 'u':
                System.out.println(value.charAt(3) + " statement");
                break;
            case 'l': {
                System.out.println(value.charAt(2) + " statement");
            } break;
            default: {
                System.out.println(value.charAt(1) + " default");
            } break;
        }
    }
}