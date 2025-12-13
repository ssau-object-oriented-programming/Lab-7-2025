package threads;

import functions.Functions;
import functions.Function;

public class SimpleIntegrator implements Runnable {
    private Task task;
    private int processedCount = 0;
    
    public SimpleIntegrator(Task task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        try {
            while (processedCount < task.getTasksCount()) {
                synchronized (task) {
                    // Ждем, пока данные будут готовы
                    while (!task.isDataReady()) {
                        task.wait();
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
                    
                    task.notifyAll();
                    
                }
                
             
                Thread.sleep(10);
            }
            
            System.out.println("Integrator: все задания обработаны");
            
        } catch (InterruptedException e) {
            System.err.println("Integrator: поток был прерван");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Integrator: ошибка: " + e.getMessage());
        }
    }
}