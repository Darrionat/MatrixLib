import me.darrionat.matrixlib.builder.MatrixBuilder;
import me.darrionat.matrixlib.matrices.Matrix;
import me.darrionat.matrixlib.matrices.SquareMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TestMatrix {
    public static final String MATRICES_FOLDER = "me/darrionat/matrixlib/matrices" + File.separator;
    public static final String SAVED_MATRIX_CSV = MATRICES_FOLDER + "savedMatrix.csv";
    public static final String COMPRESSED_MATRIX = MATRICES_FOLDER + "compressedMatrix.matrix";

    private static final boolean SAVE_CSV = false;

    public static void main(String[] args) {
        File folder = new File(MATRICES_FOLDER);
        if (!folder.exists())
            folder.mkdir();
        time("WHOLE");
        int N = 1500;
        System.out.println("N=" + N);
        Matrix matrix = new SquareMatrix(N);
        Matrix matrix2 = new SquareMatrix(N);

        Random r = new Random();
        time("Matrix Creation");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int r1 = r.nextInt(100);
                matrix.setValue(i, j, r1);
                int r2 = r.nextInt(100);
                matrix2.setValue(i, j, r2);
            }
        }
        endTime("Matrix Creation");

        time("Matrix Multiplication");
        Matrix product = matrix.multiply(matrix2);
        endTime("Matrix Multiplication");

        time("Compression");
        CompressionHandler compress = new CompressionHandler();
        try {
            compress.compress(product, COMPRESSED_MATRIX);
        } catch (IOException e) {
            e.printStackTrace();
        }
        endTime("Compression");

        time("Decompression");
        Matrix decompressedMatrix = null;
        try {
            decompressedMatrix = compress.loadCompressedMatrix(COMPRESSED_MATRIX);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        endTime("Decompression");

        if (SAVE_CSV) {
            time("To CSV");
            if (decompressedMatrix != null)
                saveToCSV(decompressedMatrix, SAVED_MATRIX_CSV);
            endTime("To CSV");
        }
        endTime("WHOLE");
    }

    private static final HashMap<String, Long> TIMES = new HashMap<>();

    private static void time(String timer) {
        TIMES.put(timer, System.currentTimeMillis());
    }

    private static void endTime(String timer) {
        long time = System.currentTimeMillis() - TIMES.remove(timer);
        System.out.println(timer + " (ms): " + time);
    }

    /**
     * Reads a matrix from a CSV file.
     *
     * @param fileName The path of the file
     * @return Returns a {@link Matrix} read from a CSV file
     */
    public static Matrix readFromCSV(String fileName) {
        Scanner sc;
        // init scanner
        try {
            sc = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        // Read lines in file and save them in ArrayList
        List<String[]> readInputs = new ArrayList<>();
        while (sc.hasNext()) {
            String[] rowStr = sc.next().split(",");
            readInputs.add(rowStr);
        }
        sc.close();

        // Set up matrix
        int rows = readInputs.size();
        int cols = readInputs.get(0).length;
        MatrixBuilder matrix = new MatrixBuilder(rows, cols);
        // Read rows
        for (int i = 0; i < rows; i++) {
            String[] rowStr = readInputs.get(i);
            double[] row = new double[cols];
            // From string to double
            for (int j = 0; j < cols; j++) {
                row[j] = Double.parseDouble(rowStr[j]);
            }
            // Insert row into matrix
            matrix.setRow(i, row);
        }
        return matrix.build();
    }

    /**
     * Saves a matrix to a CSV file
     *
     * @param matrix   The matrix to save
     * @param fileName The path of the file to save to
     */
    public static void saveToCSV(Matrix matrix, String fileName) {
        File file = new File(fileName);
        FileWriter writer;
        try {
            writer = new FileWriter(file);
            for (int i = 0; i < matrix.getRowAmount(); i++) {
                double[] row = matrix.getRow(i);
                StringBuilder line = new StringBuilder();
                for (double v : row) {
                    if (line.isEmpty()) {
                        line = new StringBuilder("" + v);
                        continue;
                    }
                    line.append(",").append(v);
                }
                writer.write(line.toString());
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void print(Matrix matrix) {
        int rows = matrix.getRowAmount();
        int cols = matrix.getColumnAmount();

        System.out.println(rows + "x" + cols + " Matrix");

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (col == 0)
                    System.out.print(matrix.getValue(row, col));
                else
                    System.out.print("\t" + matrix.getValue(row, col));
            }
            System.out.print("\n");
        }
    }
}