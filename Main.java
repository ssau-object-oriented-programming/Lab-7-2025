import functions.*;
import functions.basic.Cos;
import functions.basic.Sin;

public class Main
{
    public static void main(String[] args)
    {
        // проверка for-each
        System.out.println("First task:");

        TabulatedFunction f = new ArrayTabulatedFunction(1, 10, 10);

        for(FunctionPoint p : f)
        {
            System.out.println(p);
        }

        // проверка установки типа TabulatedFunctionFactory
        System.out.println("\nSecond task:");

        Function f1 = new Cos();
        TabulatedFunction tf;
        tf = TabulatedFunctions.tabulate(f1, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new
                LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f1, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new
                ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f1, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        // проверка рефлексивного создания объектов
        System.out.println("\nThird task:");

        TabulatedFunction f2;

        f2 = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(f2.getClass());
        System.out.println(f2);

        f2 = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(f2.getClass());
        System.out.println(f2);

        f2 = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class,
                new FunctionPoint[] {
                        new FunctionPoint(0, 0),
                        new FunctionPoint(10, 10)
                }
        );
        System.out.println(f2.getClass());
        System.out.println(f2);

        f2 = TabulatedFunctions.tabulate(
                LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(f2.getClass());
        System.out.println(f2);
    }
}
