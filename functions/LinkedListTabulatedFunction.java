package functions;

import static functions.FunctionPoint.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable{

    int size; // Размер списка // был private

    FunctionNode head; // Голова списка // был private
    FunctionNode tail; // Хвост списка // был private

    double leftDomainBorder, rightDomainBorder; // Левая и правая границы // был private




    public LinkedListTabulatedFunction() { // Конструктор по умолчанию для Externalizable
        // Инициализация по умолчанию
        this.size = 0;
        this.head = null;
        this.tail = null;
        this.leftDomainBorder = 0;
        this.rightDomainBorder = 0;
    }



    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException{
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая ганица не может быть больше или равной правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек не может быть меньше двух.");
        }

        this.leftDomainBorder = leftX;
        this.rightDomainBorder = rightX;
        this.size = pointsCount;

        head = new FunctionNode(new FunctionPoint(leftX, 0));
        FunctionNode currNode = head;

        double delta = (rightX - leftX) / (pointsCount - 1);
        for (int i = 1; i < pointsCount - 1; i++){
                currNode.setNext(new FunctionNode(new FunctionPoint(leftX+delta*i, 0)));
                currNode.getNext().setPrev(currNode);
                currNode = currNode.getNext();}
        tail = new FunctionNode(new FunctionPoint(rightX, 0));
        currNode.setNext(tail);
        tail.setPrev(currNode);
}

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException{
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая ганица не может быть больше или равной правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек не может быть меньше двух.");
        }
        this.leftDomainBorder = leftX;
        this.rightDomainBorder = rightX;
        this.size = values.length;

        head = new FunctionNode(new FunctionPoint(leftX, values[0]));
        FunctionNode currNode = head;

        double delta = (rightX - leftX) / (size - 1);
        for (int i = 1; i < size - 1; i++) {
            currNode.setNext(new FunctionNode(new FunctionPoint(leftX + delta * i, values[i])));
            currNode.getNext().setPrev(currNode);
            currNode = currNode.getNext();
        }
        tail = new FunctionNode(new FunctionPoint(rightX, values[size-1]));
        currNode.setNext(tail);
        tail.setPrev(currNode);
    }

    public LinkedListTabulatedFunction(FunctionPoint[] newPoints){
        
        if (newPoints.length < 2) {
            throw new IllegalArgumentException("В массиые не может быть меньше двух точек");
        }

        for (int i = 0; i < newPoints.length - 1; i++) {
            if (largeThan(newPoints[i].getCoorX(), newPoints[i + 1].getCoorX())
                    || equal(newPoints[i].getCoorX(), newPoints[i + 1].getCoorX())) {
                throw new IllegalArgumentException("В массиве точек нарушен порядок");
            }


        }

        head = new FunctionNode(newPoints[0]);
        tail = head;

        for (int i = 1; i < newPoints.length; i++){
            FunctionNode newNode = new FunctionNode(newPoints[i]);
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }

        this.leftDomainBorder = newPoints[0].getCoorX();
        this.rightDomainBorder = newPoints[newPoints.length - 1].getCoorX();

        this.size = newPoints.length;
    }
    // Гет методы
    public double getLeftDomainBorder() {
        return leftDomainBorder;
    }

    public double getRightDomainBorder() {
        return rightDomainBorder;
    }


    // Метод для получения прямой по двум точкам
    private double interpolate(double x, double x1, double y1, double x2, double y2) {
        return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
    }

    // Метод для получения значения функции
    public double getFunctionValue(double x) {
        try{

            if (x < this.leftDomainBorder || x > this.rightDomainBorder) { // Если x выходит из диапозона возращаем Double.NaN
                return Double.NaN;
            }
            FunctionNode currNode = head;
            for (int i = 0; i < size - 1; ++i) { // Перебераем в цикле все отрезки интервала
                double point_x1 = currNode.getPoint().getCoorX();
                double point_y1 = currNode.getPoint().getCoorY();

                double point_x2 = currNode.getNext().getPoint().getCoorX();
                double point_y2 = currNode.getNext().getPoint().getCoorY();

                if (equal(x, point_x1)){
                    return point_y1;
                }

                if (equal(x, point_x2)){
                    return point_y2;
                }

                if ((largeThan(x, point_x1))  && largeThan(point_x2, x)) { // Если x входит в определённый отрезок
                    return interpolate(x, point_x1, point_y1, point_x2,point_y2);
                }

                currNode = currNode.getNext();
            }

            return Double.NaN;
        }catch(IllegalStateException e){
            System.out.println(e.getMessage());
            return Double.NaN;
        }
    }
    public int getPointsCount() { // Возвращает количество точек
        return size;
    }

    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException{

        if (index < 0 || index >= size) { // Если не подходит под условие выбрасываем исключение
            throw new FunctionPointIndexOutOfBoundsException("Выход за пределы массива");
        }
        try{
        FunctionNode idxNode = getNodeByIndex(index);
        FunctionPoint pointCopy = new FunctionPoint(idxNode.getPoint());                                                                                              // нарушат                                                                                         // инкапсуляцию
        return pointCopy;
        }catch(IllegalStateException e){
            throw new IllegalStateException("Ошибка получения ссылки на точку с индексом: " + index, e);
        }
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException, FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (point == null){
            throw  new InappropriateFunctionPointException("Попытка изменить точку на null");
        }
        
        if (index < 0 || index >= size) { // Если не подходит под условие выбрасываем исключение
            throw new FunctionPointIndexOutOfBoundsException("Выход за пределы массива");
        }
        try{
        FunctionNode idxNode = getNodeByIndex(index);
        FunctionPoint idxPoint = idxNode.getPoint(); // хранит ссылку на поле point в узле

        if (index > 0 &&  largeThan(idxNode.getPrev().getPoint().getCoorX(), point.getCoorX())) {
            throw new InappropriateFunctionPointException("x Нарушает порядок,выберите другой индекс");
        }
        if (index < size - 1 && largeThan(point.getCoorX(), idxNode.getNext().getPoint().getCoorX())) {
            throw new InappropriateFunctionPointException("x Нарушает порядок,выберите другой индекс");
        }
        idxPoint.setCoorX(point.getCoorX());
        idxPoint.setCoorY(point.getCoorY());
        }catch(IllegalStateException e){
            throw new IllegalStateException("Ошибка получения ссылки на точку с индексом: " + index, e);
        }
    }


    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException,  IllegalStateException{
        if (index < 0 || index >= size) { // Если не подходит под условие выбрасываем исключение
            throw new FunctionPointIndexOutOfBoundsException("Выход за пределы массива");
        }
        try{
        FunctionNode idxNode = getNodeByIndex(index);
        FunctionPoint idxPoint = idxNode.getPoint();
        return idxPoint.getCoorX();
        } catch(IllegalStateException e){
            throw new IllegalStateException("Ошибка получения ссылки на точку с индексом: " + index, e);
        }
    }

    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (index < 0 || index >= size) { // Если не подходит под условие выбрасываем исключение
            throw new FunctionPointIndexOutOfBoundsException("Выход за пределы массива");
        }
        try{
        FunctionNode idxNode = getNodeByIndex(index);
        FunctionPoint idxPoint = idxNode.getPoint();
        return idxPoint.getCoorY();
        }catch(IllegalArgumentException e){
            throw new IllegalStateException("Ошибка получения ссылки на точку с индексом: " + index, e);
        }
    }

    public void setPointX(int index, double x)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException, IllegalStateException {
        if (index < 0 || index >= size) { // Если не подходит под условие выбрасываем исключение
            throw new FunctionPointIndexOutOfBoundsException("Выход за пределы массива");
        }
        try{
        FunctionNode idxNode = getNodeByIndex(index);
        FunctionPoint idxPoint = idxNode.getPoint();

        if (index > 0 && largeThan(idxNode.getPrev().getPoint().getCoorX(), x)) {
            throw new InappropriateFunctionPointException("x Нарушает порядок,выберите другой индекс");
        }
        if (index < size - 1 && largeThan(x, idxNode.getNext().getPoint().getCoorX())) {
            throw new InappropriateFunctionPointException("x Нарушает порядок,выберите другой индекс");
        }
        idxPoint.setCoorX(x);
        }catch (IllegalStateException e){
            throw new IllegalStateException("Ошибка получения ссылки на точку с индексом: " + index, e);
        }
    }

    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= size) { // Если не подходит под условие выбрасываем исключение
            throw new FunctionPointIndexOutOfBoundsException("Выход за пределы массива");
        }
        try{
        FunctionNode idxNode = getNodeByIndex(index);

        FunctionPoint idxPoint = idxNode.getPoint();
        idxPoint.setCoorY(y);
        }catch(IllegalStateException e){
            throw new IllegalStateException("Ошибка получения ссылки на точку с индексом: " + index, e);
        }
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (size < 3) {
            throw new IllegalStateException("Нельзя удалить точку из массива. Текущий размер: " + size);
        }
            deleteNodeByIndex(index);
    

        if (head != null && tail != null) {
            try {
                this.leftDomainBorder = head.getPoint().getCoorX();
                this.rightDomainBorder = tail.getPoint().getCoorX();
            } catch (IllegalStateException e) {
                throw new IllegalStateException("Некорректное состояние узлов после удаления", e);
            }
        }
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (point == null) {
            throw new IllegalArgumentException("Point cannot be null");
        }

        double point_x = point.getCoorX();
        int insertIndex = size; // По умолчанию в конец

        // Проверяем расширяет ли точка диапазон
        if (largeThan(leftDomainBorder, point_x)) {
            insertIndex = 0;
        } else if (largeThan(point_x ,rightDomainBorder)) {
            insertIndex = size;
        }

        // Проверка уникальности X
        FunctionNode current = head;
        while (current != null) {
            if (equal(point_x, current.getPoint().getCoorX())) {
                throw new InappropriateFunctionPointException("Точка должна иметь уникальное значение x");
            }
            current = current.getNext();
        }

        // Если точка принадлежит интервалу - ищем правильную позицию
        if (insertIndex == size) { // если еще не нашли позицию
            current = head;
            int index = 0;
            while (current != null && current.getNext() != null) {
                double currentX = current.getPoint().getCoorX();
                double nextX = current.getNext().getPoint().getCoorX();

                if (currentX < point_x && point_x < nextX) {
                    insertIndex = index + 1;
                    break;
                }
                current = current.getNext();
                index++;
            }
        }

        // Создаем новый узел
        FunctionNode newNode = new FunctionNode(new FunctionPoint(point));

        // Вставляем узел в найденную позицию
        if (size == 0) {
            // Вставка в пустой список
            head = newNode;
            tail = newNode;
        } else if (insertIndex == 0) {
            // Вставка в начало
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        } else if (insertIndex == size) {
            // Вставка в конец
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        } else {
            // Вставка в середину
            FunctionNode targetNode = getNodeByIndex(insertIndex);
            FunctionNode prevNode = targetNode.getPrev();

            newNode.setNext(targetNode);
            newNode.setPrev(prevNode);
            prevNode.setNext(newNode);
            targetNode.setPrev(newNode);
        }

        size++;

        // Обновляем границы
        if (insertIndex == 0) {
            this.leftDomainBorder = point_x;
        }
        if (insertIndex == size - 1) {
            this.rightDomainBorder = point_x;
        }
    }


    // Методы списка 
    private FunctionNode getNodeByIndex(int index){//Возвращает ссылку на узел // Может вернуть узел с point = null
            if (index < 0 || index >= size) { // Проверка необходимого условия
                throw new FunctionPointIndexOutOfBoundsException("Ошибка введённого индекса");
            }

            FunctionNode current; // текущий элемент списка

            if (index < size / 2) { // Определяем с какого конца списка лучше начать поиск элемента
                current = head;
                for (int i = 0; i < index; i++) {
                    current = current.getNext();
                }
            } else {
                current = tail;
                for (int i = size - 1; i > index; i--) {
                    current = current.getPrev();
                }
            }
            return current; // возвращаем ссылку на элемент
        }

    private FunctionNode addNodeToTail(FunctionNode newNode){ // Добавляет узел в конец списка
            if (newNode == null){ // Если добавляемый узел пустой
                throw new IllegalArgumentException("Нельзя добавить пустой узел!");
            }
            if (size == 0){ // Если длина равна 0
                head = newNode; // голова хранит ссылку наждемент
                tail = newNode; // хвост хранит ссылку на элемент
            }else{
            tail.setNext(newNode); 
            newNode.setPrev(tail);
            tail = newNode;}
            size++;
            return newNode;
        }
        
    private FunctionNode addNodeByIndex(int index) { // Добавляем элемент по индексу
            if (index < 0 || index > size){ // Проверяем необходимое условие
                throw new FunctionPointIndexOutOfBoundsException();}

            FunctionNode newNode = new FunctionNode(null); // создаём узел c point = null

            if (size == 0) {
                head = tail = newNode;
            } else if (index == size) {
                tail.setNext(newNode);
                newNode.setPrev(tail);
                tail = newNode;
            } else if (index == 0) {
                newNode.setNext(head);
                head.setPrev(newNode);
                head = newNode;
            } else {
                FunctionNode current = getNodeByIndex(index);
                newNode.setNext(current);
                newNode.setPrev(current.getPrev());
                current.getPrev().setNext(newNode);
                current.setPrev(newNode);
            }
            size++;
            return newNode;
        } 

    private FunctionNode deleteNodeByIndex(int index) { // Удаления элемента по индексу
            if (index < 0 || index >= size) {
                throw new FunctionPointIndexOutOfBoundsException("Индекс: " + index + " за пределами массива");
            }

            FunctionNode current = getNodeByIndex(index);

            if (size == 1) {
                // Удаление единственного элемента
                head = null;
                tail = null;
            } else if (index == 0) {
                // Удаление первого элемента
                head = current.getNext();
                head.setPrev(null);
                current.setNext(null);
            } else if (index == size - 1) {
                // Удаление последнего элемента
                tail = current.getPrev();
                tail.setNext(null);
                current.setPrev(null);
            } else {
                // Удаление из середины
                FunctionNode prev = current.getPrev();
                FunctionNode next = current.getNext();

                prev.setNext(next);
                next.setPrev(prev);

                current.setPrev(null);
                current.setNext(null);
            }
            size--;
            return current;
        }

    private int getSize(){
            return size;
        }

        @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(getPointsCount());
        
        // Сериализуем каждую точку
        for (int i = 0; i < getPointsCount(); i++) {
            out.writeDouble(getPoint(i).getCoorX());
            out.writeDouble(getPoint(i).getCoorY());
        }

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        // Восстанавливаем данные в том же порядке
        int pointsCount = in.readInt();
        size = pointsCount;

        double x = in.readDouble();
        double y = in.readDouble();
        head = new FunctionNode(new FunctionPoint(x, y));
        tail = head;
        for (int i = 1; i < pointsCount; i++) {
            x = in.readDouble();
            y = in.readDouble();
            
            tail.setNext(new FunctionNode(new FunctionPoint(x, y)));
            tail.getNext().setPrev(tail);
            tail = tail.getNext();
        }

        leftDomainBorder = head.getPoint().getCoorX();
        rightDomainBorder = tail.getPoint().getCoorX();
    }

    @Override
    public String toString() {
        FunctionNode currNode = head;
        String result = "{" + currNode.getPoint().toString();
        currNode = currNode.getNext();
        for (int i = 1; i < size; i++) {
            result += (", " + currNode.getPoint().toString());
            currNode = currNode.getNext();
        }
        result += "}";

        return result;
    }

    @Override
    public boolean equals(Object a) {
        if (this == a) { // Сравнение ссылок
            return true;
        }

        if (a == null) {
            return false;
        }

        if (getClass() == a.getClass()) {
            LinkedListTabulatedFunction b = (LinkedListTabulatedFunction) a;

            if (this.size != b.size) {
                return false;
            }

            if (!equal(this.leftDomainBorder, b.leftDomainBorder) ||
                    !equal(this.rightDomainBorder, b.rightDomainBorder)) {
                return false;
            }

            FunctionNode currentThis = this.head;
            FunctionNode currentOther = b.head;

            for (int i = 0; i < size; i++) {
                if (currentThis == null || currentOther == null) {
                    return false;
                }

                if (!equal(currentThis.getPoint().getCoorX(), currentOther.getPoint().getCoorX()) ||
                        !equal(currentThis.getPoint().getCoorY(), currentOther.getPoint().getCoorY())) {
                    return false;
                }

                currentThis = currentThis.getNext();
                currentOther = currentOther.getNext();
            }
            return true;
        }

        if (!(a instanceof TabulatedFunction)) {
            return false;
        }

        TabulatedFunction b = (TabulatedFunction) a;


        if (this.size != b.getPointsCount()) {
            return false;
        }

        if (!equal(this.leftDomainBorder, b.getLeftDomainBorder()) ||
                !equal(this.rightDomainBorder, b.getRightDomainBorder())) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (!equal(this.getPointX(i), b.getPointX(i)) ||
                    !equal(this.getPointY(i), b.getPointY(i))) {
                return false;
            }
        }
        return true;
    }

        @Override
        public int hashCode() {
            FunctionNode currNode = head;

            int hash = size; // Начинаем с количества точек
            // Добавляем хэш-коды всех точек через XOR
            for (int i = 0; i < size; i++) {
                hash ^= currNode.getPoint().hashCode();
                currNode = currNode.getNext();
            }
            return hash;
        }

        @Override
        public LinkedListTabulatedFunction clone() {
            LinkedListTabulatedFunction cloned = new LinkedListTabulatedFunction();

            if (size == 0) {
                return cloned; // возвращаем пустой список
            }

            for (int i = 0; i < size; i++) {
                try {
                    FunctionPoint point = this.getPoint(i);
                    cloned.addPoint(new FunctionPoint(point));
                } catch (InappropriateFunctionPointException e) {
                    throw new IllegalArgumentException("Ошибка во время клонирования", e);
                }
            }

            return cloned;
        }



        @Override
        public Iterator<FunctionPoint> iterator(){
            return new Iterator<FunctionPoint>(){

                private FunctionNode current = head;
                private FunctionNode lastReturned  = null;

                @Override
                public boolean hasNext(){
                    return current != null;
                }
                

                @Override
                public FunctionPoint next(){
                    if (!hasNext()){
                        throw new NoSuchElementException();
                    }

                    lastReturned = current;
                    FunctionPoint point = current.getPoint();
                    current = current.getNext();
                    return point;
                }

                public void delete(int index) throws UnsupportedOperationException{
                    throw new UnsupportedOperationException();
                }
            };
        }

        public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {

            public LinkedListTabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
                return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
            }

            public LinkedListTabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
                return new LinkedListTabulatedFunction(leftX, rightX, values);

            }

            public LinkedListTabulatedFunction createTabulatedFunction(FunctionPoint[] newPoints) {
                return new LinkedListTabulatedFunction(newPoints);

            }

        }

    }
