package com.jdh.microcraft.gui.crafting;

import com.jdh.microcraft.entity.EntityPlayer;
import com.jdh.microcraft.entity.furniture.EntityAnvil;
import com.jdh.microcraft.item.crafting.Recipe;

public class AnvilCraftingMenu extends FurnitureCraftingMenu {
    public AnvilCraftingMenu(EntityPlayer player, EntityAnvil anvil) {
        super(player, anvil);
    }

    @Override
    public int getStation() {
        return Recipe.STATION_ANVIL;
    }

    @Override
    public String getName() {
        return "ANVIL";
    }
}
