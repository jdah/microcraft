package com.jdh.microcraft.gui.crafting;

import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.entity.furniture.EntityCraftingBench;
import com.jdh.microcraft.item.crafting.Recipe;

public class BenchCraftingMenu extends FurnitureCraftingMenu {
    public BenchCraftingMenu(EntityPlayer player, EntityCraftingBench bench) {
        super(player, bench);
    }

    @Override
    public int getStation() {
        return Recipe.STATION_ALL_CRAFTING;
    }

    @Override
    public String getName() {
        return "CRAFTING";
    }
}
