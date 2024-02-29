package orme.dominic.canvasfiller.dto.websocket.commands;

public record StartCommand(String command, int width, int height, String type, int blur) {
}
