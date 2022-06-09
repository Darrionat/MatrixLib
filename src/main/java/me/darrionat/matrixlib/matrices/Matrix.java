package me.darrionat.matrixlib.matrices;

import me.darrionat.matrixlib.algebra.sets.Complex;
import me.darrionat.matrixlib.algebra.sets.Quantity;
import me.darrionat.matrixlib.algebra.sets.Rational;
import me.darrionat.matrixlib.exceptions.DimensionException;
import me.darrionat.matrixlib.exceptions.MatrixMultiplicationDimensionException;

import java.util.Objects;

/**
 * Represents a {@code MxN} matrix.
 *
 * @author Darrion Thornburgh
 */
public class Matrix extends OperableMatrix {
    private static final String DIMENSION_ERROR = "Vector dimensions are not equal";

    /**
     * Creates a new MxN matrix.
     *
     * @param rowAmount    The amount of rows in the matrix, represented as M.
     * @param columnAmount The amount of columns in the matrix, represented as N.
     */
    public Matrix(int rowAmount, int columnAmount) {
        this(rowAmount, columnAmount, null);
    }

    /**
     * Creates a new MxN matrix with defined entries.
     *
     * @param rowAmount    The amount of rows in the matrix, represented as M.
     * @param columnAmount The amount of columns in the matrix, represented as N.
     * @param entries      The values within the matrix
     */
    public Matrix(int rowAmount, int columnAmount, Quantity[][] entries) {
        super(rowAmount, columnAmount, entries);
    }

    /**
     * Determines if the matrix is filled with all zero entries.
     *
     * @return {@code true} if all entries are zero; {@code false} otherwise.
     */
    public boolean zero() {
        for (Quantity value : this)
            if (!value.zero()) return false;
        return true;
    }

    /**
     * Gets the a {@code Matrix} whose values are all a multiple of this matrix's entries by a factor of the {@code
     * scalar}.
     *
     * @param scalar The multiplier.
     * @return The result of this matrix being scaled by the given quantity.
     */
    public Matrix scale(Quantity scalar) {
        Matrix scaledMatrix = new Matrix(rowAmount, columnAmount, entries);
        for (int row = 0; row < rowAmount; row++)
            for (int col = 0; col < columnAmount; col++)
                scaledMatrix.setValue(row, col, getValue(row, col).multiply(scalar));
        return scaledMatrix;
    }

    /**
     * Get the entry-wise sum of this matrix and another matrix.
     * <p>
     * The dimensions of this matrix are MxN. The {@code addend} must also be a MxN matrix. The sum will be a MxN
     * matrix.
     *
     * @param addend the matrix being added.
     * @return calculates the sum of this matrix and another matrix.
     * @throws DimensionException thrown when the matrices do not have the same dimensions.
     */
    public Matrix addEntries(Matrix addend) {
        if (rowAmount != addend.rowAmount || columnAmount != addend.columnAmount)
            throw new DimensionException(this, addend);

        Matrix sum = new Matrix(rowAmount, columnAmount);
        for (int row = 0; row < rowAmount; row++)
            for (int col = 0; col < columnAmount; col++)
                // entry = a+b
                sum.setValue(row, col, getValue(row, col).add(addend.getValue(row, col)));
        return sum;
    }

    /**
     * Get the product of this matrix and another through row column matrix multiplication.
     * <p>
     * The dimensions of this matrix are MxN. The {@code multiplier} must be a NxP matrix. The product will be a MxP
     * matrix.
     *
     * @param multiplier Another matrix that has an amount of rows which equals the amount of columns of this matrix.
     * @return Calculates the product of this matrix and another matrix.
     * @throws MatrixMultiplicationDimensionException thrown when the matrices are not compatible.
     */
    public Matrix multiply(Matrix multiplier) {
        Objects.requireNonNull(multiplier);
        // Handle illegal dimensions
        if (columnAmount != multiplier.rowAmount)
            throw new MatrixMultiplicationDimensionException(this, multiplier);

        Matrix product = new Matrix(rowAmount, multiplier.columnAmount);
        for (int row = 0; row < rowAmount; row++) {
            // The row to be multiplied along
            Quantity[] rowVector = getRow(row);
            for (int col = 0; col < multiplier.columnAmount; col++) {
                // The vector multiplying the row
                Quantity[] columnVector = multiplier.getColumn(col);
                // Dot product of the two vectors
                Quantity dotProduct = getDotProduct(rowVector, columnVector);
                // Set the position to the dot product
                product.setValue(row, col, dotProduct);
            }
        }
        return product;
    }

