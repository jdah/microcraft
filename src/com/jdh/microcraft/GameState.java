package com.jdh.microcraft;

import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.entity.SpawnProperties;
import com.jdh.microcraft.entity.mob.EntityAirWizard;
import com.jdh.microcraft.gfx.Font;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.gui.*;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.gen.LevelGenerator;
import com.jdh.microcraft.level.tile.Tile;
import com.jdh.microcraft.sound.Sound;
import com.jdh.microcraft.util.ControlHandler;
import com.jdh.microcraft.util.Time;
import com.jdh.microcraft.util.Window;

public class GameState implements State {
    private static final int LEVEL_DEPTH_OFFSET = LevelGenerator.UNDERWORLD_LEVELS;

    // all world levels
    private final Level[] levels = new Level[LevelGenerator.OVERWORLD_LEVELS + LevelGenerator.UNDERWORLD_LEVELS];

    // current level depth, ranges from [-UNDERWORLD_LEVELS, OVERWORLD_LEVELS)
    private int levelDepth;

    // global, must be kept on game state to keep unique across levels
    private int nextEntityId, nextItemInstanceId;

    // overall game difficulty multiplier
    public double difficulty;

    public int score;
    public long playerDiedTicks;

    public HUD hud;
    public Menu menu;

    private final int playerColor;

    public GameState(double difficulty, int playerColor) {
        this.difficulty = difficulty;
        this.playerColor = playerColor;
        this.nextEntityId = 1;
        this.nextItemInstanceId = 1;
        this.score = 0;
        this.playerDiedTicks = 0;
    }

    public void init() {
        // generate levels
        for (int i = 0; i < levels.length; i++) {
            this.levels[i] = new Level(Global.ticks + Time.now() + i, i - LEVEL_DEPTH_OFFSET, 256, 256);
            LevelGenerator.getGenerator(this.levels[i]).generate();
        }

        // generate stairs
        Global.mainMenu.loadingMenu.setProgress(
                Font.Colors.YELLOW + "PLACING STAIRS...", 0.0);
        for (int i = 0; i < levels.length - 1; i++) {
            LevelGenerator.getStairsGenerator(this.levels[i], this.levels[i + 1]).generate();
            Global.mainMenu.loadingMenu.setProgress(i / (double) levels.length);
        }

        // spawn entities
        Global.mainMenu.loadingMenu.setProgress(
            Font.Colors.YELLOW + "SPAWNING...", 0.0);
        for (int i = 0; i < levels.length; i++) {
            Level level = this.levels[i];

            level.addEntitySpawns(SpawnProperties.getSpawnProperties(level));
            level.populate();

            if (level.depth == 1) {
                EntityAirWizard wizard = new EntityAirWizard(level);
                level.spawnOnTile(wizard, Tile.CLOUD.id, level.width / 2, (level.height / 2) - 6);
            }

            Global.mainMenu.loadingMenu.setProgress(i / (double) levels.length);
        }

        // spawn player in overworld
        Level level = this.getLevel(0);
        this.setLevel(level.depth);

        EntityPlayer player = new EntityPlayer(level, playerColor);
        this.hud = new HUD(player);

        player.x = Level.toPixel(level.width / 2);
        player.y = Level.toPixel((level.height / 2) + 1);
        player.updateCamera();
        level.addEntity(player);

        this.setMenu(new WelcomeMenu());
    }

    @Override
    public void tick() {
        if (!Window.hasFocus()) {
            if (!(this.menu instanceof FocusMenu)) {
                this.setMenu(new FocusMenu(this.menu));
            }

            return;
        }

        // death screen
        if (!(this.menu instanceof LoseMenu) &&
            this.playerDiedTicks != 0 &&
            (this.playerDiedTicks + 120) <= Global.ticks) {
            Sound.LOSE.play();
            this.setMenu(new LoseMenu());
        }

        this.hud.tick();

        if (this.menu != null) {
            Menu oldMenu = this.menu;
            this.menu.tick();

            // interact pressed + nothing closed = close the current menu
            if (oldMenu == this.menu &&
                ControlHandler.INTERACT.pressedTick()) {
                this.setMenu(null);
            }
        } else {
            this.getCurrentLevel().tick();

            // don't allow toggles on ticks where a new menu was opened
            if (this.menu != null) {
                return;
            }
        }

        // menu controls
        if (ControlHandler.MENU_QUIT.pressedTick()) {
            if (this.menu == null) {
                this.setMenu(new PauseMenu());
            } else {
                // quit the current menu
                Global.game.setMenu(null);
            }
        }
    }

    @Override
    public void update() {
        if (this.menu != null) {
            this.menu.update();
        } else {
            this.getCurrentLevel().update();
        }

        this.hud.update();
    }

    @Override
    public void render() {
        Level level = this.getCurrentLevel();
        level.render();
        Renderer.clearLights();

        if (level.depth < 0) {
            Renderer.addLights(this.getCurrentLevel().getLights(Renderer.getAABB()));
            Renderer.light(-111);
        }

        Renderer.pushCamera();
        Renderer.camera.tx = 0;
        Renderer.camera.ty = 0;
        this.hud.render();

        if (this.menu != null) {
            this.menu.render();
        }
        Renderer.popCamera();
    }

    public void setMenu(Menu menu) {
        if (this.menu != null) {
            this.menu.destroy();
        }

        if (menu != null) {
            menu.init();
        }

        this.menu = menu;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public Level getCurrentLevel() {
        return this.levels[this.levelDepth + LEVEL_DEPTH_OFFSET];
    }

    public Level getLevel(int depth) {
        assert (this.isDepthValid(depth));
        return this.levels[depth + LEVEL_DEPTH_OFFSET];
    }

    public void setLevel(int depth) {
        assert (this.isDepthValid(depth));
        this.levelDepth = depth;
    }

    public boolean isDepthValid(int depth) {
        return depth >= -LEVEL_DEPTH_OFFSET && depth < LevelGenerator.OVERWORLD_LEVELS;
    }

    public int getNextEntityId() {
        return this.nextEntityId++;
    }

    public int getNextItemInstanceId() {
        return this.nextItemInstanceId++;
    }
}
