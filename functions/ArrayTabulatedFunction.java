package functions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable, Cloneable {

    private FunctionPoint[] points_arr;
    private int pointslength;

    // Обязательный конструктор для Externalizable
    public ArrayTabulatedFunction() {
    }

    public static boolean compareDouble(double a, double b) {
        final double epsilon = 1e-10;
        return Math.abs(a - b) < epsilon;
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        // проверка левая граница >= правой
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница " + leftX + " >= правой границы " + rightX);
        }

        // проверка количество точек < 2
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек " + pointsCount + " < 2");
        }

        points_arr = new FunctionPoint[pointsCount];
        pointslength = pointsCount;

        double intervalLength = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            points_arr[i] = new FunctionPoint(leftX + intervalLength * i, 0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] points) {
        // проверка левая граница >= правой
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница " + leftX + " >= правой границы " + rightX);
        }

        // проверка количество точек < 2
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек " + points.length + " < 2");
        }

        pointslength = points.length;
        points_arr = new FunctionPoint[points.length];
        double intervalLength = (rightX - leftX) / (points.length - 1);

        if (pointslength != 0) {
            for (int i = 0; i < points.length; i++) {
                points_arr[i] = new FunctionPoint(leftX + intervalLength * i, points[i]);
            }
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] points) {
        // Проверка количества точек
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек " + points.length + " < 2");
        }

        // Проверка упорядоченности по X
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].getX() >= points[i + 1].getX()) {
                throw new IllegalArgumentException("Точки не упорядочены по X. Индексы " + i + " и " + (i + 1));
            }
        }

        // Создание копии массива для инкапсуляции
        points_arr = new FunctionPoint[points.length];
        pointslength = points.length;

        for (int i = 0; i < points.length; i++) {
            points_arr[i] = new FunctionPoint(points[i]);
        }
    }

    public double getLeftDomainBorder() {
        return points_arr[0].getX();
    }

    public double getRightDomainBorder(){
        return points_arr[pointslength-1].getX();
    }

    public double getFunctionValue(double x){
        // Проверка границ
        if (x < points_arr[0].getX() || x > points_arr[pointslength-1].getX()) {
            return Double.NaN;
        }

        // Поиск интервала, содержащего x
        for (int i = 0; i < pointslength - 1; i++) {
            if (points_arr[i].getX() <= x && points_arr[i + 1].getX() >= x) {


                if (compareDouble(x, points_arr[i].getX())) {
                    return points_arr[i].getY();
                }
                if (compareDouble(x, points_arr[i + 1].getX())) {
                    return points_arr[i + 1].getY();
                }


                double x1 = points_arr[i].getX();
                double y1 = points_arr[i].getY();
                double x2 = points_arr[i + 1].getX();
                double y2 = points_arr[i + 1].getY();

                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }

        return Double.NaN;
    }

    public int getPointsCount(){
        return pointslength;
    }

    public FunctionPoint getPoint(int index){
        // проверка индекс за границами
        if (index < 0 || index >= pointslength) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы [0, " + (pointslength-1) + "]");
        }

        FunctionPoint point = new FunctionPoint(points_arr[index]);
        return point;
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        // проверка индекс за границами
        if (index < 0 || index >= pointslength) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы [0, " + (pointslength-1) + "]");
        }

        // проверка упорядоченность X
        double newX = point.getX();
        double prevX;
        double nextX;

        // Определяем предыдущий X
        if (index > 0) {
            prevX = points_arr[index-1].getX();
        } else {
            prevX = Double.NEGATIVE_INFINITY;
        }

        // Определяем следующий X
        if (index < pointslength - 1) {
            nextX = points_arr[index+1].getX();
        } else {
            nextX = Double.POSITIVE_INFINITY;
        }

        if (newX <= prevX || newX >= nextX) {
            throw new InappropriateFunctionPointException(
                    "X=" + newX + " должен быть строго между " + prevX + " и " + nextX);
        }

        points_arr[index] = new FunctionPoint(point);
    }

    public double getPointX(int index){
        // проверка индекс за границами
        if (index < 0 || index >= pointslength) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы [0, " + (pointslength-1) + "]");
        }

        return points_arr[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        // проверка 1: индекс за границами
        if (index < 0 || index >= pointslength) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы [0, " + (pointslength-1) + "]");
        }

        // проверка упорядоченность X
        double prevX;
        if (index > 0) {
            prevX = points_arr[index-1].getX();
        } else {
            prevX = Double.NEGATIVE_INFINITY;
        }

        double nextX;
        if (index < pointslength - 1) {
            nextX = points_arr[index+1].getX();
        } else {
            nextX = Double.POSITIVE_INFINITY;
        }

        if (x <= prevX || x >= nextX) {
            throw new InappropriateFunctionPointException(
                    "X=" + x + " должен быть строго между " + prevX + " и " + nextX);
        }

        points_arr[index] = new FunctionPoint(x, points_arr[index].getY());
    }

    public double getPointY(int index){
        // проверка индекс за границами
        if (index < 0 || index >= pointslength) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы [0, " + (pointslength-1) + "]");
        }

        return points_arr[index].getY();
    }

    public void setPointY(int index, double y){
        // проверка индекс за границами
        if (index < 0 || index >= pointslength) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы [0, " + (pointslength-1) + "]");
        }

        points_arr[index] = new FunctionPoint(points_arr[index].getX(), y);
    }

    public void deletePoint(int index){
        // проверка индекс за границами
        if (index < 0 || index >= pointslength) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы [0, " + (pointslength-1) + "]");
        }

        // проверка минимальное количество точек
        if (pointslength <= 3) {
            throw new IllegalStateException("Нельзя удалить точку - останется меньше 3 точек. Текущее количество: " + pointslength);
        }

        if(index >= 0 && index < pointslength){
            points_arr[index] = null;
            for(int i = index; i < pointslength-1; i++){
                points_arr[i] = points_arr[i+1];
            }
            points_arr[pointslength-1] = null;
            pointslength--;
        }
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        // проверка: дублирование X
        for (int i = 0; i < pointslength; i++) {
            if (compareDouble(points_arr[i].getX(), point.getX())) {
                throw new InappropriateFunctionPointException("Точка с X=" + point.getX() + " уже существует");
            }
        }

        int indx = 0;
        if(point.getX() > points_arr[pointslength-1].getX()){
            indx = pointslength;
        }
        for(int i = 0; i < pointslength-1; i++){
            if(point.getX() >= points_arr[i].getX() && point.getX() <= points_arr[i+1].getX()){
                indx = i+1;
                i=pointslength-1;
            }
        }
        FunctionPoint[] temp_arr = new FunctionPoint[pointslength+1];
        System.arraycopy(points_arr, 0, temp_arr, 0, indx);
        temp_arr[indx] = point;
        System.arraycopy(points_arr, indx, temp_arr, indx+1, pointslength-indx);
        points_arr = temp_arr;
        pointslength++;
    }

    //Возвращает текстовое описание табулированной функции в формате {(x1; y1), (x2; y2), ...}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < pointslength; i++) {
            sb.append("(").append(points_arr[i].getX())
                    .append("; ").append(points_arr[i].getY()).append(")");
            if (i < pointslength - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    //Сравнивает две табулированные функции на равенство

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        // Если объект является ArrayTabulatedFunction, используем оптимизированное сравнение
        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction other = (ArrayTabulatedFunction) o;

            // Быстрая проверка количества точек
            if (this.pointslength != other.pointslength) {
                return false;
            }

            // Прямое сравнение массивов точек
            for (int i = 0; i < pointslength; i++) {
                if (!this.points_arr[i].equals(other.points_arr[i])) {
                    return false;
                }
            }
            return true;
        }

        // Если объект реализует TabulatedFunction, но не ArrayTabulatedFunction
        if (o instanceof TabulatedFunction) {
            TabulatedFunction other = (TabulatedFunction) o;

            // Проверка количества точек
            if (this.getPointsCount() != other.getPointsCount()) {
                return false;
            }

            // Сравнение каждой точки через getPoint
            for (int i = 0; i < pointslength; i++) {
                FunctionPoint thisPoint = this.points_arr[i];
                FunctionPoint otherPoint = other.getPoint(i);

                if (!thisPoint.equals(otherPoint)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    //Вычисляет хэш-код табулированной функции

    @Override
    public int hashCode() {
        int hash = pointslength;

        // XOR хэш-кодов всех точек
        for (int i = 0; i < pointslength; i++) {
            hash ^= points_arr[i].hashCode();
        }

        return hash;
    }

    //Создает глубокую копию табулированной функции

    @Override
    public Object clone() {
        try {
            ArrayTabulatedFunction cloned = (ArrayTabulatedFunction) super.clone();

            // Глубокое копирование массива точек
            cloned.points_arr = new FunctionPoint[this.points_arr.length];
            for (int i = 0; i < pointslength; i++) {
                cloned.points_arr[i] = (FunctionPoint) this.points_arr[i].clone();
            }
            cloned.pointslength = this.pointslength;

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Ошибка при клонировании объекта ArrayTabulatedFunction", e);
        }
    }

    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < pointslength;
            }

            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Нет следующего элемента");
                }
                // Возвращаем копию точки для защиты инкапсуляции
                return new FunctionPoint(points_arr[currentIndex++]);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Удаление не поддерживается");
            }
        };
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