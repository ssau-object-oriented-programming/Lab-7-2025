package functions;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction implements TabulatedFunction, Serializable  {
    private static final double EPS = 1e-9;
    private static final long serialVersionUID = 1L;

    //  Внутренний класс узла списка
    private static class FunctionNode implements Serializable {
        private static final long serialVersionUID = 1L;
        FunctionPoint point;
        FunctionNode next;
        FunctionNode prev;

        FunctionNode(FunctionPoint point) {
            this.point = point;
        }
    }


    private FunctionNode head;
    private int pointsCount;

    // Конструктор по умолчанию (создает пустой список)
    public LinkedListTabulatedFunction() {
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        pointsCount = 0;
    }

    // Конструктор с количеством точек (y = 0)
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        this();
        if (leftX >= rightX - EPS)
            throw new IllegalArgumentException("Left border must be less than right border");
        if (pointsCount < 2)
            throw new IllegalArgumentException("At least two points required");

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            addNodeToTail(new FunctionPoint(x, 0.0));
        }
    }

    // Конструктор с массивом значений
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        this();
        if (leftX >= rightX - EPS)
            throw new IllegalArgumentException("Left border must be less than right border");
        if (values.length < 2)
            throw new IllegalArgumentException("At least two points required");

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            addNodeToTail(new FunctionPoint(x, values[i]));
        }
    }
    // в классе LinkedListTabulatedFunction (package functions)

    //конструктор, получающий сразу все точки функции в виде массива объектов типа FunctionPoint
    public LinkedListTabulatedFunction(FunctionPoint[] points) {

        this();

        if (points == null) {
            throw new IllegalArgumentException("Points array is null");
        }
        if (points.length < 2) {
            throw new IllegalArgumentException("At least two points required");
        }

        // Копируем первый элемент
        FunctionPoint prevCopy = new FunctionPoint(points[0]);
        addNodeToTail(prevCopy);

        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i - 1].getX() + EPS) {//  строгое возрастание
                throw new IllegalArgumentException("Points must be strictly increasing by X (index " + i + ")");
            }
            addNodeToTail(new FunctionPoint(points[i]));// вставляем копию
        }
    }

    // Добавление узла в конец списка
    private FunctionNode addNodeToTail(FunctionPoint point) {
        FunctionNode newNode = new FunctionNode(point);
        newNode.prev = head.prev;
        newNode.next = head;
        head.prev.next = newNode;
        head.prev = newNode;
        pointsCount++;
        return newNode;
    }

    // Поиск узла по индексу
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);

        FunctionNode current;
        if (index < pointsCount / 2) {
            current = head.next;
            for (int i = 0; i < index; i++) current = current.next;
        } else {
            current = head.prev;
            for (int i = pointsCount - 1; i > index; i--) current = current.prev;
        }
        return current;
    }

    // Получение границ функции
    public double getLeftDomainBorder() {
        return head.next.point.getX();
    }

    public double getRightDomainBorder() {
        return head.prev.point.getX();
    }

    // Количество точек
    public int getPointsCount() {
        return pointsCount;
    }

    // Линейная интерполяция
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder())
            return Double.NaN;

        FunctionNode current = head.next;
        while (current.next != head) {
            double x1 = current.point.getX();
            double y1 = current.point.getY();
            double x2 = current.next.point.getX();
            double y2 = current.next.point.getY();

            if (Math.abs(x - x1) < EPS)
                return y1;
            if (x > x1 && x < x2)
                return y1 + (y2 - y1) * ((x - x1) / (x2 - x1));

            current = current.next;
        }
        return Double.NaN;
    }

    // Получение точки
    public FunctionPoint getPoint(int index) {
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    // Изменение всей точки
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        double newX = point.getX();

        if ((index > 0 && newX <= getNodeByIndex(index - 1).point.getX() + EPS) ||
                (index < pointsCount - 1 && newX >= getNodeByIndex(index + 1).point.getX() - EPS)) {
            throw new InappropriateFunctionPointException("New X breaks order of points");
        }

        node.point = new FunctionPoint(point);
    }

    // Получение X
    public double getPointX(int index) {
        return getNodeByIndex(index).point.getX();
    }
    // Получение Y
    public double getPointY(int index) {
        return getNodeByIndex(index).point.getY();
    }

    // Изменение X
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        if ((index > 0 && x <= getNodeByIndex(index - 1).point.getX() + EPS) ||
                (index < pointsCount - 1 && x >= getNodeByIndex(index + 1).point.getX() - EPS)) {
            throw new InappropriateFunctionPointException("New X breaks order of points");
        }

        node.point.setX(x);
    }

    // Изменение Y
    public void setPointY(int index, double y) {
        getNodeByIndex(index).point.setY(y);
    }

    // Удаление точки
    public void deletePoint(int index) {
        if (pointsCount < 3)
            throw new IllegalStateException("Cannot delete: at least 3 points required");

        FunctionNode node = getNodeByIndex(index);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        pointsCount--;
    }

    // Добавление новой точки (по значению)
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double newX = point.getX();

        // Проверяем, нет ли точки с таким же X
        FunctionNode current = head.next;
        while (current != head) {
            if (Math.abs(current.point.getX() - newX) < EPS)
                throw new InappropriateFunctionPointException("Point with same X already exists");
            current = current.next;
        }

        // Ищем место для вставки
        current = head.next;
        while (current != head && current.point.getX() < newX)
            current = current.next;

        // Вставляем новую точку
        FunctionNode newNode = new FunctionNode(new FunctionPoint(point));
        newNode.next = current;
        newNode.prev = current.prev;
        current.prev.next = newNode;
        current.prev = newNode;

        pointsCount++;
    }

    public String toString() {
        if (pointsCount == 0) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('{');

        FunctionNode current = head.next;

        for (int i = 0; i < pointsCount; i++) {
            // Убедимся, что узел существует, прежде чем обращаться к нему
            if (current == null) {
                throw new IllegalStateException("Corrupted list state: list ended unexpectedly.");
            }

            sb.append(current.point.toString());

            if (i < pointsCount - 1) {
                sb.append(", ");
            }
            current = current.next;
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
        if (this.pointsCount != that.getPointsCount()) return false;

        //Если это LinkedListTabulatedFunction, используем прямой обход узлов
        if (o instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction thatList = (LinkedListTabulatedFunction) o;

            FunctionNode currentThis = this.head.next;
            FunctionNode currentThat = thatList.head.next;

            for (int i = 0; i < pointsCount; i++) {
                // Используем FunctionPoint.equals() для сравнения координат
                if (!currentThis.point.equals(currentThat.point)) {
                    return false;
                }
                currentThis = currentThis.next;
                currentThat = currentThat.next;
            }
            return true;
        }

        // Если это ArrayTabulatedFunction
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

        FunctionNode current = head.next;

        // Обходим список и последовательно объединяем хэш-коды всех точек через XOR
        for (int i = 0; i < pointsCount; i++) {
            // points[i].hashCode() вызывает переопределенный метод из FunctionPoint
            result ^= current.point.hashCode();
            current = current.next;
        }

        return result;
    }


    public Object clone() {
        try {

            //Поверхностное клонирование
            LinkedListTabulatedFunction clone = (LinkedListTabulatedFunction) super.clone();

            clone.head = new FunctionNode(null);
            clone.head.next = clone.head;
            clone.head.prev = clone.head;
            clone.pointsCount = 0;

            if (this.pointsCount == 0) {
                return clone;
            }

            FunctionNode currentOriginal = this.head.next;

            for (int i = 0; i < this.pointsCount; i++) {

                // Глубокое клонирование точки
                FunctionPoint clonedPoint = (FunctionPoint) currentOriginal.point.clone();

                clone.addNodeToTail(clonedPoint);

                currentOriginal = currentOriginal.next;
            }

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {

            private FunctionNode node = head.next;

            public boolean hasNext() {
                return node != head;
            }

            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                FunctionPoint point = new FunctionPoint(node.point);

                node = node.next;

                return point;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {

        public TabulatedFunction create(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }

        public TabulatedFunction create(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX, rightX, values);
        }

        public TabulatedFunction create(FunctionPoint[] points) {
            return new LinkedListTabulatedFunction(points);
        }
    }
}



