package com.jdh.microcraft.gui;

import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.gfx.Font;
import com.jdh.microcraft.gfx.Renderer;

public class Menu {
    public boolean focused = false;

    private static final int MENU_SPRITES_X = 11, MENU_SPRITES_Y = 11;
    public static final int MENU_BG_COLOR = 115;

    protected void renderMenu(String title, int x, int y, int wt, int ht) {
        int color = this.focused ?
            Color.get(111, 333, MENU_BG_COLOR, 444) :
            Color.get(000, 111, MENU_BG_COLOR, 222);

        // corners
        Renderer.render(MENU_SPRITES_X, MENU_SPRITES_Y,
            x, y, color, Renderer.FLIP_NONE);
        Renderer.render(MENU_SPRITES_X, MENU_SPRITES_Y,
            x + ((wt - 1) * 8), y, color, Renderer.FLIP_X);
        Renderer.render(MENU_SPRITES_X, MENU_SPRITES_Y,
            x, y + ((ht - 1) * 8), color, Renderer.FLIP_Y);
        Renderer.render(MENU_SPRITES_X, MENU_SPRITES_Y,
            x + ((wt - 1) * 8), y + ((ht - 1) * 8), color, Renderer.FLIP_XY);

        // vertical sides
        for (int i = 0; i < (ht - 2); i++) {
            Renderer.render(MENU_SPRITES_X, MENU_SPRITES_Y + 1,
                x, y + ((i + 1) * 8), color, Renderer.FLIP_NONE);
            Renderer.render(MENU_SPRITES_X, MENU_SPRITES_Y + 1,
                x + ((wt - 1) * 8), y + ((i + 1) * 8), color, Renderer.FLIP_X);
        }

        // horizontal sides
        for (int i = 0; i < (wt - 2); i++) {
            Renderer.render(MENU_SPRITES_X + 1, MENU_SPRITES_Y,
                x + ((i + 1) * 8), y, color, Renderer.FLIP_NONE);
            Renderer.render(MENU_SPRITES_X + 1, MENU_SPRITES_Y,
                x + ((i + 1) * 8), y + ((ht - 1) * 8), color, Renderer.FLIP_Y);
        }

        // center
        Renderer.fill(
            x + 8, y + 8,
            (wt - 2) * 8, (ht - 2) * 8,
            MENU_BG_COLOR
        );

        // title
        int titleX = x + (((wt * 8) / 2) - (Font.width(title) / 2));
        Font.render(title, titleX, y, this.focused ? 551 : 331, MENU_BG_COLOR);
    }

    public void init() {

    }

    public void destroy() {

    }

    public void tick() {

    }

    public void update() {

    }

    public void render() {

    }
}
