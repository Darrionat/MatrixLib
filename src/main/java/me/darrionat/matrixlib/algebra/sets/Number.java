package me.darrionat.matrixlib.algebra.sets;

/**
 * Represents a mathematical quantity or set of numbers. This includes complex numbers, rationals, etc.
 *
 * @author Darrion Thornburgh
 */
public abstract class Number implements Comparable<Number> {
    /**
     * Gets the sum of this quantity and a passed addend.
     *
     * @param b The addend.
     * @return The sum of this quantity plus a passed quantity.
     */
    public abstract Number add(Number b);

    /**
     * Gets the difference of this quantity to the passed value.
     *
     * @param b The value to subtract by.
     * @return The difference between this quantity and the passed value.
     */
    public abstract Number subtract(Number b);

    /**
     * Gets the product of this quantity and a given multiplicand.
     *
     * @param b The scalar or multiplicand.
     * @return The product of this quantity and by the passed multiplicand.
     */
    public abstract Number multiply(Number b);

    /**
     * Gets the quotient of this quantity by a given dividend.
     *
     * @param b The dividend.
     * @return The quotient of this quantity divided by the passed dividend.
     */
    public abstract Number divide(Number b);

    /**
     * Gets the result of this quantity raised to a given exponent.
     *
     * @param pow The power to raise the value to.
     * @return The quantity raised to the given exponent.
     */
    public abstract Number pow(int pow);

    /**
     * Gets the opposite of this quantity.
     *
     * @return returns the opposite value of the quantity.
     */
    public abstract Number negate();

    /**
     * Determines if the quantity is equal to zero.
     *
     * @return {@code true} if this quantity is equal to zero; {@code false} otherwise.
     */
    public abstract boolean zero();

    /**
     * Parses the quantity as a Java integer.
     *
     * @return The quantity as an {@code int}.
     * @throws ArithmeticException thrown when the value is not within {@code Integer.MIN_VALUE} to {@code Integer.MAX_VALUE}
     *                             or if the value cannot be represented as an integer.
     */
    public abstract int toInt() throws ArithmeticException;

    /**
     * Parses a number from a string.
     *
     * @param s The string to be parsed.
     * @return the parsed value.
     */
    public static Number parseNumber(String s) {
        Complex c = Complex.parseComplex(s);
        if (c.isReal()) return c.toRational();
        return c;
    }
}