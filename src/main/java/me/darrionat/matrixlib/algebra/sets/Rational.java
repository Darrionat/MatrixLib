package me.darrionat.matrixlib.algebra.sets;

import java.math.BigInteger;

/**
 * An immutable data type for rational numbers.
 * <p>
 * This class is a modified version of Sedgewick and Wayne's {@code Rational}. The original can be found here
 * https://introcs.cs.princeton.edu/java/92symbolic/Rational.java
 *
 * @author Robert Sedgewick, Kevin Wayne, and Darrion Thornburgh
 */
public class Rational extends Quantity {
    private static final long serialVersionUID = 1459424736841695044L;
    /**
     * The {@code Rational} representation of {@code 0}.
     */
    public static final Rational ZERO = new Rational(BigInteger.ZERO, BigInteger.ONE);
    /**
     * The {@code Rational} representation of {@code 1}.
     */
    public static final Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE);
    /**
     * The string used to separate the numerator and denominator of the rational.
     */
    private static final String SEP = " over ";
    /**
     * The numerator
     */
    private final BigInteger num;
    /**
     * The denominator
     */
    private final BigInteger den;

    /**
     * Constructs a new {@code Rational} to indicate a whole number.
     *
     * @param n The value that the rational represents.
     */
    public Rational(long n) {
        this(n, 1);
    }

    /**
     * Constructs a new {@code Rational} to indicate a rational number as a fraction using {@code long}.
     *
     * @param numerator   The numerator of the fraction.
     * @param denominator The denominator of the fraction.
     */
    public Rational(long numerator, long denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    /**
     * Constructs a new {@code Rational} to indicate a rational number as a fraction using {@code BigInteger}.
     *
     * @param numerator   The numerator of the fraction.
     * @param denominator The denominator of the fraction.
     */
    public Rational(BigInteger numerator, BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO))
            throw new ArithmeticException("Denominator is zero");
        // Reduce
        BigInteger g = new BigInteger("" + gcd(numerator, denominator));
        numerator = numerator.divide(g);
        denominator = denominator.divide(g);
        // Negative values
        if (denominator.compareTo(BigInteger.ZERO) < 0) {
            numerator = numerator.negate();
            denominator = denominator.negate();
        }
        this.num = numerator;
        this.den = denominator;
    }

    /**
     * Gets the numerator.
     *
     * @return the numerator of this rational.
     */
    public BigInteger numerator() {
        return num;
    }

    /**
     * Gets the denominator.
     *
     * @return the denominator of this rational.
     */
    public BigInteger denominator() {
        return den;
    }

    /**
     * Gets the {@code Rational} represented as a BigInteger.
     * <p>
     * Equal to {@code numerator/denominator}.
     *
     * @return the fraction represented as a BigInteger.
     */
    public BigInteger toBigInteger() {
        return num.divide(den);
    }

    /**
     * {@inheritDoc}
     */
    public boolean zero() {
        return this.equals(ZERO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int intValue() throws ArithmeticException {
        try {
            return Integer.parseInt(toBigInteger().toString());
        } catch (NumberFormatException e) {
            throw new ArithmeticException("Out of int bounds");
        }
    }

    /**
     * Gets the mediant of two rational numbers.
     * <p>
     * For example: {@code mediant(1/3, 2/4)} will return
     * <p>
     * {@code (1+2)/(3+4)}
     *
     * @param r The first rational.
     * @param s The second rational.
     * @return The mediant of the two given rationals.
     */
    public static Rational mediant(Rational r, Rational s) {
        return new Rational(r.num.add(s.num), r.den.add(s.den));
    }

    /**
     * Gets the greatest common denominator between two integers.
     *
     * @param m the first integer.
     * @param n the second integer.
     * @return the great common denominator of the passed integers.
     */
    private static BigInteger gcd(BigInteger m, BigInteger n) {
        if (m.compareTo(BigInteger.ZERO) < 0) m = m.negate();
        if (n.compareTo(BigInteger.ZERO) < 0) n = n.negate();
        if (n.equals(BigInteger.ZERO)) return m;
        else return gcd(n, m.mod(n));
    }

    /**
     * Gets the least common multiple between two integers.
     *
     * @param m the first integer.
     * @param n the second integer.
     * @return the least common multiple of the passed integers.
     */
    private static BigInteger lcm(BigInteger m, BigInteger n) {
        if (m.compareTo(BigInteger.ZERO) < 0) m = m.negate();
        if (n.compareTo(BigInteger.ZERO) < 0) n = n.negate();
        // m * (n / gcd(m,n))
        return m.multiply(n.divide(gcd(m, n)));
    }

    /**
     * {@inheritDoc}
     */
    public Quantity add(Quantity b) {
        if (b instanceof Complex) {
            Complex complex = (Complex) b;
            return complex.add(this);
        }
        assert b instanceof Rational;
        Rational rational = (Rational) b;
        Rational a = this;
        if (a.den.equals(rational.den)) return new Rational(a.num.add(rational.num), a.den);
        // Base cases
        if (a.equals(ZERO)) return b;
        if (b.equals(ZERO)) return a;

        // Find gcd of numerators and denominators
        BigInteger f = gcd(a.num, rational.num);
        BigInteger g = gcd(a.den, rational.den);

        // Add cross-product terms for numerator
        // x = (a.num / f) * (b.den / g) + (b.num / f) * (a.den / g)
        // num = x*f
        BigInteger left = a.num.divide(f).multiply(rational.den.divide(g));
        BigInteger right = rational.num.divide(f).multiply(a.den.divide(g));
        BigInteger crossProduct = left.add(right);
        return new Rational(crossProduct.multiply(f), lcm(a.den, rational.den));
    }

    /**
     * {@inheritDoc}
     */
    public Quantity subtract(Quantity b) {
        return add(b.negate());
    }

    /**
     * {@inheritDoc}
     */
    public Quantity multiply(Quantity b) {
        if (b instanceof Complex) {
            Complex complex = (Complex) b;
            return complex.multiply(b);
        }
        assert b instanceof Rational;
        Rational rational = (Rational) b;
        return new Rational(num.multiply(rational.num), den.multiply(rational.den));
    }

    /**
     * {@inheritDoc}
     */
    public Quantity divide(Quantity b) {
        if (b instanceof Complex) {
            Complex complex = (Complex) b;
            return complex.pow(-1).multiply(this);
        }
        assert b instanceof Rational;
        Rational rational = (Rational) b;
        return multiply(rational.reciprocal());
    }

    /**
     * Gets the result of this rational added to another.
     *
     * @param b The addend.
     * @return The sum of this rational and the other rational.
     */
    public Rational addRational(Rational b) {
        return (Rational) add(b);
    }

    /**
     * Gets the result of this rational subtracted by another.
     *
     * @param b The difference to subtract by.
     * @return The difference of this rational and the other rational.
     */
    public Rational subtractRational(Rational b) {
        return (Rational) subtract(b);
    }

    /**
     * Gets the product of this rational and another.
     *
     * @param b The rational to multiply by.
     * @return The product of this rational and the multiplicand.
     */
    public Rational multiplyRational(Rational b) {
        return (Rational) multiply(b);
    }

    /**
     * Gets the result of this rational divided by another.
     *
     * @param b The dividend.
     * @return The quotient of this rational divided by the passed rational dividend.
     */
    public Rational divideRational(Rational b) {
        return (Rational) divide(b);
    }

    /**
     * {@inheritDoc}
     */
    public Rational negate() {
        return new Rational(num.negate(), den);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rational pow(int pow) {
        if (pow == 0)
            return ONE;
        BigInteger num = this.num.pow(Math.abs(pow));
        BigInteger den = this.den.pow(Math.abs(pow));
        if (pow > 0)
            return new Rational(num, den);
        else
            return new Rational(den, num);
    }

    /**
     * Get the absolute value of this rational.
     *
     * @return the distance from {@code 0}.
     */
    public Rational abs() {
        if (num.compareTo(BigInteger.ZERO) >= 0) return this;
        return negate();
    }

    /**
     * Gets the reciprocal of this fraction.
     *
     * @return the reciprocal of this fraction.
     */
    public Rational reciprocal() {
        return new Rational(den, num);
    }

    /**
     * Parses a {@code Rational} from a string.
     * <p>
     * The desired format: {@code numerator/denominator}.
     *
     * @param s The string containing the rational expression to be parsed.
     * @return A fraction parsed from a string.
     * @throws NumberFormatException Thrown when the input string is not a valid {@code Rational}.
     */
    public static Rational parseRational(String s) {
        if (s == null) throw new NumberFormatException();

        String[] fraction = s.split(SEP);

        BigInteger num = new BigInteger(fraction[0]);

        if (fraction.length != 2)
            return new Rational(num, BigInteger.ONE);

        BigInteger dem = new BigInteger(fraction[1]);
        return new Rational(num, dem);
    }

    /**
     * Compares two {@code Rational}s.
     * <p>
     * The result is less than zero if this is less than b, greater than zero if this is greater than b, and equal to zero when this and b are equal.
     *
     * @param b The Rational to compare to.
     * @return returns an integer that is the comparison between the two rationals.
     */
    public int compareTo(Quantity b) {
        if (b instanceof Complex) {
            Complex complex = (Complex) b;
            return complex.compareTo(this);
        }
        assert b instanceof Rational;
        Rational rational = (Rational) b;
        // Left-hand side
        BigInteger lhs = num.multiply(rational.den);
        // Right-hand side
        BigInteger rhs = den.multiply(rational.num);
        return lhs.compareTo(rhs);
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
        if (den.equals(BigInteger.ONE)) return num + "";
        return num + SEP + den;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}