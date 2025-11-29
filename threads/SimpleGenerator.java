package threads;

import functions.basic.Log;

public class SimpleGenerator implements Runnable
{
    private Task task;

    public SimpleGenerator(Task task)
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
            synchronized (task)
            {
                task.setFunc(new Log(Math.random() * 9 + 1));
                task.setLeftBorderX(Math.random() * 100);
                task.setRightBorderX(Math.random() * 100 + 100);
                task.setDiscretizationStep(Math.random());
                task.setReady(true);

                System.out.printf("Source <%f> <%f> <%f>\n",
                        task.getLeftBorderX(), task.getRightBorderX(), task.getDiscretizationStep());

                processedTaskCounter++;
            }
            try
            {
                Thread.sleep(10);
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
