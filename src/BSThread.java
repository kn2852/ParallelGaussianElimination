import java.util.ArrayList;

public class BSThread extends Thread{
    private ArrayList<ArrayList<Double>> matrix;
    private int row;
    private int col;
    private double value;

    public BSThread(ArrayList<ArrayList<Double>> matrix, int row, int col, double value) {
        this.matrix = matrix;
        this.row = row;
        this.col = col;
        this.value = value;
    }

    @Override
    public void run() {
        double currentValue = matrix.get(row).get(col);
        matrix.get(row).set(matrix.size(), currentValue + value);
        matrix.get(row).set(col, 0.0);
    }
}