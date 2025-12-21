package functions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class TabulatedFunctions {
    private static final double EPS = Math.ulp(1.0);
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newFactory) {
        if (newFactory == null) {
            throw new IllegalArgumentException("Factory must not be null");
        }
        factory = newFactory;
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

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz,
                                                            double leftX, double rightX, int pointsCount) {
        return invokeConstructor(clazz, new Class<?>[]{double.class, double.class, int.class}, leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz,
                                                            double leftX, double rightX, double[] values) {
        return invokeConstructor(clazz, new Class<?>[]{double.class, double.class, double[].class}, leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz,
                                                            FunctionPoint[] points) {
        return invokeConstructor(clazz, new Class<?>[]{FunctionPoint[].class}, (Object) points);
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        FunctionPoint[] points = buildPoints(function, leftX, rightX, pointsCount);
        return createTabulatedFunction(points);
    }

    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> clazz, Function function,
                                             double leftX, double rightX, int pointsCount) {
        FunctionPoint[] points = buildPoints(function, leftX, rightX, pointsCount);
        return createTabulatedFunction(clazz, points);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(out));
        dataOut.writeInt(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            FunctionPoint point = function.getPoint(i);
            dataOut.writeDouble(point.getX());
            dataOut.writeDouble(point.getY());
        }
        dataOut.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(new BufferedInputStream(in));
        int pointsCount = dataIn.readInt();
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(points);
    }

    public static TabulatedFunction inputTabulatedFunction(Class<? extends TabulatedFunction> clazz, InputStream in)
            throws IOException {
        DataInputStream dataIn = new DataInputStream(new BufferedInputStream(in));
        int pointsCount = dataIn.readInt();
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(clazz, points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        BufferedWriter writer = new BufferedWriter(out);
        StringBuilder builder = new StringBuilder();
        builder.append(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            FunctionPoint point = function.getPoint(i);
            builder.append(' ')
                   .append(point.getX())
                   .append(' ')
                   .append(point.getY());
        }
        writer.write(builder.toString());
        writer.newLine();
        writer.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(new BufferedReader(in));
        int pointsCount = (int) nextNumber(tokenizer);
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = nextNumber(tokenizer);
            double y = nextNumber(tokenizer);
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(points);
    }

    public static TabulatedFunction readTabulatedFunction(Class<? extends TabulatedFunction> clazz, Reader in)
            throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(new BufferedReader(in));
        int pointsCount = (int) nextNumber(tokenizer);
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = nextNumber(tokenizer);
            double y = nextNumber(tokenizer);
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(clazz, points);
    }

    private static FunctionPoint[] buildPoints(Function function, double leftX, double rightX, int pointsCount) {
        if (function == null) {
            throw new IllegalArgumentException("Function must not be null");
        }
        if (!(rightX - leftX > EPS)) {
            throw new IllegalArgumentException("leftX must be less than rightX");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("At least two points are required");
        }
        if (leftX < function.getLeftDomainBorder() - EPS || rightX > function.getRightDomainBorder() + EPS) {
            throw new IllegalArgumentException("Segment is outside function domain");
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            if (Double.isNaN(y)) {
                throw new IllegalArgumentException("Function value is undefined inside domain at x=" + x);
            }
            points[i] = new FunctionPoint(x, y);
        }
        return points;
    }

    private static TabulatedFunction invokeConstructor(Class<? extends TabulatedFunction> clazz, Class<?>[] parameterTypes,
                                                       Object... args) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class reference must not be null");
        }
        try {
            Constructor<? extends TabulatedFunction> ctor = clazz.getConstructor(parameterTypes);
            return ctor.newInstance(args);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Failed to create tabulated function via reflection", e);
        }
    }

    private static double nextNumber(StreamTokenizer tokenizer) throws IOException {
        int token = tokenizer.nextToken();
        if (token != StreamTokenizer.TT_NUMBER) {
            throw new IOException("Expected number token");
        }
        return tokenizer.nval;
    }
}
