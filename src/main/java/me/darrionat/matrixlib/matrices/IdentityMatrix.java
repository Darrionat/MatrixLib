package me.darrionat.matrixlib.matrices;

import me.darrionat.matrixlib.algebra.sets.Quantity;
import me.darrionat.matrixlib.algebra.sets.Rational;

/**
 * Represents the identity matrix for a particular NxN sized matrix.
 * <p>
 * A {@code IdentityMatrix} is immutable.
 * <p>
 * Identity matrices are always triangular and always have a determinant of 1.
 *
 * @author Darrion Thornburgh
 */
public final class IdentityMatrix extends SquareMatrix {
    /**
     * Creates a NxN {@code IdentityMatrix}.
     * <p>
     * An identity matrix is a {@link SquareMatrix} that has {@code 1} along the diagonal and all other entries are
     * {@code 0}.
     *
     * @param dimension The amount of rows and columns the matrix will have
     */
    public IdentityMatrix(int dimension) {
        super(dimension);
        for (int i = 0; i < rowAmount; i++)
            super.setValue(i, i, Rational.ONE);
    }

    @Override
    public final void setColumn(int column, Quantity[] values) {
    }

    @Override
    public final void setRow(int row, Quantity[] values) {
    }

    @Override
    public final void setValue(int row, int column, Quantity value) {
    }

    @Override
    public final void swapRows(int a, int b) {
    }

    @Override
    public final void swapColumns(int a, int b) {
    }

    @Override
    public final void multiplyRow(int row, Quantity scalar) {
    }

    @Override
    public final void divideRow(int row, Quantity dividend) {
    }

    @Override
    public final void multiplyColumn(int column, Quantity scalar) {
    }

    @Override
    public final void divideColumn(int column, Quantity dividend) {
    }

    @Override
    public final void ref() {
    }

    @Override
    public final void rref() {
    }
}