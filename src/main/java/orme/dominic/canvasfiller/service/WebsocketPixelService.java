package orme.dominic.canvasfiller.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.socket.WebSocketSession;
import orme.dominic.canvasfiller.dto.CanvasInterface;
import orme.dominic.canvasfiller.dto.websocket.WebsocketCanvas;
import orme.dominic.canvasfiller.generator.*;

import java.util.HashMap;
import java.util.stream.Collectors;

public class WebsocketPixelService {
    private final HashMap<String, CanvasInterface> canvases = new HashMap<>();

    // Needs to return all canvases and some level of info.
    public String toString() {
        return this.canvases.keySet()
            .stream()
            .map(k -> "{\"id\":\"" + k + "\", \"remaining-points:\":" + this.canvases.get(k).getRemainingPointCount() + ",\"generator\":\"" + this.canvases.get(k).getGeneratorName() + "\"")
            .collect(Collectors.joining("|"));
    }

    @Async("PixelServiceThreads")
    public void start(WebSocketSession session, String queueName, int width, int height, String type, int blur) throws Exception {

        GeneratorInterface g = new EmptyGenerator();;

        if (GeneratorEnum.RANDOM.name().equals(type)) {
            g = new RandomGenerator();
            System.out.println("Random generator chosen");
        } else if (GeneratorEnum.SEQUENTIAL.name().equals(type)) {
            if (width * height > (4096*4096)) {
                throw new Exception("Canvas too large");
            }
            g = new SequentialGenerator();
            System.out.println("Sequential generator chosen");
        } else if (GeneratorEnum.ALLCOLOURS.name().equals(type)) {
            if (width * height > (4096*4096)) {
                throw new Exception("Canvas too large");
            }
            g = new AllColoursGenerator(blur);
            System.out.println("All Colours generator chosen");
        } else if (GeneratorEnum.SPIRALIN.name().equals(type)) {
            if (width * height > (4096*4096)) {
                throw new Exception("Canvas too large");
            }
            System.out.println("Spiral In generator chosen");
            g = new SpiralInGenerator(blur);
        } else {
            System.out.println("Using Empty Generator");
        }

        System.out.println("Started WebSocket Canvas with " + g.getClass() + " w: " + width + " h: " + height);
        this.canvases.put(queueName, new WebsocketCanvas(session, g, width, height));
        this.canvases.get(queueName).start();
    }
}
