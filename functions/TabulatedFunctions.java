package functions;

import java.io.*;
import java.lang.reflect.*;

public class TabulatedFunctions {
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    // приватный конструктор
    private TabulatedFunctions() {}

    // устанавливает фабрику для создания табулированных ф-ций
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }

    // создает табулированную ф-цию с нулевыми значениями (фабрика)
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    // создает табулированную ф-цию с заданными значениями (фабрика)
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    // создает табулированную ф-цию из массива точек (фабрика)
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    // создает табулированную ф-цию с нулевыми значениями (рефлексия)
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass,
                                                            double leftX, double rightX, int pointsCount) {
        try {
            // получаем конструктор с нужными параметрами
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(double.class, double.class, int.class);
            // создаем объект
            return constructor.newInstance(leftX, rightX, pointsCount);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new IllegalArgumentException("ошибка при создании объекта через рефлексию", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("ошибка при создании объекта через рефлексию", e);
        }
    }

    // создает табулированную ф-цию с заданными значениями (рефлексия)
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass,
                                                            double leftX, double rightX, double[] values) {
        try {
            // получаем конструктор с нужными параметрами
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(double.class, double.class, double[].class);
            // создаем объект
            return constructor.newInstance(leftX, rightX, values);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new IllegalArgumentException("ошибка при создании объекта через рефлексию", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("ошибка при создании объекта через рефлексию", e);
        }
    }

    // создает табулированную ф-цию из массива точек (рефлексия)
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass,
                                                            FunctionPoint[] points) {
        try {
            // получаем конструктор с нужными параметрами
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(FunctionPoint[].class);
            // создаем объект
            return constructor.newInstance((Object) points);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new IllegalArgumentException("ошибка при создании объекта через рефлексию", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("ошибка при создании объекта через рефлексию", e);
        }
    }

    // табулирует ф-цию (фабрика)
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        // проверка границ
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("границы табулирования выходят за область определения ф-ции");
        }

        // проверка минимального количества точек
        if (pointsCount < 2) {
            throw new IllegalArgumentException("количество точек должно быть не менее 2");
        }

        // проверка интервала
        if (leftX >= rightX) {
            throw new IllegalArgumentException("левая граница должна быть меньше правой");
        }

        // создаем массив значений ф-ции
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        // вычисляем значения ф-ции
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        // используем фабрику для создания табулированной ф-ции
        return createTabulatedFunction(leftX, rightX, values);
    }

    // табулирует ф-цию (рефлексия)
    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> functionClass,
                                             Function function, double leftX, double rightX, int pointsCount) {
        // проверка границ
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("границы табулирования выходят за область определения ф-ции");
        }

        // проверка минимального количества точек
        if (pointsCount < 2) {
            throw new IllegalArgumentException("количество точек должно быть не менее 2");
        }

        // проверка интервала
        if (leftX >= rightX) {
            throw new IllegalArgumentException("левая граница должна быть меньше правой");
        }

        // создаем массив значений ф-ции
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        // вычисляем значения ф-ции
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        // используем рефлексию для создания табулированной ф-ции
        return createTabulatedFunction(functionClass, leftX, rightX, values);
    }

    // вывод табулированной ф-ции в байтовый поток
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);

        // записываем кол-во точек
        dataOut.writeInt(function.getPointsCount());

        // записываем координаты всех точек
        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }

        dataOut.flush();
    }

    // ввод табулированной ф-ции из байтового потока (фабрика)
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);

        // читаем кол-во точек
        int pointsCount = dataIn.readInt();

        // читаем координаты точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        // используем фабрику для создания табулированной ф-ции
        return createTabulatedFunction(points);
    }

    // ввод табулированной ф-ции из байтового потока (рефлексия)
    public static TabulatedFunction inputTabulatedFunction(Class<? extends TabulatedFunction> functionClass,
                                                           InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);

        // читаем кол-во точек
        int pointsCount = dataIn.readInt();

        // читаем координаты точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        // используем рефлексию для создания табулированной ф-ции
        return createTabulatedFunction(functionClass, points);
    }

    // запись ф-ции в символьный поток
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter writer = new PrintWriter(out);

        // записываем кол-во точек
        writer.print(function.getPointsCount());
        writer.print(" ");

        for (int i = 0; i < function.getPointsCount(); i++) {
            writer.print(function.getPointX(i));
            writer.print(" ");
            writer.print(function.getPointY(i));
            if (i < function.getPointsCount() - 1) {
                writer.print(" ");
            }
        }

        writer.flush();
    }

    // считывание ф-ции из символьного потока (фабрика)
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("передан нулевой поток ввода");
        }

        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.parseNumbers();

        // считываем число точек
        int tokenType = tokenizer.nextToken();
        if (tokenType != StreamTokenizer.TT_NUMBER) {
            throw new IOException("не найдено количество точек");
        }
        int count = (int) tokenizer.nval;

        // создаем массив для хранения точек
        FunctionPoint[] functionPoints = new FunctionPoint[count];

        // считываем пары
        for (int i = 0; i < count; i++) {
            // считываем x координату
            tokenType = tokenizer.nextToken();
            if (tokenType != StreamTokenizer.TT_NUMBER) {
                throw new IOException("отсутствует x координата для точки " + i);
            }
            double xCoord = tokenizer.nval;

            // считываем y координату
            tokenType = tokenizer.nextToken();
            if (tokenType != StreamTokenizer.TT_NUMBER) {
                throw new IOException("отсутствует y координата для точки " + i);
            }
            double yCoord = tokenizer.nval;

            // создаем точку с полученными координатами
            functionPoints[i] = new FunctionPoint(xCoord, yCoord);
        }

        // используем фабрику для создания табулированной ф-ции
        return createTabulatedFunction(functionPoints);
    }

    // считывание ф-ции из символьного потока (рефлексия)
    public static TabulatedFunction readTabulatedFunction(Class<? extends TabulatedFunction> functionClass,
                                                          Reader in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("передан нулевой поток ввода");
        }

        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.parseNumbers();

        // считываем число точек
        int tokenType = tokenizer.nextToken();
        if (tokenType != StreamTokenizer.TT_NUMBER) {
            throw new IOException("не найдено количество точек");
        }
        int count = (int) tokenizer.nval;

        // создаем массив для хранения точек
        FunctionPoint[] functionPoints = new FunctionPoint[count];

        // считываем пары
        for (int i = 0; i < count; i++) {
            // считываем x координату
            tokenType = tokenizer.nextToken();
            if (tokenType != StreamTokenizer.TT_NUMBER) {
                throw new IOException("отсутствует x координата для точки " + i);
            }
            double xCoord = tokenizer.nval;

            // считываем y координату
            tokenType = tokenizer.nextToken();
            if (tokenType != StreamTokenizer.TT_NUMBER) {
                throw new IOException("отсутствует y координата для точки " + i);
            }
            double yCoord = tokenizer.nval;

            // создаем точку с полученными координатами
            functionPoints[i] = new FunctionPoint(xCoord, yCoord);
        }

        // используем рефлексию для создания табулированной ф-ции
        return createTabulatedFunction(functionClass, functionPoints);
    }
}