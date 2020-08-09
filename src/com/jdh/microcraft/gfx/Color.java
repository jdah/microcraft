package com.jdh.microcraft.gfx;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.util.FMath;

public class Color {
    // get(a, b, c, d) but with array elements
    public static int get(int[] abcd) {
        return Color.get(abcd[0], abcd[1], abcd[2], abcd[3]);
    }

    // gets color palette integer of specified 4 colors in format RGB where each component is one (base 10) digit
    public static int get(int a, int b, int c, int d) {
        return ((Color.map(d) & 0xFF) << 24) |
            ((Color.map(c) & 0xFF) << 16) |
            ((Color.map(b) & 0xFF) << 8) |
            (Color.map(a) & 0xFF);
    }

    // replaces color c component i with RGB triple x
    public static int withComponent(int c, int x, int i) {
        return (c & ~(0xFF << (i * 8))) | ((Color.map(x) & 0xFF) << (i * 8));
    }

    // gets a single component of a color as a triple
    public static int component(int c, int i) {
        return Color.iToRGB((c >> (i * 8)) & 0xFF);
    }

    // palette index to rgb triple
    public static int iToRGB(int i) {
        int r = i / 36,
            g = ((i - (r * 36)) / 6),
            b = ((i - (r * 36)) % 6);
        return (r * 100) + (g * 10) + b;
    }

    // maps RGB (where R, G, B are base 10 digits in [0..5]) to palette index
    public static int map(int d) {
        if (d < 0) {
            return 0;
        }

        return ((d / 100) % 10) * 36 + ((d / 10) % 10) * 6 + (d % 10);
    }

    // adds and clamps a single RGB triple
    public static int add(int c, int v) {
        return FMath.clamp(((c / 100) % 10) + ((v / 100) % 10), 0, 5) * 100 +
            FMath.clamp(((c / 10) % 10) + ((v / 10) % 10), 0, 5) * 10 +
            FMath.clamp((c % 10) + (v % 10), 0, 5);
    }

    // multiplies and clamps a single RGB triple
    public static int mul(int c, int v) {
        return FMath.clamp(((c / 100) % 10) * ((v / 100) % 10), 0, 5) * 100 +
            FMath.clamp(((c / 10) % 10) * ((v / 10) % 10), 0, 5) * 10 +
            FMath.clamp((c % 10) * (v % 10), 0, 5);
    }

    // adds and clamps all RGB triples in a color
    public static int addAll(int c, int v) {
        return get(
            add(component(c, 0), v),
            add(component(c, 1), v),
            add(component(c, 2), v),
            add(component(c, 3), v)
        );
    }

    public static int randomRGB(int min, int max) {
        return
            (Global.random.nextInt(max - min + 1)) * 100 +
            (Global.random.nextInt(max - min + 1)) * 10 +
            (Global.random.nextInt(max - min + 1));
    }

    // gets r, g, or b from an RGB triple
    public static int getRGBComponent(int rgb, int i) {
        return (rgb / ((int) Math.pow(10, i))) % 10;
    }

    // gets RGB triple from R, G, B components in 0..5 range
    public static int getRGB(int r, int g, int b) {
        return (r * 100) + (g * 10) + b;
    }
}
