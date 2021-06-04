package me.darrionat.matrixlib.algebra.exceptions;

import me.darrionat.matrixlib.algebra.sets.Complex;

/**
 * Thrown to indicate that a {@code Complex} value cannot be converted into a real {@code Rational}.
 *
 * @author Darrion Thornburgh
 */
public class NonRealException extends RuntimeException {
    /**
     * Constructs a new {@code ImaginaryException} with an argument being an imaginary value.
     *
     * @param imaginaryValue the value that is non-real.
     */
    public NonRealException(Complex imaginaryValue) {
        super(imaginaryValue + " is non-real");
    }
}