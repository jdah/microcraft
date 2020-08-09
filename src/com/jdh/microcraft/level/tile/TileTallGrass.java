package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.Global;
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

public class TileTallGrass extends TileGrass {
    public static int COLOR = Color.get(252, 252, 353, 454);

    public TileTallGrass(int id) {
        super(id);
    }

    @Override
    public int getUsableTools() {
        return ItemTool.TYPE_ALL;
    }

    @Override
    public int getIdealTools() {
        return ItemTool.TYPE_HOE;
    }

    @Override
    public boolean hit(Level level, int x, int y, Entity e) {
        ItemTool tool = ((ItemTool) ((EntityMob) e).equipped.instance.item);

        if (Global.random.nextInt( (tool.type & ItemTool.TYPE_HOE) != 0 ? 2 : 4) == 0) {
            EntityItem.spawn(
                level, new ItemStack(new ItemInstance(Item.SEED, 0), 1),
                Level.toCenter(x), Level.toCenter(y)
            );
        }
        level.setTile(x, y, Tile.GRASS.id);
        return true;
    }

    @Override
    public void render(Level level, int x, int y) {
        super.render(level, x, y);
        Global.random.setSeed((x * 43) ^ (y * 117));
        Renderer.render(
            4 + Global.random.nextInt(2),
            0 + Global.random.nextInt(2),
            Level.toPixel(x) + (Global.random.nextInt(1) * 8),
            Level.toPixel(y) + (Global.random.nextInt(1) * 8),
            COLOR, Global.random.nextBoolean() ? Renderer.FLIP_X : Renderer.FLIP_NONE
        );
    }

    @Override
    public Tile getBaseTile() {
        return Tile.GRASS;
    }
}
