package functions;

import java.io.*;
import java.lang.reflect.Constructor;

public class TabulatedFunctions {
    
    // Приватный конструктор, чтобы нельзя было создать объект класса
    private TabulatedFunctions() {
    }
    
    // Фабрика по умолчанию (создает ArrayTabulatedFunction)
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();
    
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
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
            Class<?> clazz, double leftX, double rightX, int pointsCount) {
        
        // Проверяем, что класс реализует интерфейс TabulatedFunction
        if (!TabulatedFunction.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(
                "Класс " + clazz.getName() + " не реализует интерфейс TabulatedFunction");
        }
        
        try {
         
            Constructor<?> constructor = clazz.getConstructor(double.class, double.class, int.class);
            
            // Создаем экземпляр с помощью найденного конструктора
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);
            
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                "Класс " + clazz.getName() + " не имеет конструктора (double, double, int)", e);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Ошибка при создании объекта класса " + clazz.getName(), e);
        }
    }
    
    
    public static TabulatedFunction createTabulatedFunction(
            Class<?> clazz, double leftX, double rightX, double[] values) {
        
        // Проверяем, что класс реализует интерфейс TabulatedFunction
        if (!TabulatedFunction.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(
                "Класс " + clazz.getName() + " не реализует интерфейс TabulatedFunction");
        }
        
        try {
            
            Constructor<?> constructor = clazz.getConstructor(double.class, double.class, double[].class);
            
            // Создаем экземпляр с помощью найденного конструктора
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, values);
            
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                "Класс " + clazz.getName() + " не имеет конструктора (double, double, double[])", e);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Ошибка при создании объекта класса " + clazz.getName(), e);
        }
    }
    
    
    public static TabulatedFunction createTabulatedFunction(
            Class<?> clazz, FunctionPoint[] points) {
        
        // Проверяем, что класс реализует интерфейс TabulatedFunction
        if (!TabulatedFunction.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(
                "Класс " + clazz.getName() + " не реализует интерфейс TabulatedFunction");
        }
        
        try {
            // Находим конструктор с параметрами (FunctionPoint[])
            Constructor<?> constructor = clazz.getConstructor(FunctionPoint[].class);
            
            // Создаем экземпляр с помощью найденного конструктора
            return (TabulatedFunction) constructor.newInstance((Object) points);
            
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                "Класс " + clazz.getName() + " не имеет конструктора (FunctionPoint[])", e);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Ошибка при создании объекта класса " + clazz.getName(), e);
        }
    }
    
    //Табулирует функцию на заданном отрезке с заданным количеством точек
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        // Проверяем, что границы табулирования находятся в области определения функции
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }
        
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        
        // Создаем массив значений Y
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        
        // Заполняем массив значений, вычисляя функцию в каждой точке
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }
        
        // Возвращаем табулированную функцию с помощью фабрики
        return createTabulatedFunction(leftX, rightX, values);
    }
    
    public static TabulatedFunction tabulate(
            Class<?> clazz, Function function, double leftX, double rightX, int pointsCount) {
        
        // Проверяем, что границы табулирования находятся в области определения функции
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }
        
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        
        // Создаем массив значений Y
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        
        // Заполняем массив значений, вычисляя функцию в каждой точке
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }
        
        // Возвращаем табулированную функцию с помощью рефлексии
        return createTabulatedFunction(clazz, leftX, rightX, values);
    }
    
    //Выводит табулированную функцию в байтовый поток
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) {
        try (DataOutputStream dataout = new DataOutputStream(out)) {
            
            dataout.writeInt(function.getPointsCount());

            for (int i = 0; i < function.getPointsCount(); i++) {
                dataout.writeDouble(function.getPointX(i));
                dataout.writeDouble(function.getPointY(i));
            }
            dataout.flush();
        } catch (IOException e) {
            
            throw new RuntimeException("Ошибка вывода табулированной функции", e);
        }
    }
    
    
    //Вводит табулированную функцию из байтового потока
    public static TabulatedFunction inputTabulatedFunction(InputStream in) {
        DataInputStream dataIn = new DataInputStream(in);
        try {
            
            int pointsCount = dataIn.readInt();
            
           
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; i++) {
                double x = dataIn.readDouble();
                double y = dataIn.readDouble();
                points[i] = new FunctionPoint(x, y);
            }
            
            // Используем фабрику для создания функции
            return createTabulatedFunction(points);
            
        } catch (IOException e) {
            throw new RuntimeException("Ошибка ввода табулированной функции", e);
        }
        
    }
    
    
    public static TabulatedFunction inputTabulatedFunction(Class<?> clazz, InputStream in) {
        DataInputStream dataIn = new DataInputStream(in);
        try {
            
            int pointsCount = dataIn.readInt();
            
           
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; i++) {
                double x = dataIn.readDouble();
                double y = dataIn.readDouble();
                points[i] = new FunctionPoint(x, y);
            }
            
            return createTabulatedFunction(clazz, points);
            
        } catch (IOException e) {
            throw new RuntimeException("Ошибка ввода табулированной функции", e);
        }
        
    }
    
    //Записывает табулированную функцию в символьный поток
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) {
        PrintWriter writer = new PrintWriter(new BufferedWriter(out));
        try {
            // Записываем количество точек
            writer.print(function.getPointsCount());
            writer.print(' ');
            
            // Записываем координаты всех точек через пробел
            for (int i = 0; i < function.getPointsCount(); i++) {
                FunctionPoint point = function.getPoint(i);
                writer.print(point.getX());
                writer.print(' ');
                writer.print(point.getY());
                if (i < function.getPointsCount() - 1) {
                    writer.print(' ');
                }
            }
            
            
            writer.flush();
            
        } catch (Exception e) {
            throw new RuntimeException("Ошибка записи табулированной функции", e);
        }
        
    }
    
    public static TabulatedFunction readTabulatedFunction(Reader in) {
        try {
            StreamTokenizer tokenizer = new StreamTokenizer(in);
            tokenizer.nextToken();
            int pointsCount = (int) tokenizer.nval;

            FunctionPoint[] points = new FunctionPoint[pointsCount];

            for (int i = 0; i < pointsCount; i++) {
                tokenizer.nextToken();
                double x = tokenizer.nval;

                tokenizer.nextToken();
                double y = tokenizer.nval;

                points[i] = new FunctionPoint(x, y);
            }

            // Используем фабрику для создания функции
            return createTabulatedFunction(points);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении из потока", e);
        }
    }
    
    
    public static TabulatedFunction readTabulatedFunction(Class<?> clazz, Reader in) {
        try {
            StreamTokenizer tokenizer = new StreamTokenizer(in);
            tokenizer.nextToken();
            int pointsCount = (int) tokenizer.nval;

            FunctionPoint[] points = new FunctionPoint[pointsCount];

            for (int i = 0; i < pointsCount; i++) {
                tokenizer.nextToken();
                double x = tokenizer.nval;

                tokenizer.nextToken();
                double y = tokenizer.nval;

                points[i] = new FunctionPoint(x, y);
            }

            // Используем рефлексию для создания функции
            return createTabulatedFunction(clazz, points);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении из потока", e);
        }
    }
}