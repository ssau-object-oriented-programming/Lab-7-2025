import functions.*;
import functions.basic.*;
import threads.*;
import java.util.Iterator;


public class Main {

    public static void ReflectionMethods() {

        TabulatedFunction f;

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[] {0, 5, 10});
        System.out.println(f.getClass());
        System.out.println(f);


        f = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class,
                new FunctionPoint[] {
                        new FunctionPoint(0, 0),
                        new FunctionPoint(5, 25),
                        new FunctionPoint(10, 100)
                }
        );

        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(f.getClass());
        System.out.println(f);

    }

    public static void main(String[] args) {
        ReflectionMethods();
    }
}