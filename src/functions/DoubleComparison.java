package functions;

public class DoubleComparison {

    public static final double EPSILON = 1e-10;
    //Сравнение чисел с плавающей точкой

    //равенство
    public static boolean doubleEq(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }
    //строго меньше
    public static boolean doubleLess(double a, double b) {
        return a < b - EPSILON;
    }
    //сторого больше
    public static boolean doubleGreater(double a, double b) {
        return a > b + EPSILON;
    }
    //меньше или равно
    public static boolean doubleLessOrEq(double a, double b) {
        return doubleLess(a,b) || doubleEq(a,b);
    }
    //больше или равно
    public static boolean doubleGreaterOrEq(double a, double b) {
        return doubleGreater(a,b) || doubleEq(a,b);
    }
}
