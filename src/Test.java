import java.util.ArrayList;
import java.util.ArrayList;

public class Test {
    private final ArrayList<ArrayList<Double>> matrix64 = populateMatrix(64);
    private final ArrayList<ArrayList<Double>> matrix256 = populateMatrix(256);
    private final ArrayList<ArrayList<Double>> matrix512 = populateMatrix(512);
    private final ArrayList<ArrayList<Double>> matrix1024 = populateMatrix(1024);
    private final ArrayList<ArrayList<Double>> matrix2048 = populateMatrix(2048);

    /**
     * Creates and returns a matrix of size N x (N + 1), populated with random values.
     * @param N the size of the matrix
     * @return the matrix created
     */
    private ArrayList<ArrayList<Double>> populateMatrix(int N) {
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        for (int r = 0; r < N; r++) {
            matrix.add(new ArrayList<>());
            for (int c = 0; c < N + 1; c++) {
                Double value = Math.round(Math.random() * 10000) / 100.0;
                matrix.get(r).add(value);
            }
        }
        return matrix;
    }

    public void test64(int testAmount) throws InterruptedException {
        matrixTest(testAmount, matrix64);
    }

    public void test256(int testAmount) throws InterruptedException {
        matrixTest(testAmount, matrix256);
    }

    public void test512(int testAmount) throws InterruptedException {
        matrixTest(testAmount, matrix512);
    }

    public void test1024(int testAmount) throws InterruptedException {
        matrixTest(testAmount, matrix1024);
    }

    public void test2048(int testAmount) throws InterruptedException {
        matrixTest(testAmount, matrix2048);
    }

    private void matrixTest(int testAmount, ArrayList<ArrayList<Double>> matrix256) throws InterruptedException {
        ArrayList<Double> parallelTimes = new ArrayList<>();
        ArrayList<Double> serialTimes = new ArrayList<>();
        for (int i = 0; i < testAmount; i++) {
            ArrayList<ArrayList<Double>> temp = copyMatrix(matrix256);
            double startTime = System.nanoTime();
            ParallelAlgorithm p = new ParallelAlgorithm(temp);
            p.GE();
            p.backSubstitution();
            double endTime = System.nanoTime();
            parallelTimes.add(endTime - startTime);
        }
        for (int i = 0; i < testAmount; i++) {
            ArrayList<ArrayList<Double>> temp = copyMatrix(matrix256);
            double startTime = System.nanoTime();
            SerialAlgorithm s = new SerialAlgorithm(temp);
            s.GE();
            s.backSubstitution();
            double endTime = System.nanoTime();
            serialTimes.add(endTime - startTime);
        }
        System.out.println("Parallel");
        for (double p : parallelTimes) {
            System.out.println(p);
        }
        System.out.println("Serial");
        for (double s : serialTimes) {
            System.out.println(s);
        }
    }

    private ArrayList<ArrayList<Double>> copyMatrix(ArrayList<ArrayList<Double>> matrix) {
        ArrayList<ArrayList<Double>> copy = new ArrayList<>();
        for (int r = 0; r < matrix.size(); r++) {
            copy.add(new ArrayList<>());
            for (int c = 0; c < matrix.get(0).size(); c++) {
                copy.get(r).add(matrix.get(r).get(c));
            }
        }
        return copy;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Runtime.getRuntime().availableProcessors());
        Test t = new Test();
        t.test64(1);
        t.test256(1);
        t.test512(1);
        t.test1024(1);
        t.test2048(1);
    }
}