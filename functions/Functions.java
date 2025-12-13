package functions;

import functions.meta.*;

public class Functions {
    
    // Приватный конструктор, чтобы нельзя было создать объект класса
    private Functions() {
    }
    
    //Сдвиг вдоль осей
    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }
    
    //Масштабирование
    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }
    
    //Степень функции
    public static Function power(Function f, double power) {
        return new Power(f, power);
    }
    
    //Сумма функций
    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }
    
    //Произведение функции
    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }
    
    
    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }
	
	
	public static double integrate(Function f, double left, double right, double step) {
        // Проверка границ интегрирования
		if (left < f.getLeftDomainBorder() || right > f.getRightDomainBorder()) {
			throw new IllegalArgumentException("Интервал интегрирования выходит за границы области определения функции");
		}
        
		if (left >= right) {
			throw new IllegalArgumentException("Левая граница должна быть меньше правой");
		}
        
		if (step <= 0) {
			throw new IllegalArgumentException("Шаг дискретизации должен быть положительным");
		}
        
		double integral = 0.0;
		double current = left;
        
        // Основной цикл по полным шагам
		while (current + step <= right) {
			double x1 = current;
			double x2 = current + step;
			double y1 = f.getFunctionValue(x1);
			double y2 = f.getFunctionValue(x2);
            
            // Площадь трапеции: (основание1 + основание2) * высота / 2
			integral += (y1 + y2) * step / 2.0;
			current += step;
		}
        
        // Обработка последнего неполного шага (если есть)
		if (current < right) {
			double lastStep = right - current;
			double x1 = current;
			double x2 = right;
			double y1 = f.getFunctionValue(x1);
			double y2 = f.getFunctionValue(x2);
            
			integral += (y1 + y2) * lastStep / 2.0;
		}
        
		return integral;
	}
}