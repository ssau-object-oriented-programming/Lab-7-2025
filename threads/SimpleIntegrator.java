package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private Task task; // ссылка на задание

    public SimpleIntegrator(Task task) { // конструктор получает задание
        this.task = task; // сохраняем задание в поле
    }

    @Override
    public void run() { // метод который выполняется в потоке
        try { // начинаем блок обработки исключений
            for (int i = 0; i < task.getCount(); i++) { // цикл по количеству заданий
                double leftBorder, rightBorder, step, result; // переменные для данных и результата

                synchronized (task) { // начинаем синхронизированный блок
                    while (task.getFunc() == null) { // ждем пока появятся данные
                        try { // пробуем ждать
                            task.wait(50); // ждем с таймаутом 50 мс
                        } catch (InterruptedException e) { // если прервали во время ожидания
                            return; // выходим из метода
                        }
                        if (task.getFunc() == null) continue; // если данных нет продолжаем ждать
                    }

                    leftBorder = task.getLeftBorder(); // читаем левую границу
                    rightBorder = task.getRightBorder(); // читаем правую границу
                    step = task.getStep(); // читаем шаг дискретизации

                    result = Functions.integral(task.getFunc(), leftBorder, rightBorder, step); // вычисляем интеграл

                    System.out.printf("Result %.4f %.4f %.4f %.4f%n",
                            leftBorder, rightBorder, step, result);

                    task.setFunc(null); // очищаем задание

                    task.notifyAll(); // уведомляем ждущие потоки
                } // конец синхронизированного блока

                Thread.sleep(10); // небольшая пауза
            }
        } catch (InterruptedException e) { // если поток прервали
            System.out.println("Integrator was interrupted"); // сообщаем о прерывании
        } catch (Exception e) { // если другая ошибка
            System.out.println("Integrator error: " + e.getMessage()); // выводим сообщение об ошибке
        }
    }
}