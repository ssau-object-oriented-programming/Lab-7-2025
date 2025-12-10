package threads;

import functions.Function;
import functions.basic.*;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private final Task task;
    
    public SimpleGenerator(Task task){
        this.task = task;
    }
        
    @Override
    public void run(){
        Random random = new Random();

        for (int i = 0; i < task.getTaskCount(); i++) {
            try {
                double base = 1 + random.nextDouble() * 9;
                Function logFunction = new Log(base);

                double left = random.nextDouble() * 100;
                double right = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();

                synchronized (task){
                    task.setFunction(logFunction);
                    task.setLeftBorder(left);
                    task.setRightBorder(right);
                    task.setStep(step);
                    System.out.printf("(SimpleGenerator %d) Задание: левая граница = %.6f, правая граница = %.6f, шаг = %.6f%n", i, left, right, step);
                }
                Thread.sleep(1);

            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка при генерации" + e.getMessage());
            } catch (InterruptedException e) {
                e.getMessage();
            }
        }
        System.out.println("Конец simpleThreads");
    }
}

