package functions;
//исключение, выбрасываемое при попытке добавления или изменения точки функции несоответствующим образом
public class InappropriateFunctionPointException extends Exception{
    public InappropriateFunctionPointException() {
        super();
    }
    public InappropriateFunctionPointException(String m) {
        super(m);
    }
}
