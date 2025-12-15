package functions;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Вспомогательный класс для работы с табулированными функциями.
 * Содержит статические методы для создания и обработки табулированных функций.
 * Реализует паттерны "Фабричный метод" и рефлексивное создание объектов.
 */
public class TabulatedFunctions {

    // Приватный конструктор, чтобы нельзя было создать экземпляр класса
    private TabulatedFunctions() {
        throw new UnsupportedOperationException("Нельзя создать экземпляр класса TabulatedFunctions");
    }

    // ============ ФАБРИКА (ЗАДАНИЕ 2) ============

    // Статическое поле для хранения фабрики табулированных функций
    private static TabulatedFunctionFactory factory =
            new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    /**
     * Устанавливает фабрику для создания табулированных функций.
     * @param factory объект фабрики
     */
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("Фабрика не может быть null");
        }
        TabulatedFunctions.factory = factory;
    }

    /**
     * Возвращает текущую фабрику табулированных функций.
     * @return текущая фабрика
     */
    public static TabulatedFunctionFactory getFactory() {
        return factory;
    }

    // ============ МЕТОДЫ СОЗДАНИЯ ЧЕРЕЗ ФАБРИКУ (ЗАДАНИЕ 2) ============

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        validateCreateParameters(leftX, rightX, pointsCount);
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        validateCreateParameters(leftX, rightX, values);
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        validateCreateParameters(points);
        return factory.createTabulatedFunction(points);
    }

    // ============ МЕТОДЫ СОЗДАНИЯ ЧЕРЕЗ РЕФЛЕКСИЮ (ЗАДАНИЕ 3) ============

    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass,
            double leftX, double rightX, int pointsCount) {

        validateClassParameter(functionClass);
        validateCreateParameters(leftX, rightX, pointsCount);

        try {
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(double.class, double.class, int.class);
            return constructor.newInstance(leftX, rightX, pointsCount);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Конструктор (double, double, int) не найден в классе " +
                    functionClass.getSimpleName(), e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Не удалось создать экземпляр класса " +
                    functionClass.getSimpleName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Нет доступа к конструктору класса " +
                    functionClass.getSimpleName(), e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            throw new IllegalArgumentException("Исключение в конструкторе класса " +
                    functionClass.getSimpleName() + ": " +
                    (cause != null ? cause.getMessage() : e.getMessage()), cause);
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass,
            double leftX, double rightX, double[] values) {

        validateClassParameter(functionClass);
        validateCreateParameters(leftX, rightX, values);

        try {
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(double.class, double.class, double[].class);
            return constructor.newInstance(leftX, rightX, values);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Конструктор (double, double, double[]) не найден в классе " +
                    functionClass.getSimpleName(), e);
        } catch (Exception e) {
            handleReflectionException(e, functionClass);
            return null; // Эта строка никогда не выполнится
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass,
            FunctionPoint[] points) {

        validateClassParameter(functionClass);
        validateCreateParameters(points);

        try {
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object) points);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Конструктор (FunctionPoint[]) не найден в классе " +
                    functionClass.getSimpleName(), e);
        } catch (Exception e) {
            handleReflectionException(e, functionClass);
            return null;
        }
    }

    // ============ МЕТОДЫ ТАБУЛИРОВАНИЯ ============

    /**
     * Табулирует функцию с использованием текущей фабрики.
     */
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        validateTabulateParameters(function, leftX, rightX, pointsCount);
        double[] values = computeFunctionValues(function, leftX, rightX, pointsCount);
        return createTabulatedFunction(leftX, rightX, values);
    }

    /**
     * Табулирует функцию с использованием рефлексии (ЗАДАНИЕ 3).
     */
    public static TabulatedFunction tabulate(
            Class<? extends TabulatedFunction> functionClass,
            Function function, double leftX, double rightX, int pointsCount) {

        validateClassParameter(functionClass);
        validateTabulateParameters(function, leftX, rightX, pointsCount);
        double[] values = computeFunctionValues(function, leftX, rightX, pointsCount);
        return createTabulatedFunction(functionClass, leftX, rightX, values);
    }

    // ============ МЕТОДЫ ВВОДА/ВЫВОДА ============

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeInt(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        dataOut.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        int pointsCount = dataIn.readInt();
        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            points[i] = new FunctionPoint(dataIn.readDouble(), dataIn.readDouble());
        }
        return createTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        writer.print(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            writer.print(" " + function.getPointX(i) + " " + function.getPointY(i));
        }
        writer.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
            throw new IOException("Ожидалось количество точек");
        }
        int pointsCount = (int) tokenizer.nval;
        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата X");
            }
            double x = tokenizer.nval;
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата Y");
            }
            points[i] = new FunctionPoint(x, tokenizer.nval);
        }
        return createTabulatedFunction(points);
    }

    // ============ ВСПОМОГАТЕЛЬНЫЕ ПРИВАТНЫЕ МЕТОДЫ ============

    private static void validateClassParameter(Class<? extends TabulatedFunction> functionClass) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс " + functionClass.getName() +
                    " не реализует интерфейс TabulatedFunction");
        }
    }

    private static void validateCreateParameters(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
    }

    private static void validateCreateParameters(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (values == null || values.length < 2) {
            throw new IllegalArgumentException("Массив значений должен содержать минимум 2 элемента");
        }
    }

    private static void validateCreateParameters(FunctionPoint[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Массив точек должен содержать минимум 2 точки");
        }
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i - 1].getX()) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по возрастанию X");
            }
        }
    }

    private static void validateTabulateParameters(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException(
                    String.format("Границы [%.2f, %.2f] выходят за область определения [%.2f, %.2f]",
                            leftX, rightX, function.getLeftDomainBorder(), function.getRightDomainBorder()));
        }
        validateCreateParameters(leftX, rightX, pointsCount);
    }

    private static double[] computeFunctionValues(Function function, double leftX, double rightX, int pointsCount) {
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }
        return values;
    }

    private static void handleReflectionException(Exception e, Class<?> clazz) {
        if (e instanceof NoSuchMethodException) {
            throw new IllegalArgumentException("Не найден конструктор в классе " + clazz.getSimpleName(), e);
        } else if (e instanceof InstantiationException) {
            throw new IllegalArgumentException("Не удалось создать экземпляр класса " + clazz.getSimpleName(), e);
        } else if (e instanceof IllegalAccessException) {
            throw new IllegalArgumentException("Нет доступа к конструктору класса " + clazz.getSimpleName(), e);
        } else if (e instanceof InvocationTargetException) {
            Throwable cause = ((InvocationTargetException) e).getCause();
            throw new IllegalArgumentException("Исключение в конструкторе класса " + clazz.getSimpleName() +
                    ": " + (cause != null ? cause.getMessage() : e.getMessage()), cause);
        } else {
            throw new IllegalArgumentException("Ошибка при создании объекта класса " + clazz.getSimpleName(), e);
        }
    }

    // ============ ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ============

    /**
     * Сбрасывает фабрику к значению по умолчанию.
     */
    public static void resetFactoryToDefault() {
        factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();
    }

    /**
     * Создает копию табулированной функции.
     */
    public static TabulatedFunction copyTabulatedFunction(TabulatedFunction source) {
        if (source == null) {
            throw new IllegalArgumentException("Исходная функция не может быть null");
        }
        FunctionPoint[] points = new FunctionPoint[source.getPointsCount()];
        for (int i = 0; i < points.length; i++) {
            points[i] = (FunctionPoint) source.getPoint(i).clone();
        }
        return createTabulatedFunction(points);
    }
}