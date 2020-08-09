package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityItem;
import com.jdh.microcraft.entity.mob.EntityMob;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.tool.ItemTool;
import com.jdh.microcraft.level.Level;

public class TileSapling extends TileGrass {
    public TileSapling(int id) {
        super(id);
    }

    @Override
    public int getUsableTools() {
        return ItemTool.TYPE_ALL;
    }

    @Override
    public int getIdealTools() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public void randomTick(Level level, int x, int y) {
        super.randomTick(level, x, y);

        if (Global.random.nextInt(30) != 0) {
            return;
        }

        level.setTile(x, y, Tile.TREE.id);
    }

    @Override
    public boolean hit(Level level, int x, int y, Entity e) {
        if (e instanceof EntityMob &&
            ((EntityMob) e).equipped.instance.item instanceof ItemTool &&
            (((ItemTool) ((EntityMob) e).equipped.instance.item).type & ItemTool.TYPE_AXE) != 0 &&
            Global.random.nextInt(2) == 0) {
            EntityItem.spawn(
                level, new ItemStack(new ItemInstance(Item.SAPLING, 0), 1),
                Level.toCenter(x), Level.toCenter(y)
            );
        }
        level.setTile(x, y, Tile.GRASS.id);
        return true;
    }

    @Override
    public void render(Level level, int x, int y) {
        super.render(level, x, y);

        Renderer.render(
            Item.SAPLING.getIconX(), Item.SAPLING.getIconY(),
            Level.toCenter(x) - 4, Level.toCenter(y) - 4,
            Item.SAPLING.getColor(), Renderer.FLIP_NONE
        );
    }

    @Override
    public Tile getBaseTile() {
        return Tile.GRASS;
    }
}
