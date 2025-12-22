package functions;

import java.io.*;
import java.lang.reflect.Constructor;

public class TabulatedFunctions {

    private static TabulatedFunctionFactory factory =
            new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory f) {
        factory = f;
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
            Class<? extends TabulatedFunction> clazz,
            double leftX, double rightX, int pointsCount) {
        try {
            Constructor<? extends TabulatedFunction> c =
                    clazz.getConstructor(double.class, double.class, int.class);
            return c.newInstance(leftX, rightX, pointsCount);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> clazz,
            double leftX, double rightX, double[] values) {
        try {
            Constructor<? extends TabulatedFunction> c =
                    clazz.getConstructor(double.class, double.class, double[].class);
            return c.newInstance(leftX, rightX, values);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> clazz,
            FunctionPoint[] points) {
        try {
            Constructor<? extends TabulatedFunction> c =
                    clazz.getConstructor(FunctionPoint[].class);
            return c.newInstance((Object) points);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction tabulate(Function f, double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2 || leftX >= rightX)
            throw new IllegalArgumentException();

        double step = (rightX - leftX) / (pointsCount - 1);
        double[] values = new double[pointsCount];
        for (int i = 0; i < pointsCount; i++)
            values[i] = f.getFunctionValue(leftX + i * step);

        return createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction tabulate(
            Class<? extends TabulatedFunction> clazz,
            Function f, double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2 || leftX >= rightX)
            throw new IllegalArgumentException();

        double step = (rightX - leftX) / (pointsCount - 1);
        double[] values = new double[pointsCount];
        for (int i = 0; i < pointsCount; i++)
            values[i] = f.getFunctionValue(leftX + i * step);

        return createTabulatedFunction(clazz, leftX, rightX, values);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            dos.writeDouble(function.getPointX(i));
            dos.writeDouble(function.getPointY(i));
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        int count = dis.readInt();
        double[] x = new double[count];
        double[] y = new double[count];
        for (int i = 0; i < count; i++) {
            x[i] = dis.readDouble();
            y[i] = dis.readDouble();
        }
        return createTabulatedFunction(x[0], x[count - 1], y);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter pw = new PrintWriter(out);
        int count = function.getPointsCount();
        pw.print(count);
        for (int i = 0; i < count; i++)
            pw.print(" " + function.getPointX(i) + " " + function.getPointY(i));
        pw.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int count = (int) st.nval;
        FunctionPoint[] points = new FunctionPoint[count];
        for (int i = 0; i < count; i++) {
            st.nextToken();
            double x = st.nval;
            st.nextToken();
            double y = st.nval;
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(points);
    }
    
    public static TabulatedFunction inputTabulatedFunction(
            Class<? extends TabulatedFunction> clazz,
            InputStream in) throws IOException {

        DataInputStream dis = new DataInputStream(in);
        int count = dis.readInt();

        double[] x = new double[count];
        double[] y = new double[count];

        for (int i = 0; i < count; i++) {
            x[i] = dis.readDouble();
            y[i] = dis.readDouble();
        }

        try {
            Constructor<? extends TabulatedFunction> c =
                    clazz.getConstructor(double.class, double.class, double[].class);
            return c.newInstance(x[0], x[count - 1], y);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction readTabulatedFunction(
            Class<? extends TabulatedFunction> clazz,
            Reader in) throws IOException {

        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int count = (int) st.nval;

        FunctionPoint[] points = new FunctionPoint[count];

        for (int i = 0; i < count; i++) {
            st.nextToken();
            double x = st.nval;
            st.nextToken();
            double y = st.nval;
            points[i] = new FunctionPoint(x, y);
        }

        try {
            Constructor<? extends TabulatedFunction> c =
                    clazz.getConstructor(FunctionPoint[].class);
            return c.newInstance((Object) points);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
