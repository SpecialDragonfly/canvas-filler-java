package orme.dominic.canvasfiller.generator;

import org.springframework.scheduling.annotation.Async;
import orme.dominic.canvasfiller.dto.CanvasInterface;
import orme.dominic.canvasfiller.dto.Point;

public interface GeneratorInterface {

    @Async
    void start(CanvasInterface canvas) throws Exception;

    Point getPoint(int row, int column);

    String toString();
}
