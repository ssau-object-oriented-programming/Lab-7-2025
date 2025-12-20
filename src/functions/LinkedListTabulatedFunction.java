package functions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static functions.DoubleComparison.*;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {
    private class FunctionNode {
        private FunctionPoint point;
        private FunctionNode prev;
        private FunctionNode next;

        public FunctionNode() {
            this.point = null;
            this.prev = null;
            this.next = null;
        }

        public FunctionNode(FunctionPoint point, FunctionNode prev, FunctionNode next) {
            this.point = point;
            this.prev = prev;
            this.next = next;
        }
    }

    private FunctionNode head;
    private int pointsCount;
    private FunctionNode lastNode;
    private int lastIndex;

    public LinkedListTabulatedFunction() {
        head = new FunctionNode();

        head.next = head;
        head.prev = head;

        pointsCount = 0;
        lastNode = head;
        lastIndex = -1;
    }
    //возвращает ссылку на узел списка по его номеру
    private FunctionNode getNodeByIndex(int index) {

        FunctionNode currentNode;
        //с запомненного элемента
        if (lastIndex != -1 && Math.abs(index - lastIndex) <= 1) {
            currentNode = lastNode;
            if (index > lastIndex) {
                for (int i = lastIndex; i < index; i++) {
                    currentNode = currentNode.next;
                }
            } else if (index < lastIndex) {
                for (int i = lastIndex; i > index; i--) {
                    currentNode = currentNode.prev;
                }
            }
        }
        //с начала списка
        else if (index <= pointsCount / 2) {
            currentNode = head.next;
            for (int i = 0; i < index; i++) {
                currentNode = currentNode.next;
            }
        }
        //с конца
        else {
            currentNode = head.prev;
            for (int i = pointsCount - 1; i > index; i--) {
                currentNode = currentNode.prev;
            }
        }
        lastNode = currentNode;
        lastIndex = index;

        return currentNode;
    }
    //добавляет новый элемент в конец списка
    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode();

        FunctionNode currentLast = head.prev;

        newNode.prev = currentLast;
        newNode.next = head;
        currentLast.next = newNode;
        head.prev = newNode;

        pointsCount ++;
        return newNode;
    }
    //добавляет новый элемент в указанную позицию списка
    private FunctionNode addNodeByIndex(int index) {
        //в конец списка
        if (index == pointsCount) {
            return addNodeToTail();
        }

        FunctionNode newNode = new FunctionNode();
        FunctionNode currentNode = getNodeByIndex(index);

        newNode.prev = currentNode.prev;
        newNode.next = currentNode;

        currentNode.prev.next = newNode;
        currentNode.prev = newNode;

        pointsCount ++;
        return newNode;
    }
    //удаляет элемент списка по номеру
    private FunctionNode deleteNodeByIndex(int index) {

        FunctionNode deleteNode = getNodeByIndex(index);

        deleteNode.prev.next = deleteNode.next;
        deleteNode.next.prev = deleteNode.prev;
        pointsCount --;
        return deleteNode;
    }
    //задание 5
    // Конструктор
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        this(); // Вызываем основной конструктор

        if (doubleGreaterOrEq(leftX, rightX)) {
            throw new IllegalArgumentException("Левая граница не может быть больше или равна правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек не может быть меньше 2");
        }

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            addNodeToTail().point = new FunctionPoint(x,0);
        }
    }
    // Конструктор
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        this();

        if (doubleGreaterOrEq(leftX, rightX)) {
            throw new IllegalArgumentException("Левая граница не может быть больше или равна правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек не может быть меньше 2");
        }

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            addNodeToTail().point = new FunctionPoint(x, values[i]);
        }
    }

    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        this();
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек не может быть меньше 2");
        }
        for (int i = 0; i < points.length - 1; i++) {
            if (doubleGreaterOrEq(points[i].getX(), points[i + 1].getX())) {
                throw new IllegalArgumentException("Точки в массиве не упорядочены по X");
            }
        }
        for (int i = 0; i < points.length; i++) {
            addNodeToTail().point = new FunctionPoint(points[i]);
        }
    }
    public double getLeftDomainBorder() {
        return  getPointX(0);
    }
    public double getRightDomainBorder() {
        return getPointX(pointsCount - 1);
    }
    //Возвращает значение функции в точке х, если точка лежит в обл. определения
    public double getFunctionValue(double x) {
        if (doubleLess(x,getLeftDomainBorder()) || doubleGreater(x,getRightDomainBorder())) {
            return Double.NaN;
        }
        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = current.point.getX();
            double x2 = current.next.point.getX();

            if (doubleGreaterOrEq(x, x1) && doubleLessOrEq(x, x2)) {
                if (doubleEq(x, x1)) {
                    return  current.point.getY();
                }
                else if (doubleEq(x, x2)) {
                    return  current.next.point.getY();
                }
                else {
                    return interpolation(x, current.point, current.next.point);
                }
            }
            current = current.next;
        }
        return Double.NaN;
    }
    // Линейная интерполяция
    private double interpolation(double x, FunctionPoint p1, FunctionPoint p2) {
        return p1.getY() + (x - p1.getX()) * (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
    }

    public int getPointsCount() {
        return pointsCount;
    }

    //Метод возвращает копию точки, соответствующей данному индексу
    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы.");
        }
        return new FunctionPoint(getNodeByIndex(index).point);
    }
    //Метод заменяет указанную точку на переданную
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {

        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы.");
        }

        //находим нужный узел
        FunctionNode currentNode = getNodeByIndex(index);
        double newX = point.getX();

        //если newX меньше левой границы
        if (index != 0 && doubleLessOrEq(newX,currentNode.prev.point.getX())) {
            throw new InappropriateFunctionPointException("X новой точки должен быть больше X предыдущей точки");
        }
        //если newX больше правой границы
        if (index != pointsCount - 1 && doubleGreaterOrEq(newX,currentNode.next.point.getX())) {
            throw new InappropriateFunctionPointException("X новой точки должен быть меньше X следующей точки");
        }

        currentNode.point = new FunctionPoint(point);
    }

    //Метод возвращает значение абсциссы точки с указанным номером
    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы.");
        }
        return getNodeByIndex(index).point.getX();
    }
    //Метод возвращает значение ординаты точки с указанным номером
    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы.");
        }
        return getNodeByIndex(index).point.getY();
    }
    //Метод изменяет значение абсциссы точки с указанным номером
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы.");
        }

        FunctionNode currentNode = getNodeByIndex(index);

        if (index != 0 && doubleLessOrEq(x, currentNode.prev.point.getX())) {
            throw new InappropriateFunctionPointException("X новой точки должен быть больше X предыдущей точки");
        }
        if (index != pointsCount - 1 && doubleGreaterOrEq(x, currentNode.next.point.getX())) {
            throw new InappropriateFunctionPointException("X новой точки должен быть меньше X следующей точки");
        }

        currentNode.point = new FunctionPoint(x, currentNode.point.getY());
    }
    //Метод изменяет значение ординаты точки с указанным номером
    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы.");
        }

        FunctionNode currentNode = getNodeByIndex(index);
        currentNode.point = new FunctionPoint(currentNode.point.getX(), y);
    }
    //Метод удаления точки функции
    public void deletePoint(int index) {
        if (pointsCount < 3) {
            throw new IllegalStateException("Невозможно удалить точку, т.к. в наборе должно быть не менее двух точек" );
        }
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы.");
        }
        deleteNodeByIndex(index);
    }
    //Метод добавления новой точки
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {

        for (int i = 0; i < pointsCount; i++) {
            if (doubleEq(point.getX(),getPointX(i))) {
                throw new InappropriateFunctionPointException("Точка с таким X уже существует");
            }
        }

        FunctionNode current = head.next;
        int searchIndex = 0;

        while (current != head && doubleGreater(point.getX(), current.point.getX())) {
            current = current.next;
            searchIndex++;
        }

        FunctionNode newNode = addNodeByIndex(searchIndex);
        newNode.point = new FunctionPoint(point);
    }

    //сериализует табул. функцию в поток
    public void writeExternal(ObjectOutput out) throws IOException {
        //Записываем количество точек
        out.writeInt(pointsCount);

        //Записываем все точки
        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(current.point.getX());
            out.writeDouble(current.point.getY());
            current = current.next;
        }
    }
    //десериализует табул. функцию из потока
    public void readExternal(ObjectInput in) throws IOException {
        clear();

        //Читаем количество точек
        int count = in.readInt();

        //Читаем и создаем точки
        for (int i = 0; i < count; i++) {
            double x = in.readDouble();
            double y = in.readDouble();

            //Вставляем точку в конец
            addNodeToTail().point = new FunctionPoint(x, y);
        }
    }

    private void clear() {
        //Очищаем список
        head = new FunctionNode();
        head.next = head;
        head.prev = head;
        pointsCount = 0;
        lastNode = head;
        lastIndex = -1;
    }


    public String toString() {
        StringBuilder str = new StringBuilder("{");

        FunctionNode current = head.next;

        while (current != head) {
            str.append(current.point.toString());

            if (current.next != head) {
                str.append(", ");
            }
            current = current.next;
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
        if (o instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction otherList = (LinkedListTabulatedFunction) o;

            FunctionNode thisNode = this.head.next;
            FunctionNode otherNode = otherList.head.next;

            while (thisNode != this.head && otherNode != otherList.head) {

                if (!thisNode.point.equals(otherNode.point)) {
                    return false;
                }
                thisNode = thisNode.next;
                otherNode = otherNode.next;
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

        FunctionNode current = head.next;

        while (current != head) {
            hash = hash ^ current.point.hashCode();
            current = current.next;
        }
        return hash;
    }

    public Object clone() {
        FunctionPoint[] clonedPoints = new FunctionPoint[pointsCount];

        FunctionNode current = head.next;

        for (int i = 0; i < pointsCount; i++) {
            clonedPoints[i] = (FunctionPoint) current.point.clone();
            current = current.next;
        }
        return new LinkedListTabulatedFunction(clonedPoints);
    }

    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private FunctionNode current = head.next;
            private int currentIndex = 0;

            public boolean hasNext() {
                return currentIndex < pointsCount;
            }

            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Нет следующего элемента");
                }
                FunctionPoint point = new FunctionPoint(current.point);
                current = current.next;
                currentIndex++;
                return point;
            }

            public void remove() {
                throw new UnsupportedOperationException("Удаление не доступно");
            }
        };
        }

    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX, rightX, values);
        }

        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new LinkedListTabulatedFunction(points);
        }
    }
    }
