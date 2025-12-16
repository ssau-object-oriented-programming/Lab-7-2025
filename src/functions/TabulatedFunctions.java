package functions;

import java.io.*;
import java.util.*;
import java.io.StreamTokenizer;
import java.lang.reflect.Constructor;

public class TabulatedFunctions {
    // НОВОЕ для лабы 7: статическое поле фабрики
    private static TabulatedFunctionFactory factory =
            new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    // НОВОЕ для лабы 7: метод для установки фабрики
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }

    // НОВОЕ для лабы 7: методы создания через фабрику
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    // НОВОЕ для лабы 7: методы создания через рефлексию
    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass,
            double leftX, double rightX, int pointsCount) {
        try {
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(double.class, double.class, int.class);
            return constructor.newInstance(leftX, rightX, pointsCount);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create tabulated function", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass,
            double leftX, double rightX, double[] values) {
        try {
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(double.class, double.class, double[].class);
            return constructor.newInstance(leftX, rightX, values);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create tabulated function", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass,
            FunctionPoint[] points) {
        try {
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object)points);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create tabulated function", e);
        }
    }

    // Существующие методы, но теперь используют фабрику
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше двух");
        }

        // ИЗМЕНЕНО: теперь используем фабрику
        TabulatedFunction tabulatedFunc = createTabulatedFunction(leftX, rightX, pointsCount);

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            tabulatedFunc.setPointY(i, function.getFunctionValue(x));
        }

        return tabulatedFunc;
    }

    // НОВОЕ для лабы 7: перегруженный метод tabulate с рефлексией
    public static TabulatedFunction tabulate(
            Class<? extends TabulatedFunction> functionClass,
            Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше двух");
        }

        // Используем рефлексию
        TabulatedFunction tabulatedFunc = createTabulatedFunction(functionClass, leftX, rightX, pointsCount);

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            tabulatedFunc.setPointY(i, function.getFunctionValue(x));
        }

        return tabulatedFunc;
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        int pointsCount = function.getPointsCount();
        dos.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            dos.writeDouble(function.getPointX(i));
            dos.writeDouble(function.getPointY(i));
        }
        dos.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        int pointsCount = dis.readInt();
        double[] yValues = new double[pointsCount];
        double leftX = dis.readDouble();
        double rightX = leftX; // будем пересчитывать rightX
        yValues[0] = dis.readDouble();
        for (int i = 1; i < pointsCount; i++) {
            double x = dis.readDouble();
            yValues[i] = dis.readDouble();
            rightX = x;
        }

        // ИЗМЕНЕНО: теперь используем фабрику
        return createTabulatedFunction(leftX, rightX, yValues);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        BufferedWriter bw = new BufferedWriter(out);
        int pointsCount = function.getPointsCount();
        bw.write(String.valueOf(pointsCount));
        bw.write(" ");
        for (int i = 0; i < pointsCount; i++) {
            bw.write(function.getPointX(i) + " " + function.getPointY(i) + " ");
        }
        bw.newLine();
        bw.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int pointsCount = (int) st.nval;
        double[] xValues = new double[pointsCount];
        double[] yValues = new double[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            st.nextToken();
            xValues[i] = st.nval;
            st.nextToken();
            yValues[i] = st.nval;
        }

        // ИЗМЕНЕНО: теперь используем фабрику
        return createTabulatedFunction(xValues[0], xValues[pointsCount - 1], yValues);
    }
}