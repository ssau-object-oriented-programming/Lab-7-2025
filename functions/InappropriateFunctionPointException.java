package functions;

public class InappropriateFunctionPointException extends Exception{
    // Конструктор без параметров
    public InappropriateFunctionPointException() {
        super();
    }

    // Конструктор с сообщением
    public InappropriateFunctionPointException(String message) {
        super(message);
    }
}
