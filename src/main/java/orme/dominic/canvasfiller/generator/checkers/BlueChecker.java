package orme.dominic.canvasfiller.generator.checkers;

import orme.dominic.canvasfiller.dto.Colour;

public class BlueChecker implements CheckerInterface {
    @Override
    public Colour checkPositive(boolean[][][] colours, int r, int g, int b, int shell) {
        if (b + shell > 255) {
            return null;
        }

        int checkB = b + shell;
        for (int checkR = r - shell; checkR <= r + shell; checkR++) {
            if (checkR < 0 || checkR > 255) {
                continue;
            }
            for (int checkG = g - shell; checkG <= g + shell; checkG++) {
                if (checkG < 0 || checkG > 255) {
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
        if (b - shell < 0) {
            return null;
        }

        int checkB = b - shell;
        for (int checkR = r - shell; checkR <= r + shell; checkR++) {
            if (checkR < 0 || checkR > 255) {
                continue;
            }
            for (int checkG = g - shell; checkG <= g + shell; checkG++) {
                if (checkG < 0 || checkG > 255) {
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
