import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Vector;

public class GEThread extends Thread{
    private Vector<Vector<Double>> matrix;
    private Vector<TargetCell> cells;

    public GEThread(Vector<Vector<Double>> matrix, Vector<TargetCell> cells) {
        this.matrix = matrix;
        this.cells = cells;
    }

    @Override
    public void run() {
        for (TargetCell cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            double val = cell.getVal();
            double cellCurrent = matrix.get(row).get(col);
            matrix.get(row).set(col, round(cellCurrent + val, 8));
            //System.out.println("r " + row + " c " + col + " val " + val);
        }
        //matrix.forEach((n) -> System.out.println(n));
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}