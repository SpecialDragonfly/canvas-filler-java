package orme.dominic.canvasfiller.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import orme.dominic.canvasfiller.dto.websocket.commands.StartCommand;
import orme.dominic.canvasfiller.service.WebsocketPixelService;

import java.util.HashMap;

public class CanvasHandler extends TextWebSocketHandler {

    HashMap<String, WebSocketSession> sessions;
    WebsocketPixelService pixelService = new WebsocketPixelService();

    public CanvasHandler() {
        sessions = new HashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.printf("Connection established on session: %s%n", session.getId());
        this.sessions.put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();

        System.out.println("Message came in with payload: " + payload);

        // payload has the format: <command>|<JSON arguments>

        String[] parts = payload.split("/\\|/", 2);
        ObjectMapper jsonParser = new ObjectMapper();
        try {
            switch (parts[0]) {
                case "start":
                    System.out.println("Command was start");
                    StartCommand result = jsonParser.readValue(parts[1], StartCommand.class);
                    this.pixelService.start(session, session.getId(), result.width(), result.height(), result.type(), result.blur());
                    break;
                case "stop":
                    System.out.println("Command was stop");
                    this.sessions.remove(session.getId());
                    break;
                default:
                    System.out.println("Unknown command " + parts[0]);
                    // error
            }
        } catch (JsonProcessingException e) {
            System.out.println("Wasn't a valid command: " + payload);
        } catch (Exception e) {
            System.out.println("Something else wasn't right: " + e.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.out.printf("Exception occurred: %s on session: %s%n", exception.getMessage(), session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        System.out.printf("Connection closed on session: %s with status: %s%n", session.getId(), closeStatus.getCode());
        this.sessions.remove(session.getId());
    }
}
