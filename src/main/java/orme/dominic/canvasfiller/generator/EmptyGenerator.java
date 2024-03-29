package orme.dominic.canvasfiller.generator;

import org.springframework.scheduling.annotation.Async;
import orme.dominic.canvasfiller.dto.Canvas;
import orme.dominic.canvasfiller.dto.CanvasInterface;
import orme.dominic.canvasfiller.dto.Point;

public class EmptyGenerator implements GeneratorInterface {
    @Override
    @Async
    public void start(CanvasInterface canvas) throws Exception {
        for (int i = 0; i < canvas.getWidth(); i++) {
            for (int j = 0; j < canvas.getHeight(); j++) {
                canvas.getQueue().addPoint(
                    new Point(
                        i,
                        j,
                        100, 100, 100
                    )
                );
                canvas.decrementPoint();
            }
        }
    }

    @Override
    public String toString() {
        return "EMPTY";
    }

    @Override
    public Point getPoint(int row, int column) {
        return new Point(column, row, 100, 100, 100);
    }
}
