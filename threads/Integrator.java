package threads;

import functions.Functions;

import java.util.concurrent.Semaphore;

public class Integrator extends Thread{
    private Task obj;
    private Semaphore semaphoreGen;
    private Semaphore semaphoreInt;
    public Integrator(Task obj, Semaphore semaphoreGen, Semaphore semaphoreInt) {
        this.obj = obj;
        this.semaphoreGen = semaphoreGen;
        this.semaphoreInt = semaphoreInt;
    }
    @Override
    public void run() {
        for (int i = 0; i < obj.getCount();i++){
            try {
                semaphoreInt.acquire();//Уменьшили счетчик семафора у интегратора
            } catch (InterruptedException e) {
                System.out.println("Поток Integrator прерван");
                Thread.currentThread().interrupt();
                return;
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
            semaphoreGen.release(); //Увеличили счетчик семафора у генератора, чтобы его поток начал выполнение
        }
    }
}
