package orme.dominic.canvasfiller.dto;

import orme.dominic.canvasfiller.annotations.GreaterThanZero;

public record Point(@GreaterThanZero int x, @GreaterThanZero int y, int r, int g, int b) {
    public String toString() {
        return String.format("%d|%d|%02x%02x%02x", x, y, r, g, b);
    }
}
