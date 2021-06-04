package me.darrionat.matrixlib.algebra.sets;

import me.darrionat.matrixlib.algebra.exceptions.NonRealException;

import java.math.BigInteger;
import java.util.HashMap;

/**
 * An immutable data type for values within the complex plane.
 *
 * @author Darrion Thornburgh
 */
public class Complex extends NumberSet<Complex> {
    /**
     * The complex representation of zero.
     */
    public static final Complex ZERO = new Complex(Rational.ZERO, Rational.ZERO);
    /**
     * The complex representation of one.
     */
    public static final Complex ONE = new Complex(Rational.ONE, Rational.ZERO);
    /**
     * A cache that represents powers of this complex number.
     * <p>
     * Key is the power to raise to, and the value is the result.
     *
     * @see #pow(int)
     */
    private final HashMap<Integer, Complex> powers = new HashMap<>();

    /**
     * The real value in the complex plane.
     */
    private final Rational real;
    /**
     * The coefficient of the imaginary value.
     */
    private final Rational scalar;

    /**
     * Constructs a new {@code Complex} number to indicate a value within the complex plane.
     *
     * @param real            The value along the real axis in the complex plane.
     * @param imaginaryScalar The value along the imaginary axis in the complex plane.
     */
    public Complex(Rational real, Rational imaginaryScalar) {
        this.real = real;
        this.scalar = imaginaryScalar;
        powers.put(0, ONE);
        powers.put(1, this);
    }

    /**
     * Determines if the value within the complex plane is a real value.
     *
     * @return {@code true} if there is no scalar of {@code i}; {@code false} otherwise.
     */
    public boolean isReal() {
        return scalar.zero();
    }

    /**
     * Converts the {@code Complex} value into a real {@code Rational}.
     *
     * @return The complex value represented only on the real axis.
     * @throws NonRealException Thrown when the {@code Complex} value is non-real.
     */
    public Rational toRational() {
        if (!isReal()) throw new NonRealException(this);
        return real;
    }

    @Override
    public Complex add(Complex b) {
        return new Complex(real.add(real), scalar.add(b.scalar));
    }

    @Override
    public Complex subtract(Complex b) {
        return add(b.negate());
    }

    @Override
    public Complex multiply(Complex b) {
        /*
            (a+bi)(c+di) = ac+adi+bci+bdi^2 = ac+bdi^2+adi+bci
            = (ac - bd) + (ad + bc)i
         */
        // Real = ac - bd
        Rational r = real.multiply(b.real).subtract(scalar.multiply(b.scalar));
        // Imaginary = (ad+bc)i
        Rational c = real.multiply(b.scalar).add(scalar.multiply(b.real));
        return new Complex(r, c);
    }

    @Override
    public Complex divide(Complex b) {
        /*
           (a+bi)   (c-di)   (ac-bd) + (bc-ad)i
           ------ * ------ = ------------------
           (c+di)   (c-di)       c^2 + cd
         */
        // c^2 + cd
        Rational divisor = b.real.pow(2).add(b.real.multiply(b.scalar));
        // Real = (ac-bd)/(c^2+cd)
        Rational r = real.multiply(b.real).subtract(scalar.multiply(b.scalar));
        // Imaginary = (bc-ad)i/(c^2+cd)
        Rational c = scalar.multiply(b.real).subtract(real.multiply(b.scalar));
        // Divide r and c by divisor
        return new Complex(r.divide(divisor), c.divide(divisor));
    }


    @Override
    public Complex pow(int pow) {
        if (powers.containsKey(pow))
            return powers.get(pow);

        // Convert into binary representation
        // 55 -> 32+16+0+4+2+1 -> 110111
        String binary = Integer.toBinaryString(Math.abs(pow));
        // Determine all values of the powers of 2 of this complex number
        // Start at the 2nd to last binary digit because the last is already known, because it represents 2^0 or 1.
        for (int i = binary.length() - 2, expo = 1; i >= 0; i--, expo++) {
            int powerOfTwo = (int) Math.pow(2, expo);
            // Get the previous power of two, e.g. 32 -> 16
            Complex prev = powers.get(powerOfTwo / 2);
            // Get this current power of two e.g. 32 -> complex ^ 2
            powers.put(powerOfTwo, prev.multiply(prev));
        }

        Complex result = ONE;
        for (int i = binary.length() - 1, expo = 0; i >= 0; i--, expo++) {
            int powerOfTwo = (int) Math.pow(2, expo);
            if (binary.charAt(i) == '1')
                result = result.multiply(powers.get(powerOfTwo));
        }
        return result;
    }

    @Override
    public Complex negate() {
        return new Complex(real.negate(), scalar.negate());
    }

    @Override
    public boolean zero() {
        return real.zero() && scalar.zero();
    }

    /**
     * Parses a {@code Complex} from a string.
     * <p>
     * The desired format: {@code a+bi}. The string should always contain a <i>+</i> regardless of b's value.
     * <p>
     * If the value within the complex plane is real, then do not include {@code +bi}.
     *
     * @param s The string containing the complex expression to be parsed.
     * @return A complex expression parsed from a string.
     * @throws NumberFormatException Thrown when the input string is not a valid {@code Complex} value.
     */
    public static Complex parseComplex(String s) {
        if (s == null) throw new NumberFormatException();
        String[] complex = s.split("\\+");
        Rational r = Rational.parseRational(complex[0]);

        if (complex.length != 2)
            return new Complex(r, Rational.ZERO);
        Rational c = Rational.parseRational(complex[1].replace("i", ""));
        return new Complex(r, c);
    }

    /**
     * Gets the distance from this complex number to {@code b} on the complex plane.
     * <p>
     * If the distance is greater than {@code Integer.MAX_VALUE}, the returned value will be the max value.
     *
     * @param b The complex number to compare to.
     * @return {@code 0} if both complex numbers are equal; otherwise the distance between the two points in the complex plane.
     * @see #equals(Object)
     */
    @Override
    public int compareTo(Complex b) {
        if (equals(b)) return 0;
        Rational rDiffSqr = real.subtract(b.real).pow(2);
        Rational cDiffSqr = scalar.subtract(b.scalar).pow(2);
        BigInteger distance = rDiffSqr.add(cDiffSqr).toBigInteger().sqrt();
        try {
            return Integer.parseInt(distance.toString());
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (obj instanceof Rational && scalar.zero()) {
            return obj.equals(real);
        }

        if (!(obj instanceof Complex)) return false;
        Complex b = (Complex) obj;
        return real.equals(b.real) && scalar.equals(b.scalar);
    }

    public String toString() {
        if (scalar.zero()) return real.toString();
        return real.toString() + "+" + scalar + "i";
    }

    public int hashCode() {
        return toString().hashCode();
    }
}