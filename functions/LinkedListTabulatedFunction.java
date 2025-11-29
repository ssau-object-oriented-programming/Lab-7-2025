package functions;
import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable{
    private FunctionNode head = new FunctionNode(null); // голова списка
    private int pointcount; // количество точек
    private static class FunctionNode{
        private FunctionPoint point; // точка функции
        private FunctionNode prev; // ссылка на предыдущий узел
        private FunctionNode next; // ссылка на следующий узел

        public FunctionNode(FunctionPoint point){
            this.point = point; // устанавливаем точку
            this.prev = null; // предыдущий узел пока null
            this.next = null; // следующий узел пока null
        }

        public FunctionPoint getPoint(){
            return point; // возвращаем точку
        }
        public void setPoint(FunctionPoint point){
            this.point = point; // устанавливаем новую точку
        }
        public FunctionNode getPrev(){
            return prev; // возвращаем предыдущий узел
        }
        public void setPrev(FunctionNode prev){
            this.prev = prev; // устанавливаем предыдущий узел
        }
        public FunctionNode getNext(){
            return next; // возвращаем следующий узел
        }
        public void setNext(FunctionNode next){
            this.next = next; // устанавливаем следующий узел
        }
    }

    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {
        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new LinkedListTabulatedFunction(points);  // создает связную табулированную функцию из точек
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);  // создает связную функцию с границами и количеством точек
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX, rightX, values);  // создает связную функцию с границами и значениями
        }
    }

    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private FunctionNode currentNode = head.getNext();  // начинаем с первого узла после головы
            private final FunctionNode endNode = head;  // конечный узел - голова

            @Override
            public boolean hasNext() {
                return currentNode != endNode;  // проверяем не дошли ли до конца
            }

            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more points");  // исключение если точек нет
                }
                // создаем копию точки для защиты инкапсуляции
                FunctionPoint point = currentNode.getPoint();  // получаем точку из текущего узла
                FunctionPoint copy = new FunctionPoint(point.getX(), point.getY());  // создаем копию точки
                currentNode = currentNode.getNext();  // переходим к следующему узлу
                return copy;  // возвращаем копию
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove operation is not supported");  // операция удаления не поддерживается
            }
        };
    }

    @Override
    public String toString(){
        String res = "{"; // начинаем строку с фигурной скобки
        for (int i = 0; i < pointcount; i++){ // проходим по всем точкам
            res+= this.getNodeByIndex(i).getPoint().toString(); // добавляем строковое представление точки
            if(i<pointcount -1){ // если это не последняя точка
                res+=", "; // добавляем запятую и пробел
            }
        }
        res += "}"; // закрываем фигурную скобку
        return res; // возвращаем результат
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true; // если это тот же объект возвращаем true
        if (o == null || !(o instanceof TabulatedFunction)) return false; // если объект null или не табулированная функция возвращаем false

        if (o instanceof LinkedListTabulatedFunction) { // если объект тоже linked list
            LinkedListTabulatedFunction other = (LinkedListTabulatedFunction) o; // приводим к linked list
            if (this.pointcount != ((LinkedListTabulatedFunction) o).pointcount) { // сравниваем количество точек
                return false; // если разное количество точек возвращаем false
            }
            FunctionNode curThis = this.head.getNext(); // начинаем с первого узла текущего списка
            FunctionNode curOther = other.head.getNext(); // начинаем с первого узла другого списка

            while (curThis != this.head) { // пока не вернемся к голове
                if (!curThis.getPoint().equals(curOther.getPoint())) { // сравниваем координаты точек
                    return false; // если координаты не равны возвращаем false
                }
                curThis = curThis.getNext(); // переходим к следующему узлу текущего списка
                curOther = curOther.getNext(); // переходим к следующему узлу другого списка
            }
        }
        else { // если объект другой реализации tabulated function
            TabulatedFunction other = (TabulatedFunction) o;
            if (this.getPointsCount() != other.getPointsCount()) { // сравниваем количество точек
                return false; // если разное количество точек возвращаем false
            }
            for (int i = 0; i < pointcount; i++) { // проходим по всем точкам
                FunctionPoint thisPoint = new FunctionPoint(this.getPointX(i), this.getPointY(i));
                FunctionPoint otherPoint = new FunctionPoint(other.getPointX(i), other.getPointY(i));
                if (!thisPoint.equals(otherPoint)) {
                    return false;
                }
            }
        }

        return true; // если все проверки пройдены возвращаем true
    }

    @Override
    public int hashCode(){
        int hash = pointcount; // начинаем с количества точек

        FunctionNode cur = head.getNext(); // начинаем с первого узла
        while(cur!=head) { // пока не вернемся к голове
            hash ^= cur.getPoint().hashCode(); // комбинируем хэш точки через xor
            cur = cur.getNext(); // переходим к следующему узлу
        }
        return hash; // возвращаем результат
    }

    @Override
    public Object clone(){
        FunctionPoint[] point_mass = new FunctionPoint[pointcount]; // создаем массив для точек
        FunctionNode cur = head.getNext(); // начинаем с первого узла
        int i =0; // индекс для массива
        while(cur != head){ // пока не вернемся к голове
            point_mass[i] = (FunctionPoint)cur.getPoint().clone(); // клонируем точку и сохраняем в массив
            cur = cur.getNext(); // переходим к следующему узлу
            i++; // увеличиваем индекс
        }
        return new LinkedListTabulatedFunction(point_mass); // создаем новый список из массива точек
    }

    public LinkedListTabulatedFunction(){
        this.head = null; // устанавливаем голову списка в null
        this.pointcount = 0; // устанавливаем количество точек в ноль
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointcount); // записываем количество точек

        // записываем все точки
        FunctionNode current = head.getNext(); // начинаем с первого узла после головы
        while (current != head) { // проходим по всем узлам пока не вернемся к голове
            out.writeObject(current.getPoint()); // записываем точку текущего узла
            current = current.getNext(); // переходим к следующему узлу
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // инициализируем пустой список
        this.head = new FunctionNode(null); // создаем головной узел
        this.head.setPrev(head); // устанавливаем предыдущий узел на саму голову
        this.head.setNext(head); // устанавливаем следующий узел на саму голову
        this.pointcount = 0; // устанавливаем количество точек в ноль

        // читаем количество точек
        int count = in.readInt(); // читаем количество точек из потока

        // восстанавливаем точки
        for (int i = 0; i < count; i++) { // проходим по всем точкам
            FunctionPoint point = (FunctionPoint) in.readObject(); // читаем точку из потока
            addNodeToTail(point); // добавляем точку в конец списка
        }
    }
    // конструктор получающий массив точек FunctionPoint
    public LinkedListTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
        // проверяем что точек достаточно и они упорядочены
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее двух");
        }

        // проверяем упорядоченность точек по x
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].getX() >= points[i + 1].getX()) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по возрастанию X");
            }
        }

        // инициализируем голову списка
        this.head = new FunctionNode(null);
        head.setPrev(head);
        head.setNext(head);
        this.pointcount = 0;

        // добавляем точки в список обеспечивая инкапсуляцию
        for (int i = 0; i < points.length; i++) {
            // создаем копию точки чтобы избежать ссылочной зависимости
            addNodeToTail(new FunctionPoint(points[i]));
        }
    }

    public FunctionNode getNodeByIndex(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >= pointcount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        FunctionNode node = head; // начинаем с головы
        if(index < pointcount/2){
            for(int i =0; i<=index; i++){
                node = node.getNext(); // идем вперед по списку
            }
        }
        else{
            for(int i =pointcount-1; i >=index; i--){
                node = node.getPrev(); // идем назад по списку
            }
        }
        return node; // возвращаем найденный узел
    }
    public FunctionNode addNodeToTail(FunctionPoint point){
        FunctionNode newNode = new FunctionNode(point); // создаем новый узел
        FunctionNode tail = head.getPrev(); // получаем хвост списка

        newNode.setPrev(tail); // новый узел указывает на хвост
        newNode.setNext(head); // новый узел указывает на голову
        tail.setNext(newNode); // хвост указывает на новый узел
        head.setPrev(newNode); // голова указывает на новый узел

        pointcount ++; // увеличиваем счетчик точек
        return newNode; // возвращаем новый узел
    }
    public FunctionNode addNodeByIndex(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index > pointcount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        if(index == pointcount){
            return addNodeToTail(point); // добавляем в конец
        }

        FunctionNode curNode = getNodeByIndex(index); // получаем текущий узел
        FunctionNode newNode = new FunctionNode(point); // создаем новый узел
        FunctionNode prevNode = curNode.getPrev(); // получаем предыдущий узел

        newNode.setPrev(prevNode); // новый узел указывает на предыдущий
        newNode.setNext(curNode); // новый узел указывает на текущий
        prevNode.setNext(newNode); // предыдущий указывает на новый
        curNode.setPrev(newNode); // текущий указывает на новый

        pointcount++; // увеличиваем счетчик точек
        return newNode; // возвращаем новый узел
    }
    public FunctionNode deleteNodeByIndex(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >= pointcount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        FunctionNode delNode = getNodeByIndex(index); // узел для удаления
        FunctionNode prevNode = delNode.getPrev(); // предыдущий узел
        FunctionNode nextNode = delNode.getNext(); // следующий узел

        prevNode.setNext(nextNode); // предыдущий указывает на следующий
        nextNode.setPrev(prevNode); // следующий указывает на предыдущий

        pointcount--; // уменьшаем счетчик точек
        return delNode; // возвращаем удаленный узел
    }
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException{
        if(leftX >= rightX || pointsCount<2){
            throw new IllegalArgumentException("Левая граница больше левой или количество точек меньше 2");
        }

        this.head = new FunctionNode(null); // создаем голову
        head.setPrev(head); // голова указывает сама на себя
        head.setNext(head); // голова указывает сама на себя
        this.pointcount = 0; // начинаем с нуля точек
        // вычисляем шаг между точками
        double step = (rightX-leftX)/(pointsCount-1);

        for(int i =0; i < pointsCount; i++){
            // вычисляем x координату для каждой точки
            double x = leftX + i * step;
            // создаем точку с вычисленным x и y=0
            addNodeToTail(new FunctionPoint(x, 0));
        }
    }
    // конструктор создающий функцию с заданными значениями y
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException{
        if(leftX >= rightX || values.length <2){
            throw new IllegalArgumentException("Левая граница больше левой или количество точек меньше 2");
        }
        this.head = new FunctionNode(null); // создаем голову
        head.setPrev(head); // голова указывает сама на себя
        head.setNext(head); // голова указывает сама на себя
        this.pointcount = 0; // начинаем с нуля точек
        // вычисляем шаг между точками
        double step = (rightX-leftX)/(values.length-1);

        for(int i =0; i < values.length; i++){
            // вычисляем x координату
            double x = leftX + i * step;
            // создаем точку с вычисленным x и заданным y
            addNodeToTail(new FunctionPoint(x, values[i]));
        }
    }
    public double getLeftDomainBorder(){
        return getNodeByIndex(0).getPoint().getX(); // x первой точки
    }
    public double getRightDomainBorder(){
        return getNodeByIndex(pointcount-1).getPoint().getX(); // x последней точки
    }
    // вычислить значение функции в заданной точке x
    public double getFunctionValue(double x){
        // если точек нет возвращаем не число
        if(pointcount == 0){
            return Double.NaN;
        }

        // проверяем что x находится в области определения
        if(x >= this.getLeftDomainBorder() && x <= this.getRightDomainBorder()){
            // ищем точку с точно таким же x
            for(int i = 0; i < pointcount; i++){
                if(Math.abs(getNodeByIndex(i).getPoint().getX() - x) < 1e-10){
                    // если нашли возвращаем соответствующий y
                    return getNodeByIndex(i).getPoint().getY();
                }
            }
            // если точного совпадения нет ищем интервал для интерполяции
            for(int i = 0; i < pointcount-1; i++){
                double x1 = getNodeByIndex(i).getPoint().getX();
                double x2 = getNodeByIndex(i+1).getPoint().getX();

                // проверяем попадает ли x в текущий интервал
                if(x >= x1 && x <= x2){
                    double y1 = getNodeByIndex(i).getPoint().getY();
                    double y2 = getNodeByIndex(i+1).getPoint().getY();

                    // вычисляем значение линейной интерполяции
                    return y1+(y2-y1)*(x-x1)/(x2-x1);
                }
            }
        }
        // если x вне области определения возвращаем не число
        return Double.NaN;
    }
    // получить общее количество точек в функции
    public int getPointsCount(){
        return pointcount; // возвращаем количество точек
    }
    // получить копию точки по указанному индексу
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >= pointcount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        // возвращаем копию чтобы защитить исходные данные
        return new FunctionPoint(getNodeByIndex(index).getPoint().getX(), getNodeByIndex(index).getPoint().getY());
    }
    // заменить точку по указанному индексу
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException{
        // проверяем валидность индекса
        if (index < 0 || index >= pointcount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        // проверяем что новый x не нарушает порядок точек
        // слева от текущей точки x должен быть меньше
        // справа от текущей точки x должен быть больше
        if ((index > 0 && point.getX() <= getNodeByIndex(index-1).getPoint().getX()) || (index < pointcount - 1 && point.getX() >= getNodeByIndex(index+1).getPoint().getX())){
            throw new InappropriateFunctionPointException("Точка не входит в интервал");
        }

        // создаем новую точку чтобы избежать ссылочной зависимости
        getNodeByIndex(index).setPoint(new FunctionPoint(point));
    }
    // получить координату x точки по индексу
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >=pointcount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        return getNodeByIndex(index).getPoint().getX(); // возвращаем x точки
    }
    // установить новую координату x для точки по индексу
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException{
        // проверяем валидность индекса
        if (index < 0 || index >= pointcount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        // проверяем что новый x сохраняет порядок точек
        if ((index > 0 && x <= getNodeByIndex(index-1).getPoint().getX()) || (index < pointcount - 1 && x >= getNodeByIndex(index+1).getPoint().getX())){
            throw new InappropriateFunctionPointException("Точка не входит в интервал");
        }
        getNodeByIndex(index).getPoint().setX(x); // меняем x точки
    }
    // получить координату y точки по индексу
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index < 0 || index >= pointcount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        return getNodeByIndex(index).getPoint().getY(); // возвращаем y точки
    }
    // установить новую координату y для точки по индексу
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException{
        // проверяем валидность индекса
        if (index < 0 || index >= pointcount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        getNodeByIndex(index).getPoint().setY(y); // меняем y точки
    }
    // удалить точку по указанному индексу
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException{
        // проверяем можно ли удалить точку
        // должно быть минимум 2 точки и индекс должен быть валидным
        if(index < 0 || index >= pointcount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс не входит в границы");
        }
        if(pointcount < 3){
            throw new IllegalStateException("Точка не входит в интервал");
        }
        deleteNodeByIndex(index); // удаляем узел
    }
    // добавить новую точку в функцию
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        int ins_index= 0;
        // ищем позицию для вставки чтобы сохранить возрастающий порядок по x
        while (ins_index < pointcount && getNodeByIndex(ins_index).getPoint().getX() < point.getX()) {
            ins_index++;
        }
        // если точка с таким x уже существует выходим
        if (ins_index < pointcount && Math.abs(getNodeByIndex(ins_index).getPoint().getX() - point.getX()) < 1e-10) {
            throw new InappropriateFunctionPointException("Совпадают Х");
        }

        // вставляем новую точку
        addNodeByIndex(ins_index, point);
    }
}