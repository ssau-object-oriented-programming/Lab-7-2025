package threads;

import functions.Function;
import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
     private Task task;
    
    public SimpleGenerator(Task task) {
        this.task = task;
    }
    
    public void run() {
        Random rand = new Random();
        int count = task.getTaskCount();
        
        for (int i = 0; i < count; i++) {
            try {
                // Ждем, пока интегратор обработает
                while (task.isDataReady()) {
                    Thread.sleep(1);
                }
                // Генерируем случайные параметры
                double base = 1.1 + rand.nextDouble() * 8.9;     // основание
                double left = rand.nextDouble() * 100;           // левая граница
                double right = 100 + rand.nextDouble() * 100;    // правая граница
                double step = rand.nextDouble();                 // шаг
                if (step < 0.001) step = 0.001;                  // минимальный шаг
                
                // Заполняем Task
                 synchronized (task) {
                    task.setFunction(new Log(base));
                    task.setLeft(left);
                    task.setRight(right);
                    task.setStep(step);
                }
                task.setDataReady(true);

                // Выводим сообщение
                System.out.println("Generator [" + i + "]: " + 
                                left + " " + right + " " + step);

                    Thread.sleep(50);}
                    catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            
            // Помечаем завершение
            task.setCompleted(true);
        }
}
    
