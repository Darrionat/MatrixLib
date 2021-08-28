package me.darrionat.matrixlib.algebra;

import me.darrionat.matrixlib.algebra.math.Operation;
import me.darrionat.matrixlib.algebra.sets.Quantity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code Interpreter} is a handler for evaluating algebraic expressions.
 *
 * @author Darrion Thornburgh
 */
public class Interpreter {
    /**
     * Gets the quantity of what an expression is equivalent to.
     *
     * @param expression The expression to evaluate.
     * @return The evaluated value of the expression.
     */
    static Quantity evaluateExpression(Expression expression) {
        // Simple expression, base case
        if (expression.getParentheses() == 0)
            return evaluateNoParensExpression(expression);
        String expressionStr = expression.toString();
        char[] chars = expressionStr.toCharArray();
        // The count of left and right parentheses at the current index
        int left = 0, right = 0;
        // Saves the last index for left and right parentheses
        int leftIndex = -1, rightIndex = -1;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            // Counting left and right parentheses
            if (c == '(') {
                left++;
                leftIndex = i;
                continue;
            } else if (c == ')') {
                right++;
                rightIndex = i;
            }
            // Haven't found valid parentheses
            if (left != right || left == 0) continue;
            String subExpressionStr = expressionStr.substring(leftIndex + 1, rightIndex);
            Quantity value = evaluateExpression(subExpressionStr);
            // Determines if there should be a multiplication sign before the parentheses
            boolean multiplyLeft = leftIndex - 1 >= 0 && !Operation.validOperator(chars[leftIndex - 1]);
            // Determines if there should be a multiplication sign after the parentheses
            boolean multiplyRight = rightIndex + 1 < chars.length && !Operation.validOperator(chars[rightIndex + 1]);
            StringBuilder builder = new StringBuilder();
            // Append everything before the replacement
            builder.append(expressionStr, 0, leftIndex);
            // Handle parens not having * on left side
            if (multiplyLeft) builder.append("*");
            // Append the calculated value
            builder.append(value);
            // Handle parens not having * on right side
            if (multiplyRight) builder.append("*");
            // Append everything after the replacement
            builder.append(expressionStr, rightIndex + 1, expressionStr.length());
            // Recursively evaluate new expression
            return evaluateExpression(builder.toString());
        }
        return null;
    }

    /**
     * Gets the quantity of what an expression is equivalent to.
     * <p>
     * {@code s} is built into an {@link Expression} which is then evaluated.
     *
     * @param s The string of the expression to evaluate.
     * @return The evaluated value of the expression.
     */
    private static Quantity evaluateExpression(String s) {
        // assert s is always a valid expression or internal error
        return evaluateExpression(Expression.buildExpression(s));
    }

    /**
     * Evaluates an expression containing no parentheses.
     *
     * @param expression The expression to evaluate.
     * @return The evaluated value of the expression.
     */
    private static Quantity evaluateNoParensExpression(Expression expression) {
        List<String> splitExpression = splitSimpleExpression(expression);
        // The size can only be odd. So if it's one, it's just a quantity.
        // If the split expression has a size greater than 1, it needs to be evaluated.
        if (splitExpression.size() == 1)
            return Quantity.parseNumber(splitExpression.get(0));
        String[] evaluate = splitExpression.toArray(new String[0]);
        while (evaluate.length >= 3) {
            // Find operator with the highest precedence
            Operation operation = getHighestOperator(evaluate);
            assert operation != null;
            for (int i = 1; i < evaluate.length; i += 2) {
                if (Operation.parseOperator(evaluate[i]) != operation) continue;
                // Use that operator on string before and after operator
                Quantity a = Quantity.parseNumber(evaluate[i - 1]), b = Quantity.parseNumber(evaluate[i + 1]);
                Quantity result = operation.getResult(a, b);
                // Replace all 3 parts with the result, resulting in an array w/ a size of 2 less
                ArrayUtil<String> arrayUtil = new ArrayUtil<>();
                evaluate = arrayUtil.arrayReplace(evaluate, i - 1, i + 1, result.toString());
                break;
            }
        }
        return Quantity.parseNumber(evaluate[0]);
    }

    /**
     * Gets the {@code Operation} with the highest precedence within an array of strings, representing an {@code Expression}.
     *
     * @param expression The split expression.
     * @return The operation that should be executed first within an expression.
     */
    private static Operation getHighestOperator(String[] expression) {
        Operation highest = null;
        // Operators can't be at the start and are every other entry
        for (int i = 1; i < expression.length; i += 2) {
            // Assert that every other index is an operator.
            // assert Operation.validOperator(expression[i].toCharArray()[0]);
            Operation operation = Operation.parseOperator(expression[i]);
            // Exponents should always be first
            if (operation == Operation.POW) return Operation.POW;
            if (highest == null) highest = operation;
            // Multiplication or division takes priority over addition or subtraction
            if (highest == Operation.ADD || highest == Operation.SUBTRACT)
                if (operation == Operation.MULTIPLY || operation == Operation.DIVIDE)
                    highest = operation;
        }
        return highest;
    }

    /**
     * Splits a given {@code Expression} at every operator but keeps the operator within the array.
     * <p>
     * {@code "3+4*15"} will be split into {@code ["3", "+", "4", "*", "15"]}.
     * Note that the list size will always be odd.
     *
     * @param expression The expression with no parentheses to split.
     * @return The string split at every operator
     * @throws IllegalArgumentException thrown when the expression contains parentheses.
     */
    private static List<String> splitSimpleExpression(Expression expression) {
        if (expression.getParentheses() != 0)
            throw new IllegalArgumentException("Expression contains parentheses");

        List<String> splitExpression = new ArrayList<>();
        // The expression contains no operators, so no splitting is needed
        if (expression.operators() == 0) {
            splitExpression.add(expression.toString());
            return splitExpression;
        }

        char[] chars = expression.toString().toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : chars) {
            // If the char isn't an operator, it's part of a quantity, so append
            if (!Operation.validOperator(c)) {
                builder.append(c);
                continue;
            }
            // c is an operator, so add the builder, add the operator, and reset the builder
            splitExpression.add(builder.toString());
            splitExpression.add(c + "");
            builder = new StringBuilder();
        }
        // Add the final part of the expression
        splitExpression.add(builder.toString());
        return splitExpression;
    }

    /**
     * Represents a util that executes array replacement and resizing.
     *
     * @param <T> The type of array to be worked on.
     */
    private static class ArrayUtil<T> {
        /**
         * Replaces all values within a range, of an array, with a given value.
         *
         * @param arr   The array to copy data from.
         * @param from  The starting index to replace.
         * @param to    The last index to replace until (inclusive).
         * @param value The value to replace the range with.
         * @return The array with a replaced range with a new value.
         */
        T[] arrayReplace(T[] arr, int from, int to, T value) {
            if (to < from)
                throw new IllegalArgumentException("Invalid range");
            int diff = to - from;
            int size = arr.length - diff;

            T[] toReturn = Arrays.copyOf(arr, size);
            for (int i = 0; i < toReturn.length; i++) {
                if (i < from) {
                    toReturn[i] = arr[i];
                } else if (i == from) {
                    toReturn[i] = value;
                } else {
                    // i is greater than from
                    toReturn[i] = arr[i + diff];
                }
            }
            return toReturn;
        }
    }
}