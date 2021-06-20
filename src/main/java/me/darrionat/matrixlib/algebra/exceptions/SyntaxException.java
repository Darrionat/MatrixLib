package me.darrionat.matrixlib.algebra.exceptions;

/**
 * Thrown to indicate that an {@code Expression} has invalid syntax.
 *
 * @author Darrion Thornburgh
 */
public class SyntaxException extends RuntimeException {
    private static final long serialVersionUID = -2895981935328372749L;

    /**
     * Constructs a new {@code SyntaxException} with an argument representing an invalid expression.
     */
    public SyntaxException() {
        super("Invalid Syntax");
    }
}