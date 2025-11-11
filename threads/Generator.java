package threads;

import functions.basic.Log;

import java.util.concurrent.Semaphore;

public class Generator extends Thread{
    private Task obj;
    private Semaphore semaphoreGen;
    private Semaphore semaphoreInt;
    public Generator(Task obj, Semaphore semaphoreGen, Semaphore semaphoreInt) {
        this.obj = obj;
        this.semaphoreGen = semaphoreGen;
        this.semaphoreInt = semaphoreInt;
    }
    @Override
    public void run() {
        for (int i = 0; i < obj.getCount(); i++) {
            try {
                semaphoreGen.acquire(); //Уменьшили счетчик семафора у генератора
            } catch (InterruptedException e) {
                System.out.println("Поток Generator прерван");
                Thread.currentThread().interrupt(); //восстановили статус прерывания
                return;
            }
            obj.setF(new Log((Math.random() * 9) + 1)); //[1;10)
            obj.setLeft(Math.random() * 100); //[0;100)
            obj.setRight(Math.random() * 100 + 100); //[100;200)
            obj.setDiskr(Math.random());//[0;1)
            System.out.println("Номер итерации " + i);
            System.out.println("Source <" + obj.getLeft() + "> <" + obj.getRight() + "> <" + obj.getDiskr() + ">");
            semaphoreInt.release(); //Увеличили счетчик семафора у интегратора, чтобы его поток начал выполнение
        }
    }
}
