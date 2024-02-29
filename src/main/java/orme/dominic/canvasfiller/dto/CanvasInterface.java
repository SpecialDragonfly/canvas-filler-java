package orme.dominic.canvasfiller.dto;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface CanvasInterface {
    int getWidth();

    int getHeight();

    void decrementPoint();

    ConcurrentLinkedQueue<Point> getQueue();

    int getRemainingPointCount();

    void start();
}
