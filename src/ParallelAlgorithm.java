import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelAlgorithm {
    public Vector<Vector<Double>> getMatrix() {
        return matrix;
    }

    public Vector<Vector<Double>> matrix;
    private int rowSize;
    private int colSize;
    private static double EPSILON = 0.00000001;

    /**
     * Performs Gaussian Elimination on the matrix
     * @throws InterruptedException
     */
    public void GE() {
        for (int pivot = 0; pivot < rowSize; pivot++) {
            if (matrix.get(pivot).get(pivot) < EPSILON) {
                for (int i = pivot + 1; i < rowSize; i++) {
                    if (matrix.get(i).get(pivot) < EPSILON) {
                        swapRow(pivot, i);
                        break;
                    }
                }
            }
            Vector<Double> row = matrix.get(pivot);
            int cores = Runtime.getRuntime().availableProcessors();
            ExecutorService threadPool = Executors.newFixedThreadPool(cores);
            Vector<TargetCell> cells = new Vector<>();
            int cellAmount = (rowSize - pivot - 1) * (colSize - pivot) / cores + 1;
            row = multiplyRow(row, 1 / row.get(pivot));
            for (int r = pivot + 1; r < rowSize; r++) {
                Double value = -1 * matrix.get(r).get(pivot) / row.get(pivot);
                for (int c = pivot; c < colSize; c++) {
                    if (cells.size() >= cellAmount) {
                        threadPool.submit(new GEThread(matrix, copy(cells)));
                        cells.clear();
                    }
                    cells.add(new TargetCell(r, c, value));
                }
            }
            threadPool.shutdown();
            /*
            System.out.println();
            matrix.forEach((n) -> System.out.println(n));*/
        }
    }

    public double[] backSubstitution() {
        double[] results = new double[rowSize];
        for (int pivot = matrix.size() - 1; pivot >= 0; pivot--) {
            ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            results[pivot] = matrix.get(pivot).get(rowSize);
            for (int row = pivot - 1; row >= 0; row--) {
                threadPool.submit(new BSThread(matrix, row, pivot, results[pivot]));
            }
            threadPool.shutdown();
        }
        return results;
    }

    private Vector<TargetCell> copy(Vector<TargetCell> cells) {
        Vector<TargetCell> copy = new Vector<>();
        for (int i = 0; i < cells.size(); i++) {
            copy.add(cells.get(i));
        }
        return copy;
    }

    /**
     * Multiplies an entire row by a scalar
     * @param row the row to be multiplied
     * @param scalar the scalar to multiply with
     * @return the new row
     */
    private Vector<Double> multiplyRow(Vector<Double> row, double scalar) {
        row.replaceAll(n -> round(scalar * n, 8));
        return row;
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Swaps two rows
     * @param row1 the first row to swap
     * @param row2 the second row to swap
     */
    private void swapRow(int row1, int row2) {
        Vector<Double> temp = matrix.get(row2);
        matrix.set(row2, matrix.get(row1));
        matrix.set(row1, temp);
    }

    public ParallelAlgorithm(Vector<Vector<Double>> matrix) {
        this.matrix = matrix;
        rowSize = matrix.size();
        colSize = rowSize + 1;
    }
}
