package com.jdh.microcraft.gui;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.mob.EntityHumanoid;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.gfx.Font;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.sound.Sound;
import com.jdh.microcraft.util.ControlHandler;
import com.jdh.microcraft.util.FMath;

public class ColorSelectMenu extends Menu {
    private static final String[] TITLE = new String[]{
        "CHOOSE YOUR",
        Font.Colors.YELLOW + "APPEARANCE"
    };

    private static final String[] LABELS = new String[]{
        "OUTLINE/HAIR", "PANTS", "SHIRT", "SKIN"
    };

    private static final String[] COLOR_LABELS = new String[]{
        "R", "G", "B"
    };

    private static final String[] INSTRUCTIONS = new String[] {
        Font.Colors.GREY + "(" + Font.Colors.YELLOW + "X" + Font.Colors.GREY + " TO DECREASE/" +
            Font.Colors.YELLOW + "C" + Font.Colors.GREY + " TO INCREASE)",
        Font.Colors.GREY + "(" + Font.Colors.YELLOW + "R" + Font.Colors.GREY + " TO RANDOMIZE" +
            Font.Colors.GREY + ")",
        "",
        Font.Colors.GREY + "PRESS " + Font.Colors.YELLOW + "ENTER"
    };

    public int[] colors;
    private int colorIndex, rgbIndex;

    public ColorSelectMenu() {
        this.colorIndex = 0;
        this.rgbIndex = 0;
        this.randomize();
    }

    private void randomize() {
        this.colors = new int[]{
            Color.randomRGB(0, 2),
            Color.randomRGB(0, 5),
            Color.randomRGB(0, 5),
            Color.randomRGB(0, 5)
        };
    }

    // updates currently selected color component by the specified change (d)
    private void updateCurrentComponent(int d) {
        int rgb = this.colors[this.colorIndex];
        int[] components = {
            Color.getRGBComponent(rgb, 0),
            Color.getRGBComponent(rgb, 1),
            Color.getRGBComponent(rgb, 2)
        };
        components[2 - this.rgbIndex] = FMath.clamp(components[2 - this.rgbIndex] + d, 0, 5);
        this.colors[this.colorIndex] = Color.getRGB(
            components[2],
            components[1],
            components[0]
        );
    }

    @Override
    public void tick() {
        super.tick();

        if (ControlHandler.MENU_UP.pressedTick() && this.colorIndex > 0) {
            this.colorIndex--;
        }

        if (ControlHandler.MENU_DOWN.pressedTick() && this.colorIndex < (LABELS.length - 1)) {
            this.colorIndex++;
        }

        if (ControlHandler.MENU_LEFT.pressedTick() && this.rgbIndex > 0) {
            this.rgbIndex--;
        }

        if (ControlHandler.MENU_RIGHT.pressedTick() && this.rgbIndex < (COLOR_LABELS.length - 1)) {
            this.rgbIndex++;
        }

        if (ControlHandler.MENU_INCREASE.pressedTick()) {
            this.updateCurrentComponent(+1);
        }

        if (ControlHandler.MENU_DECREASE.pressedTick()) {
            this.updateCurrentComponent(-1);
        }

        if (ControlHandler.MENU_QUIT.pressedTick()) {
            Global.mainMenu.menu = Global.mainMenu.mainMenu;
        }

        if (ControlHandler.MENU_SELECT.pressedTick()) {
            Sound.CRAFT.play();
            Global.mainMenu.menu = Global.mainMenu.difficultySelectMenu;
        }

        if (ControlHandler.MENU_RANDOMIZE.pressedTick()) {
            this.randomize();
        }
    }

    @Override
    public void render() {
        super.render();

        int y = 8;
        for (String s : TITLE) {
            Font.render(s, (Renderer.WIDTH - Font.width(s)) / 2, y, 555);
            y += 8;
        }

        y += 8;

        int px = (Renderer.WIDTH - 16) / 2, py = y,
            sx = EntityHumanoid.BASE_SPRITE_X, sy = EntityHumanoid.BASE_SPRITE_Y;

        boolean flip = (Global.ticks / 15) % 2 == 0;
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                Renderer.render(
                    sx + (flip ? (1 - i) : i), sy + j,
                    px + (i * 8), py + (j * 8),
                    Color.get(colors[0], colors[1], colors[2], colors[3]),
                    flip ? Renderer.FLIP_X : Renderer.FLIP_NONE
                );
            }
        }
        y += 24;

        // labels/colors
        int maxWidth = 0;
        for (String label : LABELS) {
            int w = Font.width(label);
            if (w > maxWidth) {
                maxWidth = w;
            }
        }

        for (int i = 0; i < 4; i++) {
            int x = (Renderer.WIDTH - (maxWidth + 8 + (24 * 3) + 4 + 8)) / 2;

            if (i == this.colorIndex) {
                Font.render(">", x - 8, y, 555);
            }

            Font.render(LABELS[i], x, y, i == this.colorIndex ? 555 : 333);

            x += maxWidth + 8;
            for (int j = 0; j < 3; j++) {
                Font.render(COLOR_LABELS[j], x, y, (i == this.colorIndex && j == this.rgbIndex) ? 550 : 333);
                Renderer.fill(
                    x + 12, y, 8, 8,
                    Color.getRGBComponent(this.colors[i], 2 - j) * (int) Math.pow(10, 2 - j)
                );
                x += 24;
            }

            x += 4;
            Renderer.fill(x, y, 8, 8, this.colors[i]);
            x += 8;

            if (i == this.colorIndex) {
                Font.render("<", x, y, 555);
            }

            y += 12;
        }
        y += 8;

        for (String s : INSTRUCTIONS) {
            Font.render(s, (Renderer.WIDTH - Font.width(s)) / 2, y, 555);
            y += 8;
        }
    }
}
