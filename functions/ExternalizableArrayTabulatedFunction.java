package functions;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class ExternalizableArrayTabulatedFunction implements TabulatedFunction, Externalizable {
    private static final long serialVersionUID = 2L;
    
    private FunctionPoint[] points;
    private int pointsCount;

    // Конструктор по умолчанию, необходимый для Externalizable
    public ExternalizableArrayTabulatedFunction() {
        this.points = new FunctionPoint[0];
        this.pointsCount = 0;
    }

    public ExternalizableArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница (" + leftX + 
                ") должна быть меньше правой границы (" + rightX + ")");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек (" + pointsCount + 
                ") должно быть не менее 2");
        }

        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount];

        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0.0);
        }
    }

    public ExternalizableArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница (" + leftX + 
                ") должна быть меньше правой границы (" + rightX + ")");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек (" + values.length + 
                ") должно быть не менее 2");
        }

        this.pointsCount = values.length;
        this.points = new FunctionPoint[pointsCount];

        double step = (rightX - leftX) / (pointsCount - 1);
        
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    // Реализация методов интерфейса TabulatedFunction (как в ArrayTabulatedFunction)
    public double getLeftDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return points[0].get_x();
    }

    public double getRightDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return points[pointsCount - 1].get_x();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(x - points[i].get_x()) < 1e-10) {
                return points[i].get_y();
            }
        
            if (i < pointsCount - 1) {
                double x1 = points[i].get_x();
                double x2 = points[i + 1].get_x();
                
                if (x > x1 && x < x2) {
                    double y1 = points[i].get_y();
                    double y2 = points[i + 1].get_y();
                    return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
                }
            }
        }
        return Double.NaN;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }

        if (point == null) {
            throw new IllegalArgumentException("Точка не может быть null");
        }
        
        if (index > 0) {
            double leftX = points[index - 1].get_x();
            if (point.get_x() <= leftX) {
                throw new InappropriateFunctionPointException("X должен быть больше чем у левой точки");
            }
        }
        
        if (index < pointsCount - 1) {
            double rightX = points[index + 1].get_x();
            if (point.get_x() >= rightX) {
                throw new InappropriateFunctionPointException("X должен быть меньше чем у правой точки");
            }
        }
        
        points[index] = new FunctionPoint(point);
    }

    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        return points[index].get_x();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        
        if (index > 0 && x <= points[index - 1].get_x()) {
            throw new InappropriateFunctionPointException("X должен быть больше чем у левой точки");
        }
        
        if (index < pointsCount - 1 && x >= points[index + 1].get_x()) {
            throw new InappropriateFunctionPointException("X должен быть меньше чем у правой точки");
        }
        
        points[index].set_x(x);
    }

    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        return points[index].get_y();
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        points[index].set_y(y);
    }

    public void deletePoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException();
        }

        if (pointsCount < 3) {
            throw new IllegalStateException("Невозможно удалить точку: в наборе менее 3 точек");
        }

        for (int i = index; i < pointsCount - 1; i++) {
            points[i] = points[i + 1];
        }

        pointsCount--;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (point == null) {
            throw new IllegalArgumentException("Точка не может быть null");
        }
    
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(point.get_x() - points[i].get_x()) < 1e-10) {
                throw new InappropriateFunctionPointException("Точка с таким x уже существует");
            }
        }

        int insertIndex = pointsCount;
        for (int i = 0; i < pointsCount; i++) {
            if (point.get_x() < points[i].get_x()) {
                insertIndex = i;
                break;
            }
        }

        if (pointsCount >= points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        if (insertIndex < pointsCount) {
            System.arraycopy(points, insertIndex, points, insertIndex + 1, pointsCount - insertIndex);
        }
        
        points[insertIndex] = new FunctionPoint(point);
        pointsCount++;
    }

    // Реализация методов Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        // Записываем количество точек
        out.writeInt(pointsCount);
        
        // Записываем координаты каждой точки
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(points[i].get_x());
            out.writeDouble(points[i].get_y());
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // Читаем количество точек
        pointsCount = in.readInt();
        
        // Инициализируем массив
        points = new FunctionPoint[pointsCount];
        
        // Читаем координаты каждой точки
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }

    public ExternalizableArrayTabulatedFunction(FunctionPoint[] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        for (int i = 1; i < points.length; i++) {
            if (points[i].get_x() <= points[i-1].get_x()) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по возрастанию X");
            }
        }
        
        this.pointsCount = points.length;
        this.points = new FunctionPoint[pointsCount];
        
        for (int i = 0; i < pointsCount; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    public ExternalizableArrayTabulatedFunction clone() {
        FunctionPoint[] pointsCopy = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            pointsCopy[i] = new FunctionPoint(points[i]);
        }
        return new ExternalizableArrayTabulatedFunction(pointsCopy);
    }

    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int currentIndex = 0;
            
            public boolean hasNext() {
                return currentIndex < pointsCount;
            }
            
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements in the tabulated function");                }
                    // Возвращаем копию точки для сохранения инкапсуляции
                    return new FunctionPoint(points[currentIndex++]);
                }
                
            public void remove() {
                throw new UnsupportedOperationException("Remove operation is not supported");
            }
            };
    }

    public static class ExternalizableArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {
        
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ExternalizableArrayTabulatedFunction(leftX, rightX, pointsCount);
        }
        
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ExternalizableArrayTabulatedFunction(leftX, rightX, values);
        }
        
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ExternalizableArrayTabulatedFunction(points);
        }
    }
}