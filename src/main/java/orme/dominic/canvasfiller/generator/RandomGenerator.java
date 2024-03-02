package orme.dominic.canvasfiller.generator;

import org.springframework.scheduling.annotation.Async;
import orme.dominic.canvasfiller.dto.CanvasInterface;
import orme.dominic.canvasfiller.dto.Colour;
import orme.dominic.canvasfiller.dto.Point;

import java.util.concurrent.ThreadLocalRandom;

public class RandomGenerator implements GeneratorInterface {

    private final boolean[][][] colours;

    public RandomGenerator() {
        // Create an object of all colours
        this.colours = new boolean[256][256][256];
        for (int r = 0; r < 256; r++) {
            for (int g = 0; g < 256; g++) {
                for (int b = 0; b < 256; b++) {
                    this.colours[r][g][b] = true;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "RANDOM";
    }

    @Async
    public void start(CanvasInterface canvas) throws Exception {
        for (int i = 0; i < canvas.getWidth(); i++) {
            for (int j = 0; j < canvas.getHeight(); j++) {
                Colour c = getColourForPoint();
                this.colours[c.r()][c.g()][c.b()] = false;
                canvas.getQueue().addPoint(new Point(i, j, c.r(), c.g(), c.b()));
                canvas.decrementPoint();
            }
        }
    }

    @Override
    public Point getPoint(int row, int column) {
        return new Point(
            column,
            row,
            ThreadLocalRandom.current().nextInt(0, 256),
            ThreadLocalRandom.current().nextInt(0, 256),
            ThreadLocalRandom.current().nextInt(0, 256)
        );
    }

    private Colour getColourForPoint() {
        Colour c = new Colour(
            ThreadLocalRandom.current().nextInt(0, 256),
            ThreadLocalRandom.current().nextInt(0, 256),
            ThreadLocalRandom.current().nextInt(0, 256)
        );
        while (!this.colours[c.r()][c.g()][c.b()]) {
            c = new Colour(
                ThreadLocalRandom.current().nextInt(0, 256),
                ThreadLocalRandom.current().nextInt(0, 256),
                ThreadLocalRandom.current().nextInt(0, 256)
            );
        }
        return c;
    }
}
