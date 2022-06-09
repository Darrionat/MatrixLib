package me.darrionat.matrixlib.algebra;

import me.darrionat.matrixlib.algebra.math.Operation;
import me.darrionat.matrixlib.algebra.exceptions.SyntaxException;
import me.darrionat.matrixlib.algebra.sets.Quantity;
import me.darrionat.matrixlib.util.KeyValue;

import java.util.Objects;

/**
 * Represents multiple operations that can be performed to obtain a result.
 * <p>
 * An expression contains no algebraic variables.
 *
 * @author Darrion Thornburgh
 */
public class Expression {
    /**
     * The expression represented as a string.
     */
    private final String expression;
    /**
     * The amount of pairs of parentheses within the expression.
     */
    private final int parens;

    /**
     * Constructs a {@code Expression} with a valid expression and amount of left and right parentheses.
     *
     * @param expression A valid expression.
     * @param parens     The amount of pairs of parentheses within the expression.
     */
    private Expression(String expression, int parens) {
        this.expression = expression;
        this.parens = parens;
    }

    /**
     * Builds a new {@code Expression} from the given string.
     *
     * @param s The string of the expression to parse.
     * @return The parsed {@code Expression}.
     * @throws SyntaxException Thrown when the expression does not contain at least one operator or sequential operators.
     */
    public static Expression buildExpression(String s) throws SyntaxException {
        Objects.requireNonNull(s, "Expression cannot be null");
        s = s.replaceAll("\\[", "(");
        s = s.replaceAll("]", ")");
        KeyValue<Integer, Integer> parens = getLeftRightParenCount(s);
        int left = parens.getKey(), right = parens.getValue();
        // If there are more right parentheses, there is a syntax error.
        if (left < right)
            throw new SyntaxException();
        // Check for invalid operators
        if (!validOperators(s))
            throw new SyntaxException();
        String expression;
        // If there are an equal amount of left and right parentheses, valid expression.
        if (left == right) {
            expression = s;
        } else { // If there are more left parentheses than right parentheses, add right parentheses to the end.
            StringBuilder builder = new StringBuilder(s);
            // If equal, no iterations
            for (int i = 0; i < left - right; i++)
                builder.append(")");
            expression = builder.toString();
        }
        // Assert: The amount of left/right parens are now equal
        return new Expression(expression, left);
    }

    /**
     * Gets the amount of the parentheses contained within the expression as a {@code KeyValue}.
     * <p>
     * The {@code key} is the amount of left parentheses and the {@code value} is the amount of right parentheses.
     *
     * @return The amount of left and right parentheses within an expression represented as a key value pair, respectively.
     */
    private static KeyValue<Integer, Integer> getLeftRightParenCount(String expression) {
        int left = 0, right = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') left++;
            if (c == ')') right++;
        }
        return new KeyValue<>(left, right);
    }

    /**
     * Checks the expression as a string for valid operators.
     *
     * @param expression The expression to validate.
     * @return {@code true} if the string is a valid expression; {@code false} otherwise.
     */
    private static boolean validOperators(String expression) {
        char[] chars = expression.toCharArray();
        int n = chars.length;
        // Checks for a leading or trailing operator.
        if (Operation.validOperator(chars[0]) || Operation.validOperator(chars[n - 1]))
            return false;
        boolean operatorAsLastChar = false;
        for (int i = 1; i < n - 1; i++) {
            boolean isOperator = Operation.validOperator(chars[i]);
            // If this character and the previous one are both operators, invalid operation.
            if (isOperator && operatorAsLastChar)
                return false;
            // Set the last visited character's state to be this state for next iteration.
            operatorAsLastChar = isOperator;
        }
        return true;
    }

    /**
     * Gets the quantity that the expression is equal to.
     *
     * @return The evaluated expression.
     */
    public Quantity evaluate() {
        return Interpreter.evaluateExpression(this);
    }

    /**
     * The number of pairs of parentheses within the expression.
     *
     * @return The pairs of parentheses within this expression.
     */
    public int getParentheses() {
        return parens;
    }

    /**
     * The amount of operations within the expression.
     *
     * @return The amount of operators contained within the expression.
     */
    public int operators() {
        int operators = 0;
        for (char c : expression.toCharArray())
            if (Operation.validOperator(c)) operators++;
        return operators;
    }

    @Override
    public String toString() {
        return expression;
    }
}