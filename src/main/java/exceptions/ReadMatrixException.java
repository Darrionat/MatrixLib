package exceptions;

import matrices.Matrix;

import java.io.File;

/**
 * Represents an error in reading a {@link Matrix} from a file.
 */
public class ReadMatrixException extends RuntimeException {
    /**
     * {@link ReadMatrixException} when a saved serialized matrix cannot be read.
     *
     * @param savedFile The file that is not the correct format
     */
    public ReadMatrixException(File savedFile) {
        super(savedFile.getPath() + " is not a valid matrix file or is corrupted");
    }

    /**
     * {@link ReadMatrixException} when an unexpected character arises.
     *
     * @param received The unexpected character
     * @param expected The character that was expected
     */
    public ReadMatrixException(char received, char expected) {
        super("Expected " + expected + " but received "+ received);
    }
}