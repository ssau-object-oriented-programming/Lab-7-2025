package functions.meta;

import functions.Function;

public class Composition implements Function{
    private Function f;  // f(g(x)) - внешняя
    private Function g;  // f(g(x)) - внутренняя
    
    public Composition(Function f, Function g) {
        this.f = f;
        this.g = g;
    }

    public double getLeftDomainBorder() {
        // Просто берем область определения ВТОРОЙ (внутренней) функции
        return g.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return g.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        // Просто вычисляем f(g(x)) без сложных проверок
        return f.getFunctionValue(g.getFunctionValue(x));
    }

}
