package com.jdh.microcraft.item.resource;

import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.mob.EntityMob;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.level.tile.Tile;
import com.jdh.microcraft.level.tile.TileFlower;

public class ItemFlower extends Item {
    private final String name;
    private final TileFlower tile;

    public ItemFlower(int id, String name, TileFlower tile) {
        super(id);
        this.name = name;
        this.tile = tile;
    }

    @Override
    public boolean use(ItemInstance instance, Level level, int x, int y, Entity e) {
        if (level.getTile(x, y) != Tile.GRASS.id) {
            return false;
        }

        EntityMob mob = (EntityMob) e;
        if (!mob.takeApproxStamina(1)) {
            return false;
        }

        mob.removeItem(this, 1);
        level.setTile(x, y, this.tile.id);
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getColor() {
        return tile.getFlowerColor();
    }

    @Override
    public int getIconX() {
        return 2;
    }

    @Override
    public int getIconY() {
        return 0;
    }
}
