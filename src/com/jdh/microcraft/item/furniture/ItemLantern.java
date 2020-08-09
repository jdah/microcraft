package com.jdh.microcraft.item.furniture;

import com.jdh.microcraft.entity.Entity;
import com.jdh.microcraft.entity.furniture.EntityLantern;
import com.jdh.microcraft.gfx.Color;
import com.jdh.microcraft.item.Item;
import com.jdh.microcraft.item.ItemInstance;
import com.jdh.microcraft.item.ItemStack;
import com.jdh.microcraft.item.crafting.Recipe;
import com.jdh.microcraft.item.crafting.RecipeIngredient;
import com.jdh.microcraft.level.Level;

import java.util.Collection;
import java.util.List;

public class ItemLantern extends ItemFurniture {
    public ItemLantern(int id) {
        super(id);
    }

    @Override
    public int getLightPower() {
        return EntityLantern.LIGHT_POWER;
    }

    @Override
    public Collection<Recipe> getRecipes() {
        return List.of(
            new Recipe(
                new ItemStack(new ItemInstance(this, 0), 1),
                Recipe.STATION_BENCH,
                new RecipeIngredient(Item.IRON_INGOT, 1),
                new RecipeIngredient(Item.GLASS, 4),
                new RecipeIngredient(Item.SLIME, 8)
            )
        );
    }

    @Override
    public Entity createEntity(Level level) {
        return new EntityLantern(level);
    }

    @Override
    public int getTileSpriteX() {
        return 10;
    }

    @Override
    public int getTileSpriteY() {
        return 6;
    }

    @Override
    public String getName() {
        return "LANTERN";
    }

    @Override
    public int getColor() {
        return Color.get(111, 222, 333, 552);
    }

    @Override
    public int getIconX() {
        return 5;
    }

    @Override
    public int getIconY() {
        return 7;
    }
}
