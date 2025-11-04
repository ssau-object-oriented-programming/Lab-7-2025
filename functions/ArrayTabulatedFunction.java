package functions;
import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable{
    private static final double E = 1e-10;
    private FunctionPoint[] points;
    private int PointsCount;

    public ArrayTabulatedFunction() {}

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX - E)
            throw new IllegalArgumentException("Левая граница больше или равна правой границе");
        if (pointsCount < 2)
            throw new IllegalArgumentException("Точек меньше двух");
        points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i, leftX += step)
            points[i] = new FunctionPoint(leftX, 0);
        PointsCount = pointsCount;
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX - E)
            throw new IllegalArgumentException("Левая граница больше или равна правой границе");
        if (values.length < 2)
            throw new IllegalArgumentException("Точек меньше двух");
        int pointsCount =  values.length;
        points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i, leftX += step)
            points[i] = new FunctionPoint(leftX, values[i]);
        PointsCount = pointsCount;
    }

    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if (points == null)
            throw new IllegalArgumentException("Точек меньше двух");
        if (points.length < 2)
            throw new IllegalArgumentException("Точек меньше двух");
        for (int i = 1; i < points.length; ++i) {
            if (points[i - 1].getX() >= points[i].getX())
                throw new IllegalArgumentException("Точки не упорядоченные");
        }
        this.points = new FunctionPoint[points.length];
        for (int i = 0; i < points.length; ++i)
            this.points[i] = new FunctionPoint(points[i]);
        PointsCount = points.length;
    }

    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    public double getRightDomainBorder() {
        return points[PointsCount - 1].getX();
    }

    public double getFunctionValue(double x) {
        if (x >= getLeftDomainBorder() - E && x <= getRightDomainBorder() + E) {
            for (int i = 0; i < PointsCount; ++i) {
                if (Math.abs(x - points[i].getX()) < E)
                    return points[i].getY();
                if (i < PointsCount - 1 && x > points[i].getX() && x < points[i + 1].getX()) {
                    double x1 = points[i].getX();
                    double y1 = points[i].getY();
                    double x2 = points[i + 1].getX();
                    double y2 = points[i + 1].getY();
                    if (Double.isInfinite(y1) || Double.isInfinite(y2) || Double.isNaN(y1) || Double.isNaN(y2))
                        return Double.NaN;
                    return ((x - x1) * (y2 - y1) / (x2 - x1)) + y1;
                }
            }
        }
        return Double.NaN;
    }

    public int getPointsCount() {
        return PointsCount;
    }

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= PointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= PointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        if ((index == 0 || index == PointsCount - 1)) {
            if (Math.abs(points[index].getX() - point.getX()) > E)
                throw new InappropriateFunctionPointException("x выходит за границы определения функции");
            points[index] = new FunctionPoint(point);
        }
        else
            if (point.getX() > points[index - 1].getX() + E && point.getX() < points[index + 1].getX() - E)
                points[index] = new FunctionPoint(point);
            else
                throw new InappropriateFunctionPointException("x выходит за границы определения соседних точек");
    }

    public double getPointX(int index) {
        if (index < 0 || index >= PointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        return points[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= PointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        if (index == 0 || index == PointsCount - 1 || x <= points[index - 1].getX() || x >= points[index + 1].getX()) 
            throw new InappropriateFunctionPointException("x выходит за границы определения соседних точек");
        points[index].setX(x);
    }

    public double getPointY(int index) {
        if (index < 0 || index >= PointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        return points[index].getY();
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= PointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        points[index].setY(y);
    }

    public void deletePoint(int index) {
        if (index < 0 || index >= PointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        if (PointsCount < 3)
            throw new IllegalStateException("Кол-во элементов меньше трех");
        System.arraycopy(points, index + 1, points, index, PointsCount - index - 1);
        --PointsCount;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int index = 0;
        if (point.getX() > points[PointsCount - 1].getX() + E)
            index = PointsCount;
        else {
            if (Math.abs(point.getX() - points[index].getX()) < E)
                    throw new InappropriateFunctionPointException("Точка c такой координатой  x определена");
            while (point.getX() >= points[index].getX() - E) {
                if (Math.abs(point.getX() - points[index].getX()) < E)
                    throw new InappropriateFunctionPointException("Точка c такой координатой  x определена");
                ++index;
            }
        }
        if (PointsCount + 1 > points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[PointsCount * 2 + 1];
            System.arraycopy(points, 0, newPoints, 0, index);
            newPoints[index] = new FunctionPoint(point);
            System.arraycopy(points, index, newPoints, index + 1, PointsCount - index);
            points = newPoints;
        }
        else {
            System.arraycopy(points, index, points, index + 1, PointsCount - index);
            points[index] = new FunctionPoint(point);
        }
        ++PointsCount;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(PointsCount);
        for (int i = 0; i < PointsCount; ++i) {
            out.writeObject(points[i]);
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        PointsCount = in.readInt();
        points = new FunctionPoint[PointsCount];
        for (int i = 0; i < PointsCount; ++i) {
            points[i] = (FunctionPoint) in.readObject();
        }
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < PointsCount - 1; ++i)
            s += points[i].toString() + ", ";
        s += points[PointsCount - 1].toString();
        return "{" + s + "}";
    }

    public boolean equals(Object o) {
        if (o instanceof ArrayTabulatedFunction && PointsCount == ((ArrayTabulatedFunction) o).PointsCount) {
            for (int i = 0; i < PointsCount; ++i)
                if (!points[i].equals(((ArrayTabulatedFunction) o).points[i]))
                    return false;
            return true;
        }
        else if (o instanceof TabulatedFunction && PointsCount == ((TabulatedFunction) o).getPointsCount()) {
            for (int i = 0; i < PointsCount; ++i)
                if (!points[i].equals(((TabulatedFunction) o).getPoint(i)))
                    return false;
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hash = PointsCount;
        for (int i = 0; i < PointsCount; ++i)
            hash ^= points[i].hashCode();
        return hash;
    }

    public Object clone() {
        FunctionPoint[] points = new FunctionPoint[PointsCount];
        for (int i = 0; i < PointsCount; ++i)
            points[i] = (FunctionPoint) this.points[i].clone();
        return new ArrayTabulatedFunction(points);
    }

    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int index = 0;

            public boolean hasNext() {
                return index < PointsCount;
            }

            public FunctionPoint next() {
                if (hasNext())
                    return new FunctionPoint(points[index++]);
                throw new NoSuchElementException("Нет больше точек");
            }

            public void remove() {
                throw new UnsupportedOperationException("Нельзя удалить элемент");
            }
        };
    }

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }

        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);
        }
    }
}