package orme.dominic.canvasfiller.generator;

import org.springframework.scheduling.annotation.Async;
import orme.dominic.canvasfiller.generator.checkers.BlueChecker;
import orme.dominic.canvasfiller.generator.checkers.CheckerInterface;
import orme.dominic.canvasfiller.generator.checkers.GreenChecker;
import orme.dominic.canvasfiller.generator.checkers.RedChecker;
import orme.dominic.canvasfiller.dto.*;
import orme.dominic.canvasfiller.generator.dto.BlankCanvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class AllColoursGenerator implements GeneratorInterface {

    private final boolean[][][] colours;
    private final ArrayList<CheckerInterface> picker = new ArrayList<>();
    private final int blur;
    BlankCanvas<Colour> blankCanvas;

    public AllColoursGenerator(int blur) {
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
    public String toString() {
        return "ALLCOLOURS";
    }

    @Override
    @Async("GeneratorThreads")
    public void start(CanvasInterface canvas) throws Exception {
        this.blankCanvas = new BlankCanvas<>(canvas.getWidth(), canvas.getHeight());

        int height = canvas.getHeight();
        int width = canvas.getWidth();
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {
                Colour c = this.getColourForPoint(row, column);
                this.colours[c.r()][c.g()][c.b()] = false;
                this.blankCanvas.setValue(row, column, c);
                canvas.getQueue().addPoint(new Point(column, row, c.r(), c.g(), c.b()));

                canvas.decrementPoint();
                if (canvas.getQueue().size() > 50000) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
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
