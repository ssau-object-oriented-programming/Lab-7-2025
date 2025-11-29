package threads;

import functions.Functions;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Integrator extends Thread
{
    private Semaphore semaphore;
    private Task task;

    public Integrator(Task task, Semaphore semaphore)
    {
        this.semaphore = semaphore;
        this.task = task;
    }

    @Override
    public void run()
    {
        try
        {
            int processedTaskCounter = 0;
            System.out.println(Thread.currentThread().getName() + " запущен");

            while (processedTaskCounter < task.getTaskCount() && !isInterrupted())
            {
                if (semaphore.tryAcquire(100, TimeUnit.MILLISECONDS))
                {
                    if (isInterrupted())
                    {
                        break;
                    }

                    try
                    {
                        if (task.getFunc() != null)
                        {
                            try
                            {
                                System.out.printf("Result <%f> <%f> <%f> <%f>\n",
                                        task.getLeftBorderX(), task.getRightBorderX(), task.getDiscretizationStep(),
                                        Functions.definiteIntegralCalculation(task.getFunc(), task.getLeftBorderX(), task.getRightBorderX(), task.getDiscretizationStep()));

                                processedTaskCounter++;
                            }
                            catch (Exception e)
                            {
                                System.out.println("Произошла ошибка при интегрировании: " + e.getMessage());
                            }

                            task.setFunc(null);
                        }
                    }
                    finally
                    {
                        semaphore.release();
                    }
                }
                else
                {
                    if (isInterrupted())
                    {
                        break;
                    }
                }
            }

            System.out.println(Thread.currentThread().getName() + " завершен. " +
                    "Выполнено задач:" + processedTaskCounter);
        }
        catch (InterruptedException e)
        {
            System.out.println("Выполнение потока " + Thread.currentThread().getName() + " было прервано");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
