import java.util.ArrayList;

public class ParallelAlgorithm {
    private ArrayList<ArrayList<Double>> matrix;
    private int rowSize;
    private int colSize;
    private static double EPSILON = 0.00000001;

    /**
     * Performs Gaussian Elimination on the matrix
     * @throws InterruptedException
     */
    public void GE() throws InterruptedException {
        for (int pivot = 0; pivot < rowSize; pivot++) {
            ArrayList<GEThread> threads = new ArrayList<>();
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
                for (int c = pivot; c < colSize; c++) { threads.add(new GEThread(matrix, r, c, value)); }
            }
            threads.forEach((n) -> (n).start());
            for (GEThread thread : threads) { thread.join(); }
        }
        ArrayList<RoundingThread> threads = new ArrayList<>();
        for (int r = 0; r < rowSize; r++) {
            for (int c = 0; c < colSize; c++) {
                threads.add(new RoundingThread(matrix, r, c));
            }
        }
        threads.forEach((n) -> (n).start());
        for (RoundingThread thread : threads) { thread.join(); }
    }

    public double[] backSubstitution() throws InterruptedException {
        double[] results = new double[rowSize];
        for (int pivot = matrix.size() - 1; pivot >= 0; pivot--) {
            ArrayList<BSThread> threads = new ArrayList<>();
            results[pivot] = matrix.get(pivot).get(rowSize);
            for (int row = pivot - 1; row >= 0; row--) {
                threads.add(new BSThread(matrix, row, pivot, results[pivot]));
            }
            threads.forEach((n) -> n.start());
            for (BSThread thread : threads) { thread.join(); }
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
