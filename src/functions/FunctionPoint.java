package functions;
import java.io.Serializable;

public class FunctionPoint implements Serializable {
    private double x;
    private double y;

    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }
    public FunctionPoint() {
        this(0.0, 0.0);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String toString() {
        return "(" + x + "; " + y + ")";
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        if (!(o instanceof FunctionPoint)) return false;

        FunctionPoint p = (FunctionPoint) o;

        return Math.abs(this.x - p.x) < 1e-9 && Math.abs(this.y - p.y) < 1e-9;
    }

    public int hashCode() {
        long xb = Double.doubleToLongBits(x);
        long yb = Double.doubleToLongBits(y);

        int x1 = (int)(xb >>> 32);
        int x2 = (int)(xb & 0xFFFFFFFF);
        int y1 = (int)(yb >>> 32);
        int y2 = (int)(yb & 0xFFFFFFFF);

        return x1 ^ x2 ^ y1 ^ y2;
    }

    public Object clone() {
        return new FunctionPoint(this.x, this.y);
    }

}
