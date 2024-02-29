package orme.dominic.canvasfiller.generator.checkers;

import orme.dominic.canvasfiller.dto.Colour;

public class GreenChecker implements CheckerInterface {
    @Override
    public Colour checkPositive(boolean[][][] colours, int r, int g, int b, int shell) {
        if (g + shell > 255) {
            return null;
        }

        int checkG = g + shell;
        for (int checkR = r - shell; checkR <= r + shell; checkR++) {
            if (checkR < 0 || checkR > 255) {
                continue;
            }
            for (int checkB = b - shell; checkB <= b + shell; checkB++) {
                if (checkB < 0 || checkB > 255) {
                    continue;
                }
                if (colours[checkR][checkG][checkB]) {
                    return new Colour(checkR, checkG, checkB);
                }
            }
        }

        return null;
    }

    @Override
    public Colour checkNegative(boolean[][][] colours, int r, int g, int b, int shell) {
        if (g - shell < 0) {
            return null;
        }

        int checkG = g - shell;
        for (int checkR = r - shell; checkR <= r + shell; checkR++) {
            if (checkR < 0 || checkR > 255) {
                continue;
            }
            for (int checkB = b - shell; checkB <= b + shell; checkB++) {
                if (checkB < 0 || checkB > 255) {
                    continue;
                }

                if (colours[checkR][checkG][checkB]) {
                    return new Colour(checkR, checkG, checkB);
                }
            }
        }

        return null;
    }
}
