package functions;

import functions.basic.*;
import java.lang.reflect.Constructor;

public class TabulatedFunctions {
    // Приватное статическое поле фабрики
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    // Метод для замены фабрики
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }

    // Три перегруженных метода создания табулированных функций (через фабрику)
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    // Три перегруженных метода создания табулированных функций (через рефлексию)
    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, double leftX, double rightX, int pointsCount) {
        // Проверяем, что класс реализует интерфейс TabulatedFunction
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс " + functionClass + " не реализует интерфейс TabulatedFunction");
        }

        try {
            // Ищем конструктор с параметрами (double, double, int)
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, int.class);
            // Создаем объект с помощью рефлексии
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, double leftX, double rightX, double[] values) {
        // Проверяем, что класс реализует интерфейс TabulatedFunction
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс " + functionClass + " не реализует интерфейс TabulatedFunction");
        }

        try {
            // Ищем конструктор с параметрами (double, double, double[])
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, double[].class);
            // Создаем объект с помощью рефлексии
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, values);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, FunctionPoint[] points) {
        // Проверяем, что класс реализует интерфейс TabulatedFunction
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс " + functionClass + " не реализует интерфейс TabulatedFunction");
        }

        try {
            // Ищем конструктор с параметрами (FunctionPoint[])
            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);
            // Создаем объект с помощью рефлексии
            return (TabulatedFunction) constructor.newInstance((Object) points);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта через рефлексию", e);
        }
    }

    // Перегруженные методы tabulate с использованием рефлексии
    public static TabulatedFunction tabulate(Class<?> functionClass, Function function, double leftX, double rightX, int pointsCount) {
        // Создаем табулированную функцию через рефлексию
        TabulatedFunction tabulatedFunction = createTabulatedFunction(functionClass, leftX, rightX, pointsCount);

        // Заполняем значениями функции
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            tabulatedFunction.setPointY(i, y);
        }

        return tabulatedFunction;
    }

    public static TabulatedFunction tabulate(Class<?> functionClass, Function function, double leftX, double rightX, double samplingStep) {
        if (samplingStep <= 0) {
            throw new IllegalArgumentException("Шаг табуляции должен быть положительным");
        }

        int pointsCount = (int) Math.ceil((rightX - leftX) / samplingStep) + 1;
        // Создаем табулированную функцию через рефлексию
        TabulatedFunction tabulatedFunction = createTabulatedFunction(functionClass, leftX, rightX, pointsCount);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * samplingStep;
            if (x > rightX) x = rightX;
            double y = function.getFunctionValue(x);
            tabulatedFunction.setPointY(i, y);
        }

        return tabulatedFunction;
    }

    // Существующие методы с использованием фабрики
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        TabulatedFunction tabulatedFunction = createTabulatedFunction(leftX, rightX, pointsCount);

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            tabulatedFunction.setPointY(i, y);
        }

        return tabulatedFunction;
    }

    public static void tabulate(Function function, TabulatedFunction tabulatedFunction) {
        int pointsCount = tabulatedFunction.getPointsCount();
        double leftX = tabulatedFunction.getPointX(0);
        double rightX = tabulatedFunction.getPointX(pointsCount - 1);

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            tabulatedFunction.setPointY(i, y);
        }
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, double samplingStep) {
        if (samplingStep <= 0) {
            throw new IllegalArgumentException("Шаг табуляции должен быть положительным");
        }

        int pointsCount = (int) Math.ceil((rightX - leftX) / samplingStep) + 1;
        TabulatedFunction tabulatedFunction = createTabulatedFunction(leftX, rightX, pointsCount);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * samplingStep;
            if (x > rightX) x = rightX;
            double y = function.getFunctionValue(x);
            tabulatedFunction.setPointY(i, y);
        }

        return tabulatedFunction;
    }
}