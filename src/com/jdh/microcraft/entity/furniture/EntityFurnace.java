package com.jdh.microcraft.entity.furniture;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.gui.crafting.FurnaceMenu;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.level.Level;

public class EntityFurnace extends EntityFurniture {
    public EntityFurnace(Level level) {
        super(level, Item.FURNACE, 15, 14);
    }

    @Override
    public boolean interact(Entity e) {
        if (!(e instanceof EntityPlayer)) {
            return false;
        }

        Global.game.setMenu(new FurnaceMenu((EntityPlayer) e, this));
        return true;
    }
}
