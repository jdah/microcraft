package com.jdh.microcraft.gui.mainmenu;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.gfx.Font;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.gui.DialogMenu;

public class HowToPlayMenu extends DialogMenu {
    public HowToPlayMenu() {
        super(
            "HOW TO PLAY",
            new String [] {
                " ",
                Font.Colors.GREY + "USE THE " + Font.Colors.GREEN + "POW GLOVE " + Font.Colors.GREY + "TO HIT.",
                Font.Colors.GREY + "GEAR UP TO FIGHT THE " + Font.Colors.RED + "WIZARD" + Font.Colors.GREY + ".",
                "",
                Font.Colors.YELLOW + " WASD" + Font.Colors.WHITE + " TO MOVE",
                Font.Colors.YELLOW + "SPACE" + Font.Colors.WHITE + " TO HIT",
                Font.Colors.YELLOW + "    E" + Font.Colors.WHITE + " FOR INVENTORY",
                Font.Colors.YELLOW + "    E" + Font.Colors.WHITE + " TO USE",
                Font.Colors.YELLOW + "ENTER" + Font.Colors.WHITE + " TO SELECT",
                Font.Colors.YELLOW + "    C" + Font.Colors.WHITE + " TO CRAFT",
                Font.Colors.YELLOW + "    Z" + Font.Colors.WHITE + " TO EQUIP",
                Font.Colors.YELLOW + "    Q" + Font.Colors.WHITE + " TO DROP"
            },
            Renderer.WIDTH / 8, Renderer.HEIGHT / 8,
            () -> Global.mainMenu.menu = Global.mainMenu.mainMenu
        );
    }

    @Override
    protected boolean shouldAlignToMaxWidth() {
        return true;
    }
}
