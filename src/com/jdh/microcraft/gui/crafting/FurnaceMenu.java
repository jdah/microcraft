package com.jdh.microcraft.gui.crafting;

import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.entity.furniture.EntityFurnace;
import com.jdh.microcraft.entity.particle.EntitySmokeParticle;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.crafting.Recipe;

public class FurnaceMenu extends FurnitureCraftingMenu {
    public FurnaceMenu(EntityPlayer player, EntityFurnace furnace) {
        super(player, furnace);
    }

    @Override
    protected void onCraft(ItemStack stack) {
        super.onCraft(stack);
        EntitySmokeParticle.spawn(
            this.furniture.level,
            this.furniture.getCenterX(), this.furniture.getCenterY(),
            Item.COAL.getColor(),
            3, 6
        );
    }

    @Override
    public int getStation() {
        return Recipe.STATION_FURNACE;
    }

    @Override
    public String getName() {
        return "FURNACE";
    }
}
