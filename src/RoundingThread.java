import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class RoundingThread extends Thread {
    private ArrayList<ArrayList<Double>> matrix;
    private int row;
    private int col;

    public RoundingThread(ArrayList<ArrayList<Double>> matrix, int row, int col) {
        this.matrix = matrix;
        this.row = row;
        this.col = col;
    }

    @Override
    public void run() {
        double currentValue = matrix.get(row).get(col);
        matrix.get(row).set(col, round(currentValue, 6));
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}