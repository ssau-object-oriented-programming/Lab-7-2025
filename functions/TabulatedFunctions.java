package functions;
import java.io.*;
import java.lang.reflect.Constructor;

//Класс, содержащий вспомогательные статические методы для работы с табулированными функциями

public final class TabulatedFunctions {

    private static final double EPS = 1e-9;

    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    // Метод для смены фабрики
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newFactory) {
        factory = newFactory;
    }

    // Метод для получения текущей фабрики
    public static TabulatedFunctionFactory getFactory() {
        return factory;
    }

    // Методы-обертки для создания функций через фабрику
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.create(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.create(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.create(points);
    }

    //Приватный конструктор запрещает создание объектов этого класса
    private TabulatedFunctions() {

    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {

        //Проверка на корректность входных параметров

        if (leftX >= rightX - EPS) {
            throw new IllegalArgumentException("Left border must be less than right border");
        }

        //Выбрасывает исключение, если счетчик точек меньше двух
        if (pointsCount < 2) {
            throw new IllegalArgumentException("At least two points required");
        }

        //Проверка границ табулирования относительно области определения функции

        double domainLeft = function.getLeftDomainBorder();
        double domainRight = function.getRightDomainBorder();

        //leftX должен быть >= domainLeft
        if (leftX < domainLeft && Math.abs(leftX - domainLeft) > EPS) {
            throw new IllegalArgumentException("The left tabulation boundary ( " + leftX + ") extends beyond the function's domain ( " + domainLeft + ").");
        }

        //rightX должен быть <= domainRight (с учетом EPS)
        if (rightX > domainRight && Math.abs(rightX - domainRight) > EPS) {
            throw new IllegalArgumentException("The right tabulation boundary (' + rightX + ') extends beyond the function's domain (' + domainRight + ').");
        }

        //Табулирование

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;

            // Если мы достигли последней точки, принудительно устанавливаем x = rightX, чтобы избежать накопления погрешности
            if (i == pointsCount - 1) {
                x = rightX;
            }

            double y = function.getFunctionValue(x);
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(points);
    }

    // Выводит табулированную функцию в байтовый поток
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream outputStream) throws IOException {

        // DataOutputStream позволяет удобно записывать примитивные типы данных (int, double)
        DataOutputStream out = new DataOutputStream(outputStream);

        // Записываем количество точек
        out.writeInt(function.getPointsCount());

        // Записываем пары (x, y)
        for (int i = 0; i < function.getPointsCount(); i++) {
            out.writeDouble(function.getPointX(i));
            out.writeDouble(function.getPointY(i));
        }
    }

    // Считывает табулированную функцию из байтового потока, создает и настраивает её объект, и возвращает его из метода
    public static TabulatedFunction inputTabulatedFunction(InputStream inputStream) throws IOException {

        // DataInputStream позволяет считывать примитивные типы данных
        DataInputStream in = new DataInputStream(inputStream);

        // Считываем количество точек
        int pointsCount = in.readInt();

        // Создаем массив для точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        // Считываем пары (x, y)
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        // Возвращаем табулированную функцию
        return new ArrayTabulatedFunction(points);
    }
    
    public static TabulatedFunction inputTabulatedFunction(Class<? extends TabulatedFunction> clazz, InputStream inputStream) throws IOException {
        DataInputStream in = new DataInputStream(inputStream);

        int pointsCount = in.readInt();

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(clazz, points);
    }

    // Записывает табулированную функцию в символьный поток
    public static void writeTabulatedFunction(TabulatedFunction function, Writer writer) {

        // PrintWriter позволяет удобно записывать текст с форматированием
        PrintWriter out = new PrintWriter(writer);

        // Записываем количество точек
        out.println(function.getPointsCount());

        // Записываем пары (x, y)
        for (int i = 0; i < function.getPointsCount(); i++) {
            // Записываем X и Y через пробел.
            out.println(function.getPointX(i) + " " + function.getPointY(i));
        }
        out.flush(); // Сброс буфера для PrintWriter
    }

    // Считывает табулированную функцию из символьного потока, создает и настраивает её объект, возвращает его из метода
    public static TabulatedFunction readTabulatedFunction(Reader reader) throws IOException {
        // StreamTokenizer - класс для парсинга числовых данных из потока
        StreamTokenizer tokenizer = new StreamTokenizer(reader);
        tokenizer.parseNumbers(); // Настраиваем токенизатор на чтение чисел

        // Считываем количество точек, первый токен должен быть количеством точек
        tokenizer.nextToken();
        int pointsCount = (int) tokenizer.nval;

        // Массив для точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        //  Считываем пары (x, y)
        for (int i = 0; i < pointsCount; i++) {

            // Считываем X
            tokenizer.nextToken();
            double x = tokenizer.nval;

            // Считываем Y
            tokenizer.nextToken();
            double y = tokenizer.nval;

            points[i] = new FunctionPoint(x, y);
        }

        // Возвращаем табулированную функцию
        return new ArrayTabulatedFunction(points);
    }
    
public static TabulatedFunction readTabulatedFunction(Class<? extends TabulatedFunction> clazz, Reader reader) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(reader);
        tokenizer.parseNumbers();

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

        return createTabulatedFunction(clazz, points);
    }
    
    //Сериализует (сохраняет) табулированную функцию в байтовый поток
    public static void serialize(TabulatedFunction function, OutputStream outputStream) throws IOException {
        // ObjectOutputStream записывает объект в бинарном виде
        try (ObjectOutputStream out = new ObjectOutputStream(outputStream)) {
            out.writeObject(function);
        }
    }

    //Десериализует (восстанавливает) табулированную функцию из байтового потока

    public static TabulatedFunction deserialize(InputStream inputStream) throws IOException, ClassNotFoundException {
        // ObjectInputStream считывает байты и восстанавливает объект
        try (ObjectInputStream in = new ObjectInputStream(inputStream)) {
            // Читаем объект и приводим его к типу TabulatedFunction
            return (TabulatedFunction) in.readObject();
        }
    }

    // Создание функции через класс и массив точек
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz, FunctionPoint[] points) {
        try {
            // Ищем конструктор, принимающий массив FunctionPoint[]
            Constructor<? extends TabulatedFunction> constructor = clazz.getConstructor(FunctionPoint[].class);
            // Создаем новый объект
            return constructor.newInstance(new Object[]{points});
        } catch (Exception e) {
            // Ловим любую ошибку рефлексии
            throw new IllegalArgumentException(e);
        }
    }

    // Создание функции через класс и параметры (leftX, rightX, pointsCount)
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz, double leftX, double rightX, int pointsCount) {
        try {
            // Ищем конструктор (double, double, int)
            Constructor<? extends TabulatedFunction> constructor = clazz.getConstructor(double.class, double.class, int.class);
            return constructor.newInstance(leftX, rightX, pointsCount);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    // Создание функции через класс и параметры (leftX, rightX, values)
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> clazz, double leftX, double rightX, double[] values) {
        try {
            // Ищем конструктор (double, double, double[])
            Constructor<? extends TabulatedFunction> constructor = clazz.getConstructor(double.class, double.class, double[].class);
            return constructor.newInstance(leftX, rightX, values);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    // Перегруженный метод tabulate, принимающий класс
    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> clazz, Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Boundaries out of domain");
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            if (i == pointsCount - 1) {
                x = rightX;
            }
            double y = function.getFunctionValue(x);
            points[i] = new FunctionPoint(x, y);
        }

        // Вызываем рефлексивный метод создания
        return createTabulatedFunction(clazz, points);
    }

}
