public class TargetCell {
    private int row;
    private int col;
    private double val;

    public TargetCell(int row, int col, double val) {
        this.row = row;
        this.col = col;
        this.val = val;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public double getVal() {
        return val;
    }
}