package orme.dominic.canvasfiller.dto;

public interface CanvasQueueInterface {
    void addPoint(Point p) throws Exception;

    int size();

    Point remove();
}
