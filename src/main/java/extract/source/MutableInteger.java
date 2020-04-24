package extract.source;

public class MutableInteger extends Number implements Comparable<MutableInteger> {
    private int number;

    /**
     * Initialize object with the value
     *
     * @param number start value to initialize
     */
    public MutableInteger(int number) {
        this.number = number;
    }

    /**
     * Initialize object with 0 value
     */
    public MutableInteger() {
        this(0);
    }

    /**
     * Increase value by 1
     */
    public void increment() {
        ++number;
    }

    /**
     * Add value to the current number
     *
     * @param value - value to add
     */
    public void add(int value) {
        number += value;
    }

    /**
     * Set the value to 0
     */
    public void clear() {
        this.number = 0;
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
}
