package com.jdh.microcraft.item.furniture;

import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.furniture.EntityChest;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.crafting.Recipe;
import com.jdh.microcraft.item.crafting.RecipeIngredient;
import com.jdh.microcraft.level.Level;

import java.util.Collection;
import java.util.List;

public class ItemChest extends ItemFurniture {
    public ItemChest(int id) {
        super(id);
    }

    @Override
    public Collection<Recipe> getRecipes() {
        return List.of(
            new Recipe(
                new ItemStack(new ItemInstance(this, 0), 1),
                Recipe.STATION_BENCH,
                new RecipeIngredient(Item.WOOD, 10)
            )
        );
    }

    @Override
    public Entity createEntity(Level level) {
        return new EntityChest(level);
    }

    @Override
    public int getTileSpriteX() {
        return 13;
    }

    @Override
    public int getTileSpriteY() {
        return 0;
    }

    @Override
    public String getName() {
        return "CHEST";
    }

    @Override
    public int getColor() {
        return Color.get(110, 221, 331, 441);
    }

    @Override
    public int getIconX() {
        return 2;
    }

    @Override
    public int getIconY() {
        return 7;
    }
}
