package me.darrionat.matrixlib.algebra.math;

import me.darrionat.matrixlib.algebra.sets.Quantity;

/**
 * A quantity that is able to have mathematical operations performed on it.
 *
 * @author Darrion Thornburgh
 */
public interface Operable {
    /**
     * Gets the sum of this quantity and a passed addend.
     *
     * @param b The addend.
     * @return The sum of this quantity plus a passed quantity.
     */
    Quantity add(Quantity b);

    /**
     * Gets the difference of this quantity to the passed value.
     *
     * @param b The value to subtract by.
     * @return The difference between this quantity and the passed value.
     */
    Quantity subtract(Quantity b);

    /**
     * Gets the product of this quantity and a given multiplicand.
     *
     * @param b The scalar or multiplicand.
     * @return The product of this quantity and by the passed multiplicand.
     */
    Quantity multiply(Quantity b);

    /**
     * Gets the quotient of this quantity by a given dividend.
     *
     * @param b The dividend.
     * @return The quotient of this quantity divided by the passed dividend.
     */
    Quantity divide(Quantity b);

    /**
     * Gets the result of this quantity raised to a given exponent.
     *
     * @param pow The power to raise the value to.
     * @return The quantity raised to the given exponent.
     */
    Quantity pow(int pow);

    /**
     * Gets the opposite of this quantity.
     *
     * @return returns the opposite value of the quantity.
     */
    Quantity negate();

    /**
     * Determines if the quantity is equal to zero.
     *
     * @return {@code true} if this quantity is equal to zero; {@code false} otherwise.
     */
    boolean zero();
}