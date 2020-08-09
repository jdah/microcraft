package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.mob.EntityAirWizard;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.level.Level;

public class TileStair extends Tile {
    private static final int SPRITE_X = 14, SPRITE_Y_UP = 4, SPRITE_Y_DOWN = 6;
    private static final int COLOR_UP = Color.get(000, 111, 222, 333);
    private static final int COLOR_DOWN = Color.get(000, 000, 111, 222);

    private final boolean up;

    public TileStair(int id, boolean up) {
        super(id);
        this.up = up;
    }

    @Override
    public void step(Level level, int x, int y, Entity e) {
        if (e instanceof EntityAirWizard) {
            return;
        }

        // only allow level transitions when an entity has JUST entered a stair tile
        if (e.onStairs) {
            return;
        }

        e.onStairs = true;

        int newDepth = level.depth + (this.isUp() ? 1 : -1);
        assert(Global.game.isDepthValid(newDepth));
        e.moveLevel(newDepth, Level.toPixel(x), Level.toPixel(y));
    }

    @Override
    public void render(Level level, int x, int y) {
        Tile.STONE.render(level, x, y);
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                Renderer.render(
                    SPRITE_X + i, (this.isUp() ? SPRITE_Y_UP : SPRITE_Y_DOWN) + j,
                    Level.toPixel(x) + (i * 8), Level.toPixel(y) + (j * 8),
                    this.isUp() ? COLOR_UP : COLOR_DOWN, Renderer.FLIP_NONE
                );
            }
        }
    }

    public boolean isUp() {
        return up;
    }
}
