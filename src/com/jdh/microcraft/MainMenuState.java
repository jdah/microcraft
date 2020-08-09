package com.jdh.microcraft;

import com.jdh.microcraft.gui.ColorSelectMenu;
import com.jdh.microcraft.gui.DifficultySelectMenu;
import com.jdh.microcraft.gui.Menu;
import com.jdh.microcraft.gui.mainmenu.AboutMenu;
import com.jdh.microcraft.gui.mainmenu.HowToPlayMenu;
import com.jdh.microcraft.gui.mainmenu.LoadingMenu;
import com.jdh.microcraft.gui.mainmenu.MainMenu;

public class MainMenuState implements State {
    public Menu menu;

    public MainMenu mainMenu;
    public LoadingMenu loadingMenu;
    public HowToPlayMenu howToPlayMenu;
    public AboutMenu aboutMenu;
    public ColorSelectMenu colorSelectMenu;
    public DifficultySelectMenu difficultySelectMenu;

    public MainMenuState() {
        this.mainMenu = new MainMenu();
        this.loadingMenu = new LoadingMenu();
        this.howToPlayMenu = new HowToPlayMenu();
        this.aboutMenu = new AboutMenu();
        this.colorSelectMenu = new ColorSelectMenu();
        this.difficultySelectMenu = new DifficultySelectMenu();
        this.menu = this.mainMenu;
    }

    @Override
    public void tick() {
        this.menu.tick();
    }

    @Override
    public void update() {
        this.menu.update();
    }

    @Override
    public void render() {
        this.menu.render();
    }
}
