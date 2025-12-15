package functions;

import java.io.Serializable;

public class FunctionNode implements Serializable{

    private FunctionPoint point;
    private FunctionNode prev;
    private FunctionNode next;

    public FunctionNode(FunctionPoint point) {
        this.point = (point != null) ? new FunctionPoint(point) : null;
    }

    public void setPrev(FunctionNode prev){ this.prev = prev;}
    public void setNext(FunctionNode next){this.next = next;}
    public void setPoint(FunctionPoint newPoint){
        point.setCoorX(newPoint.getCoorX());
        point.setCoorY(newPoint.getCoorY());
    }

    public FunctionNode getPrev(){ return prev;} // возвращает ссылку!!!
    public FunctionNode getNext(){return next;}
    public FunctionPoint getPoint() throws IllegalStateException{
        if (point == null)
            throw new IllegalStateException("Текущий узел имеет point равный null");
        return point;} // возвращаем ссылку
}