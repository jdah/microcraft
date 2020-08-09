package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityItem;
import com.jdh.microcraft.entity.mob.EntityMob;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.tool.ItemTool;
import com.jdh.microcraft.level.Level;

public class TileCactus extends TileSand {
    public static int COLOR = Color.get(121, 141, 252, 252);

    public TileCactus(int id) {
        super(id);
    }

    @Override
    public int getUsableTools() {
        return ItemTool.TYPE_NONE;
    }

    @Override
    public int getIdealTools() {
        return ItemTool.TYPE_AXE | ItemTool.TYPE_SWORD;
    }

    @Override
    public boolean hit(Level level, int x, int y, Entity e) {
        EntityItem.spawn(
            level, new ItemStack(new ItemInstance(Item.CACTUS, 0), 1),
            Level.toCenter(x), Level.toCenter(y)
        );
        level.setTile(x, y, Tile.SAND.id);
        return true;
    }

    @Override
    public void bump(Level level, int x, int y, Entity e) {
        super.bump(level, x, y, e);

        if (e instanceof EntityMob) {
            EntityMob mob = (EntityMob) e;
            mob.hurt(1,  mob.getDirection().opposite());
        } else if (e instanceof EntityItem) {
            e.remove();
        }
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public int getCollisionWidth() {
        return 9;
    }

    @Override
    public int getCollisionHeight() {
        return 14;
    }

    @Override
    public int getCollisionOffsetX() {
        return 4;
    }

    @Override
    public int getCollisionOffsetY() {
        return 1;
    }

    @Override
    public boolean collides(Level level, int x, int y, Entity e) {
        return true;
    }

    @Override
    public void render(Level level, int x, int y) {
        super.render(level, x, y);

        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                Renderer.render(
                    10 + i, 2 + j,
                    Level.toPixel(x) + (i * 8), Level.toPixel(y) + (j * 8),
                    COLOR, Renderer.FLIP_NONE
                );
            }
        }
    }

    @Override
    public Tile getBaseTile() {
        return Tile.SAND;
    }
}
