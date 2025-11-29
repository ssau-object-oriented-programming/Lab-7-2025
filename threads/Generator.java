package threads;

import functions.basic.Log;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Generator extends Thread
{
    private Semaphore semaphore;
    private Task task;

    public Generator(Task task, Semaphore semaphore)
    {
        this.semaphore = semaphore;
        this.task = task;
    }

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
                        task.setFunc(new Log(Math.random() * 9 + 1));
                        task.setLeftBorderX(Math.random() * 100);
                        task.setRightBorderX(Math.random() * 100 + 100);
                        task.setDiscretizationStep(Math.random());

                        System.out.printf("Source <%f> <%f> <%f>\n",
                                task.getLeftBorderX(), task.getRightBorderX(), task.getDiscretizationStep());

                        processedTaskCounter++;
                    }
                    finally
                    {
                        semaphore.release();
                    }
                    try
                    {
                        Thread.sleep(1);
                    }
                    catch (InterruptedException e)
                    {
                        // сообщаем о прерывании сна
                        System.out.println("Прерывания сна " + Thread.currentThread().getName());
                        // устанавливаем флаг прерывания
                        interrupt();
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
                    "Выполнено задач: " + processedTaskCounter);
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
