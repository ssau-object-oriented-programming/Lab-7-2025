package functions;

import java.io.*;
import java.lang.reflect.Constructor;

public class TabulatedFunctions {

    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() {}

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newFactory) {
        if (newFactory == null) {
            throw new IllegalArgumentException("Factory cannot be null");
        }
        factory = newFactory;
    }

    // === ФАБРИЧНЫЕ МЕТОДЫ ===
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points, int count) {
        return factory.createTabulatedFunction(points, count);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    // === РЕФЛЕКСИВНЫЕ МЕТОДЫ ===
    public static <T extends TabulatedFunction> T createTabulatedFunction(
            Class<T> clazz, FunctionPoint[] points, int count) {
        try {
            Constructor<T> ctor = clazz.getConstructor(FunctionPoint[].class, int.class);
            return ctor.newInstance(points, count);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create instance of " + clazz, e);
        }
    }

    public static <T extends TabulatedFunction> T createTabulatedFunction(
            Class<T> clazz, double leftX, double rightX, int pointCount) {
        try {
            Constructor<T> ctor = clazz.getConstructor(double.class, double.class, int.class);
            return ctor.newInstance(leftX, rightX, pointCount);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create instance of " + clazz, e);
        }
    }

    public static <T extends TabulatedFunction> T createTabulatedFunction(
            Class<T> clazz, double leftX, double rightX, double[] values) {
        try {
            Constructor<T> ctor = clazz.getConstructor(double.class, double.class, double[].class);
            return ctor.newInstance(leftX, rightX, values);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create instance of " + clazz, e);
        }
    }

    // === ТАБУЛИРОВАНИЕ ===
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2 || leftX >= rightX) {
            throw new IllegalArgumentException("Invalid point count or domain");
        }
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Tabulation domain outside function domain");
        }

        FunctionPoint[] pointArray = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + step * i;
            pointArray[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }
        return createTabulatedFunction(pointArray, pointsCount);
    }

    public static <T extends TabulatedFunction> T tabulate(
            Class<T> clazz, Function function, double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2 || leftX >= rightX) {
            throw new IllegalArgumentException("Invalid point count or domain");
        }
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Tabulation domain outside function domain");
        }

        FunctionPoint[] pointArray = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + step * i;
            pointArray[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }
        return createTabulatedFunction(clazz, pointArray, pointsCount);
    }

    // === ВВОД/ВЫВОД — ТОЛЬКО ОДНА ВЕРСИЯ (без дубликатов!) ===

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(out)) {
            int n = function.getPointCount();
            dos.writeInt(n);
            for (int i = 0; i < n; i++) {
                dos.writeDouble(function.getPointX(i));
                dos.writeDouble(function.getPointY(i));
            }
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        try (DataInputStream dis = new DataInputStream(in)) {
            int n = dis.readInt();
            FunctionPoint[] points = new FunctionPoint[n];
            for (int i = 0; i < n; i++) {
                points[i] = new FunctionPoint(dis.readDouble(), dis.readDouble());
            }
            return createTabulatedFunction(points, n);
        }
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(out)) {
            int n = function.getPointCount();
            bw.write(Integer.toString(n));
            for (int i = 0; i < n; i++) {
                bw.write(' ');
                bw.write(Double.toString(function.getPointX(i)));
                bw.write(' ');
                bw.write(Double.toString(function.getPointY(i)));
            }
        }
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        if (st.nextToken() != StreamTokenizer.TT_NUMBER) {
            throw new IOException("Expected number of points");
        }
        int n = (int) st.nval;
        FunctionPoint[] points = new FunctionPoint[n];
        for (int i = 0; i < n; i++) {
            if (st.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Expected x");
            double x = st.nval;
            if (st.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Expected y");
            double y = st.nval;
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(points, n);
    }

    // === ВВОД С УКАЗАНИЕМ КЛАССА (рефлексия) ===

    public static <T extends TabulatedFunction> T inputTabulatedFunction(Class<T> clazz, InputStream in) throws IOException {
        try (DataInputStream dis = new DataInputStream(in)) {
            int n = dis.readInt();
            FunctionPoint[] points = new FunctionPoint[n];
            for (int i = 0; i < n; i++) {
                points[i] = new FunctionPoint(dis.readDouble(), dis.readDouble());
            }
            return createTabulatedFunction(clazz, points, n);
        }
    }

    public static <T extends TabulatedFunction> T readTabulatedFunction(Class<T> clazz, Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        if (st.nextToken() != StreamTokenizer.TT_NUMBER) {
            throw new IOException("Expected number of points");
        }
        int n = (int) st.nval;
        FunctionPoint[] points = new FunctionPoint[n];
        for (int i = 0; i < n; i++) {
            if (st.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Expected x");
            double x = st.nval;
            if (st.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Expected y");
            double y = st.nval;
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(clazz, points, n);
    }
}