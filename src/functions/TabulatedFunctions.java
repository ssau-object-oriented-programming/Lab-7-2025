package functions;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TabulatedFunctions {
    
    // Поле фабрики. По умолчанию используем фабрику массивов.
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() {}

    // --- Методы работы с фабрикой (из Шага 2) ---

    // Установка новой фабрики
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newFactory) {
        factory = newFactory;
    }

    // Создание через фабрику (границы и количество)
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    // Создание через фабрику (границы и значения)
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    // Создание через фабрику (точки)
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    // Табулирование через текущую фабрику
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Tabulation bounds are out of function's domain");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Points count must be 2 or more");
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }
        
        return createTabulatedFunction(points);
    }

    // --- ЗАДАНИЕ 3: Методы с использованием REFLECTION ---

    // Создание функции указанного класса (границы и количество)
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz, double leftX, double rightX, int pointsCount) {
        try {
            // Получаем конструктор с параметрами (double, double, int)
            Constructor<? extends TabulatedFunction> constructor = clazz.getConstructor(double.class, double.class, int.class);
            // Создаем новый экземпляр
            return constructor.newInstance(leftX, rightX, pointsCount);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // Оборачиваем исключения рефлексии в IllegalArgumentException
            throw new IllegalArgumentException(e);
        }
    }

    // Создание функции указанного класса (границы и значения)
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz, double leftX, double rightX, double[] values) {
        try {
            // Получаем конструктор с параметрами (double, double, double[])
            Constructor<? extends TabulatedFunction> constructor = clazz.getConstructor(double.class, double.class, double[].class);
            return constructor.newInstance(leftX, rightX, values);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // Создание функции указанного класса (массив точек)
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz, FunctionPoint[] points) {
        try {
            // Получаем конструктор с параметром (FunctionPoint[])
            Constructor<? extends TabulatedFunction> constructor = clazz.getConstructor(FunctionPoint[].class);
            // Приводим points к Object, чтобы вариадические аргументы newInstance сработали корректно
            return constructor.newInstance((Object) points);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // Табулирование с указанием класса функции
    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> clazz, Function function, double leftX, double rightX, int pointsCount) {
         if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Tabulation bounds are out of function's domain");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Points count must be 2 or more");
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }
        
        // Создаем через метод рефлексии
        return createTabulatedFunction(clazz, points);
    }

    // --- Методы ввода/вывода (остаются прежними) ---

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            dos.writeDouble(function.getPointX(i));
            dos.writeDouble(function.getPointY(i));
        }
        dos.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        int count = dis.readInt();
        FunctionPoint[] points = new FunctionPoint[count];
        for (int i = 0; i < count; i++) {
            points[i] = new FunctionPoint(dis.readDouble(), dis.readDouble());
        }
        return createTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter pw = new PrintWriter(out);
        pw.println(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            pw.println(function.getPointX(i) + " " + function.getPointY(i));
        }
        pw.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.nextToken();
        int count = (int) tokenizer.nval;

        FunctionPoint[] points = new FunctionPoint[count];
        for (int i = 0; i < count; i++) {
            tokenizer.nextToken();
            double x = tokenizer.nval;
            tokenizer.nextToken();
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(points);
    }
}
