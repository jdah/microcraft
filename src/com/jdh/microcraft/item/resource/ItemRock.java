package com.jdh.microcraft.item.resource;

import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.mob.EntityMob;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.tile.Tile;
import com.jdh.microcraft.level.tile.TileRock;

public class ItemRock extends Item {
    public ItemRock(int id) {
        super(id);
    }

    @Override
    public boolean use(ItemInstance instance, Level level, int x, int y, Entity e) {
        int t = level.getTile(x, y);
        if (t != Tile.GRASS.id && t != Tile.DIRT.id &&
            t != Tile.STONE.id && t != Tile.SAND.id) {
            return false;
        }

        if (!level.trySetTile(x, y, Tile.COBBLESTONE.id)) {
            return false;
        }

        EntityMob mob = (EntityMob) e;
        if (!mob.takeApproxStamina(4)) {
            return false;
        }

        mob.removeItem(this, 1);
        return true;
    }

    @Override
    public String getName() {
        return "ROCK";
    }

    @Override
    public int getColor() {
        return TileRock.COLOR;
    }

    @Override
    public int getIconX() {
        return 6;
    }

    @Override
    public int getIconY() {
        return 0;
    }
}
