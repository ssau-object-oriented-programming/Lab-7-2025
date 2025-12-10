package functions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction implements TabulatedFunction,  Serializable{

    private class FunctionNode {
        private FunctionPoint point;
        private FunctionNode prev;
        private FunctionNode next;
        
        public FunctionNode(FunctionPoint point, FunctionNode prev, FunctionNode next) {
            this.point = point;
            this.prev = prev;
            this.next = next;
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
    
    private FunctionNode head;
    private int pointsCount;
    private final double EPSILON_DOUBLE = 1e-9;
    
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс" + index + " лежит вне границ массива" + pointsCount);
        }

        FunctionNode current;

        if (index < pointsCount / 2) {
            current = head.getNext();
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else {
            current = head;
            for (int i = pointsCount; i > index; i--) {
                current = current.getPrev();
            }
        }

        return current;
    }
    
    private FunctionNode addNodeToTail(FunctionPoint point) {
        FunctionNode newNode = new FunctionNode(point, head.getPrev(), head);
        FunctionNode tail = head.getPrev();
        tail.setNext(newNode);
        head.setPrev(newNode);
        return newNode;
    }

    private FunctionNode addNodeByIndex(int index, FunctionPoint point) {
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " лежит вне границ массива " + pointsCount);
        }
        if (index == pointsCount) {
            pointsCount++;
            return addNodeToTail(point);
        }
        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode prevNode = nextNode.getPrev();
        FunctionNode newNode = new FunctionNode(point, prevNode, nextNode);
        prevNode.setNext(newNode);
        nextNode.setPrev(newNode);
        pointsCount++;
        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " лежит вне границ массива " + pointsCount);
        }
        FunctionNode nodeToDelete = getNodeByIndex(index);
        FunctionNode prevNode = nodeToDelete.getPrev();
        FunctionNode nextNode = nodeToDelete.getNext();
        prevNode.setNext(nextNode);
        nextNode.setPrev(prevNode);
        pointsCount--;
        return nodeToDelete;
    }
    
    public LinkedListTabulatedFunction(FunctionPoint[] points){
        if (points.length < 2) {
            throw new IllegalStateException("В массиве не может быть только одна точка");
        }
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() < points[i - 1].getX()){
                throw new IllegalStateException("Массив не упорядочен по координатам X");
            }
        }
        pointsCount = points.length;
        head = new FunctionNode(null, null, null);
        head.setNext(head);
        head.setPrev(head);
        for (int i = 0; i < points.length; i++) {
            addNodeToTail(new FunctionPoint(points[i]));
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount){
        this.pointsCount = pointsCount;
        if (leftX >= rightX) {
            throw new IllegalStateException("Левая граница больше или равна правой");
        }
        if (pointsCount < 2) {
            throw new IllegalStateException("В массиве не может быть только одна точка");
        }
        head = new FunctionNode(null, null, null);
        head.setNext(head);
        head.setPrev(head);
        double step = (rightX - leftX)/(pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            addNodeToTail(new FunctionPoint(leftX + step * i, 0));
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values){
        this.pointsCount = values.length;
        if (leftX >= rightX) {
            throw new IllegalStateException("Левая граница больше или равна правой");
        }
        if (pointsCount < 2) {
            throw new IllegalStateException("В массиве не может быть только одна точка");
        }
        head = new FunctionNode(null, null, null);
        head.setNext(head);
        head.setPrev(head);
        double step = (rightX - leftX)/(pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            addNodeToTail(new FunctionPoint(leftX + step * i,  values[i]));
        }
    }


    @Override
    public double getLeftDomainBorder() {
        return head.getNext().getPoint().getX();
    }
    

    @Override
    public double getRightDomainBorder() {
        return head.getPrev().getPoint().getX();
    }


    @Override
    public double getFunctionValue(double x) {
        if (x <  getLeftDomainBorder()|| x > getRightDomainBorder()) {
            return Double.NaN;
        }
        if (Math.abs(x - getLeftDomainBorder()) < EPSILON_DOUBLE) {
            return head.getNext().getPoint().getY();
        }
        if (Math.abs(x  - getRightDomainBorder()) < EPSILON_DOUBLE) {
            return head.getPrev().getPoint().getY();
        }
        else {
            int i;
            double value = 0;
            for (i = 0; x >= getNodeByIndex(i).getPoint().getX(); i++){
                FunctionNode Node = getNodeByIndex(i);
                if (Math.abs(x - Node.getPoint().getX()) < EPSILON_DOUBLE){
                   value = Node.getPoint().getY(); 
                }
                else{
                    value = Node.getPoint().getY() + (Node.getNext().getPoint().getY() - Node.getPoint().getY())*(x - Node.getPoint().getX())/(Node.getNext().getPoint().getX() - Node.getPoint().getX());
                }
            }
            return value;
        }
    }

    @Override
    public int getPointsCount() {
        return pointsCount;
    }
    
    @Override
    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Получить точку с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        return new FunctionPoint(getNodeByIndex(index).getPoint().getX(), getNodeByIndex(index).getPoint().getY());
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Задать точку с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        FunctionNode currentNode = getNodeByIndex(index);
        if (index == 0) {
            if (point.getX() > getRightDomainBorder()) {
                throw new InappropriateFunctionPointException("Новая точка X (" + point.getX() + ") выходит за границы (больше правой границы).");
            }
        } else if (index == pointsCount - 1) {
            if (point.getX() < getLeftDomainBorder()) {
                throw new InappropriateFunctionPointException("Новая точка X (" + point.getX() + ") выходит за границы (меньше левой границы).");
            }
        }
        else {
            if (point.getX() < currentNode.getPrev().getPoint().getX() || point.getX() > currentNode.getNext().getPoint().getX()) {
                throw new InappropriateFunctionPointException("Новая точка X (" + point.getX() + ") выходит за границы соседних к ней точек.");
            }
        }
        currentNode.setPoint(point);
    }

    @Override
    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Получить координату X с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        else{
            return getNodeByIndex(index).getPoint().getX();
        }
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Задать координату X с индексом " + index + " невозможно, так как размер массива" + this.pointsCount);
        }
        FunctionNode currentNode = getNodeByIndex(index);
        if (x <  currentNode.getPrev().getPoint().getX() || x > currentNode.getNext().getPoint().getX()) {
            throw new InappropriateFunctionPointException("Новая точка X (" + x + ") выходит за границы соседних к ней точек.");
        }
        currentNode.getPoint().setX(x);
    }

    @Override
    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Получить координату Y с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        else{
            return  getNodeByIndex(index).getPoint().getY();
        }
    }

    @Override
    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Задать координату Y с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        else{
            FunctionNode currentNode = getNodeByIndex(index);
            currentNode.getPoint().setY(y);
        }
    }
    
    @Override
    public void deletePoint(int index){
		if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Удалить точку с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        } 
        if (pointsCount < 3) {
            throw new IllegalStateException("В массиве меньше 3 точек");
        }
        deleteNodeByIndex(index); 
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (point.getX() > getNodeByIndex(pointsCount - 1).getPoint().getX()){
            addNodeByIndex(pointsCount, point);
        }
        else{
            int i = 0;
            while (point.getX() > getNodeByIndex(i).getPoint().getX()) ++i;
            FunctionNode Node = getNodeByIndex(i);
            if (Math.abs(point.getX() - Node.getPoint().getX()) < EPSILON_DOUBLE) {
                throw new InappropriateFunctionPointException("Координата X добавляемой точки совпадает с уже сужествующим X = " +  Node.getPoint().getX());
            }
            else{
                addNodeByIndex(i, point);
            } 
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        FunctionNode Node = head.getNext(); 
        sb.append("{");
        for (int i = 0; i < pointsCount; i++) {
            if (Node != null) {
                sb.append("(").append(Node.getPoint().getX()).append(" ; ").append(Node.getPoint().getY()).append(")");
                Node = Node.getNext();
            }
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
    
        if (o == null) {
            return false;
        }
    
        TabulatedFunction otherFunction = (TabulatedFunction) o;
        if (this.pointsCount != otherFunction.getPointsCount()) {
            return false;
        }

        LinkedListTabulatedFunction otherLinkedListFunction = null;
        if (o instanceof LinkedListTabulatedFunction) {
            otherLinkedListFunction = (LinkedListTabulatedFunction) o;
        }
        double epsilon = 1e-9;
    
        if (otherLinkedListFunction != null) {
            for (int i = 0; i < this.pointsCount; i++) {
                FunctionNode p1 = this.getNodeByIndex(i);
                FunctionNode p2 = otherLinkedListFunction.getNodeByIndex(i);
                if (p1 == null || p2 == null) {
                    return false;
                }
                if (Math.abs(p1.getPoint().getX() - p2.getPoint().getX()) >= epsilon || Math.abs(p1.getPoint().getY() - p2.getPoint().getY()) >= epsilon) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < this.pointsCount; i++) {
                FunctionNode p1 = this.getNodeByIndex(i);
                FunctionPoint p2 = otherFunction.getPoint(i);
                if (p1 == null || p2 == null) {
                    return false;
                }
                if (Math.abs(p1.getPoint().getX() - p2.getX()) >= epsilon || Math.abs(p1.getPoint().getY() - p2.getY()) >= epsilon) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = pointsCount;
        for (int i = 0; i < pointsCount; i++) {
            FunctionPoint point = getPoint(i);
            result = point.hashCode();
        }
        return result;
    }

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public TabulatedFunction clone(){
        try {
            LinkedListTabulatedFunction clonedFunction = (LinkedListTabulatedFunction) super.clone();
            clonedFunction.head = new FunctionNode(null, null, null);
            clonedFunction.head.setNext(clonedFunction.head);
            clonedFunction.head.setPrev(clonedFunction.head);

            FunctionNode current = this.head.getNext();
            for (int i = 0; i < this.pointsCount; i++) {
                FunctionPoint newPoint = new FunctionPoint(current.getPoint().getX(), current.getPoint().getY());
                clonedFunction.addNodeToTail(newPoint);
                current = current.getNext();
            }
            return clonedFunction;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Клонирование невозможно", e);
        }
    }

    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private FunctionNode node = head.getNext();

            @Override
            public boolean hasNext() {
                return node != head;
            }

            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Следующего элемента нет");
                }
                FunctionPoint point = new FunctionPoint(node.getPoint());
                node = node.getNext();
                return point;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Удалить элемент нельзя");
            }
        };
    }

    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory{
        
        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new LinkedListTabulatedFunction(points);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX, rightX, values);
        }
    }
}

