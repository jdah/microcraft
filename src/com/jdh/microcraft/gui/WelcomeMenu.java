package com.jdh.microcraft.gui;

import com.jdh.microcraft.gfx.Font;

public class WelcomeMenu extends DialogMenu {
    public WelcomeMenu() {
        super("WELCOME", new String[]{
            "",
            "YOU MUST DEFEAT ",
            "THE EVIL " + Font.Colors.RED + "AIR WIZARD!",
            "",
            "ONE SHOULD GO " + Font.Colors.GREEN + "DOWN",
            "BEFORE MAKING A",
            "WAY" + Font.Colors.YELLOW + " UP",
        }, 22, 11, () -> {});
    }
}
