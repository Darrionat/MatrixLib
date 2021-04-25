package me.darrionat.matrixlib.matrices;

import me.darrionat.matrixlib.exceptions.MatrixMultiplicationDimensionException;
import me.darrionat.matrixlib.util.MatrixIterator;
import me.darrionat.matrixlib.util.Rational;
import me.darrionat.matrixlib.vectors.Vector;

import java.util.Objects;

/**
 * Represents a {@code MxN} matrix.
 *
 * @author Darrion Thornburgh
 */
public class Matrix extends OperableMatrix {
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
    public Matrix(int rowAmount, int columnAmount, Rational[][] entries) {
        super(rowAmount, columnAmount, entries);
    }

    /**
     * Determines if the matrix is filled with all zero entries.
     *
     * @return {@code true} if all entries are zero; {@code false} otherwise.
     */
    public boolean isZero() {
        // There exists a non-zero value within the matrix
        for (Rational rational : this) if (!rational.zero()) return false;
        return true;
    }

    /**
     * Multiplies all values within the entire {@link Matrix} by a given scalar.
     *
     * @param scalar The multiplier
     */
    public void scale(Rational scalar) {
        MatrixIterator iterator = iterator();
        while (iterator.hasNext()) {
            Rational value = iterator.next();
            iterator.setValue(value.multiply(scalar));
        }
    }

    /**
     * Get the product of this matrix and another through row column matrix multiplication.
     * <p>
     * The dimensions of this matrix are MxN. The multiplier must be a NxP matrix. The product will be a MxP matrix.
     *
     * @param multiplier Another matrix that has an amount of rows equal to the amount of columns of this matrix.
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
            Rational[] rowVector = getRow(row);
            for (int col = 0; col < multiplier.columnAmount; col++) {
                // The vector multiplying the row
                Rational[] columnVector = multiplier.getColumn(col);
                // Dot product of the two vectors
                Rational dotProduct = Vector.getDotProduct(rowVector, columnVector);
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
        if (rowAmount == 1)
            return;

        for (int col = 0; col < columnAmount; col++) {
        /*
         Determine the leftmost non-zero column and the row of the non-zero entry
         */
            int leftMostNonZeroColIndex = -1, nonZeroEntryRow = -1;

            for (int c = col; c < columnAmount; c++) {
                for (int r = col; r < rowAmount; r++) {
                    if (getValue(r, c).zero()) continue;
                    leftMostNonZeroColIndex = c;
                    nonZeroEntryRow = r;
                    break;
                }
                if (nonZeroEntryRow != -1) break;
            }
            // Zero-Matrix
            if (leftMostNonZeroColIndex == -1) return;

            /*
            Use elementary row operations to put a 1 in the topmost position (we call this position pivot position) of this column.
            */
            // Swap non-zero entry row to top
            swapRows(col, nonZeroEntryRow);
            Rational[] leftMostNonZeroCol = getColumn(leftMostNonZeroColIndex);
            // Puts a 1 in the topmost position of this column
            // divideRow(col, leftMostNonZeroCol[col]);

            /*
             Use elementary row operations to put zeros (strictly) below the pivot
             */
            for (int r = col + 1; r < rowAmount; r++) {
                Rational value = getValue(r, leftMostNonZeroColIndex);
                if (value.zero()) continue;
                // The value to scale the subtracting row by to make the entry zero
                Rational c = value.divide(leftMostNonZeroCol[col]);
                rowSum(r, col, c.negate());
            }
        }
    }

    /**
     * Reduces the matrix into reduced row echelon form.
     */
    public void rref() {
        // ref();
        //Step 8. Determine all the leading ones in the row-echelon form obtained in
        //Step 7.
        //Step 9. Determine the right most column containing a leading one (we call
        //this column pivot column).
        //Step 10. Use type III elementary row operations to erase all the non-zero
        //entries above the leading one in the pivot column.
        //Step 11. If there are no more columns containing leading ones to the left of
        //the pivot column, then go to Step 13.
        //Step 12. Apply Step 9-11 to the submatrix consisting of the columns that lie
        //to the left of the pivot column.
        //13. The resulting matrix is in reduced row-echelon form.
    }
}