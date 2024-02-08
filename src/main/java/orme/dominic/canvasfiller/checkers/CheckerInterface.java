package orme.dominic.canvasfiller.checkers;

import orme.dominic.canvasfiller.dto.Colour;

public interface CheckerInterface {
    public Colour checkPositive(boolean[][][] colours, int r, int g, int b, int shell);
    public Colour checkNegative(boolean[][][] colours, int r, int g, int b, int shell);
}
