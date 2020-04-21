public class AssertStatement {

    public static void main(String[] args) {
        String value = "value";

        assert value.charAt(0) <= 20 : " Underweight";
        System.out.println("value is "+value);
    }
}