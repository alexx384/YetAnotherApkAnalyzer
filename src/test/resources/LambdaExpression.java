public class LambdaExpression {
    interface Drawable{
        void draw(String variable);
    }

    public static void main(String[] args) {
        Drawable d2 = (String variable)-> {
            variable = variable.toLowerCase();
            System.out.println("Drawing " + variable);
        };
        d2.draw("Hello World");
    }
}
