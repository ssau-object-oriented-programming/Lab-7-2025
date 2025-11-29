package  functions;

public class FunctionPoint{
    private double x;
    private double y;

    // конструктор с параметрами
    public FunctionPoint(double x, double y){
        this.x = x;
        this.y = y;
    }
    // конструктор копирования
    public FunctionPoint(FunctionPoint point){
        this.x = point.x;
        this.y = point.y;
    }
    // конструктор по умолчанию
    public FunctionPoint(){
        this.x = 0;
        this.y = 0;
    }
    @Override
    public String toString(){
        return "("+this.x+ "," + this.y + ")"; // возвращаем строку с координатами точки
    }

    @Override
    public boolean equals(Object o){
        if( o == null || this.getClass() != o.getClass()){ // проверяем что объект не null и того же класса
            return false; // возвращаем false если классы разные
        }
        if(o == this){ // проверяем что это тот же объект
            return true; // возвращаем true если объекты одинаковые
        }
        return Math.abs(this.x - ((FunctionPoint) o).x) < 1e-10 && Math.abs(this.y- ((FunctionPoint) o).y) < 1e-10; // сравниваем координаты x и y
    }

    @Override
    public int hashCode(){
        long xBit = Double.doubleToLongBits(this.x); // получаем битовое представление x
        long yBit = Double.doubleToLongBits(this.y); // получаем битовое представление y

        int x1 = (int)(xBit & 0xFFFFFFFF); // младшие 4 байта x
        int x2 = (int)(xBit >>> 32); // старшие 4 байта x
        int y1 = (int)(yBit & 0xFFFFFFFF); // младшие 4 байта y
        int y2 = (int)(yBit >>> 32); // старшие 4 байта y

        return x1^x2^y1^y2; // комбинируем все части через xor
    }

    @Override
    public Object clone(){
        return new FunctionPoint(this.x, this.y); // создаем новую точку с теми же координатами
    }
    // получить x
    public double getX(){
        return x;
    }
    // получить y
    public double getY(){
        return y;
    }
    // установить x
    public void setX(double x){
        this.x = x;
    }
    // установить y
    public void setY(double y){
        this.y = y;
    }
}