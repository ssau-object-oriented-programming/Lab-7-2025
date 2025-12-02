package functions;

import java.io.*;
import java.io.StreamTokenizer;
import java.lang.reflect.Constructor;

public class TabulatedFunctions {
    // Приватное статическое поле фабрики
    private static TabulatedFunctionFactory factory =
            new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() {
        throw new AssertionError("Нельзя создать объект класса TabulatedFunctions");
    }

    // =========== ФАБРИЧНЫЕ МЕТОДЫ ===========

    // Метод для замены фабрики
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }

    // Методы-фабрики (без рефлексии)
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    // =========== МЕТОДЫ С РЕФЛЕКСИЕЙ (Class первым параметром) ===========

    //Создает табулированную функцию с помощью рефлексии
    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass,
                                                            double leftX, double rightX, int pointsCount) {
        // Проверяем, что класс реализует TabulatedFunction
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Class must implement TabulatedFunction");
        }

        try {
            // Находим конструктор с параметрами (double, double, int)
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, int.class);

            // Создаем объект
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);

        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating tabulated function", e);
        }
    }

    //Создает табулированную функцию с помощью рефлексии
    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass,
                                                            double leftX, double rightX, double[] values) {
        // Проверяем, что класс реализует TabulatedFunction
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Class must implement TabulatedFunction");
        }

        try {
            // Находим конструктор с параметрами (double, double, double[])
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, double[].class);

            // Создаем объект
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, values);

        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating tabulated function", e);
        }
    }

    //Создает табулированную функцию с помощью рефлексии
    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, FunctionPoint[] points) {
        // Проверяем, что класс реализует TabulatedFunction
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Class must implement TabulatedFunction");
        }

        try {
            // Находим конструктор с параметрами (FunctionPoint[])
            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);

            // Создаем объект
            return (TabulatedFunction) constructor.newInstance((Object) points);

        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating tabulated function", e);
        }
    }

    // =========== БИНАРНЫЕ МЕТОДЫ ЧТЕНИЯ/ЗАПИСИ ===========

    //Бинарный вывод табулированной функции
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);

        dataOut.writeInt(function.getPointsCount());

        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }

        dataOut.flush();
    }

    //Бинарный ввод табулированной функции (использует текущую фабрику)
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);

        int pointsCount = dataIn.readInt();
        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        // Используем текущую фабрику
        return factory.createTabulatedFunction(points);
    }

    //Бинарный ввод табулированной функции с указанием класса через рефлексию
    public static TabulatedFunction inputTabulatedFunction(Class<?> functionClass, InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);

        int pointsCount = dataIn.readInt();
        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        // Используем рефлексию для создания объекта указанного класса
        try {
            if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
                throw new IllegalArgumentException("Class must implement TabulatedFunction");
            }

            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);
            return (TabulatedFunction) constructor.newInstance((Object) points);

        } catch (Exception e) {
            throw new IOException("Error creating tabulated function from stream", e);
        }
    }

    // =========== ТЕКСТОВЫЕ МЕТОДЫ ЧТЕНИЯ/ЗАПИСИ ===========

    //Текстовый вывод табулированной функции
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter writer = new PrintWriter(out);

        writer.print(function.getPointsCount());
        writer.print(' ');

        for (int i = 0; i < function.getPointsCount(); i++) {
            writer.print(function.getPointX(i));
            writer.print(' ');
            writer.print(function.getPointY(i));
            if (i < function.getPointsCount() - 1) {
                writer.print(' ');
            }
        }

        writer.flush();
    }

    //Текстовый ввод табулированной функции (использует текущую фабрику)
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);

        if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
            throw new IOException("Ожидалось количество точек");
        }
        int pointsCount = (int) tokenizer.nval;

        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата X точки " + (i + 1));
            }
            double x = tokenizer.nval;

            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата Y точки " + (i + 1));
            }
            double y = tokenizer.nval;

            points[i] = new FunctionPoint(x, y);
        }

        // Используем текущую фабрику
        return factory.createTabulatedFunction(points);
    }

    //Текстовый ввод табулированной функции с указанием класса через рефлексию
    public static TabulatedFunction readTabulatedFunction(Class<?> functionClass, Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);

        if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
            throw new IOException("Ожидалось количество точек");
        }
        int pointsCount = (int) tokenizer.nval;

        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата X точки " + (i + 1));
            }
            double x = tokenizer.nval;

            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата Y точки " + (i + 1));
            }
            double y = tokenizer.nval;

            points[i] = new FunctionPoint(x, y);
        }

        try {
            if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
                throw new IllegalArgumentException("Class must implement TabulatedFunction");
            }

            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);
            return (TabulatedFunction) constructor.newInstance((Object) points);

        } catch (Exception e) {
            throw new IOException("Error creating tabulated function from reader", e);
        }
    }

    // =========== МЕТОД TABULATE ===========

    //Табулирует функцию (использует текущую фабрику)
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }

        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }

        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    //Табулирует функцию с указанием класса через рефлексию (Class первым параметром)
    public static TabulatedFunction tabulate(Class<?> functionClass, Function function,
                                             double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }

        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }

        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        return createTabulatedFunction(functionClass, leftX, rightX, values);
    }

    //Табулирует функцию с указанием класса через рефлексию (Class последним параметром)
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount,
                                             Class<?> functionClass) {
        return tabulate(functionClass, function, leftX, rightX, pointsCount);
    }
}