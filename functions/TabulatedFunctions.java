package functions;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

public final class TabulatedFunctions {

    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() {
    }

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> Class, FunctionPoint[] points) {
        try {
            return Class.getConstructor(FunctionPoint[].class).newInstance((Object) points);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Не найден конструктор " +e.getMessage());
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка при создании" + e.getMessage());
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> Class, double leftX, double rightX, int pointsCount) {
        try {
            return Class.getConstructor(double.class, double.class, int.class).newInstance(leftX, rightX, pointsCount);
            
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Не найден конструктор " +e.getMessage());
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка при создании" + e.getMessage());
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> Class, double leftX, double rightX, double[] values) {
        try {
            return Class.getConstructor(double.class, double.class, double[].class).newInstance(leftX, rightX, values);
            
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Не найден конструктор " +e.getMessage());    
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка при создании" + e.getMessage());
        }
    }

    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> Class, Function function, double leftX, double rightX, int pointsCount) {
        if(leftX >= rightX){
            throw new IllegalArgumentException("Левая граница больше или равна правой");
        }
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Заданные границы выходят за область определения");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Требуется не менее 2 точек");
        }
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }

        return createTabulatedFunction(Class, points);
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
            if(leftX >= rightX){
                throw new IllegalArgumentException("Левая граница больше или равна правой");
            }
            if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
                throw new IllegalArgumentException("Заданные границы выходят за область определения");
            }
            if (pointsCount < 2) {
                throw new IllegalArgumentException("Требуется не менее 2 точек");
            }
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            double step = (rightX - leftX) / (pointsCount - 1);
            
            for (int i = 0; i < pointsCount; i++) {
                double x = leftX + i * step;
                points[i] = new FunctionPoint(x, function.getFunctionValue(x));
            }

            return createTabulatedFunction(points);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeInt(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); ++i) {
            FunctionPoint point = function.getPoint(i);
            dataOut.writeDouble(point.getX());
            dataOut.writeDouble(point.getY());
        }
        dataOut.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(Class<? extends TabulatedFunction> Class, InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in); 
        int pointCount = dis.readInt();
        FunctionPoint[] points = new FunctionPoint[pointCount];

        for (int i = 0; i < pointCount; i++) {
            points[i] = new FunctionPoint(dis.readDouble(), dis.readDouble());
        }
        return createTabulatedFunction(Class, points);
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in); 
        int pointCount = dis.readInt();
        FunctionPoint[] points = new FunctionPoint[pointCount];

        for (int i = 0; i < pointCount; i++) {
            points[i] = new FunctionPoint(dis.readDouble(), dis.readDouble());
        }
        return createTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        BufferedWriter Writer = new BufferedWriter(out);
        int pointsCount = function.getPointsCount();
        Writer.write(" " + pointsCount);

        for (int i = 0; i < pointsCount; i++) {
            FunctionPoint point = function.getPoint(i);
            Writer.write("\n " + point.getX());
            Writer.write(" " + point.getY());
        }
        Writer.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Class<? extends TabulatedFunction> Class, Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int pointsCount = (int) st.nval;
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        
        for (int i = 0; i < pointsCount; i++) {
            st.nextToken();
            double x = st.nval;
            st.nextToken();
            double y = st.nval;
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(Class, points);
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int pointsCount = (int) st.nval;
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        
        for (int i = 0; i < pointsCount; i++) {
            st.nextToken();
            double x = st.nval;
            st.nextToken();
            double y = st.nval;
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(points);
    }
}




