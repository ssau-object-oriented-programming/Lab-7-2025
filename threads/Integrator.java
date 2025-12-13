package threads;

import functions.Function;
import functions.Functions;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Integrator extends Thread {
    private Task task;
    private final Semaphore dataReady;
    private final Semaphore dataProcessed;
    
    public Integrator(Task task, Semaphore dataReady, Semaphore dataProcessed) {
        this.task = task;
        this.dataReady = dataReady;
        this.dataProcessed = dataProcessed;
    }
    

    public void run() {
int processed = 0;
        
        try {
            for (int i = 0; i < task.getTaskCount(); ++i) {
                // Проверяем не прерван ли поток
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                
                // Пытаемся получить семафор с таймаутом
                if (!dataReady.tryAcquire(10, TimeUnit.MILLISECONDS)) {
                    // Если не получили семафор за 10 мс, проверяем прерывание
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    i--; // Повторяем эту итерацию
                    continue;
                }
                
                double left = task.getLeft();
                double right = task.getRight();
                double step = task.getStep();
                double result = Functions.integral(task.getFunction(), left, right, step);
                
                processed++;
                System.out.printf("(Integrator %d) Результат: левая граница = %.6f, правая граница = %.6f, шаг = %.6f, значение интеграла = %.6f\n", 
                                processed, left, right, step, result);
                System.out.println("---------");
                
                dataProcessed.release();
                
                // Короткая пауза
                Thread.sleep(5);
            }
            
            System.out.println("Integrator завершил все " + processed + " заданий");
            
        } catch (InterruptedException e) {
            System.out.println("Integrator прерван. Обработано: " + processed);
        }
    }
}
    

