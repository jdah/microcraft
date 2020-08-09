package com.jdh.microcraft.gui.mainmenu;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.gfx.Font;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.gfx.Sprite;
import com.jdh.microcraft.gui.Menu;
import com.jdh.microcraft.sound.Sound;
import com.jdh.microcraft.util.ControlHandler;

public class MainMenu extends Menu {
    private static final Sprite LOGO = new Sprite("/logo.png");
    private static final int LOGO_Y = 32;

    private static final String[] OPTIONS = {
        "START",
        "HOW TO PLAY",
        "ABOUT",
        "QUIT"
    };

    private static final Runnable[] FUNCTIONS = {
        () -> {
            Sound.CRAFT.play();
            Global.mainMenu.menu = Global.mainMenu.colorSelectMenu;
        },
        () -> {
            Sound.CRAFT.play();
            Global.mainMenu.menu = Global.mainMenu.howToPlayMenu;
        },
        () -> {
            Sound.CRAFT.play();
            Global.mainMenu.menu = Global.mainMenu.aboutMenu;
        },
        () -> System.exit(0)
    };

    private int index;

    @Override
    public void render() {
        super.render();

        Renderer.render(
            LOGO,
            (Renderer.WIDTH - LOGO.width) / 2,
            LOGO_Y,
            Color.get(112, 114, 151, 225)
        );

        int y = LOGO_Y + LOGO.height + 16;
        for (int i = 0; i < OPTIONS.length; i++) {
            String s = OPTIONS[i];
            int color = 333;

            if (i == this.index) {
                s = "> " + s + " <";
                color = 555;
            }

            Font.render(
                s,
                (Renderer.WIDTH - Font.width(s)) / 2,
                y + (i * 8),
                color
            );
        }

        Font.render(
            Font.Colors.DARK_GREY + "ORIGINALLY BY" + Font.Colors.GREY + " NOTCH",
            0, Renderer.HEIGHT - 16, 555
        );
        Font.render(
            Font.Colors.DARK_GREY + "REMAKE BY" + Font.Colors.BLUE + " JDH" +
                Font.Colors.DARK_GREY + " (github.com/jdah)",
            0, Renderer.HEIGHT - 8, 555);
    }

    @Override
    public void tick() {
        super.tick();

        if (ControlHandler.MENU_UP.pressedTick() && this.index > 0) {
            this.index--;
        }

        if (ControlHandler.MENU_DOWN.pressedTick() && this.index < (OPTIONS.length - 1)) {
            this.index++;
        }

        if (ControlHandler.MENU_SELECT.pressedTick()) {
            FUNCTIONS[this.index].run();
        }
    }
}
