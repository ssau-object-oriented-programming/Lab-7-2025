package threads;

import functions.Function;
import functions.basic.Log;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Generator extends Thread {
    private Task task;
    private Semaphore dataReady; // Семафор для уведомления о готовности данных
    private Semaphore dataProcessed; // Семафор для уведомления об обработке данных
    private Random random = new Random();

    public Generator(Task task, Semaphore dataReady, Semaphore dataProcessed) {
        this.task = task;
        this.dataReady = dataReady;
        this.dataProcessed = dataProcessed;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                // Проверяем прерывание
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }

                // Создаем логарифмическую функцию со случайным основанием от 1 до 10
                double base = 1 + random.nextDouble() * 9; // от 1 до 10
                Function function = new Log(base);

                // Генерируем параметры
                double LeftBorder = random.nextDouble() * 100; // от 0 до 100
                double RightBorder = 100 + random.nextDouble() * 100; // от 100 до 200
                double step = random.nextDouble(); // от 0 до 1

                // Захват семафора записи
                dataProcessed.acquire();
                // Устанавливаем данные
                task.setFunction(function);
                task.setLeftBorder(LeftBorder);
                task.setRightBorder(RightBorder);
                task.setStep(step);

                // Выводим сообщение
                System.out.printf("Source %.6f %.6f %.6f (base=%.6f)%n", LeftBorder, RightBorder, step, base);
                dataReady.release();
            }
        } catch (InterruptedException e) {
            System.out.println("Generator прерван в ожидании семафора");
        }
    }
}