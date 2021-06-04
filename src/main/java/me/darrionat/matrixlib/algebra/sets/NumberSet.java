package me.darrionat.matrixlib.algebra.sets;

public abstract class NumberSet<Quantity> implements Comparable<Quantity> {
    /**
     * Gets the sum of this quantity and a passed addend.
     *
     * @param b The addend.
     * @return The sum of this quantity plus a passed quantity.
     */
    public abstract Quantity add(Quantity b);

    /**
     * Gets the difference of this quantity to the passed value.
     *
     * @param b The value to subtract by.
     * @return The difference between this quantity and the passed value.
     */
    public abstract Quantity subtract(Quantity b);

    /**
     * Gets the product of this quantity and a given multiplicand.
     *
     * @param b The scalar or multiplicand.
     * @return The product of this quantity and by the passed multiplicand.
     */
    public abstract Quantity multiply(Quantity b);

    /**
     * Gets the quotient of this quantity by a given dividend.
     *
     * @param b The dividend.
     * @return The quotient of this quantity divided by the passed dividend.
     */
    public abstract Quantity divide(Quantity b);

    /**
     * Gets the result of this quantity raised to a given exponent.
     *
     * @param pow The power to raise the value to.
     * @return The quantity raised to the given exponent.
     */
    public abstract Quantity pow(int pow);

    /**
     * Gets the opposite of this quantity.
     *
     * @return returns the opposite value of the quantity.
     */
    public abstract Quantity negate();

    /**
     * Determines if the quantity is equal to zero.
     *
     * @return {@code true} if this quantity is equal to zero; {@code false} otherwise.
     */
    public abstract boolean zero();
}