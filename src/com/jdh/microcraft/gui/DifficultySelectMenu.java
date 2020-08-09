package com.jdh.microcraft.gui;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.gfx.Font;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.util.ControlHandler;

public class DifficultySelectMenu extends Menu {
    private static final String[] TITLE = {
        Font.Colors.WHITE + "CHOOSE YOUR",
        Font.Colors.RED + "DIFFICULTY"
    };

    private static final String[] INSTRUCTIONS = new String[] {
        Font.Colors.GREY + "PRESS " + Font.Colors.YELLOW + "ENTER" + Font.Colors.GREY + " TO START!"
    };

    private static final int COUNT = 5;

    private static final String[] DIFFICULTY_NAMES = {
        "TOO EASY",
        "EASY",
        "NORMAL",
        "KINDA HARD",
        "HARD"
    };

    private static final double[] DIFFICULTY_VALUES = {
        0.70,
        0.85,
        1.0,
        1.15,
        1.30
    };

    private int index;

    public DifficultySelectMenu() {
        this.index = 2;
    }

    public double getSelectedDifficulty() {
        return DIFFICULTY_VALUES[this.index];
    }

    @Override
    public void tick() {
        super.tick();

        if (ControlHandler.MENU_UP.pressedTick() && this.index > 0) {
            this.index--;
        }

        if (ControlHandler.MENU_DOWN.pressedTick() && this.index < (COUNT - 1)) {
            this.index++;
        }

        if (ControlHandler.MENU_QUIT.pressedTick()) {
            Global.mainMenu.menu = Global.mainMenu.colorSelectMenu;
        }

        if (ControlHandler.MENU_SELECT.pressedTick()) {
            Global.setState(Global.StateType.GAME);
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

        y += 32;

        int maxWidth = 0;
        for (String s : DIFFICULTY_NAMES) {
            maxWidth = Math.max(maxWidth, Font.width(s));
        }

        for (int i = 0; i < COUNT; i++) {
            String s = DIFFICULTY_NAMES[i];
            Font.render(
                s,
                (Renderer.WIDTH - Font.width(s)) / 2, y + (i * 8),
                i == this.index ? 555 : 333);
        }

        Font.render(">", ((Renderer.WIDTH - maxWidth) / 2) - 8, y + (this.index * 8), 555);
        Font.render("<", (Renderer.WIDTH + maxWidth) / 2, y + (this.index * 8), 555);

        for (String s : INSTRUCTIONS) {
            Font.render(s, (Renderer.WIDTH - Font.width(s)) / 2, Renderer.HEIGHT - 8, 555);
        }
    }
}
