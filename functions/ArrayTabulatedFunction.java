package functions;
import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable{
    private FunctionPoint[] point_mass; // массив точек функции
    private int point_count; // количество точек

    public ArrayTabulatedFunction() {
        this.point_mass = new FunctionPoint[0]; // создаем пустой массив точек
        this.point_count = 0; // устанавливаем количество точек в ноль
    }

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {
        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);  // создает массивную табулированную функцию из точек
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);  // создает массивную функцию с границами и количеством точек
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);  // создает массивную функцию с границами и значениями
        }
    }

    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int currentIndex = 0;  // текущий индекс для итерации

            @Override
            public boolean hasNext() {
                return currentIndex < point_count;  // проверяет есть ли следующая точка
            }

            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more points");  // исключение если точек нет
                }
                // создаем копию точки для защиты инкапсуляции
                FunctionPoint point = point_mass[currentIndex];  // получаем текущую точку
                FunctionPoint copy = new FunctionPoint(point.getX(), point.getY());  // создаем копию точки
                currentIndex++;  // увеличиваем индекс
                return copy;  // возвращаем копию
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove operation is not supported");  // операция удаления не поддерживается
            }
        };
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(point_count); // записываем количество точек
        for (int i = 0; i < point_count; i++) { // проходим по всем точкам
            out.writeDouble(point_mass[i].getX()); // записываем координату x точки
            out.writeDouble(point_mass[i].getY()); // записываем координату y точки
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int count = in.readInt(); // читаем количество точек
        this.point_mass = new FunctionPoint[count + 10]; // создаем массив с запасом
        this.point_count = count; // устанавливаем количество точек

        for (int i = 0; i < count; i++) { // проходим по всем точкам
            double x = in.readDouble(); // читаем координату x
            double y = in.readDouble(); // читаем координату y
            point_mass[i] = new FunctionPoint(x, y); // создаем новую точку
        }
    }
    @Override
    public String toString(){
        String res = "{"; // начинаем строку с фигурной скобки
        for (int i = 0; i < point_count; i++){ // проходим по всем точкам массива
            res+= "("+ point_mass[i].getX() + ";" + point_mass[i].getY() + ")"; // добавляем координаты точки в скобках
            if(i<point_count -1){ // если это не последняя точка
                res+=", "; // добавляем запятую и пробел
            }
        }
        res += "}"; // закрываем фигурную скобку
        return res; // возвращаем результат
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true; // если это тот же объект возвращаем true
        if (o == null || !(o instanceof TabulatedFunction)) return false; // если объект null или не табулированная функция возвращаем false

        if (o instanceof ArrayTabulatedFunction) { // если объект тоже array tabulated function
            if (this.point_count != ((ArrayTabulatedFunction) o).point_count) { // сравниваем количество точек
                return false; // если разное количество точек возвращаем false
            }
            for (int i = 0; i < point_count; i++) { // проходим по всем точкам массива
                if (!this.point_mass[i].equals(((ArrayTabulatedFunction) o).point_mass[i])) { // сравниваем координаты точек
                    return false; // если координаты не равны возвращаем false
                }
            }
        }
        else { // если объект другой реализации tabulated function
            if (this.getPointsCount() != ((TabulatedFunction) o).getPointsCount()) { // сравниваем количество точек через метод интерфейса
                return false; // если разное количество точек возвращаем false
            }
            for (int i = 0; i < point_count; i++) { // проходим по всем точкам
                FunctionPoint thisPoint = new FunctionPoint(this.getPointX(i), this.getPointY(i));
                FunctionPoint otherPoint = new FunctionPoint(((TabulatedFunction) o).getPointX(i), ((TabulatedFunction) o).getPointY(i));
                if (!thisPoint.equals(otherPoint)) {
                    return false;
                }
            }
        }

        return true; // если все проверки пройдены возвращаем true
    }

    @Override
    public int hashCode(){
        int hash = point_count; // начинаем с количества точек

        for (int i = 0; i < point_count; i++) { // проходим по всем точкам массива
            hash ^= point_mass[i].hashCode(); // комбинируем хэш точки через xor
        }
        return hash; // возвращаем результат
    }

    @Override
    public Object clone(){
        FunctionPoint[] points_copy = new FunctionPoint[point_count]; // создаем новый массив для точек
        for(int i =0; i < point_count; i++){ // проходим по всем точкам исходного массива
            points_copy[i] = (FunctionPoint)point_mass[i].clone(); // клонируем каждую точку
        }
        return new ArrayTabulatedFunction(points_copy); // создаем новый объект с скопированными точками
    }

    // конструктор получающий массив точек FunctionPoint
    public ArrayTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
        // проверяем что точек достаточно и они упорядочены
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее двух");
        }

        // проверяем упорядоченность точек по x
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].getX() >= points[i + 1].getX()) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по возрастанию X");
            }
        }

        // создаем массив для хранения точек
        this.point_mass = new FunctionPoint[points.length];
        this.point_count = points.length;

        // копируем точки обеспечивая инкапсуляцию
        for (int i = 0; i < points.length; i++) {
            // создаем копию каждой точки чтобы избежать ссылочной зависимости
            point_mass[i] = new FunctionPoint(points[i]);
        }
    }

    // конструктор создающий функцию с равномерно распределенными точками
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException{
        if(leftX >= rightX || pointsCount<2){
            throw new IllegalArgumentException("Левая граница больше левой или количество точек меньше 2");
        }
        // создаем массив для хранения точек
        this.point_mass = new FunctionPoint[pointsCount];
        this.point_count = pointsCount;
        // вычисляем шаг между точками
        double step = (rightX-leftX)/(pointsCount-1);

        for(int i =0; i < pointsCount; i++){
            // вычисляем x координату для каждой точки
            double x = leftX + i * step;
            // создаем точку с вычисленным x и y=0
            point_mass[i] = new FunctionPoint(x, 0);
        }
    }
    // конструктор создающий функцию с заданными значениями y
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException{
        if(leftX >= rightX || values.length <2){
            throw new IllegalArgumentException("Левая граница больше левой или количество точек меньше 2");
        }
        // создаем массив точек размером с массив значений
        this.point_mass = new FunctionPoint[values.length];
        this.point_count = values.length;

        // вычисляем шаг между точками
        double step = (rightX-leftX)/(point_count-1);

        for(int i =0; i < point_count; i++){
            // вычисляем x координату
            double x = leftX + i * step;
            // создаем точку с вычисленным x и заданным y
            point_mass[i] = new FunctionPoint(x, values[i]);
        }
    }
    // получить левую границу области определения функции
    public double getLeftDomainBorder(){
        return this.point_mass[0].getX(); // x первой точки
    }
    // получить правую границу области определения функции
    public double getRightDomainBorder(){
        return this.point_mass[point_count-1].getX(); // x последней точки
    }
    // вычислить значение функции в заданной точке x
    public double getFunctionValue(double x){
        // если точек нет возвращаем не число
        if(point_count == 0){
            return Double.NaN;
        }

        // проверяем что x находится в области определения
        if(x >= point_mass[0].getX() && x <= point_mass[point_count-1].getX()){
            // ищем точку с точно таким же x
            for(int i = 0; i < point_count; i++){
                if(Math.abs(point_mass[i].getX() - x) < 1e-10){
                    // если нашли возвращаем соответствующий y
                    return point_mass[i].getY();
                }
            }
            // если точного совпадения нет ищем интервал для интерполяции
            for(int i = 0; i < point_count-1; i++){
                double x1 = point_mass[i].getX();
                double x2 = point_mass[i+1].getX();

                // проверяем попадает ли x в текущий интервал
                if(x >= x1 && x <= x2){
                    double y1 = point_mass[i].getY();
                    double y2 = point_mass[i+1].getY();

                    // вычисляем значение линейной интерполяции
                    return y1+(y2-y1)*(x-x1)/(x2-x1);
                }
            }
        }
        // если x вне области определения возвращаем не число
        return Double.NaN;
    }
    // получить общее количество точек в функции
    public int getPointsCount(){
        return point_count; // возвращаем количество точек
    }
    // получить копию точки по указанному индексу
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >= point_count){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        // возвращаем копию чтобы защитить исходные данные
        return new FunctionPoint(point_mass[index].getX(), point_mass[index].getY());
    }
    // заменить точку по указанному индексу
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException{
        // проверяем валидность индекса
        if (index < 0 || index >= point_count) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        // проверяем что новый x не нарушает порядок точек
        // слева от текущей точки x должен быть меньше
        // справа от текущей точки x должен быть больше
        if ((index > 0 && point.getX() <= point_mass[index - 1].getX()) || (index < point_count - 1 && point.getX() >= point_mass[index + 1].getX())){
            throw new InappropriateFunctionPointException("Точка не входит в интервал");
        }
        // создаем новую точку чтобы избежать ссылочной зависимости
        point_mass[index] = new FunctionPoint(point);
    }
    // получить координату x точки по индексу
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >=point_count){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        return point_mass[index].getX(); // возвращаем x точки
    }
    // установить новую координату x для точки по индексу
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException{
        // проверяем валидность индекса
        if (index < 0 || index >= point_count) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        // проверяем что новый x сохраняет порядок точек
        if ((index > 0 && x <= point_mass[index - 1].getX()) || (index < point_count - 1 && x >= point_mass[index + 1].getX())){
            throw new InappropriateFunctionPointException("Точка не входит в интервал");
        }
        point_mass[index].setX(x); // меняем x точки
    }
    // получить координату y точки по индексу
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >= point_count){
            throw new FunctionPointIndexOutOfBoundsException();
        }
        return point_mass[index].getY(); // возвращаем y точки
    }
    // установить новую координату y для точки по индексу
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException{
        // проверяем валидность индекса
        if (index < 0 || index >= point_count) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        point_mass[index].setY(y); // меняем y точки
    }
    // удалить точку по указанному индексу
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException{
        // проверяем можно ли удалить точку
        // должно быть минимум 2 точки и индекс должен быть валидным
        if(index < 0 || index >= point_count){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        if(point_count < 3){
            throw new IllegalStateException("Количество точек меньще 3");
        }
        // сдвигаем все точки после удаляемой влево
        if (index < point_count - 1) {
            System.arraycopy(point_mass, index + 1, point_mass, index, point_count - index - 1);
        }
        // уменьшаем счетчик точек
        point_count--;
        // очищаем последний элемент
        point_mass[point_count] = null;
    }
    // добавить новую точку в функцию
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        int ins_index= 0;
        // ищем позицию для вставки чтобы сохранить возрастающий порядок по x
        while (ins_index < point_count && point_mass[ins_index].getX() < point.getX()) {
            ins_index++;
        }
        // если точка с таким x уже существует выходим
        if (ins_index < point_count && Math.abs(point_mass[ins_index].getX() - point.getX()) < 1e-10) {
            throw new InappropriateFunctionPointException("Совпадают X");
        }
        // проверяем нужно ли увеличивать массив
        if (point_count == point_mass.length) {
            // удваиваем размер массива
            int newcap = point_mass.length * 2;
            // если массив был пустой устанавливаем размер 1
            if (newcap == 0) {
                newcap = 1;
            }
            // создаем новый массив большего размера
            FunctionPoint[] newArray = new FunctionPoint[newcap];
            // копируем старые точки в новый массив
            System.arraycopy(point_mass, 0, newArray, 0, point_count);
            point_mass = newArray;
        }
        // сдвигаем точки чтобы освободить место для новой
        if (ins_index < point_count) {
            System.arraycopy(point_mass, ins_index, point_mass, ins_index + 1, point_count - ins_index);
        }

        // вставляем новую точку
        point_mass[ins_index] = new FunctionPoint(point);
        // увеличиваем счетчик точек
        point_count++;
    }
}