package threads;

import functions.basic.Log;
import functions.Function;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private Task task;
    private Random random;
    
    public SimpleGenerator(Task task) {
        this.task = task;
        this.random = new Random();
    }
    
    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                synchronized (task) {
                    // Ждем, пока предыдущие данные будут обработаны (кроме первой итерации)
                    if (i > 0) {
                        while (task.isDataReady()) {
                            task.wait();
                        }
                    }
                    
                    // Создаем логарифмическую функцию со случайным основанием от 1 до 10
                    double base = 1 + random.nextDouble() * 9; 
                    Function logFunction = new Log(base);
                    task.setFunction(logFunction);
                    
                    // Левая граница от 0 до 100
                    double leftBorder = random.nextDouble() * 100;
                    task.setLeftBorder(leftBorder);
                    
                    // Правая граница от 100 до 200
                    double rightBorder = 100 + random.nextDouble() * 100;
                    task.setRightBorder(rightBorder);
                    
                    // Шаг дискретизации от 0 до 1
                    double step = random.nextDouble();
                    task.setStep(step);
                    
                    // Помечаем данные как готовые
                    task.setDataReady(true);
                    
                    // Выводим исходные данные
                    System.out.printf("Source %.6f %.6f %.6f%n", 
                        task.getLeftBorder(), task.getRightBorder(), task.getStep());
                    
                    
                    task.notifyAll();
                    
                    // Если это не последняя итерация, ждем пока интегрирующий поток обработает
                    if (i < task.getTasksCount() - 1) {
                        task.wait();
                    }
                }
                
                
                Thread.sleep(10);
            }
            
            System.out.println("Generator: все задания созданы");
            
        } catch (InterruptedException e) {
            System.err.println("Generator: поток был прерван");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Generator: ошибка: " + e.getMessage());
        }
    }
}