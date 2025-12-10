package functions;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable{
    /**
     * @param points массив для табулированной функции
     * @param EPSILON_DOUBLE число необходимое для проверки равенства переменных типа double
     */
    private FunctionPoint[] points;
    private int pointsCount;
    private final double EPSILON_DOUBLE = 1e-9;

    public ArrayTabulatedFunction() {
        points = new FunctionPoint[0];
        pointsCount = 0;
    }

    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if (points.length < 2) {
            throw new IllegalStateException("В массиве не может быть только одна точка");
        }
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() < points[i - 1].getX()){
                throw new IllegalStateException("Массив не упорядочен по координатам X");
            }
        }
        pointsCount = points.length;
        this.points = new FunctionPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        pointsCount = in.readInt();
        points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; ++i) {
            points[i] = new FunctionPoint(in.readDouble(), in.readDouble());
        }
    }

    /**
     * Создает табулированную функцию в случае если дано только количество точек
     * @param step шаг между координатами X
     */
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        this.pointsCount = pointsCount;
        if (leftX >= rightX) {
            throw new IllegalStateException("Левая граница больше или равна правой");
        }
        if (pointsCount < 2) {
            throw new IllegalStateException("В массиве не может быть только одна точка");
        }
        this.points = new FunctionPoint[this.pointsCount];
        double step = (rightX - leftX)/(pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            points[i] = new FunctionPoint(leftX + i*step, 0);
        }
    }

    /**
     * Создает табулированную функцию в случае если дан массив координат Y
     */
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        this.pointsCount = values.length;
        if (leftX >= rightX) {
            throw new IllegalStateException("Левая граница больше или равна правой");
        }
        if (pointsCount < 2) {
            throw new IllegalStateException("В массиве не может быть только одна точка");
        }
        this.points = new FunctionPoint[this.pointsCount];
        double step = (rightX - leftX)/(pointsCount - 1);
        int i;
        for (i = 0; i < pointsCount; i++) {
            points[i] = new FunctionPoint(leftX + i*step, values[i]);
        }
    }
    /**
     * @return возвращает левую границу
     */

    @Override
    public double getLeftDomainBorder() {
        return points[0].getX();
    }
    /**
     * @return возвращает правую границу
     */

    @Override
    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }
    /**
     * Метод позволяет посчитать значение Y для заданного X, которого нет в табулированной функции
     * @param value полученное значение Y
     * @return значение Y для переданного X
     */

    @Override
    public double getFunctionValue(double x) {
        if (x < points[0].getX() || x > points[pointsCount - 1].getX()) {
            return Double.NaN;
        }
        if (Math.abs(x - points[0].getX()) < EPSILON_DOUBLE) { // Случай совпадания переданного X с левой границей 
            return points[0].getY();
        }
        if (Math.abs(x  - points[pointsCount - 1].getX()) < EPSILON_DOUBLE) { // Случай совпадания переданного X с правой границей
            return points[pointsCount - 1].getY();
        }
        else {
            int i;
            double value = 0;
            // Условие цикла необходимо для нахождения нужного промежутка для переданного X
            for (i = 0; x >= points[i].getX(); i++){
                if (Math.abs(x - points[i].getX()) < EPSILON_DOUBLE){
                    value = points[i].getY();
                }
                else{
                    value = points[i].getY()+(points[i+1].getY() - points[i].getY())*(x-points[i].getX())/(points[i+1].getX()-points[i].getX());
                }
            }
            return value; 
        }
    }
    /**
     * @return возвращает количество точек в табулированной функции
     */
    @Override
    public int getPointsCount() {
        return pointsCount;
    }
    /**
     * Метод позволяет получить значение точки (X,Y) по переданному индексу
     * @return возвращает 
     */
    @Override
    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Получить точку с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        return new FunctionPoint(points[index].getX(), points[index].getY());
    }
    /*
     * Метод позволяет поменять значение точки point (X,Y) по переданному индексу
     */
    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Задать точку с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        if (index == 0) { // Проверка, если пользователь пытается изменить левую границу
            if (point.getX() > getRightDomainBorder()) {
                throw new InappropriateFunctionPointException("Новая точка X (" + point.getX() + ") выходит за границы (больше правой границы).");
            }
        } else if (index == pointsCount - 1) {
            if (point.getX() < getLeftDomainBorder())  { // Проверка, если пользователь пытается изменить правую границу
                throw new InappropriateFunctionPointException("Новая точка X (" + point.getX() + ") выходит за границы (меньше левой границы).");
            }
        }
        else {
            if (point.getX() < points[index - 1].getX() || point.getX() > points[index + 1].getX()) { // Условия проверяют координату X у point на то, что она не выходит за значения соседних X
                throw new InappropriateFunctionPointException("Новая точка X (" + point.getX() + ") выходит за границы соседних к ней точек.");
            }
        }
        points[index] = new FunctionPoint(point);
    }
    /**
     * Метод позволяет получить значение X по переданному индексу
     * @return координата X
     */
    @Override
    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Получить координату X с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        else{
            return points[index].getX();
        }
    }
     /*
     * Метод позволяет поменять значение X по переданному индексу
     */
    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Задать координату X с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        if (x < points[index - 1].getX() || x > points[index + 1].getX()) {
            throw new InappropriateFunctionPointException("Новая точка X (" + x + ") выходит за границы соседних к ней точек.");
        }
        points[index].setX(x);
    }
    /**
     * Метод позволяет получить значение Y по переданному индексу
     * @return координата Y
     */
    @Override
    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Получить координату Y с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        else{
            return points[index].getY();
        }
    }
    /*
     * Метод позволяет поменять значение Y по переданному индексу
     */
    @Override
    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Задать координату Y с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        else{
            points[index].setY(y);
        }
    }
    /*
     * Метод позволяет удалить точку по переданному индексу
     */
    @Override
    public void deletePoint(int index) {
		if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Удалить точку с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        } 
        if (pointsCount < 3) {
            throw new IllegalStateException("В массиве меньше 3 точек");
        }
        //Удаление крайней правой точки
        if(index == pointsCount - 1) {
            points[pointsCount - 1] = null;
            pointsCount--;
        }
        else { 
            System.arraycopy(points, index + 1, points, index, pointsCount - 1 - index);//Сдвиг всех элементов влево и непосредственное удаление точки
            pointsCount--;
            points[pointsCount] = null; //Необходимо для удаления скопированной крайней правой точки 

        }

    }
    /*
     * Метод позволяет добавить точку
     */
    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        if (point.getX() > points[pointsCount - 1].getX()){
            if(pointsCount < points.length){
                points[pointsCount] = point;
                pointsCount++;
            }
            else{
                FunctionPoint [] newPoints = new FunctionPoint[pointsCount * 2];
                System.arraycopy(points, 0, newPoints, 0, pointsCount);
                System.arraycopy(points, pointsCount, newPoints, pointsCount + 1, pointsCount - pointsCount + 1);
                points[pointsCount] = point;
                pointsCount++; 
            }
        }
        else{
            int i = 0;
            while (point.getX() > points[i].getX()) ++i;
            if(Math.abs(points[i].getX() - point.getX()) < EPSILON_DOUBLE) {
                throw new InappropriateFunctionPointException("Координата X добавляемой точки совпадает с уже сужествующим X ");
            }
            if(pointsCount >= points.length){
                FunctionPoint [] newPoints = new FunctionPoint[pointsCount * 2];
                if (i > 0){
                    System.arraycopy(points, 0, newPoints, 0, i);
                }
                if (i < pointsCount){
                    System.arraycopy(points, i, newPoints, i + 1, pointsCount - i);
                }
            }
            else{
                if(i > 0){
                    System.arraycopy(points, 0, points, 0, i);
                }
                if(i < pointsCount){
                    System.arraycopy(points, i, points, i+1, pointsCount - i);
                }
            }
            setPoint (i, point);
            pointsCount++;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(); 
        sb.append("{");
        for (int i = 0; i < pointsCount; i++) {
            FunctionPoint point = getPoint(i);
            if (point != null) {
                sb.append("(").append(point.getX()).append(" ; ").append(point.getY()).append(")");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
    
        if (o == null) {
            return false;
        }

        TabulatedFunction otherFunction = (TabulatedFunction) o;
        if (this.pointsCount != otherFunction.getPointsCount()) {
            return false;
        }

        ArrayTabulatedFunction otherArrayFunction = null;
        if (o instanceof ArrayTabulatedFunction) {
            otherArrayFunction = (ArrayTabulatedFunction) o;
        }
        double epsilon = 1e-9;
    
        if (otherArrayFunction != null) {
            for (int i = 0; i < this.pointsCount; i++) {
                FunctionPoint p1 = this.points[i];
                FunctionPoint p2 = otherArrayFunction.points[i];
                if (p1 == null || p2 == null) {
                    return false;
                }
                if (Math.abs(p1.getX() - p2.getX()) >= epsilon || Math.abs(p1.getY() - p2.getY()) >= epsilon) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < this.pointsCount; i++) {
                FunctionPoint p1 = this.points[i];
                FunctionPoint p2 = otherFunction.getPoint(i);
                if (p1 == null || p2 == null) {
                    return false;
                }
                if (Math.abs(p1.getX() - p2.getX()) >= epsilon || Math.abs(p1.getY() - p2.getY()) >= epsilon) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = pointsCount;
        for (int i = 0; i < pointsCount; i++) {
            FunctionPoint point = points[i];
            result = point.hashCode();
        }
        return result;
    }
    
    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public TabulatedFunction clone(){
        try {
            ArrayTabulatedFunction clonedFunction = (ArrayTabulatedFunction) super.clone();
            clonedFunction.points = new FunctionPoint[this.pointsCount];
            for (int i = 0; i < this.pointsCount; i++) {
                FunctionPoint originalPoint = this.points[i];
                clonedFunction.points[i] = (FunctionPoint) originalPoint.clone();
            }
            return clonedFunction;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Клонирование невозможно", e);
        }
    }

    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < pointsCount;
            }

            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Следующего элемента нет");
                }
                return new FunctionPoint(points[index++]);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Удалить элемент нельзя");
            }
        };
    }

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory{

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);
        }
        
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }
    }
}

