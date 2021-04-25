package me.darrionat.matrixlib.util;

/**
 * An immutable data type for rational numbers.
 * <p>
 * This class is a modified version of Sedgewick and Wayne's {@code Rational}. The original can be found here
 * https://introcs.cs.princeton.edu/java/92symbolic/Rational.java
 *
 * @author Robert Sedgewick, Kevin Wayne, and Darrion Thornburgh
 */
public class Rational implements Comparable<Rational> {
    /**
     * The {@code Rational} representation of {@code 0}.
     */
    public static final Rational ZERO = new Rational(0, 1);
    /**
     * The {@code Rational} representation of {@code 1}.
     */
    public static final Rational ONE = new Rational(1, 1);
    /**
     * The numerator
     */
    private int num;
    /**
     * The denominator
     */
    private int den;

    /**
     * Constructs a new {@code Rational} to indicate a rational number as a fraction.
     *
     * @param numerator   the numerator of the fraction.
     * @param denominator the denominator of the fraction.
     */
    public Rational(int numerator, int denominator) {
        if (denominator == 0)
            throw new ArithmeticException("Denominator is zero");
        // Reduce
        int g = gcd(numerator, denominator);
        num = numerator / g;
        den = denominator / g;
        // Negative values
        if (den < 0) {
            den = -den;
            num = -num;
        }
    }

    /**
     * Gets the numerator.
     *
     * @return the numerator of this rational.
     */
    public int numerator() {
        return num;
    }

    /**
     * Gets the denominator.
     *
     * @return the denominator of this rational.
     */
    public int denominator() {
        return den;
    }

    /**
     * Gets the {@code Rational} represented as a double.
     *
     * @return the fraction represented as a double.
     */
    public double toDouble() {
        return (double) num / den;
    }

    /**
     * Determines if the rational is equal to zero.
     *
     * @return {@code true} if this fraction is equal to zero; {@code false} otherwise.
     */
    public boolean zero() {
        return this.equals(ZERO);
    }

    /**
     * Parses a {@code Rational} from a string.
     * <p>
     * The desired format: {@code numerator/denominator}.
     *
     * @param s the string containing the rational expression to be parsed.
     * @return a fraction parsed from a string.
     */
    public static Rational parseRational(String s) {
        if (s == null) throw new NumberFormatException();

        String[] fraction = s.split("/");
        int num = Integer.parseInt(fraction[0]);

        if (fraction.length != 2)
            return new Rational(num, 1);

        int dem = Integer.parseInt(fraction[1]);
        return new Rational(num, dem);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Rational))
            return false;
        Rational b = (Rational) obj;
        return compareTo(b) == 0;
    }

    @Override
    public String toString() {
        if (den == 1) return num + "";
        return num + "/" + den;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public int compareTo(Rational b) {
        // Left-hand side
        int lhs = this.num * b.den;
        // Right-hand side
        int rhs = this.den * b.num;
        return Integer.compare(lhs, rhs);
    }

    /**
     * Gets the mediant of two rational numbers.
     * <p>
     * For example: {@code mediant(1/3, 2/4)} will return
     * <p>
     * {@code (1+2)/(3+4)}
     *
     * @param r the first rational.
     * @param s the second rational.
     */
    public static Rational mediant(Rational r, Rational s) {
        return new Rational(r.num + s.num, r.den + s.den);
    }

    // return gcd(|m|, |n|)

    /**
     * Gets the greatest common denominator between two integers.
     *
     * @param m the first integer.
     * @param n the second integer.
     * @return the great common denominator of the passed integers.
     */
    private static int gcd(int m, int n) {
        if (m < 0) m = -m;
        if (n < 0) n = -n;
        if (0 == n) return m;
        else return gcd(n, m % n);
    }

    /**
     * Gets the least common multiple between two integers.
     *
     * @param m the first integer.
     * @param n the second integer.
     * @return the least common multiple of the passed integers.
     */
    private static int lcm(int m, int n) {
        if (m < 0) m = -m;
        if (n < 0) n = -n;
        return m * (n / gcd(m, n));
    }

    /**
     * Gets the sum of this rational and another.
     *
     * @param b the rational to add by.
     * @return the sum of this rational and another.
     */
    public Rational add(Rational b) {
        Rational a = this;
        if (a.den == b.den) return new Rational(a.num + b.num, a.den);
        // Base cases
        if (a.compareTo(ZERO) == 0) return b;
        if (b.compareTo(ZERO) == 0) return a;

        // Find gcd of numerators and denominators
        int f = gcd(a.num, b.num);
        int g = gcd(a.den, b.den);

        // Add cross-product terms for numerator
        Rational s = new Rational((a.num / f) * (b.den / g) + (b.num / f) * (a.den / g),
                lcm(a.den, b.den));

        // Multiply back in
        s.num *= f;
        return s;
    }   // return a - b

    /**
     * Gets the difference between this rational and another.
     *
     * @param b the rational to subtract by.
     * @return the difference between this rational and another.
     */
    public Rational subtract(Rational b) {
        Rational a = this;
        return a.add(b.negate());
    }

    /**
     * Gets the product of this rational and another.
     *
     * @param b the multiplicand.
     * @return the product of this rational and another.
     */
    public Rational multiply(Rational b) {
        return new Rational(num * b.num, den * b.den);
    }

    // return a / b

    /**
     * Gets the quotient of this rational and another.
     *
     * @param b the divisor.
     * @return gets the amount of times {@code b} goes into {@code this}.
     */
    public Rational divide(Rational b) {
        Rational a = this;
        return a.multiply(b.reciprocal());
    }

    /**
     * Gets this negation of this fraction.
     *
     * @return returns the opposite of this fraction.
     */
    public Rational negate() {
        return new Rational(-num, den);
    }

    /**
     * Get the absolute value of this rational.
     *
     * @return the distance from {@code 0}.
     */
    public Rational abs() {
        if (num >= 0) return this;
        else return negate();
    }

    /**
     * Gets the reciprocal of this fraction.
     *
     * @return the reciprocal of this fraction.
     */
    public Rational reciprocal() {
        return new Rational(den, num);
    }
}