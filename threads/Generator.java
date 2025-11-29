package threads;

import java.util.concurrent.Semaphore;

public class Generator extends Thread {
    private Task task;
    private Semaphore semaphore;
    private int generatedCount = 0;

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            // цикл по количеству заданий
            for (int i = 0; i < task.getCount() && !isInterrupted(); i++) {
                // захватываем семафор для доступа к заданию
                semaphore.acquire();
                try {
                    // проверяем свободно ли задание
                    if (task.getFunc() == null) {
                        // генерируем случайное основание логарифма
                        double base = 1 + Math.random() * 9;
                        // создаем логарифмическую функцию
                        functions.basic.Log logFunction = new functions.basic.Log(base);
                        // генерируем левую границу интегрирования
                        double leftBorder = Math.random() * 100;
                        // генерируем правую границу интегрирования
                        double rightBorder = 100 + Math.random() * 100;
                        // генерируем случайный шаг интегрирования
                        double step = Math.random();

                        // устанавливаем функцию в задание
                        task.setFunc(logFunction);
                        // устанавливаем левую границу
                        task.setLeftBorder(leftBorder);
                        // устанавливаем правую границу
                        task.setRightBorder(rightBorder);
                        // устанавливаем шаг интегрирования
                        task.setStep(step);

                        // выводим сгенерированные параметры
                        System.out.printf("Generator: Source %.4f %.4f %.4f%n", leftBorder, rightBorder, step);
                        // увеличиваем счетчик сгенерированных заданий
                        generatedCount++;
                    }
                } finally {
                    // освобождаем семафор после работы
                    semaphore.release();
                }

                // небольшая пауза между генерациями
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // сообщаем о прерывании сна
                    System.out.println("Generator sleep interrupted");
                    // устанавливаем флаг прерывания
                    interrupt();
                }
            }
            // выводим итоговую информацию
            System.out.println("Generator finished. Generated: " + generatedCount);
        } catch (InterruptedException e) {
            // обрабатываем прерывание при работе с семафором
            System.out.println("Generator was interrupted during semaphore operation");
        }
    }
}