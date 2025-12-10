package functions;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TabulatedFunctions {
    private TabulatedFunctions() {
    }

    // фабрика
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory f) {
        factory = f;
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (function == null)
            throw new IllegalArgumentException("функция null");
        if (pointsCount < 2) {
            throw new IllegalArgumentException("недостаточное количество точек");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("левая граница меньше правой");
        }
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("границы табулирования выходят за область определения");
        }

        TabulatedFunction tabulatedFunc = createTabulatedFunction(leftX, rightX, pointsCount);
        // записываем значения в массив
        for (int i = 0; i < pointsCount; i++){
            double x = tabulatedFunc.getPointX(i);
            double y = function.getFunctionValue(x);
            tabulatedFunc.setPointY(i, y);
        }

        return tabulatedFunc;
    }

    // РЕФЛЕКСИЯ
    public static TabulatedFunction createTabulatedFunction(Class<?> funcClass, double leftX, double rightX, int pointsCount) {
        try {
            if (!TabulatedFunction.class.isAssignableFrom(funcClass)) { // проверяем, что класс реализует TabulatedFunction
                throw new IllegalArgumentException("класс не реализует интерфейс TabulatedFunction");
            }
            Constructor<?> constructor = funcClass.getConstructor(double.class, double.class, int.class); // ищем нужный конструктор

            return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount); // создаем объект через рефлексию
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new IllegalArgumentException("ошибка при создании объекта", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> funcClass, double leftX, double rightX, double[] values) {
        try {
            if (!TabulatedFunction.class.isAssignableFrom(funcClass)) { // проверяем, что класс реализует TabulatedFunction
                throw new IllegalArgumentException("класс не реализует интерфейс TabulatedFunction");
            }
            Constructor<?> constructor = funcClass.getConstructor(double.class, double.class, double[].class); // ищем нужный конструктор

            return (TabulatedFunction) constructor.newInstance(leftX, rightX, values); // создаем объект через рефлексию
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new IllegalArgumentException("ошибка при создании объекта", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> funcClass, FunctionPoint[] points) {
        try {
            if (!TabulatedFunction.class.isAssignableFrom(funcClass)) { // проверяем, что класс реализует TabulatedFunction
                throw new IllegalArgumentException("класс не реализует интерфейс TabulatedFunction");
            }
            Constructor<?> constructor = funcClass.getConstructor(FunctionPoint[].class); // ищем нужный конструктор

            return (TabulatedFunction) constructor.newInstance((Object) points); // создаем объект через рефлексию
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new IllegalArgumentException("ошибка при создании объекта", e);
        }
    }

    public static TabulatedFunction tabulate(Class<?> funcClass, Function function, double leftX, double rightX, int pointsCount) {
        if (function == null)
            throw new IllegalArgumentException("функция null");
        if (pointsCount < 2) {
            throw new IllegalArgumentException("недостаточное количество точек");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("левая граница меньше правой");
        }
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("границы табулирования выходят за область определения");
        }

        TabulatedFunction tabulatedFunc = createTabulatedFunction(funcClass, leftX, rightX, pointsCount);
        // записываем значения в массив
        for (int i = 0; i < pointsCount; i++){
            double x = tabulatedFunc.getPointX(i);
            double y = function.getFunctionValue(x);
            tabulatedFunc.setPointY(i, y);
        }

        return tabulatedFunc;
    }


    public static TabulatedFunction inputTabulatedFunction(Class<?> funcClass, InputStream in) throws IOException {
        DataInputStream inputData = new DataInputStream(in);
        int pointsCount = inputData.readInt();
        FunctionPoint[] points = new FunctionPoint[pointsCount];

            for (int i = 0; i < pointsCount; i++) {
                double x = inputData.readDouble();
                double y = inputData.readDouble();
                points[i] = new FunctionPoint(x, y);
            }

        return createTabulatedFunction(funcClass, points);

    }

    public static TabulatedFunction readTabulatedFunction(Class<?> funcClass, Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.nextToken();
        int pointsCount = (int) tokenizer.nval;
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            tokenizer.nextToken();
            double x = tokenizer.nval;
            tokenizer.nextToken();
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(funcClass,points);
    }







    // работа с байтовым потоком
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        int pointsCount = function.getPointsCount();
        DataOutputStream outputData = new DataOutputStream(out);
        outputData.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            outputData.writeDouble(function.getPointX(i));
            outputData.writeDouble(function.getPointY(i));
        }
        outputData.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream inputData = new DataInputStream(in);
        int pointsCount = inputData.readInt();
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            double x = inputData.readDouble();
            double y = inputData.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(points);

    }

    // работа с символьным потоком
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException{
        PrintWriter writer = new PrintWriter(out);
        int pointsCount = function.getPointsCount();
        writer.print(pointsCount);
        writer.print(" ");

        for (int i = 0; i < pointsCount; i++) {
            writer.print(function.getPointX(i));
            writer.print(" ");
            writer.print(function.getPointY(i));
            writer.print(" ");
        }
        writer.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.nextToken();
        int pointsCount = (int) tokenizer.nval;
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            tokenizer.nextToken();
            double x = tokenizer.nval;
            tokenizer.nextToken();
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(points);
    }
}
