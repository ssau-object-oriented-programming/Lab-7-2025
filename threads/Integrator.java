package threads;

import functions.Functions;
import functions.Function;

public class Integrator extends Thread {
    private Task task;
    private Semaphore semaphore;
    private int processedCount = 0;
    
    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    
    @Override
    public void run() {
        try {
            while (processedCount < task.getTasksCount()) {
                // Захватываем семафор для чтения
                semaphore.beginRead();
                
                // Проверяем, не был ли поток прерван
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                
                // Проверяем, что данные готовы
                if (!task.isDataReady()) {
                    semaphore.endRead();
                    continue;
                }
                
                // Проверяем, что функция установлена
                Function function = task.getFunction();
                if (function == null) {
                    throw new IllegalStateException("Функция не установлена");
                }
                
                // Вычисляем интеграл
                double result = Functions.integrate(
                    function, 
                    task.getLeftBorder(), 
                    task.getRightBorder(), 
                    task.getStep()
                );
                
                processedCount++;
                
                // Выводим результат
                System.out.printf("Result %.6f %.6f %.6f %.6f%n", 
                    task.getLeftBorder(), task.getRightBorder(), task.getStep(), result);
                
                // Помечаем данные как обработанные
                task.setDataReady(false);
                
                // Освобождаем семафор
                semaphore.endRead();
                
                // Небольшая пауза
                Thread.sleep(10);
            }
            
            System.out.println("Integrator: все задания обработаны");
            
        } catch (InterruptedException e) {
            System.err.println("Integrator: поток был прерван");
        } catch (Exception e) {
            System.err.println("Integrator: ошибка: " + e.getMessage());
        }
    }
}