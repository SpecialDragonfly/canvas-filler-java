package orme.dominic.canvasfiller.dto;

import java.util.concurrent.ConcurrentLinkedQueue;

public class StandardQueue extends ConcurrentLinkedQueue<Point> implements CanvasQueueInterface {
    @Override
    public void addPoint(Point p) throws Exception {
        super.add(p);
    }
}
