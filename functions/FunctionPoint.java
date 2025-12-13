package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {
    double x;
    double y;
	private static final double EPSILON = 1e-10;

    
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    
    public FunctionPoint(FunctionPoint p) {
        x = p.x;
        y = p.y;
    }

 
    public FunctionPoint() {
        x = 0;
        y = 0;
    }

   public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }
	
	//Возвращаем текстовое описание точки в формате (x; y)
	@Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        FunctionPoint that = (FunctionPoint) o;
        
        // Сравниваем координаты с учетом погрешности для чисел с плавающей точкой
        return Math.abs(that.x - x) < EPSILON && Math.abs(that.y - y) < EPSILON;
    }
	
	//Возвращаем хэш-код точки на основе её координат
	@Override
    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);
        
        // Преобразуем double в два int и применяем XOR
        int xHash = (int)(xBits ^ (xBits >>> 32));
        int yHash = (int)(yBits ^ (yBits >>> 32));
        
        return xHash ^ yHash;
    }
	
	//Создаем и возвращаем копию текущей точки
	@Override
    public Object clone() {
        return new FunctionPoint(this.x, this.y);
    }
}
