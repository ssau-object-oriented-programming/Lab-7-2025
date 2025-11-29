package functions;

public interface Function {
    // получить левую границу области определения функции
    public double getLeftDomainBorder();
    // получить правую границу области определения функции
    public double getRightDomainBorder();
    // вычислить значение функции в заданной точке x
    public double getFunctionValue(double x);
}
