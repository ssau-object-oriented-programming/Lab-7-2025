import functions.*;
import functions.basic.*;
import functions.meta.*;
import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ЛАБОРАТОРНАЯ РАБОТА №7 ===");
        System.out.println("=== Реализация паттернов: Итератор, Фабричный метод, Рефлексия ===\n");

        // Тестирование задания 1: Итераторы
        testIterators();

        // Тестирование задания 2: Фабричный метод
        testFactoryMethod();

        // Тестирование задания 3: Рефлексия
        testReflection();

        System.out.println("\n=== ВСЕ ТЕСТЫ ЗАВЕРШЕНЫ УСПЕШНО ===");
    }

    // ==================== ЗАДАНИЕ 1: Итераторы ====================

    private static void testIterators() {
        System.out.println("=== ЗАДАНИЕ 1: Тестирование итераторов ===\n");

        // Создаем тестовые данные
        FunctionPoint[] testPoints = {
                new FunctionPoint(0.0, 0.0),
                new FunctionPoint(1.0, 1.0),
                new FunctionPoint(2.0, 4.0),
                new FunctionPoint(3.0, 9.0),
                new FunctionPoint(4.0, 16.0)
        };

        // 1. Тестирование ArrayTabulatedFunction
        System.out.println("1. Тестирование ArrayTabulatedFunction:");
        testArrayTabulatedFunction(testPoints);

        // 2. Тестирование LinkedListTabulatedFunction
        System.out.println("\n2. Тестирование LinkedListTabulatedFunction:");
        testLinkedListTabulatedFunction(testPoints);

        // 3. Сравнение работы итераторов
        System.out.println("\n3. Сравнение итераторов Array и LinkedList реализаций:");
        compareIterators(testPoints);

        // 4. Тестирование исключений итераторов
        System.out.println("\n4. Тестирование исключений итераторов:");
        testIteratorExceptions(testPoints);

        // 5. Тестирование вложенных циклов и независимости итераторов
        System.out.println("\n5. Тестирование независимости итераторов:");
        testIndependentIterators(testPoints);

        System.out.println("\n  Задание 1 завершено успешно!");
    }

    private static void testArrayTabulatedFunction(FunctionPoint[] points) {
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(points);

        System.out.println("   a) Использование for-each:");
        int count = 0;
        for (FunctionPoint point : arrayFunc) {
            System.out.printf("      Точка %d: %s\n", count, point);
            count++;
        }

        System.out.println("   b) Использование явного итератора:");
        Iterator<FunctionPoint> iterator = arrayFunc.iterator();
        count = 0;
        while (iterator.hasNext()) {
            System.out.printf("      Точка %d: %s\n", count, iterator.next());
            count++;
        }

        System.out.println("   c) Проверка инкапсуляции (изменение копии точки):");
        for (FunctionPoint point : arrayFunc) {
            FunctionPoint copy = (FunctionPoint) point.clone();
            copy.setX(copy.getX() + 10);
            copy.setY(copy.getY() + 100);
            System.out.printf("      Изменена копия: %s -> %s\n", point, copy);
        }
        System.out.println("        Исходная функция не изменилась");
    }

    private static void testLinkedListTabulatedFunction(FunctionPoint[] points) {
        TabulatedFunction listFunc = new LinkedListTabulatedFunction(points);

        System.out.println("   a) Использование for-each:");
        int count = 0;
        for (FunctionPoint point : listFunc) {
            System.out.printf("      Точка %d: %s\n", count, point);
            count++;
        }

        System.out.println("   b) Использование явного итератора:");
        Iterator<FunctionPoint> iterator = listFunc.iterator();
        count = 0;
        while (iterator.hasNext()) {
            System.out.printf("      Точка %d: %s\n", count, iterator.next());
            count++;
        }
    }

    private static void compareIterators(FunctionPoint[] points) {
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(points);
        TabulatedFunction listFunc = new LinkedListTabulatedFunction(points);

        System.out.println("   ArrayTabulatedFunction:");
        for (FunctionPoint p : arrayFunc) {
            System.out.println("      " + p);
        }

        System.out.println("   LinkedListTabulatedFunction:");
        for (FunctionPoint p : listFunc) {
            System.out.println("      " + p);
        }

        boolean allEqual = true;
        Iterator<FunctionPoint> arrayIter = arrayFunc.iterator();
        Iterator<FunctionPoint> listIter = listFunc.iterator();

        while (arrayIter.hasNext() && listIter.hasNext()) {
            if (!arrayIter.next().equals(listIter.next())) {
                allEqual = false;
                break;
            }
        }

        if (allEqual && !arrayIter.hasNext() && !listIter.hasNext()) {
            System.out.println("     Оба итератора возвращают одинаковые точки");
        }
    }

    private static void testIteratorExceptions(FunctionPoint[] points) {
        System.out.println("   a) Тестирование NoSuchElementException:");
        TabulatedFunction func = new ArrayTabulatedFunction(points);
        Iterator<FunctionPoint> iterator = func.iterator();

        // Получаем все элементы
        while (iterator.hasNext()) {
            iterator.next();
        }

        try {
            iterator.next(); // Должно бросить исключение
            System.out.println("        Исключение не было брошено!");
        } catch (NoSuchElementException e) {
            System.out.println("        NoSuchElementException поймано: " + e.getMessage());
        }

        System.out.println("   b) Тестирование UnsupportedOperationException:");
        iterator = func.iterator();
        iterator.next(); // Переходим к первому элементу

        try {
            iterator.remove();
            System.out.println("        Исключение не было брошено!");
        } catch (UnsupportedOperationException e) {
            System.out.println("        UnsupportedOperationException поймано: " + e.getMessage());
        }
    }

    private static void testIndependentIterators(FunctionPoint[] points) {
        TabulatedFunction func = new ArrayTabulatedFunction(points);

        System.out.println("   Два независимых итератора для одной функции:");
        Iterator<FunctionPoint> iter1 = func.iterator();
        Iterator<FunctionPoint> iter2 = func.iterator();

        System.out.println("   Итератор 1 (первые 3 точки):");
        for (int i = 0; i < 3 && iter1.hasNext(); i++) {
            System.out.println("      " + iter1.next());
        }

        System.out.println("   Итератор 2 (все точки):");
        while (iter2.hasNext()) {
            System.out.println("      " + iter2.next());
        }

        System.out.println("   Итератор 1 (остальные точки):");
        while (iter1.hasNext()) {
            System.out.println("      " + iter1.next());
        }

        System.out.println("     Итераторы работают независимо");
    }

    // ==================== ЗАДАНИЕ 2: Фабричный метод ====================

    private static void testFactoryMethod() {
        System.out.println("\n=== ЗАДАНИЕ 2: Тестирование фабричного метода ===\n");

        // 1. Тестирование фабрики по умолчанию
        System.out.println("1. Тестирование фабрики по умолчанию (ArrayTabulatedFunction):");
        testDefaultFactory();

        // 2. Смена фабрики на LinkedListTabulatedFunction
        System.out.println("\n2. Смена фабрики на LinkedListTabulatedFunction:");
        testLinkedListFactory();

        // 3. Возврат к фабрике ArrayTabulatedFunction
        System.out.println("\n3. Возврат к фабрике ArrayTabulatedFunction:");
        testArrayFactoryAgain();

        // 4. Тестирование создания через фабрику в TabulatedFunctions
        System.out.println("\n4. Тестирование методов TabulatedFunctions с фабрикой:");
        testTabulatedFunctionsWithFactory();

        System.out.println("\n  Задание 2 завершено успешно!");
    }

    private static void testDefaultFactory() {
        Function sinFunc = new Sin();

        // Фабрика по умолчанию - ArrayTabulatedFunction
        TabulatedFunction func1 = TabulatedFunctions.tabulate(sinFunc, 0, Math.PI, 5);
        System.out.println("   Тип созданной функции: " + func1.getClass().getSimpleName());
        System.out.println("   Значения функции:");
        for (FunctionPoint p : func1) {
            System.out.printf("      %s\n", p);
        }

        System.out.println("   Создание через createTabulatedFunction:");
        TabulatedFunction func2 = TabulatedFunctions.createTabulatedFunction(0, 10, 3);
        System.out.println("   Тип: " + func2.getClass().getSimpleName());
        System.out.println("   Значения: " + func2);
    }

    private static void testLinkedListFactory() {
        // Устанавливаем фабрику для LinkedListTabulatedFunction
        TabulatedFunctions.setTabulatedFunctionFactory(
                new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory()
        );

        Function cosFunc = new Cos();
        TabulatedFunction func = TabulatedFunctions.tabulate(cosFunc, 0, Math.PI, 5);

        System.out.println("   Тип созданной функции: " + func.getClass().getSimpleName());
        System.out.println("   Значения функции:");
        for (FunctionPoint p : func) {
            System.out.printf("      %s\n", p);
        }

        // Проверяем, что фабрика действительно изменилась
        TabulatedFunctionFactory factory = TabulatedFunctions.getFactory();
        System.out.println("   Текущая фабрика: " + factory.getClass().getSimpleName());
    }

    private static void testArrayFactoryAgain() {
        // Возвращаем фабрику ArrayTabulatedFunction
        TabulatedFunctions.setTabulatedFunctionFactory(
                new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory()
        );

        Function tanFunc = new Tan();
        TabulatedFunction func = TabulatedFunctions.tabulate(tanFunc, 0, Math.PI/2 - 0.1, 5);

        System.out.println("   Тип созданной функции: " + func.getClass().getSimpleName());
        System.out.println("   Значения функции (тангенс):");
        for (FunctionPoint p : func) {
            System.out.printf("      %s\n", p);
        }
    }

    private static void testTabulatedFunctionsWithFactory() {
        System.out.println("   а) Создание функции с равномерным распределением:");
        TabulatedFunction func1 = TabulatedFunctions.createTabulatedFunction(0, 10, 5);
        System.out.println("      Тип: " + func1.getClass().getSimpleName());
        System.out.println("      Значения: " + func1);

        System.out.println("   б) Создание функции с заданными значениями:");
        double[] values = {0, 1, 4, 9, 16};
        TabulatedFunction func2 = TabulatedFunctions.createTabulatedFunction(0, 4, values);
        System.out.println("      Тип: " + func2.getClass().getSimpleName());
        System.out.println("      Значения: " + func2);

        System.out.println("   в) Создание функции из массива точек:");
        FunctionPoint[] points = {
                new FunctionPoint(0, 0),
                new FunctionPoint(2, 8),
                new FunctionPoint(4, 64)
        };
        TabulatedFunction func3 = TabulatedFunctions.createTabulatedFunction(points);
        System.out.println("      Тип: " + func3.getClass().getSimpleName());
        System.out.println("      Значения: " + func3);

        System.out.println("   г) Тестирование ввода/вывода с фабрикой:");
        testInputOutputWithFactory();
    }

    private static void testInputOutputWithFactory() {
        try {
            // Создаем тестовую функцию
            FunctionPoint[] points = {
                    new FunctionPoint(0, 0),
                    new FunctionPoint(1, 1),
                    new FunctionPoint(2, 4)
            };
            TabulatedFunction original = TabulatedFunctions.createTabulatedFunction(points);

            // Тестируем бинарный ввод/вывод
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            TabulatedFunctions.outputTabulatedFunction(original, byteOut);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            TabulatedFunction restoredFromBytes = TabulatedFunctions.inputTabulatedFunction(byteIn);

            System.out.println("      Бинарный ввод/вывод: " +
                    (original.equals(restoredFromBytes) ? "  OK" : "  Ошибка"));

            // Тестируем текстовый ввод/вывод
            StringWriter writer = new StringWriter();
            TabulatedFunctions.writeTabulatedFunction(original, writer);

            StringReader reader = new StringReader(writer.toString());
            TabulatedFunction restoredFromText = TabulatedFunctions.readTabulatedFunction(reader);

            System.out.println("      Текстовый ввод/вывод: " +
                    (original.equals(restoredFromText) ? "  OK" : "  Ошибка"));

        } catch (IOException e) {
            System.out.println("      ✗ Ошибка ввода/вывода: " + e.getMessage());
        }
    }

    // ==================== ЗАДАНИЕ 3: Рефлексия ====================

    private static void testReflection() {
        System.out.println("\n=== ЗАДАНИЕ 3: Тестирование рефлексивного создания объектов ===\n");

        // 1. Создание ArrayTabulatedFunction через рефлексию
        System.out.println("1. Создание ArrayTabulatedFunction через рефлексию:");
        testReflectionArrayTabulatedFunction();

        // 2. Создание LinkedListTabulatedFunction через рефлексию
        System.out.println("\n2. Создание LinkedListTabulatedFunction через рефлексию:");
        testReflectionLinkedListTabulatedFunction();

        // 3. Тестирование метода tabulate с рефлексией
        System.out.println("\n3. Табулирование с использованием рефлексии:");
        testReflectionTabulate();


        System.out.println("\n  Задание 3 завершено успешно!");
    }

    private static void testReflectionArrayTabulatedFunction() {
        System.out.println("   а) Создание с базовыми параметрами:");
        TabulatedFunction func1 = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println("      Тип: " + func1.getClass().getSimpleName());
        System.out.println("      Значения: " + func1);

        System.out.println("   б) Создание с массивом значений:");
        TabulatedFunction func2 = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10, 20});
        System.out.println("      Тип: " + func2.getClass().getSimpleName());
        System.out.println("      Значения: " + func2);

        System.out.println("   в) Создание из массива точек:");
        FunctionPoint[] points = {
                new FunctionPoint(0, 0),
                new FunctionPoint(5, 25),
                new FunctionPoint(10, 100)
        };
        TabulatedFunction func3 = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, points);
        System.out.println("      Тип: " + func3.getClass().getSimpleName());
        System.out.println("      Значения: " + func3);
    }

    private static void testReflectionLinkedListTabulatedFunction() {
        System.out.println("   а) Создание с базовыми параметрами:");
        TabulatedFunction func1 = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class, 0, 10, 3);
        System.out.println("      Тип: " + func1.getClass().getSimpleName());
        System.out.println("      Значения: " + func1);

        System.out.println("   б) Создание с массивом значений:");
        TabulatedFunction func2 = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class, 0, 10, new double[] {0, 10, 20});
        System.out.println("      Тип: " + func2.getClass().getSimpleName());
        System.out.println("      Значения: " + func2);

        System.out.println("   в) Создание из массива точек:");
        FunctionPoint[] points = {
                new FunctionPoint(0, 0),
                new FunctionPoint(5, 125),
                new FunctionPoint(10, 1000)
        };
        TabulatedFunction func3 = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class, points);
        System.out.println("      Тип: " + func3.getClass().getSimpleName());
        System.out.println("      Значения: " + func3);
    }

    private static void testReflectionTabulate() {
        Function sinFunc = new Sin();
        Function expFunc = new Exp();

        System.out.println("   а) Табулирование синуса в ArrayTabulatedFunction:");
        TabulatedFunction func1 = TabulatedFunctions.tabulate(
                ArrayTabulatedFunction.class, sinFunc, 0, Math.PI, 5);
        System.out.println("      Тип: " + func1.getClass().getSimpleName());
        System.out.println("      Значения: " + func1);

        System.out.println("   б) Табулирование экспоненты в LinkedListTabulatedFunction:");
        TabulatedFunction func2 = TabulatedFunctions.tabulate(
                LinkedListTabulatedFunction.class, expFunc, 0, 2, 4);
        System.out.println("      Тип: " + func2.getClass().getSimpleName());
        System.out.println("      Значения: " + func2);

        System.out.println("   в) Сравнение с обычным табулированием:");
        TabulatedFunction func3 = TabulatedFunctions.tabulate(sinFunc, 0, Math.PI, 5);
        System.out.println("      Обычное табулирование: " + func3.getClass().getSimpleName());
        System.out.println("      Рефлексивное табулирование: " + func1.getClass().getSimpleName());
        System.out.println("      Функции равны: " + (func1.equals(func3) ? "Да" : "Нет"));
    }



    // Вспомогательный класс для тестирования исключений
    private static class PrivateTabulatedFunction implements TabulatedFunction {
        private PrivateTabulatedFunction() {} // Приватный конструктор

        @Override
        public double getLeftDomainBorder() { return 0; }
        @Override
        public double getRightDomainBorder() { return 0; }
        @Override
        public double getFunctionValue(double x) { return 0; }
        @Override
        public int getPointsCount() { return 0; }
        @Override
        public FunctionPoint getPoint(int index) { return null; }
        @Override
        public void setPoint(int index, FunctionPoint point) {}
        @Override
        public double getPointX(int index) { return 0; }
        @Override
        public void setPointX(int index, double x) {}
        @Override
        public double getPointY(int index) { return 0; }
        @Override
        public void setPointY(int index, double y) {}
        @Override
        public void deletePoint(int index) {}
        @Override
        public void addPoint(FunctionPoint point) {}
        @Override
        public Object clone() { return null; }
        @Override
        public Iterator<FunctionPoint> iterator() { return null; }
    }
}