package functions;

import java.io.Externalizable;
import java.io.Serial;
import java.io.Serializable;

/**
 * Класс табулированной функции... неясно, для чего
 */
public interface TabulatedFunction extends Function, Serializable, Cloneable, Iterable<FunctionPoint> {
    /**
     * Метод получения кол-ва точек в данном классе(объекте)
     * @return кол-во точек в данном классе(объекте)
     */
    int getPointsCount();
    /**
     * Метод получения точки через индекс
     * @param index индекс получаемой точки
     * @return копия точки
     */
    FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException;
    /**
     * Метод установки точки по заданному индексу
     * @param index индекс точки
     * @param point сама точка. Будет сохранена ее копия
     */
    void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;
    /**
     * Метод получения X координаты точки на index позиции
     * @param index индекс получаемой точки
     * @return значение точки по координате X
     */
    double getPointX(int index) throws FunctionPointIndexOutOfBoundsException;

    /**
     * Метод изменения у точки X координаты
     * @param index индекс, чьей точки мы меняем X
     * @param x значение X координаты
     */
    void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;
    /**
     * Метод получения Y координаты точки на index позиции
     * @param index индекс получаемой точки
     * @return значение точки по координате Y
     */
    double getPointY(int index) throws FunctionPointIndexOutOfBoundsException;

    /**
     * Метод изменения у точки Y координаты
     * @param index индекс, чьей точки мы меняем Y
     * @param y значение Y координаты
     */
    void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException;

    /**
     * Метод удаления точки по заданному индексу
     * @param index индекс удаляемой точки
     */
    void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException;

    /**
     * Метод добавления точки в список
     * @param point сама собственно точка
     */
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;

    /**
     * Метод клонирования(копирования)
     * @return объект с ровно такими же данными, но уникальными в плане объектов
     * @throws CloneNotSupportedException я фиг знает, но это зависит от реализаций
     */
    Object clone() throws CloneNotSupportedException;
}