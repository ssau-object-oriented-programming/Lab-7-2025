package functions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Класс табулированной функции... неясно, для чего
 */
public class LinkedListTabulatedFunction implements TabulatedFunction {
    private LinkedList points;
    /**
     * Конструктор табулированной функции по самим точкам
     * @param points точки данной функции
     */
    public LinkedListTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
        if (points.length < 2) throw new IllegalArgumentException("Points count must be greater than 2");
        for (int i = points.length - 1; i > 0; i--) {
            if (points[i].getX() <= points[i-1].getX()) throw new IllegalArgumentException("Illegal points(x coordinate)");
        }
        this.points = new LinkedList();
        for (FunctionPoint point : points) {
            this.points.addToEnd(new FunctionPoint(point));
        }
    }
    /**
     * Конструктор табулированной функции по кол-ву точек
     * @param leftX левая граница <s>страданий</s> по значению X
     * @param rightX правая граница <s>страданий</s> по значению X
     * @param pointsCount кол-во точек данной табулированной функции
     */
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (pointsCount < 2) throw new IllegalArgumentException("Points count must be greater than 2");
        if (leftX >= rightX) throw new IllegalArgumentException("LeftX border must be lower than RightX border");
        this.points = new LinkedList();
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i <= pointsCount - 1; i++) {
            this.points.addToEnd(new FunctionPoint(leftX + i * step, 0));
        }
    }
    /**
     * Конструктор табулированной функции по заданным значениям Y(массив)
     * @param leftX левая граница <s>страданий</s> по значению X
     * @param rightX правая граница <s>страданий</s> по значению X
     * @param values точки данной табулированной функции
     */
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {
        this(leftX, rightX, values.length);
        for (int i = 0; i <= values.length - 1; i++) {
            FunctionPoint p = points.get(i);
            p.setY(values[i]);
            this.points.set(i, p);
        }
    }
    @Override
    public double getLeftDomainBorder() {
        return (this.points.getSize() > 0) ? (this.points.get(0).getX()) : (Double.NaN);
    }
    @Override
    public double getRightDomainBorder() {
        return (this.points.getSize() > 0) ? (this.points.get(this.points.getSize() - 1).getX()) : (Double.NaN);
    }
    @Override
    public double getFunctionValue(double x) {
        if (x < this.getLeftDomainBorder() || this.getRightDomainBorder() < x) return Double.NaN;
        int index;
        for (index = this.points.getSize() - 2; index >= 0; index--) { // нет смысла проверять правую точку, скипаем автоматом
            if (this.points.get(index).getX() < x) break;
            if (this.points.get(index).getX() == x) return this.points.get(index).getY(); // нет смысла интерполировать
        } // здесь используется функция (y1-y0)/(x1-x0)*(x-x0) + y0
        return (this.points.get(index+1).getY() - this.points.get(index).getY()) /
                (this.points.get(index+1).getX() - this.points.get(index).getX()) *
                (x - this.points.get(index).getX()) + this.points.get(index).getY();
    }
    @Override
    public int getPointsCount() { return this.points.getSize(); }
    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.points.getSize()) throw new FunctionPointIndexOutOfBoundsException();
        return new FunctionPoint(this.points.get(index));
    }
    @Override
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= this.points.getSize()) throw new FunctionPointIndexOutOfBoundsException();
        double leftX = (index == 0) ? (Double.NEGATIVE_INFINITY) : (this.points.get(index-1).getX());
        double rightX = (index == this.points.getSize() - 1) ? (Double.POSITIVE_INFINITY) : (this.points.get(index+1).getX());
        if (leftX >= point.getX() || point.getX() >= rightX) throw new InappropriateFunctionPointException();
        this.points.set(index, new FunctionPoint(point));
    }
    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.points.getSize()) throw new FunctionPointIndexOutOfBoundsException();
        return this.points.get(index).getX();
    }

    @Override
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= this.points.getSize()) throw new FunctionPointIndexOutOfBoundsException();
        this.points.get(index).setX(x);
    }
    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.points.getSize()) throw new FunctionPointIndexOutOfBoundsException();
        return this.points.get(index).getY();
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= this.points.getSize()) throw new FunctionPointIndexOutOfBoundsException();
        this.points.get(index).setY(y);
    }

    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (index < 0 || index >= this.points.getSize()) throw new FunctionPointIndexOutOfBoundsException();
        if (this.points.getSize() < 3) throw new IllegalStateException("Points count must be greater than 2 for deletion");
        this.points.remove(index);
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double x = point.getX();
        int index = 0;
        for (; index < this.points.getSize(); index++) {
            if (this.points.get(index).getX() == x) throw new InappropriateFunctionPointException();
            if (this.points.get(index).getX() > x) break;
        }
        this.points.insert(index - 1, new FunctionPoint(point));
    }
    public static class LinkedList implements Serializable {
        private Node head = null;
        private Node tail = null;
        private int size = 0;
        public void addToEnd(FunctionPoint data) {
            if (head == null) {
                head = new Node(data);
                tail = head;
                this.size++;
                return;
            }
            this.tail.setNext(new Node(data));
            Node temp = this.tail;
            this.tail = this.tail.getNext();
            this.tail.setPrev(temp);
            this.size++;
        }
        private Node getNode(int index) {
            if (index < 0 || index >= this.size) throw new IndexOutOfBoundsException();
            if (this.size / 2 >= index) {
                Node temp = head;
                while (index > 0) {
                    temp = temp.getNext();
                    index--;
                }
                return temp;
            }
            Node temp = this.tail;
            index = this.size - index - 1;
            while (index > 0) {
                temp = temp.getPrev();
                index--;
            }
            return temp;
        }
        public void remove(int index) {
            if (index >= this.size || index < 0) throw new IndexOutOfBoundsException();
            if (this.size == 1) {
                this.head.onDelete();
                this.head = null;
                this.tail = null;
                this.size--;
                return;
            }
            if (index == 0) {
                this.head = this.head.getNext();
                this.head.getPrev().onDelete();
                this.head.setPrev(null);
                this.size--;
                return;
            }
            if (index == this.size - 1) {
                this.tail = this.tail.getPrev();
                this.tail.getNext().onDelete();
                this.tail.setNext(null);
                this.size--;
                return;
            }
            Node temp = this.getNode(index);
            temp.getPrev().setNext(temp.getNext());
            temp.getNext().setPrev(temp.getPrev());
            temp.onDelete();
            this.size--;
        }
        public void set(int index, FunctionPoint data) {
            if (index >= this.size || index < 0) throw new IndexOutOfBoundsException();
            Node temp = this.getNode(index);
            temp.set(data);
        }
        public void insert(int index, FunctionPoint data) {
            if (index >= this.size || index < -1) throw new IndexOutOfBoundsException();
            if (this.size == 0) { this.addToEnd(data); return; }
            if (index == -1) {
                Node t = this.head;
                this.head = new Node(data);
                this.head.setNext(t);
                t.setPrev(this.head);
                this.size++;
                return;
            }
            if (index == this.size - 1) {
                Node t = this.tail;
                this.tail = new Node(data);
                this.tail.setPrev(t);
                t.setNext(this.tail);
                this.size++;
                return;
            }
            Node temp = this.getNode(index);
            Node newNode = new Node(data);
            temp.getNext().setPrev(newNode);
            newNode.setNext(temp.getNext());
            temp.setNext(newNode);
            newNode.setPrev(temp);
            this.size++;
        }
        public FunctionPoint get(int index) {
            if (index >= this.size || index < 0) throw new IndexOutOfBoundsException();
            return this.getNode(index).get();
        }
        public int getSize() {
            return this.size;
        }
        @Override
        public String toString() {
            if (this.size == 0) return "[]";
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            Node temp = this.head;
            while (temp != null) {
                sb.append(temp.get());
                sb.append(",");
                temp = temp.getNext();
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
            return sb.toString();
        }

        private static class Node implements Serializable {
            private FunctionPoint data;
            private Node next;
            private Node prev;
            private Node(FunctionPoint data) {
                this.data = data;
            }
            private Node(FunctionPoint data, Node prev) {
                this.data = data;
                this.prev = prev;
                this.next = this.prev.next;
                this.prev.next = this;
            }
            private FunctionPoint get() {
                return this.data;
            }
            private void set(FunctionPoint data) {
                this.data = data;
            }
            private Node getNext() {
                return this.next;
            }
            private void setNext(Node next) {
                this.next = next;
            }
            private Node getPrev() {
                return this.prev;
            }
            private void setPrev(Node prev) {
                this.prev = prev;
            }
            private void onDelete() {
                this.prev = null;
                this.next = null;
                this.data = null;
            }
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < this.points.getSize(); i++) {
            sb.append(this.points.get(i));
            if (i < this.points.getSize() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TabulatedFunction other)) return false;
        if (this.getPointsCount() != other.getPointsCount()) return false;
        for (int i = 0; i < this.points.getSize(); i++) {
            if (!this.points.get(i).equals(other.getPoint(i))) return false;
        }
        return true;
    }
    @Override
    public int hashCode() {
        int base = 777;
        for (int i = 0; i < this.getPointsCount(); i++) {
            base ^= this.points.get(i).hashCode();
        }
        return base;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        FunctionPoint[] points = new FunctionPoint[this.points.getSize()];
        for (int i = 0; i < this.points.getSize(); i++) {
            points[i] = this.points.get(i);
        }
        return new LinkedListTabulatedFunction(points);
    } // благодаря тому, что стоит инкапсуляция - можно не волноваться
    // за "общие" ссылки у двух разных объектов данного класса. При этом
    // массив points самоуничтожится, с объектами ничего не случится(они останутся у
    // "донора")
    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            LinkedList.Node node = points.head;
            @Override
            public boolean hasNext() {
                return (node != null) && (node.next != null);
            }
            @Override
            public FunctionPoint next() {
                if (!hasNext()) throw new NoSuchElementException();
                FunctionPoint point = node.next.get();
                node = node.getNext();
                return new FunctionPoint(point);
            }
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {
        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
            return new LinkedListTabulatedFunction(points);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {
            return new LinkedListTabulatedFunction(leftX, rightX, values);
        }
    }
}
