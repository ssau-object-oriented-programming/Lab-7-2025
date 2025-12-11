package functions;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

//Класс описывает табулированную функцию
public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
    private FunctionPoint[] points;
    private int pointsCount;

    //Машинный эпсилон, нужен для того, чтобы сравнивать переменные с типами double и float
    private static final double EPS = 1e-9;
    private static final long serialVersionUID = 1L;

    //Конструктор, создающий табулированную функцию с равномерными интервалами по X. Значения функции по умолчанию равны 0.
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {

        //Выбрасывает исключение если левая граница больше правой
        if (leftX >= rightX - EPS) {
            throw new IllegalArgumentException("Left border must be less than right border");
        }

        //Выбрасывает исключение, если счетчик точек меньше двух
        if (pointsCount < 2) {
            throw new IllegalArgumentException("At least two points required");
        }
        // Инициализация массива точек
        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount];

        // Вычисление шага по X между точками
        double step = (rightX - leftX) / (pointsCount - 1);

        // Заполнение массива точек, Y = 0 по умолчанию
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0.0);
        }
    }


    // Создаёт табулированную функцию с заданными значениями, X распределяются равномерно между leftX и rightX.
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {

        //Выбрасывает исключение если левая граница больше правой
        if (leftX >= rightX - EPS) {
            throw new IllegalArgumentException("Left border must be less than right border");
        }

        //Выбрасывает исключение, если длинна массива меньше 2
        if (values.length < 2) {
            throw new IllegalArgumentException("At least two points required");
        }

        // Инициализация массива точек
        this.pointsCount = values.length;
        this.points = new FunctionPoint[pointsCount];

        // Вычисление шага по X между точками
        double step = (rightX - leftX) / (pointsCount - 1);

        // Заполнение массива точек, Y = 0 по умолчанию
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    //конструктор, получающий сразу все точки функции в виде массива объектов типа FunctionPoint
    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points array is null");
        }
        if (points.length < 2) {
            throw new IllegalArgumentException("At least two points required");
        }

        this.pointsCount = points.length;
        this.points = new FunctionPoint[pointsCount];

        // Проверяем и копируем первый элемент
        this.points[0] = new FunctionPoint(points[0]); // копия
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i - 1].getX() + EPS)  { //  строгое возрастание
                throw new IllegalArgumentException("Points must be strictly increasing by X (index " + i + ")");
            }
            this.points[i] = new FunctionPoint(points[i]); // копия
        }
    }

    //Возвращает значение левой границы области определения функции.
    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    //Возвращает значение правой границы области определения функции.
    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }

    //Возвращает значение функции в точке x.
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        // Проверка крайних точек
        if (Math.abs(x - points[0].getX()) < EPS)
            return points[0].getY();

        if (Math.abs(x - points[pointsCount - 1].getX()) < EPS)
            return points[pointsCount - 1].getY();

        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = points[i].getX();
            double x2 = points[i+1].getX();

            if (Math.abs(x - x2) < EPS) return points[i+1].getY();

            if (x > x1 && x < x2) {
                double y1 = points[i].getY();
                double y2 = points[i+1].getY();
                return y1 + (y2 - y1) * ( (x - x1) / (x2 - x1) );
            }
        }

        return Double.NaN;
    }


    //Возвращает количество точек табулированной функции.
    public int getPointsCount() {
        return pointsCount;
    }


    //Возвращает копию точки с указанным индексом. Используется конструктор копирования для сохранения принципа инкапсуляции
    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return new FunctionPoint(points[index]);
    }

    //Заменяет точку по индексу на переданную.
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException{
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of range: " + index);
        }

        double newX = point.getX();

        // Для индексa = 0 или index = pointsCount-1 соответствующая проверка просто пропускается
        if ((index > 0 && newX <= points[index - 1].getX() + EPS) ||
                (index < pointsCount - 1 && newX >= points[index + 1].getX() - EPS)) {
            throw new InappropriateFunctionPointException("New X breaks order of points");
        }

        points[index] = new FunctionPoint(point);
    }

    //Возвращает X точки с указанным индексом.
    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return points[index].getX();
    }


    //Изменяет абсциссу точки с указанным индексом.
    public void setPointX(int index, double x)throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of range: " + index);
        }

        // Для индексa = 0 или index = pointsCount-1 соответствующая проверка просто пропускается
        if ((index > 0 && x <= points[index - 1].getX() + EPS) ||
                (index < pointsCount - 1 && x >= points[index + 1].getX() - EPS)) {
            throw new InappropriateFunctionPointException("New X breaks order of points");
        }

        points[index].setX(x);
    }

    //Возвращает ординату точки с указанным индексом.
    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return points[index].getY();
    }

    //Устанавливает новое значение ординаты точки с указанным индексом.
    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        points[index].setY(y);
    }

    //Удаляет точку табулированной функции по индексу.
    public void deletePoint(int index) {
        if (pointsCount < 3) {
            throw new IllegalStateException("Cannot delete: function must have at least 3 points");
        }
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }

        // Сдвигаем все элементы после удаляемого на одну позицию влево
        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);

        pointsCount--; // уменьшаем счётчик точек
    }

    //Добавляет новую точку в таблицу, сохраняя при этом порядок по X.
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double newX = point.getX();

        // Проверяем, нет ли уже такой точки
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(points[i].getX() - newX) < EPS) {
                throw new InappropriateFunctionPointException("Point with same X already exists");
            }
        }

        // Если массив заполнен, создаём новый массив большего размера
        if (pointsCount == points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        // Найдём, куда вставить новую точку, чтобы сохранить порядок по X
        int insertIndex = 0;
        while (insertIndex < pointsCount && points[insertIndex].getX() < newX) {
            insertIndex++;
        }

        // Сдвигаем элементы вправо, освобождая место
        System.arraycopy(points, insertIndex, points, insertIndex + 1, pointsCount - insertIndex);

        // Вставляем копию точки (инкапсуляция!)
        points[insertIndex] = new FunctionPoint(point);
        pointsCount++;
    }

    public String toString() {
        if (pointsCount == 0) {
            return "{}";
        }

        // StringBuilder - для эффективной работы со строками в цикле
        StringBuilder sb = new StringBuilder();
        sb.append('{');

        for (int i = 0; i < pointsCount; i++) {
            // Добавляем строковое представление каждой точки
            sb.append(points[i].toString());

            if (i < pointsCount - 1) {
                sb.append(", ");
            }
        }

        sb.append('}');
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;

        // Проверяем, что объект реализует интерфейс TabulatedFunction
        if (!(o instanceof TabulatedFunction)) return false;

        TabulatedFunction that = (TabulatedFunction) o;

        // Проверяем количество точек
        if (this.getPointsCount() != that.getPointsCount()) return false;

        // Если это ArrayTabulatedFunction, используем прямой доступ
        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction thatArray = (ArrayTabulatedFunction) o;

            // Прямое сравнение массивов, используя FunctionPoint.equals()
            for (int i = 0; i < pointsCount; i++) {
                // Сравниваем точки через FunctionPoint.equals()
                if (!this.points[i].equals(thatArray.points[i])) {
                    return false;
                }
            }
            return true;
        }

        // Если это LinkedList
        else {
            for (int i = 0; i < pointsCount; i++) {
                FunctionPoint pointThis = this.getPoint(i);
                FunctionPoint pointThat = that.getPoint(i); // 'that' — это TabulatedFunction 'o'

                // Сравниваем точки через их собственный equals(), который уже содержит логику EPS
                if (!pointThis.equals(pointThat)) {
                    return false;
                }
            }

            return true;
        }
    }

    public int hashCode() {
        int result = pointsCount;
        // Последовательно объединяем хэш-коды всех точек через XOR
        for (int i = 0; i < pointsCount; i++) {
            // points[i].hashCode() вызывает переопределенный метод из FunctionPoint
            result ^= points[i].hashCode();
        }

        return result;
    }

    public Object clone() {
        try {
            // Поверхностное клонирование самого объекта
            ArrayTabulatedFunction clone = (ArrayTabulatedFunction) super.clone();

            // Глубокое клонирование: создаем новый массив
            clone.points = new FunctionPoint[this.points.length];

            // Глубокое клонирование: копируем точки одну за другой
            for (int i = 0; i < this.pointsCount; i++) {
                clone.points[i] = (FunctionPoint) this.points[i].clone();
            }

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int index = 0;

            public boolean hasNext() {
                return index < pointsCount;
            }

            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                FunctionPoint point = new FunctionPoint(points[index]);
                index++;
                return point;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {
        public TabulatedFunction create(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }

        public TabulatedFunction create(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }

        public TabulatedFunction create(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);
        }
    }
}
