package functions;
import static functions.FunctionPoint.*;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class ArrayTabulatedFunction implements TabulatedFunction, Serializable{ 
    double leftDomainBorder, rightDomainBorder; // Левая и правая границы // был private

   FunctionPoint[] points; // Массив точек // был private
   int size; // был private

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException{ // Конструктор с количеством точек
        if (leftX >= rightX){
            throw new IllegalArgumentException("Левая ганица не может быть больше или равной правой");
        }
        if (pointsCount < 2){
            throw new IllegalArgumentException("Количество точек не может быть меньше двух.");
        }
        this.leftDomainBorder = leftX; // Левая граница области определения
        this.rightDomainBorder = rightX; // Правая граница области определения
        this.points = new FunctionPoint[pointsCount]; // Массив точек (FunctionPoint)
        this.size = pointsCount;

        double delta = (rightX - leftX) / (pointsCount - 1); // Считаем дельту, размер каждого из отрезков
        for (int i = 0; i < pointsCount-1; ++i){ // Инициализируем значения точек в цикле кроме последнего
            points[i] = new FunctionPoint(leftX + (i * delta), 0);
        }
        points[pointsCount-1] = new FunctionPoint(rightX, 0);
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException{ // Конструктор с значениями функций
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая ганица не может быть больше или равной правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек не может быть меньше двух.");
        }
        this.leftDomainBorder = leftX; 
        this.rightDomainBorder = rightX;
        this.points = new FunctionPoint[values.length];
        this.size = values.length;

        double delta = (rightX - leftX) / (values.length - 1); 
        for (int i = 0; i < values.length - 1; ++i) { // В цикле инициализируем значения точек, но уже вместе с y
            points[i] = new FunctionPoint(leftX + (i * delta), values[i]);
        }
        points[values.length- 1] = new FunctionPoint(rightX, values[values.length - 1]);
    }

    public ArrayTabulatedFunction(FunctionPoint[] newPoints){
        if (newPoints.length < 2){throw new IllegalArgumentException("В массиые не может быть меньше двух точек");}

        for (int i = 0; i < newPoints.length - 1; i++){
            if (largeThan(newPoints[i].getCoorX() , newPoints[i+1].getCoorX()) || equal(newPoints[i].getCoorX(),newPoints[i+1].getCoorX())){
                throw new IllegalArgumentException("В массиве точек нарушен порядок");
            }
        }

        points = new FunctionPoint[newPoints.length];
        for (int i = 0; i < newPoints.length; i++) {
            points[i] = new FunctionPoint(newPoints[i]); 
        }

        this.leftDomainBorder = points[0].getCoorX();
        this.rightDomainBorder = points[points.length - 1].getCoorX();

        this.size = points.length;

    }

    // Гет методы
    public double getLeftDomainBorder(){return leftDomainBorder;} 
    public double getRightDomainBorder() {return rightDomainBorder;}


    // Метод для получения прямой по двум точкам
    private double interpolate(double x, double x1, double y1, double x2, double y2){
        return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
    }

    // Метод для получения значения функции
    public double getFunctionValue(double x){
        if (x < this.leftDomainBorder || x > this.rightDomainBorder){ // Если x выходит из диапозона возращаем Double.NaN 
            return Double.NaN;
        }

        for (int i = 0; i < size - 1; ++i){ // Перебараем в цикле все отрезки интервала

            if (equal(x, points[i].getCoorX())){
                return points[i].getCoorY();
            }

            if (equal(x, points[i+1].getCoorX())){
                return points[i+1].getCoorY();
            }
            if (largeThan(x ,points[i].getCoorX()) && largeThan(points[i+1].getCoorX(), x)){ // Если x входит в определённый отрезок
                return interpolate(x, points[i].getCoorX(), points[i].getCoorY(), points[i+1].getCoorX(), points[i+1].getCoorY());
            }

            
        }
        return Double.NaN;
    }

    public int getPointsCount(){ // Возвращает количество точек
        return size;
    }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException{

        if (index < 0 || index >=size){ // Если не подходит под условие выбрасываем исключение
            throw new FunctionPointIndexOutOfBoundsException("Выход за пределы массива"); 
        }

        FunctionPoint pointCopy = new FunctionPoint(points[index]); // Создаём копию, чтобы не нарушать инкапчуляцию
        return pointCopy;
    }

    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, 
            InappropriateFunctionPointException{ 
        if (point == null){
            throw new InappropriateFunctionPointException("Попытка изменить точку на null");
        }
        if (index < 0 || index >= size) { // Если не подходит под условие выбрасываем исключение
            throw new FunctionPointIndexOutOfBoundsException("Выход за пределы массива");
        }
        if (index > 0 && largeThan(points[index - 1].getCoorX(), point.getCoorX())){
            throw new InappropriateFunctionPointException("x Нарушает порядок,выберите другой индекс");
        }
        if (index < size - 1 && largeThan(point.getCoorX(), points[index + 1].getCoorX())){
            throw new InappropriateFunctionPointException("x Нарушает порядок,выберите другой индекс");
        }
        points[index].setCoorX(point.getCoorX());
        points[index].setCoorY(point.getCoorY());
    }

    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException{
        if (index < 0 || index >= size) { // Если не подходит под условие выбрасываем исключение
            throw new FunctionPointIndexOutOfBoundsException("Выход за пределы массива");
        }
        return points[index].getCoorX();
    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException{
        if (index < 0 || index >= size) { // Если не подходит под условие выбрасываем исключение
            throw new FunctionPointIndexOutOfBoundsException("Выход за пределы массива");
        }
        return points[index].getCoorY();
    }

    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException{ 
        if (index < 0 || index >= size) { // Если не подходит под условие выбрасываем исключение
            throw new FunctionPointIndexOutOfBoundsException("Выход за пределы массива");
        }

        if (index > 0 && largeThan(points[index-1].getCoorX(), x )){
            throw new InappropriateFunctionPointException("x Нарушает порядок,выберите другой индекс");
        }
        if (index < size-1 && largeThan(x, points[index + 1].getCoorX())){
            throw new InappropriateFunctionPointException("x Нарушает порядок,выберите другой индекс");
        }
            points[index].setCoorX(x);   
    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) { // Если не подходит под условие выбрасываем исключение
            throw new FunctionPointIndexOutOfBoundsException("Выход за пределы массива");
        }
        points[index].setCoorY(y);
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException{
        if (size < 3){
            throw new IllegalStateException("Нельзя удалить точку из массива");
        }

        if (index < 0 || index >= size) { // Если не подходит под условие выбрасываем исключение
            throw new FunctionPointIndexOutOfBoundsException("Выход за пределы массива");
        }
    
        System.arraycopy(points, index + 1, points, index, size - index - 1);
        points[size-1] = null;
        --size;


        if (size > 0) { // Обнавляем границы после удаления
            this.leftDomainBorder = points[0].getCoorX();
            this.rightDomainBorder = points[size - 1].getCoorX();
        }
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{

        final int k = 2; // Коэфф. расширения длины массива

        double point_x = point.getCoorX();
        int point_idx = size; // Индекс новой точки // По умолчанию в конец

        if (largeThan(leftDomainBorder, point_x)){ // Проверяем расширяет ли точка диапазон
            point_idx = 0; 
        }else{
            if (largeThan(point_x, rightDomainBorder)){
                point_idx = size;
            }
        }

        for (int i = 0; i < size; ++i){
            if (equal(point_x, points[i].getCoorX())){ // Если точка по x совпадает с другой точкой
                throw new InappropriateFunctionPointException("Точка должна иметь уникальное значение x");
            }
        }

        for (int i = 0; i < size - 1; ++i){ // Если точка принадлежит интервалу
            if (largeThan(point_x, points[i].getCoorX()) && largeThan(points[i+1].getCoorX(), point_x)){ // Если входит в один из отрезков интервала
                point_idx = i + 1;
                break;
            }
        }

        if (points.length <= size) { // Проверяем хватает ли длины массива, если нет расширяем массив на 1
            FunctionPoint[] new_points = new FunctionPoint[k*points.length];
            
            System.arraycopy(points, 0, new_points, 0, point_idx);
            new_points[point_idx] = new FunctionPoint(point);
            System.arraycopy(points, point_idx, new_points, point_idx+1, size - point_idx);

            points = new_points;
        }else{
            System.arraycopy(points, point_idx, points, point_idx + 1, size - point_idx);
            points[point_idx] = new FunctionPoint(point);
            
        }
        size++;
        if (point_idx == 0) { // обновляем границы
            this.leftDomainBorder = point_x;
        }
        if (point_idx == size - 1) { 
            this.rightDomainBorder = point_x;
        }
    }

    @Override
    public String toString(){
        String result = "{" + points[0].toString();
        for (int i = 1; i < size; i++){
            result += (", " +points[i].toString());
        }
        result += "}";

        return result;
    }

    @Override
    public boolean equals(Object a) {
        if (this == a) { // Сравнение ссылок
            return true;
        }

        if (a == null) {
            return false;
        }

        if (getClass() == a.getClass()) {
            ArrayTabulatedFunction b = (ArrayTabulatedFunction) a;

            if (this.size != b.size) {
                return false;
            }

            if (!equal(this.leftDomainBorder, b.leftDomainBorder) ||
                    !equal(this.rightDomainBorder, b.rightDomainBorder)) {
                return false;
            }

            for (int i = 0; i < size; i++) {
                if (!equal(this.points[i].getCoorX(), b.points[i].getCoorX()) ||
                        !equal(this.points[i].getCoorY(), b.points[i].getCoorY())) {
                    return false;
                }
            }
            return true;
        }

        if (!(a instanceof TabulatedFunction)) {
            return false;
        }

        TabulatedFunction b = (TabulatedFunction) a;

        if (this.size != b.getPointsCount()) {
            return false;
        }

        if (!equal(this.leftDomainBorder, b.getLeftDomainBorder()) ||
                !equal(this.rightDomainBorder, b.getRightDomainBorder())) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (!equal(this.getPointX(i), b.getPointX(i)) ||
                    !equal(this.getPointY(i), b.getPointY(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = size; // Начинаем с количества точек
        // Добавляем хэш-коды всех точек через XOR
        for (int i = 0; i < size; i++) {
            hash ^= points[i].hashCode();
        }
        return hash;
    }

    @Override
    public ArrayTabulatedFunction clone(){
        FunctionPoint[] clone_points = new FunctionPoint[this.size];

        for (int i = 0; i < size; i++){ // Сначала собрали массив
            clone_points[i] = new FunctionPoint(points[i]);
        }

        return new ArrayTabulatedFunction(clone_points); 
    }


    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>(){
            private int index = 0;

            @Override
            public boolean hasNext(){
                return index < size;
            }

            @Override
            public FunctionPoint next(){
                if (!hasNext()){
                    throw new NoSuchElementException();
                }
                return points[index++];
            }

            public void delete(int index) throws UnsupportedOperationException {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {

        public ArrayTabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount){
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }

        public ArrayTabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values){
            return new ArrayTabulatedFunction(leftX, rightX, values);

        }

        public ArrayTabulatedFunction createTabulatedFunction(FunctionPoint[] newPoints){
            return new ArrayTabulatedFunction(newPoints);

        }


    }

}