package functions;
import java.io.*;
import java.lang.reflect.Constructor;

public class TabulatedFunctions {
    private TabulatedFunctions(){} // приватный конструктор чтобы нельзя было создать экземпляр

    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();  // фабрика по умолчанию

    // метод для замены фабрики
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newFactory) {
        factory = newFactory;  // устанавливаем новую фабрику
    }

    // три перегруженных метода создания табулированных функций
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);  // создаем функцию через фабрику из точек
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);  // создаем функцию через фабрику с границами и количеством точек
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);  // создаем функцию через фабрику с границами и значениями
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, FunctionPoint[] points) {
        // проверяем что класс реализует tabulatedFunction
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Class must implement tabulatedFunction interface");  // исключение если класс не реализует интерфейс
        }

        try {
            // ищем конструктор с параметром functionpoint[]
            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);  // получаем конструктор
            // создаем объект с помощью рефлексии
            return (TabulatedFunction) constructor.newInstance((Object) points);  // создаем экземпляр через рефлексию
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating function with points array", e);  // исключение при ошибке создания
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, double leftX, double rightX, int pointsCount) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Class must implement tabulatedFunction interface");  // исключение если класс не реализует интерфейс
        }

        try {
            // ищем конструктор с параметрами double double int
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, int.class);  // получаем конструктор
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);  // создаем экземпляр через рефлексию
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating function with bounds and point count", e);  // исключение при ошибке создания
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, double leftX, double rightX, double[] values) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Class must implement tabulatedFunction interface");  // исключение если класс не реализует интерфейс
        }

        try {
            // ищем конструктор с параметрами double double double[]
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, double[].class);  // получаем конструктор
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, values);  // создаем экземпляр через рефлексию
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating function with bounds and values", e);  // исключение при ошибке создания
        }
    }

    // перегруженный метод tabulate с рефлексией
    public static TabulatedFunction tabulate(Class<?> functionClass, Function function, double leftX, double rightX, int pointsCount) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Class must implement tabulatedFunction interface");  // исключение если класс не реализует интерфейс
        }

        if(function == null){
            throw new IllegalArgumentException("Function cannot be null");  // проверяем что функция не null
        }
        if(pointsCount < 2){
            throw new IllegalArgumentException("The number of points must be at least 2");  // проверяем минимальное количество точек
        }
        if(leftX >= rightX){
            throw new IllegalArgumentException("The left border must be smaller than the right one");  // проверяем порядок границ
        }
        if(leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()){
            throw new IllegalArgumentException("Tabulation boundaries extend beyond the definition domain");  // проверяем что границы в области определения
        }

        double vals[] = new double[pointsCount];  // создаем массив для значений
        double step = (rightX - leftX) / (pointsCount - 1);  // вычисляем шаг

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;  // вычисляем x координату
            vals[i] = function.getFunctionValue(x);  // вычисляем значение функции
        }

        return createTabulatedFunction(functionClass, leftX, rightX, vals);  // создаем функцию через рефлексию
    }
    public static TabulatedFunction inputTabulatedFunction(Class<?> functionClass, InputStream in) throws IOException {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) { // проверяем что класс реализует нужный интерфейс
            throw new IllegalArgumentException("Class must implement TabulatedFunction interface"); // бросаем исключение если класс неподходящий
        }

        if (in == null) { // проверяем что поток не нулевой
            throw new IllegalArgumentException("Input stream cannot be null"); // бросаем исключение если поток нулевой
        }
        DataInputStream dataIn = new DataInputStream(in); // создаем поток для чтения данных

        int pointsCount = dataIn.readInt(); // читаем количество точек
        FunctionPoint[] points = new FunctionPoint[pointsCount]; // создаем массив для точек

        for (int i = 0; i < pointsCount; i++) { // цикл по всем точкам
            double x = dataIn.readDouble(); // читаем координату x
            double y = dataIn.readDouble(); // читаем координату y
            points[i] = new FunctionPoint(x, y); // создаем новую точку
        }

        return createTabulatedFunction(functionClass, points); // создаем табулированную функцию через рефлексию
    }

    public static TabulatedFunction readTabulatedFunction(Class<?> functionClass, Reader in) throws IOException {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) { // проверяем что класс реализует нужный интерфейс
            throw new IllegalArgumentException("Class must implement TabulatedFunction interface"); // бросаем исключение если класс неподходящий
        }

        if (in == null) { // проверяем что поток не нулевой
            throw new IllegalArgumentException("Reader cannot be null"); // бросаем исключение если поток нулевой
        }

        StreamTokenizer tokenizer = new StreamTokenizer(in); // создаем токенизатор для чтения
        tokenizer.parseNumbers(); // настраиваем обработку чисел как double

        if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) { // читаем следующий токен и проверяем что это число
            throw new IOException("Expected number of points"); // бросаем исключение если не число
        }
        int pointsCount = (int) tokenizer.nval; // получаем количество точек

        FunctionPoint[] points = new FunctionPoint[pointsCount]; // создаем массив для точек

        for (int i = 0; i < pointsCount; i++) { // цикл по всем точкам
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) { // читаем следующий токен и проверяем что это число
                throw new IOException("Expected x coordinate"); // бросаем исключение если не число
            }
            double x = tokenizer.nval; // получаем координату x

            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) { // читаем следующий токен и проверяем что это число
                throw new IOException("Expected y coordinate"); // бросаем исключение если не число
            }
            double y = tokenizer.nval; // получаем координату y

            points[i] = new FunctionPoint(x, y); // создаем новую точку
        }

        return createTabulatedFunction(functionClass, points); // создаем табулированную функцию через рефлексию
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) throws IllegalArgumentException{
        if(function == null){
            throw new IllegalArgumentException("Function cannot be null"); // проверяем что функция не null
        }
        if(pointsCount < 2){
            throw new IllegalArgumentException("The number of points must be at least 2"); // проверяем минимальное количество точек
        }
        if(leftX >= rightX){
            throw new IllegalArgumentException("The left border must be smaller than the right one"); // проверяем порядок границ
        }
        if(leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()){
            throw new IllegalArgumentException("Tabulation boundaries extend beyond the definition domain"); // проверяем что границы в области определения
        }

        double vals[] = new double[pointsCount]; // создаем массив для значений y
        double step = (rightX - leftX) / (pointsCount - 1); // вычисляем шаг между точками

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step; // вычисляем x координату
            vals[i] = function.getFunctionValue(x); // вычисляем значение функции в точке x
        }
        return createTabulatedFunction(leftX, rightX, vals); // создаем табулированную функцию
    }
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException, IllegalArgumentException{
        if (function == null) {
            throw new IllegalArgumentException("Function cannot be null"); // проверяем что функция не null
        }
        if (out == null) {
            throw new IllegalArgumentException("OutputStream cannot be null"); // проверяем что поток не null
        }

        DataOutputStream dataOut = new DataOutputStream(out); // создаем поток для записи данных

        // записываем количество точек
        int pointsCount = function.getPointsCount(); // получаем количество точек
        dataOut.writeInt(pointsCount); // записываем количество точек

        // записываем координаты всех точек (x, y)
        for (int i = 0; i < pointsCount; i++) {
            double x = function.getPointX(i); // получаем x координату точки
            double y = function.getPointY(i); // получаем y координату точки

            dataOut.writeDouble(x); // записываем x координату
            dataOut.writeDouble(y); // записываем y координату
        }

        // сбрасываем буфер чтобы убедиться что данные записаны
        dataOut.flush(); // сбрасываем буфер
    }
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException{
        if (in == null) {
            throw new IllegalArgumentException("Input stream cannot be null"); // проверяем что поток не null
        }
        DataInputStream dataIn = new DataInputStream(in); // создаем поток для чтения данных

        // считываем количество точек
        int pointsCount = dataIn.readInt(); // читаем количество точек

        // создаем массив точек
        FunctionPoint[] points = new FunctionPoint[pointsCount]; // создаем массив для точек

        // считываем и создаем точки
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble(); // читаем x координату
            double y = dataIn.readDouble(); // читаем y координату
            points[i] = new FunctionPoint(x, y); // создаем новую точку
        }

        // используем конструктор с массивом точек
        return createTabulatedFunction(points); // создаем табулированную функцию из массива точек
    }
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        if (function == null || out == null) {
            throw new IllegalArgumentException("Function and Writer cannot be null"); // проверяем что функция и поток не null
        }

        PrintWriter writer = new PrintWriter(out); // создаем писатель для текстового вывода

        int pointsCount = function.getPointsCount(); // получаем количество точек
        writer.print(pointsCount); // записываем количество точек
        writer.print(" "); // записываем пробел

        for (int i = 0; i < pointsCount; i++) {
            writer.print(function.getPointX(i)); // записываем x координату
            writer.print(" "); // записываем пробел
            writer.print(function.getPointY(i)); // записываем y координату
            if (i < pointsCount - 1) {
                writer.print(" "); // записываем пробел между точками
            }
        }

        writer.flush(); // сбрасываем буфер
    }
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {

        if (in == null) {
            throw new IllegalArgumentException("Reader cannot be null"); // проверяем что поток не null
        }

        StreamTokenizer tokenizer = new StreamTokenizer(in); // создаем токенизатор для чтения
        tokenizer.parseNumbers(); // обрабатывать числа как double

        // читаем количество точек
        if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
            throw new IOException("Expected number of points"); // проверяем что следующий токен число
        }
        int pointsCount = (int) tokenizer.nval; // получаем количество точек

        // читаем координаты точек
        FunctionPoint[] points = new FunctionPoint[pointsCount]; // создаем массив для точек

        for (int i = 0; i < pointsCount; i++) {
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Expected x coordinate"); // проверяем что следующий токен число
            }
            double x = tokenizer.nval; // получаем x координату

            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Expected y coordinate"); // проверяем что следующий токен число
            }
            double y = tokenizer.nval; // получаем y координату

            points[i] = new FunctionPoint(x, y); // создаем новую точку
        }

        return createTabulatedFunction(points); // создаем табулированную функцию из массива точек
    }
}