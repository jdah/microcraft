package com.jdh.microcraft.gui.crafting;

import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.entity.furniture.EntityFurniture;

public abstract class FurnitureCraftingMenu extends CraftingMenu {
    protected final EntityFurniture furniture;

    public FurnitureCraftingMenu(EntityPlayer player, EntityFurniture furniture) {
        super(player);
        this.furniture = furniture;
    }
}
