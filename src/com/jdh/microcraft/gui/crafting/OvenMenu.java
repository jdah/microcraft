package com.jdh.microcraft.gui.crafting;

import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.entity.furniture.EntityOven;
import com.jdh.microcraft.entity.particle.EntitySmokeParticle;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.crafting.Recipe;

public class OvenMenu extends FurnitureCraftingMenu {
    public OvenMenu(EntityPlayer player, EntityOven oven) {
        super(player, oven);
    }

    @Override
    protected void onCraft(ItemStack stack) {
        super.onCraft(stack);
        EntitySmokeParticle.spawn(
            this.furniture.level,
            this.furniture.getCenterX(), this.furniture.getCenterY(),
            Item.COAL.getColor(),
            2, 5
        );
    }

    @Override
    public int getStation() {
        return Recipe.STATION_OVEN;
    }

    @Override
    public String getName() {
        return "OVEN";
    }
}
