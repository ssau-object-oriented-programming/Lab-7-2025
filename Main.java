package functions;

import functions.basic.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== TEST ITERATORS ===");

        // Тестирование итератора
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 10, new double[]{0, 1, 4, 9, 16});
        System.out.println("ArrayTabulatedFunction points:");
        for (FunctionPoint point : arrayFunc) {
            System.out.println(point);
        }

        TabulatedFunction listFunc = new LinkedListTabulatedFunction(0, 10, new double[]{0, 1, 4, 9, 16});
        System.out.println("\nLinkedListTabulatedFunction points:");
        for (FunctionPoint point : listFunc) {
            System.out.println(point);
        }

        System.out.println("\n=== ITERATOR TEST COMPLETED ===");

        // ===================================================
        System.out.println("\n=== TEST FACTORY METHOD ===");

        Function cos = new Cos();
        TabulatedFunction tf;

        // Тест 1: Фабрика по умолчанию (ArrayTabulatedFunction)
        tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
        System.out.println("Default factory: " + tf.getClass().getSimpleName());

        // Тест 2: Меняем на LinkedListTabulatedFunctionFactory
        TabulatedFunctions.setTabulatedFunctionFactory(new
                LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
        System.out.println("LinkedList factory: " + tf.getClass().getSimpleName());

        // Тест 3: Возвращаем на ArrayTabulatedFunctionFactory
        TabulatedFunctions.setTabulatedFunctionFactory(new
                ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
        System.out.println("Array factory: " + tf.getClass().getSimpleName());

        System.out.println("=== FACTORY TEST COMPLETED ===");

        // ===================================================
        System.out.println("\n=== TEST REFLECTION METHODS ===");

        TabulatedFunction f;

        // Тест 1: Создание ArrayTabulatedFunction через рефлексию
        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println("Array via reflection: " + f.getClass().getSimpleName());
        System.out.println(f);

        // Тест 2: Создание LinkedListTabulatedFunction через рефлексию
        f = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class,
                0, 10, new double[]{0, 5, 10});
        System.out.println("LinkedList via reflection: " + f.getClass().getSimpleName());
        System.out.println(f);

        // Тест 3: Табулирование с рефлексией
        f = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println("Tabulate with reflection: " + f.getClass().getSimpleName());
        System.out.println(f);

        System.out.println("=== REFLECTION TEST COMPLETED ===");

        // ===================================================
        System.out.println("\n=== TEST BINARY INPUT/OUTPUT WITH FACTORY ===");

        try {
            TabulatedFunction originalFunc = new ArrayTabulatedFunction(
                    0, 10, new double[]{0, 1, 4, 9, 16, 25});

            // Записываем в байтовый поток
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            TabulatedFunctions.outputTabulatedFunction(originalFunc, byteOut);

            // Читаем обратно с фабрикой по умолчанию (Array)
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            TabulatedFunction funcFromBinary = TabulatedFunctions.inputTabulatedFunction(byteIn);
            System.out.println("From binary stream (factory): " + funcFromBinary.getClass().getSimpleName());

            // Читаем с указанием конкретного класса через рефлексию
            byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            TabulatedFunction funcFromBinaryReflection = TabulatedFunctions.inputTabulatedFunction(
                    LinkedListTabulatedFunction.class, byteIn);
            System.out.println("From binary stream (reflection): " + funcFromBinaryReflection.getClass().getSimpleName());

            System.out.println("Original: " + originalFunc);
            System.out.println("From binary (factory): " + funcFromBinary);
            System.out.println("From binary (reflection): " + funcFromBinaryReflection);

        } catch (Exception e) {
            System.err.println("Binary I/O error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== BINARY I/O TEST COMPLETED ===");

        // ===================================================
        System.out.println("\n=== TEST TEXT INPUT/OUTPUT ===");

        try {
            TabulatedFunction originalFunc = new ArrayTabulatedFunction(
                    0, 10, new double[]{0, 1, 4, 9, 16});

            // Записываем в символьный поток
            StringWriter stringWriter = new StringWriter();
            TabulatedFunctions.writeTabulatedFunction(originalFunc, stringWriter);
            String serialized = stringWriter.toString();

            // Читаем обратно с фабрикой по умолчанию
            StringReader stringReader = new StringReader(serialized);
            TabulatedFunction funcFromText = TabulatedFunctions.readTabulatedFunction(stringReader);
            System.out.println("From text stream (factory): " + funcFromText.getClass().getSimpleName());

            // Читаем с указанием конкретного класса через рефлексию
            stringReader = new StringReader(serialized);
            TabulatedFunction funcFromTextReflection = TabulatedFunctions.readTabulatedFunction(
                    LinkedListTabulatedFunction.class, stringReader);
            System.out.println("From text stream (reflection): " + funcFromTextReflection.getClass().getSimpleName());

            System.out.println("Original: " + originalFunc);
            System.out.println("From text (factory): " + funcFromText);
            System.out.println("From text (reflection): " + funcFromTextReflection);

        } catch (Exception e) {
            System.err.println("Text I/O error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== TEXT I/O TEST COMPLETED ===");
    }
}