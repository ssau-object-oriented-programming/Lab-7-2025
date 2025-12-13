package threads;

import functions.Function;
import functions.Functions;

public class SimpleIntegrator implements Runnable{
    private Task task;
    
    public SimpleIntegrator(Task task) {
        this.task = task;
    }
    
    public void run() {
        int count = task.getTaskCount();
        int processed = 0;

        while (processed < count) {

           try {
                // Ждем готовых данных
                if (!task.isDataReady()) {
                    Thread.sleep(1);
                    continue;
                 }
                // Берем параметры из Task
                Function func = task.getFunction();
                double left, right, step;
                synchronized (task) {
                    func = task.getFunction();
                    left = task.getLeft();
                    right = task.getRight();
                    step = task.getStep();
                }
                
                // Вычисляем интеграл
                double result;
                try {
                    result = Functions.integral(func, left, right, step);
                } catch (Exception e) {
                    result = Double.NaN;
                }
            
            // Выводим результат
            System.out.println("Integrator [" + processed + "]: " + 
                             left + " " + right + " " + step + " " + result);
            processed++;
            task.setDataReady(false); //помечает как обработанные
            Thread.sleep(150);
            
            }catch (Exception e) {
                    System.out.println("Integrator ошибка: " + e.getMessage());
                }
    }
}
    }
