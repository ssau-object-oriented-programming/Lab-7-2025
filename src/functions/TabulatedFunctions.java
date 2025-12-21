package functions;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class TabulatedFunctions {
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() {
        throw new AssertionError("Нельзя создать экземпляр класса TabulatedFunctions");
    }
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

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (function == null) {
            throw new IllegalArgumentException("Функция не может быть null");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2: " + pointsCount);
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой: " + leftX + " >= " + rightX);
        }
        if (leftX < function.getLeftDomainBorder()) {
            throw new IllegalArgumentException("Левая граница табулирования выходит за область определения функции: " +
                    leftX + " < " + function.getLeftDomainBorder());
        }
        if (rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Правая граница табулирования выходит за область определения функции: " +
                    rightX + " > " + function.getRightDomainBorder());
        }
        TabulatedFunction tabulatedFunc = factory.createTabulatedFunction(leftX, rightX, pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            double x = tabulatedFunc.getPointX(i);
            double y = function.getFunctionValue(x);
            tabulatedFunc.setPointY(i, y);
        }
        return tabulatedFunc;
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX) {
        int pointsCount = (int) Math.max(2, Math.ceil((rightX - leftX) * 10) + 1);
        return tabulate(function, leftX, rightX, pointsCount);
    }

    public static TabulatedFunction tabulate(Function function, int pointsCount) {
        if (function == null) {
            throw new IllegalArgumentException("Функция не может быть null");
        }
        double leftX = function.getLeftDomainBorder();
        double rightX = function.getRightDomainBorder();
        if (Double.isInfinite(leftX) || Double.isInfinite(rightX)) {
            throw new IllegalArgumentException("Нельзя табулировать функцию с бесконечной областью определения: [" +
                    leftX + ", " + rightX + "]");
        }
        return tabulate(function, leftX, rightX, pointsCount);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        if (function == null) {
            throw new IllegalArgumentException("Функция не может быть null");
        }
        if (out == null) {
            throw new IllegalArgumentException("Выходной поток не может быть null");
        }
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeInt(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        dataOut.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("Входной поток не может быть null");
        }
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
        return factory.createTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        if (function == null) {
            throw new IllegalArgumentException("Функция не может быть null");
        }
        if (out == null) {
            throw new IllegalArgumentException("Выходной поток не может быть null");
        }
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

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("Входной поток не может быть null");
        }
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
                throw new IOException("Ожидалась координата x для точки " + i);
            }
            double x = tokenizer.nval;
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата y для точки " + i);
            }
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }
        return factory.createTabulatedFunction(points);
    }
    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass, double leftX, double rightX, int pointsCount) {
        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(double.class, double.class, int.class);
            return constructor.newInstance(leftX, rightX, pointsCount);
        } catch (NoSuchMethodException | SecurityException |
                 InstantiationException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot create tabulated function", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass, double leftX, double rightX, double[] values) {
        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(double.class, double.class, double[].class);
            return constructor.newInstance(leftX, rightX, values);
        } catch (NoSuchMethodException | SecurityException |
                 InstantiationException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot create tabulated function", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass, FunctionPoint[] points) {
        try {
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object) points);
        } catch (NoSuchMethodException | SecurityException |
                 InstantiationException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot create tabulated function", e);
        }
    }
    public static TabulatedFunction tabulate(
            Class<? extends TabulatedFunction> functionClass, Function function, double leftX, double rightX, int pointsCount) {
        if (function == null) {
            throw new IllegalArgumentException("Функция не может быть null");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2: " + pointsCount);
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой: " + leftX + " >= " + rightX);
        }
        if (leftX < function.getLeftDomainBorder()) {
            throw new IllegalArgumentException("Левая граница табулирования выходит за область определения функции: " +
                    leftX + " < " + function.getLeftDomainBorder());
        }
        if (rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Правая граница табулирования выходит за область определения функции: " +
                    rightX + " > " + function.getRightDomainBorder());
        }
        TabulatedFunction tabulatedFunc = createTabulatedFunction(functionClass, leftX, rightX, pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            double x = tabulatedFunc.getPointX(i);
            double y = function.getFunctionValue(x);
            tabulatedFunc.setPointY(i, y);
        }
        return tabulatedFunc;
    }
    public static TabulatedFunction tabulate(
            Class<? extends TabulatedFunction> functionClass, Function function, double leftX, double rightX) {
        int pointsCount = (int) Math.max(2, Math.ceil((rightX - leftX) * 10) + 1);
        return tabulate(functionClass, function, leftX, rightX, pointsCount);
    }
    public static TabulatedFunction tabulate(
            Class<? extends TabulatedFunction> functionClass, Function function, int pointsCount) {
        if (function == null) {
            throw new IllegalArgumentException("Функция не может быть null");
        }
        double leftX = function.getLeftDomainBorder();
        double rightX = function.getRightDomainBorder();
        if (Double.isInfinite(leftX) || Double.isInfinite(rightX)) {
            throw new IllegalArgumentException("Нельзя табулировать функцию с бесконечной областью определения: [" +
                    leftX + ", " + rightX + "]");
        }
        return tabulate(functionClass, function, leftX, rightX, pointsCount);
    }
    public static TabulatedFunction inputTabulatedFunction(InputStream in,
                                                           Class<? extends TabulatedFunction> functionClass) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        int pointsCount = dataIn.readInt();
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            points[i] = new FunctionPoint(dataIn.readDouble(), dataIn.readDouble());
        }
        return createTabulatedFunction(functionClass, points);
    }

    public static TabulatedFunction readTabulatedFunction(Reader in,
                                                          Class<? extends TabulatedFunction> functionClass) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.parseNumbers();
        if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Ошибка формата");
        int pointCount = (int) tokenizer.nval;
        FunctionPoint[] points = new FunctionPoint[pointCount];
        for (int i = 0; i < pointCount; i++) {
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Ошибка X");
            double x = tokenizer.nval;
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Ошибка Y");
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(functionClass, points);
    }
}