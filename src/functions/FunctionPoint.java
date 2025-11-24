package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {
    private static final long serialVersionUID = 1L;

    private double x, y;

    public FunctionPoint(double x, double y){
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point){
        this.x = point.x;
        this.y = point.y;
    }

    public FunctionPoint(){
        this.x = 0d;
        this.y = 0d;
    }


    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionPoint)) return false;
        FunctionPoint p = (FunctionPoint) o;
        return MathUtil.equals(this.x, p.x) && MathUtil.equals(this.y, p.y);
    }

    @Override
    public int hashCode() {
        long lx = Double.doubleToLongBits(x);
        long ly = Double.doubleToLongBits(y);
        int hx = (int) (lx ^ (lx >>> 32));
        int hy = (int) (ly ^ (ly >>> 32));
        return hx ^ hy;
    }

    @Override
    public Object clone() {
        return new FunctionPoint(this.x, this.y);
    }
}

