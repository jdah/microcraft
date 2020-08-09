package com.jdh.microcraft.util;

public class FMath {
    public static double norm(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

    public static int clamp(int x, int min, int max) {
        return Math.min(Math.max(x, min), max);
    }

    public static boolean sameSign(double x, double y) {
        if (x == 0 && y == 0) {
            return true;
        } else if ((x != 0 && y == 0) || (x == 0 && y != 0)) {
            return false;
        }

        return (x < 0 ? -1 : 1) == (y < 0 ? -1 : 1);
    }

    public static double safepow(double x, double e) {
        return Math.signum(x) * Math.abs(Math.pow(Math.abs(x), e));
    }

    public static int tickedDoubleToInt(long ticks, double d) {
        int i = (int) Math.floor(Math.abs(d));
        double f = Math.abs(d) - i;
        return (int) (Math.signum(d) * (i +
            ((Math.abs(f) > 0.001 && ((ticks % ((int) (1.0 / Math.abs(f)))) == 0)) ? 1 : 0)));
    }

    public static int sign(int i) {
        return Integer.compare(i, 0);
    }
}
