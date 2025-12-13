package functions;

import java.io.*;
import java.lang.reflect.Constructor;


public class TabulatedFunctions {

    private static TabulatedFunctionFactory factory = 
        new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();
    
    // Метод для замены фабрики
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newFactory) {
        factory = newFactory;
    }
    
    // Три перегруженных метода createTabulatedFunction
    public static TabulatedFunction createTabulatedFunction(
        double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }
    
    public static TabulatedFunction createTabulatedFunction(
        double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }
    
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

     private TabulatedFunctions() {
        throw new UnsupportedOperationException("Класс TabulatedFunctions не может быть инстанцирован");
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX || pointsCount < 2) {
            throw new IllegalArgumentException("Некорректные параметры");
        }
        
        //Используем фабрику вместо new ArrayTabulatedFunction
        TabulatedFunction tabulatedFunction = 
            createTabulatedFunction(leftX, rightX, pointsCount);
        
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            tabulatedFunction.setPointY(i, y);
        }
        
        return tabulatedFunction;
    }

    // Метод 1: Создать функцию из интервала и количества точек
    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> clazz,
            double leftX, double rightX, int pointsCount) {
        
        try {
            // Найти конструктор (double, double, int)
            Constructor<? extends TabulatedFunction> constructor = 
                clazz.getConstructor(double.class, double.class, int.class);
            
            // Создать объект
            return constructor.newInstance(leftX, rightX, pointsCount);
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка создания функции", e);
        }
    }
    
    // Метод 2: Создать функцию из интервала и значений Y
    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> clazz,
            double leftX, double rightX, double[] values) {
        
        try {
            // Найти конструктор (double, double, double[])
            Constructor<? extends TabulatedFunction> constructor = 
                clazz.getConstructor(double.class, double.class, double[].class);
            
            // Создать объект
            return constructor.newInstance(leftX, rightX, values);
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка создания функции", e);
        }
    }
    
    // Метод 3: Создать функцию из массива точек
    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> clazz,
            FunctionPoint[] points) {
        
        try {
            // Найти конструктор (FunctionPoint[])
            Constructor<? extends TabulatedFunction> constructor = 
                clazz.getConstructor(FunctionPoint[].class);
            
            // Создать объект
            return constructor.newInstance((Object) points);
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка создания функции", e);
        }
    }

    // Метод tabulate с рефлексией
    public static TabulatedFunction tabulate(
        Class<? extends TabulatedFunction> clazz,
        Function function, 
        double leftX, 
        double rightX, 
        int pointsCount) {
    
    if (leftX >= rightX || pointsCount < 2) {
        throw new IllegalArgumentException("Некорректные параметры");
    }
    
    // Создаем функцию через рефлексию
    TabulatedFunction tabulatedFunction = 
        createTabulatedFunction(clazz, leftX, rightX, pointsCount);
    
    // Заполняем значения Y
    double step = (rightX - leftX) / (pointsCount - 1);
    for (int i = 0; i < pointsCount; i++) {
        double x = leftX + i * step;
        double y = function.getFunctionValue(x);
        tabulatedFunction.setPointY(i, y);
    }
    
    return tabulatedFunction;
}



    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        
        // Записываем количество точек
        int pointsCount = function.getPointsCount();
        dataOut.writeInt(pointsCount);
        
        // Записываем координаты точек
        for (int i = 0; i < pointsCount; i++) {
            double x = function.getPointX(i);
            double y = function.getPointY(i);
            dataOut.writeDouble(x);
            dataOut.writeDouble(y);
        }
        
        dataOut.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        int pointsCount = dataIn.readInt();
            
            // Читаем координаты точек
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; i++) {
                double x = dataIn.readDouble();
                double y = dataIn.readDouble();
                points[i] = new FunctionPoint(x, y);
            }
            
            return new ArrayTabulatedFunction(points);
            
    } 

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        
        // Записываем количество точек
        writer.print(function.getPointsCount());
        
        // Записываем координаты точек
        for (int i = 0; i < function.getPointsCount(); i++) {
            writer.print(" " + function.getPointX(i) + " " + function.getPointY(i));
        }
        
        writer.flush();
        // НЕ закрываем поток - это ответственность вызывающего кода
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        // Используем StreamTokenizer для разбора чисел
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        
        // Настраиваем tokenizer для чтения чисел
        tokenizer.resetSyntax();
        tokenizer.parseNumbers();  // Автоматически настраивает для чтения чисел
        tokenizer.whitespaceChars(' ', ' ');
        tokenizer.whitespaceChars('\t', '\t');
        tokenizer.whitespaceChars('\n', '\n');
        tokenizer.whitespaceChars('\r', '\r');
        // Также разрешаем символы для чисел (на всякий случай)
        tokenizer.wordChars('0', '9');  // цифры
        tokenizer.wordChars('.', '.');  // точка
        tokenizer.wordChars('-', '-');  // минус
        tokenizer.wordChars('E', 'E');  // экспонента заглавная
        tokenizer.wordChars('e', 'e');  // экспонента строчная
        
        if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
            throw new IOException("Ожидалось количество точек");
        }
        int pointsCount = (int) tokenizer.nval;

        if (pointsCount < 2) {
            throw new IOException("Количество точек должно быть не менее 2");
        }
        // Читаем координаты точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            // Читаем x
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата x точки " + i);
            }
            double x = tokenizer.nval;
            
            // Читаем y
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата y точки " + i);
            }
            double y = tokenizer.nval;
            
            points[i] = new FunctionPoint(x, y);
            }
            
            return new ArrayTabulatedFunction(points);
    }

}
    
