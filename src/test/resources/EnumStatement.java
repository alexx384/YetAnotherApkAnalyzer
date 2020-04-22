public enum EnumStatement {
    FIRST(1) {
        @Override
        public String asLowerCase() {
            String name = this.name();
            return name.toLowerCase();
        }
    },
    SECOND(2) {
        @Override
        public String asLowerCase() {
            String name = this.name();
            return name.toLowerCase();
        }
    },
    THIRD(3) {
        @Override
        public String asLowerCase() {
            String name = this.name();
            return name.toLowerCase();
        }
    };

    private int value;

    EnumStatement(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public abstract String asLowerCase();

    public static void main(String[] args) {
        System.out.println("Hello World");
        System.out.println(THIRD.asLowerCase());
        System.out.println(THIRD.getValue());
    }
}
