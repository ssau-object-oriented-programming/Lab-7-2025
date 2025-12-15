package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable{

    private double coorX, coorY; // Координаты X, Y
    public static final double epsilon; // Создадим статическое поле, которое отвечает за машинный epsilon

    static {
        double eps = 1.0;
        while (1.0 + eps / 2.0 != 1.0) {
            eps /= 2.0;
        }
        epsilon = eps;
    }

    public static boolean equal(double a, double b){
        return Math.abs(a - b) <= epsilon;
    }

    public static boolean largeThan(double a, double b){
        return (a - b) > epsilon;
    }

    public FunctionPoint(double x, double y){ // Конструктор с двумя параметрами
        this.coorX = x;
        this.coorY = y;
    }

    public FunctionPoint(FunctionPoint point){ // Коструктор с аргументом точки
        this.coorX = point.getCoorX();
        this.coorY = point.getCoorY();
    }

    public FunctionPoint(){ // Конструктор по умолчанию
        this.coorX = 0;
        this.coorY = 0;
    }
    
    // Сеттеры
    public void setCoorX(double x){this.coorX = x;} 
    public void setCoorY(double y) {this.coorY = y;}

    // Геттеры
    public double getCoorX(){return this.coorX;}
    public double getCoorY() {return this.coorY;}

    @Override
    public String toString(){
        return String.format("(%.3f, %.3f)", coorX, coorY);
    }

    @Override
    public boolean equals(Object a){
        if (this == a){
            return true;
        }

        if (!(a instanceof FunctionPoint) || a == null){
            return false;
        }

        FunctionPoint b = (FunctionPoint) a;

        if (equal(coorX, b.getCoorX()) && equal(coorY, b.getCoorY())){
            return true;
        }else{
            return false;
        }
        
    }

    @Override
    public int hashCode() {

        long xBits = Double.doubleToLongBits(coorX);
        long yBits = Double.doubleToLongBits(coorY);

        int xHigh = (int) (xBits >>> 32); 
        int xLow = (int) (xBits & 0xFFFFFFFFL); 

        int yHigh = (int) (yBits >>> 32); 
        int yLow = (int) (yBits & 0xFFFFFFFFL); 


        return xHigh ^ xLow ^ yHigh ^ yLow;
    }

    @Override
    public FunctionPoint clone(){
        return new FunctionPoint(this);
    }



}