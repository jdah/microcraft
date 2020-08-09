package com.jdh.microcraft.level.tile;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityItem;
import com.jdh.microcraft.entity.mob.EntityMob;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.tool.ItemTool;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.util.Direction;

public class TileGrass extends TileGround {
    public static final int COLOR = Color.get(322, 020, 141, 252),
        TRANSITION_COLOR = Color.get(322, 020, 141, 252);

    public TileGrass(int id) {
        super(id);
    }

    @Override
    public int getUsableTools() {
        return ItemTool.TYPE_HOE;
    }

    @Override
    public int getIdealTools() {
        return ItemTool.TYPE_PICKAXE | ItemTool.TYPE_SHOVEL;
    }

    @Override
    public boolean hit(Level level, int x, int y, Entity e) {
        if (this.id != Tile.GRASS.id) {
            return super.hit(level, x, y, e);
        }

        assert(e instanceof EntityMob && ((EntityMob) e).equipped.instance.item instanceof ItemTool);
        ItemTool equipped = ((ItemTool) ((EntityMob) e).equipped.instance.item);

        if ((equipped.type & (ItemTool.TYPE_SHOVEL | ItemTool.TYPE_PICKAXE)) != 0) {
            level.setTile(x, y, Tile.DIRT.id);
        } else if ((equipped.type & ItemTool.TYPE_HOE) != 0) {
            if (Global.random.nextInt(10) < (int) equipped.material.efficiency) {
                EntityItem.spawn(level, new ItemStack(Item.SEED), Level.toCenter(x), Level.toCenter(y));
            }

            level.setTile(x, y, Tile.DIRT.id);
        } else {
            return false;
        }

        return true;
    }

    @Override
    public void randomTick(Level level, int x, int y) {
        super.randomTick(level, x, y);

        if (Global.random.nextInt(15) != 0) {
            return;
        }

        // check for non-grass tiles surrounding
        for (Direction d : Direction.ALL) {
            if (Global.random.nextInt(5) != 0) {
                continue;
            }

            int tx = x + d.x, ty = y + d.y;
            if (level.getTile(tx, ty) == Tile.DIRT.id) {
                level.setTile(tx, ty, Tile.GRASS.id);
            }
        }
    }

    @Override
    public int getColor() {
        return COLOR;
    }

    @Override
    public int getTransitionColor() {
        return TRANSITION_COLOR;
    }
}
