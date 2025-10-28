package functions;

import java.io.*;
import java.lang.reflect.Constructor;

public class TabulatedFunctions {
    // Приватное статическое поле фабрики
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    // Метод для замены фабрики
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }

    // Три перегруженных метода создания табулированных функций (через фабрику)
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    // Три перегруженных метода создания табулированных функций (через рефлексию)
    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, double leftX, double rightX, int pointsCount) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс " + functionClass + " не реализует интерфейс TabulatedFunction");
        }

        try {
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, int.class);
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, double leftX, double rightX, double[] values) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс " + functionClass + " не реализует интерфейс TabulatedFunction");
        }

        try {
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, double[].class);
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, values);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, FunctionPoint[] points) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс " + functionClass + " не реализует интерфейс TabulatedFunction");
        }

        try {
            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);
            return (TabulatedFunction) constructor.newInstance((Object) points);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) {
        try (DataOutputStream dos = new DataOutputStream(out)) {
            int pointsCount = function.getPointsCount();
            dos.writeInt(pointsCount);

            for (int i = 0; i < pointsCount; i++) {
                dos.writeDouble(function.getPointX(i));
                dos.writeDouble(function.getPointY(i));
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при выводе функции", e);
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) {
        try (DataInputStream dis = new DataInputStream(in)) {
            int pointsCount = dis.readInt();
            FunctionPoint[] points = new FunctionPoint[pointsCount];

            for (int i = 0; i < pointsCount; i++) {
                double x = dis.readDouble();
                double y = dis.readDouble();
                points[i] = new FunctionPoint(x, y);
            }

            return createTabulatedFunction(points); // Используем фабрику вместо new ArrayTabulatedFunction(points)
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при вводе функции", e);
        }
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) {
        try (PrintWriter writer = new PrintWriter(out)) {
            int pointsCount = function.getPointsCount();
            writer.print(pointsCount);

            for (int i = 0; i < pointsCount; i++) {
                writer.print(" " + function.getPointX(i));
                writer.print(" " + function.getPointY(i));
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при записи функции", e);
        }
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) {
        try {
            StreamTokenizer tokenizer = new StreamTokenizer(in);
            tokenizer.parseNumbers();

            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new RuntimeException("Ожидалось количество точек");
            }
            int pointsCount = (int) tokenizer.nval;

            FunctionPoint[] points = new FunctionPoint[pointsCount];

            for (int i = 0; i < pointsCount; i++) {
                if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                    throw new RuntimeException("Ожидалась координата X");
                }
                double x = tokenizer.nval;

                if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                    throw new RuntimeException("Ожидалась координата Y");
                }
                double y = tokenizer.nval;

                points[i] = new FunctionPoint(x, y);
            }

            return createTabulatedFunction(points); // Используем фабрику вместо new ArrayTabulatedFunction(points)
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении функции", e);
        }
    }

    public static TabulatedFunction inputTabulatedFunction(Class<?> functionClass, InputStream in) {
        try (DataInputStream dis = new DataInputStream(in)) {
            int pointsCount = dis.readInt();
            FunctionPoint[] points = new FunctionPoint[pointsCount];

            for (int i = 0; i < pointsCount; i++) {
                double x = dis.readDouble();
                double y = dis.readDouble();
                points[i] = new FunctionPoint(x, y);
            }

            return createTabulatedFunction(functionClass, points); // Используем рефлексию
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при вводе функции", e);
        }
    }

    public static TabulatedFunction readTabulatedFunction(Class<?> functionClass, Reader in) {
        try {
            StreamTokenizer tokenizer = new StreamTokenizer(in);
            tokenizer.parseNumbers();

            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new RuntimeException("Ожидалось количество точек");
            }
            int pointsCount = (int) tokenizer.nval;

            FunctionPoint[] points = new FunctionPoint[pointsCount];

            for (int i = 0; i < pointsCount; i++) {
                if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                    throw new RuntimeException("Ожидалась координата X");
                }
                double x = tokenizer.nval;

                if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                    throw new RuntimeException("Ожидалась координата Y");
                }
                double y = tokenizer.nval;

                points[i] = new FunctionPoint(x, y);
            }

            return createTabulatedFunction(functionClass, points); // Используем рефлексию
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении функции", e);
        }
    }

    // Перегруженные методы tabulate с использованием рефлексии
    public static TabulatedFunction tabulate(Class<?> functionClass, Function function, double leftX, double rightX, int pointsCount) {
        TabulatedFunction tabulatedFunction = createTabulatedFunction(functionClass, leftX, rightX, pointsCount);

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            tabulatedFunction.setPointY(i, y);
        }

        return tabulatedFunction;
    }

    public static TabulatedFunction tabulate(Class<?> functionClass, Function function, double leftX, double rightX, double samplingStep) {
        if (samplingStep <= 0) {
            throw new IllegalArgumentException("Шаг табуляции должен быть положительным");
        }

        int pointsCount = (int) Math.ceil((rightX - leftX) / samplingStep) + 1;
        TabulatedFunction tabulatedFunction = createTabulatedFunction(functionClass, leftX, rightX, pointsCount);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * samplingStep;
            if (x > rightX) x = rightX;
            double y = function.getFunctionValue(x);
            tabulatedFunction.setPointY(i, y);
        }

        return tabulatedFunction;
    }

    // Существующие методы с использованием фабрики
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        TabulatedFunction tabulatedFunction = createTabulatedFunction(leftX, rightX, pointsCount);

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            tabulatedFunction.setPointY(i, y);
        }

        return tabulatedFunction;
    }

    public static void tabulate(Function function, TabulatedFunction tabulatedFunction) {
        int pointsCount = tabulatedFunction.getPointsCount();
        double leftX = tabulatedFunction.getPointX(0);
        double rightX = tabulatedFunction.getPointX(pointsCount - 1);

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            tabulatedFunction.setPointY(i, y);
        }
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, double samplingStep) {
        if (samplingStep <= 0) {
            throw new IllegalArgumentException("Шаг табуляции должен быть положительным");
        }

        int pointsCount = (int) Math.ceil((rightX - leftX) / samplingStep) + 1;
        TabulatedFunction tabulatedFunction = createTabulatedFunction(leftX, rightX, pointsCount);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * samplingStep;
            if (x > rightX) x = rightX;
            double y = function.getFunctionValue(x);
            tabulatedFunction.setPointY(i, y);
        }

        return tabulatedFunction;
    }
}