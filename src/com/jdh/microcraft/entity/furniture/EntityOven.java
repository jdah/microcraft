package com.jdh.microcraft.entity.furniture;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.gui.crafting.OvenMenu;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.level.Level;

public class EntityOven extends EntityFurniture {
    public EntityOven(Level level) {
        super(level, Item.OVEN, 15, 13);
    }

    @Override
    public boolean interact(Entity e) {
        if (!(e instanceof EntityPlayer)) {
            return false;
        }

        Global.game.setMenu(new OvenMenu((EntityPlayer) e, this));
        return true;
    }
}
