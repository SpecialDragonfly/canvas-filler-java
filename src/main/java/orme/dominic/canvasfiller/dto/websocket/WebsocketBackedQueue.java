package orme.dominic.canvasfiller.dto.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import orme.dominic.canvasfiller.dto.Point;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WebsocketBackedQueue extends ConcurrentLinkedQueue<Point> {

    private final WebSocketSession session;

    public WebsocketBackedQueue(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public boolean add(Point o) {
        try {
            session.sendMessage(new TextMessage(o.toString()));
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
