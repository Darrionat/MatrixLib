import me.darrionat.matrixlib.builder.MatrixBuilder;
import me.darrionat.matrixlib.exceptions.ReadMatrixException;
import me.darrionat.matrixlib.matrices.Matrix;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressionHandler {
    public void compress(Matrix matrix, String destination) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzipOut = new GZIPOutputStream(baos);
        ObjectOutputStream objectOut = new ObjectOutputStream(gzipOut);
        // Write matrix to object output
        objectOut.writeObject(matrix.toString());
        objectOut.close();
        // Write matrix to file
        FileOutputStream fos = new FileOutputStream(destination);
        baos.writeTo(fos);
        // Close things
        gzipOut.finish();
        gzipOut.close();
        baos.close();
    }

    public Matrix loadCompressedMatrix(String source) throws IOException, ClassNotFoundException {
        File file = new File(source);
        FileInputStream fin = new FileInputStream(file);
        byte[] fileContent = new byte[(int) file.length()];
        // Reads up to certain bytes of data from this input stream into an array of bytes.
        fin.read(fileContent);

        ByteArrayInputStream bais = new ByteArrayInputStream(fileContent);
        GZIPInputStream gzipIn = new GZIPInputStream(bais);
        ObjectInputStream objectIn = new ObjectInputStream(gzipIn);
        String matrixStr;
        try {
            matrixStr = (String) objectIn.readObject();
        } catch (ArrayStoreException e) {
            throw new ReadMatrixException(file);
        }
        objectIn.close();
        return MatrixBuilder.fromString(matrixStr);
    }
}