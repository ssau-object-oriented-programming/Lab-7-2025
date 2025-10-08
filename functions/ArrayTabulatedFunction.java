package functions;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Класс табулированной функции... неясно, для чего
 */
public class ArrayTabulatedFunction implements TabulatedFunction {
    private FunctionPoint[] points; // массив точек
    private int amountOfPoints; // реальное кол-во точек в данном массиве
    /**
     * Конструктор табулированной функции по самим точкам
     * @param points точки данной функции
     */
    public ArrayTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
        if (points.length < 2) throw new IllegalArgumentException("Points count must be greater than 2");
        for (int i = points.length - 1; i > 0; i--) {
            if (points[i].getX() <= points[i-1].getX()) throw new IllegalArgumentException("Illegal points(x coordinate)");
        }
        this.points = new FunctionPoint[points.length];
        this.amountOfPoints = points.length;
        for (int i = 0; i < points.length; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }
    /**
     * Конструктор табулированной функции по кол-ву точек
     * @param leftX левая граница <s>страданий</s> по значению X
     * @param rightX правая граница <s>страданий</s> по значению X
     * @param pointsCount кол-во точек данной табулированной функции
     */
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (pointsCount < 2) throw new IllegalArgumentException("Points count must be greater than 2");
        if (leftX >= rightX) throw new IllegalArgumentException("LeftX border must be lower than RightX border");
        this.points = new FunctionPoint[pointsCount];
        this.amountOfPoints = pointsCount;
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = pointsCount - 1; i >= 0; i--) {
            this.points[i] = new FunctionPoint(leftX + i * step, 0);
        }
    }
    /**
     * Конструктор табулированной функции по заданным значениям Y(массив)
     * @param leftX левая граница <s>страданий</s> по значению X
     * @param rightX правая граница <s>страданий</s> по значению X
     * @param values точки данной табулированной функции
     */
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {
        this(leftX, rightX, values.length);
        for (int i = this.amountOfPoints - 1; i >= 0; i--) {
            this.points[i].setY(values[i]);
        }
    }
    @Override
    public double getLeftDomainBorder() {
        return (this.amountOfPoints > 0) ? (this.points[0].getX()) : (Double.NaN);
    }
    @Override
    public double getRightDomainBorder() {
        return (this.amountOfPoints > 0) ? (this.points[this.amountOfPoints - 1].getX()) : (Double.NaN);
    }
    @Override
    public double getFunctionValue(double x) {
        if (x < this.getLeftDomainBorder() || this.getRightDomainBorder() < x) return Double.NaN;
        int index;
        for (index = this.amountOfPoints - 2; index >= 0; index--) { // нет смысла проверять правую точку, скипаем автоматом
            if (this.points[index].getX() < x) break;
            if (this.points[index].getX() == x) return this.points[index].getY(); // нет смысла интерполировать
        } // здесь используется функция (y1-y0)/(x1-x0)*(x-x0) + y0
        return (this.points[index+1].getY() - this.points[index].getY()) /
                (this.points[index+1].getX() - this.points[index].getX()) *
                (x - this.points[index].getX()) + this.points[index].getY();
    }
    @Override
    public int getPointsCount() { return this.amountOfPoints; }
    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.amountOfPoints) throw new FunctionPointIndexOutOfBoundsException();
        return new FunctionPoint(this.points[index]);
    }
    @Override
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= this.amountOfPoints) throw new FunctionPointIndexOutOfBoundsException();
        double leftX = (index == 0) ? (Double.NEGATIVE_INFINITY) : (this.points[index-1].getX());
        double rightX = (index == this.amountOfPoints - 1) ? (Double.POSITIVE_INFINITY) : (this.points[index+1].getX());
        if (leftX >= point.getX() || point.getX() >= rightX) throw new InappropriateFunctionPointException();
        this.points[index] = new FunctionPoint(point);
    }
    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.amountOfPoints) throw new FunctionPointIndexOutOfBoundsException();
        return this.points[index].getX();
    }

    @Override
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= this.amountOfPoints) throw new FunctionPointIndexOutOfBoundsException();
        this.setPoint(index, new FunctionPoint(x, this.points[index].getY()));
    }
    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.amountOfPoints) throw new FunctionPointIndexOutOfBoundsException();
        return this.points[index].getY();
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.amountOfPoints) throw new FunctionPointIndexOutOfBoundsException();
        this.points[index].setY(y);
    }

    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (index < 0 || index >= this.amountOfPoints) throw new FunctionPointIndexOutOfBoundsException();
        if (this.amountOfPoints < 3) throw new IllegalStateException("Points count must be greater than 2 for deletion");
        for (; index < this.amountOfPoints - 2; index++) {
            this.points[index] = this.points[index + 1]; // делаем циклический сдвиг
        }
        this.amountOfPoints--;
        this.points[index + 1] = null; /* стираем ссылку на последний элемент, т.к. если удалим последний элемент,
        то из памяти он не исчезнет(ссылка останется на неиспользуемой части массива) */
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double x = point.getX();
        int index = 0;
        for (; index < this.amountOfPoints; index++) {
            if (this.points[index].getX() == x) throw new InappropriateFunctionPointException();
            if (this.points[index].getX() > x) break;
        }
        if (this.points.length == this.amountOfPoints) {
            FunctionPoint[] newArray = new FunctionPoint[this.points.length * 2];
            System.arraycopy(this.points, 0, newArray, 0, index);
            newArray[index] = new FunctionPoint(point);
            System.arraycopy(this.points, index, newArray, index + 1, this.amountOfPoints - index);
            this.points = newArray; // по идее массив удалится, т.к. нету на него ссылок
        } else {
            for (int i = this.amountOfPoints; i > index; i--) {
                this.points[i] = this.points[i - 1];
            }
            this.points[index] = new FunctionPoint(point);
        }
        this.amountOfPoints++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < this.amountOfPoints; i++) {
            sb.append(this.points[i]);
            if (i < this.amountOfPoints - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TabulatedFunction other)) return false;
        if (this.getPointsCount() != other.getPointsCount()) return false;
        for (int i = 0; i < this.amountOfPoints; i++) {
            if (!this.points[i].equals(other.getPoint(i))) return false;
        }
        return true;
    }
    @Override
    public int hashCode() {
        int base = 666;
        for (int i = 0; i < this.amountOfPoints; i++) {
            base ^= this.points[i].hashCode();
        }
        return base;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new ArrayTabulatedFunction(this.points);
    } // благодаря тому, что стоит инкапсуляция - можно не волноваться
    // за "общие" ссылки у двух разных объектов данного класса
    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            int index = 0;
            final FunctionPoint[] pointArray = points;
            final int elements = amountOfPoints;
            @Override
            public boolean hasNext() {
                return index < elements;
            }
            @Override
            public FunctionPoint next() {
                if (!hasNext()) throw new NoSuchElementException();
                return new FunctionPoint(pointArray[index++]);
            }
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {
        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
            return new ArrayTabulatedFunction(points);
        }
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }
    }
}