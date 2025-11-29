package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable
{
    private Task task;

    public SimpleIntegrator(Task task)
    {
        this.task = task;
    }

    @Override
    public void run()
    {
        int processedTaskCounter = 0;
        System.out.println(Thread.currentThread().getName() + " запущен");

        while(processedTaskCounter < task.getTaskCount())
        {
            if(task.isReady() && task.getFunc() != null)
            {
                synchronized (task)
                {
                    try
                    {
                        System.out.printf("Result <%f> <%f> <%f> <%f>\n",
                                task.getLeftBorderX(), task.getRightBorderX(), task.getDiscretizationStep(),
                                Functions.definiteIntegralCalculation(task.getFunc(), task.getLeftBorderX(), task.getRightBorderX(), task.getDiscretizationStep()));

                        task.setReady(false);
                        processedTaskCounter++;
                    } catch (Exception e)
                    {
                        System.out.println("Произошла ошибка при интегрировании: " + e.getMessage());
                        task.setReady(false);
                    }
                }
            }

            try
            {
                Thread.sleep(1);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                break;
            }
        }

        System.out.println(Thread.currentThread().getName() + " завершен. " +
                "Выполнено задач: " + processedTaskCounter);
    }
}
