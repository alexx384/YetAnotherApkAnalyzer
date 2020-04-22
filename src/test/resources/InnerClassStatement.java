public class InnerClassStatement {
    public static class InnerClass {
        public static final String CONST = "Const phrase";
        public static String changeTest() {
            return CONST.toLowerCase();
        }
    }

    public static void main(String[] args) {
        System.out.println(InnerClass.changeTest());
    }
}
