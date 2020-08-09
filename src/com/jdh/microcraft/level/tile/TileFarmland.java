package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.item.tool.ItemTool;
import com.jdh.microcraft.level.Level;

public class TileFarmland extends Tile {
    public static final int COLOR = Color.get(322, 221, 110, 211);

    private static final int BASE_SPRITE_X = 6, BASE_SPRITE_Y = 4;

    public TileFarmland(int id) {
        super(id);
    }

    @Override
    public int getIdealTools() {
        return ItemTool.TYPE_PICKAXE | ItemTool.TYPE_SHOVEL;
    }

    @Override
    public boolean hit(Level level, int x, int y, Entity e) {
        level.setTile(x, y, Tile.DIRT.id);
        return true;
    }

    @Override
    public void randomTick(Level level, int x, int y) {
        super.randomTick(level, x, y);

        if (Global.random.nextInt(8) != 0) {
            return;
        }

        int damage = level.getData(x, y);
        if (damage == 3) {
            level.setTile(x, y, Tile.DIRT.id);
        } else {
            level.setData(x, y, damage + 1);
        }
    }

    @Override
    public void render(Level level, int x, int y) {
        Global.random.setSeed((x * 19) ^ (y * 117));
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                Renderer.render(
                    BASE_SPRITE_X, BASE_SPRITE_Y,
                    Level.toPixel(x) + (i * 8), Level.toPixel(y) + (j * 8),
                    TileFarmland.COLOR,
                    Global.random.nextBoolean() ? Renderer.FLIP_X : Renderer.FLIP_NONE
                );
            }
        }
    }
}
