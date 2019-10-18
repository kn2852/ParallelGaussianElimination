import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class SerialAlgorithm {
    public static ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
    private int rowSize;
    private int colSize;
    public static double EPSILON = 0.00000001;

    /**
     * Performs Gaussian Elimination on the matrix
     * @throws InterruptedException
     */
    public void GE() throws InterruptedException {
        for (int pivot = 0; pivot < rowSize; pivot++) {
            if (matrix.get(pivot).get(pivot) < EPSILON) {
                for (int i = pivot + 1; i < rowSize; i++) {
                    if (matrix.get(i).get(pivot) < EPSILON) {
                        swapRow(pivot, i);
                        break;
                    }
                }
            }
            ArrayList<Double> row = matrix.get(pivot);
            row = multiplyRow(row, 1 / row.get(pivot));
            for (int r = pivot + 1; r < rowSize; r++) {
                Double value = -1 * matrix.get(r).get(pivot) / row.get(pivot);
                for (int c = pivot; c < colSize; c++) {
                    double cellCurrent = matrix.get(r).get(c);
                    matrix.get(r).set(c, cellCurrent + value);
                }
            }
        }
        for (ArrayList<Double> row : matrix) {
            row.replaceAll((n) -> round(n, 6));
        }
    }

    public double[] backSubstitution() throws InterruptedException {
        double[] results = new double[rowSize];
        for (int pivot = matrix.size() - 1; pivot >= 0; pivot--) {
            results[pivot] = matrix.get(pivot).get(rowSize);
            for (int row = pivot - 1; row >= 0; row--) {
                double currentValue = matrix.get(row).get(pivot);
                matrix.get(row).set(matrix.size(), currentValue + results[pivot]);
                matrix.get(row).set(pivot, 0.0);
            }
        }
        return results;
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Multiplies an entire row by a scalar
     * @param row the row to be multiplied
     * @param scalar the scalar to multiply with
     * @return the new row
     */
    private ArrayList<Double> multiplyRow(ArrayList<Double> row, double scalar) {
        row.replaceAll(n -> scalar * n);
        return row;
    }

    /**
     * Swaps two rows
     * @param row1 the first row to swap
     * @param row2 the second row to swap
     */
    private void swapRow(int row1, int row2) {
        ArrayList<Double> temp = matrix.get(row2);
        matrix.set(row2, matrix.get(row1));
        matrix.set(row1, temp);
    }

    public SerialAlgorithm(ArrayList<ArrayList<Double>> matrix) {
        this.matrix = matrix;
        rowSize = matrix.size();
        colSize = rowSize + 1;
    }

}