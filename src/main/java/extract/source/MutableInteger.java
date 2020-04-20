package extract.source;

public class MutableInteger extends Number implements Comparable<MutableInteger> {
    private int number;

    public MutableInteger(int number) {
        this.number = number;
    }

    public MutableInteger() {
        this(0);
    }

    public void increment() {
        ++number;
    }

    @Override
    public String toString() {
        return Integer.toString(number);
    }

    @Override
    public int intValue() {
        return number;
    }

    @Override
    public long longValue() {
        return number;
    }

    @Override
    public float floatValue() {
        return number;
    }

    @Override
    public double doubleValue() {
        return number;
    }

    @Override
    public int compareTo(MutableInteger o) {
        return Integer.compare(this.number, o.number);
    }

    public static void main(String[] args) {
//        Integer i = 2;
//        Integer r = i;
//        i += 2;
//        System.out.println(i);
//        System.out.println(r);
        MutableInteger i = new MutableInteger(2);
        MutableInteger r = i;
        i.increment();
        System.out.println(i);
        System.out.println(r);
    }
}
