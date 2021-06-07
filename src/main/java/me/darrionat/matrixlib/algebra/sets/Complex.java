package me.darrionat.matrixlib.algebra.sets;

import me.darrionat.matrixlib.algebra.exceptions.NonRealException;

import java.math.BigInteger;
import java.util.HashMap;

/**
 * An immutable data type for values within the complex plane.
 *
 * @author Darrion Thornburgh
 */
public class Complex extends Number {

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
    private final HashMap<Integer, Number> powers = new HashMap<>();

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
    public Number add(Number b) {
        if (b instanceof Rational) {
            Rational rationalB = (Rational) b;
            return new Complex((Rational) real.add(rationalB), scalar);
        }
        assert b instanceof Complex;
        Complex complex = (Complex) b;
        Rational r = (Rational) real.add(complex.real);
        Rational c = (Rational) scalar.add(complex.scalar);
        return new Complex(r, c);
    }

    @Override
    public Number subtract(Number b) {
        return add(b.negate());
    }

    @Override
    public Number multiply(Number b) {
        if (b instanceof Rational) {
            Rational rationalB = (Rational) b;
            return new Complex(real.multiplyRational(rationalB), scalar.multiplyRational(rationalB));
        }
        /*
           (a+bi)(c+di) = ac+adi+bci+bdi^2 = ac+bdi^2+adi+bci
           = (ac - bd) + (ad + bc)i
         */
        assert b instanceof Complex;
        Complex complex = (Complex) b;
        // Real = ac - bd
        Rational r = real.multiplyRational(complex.real).subtractRational(scalar.multiplyRational(complex.scalar));
        // Imaginary = (ad+bc)i
        Rational c = real.multiplyRational(complex.scalar).addRational(scalar.multiplyRational(complex.real));
        return new Complex(r, c);
    }

    @Override
    public Number divide(Number b) {
        if (b instanceof Rational) {
            Rational rationalB = (Rational) b;
            return new Complex(real.divideRational(rationalB), scalar.divideRational(rationalB));
        }
        /*
           (a+bi)   (c-di)   (ac-bd) + (bc-ad)i
           ------ * ------ = ------------------
           (c+di)   (c-di)       c^2 + cd
         */
        assert b instanceof Complex;
        Complex complex = (Complex) b;
        // c^2 + cd
        Rational divisor = complex.real.pow(2).addRational(complex.real.multiplyRational(complex.scalar));
        // Real = (ac-bd)/(c^2+cd)
        Rational r = real.multiplyRational(complex.real).subtractRational(scalar.multiplyRational(complex.scalar));
        // Imaginary = (bc-ad)i/(c^2+cd)
        Rational c = scalar.multiplyRational(complex.real).subtractRational(real.multiplyRational(complex.scalar));
        // Divide r and c by divisor
        return new Complex(r.divideRational(divisor), c.divideRational(divisor));
    }


    @Override
    public Number pow(int pow) {
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
            Number prev = powers.get(powerOfTwo / 2);
            // Get this current power of two e.g. 32 -> complex ^ 2
            powers.put(powerOfTwo, prev.multiply(prev));
        }

        Number result = ONE;
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

    @Override
    public int toInt() throws ArithmeticException {
        if (!isReal()) throw new ArithmeticException("Imaginary value");
        return toRational().toInt();
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
    public int compareTo(Number b) {
        Complex compare;
        if (b instanceof Complex)
            compare = (Complex) b;
        else {
            assert b instanceof Rational;
            compare = new Complex((Rational) b, Rational.ZERO);
        }
        if (equals(b)) return 0;

        Rational rDiffSqr = real.subtractRational(compare.real).pow(2);
        Rational cDiffSqr = scalar.subtractRational(compare.scalar).pow(2);
        BigInteger distance = rDiffSqr.addRational(cDiffSqr).toBigInteger().sqrt();
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