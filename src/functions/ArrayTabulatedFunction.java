package functions;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import static functions.DoubleComparison.*;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
    private FunctionPoint[] points;
    private int  pointsCount;

    //Конструкторы
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {

        if (doubleGreaterOrEq(leftX, rightX)) {
            throw new IllegalArgumentException("Левая граница не может быть больше или равна правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек не может быть меньше 2");
        }

        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount + 10];

        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0);
        }
    }
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {

        if (doubleGreaterOrEq(leftX, rightX)) {
            throw new IllegalArgumentException("Левая граница не может быть больше или равна правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек не может быть меньше 2");
        }

        this.pointsCount = values.length;
        this.points = new FunctionPoint[values.length + 10];

        double step = (rightX - leftX) / (values.length - 1);

        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек не может быть меньше 2");
        }
        for (int i = 0; i < points.length - 1; i++) {
            if (doubleGreaterOrEq(points[i].getX(), points[i + 1].getX())) {
                throw new IllegalArgumentException("Точки в массиве не упорядочены по X");
            }
        }
        this.pointsCount = points.length;
        this.points = new FunctionPoint[pointsCount + 10];

        for (int i = 0; i < pointsCount; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    public double getLeftDomainBorder() {
        return points[0].getX();
    }
    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }

    //Возвращает значение функции в точке х, если точка лежит в обл. определения
    public double getFunctionValue(double x) {
        if (doubleLess(x,getLeftDomainBorder()) || doubleGreater(x,getRightDomainBorder())) {
            return Double.NaN;
        }

        for (int i = 0; i < pointsCount - 1; i++) {
            if (doubleGreaterOrEq(x,points[i].getX()) && doubleLessOrEq(x,points[i + 1].getX())) {
                if (doubleEq(x,points[i].getX())) {
                    return points[i].getY();
                }
                else if (doubleEq(x,points[i + 1].getX())) {
                    return points[i + 1].getY();
                }
                else {
                    return interpolation(x, points[i], points[i + 1]);
                }
            }
        }
        return Double.NaN;
    }

    // Линейная интерполяция
    private double interpolation(double x, FunctionPoint p1, FunctionPoint p2) {
        return p1.getY() + (x - p1.getX()) * (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
    }

    //Метод возвращает количество точек
    public int getPointsCount() {
        return pointsCount;
    }

    //Метод возвращает копию точки, соответствующей данному индексу
    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы. ");
        }
        return new FunctionPoint(points[index]);
    }

    //Метод заменяет указанную точку на переданную
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {

        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы. ");
        }

        double newX = point.getX();

        //если newX меньше левой границы
        if (index != 0 && doubleLessOrEq(newX,getPointX(index - 1))) {
            throw new InappropriateFunctionPointException("X новой точки должен быть больше X предыдущей точки");
        }

        //если newX больше правой границы
        if (index != pointsCount - 1 && doubleGreaterOrEq(newX,getPointX(index + 1))) {
            throw new InappropriateFunctionPointException("X новой точки должен быть меньше X следующей точки");
        }
        //если Х лежит в нужном интервале, создаем новую точку
        points[index] = new FunctionPoint(point);
    }
    //Метод возвращает значение абсциссы точки с указанным номером
    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы. ");
        }
        return points[index].getX();
    }
    //Метод возвращает значение ординаты точки с указанным номером
    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы.");
        }
        return points[index].getY();
    }
    //Метод изменяет значение абсциссы точки с указанным номером
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы. ");
        }
        //если x меньше левой границы
        if (index != 0 && doubleLessOrEq(x,getPointX(index - 1))) {
            throw new InappropriateFunctionPointException("X новой точки должен быть больше X предыдущей точки");
        }
        //если x больше правой границы
        if (index != pointsCount - 1 && doubleGreaterOrEq(x,getPointX(index + 1))) {
            throw new InappropriateFunctionPointException("X новой точки должен быть меньше X следующей точки");
        }
        //создаем новую точку с новым Х и старым Y
        points[index] = new FunctionPoint(x, getPointY(index));
    }

    //Метод изменяет значение ординаты точки с указанным номером
    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы. ");
        }
        points[index] = new FunctionPoint(getPointX(index), y);
    }

    //Метод удаления точки функции
    public void deletePoint(int index) {
        if (pointsCount < 3) {
            throw new IllegalStateException("Невозможно удалить точку, т.к. в наборе должно быть не менее двух точек");
        }
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы. ");
        }
        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        pointsCount--;
        points[pointsCount] = null;
    }
    //Метод добавления новой точки
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {

        for (int i = 0; i < pointsCount; i++) {
            if (doubleEq(point.getX(),getPointX(i))) {
                throw new InappropriateFunctionPointException("Точка с таким X уже существует");
            }
        }
        int searchIndex = 0;
        while (searchIndex < pointsCount && doubleGreater(point.getX(),getPointX(searchIndex))) {
            searchIndex++;
        }
        if (pointsCount == points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }
        System.arraycopy(points, searchIndex, points, searchIndex + 1, pointsCount - searchIndex);
        points[searchIndex] = new FunctionPoint(point);
        pointsCount++;
    }

    public String toString() {
        StringBuilder str = new StringBuilder("{");

        for (int i = 0; i < pointsCount; i++) {
            str.append(points[i].toString());

            if (i < pointsCount - 1) {
                str.append(", ");
            }
        }
        str.append("}");
        return str.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TabulatedFunction)) {
            return false;
        }

        TabulatedFunction other = (TabulatedFunction) o;

        if (this.getPointsCount() != other.getPointsCount()) {
            return false;
        }

        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction otherFunc = (ArrayTabulatedFunction) o;

            for (int i = 0; i < pointsCount; i++) {
                if (!this.points[i].equals(otherFunc.points[i])) {
                    return false;
                }
            }
        }
        else {
            for (int i = 0; i < pointsCount; i++) {
                if (!this.getPoint(i).equals(other.getPoint(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public int hashCode() {
        int hash = pointsCount;

        for (int i = 0; i < pointsCount; i++) {
            hash = hash ^ points[i].hashCode();
        }
        return hash;
    }

    public Object clone() {
        FunctionPoint[] clonedPoints = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            clonedPoints[i] = (FunctionPoint) points[i].clone();
        }
        return new ArrayTabulatedFunction(clonedPoints);
    }


    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int index = 0;

            public boolean hasNext() {
                return index < pointsCount;
            }

            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Нет следующего элемента");
                }

                return new FunctionPoint(points[index++]);
            }

            public void remove() {
                throw new UnsupportedOperationException("Удаление не доступно");
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
