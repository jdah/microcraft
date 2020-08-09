package com.jdh.microcraft.gfx;

import java.util.Arrays;
import java.util.Optional;

public class Font {
    public enum Colors {
        RED('R', 500),
        ORANGE('O', 441),
        YELLOW('Y', 550),
        GREEN('G', 050),
        BLUE('B', 005),
        PURPLE('P', 404),
        PINK('I', 533),
        WHITE('W', 555),
        GREY('E', 333),
        BLACK('L', 000),
        DARK_GREY('D', 111);

        public final char c;
        public final int color;

        Colors(char c, int color) {
            this.c = c;
            this.color = color;
        }

        @Override
        public String toString() {
            return "$" + this.c;
        }
    }

    private static final int BASE_OFFSET_X = 0, BASE_OFFSET_Y = 13;
    private static final String[] LAYOUT = {
        "ABCDEFGHIJKLMNOP",
        "QRSTUVWXYZ!/<>:+",
        "1234567890&=().?"
    };

    public static int width(String s) {
        int w = 0;

        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '$' && i != (chars.length - 1)) {
                if (chars[i + 1] != '$') {
                    i++;
                }
                continue;
            }

            w += 8;
        }
        return w;
    }

    public static int offset(char c) {
        for (int i = 0; i < LAYOUT.length; i++) {
            int j;
            if ((j = LAYOUT[i].indexOf(c)) != -1) {
                return ((BASE_OFFSET_Y + i) * Renderer.spritesheet.sizeSprites) + (BASE_OFFSET_X + j);
            }
        }

        // character not found -> ?
        return 15 * Renderer.spritesheet.sizeSprites + 15;
    }

    public static void render(char c, int x, int y, int color) {
        Font.render(c, x, y, color, -1);
    }

    public static void render(char c, int x, int y, int color, int backgroundColor) {
        if (Character.isWhitespace(c)) {
            return;
        }

        if (backgroundColor != -1) {
            Renderer.fill(x, y, 8, 8, backgroundColor);
        }

        Renderer.render(
            Font.offset(c),
            x, y,
            Color.get(color, color, color, color),
            Renderer.FLIP_NONE
        );
    }

    public static void render(String s, int x, int y, int color) {
        Font.render(s, x, y, color, -1);
    }

    // color, backgroundColor should be single RGB digit triple
    // no background if backgroundColor == -1
    public static void render(String s, int x, int y, int color, int backgroundColor) {
        int count = 0;
        int currentColor = color;
        for (int i = 0; i < s.length(); i++) {
            char c = Character.toUpperCase(s.charAt(i));

            if (c == '$' && (i != (s.length() - 1))) {
                char n = s.charAt(i + 1);

                if (n == '$') {
                    continue;
                } else {
                    Optional<Colors> cl = Arrays.stream(Colors.values()).filter(e -> e.c == n).findFirst();

                    if (cl.isEmpty()) {
                        throw new IllegalStateException();
                    }

                    currentColor = cl.get().color;
                    i++;
                    continue;
                }
            }

            render(c, x + (count * 8), y, currentColor, backgroundColor);
            count++;
        }
    }
}
