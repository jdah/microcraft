package com.jdh.microcraft.gui;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.gfx.Font;
import com.jdh.microcraft.util.ControlHandler;

public class PauseMenu extends DialogMenu {
    private boolean onYes;

    public PauseMenu() {
        super("", new String[]{}, 20, 4,  () -> {});
        this.onYes = false;
    }

    @Override
    protected boolean showPressAnyKey() {
        return false;
    }

    @Override
    protected boolean shouldCenterText() {
        return true;
    }

    @Override
    protected String[] getLines() {
        return new String[] {
            "EXIT TO MAIN MENU?",
            (this.onYes ? Font.Colors.YELLOW : Font.Colors.GREY) + "YES" +
                (this.onYes ? Font.Colors.GREY : Font.Colors.YELLOW) + " NO"
        };
    }

    @Override
    public void tick() {
        super.tick();

        if (ControlHandler.MENU_SELECT.pressedTick()) {
            if (this.onYes) {
                Global.setState(Global.StateType.MENU);
            } else {
                Global.game.setMenu(null);
            }
        }

        if (ControlHandler.MENU_LEFT.pressedTick() && !this.onYes) {
            this.onYes = true;
        }

        if (ControlHandler.MENU_RIGHT.pressedTick() && this.onYes) {
            this.onYes = false;
        }
    }
}
