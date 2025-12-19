package functions;

import java.io.*;
import java.util.*;
import java.io.StreamTokenizer;
import java.lang.reflect.Constructor;

public class TabulatedFunctions {
    // Статическое поле для хранения фабрики табулированных функций
    // По умолчанию используется фабрика для ArrayTabulatedFunction
    private static TabulatedFunctionFactory factory =
            new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    // Установка новой фабрики (позволяет менять тип создаваемых функций)
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }

    // ========== МЕТОДЫ СОЗДАНИЯ ЧЕРЕЗ ФАБРИКУ ==========

    /**
     * Создание табулированной функции с равномерным распределением точек
     * @param leftX левая граница области определения
     * @param rightX правая граница области определения
     * @param pointsCount количество точек
     * @return табулированная функция
     */
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    /**
     * Создание табулированной функции с заданными значениями Y
     * @param leftX левая граница области определения
     * @param rightX правая граница области определения
     * @param values массив значений Y
     * @return табулированная функция
     */
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    /**
     * Создание табулированной функции из массива точек
     * @param points массив точек функции
     * @return табулированная функция
     */
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    // ========== МЕТОДЫ СОЗДАНИЯ ЧЕРЕЗ РЕФЛЕКСИЮ ==========

    /**
     * Создание табулированной функции через рефлексию
     * @param functionClass класс функции (ArrayTabulatedFunction или LinkedListTabulatedFunction)
     * @param leftX левая граница области определения
     * @param rightX правая граница области определения
     * @param pointsCount количество точек
     * @return табулированная функция указанного класса
     */
    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass,
            double leftX, double rightX, int pointsCount) {
        try {
            // Получаем конструктор с тремя параметрами: double, double, int
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(double.class, double.class, int.class);
            return constructor.newInstance(leftX, rightX, pointsCount);
        } catch (Exception e) {
            throw new IllegalArgumentException("Не удалось создать табулированную функцию", e);
        }
    }
    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass,
            double leftX, double rightX, double[] values) {
        try {
            // Получаем конструктор с тремя параметрами: double, double, double[]
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(double.class, double.class, double[].class);
            return constructor.newInstance(leftX, rightX, values);
        } catch (Exception e) {
            throw new IllegalArgumentException("Не удалось создать табулированную функцию", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass,
            FunctionPoint[] points) {
        try {
            // Получаем конструктор с одним параметром: FunctionPoint[]
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object)points);
        } catch (Exception e) {
            throw new IllegalArgumentException("Не удалось создать табулированную функцию", e);
        }
    }

    // ========== МЕТОДЫ ЧТЕНИЯ ЧЕРЕЗ РЕФЛЕКСИЮ ==========


    public static TabulatedFunction inputTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass, InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);

        // Читаем количество точек
        int pointsCount = dis.readInt();
        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }

        // Создаем массив точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            double x = dis.readDouble();
            double y = dis.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        try {
            // Используем рефлексию для создания функции нужного типа
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object)points);
        } catch (Exception e) {
            throw new IllegalArgumentException("Не удалось создать табулированную функцию из входного потока", e);
        }
    }

    public static TabulatedFunction readTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass, Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);

        // Читаем количество точек
        st.nextToken();
        int pointsCount = (int) st.nval;
        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }

        // Создаем массив точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            st.nextToken();
            double x = st.nval;
            st.nextToken();
            double y = st.nval;
            points[i] = new FunctionPoint(x, y);
        }

        try {
            // Используем рефлексию для создания функции нужного типа
            Constructor<? extends TabulatedFunction> constructor =
                    functionClass.getConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object)points);
        } catch (Exception e) {
            throw new IllegalArgumentException("Не удалось создать табулированную функцию из читателя", e);
        }
    }

    // ========== МЕТОДЫ ТАБУЛИРОВАНИЯ ==========

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше двух");
        }

        // Создаем табулированную функцию через фабрику
        TabulatedFunction tabulatedFunc = createTabulatedFunction(leftX, rightX, pointsCount);

        // Заполняем значения Y
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            tabulatedFunc.setPointY(i, function.getFunctionValue(x));
        }

        return tabulatedFunc;
    }

    public static TabulatedFunction tabulate(
            Class<? extends TabulatedFunction> functionClass,
            Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше двух");
        }

        // Создаем табулированную функцию через рефлексию
        TabulatedFunction tabulatedFunc = createTabulatedFunction(functionClass, leftX, rightX, pointsCount);

        // Заполняем значения Y
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            tabulatedFunc.setPointY(i, function.getFunctionValue(x));
        }

        return tabulatedFunc;
    }

    // ========== МЕТОДЫ ВВОДА/ВЫВОДА ==========

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        int pointsCount = function.getPointsCount();
        dos.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            dos.writeDouble(function.getPointX(i));
            dos.writeDouble(function.getPointY(i));
        }
        dos.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        int pointsCount = dis.readInt();
        double[] yValues = new double[pointsCount];
        double leftX = dis.readDouble();
        double rightX = leftX; // будем пересчитывать rightX

        yValues[0] = dis.readDouble();
        for (int i = 1; i < pointsCount; i++) {
            double x = dis.readDouble();
            yValues[i] = dis.readDouble();
            rightX = x; // последнее значение x становится правой границей
        }

        // Используем фабрику для создания функции
        return createTabulatedFunction(leftX, rightX, yValues);
    }


    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        BufferedWriter bw = new BufferedWriter(out);
        int pointsCount = function.getPointsCount();
        bw.write(String.valueOf(pointsCount));
        bw.write(" ");
        for (int i = 0; i < pointsCount; i++) {
            bw.write(function.getPointX(i) + " " + function.getPointY(i) + " ");
        }
        bw.newLine();
        bw.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int pointsCount = (int) st.nval;
        double[] xValues = new double[pointsCount];
        double[] yValues = new double[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            st.nextToken();
            xValues[i] = st.nval;
            st.nextToken();
            yValues[i] = st.nval;
        }

        // Используем фабрику для создания функции
        return createTabulatedFunction(xValues[0], xValues[pointsCount - 1], yValues);
    }
}
