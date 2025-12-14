package functions;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class TabulatedFunctions {
    // Приватное статическое поле фабрики с инициализацией Array фабрикой по умолчанию
    private static TabulatedFunctionFactory factory = 
        new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();
    
    // Метод для замены фабрики
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newFactory) {
        factory = newFactory;
    }
    
    // Три перегруженных метода createTabulatedFunction, использующие текущую фабрику
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }
    
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }
    
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] array) {
        return factory.createTabulatedFunction(array);
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) 
    {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Заданный интервал выходит за границы области определения функции");
        }
        
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(points);
    }

 // Tabulate через рефлексию с указанием класса
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount, Class<? extends TabulatedFunction> functionClass) 
    {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Заданный интервал выходит за границы области определения функции");
        }
        
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            points[i] = new FunctionPoint(x, y);
        }
        
        // Используем рефлексивный метод для создания функции
        return createTabulatedFunction(functionClass, points);
    }

    // Метод создания функции через рефлексию с тремя параметрами
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> functionClass,double leftX, double rightX, int pointsCount) {
        
        try {
            // Получаем конструктор с параметрами (double, double, int)
            Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(double.class, double.class, int.class);
            
            // Создаем объект через конструктор
            return constructor.newInstance(leftX, rightX, pointsCount);
            
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Класс " + functionClass.getName() + " не имеет конструктора (double, double, int)", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Невозможно создать экземпляр класса " + functionClass.getName() + " (абстрактный класс?)", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Конструктор класса " + functionClass.getName() + " недоступен", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка в конструкторе класса " + functionClass.getName(), e);
        }
    }
    
    // Метод создания функции через рефлексию с массивом значений
    public static TabulatedFunction createTabulatedFunction( Class<? extends TabulatedFunction> functionClass, double leftX, double rightX, double[] values) 
    {
        
        try {
            // Получаем конструктор с параметрами (double, double, double[])
            Constructor<? extends TabulatedFunction> constructor = functionClass.getConstructor(double.class, double.class, double[].class);
            
            // Создаем объект через конструктор
            return constructor.newInstance(leftX, rightX, values);
            
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Класс " + functionClass.getName() +  " не имеет конструктора (double, double, double[])", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Невозможно создать экземпляр класса " + functionClass.getName() + " (абстрактный класс?)", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Конструктор класса " + functionClass.getName() + " недоступен", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка в конструкторе класса " + functionClass.getName(), e);
        }
    }
    
    // Метод создания функции через рефлексию с массивом точек
    public static TabulatedFunction createTabulatedFunction( Class<? extends TabulatedFunction> functionClass, FunctionPoint[] array) {
        
        try {
            // Получаем конструктор с параметрами (FunctionPoint[])
            Constructor<? extends TabulatedFunction> constructor = 
                functionClass.getConstructor(FunctionPoint[].class);
            
            // Создаем объект через конструктор
            return constructor.newInstance((Object)array); // Приведение типа для varargs
            
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Класс " + functionClass.getName() + " не имеет конструктора (FunctionPoint[])", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Невозможно создать экземпляр класса " +  functionClass.getName() + " (абстрактный класс?)", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Конструктор класса " + functionClass.getName() + " недоступен", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Ошибка в конструкторе класса " + functionClass.getName(), e);
        }
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        int pointCount = function.getPointsCount();
        dataOut.writeInt(pointCount);

        for (int i = 0; i < pointCount; i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        dataOut.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        int pointCount = dataIn.readInt();
        FunctionPoint[] data = new FunctionPoint[pointCount];
        
        for (int i = 0; i < pointCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            data[i] = new FunctionPoint(x, y);
        }
        
        return createTabulatedFunction(data);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        int pointCount = function.getPointsCount();
        writer.println(pointCount);
        
        for (int i = 0; i < pointCount; i++) {
            writer.println(function.getPointX(i));
            writer.println(function.getPointY(i));
        }
        writer.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int itemsCount = (int) st.nval;
        FunctionPoint[] data = new FunctionPoint[itemsCount];
        
        for (int i = 0; i < itemsCount; i++) {
            st.nextToken();
            double x = st.nval;
            st.nextToken();
            double y = st.nval;
            data[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(data);
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in, Class<? extends TabulatedFunction> functionClass) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        int pointCount = dataIn.readInt();
        FunctionPoint[] data = new FunctionPoint[pointCount];
        
        for (int i = 0; i < pointCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            data[i] = new FunctionPoint(x, y);
        }
        
        // Используем рефлексивный метод для создания функции
        return createTabulatedFunction(functionClass, data);
    }

    // Добавить этот перегруженный метод readTabulatedFunction с рефлексией
    public static TabulatedFunction readTabulatedFunction(Reader in, Class<? extends TabulatedFunction> functionClass) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int itemsCount = (int) st.nval;
        FunctionPoint[] data = new FunctionPoint[itemsCount];
        
        for (int i = 0; i < itemsCount; i++) {
            st.nextToken();
            double x = st.nval;
            st.nextToken();
            double y = st.nval;
            data[i] = new FunctionPoint(x, y);
        }
        
        // Используем рефлексивный метод для создания функции
        return createTabulatedFunction(functionClass, data);
    }

}