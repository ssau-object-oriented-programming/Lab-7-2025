package threads;

import functions.basic.Log;
import functions.Function;
import java.util.Random;

public class Generator extends Thread {
    private Task task;
    private Semaphore semaphore;
    private Random random;
    
    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
        this.random = new Random();
    }
    
    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                // Захватываем семафор для записи
                semaphore.beginWrite();
                
                // Проверяем, не был ли поток прерван
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                
                // Создаем логарифмическую функцию
                double base = 1 + random.nextDouble() * 9;
                Function logFunction = new Log(base);
                task.setFunction(logFunction);
                
                // Устанавливаем границы и шаг
                double leftBorder = random.nextDouble() * 100;
                task.setLeftBorder(leftBorder);
                
                double rightBorder = 100 + random.nextDouble() * 100;
                task.setRightBorder(rightBorder);
                
                double step = random.nextDouble();
                task.setStep(step);
                
                // Помечаем данные как готовые
                task.setDataReady(true);
                
                // Выводим исходные данные
                System.out.printf("Source %.6f %.6f %.6f%n", 
                    task.getLeftBorder(), task.getRightBorder(), task.getStep());
                
                // Освобождаем семафор
                semaphore.endWrite();
                
                // Небольшая пауза
                Thread.sleep(10);
            }
            
            System.out.println("Generator: все задания созданы");
            
        } catch (InterruptedException e) {
            System.err.println("Generator: поток был прерван");
        } catch (Exception e) {
            System.err.println("Generator: ошибка: " + e.getMessage());
        }
    }
}