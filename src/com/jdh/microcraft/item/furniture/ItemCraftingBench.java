package com.jdh.microcraft.item.furniture;

import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.furniture.EntityCraftingBench;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.crafting.Recipe;
import com.jdh.microcraft.item.crafting.RecipeIngredient;
import com.jdh.microcraft.level.Level;

import java.util.Collection;
import java.util.List;

public class ItemCraftingBench extends ItemFurniture {
    public ItemCraftingBench(int id) {
        super(id);
    }

    @Override
    public Collection<Recipe> getRecipes() {
        return List.of(
            new Recipe(
                new ItemStack(new ItemInstance(this, 0), 1),
                Recipe.STATION_ALL_CRAFTING,
                new RecipeIngredient(Item.WOOD, 6)
            )
        );
    }

    @Override
    public Entity createEntity(Level level) {
        return new EntityCraftingBench(level);
    }

    @Override
    public int getTileSpriteX() {
        return 9;
    }

    @Override
    public int getTileSpriteY() {
        return 0;
    }

    @Override
    public String getName() {
        return "CRFT BENCH";
    }

    @Override
    public int getColor() {
        return Color.get(110, 220, 330, 330);
    }

    @Override
    public int getIconX() {
        return 1;
    }

    @Override
    public int getIconY() {
        return 7;
    }
}
