package me.darrionat.matrixlib.algebra.math;

import me.darrionat.matrixlib.algebra.sets.Quantity;

/**
 * Represents an arithmetic operation. This includes operations such as addition, multiplication, etc.
 *
 * @author Darrion Thornburgh
 */
public enum Operation {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    POW("^");

    private final String operator;

    Operation(String c) {
        this.operator = c;
    }

    /**
     * Determines if the given character is a valid operator.
     *
     * @param c The character to test for validity.
     * @return {@code true} if the character is a valid operator; {@code false} otherwise.
     */
    public static boolean validOperator(char c) {
        Operation[] values = values();
        for (Operation value : values)
            if ((c + "").equals(value.operator))
                return true;
        return false;
    }

    /**
     * Parses an {@code Operation} from a string.
     *
     * @param s The string to be parsed.
     * @return returns an {@code Operation} that was parsed from a string; {@code null} if not a valid operator.
     * @throws IllegalArgumentException thrown when the string contains more than one operation.
     */
    public static Operation parseOperator(String s) throws IllegalArgumentException {
        for (Operation operation : values())
            if (s.contains(operation.getOperator())) return operation;
        throw new IllegalArgumentException("Input must contain a single operator");
    }

    /**
     * Parses the operator of this type of operation.
     *
     * @return The operator string for this type of operation.
     */
    public String getOperator() {
        return this.operator;
    }

    /**
     * Performs this operation on two quantities.
     * <p>
     * For example: {@code a+b} or {@code a*b}.
     * <p>
     * For {@code a^b} to be a valid operation, {@code b} must be able to be parsed as an {@code int}.
     *
     * @param a The first quantity.
     * @param b The second quantity.
     * @return The result of this operation performed on two quantities.
     */
    public Quantity getResult(Quantity a, Quantity b) {
        switch (this) {
            case ADD:
                return a.add(b);
            case SUBTRACT:
                return a.subtract(b);
            case MULTIPLY:
                return a.multiply(b);
            case DIVIDE:
                return a.divide(b);
            case POW:
                return a.pow(b.intValue());
        }
        throw new ArithmeticException("Internal error: Operation not supported");
    }
}