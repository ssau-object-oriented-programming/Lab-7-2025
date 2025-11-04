package functions.meta;
import functions.Function;

public class Scale implements Function {
    private Function f;
    private double scaleX;
    private double scaleY;

    public Scale(Function f, double scaleX, double scaleY) {
        this.f = f;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public double getLeftDomainBorder() {
        if (scaleX > 0)
            return f.getLeftDomainBorder() * scaleX;
        if (scaleX < 0)
            return f.getRightDomainBorder() * scaleX;
        return Double.NaN;
    }

    public double getRightDomainBorder() {
        if (scaleX > 0)
            return f.getRightDomainBorder() * scaleX;
        if (scaleX < 0)
            return f.getLeftDomainBorder() * scaleX;
        return Double.NaN;
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder())
            return Double.NaN;
        return f.getFunctionValue(x) * scaleY;
    }
}