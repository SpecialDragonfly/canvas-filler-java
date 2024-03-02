package orme.dominic.canvasfiller.dto;

public interface CanvasInterface {
    int getWidth();

    int getHeight();

    void decrementPoint();

    CanvasQueueInterface getQueue();

    int getRemainingPointCount();

    void start();
}
