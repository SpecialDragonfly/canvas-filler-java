package orme.dominic.canvasfiller.checkers;

import orme.dominic.canvasfiller.dto.Colour;

public class RedChecker implements CheckerInterface {
    @Override
    public Colour checkPositive(boolean[][][] colours, int r, int g, int b, int shell) {
        if (r + shell > 255) {
            return null;
        }

        int checkR = r + shell;
        for (int checkG = g - shell; checkG <= g + shell; checkG++) {
            if (checkG < 0 || checkG > 255) {
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
        if (r - shell < 0) {
            return null;
        }

        int checkR = r - shell;
        for (int checkG = g - shell; checkG <= g + shell; checkG++) {
            if (checkG < 0 || checkG > 255) {
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
