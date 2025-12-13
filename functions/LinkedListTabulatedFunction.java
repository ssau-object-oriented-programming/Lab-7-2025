package functions;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.*;

public class LinkedListTabulatedFunction implements TabulatedFunction, Serializable {
    private FunctionNode head;
    private int pointcount;
    private static final double EPSILON = 1e-10;

    private static class FunctionNode implements Serializable {
        private FunctionPoint point;
        private FunctionNode prev;
        private FunctionNode next;

        public FunctionNode(FunctionPoint point) {
            this.point = point;
            this.prev = null;
            this.next = null;
        }

        public FunctionPoint getPoint() {
            return point;
        }

        public void setPoint(FunctionPoint point) {
            this.point = point;
        }

        public FunctionNode getPrev() {
            return prev;
        }

        public void setPrev(FunctionNode prev) {
            this.prev = prev;
        }

        public FunctionNode getNext() {
            return next;
        }

        public void setNext(FunctionNode next) {
            this.next = next;
        }
    }

    public LinkedListTabulatedFunction() {
        this.head = new FunctionNode(null);
        head.setPrev(head);
        head.setNext(head);
        this.pointcount = 0;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        this();

        if (leftX >= rightX - EPSILON) {
            throw new IllegalArgumentException("некорректные границы области определения");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("недостаточное количество точек");
        }

        double interval = rightX - leftX;
        double step = interval / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double currentX = leftX + i * step;
            addNodeToTail(new FunctionPoint(currentX, 0));
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        this();

        if (leftX >= rightX - EPSILON) {
            throw new IllegalArgumentException("некорректные границы области определения");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("недостаточное количество точек");
        }

        double interval = rightX - leftX;
        double step = interval / (values.length - 1);

        for (int i = 0; i < values.length; i++) {
            double currentX = leftX + i * step;
            addNodeToTail(new FunctionPoint(currentX, values[i]));
        }
    }

    // конструктор, получающий массив точек
    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        this();

        if (points.length < 2) {
            throw new IllegalArgumentException("количество точек должно быть не менее 2");
        }

