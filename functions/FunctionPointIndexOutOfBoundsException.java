package functions;

// исключение выхода за границы точек
public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException{
    // конструктор по умолчанию
    public FunctionPointIndexOutOfBoundsException() {
        // вызываем конструктор родителя
        super();
    }
    // конструктор с сообщением
    public FunctionPointIndexOutOfBoundsException(String mes) {
        // передаем сообщение родителю
        super(mes);
    }
}