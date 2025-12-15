package functions;

import static functions.FunctionPoint.*;

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
import java.util.Locale;
import java.util.stream.Stream;

public final class TabulatedFunctions {

    private static TabulatedFunctionFactory factory = new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory();

    private TabulatedFunctions(){}

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount){
        if (largeThan(rightX, function.getRightDomainBorder()) || largeThan(function.getLeftDomainBorder(), leftX)){
            throw new IllegalArgumentException("Границы выходят за область определения функции");
        }

        // Выясняем значение x для каждой точки
        double[] values = new double[pointsCount];
        double delta = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount - 1; ++i){
            values[i] = function.getFunctionValue(leftX + i*delta);
        }
        values[pointsCount-1] = function.getFunctionValue(rightX);

        //return new LinkedListTabulatedFunction(leftX, rightX, values);
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException{

        try(BufferedOutputStream buffOut = new BufferedOutputStream(out);
        DataOutputStream dataBuffOut = new DataOutputStream(buffOut);){
        int pointsCount = function.getPointsCount();
        dataBuffOut.writeInt(pointsCount);

        for (int i = 0; i < pointsCount; i++){
            dataBuffOut.writeDouble(function.getPointX(i));
            dataBuffOut.writeDouble(function.getPointY(i));
        }
        dataBuffOut.flush();
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException{

        try(BufferedInputStream buffIn = new BufferedInputStream(in);
        DataInputStream dataBuffIn = new DataInputStream(buffIn);){

            
            int pointsCount = dataBuffIn.readInt();
            FunctionPoint[] points = new FunctionPoint[pointsCount];


            for (int i = 0; i < pointsCount; i++){
                double x = dataBuffIn.readDouble();
                double y = dataBuffIn.readDouble();

                points[i] = new FunctionPoint(x, y);        
            }

            return new ArrayTabulatedFunction(points);
        }

        
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException{
        try{
        StreamTokenizer tokenizeIn = new StreamTokenizer(in);
        
        tokenizeIn.parseNumbers();
        tokenizeIn.nextToken(); // Переход к следующему токену

        int pointsCount = (int) tokenizeIn.nval;

        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; ++i){

            tokenizeIn.nextToken();
            double x = (double) tokenizeIn.nval;
                        
            tokenizeIn.nextToken();
            double y = (double) tokenizeIn.nval;

            points[i] = new FunctionPoint(x, y);

        }

        return new ArrayTabulatedFunction(points);
    } catch(IOException e){
        throw new IOException();
    }
    }
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException{
        int pointsCount = function.getPointsCount();

        try(BufferedWriter buffOut = new BufferedWriter(out);){
            buffOut.write(String.format("%d", pointsCount));

            for (int i = 0; i < pointsCount; i++) {
                buffOut.write(String.format(Locale.US, " %.3f %.3f", function.getPointX(i), function.getPointY(i)));
            }
        }catch(IOException e){
            throw new IOException("Ошибка при записи значений табулированной функции: " + e.getMessage());
        }
    }

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newFactory){
        factory = newFactory;
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass,
            double leftX, double rightX, int pointsCount) {

        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(double.class,
                    double.class, int.class);

            return constructor.newInstance(leftX, rightX, pointsCount);

        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Ошибка в создании объекта " + functionClass.getName(), e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass,
            double leftX, double rightX, double[] values) {

        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(double.class,
                    double.class, double[].class);

            return constructor.newInstance(leftX, rightX, values);

        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Ошибка в создании объекта " + functionClass.getName(), e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass,
            FunctionPoint[] points) {

        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(FunctionPoint[].class);

            return constructor.newInstance((Object) points);

        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Ошибка в создании объекта " + functionClass.getName(), e);
        }
    }


    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> functionClass,
            Function function, double leftX, double rightX, int pointsCount) {

        if (largeThan(rightX, function.getRightDomainBorder())
                || largeThan(function.getLeftDomainBorder(), leftX)) {
            throw new IllegalArgumentException("Границы выходят за область определения функции");
        }

        try {

            Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(double.class,
                    double.class, int.class);

            TabulatedFunction tabulatedFunction = constructor.newInstance(leftX, rightX, pointsCount);
            double delta = (rightX - leftX) / (pointsCount - 1);
            for (int i = 0; i < pointsCount; ++i) {
                double x = leftX + i * delta;
                tabulatedFunction.setPointY(i, function.getFunctionValue(x));
            }

            return tabulatedFunction;

        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Ошибка при создании объекта " + functionClass.getName(), e);
        }
    }

}
