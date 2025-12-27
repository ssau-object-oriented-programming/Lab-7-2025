package functions;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TabulatedFunctions {

    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions(){}
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount){
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if(leftX<function.getLeftDomainBorder()||rightX>function.getRightDomainBorder()){
            throw new IllegalArgumentException("Указанные границы для табулирования" +
                    " выходят за область определения функции");
        }
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }
        return factory.createTabulatedFunction(leftX,rightX,values);
    }
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount, Class<?extends TabulatedFunction> functionClass){
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if(leftX<function.getLeftDomainBorder()||rightX>function.getRightDomainBorder()){
            throw new IllegalArgumentException("Указанные границы для табулирования" +
                    " выходят за область определения функции");
        }
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }
        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(double.class, double.class, int.class);
            return constructor.newInstance(leftX, rightX, pointsCount);
        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot create tabulated function", e);
        }
    }
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out)throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        try {
            dataOut.writeInt(function.getPointsCount());

            for (int i = 0; i < function.getPointsCount(); i++) {
                dataOut.writeDouble(function.getPointX(i));
                dataOut.writeDouble(function.getPointY(i));
            }

            dataOut.flush();
        } finally {
        }
    }

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory){
        TabulatedFunctions.factory = factory;
    }
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount){
        return factory.createTabulatedFunction(leftX,rightX,pointsCount);
    }
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values){
        return factory.createTabulatedFunction(leftX,rightX,values);
    }
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points){
        return factory.createTabulatedFunction(points);
    }
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount, Class<?extends TabulatedFunction> functionClass){
        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(double.class, double.class, int.class);
            return constructor.newInstance(leftX, rightX, pointsCount);
        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot create tabulated function", e);
        }
    }
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values, Class<?extends TabulatedFunction> functionClass){
        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(double.class, double.class, double[].class);
            return constructor.newInstance(leftX, rightX, values);
        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot create tabulated function", e);
        }
    }
    public static TabulatedFunction createTabulatedFunction(Class<?extends TabulatedFunction> functionClass, FunctionPoint[] points){
        try {
            Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object) points);
        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot create tabulated function", e);
        }
    }
    public static TabulatedFunction inputTabulatedFunction(InputStream in)throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        try {
            int pointsCount = dataIn.readInt();

            if (pointsCount < 2) {
                throw new IOException("Некорректные данные: количество точек должно быть не менее 2");
            }

            double[] xValues = new double[pointsCount];
            double[] yValues = new double[pointsCount];
            for (int i = 0; i < pointsCount; i++) {
                xValues[i] = dataIn.readDouble();
                yValues[i] = dataIn.readDouble();
            }

            for (int i = 1; i < pointsCount; i++) {
                if (xValues[i] <= xValues[i - 1]) {
                    throw new IOException("Некорректные данные: значения X должны быть упорядочены по возрастанию");
                }
            }
            double leftX = xValues[0];
            double rightX = xValues[xValues.length - 1];
            return factory.createTabulatedFunction(leftX,rightX,yValues);

        } catch (EOFException e) {
        throw new IOException("Неожиданный конец потока: данные неполные", e);
        } catch (IllegalArgumentException e) {
        throw new IOException("Некорректные данные: " + e.getMessage(), e);
        } finally {

        }
    }
    public static TabulatedFunction inputTabulatedFunction(InputStream in, Class<?extends TabulatedFunction> functionClass)throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        try {
            int pointsCount = dataIn.readInt();

            if (pointsCount < 2) {
                throw new IOException("Некорректные данные: количество точек должно быть не менее 2");
            }

            double[] xValues = new double[pointsCount];
            double[] yValues = new double[pointsCount];
            for (int i = 0; i < pointsCount; i++) {
                xValues[i] = dataIn.readDouble();
                yValues[i] = dataIn.readDouble();
            }

            for (int i = 1; i < pointsCount; i++) {
                if (xValues[i] <= xValues[i - 1]) {
                    throw new IOException("Некорректные данные: значения X должны быть упорядочены по возрастанию");
                }
            }
            double leftX = xValues[0];
            double rightX = xValues[xValues.length - 1];
            try {
                Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(double.class, double.class, int.class);
                return constructor.newInstance(leftX, rightX, pointsCount);
            } catch (NoSuchMethodException | InstantiationException |
                     IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException("Cannot create tabulated function", e);
            }

        } catch (EOFException e) {
            throw new IOException("Неожиданный конец потока: данные неполные", e);
        } catch (IllegalArgumentException e) {
            throw new IOException("Некорректные данные: " + e.getMessage(), e);
        } finally {

        }
    }
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out)throws IOException{
        BufferedWriter writer = new BufferedWriter(out);
        try {
            writer.write(String.valueOf(function.getPointsCount()));
            writer.write(" ");

            for (int i = 0; i < function.getPointsCount(); i++) {
                writer.write(String.valueOf(function.getPointX(i)));
                writer.write(" ");
                writer.write(String.valueOf(function.getPointY(i)));

                if (i < function.getPointsCount() - 1) {
                    writer.write(" ");
                }
            }

            writer.newLine();
            writer.flush();
        } finally {
        }
    }
    public static TabulatedFunction readTabulatedFunction(Reader in)throws IOException{
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        try {
            // Настраиваем токенизатор
            tokenizer.resetSyntax();
            tokenizer.wordChars('0', '9');
            tokenizer.wordChars('.', '.');
            tokenizer.wordChars('-', '-');
            tokenizer.wordChars('e', 'e');
            tokenizer.wordChars('E', 'E');
            tokenizer.whitespaceChars(' ', ' ');
            tokenizer.whitespaceChars('\t', '\t');
            tokenizer.whitespaceChars('\n', '\n');
            tokenizer.whitespaceChars('\r', '\r');


            if (tokenizer.nextToken() != StreamTokenizer.TT_WORD) {
                throw new IOException("Ожидалось количество точек (целое число)");
            }

            int pointsCount;
            try {
                pointsCount = Integer.parseInt(tokenizer.sval);
            } catch (NumberFormatException e) {
                throw new IOException("Некорректное количество точек: " + tokenizer.sval, e);
            }

            if (pointsCount < 2) {
                throw new IOException("Некорректные данные: количество точек должно быть не менее 2");
            }


            double[] xValues = new double[pointsCount];
            double[] yValues = new double[pointsCount];

            for (int i = 0; i < pointsCount; i++) {

                if (tokenizer.nextToken() != StreamTokenizer.TT_WORD) {
                    throw new IOException("Ожидалось значение x для точки " + i);
                }

                try {
                    xValues[i] = Double.parseDouble(tokenizer.sval);
                } catch (NumberFormatException e) {
                    throw new IOException("Некорректное значение x для точки " + i + ": " + tokenizer.sval, e);
                }


                if (tokenizer.nextToken() != StreamTokenizer.TT_WORD) {
                    throw new IOException("Ожидалось значение y для точки " + i);
                }

                try {
                    yValues[i] = Double.parseDouble(tokenizer.sval);
                } catch (NumberFormatException e) {
                    throw new IOException("Некорректное значение y для точки " + i + ": " + tokenizer.sval, e);
                }
            }


            for (int i = 1; i < pointsCount; i++) {
                if (xValues[i] <= xValues[i-1]) {
                    throw new IOException("Некорректные данные: значения X должны быть упорядочены по возрастанию");
                }
            }


            double leftX = xValues[0];
            double rightX = xValues[xValues.length - 1];
            return factory.createTabulatedFunction(leftX,rightX,yValues);

        } finally {
        }
    }
    public static TabulatedFunction readTabulatedFunction(Reader in, Class<?extends TabulatedFunction> functionClass)throws IOException{
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        try {
            // Настраиваем токенизатор
            tokenizer.resetSyntax();
            tokenizer.wordChars('0', '9');
            tokenizer.wordChars('.', '.');
            tokenizer.wordChars('-', '-');
            tokenizer.wordChars('e', 'e');
            tokenizer.wordChars('E', 'E');
            tokenizer.whitespaceChars(' ', ' ');
            tokenizer.whitespaceChars('\t', '\t');
            tokenizer.whitespaceChars('\n', '\n');
            tokenizer.whitespaceChars('\r', '\r');


            if (tokenizer.nextToken() != StreamTokenizer.TT_WORD) {
                throw new IOException("Ожидалось количество точек (целое число)");
            }

            int pointsCount;
            try {
                pointsCount = Integer.parseInt(tokenizer.sval);
            } catch (NumberFormatException e) {
                throw new IOException("Некорректное количество точек: " + tokenizer.sval, e);
            }

            if (pointsCount < 2) {
                throw new IOException("Некорректные данные: количество точек должно быть не менее 2");
            }


            double[] xValues = new double[pointsCount];
            double[] yValues = new double[pointsCount];

            for (int i = 0; i < pointsCount; i++) {

                if (tokenizer.nextToken() != StreamTokenizer.TT_WORD) {
                    throw new IOException("Ожидалось значение x для точки " + i);
                }

                try {
                    xValues[i] = Double.parseDouble(tokenizer.sval);
                } catch (NumberFormatException e) {
                    throw new IOException("Некорректное значение x для точки " + i + ": " + tokenizer.sval, e);
                }


                if (tokenizer.nextToken() != StreamTokenizer.TT_WORD) {
                    throw new IOException("Ожидалось значение y для точки " + i);
                }

                try {
                    yValues[i] = Double.parseDouble(tokenizer.sval);
                } catch (NumberFormatException e) {
                    throw new IOException("Некорректное значение y для точки " + i + ": " + tokenizer.sval, e);
                }
            }


            for (int i = 1; i < pointsCount; i++) {
                if (xValues[i] <= xValues[i-1]) {
                    throw new IOException("Некорректные данные: значения X должны быть упорядочены по возрастанию");
                }
            }


            double leftX = xValues[0];
            double rightX = xValues[xValues.length - 1];
            try {
                Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(double.class, double.class, int.class);
                return constructor.newInstance(leftX, rightX, pointsCount);
            } catch (NoSuchMethodException | InstantiationException |
                     IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException("Cannot create tabulated function", e);
            }

        } finally {
        }
    }
}
