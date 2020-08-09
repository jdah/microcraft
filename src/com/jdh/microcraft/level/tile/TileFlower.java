package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityItem;
import com.jdh.microcraft.gfx.Renderer;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.tool.ItemTool;
import com.jdh.microcraft.level.Level;

public abstract class TileFlower extends TileGrass {

    public TileFlower(int id) {
        super(id);
    }

    @Override
    public int getUsableTools() {
        return ItemTool.TYPE_ALL;
    }

    @Override
    public int getIdealTools() {
        return ItemTool.TYPE_HOE | ItemTool.TYPE_SWORD;
    }

    @Override
    public boolean hit(Level level, int x, int y, Entity e) {
        EntityItem.spawn(
            level, new ItemStack(new ItemInstance(this.getDrop(), 0), 1),
            Level.toCenter(x), Level.toCenter(y)
        );
        level.setTile(x, y, Tile.GRASS.id);
        return true;
    }

    @Override
    public void render(Level level, int x, int y) {
        super.render(level, x, y);
        Global.random.setSeed((x * 31) ^ (y * 43));
        Renderer.render(
            2 + Global.random.nextInt(2),
            0 + Global.random.nextInt(2),
            Level.toPixel(x) + (Global.random.nextInt(2) * 8),
            Level.toPixel(y) + (Global.random.nextInt(2) * 8),
            this.getFlowerColor(), Renderer.FLIP_NONE
        );
    }

    @Override
    public Tile getBaseTile() {
        return Tile.GRASS;
    }

    public abstract Item getDrop();

    public abstract int getFlowerColor();
}
