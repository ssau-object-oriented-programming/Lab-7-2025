package functions;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TabulatedFunctions {
    private TabulatedFunctions() { //конструктор, чтобы нельзя было создать объект этого класса
    }

    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory f) {
        factory = f; //меняем объект фабрики
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] values) {
        return factory.createTabulatedFunction(values); //вызываем метод у того класса, к которому принадежит фабрика
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }


    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> cl, FunctionPoint[] values){
        try {
            Constructor<? extends TabulatedFunction> constructor = cl.getConstructor(FunctionPoint[].class); //находим конструктор по типу передаваемых значений
            return constructor.newInstance((Object) values); //вызываем конструктор
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("В классе нет такого конструктора", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Нельзя создать объект абстрактного класса", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Нет доступа к конструктору", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Конструктор выбрасывает исключение", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> cl, double leftX, double rightX, double[] values) {
        try {
            Constructor<? extends TabulatedFunction> constructor = cl.getConstructor(double.class, double.class, double[].class); //находим конструктор
            return constructor.newInstance(leftX, rightX, values); //вызываем конструктор
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("В классе нет такого конструктора", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Нельзя создать объект абстрактного класса", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Нет доступа к конструктору", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Конструктор выбрасывает исключение", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> cl, double leftX, double rightX, int pointsCount) {
        try {
            Constructor<? extends TabulatedFunction> constructor = cl.getConstructor(double.class, double.class, int.class); //находим конструктор
            return constructor.newInstance(leftX, rightX, pointsCount); //вызываем конструктор
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("В классе нет такого конструктора", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Нельзя создать объект абстрактного класса", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Нет доступа к конструктору", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Конструктор выбрасывает исключение", e);
        }
    }
    //метод tabulate() с рефлексией
    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> cl, Function function, double leftX, double rightX, int pointsCount) {
        if (Double.compare(leftX, function.getLeftDomainBorder()) < 0 || Double.compare(rightX, function.getRightDomainBorder()) > 0) {
            throw new IllegalArgumentException("Границы для табулирования выходят за область определения функции");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Точек меньше двух");
        }
        if (Double.compare(leftX, rightX) >= 0) {
            throw new IllegalArgumentException("Левая граница больше правой");
        }
        double[] values = new double[pointsCount]; //создали массив, который будет хранить значения табулированной функции
        double funVal; //переменная для хранения y в каждой точке
        double intervalLength = Math.abs(leftX - rightX) / (pointsCount - 1); //находим длину интервала между двумя точками
        for (int i=0; i < pointsCount; i++) {
            if (i != (pointsCount - 1)) { //если не последняя точка
                funVal = function.getFunctionValue(leftX + i*intervalLength); //получаем значение функции в заданной точке, каждый раз перемещая x на длину интервала
            }
            else {
                funVal = function.getFunctionValue(rightX); //последней точке присваиваем значение у правой границы
            }
            values[i] = funVal; //заполняем массив значениями
        }
        TabulatedFunction tabFun = createTabulatedFunction(cl, leftX, rightX, values);
        return tabFun;
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (Double.compare(leftX, function.getLeftDomainBorder()) < 0 || Double.compare(rightX, function.getRightDomainBorder()) > 0) {
            throw new IllegalArgumentException("Границы для табулирования выходят за область определения функции");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Точек меньше двух");
        }
        if (Double.compare(leftX, rightX) >= 0) {
            throw new IllegalArgumentException("Левая граница больше правой");
        }
        double[] values = new double[pointsCount]; //создали массив, который будет хранить значения табулированной функции
        double funVal; //переменная для хранения y в каждой точке
        double intervalLength = Math.abs(leftX - rightX) / (pointsCount - 1); //находим длину интервала между двумя точками
        for (int i=0; i < pointsCount; i++) {
            if (i != (pointsCount - 1)) { //если не последняя точка
                funVal = function.getFunctionValue(leftX + i*intervalLength); //получаем значение функции в заданной точке, каждый раз перемещая x на длину интервала
            }
            else {
                funVal = function.getFunctionValue(rightX); //последней точке присваиваем значение у правой границы
            }
            values[i] = funVal; //заполняем массив значениями
        }
        TabulatedFunction tabFun = createTabulatedFunction(leftX, rightX, values); //вызываем метод этого же класса, который вызовет этот метод для текущей фабрики и создаст объект нужного класса
        return tabFun;
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException{
        try (DataOutputStream Out = new DataOutputStream(out)) {
            Out.writeInt(function.getPointsCount());
            for (int i=0; i < function.getPointsCount(); i++) {
                Out.writeDouble(function.getPointX(i));
                Out.writeDouble(function.getPointY(i));
            }
            Out.flush(); //сбрасываем буфер
        }
    }

    //метод inputTabulatedFunction() с рефлексией
    public static TabulatedFunction inputTabulatedFunction(Class<? extends TabulatedFunction> cl, InputStream in) throws IOException, InappropriateFunctionPointException {
        try (DataInputStream In = new DataInputStream(in)) {
            int pointsCount = In.readInt();
            TabulatedFunction tabFun = createTabulatedFunction(cl, 0, pointsCount - 1, pointsCount); //заменили на createTabulatedFunction
            for (int i=0; i < pointsCount; i++) {
                tabFun.setPointX(i, In.readDouble()); //записывам значения x и y
                tabFun.setPointY(i, In.readDouble());
            }

            return tabFun;
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException, InappropriateFunctionPointException {
        try (DataInputStream In = new DataInputStream(in)) {
            int pointsCount = In.readInt();
            TabulatedFunction tabFun = createTabulatedFunction(0, pointsCount - 1, pointsCount); //заменили на createTabulatedFunction
            for (int i=0; i < pointsCount; i++) {
                tabFun.setPointX(i, In.readDouble()); //записывам значения x и y
                tabFun.setPointY(i, In.readDouble());
            }

            return tabFun;
        }
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        try (BufferedWriter Out = new BufferedWriter(out)) {
            Out.write(String.valueOf(function.getPointsCount()));
            Out.newLine();
            for (int i=0; i < function.getPointsCount(); i++) {
                Out.write(String.valueOf(function.getPointX(i)));
                Out.write(" ");
                Out.write(String.valueOf(function.getPointY(i)));
                Out.newLine();
            }
            Out.flush();
        }
    }

    //метод readTabulatedFunction() с рефлексией
    public static TabulatedFunction readTabulatedFunction(Class<? extends TabulatedFunction> cl, Reader in) throws IOException, InappropriateFunctionPointException {
        StreamTokenizer In = new StreamTokenizer(in);
        In.nextToken(); //переход к следующему токену
        int pointsCount = (int)In.nval; //числовое значение токена преобразуем в число типа int
        TabulatedFunction tabFun = createTabulatedFunction(cl,0, pointsCount - 1, pointsCount); //заменили на createTabulatedFunction
        for (int i=0; i < pointsCount; i++) {
            In.nextToken();
            tabFun.setPointX(i, In.nval); //записывам значения x и y
            In.nextToken();
            tabFun.setPointY(i, In.nval); //In.nval - статическое плое типа double
        }

        return tabFun;
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException, InappropriateFunctionPointException {
        StreamTokenizer In = new StreamTokenizer(in);
        In.nextToken(); //переход к следующему токену
        int pointsCount = (int)In.nval; //числовое значение токена преобразуем в число типа int
        TabulatedFunction tabFun = createTabulatedFunction(0, pointsCount - 1, pointsCount); //заменили на createTabulatedFunction
        for (int i=0; i < pointsCount; i++) {
            In.nextToken();
            tabFun.setPointX(i, In.nval); //записывам значения x и y
            In.nextToken();
            tabFun.setPointY(i, In.nval); //In.nval - статическое плое типа double
        }

        return tabFun;
    }


}
