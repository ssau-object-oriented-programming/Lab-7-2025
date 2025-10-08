package functions;

public interface Function {
    /**
     * Метод получения левой границы по X
     * @return левая граница X
     */
    double getLeftDomainBorder();
    /**
     * Метод получения правой границы по X
     * @return правая граница X
     */
    double getRightDomainBorder();
    /**
     * Получение значения в данной точке
     * @param x точка по X
     * @return интерполированное линейно значение в данной точке(Y)
     */
    double getFunctionValue(double x);
}
