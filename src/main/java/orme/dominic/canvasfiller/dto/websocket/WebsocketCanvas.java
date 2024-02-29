package orme.dominic.canvasfiller.dto.websocket;

import org.springframework.web.socket.WebSocketSession;
import orme.dominic.canvasfiller.dto.CanvasInterface;
import orme.dominic.canvasfiller.dto.Point;
import orme.dominic.canvasfiller.generator.GeneratorInterface;

import java.util.concurrent.ConcurrentLinkedQueue;

public class WebsocketCanvas implements CanvasInterface {
    private final int width;
    private final int height;
    private final ConcurrentLinkedQueue<Point> queue;
    private final GeneratorInterface generator;
    private int pointsRemaining;

    public WebsocketCanvas(WebSocketSession session, GeneratorInterface generator, int width, int height) {
        this.width = width;
        this.height = height;
        this.queue = new WebsocketBackedQueue(session);
        this.pointsRemaining = width * height;
        this.generator = generator;
    }

    public ConcurrentLinkedQueue<Point> getQueue() {
        return queue;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public synchronized void decrementPoint() {
        this.pointsRemaining--;
    }

    public int getRemainingPointCount() {
        return this.pointsRemaining;
    }

    public void start() {
        this.generator.start(this);
    }
}
