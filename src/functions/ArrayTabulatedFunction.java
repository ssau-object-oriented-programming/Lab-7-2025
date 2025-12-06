package functions;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction implements TabulatedFunction {
    private FunctionPoint[] points; // Массив точек
    private static final long serialVersionUID = 1L; 

    // --- Конструкторы ---

    // Конструктор по границам и количеству точек
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX || pointsCount < 2) {
            throw new IllegalArgumentException("Invalid arguments: leftX >= rightX or pointsCount < 2");
        }
        this.points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i) {
            points[i] = new FunctionPoint(leftX + i * step, 0);
        }
    }
    
    // Конструктор по границам и значениям Y
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        int count = values.length;
        if (leftX >= rightX || count < 2) {
            throw new IllegalArgumentException("Invalid arguments: leftX >= rightX or pointsCount < 2");
        }
        this.points = new FunctionPoint[count];
        double step = (rightX - leftX) / (count - 1);
        for (int i = 0; i < count; ++i) {
            points[i] = new FunctionPoint(leftX + i * step, values[i]);
        }
    }
    
    // Конструктор по готовым точкам
    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("Function must have at least 2 points");
        }
        for (int i = 0; i < points.length - 1; ++i) {
            if (points[i].getX() >= points[i + 1].getX()) {
                throw new IllegalArgumentException("Points are not sorted by X");
            }
        }
        this.points = new FunctionPoint[points.length];
        for (int i = 0; i < points.length; ++i) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    // --- Основные методы ---

    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    public double getRightDomainBorder() {
        return points[points.length - 1].getX();
    }

    // Вычисление значения (интерполяция)
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        for (int i = 0; i < points.length - 1; ++i) {
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();
            double y1 = points[i].getY();
            double y2 = points[i + 1].getY();

            if (x >= x1 && x <= x2) {
                double epsilon = 1e-9;
                if (Math.abs(x - x1) < epsilon) return y1;
                if (Math.abs(x - x2) < epsilon) return y2;
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }
        return Double.NaN;
    }

    public int getPointsCount() { return points.length; }

    private void checkIndex(int index) {
        if (index < 0 || index >= points.length) {
            throw new FunctionPointIndexOutOfBoundsException("Index " + index + " is out of bounds");
        }
    }

    public FunctionPoint getPoint(int index) {
        checkIndex(index);
        return new FunctionPoint(points[index]); 
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        checkIndex(index);
        double newX = point.getX();
        double leftBound = (index > 0) ? points[index - 1].getX() : Double.NEGATIVE_INFINITY;
        double rightBound = (index < points.length - 1) ? points[index + 1].getX() : Double.POSITIVE_INFINITY;

        if (newX <= leftBound || newX >= rightBound) {
            throw new InappropriateFunctionPointException("New point's X coordinate is out of the allowed interval");
        }
        points[index] = new FunctionPoint(point);
    }

    public double getPointX(int index) {
        checkIndex(index);
        return points[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        checkIndex(index);
        double leftBound = (index > 0) ? points[index - 1].getX() : Double.NEGATIVE_INFINITY;
        double rightBound = (index < points.length - 1) ? points[index + 1].getX() : Double.POSITIVE_INFINITY;
        
        if (x <= leftBound || x >= rightBound) {
            throw new InappropriateFunctionPointException("New X coordinate is out of the allowed interval");
        }
        points[index].setX(x);
    }

    public double getPointY(int index) {
        checkIndex(index);
        return points[index].getY();
    }
    
    public void setPointY(int index, double y) {
        checkIndex(index);
        points[index].setY(y);
    }

    public void deletePoint(int index) {
        checkIndex(index);
        if (points.length < 3) {
            throw new IllegalStateException("Cannot delete point: function must have at least 2 points remaining.");
        }
        FunctionPoint[] newPoints = new FunctionPoint[points.length - 1];
        System.arraycopy(points, 0, newPoints, 0, index);
        System.arraycopy(points, index + 1, newPoints, index, points.length - index - 1);
        points = newPoints;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int insertIndex = 0;
        while (insertIndex < points.length && points[insertIndex].getX() < point.getX()) {
            insertIndex++;
        }
        if (insertIndex < points.length && points[insertIndex].getX() == point.getX()) {
            throw new InappropriateFunctionPointException("Point with X=" + point.getX() + " already exists.");
        }
        FunctionPoint[] newPoints = new FunctionPoint[points.length + 1];
        System.arraycopy(points, 0, newPoints, 0, insertIndex);
        newPoints[insertIndex] = new FunctionPoint(point);
        System.arraycopy(points, insertIndex, newPoints, insertIndex + 1, points.length - insertIndex);
        points = newPoints;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < points.length; i++) {
            sb.append(points[i].toString());
            if (i < points.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabulatedFunction)) return false; 
        
        TabulatedFunction that = (TabulatedFunction) o;
        if (this.getPointsCount() != that.getPointsCount()) return false;

        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction thatArray = (ArrayTabulatedFunction) o;
            for (int i = 0; i < this.points.length; i++) {
                if (!this.points[i].equals(thatArray.points[i])) return false;
            }
        } else {
            for (int i = 0; i < this.getPointsCount(); i++) {
                if (!this.getPoint(i).equals(that.getPoint(i))) return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int result = Arrays.hashCode(points);
        result = 31 * result + Integer.hashCode(points.length);
        return result;
    }

    public Object clone() throws CloneNotSupportedException {
        FunctionPoint[] clonedPoints = new FunctionPoint[this.points.length];
        for (int i = 0; i < this.points.length; i++) {
            clonedPoints[i] = (FunctionPoint) this.points[i].clone();
        }
        return new ArrayTabulatedFunction(clonedPoints);
    }

    // ЗАДАНИЕ 1 - Итератор
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int index = 0;

            public boolean hasNext() {
                return index < points.length;
            }

            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return new FunctionPoint(points[index++]);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // ЗАДАНИЕ 2 - Фабрика
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
