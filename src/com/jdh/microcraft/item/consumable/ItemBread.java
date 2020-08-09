package com.jdh.microcraft.item.consumable;

import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.crafting.Recipe;
import com.jdh.microcraft.item.crafting.RecipeIngredient;

import java.util.Collection;
import java.util.List;

public class ItemBread extends ItemFood {
    public ItemBread(int id) {
        super(id);
    }

    @Override
    public int getFoodValue() {
        return 3;
    }

    @Override
    public Collection<Recipe> getRecipes() {
        return List.of(
            new Recipe(
                new ItemStack(new ItemInstance(this, 0), 1),
                Recipe.STATION_OVEN,
                new RecipeIngredient(Item.WHEAT, 3),
                new RecipeIngredient(Item.WOOD, 1)
            )
        );
    }

    @Override
    public String getName() {
        return "BREAD";
    }

    @Override
    public int getColor() {
        return Color.get(110, 221, 331, 441);
    }

    @Override
    public int getIconX() {
        return 4;
    }

    @Override
    public int getIconY() {
        return 5;
    }
}
