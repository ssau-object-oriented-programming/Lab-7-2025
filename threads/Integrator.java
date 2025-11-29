package threads;

import java.util.concurrent.Semaphore;
import functions.Functions;

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
            // цикл пока не прервали и не обработали все задания
            while (!isInterrupted() && processedCount < task.getCount()) {
                // захватываем семафор для доступа к заданию
                semaphore.acquire();
                try {
                    // проверяем есть ли функция для интегрирования
                    if (task.getFunc() != null) {
                        // получаем границы интегрирования
                        double leftBorder = task.getLeftBorder();
                        double rightBorder = task.getRightBorder();
                        // получаем шаг интегрирования
                        double step = task.getStep();
                        // вычисляем значение интеграла
                        double result = Functions.integral(task.getFunc(), leftBorder, rightBorder, step);

                        // выводим результат вычисления
                        System.out.printf("Integrator: Result %.4f %.4f %.4f %.4f%n", leftBorder, rightBorder, step, result);

                        // очищаем задание для новых данных
                        task.setFunc(null);
                        // увеличиваем счетчик обработанных заданий
                        processedCount++;
                    }
                } finally {
                    // освобождаем семафор после работы
                    semaphore.release();
                }

                // небольшая пауза между итерациями
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // сообщаем о прерывании сна
                    System.out.println("Integrator sleep interrupted");
                    // устанавливаем флаг прерывания
                    interrupt();
                }
            }
            // выводим итоговую информацию
            System.out.println("Integrator finished. Processed: " + processedCount);
        } catch (InterruptedException e) {
            // обрабатываем прерывание при работе с семафором
            System.out.println("Integrator was interrupted during semaphore operation");
        } catch (Exception e) {
            // обрабатываем другие ошибки
            System.out.println("Integrator error: " + e.getMessage());
        }
    }
}