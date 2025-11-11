package threads;

import functions.Functions;
import functions.basic.Log;

public class SimpleIntegrator implements Runnable{
    private Task obj;
    public SimpleIntegrator(Task obj) {
        this.obj = obj;
    }
    @Override
    public void run() {
        for (int i = 0; i < obj.getCount();i++){
            try { //добавляем задержку, чтобы поток generator успел начать свое выполнение
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Исключение");
            }
            try {
                if (obj.getF() != null) { //если функция установлена
                    double result = Functions.integral(obj.getF(), obj.getLeft(), obj.getRight(), obj.getDiskr());
                    System.out.println("Result <" + obj.getLeft() + "> <" + obj.getRight() + "> <" + obj.getDiskr() + "> <" + result + ">");
                    System.out.println();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            synchronized(obj) { //синхронизируем, чтобы оба потока не могли одновременно работать с объектом

                obj.notify(); //будим поток generator
            }
        }
    }
}
