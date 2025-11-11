package threads;

import functions.basic.Log;

public class SimpleGenerator implements Runnable{
    private Task obj;
    public SimpleGenerator(Task obj) {
        this.obj = obj;
    }
    @Override
    public void run() {
        for (int i = 0; i < obj.getCount();i++) {
            synchronized (obj) { //синхронизируем
                obj.setF(new Log((Math.random() * 9) + 1)); //[1;10)
                obj.setLeft(Math.random() * 100); //[0;100)
                obj.setRight(Math.random() * 100 + 100); //[100;200)
                obj.setDiskr(Math.random());//[0;1)
                System.out.println("Номер итерации "+i);
                System.out.println("Source <" + obj.getLeft() + "> <" + obj.getRight() + "> <" + obj.getDiskr() + ">");
                try {
                    obj.wait(); //ждем пока другой поток не вызовет notify()
                } catch (InterruptedException e) {
                    System.out.println("Исключение");
                }
            }
        }
    }
}
