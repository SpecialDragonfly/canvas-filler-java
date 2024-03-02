package orme.dominic.canvasfiller.dto.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import orme.dominic.canvasfiller.dto.CanvasQueueInterface;
import orme.dominic.canvasfiller.dto.Point;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WebsocketBackedQueue extends ConcurrentLinkedQueue<Point> implements CanvasQueueInterface {

    private final WebSocketSession session;

    public WebsocketBackedQueue(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public void addPoint(Point p) throws Exception {
        if (!session.isOpen()) {
            throw new Exception("Socket closed");
        }
        try {
            session.sendMessage(new TextMessage(p.toString()));
        } catch (IOException e) {
            throw new Exception(e);
        }
    }
}
