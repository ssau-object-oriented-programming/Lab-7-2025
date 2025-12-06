package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private final Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    public void run() {
        for (int i = 0; i < task.taskCount; i++) {
            synchronized (task) {
                if (task.function == null) {
                    // Если данных нет, уменьшаем счетчик, чтобы остаться на этой же итерации и попробовать снова
                    i--; 
                    continue; 
                }
                double result = Functions.integrate(task.function, task.leftX, task.rightX, task.step);
                System.out.println("Result: " + task.leftX + " " + task.rightX + " " + task.step + " = " + result);
                
                // Зануляем функцию, чтобы не считать одно и то же дважды
                task.function = null;
            }
        }
    }
}
