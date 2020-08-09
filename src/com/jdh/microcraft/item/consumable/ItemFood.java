package com.jdh.microcraft.item.consumable;

import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.mob.EntityMob;
import com.jdh.microcraft.entity.particle.EntitySmashParticle;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.level.Level;
import com.jdh.microcraft.sound.Sound;

public abstract class ItemFood extends ItemConsumable {
    public ItemFood(int id) {
        super(id);
    }

    @Override
    public boolean use(ItemInstance instance, Level level, int x, int y, Entity e) {
        if (!(e instanceof EntityMob)) {
            return false;
        }

        EntityMob mob = ((EntityMob) e);
        mob.heal(this.getFoodValue());
        mob.removeItem(this, 1);

        Sound.EQUIP.play();
        EntitySmashParticle.spawn(level, e.getCenterX(), e.getCenterY(), this.getColor(), 3, 8);

        return true;
    }

    public abstract int getFoodValue();
}
