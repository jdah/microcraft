package com.jdh.microcraft.entity.furniture;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.gui.crafting.BenchCraftingMenu;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.level.Level;

public class EntityCraftingBench extends EntityFurniture {
    public EntityCraftingBench(Level level) {
        super(level, Item.CRAFTING_BENCH, 15, 12);
    }

    @Override
    public boolean interact(Entity e) {
        if (!(e instanceof EntityPlayer)) {
            return false;
        }

        Global.game.setMenu(new BenchCraftingMenu((EntityPlayer) e, this));
        return true;
    }
}
