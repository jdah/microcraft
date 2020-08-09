package com.jdh.microcraft.gui;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.gfx.Font;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.util.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogMenu extends Menu {
    protected final String title;
    protected final String[] lines;
    protected final int w;
    protected final int h;
    protected final Runnable onClose;
    protected final int x, y;

    public DialogMenu(String title, String[] lines, int w, int h, Runnable onClose) {
        this.title = title;
        this.lines = lines;
        this.w = w;
        this.h = h;
        this.x = (Renderer.WIDTH - (this.w * 8)) / 2;
        this.y = (Renderer.HEIGHT - (this.h * 8)) / 2;
        this.onClose = onClose;
        this.focused = true;
    }

    protected boolean shouldCenterText() {
        return false;
    }

    protected boolean showPressAnyKey() {
        return true;
    }

    protected boolean shouldAlignToMaxWidth() {
        return false;
    }

    protected String[] getLines() {
        return this.lines;
    }

    @Override
    public void render() {
        super.render();

        this.renderMenu(title, x, y, this.w, this.h);

        List<String> allLines = new ArrayList<>(Arrays.asList(this.getLines()));

        if (this.showPressAnyKey()) {
            allLines.add(" ");
            allLines.add(Font.Colors.GREY + "PRESS ANY KEY");
        }

        int maxWidth = 0;
        for (String line : this.getLines()) {
            maxWidth = Math.max(maxWidth, Font.width(line));
        }

        for (int i = 0; i < allLines.size(); i++) {
            String s = allLines.get(i);

            int tx;
            if (this.shouldCenterText() ||
                (this.shouldAlignToMaxWidth() && i == (allLines.size() - 1))) {
                tx = x + (((this.w * 8) - Font.width(s)) / 2);
            } else if (this.shouldAlignToMaxWidth()) {
                tx = x + (((this.w * 8) - maxWidth) / 2);
            } else {
                tx = x + 8;
            }

            Font.render(s, tx, y + ((i + 1) * 8), 555, -1);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.showPressAnyKey()) {
            for (Keyboard.Key k : Keyboard.keys) {
                if (k.pressedTick) {
                    if (Global.game != null) {
                        Global.game.setMenu(null);
                    }
                    this.onClose.run();
                }
            }
        }
    }
}
