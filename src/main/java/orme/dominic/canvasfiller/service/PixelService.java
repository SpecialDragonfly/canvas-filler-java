package orme.dominic.canvasfiller.service;

import org.springframework.scheduling.annotation.Async;
import orme.dominic.canvasfiller.dto.Canvas;
import orme.dominic.canvasfiller.dto.Point;
import orme.dominic.canvasfiller.generator.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class PixelService {
    private final HashMap<String, Canvas> canvases = new HashMap<>();

    public String toString() {
        return this.canvases.keySet()
            .stream()
            .map(k -> k + ": " + this.canvases.get(k).getQueue().size())
            .collect(Collectors.joining("|"));
    }

    public void createCanvas(String canvasName, int width, int height, String type, int blur) throws Exception {
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
        this.canvases.put(canvasName, new Canvas(g, width, height));
    }

    public int numberInQueue(String queueName) {
        return this.canvases.get(queueName).getQueue().size();
    }

    public boolean isFinished(String queueName) {
        return this.canvases.get(queueName).getRemainingPointCount() == 0;
    }

    @Async("PixelServiceThreads")
    public void start(String queueName) {
        this.canvases.get(queueName).start();
    }

    public ArrayList<Point> getPoints(String queueName, String retry) {
        Canvas canvas = this.canvases.get(queueName);
        if (retry.equals("true")) {
            return canvas.getBuffer();
        }
        canvas.clearBuffer();

        return canvas.getPointsFromQueue();
    }

    public String getPoint(String queueName, int row, int column) {
        Canvas canvas = this.canvases.get(queueName);
        return canvas.getPoint(row, column).toString();
    }
}
