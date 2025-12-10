package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private final Task task;
    
    public SimpleIntegrator(Task task){
        this.task = task;
    }

    @Override
    public void run(){
        for (int i = 0; i < task.getTaskCount(); ++i){
            try {
                double left; 
                double right; 
                double step; 
                double result;

                synchronized (task) {
                    while (!task.isTaskReadyForIntegration()) {
                        task.wait();
                    }    
                    left = task.getLeftBorder();
                    right = task.getRightBorder();
                    step = task.getStep();
                    result = Functions.integrate(task.getFunction(), left, right, step);
                    System.out.printf("(SimpleIntegrator %d) Результат: левая граница = %.6f, правая граница = %.6f, шаг = %.6f, значение интеграла =  %.6f", i, left, right, step, result);
                    System.out.printf("\n---------\n");
                    task.resetTaskReadiness();
                }

            } catch(InterruptedException e){
                System.out.println("При интегрировании произошла ошибка" + e.getMessage());
            } catch (Exception e) {
                System.out.println("В SimpleIntegrator ошибка: " + e.getMessage());
            }
        }
    }
}