    /**
     * Reduces the matrix into row echelon form.
     */
    public void ref() {
        // Already in REF
        if (rowAmount == 1) return;

        // Loop from left to right across the matrix
        for (int col = 0; col < columnAmount; col++) {
            // Determine the leftmost non-zero column and the row of the non-zero entry
            int leftMostNonZeroCol = -1, nonZeroEntryRow = -1;
            for (int c = col; c < columnAmount && nonZeroEntryRow == -1; c++) {
                for (int r = col; r < rowAmount; r++) {
                    if (getValue(r, c).zero()) continue;
                    leftMostNonZeroCol = c;
                    nonZeroEntryRow = r;
                    break;
                }
            }
            // Zero-Matrix
            if (leftMostNonZeroCol == -1) return;
            // Swap non-zero entry row to top
            swapRows(col, nonZeroEntryRow);

            // Use  row operations to put zeros (strictly) below the pivot
            for (int r = col + 1; r < rowAmount; r++) {
                Quantity value = getValue(r, leftMostNonZeroCol);
                if (value.zero()) continue;
                // The value to scale the subtracting row by to make the entry zero
                Quantity c = value.divide(getValue(col, leftMostNonZeroCol));
                rowSum(r, col, c.negate());
            }
        }
    }

    /**
     * Reduces the matrix into reduced row echelon form.
     */
    public void rref() {
        // Already in RREF
        if (rowAmount == 1) return;
        ref();
        //  Make all leading pivots equal to 1
        for (int row = 0; row < rowAmount; row++)
            for (int col = row; col < columnAmount; col++) {
                Quantity value = getValue(row, col);
                if (!value.zero()) {
                    divideRow(row, value);
                    break;
                }
            }

        // Loop from bottom to top across the matrix to reduce
        for (int row = rowAmount - 1; row >= 0; row--) {

            // Determine the rightmost column containing a leading one.
            int pivotRow = -1, pivotCol = -1;
            for (int r = row; r >= 0 && pivotRow == -1; r--) {
                for (int c = 0; c < columnAmount; c++) {
                    Quantity value = getValue(r, c);
                    if (!value.equals(Rational.ONE) && !value.equals(Complex.ONE)) continue;
                    pivotRow = r;
                    pivotCol = c;
                    break;
                }
            }
            // Zero-Matrix
            if (pivotRow == -1) return;
            /*
             * Use row operations to erase all the non-zero entries above the leading one in the pivot column.
             */
            for (int r = pivotRow - 1; r >= 0; r--) {
                Quantity value = getValue(r, pivotCol);
                if (value.zero()) continue;
                // The value to scale the subtracting row by to make the entry zero
                rowSum(r, row, getValue(r, pivotCol).negate());
            }
        }
    }

    /**
     * Calculates the dot product of two vectors of same dimension.
     *
     * @param v The first vector
     * @param u The second vector
     * @return Returns the dot product of two vectors
     * @throws IllegalArgumentException thrown when the vectors' dimensions are not equal.
     */
    private static Quantity getDotProduct(Quantity[] v, Quantity[] u) {
        if (v.length != u.length)
            throw new IllegalArgumentException(DIMENSION_ERROR);

        Quantity dotProduct = Complex.ZERO;
        for (int i = 0; i < v.length; i++)
            dotProduct = dotProduct.add(v[i].multiply(u[i]));
        return dotProduct;
    }
}