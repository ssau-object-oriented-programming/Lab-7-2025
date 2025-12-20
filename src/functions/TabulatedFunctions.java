package functions;

import java.io.*;
import java.lang.reflect.*;
import static functions.DoubleComparison.*;

public class TabulatedFunctions {

    private TabulatedFunctions() {}

    private static TabulatedFunctionFactory factory = new
            LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory();

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }

    //фабрика
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    //рефлексия
    public static TabulatedFunction createTabulatedFunction(Class<?> clas, double leftX, double rightX, int pointsCount) {
        checkClass(clas);
        try {
            Constructor<?> constructor = clas.getConstructor(double.class, double.class, int.class);

            return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);

        } catch (Exception e) {
            throw new IllegalArgumentException("При создания объекта через рефлексию произошла ошибка ", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> clas, double leftX, double rightX, double[] values) {
        checkClass(clas);
        try {
            Constructor<?> constructor = clas.getConstructor(double.class, double.class, double[].class);

            return (TabulatedFunction) constructor.newInstance(leftX, rightX, values);

        } catch (Exception e) {
            throw new IllegalArgumentException("При создания объекта через рефлексию произошла ошибка ", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> clas, FunctionPoint[] points) {
        checkClass(clas);
        try {
            Constructor<?> constructor = clas.getConstructor(FunctionPoint[].class);

            return (TabulatedFunction) constructor.newInstance((Object) points);

        } catch (Exception e) {
            throw new IllegalArgumentException("При создания объекта через рефлексию произошла ошибка ", e);
        }
    }

    private static void checkClass(Class<?> clas) {
        if (!TabulatedFunction.class.isAssignableFrom(clas)) {
            throw new IllegalArgumentException("Класс должен реализовывать TabulatedFunction");
        }
    }

    //табулирование
    //фабрика
    public static TabulatedFunction tabulate(Function f, double leftX, double rightX, int pointsCount) {

        if (doubleLess(leftX, f.getLeftDomainBorder()) || doubleGreater(rightX, f.getRightDomainBorder())) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }

        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек табулирования должно быть не менее 2");
        }

        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = f.getFunctionValue(x);
        }

        return createTabulatedFunction(leftX, rightX, values);
    }
    //рефлексия
    public static TabulatedFunction tabulate(Class<?> clas, Function f, double leftX,
                                             double rightX, int pointsCount) {
        checkClass(clas);

        if (doubleLess(leftX, f.getLeftDomainBorder()) || doubleGreater(rightX, f.getRightDomainBorder())) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }

        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек табулирования должно быть не менее 2");
        }

        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = f.getFunctionValue(x);
        }

        return createTabulatedFunction(clas, leftX, rightX, values);
    }


    //Чтение табулированной функции из байтового потока
    //фабрика
    public static TabulatedFunction inputTabulatedFunction(InputStream in) {
        try (DataInputStream dataIn = new DataInputStream(in)) {

            int count = dataIn.readInt();
            FunctionPoint[] points = new FunctionPoint[count];

            for (int i = 0; i < count; i++) {
                double x = dataIn.readDouble();
                double y = dataIn.readDouble();
                points[i] = new FunctionPoint(x, y);
            }

            return createTabulatedFunction(points);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка ввода табулированной функции", e);
        }
    }
    //рефлексия
    public static TabulatedFunction inputTabulatedFunction(Class<?> clas, InputStream in) {

        checkClass(clas);

        try (DataInputStream dataIn = new DataInputStream(in)) {

            int count = dataIn.readInt();
            FunctionPoint[] points = new FunctionPoint[count];

            for (int i = 0; i < count; i++) {
                double x = dataIn.readDouble();
                double y = dataIn.readDouble();
                points[i] = new FunctionPoint(x, y);
            }

            return createTabulatedFunction(clas, points);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка ввода табулированной функции", e);
        }
    }

    //Чтение табулированной функции из символьного потока
    //фабрика
    public static TabulatedFunction readTabulatedFunction(Reader in) {
        try {
            StreamTokenizer tokenizer = new StreamTokenizer(in);

            tokenizer.nextToken();
            int count = (int) tokenizer.nval;

            FunctionPoint[] points = new FunctionPoint[count];

            for (int i = 0; i < count; i++) {
                tokenizer.nextToken();
                double x = tokenizer.nval;

                tokenizer.nextToken();
                double y = tokenizer.nval;

                points[i] = new FunctionPoint(x, y);
            }

            return createTabulatedFunction(points);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения табулированной функции", e);
        }
    }

    //рефлексия
    public static TabulatedFunction readTabulatedFunction(Class<?> clas, Reader in) {

        checkClass(clas);

        try {
            StreamTokenizer tokenizer = new StreamTokenizer(in);

            tokenizer.nextToken();
            int count = (int) tokenizer.nval;

            FunctionPoint[] points = new FunctionPoint[count];

            for (int i = 0; i < count; i++) {
                tokenizer.nextToken();
                double x = tokenizer.nval;

                tokenizer.nextToken();
                double y = tokenizer.nval;

                points[i] = new FunctionPoint(x, y);
            }

            return createTabulatedFunction(clas, points);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения табулированной функции", e);
        }
    }

    //Запись табулированной функции в байтовый поток
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) {
        try (DataOutputStream dataOut = new DataOutputStream(out)) {

            int count = function.getPointsCount();
            dataOut.writeInt(count);

            for (int i = 0; i < count; i++) {
                dataOut.writeDouble(function.getPointX(i));
                dataOut.writeDouble(function.getPointY(i));
            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка вывода табулированной функции", e);
        }
    }
    //Запись табулированной функции в символьный поток
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) {
        try {
            int count = function.getPointsCount();
            out.write(Integer.toString(count));

            for (int i = 0; i < count; i++) {
                out.write(" " + function.getPointX(i) + " " + function.getPointY(i));
            }

            out.flush();

        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи табулированной функции", e);
        }
    }

}