        // проверка упорядоченности точек по x
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].getX() >= points[i + 1].getX() - EPSILON) {
                throw new IllegalArgumentException("точки должны быть упорядочены по возрастанию x");
            }
        }

        // добавляем точки в список, создавая копии для инкапсуляции
        for (FunctionPoint point : points) {
            addNodeToTail(new FunctionPoint(point));
        }
    }

    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointcount) {
            throw new FunctionPointIndexOutOfBoundsException("индекс выходит за границы");
        }

        FunctionNode current;
        if (index < pointcount / 2) {
            current = head.getNext();
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else {
            current = head.getPrev();
            for (int i = pointcount - 1; i > index; i--) {
                current = current.getPrev();
            }
        }
        return current;
    }

    private FunctionNode addNodeToTail(FunctionPoint point) {
        FunctionNode newNode = new FunctionNode(point);
        FunctionNode tail = head.getPrev();

        newNode.setPrev(tail);
        newNode.setNext(head);
        tail.setNext(newNode);
        head.setPrev(newNode);

        pointcount++;
        return newNode;
    }

    private FunctionNode addNodeByIndex(int index, FunctionPoint point) {
        if (index < 0 || index > pointcount) {
            throw new FunctionPointIndexOutOfBoundsException("индекс выходит за границы");
        }

        if (index == pointcount) {
            return addNodeToTail(point);
        }

        FunctionNode currentNode = getNodeByIndex(index);
        FunctionNode newNode = new FunctionNode(point);
        FunctionNode prevNode = currentNode.getPrev();

        newNode.setPrev(prevNode);
        newNode.setNext(currentNode);
        prevNode.setNext(newNode);
        currentNode.setPrev(newNode);

        pointcount++;
        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointcount) {
            throw new FunctionPointIndexOutOfBoundsException("индекс выходит за границы");
        }

        FunctionNode nodeToDelete = getNodeByIndex(index);
        FunctionNode prevNode = nodeToDelete.getPrev();
        FunctionNode nextNode = nodeToDelete.getNext();

        prevNode.setNext(nextNode);
        nextNode.setPrev(prevNode);

        pointcount--;
        return nodeToDelete;
    }

    public double getLeftDomainBorder() {
        if (pointcount == 0) return Double.NaN;
        return head.getNext().getPoint().getX();
    }

    public double getRightDomainBorder() {
        if (pointcount == 0) return Double.NaN;
        return head.getPrev().getPoint().getX();
    }

    public double getFunctionValue(double x) {
        if (pointcount == 0) {
            return Double.NaN;
        }

        double leftBorder = getLeftDomainBorder();
        double rightBorder = getRightDomainBorder();

        if (x < leftBorder - EPSILON || x > rightBorder + EPSILON) {
            return Double.NaN;
        }

        // ищем точку с точно таким же x
        FunctionNode current = head.getNext();
        while (current != head) {
            if (Math.abs(current.getPoint().getX() - x) < EPSILON) {
                return current.getPoint().getY();
            }
            current = current.getNext();
        }

        // совпадения нет - ищем интервал для интерполяции
        current = head.getNext();
        while (current != head && current.getNext() != head) {
            double x1 = current.getPoint().getX();
            double x2 = current.getNext().getPoint().getX();

            if (x >= x1 - EPSILON && x <= x2 + EPSILON) {
                double y1 = current.getPoint().getY();
                double y2 = current.getNext().getPoint().getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            current = current.getNext();
        }

        return Double.NaN;
    }

    public int getPointsCount() {
        return pointcount;
    }

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointcount) {
            throw new FunctionPointIndexOutOfBoundsException("индекс выходит за границы");
        }
        // возвращаем копию чтобы защитить исходные данные
        FunctionPoint original = getNodeByIndex(index).getPoint();
        return new FunctionPoint(original.getX(), original.getY());
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointcount) {
            throw new FunctionPointIndexOutOfBoundsException("индекс выходит за границы");
        }

        double newX = point.getX();
        if (index > 0 && newX <= getNodeByIndex(index - 1).getPoint().getX() + EPSILON) {
            throw new InappropriateFunctionPointException("нарушение порядка точек");
        }
        if (index < pointcount - 1 && newX >= getNodeByIndex(index + 1).getPoint().getX() - EPSILON) {
            throw new InappropriateFunctionPointException("нарушение порядка точек");
        }

        getNodeByIndex(index).setPoint(new FunctionPoint(point));
    }

    public double getPointX(int index) {
        if (index < 0 || index >= pointcount) {
            throw new FunctionPointIndexOutOfBoundsException("индекс выходит за границы");
        }
        return getNodeByIndex(index).getPoint().getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionPoint currentPoint = getNodeByIndex(index).getPoint();
        FunctionPoint newPoint = new FunctionPoint(x, currentPoint.getY());
        setPoint(index, newPoint);
    }

    public double getPointY(int index) {
        if (index < 0 || index >= pointcount) {
            throw new FunctionPointIndexOutOfBoundsException("индекс выходит за границы");
        }
        return getNodeByIndex(index).getPoint().getY();
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointcount) {
            throw new FunctionPointIndexOutOfBoundsException("индекс выходит за границы");
        }
        FunctionPoint currentPoint = getNodeByIndex(index).getPoint();
        FunctionPoint newPoint = new FunctionPoint(currentPoint.getX(), y);
        getNodeByIndex(index).setPoint(newPoint);
    }

    public void deletePoint(int index) {
        if (index < 0 || index >= pointcount) {
            throw new FunctionPointIndexOutOfBoundsException("индекс выходит за границы");
        }
        if (pointcount < 3) {
            throw new IllegalStateException("невозможно удалить точку");
        }
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode current = head.getNext();
        int insertIndex = 0;

        while (current != head && current.getPoint().getX() < point.getX() - EPSILON) {
            insertIndex++;
            current = current.getNext();
        }

        if (current != head && Math.abs(current.getPoint().getX() - point.getX()) < EPSILON) {
            throw new InappropriateFunctionPointException("точка с таким x уже существует");
        }

        addNodeByIndex(insertIndex, new FunctionPoint(point));
    }

    // возвращает текстовое описание табулированной ф-ции
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        FunctionNode current = head.getNext();
        while (current != head) {
            sb.append(current.getPoint().toString());
            current = current.getNext();
            if (current != head) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    // сравнивает текущую ф-цию с другим объектом
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabulatedFunction)) return false;

        TabulatedFunction other = (TabulatedFunction) o;

        // проверяем количество точек
        if (this.pointcount != other.getPointsCount()) return false;

        // если объект linkedlisttabulatedfunction
        if (other instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction listOther = (LinkedListTabulatedFunction) other;
            FunctionNode currentThis = this.head.getNext();
            FunctionNode currentOther = listOther.head.getNext();

            while (currentThis != this.head && currentOther != listOther.head) {
                if (!currentThis.getPoint().equals(currentOther.getPoint())) {
                    return false;
                }
                currentThis = currentThis.getNext();
                currentOther = currentOther.getNext();
            }
        } else {
            // для других реализаций TabulatedFunction используем интерфейс
            for (int i = 0; i < pointcount; i++) {
                FunctionPoint thisPoint = this.getPoint(i);
                FunctionPoint otherPoint = other.getPoint(i);
                if (!thisPoint.equals(otherPoint)) {
                    return false;
                }
            }
        }

        return true;
    }

    // вычисляет хэш-код ф-ции на основе её точек
    @Override
    public int hashCode() {
        int hash = pointcount; // начинаем с количества точек

        // комбинируем хэш-коды всех точек через xor
        FunctionNode current = head.getNext();
        while (current != head) {
            hash ^= current.getPoint().hashCode();
            current = current.getNext();
        }

        return hash;
    }

    // создает и возвращает копию текущей ф-ции
    @Override
    public Object clone() {
        FunctionPoint[] pointArr = new FunctionPoint[pointcount];
        FunctionNode cur = head.getNext();
        int i = 0;
        while (cur != head) {
            pointArr[i] = (FunctionPoint) cur.getPoint().clone();
            cur = cur.getNext();
            i++;
        }
        return new LinkedListTabulatedFunction(pointArr);
    }

    // реализация итератора для поддержки for-each
    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private FunctionNode currentNode = head.getNext();

            @Override
            public boolean hasNext() {
                return currentNode != head;
            }

            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("нет следующего элемента");
                }

                FunctionPoint point = currentNode.getPoint();
                currentNode = currentNode.getNext();

                // возвращаем копию для защиты инкапсуляции
                return new FunctionPoint(point);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("удаление не поддерживается");
            }
        };
    }

    // класс-фабрика для создания LinkedListTabulatedFunction
    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX, rightX, values);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new LinkedListTabulatedFunction(points);
        }
    }
}