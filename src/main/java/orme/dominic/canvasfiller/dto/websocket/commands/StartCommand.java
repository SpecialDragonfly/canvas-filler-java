package orme.dominic.canvasfiller.dto.websocket.commands;

public record StartCommand(String queueName, int width, int height, String type, int blur) {
}
