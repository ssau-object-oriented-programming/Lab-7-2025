package functions;
import java.io.*;

public class FunctionPoint implements Externalizable {
    private static final double E = 1e-10;
    private double x;
    private double y;

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point) {
        x = point.x;
        y = point.y;
    }

    public FunctionPoint() {
        x = 0;
        y = 0;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(x);
        out.writeDouble(y);
    }

    public void readExternal(ObjectInput in) throws IOException {
        x = in.readDouble();
        y = in.readDouble();
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Object o) {
        return o instanceof FunctionPoint && Math.abs(((FunctionPoint) o).getX() - x) < E && Math.abs(((FunctionPoint) o).getY() - y) < E;
    }

    public int hashCode() {
        long longX = Double.doubleToLongBits(x);
        long longY = Double.doubleToLongBits(y);
        return (int) (longX >> 32) ^ (int) (longX & 0xFFFFFFFFL) ^ (int) (longY >> 32) ^ (int) (longY & 0xFFFFFFFFL);
    }

    public Object clone() {
        return new FunctionPoint(this);
    }
}