import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelAlgorithm {
    private ArrayList<ArrayList<Double>> matrix;
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
            ArrayList<Double> row = matrix.get(pivot);
            ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            //ExecutorService threadPool = Executors.newFixedThreadPool(6);
            row = multiplyRow(row, 1 / row.get(pivot));
            for (int r = pivot + 1; r < rowSize; r++) {
                Double value = -1 * matrix.get(r).get(pivot) / row.get(pivot);
                for (int c = pivot; c < colSize; c++) {
                    threadPool.submit(new GEThread(matrix, r, c, value));
                }
            }
            threadPool.shutdown();
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int r = 0; r < rowSize; r++) {
            for (int c = 0; c < colSize; c++) {
                threadPool.submit(new RoundingThread(matrix, r, c));
            }
        }
        threadPool.shutdown();
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

    public ParallelAlgorithm(ArrayList<ArrayList<Double>> matrix) {
        this.matrix = matrix;
        rowSize = matrix.size();
        colSize = rowSize + 1;
    }
}
