package com.jdh.microcraft.gui.mainmenu;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.gfx.Font;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.gui.DialogMenu;

public class AboutMenu extends DialogMenu {
    public AboutMenu() {
        super(
            "ABOUT",
            new String [] {
                "",
                Font.Colors.YELLOW + "MINICRAFT " + Font.Colors.WHITE + "MADE BY " + Font.Colors.YELLOW + "NOTCH",
                "FOR " + Font.Colors.BLUE + "LUDUM DARE 22" + Font.Colors.WHITE + " IN 2011.",
                "",
                "",
                Font.Colors.GREEN + "MICROCRAFT" + Font.Colors.WHITE + " REMAKE BY " + Font.Colors.GREEN + "JDH",
                "FOR " + Font.Colors.ORANGE + "FUN" + Font.Colors.WHITE + " IN 2020.",
                "",
                Font.Colors.GREY + "GITHUB.COM/JDAH",
                Font.Colors.GREY + "YOUTUBE.COM/C/JDHVIDEO"
            },
            Renderer.WIDTH / 8, Renderer.HEIGHT / 8,
            () -> Global.mainMenu.menu = Global.mainMenu.mainMenu
        );
    }

    @Override
    protected boolean shouldCenterText() {
        return true;
    }
}
