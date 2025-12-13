package functions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction implements TabulatedFunction, Serializable {
    private static class FunctionNode implements Serializable {
        private FunctionPoint point;
        private FunctionNode prev;
        private FunctionNode next;
        
        public FunctionNode(FunctionPoint point) {
            this.point = point;
        }
    }
    
    private FunctionNode head;
    private int pointsCount;
    private static final double EPSILON = 1e-9;
    
    private void initializeList() {
        head = new FunctionNode(null);
        head.prev = head;
        head.next = head;
        pointsCount = 0;
    }
    
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс");
        }
        
        FunctionNode currentNode;
        
        
        if (index < pointsCount - index) {
            
            currentNode = head.next;
            for (int i = 0; i < index; i++) {
                currentNode = currentNode.next;
            }
        } else {
            
            currentNode = head.prev;
            for (int i = pointsCount - 1; i > index; i--) {
                currentNode = currentNode.prev;
            }
        }
        
        return currentNode;
    }
    
    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode(null);
        FunctionNode tail = head.prev;
        
        tail.next = newNode;
        newNode.prev = tail;
        newNode.next = head;
        head.prev = newNode;
        
        pointsCount++;
        return newNode;
    }
    
    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс");
        }
        
        if (index == pointsCount) {
            
            return addNodeToTail();
        }
        
        FunctionNode currentNode = getNodeByIndex(index);
        FunctionNode prevNode = currentNode.prev;
        FunctionNode newNode = new FunctionNode(null);
        
        prevNode.next = newNode;
        newNode.prev = prevNode;
        newNode.next = currentNode;
        currentNode.prev = newNode;
        
        pointsCount++;
        return newNode;
    }
    
    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс");
        }
        
        if (pointsCount <= 2) {
            throw new IllegalStateException("Невозможно удалить точку, требуется минимум 2");
        }
        
        FunctionNode nodeToDelete = getNodeByIndex(index);
        FunctionNode prevNode = nodeToDelete.prev;
        FunctionNode nextNode = nodeToDelete.next;
        
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
        
        pointsCount--;
        return nodeToDelete;
    }
    
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Кол-во точек меньше 2");
        }
        
        initializeList();
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            addNodeToTail().point = new FunctionPoint(x, 0);
        }
    }
    
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Кол-во точек меньше 2");
        }
        
        initializeList();
        double step = (rightX - leftX) / (values.length - 1);
        
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            addNodeToTail().point = new FunctionPoint(x, values[i]);
        }
    }
    
    // Конструктор 3: 
    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("Кол-во точек меньше 2");
        }
    
        // Проверяем упорядоченность точек по X
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i - 1].getX()) {
                throw new IllegalArgumentException("Точки не упорядочены по значению X");
            }
        }
    
        initializeList();
    
    
        for (FunctionPoint point : points) {
            addNodeToTail().point = new FunctionPoint(point);
        }
    }
    
  
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
    
    
    @Override
    public double getLeftDomainBorder() {
        if (pointsCount == 0) {
            return Double.NaN;
        }
        return head.next.point.getX();
    }
    
    @Override
    public double getRightDomainBorder() {
        if (pointsCount == 0) {
            return Double.NaN;
        }
        return head.prev.point.getX();
    }
    
    
    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder())
            return Double.NaN;

        FunctionNode cur = head.next;
        while (cur.next != head) {
            double x1 = cur.point.getX();
            double x2 = cur.next.point.getX();
        
        
            if (Math.abs(x - x1) < EPSILON) 
                return cur.point.getY();
            if (Math.abs(x - x2) < EPSILON) 
                return cur.next.point.getY();
        
            if (x >= x1 && x <= x2) {
                double y1 = cur.point.getY();
                double y2 = cur.next.point.getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            cur = cur.next;
        }
        return Double.NaN; 
    }
    
    @Override
    public int getPointsCount() {
        return pointsCount;
    }
    
    @Override
    public FunctionPoint getPoint(int index) {
        return new FunctionPoint(getNodeByIndex(index).point);
    }
    
    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException{
        FunctionNode node = getNodeByIndex(index);
        
        if (index > 0 && point.getX() <= getNodeByIndex(index - 1).point.getX()) {
            throw new InappropriateFunctionPointException("Координата точки X должна быть больше, чем координата предыдущей точки X");
        }
        if (index < pointsCount - 1 && point.getX() >= getNodeByIndex(index + 1).point.getX()) {
            throw new InappropriateFunctionPointException("Координата точки X должна быть меньше координаты X следующей точки");
        }
        
        node.point = new FunctionPoint(point);
    }
    
    @Override
    public double getPointX(int index) {
        return getNodeByIndex(index).point.getX();
    }
    
    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        
        
        if (index > 0 && x <= getNodeByIndex(index - 1).point.getX()) {
            throw new InappropriateFunctionPointException("Координата точки X должна быть больше, чем координата предыдущей точки X");
        }
        if (index < pointsCount - 1 && x >= getNodeByIndex(index + 1).point.getX()) {
            throw new InappropriateFunctionPointException("Координата точки X должна быть меньше координаты X следующей точки");
        }
        
        node.point.setX(x);
    }
    
    @Override
    public double getPointY(int index) {
        return getNodeByIndex(index).point.getY();
    }
    
    @Override
    public void setPointY(int index, double y) {
        getNodeByIndex(index).point.setY(y);
    }
    
    @Override
    public void deletePoint(int index) {
        if (pointsCount <= 2) throw new IllegalStateException("Точек меньше или равно 2");
        deleteNodeByIndex(index);
    }
    
    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        
        int insertIndex = 0;
        FunctionNode current = head.next;
        
        while (current != head && point.getX() > current.point.getX()) {
            current = current.next;
            insertIndex++;
        }
        
        
        if (current != head && Math.abs(point.getX() - current.point.getX()) < EPSILON) {
            throw new InappropriateFunctionPointException("Такая точка с координатой X уже существует");
        }
        
        
        FunctionNode newNode = addNodeByIndex(insertIndex);
        newNode.point = new FunctionPoint(point);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("{");
        // Начинаем с первой реальной точки (после head)
        FunctionNode current = head.next;
        
        // Проходим по всем узлам списка до возврата к head
        while (current != head) {
            // Добавляем точку в формате (x; y)
            sb.append("(").append(current.point.getX()).append("; ").append(current.point.getY()).append(")");
            
            // Переходим к следующему узлу
            current = current.next;
            // Если это не последняя точка, добавляем запятую и пробел
            if (current != head) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction other = (LinkedListTabulatedFunction) o;
            
            if (this.pointsCount != other.pointsCount) return false;
            
            FunctionNode currentThis = this.head.next;
            FunctionNode currentOther = other.head.next;
            
            while (currentThis != this.head) {
                if (!currentThis.point.equals(currentOther.point)) {
                    return false;
                }
                currentThis = currentThis.next;
                currentOther = currentOther.next;
            }
            return true;
        }
        if (o instanceof TabulatedFunction) {
            TabulatedFunction other = (TabulatedFunction) o;
            if (this.getPointsCount() != other.getPointsCount()) return false;
            FunctionNode current = this.head.next;
            int index = 0;
            while (current != this.head) {
                FunctionPoint otherPoint = other.getPoint(index);
                if (!current.point.equals(otherPoint)) {
                    return false;
                }
                current = current.next;
                index++;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = pointsCount;
        FunctionNode current = head.next;
        
        // Проходим по всем узлам списка
        while (current != head) {
            // Комбинируем хэш-код текущей точки с общим хэш-кодом через XOR
            hash ^= current.point.hashCode();
            // Переходим к следующему узлу
            current = current.next;
        }
        return hash;
    }
    
    @Override
    public Object clone() {
        FunctionPoint[] clonePoints = new FunctionPoint[pointsCount];
        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount; i++) {
            clonePoints[i] = (FunctionPoint) current.point.clone();
            current = current.next;
        }
        return new LinkedListTabulatedFunction(clonePoints);
    }
    
    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private FunctionNode currentNode = head.next;
            private int currentIndex = 0;
            
            @Override
            public boolean hasNext() {
                return currentIndex < pointsCount;
            }
            
            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Нет следующего элемента");
                }
                FunctionPoint result = new FunctionPoint(currentNode.point);
                currentNode = currentNode.next;
                currentIndex++;
                return result;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException("Удаление не поддерживается");
            }
        };
    }
}