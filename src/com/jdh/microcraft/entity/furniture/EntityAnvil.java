package com.jdh.microcraft.entity.furniture;

import com.jdh.microcraft.Global;
import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.gui.crafting.AnvilCraftingMenu;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.level.Level;

public class EntityAnvil extends EntityFurniture {
    public EntityAnvil(Level level) {
        super(level, Item.ANVIL, 15, 10);
    }

    @Override
    public boolean interact(Entity e) {
        if (!(e instanceof EntityPlayer)) {
            return false;
        }

        Global.game.setMenu(new AnvilCraftingMenu((EntityPlayer) e, this));
        return true;
    }
}
