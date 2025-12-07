import functions.*;
import functions.basic.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        
        // Задание 1 - Итератор
        System.out.println("Тест Итератора");
        Function fCos = new Cos();
        TabulatedFunction tfIterator = TabulatedFunctions.tabulate(fCos, 0, Math.PI, 5);
        for (FunctionPoint p : tfIterator) {
            System.out.println(p);
        }

        // Задание 2 - Фабрики
        System.out.println("\nТест Фабрик");
        Function f = new Cos();
        TabulatedFunction tf;
        
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("Фабрика по умолчанию создала: " + tf.getClass().getSimpleName());
        
        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("Фабрика LinkedList создала: " + tf.getClass().getSimpleName());
        
        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("Фабрика Array создала: " + tf.getClass().getSimpleName());


        // Задание 3 - Рефлексия
        System.out.println("\nТест Рефлексии");
        
        TabulatedFunction fReflectArray;
        fReflectArray = TabulatedFunctions.createTabulatedFunction(
          ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println("Через рефлексию создан объект Array: " + fReflectArray.getClass().getSimpleName());
        System.out.println(fReflectArray);

        fReflectArray = TabulatedFunctions.createTabulatedFunction(
          ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println("Через рефлексию создан объект Array (по значениям): " + fReflectArray.getClass().getSimpleName());
        System.out.println(fReflectArray);

        TabulatedFunction fReflectList;
        fReflectList = TabulatedFunctions.createTabulatedFunction(
          LinkedListTabulatedFunction.class, 
          new FunctionPoint[] {
            new FunctionPoint(0, 0),
            new FunctionPoint(10, 10)
          }
        );
        System.out.println("Через рефлексию создан объект LinkedList (по точкам): " + fReflectList.getClass().getSimpleName());
        System.out.println(fReflectList);

        TabulatedFunction fTabulateReflect = TabulatedFunctions.tabulate(
          LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println("Через рефлексию (tabulate) создан объект: " + fTabulateReflect.getClass().getSimpleName());
        System.out.println(fTabulateReflect);

        // Тест ввода/вывода с рефлексией
        System.out.println("\nТест ввода/вывода с рефлексией");

        // Тест байтового потока
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            TabulatedFunctions.outputTabulatedFunction(fTabulateReflect, out);
            try (ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray())) {
                 // Читаем как ArrayTabulatedFunction, хотя писали LinkedList
                 TabulatedFunction fRes = TabulatedFunctions.inputTabulatedFunction(ArrayTabulatedFunction.class, in);
                 System.out.println("Байтовый поток (читаем в Array): " + fRes.getClass().getSimpleName());
                 System.out.println(fRes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Тест символьного потока
        try (StringWriter writer = new StringWriter()) {
            TabulatedFunctions.writeTabulatedFunction(fTabulateReflect, writer);
            try (StringReader reader = new StringReader(writer.toString())) {
                // Читаем как LinkedListTabulatedFunction
                TabulatedFunction fRes = TabulatedFunctions.readTabulatedFunction(LinkedListTabulatedFunction.class, reader);
                System.out.println("Символьный поток (читаем в LinkedList): " + fRes.getClass().getSimpleName());
                System.out.println(fRes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
