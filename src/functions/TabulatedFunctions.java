package functions;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class TabulatedFunctions {
    private static final double EPS = 1e-10;

    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() {}

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory f) {
        if (f == null) throw new IllegalArgumentException("Factory cannot be null");
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

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz, double leftX, double rightX, int pointsCount) {
        try {
            Constructor<? extends TabulatedFunction> ctor = clazz.getConstructor(double.class, double.class, int.class);
            return ctor.newInstance(leftX, rightX, pointsCount);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz, double leftX, double rightX, double[] values) {
        try {
            Constructor<? extends TabulatedFunction> ctor = clazz.getConstructor(double.class, double.class, double[].class);
            return ctor.newInstance(leftX, rightX, values);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz, FunctionPoint[] points) {
        try {
            Constructor<? extends TabulatedFunction> ctor = clazz.getConstructor(FunctionPoint[].class);
            return ctor.newInstance((Object) points);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> clazz, Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() - EPS || rightX > function.getRightDomainBorder() + EPS)
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        if (pointsCount < 2) throw new IllegalArgumentException("pointsCount < 2");

        double step = (rightX - leftX) / (pointsCount - 1);
        FunctionPoint[] pts = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            pts[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }
        return createTabulatedFunction(clazz, pts);
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() - EPS || rightX > function.getRightDomainBorder() + EPS)
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        if (pointsCount < 2) throw new IllegalArgumentException("pointsCount < 2");

        double step = (rightX - leftX) / (pointsCount - 1);
        FunctionPoint[] pts = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            pts[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }
        return createTabulatedFunction(pts);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(out));
        try {
            dos.writeInt(function.getPointsCount());
            for (int i = 0; i < function.getPointsCount(); i++) {
                dos.writeDouble(function.getPointX(i));
                dos.writeDouble(function.getPointY(i));
            }
            dos.flush();
        } finally {
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(new BufferedInputStream(in));
        int n = dis.readInt();
        FunctionPoint[] pts = new FunctionPoint[n];
        for (int i = 0; i < n; i++) {
            double x = dis.readDouble();
            double y = dis.readDouble();
            pts[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(pts);
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in, Class<? extends TabulatedFunction> clazz) throws IOException {
        DataInputStream dis = new DataInputStream(new BufferedInputStream(in));
        int n = dis.readInt();
        FunctionPoint[] pts = new FunctionPoint[n];
        for (int i = 0; i < n; i++) {
            double x = dis.readDouble();
            double y = dis.readDouble();
            pts[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(clazz, pts);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter pw = new PrintWriter(new BufferedWriter(out));
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(function.getPointsCount());
            for (int i = 0; i < function.getPointsCount(); i++) {
                sb.append(' ').append(function.getPointX(i)).append(' ').append(function.getPointY(i));
            }
            pw.println(sb.toString());
            pw.flush();
        } finally {
        }
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.parseNumbers();
        int n;
        if (st.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Invalid format: expected number of points");
        n = (int) st.nval;
        FunctionPoint[] pts = new FunctionPoint[n];
        for (int i = 0; i < n; i++) {
            if (st.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Invalid format: expected x");
            double x = st.nval;
            if (st.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Invalid format: expected y");
            double y = st.nval;
            pts[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(pts);
    }

    public static TabulatedFunction readTabulatedFunction(Reader in, Class<? extends TabulatedFunction> clazz) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.parseNumbers();
        int n;
        if (st.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Invalid format: expected number of points");
        n = (int) st.nval;
        FunctionPoint[] pts = new FunctionPoint[n];
        for (int i = 0; i < n; i++) {
            if (st.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Invalid format: expected x");
            double x = st.nval;
            if (st.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Invalid format: expected y");
            double y = st.nval;
            pts[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(clazz, pts);
    }
}
