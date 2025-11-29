package functions;

// исключение неподходящей точки
public class InappropriateFunctionPointException extends Exception{
    // конструктор по умолчанию
    public InappropriateFunctionPointException(){
        // вызываем конструктор родителя
        super();
    }
    // конструктор с сообщением
    public InappropriateFunctionPointException(String mes){
        // передаем сообщение родителю
        super(mes);
    }
}