import functions.*;
import functions.basic.*;
import java.io.*;


public class Main {

    public static void main(String[] args) throws IOException, FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException, ClassNotFoundException, InterruptedException {

        double[] values = {1, 5, 3, 10, 2};
        ArrayTabulatedFunction arrayFunc1 = new ArrayTabulatedFunction(1, 15, values);
        LinkedListTabulatedFunction listFunc1 = new LinkedListTabulatedFunction(1, 15, values);

        System.out.println("\nArrayTabulatedFunction\n");
		for (FunctionPoint p : arrayFunc1) {
			System.out.println(p);
		}
        System.out.println("\nLinkedListTabulatedFunction\n");
        for (FunctionPoint p : listFunc1) {
			System.out.println(p);
		}

        System.out.println("\n-------\n");
        
        Function f = new Cos();
        TabulatedFunction tf;
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());

        System.err.println("\n-------\n");

        TabulatedFunction f1;

        f1 = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(f1.getClass());
        System.out.println(f1);

        f1 = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(f1.getClass());
        System.out.println(f1);

        f1 = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class, new FunctionPoint[] {new FunctionPoint(0, 0), new FunctionPoint(10, 10)});

        System.out.println(f1.getClass());
        System.out.println(f1);

        f1 = TabulatedFunctions.tabulate(LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(f1.getClass());
        System.out.println(f1);
    }
}


