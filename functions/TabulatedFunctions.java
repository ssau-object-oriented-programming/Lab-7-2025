package functions;

import java.io.*;
import java.lang.reflect.Constructor;

public final class TabulatedFunctions {

    // Статическая фабрика
    private static TabulatedFunctionFactory factory =
            new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    // Приватный конструктор для запрета создания объектов
    private TabulatedFunctions() {
    }

    // Метод для установки фабрики
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<?> functionClass, double leftX, double rightX, int pointsCount) {

        checkTabulatedFunctionClass(functionClass);

        try {
            // Ищем конструктор
            Constructor<?> constructor = functionClass.getConstructor(
                    double.class, double.class, int.class);

            // Создаем объект через рефлексию
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Данный класс не имеет конструктора (double, double, int)", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<?> functionClass, double leftX, double rightX, double[] values) {

        checkTabulatedFunctionClass(functionClass);

        try {
            Constructor<?> constructor = functionClass.getConstructor(
                    double.class, double.class, double[].class);

            return (TabulatedFunction) constructor.newInstance(leftX, rightX, values);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Данный класс не имеет конструктора (double, double, double[])", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта " +
                    functionClass.getName(), e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<?> functionClass, FunctionPoint[] points) {

        checkTabulatedFunctionClass(functionClass);

        try {
            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);

            return (TabulatedFunction) constructor.newInstance((Object) points);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Данный класс не имеет конструктора (FunctionPoint[])", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта", e);
        }
    }


    // Проверяет, что класс реализует TabulatedFunction
    private static void checkTabulatedFunctionClass(Class<?> functionClass) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Данный класс не реализует интерфейс TabulatedFunction");
        }
    }

    // Табулирует функцию с указанием класса реализации
    public static TabulatedFunction tabulate(
            Class<?> functionClass, Function function,
            double leftX, double rightX, int pointsCount) {

        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходит за область определения функции");
        }

        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        // Создание массива значений функции
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        // Используем рефлексивное создание
        return createTabulatedFunction(functionClass, leftX, rightX, values);
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        // Используем текущую фабрику
        return factory.createTabulatedFunction(leftX, rightX,
                createValuesArray(function, leftX, rightX, pointsCount));
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        writer.println(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            writer.println(function.getPointX(i) + " " + function.getPointY(i));
        }
        writer.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);

        tokenizer.nextToken();
        int pointsCount = (int)tokenizer.nval;

        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            tokenizer.nextToken();
            double x = tokenizer.nval;
            tokenizer.nextToken();
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }

        // Используем текущую фабрику
        return factory.createTabulatedFunction(points);
    }


    // Перегруженная версия с указанием класса
    public static TabulatedFunction readTabulatedFunction(
            Class<?> functionClass, Reader in) throws IOException {

        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.nextToken();
        int pointsCount = (int)tokenizer.nval;

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            tokenizer.nextToken();
            double x = tokenizer.nval;
            tokenizer.nextToken();
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }

        // Используем рефлексивное создание
        return createTabulatedFunction(functionClass, points);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            dos.writeDouble(function.getPointX(i));
            dos.writeDouble(function.getPointY(i));
        }
        dos.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        int pointsCount = dis.readInt();
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            double x = dis.readDouble();
            double y = dis.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        // Используем текущую фабрику
        return factory.createTabulatedFunction(points);
    }

    // Перегруженная версия с указанием класса

    public static TabulatedFunction inputTabulatedFunction(
            Class<?> functionClass, InputStream in) throws IOException {

        DataInputStream dis = new DataInputStream(in);
        int pointsCount = dis.readInt();
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            double x = dis.readDouble();
            double y = dis.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        // Используем рефлексивное создание
        return createTabulatedFunction(functionClass, points);
    }

    // Вспомогательный метод для создания массива значений
    private static double[] createValuesArray(Function function, double leftX, double rightX, int pointsCount) {

        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        return values;
    }
}