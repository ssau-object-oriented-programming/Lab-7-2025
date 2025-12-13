package functions;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class ExternalizableLinkedListTabulatedFunction implements TabulatedFunction, Externalizable {
    private static final long serialVersionUID = 3L;
    
    private static class FunctionNode implements Serializable {
        private static final long serialVersionUID = 1L;
        public FunctionPoint point;
        public FunctionNode prevEl;
        public FunctionNode nextEl;

        public FunctionNode(FunctionPoint point) {
            this.point = new FunctionPoint(point);
            this.prevEl = this.nextEl = null;
        }

        public FunctionNode() {
            this.point = new FunctionPoint(0, 0);
            this.prevEl = this.nextEl = null;
        }
    }

    private FunctionNode head;
    private int pointsCount;
    private FunctionNode lastNode;
    private int lastIndex;

    // Конструктор по умолчанию, необходимый для Externalizable
    public ExternalizableLinkedListTabulatedFunction() {
        initHead();
    }

    public ExternalizableLinkedListTabulatedFunction(FunctionPoint[] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        for (int i = 1; i < points.length; i++) {
            if (points[i].get_x() <= points[i-1].get_x()) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по возрастанию X.");
            }
        }
        
        initHead();
        for (FunctionPoint point : points) {
            FunctionNode newNode = addNodeToTail();
            newNode.point = new FunctionPoint(point);
        }
    }

    public ExternalizableLinkedListTabulatedFunction (double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница (" + leftX + 
                ") должна быть меньше правой границы (" + rightX + ")");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек (" + pointsCount + 
                ") должно быть не менее 2");
        }

        initHead();
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            FunctionNode newNode = addNodeToTail();
            newNode.point = new FunctionPoint(x, 0.0);
        }
    }

    public ExternalizableLinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница (" + leftX + 
                ") должна быть меньше правой границы (" + rightX + ")");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек (" + values.length + 
                ") должно быть не менее 2");
        }

        initHead();
        int pointsCount = values.length;  // Используем длину массива
        
        // Если только одна точка, шаг будет 0, что некорректно
        if (pointsCount == 1) {
            throw new IllegalArgumentException("Для интервала необходимо хотя бы 2 точки");
        }
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            FunctionNode newNode = addNodeToTail();
            newNode.point = new FunctionPoint(x, values[i]);
        }
    }

    private void initHead() {
        head = new FunctionNode(new FunctionPoint(0, 0));
        head.prevEl = head;
        head.nextEl = head;
        pointsCount = 0;
        lastNode = null;
        lastIndex = -1;
    }


    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }

        if (lastNode != null && lastIndex == index) {
            return lastNode;
        }

        if (lastNode != null && lastIndex == index - 1) {
            lastNode = lastNode.nextEl;
            lastIndex = index;
            return lastNode;
        }

        if (lastNode != null && lastIndex == index + 1) {
            lastNode = lastNode.prevEl;
            lastIndex = index;
            return lastNode;
        }

        FunctionNode current = head.nextEl;
        int currentIndex = 0;
        while (currentIndex < index) {
            current = current.nextEl;
            currentIndex++;
        }

        lastNode = current;
        lastIndex = index;
        return current;
    }

    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode(new FunctionPoint(0, 0));
        newNode.prevEl = head.prevEl;
        newNode.nextEl = head;

        head.prevEl.nextEl = newNode;
        head.prevEl = newNode;

        pointsCount++;
        lastNode = null;
        lastIndex = -1;

        return newNode;
    }

    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }

        if (index == pointsCount) {
            return addNodeToTail();
        }

        FunctionNode newNode = new FunctionNode(new FunctionPoint(0, 0));
        FunctionNode targetNode = getNodeByIndex(index);

        newNode.prevEl = targetNode.prevEl;
        newNode.nextEl = targetNode;

        targetNode.prevEl.nextEl = newNode;
        targetNode.prevEl = newNode;

        pointsCount++;
        lastNode = null;
        lastIndex = -1;

        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }

        FunctionNode nodeToDelete = getNodeByIndex(index);

        nodeToDelete.prevEl.nextEl = nodeToDelete.nextEl;
        nodeToDelete.nextEl.prevEl = nodeToDelete.prevEl;

        nodeToDelete.prevEl = null;
        nodeToDelete.nextEl = null;

        pointsCount--;
        lastNode = null;
        lastIndex = -1;

        return nodeToDelete;
    }

    public double getLeftDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return head.nextEl.point.get_x();
    }

    public double getRightDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return head.prevEl.point.get_x();
    }

    public double getFunctionValue(double x) {
        if (pointsCount == 0) return Double.NaN;

        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        FunctionNode current = head.nextEl;
        while (current != head) {
            double currentX = current.point.get_x();

            if (Math.abs(x - currentX) < 1e-10) {
                return current.point.get_y();
            }
            current = current.nextEl;
        }

        current = head.nextEl;
        while (current.nextEl != head) {
            double x1 = current.point.get_x();
            double x2 = current.nextEl.point.get_x();

            if (x > x1 && x < x2) {
                double y1 = current.point.get_y();
                double y2 = current.nextEl.point.get_y();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            current = current.nextEl;
        }
        return Double.NaN;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        FunctionNode node = getNodeByIndex(index);
        return new FunctionPoint(node.point);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }

        if (point == null) {
            throw new IllegalArgumentException("Точка не может быть null");
        }

        FunctionNode node = getNodeByIndex(index);

        if (index > 0) {
            double leftX = node.prevEl.point.get_x();
            if (point.get_x() <= leftX) {
                throw new InappropriateFunctionPointException("X должен быть больше чем у левой точки");
            }
        }
        if (index < pointsCount - 1) {
            double rightX = node.nextEl.point.get_x();
            if (point.get_x() >= rightX) {
                throw new InappropriateFunctionPointException("X должен быть меньше чем у правой точки");
            }
        }
        node.point = new FunctionPoint(point);
    }

    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        return getNodeByIndex(index).point.get_x();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }

        FunctionNode node = getNodeByIndex(index);

        if (index > 0 && x <= node.prevEl.point.get_x()) {
            throw new InappropriateFunctionPointException("X должен быть больше чем у левой точки");
        }
        if (index < pointsCount - 1 && x >= node.nextEl.point.get_x()) {
            throw new InappropriateFunctionPointException("X должен быть меньше чем у правой точки");
        }
        node.point.set_x(x);
    }

    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        return getNodeByIndex(index).point.get_y();
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        FunctionNode node = getNodeByIndex(index);
        node.point.set_y(y);
    }

    public void deletePoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }

        if (pointsCount < 3) {
            throw new IllegalStateException("Невозможно удалить точку: в наборе менее 3 точек");
        }

        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (point == null) {
            throw new IllegalArgumentException("Точка не может быть null");
        }

        FunctionNode current = head.nextEl;
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(point.get_x() - current.point.get_x()) < 1e-10) {
                throw new InappropriateFunctionPointException("Точка с таким x уже существует");
            }
            current = current.nextEl;
        }

        int insertIndex = pointsCount;
        current = head.nextEl;

        for (int i = 0; i < pointsCount; i++) {
            if (point.get_x() < current.point.get_x()) {
                insertIndex = i;
                break;
            }
            current = current.nextEl;
        }

        FunctionNode newNode = addNodeByIndex(insertIndex);
        newNode.point = new FunctionPoint(point);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        // Записываем количество точек
        out.writeInt(pointsCount);
        
        // Записываем координаты каждой точки
        FunctionNode current = head.nextEl;
        while (current != head) {
            out.writeDouble(current.point.get_x());
            out.writeDouble(current.point.get_y());
            current = current.nextEl;
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int numPoints = in.readInt();
        // Инициализируем структуру
        initHead();
        for (int i = 0; i < numPoints; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            FunctionNode newNode = addNodeToTail();
            newNode.point = new FunctionPoint(x, y);
        }
    }


    public void printDebugInfo() {
    System.out.println("Debug Info for ExternalizableLinkedListTabulatedFunction:");
    System.out.println("pointsCount = " + pointsCount);
    System.out.println("head != null: " + (head != null));
    
    if (head != null) {
        System.out.println("head.nextEl != head: " + (head.nextEl != head));
        System.out.println("head.prevEl != head: " + (head.prevEl != head));
        
        // Выведем все точки
        System.out.println("Points:");
        FunctionNode current = head.nextEl;
        int i = 0;
        while (current != head && i < pointsCount) {
            System.out.printf("  [%d] x=%.4f, y=%.4f%n", 
                i, current.point.get_x(), current.point.get_y());
            current = current.nextEl;
            i++;
        }
    }
}
    public ExternalizableLinkedListTabulatedFunction clone() {
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        FunctionNode current = head.nextEl;
        for (int i = 0; i < pointsCount; i++) {
            points[i] = new FunctionPoint(current.point);
            current = current.nextEl;
        }
        return new ExternalizableLinkedListTabulatedFunction(points);
    }

    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private FunctionNode currentNode = head.nextEl;
            private boolean started = false;
            
            public boolean hasNext() {
                return currentNode != head && (!started || currentNode != head.nextEl);
            }
            
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements in the tabulated function");
                }
                
                FunctionPoint point = new FunctionPoint(currentNode.point);
                currentNode = currentNode.nextEl;
                started = true;
                return point;
            }
            
            public void remove() {
                throw new UnsupportedOperationException("Remove operation is not supported");
            }
        };
    }

     public static class ExternalizableLinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {
        
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ExternalizableLinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }
        
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ExternalizableLinkedListTabulatedFunction(leftX, rightX, values);
        }
        
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ExternalizableLinkedListTabulatedFunction(points);
        }
    }
    



}
