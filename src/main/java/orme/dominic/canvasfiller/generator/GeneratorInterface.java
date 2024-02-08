package orme.dominic.canvasfiller.generator;

import org.springframework.scheduling.annotation.Async;
import orme.dominic.canvasfiller.dto.Canvas;
import orme.dominic.canvasfiller.dto.Point;

public interface GeneratorInterface {

    @Async
    public void start(Canvas canvas);

    public Point getPoint(int row, int column);
}
