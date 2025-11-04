package functions;
import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {
    private static final double E = 1e-10;
    private FunctionNode head;
    private FunctionNode lastNode;
    private int lastindex = 0;
    private int size;

    private static class FunctionNode {
        private FunctionPoint point;
        private FunctionNode next = null;
        private FunctionNode prev = null;

        public FunctionNode() {
            point = new FunctionPoint();
        }

        public FunctionNode(double x, double y) {
            point = new FunctionPoint(x, y);
        }

        public FunctionNode(FunctionNode node) {
            point = new FunctionPoint(node.point);
        }

        public FunctionNode(FunctionPoint point) {
            this.point = new FunctionPoint(point);
        }

        public FunctionPoint getPoint() {
            return point;
        }

        public FunctionNode getNext() {
            return next;
        }

        public FunctionNode getPrev() {
            return prev;
        }

        public void setPoint(FunctionPoint point) {
            this.point = point;
        }

        public void setNext(FunctionNode next) {
            this.next = next;
        }

        public void setPrev(FunctionNode prev) {
            this.prev = prev;
        }
    }

    private FunctionNode getNodeByIndex(int index) {
        FunctionNode node;
        if (Math.abs(index - lastindex) <= Math.min(index, size - 1 - index)) {
            node = lastNode;
            if (index > lastindex)
                for (int i = lastindex; i < index; i++)
                    node = node.next;
            else
                for (int i = lastindex; i > index; i--)
                    node = node.prev;
        }
        else
            if (index <= size - 1 - index) {
            node = head;
            for (int i = 0; i < index; i++)
                node = node.next;
            }
            else {
            node = head.prev;
            for (int i = size - 1; i > index; i--)
                node = node.prev;
        }
        lastindex = index;
        lastNode = node;
        return node;
    }

    private FunctionNode addNodeToTail(FunctionNode node) {
        FunctionNode tail = head.prev;
        head.prev = new FunctionNode(node);
        tail.next = head.prev;
        tail.next.prev = tail;
        tail.next.next = head;
        ++size;
        return tail.next;
    }

    private FunctionNode addNodeByIndex(int index, FunctionNode node) {
        if (index == size)
            addNodeToTail(node);
        FunctionNode nextnode = getNodeByIndex(index);
        nextnode.prev.next = new FunctionNode(node);
        nextnode.prev.next.next = nextnode;
        nextnode.prev.next.prev = nextnode.prev;
        nextnode.prev = nextnode.prev.next;
        if (index == 0)
            head = head.prev;
        ++size;
        return nextnode.prev;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        FunctionNode node = getNodeByIndex(index);
        node.next.prev = node.prev;
        node.prev.next = node.next;
        if (index == 0)
            head = head.next;
        --size;
        lastindex = 0;
        lastNode = head;
        return node;
    }

    public LinkedListTabulatedFunction() {}

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX - E)
            throw new IllegalArgumentException("Левая граница больше или равна правой границе");
        if (pointsCount < 2)
            throw new IllegalArgumentException("Точек меньше двух");
        double step = (rightX - leftX) / (pointsCount - 1);
        head = new FunctionNode(leftX, 0);
        head.next = head;
        head.prev = head;
        ++size;
        leftX += step;
        for(int i = 1; i != pointsCount; ++i, leftX += step)
            addNodeToTail(new FunctionNode(leftX, 0));
        lastNode = head;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX - E)
            throw new IllegalArgumentException("Левая граница больше или равна правой границе");
        if (values.length < 2)
            throw new IllegalArgumentException("Точек меньше двух");
        int pointsCount = values.length;
        double step = (rightX - leftX) / (pointsCount - 1);
        head = new FunctionNode(leftX, values[0]);
        head.next = head;
        head.prev = head;
        ++size;
        leftX += step;
        for (int i = 1; i != pointsCount; ++i, leftX += step)
            addNodeToTail(new FunctionNode(leftX, values[i]));
        lastNode = head;
    }

    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        if (points == null)
            throw new IllegalArgumentException("Точек меньше двух");
        if (points.length < 2)
            throw new IllegalArgumentException("Точек меньше двух");
        for (int i = 1; i < points.length; ++i) {
            if (points[i - 1].getX() >= points[i].getX())
                throw new IllegalArgumentException("Точки не упорядоченные");
        }
        head = new FunctionNode(points[0]);
        head.next = head;
        head.prev = head;
        ++size;
        for (int i = 1; i < points.length; ++i)
            addNodeToTail(new FunctionNode(points[i]));
        lastNode = head;
    }

    public double getLeftDomainBorder() {
        return head.point.getX();
    }

    public double getRightDomainBorder() {
        return head.prev.point.getX();
    }
    
    public double getFunctionValue(double x) {
        if (x >= getLeftDomainBorder() - E && x <= getRightDomainBorder() + E) {
            FunctionNode node = head;
            for (int i = 0; i < size; ++i, node = node.next) {
                if (Math.abs(x - node.point.getX()) < E)
                    return node.point.getY();
                if (i < size - 1 && x > node.point.getX() && x < node.next.point.getX()) {
                    double x1 = node.point.getX();
                    double y1 = node.point.getY();
                    double x2 = node.next.point.getX();
                    double y2 = node.next.point.getY();
                    if (Double.isInfinite(y1) || Double.isInfinite(y2) || Double.isNaN(y1) || Double.isNaN(y2))
                        return Double.NaN;
                    return ((x - x1) * (y2 - y1) / (x2 - x1)) + y1;
                }
            }
            if (Math.abs(x - getRightDomainBorder()) < E) {
                return head.prev.point.getY();
            }
        }
        return Double.NaN;
    }

    public int getPointsCount() {
        return size;
    }

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        if ((index == 0 || index == size - 1)) {
            if (Math.abs(getNodeByIndex(index).point.getX() - point.getX()) > E)
                throw new InappropriateFunctionPointException("x выходит за границы определения функции");
            getNodeByIndex(index).point = new FunctionPoint(point);
        }
        else
            if (point.getX() > getNodeByIndex(index).prev.point.getX() + E && point.getX() < getNodeByIndex(index).next.point.getX() - E) {
                getNodeByIndex(index).point = new FunctionPoint(point);
            }
            else
                throw new InappropriateFunctionPointException("x выходит за границы определения соседних точек");
    }

    public double getPointX(int index) {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        return getNodeByIndex(index).point.getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        if (index == 0 || index == size - 1 || x <= getNodeByIndex(index).prev.point.getX() || x >= getNodeByIndex(index).next.point.getX()) 
            throw new InappropriateFunctionPointException("x выходит за границы определения соседних точек");
        getNodeByIndex(index).point.setX(x);
    }

    public double getPointY(int index) {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        return getNodeByIndex(index).point.getY();
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        getNodeByIndex(index).point.setY(y);
    }

    public void deletePoint(int index) {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        if (size < 3)
            throw new IllegalStateException("Кол-во элементов меньше трех");
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int index = 0;
        if (point.getX() > head.prev.point.getX() + E)
            index = size;
        else {
            if (Math.abs(point.getX() - getNodeByIndex(index).point.getX()) < E)
                    throw new InappropriateFunctionPointException("Точка c такой координатой  x определена");
            while (point.getX() >= getNodeByIndex(index).point.getX() - E) {
                if (Math.abs(point.getX() - getNodeByIndex(index).point.getX()) < E)
                    throw new InappropriateFunctionPointException("Точка c такой координатой  x определена");
                ++index;
            }
        }
        addNodeByIndex(index, new FunctionNode(point));
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(size);
        FunctionNode node = head;
        for (int i = 0; i < size; ++i, node = node.next) {
            out.writeObject(node.point);
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int pointsCount = in.readInt();
        head = new FunctionNode((FunctionPoint) in.readObject());
        head.next = head.prev = head;
        for (int i = 1; i < pointsCount; ++i)
            addNodeToTail(new FunctionNode((FunctionPoint) in.readObject()));
        lastNode = head;
    }

    public String toString() {
        String s = "";
        FunctionNode node = head;
        for (int i = 0; i < size - 1; ++i, node = node.next)
            s += node.point.toString() + ", ";
        s += node.point.toString();
        return "{" + s + "}";
    }

    public boolean equals(Object o) {
        if (o instanceof LinkedListTabulatedFunction && size == ((LinkedListTabulatedFunction) o).size) {
            FunctionNode node = ((LinkedListTabulatedFunction) o).head;
            if (!node.point.equals(head.point)) return false;
            node = node.next;
            for (FunctionNode nodeThis = head.next; nodeThis != head; nodeThis = nodeThis.next, node = node.next)
                if (!nodeThis.point.equals(node.point))
                    return false;
            return true;
        }
        else if (o instanceof TabulatedFunction && size == ((TabulatedFunction) o).getPointsCount()) {
            FunctionNode node = head;
            for (int i = 0; i < size; ++i, node = node.next)
                if (!node.point.equals(((TabulatedFunction) o).getPoint(i)))
                    return false;
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hash = size;
        hash ^= head.point.hashCode();
        for (FunctionNode node = head.next; node != head; node = node.next)
            hash ^= node.point.hashCode();
        return hash;
    }

    public Object clone() {
        FunctionPoint[] points = new FunctionPoint[size];
        FunctionNode node = head;
        for (int i = 0; i < size; ++i, node = node.next)
            points[i] = (FunctionPoint) node.point.clone();
        return new LinkedListTabulatedFunction(points);
    }

    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private FunctionNode node = head;
            private int index = 0;

            public boolean hasNext() {
                return index < size;
            }

            public FunctionPoint next() {
                if (hasNext()) {
                    FunctionPoint point = new FunctionPoint(node.point);
                    ++index;
                    node = node.next;
                    return point;
                }
                throw new NoSuchElementException("Нет больше точек");
            }

            public void remove() {
                throw new UnsupportedOperationException("Нельзя удалить элемент");
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