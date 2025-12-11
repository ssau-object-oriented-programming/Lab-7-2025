package threads;

import functions.Function;

public class Task {
    public Function function; // функция
    public double left;       // Левая граница
    public double right;      // Правая граница
    public double step;       // Шаг дискретизации
    public int taskCount;     // Количество заданий, которые нужно выполнить

    public Task(int taskCount) {
        this.taskCount = taskCount;
    }
}