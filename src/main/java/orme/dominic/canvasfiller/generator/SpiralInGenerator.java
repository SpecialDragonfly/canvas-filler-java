package orme.dominic.canvasfiller.generator;

import org.springframework.scheduling.annotation.Async;
import orme.dominic.canvasfiller.checkers.BlueChecker;
import orme.dominic.canvasfiller.checkers.CheckerInterface;
import orme.dominic.canvasfiller.checkers.GreenChecker;
import orme.dominic.canvasfiller.checkers.RedChecker;
import orme.dominic.canvasfiller.dto.BlankCanvas;
import orme.dominic.canvasfiller.dto.Canvas;
import orme.dominic.canvasfiller.dto.Colour;
import orme.dominic.canvasfiller.dto.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class SpiralInGenerator implements GeneratorInterface {

    private final boolean[][][] colours;
    private final int blur;
    BlankCanvas<Colour> blankCanvas;
    private final ArrayList<CheckerInterface> picker = new ArrayList<>();

    public SpiralInGenerator(int blur) {
        this.blur = blur;
        // Create an object of all colours
        this.colours = new boolean[256][256][256];
        for (int r = 0; r < 256; r++) {
            for (int g = 0; g < 256; g++) {
                for (int b = 0; b < 256; b++) {
                    this.colours[r][g][b] = true;
                }
            }
        }

        this.picker.add(new RedChecker());
        this.picker.add(new GreenChecker());
        this.picker.add(new BlueChecker());
    }

    @Override
    @Async("GeneratorThreads")
    public void start(Canvas canvas) {
        this.blankCanvas = new BlankCanvas<>(canvas.getWidth(), canvas.getHeight());

        int minHeight = 0;
        int minWidth = 0;
        int maxHeight = canvas.getHeight();
        int maxWidth = canvas.getWidth();
        int row = 0;
        int column = 0;
        int direction = 0; // 0 down, 1 left, 2 up, 3 right
        while (canvas.getRemainingPointCount() > 0) {
            Colour c = this.getColourForPoint(row, column);
            this.colours[c.r()][c.g()][c.b()] = false;
            this.blankCanvas.setValue(row, column, c);
            canvas.getQueue().add(new Point(column, row, c.r(), c.g(), c.b()));

            canvas.decrementPoint();

            if (direction == 0) {
                row++;
            }
            if (direction == 1) {
                column++;
            }
            if (direction == 2) {
                row--;
            }
            if (direction == 3) {
                column--;
            }

            if (row == maxHeight) {
                row--;
                column++;
                direction = 1;
                minWidth++;
            } else if (column == maxWidth) {
                column--;
                row--;
                direction = 2;
                maxHeight--;
            } else if (row < minHeight) {
                row = minHeight;
                column--;
                direction = 3;
                maxWidth--;
            } else if (column < minWidth) {
                column = minWidth;
                row++;
                direction = 0;
                minHeight++;
            }
        }
    }

    @Override
    public Point getPoint(int row, int column) {
        Colour c = this.blankCanvas.getValue(row, column);
        return new Point(column, row, c.r(), c.g(), c.b());
    }

    private Colour getColourForPoint(int row, int column) {
        if (row == 0 && column == 0) {
            // this is the first pixel!
            return new Colour(
                ThreadLocalRandom.current().nextInt(0, 256),
                ThreadLocalRandom.current().nextInt(0, 256),
                ThreadLocalRandom.current().nextInt(0, 256)
            );
        }

        ArrayList<Colour> usedColours = this.getUsedColours(row, column);
        // get the average colour of those around it.
        int avgRed = usedColours.stream().map(Colour::r).reduce(0, Integer::sum) / usedColours.size();
        int avgGreen = usedColours.stream().map(Colour::g).reduce(0, Integer::sum) / usedColours.size();
        int avgBlue = usedColours.stream().map(Colour::b).reduce(0, Integer::sum) / usedColours.size();

        // is that colour available? If false, it's not available.
        if (!this.colours[avgRed][avgGreen][avgBlue]) {
            Colour c = new Colour(avgRed, avgGreen, avgBlue);
            int shell = 1;
            boolean found = false;
            while (true) {
                // Shuffle the various pickers so that we don't bias ourselves always towards one colour.
                Collections.shuffle(this.picker);

                // First check positive values
                for (CheckerInterface check : this.picker) {
                    c = check.checkPositive(this.colours, avgRed, avgGreen, avgBlue, shell);
                    if (c != null) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }

                // Shuffle again on this side too.
                Collections.shuffle(this.picker);

                // Then check negative values
                for (CheckerInterface check : this.picker) {
                    c = check.checkNegative(this.colours, avgRed, avgGreen, avgBlue, shell);
                    if (c != null) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }

                shell++;
            }
            return c;
        }

        return new Colour(avgRed, avgGreen, avgBlue);
    }

    private ArrayList<Colour> getUsedColours(int row, int column) {
        ArrayList<Colour> usedColours = new ArrayList<>();
        // get all the values around the point row/column
        for (int rowIndex = row - this.blur; rowIndex <= row + this.blur; rowIndex++) {
            for (int columnIndex = column - this.blur; columnIndex <= column + this.blur; columnIndex++) {
                if (rowIndex < 0 || columnIndex < 0) {
                    continue;
                }
                if (rowIndex == row && columnIndex == column) {
                    continue;
                }
                if (rowIndex >= this.blankCanvas.getHeight() || columnIndex >= this.blankCanvas.getWidth()) {
                    continue;
                }
                if (!this.blankCanvas.isNull(rowIndex, columnIndex)) {
                    usedColours.add(this.blankCanvas.getValue(rowIndex, columnIndex));
                }
            }
        }
        return usedColours;
    }
}
