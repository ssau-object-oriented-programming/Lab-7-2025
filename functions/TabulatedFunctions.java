package functions;

import java.io.*;

public class TabulatedFunctions
{
    private static TabulatedFunctionFactory tabulatedFunctionFactory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory tff)
    {
        tabulatedFunctionFactory = tff;
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount)
            throws IllegalArgumentException
    {
        return tabulatedFunctionFactory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values)
            throws IllegalArgumentException
    {
        return tabulatedFunctionFactory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points)
            throws IllegalArgumentException
    {
        return tabulatedFunctionFactory.createTabulatedFunction(points);
    }

    // перегрузки
    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, double leftX, double rightX, int pointsCount)
            throws IllegalArgumentException
    {
        if(TabulatedFunction.class.isAssignableFrom(functionClass))
        {
            try
            {
                java.lang.reflect.Constructor<?> constructor = functionClass.getDeclaredConstructor(
                        double.class, double.class, int.class
                );

                return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);
            }
            catch (NoSuchMethodException e)
            {
                throw new IllegalArgumentException("Класс не имеет конструктора");
            }
            catch (Exception e)
            {
                throw new RuntimeException("Возникла ошибка при создании табулированной функции");
            }
        }
        else
        {
            throw new IllegalArgumentException("Был передан недопустимый класс");
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, double leftX, double rightX, double[] values)
            throws IllegalArgumentException
    {
        if(TabulatedFunction.class.isAssignableFrom(functionClass))
        {
            try
            {
                java.lang.reflect.Constructor<?> constructor = functionClass.getDeclaredConstructor(
                        double.class, double.class, double[].class
                );

                return (TabulatedFunction) constructor.newInstance(leftX, rightX, values);
            }
            catch (NoSuchMethodException e)
            {
                throw new IllegalArgumentException("Класс не имеет конструктора");
            }
            catch (Exception e)
            {
                throw new RuntimeException("Возникла ошибка при создании табулированной функции");
            }
        }
        else
        {
            throw new IllegalArgumentException("Был передан недопустимый класс");
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, FunctionPoint[] points)
            throws IllegalArgumentException
    {
        if(TabulatedFunction.class.isAssignableFrom(functionClass))
        {
            try
            {
                java.lang.reflect.Constructor<?> constructor = functionClass.getDeclaredConstructor(
                        FunctionPoint[].class
                );

                return (TabulatedFunction) constructor.newInstance((Object) points);
            }
            catch (NoSuchMethodException e)
            {
                throw new IllegalArgumentException("Класс не имеет конструктора");
            }
            catch (Exception e)
            {
                throw new RuntimeException("Возникла ошибка при создании табулированной функции");
            }
        }
        else
        {
            throw new IllegalArgumentException("Был передан недопустимый класс");
        }
    }

    public static TabulatedFunction tabulate(Class<?> functionClass, Function function, double leftX, double rightX, int pointsCount)
    {
        if(!TabulatedFunction.class.isAssignableFrom(functionClass))
        {
           throw new IllegalArgumentException("Был передан недопустимый класс");
        }
        if(function == null)
        {
            throw new IllegalArgumentException("Функция не определена");
        }

        if(leftX >= rightX)
        {
            throw new IllegalArgumentException("Левая граница не может быть больше правой");
        }

        if(pointsCount < 2)
        {
            throw new IllegalArgumentException("Количество точек не может быть меньше двух");
        }

        if(leftX < function.getLeftDomainBorder() - 1e-10 ||
                rightX > function.getRightDomainBorder() + 1e-10)
        {
            throw new IllegalArgumentException("Границы не входят в область определения функции");
        }

        try
        {
            java.lang.reflect.Constructor<?> constructor = functionClass.getDeclaredConstructor(
                    double.class, double.class, int.class
            );

            TabulatedFunction tabulatedFunction = (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);

            for(int i = 0; i < pointsCount; i++)
            {
                double x = tabulatedFunction.getPointX(i);
                tabulatedFunction.setPointY(i, function.getFunctionValue(x));
            }

            return tabulatedFunction;
        }
        catch (NoSuchMethodException e)
        {
            throw new IllegalArgumentException("Класс не имеет конструктора");
        }
        catch (Exception e)
        {
            throw new RuntimeException("Возникла ошибка при создании табулированной функции");
        }
    }

    public static TabulatedFunction inputTabulatedFunction(Class<?> functionClass, InputStream in)
    {
        if(TabulatedFunction.class.isAssignableFrom(functionClass))
        {
            try
            {
                DataInputStream inputStream = new DataInputStream(in);
                int pointsCount = inputStream.readInt();

                FunctionPoint[] points = new FunctionPoint[pointsCount];

                java.lang.reflect.Constructor<?> constructor = functionClass.getDeclaredConstructor(
                        FunctionPoint[].class
                );

                for(int i = 0; i < pointsCount; i++)
                {
                    double x = inputStream.readDouble();
                    double y = inputStream.readDouble();

                    points[i] = new FunctionPoint(x, y);
                }

                return (TabulatedFunction) constructor.newInstance((Object) points);
            }
            catch (NoSuchMethodException e)
            {
                throw new IllegalArgumentException("Класс не имеет конструктора");
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Возникла ошибка при создании табулированной функции");
            }
        }
        else
        {
            throw new IllegalArgumentException("Был передан недопустимый класс");
        }
    }

    public static TabulatedFunction readTabulatedFunction(Class<?> functionClass, Reader in)
    {
        if(TabulatedFunction.class.isAssignableFrom(functionClass))
        {
            try
            {
                StreamTokenizer streamTokenizer = new StreamTokenizer(in);
                streamTokenizer.parseNumbers();

                if (streamTokenizer.nextToken() != StreamTokenizer.TT_NUMBER)
                {
                    throw new RuntimeException();
                }

                int pointsCount = (int)streamTokenizer.nval;

                FunctionPoint[] points = new FunctionPoint[pointsCount];

                java.lang.reflect.Constructor<?> constructor = functionClass.getDeclaredConstructor(
                        FunctionPoint[].class
                );

                for(int i = 0; i < pointsCount; i++)
                {
                    if (streamTokenizer.nextToken() != StreamTokenizer.TT_NUMBER)
                    {
                        throw new RuntimeException("Ожидалось число при чтении табулированной функции");
                    }
                    double x = streamTokenizer.nval;

                    if (streamTokenizer.nextToken() != StreamTokenizer.TT_NUMBER)
                    {
                        throw new RuntimeException("Ожидалось число при чтении табулированной функции");
                    }
                    double y = streamTokenizer.nval;

                    points[i] = new FunctionPoint(x, y);
                }

                return (TabulatedFunction) constructor.newInstance((Object) points);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            catch (NoSuchMethodException e)
            {
                throw new IllegalArgumentException("Класс не имеет конструктора");
            }
            catch (Exception e)
            {
                throw new RuntimeException("Возникла ошибка при создании табулированной функции");
            }
        }
        else
        {
            throw new IllegalArgumentException("Был передан недопустимый класс");
        }
    }

    private TabulatedFunctions(){}

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount)
    {
        if(function == null)
        {
            throw new IllegalArgumentException("Функция не определена");
        }

        if(leftX >= rightX)
        {
            throw new IllegalArgumentException("Левая граница не может быть больше правой");
        }

        if(pointsCount < 2)
        {
            throw new IllegalArgumentException("Количество точек не может быть меньше двух");
        }

        if(leftX < function.getLeftDomainBorder() - 1e-10 ||
                rightX > function.getRightDomainBorder() + 1e-10)
        {
            throw new IllegalArgumentException("Границы не входят в область определения функции");
        }

        TabulatedFunction tabulatedFunction = tabulatedFunctionFactory.createTabulatedFunction(leftX, rightX, pointsCount);

        for(int i = 0; i < pointsCount; i++)
        {
            double x = tabulatedFunction.getPointX(i);
            tabulatedFunction.setPointY(i, function.getFunctionValue(x));
        }

        return tabulatedFunction;
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out)
    {
        DataOutputStream outputStream = new DataOutputStream(out);

        try
        {
            int pointsCount = function.getPointsCount();
            outputStream.writeInt(pointsCount);

            for (int i = 0; i < pointsCount; i++)
            {
                outputStream.writeDouble(function.getPointX(i));
                outputStream.writeDouble(function.getPointY(i));
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

        public static TabulatedFunction inputTabulatedFunction(InputStream in)
        {
            try
            {
                DataInputStream inputStream = new DataInputStream(in);
                int pointsCount = inputStream.readInt();

                FunctionPoint[] points = new FunctionPoint[pointsCount];

                for(int i = 0; i < pointsCount; i++)
                {
                    double x = inputStream.readDouble();
                    double y = inputStream.readDouble();

                    points[i] = new FunctionPoint(x, y);
                }

                return tabulatedFunctionFactory.createTabulatedFunction(points);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out)
    {
        try(BufferedWriter bufferedWriter = new BufferedWriter(out))
        {
            int pointsCount = function.getPointsCount();

            bufferedWriter.write(String.valueOf(pointsCount));
            bufferedWriter.newLine();

            for(int i = 0; i < pointsCount; i++)
            {
                bufferedWriter.write(String.valueOf(function.getPointX(i)));
                bufferedWriter.write(' ');

                bufferedWriter.write(String.valueOf(function.getPointY(i)));
                bufferedWriter.newLine();
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static TabulatedFunction readTabulatedFunction(Reader in)
    {
        try
        {
            StreamTokenizer streamTokenizer = new StreamTokenizer(in);
            streamTokenizer.parseNumbers();

            if (streamTokenizer.nextToken() != StreamTokenizer.TT_NUMBER)
            {
                throw new RuntimeException();
            }

            int pointsCount = (int)streamTokenizer.nval;

            FunctionPoint[] points = new FunctionPoint[pointsCount];

            for(int i = 0; i < pointsCount; i++)
            {
                if (streamTokenizer.nextToken() != StreamTokenizer.TT_NUMBER)
                {
                    throw new RuntimeException("Ожидалось число при чтении табулированной функции");
                }
                double x = streamTokenizer.nval;

                if (streamTokenizer.nextToken() != StreamTokenizer.TT_NUMBER)
                {
                    throw new RuntimeException("Ожидалось число при чтении табулированной функции");
                }
                double y = streamTokenizer.nval;

                points[i] = new FunctionPoint(x, y);
            }

            return tabulatedFunctionFactory.createTabulatedFunction(points);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
