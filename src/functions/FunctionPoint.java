package functions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class FunctionPoint implements Externalizable, Cloneable{
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

    @Override
    public String toString() {
        return String.format("(%.2f; %.2f)", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionPoint that = (FunctionPoint) o;

        final double EPSILON = 1e-10;
        return Math.abs(this.x - that.x) < EPSILON && Math.abs(this.y - that.y) < EPSILON;
    }

    @Override
    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);

        int x1 = (int)(xBits & 0xFFFFFFFFL);
        int x2 = (int)((xBits >> 32) & 0xFFFFFFFFL);
        int y1 = (int)(yBits & 0xFFFFFFFFL);
        int y2 = (int)((yBits >> 32) & 0xFFFFFFFFL);
        // Комбинирование через XOR
        return x1 ^ x2 ^ y1 ^ y2;
    }

    @Override
    public FunctionPoint clone() {
        try {
            return (FunctionPoint) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Клонирование не поддерживается", e);
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(x);
        out.writeDouble(y);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        x = in.readDouble();
        y = in.readDouble();
    }
}
