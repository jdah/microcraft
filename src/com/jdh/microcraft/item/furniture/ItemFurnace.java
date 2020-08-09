package com.jdh.microcraft.item.furniture;

import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.furniture.EntityFurnace;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.crafting.Recipe;
import com.jdh.microcraft.item.crafting.RecipeIngredient;
import com.jdh.microcraft.level.Level;

import java.util.Collection;
import java.util.List;

public class ItemFurnace extends ItemFurniture {
    public ItemFurnace(int id) {
        super(id);
    }

    @Override
    public Collection<Recipe> getRecipes() {
        return List.of(
            new Recipe(
                new ItemStack(new ItemInstance(this, 0), 1),
                Recipe.STATION_BENCH,
                new RecipeIngredient(Item.ROCK, 20),
                new RecipeIngredient(Item.COAL, 8)
            )
        );
    }

    @Override
    public Entity createEntity(Level level) {
        return new EntityFurnace(level);
    }

    @Override
    public int getTileSpriteX() {
        return 13;
    }

    @Override
    public int getTileSpriteY() {
        return 2;
    }

    @Override
    public String getName() {
        return "FURNACE";
    }

    @Override
    public int getColor() {
        return Color.get(111, 333, 444, 530);
    }

    @Override
    public int getIconX() {
        return 4;
    }

    @Override
    public int getIconY() {
        return 7;
    }
}
