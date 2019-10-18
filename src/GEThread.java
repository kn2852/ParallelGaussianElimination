import java.util.ArrayList;

public class GEThread extends Thread{
    private ArrayList<ArrayList<Double>> matrix;
    private int row;
    private int col;
    private double value;

    public GEThread(ArrayList<ArrayList<Double>> matrix, int row, int col, double value) {
        this.matrix = matrix;
        this.row = row;
        this.col = col;
        this.value = value;
    }

    @Override
    public void run() {
        double cellCurrent = matrix.get(row).get(col);
        matrix.get(row).set(col, cellCurrent + value);
    }
}