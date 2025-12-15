package functions;


public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException{

    // Конструктор 1: с сообщением об ошибке
        public FunctionPointIndexOutOfBoundsException(String errmesg) {
            super(errmesg); // Передаем сообщение в конструктор IndexOutOfBoundsException
        }
}
