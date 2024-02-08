package orme.dominic.canvasfiller.dto;

import java.util.Vector;

public class BlankCanvas<T> {

    private final int height;
    private final int width;
    Vector<Vector<T>> area = new Vector<>();

    public BlankCanvas(int width, int height) {
        this.height = height;
        this.width = width;
        for (int row = 0; row < height; row++) {
            this.area.add(new Vector<>());
            for (int column = 0; column < width; column++) {
                this.area.get(row).add(null);
            }
        }
    }

    public T getValue(int row, int column) {
        return this.area.get(row).get(column);
    }

    public void setValue(int row, int column, T value) {
        this.area.get(row).set(column, value);
    }

    public boolean isNull(int row, int column) {
        return this.area.get(row).get(column) == null;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }
}
