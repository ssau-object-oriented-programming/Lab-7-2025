package threads;

import functions.Function;
import functions.basic.Log;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Generator extends Thread{
    private Task task;
    private final Semaphore dataReady;
    private final Semaphore dataProcessed;

    public Generator(Task task, Semaphore dataReady, Semaphore dataProcessed) {
        this.task = task;
        this.dataReady = dataReady;
        this.dataProcessed = dataProcessed;
    }
    
    public void run() {
        Random rand = new Random();
        int generated = 0;

        try {
            for (int i = 0; i < task.getTaskCount(); ++i) {
                 // Проверяем не прерван ли поток
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                double base = 1.1 + rand.nextDouble() * 8.9;
                double left = rand.nextDouble() * 100;
                double right = 100 + rand.nextDouble() * 100;
                double step = 0.001 + rand.nextDouble() * 0.999;
                    
                // Используем tryAcquire с таймаутом вместо acquire
                if (!dataProcessed.tryAcquire(50, TimeUnit.MILLISECONDS)) {
                    // Если не получили семафор за 10 мс, проверяем прерывание
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    i--; // Повторяем эту итерацию
                    continue;
                }

                // Записываем данные
                task.setFunction(new Log(base));
                task.setLeft(left);
                task.setRight(right);
                task.setStep(step);
                    
                generated++;
                System.out.printf("(Generator %d) Сгенерировано: основание = %.4f, левая граница = %.6f, правая граница = %.6f, шаг = %.6f\n", 
                            generated, base, left, right, step);
                    
                // Сигнализируем, что данные готовы
                dataReady.release();
                    
                Thread.sleep(5);

                } System.out.println("Generator завершил все " + generated + " заданий");
            
                } catch (InterruptedException e) {
                    System.out.println("Generator прерван. Сгенерировано: " + generated);
                }
            }
}
            
           
