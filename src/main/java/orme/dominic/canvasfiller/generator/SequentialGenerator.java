package orme.dominic.canvasfiller.generator;

import org.springframework.scheduling.annotation.Async;
import orme.dominic.canvasfiller.dto.*;
import orme.dominic.canvasfiller.generator.dto.BlankCanvas;

public class SequentialGenerator implements GeneratorInterface {
    BlankCanvas<Colour> blankCanvas;
    @Override
    @Async
    public void start(CanvasInterface canvas) throws Exception {
        this.blankCanvas = new BlankCanvas<>(canvas.getWidth(), canvas.getHeight());
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i = 0; i < canvas.getWidth(); i++) {
            for (int j = 0; j < canvas.getHeight(); j++) {
                this.blankCanvas.setValue(j, i, new Colour(r, g, b));
                canvas.getQueue().addPoint(
                    new Point(
                        i,
                        j,
                        r, g, b
                    )
                );
                canvas.decrementPoint();
                r++;
                if (r == 256) {
                    r = 0;
                    g++;
                }
                if (g == 256) {
                    g = 0;
                    b++;
                }
            }
        }
    }

    @Override
    public Point getPoint(int row, int column) {
        Colour c = this.blankCanvas.getValue(row, column);
        return new Point(column, row, c.r(), c.g(), c.b());
    }
}
