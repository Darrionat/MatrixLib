package me.darrionat.matrixlib.exceptions;

import me.darrionat.matrixlib.matrices.Matrix;

import java.io.File;

/**
 * Thrown to indicate that there was an error in reading a {@link Matrix} from a file.
 *
 * @author Darrion Thornburgh
 */
public class ReadMatrixException extends RuntimeException {
    /**
     * Constructs a new {@code ReadMatrixException} with an argument indicating what file does not have valid {@link
     * Matrix} syntax.
     *
     * @param savedFile the file that is not the correct format.
     */
    public ReadMatrixException(File savedFile) {
        super(savedFile.getPath() + " is not a valid matrix file or is corrupted");
    }

    /**
     * {@link ReadMatrixException} when an unexpected character arises.
     *
     * @param received the unexpected character.
     * @param expected the character that was expected.
     */
    public ReadMatrixException(char received, char expected) {
        super("Expected " + expected + " but received " + received);
    }
}