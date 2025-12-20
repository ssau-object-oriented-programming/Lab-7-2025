package functions;

public interface Function {
    //возвращает значение левой границы области определения функции
    double getLeftDomainBorder();

    //возвращает значение правой  границы области определения функции
    double getRightDomainBorder();

    //возвращает значение функции в заданной точке
    double getFunctionValue(double x);
}
