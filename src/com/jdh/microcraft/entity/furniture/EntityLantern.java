package com.jdh.microcraft.entity.furniture;

import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.mob.EntityMob;
import com.jdh.microcraft.gfx.Light;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.level.Level;

public class EntityLantern extends EntityFurniture {
    public static final int LIGHT_POWER = 6;

    public EntityLantern(Level level) {
        super(level, Item.LANTERN, 13, 13);
    }

    @Override
    public int getRenderOffsetX() {
        return -2;
    }

    @Override
    public int getRenderOffsetY() {
        return -3;
    }

    @Override
    public Light getLight() {
        return new Light(
            this.getCenterX(),
            this.getCenterY(),
            LIGHT_POWER
        );
    }

    @Override
    public boolean interact(Entity e) {
        ItemStack s = new ItemStack(new ItemInstance(Item.LANTERN, 0), 1);

        if (e instanceof EntityMob && ((EntityMob) e).inventory.add(s)) {
            EntityMob mob = (EntityMob) e;
            mob.equipped = s;
            this.remove();
            return true;
        }

        return false;
    }
}
