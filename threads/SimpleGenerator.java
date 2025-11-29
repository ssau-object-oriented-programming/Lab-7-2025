package threads;

import functions.basic.Log;

public class SimpleGenerator implements Runnable {
    private Task task; // ссылка на задание
    private int generatedCount = 0; // счетчик созданных заданий

    public SimpleGenerator(Task task) { // конструктор получает задание
        this.task = task; // сохраняем задание в поле
    }

    @Override
    public void run() { // метод который выполняется в потоке
        try { // начинаем блок обработки исключений
            for (int i = 0; i < task.getCount(); i++) { // цикл по количеству заданий
                double base = 1 + Math.random() * 9; // генерируем основание логарифма
                Log logFunction = new Log(base); // создаем логарифмическую функцию
                double leftBorder = Math.random() * 100; // генерируем левую границу
                double rightBorder = 100 + Math.random() * 100; // генерируем правую границу
                double step = Math.random(); // генерируем шаг дискретизации

                synchronized (task) { // начинаем синхронизированный блок
                    while (task.getFunc() != null) { // ждем пока задание не обработано
                        task.wait(); // ожидаем уведомления
                    }

                    task.setFunc(logFunction); // устанавливаем функцию в задание
                    task.setLeftBorder(leftBorder); // устанавливаем левую границу
                    task.setRightBorder(rightBorder); // устанавливаем правую границу
                    task.setStep(step); // устанавливаем шаг

                    System.out.printf("Source %.4f %.4f %.4f%n", leftBorder, rightBorder, step);
                    generatedCount++; // увеличиваем счетчик созданных заданий

                    task.notifyAll(); // уведомляем ждущие потоки
                } // конец синхронизированного блока

                Thread.sleep(10); // небольшая пауза
            }

            System.out.println("Generator finished. Generated: " + generatedCount);
        } catch (InterruptedException e) { // если поток прервали
            System.out.println("Generator was interrupted"); // сообщаем о прерывании
        }
    }
}