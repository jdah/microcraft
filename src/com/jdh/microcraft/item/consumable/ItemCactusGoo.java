package com.jdh.microcraft.item.consumable;

import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.crafting.Recipe;
import com.jdh.microcraft.item.crafting.RecipeIngredient;
import com.jdh.microcraft.level.tile.Tile;

import java.util.Collection;
import java.util.List;

public class ItemCactusGoo extends ItemFood {
    public ItemCactusGoo(int id) {
        super(id);
    }

    @Override
    public Collection<Recipe> getRecipes() {
        return List.of(
            new Recipe(
                new ItemStack(this),
                Recipe.STATION_ALL_CRAFTING,
                new RecipeIngredient(Item.CACTUS)
            )
        );
    }

    @Override
    public int getFoodValue() {
        return 1;
    }

    @Override
    public String getName() {
        return "GOO";
    }

    @Override
    public int getColor() {
        return Tile.CACTUS.getColor();
    }

    @Override
    public int getIconX() {
        return 6;
    }

    @Override
    public int getIconY() {
        return 7;
    }
}
