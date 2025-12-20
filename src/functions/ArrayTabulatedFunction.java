package functions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
    private static final long serialVersionUID = 1L;
    private FunctionPoint[] points;
    private int size;
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения >= правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек меньше двух");
        }
        this.points = new FunctionPoint[pointsCount];
        this.size = pointsCount;
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0.0);
        }
    }
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения больше или равна правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек меньше двух");
        }
        int n = values.length;
        this.points = new FunctionPoint[n];
        this.size = n;
        double step = (rightX - leftX) / (n - 1);
        for (int i = 0; i < n; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }
    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Массив точек не может быть null");
        }
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2: " + points.length);
        }
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Точка с индексом " + i + " равна null");
            }
            if (points[i + 1] == null) {
                throw new IllegalArgumentException("Точка с индексом " + (i + 1) + " равна null");
            }
            if (points[i].getX() >= points[i + 1].getX()) {
                throw new IllegalArgumentException("Точки не упорядочены по X: " +
                        points[i].getX() + " >= " + points[i + 1].getX());
            }
        }
        this.points = new FunctionPoint[points.length];
        this.size = points.length;
        for (int i = 0; i < points.length; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }
    private ArrayTabulatedFunction() {
    }
    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Нет следующего элемента");
                }
                // Возвращаем копию точки, чтобы не нарушать инкапсуляцию
                return new FunctionPoint(points[currentIndex++]);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Удаление не поддерживается");
            }
        };
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (int i = 0; i < size; i++) {
            sb.append(points[i].toString());
            if (i < size - 1) {
                sb.append(", ");
            }
        }

        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction other = (ArrayTabulatedFunction) o;

            if (this.size != other.size) return false;

            for (int i = 0; i < size; i++) {
                if (!this.points[i].equals(other.points[i])) {
                    return false;
                }
            }
            return true;

        } else if (o instanceof TabulatedFunction) {
            TabulatedFunction other = (TabulatedFunction) o;

            if (this.getPointsCount() != other.getPointsCount()) return false;

            final double EPSILON = 1e-10;

            for (int i = 0; i < size; i++) {

                FunctionPoint otherPoint = other.getPoint(i);
                FunctionPoint that = this.points[i];

                if (!otherPoint.equals(that)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = size;

        for (int i = 0; i < size; i++) {
            hash ^= points[i].hashCode();
        }

        return hash;
    }

    @Override
    public ArrayTabulatedFunction clone() {
        try {
            ArrayTabulatedFunction clone = (ArrayTabulatedFunction) super.clone();
            clone.points = new FunctionPoint[this.points.length];
            for (int i = 0; i < this.size; i++) {
                if (this.points[i] != null) {
                    clone.points[i] = this.points[i].clone();
                }
            }
            for (int i = this.size; i < this.points.length; i++) {
                clone.points[i] = null;
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Клонирование не поддерживается", e);
        }
    }
    public double getLeftDomainBorder() {
        return points[0].getX();
    }
    public double getRightDomainBorder() {
        return points[size - 1].getX();
    }
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() - 1e-10 || x > getRightDomainBorder() + 1e-10) {
            return Double.NaN;
        }
        for (int i = 0; i < size - 1; i++) {
            double x0 = points[i].getX();
            double x1 = points[i + 1].getX();
            double y0 = points[i].getY();
            double y1 = points[i + 1].getY();
            if (Math.abs(x - x0) < 1e-10) return y0;
            if (Math.abs(x - x1) < 1e-10) return y1;
            if (x > x0 - 1e-10 && x < x1 + 1e-10) {
                return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
            }
        }
        return Double.NaN;
    }
    public int getPointsCount() {
        return size;
    }
    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс точки выходит за границы: " + index);
        }
        return new FunctionPoint(points[index]);
    }
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс точки выходит за границы: " + index);
        }
        if ((index > 0 && point.getX() <= points[index - 1].getX()) || (index < size - 1 && point.getX() >= points[index + 1].getX())) {
            throw new InappropriateFunctionPointException("Координата X точки нарушает упорядоченность точек функции");
        }
        points[index] = new FunctionPoint(point);
    }
    public double getPointX(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс точки выходит за границы: " + index);
        }
        return points[index].getX();
    }
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс точки выходит за границы: " + index);
        }
        if ((index > 0 && x <= points[index - 1].getX()) ||
                (index < size - 1 && x >= points[index + 1].getX())) {
            throw new InappropriateFunctionPointException("Координата X точки нарушает упорядоченность точек функции");
        }
        points[index].setX(x);
    }
    public double getPointY(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс точки выходит за границы: " + index);
        }
        return points[index].getY();
    }
    public void setPointY(int index, double y) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс точки выходит за границы: " + index);
        }
        points[index].setY(y);
    }
    public void deletePoint(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс точки выходит за границы: " + index);
        }
        if (size <= 2) {
            throw new IllegalStateException("Невозможно удалить точку: должно остаться минимум 2 точки");
        }

        for (int i = index; i < size - 1; i++) {
            points[i] = points[i + 1];
        }
        points[size - 1] = null;
        size--;
    }
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        for (int i = 0; i < size; i++) {
            if (Math.abs(points[i].getX() - point.getX()) < 1e-10) {
                throw new InappropriateFunctionPointException("Точка с координатой X=" + point.getX() + " уже существует");
            }
        }

        if (size == points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[size * 2];
            System.arraycopy(points, 0, newPoints, 0, size);
            points = newPoints;
        }

        int insertIndex = 0;
        double x = point.getX();
        while (insertIndex < size && points[insertIndex].getX() < x) {
            insertIndex++;
        }

        for (int i = size; i > insertIndex; i--) {
            points[i] = points[i - 1];
        }
        points[insertIndex] = new FunctionPoint(point);
        size++;
    }
    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }
        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);
        }
    }
}